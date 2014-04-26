struct personne{
  char nom[9];
  int port;
  char adr[16];
};

struct personnes {
  struct personne * tab;
  int size;
  int nbr;
};

void print(struct personnes * t);
struct personnes create();
void add(struct personnes * t, char* nom, int port, char * adr);
void split_and_add(struct personnes *t, char * s);
void del(struct personnes * t, int pos);
void del_all(struct personnes * t);
struct personne * get(struct personnes * t, int pos);
void split_and_del(struct personnes * t, char * s);
