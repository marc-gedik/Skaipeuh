#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>

void int_bourrage(int n, int size, char * s){
  int j;

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


