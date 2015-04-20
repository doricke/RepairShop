#!/usr/bin/perl -w

use strict;

# Main program:
main:
{
  # initialization:
  my $line_count = 0;			# number of lines in the input
  my $max_lines = 100000;		# maximum number of lines allowed

  while ( <> )
  {
    if ( $line_count < $max_lines )
    {
      $line_count++; 
      print "$_"; 
    }  # if
  }  # while
}  # main program


