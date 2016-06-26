/* Print
 * Wesley Walker
 *  
 *  Input: none
 *  Output: all records existing in shared memory
 */

#include "header.h"
using namespace std;

int main(int argc, char* argv[])
{
	int id, sema_set;
	struct StudentInfo *infoptr;
	
	//Get shared memory
	id = shmget(KEY,SEGSIZE, 0);
	if (id <0){
		perror("change: shmget failed 1");
		return 1;
	}

	//Attach to shared memory segment
	infoptr=(struct StudentInfo *)shmat(id,0,0);
	if (infoptr <= (struct StudentInfo *) (0)) {
		perror("change: shmat failed");
		return 2;
	}
	
	//Iterate through all shared memory segments
	for(int i=0; i < 20; i++) 
	{
		sema_set=semget(SEMA_KEY, 0,0); 
		Wait(sema_set, i);
		if((strncmp(infoptr[i].name, "null", 4) == 0) && (strncmp(infoptr[i].sID, "null", 4) == 0))
			break;
		printf("the value of sema_set=%d\n", sema_set);
		printf("Name: %s\n", infoptr[i].name);
		printf("Student ID: %s\n", infoptr[i].sID);
		printf("Address: %s\n", infoptr[i].address);
		printf("Telephone: %s\n", infoptr[i].telNumber);
		sleep(10);
		Signal(sema_set, i); 
	}
	return 0;
}
