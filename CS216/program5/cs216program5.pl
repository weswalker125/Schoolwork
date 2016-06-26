#!usr/bin/perl -w
#use strict;

# Author: Wesley Walker
# Completion Date: November 16th, 2009
#					8:30pm
# Submitted late for C++ file edits.

#
# Program (similar to Program Assignment 3) reads in data from 2 files,
# patient.data and event.data.  Data read in is not assumed to be good,
# find all exceptions, kill program.  The program accomodates any number 
# of records in the files.  It's assumed that patient.data and event.data 
# are in the current directory, if not, the program will display error 
# message and terminate.
#
# P.S. sorry I didn't use strict.  I got caught up in using global
# variables, by the time I realized it, I [think I] would've had to
# restructure my entire program to fix it.  
# Program5 is an alteration of Program4, simply removing some loops.
#


# arguments passed into the program:
$SEARCHMETHOD = $ARGV[0];
$IDNAME = $ARGV[1];

# Open/read in patient.data records.
open(PATIENT, "<patient.data")
	or die "File patient.data not found...\n";
my @patient;
@patient = <PATIENT>;
close(PATIENT);

# Call function to check file data
&checkPatient;

# Open/read in event.data records.
open(EVENT, "<event.data")
	or die "File event.data not found...\n";
my @event;
@event = <EVENT>;
close(EVENT);

# Call function to check file data
&checkEvent;

&SelectAnOption;

# checkPatient error-checks the input from patient.data
sub checkPatient {
    my $ct = 0;
    my $linect = 0; #keeps track of what line of the file we're on (for output)
    
    #traverse the array
	foreach(@patient) {
		$linect++;
		my @patientLine = split(' ', $_);
			
			# if there's more/less than 6 data items:
			if(@patientLine != 6) {
				if($ct == 0) {
					$ct = 1;
					print "Too few or too many data items on a line:\n"}
				die "Line $linect of patient.data has incorrect number of data items\n";
			}
			
			# if the patient number is more/less than 6 in length
			if(length $patientLine[0] != 6) {
				die "Length of patient number on line $linect of patient.data is incorrect\n";}
				
			# if non-digits are detected
			if($patientLine[0] =~ /\D/) {
				die "patient number on line $linect of patient.data contains non-digits\n";}
				
			# if last name contains non-alphabetical characters
			if($patientLine[1] =~ /\d|\W/) {
				die "Last name on line $linect of patient.data contains nonalphabetical characters\n";}
			
			#if last name is more than 20 character
			if(length $patientLine[1] > 20) {
				die "Last name on line $linect of patient.data contains too many characters\n";}
				
			# if first name contains non-alphabetical characters
			if($patientLine[2] =~ /\d|\W/) {
				die "First name on line $linect of patient.data contains nonalphabetical characters\n";}
			
			#if first name is more than 20 character
			if(length $patientLine[2] > 20) {
				die "First name on line $linect of patient.data contains too many characters\n";}
				
			# if birthdate contains non-digits
			if($patientLine[3] =~ /D/) {
				die "Birthdate on line $linect of patient.data contains non-digits\n";}
				
			# if birthdate is formatted incorrectly
			$temp = &birthday($patientLine[3]);
			if($temp eq "BAD") {
				die "Birthday on line $linect of patient.data is invalid\n";}
			
			# if floor contains non-alphanumeric characters
			if($patientLine[4] =~ /\W/)	{
				die "Floor on line $linect of patient.data contains non-alphanumeric characters\n";}

			# check the DNA file's format
			if($patientLine[5] =~ /DNA(\d*)\.data/) {
				if($1 < 0 or $1 > 999) {
					die "DNA file on line $linect of patient.data is incorrectly formatted\n";}
			}
			if($patientLine[5] =~ /NONE/) {
				#that's okay
			}
			#else {
			#	die "DNA file on line $linect of patient.data is incorrectly formatted\n";}
	}
}

# checkEvent error-checks the file input of event.data
sub checkEvent {
	my $ct = 0;
    my $linect = 0; #keeps track of the line we're on.
    
    #traverse the array of events
	foreach(@event) {
		$linect++;
		my @eventline = split(' ', $_);
		
		# check for 4 data items per line
		if(@eventline != 4) {
			if($ct == 0) {
				$ct = 1;
				print "Too few or too many data items on a line:\n";}
			die "Line $linect does not contain 4 data items\n";}
	}
	
	$linect = 0;
	
	#traverse for remaining possible errors
	foreach(@event) {
			$linect++;
			@eventline = split(' ', $_);
			
			# Check format of Event ID
			if(length $eventline[0] != 5) {
				print "Length of Event ID on line $linect of event.data is incorrect\n"; 
				die;}
				
			# check event ID for non-alphanumeric characters	
			if($eventline[0] =~ /\W+/) {
				die "Event ID on line $linect of event.data contains non-alphanumeric characters\n";}

			# Check patient number for non-digits
			if($eventline[1] =~ /\D/) {
				die "Patient number on line $linect of event.data contains non-digits\n";}
				
			# Check length of patient number	
			if(length $eventline[1] != 6) {
				die "Patient number on line $linect of event.data is invalid\n";}
				
			# Check code ID for non-alphabetical characters	
			if($eventline[2] =~ /\d|\W/) {
				die "Code ID on line $linect of event.data contains non-alphabetical characters\n";}
			# Check length of code ID
			if(length $eventline[2] != 3) {
				die "Code ID on line $linect of event.data is invalid\n";}
				
			# if eventdate contains non-digits
			if($eventline[3] =~ /D/) {
				print "Birthdate on line $linect of event.data contains non-digits\n";
				die;}
				
			# if eventdate is formatted incorrectly
			$temp = &birthday($eventline[3]);
			if($temp eq "BAD") {
				die "event date on line $linect of event.data is invalid\n";}
		
	}	
}	

sub SelectAnOption {

		#get input from user
		$option = $SEARCHMETHOD;
   
   		chomp $option;
   
		if($option == 1){
			&option1;}
   
		if($option == 2) {
			&option2;}
   
		if($option == 3) {
			&option3;}
   
		if($option == 4) {
			&option4;}  
}

# Option 1 takes the user's input as a "patient number" and finds it in the array of 
# existing patients, then prints its data in a specified format.
# After finding the patient, the function looks for the user's event ID from event.data
# and prints it in the specified way.

sub option1 {
$flag1 = "false";
$flag2 = "false";
$patientNumber = $IDNAME;

   chomp $patientNumber;
   
   # if we find a non-digit
   if($patientNumber =~ /\D/) {
   	print "Invalid input...\n";}

	else {
  	 	#traverse the array
    	foreach(@patient) {
    		#split up by spaces
		@patientLine = split(' ', $_);
		if($patientLine[0] == $patientNumber) {
		
			#set flag to true, denoting a match has been found.
		   $flag1 = "true";	   
		   
		   my $newBDAY = &birthday($patientLine[3]);
		   
		   #print the matched patient's information
		   print "Patient number& $patientLine[0]\n";
		   print "Last name& $patientLine[1]\n";
		   print "First name& $patientLine[2]\n";
		   print "Birthdate& $newBDAY\n";
		   print "Floor& $patientLine[4]\n";
		   print "DNA file name& $patientLine[5]\n";
		  
		 	
		  
		   #Find the event(s) to the matched patient		
		   foreach(@event) {
		   	#traverse array of events and split by spaces
			@eventLine = split(' ', $_);
			
			#if we find an event, flag true, and output
			if($eventLine[1] == $patientNumber) {
			   $flag2 = "true";
	
			   print "Event ID(s):& $eventLine[0] \n";
			}
		   }
		   
		   #if we find no events
		   if($flag2 eq "false") {
			print "Event ID(s):& none\n";}
		 }
	    }
	  
	  #we found no patients
	   if($flag1 eq "false") {
		   print "Patient not found...\n";}
	}
}
   


# Option 2 takes the user's input as a "patient's last name", and searches the 
# array of patient's for their match.  Output for Option 2 is identical to Option 1,
# (exception being if two people have the same last name.
sub option2 {
$flag1 = "false";
$flag2 = "false";
$patientName = $IDNAME;

	chomp $patientName;
   # Traverse array of patients
   foreach(@patient) {
   	# separate the line by the spaces,
	@patientLine = split(' ', $_);
	
	# Check the 2nd item of the line (last name) for match with input.
	if($patientLine[1] =~ /$patientName/i) {
	   
	   #set flag to true, denoting a match has been found.
	   $flag1 = "true";	   
	   
	   my $newBDAY = &birthday($patientLine[3]);
	   
	   #print the matched patient's information
	   print "Patient number& $patientLine[0]\n";
	   print "Last name& $patientLine[1]\n";
	   print "First name& $patientLine[2]\n";
	   print "Birthdate& $newBDAY\n";
	   print "Floor& $patientLine[4]\n";
	   print "DNA file name& $patientLine[5]\n";

	   #Find the event to the matched patient		
	   foreach(@event) {
	   	#reset flag
	   	$flag2 = "false";

		@eventLine = split(' ', $_);
		
		if($eventLine[1] eq $patientLine[0]) {
			#flag2 denotes that we've found an event for the patient
		   $flag2 = "true";
		
		   print "Event ID(s):&$eventLine[0] \n";}
		}
	   
	   # if we've not found an event
	  # if($flag2 eq "false") {
		#print "Event ID(s):& none";}
	   }
	  # print uniform newline (at the end) regardless of how many event IDS
	  # complete aesthetic, nothing more than for looks.
	   print "\n";
	  
   	}
   
   
   #we found no patients
   if($flag1 eq "false") {
	   print "Patient not found...\n"; }	  
}



# Option 3 takes input from the user as an "event ID", then searches 
# the array of events for a match, then prints the patient's event info.
sub option3 {
   $flag1 = "false";
  
   $eventID = $IDNAME;
   
   	chomp $eventID;
   
   #find non-alphanumerical characters
   	if($eventID =~ /\W/) {
   		print "Invalid event ID format\n" ;}
    
    #exit strategy
    if($eventID eq "0") {
    	last;}
    
    #traverse array
    foreach(@event) {
    	#split by spaces
    	@eventline = split(' ', $_);
    	# compare input to id, (lower case, so input is case insensative).
    	if(lc($eventline[0]) eq lc($eventID)) {
			#set flag to true (found)
    		$flag1 = "true";
    		
    		#change date format
    		my $newEDAY = &birthday($eventline[3]);
    		
    		print "Event ID& $eventline[0] \n";
    		print "Patient number& $eventline[1] \n";
    		print "Code ID& $eventline[2] \n";
    		print "Event date& $newEDAY\n";
    		}
    }
    
    #we found no event ID
    if($flag1 eq "false") {
    	print "Cannot find this event ID \n"; }
    	
}
# birthday is a sub routine which changes the inputted format of the birthdate of a patient,
# from (example) 01/01/1990 to January 1 1990
sub birthday {
	my $month = "";
	my $day = "";
	my $year = "";
	
	#set $BDAY to the parameter passed to it.
	my $BDAY = $_;
	
	
	#pattern match for correct form of birthday
	if($BDAY =~ /(\d*)\/(\d)(\d)\/(\d+)/) {
		#for month:
			if($1 eq "1") {
				$month = "January";}
			
			if($1 eq "2") {
				$month = "February";}
			
			if($1 eq "3") {
				$month = "March";}
			
			if($1 eq "4") {
				$month = "April";}
			
			if($1 eq "5") {
				$month = "May";}
			
			if($1 eq "6") {
				$month = "June";}
			
			if($1 eq "7") {
				$month = "July";}
			
			if($1 eq "8") {
				$month = "August";}
			
			if($1 eq "9") {
				$month = "September";}
			
			if($1 eq "10") {
				$month = "October";}
			
			if($1 eq "11") {
				$month = "November";}
			
			if($1 eq "12") {
				$month = "December";}
		#day
		if($2 eq "0") {
			$day = $3;}
			
		else {
			$day = "$2$3";}
			
		#year
		$year = $4;
		}
	# birthday is of incorrect format
	else {
		$badbday = "BAD";
		return $badbday;}
	
	$BDAY = "$month $day $year";
	#print "$BDAY\n";
	return $BDAY;
}
