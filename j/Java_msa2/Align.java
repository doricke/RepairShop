
import java.util.Vector;

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
public class Align extends Object
{

/******************************************************************************/

  String seq1;				// Sequence #1

  String seq2;				// Sequence #2

  Vector alignment = new Vector ();	// Alignment vector


/******************************************************************************/
public Align ()
{
  initialize ();
}  // constructor Align


/******************************************************************************/
public void initialize ()
{
  seq1 = null;
  seq2 = null;
}  // method initialize


/******************************************************************************/
public Vector getAlignment ()
{
  return alignment;
}  // method getAlignment


/******************************************************************************/
public void setSequence1 ( String sequence1 )
{
  String seq1 = sequence1;
}  // method setSequence1


/******************************************************************************/
public void setSequence2 ( String sequence2 )
{
  String seq2 = sequence2;
}  // method setSequence2


/******************************************************************************/
private int find_start ( )
{
  return 0;
}  // method find_start


/******************************************************************************/
private int extent ( String seq1, String seq2 )
{
  // Extend the alignment
  int length = 0;
  while ( ( length < seq1.length () ) && 
          ( length < seq2.length () ) &&
          ( seq1.charAt ( length ) == seq2.charAt ( length ) ) )

    length++;

  return length;
}  // method extent


/******************************************************************************/
private int scoreAcceptor ( String genomic, int exon_start )
{
  int score = 0;				// splice site score


  // Score splice acceptor site
  if ( exon_start - 3 >= 0 )
    if ( genomic.charAt ( exon_start - 3 ) == 'C' )  score++;

  if ( exon_start - 2 >= 0 )
    if ( genomic.charAt ( exon_start - 2 ) == 'A' )  score++;

  if ( exon_start - 1 >= 0 )
    if ( genomic.charAt ( exon_start - 1 ) == 'G' )  score++;

  return score;
}  // method scoreAcceptor


/******************************************************************************/
private int scoreDonor ( String genomic, int exon_end )
{
  int score = 0;				// splice site score


  // score splice donor site
  if ( exon_end - 1 >= 0 )
    if ( genomic.charAt ( exon_end - 1 ) == 'A' )  score++;

  if ( genomic.charAt ( exon_end     ) == 'G' )  score++;

  if ( exon_end + 1 < genomic.length () );
    if ( genomic.charAt ( exon_end + 1 ) == 'G' )  score++;

  if ( exon_end + 2 < genomic.length () );
    if ( genomic.charAt ( exon_end + 2 ) == 'T' )  score++;

  if ( exon_end + 3 < genomic.length () );
    if ( genomic.charAt ( exon_end + 3 ) == 'A' )  score++;

  if ( exon_end + 4 < genomic.length () );
    if ( genomic.charAt ( exon_end + 4 ) == 'A' )  score++;

  if ( exon_end + 5 < genomic.length () );
    if ( genomic.charAt ( exon_end + 5 ) == 'G' )  score++;

  if ( exon_end + 6 < genomic.length () );
    if ( genomic.charAt ( exon_end + 6 ) == 'T' )  score++;

  return score;
}  // method scoreDonor


/******************************************************************************/
private int scoreSplice ( String genomic, int exon_end, int next_start )
{
  return scoreDonor ( genomic, exon_end ) + scoreAcceptor ( genomic, next_start );
}  // method scoreSplice


/******************************************************************************/
// Compute the optimum length of this exon.
private int spliceOptimize 
    ( String genomic				// Genomic sequence
    , int length				// length of current exon
    , int exon_end				// end of current exon
    , int next_start 				// start of next exon
    )
{
  int new_length = length;			// next length of current exon

  // Score the 3' most possibility.
  int score = scoreSplice ( genomic, exon_end, next_start );

  // Evaluate alternatives in the 5' direction.
  int i = 1;
  while ( genomic.charAt ( exon_end - i + 1 ) == genomic.charAt ( next_start - i ) )
  {
    int new_score = scoreSplice ( genomic, exon_end - i, next_start - i );
    if ( new_score > score )
    {
      new_length = length - i;
      score = new_score;
    }  // if

    i++;
  }  // while

  return new_length;
}  // method spliceOptimize


/******************************************************************************/
// Compute the optimum length of this exon.
private int spliceOptimizeDonor
    ( String genomic				// Genomic sequence
    , int length				// length of current exon
    , int exon_end				// end of current exon
    , int limit					// length of region to examine
    )
{
  int new_length = length;			// next length of current exon
  int score = 0;				// Donor splice site score


  // Scan starting at two bases past the candidate end of exon.
  int i = -2;
  if ( exon_end - 2 >= genomic.length () )  i = -1;
  if ( exon_end - 1 >= genomic.length () )  i = 0;

  // Evaluate alternatives in the 5' direction.
  for ( ; i < limit; i++ )
  {
    // Score the candidate exon donor site.
    int new_score = scoreDonor ( genomic, exon_end - i );

    if ( new_score > score )
    {
      new_length = length - i;
      score = new_score;
    }  // if
  }  // for

  return new_length;
}  // method spliceOptimizeDonor


/******************************************************************************/
// Compute the optimum length of this exon.
private int spliceOptimizeRegion 
    ( String genomic				// Genomic sequence
    , int length				// length of current exon
    , int exon_end				// end of current exon
    , int next_start 				// start of next exon
    , int limit					// length of region to examine
    )
{
  int new_length = length;			// best length of current exon
  int score = 0;


  // Evaluate alternatives in the 5' direction.
  for ( int i = 0; i < limit; i++ )
  {
    int new_score = scoreSplice ( genomic, exon_end - i, next_start - i );
    if ( new_score > score )
    {
      new_length = length - i;
      score = new_score;
    }  // if
  }  // for

  return new_length;
}  // method spliceOptimizeRegion


/******************************************************************************/
public void align ( String genomic, String cdna )
{
  int  length = 0;
  int  g_start = 0;
  int  c_start = 0;
  int  window = 12;


  // Create an upper case genomic sequence.
  String genomic_upper = genomic.toUpperCase ();

  // Add the genomic sequence to the alignment.
  Segment genomic_segment = new Segment ();
  genomic_segment.setSequenceName ( "genomic" );
  genomic_segment.setSequenceStart ( 0 );
  genomic_segment.setSequenceEnd ( genomic.length () - 1 );
  genomic_segment.setSequenceSegment ( genomic );
  genomic_segment.setAlignmentStart ( 0 );
  genomic_segment.setAlignmentEnd ( genomic.length () - 1 );
  alignment.add ( genomic_segment );

  while ( g_start < genomic.length () )
  {
    g_start = g_start + length;
    c_start = c_start + length;

    // Check if no more sequences to align.
    if ( ( c_start >= cdna.length () ) ||
         ( g_start >= genomic.length () ) )  return;

    boolean found = false;
    while ( ( c_start < cdna.length () - window ) && ( found == false ) )
    {
      // Search for the start of the cDNA sequence.
      int start = genomic.indexOf ( cdna.substring ( c_start, c_start + window ), g_start );

      // Check if found.
      if ( start != -1 )
      {
        found = true;
        g_start = start;
      }  // if
      else
        c_start++;
    }  // while

    // Check if not found.
    if ( found == false )  return;

    // Extend the alignment
    length = extent ( genomic.substring ( g_start ), cdna.substring ( c_start ) );

    if ( length <= 0 )  return;

    // Optimize for alignment with splice consensus sequences.
    if ( c_start + length < cdna.length () )
    {
      int next_start = genomic.indexOf ( 
          cdna.substring ( c_start + length, c_start + length + window ), g_start + length );

      // Compute the optimum length of this exon.
      length = 
          spliceOptimize ( genomic_upper, length, g_start + length - 1, next_start );
    }  // if

    Segment cdna_segment = new Segment ();
    cdna_segment.setSequenceName ( "cDNA" );
    cdna_segment.setSequenceStart ( c_start );
    cdna_segment.setSequenceEnd ( c_start + length - 1 );
    cdna_segment.setSequenceSegment ( cdna.substring ( c_start, c_start + length ) );
    cdna_segment.setAlignmentStart ( g_start );
    cdna_segment.setAlignmentEnd ( g_start + length - 1 );
    alignment.add ( cdna_segment );
  }  //
}  // method align


/******************************************************************************/
public void printAlignment ()
{
  System.out.println ( "Alignment:" );
  System.out.println ();

  // Get the alignment segments.
  Segment segs [] = new Segment [ alignment.size () ];
  for ( int i = 0; i < alignment.size (); i++ )
    segs [ i ] = (Segment) alignment.get ( i );

  // Print out the alignment.
  for ( int i = 0; i < segs [ 0 ].getAlignmentEnd (); i += 50 )
  {
    int print_end = i + 50 - 1;
    System.out.println ( "\t" + (i+1) );

    // Print out each alignment segment.
    for ( int j = 0; j < segs.length; j++ )
    {
      int align_start = segs [ j ].getAlignmentStart ();
      int align_end   = segs [ j ].getAlignmentEnd ();

      if ( ( ( print_end >= align_start ) && ( print_end <= align_end ) ) ||
           ( ( i >= align_start ) && ( i <= align_end ) ) ) 
      {
        System.out.print ( segs [ j ].getSequenceName () );
        System.out.print ( "\t" );

        // Print out the sequence bases.
        String sequence = segs [ j ].getSequenceSegment ();
        for ( int k = i; k < i+50; k++ )
        { 
          if ( ( k >= align_start ) && ( k <= align_end ) )
            System.out.print ( sequence.charAt ( k - align_start ) ); 
          else
            System.out.print ( " " ); 

          if ( (k + 1) % 10 == 0 )  System.out.print ( " " );
        }  // for k
        System.out.println (); 
      }  // if
    }  // for j

    System.out.println ();
  }  // for i
}  // method printAlignment


/******************************************************************************/
public void printCoordinates ()
{
  System.out.println ( "Alignment Coordinates:" );
  System.out.println ();

  for ( int i = 0; i < alignment.size (); i++ )
  {
    Segment segment = (Segment) alignment.get ( i );

    System.out.print ( "Genomic: [" + (segment.getAlignmentStart () + 1) );
    System.out.print ( "," + (segment.getAlignmentEnd () + 1) + "] " );
    System.out.print ( "seq:     [" + (segment.getSequenceStart () + 1) );
    System.out.print ( "," + (segment.getSequenceEnd () + 1) + "] " );
    System.out.println ();
  }  // for
}  // method printCoordinates


/******************************************************************************/
// Find the promoter sequence patterns.
public void findPromoterElements ( String genomic, int gene_start )
{
  int promoter_start = gene_start - 1500;
  if ( promoter_start < 0 )  promoter_start = 0;

  int promoter_end = gene_start - 25;
  if ( promoter_end < 0 )  promoter_end = 0;

  // Find the TATA box.
  int start;
  start = SeqTools.findTATAbox ( genomic, promoter_start, promoter_end );

  if ( start >= 0 )  
    System.out.println ( "TATA box found at " + start );

  start = SeqTools.findBestPattern ( genomic, promoter_start, promoter_end, "TATAAAA" );
  if ( start >= 0 )  
    System.out.println ( "TATA box found at " + start + " using findBestPattern" );


  // Find the CAAT box.
  start = SeqTools.findBestPattern ( genomic, promoter_start, promoter_end, "GGNCAATCT" );

  if ( start >= 0 )  
    System.out.println ( "CAAT box found at " + start );

  // Find a GC box.
  start = SeqTools.findBestPattern ( genomic, promoter_start, promoter_end, "GGGCGG" );

  if ( start >= 0 )  
    System.out.println ( "GC box found at " + start );

}  // method findPromoterElements


/******************************************************************************/
public void alignPep ( String genomic, String peptide )
{
  int  frame_index = 0;				// current translation frame
  int  length = 0;				// length of alignment
  int  g_start = 0;				// genomic sequence start
  int  p_start = 0;				// peptide segment start

  Match match = new Match ();			// current alignment segment

  // Create an upper case genomic sequence.
  String genomic_upper = genomic.toUpperCase ();

  String frame [] = new String [ 3 ];			// genomic translation frames

  // Create a 3 frame translation of the DNA sequence.
  frame [ 0 ] = SeqTools.translate3 ( genomic );
  frame [ 1 ] = "X" + SeqTools.translate3 ( genomic.substring ( 1 ) );
  frame [ 2 ] = "XX" + SeqTools.translate3 ( genomic.substring ( 2 ) );

  while ( g_start < genomic.length () )
  {
    match.clear ();

    // Find the start of the first identity region.
    g_start = match.findStart ( frame, g_start, peptide, p_start );
    frame_index = match.getFrameIndex ();
    length = match.getAlignLength ();

    // Find the promoter sequence patterns.
    if ( p_start == 0 )  findPromoterElements ( genomic, g_start );

    // Check if no more sequences to align.
    if ( ( p_start >= frame [ 0 ].length () ) ||
         ( g_start >= genomic.length () ) ||
         ( g_start == -1 ) ||
         ( length <= 0 ) )  return;

    // Don't search for the donor splice site at the end of the peptide.
    if ( p_start + length < peptide.length () - 1 )

      // Optimize for alignment with donor splice consensus sequences.
      length = 
          spliceOptimizeDonor
              ( genomic_upper
              , length
              , g_start + length - 1
              , 15 // region window size  
              );

    Segment peptide_segment = new Segment ();
    peptide_segment.setSequenceName ( "peptide" );
    peptide_segment.setSequenceStart ( p_start );
    peptide_segment.setSequenceEnd ( p_start + length - 1 );
    peptide_segment.setSequenceSegment ( peptide.substring ( p_start, p_start + length ) );
    peptide_segment.setAlignmentStart ( g_start );
    peptide_segment.setAlignmentEnd ( g_start + length - 1 );
    alignment.add ( peptide_segment );

    g_start = g_start + length;
    p_start = p_start + length;
  }  // while
}  // method alignPep


/******************************************************************************/
  public static void main ( String [] args )
  {
    Align application = new Align ();

    String seq = "ATGTTTCTTATTGTTTCTCCTACTGCTTATTAATAGCATCAAAATAAAGATGAATGTTGATGGCGTAGTAGAGGT";
    application.setSequence1 ( seq );

//  seq = "ATGTTTCTTATTGTTTCTCCTACTGCTTATTAATAGCATCAAAATAAAGATGAATGTTGATGGCGTAGTAGAGGT";
//  DNA = "ATGTTTCTTATT........TACTGCTTATTAA........AAATAAAGATGA........GGCGTAGTAGAGG";
//                   1         2         3         4         5         6         7
//         012345678901234567890123456789012345678901234567890123456789012345678901234567890

    String cDNA = "ATGTTTCTTATTTACTGCTTATTAAAAATAAAGATGAGGCGTAGTAGAGG";
    application.setSequence2 ( cDNA );

    application.align ( seq, cDNA );
  }  // method main


/******************************************************************************/

}  // class Align

