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

  my @fields;		# fields of current line

  # Read in the contents of the input file as the list of source names.
  while (<>)
  {
    chomp;

    @fields = split ( /\t/, $_ );	# split lines on tabs

    $_ = $fields[4];
    if ( defined $_ )
    {
      if ( /([A-Z][a-z][a-z])\1{2,}/ )
      {
        print "($1)n\t$fields[2]\n";
      }  # if
      elsif ( /([A-Z][a-z][a-z][A-Z][a-z][a-z])\1{1,}/ )
      {
        print "($1)n\t$fields[2]\n";
      }  # elsif
      elsif ( /([A-Z][a-z][a-z][A-Z][a-z][a-z][A-Z][a-z][a-z])\1{1,}/ )
      {
        print "($1)n\t$fields[2]\n";
      }  # elsif
      else
      {
        # Couldn't recogize the pattern.
        print "$fields[4]\t$fields[2]\n";
      }  # else
    }  # if
    
  }  # while

