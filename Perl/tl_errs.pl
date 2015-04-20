#!/usr/bin/perl -w

  use strict;

# Input a list of file names to scan for failures.
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

  my $file_name;			# current file name

  my $line_count;			# number of lines in the input file

  my $line;				# current input line


# Main program:
main:
{
  # initialization:

  # Check the first line of the 

  while ( <STDIN> )
  {
    chomp;

    $file_name = $_;

    open FILE, "<$file_name";

    $line_count = 0;

    # Get the first line of the text file.
    $line = <FILE>;
    if ( $line =~ /Error=/ )
    {
      print "$file_name\t $line\n";
    }  # if
    else
    {
      while ( <FILE> )
      {
        chomp;
 
        $line_count++; 
      }  # while

      # Check if too few lines are in the input file.
      if ( $line_count < 50 )
      {
        print "$file_name has only $line_count lines: $line.\n";
      }  # if
    }  # else
  }  # while

}  # main program


