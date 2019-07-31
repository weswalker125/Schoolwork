/* Load 
 * Wesley Walker
 * 
 * Input: student records data file
 *
 * Read data from file 
 * Attach shared memory segment
 * Load student's records into shared memory
 */

#include "header.h"
#include <unistd.h>
using namespace std;

int main(int argc, char* argv[])
{
	if(argc != 2) 
	{
		printf("Error: LOAD requires 1 input parameter (data file)\n");
		return -1;
	}

	struct StudentInfo *infoptr;
	int sema_set, id, ct = 0;
	char sID[60], name[60], address[60], tNum[60];

	//Open file
	ifstream iFile;
	iFile.open(argv[1]);
	
	while(iFile)
	{
		if(iFile.eof())
			break;
		//Read data
		iFile.getline(name, 60, '\n');
		iFile.getline(sID, 60, '\n');
		iFile.getline(address, 60, '\n');
		iFile.getline(tNum, 60, '\n');
		
		//Get shared memory
		if((id = shmget(KEY, SEGSIZE, IPC_CREAT | 0666)) < 0)
		{
			perror("shmget");
			return -1;
		}

		//Attach to shared memory segment
		if((infoptr =(struct StudentInfo*) shmat(id, 0, 0)) <= (StudentInfo *) (0)) 
		{
			perror("shmat");
			return -1;
		}

		//Write record to shared memory	
		sema_set = GetSemaphs(SEMA_KEY, NUM_SEMAPHS);
		Wait(sema_set, ct);
		printf("the value of the sema_set=%d\n", sema_set);
		printf(" name: %s \n StudentID: %s \n address: %s \n telephone: %s \n", name, sID, address, tNum);
		printf("ct: %d \n\n", ct);
		strcpy(infoptr[ct].name, name);
		strcpy(infoptr[ct].sID, sID);
		strcpy(infoptr[ct].address, address);
		strcpy(infoptr[ct].telNumber, tNum);
		Signal(sema_set, ct);
		ct++;
	}
	iFile.close();

	sema_set = GetSemaphs(SEMA_KEY, NUM_SEMAPHS);	
	Wait(sema_set, ct);
	strcpy(infoptr[ct].name, "null");
	strcpy(infoptr[ct].sID, "null");
	strcpy(infoptr[ct].address, "null");
	strcpy(infoptr[ct].telNumber, "null");
	usleep(10);
	Signal(sema_set, ct);

	return 0;
}
	
