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
  my $accessions_file = shift ( @ARGV );

  # print "Taxonomy file = $accessions_file\n";

  # Read in the accession numbers from the Taxonomy file.
  my %ignore;
  open ACC, "<$accessions_file";
  my $line;
  while ( <ACC> )
  {
    chomp;
    my @fields = split;
    $ignore{$fields[0]} = $fields[1];
  }  # while
  close ACC;

  open DROP, ">drop.features";

  use ScanFeatures;

  my $result = new ScanFeatures ( \*STDIN );

  do
  {
    $line = $result->next ();

    if ( defined $line )
    {
      chomp ( $line );

      if ( defined $ignore{$result->accession ()} )
      {
        print DROP $ignore{$result->accession ()} . "\t$line\n";
      }  # if
      else
      {
        print "$line\n";
      } # else
    }  # if
  }
  while ( defined $line );
}  # main
