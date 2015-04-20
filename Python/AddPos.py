
import string

from InputFile import InputFile


class HeadersToMap:

  def init ( self ):
    self.name = ""		# name

  def __init__ ( self ):
    self.init ()		# initialize

# get methods:

# set methods:

# other methods:

  def parseHeader ( self, line ):

    # Assert: valid line.
    if ( line == "" ):  return
 
    index = string.find ( line, "Mkr:" )
    if ( index == -1 ):  return

    line_left = line [ 0:index ]
    line_right = line [ index: ]

    tab_index = string.find ( line_right, "\t" )

    print line_left + line_right [ :tab_index ] + "\tPos:" + line_right [ tab_index+1: ],


if ( __name__ == "__main__" ):

  app = HeadersToMap ()

  in_file = InputFile ()
  in_file.setName ( "q" )
  in_file.open ()

  while ( in_file.isEndOfFile () == 0 ):
    line = in_file.next ()
    if ( line != "" ):
      app.parseHeader ( line )

  in_file.close () 
