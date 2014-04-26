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
#include "tab.h"

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
  struct personnes p;
  struct personne *personne;
  int ready, n, acceptIAM;
  unsigned int csize;
  pthread_t tclient;
  char msg[35];
  char iam_msg[35];
  char * ip;

  p = create();
  ip = getip();
  
  csize = sizeof(c);

  sock_emet = socket(AF_INET, SOCK_DGRAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = inet_addr("225.1.2.4");
  addr.sin_port = htons(28888);

  sock_recep = createRecepteur(28888, "225.1.2.4"); 
  
  sock_serv = createServerSocket(atoi(args[2]));

  strcpy(iam_msg, "HLO ");
  iam_msg[4] = ' ';
  string_bourrage(args[1], 8, iam_msg +4);
  iam_msg[12] = ' ';
  strcpy(iam_msg + 13, ip);
  iam_msg[28] = ' ';
  int_bourrage(atoi(args[2]), 5, iam_msg + 29);
  iam_msg[34] = '\0';

  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));

  iam_msg[0] = 'I';
  iam_msg[1] = 'A';
  iam_msg[2] = 'M';

  printf("%s\n", iam_msg);

  polls[0].fd = 0;
  polls[0].events = POLLIN;

  polls[1].fd = sock_serv;
  polls[1].events = POLLIN;

  polls[2].fd = sock_recep;
  polls[2].events = POLLIN;
  
  acceptIAM = 1;
  msg[34] = '\0';

  while(1){
    ready = poll(polls, 3, -1);

    while(ready --){
      if(polls[0].revents == POLLIN){
	scanf("%d", &n);

	if(n == -1){
	  iam_msg[0] = 'B';
	  iam_msg[1] = 'Y';
	  iam_msg[2] = 'E';
	  
	  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	  exit(EXIT_SUCCESS);
	}

	personne = get(&p, n);

	if(personne == NULL){
	  printf("\033c");
	  print(&p);
	  
	  printf("\nIl n'y a pas de personne %d\n", n);
	  break;
	}
	sock_client = createSocket(personne->port, personne->adr);
	if(sock_client == -1){
	  printf("\033c");
	  print(&p);
	  
	  printf("Impossible de se connecter a %d\n", n);
	  break;
	}
	printf("Connection etablie avec %s \n", personne->nom);
	client(sock_client);
	printf("retour de chat\n");
	//retour de chat
	sendto(sock_emet, "RFH\0", 4, 0, (struct sockaddr *)&addr, sizeof(addr));
	
      }
      if(polls[1].revents == POLLIN){
	printf("Accept\n");
	sock_a = accept(sock_serv, (struct sockaddr *)(&c), &csize);
	sock_client = createSocket(atoi(args[2]), "127.0.0.1");
	pthread_create(&tclient, NULL, thread_client, (void *)(&sock_client));

	sock_b = accept(sock_serv, (struct sockaddr *)(&c), &csize);
	printf("Accepter\n");
	serveur(sock_a, sock_b);
	printf("retour de chat\n");


      }
      if(polls[2].revents == POLLIN){
	recv(sock_recep,  msg, 34, 0);

	if(strcmp(msg+3, iam_msg+3) == 0)
	  break;

	if(IS_HLO(msg)){
	  acceptIAM = 0;
	  split_and_add(&p,msg);
	  printf("\033c");
	  print(&p);
	  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else if(IS_IAM(msg) && acceptIAM){
	  split_and_add(&p,msg);
	  printf("\033c");
	  print(&p);
	}
	else if(IS_RFH(msg)){
	  acceptIAM = 1;
	  del_all(&p);
	  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else if(IS_BYE(msg)){
	  split_and_del(&p, msg);
	  printf("\033c");
	  print(&p);

	}
      }
    }
  } 
  return EXIT_SUCCESS;
}
