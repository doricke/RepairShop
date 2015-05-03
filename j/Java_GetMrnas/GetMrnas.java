

// import OutputTools;
// import Sequence;
// import SeqTools;

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

public class GetMrnas extends Object
{

/******************************************************************************/
// This programs extracts promoters regions from multiple FASTA genomic sequence
// files using the gene models header line annotation.  A new FASTA sequence
// library file is created.
/******************************************************************************/


private Sequence pep_file = new Sequence ();		// gene models sequences

private OutputTools mrnas_file = new OutputTools ();	// Extracted promoter sequences


/******************************************************************************/
public GetMrnas ()
{
  initialize ();
}  // constructor GetMrnas


/******************************************************************************/
private void initialize ()
{
  pep_file.setSequenceType ( Sequence.AA );
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
  // Check for EMBL delimited sequence name.
  int embl = sequence_name.indexOf ( "|" );
  if ( embl > 0 )
  {
    String contig_name = sequence_name.substring ( 0, embl ) + "_";

    int embl2 = sequence_name.indexOf ( "|", embl + 1 );
    if ( embl2 > 0 )
      return ( contig_name + sequence_name.substring ( embl+1, embl2 ) );
    else
      return ( contig_name + sequence_name.substring ( embl+1 ) ); 
  }  // if

  int index = sequence_name.indexOf ( "-" );

  if ( index <= 0 )  return sequence_name;

//  int end = sequence_name.lastIndexOf ( "_", index );

  int end = sequence_name.indexOf ( "_" );

  if ( end <= 0 )
    return sequence_name;
  else
    return sequence_name.substring ( 0, end );
}  // method getContigName


/******************************************************************************/
private void writeHeaderLine ( String header_line )
{
  int index = header_line.indexOf ( ".aa" );
  if ( index < 0 )
  {
    mrnas_file.println ( header_line );
    return;
  }  // if

  mrnas_file.println ( header_line.substring ( 0, index ) + ".mrna" 
      + header_line.substring ( index + 3 ) );
}  // method writeHeaderLine


/******************************************************************************/
private void extractMrna ()
{
  // Check for an ATG coordinate.
  String header_line = pep_file.getHeaderLine ().toString ();

  char strand = getStrand ( header_line );

  int index = header_line.indexOf ( "Exons[" );
  if ( index <= 0 )
  {
    return;		// no promoter
  }  // if

  // Ignore the start and stop codon markers.
  if ( ( header_line.charAt ( index + 6 ) == '$' ) ||
       ( header_line.charAt ( index + 6 ) == '*' ) )  index++;

  String coordinates = header_line.substring ( index + 6 );
  index = coordinates.indexOf ( ']' );
  if ( index > 0 )
  {
    // Ignore the start and stop codon markers.
    if ( ( coordinates.charAt ( index - 1 ) == '$' ) ||
         ( coordinates.charAt ( index - 1 ) == '*' ) )  index--;

    coordinates = coordinates.substring ( 0, index );
  }  // if

  Sequence contig_file = new Sequence ();
  contig_file.setSequenceType ( Sequence.DNA );
  String contig_name = getContigName ( pep_file.getSequenceName () );
  contig_file.setFileName ( contig_name );

  contig_file.openFile ();
  contig_file.readSequence ();
  String contig_sequence = contig_file.getSequence ();

  // Copy the sequence header line to the promoters file.
  writeHeaderLine ( header_line );

  // Extract the mRNA exons.
  String sequence = "";
  do
  {
    // Get the exon coordinates.
    int start = InputTools.getInteger ( coordinates ) - 1;	// sequence starts at 0 (i.e., -1)
    index = coordinates.indexOf ( '-' );
    int end = 0;
    if ( index > 0 )
      end = InputTools.getInteger ( coordinates.substring ( index + 1 ) );

    if ( ( end > start ) && ( start >= 0 ) )
    {
      // Validate start.
      if ( start >= contig_sequence.length () )
      {
        System.out.println ( "*Warning* start (" + start + ") is > contig length (" + 
            contig_sequence.length () + ") for " + header_line );
      }  // if
      else
      {
        // Extract the exon.
        if ( end >= contig_sequence.length () )
          sequence += contig_sequence.substring ( start );
        else
          sequence += contig_sequence.substring ( start, end );
      }  // else
    }  // if
    // Advance to the next set of coordinates.
    index = coordinates.indexOf ( '|' );
    if ( index > 0 )  
      coordinates = coordinates.substring ( index + 1 );
    else  
      coordinates = "";
  }
  while ( coordinates.length () > 0 );

  // Write out the mRNA sequence.
  if ( strand == '+' )

    SeqTools.writeFasta ( mrnas_file, sequence );

  else

    SeqTools.writeFasta ( mrnas_file, SeqTools.reverseSequence ( sequence ) );

  // Close the DNA contig file.
  contig_file.closeFile ();
}  // method extractMrna


/******************************************************************************/
  public void processFile ( String models_filename )
  {
    // Initialize.
    initialize ();

    // Set input filename.
    pep_file.setFileName ( models_filename );
  
    // Open the input file.
    pep_file.openFile ();
  
    mrnas_file.setFileName ( models_filename + ".mRNAs" );
    mrnas_file.openFile ();
 
    // Read in each gene prediction.
    while ( ( pep_file.isEndOfFile () == false ) && 
            ( pep_file.isValidFile () == true ) )
    {
      // Read in the next FASTA sequence from the gene models file.
      pep_file.readSequence ();

      // Extract the mRNA sequence.
      extractMrna();
    }  // while
 
    // Close input file.
    pep_file.closeFile ();
    mrnas_file.closeFile ();
  }  // method processFile 


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java GetMrnas <models sequence file>" );
    System.out.println ();
    System.out.print   ( "where <models sequence file> is the file name of the " );
    System.out.println ( "FASTA library protein sequence output file of gene models." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    GetMrnas app = new GetMrnas ();

    if ( args.length == 0 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

/******************************************************************************/

}  //class GetMrnas
