import java.io.*;
import java.awt.*;
import PgsqlTest;

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
public class ReadDNA extends Object
{

/******************************************************************************/

static private String fileName;			// Input file name

static private boolean iub_bases = false;	// IUB base found flag

static private boolean end_of_file = false;	// End of file flag

static private String line;			// Current line of the file

static private String sequence;			// DNA sequence

static private PgsqlTest database;		// PGSQL database connection


/******************************************************************************/
public ReadDNA ()
{
}  /* constructor ReadDNA */


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
       ( base == 'N' ) || ( base == 'n' ) )
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
// This method closes a FileOutputStream file.
public static void closeFile ( FileOutputStream out_file )
{
  try
  {
    out_file.flush ();
    out_file.close ();
  }
  catch ( IOException e )
  {
    System.out.println ( "method closeOutputFile: IOException closing file: " + e );
  }  // catch
}  // method closeFile


/******************************************************************************/


/******************************************************************************/
public String readDNA ( DataInputStream in_file )
{
  boolean end_of_data = false;			// End of DNA sequence flag
  String seq = new String ();			// DNA sequence

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

        // Check for end of file.
        if ( ( line == null ) || ( line.length () < 1 ) )  end_of_file = true;
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
          end_of_file = true;
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


/******************************************************************************/


/******************************************************************************/
// This method reads in a DNA sequence from a text file.
public void readSequence ( )
{
  try
  {
    DataInputStream in_file = new DataInputStream ( new FileInputStream ( fileName ) );

    /* Read in the contig. */
    String description;					// Description line
    String name;					// Sequence Name
    String sqlCommand;					// SQL command 
    int index;						// index
    int line_pos = 1;					// line position

    // Loop until end of file is reached.
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
         if ( line.startsWith ( ">" ) == true )
         {
          // Check for FASTA style sequence file. 
          while ( ( end_of_file == false ) && ( line != null ) )
          {
            // Save the FASTA description line.
            description = line.substring ( 1 );
            index = description.indexOf ( " " );
            System.out.println ( "index of space = " + index );
            if ( ( index >= 0 ) && ( index < description.length () ) )
              name = description.substring ( 1, index );
            else
              name = description;
            if ( name.length () > 40 )  name = name.substring ( 0, 40 );
            System.out.println ( "Sequence name = '" + name + "'" );

            // Read in the DNA sequence. 
            sequence = readDNA ( in_file );

            // Insert the sequence into the database table.
            sqlCommand = "INSERT INTO seqs VALUES ( 0, '" + 
                name + "', '" +
                description + "', '" +
                sequence + "', 0 )";
            System.out.println ( "readSequence: sqlCommand = '" + sqlCommand + "'" );
            database.executeSQL ( sqlCommand );
          }  // while
         }
         else

            // Check for GCG or GenBank format sequence
            if ( line.startsWith ( "LOCUS" ) == true )
            {
              System.out.println ( "readSequence: GCG or GenBank formats are not implemented yet." );
            }  /* if */
            else
            {
              // Read in the DNA sequence. 
              sequence = readDNA ( in_file );

              System.out.println ( "Sequence = '" + sequence + "'" );
            }  // else
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

  System.out.println ( "DNA sequence length = " + sequence.length () + " base pairs" );

  if ( iub_bases == true )
    System.out.println ( "readSequence: IUB bases found!" );
}  /* method readSequence */


/******************************************************************************/
public void readSequence ( String filename )
{
  /* Set the .ace file name. */
  fileName = filename;

  /* Read in the contig. */
  readSequence ();
}  /* method readSequence */


/******************************************************************************/


/******************************************************************************/
public static void showParameters ( )
{
  System.out.println ( "use: java ReadDNA <seq>" );
  System.out.println ( );
  System.out.println ( "e.g.: java ReadDNA H109.seq" );
}  // showParameters


/******************************************************************************/
public static void main ( String [] args )
{
  System.out.println ( "ReadDNA\tversion 0.1" );
  System.out.println ( );

  fileName = "test.seq";

  database = new PgsqlTest ( );
  database.connectDB ();

  System.out.println ( "After connect to database" );

  if ( args [ 0 ].length () <= 1 )
  {
    showParameters ();
  }
  else
  {
    // Capture the command line parameters.
    fileName = args [ 0 ];

    ReadDNA readSeq = new ReadDNA ();
    readSeq.readSequence ();			// Read in the contigs
  }  /* else */

}  /* method main */

}  /* class ReadDNA */
