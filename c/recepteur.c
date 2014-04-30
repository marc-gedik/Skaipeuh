#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <arpa/inet.h>

int createRecepteur(int port, char* adresse){
  int sock;
  int ok = 1;
  struct sockaddr_in addr;
  struct ip_mreq mreq;

  sock = socket(AF_INET, SOCK_DGRAM, 0);
  if(sock == -1){
    perror("Recepteur socket");
    exit(EXIT_FAILURE);
  }

  if(setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &ok, sizeof(ok)) == -1){
    perror("Recepteur setsockopt 1");
    exit(EXIT_FAILURE);
  }
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = INADDR_ANY;
  addr.sin_port = htons(port);
  
  if(bind(sock, (struct sockaddr *)&addr, sizeof(addr)) == -1){
    perror("Recepteur bind");
    exit(EXIT_FAILURE);
  }
  
  mreq.imr_multiaddr.s_addr = inet_addr(adresse);
  mreq.imr_interface.s_addr = htonl(INADDR_ANY);
  
  if(setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) == -1){
    perror("Recepteur setsockopt 2");
    exit(EXIT_FAILURE);
  }

  return sock;
}

