
import java.io.*;
import java.util.Vector;

// import InputTools;
// import Sequence;
// import SequenceAlignment;

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

public class MSF extends Object
{

/******************************************************************************/

private SequenceAlignment alignments = null;	// individual alignments

private InputTools msf_file = new InputTools ();	// GCG .msf input file


/******************************************************************************/
public MSF ()
{
  initialize ();
}  // constructor MSF


/******************************************************************************/
private void initialize ()
{
  msf_file.initialize ();
}  // method initialize


/******************************************************************************/
private Vector readNames ()
{
  String line = "";
  Vector names = new Vector ();

  do
  {
    line = msf_file.getLine ().toString ();

    // Check for a name line.
    if ( line.indexOf ( " Name:" ) == 0 )
    {
      int index = line.indexOf ( "Len:" );
      if ( index == -1 )  index = line.length () - 1;
      String name = line.substring ( 7, index ).trim ();

      System.out.println ( "Name: '" + name + "' found." );
      names.add ( name );
    }  // if
  }
  while ( ( line.indexOf ( "//" ) != 0 ) ||
          ( msf_file.isEndOfFile () == true ) );

  return names;
}  // method readNames


/******************************************************************************/
public void processFile ( String msf_name )
{
  StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

  // Open the GCG .msf file.
  msf_file.setFileName ( msf_name );
  msf_file.openFile ();

  // Read in the sequence names.
  Vector names = readNames ();

  // Read in the alignments.
  // readAlignments ();

  msf_file.closeFile ();
  msf_file = null;

  // Compare the sequences.
  // compareSequences ();

}  // method processFile



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java MSF <GCG .msf file>" );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    MSF app = new MSF ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

}  // class MSF

