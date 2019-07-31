sub WriteToFile {
	($fileName, $string) = @_;
	
	open(OUTPUTFILE, ">$fileName");
	print OUTPUTFILE $string;
}