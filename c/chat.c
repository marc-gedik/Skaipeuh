#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>

#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netdb.h>

#include <poll.h>

#include "tools.h"

#define IS_MSG(s) firstThreeLetters(s, "MSG")

#define MAX_LINE 500


int createSocket(int port, char* adresse){
  struct sockaddr_in addr;
  int sock;

  sock = socket(PF_INET, SOCK_STREAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = inet_addr(adresse);

  connect(sock, (struct sockaddr *)&addr, sizeof(addr));
  perror("client");
  return sock;
}

int createServerSocket(int port){
  struct sockaddr_in addr;
  int sock;
  
  sock = socket(PF_INET, SOCK_STREAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  
  bind(sock, (struct sockaddr *)&addr, sizeof(addr));
  
  listen(sock, 0);
  perror("serveur");
  return sock;
}

void client(int sock){
  struct pollfd polls[2]; 
  int len;
  char buff[MAX_LINE + 4 + 4];

  polls[0].fd = 0;
  polls[0].events = POLLIN;
  
  polls[1].fd = sock;
  polls[1].events = POLLIN;

  while(1){
    poll(polls, 2, -1);
    if(polls[0].revents == POLLIN){
      fgets(buff + 8, MAX_LINE, stdin);
      buff[0] = 'M', buff[1] = 'S', buff[2] = 'G', buff[3] = ' ';
      int_bourrage(strlen(buff+8), 3, buff+4);
      buff[7] = ' ';
      write(sock, buff, MAX_LINE + 8);
    }
    if(polls[1].revents == POLLIN){
      read(sock, buff, 4);
      if(IS_MSG(buff)){
	read(sock, buff, 4);
	buff[4] = '\0';
	len = atoi(buff);
	read(sock, buff, len);
	buff[len] = '\0';
	printf("%s", buff);
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

  polls[0].fd = sock_a;
  polls[0].events = POLLIN;

  polls[1].fd = sock_b;
  polls[1].events = POLLIN;

  while(1){
    poll(polls, 2, -1);
    if(polls[0].revents == POLLIN){
      read(sock_a, buff, MAX_LINE + 8);
      write(sock_b, buff, MAX_LINE + 8);
    }
    if(polls[1].revents == POLLIN){
      read(sock_b, buff, MAX_LINE + 8);
      write(sock_a, buff, MAX_LINE + 8);
    }    
  }
}
