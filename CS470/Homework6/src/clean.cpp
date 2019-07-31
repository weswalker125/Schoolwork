/* Clean
 * Wesley Walker
 *
 * Input: N/A
 *
 * Retrieve all records from shared memory,
 * write records to local file.
 * Detach and delete shared memory segments
 */

#include "header.h"
using namespace std;

int main(int argc, char* argv[]) 
{
	struct StudentInfo *infoptr;
    int sema_set, id, sema_id = 0;
    FILE *oFile;
    char sID[60], name[60], address[60], tNum[60];
	oFile = fopen("student.db", "w");

	//Get shared memory
    if((id = shmget(KEY, SEGSIZE, IPC_CREAT | 0666)) < 0)
    {
        perror("shmget");
    	return -1;
    }

    //Attach to shared memory segment
    if((infoptr =(struct StudentInfo*) shmat(id, NULL, 0)) <= (StudentInfo *) (0)) 
    {
        perror("shmat");
        return -1;
   	}

    //Write all records to file
   	for(int i=0; i < 20; i++) 
    {
		sema_set=semget(SEMA_KEY, 0,0); 
		Wait(sema_set, 1);
		//printf("the value of sema_set=%d\n", sema_set);
        	fprintf(oFile, "%s\n", infoptr[i].name);
 	       	fprintf(oFile, "%s\n", infoptr[i].sID);
		fprintf(oFile, "%s\n", infoptr[i].address);
		fprintf(oFile, "%s\n", infoptr[i].telNumber);
		Signal(sema_set, 1); 
	}

    //Detach shared memory
    shmdt((char  *)infoptr);
    shmctl(id, IPC_RMID,(struct shmid_ds *)0);
    semctl(sema_set,0,IPC_RMID);
    return 0;
}
