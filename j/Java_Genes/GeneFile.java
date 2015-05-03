

// import LineTools;
// import OutputFile;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2002 Darrell O. Ricke, Ph.D., Paragon Software
  License::   	GNU GPL license  (http://www.gnu.org/licenses/gpl.html)
  Contact::   	Paragon Software, 1314 Viking Blvd., Cedar, MN 55011
 
              	This program is free software; you can redistribute it and/or modify
              	it under the terms of the GNU General Public License as published by
              	the Free Software Foundation; either version 2 of the License, or
              	(at your option) any later version.
          
              	This program is distributed in the hope that it will be useful,
              	but WITHOUT ANY WARRANTY; without even the implied warranty of
              	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
              	GNU General Public License for more details.
 
                You should have received a copy of the GNU General Public License
                along with this program. If not, see <http://www.gnu.org/licenses/>.
*/


/******************************************************************************/
/**
  This class provides an object model for an input text file.
  @author	Darrell O. Ricke, Ph.D.
*/
public class GeneFile extends InputFile
{


/******************************************************************************/
/** This constructor creates and initializes a new GeneFile object. */
public GeneFile ()
{
}  // constructor GeneFile


/******************************************************************************/
  public void selectFields ( String line, OutputFile out )
  {
    // Select only lines that have content in the first column.
    if ( line.charAt ( 0 ) != '\t' )

        out.println ( line );
  }  // method selectFields


/******************************************************************************/
/** An object test method that illustrates the use of the GeneFile object. */
  public static void main ( String [] args )
  {
    GeneFile app = new GeneFile ();

    app.setFileName ( args [ 0 ] );

    // Open the file.
    app.openFile ();

    // Open the output file.
    OutputFile out = new OutputFile ();
    out.setFileName ( args [ 0 ] + "2" );
    out.openFile ();

    String line;

    // Copy the file to output.
    while ( app.isEndOfFile () == false )
    {
      line = app.nextLine ().toString ();

      if ( line.length () > 0 )

        // Select the data fields of interest.
        app.selectFields ( line, out );
    }  // file

    app.closeFile ();
    out.closeFile ();
  }  // method main

}  // class GeneFile

