#include <stdio.h>
#include <unistd.h>
#include <netdb.h>
#include <string.h>

int main(){
  char buff[500];
  struct hostent * stru;
  struct in_addr addr;
  int i;
  
  gethostname(buff, 500);
  printf("%s\n", buff);
  
  stru = gethostbyname(buff);

  for(i=0; stru->h_addr_list[i] != NULL; i++){
    memcpy(&addr.s_addr, stru->h_addr_list[i], stru->h_length);

printf("IP serveur: %d.%d.%d.%d\n",
        (unsigned char)stru->h_addr_list[i][0],
        (unsigned char)stru->h_addr_list[i][1],
        (unsigned char)stru->h_addr_list[i][2],
        (unsigned char)stru->h_addr_list[i][3]);
  }
}
