#include <stdlib.h>
#include <stdio.h>
 
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>

int main(){
  struct sockaddr_in addr;
  int socket_desc;
  struct ip_mreq mreq;
  char msg[256];
  int ok = 1;

  socket_desc = socket(AF_INET, SOCK_DGRAM, 0);
  
  setsockopt(socket_desc, SOL_SOCKET, SO_REUSEPORT, &ok, sizeof(ok));
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_addr.s_addr = INADDR_ANY;
  addr.sin_port = htons(28888);
  
  bind(socket_desc, (struct sockaddr *)&addr, sizeof(addr));
  
  mreq.imr_multiaddr.s_addr = inet_addr("225.1.2.4");
  mreq.imr_interface.s_addr = htonl(INADDR_ANY);
  
  setsockopt(socket_desc, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq));

  //bzero(msg, 256);
  
  printf("Recev : \n");
  recv(socket_desc, msg, 256, 0);
  printf("%s\n", msg);
  sendto(socket_desc, "IAM user... machine", 56, 0, (struct sockaddr *)&addr, sizeof(addr));
  return EXIT_SUCCESS;
}
