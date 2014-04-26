#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tab.h"

void increase_size(struct personnes * t){
  printf("Double\n");
  t->size *= 2;
  t->tab = realloc(t->tab, sizeof(struct personne) * t->size);
}

struct personnes create(){
  struct personnes p;

  p.tab = (struct personne *)malloc(sizeof(struct personne) * 20);
  p.size = 20;
  p.nbr = 0;

  return p;
}

void add_personne(struct personnes *t, struct personne p){
  if(t->size == t->nbr){
    increase_size(t);
  }

  t->tab[t->nbr++] = p;
}

void add(struct personnes * t, char* nom, int port, char * adr){
  struct personne  p;
  
  p.port = port;
  
  strcpy(p.nom, nom);
  strcpy(p.adr, adr);
  
  add_personne(t, p);
}

struct personne split(char * s){
  struct personne p;

  strncpy(p.nom, s + 4, 8);
  p.nom[8] = '\0';
  strncpy(p.adr, s + 13, 15);

  p.port = atoi(s + 29);
  
  return p;
}

void split_and_add(struct personnes *t, char * s){
  struct personne p = split(s);

  add_personne(t, p);
}

void del(struct personnes * t, int pos){
  for(; pos < t->nbr && pos < t->size; pos ++){
    t->tab[pos] = t->tab[pos + 1];
  }
  t->nbr --;
}

void del_all(struct personnes * t){
  t->nbr = 0;
}

struct personne * get(struct personnes * t, int pos){
  if(pos >= t->nbr || pos < 0)
    return NULL;
  else 
    return t->tab+pos;
}

int split_and_get_id(struct personnes * t, char * s){
  struct personne p = split(s);
  int i;
  
  for(i = 0; i < t->nbr; i ++){
    if(t->tab[i].port == p.port
       && strcmp(t->tab[i].nom, p.nom) == 0
       && strcmp(t->tab[i].adr, p.adr) == 0)
      return i;
  }
  return -1;
}

void split_and_del(struct personnes * t, char * s){
  int i = split_and_get_id(t,s);
  if(i != -1)
    del(t, i);
}

void print(struct personnes * t){
  int i;
  if(t->nbr == 0)
    printf("Il n'y a personne de connecter\n");
  else{
    printf("      - Liste des personnes - \n\n");
    printf("   nom      | port  | adresse\n");
    printf("-------------------------------------\n");
  }
  for(i=0; i < t->nbr; i++){
    printf("%d. %s | %d | %s\n", i, t->tab[i].nom, t->tab[i].port, t->tab[i].adr);
  }
  printf("-------------------------------------\n");

  printf("-1. pour se deconecter\n");
}
