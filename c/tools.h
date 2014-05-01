/* TOOLS.H */
#ifndef TOOLS_H
#define TOOLS_H

void int_bourrage(int n, int size, char * s);

void string_bourrage(char* str, int size, char* s);

void debourrage_ip(char * s);

int firstThreeLetters(char * s1, char * s2);

char * getip();

#define IS_HLO(s) firstThreeLetters(s, "HLO")
#define IS_IAM(s) firstThreeLetters(s, "IAM")
#define IS_BYE(s) firstThreeLetters(s, "BYE")
#define IS_RFH(s) firstThreeLetters(s, "RFH")
#define IS_BAN(s) firstThreeLetters(s, "BAN")

#define IS_MSG(s) firstThreeLetters(s, "MSG")
#define IS_CLO(s) firstThreeLetters(s, "CLO")
#define IS_FIL(s) firstThreeLetters(s, "FIL")
#define IS_ACK(s) firstThreeLetters(s, "ACK")
#define IS_NAK(s) firstThreeLetters(s, "NAK")

#define MAX_LINE 500
#define BUFFSIZE 512

#define ADRESSE "224.5.6.7"
#define PORT 9876

#endif

