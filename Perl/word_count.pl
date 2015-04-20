#!/usr/local/bin/perl

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

$searchword = $ARGV[0];
print ( "Word to search for: $searchword\n" );
shift ( @ARGV );

$totalwordcount = $wordcount = 0;
$filename = $ARGV[0];

while ( $line = <> )
{
  chop ( $line );
  @words = split ( / /, $line );
  $w = 1;
  while ( $w <= @words )
  {
    if ( $words [ $w - 1 ] eq $searchword )
    {
      $wordcount += 1;
    }
    $w++;
  }

  if ( eof )
  {  
    print ( "occurrences in file $filename: " );
    print ( "$wordcount\n" );
    $wordcount = 0;
    $filename = $ARGV[0];
    $totalwordcount += $wordcount;
  }
}

print ( "total number of occurrences: $totalwordcount\n" );
