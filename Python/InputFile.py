#
#
# This class provides an object model for an input text file.
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

class InputFile:

  #
  # Create an input text file object for the named file.
  #
  def init ( self ):
    self.name = ""			# File name
    self.end_of_file = 0		# Not end of file
    self.line = ""			# Current line
    self.file = None			# Input file

  def __init__ ( self ):
    self.init ()			# initialize

  #
  # Open the input text file.
  #
  def open ( self ):
    try:
      self.file = open ( self.name, "r" )	# open the file for reading
    except:
      print "There is no file named ", self.file

  #
  # Close the input text file.
  #
  def close ( self ):
    self.file.close ()			# close the input file

  #
  # Get the next line from the input text file.
  #
  def getLine ( self ):
    return self.line			# return the current line

  #
  # Get the name of the input text file.
  #
  def getName ( self ):
    return self.name

  #
  # Get the end-of-file status of the input text file.
  #
  def isEndOfFile ( self ):
    return self.end_of_file		# return end of file flag

  #
  # Set the name of the input text file.
  #
  def setName ( self, name ):
    self.name = name

  #
  # Get the next text line from the input text file.
  #
  def next ( self ):
    self.line = self.file.readline ()	# read in the next line
    if ( self.line == "" ):
      self.end_of_file = 1
    return self.line			# return the current line

#
# Testing module.
#
if ( __name__ == "__main__" ):
  print "Start-----------------------------------------"
  test = InputFile ()
  test.setName ( "test.data" )
  test.open ()
  while ( test.isEndOfFile () == 0 ):
    text = test.next ()
    if ( text != "" ):
      print text,
  print "End-------------------------------------------"
  test.close ()
