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
public class ClipXDNA extends Object
{

/******************************************************************************/

static private boolean end_of_file = false;	// End of file flag 

static private String fileName;			// Input file name

static private boolean iub_bases = false;	// IUB base found flag
  
static private String line;			// Current line of the file

static private String outputFileName;		// Output file name

static private String sequence;			// DNA sequence


/******************************************************************************/
public ClipXDNA ()
{
}  /* constructor ClipXDNA */


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  /* method setFileName */


/******************************************************************************/
// This method checks if a character is a valid DNA base or DNA IUB code.
public boolean isDNA ( char base )
{
  // Check for IUB characters.
  if ( ( base == 'B' ) || ( base == 'b' ) ||
       ( base == 'D' ) || ( base == 'd' ) ||
       ( base == 'H' ) || ( base == 'h' ) ||
       ( base == 'V' ) || ( base == 'v' ) ||
       ( base == 'R' ) || ( base == 'r' ) ||
       ( base == 'Y' ) || ( base == 'y' ) ||
       ( base == 'K' ) || ( base == 'k' ) ||
       ( base == 'M' ) || ( base == 'm' ) ||
       ( base == 'S' ) || ( base == 's' ) ||
       ( base == 'W' ) || ( base == 'w' ) ||
       ( base == 'N' ) || ( base == 'n' ) ||
       ( base == 'X' ) || ( base == 'x' ) )
  {
    iub_bases = true;
    return true;
  }
  else
    // Check for the DNA bases.
    if ( ( base == 'A' ) || ( base == 'a' ) ||
         ( base == 'C' ) || ( base == 'c' ) ||
         ( base == 'G' ) || ( base == 'g' ) ||
         ( base == 'T' ) || ( base == 't' ) )

       return true;

    else
    {
      // Check for an unexpected letter.
      if ( ( ( base >= 'A' ) && ( base <= 'Z' ) ) ||
           ( ( base >= 'a' ) && ( base <= 'z' ) ) )
        System.out.println ( "Unknown character in expected DNA sequence: '" + base + "'" );

      return false;
    }  // else
}  // method isDNA


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
private int findX ( String seq )
{
  int index;				// index

  index = seq.indexOf ( "X" );
  if ( index == -1 )
    index = seq.indexOf ( "x" );
  return index;
}  // method findX


/******************************************************************************/
public void writeDNA ( String seq, PrintStream out_file )
{
  // Write out each line of the sequence. 
  for ( int index = 0; index < seq.length (); index += 50 )
  {
    if ( index + 51 < seq.length () )
      out_file.println ( seq.substring ( index, index + 51 ) );
    else 
      out_file.println ( seq.substring ( index, seq.length () ) );
  }  // for 
}  // method writeDNA


/******************************************************************************/
public String clipDNA ( String seq )
{
  int index;			// sequence index

  // Eliminate all 'X' bases. 
  index = findX ( seq );
  while ( ( index >= 0 ) && ( index < seq.length () ) )
  {
    // Check if in the beginning of the sequence. 
    if ( index < seq.length () / 2 )
    {
      // Skip adjacent 'X' bases.
      while ( ( index+2 < seq.length () ) && 
              ( ( seq.charAt ( index+1 ) == 'X' ) ||
                ( seq.charAt ( index+1 ) == 'x' ) ) )  index++;

      seq = seq.substring ( index+1, seq.length () );
    }
    else
    {
      // Skip preceeding 'X' bases.
      while ( ( index > 1 ) &&
              ( ( seq.charAt ( index-1 ) == 'X' ) ||
                ( seq.charAt ( index-1 ) == 'x' ) ) )  index--;
              
      seq = seq.substring ( 0, index );
    }

    // Look for the next 'X' base.
    index = findX ( seq );
  }  // while

  return ( seq );
}  // method clipDNA


/******************************************************************************/
public String readDNA ( DataInputStream in_file )
{
  boolean end_of_data = false;
  String seq = new String ();

  // Read in the DNA sequence.
  while ( end_of_data == false )
  {
    try 
    {
      // Get the next line of the file.
      line = in_file.readLine ();

      if ( ( line == null ) || ( line.length () < 1 )  ||
           ( line.startsWith ( ">" ) == true ) )
      {
        end_of_data = true;
      }  /* if */
      else
      {
        // Ignore non-DNA characters.
        for ( int i = 0; i < line.length (); i++ )

          if ( isDNA ( line.charAt ( i ) ) == true )
            seq = seq.concat ( line.substring ( i, i+1 ) );

        if ( line.length () == 0 )
        {
          end_of_data = true;
        }  /* if */
      }  /* else */
    }  /* try */
    catch ( IOException e1 )
    {
        System.out.println ( "readDNA: IOException on input file " + e1 );
        end_of_data = true;
        end_of_file = true;
    }  /* catch */
  }  /* while */

  return seq;
}  /* method readDNA */


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
// This method reads in a DNA sequence from a text file.
public void readSequence ( PrintStream out_file )
{
  String description;			// DNA sequence description line


  try
  {
    DataInputStream in_file = new DataInputStream 
        ( new FileInputStream ( fileName ) );

    /* Read in the contig. */
    int line_pos = 1;

    while ( end_of_file == false )
    {
      try
      {
        // Read in the next line of the file.
        line = in_file.readLine ();

        // Check for the last line.
        if ( line == null )  end_of_file = true;
        else
        {
          // Check for FASTA style sequence file. 
          if ( line.startsWith ( ">" ) == true )
          {
            while ( ( end_of_file == false ) && ( line != null ) )
            {
              description = line;

              // Read in the DNA sequence.
              sequence = readDNA ( in_file );

              if ( sequence.length () > 0 )
              {
                // Copy the description line to the output file.
                out_file.println ( description );

                // Trim 'X' bases from the DNA sequence.
                sequence = clipDNA ( sequence );

                // Write out the trimmed DNA sequence.
                writeDNA ( sequence, out_file );
              }  // if 
            }  // while 
          }
          else

            // Check for GCG or GenBank format sequence
            if ( line.startsWith ( "LOCUS" ) == true )
            {
              System.out.println ( "GCG or GenBank formats are not implemented yet." );
            }  /* if */
            else
              sequence = readDNA ( in_file );
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

  if ( iub_bases == true )
    System.out.println ( "IUB bases found!" );
}  /* method readSequence */


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
public static void showParameters ( )
{
  System.out.println ( "use: java ClipXDNA <FASTA library> <clipped library>" );
}  // showParameters


/******************************************************************************/
public static void main ( String [] args )
{
  System.out.println ( "ClipXDNA\tversion 0.1" );
  System.out.println ( );

  fileName = "test.seq";
  outputFileName  = "test.seq.clip";

  if ( args [ 0 ].length () <= 1 )
  {
    showParameters ();
  }
  else
  {
    // Capture the command line parameters.
    fileName = args [ 0 ];
    outputFileName  = args [ 1 ];

    // Create the output file.
    try
    {
      PrintStream output_file = new PrintStream 
          ( new FileOutputStream ( outputFileName ) );

      ClipXDNA readSeq = new ClipXDNA ();
      readSeq.readSequence ( output_file );		// Read in the contigs

      // Close the output file.
      output_file.flush ();
      output_file.close ();
    }  // try
    catch ( IOException e1 )
    {
      System.out.println ( "IOException on output file " + e1 );
    }  // catch
  }  /* else */

}  /* method main */

}  /* class ClipXDNA */
