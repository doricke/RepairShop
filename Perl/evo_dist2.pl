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

  my $fraction_spacers = 0.5;
  # for ( my $fraction_spacers = 0.1; $fraction_spacers <= 1.0; $fraction_spacers += 0.1 )

  # my $years = 70;
  for ( my $years = 10; $years <= 100; $years += 10 )
  {
    # print "spacers $fraction_spacers\t";

    print "years $years\t";

    for ( my $q = 0.990; $q <= 0.999; $q += 0.001 )
    {
      my $percent = &identity ( $fraction_spacers, $q, $years ) % 100;

      print "$percent\t";
    }  # for

    print "\n";
  }  # for

  print "\nEnd of program\n";


sub identity ()
{
  my ($fraction_spacers, $q, $years) = @_;

  return ( (1.0 - $fraction_spacers) + ($q ** (2.0 * $years)) * $fraction_spacers ) * 100.0;
}  # subroutine identity

