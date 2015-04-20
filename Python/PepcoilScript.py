
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
import sys
from InputFile import InputFile;


class HmmgeneScript ( InputFile ):

  def __init__ ( self, name = "" ):
    self.init ();		# initialize
    if ( name != "" ):
      self.setName ( name )
      self.open ()

  def generate ( self ):
    print "mkdir pepcoil"

    # Process the list of file names.
    while ( self.isEndOfFile () == 0 ):
      self.next ()
      if ( self.isEndOfFile () == 0 ):
        self.name = string.rstrip ( self.line )
        self.command = "pepcoil -sequence " + self.name + " -window 28 -outfile pepcoil/" + self.name + ".pepcoil"
        print self.command

# Main program
infile = "in"
if ( sys.argv >= 2 ):
  infile = sys.argv [ 1 ]
test = HmmgeneScript ( infile )
test.generate ()
test.close ()
