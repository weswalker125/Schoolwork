Homework 6	Homework 6	Homework 6	Homework 6

Author: Wesley Walker
Due Date: 12/06/11
For CS470 - 001, Fall 2011 at the University of Kentucky

COMPILE ALL PROGRAMS:
	>make all

load
	EXECUTION:
		>./load [inputfile]
	DESCRIPTION:
		load program expects a correctly formatted ASCII file of student records
		to load into shared memory.  
print
	EXECUTION:
		>./print
	DESCRIPTION:
		print program accesses all segments of shared memory (identified by a set
		key), and writes each to standard output.
clean
	EXECUTION:
		>./clean
	DESCRIPTION:
		clean program accesses all segments of shared memory (identified by a set
		key), and writes each to a file named student.db.  After writing these 
		records, the shared memory segments are detached and destroyed.
change
	EXECUTION:
		>./change
	DESCRIPTION:
		change is a program which will prompt the user for an access password and a 
		student ID.  If the student ID matches an entry in shared memory, the record
		will be displayed with options to change any field (chosen by the user).
query
	EXECUTION:
		>./query [student ID]
	DESCRIPTION:
		query program takes in a given student ID, looks for a matching entry within 
		shared memory and if found, displays the record to standard output.
