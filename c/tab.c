#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tab.h"

void increase_size(struct personnes * t){
  //Doubler la taille
}

struct personnes create(){
  struct personnes p;

  p.tab = (struct personne *)malloc(sizeof(struct personne) * 20);
  p.size = 20;
  p.nbr = 0;

  return p;
}

void add(struct personnes * t, char* nom, int port, char * adr){
  struct personne p;
  if(t->size == t->nbr){
    increase_size(t);
  }
  
  p.port = port;
  
  strcpy(p.nom, nom);
  strcpy(p.adr, adr);
  
  t->tab[t->nbr++] = p;
}

void del(struct personnes * t, int pos){
  for(; pos < t->nbr && pos < t->size; pos ++){
    t->tab[pos] = t->tab[pos + 1];
  }
  t->nbr --;
}

void print(struct personnes * t){
  int i;
  for(i=0; i < t->nbr; i++){
    printf("%d. %s %d %s\n", i, t->tab[i].nom, t->tab[i].port, t->tab[i].adr);
  }
}

/*int main(){
  struct personnes p;
  
  p = create();
  add(&p, "num1", 1, "192.0.0.1");
  add(&p, "num2", 2, "192.0.0.2");
  add(&p, "num3", 3, "192.0.0.3");
  add(&p, "num4", 4, "192.0.0.4");
  add(&p, "num5", 5, "192.0.0.5");
  
  del(&p, 4);
  print(&p);
  
  return 0;
}
*/
