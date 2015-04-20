#!/usr/sbin/perl -w

  use strict;

# This program checks for failed output files.
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

  my %list;		# hash of source files & output file size

  # Read in the contents of the input file as the list of source names.
  while (<>)
  {
    chomp;
    $list{$_} = 0;		# Add $_ name to the hash list - set size to zero
  }  # while

  my $count = keys %list;

  print "There are $count files in the list!\n";

  # Parse the contents of the current directory checking the local files.
  my $sum_sizes = 0;
  foreach my $file (glob "*")
  {
    $_ = $file;
    if ( /(.motifs$)|(.pfam$)/ )
    {
      $list{$`} = -s $file;
      $sum_sizes += $list{$`};
      # print "$file size is $list{$`}, name is $`\n";
    }  # if
  }  # foreach

  my $average = 0;
  $average = $sum_sizes / $count if ( $count > 0 );
  printf "\nThe average output size is %d bytes.\n", $average;

  # Create a Summary output file for any small output files.
  my $min_size = $average / 10;

  # Average nothing found for Motifs is 214.
  # if ( $min_size > 190 )  { $min_size = 190; };
  $min_size = 190 if ( $min_size > 190 );

  my $short = 0;
  foreach my $key (keys %list)
  {
    if ( $list{$key} < $min_size )
    {
      print "Small file $key, size = $list{$key}\n";
      $short++;
    }  # if
  }  #foreach


  # Create an output file of the sequences to be repeated.
  if ( $short > 0 )
  {
    print "There were $short small files found!\n";

    open REDO, ">redo.list";

    foreach my $key (keys %list)
    {
      if ( $list{$key} < $min_size )
      {
        print REDO "$list{$key}\n";
      }  # if
    }  #foreach

    close REDO;
  }  # if
  else
  {
    print "\nNothing to repeat!\n";
  }  # else

