/* CLIENT.H */
#ifndef CLIENT_H

#define CLIENT_H

struct client_args{
  int socket;
  char * addr;
};

struct send_file{
  int socket;
  int file;
};

int createSocket(int port, char* addresse);
void client(int sock, char * adresse);
void * thread_client(void * s);

#endif
