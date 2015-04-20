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
  my %qN_sum;
  my %N_sum;
  my %count;

  # Read in the qN fields.
  # species1	species2	gene	V	qN	N
  while ( <> )
  {
    chomp;
    my @fields = split ();

    if ( defined ( $fields[0] ) )
    {
      my $index = $fields[0] . ':' . $fields[1];
      my $index2 = $fields[1] . ':' . $fields[0];

      if ( ( $fields[5] < 1000 ) && ( $fields[4] > 0.1 ) )
      {
        $qN_sum{$index} += $fields[4];
        $N_sum{$index} += $fields[5];
        $count{$index}++;

        $qN_sum{$index2} += $fields[4];
        $N_sum{$index2} += $fields[5];
        $count{$index2}++;
      }  # if
    }  # if
  }  # while

  # Compute the averages.
  open AVEQN, ">ave.q_N";
  my $previous;
  foreach my $key ( sort ( keys ( %count ) ) )
  {
    my @fields = split ( /:/, $key );
    if ( $fields[0] ne $previous )
    {
      if ( defined ( $previous ) )
      {
        print AVEQN "\n";
      }  # if
      $previous = $fields[0];
    }  # if

    if ( $count{$key} >= 1 )
    {
      my $ave_qN = $qN_sum{$key} / $count{$key};
      my $ave_N  = $N_sum{$key} / $count{$key};

      my $format = "%s\t%6.3f\t%d\t%d\n";
      printf AVEQN ( $format, ($key, $ave_qN, $ave_N, $count{$key}) );
    }  # if
  }  # foreach
  close AVEQN;

  # Compute the closest pairs.
  my %species;
  my %species_count;
  my %distance;
  foreach my $key ( sort ( keys ( %count ) ) )
  {
    my @names = split ( /:/, $key );

    if ( $count{$key} >= 1 )
    {
      my $ave_N  = $N_sum{$key} / $count{$key};

      if ( defined ( $species{$names[0]} ) )
      {
        if ( $ave_N < $distance{$names[0]} )
        {
          $species{$names[0]} = $names[1];
          $species_count{$names[0]}++;
          $distance{$names[0]} = $ave_N;
        }  # if
      }  # if
      else
      {
        $species{$names[0]} = $names[1];
        $species_count{$names[0]}++;
        $distance{$names[0]} = $ave_N;
      }  # else

      if ( defined ( $species{$names[1]} ) )
      {
        if ( $ave_N < $distance{$names[1]} )
        {
          $species{$names[1]} = $names[0];
          $species_count{$names[1]}++;
          $distance{$names[1]} = $ave_N;
        }  # if
      }  # if
      else
      {
        $species{$names[1]} = $names[0];
        $species_count{$names[1]}++;
        $distance{$names[1]} = $ave_N;
      }  # else
    }  # if
  }  # foreach

  # Print out the closest pairs.
  foreach my $key ( sort ( keys ( %species ) ) )
  {
    print "$key\t$species{$key}\t$distance{$key}\t$species_count{$key}\n";
  }  # foreach

}  # main

