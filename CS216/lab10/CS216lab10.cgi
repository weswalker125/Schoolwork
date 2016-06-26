#!/usr/local/bin/perl
# CS316echo.cgi
# echo program for testing
# echo name/value pairs back to browser
# execute Perl program, pass it parameters that
# are printed (therefore returned)
# Author: Paul Piwowarski 
# Date: October 2008
#		**Edited for lab 10 by Wesley Walker
#
#
# Precondition:
#    Browser executes script CS216echo.cgi via the post method
#    Browser passed  name/value pairs to script
#    
# Postcondition:
#    Name/value pairs sent back to browser two ways:
#    1. the exact string received
#    2. Special characters (%xx where xx = hexadecimal value)
#         converted, then name/value pairs displayed in an
#         unordered list with spaces inserted for "+"
#    3. perlecho.pl executed and passed the parameters
#       These are displayed when returned via print
# Implementation:
#	To execute on multilab:
#    1. Put HTML file in HTML directory under your home dir
#	 2. Action attribute of forms HTML tag:
#	     action="CS216echo.cgi"
#	 3. On multilab, the perl interpreter
#      (first line of cgi script)is in:
#			/usr/local/bin/perl
#    4. Put this cgi script (extension .cgi) in HTML dir
#    7. Directory permissions:
#         HTML   rx for others (do NOT set w)
#    8. File permissions
#       CS216echo.html in HTML r for others (do NOT set wx)
#       CS216echo.cgi  in HTML x for others (do NOT set rw)
#       perlecho.pl    in HTML x for others (do NOT set rw)
#         post method assumed. 
#
 use strict;
 # This is a header line required by HTTP. It tells the 
 # browser what type of data is in the file: 
 # text containing HTML tags
 print "Content-type: text/html\n";  
 # The HTTP protocol requires a blank line before 
 # anything that will be displayed
 print "\n"; 
 # Start the displayed lines <html> tag is always first
 print "<html>";
 # If you want a title on your web page, it goes in the 
 # <head> section
 print "<head><title>Echo N/V pairs for testing</title>";
 print "</head> <body><h1>Query Results</h1>"; 

 my $form = <STDIN>;    # via Post method received in STDIN 
# print "<p> $form ";

my @perlArgs;
my @pairs = split(/&/, $form);
my $numbers = @pairs;

# User pattern matching to get the arguments
for(my $j=0; $j<$numbers; $j++) {
	$pairs[$j] =~ /(.+)=(.+)/;
#	print "<BR>$2</br>";
	$perlArgs[$j] = $2;
}

# what cs216program1 prints returned into @returned
my @returned = `./cs216program1 @perlArgs`;
#print "Number Last name First name Birth date Floor DNA file name";
 for (my $ct=0; $ct<$numbers+1; $ct++) {
	print "<br>$returned[$ct]</br>";
}



 # End the HTML display
 # Browsers are "forgiving" (unlike compilers).If you make a
 # syntax mistake (like not using end tags (i.e. </p>))
 # they will still try to display the page. Also you
 # can use upper or lower case for the tags
 print "</body></html>";




 
