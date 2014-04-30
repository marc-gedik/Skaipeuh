#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>

void int_bourrage(int n, int size, char * s){
  while(n>0 && size > 0){
    s[-- size] = n % 10 + '0';
    n /= 10;
  }
  while(size > 0){
    s[--size] = '0';
  }
}

void string_bourrage(char* str, int size, char* s){
  int i, b = 0;

  for(i = 0; i < size; i ++){
    if(!b && str[i] == '\0')
      b = 1;
    if(b)
      s[i] = ' ';
    else
      s[i] = str[i];
  }
}

void debourrage_ip(char * s){
  char *s1, *s2, *s3, *s4;
  
  s1 = s, s1[3] = '\0';
  s2 = s+4, s2[3] = '\0';
  s3 = s+8, s3[3] = '\0';
  s4 = s+12, s4[3] = '\0';

  sprintf(s, "%d.%d.%d.%d\n", atoi(s1), atoi(s2), atoi(s3), atoi(s4));
}

int firstThreeLetters(char * s1, char * s2){
  return s1[0] == s2[0] && s1[1] == s2[1] && s1[2] == s2[2];
}

char * getip(){
  char * ip = (char *)malloc(sizeof(char) * 15);
  char buff[256];
  struct hostent * machine;
    
  gethostname(buff, 500);
  
  machine = gethostbyname(buff);

  int_bourrage((unsigned char)machine->h_addr[0], 3, ip);
  int_bourrage((unsigned char)machine->h_addr[1], 3, ip + 4);
  int_bourrage((unsigned char)machine->h_addr[2], 3, ip + 8);
  int_bourrage((unsigned char)machine->h_addr[3], 3, ip + 12);
  
  *(ip + 3) = *(ip + 7) = *(ip + 11) = '.';

  return ip;
}
