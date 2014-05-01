/* SERVEUR.H */

#ifndef SERVEUR_H

#define SERVEUR_H

struct serveur_args{
  int port, filesize;
  char *filename;
};

int createServerSocket(int port);
void serveur(int sock_a, int sock_b);
void * thread_serveur(void * s);

#endif
