/* CLIENT.H */
#ifndef CLIENT_H

#define CLIENT_H

int createSocket(int port, char* addresse);
void client(int sock, char * adresse);
void * thread_client(void * s);

#endif
