#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>

#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netdb.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <libgen.h>

#include <poll.h>

#include "tools.h"

#define IS_MSG(s) firstThreeLetters(s, "MSG")
#define IS_CLO(s) firstThreeLetters(s, "CLO")
#define IS_FIL(s) firstThreeLetters(s, "FIL")
#define IS_ACK(s) firstThreeLetters(s, "ACK")
#define IS_NAK(s) firstThreeLetters(s, "NAK")

#define MAX_LINE 500


int createSocket(int port, char* adresse){
  struct sockaddr_in addr;
  int sock;

  sock = socket(PF_INET, SOCK_STREAM, 0);
  if(sock == -1){
    perror("Erreur create client");
    return -1;
  }
  bzero(&addr, sizeof(addr));

  printf("%d, %s\n", port, adresse);
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = inet_addr(adresse);

  if(connect(sock, (struct sockaddr *)&addr, sizeof(addr)) == -1){
    perror("Client connect");
    close(sock);
    return -1;
  }

  return sock;
}

int createServerSocket(int port){
  struct sockaddr_in addr;
  int sock;
  sock = socket(PF_INET, SOCK_STREAM, 0);
  if(sock == -1){
    perror("Serveur socket");
    exit(EXIT_FAILURE);
  }
  bzero(&addr, sizeof(addr));

  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);

  if(bind(sock, (struct sockaddr *)&addr, sizeof(addr)) == -1){
    perror("Serveur bind");
    exit(EXIT_FAILURE);
  }

  if(listen(sock, 0) == -1){
    perror("Seveur listen");
    exit(EXIT_FAILURE);
  }

  return sock;
}

void client(int sock){
  struct pollfd polls[2]; 
  int len, n, port, desc;
  struct stat statbuf; 
  int filesize;
  char buff[MAX_LINE + 4 + 4];

  polls[0].fd = 0;
  polls[0].events = POLLIN;
  
  polls[1].fd = sock;
  polls[1].events = POLLIN;

  while(1){
    poll(polls, 2, -1);
    if(polls[0].revents == POLLIN){
      n = read(STDIN_FILENO, buff + 8, MAX_LINE);
      buff[7+n] = '\0';
      len = strlen(buff);
      if(strcmp(buff+8, "/q") ==0 || strcmp(buff+8, "/quit") == 0){
	strcpy(buff, "CLO");
	write(sock, buff, 3);
	printf("Discussion termine\n");
	break;
      }
      else if(strncmp("/f ", buff+8, 3) == 0 || strncmp("/file ", buff+8, 6) == 0){
	if(buff[10] == ' ')
	  n = 11;
	else 
	  n = 15;
	desc = open(buff+n, O_RDONLY);
	if(desc == -1){
	  perror("Impossible d'ouvrir le fichier");
	}
	else{
	  stat(buff+n, &statbuf);
	  filesize = (long)statbuf.st_size;
	  buff[0] = 'F', buff[1] = 'I', buff[2] = 'L';
	  printf("Sur quel port envoyer ?\n");
	  scanf("%d", &port);
	  sprintf(buff + 3, " %d %s ", filesize, buff+n);
	  len = strlen(buff);
	  int_bourrage(port, 5, buff+len);
	  buff[len+5] = '\0';
	  write(sock, buff, len + 5);

	  //attente de la reponse
	  read(sock, buff, 3);
	  buff[4] = '\0';
	  if(IS_ACK(buff)){
	    printf("L'echange a ete accepter\n");
	    //lance la socket sur le port 'port'
	  }
	  else
	    printf("L'echange a ete refusé\n");
	}
      }
      else{
	buff[0] = 'M', buff[1] = 'S', buff[2] = 'G', buff[3] = ' ';
	int_bourrage(strlen(buff+8), 3, buff+4);
	buff[7] = ' ';
	printf("envoie:%s\n", buff);
	write(sock, buff, strlen(buff) + 8);
      }
    }
    if(polls[1].revents == POLLIN){
      read(sock, buff, 4);
      //printf("Recu:%s\n", buff);
      if(IS_MSG(buff)){
	read(sock, buff, 4);
	buff[4] = '\0';
	len = atoi(buff);
	read(sock, buff, len);
	buff[len] = '\0';
	printf("%s\n", buff);
      }
      else if(IS_CLO(buff)){
	printf("Discussion termine\n");
	break;
      }
      else if(IS_FIL(buff)){
	printf("Acceptez le fichier ?(y/n)\n");
	n = read(STDIN_FILENO, buff, MAX_LINE);
	
	if(buff[0] == 'y'){
	  write(sock, "ACK", 3);
	  printf("lancement de l'echange ...\n");
	  //lancer le serveur en thread
	}
	else
	  write(sock, "NAK", 3);
      }
    }
  }
  close(sock);
}

void * thread_client(void * s){
  client(*((int *)s));
  return NULL;
}

void serveur(int sock_a, int sock_b){
  struct pollfd polls[2];
  char buff[MAX_LINE + 4 + 4];
  int lu;

  polls[0].fd = sock_a;
  polls[0].events = POLLIN;

  polls[1].fd = sock_b;
  polls[1].events = POLLIN;

  while(1){
    poll(polls, 2, -1);
    if(polls[0].revents == POLLIN){
      lu = read(sock_a, buff, MAX_LINE + 8);
      write(sock_b, buff, lu);
    }
    if(polls[1].revents == POLLIN){
      lu = read(sock_b, buff, MAX_LINE + 8);
      write(sock_a, buff, lu);
    }    
    if(IS_CLO(buff)){
      break;
    }
  }
}

