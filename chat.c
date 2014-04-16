#include <stdio.h>
#include <netinet/in.h>
#include <netdb.h>

#include <poll.h>

int createSocket(int port, char* addresse){
  struct sockaddr_in addr;
  int sock;

  sock = socket(PF_INET, SOCK_STREAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = inet_addr(addresse);
  
  connect(sock, (struct sockaddr *)&addr, sizeof(addr));
  perror("client");
  return sock;
}

int createServerSocket(int port){
  struct sockaddr_in addr;
  int sock;
  
  sock = socket(PF_INET, SOCK_STREAM, 0);
  
  bzero(&addr, sizeof(addr));
  
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  
  bind(sock, (struct sockaddr *)&addr, sizeof(addr));
  
  listen(sock, 0);

  return sock;
}

void * chat (int portServeur, int portClient){
  struct sockaddr_in c;
  int sock_serv, sock, s;
  unsigned int csize;
  struct pollfd polls[2]; 
  
  char buff[50];

  sock_serv = createServerSocket(portServeur);
  s = createSocket(portClient, "127.0.0.1");

  csize = sizeof(c);
  
  sock = accept(sock_serv, (struct sockaddr *)&c, &csize);

  polls[0].fd = 0;
  polls[0].events = POLLIN;
  
  polls[1].fd = sock;
  polls[1].events = POLLIN;

  while(1){
    poll(polls, 2, -1);
    if(polls[0].revents == POLLIN){
      fgets(buff, 50, stdin);
      write(s, buff, 50);
    }
    if(polls[1].revents == POLLIN){
      read(sock, buff, 50);
      printf("recu>%s", buff);
    }
  }
}

int main(int argc, int ** argv){
  chat(atoi(argv[1]), atoi(argv[2]));
}
