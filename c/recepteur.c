#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#include <poll.h>
#include <pthread.h>

#include "chat.h"
#include "tools.h"

#define IS_HLO(s) firstThreeLetters(s, "HLO")
#define IS_IAM(s) firstThreeLetters(s, "IAM")
#define IS_BYE(s) firstThreeLetters(s, "BYE")
#define IS_RFH(s) firstThreeLetters(s, "RFH")
#define IS_BAN(s) firstThreeLetters(s, "BAN")

int createRecepteur(int port, char* adresse){
  int sock;
  int ok = 1;
  struct sockaddr_in addr;
  struct ip_mreq mreq;

  sock = socket(AF_INET, SOCK_DGRAM, 0);
  
  setsockopt(sock, SOL_SOCKET, SO_REUSEPORT, &ok, sizeof(ok));
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = INADDR_ANY;
  addr.sin_port = htons(port);
  
  bind(sock, (struct sockaddr *)&addr, sizeof(addr));
  
  mreq.imr_multiaddr.s_addr = inet_addr(adresse);
  mreq.imr_interface.s_addr = htonl(INADDR_ANY);
  
  setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq));

  return sock;
}

int main(int argc, char ** args){
  int sock_recep, sock_emet, sock_serv, sock_a, sock_b, sock_client;
  struct sockaddr_in addr, c;
  struct pollfd polls[3];
  int ready, n;
  unsigned int csize;
  pthread_t tclient;
  char msg[256];

  csize = sizeof(c);

  sock_emet = socket(AF_INET, SOCK_DGRAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = inet_addr("225.1.2.4");
  addr.sin_port = htons(28888);

  sock_recep = createRecepteur(28888, "225.1.2.4"); 
  
  sock_serv = createServerSocket(atoi(args[1]));

  sendto(sock_emet, "HLO user", 15, 0, (struct sockaddr *)&addr, sizeof(addr));

  polls[0].fd = 0;
  polls[0].events = POLLIN;

  polls[1].fd = sock_serv;
  polls[1].events = POLLIN;

  polls[2].fd = sock_recep;
  polls[2].events = POLLIN;

  while(1){
    ready = poll(polls, 3, -1);
    while(ready --){
      if(polls[0].revents == POLLIN){
	scanf("%d", &n);
	printf("Connection a %d\n", n);
	sock_client = createSocket(n, "127.0.0.1"); 
	client(sock_client);
	
	//retour de chat
	sendto(sock_emet, "RFH", 3, 0, (struct sockaddr *)&addr, sizeof(addr));
	
      }
      if(polls[1].revents == POLLIN){
	printf("Accept\n");
	sock_a = accept(sock_serv, (struct sockaddr *)(&c), &csize);
	sock_client = createSocket(atoi(args[1]), "127.0.0.1");
	pthread_create(&tclient, NULL, thread_client, (void *)(&sock_client));

	sock_b = accept(sock_serv, (struct sockaddr *)(&c), &csize);
	printf("Accepter\n");
	serveur(sock_a, sock_b);

	//retour de chat
	sendto(sock_emet, "RFH", 3, 0, (struct sockaddr *)&addr, sizeof(addr));
      }
      if(polls[2].revents == POLLIN){
	recv(sock_recep,  msg, 256, 0);
	printf("recv: %s\n", msg);
	if(IS_HLO(msg) || IS_RFH(msg)){
	  sendto(sock_emet, "IAM ...", 15, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else if(IS_IAM(msg)){
	  //TODO
	}
      }
    }
  } 
  return EXIT_SUCCESS;
}
