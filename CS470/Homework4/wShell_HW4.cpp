/*
 * Author: Wesley Walker
 * Date: October 28th, 2011
 * For: CS470 - 001, 
 *		University of Kentucky.  
 *		Fall 2011
 *	Title: wShell
 *			(Extended for Homework 4)
*/
#include <iostream>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>

using namespace std;
void getCommand(char command[]); //Retrieves command from user
int parseCommand(char command[], char *parsedCommand[], char *command1[], char *command2[]);

int main() 
{
	int fds[2], count, fd, indicator;
	pid_t pid0, pid1, pid2;
	char userInput[255], c;
	char *parsedInput[2], *command1[20], *command2[20]; //input 1 and input 2

	//Prompt user for input
	getCommand(userInput); 
	//Parse input(returns 1 for redirect, 2 for pipe, 0 for nothing)
	indicator = parseCommand(userInput, parsedInput, command1, command2);
	while((strcmp(userInput, "quit") != 0) && (strcmp(userInput, "exit") != 0) && (indicator >= 0))
	{
		/*Regular command*/
		if(indicator == 0) { 
			if((pid0 = fork()) == 0) //Child code block
			{
				execvp(command1[0], command1);
				
				//Error message will be reached upon failure
				perror("execvp failed!\n");
				return 1;
			} 
			else {
				//Wait on fork
				waitpid(pid0, NULL, 0);
			}
		}
		/* Redirect */
		else if(indicator == 1) { 
			pipe(fds); //create pipe, handover to children
			//Child 1
			if((pid1 = fork()) == 0)
			{
				dup2(fds[1], 1); //stdout -> upstream
				close(fds[0]); //close downstream
				execvp(command1[0], command1); //execute first command
				
				//Error message will be reached upon failure
				perror("execvp failed!\n");
				return 1;
			}
			//Child2
			else if((pid2 = fork()) == 0)
			{
				fd = open(command2[0], O_RDWR|O_CREAT, 0600);
				dup2(fds[0], 0); //downstream -> stdout
				close(fds[1]); //close upstream
				while((count = read(0, &c, 1)) > 0)
					write(fd, &c, 1);
				close(fd);
				return 1;
			} 
			else 
			{
				//Close all pipes
				close(fds[0]);
				close(fds[1]);
				//Wait on all forks
				waitpid(pid1, NULL, 0);
				waitpid(pid2, NULL, 0);
			}
		}
		/* Pipe */
		else if(indicator == 2) { 
			pipe(fds); //create pipe, handover to children
			//Child 1
			if((pid1 = fork()) == 0)
			{
				dup2(fds[1], 1); //stdout -> upstream
				close(fds[0]); //close downstream
				execvp(command1[0], command1); //execute first command
				
				//Error message will be reached upon failure
				perror("execvp failed!\n");
				return 1;
			}
			else if((pid2 = fork()) == 0)
			{
				dup2(fds[0], 0); //downstream -> stdout
				close(fds[1]); //close upstream
				//Execute command2 (with stdout from command1 as stdin)						
				execvp(command2[0], command2);
				
				//Error message will be reached upon failure
				perror("Execvp failed!\n");
				return 1;
			} 
			else 
			{
				//Close all pipes
				close(fds[0]);
				close(fds[1]);
				//Wait on all forks
				waitpid(pid1, NULL, 0);
				waitpid(pid2, NULL, 0);
			}
		}

		//Reset indicator (if something breaks, we won't loop forever)
		indicator = -1;
		//Get user input 
		getCommand(userInput);
		//Parse input (returns 1 for redirect, 2 for pipe, 0 for nothing)
		indicator = parseCommand(userInput, parsedInput, command1, command2);
	}
	
	return 0;
}


// input: (possibly empty) char array
// output: (filled) char array
// action: reads input from user via standard io, stores in char array
void getCommand(char command[])
{
	string tempInput;
	//clear old user input
	strcpy(command, "\0");
	//Re-prompt user for input
	printf("\nwShell> ");
	getline(cin, tempInput);
	strcpy(command, tempInput.c_str());
	
}

// input: char array containing user commands
// output: fills array of char arrays 
//      (segments the command input to function)
int parseCommand(char command[], char *parsedCommand[], char *command1[], char *command2[])
{
        char* pToken;
		int returnVal = 0; //0 for standard, 1 for redirect, 2 for pipe
        bool background = false;
        bool redirect = false, pipe = false;
		
        //Clear arrays
        memset(parsedCommand, NULL, 2);
		memset(command1, NULL, 20);
		memset(command2, NULL, 20);
		
        for(int i=0; command[i] != '\0'; i++)
        {	
				//Looking for redirect
                if(command[i] == '>' && command[i+1] == '>') {
                        redirect = true;
						returnVal = 1; }
				//Looking for pipe
                if(command[i] == '|') {
                        pipe = true;
						returnVal = 2; }
        }
		
		/* Pipe or redirect in input */
        if(pipe || redirect)
        {
            if(pipe) //Tokenize by spaces
                pToken = strtok(command, "|");
            else
                pToken = strtok(command, ">>");
            //Place onto parsedCommand
            parsedCommand[0] = pToken;
			if(pipe)
				pToken = strtok(NULL, "|");
			else
				pToken = strtok(NULL, ">>");
					parsedCommand[1] = pToken;
			
			/* Separate each command into separate arrays*/
			//Tokenize spaces per command
			pToken = strtok(parsedCommand[0], " ");
			command1[0] = pToken;
			for(int j = 1; pToken != NULL; j++) 
			{	
				pToken = strtok(NULL, " ");
				if(pToken == NULL)
					break;
				command1[j] = pToken;
			}
			//Tokenize spaces per command
			pToken = strtok(parsedCommand[1], " ");
			command2[0] = pToken;
			for(int j = 1; pToken != NULL; j++) 
			{	
				pToken = strtok(NULL, " ");
				if(pToken == NULL)
					break;
				command2[j] = pToken;
			}
			/* Separate each command into separate arrays*/			
		}
		
		/* No pipe/redirect*/
		else 
		{	
			//Tokenize by spaces
			pToken = strtok(command, " ");
			command1[0] = pToken;
			for(int j = 1; pToken != NULL; j++)
			{
				pToken = strtok(NULL, " ");
				if(pToken == NULL)
					break;
				command1[j] = pToken;
			}
		}
		
	return returnVal;
}
