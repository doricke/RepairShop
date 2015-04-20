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

main: 
{
  my %names;

  # Zero out values for each input file.
  my @list;
  foreach my $i ( 0 .. 63 )
  {
    $list[ $i ] = 0;
  }  # foreach
  my $zeros = join ( "\t", @list );

  # Read in the Names and Identifiers for this chip.
  # open IDS, "A.ids";
  open IDS, "B.ids";
  $_ = <IDS>;
  chomp;
  my $header = $_;
  while ( <IDS> )
  {
    chomp;
    my ($name, $id) = split (/\t/);
    $names{$id} = $name . "\t" . $id . "\t" . $zeros;
  }  # while
  close IDS;

  # Read in one file name at a time.
  my $index = 0;
  while ( <> )
  {
    chomp;
    my $filename = $_;
    print "<- $filename\n";
    s/.txt//;			# strip ".txt" from file name
    my $line = $_;
    $header .= "\tCy5_" . $line . "\tCy3_" . $line;

    open FILE, "<$filename";
    my $head = <FILE>;		# file header line.

    while ( <FILE> )
    {
      chomp;
      my ($name, $id, $cy5, $cy3) = split (/\t/);

      my @values = split ( /\t/, $names{$id} );
      $values[$index*2+2] = $cy5;
      $values[$index*2+3] = $cy3;
      $names{$id} = join ( "\t", @values );
    }  # while
    close FILE;
    $index++;
  }  # while

  open OUT, ">B_merge.txt";
  print OUT ( $header . "\n" );
  foreach my $id ( keys %names )
  {
    print OUT ( $names{$id} . "\n" );
  }  # foreach
  close OUT;

}  # main

1
