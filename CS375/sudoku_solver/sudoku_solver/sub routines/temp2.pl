#!usr/bin/perl -W

($fileName) = $ARGV[0];
	
($newLine) = $ARGV[1];
	
	#read in file
	open(FILE, "<$fileName");
	@lines = <FILE>;
	close(FILE);
	
	#write out:
	open(FILE2, ">$fileName");
	#add one to header:
	$header = $lines[0];
	@headerElements = split(' ', $header);
	$headerElements[3]++;
	print FILE2 "@headerElements\n";
	for($ct=1; $ct < (scalar(@lines)); $ct++)
	{
		print FILE2 "$lines[$ct]";
	}
	print FILE2 "$newLine";
	
	close(FILE2);