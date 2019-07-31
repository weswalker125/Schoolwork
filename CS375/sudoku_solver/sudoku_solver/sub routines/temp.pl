#!usr/bin/perl -W

$result = &Uniqueness($ARGV[0], $ARGV[1], "4");
print "$result\n";

sub Uniqueness {
	($dimacsFile, $solutionFile, $problemNumber) = @_;
	
	#get list of Designer clues
	open(DIMAC, "<$dimacsFile");
	@constraints = <DIMAC>;
	close(DIMAC);
	
	#extract lines with one value (and a 0)
	#store into @designerClues
	foreach $line (@constraints) 
	{
		@tokens = split(' ', $line);
		if(scalar(@tokens) == 2) {
			push(@designerClues, $tokens[0]);
		}
	}
	
	#Get solution list
	open(SOLUTION, "<$solutionFile");
	@results = <SOLUTION>;
	close(SOLUTION);
	
	#extract all positive values from solution
	#store into @solution
	foreach $line (@results)
	{
		@tokens = split(' ', $line);
		foreach $token (@tokens)
		{
			if("-" ne (substr $token, 0, 1)) {
				push(@solution, $token);}
		}
	}
	
	open(LOL, ">lol.txt");
	print LOL "solutions:\n @solution\n";
	print LOL "designers:\n @designerClues";
	close(LOL);
	
	
	foreach $value (@solution) 
	{
		if(chomp($value) eq "SAT" || chomp($value) eq "UNSAT") {
			next;}
		if(grep {$_ eq $value} @designerClues) {
			next;}

		else {
			#add line to original dimac file
			open(DIMAC2, ">>$dimacsFile");
			print DIMAC2 "-$value";
			close(DIMAC2);
			print "Checking -$value";
			
			#run through MiniSAT
			$outputFile = "tmpSATsolution.txt";
			system("./MiniSAT $dimacsFile $outputFile");
			
			#get result:
			$result = &ReadSATresult($outputFile);
			
			#if satisfiable, type II flaw
			if($result eq "TRUE") {
				return "Problem $problemNumber has a Type-II flaw\n";
				exit;
			}
			
			#if unsatisfiable, remove last line of dimacs file, retry
			$dimacs2 = $dimacsFile . "2";
			system("sed '$' $dimacsFile > $dimacs2");
			system("mv $dimacs2 $dimacsFile");
			
			#keep looping
		}
	}
}

sub ReadSATresult {
	($resultFile) = @_;
	
	#read in file
		open(RESULTS, "<$resultFile");
		$firstLine = <RESULTS>;
		close(RESULTS);
	chomp($firstLine);

	if((lc($firstLine)) eq "sat") {
		$result = "TRUE";}
	elsif(lc($firstLine) eq "unsat") {
		$result = "FALSE";}
	else {
		return "N/A"; }
		
	return $result;
}