
import java.util.*;


/******************************************************************************/
/**
  This class reads in a tab-delimited file and generates a FASTA sequence file.
 
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

public class TabToFasta extends Object
{

/******************************************************************************/


/******************************************************************************/
/** This constructor creates and initializes a new TabToFasta object. */
public TabToFasta ()
{
  initialize ();
}  // constructor TabToFasta


/******************************************************************************/
/**
  This method initializes the TabToFasta object.
*/
public void initialize ()
{
}  // method initialize


/******************************************************************************/
private String rnaToDna ( String sequence )
{
  StringBuffer dna = new StringBuffer ( sequence );

  for ( int i = 0; i < dna.length (); i++ )
  {
    if ( dna.charAt ( i ) == 'U' )  dna.setCharAt ( i, 'T' );
    if ( dna.charAt ( i ) == 'u' )  dna.setCharAt ( i, 't' );
  }  // for

  return clip ( dna.toString () );
}  // method rnaToDna


/******************************************************************************/
private String clip ( String sequence )
{
  int len = sequence.length ();
  if ( len <= 2 )  return sequence;

  char base = sequence.charAt ( len - 2 );

  if ( ( base >= 'a' ) && ( base <= 'z' ) )

    return sequence.substring ( 0, len - 2 );

  return sequence;
}  // method clip


/******************************************************************************/
private void writeHeader 
    ( OutputFile  fasta_file
    , String      sequence_name
    , String      interpro_id
    , String      gene_symbol
    , String      rnas 
    )
{
  fasta_file.println ( ">" + sequence_name + " InterPro[" + interpro_id 
      + "] Gene[" + gene_symbol + "] RNAs[" + rnas + "]" );
}  // method writeHeader


/******************************************************************************/
private void processFile ( String file_name )
{
  System.out.println ( "processFile: file_name = '" + file_name + "'" );

  InputFile tab_file = new InputFile ();
  tab_file.setFileName ( file_name );
  tab_file.openFile ();

  // Skip the header line.
  tab_file.nextLine ();

  // Setup the FASTA sequence files.
  OutputFile sense_file = new OutputFile ();
  sense_file.setFileName ( file_name + ".sense" );
  sense_file.openFile ();

  // Setup the FASTA sequence files.
  OutputFile antisense_file = new OutputFile ();
  antisense_file.setFileName ( file_name + ".antisense" );
  antisense_file.openFile ();

  // Copy the file to output.
  String line;
  StringTokenizer tokens;
  String identifier;
  String antisense;
  String sense;
  String interpro_id;
  String gene_symbol;
  String rnas;
  while ( tab_file.isEndOfFile () == false )
  {
    line = tab_file.nextLine ().toString ();
    tokens = new StringTokenizer ( line, "\t" );

    try
    {
      tokens.nextToken ();			// Plate_Nick
      tokens.nextToken ();			// Position
      tokens.nextToken ();			// NAS
      identifier = tokens.nextToken ();		// Unique_Identifier
      antisense = rnaToDna ( tokens.nextToken () );	// Antisense
      sense = rnaToDna ( tokens.nextToken () );	// Sense
      interpro_id = tokens.nextToken ();	// IPR ID (family)
      gene_symbol = tokens.nextToken ();	// Gene symbol 
      tokens.nextToken ();			// Potency score
      tokens.nextToken ();			// Potency rank
      tokens.nextToken ();			// quality
      tokens.nextToken ();			// chromosome
      tokens.nextToken ();			// scaffold ID
      tokens.nextToken ();			// coordinate on scaffold
      tokens.nextToken ();			// coordinate on TR
      rnas = tokens.nextToken ();		// RNAs
      tokens.nextToken ();			// Transcripts
      tokens.nextToken ();			// lhit
      tokens.nextToken ();			// Specificity

      writeHeader ( sense_file, identifier + ".f", interpro_id, gene_symbol, rnas );
      sense_file.println ( sense );

      writeHeader ( antisense_file, identifier + ".r", interpro_id, gene_symbol, rnas );
      antisense_file.println ( antisense );

    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "processFile: " + e );
      System.out.println ( line );
    }  // catch
  }  // while

  tab_file.closeFile ();
  sense_file.closeFile ();
  antisense_file.closeFile ();
}  // method processFile


/******************************************************************************/
private void usage ()
{
  System.out.println ( "This program generates FASTA files from a tab-delimited siRNA file." );
  System.out.println ();
  System.out.println ( "The command line syntax for this program is:" );
  System.out.println ();
  System.out.println ( "java TabToFile <input_file>" );
  System.out.println ();
  System.out.println ( "where <input_file> is the tab-delimited input file." );
}  // method usage


/******************************************************************************/
/** An object test method that illustrates the use of the TabToFasta object. */
public static void main ( String [] args )
{
  // Create an application object.
  TabToFasta application = new TabToFasta ();

  if ( args.length != 1 )
    application.usage ();
  else
    application.processFile ( args [ 0 ] );
}  // method main

}  // class TabToFasta

