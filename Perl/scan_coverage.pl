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
  my $lengths_file = "hgenes.lengths";

  # Read in the sequence lengths.
  my %lengths;
  open LEN, "<$lengths_file";
  my $line;
  while ( <LEN> )
  {
    chomp;
    my @fields = split;
    $lengths{$fields[0]} = $fields[1];
  }  # while
  close LEN;

  open MV, ">mv.cmds";

  use ScanFeatures;

  my $result = new ScanFeatures ( \*STDIN );

  my $current_seq_name;		# current sequence name
  my @coverage;
  do
  {
    $line = $result->next ();
    # next if ( ! defined $line );

    # Check for the first sequence name.
    if ( ! defined $current_seq_name )
    {
      # Set the current sequence name to the first sequence name.
      $current_seq_name = $result->sequence_name ();

      if ( defined $lengths{$current_seq_name} )
      {
        foreach my $base ( 0 .. $lengths{$current_seq_name} )
        {
          $coverage [ $base ] = 0;
        }  # foreach
      }  # if
    }  # if

    # Check for a new sequence name.
    if ( ( $current_seq_name ne $result->sequence_name () ) ||
         ( ! defined $line ) )
    {
      my $count = 0;
      foreach my $base ( 1 .. $#coverage )
      {
        if ( $coverage [ $base ] == 1 )
        {
          $count++;
        }  # if

        # Initialize coverage.
        $coverage [ $base ] = 0;
      }  # foreach

      my $percent = 0;
      if ( defined $lengths{$current_seq_name} )
      {
        $percent = ( $count * 100 ) / $lengths{$current_seq_name};
      }  # if

      print "$current_seq_name\t$percent\n";

      if ( $percent > 74 )
      {
        print MV "mv $current_seq_name hgenes\n";
      }  # if
      elsif ( $percent > 0 )
      {
        print MV "mv $current_seq_name mgenes\n";
      }  # elsif
      else
      {
        print MV "mv $current_seq_name lgenes\n";
      }  # else

      # Update the current sequence name.
      $current_seq_name = $result->sequence_name ();

      if ( defined $lengths{$current_seq_name} )
      {
        foreach my $base ( 0 .. $lengths{$current_seq_name} )
        {
          $coverage [ $base ] = 0;
        }  # foreach
      }  # if
    }  # if

    # Tally the bases covered by the current similarity.
    foreach my $base ( $result->query_start () .. $result->query_end () )
    {
      $coverage [ $base ] = 1;
    }  # foreach
  }
  while ( defined $line );

  close MV;
}  # main

