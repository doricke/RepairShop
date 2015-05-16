
import java.util.Vector;

import InputTools;
import SeqTools;
import Sequence;
import OutputTools;

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

public class SelectProteins extends Object
{


/******************************************************************************/

private Sequence fasta_file = new Sequence ();	// FASTA sequence input file

private OutputTools proteins_file = new OutputTools ();	// Unigene names


/******************************************************************************/
public SelectProteins ()
{
  initialize ();
} // SelectProteins constructor


/******************************************************************************/
private void initialize ()
{
} // method initialize


/******************************************************************************/
public void close ()
{
}  // method close


/******************************************************************************/
private boolean isSelected ( String header, StringBuffer sequence )
{
  // Ignore short sequences.
  if ( sequence.length () < 150 )  return false;

  // Check that the protein sequence starts with Met.
  if ( sequence.charAt ( 0 ) != 'M' )  return false;

  // Check for the /partial=true qualifier.
  if ( header.indexOf ( "partial=true" ) > 0 )  return false;

  return true;
}  // method isSelected


/******************************************************************************/
private void readSequences ()
{
  StringBuffer seq = null;
  StringBuffer header = null;

  // Read in the header lines.
  while ( fasta_file.isEndOfFile () != true )
  {
    seq = fasta_file.nextSequence ();
    header = fasta_file.getHeaderLine ();

    if ( header.length () > 0 )
    {
      // Check if the sequence is selected.
      if ( isSelected ( header.toString (), seq ) == true )
      {
        proteins_file.println ( header );
        SeqTools.writeFasta ( proteins_file, seq.toString () );
      }  // if
    }  // if
  }  // while
}  // method readSequences


/******************************************************************************/
// Process the fasta header lines file.
private void processFile ( String fasta_filename, String output_name )
{
  // Set input file name
  fasta_file.setFileName ( fasta_filename );
  fasta_file.setSequenceType ( Sequence.AA );

  // Set up the unigene names file.
  proteins_file.setFileName ( output_name );
  proteins_file.openFile ();

  // Open input file
  fasta_file.openFile ();

  // read in the FASTA sequences.
  readSequences ();

  // Close input file
  fasta_file.closeFile ();
  proteins_file.closeFile ();

  close ();
} // method processFile


/******************************************************************************/
private void usage ()
{
  System.out.println ( "The command line syntax for this program is:" );
  System.out.println ();
  System.out.println ( "java SelectProteins <FASTA file> <output file>" );
  System.out.println ();
  System.out.print   ( "where <FASTA file> is the file name of a " );
  System.out.println ( "GenBank protein FASTA file and <output file>" );
  System.out.print   ( "is the FASTA output file name." );

}  // method usage


/******************************************************************************/
public static void main ( String[] args )
{
  SelectProteins app = new SelectProteins ();

  if ( args.length <= 1 ) 
    app.usage ();
  else
    app.processFile ( args [ 0 ], args [ 1 ]);
} // method main

} // class SelectProteins
