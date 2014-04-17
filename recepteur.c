#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>

#include <poll.h>

char * hlo = "HLO",
  *iam = "IAM",
  *bye = "BYE",
  *rfh = "RFH",
  *ban = "BAN";
int firstThreeLetters(char * s1, char * s2){
  return s1[0] == s2[0] && s1[1] == s2[1] && s1[2] == s2[2];
}

int isHLO(char * s){
  return firstThreeLetters(s, hlo);
}
int isIAM(char * s){
  return firstThreeLetters(s, iam);
}
int isBYE(char * s){
  return firstThreeLetters(s, bye);
}
int isRFH(char * s){
  return firstThreeLetters(s, rfh);
}
int isBAN(char * s){
  return firstThreeLetters(s, ban);
}

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

int main(){
  int sock_recep, sock_emet;
  struct sockaddr_in addr;
  struct pollfd polls;
  char msg[256];

  sock_emet = socket(AF_INET, SOCK_DGRAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = inet_addr("225.1.2.4");
  addr.sin_port = htons(28888);

  sock_recep = createRecepteur(28888, "225.1.2.4"); 
  
  sendto(sock_emet, "HLO user", 15, 0, (struct sockaddr *)&addr, sizeof(addr));

  while(1){
    recv(sock_recep,  msg, 256, 0);
    if(isHLO(msg) || isRFH(msg))
       sendto(sock_emet, "IAM ...", 15, 0, (struct sockaddr *)&addr, sizeof(addr));
       
    printf("recv: %s\n", msg);
  }

  return EXIT_SUCCESS;
}
