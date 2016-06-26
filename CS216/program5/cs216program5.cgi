#!/usr/local/bin/perl -w
use strict;
# Wesley Walker
# CGI for cs216program5
# Completion Date: Dec. 4th, 2009
# 	1:50AM
# This CGI script executes the user's requested program to do the query 
# either in perl or c++, passing the it the desired search method and ID,
# name or event.
# Then the program sends the data back to the browser in an HTML formatted
# web page, using tables etc.

# Initialize my HTML tags (despite the results, the page will
# appear in HTML).
print "Content-type: text/html\n";  
print "\n";
print "<html>\n";
print "<head><title> Search results</title></head>";

#set background
print "<body style=background-image:url(background.jpg)\;";
print "background-repeat:repeat\;\">";
print "<center>";

# Get input from HTML
my $form;
$form = <STDIN>;

# Separate the input
if($form =~ /LANGUAGE=(\w+)&SEARCHMETHOD=(\d)&IDNAME=(\w+)/) {
   my $SEARCHMETHOD = $2;
   my $IDNAME = $3;

   # Determine which language to use, and call its respective function
   # to execute that function.
   if($1 eq "PERL") {
	&callPERL($SEARCHMETHOD, $IDNAME); }
   if($1 eq "CPP") {
	&callCPP($SEARCHMETHOD, $IDNAME); }
}
# End "main code"

# Begin "callPERL" subroutine
# callPERL takes the passed parameters, the search method and the 
# string containing an ID, name or event, and runs the PERL program.
# The PERL program returns the matched information, then the function 
# formats the returned strings (in an array) into an HTML table.  The
# event information is hidden until the user clicks to display.  Here I used
# a checkbox, the method of which was learned from w3schools.com.  This site 
# provides great tutorials on web development (or atleast the 
# HTML/CSS/JAVASCRIPT stuff was pretty efficent).

sub callPERL {
   # Get passed parameters
   my $EVENTfirst = 0;
   my @form = @_;
   my $SEARCHMETHOD = $form[0];
   my $IDNAME = $form[1];

   # Print which program is ran
   print "<p>Patient data using PERL\n</p>";

   # Store patient info after calling program
   my @returnedperl = `perl cs216program5.pl $SEARCHMETHOD $IDNAME`;
   print "<table border=\"1\">";
   print "<tr>";
   # Header of table
   if($SEARCHMETHOD eq "1") {
	print "<th>Patient number searched for: </th>"; }
   if($SEARCHMETHOD eq "2") {
	print "<th>Patient name searched for: </th>"; }
   if($SEARCHMETHOD eq "3") {
	print "<th>Event searched for: </th>"; }
   # End header

   print "<th> $IDNAME</th>";   
   print "</tr>";
   
   # Traverse the output
   # Separate output for table format
   foreach(@returnedperl) {
	my @pairsperl = split('&', $_);

	# If the current pair is an Event ID
	if($pairsperl[0] =~ /Event ID\(s\):/) {
        # If the search method is by name, don't display event info
                 if($SEARCHMETHOD eq "2") {
                       last; }
       
	# Create new table for event ID
	   print "<tr></table>";
	
	# Only hide on the first Event
	   if($EVENTfirst == 0) {
		   # Script stuff for the hidden info 
		   # This is the script behind the "checkbox" which will
		   # toggle show/hide the info below it.
	   	print "<script type=\"text/javascript\">";
	   	print "function showMe (it, box) {";
	   	print "var vis = (box.checked) ? \"block\" : \"none\"\;";
	   	print "document.getElementById(it).style.display = vis; }";
	   	print "</script>";
		   # End script stuff for the hidden info

		# Show/Hide checkbox
	  	print "Show Event ID: <input type=\"checkbox\"";
	   	print "name=\"multi_note\" value=\"Show Event ID\"";
	   	print "onclick=\"showMe('div1', this)\">";
	   	print "<div id=\"div1\" style=\"display: none\;\">";
		# End Show/Hide checkbox
		
		$EVENTfirst = 1;
	   }
	# For all events after the first 
	# Begin Table
	   print "<table border=\"1\"><tr>";
	#Call program, store output
	   my @Eventperl = `perl cs216program5.pl 3 $pairsperl[1]`;
	   
	   foreach(@Eventperl) {
		# Split each pair
		my @eventpairs = split('&', $_);

	  	print "<td> $eventpairs[0] </td>";
		print "<td> $eventpairs[1] </td>";
	  	print "</tr>";
	   }
	   # close last table (event)
	   print "</table>";
	   }

	else {
	   print "<tr>";
	   print "<td> $pairsperl[0] </td>";
	   print "<td> $pairsperl[1] </td>";
	   print "<tr>";
	}
   } #End traversal of output from program
   print "</table>";	
} # end of "callPERL" subroutine

# Begin subroutine "callCPP"
# callCPP takes the passed parameters, the search method and the 
# string containing an ID, name or event, and runs the C++ program.
# The C++ program returns the matched information, then the function 
# formats the returned strings (in an array) into an HTML table.  The
# event information is hidden until the user clicks to display.  Here I used
# a checkbox, the method of which was learned from w3schools.com.  This site 
# provides great tutorials on web development (or atleast the 
# HTML/CSS/JAVASCRIPT stuff was pretty efficent).

sub callCPP {
   # Get passed parameters
   my $EVENTfirst = 0;
   my @form = @_;
   my $SEARCHMETHOD = $form[0];
   my $IDNAME = $form[1];

   # Announce which program is ran
   print "<p>Patient data using C++\n</p>";

   # Store the output from running the c++ program
   my @returned = `./cs216program5 $SEARCHMETHOD $IDNAME`;
   
   #Begin HTML table
   print "<table border=\"1\">";
   print "<tr>";

   # Header for output table
   if($SEARCHMETHOD eq "1") {
	print "<th>Patient number searched for: </th>"; }
   if($SEARCHMETHOD eq "2") {
	print "<th>Patient name searched for: </th>"; }
   if($SEARCHMETHOD eq "3") {
	print "<th>Event searched for: </th>"; }
   # End header 
   #
   print "<th> $IDNAME</th>";   
   print "</tr>";

   # Separate output for table format
   foreach(@returned) {
	my @pairs = split('&', $_);
	chomp $pairs[0];
        
   # If the current pair is an Event ID
	if($pairs[0] =~ /Event ID\(s\):/) {
        # If the search method is by name, don't display event info
                  if($SEARCHMETHOD eq "2") { 
		     last; }
                                                           
	   	print "<tr></table>";
	# If this Event is the first
		if($EVENTfirst == 0) {
		# Script stuff for the hidden info
		# Again, this is javascript which toggles the show/hide
		# value of the presented checkbox.
	  		print "<script type=\"text/javascript\">";
	   		print "function showMe (it, box) {";
	   		print "var vis = (box.checked) ? \"block\" : \"none\"\;";
	   		print "document.getElementById(it).style.display = vis; }";
	   		print "</script>";
		# End script stuff for the hidden info
	
		# Show/Hide checkbox
	   		print "Show Event ID: <input type=\"checkbox\"";
	   		print "name=\"multi_note\" value=\"Show Event ID\"";
	   		print "onclick=\"showMe('div1', this)\">";
	   		print "<div id=\"div1\" style=\"display: none\;\">";
		# End Show/Hide checkbox

			$EVENTfirst = 1;
		}
		# Begin Table
	   	print "<table border=\"1\" ><tr>";
		# Call program, store output
	   	my @Event = `./cs216program5 3 $pairs[1]`;
	   
		# Traverse the output of the c++ program
	  	 foreach(@Event) {
			# Split each pair
			my @eventpairs = split('&', $_);

	  		print "<td> $eventpairs[0] </td>";
			print "<td> $eventpairs[1] </td>";
	  		print "</tr>";
	   	}
	   # Close last table
	   print "</table>";
	   # End Table
	print "</table>";
	}
	
	# If the pair is NOT an event
	# Regular output
	else {
	   print "<tr>";
	   print "<td> $pairs[0] </td>";
	   print "<td> $pairs[1] </td>";
	   print "<tr>";
	}
	# End regular output
   }
   print "</table>";	
} # End traversal of output
# End subroutine "callCPP"

# More HTML, closing any leftover table tags, etc. 
# close 3, incase I messed up somewhere severely.
print "</table></table></table>";
print "</div>";
print "</form>\n </body>";

# Add footer which contains "Return to Main Menu" button
print "<footer>";
print "<form>\n";
print "<input align=left type=\"button\" value=\"Return to main menu\"";
print " onclick=\"window.location.href='./cs216program5.html'\"> </form>";

print "</p></footer></center>\n";
print "</html>"
# End CGI program
