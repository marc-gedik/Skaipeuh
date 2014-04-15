#include <stdio.h>
#include <netinet/in.h>
#include <netdb.h>
#include <pthread.h>

void * client(){
  struct sockaddr_in addr;
  int sock;
  char buff[50];

  sock = socket(PF_INET, SOCK_STREAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_port = htons(60212);
  addr.sin_addr.s_addr = inet_addr("127.0.0.1");
  
  connect(sock, (struct sockaddr *)&addr, sizeof(addr));
  
  for(;;){
    printf(">");
    scanf("%s", buff);
    write(sock, buff, 50);
  } 
}

void * serveur (){
  struct sockaddr_in addr, c;
  int sock_serv, sock;
  unsigned int csize;
  
  char buff[50];
  
  sock_serv = socket(PF_INET, SOCK_STREAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_port = htons(60212);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  
  bind(sock_serv, (struct sockaddr *)&addr, sizeof(addr));
  
  listen(sock_serv, 0);
  csize = sizeof(c);
  
  sock = accept(sock_serv, (struct sockaddr *)&c, &csize);
  
  while(1){
    read(sock, buff, 50);
    printf("recu>%s\n", buff);
  }
}

int main(int argc, int ** argv){
  pthread_t thread_serv, thread_client;
  
  pthread_create(&thread_serv, NULL, serveur, NULL);
  pthread_create(&thread_client, NULL, client, NULL);
  
  pthread_join(thread_serv, NULL);
  pthread_join(thread_client, NULL);
  
}
