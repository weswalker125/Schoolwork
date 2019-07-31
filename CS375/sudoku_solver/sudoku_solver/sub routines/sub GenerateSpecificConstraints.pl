sub GenerateSpecificConstraints {
	$specificConstraints = "";
		
		#read in file
		open(DESIGNERCLUES, "<$designerFile");
		@lines = <DESIGNERCLUES>;
		close(DESIGNERCLUES);
		
		foreach $line (@lines)
		{
			$row = substr $line, 6, 1;
			$column = substr $line, 8, 1;
			$value = substr $line, 10, 1;

			$specificConstraints .= &ToDimacs($row, $column, $value) . " 0\n";
		}
		
		#return the dimacs constraints
		return $specificConstraints;
}
			