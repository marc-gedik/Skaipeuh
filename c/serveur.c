#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#include <netdb.h>


#include <fcntl.h>
#include <sys/types.h>
#include <pwd.h>

#include <poll.h>

#include "tools.h"
#include "serveur.h"

int createServerSocket(int port){
  struct sockaddr_in addr;
  int sock;
  sock = socket(PF_INET, SOCK_STREAM, 0);
  if(sock == -1){
    perror("Serveur socket");
    return -1;
  }
  bzero(&addr, sizeof(addr));

  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);

  if(bind(sock, (struct sockaddr *)&addr, sizeof(addr)) == -1){
    perror("Serveur bind");
    return -1;
  }

  if(listen(sock, 0) == -1){
    perror("Seveur listen");
    return -1;
  }

  return sock;
}

void serveur(int sock_a, int sock_b){
  struct pollfd polls[2];
  char buff[MAX_LINE + 4 + 4];
  int lu;

  polls[0].fd = sock_a;
  polls[0].events = POLLIN;

  polls[1].fd = sock_b;
  polls[1].events = POLLIN;

  while(1){
    poll(polls, 2, -1);
    if(polls[0].revents == POLLIN){
      lu = read(sock_a, buff, MAX_LINE + 8);
      write(sock_b, buff, lu);
    }
    if(polls[1].revents == POLLIN){
      lu = read(sock_b, buff, MAX_LINE + 8);
      write(sock_a, buff, lu);
    }    
    if(IS_CLO(buff)){
      break;
    }
  }
}

void fileServeur(int file, int sender, int filesize){
  int n = 0, lu;
  char buff[BUFFSIZE +1];
  while(n < filesize) {
    lu = read(sender, buff, BUFFSIZE);
    n += lu;
    write(file, buff, lu);
  }
}

int openFile(char * s){
  const char *homedir = getpwuid(getuid())->pw_dir;
  int n, len, filelen, i = 0;
  char * buff;
  filelen = strlen(s);
  len = strlen(homedir) + 1;
  buff = (char *)malloc((len+filelen+100)*sizeof(char));
  sprintf(buff, "%s/Téléchargements/", homedir);
  if(open(buff, O_RDONLY) == -1)
    sprintf(buff, "%s/%s", homedir, s);
  else{
    sprintf(buff, "%s/Téléchargements/%s", homedir, s);
    len += 18;
  }
  while(1){
    n = open(buff, O_RDONLY);
    if(n == -1){
      n = open(buff, O_CREAT | O_WRONLY, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
      break;
    }
    sprintf(buff+len+filelen, ".%d", i++);
  }
  printf("Le fichier va etre enregistre a l'adresse : %s\n", buff);
  free(buff);
  return n;
}

void * thread_serveur(void * s){
  struct serveur_args* args = (struct serveur_args*) s;
  struct sockaddr_in c;
  socklen_t csize;
  int socket = createServerSocket(args->port);
  int file, sender = 0;
  
  csize = sizeof(c);

  if(socket != -1){
    sender = accept(socket, (struct sockaddr *)(&c), &csize);
    file = openFile(args->filename);
    fileServeur(file, sender, args->filesize);
    printf("Fichier recus\n");
    close(file);
    close(sender);
    close(socket);
  }
  return NULL;
}

