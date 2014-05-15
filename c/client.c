#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>

#include <arpa/inet.h>

#include <sys/stat.h>
#include <fcntl.h>

#include <poll.h>
#include <pthread.h>
#include <libgen.h>

#include "tools.h"
#include "serveur.h"
#include "client.h"

int createSocket(int port, char* adresse){
  struct sockaddr_in addr;
  int sock;

  sock = socket(PF_INET, SOCK_STREAM, 0);
  if(sock == -1){
    perror("Erreur create client");
    return -1;
  }
  bzero(&addr, sizeof(addr));

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

void * thread_client(void * s){
  struct client_args * args = ( struct client_args *)s;
  client(args->socket, args->addr);
  return NULL;
}

void * send_file(void * s){
  struct send_file * args = (struct send_file * )s;
  int lu;
  char buff[BUFFSIZE + 1];

  while((lu = read(args->file, buff, BUFFSIZE)) > 0){
    write(args->socket, buff, lu);
  }

  printf("Echange fini\n");
  close(args->socket);
  close(args->file);
  return NULL;
}

void client(int sock, char * adresse){
  struct pollfd polls[2]; 
  int i;
  int len, n, port;
  struct stat statbuf; 
  int filesize, fileport;
  struct serveur_args args;
  struct send_file sendargs;
  pthread_t tfile;
  char buff[MAX_LINE + 4 + 4];
  char filename[41];

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
      if(strcmp(buff+8, "\\h") == 0 || strcmp(buff+8, "\\help") == 0 || strcmp(buff+8, "\\?") == 0){
	  printf("Envoie de fichier\t: \\f <nom_du_fichier> <numero_de_port>\n");
	  printf("Quitter la conversation\t: \\q ou \\quit\n");
      }
      else if(strcmp(buff+8, "\\q") ==0 || strcmp(buff+8, "\\quit") == 0){
	strcpy(buff, "CLO");
	write(sock, buff, 3);
	printf("Discussion termine\n");
	break;
      }
      else if(strncmp("\\f ", buff+8, 3) == 0 || strncmp("\\file ", buff+8, 6) == 0){
	if(buff[10] == ' ')
	  n = 11;
	else 
	  n = 15;
	sendargs.file = open(buff+n, O_RDONLY);
	if(sendargs.file == -1){
	  perror("Impossible d'ouvrir le fichier");
	}
	else{
	  stat(buff+n, &statbuf);
	  filesize = (long)statbuf.st_size;
	  buff[0] = 'F', buff[1] = 'I', buff[2] = 'L';
	  printf("Sur quel port envoyer ?\n");
	  scanf("%d", &port);
	  sprintf(buff + 3, " %d ", filesize);
	  len = strlen(buff);
	  
	  string_bourrage(basename(buff+n), 40, buff + len) ;
	  buff[len+40] = ' ';
	  int_bourrage(port, 5, buff+len+41);
	  buff[len+41+5] = '\0';

	  write(sock, buff, len + 41+ 5);

	  //attente de la reponse
	  read(sock, buff, 3);
	  buff[4] = '\0';
	  if(IS_ACK(buff)){
	    printf("L'echange a ete accepter\n");
	    sendargs.socket = createSocket(port, adresse);
	    pthread_create(&tfile, NULL, send_file, (void *)(&sendargs));
	  }
	  else
	    printf("L'echange a ete refusé\n");
	}
      }
      else{
	buff[0] = 'M', buff[1] = 'S', buff[2] = 'G', buff[3] = ' ';
	int_bourrage(strlen(buff+8), 3, buff+4);
	buff[7] = ' ';
	write(sock, buff, strlen(buff));
      }
    }
    if(polls[1].revents == POLLIN){
      read(sock, buff, 4);
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
	i = 0;
	do{
	  read(sock, buff + i, 1);
	}while(buff[i++] != ' ');
	filesize = atoi(buff);
	read(sock, filename , 40);
	filename[40] = '\0';
	sprintf(filename, "%s", basename(filename));
	for(i = 40 -1; i > 0 && filename[i] == ' '; i --)
	  filename[i] = '\0';

	read(sock, buff, 1);
	read(sock, buff, 5);
	buff[5] = '\0';
	fileport = atoi(buff);

	printf("Acceptez le fichier %s (%d octets) ?(y/n)\n", filename, filesize);
	
	read(STDIN_FILENO, buff, MAX_LINE);

	if(buff[0] == 'y'){
	  args.port = fileport;
	  args.filesize = filesize;
	  args.filename = filename;
	  pthread_create(&tfile, NULL, thread_serveur, (void *)(&args));
	  write(sock, "ACK", 3);
	}
	else
	  write(sock, "NAK", 3);
      }
    }
  }
  close(sock);
}

