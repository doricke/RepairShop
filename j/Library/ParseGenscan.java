import java.io.*;
import java.awt.*;
import java.util.*;
import ParseName;
import HistoLengths.*;

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
public class ParseGenScan extends Frame
{

/******************************************************************************/

static private Frame readFrame;			// Frame for this object

static private String cloneName = "";		// Clone name to process

static private String fileName = "";		// Current file name

// Histograms of clear lengths.
static private HistoLengths lengthHistograms;

static private int insertSize = 46000;		// Default insert size

static private int shortLength = 100;		// Minimum length

static private TextArea textarea;		// Text area for Frame

static private List selectedFile;		


/******************************************************************************/
public ParseGenScan ()
{
}  /* constructor ParseGenScan */


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  /* method setFileName */


/******************************************************************************/
public static void init ( )
{
  Font font = new Font ( "Courier", Font.PLAIN, 12 );
  textarea = new TextArea ( 24, 80 );
  // textarea.setFont = ( new Font ( "Courier", Font.PLAIN, 12 ) );
  textarea.setFont ( font );

  readFrame.add ( "Center", textarea );
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

  readFrame.setMenuBar ( controlMenuBar );

  selectedFile = new List ();
}  /* method fileControl */


/******************************************************************************/
private int getInteger ( String line )
{
  int i = 0;
  int index = 0;
  int sign = 1;					// Default sign = +

  // Skip leading white space.
  while ( ( line.charAt ( index ) == ' ' ) ||
          ( line.charAt ( index ) == '\t' ) )  index++;

  // Check for a sign.
  if ( line.charAt ( index ) == '+' )
    index++;
  else
    if ( line.charAt ( index ) == '-' )
    {
      sign = -1;
      index++;
    }  /* if */

  // Traverse the integer.
  while ( index < line.length () )
  {
    if ( ( line.charAt ( index ) >= '0' ) && ( line.charAt ( index ) <= '9' ) )

      i = i * 10 + (int) line.charAt ( index ) - (int) '0';

    else  index = line.length ();		// Terminate loop

    index++;
  }  /* while */

  // Set the sign.
  i *= sign;

  return ( i );					// Return the integer
}  /* method getInteger */


/******************************************************************************/
private void openFile ( )
{
  FileDialog dialog = new FileDialog ( readFrame, "Select a .out file", FileDialog.LOAD );
  dialog.setDirectory ( "." );
  dialog.setFile ( "*.out" );
  dialog.show ();

  fileName = dialog.getFile ();

  if ( fileName != null )
    System.out.println ( "openFile: file name selected is '" + fileName + "'" );
}  /* method openFile */


/******************************************************************************/
private String getSeqName ( String line )
{
  String name = "";

  if ( ! line.startsWith ( "Sequence" ) )  return name;

  int colon = line.indexOf ( ':' );

  int start = 9;

  if ( colon > 0 )  name = line.substring ( start, colon - 1 );

  return name;
}  /* method getSeqName */


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
private void xxx ()
{

{}

/******************************************************************************/
public void readMail ( )
{
  genes = new Genes ( );

  String shortFileName = new String ( cloneName + ".short" );

  System.out.println ( "Writing short sequences list file \t" + shortFileName );
  try
  {
    DataInputStream in_file = new DataInputStream ( new FileInputStream ( fileName ) );

    FileOutputStream short_file = new FileOutputStream ( shortFileName );
    PrintStream short_data = new PrintStream ( short_file );

    /* Read in the contig. */
    boolean end_of_file = false;			// End of file flag 
    String clone_name = "";				// Name of current contig
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

          // Check for "Sequence sequenceName : ...".
          if ( line.startsWith ( "Sequence" )
          {
            cloneName = getSeqName ( line );
            System.out.println ( line );
          }  // if

          // Write out the parameters.
          if ( line.startsWith ( "Parameter" )
            System.out.println ( line );

          // Write out sequences
          if ( line.startsWith ( ">" )
          {
            // Write out the sequence for SCAN analysis.
            processSeq ( in_file, line, genes );

            // Check for peptide sequence
            if ( line.indexOf ( "_prediced_peptide_" ) > 0 )
              genes.addPeptide ();
          }  // if

          // Check for a feature.
          if ( ( line.charAt ( __ ) == '+' ) || ( line.charAt ( __ ) == '-' ) )
            processFeature ( line, genes );
        }  /* else */
      }  /* try */
      catch ( EOFException e1 )
      {
        System.out.println ( "End of file reached: " + e1 );
        end_of_file = true;
      }  /* catch */
    }  /* while */

    /* Close files */
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

  genes.writeASN ( );
}  /* method readMail */


/******************************************************************************/
public boolean action ( Event evt, Object arg )
{
  if ( evt.target instanceof MenuItem )
  {
    if ( arg.equals ( "Quit" ) )
    {
      readFrame.hide ();
      // System.out.println ( "action method Quit MenuItem selected." );
      System.exit ( 0 );
      return true;
    }  /* if */

    if ( arg.equals ( "Open" ) )
    {
      openFile ();			// Select a .ace file
      readMail ();			// Read in the contigs
      return true;
    }  /* if */
  }  /* if */
  else return false;

  return true;
}  /* method action */


/******************************************************************************/
public boolean handleEvent ( Event evt )
{
  if ( evt.id == Event.ACTION_EVENT )
  {
    if ( evt.target == selectedFile )
    {
      String fileName = (String) evt.arg;

      readFrame.show ();
      return true;
    }  /* if */
  }  /* if */

  return super.handleEvent ( evt );
}  /* method handleEvent */


/******************************************************************************/
public static void main ( String [] args )
{
  // Initialize main variables.
  cloneName = "";			// default clone name - none

  fileName = "";			// default file name - none


  /* Check for parameters. */
  if ( args.length == 0 )
  {
    readFrame = new ParseGenScan ();
    readFrame.resize ( 300, 300 );
    readFrame.setTitle ( "ParseGenScan" );

    init ();
    fileControl ();

    readFrame.show ();
  }
  else
  {
    // The .out file name is the first parameter.
    fileName = args [ 0 ];
    ParseGenScan parser = new ParseGenScan ();

    if ( args.length >= 2 )
    {
      insertSize = parser.getInteger ( args [ 1 ] );
      System.out.print ( ", Insert size = " + insertSize );
    }  /* if */

    System.out.println ( );

    // Read in clear lengths
    parser.readMail ();
  }  /* else */

}  /* method main */

}  /* class ParseGenScan */
