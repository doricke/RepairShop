

// import OutputTools;
// import Sequence;
// import SeqTools;

/******************************************************************************/
// This programs extracts promoters regions from multiple FASTA genomic sequence
// files using the gene models header line annotation.  A new FASTA sequence
// library file is created.
/******************************************************************************/

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

public class GetPromoters extends Object
{


private Sequence genes_file = new Sequence ();		// gene models sequences

private OutputTools promoters_file = new OutputTools ();	// Extracted promoter sequences


/******************************************************************************/
public GetPromoters ()
{
  initialize ();
}  // constructor GetPromoters


/******************************************************************************/
private void initialize ()
{
  genes_file.setSequenceType ( Sequence.DNA );
}  // method initialize


/******************************************************************************/
private char getStrand ( String header_line )
{
  int index = header_line.indexOf ( "[Partial " );
  if ( index > 0 )
  {
    return header_line.charAt ( index + 9 );
  }  // if
  else
  {
    index = header_line.indexOf ( "[Complete " );
    if ( index <= 0 )
    {
      System.out.println ( "*Warning* Unknown strand: " + header_line );
      return '?';
    }  // if

    return header_line.charAt ( index + 10 );
  }  // else
}  // method strand


/******************************************************************************/
private String getContigName ( String sequence_name )
{
  int index = sequence_name.indexOf ( '_' );

  if ( index <= 0 )  return sequence_name.toUpperCase ();

  return sequence_name.substring ( 0, index ).toUpperCase ();
}  // method getContigName


/******************************************************************************/
private void extractPromoter ()
{
  // Check for an ATG coordinate.
  String header_line = genes_file.getHeaderLine ().toString ();

  char strand = getStrand ( header_line );

  int index = header_line.indexOf ( "ATG@" );
  if ( index <= 0 )
  {
    return;		// no promoter
  }  // if

  int position = InputTools.getInteger ( header_line.substring ( index + 4 ) ); 
  // System.out.println ( genes_file.getSequenceName () + "\t" + header_line.substring ( index, index + 10 ) + " is " + position ); 

  Sequence contig_file = new Sequence ();
  contig_file.setSequenceType ( Sequence.DNA );
  String contig_name = getContigName ( genes_file.getSequenceName () );
  contig_file.setFileName ( contig_name );
  contig_file.openFile ();
  contig_file.readSequence ();

  // Copy the sequence header line to the promoters file.
  promoters_file.println ( header_line );

  String contig_sequence = contig_file.getSequence ();
  if ( strand == '+' )
  {
    System.out.println ( "\tplus strand: '" + contig_sequence.substring ( position-1, position + 2 ) + "'" );
    int start = position - 2001;
    if ( start < 0 )  start = 0;
    SeqTools.writeFasta ( promoters_file, contig_sequence.substring ( start, position + 2 ) );
  }  // if
  else
  {
    System.out.println ( "\tminus strand: '" + contig_sequence.substring ( position-5, position - 2 ) + "'" );
    int end = position + 1998;
    if ( end > contig_sequence.length () )
      SeqTools.writeFasta ( promoters_file, 
          SeqTools.reverseSequence ( contig_sequence.substring ( position-5 ) ) );
    else
      SeqTools.writeFasta ( promoters_file, 
          SeqTools.reverseSequence ( contig_sequence.substring ( position-5, end ) ) );
  }  // else

  // Close the DNA contig file.
  contig_file.closeFile ();
}  // method extractPromoter


/******************************************************************************/
  public void processFile ( String models_filename )
  {
    // Initialize.
    initialize ();

    // Set input filename.
    genes_file.setFileName ( models_filename );
  
    // Open the input file.
    genes_file.openFile ();
  
    promoters_file.setFileName ( models_filename + ".new2" );
    promoters_file.openFile ();
 
    // Read in each gene prediction.
    while ( ( genes_file.isEndOfFile () == false ) && 
            ( genes_file.isValidFile () == true ) )
    {
      // Read in the next FASTA sequence from the gene models file.
      genes_file.readSequence ();

      // Extract the promoter sequence.
      extractPromoter ();
    }  // while
 
    // Close input file.
    genes_file.closeFile ();
    promoters_file.closeFile ();
  }  // method processFile 


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java GetPromoters <models sequence file>" );
    System.out.println ();
    System.out.print   ( "where <models sequence file> is the file name of the " );
    System.out.println ( "FASTA library sequence output file of gene models." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    GetPromoters app = new GetPromoters ();

    if ( args.length == 0 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

/******************************************************************************/

}  //class GetPromoters
