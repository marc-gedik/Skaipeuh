int createSocket(int port, char* addresse);

int createServerSocket(int port);

void client(int sock);

void serveur(int sock_a, int sock_b);

void * thread_client(void * s);
