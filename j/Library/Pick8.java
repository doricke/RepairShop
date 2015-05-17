import java.io.*;
import java.awt.*;
import java.util.*;
import Primer.*;

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
public class Pick8 extends Frame
{

/******************************************************************************/

static private Frame aceFrame;

static private String fileName;

static private String sequence;

static private Vector primers;


/******************************************************************************/
public Pick8 ()
{
  primers = new Vector ();
}  /* constructor Pick8 */


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  /* method setFileName */


/******************************************************************************/
public static void init ( )
{
  // Font font = new Font ( "Courier", Font.PLAIN, 12 );
}  /* method init */


/******************************************************************************/
public static void fileControl ( )
{
  MenuBar controlMenuBar = new MenuBar ();
  Menu fileMenu = new Menu ( "File" );
  fileMenu.add ( new MenuItem ( "Open" ) );
  fileMenu.addSeparator ();
  fileMenu.add ( new MenuItem ( "Quit" ) );
  controlMenuBar.add ( fileMenu );
/*
  Menu showMenu = new Menu ( "Show" );
  showMenu.add ( new MenuItem ( "Forward Strand" ) );
  showMenu.add ( new MenuItem ( "Reverse Strand" ) );
  showMenu.add ( new MenuItem ( "Both Strands" ) );
  controlMenuBar.add ( showMenu );
*/
  aceFrame.setMenuBar ( controlMenuBar );
}  /* method fileControl */


/******************************************************************************/
private void openFile ( )
{
  FileDialog dialog = new FileDialog ( aceFrame, "Select a FindPatterns file", FileDialog.LOAD );
  dialog.setDirectory ( "." );
  dialog.setFile ( "*.find" );
  dialog.show ();

  fileName = dialog.getFile ();

}  /* method openFile */


/******************************************************************************/
private String readSequence ( DataInputStream in_file, String name_line )
{
  String line;
  StringBuffer line_buffer;
  String name;
  boolean end_of_data = false;
  String seq = new String ();

  name = name_line.substring ( 4, name_line.length () );

  String fileHandle = fileName.substring ( 0, fileName.indexOf ( '.' ) );
  if ( name.startsWith ( "Contig" ) )  name = fileHandle + "_" + name;

  System.out.println ( "Processing: " + name );

  try 
  {
    PrintStream out_file = new PrintStream ( new FileOutputStream ( name ) );

    while ( end_of_data == false )
    {
      try 
      {
        line = in_file.readLine ();

        if ( line == null )
        {
          end_of_data = true;
        }  /* if */
        else
        {
          line.trim ();			// Remove blanks

          // Remove '*' alignment shift characters from the line.
          line_buffer = new StringBuffer ( line );

          for ( int i = 0; i < line.length (); i++ )

            if ( line.charAt ( i ) == '*' )

              line_buffer.setCharAt ( i, ' ' );

          if ( line.length () == 0 )
          {
            end_of_data = true;
          }  /* if */
          else
          {
            out_file.println ( line_buffer );
            seq = seq.concat ( line_buffer.toString () );
          }  /* else */
        }  /* else */
      }  /* try */
      catch ( IOException e1 )
      {
        System.out.println ( "readSequence: IOException on input file " + e1 );
        end_of_data = true;
      }  /* catch */
    }  /* while */

    out_file.flush ();
    out_file.close ();
  }
  catch ( IOException e2 )
  {
    System.out.println ( "readSequence: IOException on output file " + e2 );
  }  /* catch */

  return seq;
}  /* method readSequence */


/******************************************************************************/
protected void addPrimer ( String pat, String pos )
{
  // Check if first primer
  if ( primers.isEmpty () )
  {
    Primer primer = new Primer ( pat, pos );
    primers.addElement ( primer );
    return;
  }
  else
  {
    // Search the list for pattern.
    for ( int i = 0; i < primers.size (); i++ )
    {
      // Check if primer is already present.
      if ( pat.compareTo ( ((Primer) primers.elementAt ( i )).getSequence () ) == 0 )
      {
        ((Primer) primers.elementAt ( i )).incrementCount ();
      }
      else
        if ( pat.compareTo ( ((Primer) primers.elementAt ( i )).getSequence () ) < 0 )
        {
          // Insert pattern before position i.
          Primer primer = new Primer ( pat, pos );

          try
          {
            primers.insertElementAt ( primer, i );
            return;
          }
          catch ( ArrayIndexOutOfBoundsException e1 )
          {
            System.out.println ( "addPrimer: ArrayIndexOutOfBoundsException at position " + i + ", exception " + e1 );
          }  /* catch */
        }  /* if */
    }  /* for */

    // Add primer to the end of the list.
    Primer primer = new Primer ( pat, pos );
    primers.addElement ( primer );
    return;
  }  /* else */
}  /* method addPrimer */


/******************************************************************************/
public void printPrimers ( )
{
  System.out.println ( "" );
  System.out.println ( "List of unique primers:" );

  for ( int i = 0; i < primers.size (); i++ )

    if ( ((Primer) primers.elementAt ( i )).getCount () == 1 )
    {
      System.out.println ( 
          ((Primer) primers.elementAt ( i )).getPosition () + "  " +
          ((Primer) primers.elementAt ( i )).getSequence () );
    }  /* if */
}  /* method printPrimers */


/******************************************************************************/
public void readFind ( )
{
  String pattern;
  String position;

  try
  {
    DataInputStream in_file = new DataInputStream ( new FileInputStream ( fileName ) );

    /* Read in the FindPatterns file. */
    boolean end_of_file = false;			// End of file flag 
    String line;
    int line_pos = 1;

    while ( end_of_file == false )
    {
      try
      {
        line = in_file.readLine ();

        if ( line == null )  end_of_file = true;
        else
        {
          if ( line.endsWith ( "mis=2" ) == true )
          {
            pattern = line.substring ( 22, 30 );
            position = line.substring ( 5, 14 );

            addPrimer ( pattern, position );
          }  /* if */
        }  /* else */
      }  /* try */
      catch ( EOFException e1 )
      {
        System.out.println ( "End of file reached." + e1 );
        end_of_file = true;
      }  /* catch */
    }  /* while */

    /* Close the input file */
    try 
    {
      in_file.close ();
    }
    catch ( IOException e3 )
    {
      System.out.println ( "IOException while closing input file '" + fileName + "'" + e3 );
      return;
    }  /* catch */
  }  /* try */
  catch ( IOException e2 )
  {
    System.out.println ( "IOException on input file " + e2 );
  }  /* catch */

  System.out.println ( "Finished reading FindPatterns file" );
}  /* method readFind */


/******************************************************************************/
public void readFind ( String filename )
{
  /* Set the .ace file name. */
  fileName = filename;

  /* Read in the FindPatterns output file. */
  readFind ();
}  /* method readFind */


/******************************************************************************/
public boolean action ( Event evt, Object arg )
{
  if ( evt.target instanceof MenuItem )
  {
    if ( arg.equals ( "Quit" ) )
    {
      aceFrame.hide ();
      System.out.println ( "Quit selected." );
      System.exit ( 0 );
      return true;
    }  /* if */

    if ( arg.equals ( "Open" ) )
    {
      openFile ();			// Select a .ace file
      readFind ();			// Read in FindPatterns output file
      printPrimers ();		// Print out the unique primers found
      return true;
    }  /* if */
  }  /* if */
  else return false;

  return true;
}  /* method action */


/******************************************************************************/
public boolean handleEvent ( Event evt )
{

  return super.handleEvent ( evt );
}  /* method handleEvent */


/******************************************************************************/
public static void main ( String [] args )
{
  System.out.println ( "main called" );

  fileName = "400D.sort";

  if ( args.length == 0 )
  {
    aceFrame = new Pick8 ();
    aceFrame.resize ( 300, 300 );
    aceFrame.setTitle ( "Pick8 8-mer FindPattern processor" );

    init ();
    fileControl ();

    aceFrame.show ();
  }
  else
  {
    fileName = args [ 0 ];
    Pick8 read8 = new Pick8 ();
    read8.readFind ();			// Read in the patterns
    read8.printPrimers ();
  }  /* else */
}  /* method main */

}  /* class Pick8 */
