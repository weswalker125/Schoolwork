/*
 * Author: Wesley Walker
 * Due: September 23, 2011
 * CS470 - 001
 * Homework Assignment 2
 */

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
using namespace std;

void getCommand(char command[]); //Retrieves command from user
bool parseCommand(char command[], char *parsedCommand[]); //Separates command, checks for specifics

int main()
{
	char userInput[255]; //string for user's commands
	char *parsedInput[20]; //array of command segments
	bool inBackground = false; //boolean indicating if process should be ran in the background

	//Prompt user for input
	printf("\nwShell> ");
	getCommand(userInput);
	//Parse and determine if backgrounding is desired
	inBackground = parseCommand(userInput, parsedInput);

	pid_t pid;
	while((strcmp(userInput, "quit") != 0)  && (strcmp(userInput, "exit") != 0))
	{
		pid = fork();
		if (pid ==  0)
		{ 	//Execute!
			execvp(parsedInput[0], parsedInput);
			//if execvp returns, there was an error
			perror("execvp failed!");
			return 1;
		}
		else
		{
			if(!inBackground)
				waitpid(pid, NULL, 0);
			//Re-prompt user for input
			printf("\nwShell> ");
			//clear old user input
			strcpy(userInput, "\0");
			//Get new user input
			getCommand(userInput);
			//Parse and determine if backgrounding is desired
			inBackground = parseCommand(userInput, parsedInput);
		}
	}
	return 0;
}

// input: (possibly empty) char array
// output: (filled) char array
// action: reads input from user via standard io, stores in char array
void getCommand(char command[])
{
	cin.getline(command, 255);
}


// input: char array containing user commands
// output: fills array of char arrays 
//	(segments the command input to function)
bool parseCommand(char command[], char *parsedCommand[])
{
	char* pToken;
	bool background = false;
	//Clear parsedCommand
	memset(parsedCommand, NULL, 20);

	//Tokenize by spaces
	pToken = strtok(command, " ");
	//Place onto parsedCommand
	parsedCommand[0] = pToken;
	for(int i = 1; pToken != NULL && i < 20; i++)
	{
		//Tokenize by spaces
		pToken = strtok(NULL, " ");
		//End of command:
		if(pToken == NULL)
			break;
		//Backgrounding is desired
		if(*pToken == '&')
			return true;
		//Place token onto parsedCommand
		parsedCommand[i] = pToken;
	}
	return false;
}


