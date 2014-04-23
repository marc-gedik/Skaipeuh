struct personne{
  char nom[8];
  int port;
  char adr[15];
};

struct personnes {
  struct personne * tab;
  int size;
  int nbr;
};
