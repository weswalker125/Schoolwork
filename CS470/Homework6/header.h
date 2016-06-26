#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/types.h>
#include <string>
#include <string.h>
#include <fstream>
#include <stdio.h>
#include <iostream>

#define KEY  ((key_t)(93938)) 
#define SEGSIZE sizeof(struct StudentInfo) * 20

#define NUM_SEMAPHS 5
#define SEMA_KEY   ((key_t)(3938))

struct StudentInfo{
  char sID[60];
  char name[60];
  char address[60];
  char telNumber[60];
};

void Wait(int semaph, int n);
void Signal(int semaph, int n);
int GetSemaphs(key_t k, int n);