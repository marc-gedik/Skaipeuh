#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>



int main(int argc,char *argv[]) {
  int mysocket1, mysocket2, PORT;
  struct sockaddr_in a1, a2;
  unsigned int entier;
  char * buff;

  if(argc==2)
    PORT= atoi(argv[1]);
  else{
    printf("manque le port \n");
    return -1;
  }

  mysocket1 = socket(AF_INET,SOCK_STREAM,0);
  if (mysocket1==-1) {
    perror("Socket problem");
    exit(EXIT_FAILURE);
  }

  bzero(&a1,sizeof(a1));
  a1.sin_family = AF_INET;
  a1.sin_port = htons(PORT);
  a1.sin_addr.s_addr = htonl(INADDR_ANY); // teste n'importe quel port

  if (bind(mysocket1 ,(struct sockaddr *)&a1,sizeof(a1))==-1) { //relie le socket a un port
    perror("Bind problem");
    exit(EXIT_FAILURE);
  }

  if (listen(mysocket1,0)==-1) {
    perror("Listen problem ");
    close(mysocket1);
    exit(EXIT_FAILURE);
  }

  int lus=10;
  int i;
  char buffer[256];
    
  while(1){ 
    lus=10;        
    mysocket2=accept(mysocket1, (struct sockaddr *)&a2, (socklen_t*)&entier);
    if(mysocket2==-1){
      perror("Accept problem ok");
      close(mysocket1);
      close(mysocket2);
      exit(EXIT_FAILURE);  
    }
    printf("je lis: \n");
    while (lus>2) {
      lus=read(mysocket2,buffer, 256);
      perror("read " );

      for (i=0; i<lus; i++) 
	printf("%c",buffer[i]);
      printf("nb carac %d\n",lus);     
    }

    printf("ok \n");
    buff = "Lire c'est refuser de mourir\n";   
    write(mysocket2, buff, strlen(buff));
    // close(mysocket1);
    close(mysocket2);
  }

  return 0;
}


