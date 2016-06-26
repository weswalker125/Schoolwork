#!/usr/local/bin/perl -w
# perlecho.pl
# Perl program to echo back to caller (via print
# statements) the parameters passed to it
# Date: October 2008
# Author: Paul Piwowarski
#
# Preconditions
#		Executed via the command line, parameters
#		passed to it when it is executed
# Postconditions
#		Parameters passed are printed. If executed
#		via Perl program:
#			@returned = `perlecho.pl P1 P2....`
#		the parameters printed are in @returned
#
use strict;
my $parmcount = @ARGV; # number of parms passed
my $index;			   # parm index
# Print each of the received parameters
for ($index = 0; $index < $parmcount;$index++) {
	print "$ARGV[$index]\n";
} 