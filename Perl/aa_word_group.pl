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

  my $current;				# current word

  my %empty;				# empty hash

  my @fields;				# sub fields of current line

  my @lines;				# common word lines

  my %names;				# file names of current common word

  my $new_line;				# restructured input line


# Main program:
main:
{
  # initialization:
  @lines = ();
  $current = "";

  while ( <> )
  {
    chomp;

    @fields = split ( /\t/, $_ );	# split lines on tabs

    # Reorder the line placing the peptide word last.
    $new_line = $fields[1] . "\t" . $fields[2] . "\t" . $fields [0];

    # Decide if new word is the same as the current word
    if ( $fields[0] ne $current )
    {
      # Write the previous word to the files.
      foreach my $key ( keys ( %names ) )
      {
        # Write each of the lines to each of the file names.
        foreach my $line ( @lines )
        {
          print "echo \"$line\" >> $key\n";
        }  # foreach
      }  # foreach

      # Reset.
      %names = %empty;
      @lines = ();
      $current = $fields[0];
    }  # if

    # Add the file name.
    $names{ $fields[1] } = 1;

    # Add the new line.
    push ( @lines, $new_line );
  }  # while

  # Write out the last word.
  foreach my $key ( keys ( %names ) )
  {
    # Write each of the lines to each of the file names.
    foreach my $line ( @lines )
    {
      print "echo \"$line\" >> $key\n";
    }  # foreach
  }  # foreach

}  # main program


