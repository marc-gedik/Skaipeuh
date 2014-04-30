SOURCES = skaipeuh.c recepteur.c tools.c tab.c client.c serveur.c
OBJS = $(SOURCES:%.c=%.o)
INCLUDES = -lpthread

OPTION = -Wall
COMP = gcc

all: skaipeuh 

skaipeuh: $(OBJS)
	$(COMP) -o $@ $(OBJS) $(OPTION) $(INCLUDES)

%.o: %.c
	gcc $< $(OPTION) -c

skaipeuh.o: recepteur.h tools.h tab.h client.h serveur.h
client.o: tools.h
serveur.o: tools.h

clean: 
	rm -f skaipeuh $(OBJS)

clear: 
	rm -f $(OBJS)
