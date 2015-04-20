#!/usr/bin/perl -w

  use strict;

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


# Input a list of file names to scan for huge files.

# Main program:
main:
{
  # initialization:
  my $max_lines = 100000;		# maximum number of lines per file
  my $temp_name = "TeMp";		# standard temporary file name

  my $file_name;			# name of current file
  my $line;				# current line of current file
  my $line_count;			# line counter
  my $status;				# system call status

  while ( <STDIN> )
  {
    chomp;

    $file_name = $_;			# next file name from the list

    open TEMP, ">$temp_name.$file_name";	# temporary copy of first $max_lines

    open FILE, "<$file_name";		# file being checked

    print "Processing $file_name\n";

    $line_count = 0;			# line counter

    # Read the file, count the lines, copy first $max_lines to TEMP.
    while ( $line = <FILE> )
    {
      if ( $line_count < $max_lines )
      {
        $line_count++; 
        print TEMP "$line"; 

        if ( $line =~ /Query: 0 / )
        {
          $line_count = $max_lines + 1;
        }  # if
      }  # if
    }  # while

    close FILE;
    close TEMP;

    # Check if too few lines are in the input file.
    if ( $line_count >= $max_lines )
    {
      $status = system ( "mv $temp_name.$file_name $file_name" );

      if ( $status != 0 )
      {
        print "\tstatus = $status\n";
      }  # if
    }  # if
  }  # while

}  # main program

