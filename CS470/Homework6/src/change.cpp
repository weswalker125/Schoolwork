/* Change
 * Wesley Walker
 *
 * Input: N/A
 *
 *	Prompt for password upon execute
 * 	Ask for student ID
 *	Show the student's records
 *	Ask what fields is to be changed
 *	Get new value
 * 	(reprompt)
*/

#include "header.h"
using namespace std;

int main(int argc, char* argv[])
{
	string correctPassword = "000", inputPassword = "", temp = "";
	char sID[10];
	int sema_set, id, inputEdit = -1;
  	struct StudentInfo *infoptr;

	//prompt for password
	printf("Enter password: ");
	cin >> inputPassword;
	
	//Check validity of password
	if(inputPassword != correctPassword)
	{
		printf("Incorrect password; exiting\n");
		return 1;
	}

	//prompt for student ID:
	printf("Enter student ID of record to adjust: ");
	cin >> sID;
	
	//Get shared memory
	id = shmget(KEY,SEGSIZE, 0);
  	if (id <0){
    	perror("change: shmget failed 1");
    	return 1;
  	}

	//Attach to shared memory
  	infoptr=(struct StudentInfo *)shmat(id,0,0);
  	if (infoptr <= (struct StudentInfo *) (0)) 
	{
    	perror("change: shmat failed");
    	return 2;
	}
	
	//Search for matching sID to input
	for(int i=0; i < 20; i++) 
	{
		//Get set of semaphores
		sema_set=semget(SEMA_KEY, 0,0); 
	  	Wait(sema_set, i);
    	if(strncmp(infoptr[i].sID, sID, 9) == 0)
    	{
    		//Prompt what values to edit
    		while(inputEdit != 0) {
				printf("the value of sema_set=%d\n", sema_set);
	    		printf("Student ID: %s\n", infoptr[i].sID);
		  		printf("Name: %s\n", infoptr[i].name);
		  		printf("Telephone: %s\n", infoptr[i].telNumber);
	    		printf("Address: %s\n", infoptr[i].address);
		  		
		  		//prompt for what field to change:
		  		printf("(1) Name\n(2) Telephone\n(3) Address\n(0) Exit\n\nWhat field to edit? ");
		  		cin >> inputEdit;
		  		
		  		if(inputEdit == 0)
		  			break;
		  		temp = "";
		  		printf("New value: ");
		  		while(temp == "")
		  			getline(cin, temp);

		  		if(inputEdit == 1)
		  			strcpy(infoptr[i].name, temp.c_str());
				if(inputEdit == 2)
	  				strcpy(infoptr[i].telNumber, temp.c_str());
	  			if(inputEdit == 3)
		  				strcpy(infoptr[i].address, temp.c_str());
		  	}
	  		break;
	  	}
	  	Signal(sema_set, i); 
	}
  return 0;
}