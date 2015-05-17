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
public class MakeSqn extends Object
{

/******************************************************************************/

static private String fileName;

static private boolean iub_bases = false;

static private String sqnName;

static private String sequence;


/******************************************************************************/
public MakeSqn ()
{
}  /* constructor MakeSqn */


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
// This method copies one text file to another.
public static void textCopy ( DataInputStream in_file, PrintStream out_file )
{
  try
  {
    boolean end_of_file = false;			// End of file flag 
    String line;

    // Copy until the end of the file.
    while ( end_of_file == false )
    {
      try
      {
        line = in_file.readLine ();


        if ( line == null )  end_of_file = true;
        else
        {
          out_file.println ( line );
        }  /* else */
      }  /* try */
      catch ( EOFException e1 )
      {
        System.out.println ( "method textCopy: End of file reached: " + e1 );
        end_of_file = true;
      }  /* catch */
    }  /* while */

    /* Close the input file. */
    try 
    {
      out_file.flush ();

      in_file.close ();
    }
    catch ( IOException e3 )
    {
      System.out.println ( "method textCopy: IOException while closing files: " + e3 );
      return;
    }  /* catch */
  }  /* try */
  catch ( IOException e2 )
  {
    System.out.println ( "method textCopy IOException: " + e2 );
  }  /* catch */

}  /* method textCopy */


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
// This method copies the input file to the output file.
public static FileOutputStream fileCopy ( String in_name, String out_name )
{
  FileOutputStream out_file = null;

  try
  {
    // Open the input file.
    DataInputStream in_file = new DataInputStream ( new FileInputStream ( in_name ) );

    out_file = new FileOutputStream ( out_name );
    PrintStream out_data = new PrintStream ( out_file );

    // Copy the input file to the output file.
    textCopy ( in_file, out_data );

    // Close the input file.
    in_file.close ();

    // Flush the output file.
    out_file.flush ();
  }  /* try */
  catch ( IOException e2 )
  {
    System.out.println ( "method fileCopy: IOException on input file " + e2 );
  }  /* catch */

  return out_file;
}  /* method fileCopy */


/******************************************************************************/
public String readDNA ( DataInputStream in_file )
{
  String line;
  boolean end_of_data = false;
  String seq = new String ();

  while ( end_of_data == false )
  {
    try 
    {
      line = in_file.readLine ();

      if ( ( line == null ) || ( line.length () < 1 ) )
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
    }  /* catch */
  }  /* while */

  return seq;
}  /* method readDNA */


/******************************************************************************/
// This method converts an integer to a hex string.
public static String hexString ( int hex )
{
  String hex_string = new String ( );

    switch ( hex )
    {
      case 0:
        hex_string = "0";
        break;

      case 1:
        hex_string = "1";
        break;

      case 2:
        hex_string = "2";
        break;

      case 3:
        hex_string = "3";
        break;

      case 4:
        hex_string = "4";
        break;

      case 5:
        hex_string = "5";
        break;

      case 6:
        hex_string = "6";
        break;

      case 7:
        hex_string = "7";
        break;

      case 8:
        hex_string = "8";
        break;

      case 9:
        hex_string = "9";
        break;

      case 10:
        hex_string = "A";
        break;

      case 11:
        hex_string = "B";
        break;

      case 12:
        hex_string = "C";
        break;

      case 13:
        hex_string = "D";
        break;

      case 14:
        hex_string = "E";
        break;

      case 15:
        hex_string = "F";
        break;

      default:
        System.out.println ( "hexString: interger (" + hex + ") is too large to handle." );
        break;
    }  // switch

  return hex_string;
}  // method hexString


/******************************************************************************/
// This method converts a DNA sequence to NCBI ASN.1 encoded string.
public static String to_ncbi2na ( String seq )
{
  int base_hex;					// hex code for current base
  String ncbi = new String ( );
  boolean upper = true;
  int packed_hex = 0;

  for ( int i = 0; i < seq.length (); i++ )
  {
    switch ( seq.charAt ( i ) )
    {
      case 'A':
      case 'a':
        base_hex = 0;
        break;

      case 'C':
      case 'c':
        base_hex = 1;
        break;

      case 'G':
      case 'g':
        base_hex = 2;
        break;

      case 'T':
      case 't':
        base_hex = 3;
        break;

      default:
        base_hex = 0;
        System.out.println ( "Unexpected character in DNA sequence: '" + seq.charAt ( i ) + "'" );
    }  // switch

    // Pack two base per hex character.
    if ( upper == true )
    {
      packed_hex = base_hex << 2;		// left shift two bits
      upper = false;
    }
    else
    {
      packed_hex += base_hex;			// pack the two bases together

      ncbi = ncbi.concat ( hexString ( packed_hex ) );
      upper = true;
    }  // else
  }  // for

  // Check for partial hex word.
  if ( upper == false )
  {
    ncbi = ncbi.concat ( hexString ( packed_hex ) );
  }  // if 

  return ncbi;
}  // method to_ncbi2na


/******************************************************************************/
// This method converts a DNA sequence to NCBI ASN.1 encoded string.
public static String to_ncbi4na ( String seq )
{
  String ncbi = new String ( );

  for ( int i = 0; i < seq.length (); i++ )
  {
    switch ( seq.charAt ( i ) )
    {
      case 'A':
      case 'a':
        ncbi = ncbi.concat ( "1" );
        break;

      case 'C':
      case 'c':
        ncbi = ncbi.concat ( "2" );
        break;

      case 'M':
      case 'm':
        ncbi = ncbi.concat ( "3" );
        break;

      case 'G':
      case 'g':
        ncbi = ncbi.concat ( "4" );
        break;

      case 'R':
      case 'r':
        ncbi = ncbi.concat ( "5" );
        break;

      case 'S':
      case 's':
        ncbi = ncbi.concat ( "6" );
        break;

      case 'V':
      case 'v':
        ncbi = ncbi.concat ( "7" );
        break;

      case 'T':
      case 't':
        ncbi = ncbi.concat ( "8" );
        break;

      case 'W':
      case 'w':
        ncbi = ncbi.concat ( "9" );
        break;

      case 'Y':
      case 'y':
        ncbi = ncbi.concat ( "A" );
        break;

      case 'H':
      case 'h':
        ncbi = ncbi.concat ( "B" );
        break;

      case 'K':
      case 'k':
        ncbi = ncbi.concat ( "C" );
        break;

      case 'D':
      case 'd':
        ncbi = ncbi.concat ( "D" );
        break;

      case 'B':
      case 'b':
        ncbi = ncbi.concat ( "E" );
        break;

      case 'N':
      case 'n':
        ncbi = ncbi.concat ( "F" );
        break;

      default:
        System.out.println ( "Unexpected character in DNA sequence: '" + seq.charAt ( i ) + "'" );
    }  // switch
  }  // for

  return ncbi;
}  // method to_ncbi4na


/******************************************************************************/
// This method appends the ASN.1 sequence information to a file.
public static void append_ncbina ( FileOutputStream out_file, String ncbi_type,
    String ncbina )
{
  try
  {
    PrintStream out_data = new PrintStream ( out_file );

    out_data.println ( "        inst {" );
    out_data.println ( "          repr raw ," );
    out_data.println ( "          mol dna ," );
    out_data.println ( "          length " + sequence.length () + " ," );
    out_data.println ( "          seq-data" );

    String to_print =  "            " + ncbi_type + " '" + ncbina + "'H } } } }";
    int printed = 0;
    for ( int i = 0; i < to_print.length (); i++ )
    {
      out_data.print ( to_print.charAt ( i ) );
      
      // Check for a full line.
      if ( ( ( ( i + 1 ) % 78 ) == 0 ) || ( i + 1 == to_print.length () ) )
        out_data.println ( );
    }  // for

    // Flush the output buffer.
    out_file.flush ();
  }
  catch ( IOException e1 )
  {
    System.out.println ( "method append_ncbina: IOException on append file " + e1 );
  }  // catch
}  // method append_ncbina


/******************************************************************************/


/******************************************************************************/
// This method reads in a DNA sequence from a text file.
public void readSequence ( )
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

        if ( line == null )  end_of_file = true;
        else
        {
          // Check for FASTA style sequence file. 
          if ( line.startsWith ( ">" ) == true )
          {
            sequence = readDNA ( in_file );
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

  System.out.println ( "DNA sequence length = " + sequence.length () + " base pairs" );

  if ( iub_bases == true )
    System.out.println ( "IUB bases found!" );
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
  System.out.println ( "use: java MakeSqn <seq> <sqn_template>" );
  System.out.println ( );
  System.out.println ( "e.g.: java MakeSqn H109 lbnl.sqn" );
}  // showParameters


/******************************************************************************/
public static void main ( String [] args )
{
  System.out.println ( "MakeSqn\tversion 0.1" );
  System.out.println ( );

  fileName = "test.seq";
  sqnName  = "lbnl.sqn";

  if ( args [ 0 ].length () <= 1 )
  {
    showParameters ();
  }
  else
  {
    // Capture the command line parameters.
    fileName = args [ 0 ];
    sqnName  = args [ 1 ];

    MakeSqn readSeq = new MakeSqn ();
    readSeq.readSequence ();			// Read in the contigs
  }  /* else */

  // Copy the ASN.1 header file to the output file.
  FileOutputStream sqn_file = fileCopy ( sqnName, fileName + ".sqn" );

  // Append the DNA sequence in ASN.1 format to the output file.
  if ( iub_bases == false )
    append_ncbina ( sqn_file, "ncbi2na", to_ncbi2na ( sequence ) );
  else
    append_ncbina ( sqn_file, "ncbi4na", to_ncbi4na ( sequence ) );

  // Close the ASN.1 .sqn file.
  closeFile ( sqn_file );

}  /* method main */

}  /* class MakeSqn */
