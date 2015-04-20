
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

class FastaSequence:

  def init ( self ):
    self.name = ""			# Sequence name
    self.description = ""		# Sequence description
    self.sequence = ""			# Sequence
    self.type = ""			# Sequence type

  def __init__ ( self ):
    self.init ()			# initialize

  def getDescription ( self ):
    return self.description

  def getName ( self ):
    return self.name

  def getSequence ( self ):
    return self.sequence

  def getType ( self ):
    return self.type

  def setDescription ( self, value ):
    self.description = str ( value )

  def setName ( self, name ):
    self.name = str ( name )

  def setSequence ( self, value ):
    self.sequence = str ( value )

  def setType ( self, value ):
    self.type = str ( value )

  def parseHeader ( self, line ):
    self.name = ""
    self.description = ""
    if ( line == "" ):  return
    space = string.find ( line, " " )
    if ( space == -1 ):
      if ( len ( line ) > 1 ):
        self.name = line [ 1: ]
      return
    self.name = line [ 1:space ]
    if ( len ( line ) > space + 1 ):
      self.description = string.rstrip ( line [ space+1: ] )


if ( __name__ == "__main__" ):
  test = FastaSequence ()
  test.setName ( "test.data" )
  test.setDescription ( "This is a test" )
  test.setType ( "DNA" )
  test.setSequence ( "AAAACCCGGT" )
  
  print ">" + test.getName (), test.getDescription ()
  print test.getSequence ()

