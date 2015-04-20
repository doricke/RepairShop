
# This class 
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

import string
from InputFile import InputFile;
from FastaSequence import FastaSequence;


class FastaIterator ( InputFile ):

  def __init__ ( self, name = "" ):
    self.init ();		# initialize
    if ( name != "" ):
      self.setName ( name )
      self.open ()
    self.fasta = None

  def nextHeader ( self ):
    # Find the start of the next sequence.
    while ( ( self.isEndOfFile () == 0 ) and \
            ( self.line != "" ) and \
            ( self.line [ 0 ] != '>' ) ):
      self.next ()
    return self.line

  def nextSequence ( self ):
    self.nextHeader ()

    # Parse the header line.
    self.fasta = FastaSequence ()
    self.fasta.parseHeader ( self.line )

    # Read in the sequence data.
    self.next ()
    seq = ""
    while ( ( self.isEndOfFile () == 0 ) and \
            ( self.line != "" ) and
            ( self.line [ 0 ] != '>' ) ):
      seq = seq + string.rstrip ( self.line )
      self.next ()
    self.fasta.setSequence ( seq )
    return self.fasta
    


print "Start-----------------------------------------"
test = FastaIterator ()
test.setName ( "test.data" )
test.open ()
while ( test.isEndOfFile () == 0 ):
  fasta = test.nextSequence ()
  if ( fasta.sequence != "" ):
    print fasta.getName (), fasta.getDescription ()
    print fasta.getSequence ()
print "End-------------------------------------------"
test.close ()
