import java.io.*;
import java.awt.*;

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
public class GetAceContigs extends Frame
{

/******************************************************************************/

static private Frame aceFrame;

static private String fileName;

static private String sequence;

static private TextArea textarea;

static private List selectedContig;

// protected Frame contigFrame;

// protected Contig contig;

// protected Contigs contigs;


/******************************************************************************/
public GetAceContigs ()
{
}  /* constructor GetAceContigs */


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

  aceFrame.add ( "Center", textarea );
  // aceFrame.pack ();
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

  Menu showMenu = new Menu ( "Show" );
  showMenu.add ( new MenuItem ( "Forward Strand" ) );
  showMenu.add ( new MenuItem ( "Reverse Strand" ) );
  showMenu.add ( new MenuItem ( "Both Strands" ) );
  controlMenuBar.add ( showMenu );

  aceFrame.setMenuBar ( controlMenuBar );

  selectedContig = new List ();
}  /* method fileControl */


/******************************************************************************/
private void openFile ( )
{
  FileDialog dialog = new FileDialog ( aceFrame, "Select a .ace file", FileDialog.LOAD );
  dialog.setDirectory ( "." );
  dialog.setFile ( "*.ace" );
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
public void readContigs ( )
{
  try
  {
    DataInputStream in_file = new DataInputStream ( new FileInputStream ( fileName ) );

    /* Read in the contig. */
    boolean end_of_file = false;			// End of file flag 
    String line;
    int line_pos = 1;

    while ( end_of_file == false )
    {
      try
      {
        line = in_file.readLine ();

        // Check for 'Base_segment' line. 
        if ( line != null )
          while ( line.startsWith ( "Base_segment" ) == true )
            line = in_file.readLine ();

        if ( line == null )  end_of_file = true;
        else
        {
          // Contig or individual sequence.
          if ( line.startsWith ( "DNA" ) == true )
          {
            // Check for Consensus sequence for a contig.
            if ( line.regionMatches ( 4, "Contig", 0, 6 ) == true )
            {
              // Read in the Consensus sequence.
              // readSeq ( in_file, consensus );
              sequence = readSequence ( in_file, line );
            }  /* if */
            else
            {
              // Read in individual sequence.
              // String single = readSequence ( in_file, line );
            }  /* else */
          }  /* if */

          // Quality numbers for last contig.
          if ( line.startsWith ( "BaseQuality" ) == true )
          {
            // System.out.println ( "BaseQuality line: " + line );
          }  /* if */

          // Assembly name list.
          if ( line.startsWith ( "Assembled_from" ) == true )
          {
            // System.out.println ( "AF: " + line );
          }  /* if */

          // Assembly information.
          if ( line.startsWith ( "Sequence" ) == true )
          {
            // System.out.println ( "Sequence line: " + line );
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

  System.out.println ( "Finished reading .ace file" );
}  /* method readContigs */


/******************************************************************************/
public void readContigs ( String filename )
{
  /* Set the .ace file name. */
  fileName = filename;

  /* Read in the contig. */
  readContigs ();
}  /* method readContigs */


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
      readContigs ();			// Read in the contigs
      return true;
    }  /* if */

    if ( arg.equals ( "Forward Strand" ) )
    {
      // reference currently selected contig
//      textarea = getForward ();
      aceFrame.show ();
      return true;
    }  /* if */

    if ( arg.equals ( "Reverse Strand" ) )
    {
      return true;
    }  /* if */

    if ( arg.equals ( "Both Strands" ) )
    {
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
    if ( evt.target == selectedContig )
    {
      String contigName = (String) evt.arg;

//      int contig_index = contigs.getIndex ( contigName );

//      Contig = contigs.getContig ( contigIndex );

//      textarea = contig.getAligment ();

      aceFrame.show ();
      return true;
    }  /* if */
  }  /* if */

  return super.handleEvent ( evt );
}  /* method handleEvent */


/******************************************************************************/
public static void main ( String [] args )
{
  System.out.println ( "main called" );

  fileName = "test.ace";

  if ( args [ 0 ].length () == 0 )
  {
    aceFrame = new GetAceContigs ();
    aceFrame.resize ( 300, 300 );
    aceFrame.setTitle ( "GetAceContigs .ace file DNA Sequence extractor" );

    init ();
    fileControl ();

    aceFrame.show ();
  }
  else
  {
    fileName = args [ 0 ];
    GetAceContigs readSeqs = new GetAceContigs ();
    readSeqs.readContigs ();			// Read in the contigs
  }  /* else */
}  /* method main */

}  /* class GetAceContigs */
