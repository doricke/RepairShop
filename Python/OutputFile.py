
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

class OutputFile:

  def init ( self ):
    self.name = ""			# File name
    self.file = None			# Output file
    self.lines = 0			# Lines written

  def __init__ ( self, name = "" ):
    self.init ()			# initialize
    self.setName ( name )
    self.open ()

  def open ( self ):
    if ( self.name == "" ):
      print "No file name has been specified."
      return
    try:
      self.file = open ( self.name, "w" )	# open the file for writing
    except:
      print "Open failure on file named ", self.file

  def close ( self ):
    self.file.close ()			# close the input file

  def delete ( self ):
    print "The delete file function has not been implemented yet."

  def getName ( self ):
    return self.name

  def setName ( self, name ):
    if ( name == "" ):  return
    self.name = str ( name )

  def write ( self, text ):
    self.file.write ( text )
    self.lines = self.lines + 1


if ( __name__ == "__main__" ):
  test = OutputFile ( "test.out" )
  for i in range ( 10 ):
    test.write ( "Line " + str ( i ) + "\n" )
  test.close ()

