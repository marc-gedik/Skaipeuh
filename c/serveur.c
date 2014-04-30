#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <netdb.h>

#include <poll.h>

#include "tools.h"

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

