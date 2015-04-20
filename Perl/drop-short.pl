#!/usr/bin/perl -w

use strict;
# use warnings;

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
  # Read in sequences one sequence at a time.
  while ( <> )
  {
    chomp;

    # Split record into (name\tlength) fields.
    my ($name, $length) = split;

    # Delete short sequences.
    if ( ( $length < 5000 ) && ( length ( $name ) > 0 ) )
    {
      my $status = `rm $name`;

      print "$name ($length): $status\n";
    }  # if
  }  # while
}  # main
