/* Query
 * Wesley Walker
 *
 *	Input: student ID
 *	Output: that student's records
*/

#include "header.h"
using namespace std;

int main(int argc, char* argv[])
{
	if(argc != 2)
	{
		printf("Invalid parameters\n");
		return 1;
	}

	int id, sema_set;
  	struct StudentInfo *infoptr;
	char sID[60];
	strcpy(sID, argv[1]);

	if((id = shmget(KEY, SEGSIZE, IPC_CREAT | 0666)) < 0)
	{
		perror("shmget");
		return -1;
	}

  	//attach segment
	if((infoptr =(struct StudentInfo*) shmat(id, 0, 0)) <= (StudentInfo *) (0)) 
	{
		perror("shmat");
		return -1;
	}
	
	for(int ct = 0; ct < 20; ct++) 
	{
		sema_set=semget(SEMA_KEY, 0,0); 
	  	Wait(sema_set, ct);
		if(strncmp(infoptr[ct].sID, sID, 9) == 0)
    	{
			printf("the value of sema_set=%d\n", sema_set);
    		printf("Student ID: %s\n", infoptr[ct].sID);
  			printf("Name: %s\n", infoptr[ct].name);
  			printf("Telephone: %s\n", infoptr[ct].telNumber);
			printf("Address: %s\n", infoptr[ct].address);
  			break;
		}
		sleep(10);
		Signal(sema_set, ct); 
	}
  return 0;
}
