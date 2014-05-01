#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <arpa/inet.h>

#include <poll.h>
#include <pthread.h>

#include <unistd.h>

#include "client.h"
#include "serveur.h"
#include "recepteur.h"
#include "tools.h"
#include "tab.h"


int main(int argc, char ** args){
  int sock_recep, sock_emet, sock_serv, sock_a, sock_b, sock_client;
  struct sockaddr_in addr, c, from;
  char ban[16];
  struct pollfd polls[3];
  struct personnes p;
  struct personne *personne;
  int ready, n, acceptIAM, lu;
  unsigned int csize;
  pthread_t tclient;
  char msg[35];
  char iam_msg[35];
  char buff[MAX_LINE];
  char * ip;
  socklen_t lg;
  char me[9];
  struct client_args thread_args;

  p = create();
  ip = getip();
  
  ban[0] = 0;

  csize = sizeof(c);

  sock_emet = socket(AF_INET, SOCK_DGRAM, 0);
  if(sock_emet == -1){
    perror("Emetteur socket");
    exit(EXIT_FAILURE);
  }
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = inet_addr(ADRESSE);
  addr.sin_port = htons(PORT);

  sock_recep = createRecepteur(PORT, ADRESSE); 
  
  sock_serv = createServerSocket(atoi(args[2]));
  if(sock_serv == -1)
    exit(EXIT_FAILURE);

  string_bourrage(args[1], 8, me);
  me[8] = '\0';

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
	lu = read(STDIN_FILENO, buff, MAX_LINE);
	buff[lu - 1] = '\0';
	if(strcmp(buff, "\\q") == 0 || strcmp(buff, "\\quit") == 0){
	  iam_msg[0] = 'B';
	  iam_msg[1] = 'Y';
	  iam_msg[2] = 'E';
	  
	  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	  exit(EXIT_SUCCESS);
	}
	else if(strlen(buff) > 4
		&& strncmp("ban", buff, 3) == 0){
	  msg[0] = 'B', msg[1] = 'A', msg[2] = 'N', msg[3] = ' ';
	  string_bourrage(buff + 4, 8, msg + 4);
	  msg[12] = '\0';

	  sendto(sock_emet, msg, 12, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else if(strcmp(buff, "\\r") == 0){

	  sendto(sock_emet, "RFH", 3, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else{
	  n = atoi(buff);
	  personne = get(&p, n);

	  if(personne == NULL){
	    print(&p);
	    printf("\nIl n'y a pas de personne %d\n", n);
	    break;
	  }

	  sock_client = createSocket(personne->port, personne->adr);
	  if(sock_client == -1){
	    print(&p);
	    printf("Impossible de se connecter a %d\n", n);
	    break;
	  }

	  printf("Connection etablie avec %s \n", personne->nom);
	  client(sock_client, personne->adr);
	  printf("retour de chat\n");
	  //retour de chat
	  sendto(sock_emet, "RFH", 3, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
      }
      
      // Accepte du serveur
      if(polls[1].revents == POLLIN){
	sock_a = accept(sock_serv, (struct sockaddr *)(&c), &csize);
	sock_client = createSocket(atoi(args[2]), "127.0.0.1");
	thread_args.socket = sock_client;
	thread_args.addr = inet_ntoa(c.sin_addr);

	pthread_create(&tclient, NULL, thread_client, (void *)(&thread_args));

	sock_b = accept(sock_serv, (struct sockaddr *)(&c), &csize);
	printf("Quelqu'un souhaite vous parler : \n");
	serveur(sock_a, sock_b);
	
	//retour de chat
	//	sendto(sock_emet, "RFH", 3, 0, (struct sockaddr *)&addr, sizeof(addr));

      }

      // Read dans le recepteur
      if(polls[2].revents == POLLIN){
	recvfrom(sock_recep,  msg, 34, 0, (struct sockaddr *)(&from), &lg);

	if(IS_BAN(msg)){
	  msg[12] = '\0';
	  if(strcmp(msg + 4, me) == 0){
	    if(ban[0] == 0){ 
	      strcpy(ban,inet_ntoa(from.sin_addr));
	      printf("Une personne veut vous bannir\n");
	      printf("%s\n", ban);
	    }
	    else if(strcmp(inet_ntoa(from.sin_addr), ban) != 0){
	      printf("Vous avez été banni\n");
	      
	      iam_msg[0] = 'B';
	      iam_msg[1] = 'Y';
	      iam_msg[2] = 'E';
	      
	      sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	      exit(EXIT_SUCCESS);
	    }
	  }
	}
	else if(IS_RFH(msg)){
	  acceptIAM = 1;
	  del_all(&p);
	  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else if(strcmp(msg+3, iam_msg+3) == 0)
	  break;
	else if(IS_HLO(msg)){
	  acceptIAM = 0;
	  split_and_add(&p,msg);
	  print(&p);
	  sendto(sock_emet, iam_msg, 34, 0, (struct sockaddr *)&addr, sizeof(addr));
	}
	else if(IS_IAM(msg) && acceptIAM){
	  split_and_add(&p,msg);
	  print(&p);
	}
	else if(IS_BYE(msg)){
	  split_and_del(&p, msg);
	  print(&p);
	}
      }
    }
  } 
  return EXIT_SUCCESS;
}
