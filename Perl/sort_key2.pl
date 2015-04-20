#!/usr/bin/perl -w

  use strict;

# This program reads in a tab delimited file and write out echo commands to append
# each line to the filename specified in the second column.

#
# Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
# Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
# License::   	GNU GPL license  (http://www.gnu.org/licenses/gpl.html)
# Contact::   	Paragon Software, 1314 Viking Blvd., Cedar, MN 55011
#
#             	This program is free software; you can redistribute it and/or modify
#             	it under the terms of the GNU General Public License as published by
#             	the Free Software Foundation; either version 2 of the License, or
#             	(at your option) any later version.
#         
#             	This program is distributed in the hope that it will be useful,
#             	but WITHOUT ANY WARRANTY; without even the implied warranty of
#             	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#             	GNU General Public License for more details.
#
#               You should have received a copy of the GNU General Public License
#               along with this program. If not, see <http://www.gnu.org/licenses/>.

  my $current;				# current sequence name

  my %empty;				# empty hash

  my @fields;				# sub fields of current line

  my %words;				# peptide words and positions


# Main program:
main:
{
  # initialization:
  $current = "";

  while ( <STDIN> )
  {
    chomp;

    @fields = split ( /\t/, $_ );	# split lines on tabs

    # Decide if new word is the same as the current word
    if ( $fields[0] ne $current )
    {
      # Write the previous word to the files.
      if ( keys ( %words ) > 4 )
      {
        foreach my $key ( sort by_number ( keys ( %words ) ) )
        {
          print "$current\t$key\t$words{$key}\n";
        }  # foreach
      }  # if

      # Reset.
      %words = %empty;
      $current = $fields[0];
    }  # if

    # Assign the peptide to the position.
    if ( defined ( $fields[1] ) && defined ( $fields[2] ) ) 
    {
      $words{ $fields[1] } = $fields[2];
    }  # if

  }  # while

  # Write out the last sequence.
  if ( keys ( %words ) > 4 )
  {
    foreach my $key ( sort by_number ( keys ( %words ) ) )
    {
      print "$current\t$key\t$words{$key}\n";
    }  # foreach
  }  # if
}  # main program


# This subroutine is used for sorting numerically.
sub by_number
{
  if ( defined ( $a ) && defined ( $b ) )
  {
    $a <=> $b;
  }  # if
  else
  {
    return 0;
  }  # else
}  # subroutine by_number

