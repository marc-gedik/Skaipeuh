#include <stdio.h>
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

int firstThreeLetters(char * s1, char * s2){
  return s1[0] == s2[0] && s1[1] == s2[1] && s1[2] == s2[2];
}
