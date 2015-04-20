#!/usr/local/bin/perl

@input = <STDIN>;
chop ( @input );
$string = join ( " ", @input );
print ( "$string\n" );
