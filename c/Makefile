SOURCES = recepteur.c chat.c tools.c tab.c
OBJS = $(SOURCES:%.c=%.o)
INCLUDES = -lpthread

OPTION = -Wall
COMP = gcc

all: skaipeuh 

skaipeuh: $(OBJS)
	$(COMP) -o $@ $(OBJS) $(OPTION) $(INCLUDES)

%.o: %.c
	gcc $< $(OPTION) -c

recepteur.o: chat.h tools.h
chat.c: tools.h

clean: 
	rm -f skaipeuh $(OBJS)

clear: 
	rm -f $(OBJS)
