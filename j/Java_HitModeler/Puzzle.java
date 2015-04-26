
import java.util.Vector;

// import Align;
// import Elements;
// import ExonTable;
// import HitRegion;
// import Piece;
// import SequenceTable;
// import Similarity;
// import Transcript;

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

public class Puzzle extends Object
{


/******************************************************************************/

  private  static  final  int MAX_EXON_GAP = 20;	// Maximum gap between target hits


/******************************************************************************/

  private   Elements elements = null;				// gene structure elements

  private   boolean  full_length = false;			// full-length mRNA indicator

  private   Vector  new_exons = null;				// new exons

  private   String  new_mRNA = "";				// spliced mRNA sequence

  private   String  new_protein = null;				// translate mRNA sequence

  private   Piece [] pieces = null;				// main alignment pieces/segments

  private   HitRegion  region = null;				// main hit region


/******************************************************************************/

  private  int  last_mRNA_base = 0;				// last mRNA base pair number

  private  Piece  previous_piece = null;			// Previous piece

  private  HitRegion  previous_region = null;			// Previous region


/******************************************************************************/
  // Constructor Puzzle
  public Puzzle ()
  {
    initialize ();
  }  // constructor Puzzle


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    elements = null;
    full_length = false;
    new_exons = null;
    new_mRNA = "";
    new_protein = null;
    pieces = null;
    region = null;

    previous_piece = null;
    previous_region = null;
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    if ( ( pieces != null ) && ( pieces.length > 0 ) )
    {
      for ( int p = 0; p < pieces.length; p++ )
        if ( pieces [ p ] != null )
        {
          pieces [ p ].close ();
          pieces [ p ] = null;
        }  // if
    }  // if

    if ( region != null )  region.close ();

    initialize ();
  }  // method close


/******************************************************************************/
  // This method clears all exons.
  private void clearExons ( ExonTable [] exons )
  {
    if ( ( exons == null ) || ( exons.length <= 0 ) )  return;

    for ( int e = 0; e < exons.length; e++ )

      if ( exons [ e ] != null )

        exons [ e ].initialize ();
  }  // method clearExons


/******************************************************************************/
  public boolean getFullLength ()
  {
    return full_length;
  }  // method getFullLength


/******************************************************************************/
  public String getNewMrna ()
  {
    return new_mRNA;
  }  // method getNewMrna


/******************************************************************************/
  public Piece [] getPieces ()
  {
    return pieces;
  }  // method getPieces


/******************************************************************************/
  public Elements getElements ()
  {
    return elements;
  }  // method getElements


/******************************************************************************/
  public HitRegion getRegion ()
  {
    return region;
  }  // method getRegion


/******************************************************************************/
  public boolean isFullLength ()
  {
    return full_length;
  }  // method isFullLength


/******************************************************************************/
  public void setFullLength ( boolean value )
  {
    full_length = value;
  }  // method setFullLength


/******************************************************************************/
  public void setPieces ( Piece [] value )
  {
    pieces = value;
  }  // method setPieces


/******************************************************************************/
  public void setElements ( Elements value )
  {
    elements = value;
  }  // method setElements


/******************************************************************************/
  public void setRegion ( HitRegion value )
  {
    region = value;
  }  // method setRegion


/******************************************************************************/
  private StringBuffer checkForGap
      ( StringBuffer  mRNA 
      , Piece         piece
      , HitRegion     region
      )
  {
    int n_length = 0;		// number of n's bases to add
    int query_begin = 0;	// start of query region
    int query_end   = 0;	// end of query region

    // Check for no previous piece.
    if ( previous_piece == null )  return mRNA;

    // Check for long query gap.
    int gap_aa_size = 0;
    if ( ( region.getTargetAaMinimum () > 0 ) &&
         ( previous_region.getTargetAaMaximum () > 0 ) )

      gap_aa_size = region.getTargetAaMinimum () - previous_region.getTargetAaMaximum ();

    int gap_dna_size = 0;
    if ( ( region.getTargetDnaMinimum () > 0 ) &&
         ( previous_region.getTargetDnaMaximum () > 0 ) )

      gap_dna_size = region.getTargetDnaMinimum () - previous_region.getTargetDnaMaximum ();

    query_begin = previous_region.getTargetDnaMaximum () + 1;
    query_end   = region.getTargetDnaMinimum () - 1;

/*
System.out.println ( "checkForGap: gap_aa_size * 3 = " + (gap_aa_size*3) +
    ", gap_dna_size = " + gap_dna_size );
*/

    // Check which gap is smaller.
    if ( gap_aa_size * 3 < gap_dna_size )
    {
      // Check for a valid AA gap.
      if ( ( region.getTargetAaMinimum () > 0 ) &&
           ( previous_region.getTargetAaMaximum () > 0 ) )
      {
        // Only insert N's if the gap is large enough.
        if ( gap_aa_size >= 20 )
        {
          n_length = gap_aa_size * 3;
          query_begin = previous_region.getTargetAaMaximum () + 1;
          query_end   = region.getTargetAaMinimum () - 1;
        }  // if
      }  // if
      else
      {
        // Check for a valid DNA gap.
        if ( ( region.getTargetDnaMinimum () > 0 ) &&
             ( previous_region.getTargetDnaMaximum () > 0 ) )
        {
          // Only insert N's if the gap is large enough.
          if ( gap_dna_size >= 60 )  n_length = gap_dna_size;
        }  // if
      }  // else
    }
    else  // dna gap size is smaller
    {
      // Check for a valid DNA gap.
      if ( ( region.getTargetDnaMinimum () > 0 ) &&
           ( previous_region.getTargetDnaMaximum () > 0 ) )
      {
        // Only insert N's if the gap is large enough.
        if ( gap_dna_size >= 60 )  n_length = gap_dna_size;
      }  // if
      else
      {
        // Check for a valid AA gap.
        if ( ( region.getTargetAaMinimum () > 0 ) &&
             ( previous_region.getTargetAaMaximum () > 0 ) )
        {
          // Only insert N's if the gap is large enough.
          if ( gap_aa_size >= 20 )
          {
            n_length = gap_aa_size * 3;
            query_begin = previous_region.getTargetAaMaximum () + 1;
            query_end   = region.getTargetAaMinimum () - 1;
          }  // if
        }  // if
      }  // else
    }  // else

    if ( n_length > 0 )
    {
      System.out.println ( "\tTarget gap [" + query_begin + "-" +
          query_end + "] " + n_length + " N's added" );

      // Create a new exon record.
      ExonTable exon = new ExonTable ();

      exon.setGenomicStrand ( piece.getGenomicStrand () );
      exon.setProgramName ( "Gap" );
      // exon.setExonType ( "Gap" );
      exon.setMrnaStart ( query_begin );
      exon.setMrnaEnd   ( query_end );
      exon.setGenomicStart ( previous_region.getGenomicMaximum () + 1 );
      exon.setGenomicEnd ( region.getGenomicMinimum () - 1 );

      // Add the new exon to the list of exons for this Transcript.
      new_exons.add ( exon );

      for ( int n = 0; n < n_length; n++ )
        mRNA.append ( 'n' );

      return mRNA;
    }  // if

    // Check for splicing frame alignment problem.
    Frame [] frames5 = previous_piece.getFrames ();
    Frame [] frames3 = piece.getFrames ();

    if ( ( frames5 == null ) || ( frames3 == null ) )  return mRNA;
    if ( ( frames5.length <= 0 ) || ( frames3.length <= 0 ) )  return mRNA;

    String bases5 = "";
    String bases3 = "";
    boolean splices = false;
    for ( int f5 = 0; f5 < frames5.length; f5++ )

      // Check for an open frame.
      if ( frames5 [ f5 ] != null )
      {
        bases5 = frames5 [ f5 ].getBases5 ();
  
        for ( int f3 = 0; f3 < frames3.length; f3++ )
  
          // Check for an open frame.
          if ( frames3 [ f3 ] != null )
          {
            bases3 = frames3 [ f3 ].getBases3 ();
    
            // Check if the two frames can splice together.
            if ( ( ( bases5.length () + bases3.length () ) % 3 ) == 0 )
    
              splices = true;
          }  // if
      }  // for

    // Check if no splicing solution found.
    if ( splices == false )
    {
      int count = ( bases5.length () + bases3.length () ) % 3;

      while ( ( count % 3 ) > 0 )
      {
        mRNA.append ( "n" );
        count++;
      }  // while
    }  // if

    return mRNA;
  }  // method checkForGap


/******************************************************************************/
  private void checkForStopCodon 
      ( Piece []   pieces
      , int        first
      , int        last
      , HitRegion  region 
      )
  {
    // System.out.println ( "checkForStopCodon: # pieces = " + pieces.length + " first = " + first + ", last = " + last );

    for ( int p = first; p <= last; p++ )
    {
      int stop_codon = pieces [ p ].checkForStopCodon ();

      // System.out.println ( "checkForStopCodon: stop_codon = " + stop_codon );

      if ( stop_codon > 0 )
      {
         if ( elements == null )  elements = new Elements ();
         
         if ( pieces [ p ].getGenomicStrand () == '+' )
         {
           String sequence = pieces [ p ].getGenomicSeq ().getSequence ();
           elements.setEndSequence ( sequence );
         }  // else
         else  // reverse strand
         {
           String reverse = SeqTools.reverseSequence 
               ( pieces [ p ].getGenomicSeq ().getSequence () );
           elements.setEndSequence ( reverse );
         }  // else

         elements.setStopCodon ( stop_codon );

         // Search for the polyA site.
         elements.findPolyASite ( pieces [ p ].getExonEnd () );
              
         if ( pieces [ p ].getGenomicStrand () == '-' )
              
           elements.reverseEndCoordinates ();
      }  // if
    }  // for
  }  // method checkForStopCodon


/******************************************************************************/
  // This method adds the pieces to the mRNA using the lock sequence.
  private StringBuffer addExon 
      ( StringBuffer  mRNA 
      , Piece []      pieces
      , int           first
      , int           last
      )
  {
    // Save the length of the mRNA.
    int mRNA_length = mRNA.length ();

    // Append the new exon piece.
    int begin = pieces [ first ].getExonBegin ();
    int end   = pieces [ last  ].getExonEnd ();

    if ( end <= begin )
    {
      System.out.println ( "Puzzle.addExon: first = " + first + ", last = " + last );
      System.out.println ( "Puzzle.addExon: begin = " + begin + ", end = " + end );
      System.out.println ( "*Warning* exon with poor coordinates!" );
      return mRNA;
    }  // if

    mRNA.append ( pieces [ first ].getExon ( begin, end ) );

    // Create a new exon record.
    ExonTable exon = new ExonTable ();

    char strand = pieces [ first ].getGenomicStrand ();
    exon.setGenomicStrand ( strand );
    exon.setProgramName ( "Model" );

    if ( strand == '+' )
    {
      exon.setGenomicStart ( begin+1 );
      exon.setGenomicEnd ( end+1 );
    }  // if
    else  // reverse the coordinates
    {
      int length = pieces [ first ].getGenomic ().getLength ();
      exon.setGenomicStart ( length - end + 1 );
      exon.setGenomicEnd ( length - begin + 1);
    }  // else

    last_mRNA_base++;
    exon.setMrnaStart ( pieces [ first ].getTargetBegin () );
    last_mRNA_base += end - begin;
    exon.setMrnaEnd ( pieces [ last ].getTargetEnd () );
   
    exon.setExonBeginSplice ( pieces [ first ].getExonBeginSplice () );
    exon.setExonEndSplice ( pieces [ last ].getExonEndSplice () ); 

    exon.setFrameBegin ( pieces [ first ].getFrameBegin ( begin ) );
    exon.setFrameEnd ( pieces [ last ].getFrameEnd ( end ) );

    Evidence evidence = new Evidence ();
    evidence.setBegin ( begin );
    evidence.setEnd   ( end );
    evidence.setDescription ( "similarity" );
    exon.setEvidence ( evidence );

    // Add the new exon to the list of exons for this Transcript.
    new_exons.add ( exon );

    System.out.println ( "Puzzle.addExon:" );
    exon.print ();

    return mRNA;
  }  // method addExon


/******************************************************************************/
  private void checkForFrameShifts ()
  {
    // Check for too few pieces.
    if ( ( pieces == null ) || ( pieces.length < 2 ) )  return;

    for ( int p = 0; p < pieces.length - 1; p++ )
    {
      // Check for two pieces to merge together.
      if ( ( pieces [ p ].getTargetEnd () + 10 >= pieces [ p + 1 ].getTargetBegin () ) &&
           ( pieces [ p ].getTargetEnd () - 10 <= pieces [ p + 1 ].getTargetBegin () ) )
      {
        System.out.println ( "!!! Likely frameshift between pieces [" + p +
            " and " + (p+1) );
      }  // if
    }  // for
  }  // method checkForFrameShifts


/******************************************************************************/
  private String [] getLockSequences ()
  {
    // Collect the frame lock sequences.
    Vector frame_locks = new Vector ();

    for ( int p = 0; p < pieces.length; p++ )
    {
      if ( ( pieces [ p ].getPieceType ().equals ( "TBLASTN" ) == true ) ||
           ( pieces [ p ].getPieceType ().equals ( "BLASTX" ) == true ) )

          frame_locks.add ( pieces [ p ].getFrameLock () );
    }  // for

    // Collect the translation sequences.
    String [] locks = new String [ frame_locks.size () ];

    if ( frame_locks.size () > 0 )
    {
      for ( int l = 0; l < frame_locks.size (); l++ )

        locks [ l ] = (String) frame_locks.elementAt ( l );
    }  // if

    frame_locks.removeAllElements ();
    frame_locks = null;

    return locks;
  }  // method getLockSequences


/******************************************************************************/
  private String makeSubName ( Piece piece, HitRegion hit_region )
  {
    String sub_name = "";

    // Add the genomic sequence name.
    if ( piece != null )
    {
      // Check that the genomic sequence is set.
      if ( piece.getTarget () != null )

        sub_name = piece.getTarget ().getSequenceName ();
    }  // if

    // Add the genomic coordinates of the hit region.
    if ( ( sub_name.length () > 0 ) && ( hit_region != null ) )
    {
      sub_name += "_" + (hit_region.getGenomicMinimum ()+1) + 
                  "-" + (hit_region.getGenomicMaximum ()+1);
    }  // if

    return sub_name;
  }  // method makeSubName


/******************************************************************************/
  private String makeTranscriptName ()
  {
    String name = "";
    if ( ( name != null ) && ( name.length () > 0 ) )  return name;

    // Make a name from the genomic sequences.
    if ( ( pieces != null ) && ( pieces.length > 0 ) )
      name += makeSubName ( pieces [ 0 ], region );

    // System.out.println ( "New transcript name: " + name );

    return name;
  }  // method makeTranscriptName


/******************************************************************************/
public void searchForPromoterElements ( Piece piece )
{
  // Check for near the start of a transcript.
  if ( previous_region.getTargetAaMinimum () > 50 )
  {
    if ( ( previous_region.getTargetDnaMinimum () > 150 ) ||
         ( previous_region.getTargetDnaMinimum () == 0 ) )

      return;
  }  // if

  if ( previous_region.getTargetDnaMinimum () > 150 )
  {
    if ( ( previous_region.getTargetAaMinimum () > 50 ) ||
         ( previous_region.getTargetAaMinimum () == 0 ) )

      return;
  }  // if

  int end = piece.getExonBegin ();
  int start = end - 2000;
  if ( start < 0 )  start = 0;

  elements = new Elements ();
  elements.setPromoterSequence ( piece.getTarget () );

  if ( piece.getGenomicStrand () == '+' )
  {
    String sequence = piece.getGenomicSeq ().getSequence ();
    elements.setSequence ( sequence );
    elements.findPromoterElements ( sequence, start - 1 );
    int start_codon = piece.checkForStartCodon ();
    if ( ( start_codon + 50 > end ) && ( start_codon >= 0 ) )
    {
      elements.setStartCodon ( start_codon );
    }  // if
  }
  else  // reverse strand
  {
    String reverse = SeqTools.reverseSequence ( piece.getGenomicSeq ().getSequence () );
    elements.setSequence ( reverse );
    elements.findPromoterElements ( reverse, reverse.length () - end );
    int start_codon = piece.checkForStartCodon ();
    if ( ( start_codon + 50 > end ) && ( start_codon >= 0 ) ) 
      elements.setStartCodon ( start_codon );
    elements.reversePromoterCoordinates ();
  }  // else
}  // method searchForPromoterElements


/******************************************************************************/
  private String buildSpliceInfo () 
  {
    StringBuffer splice_info = new StringBuffer ( 160 );

    // Assert: new exons
    if ( new_exons.size () <= 0 )  return "";

    ExonTable exon = (ExonTable) new_exons.elementAt ( 0 );
    ExonTable previous_exon = null;

    if ( exon.getGenomicStrand () == '+' )
    {
      // Check for start codon.
      if ( elements != null )

        if ( elements.getStartCodon () > 0 )  splice_info.append ( "$" );

      // Cycle through the exons.
      for ( int e = 0; e < new_exons.size (); e++ )
      {
        exon = (ExonTable) new_exons.elementAt ( e );

        // Check previous exon.
        if ( previous_exon != null )
        {
          // Check the distance between exons.
          if ( exon.getMrnaStart () - previous_exon.getMrnaEnd () > MAX_EXON_GAP )
          {
            if ( previous_exon.isExonEndSplice () == true )
              splice_info.append ( "|" );
            else
              splice_info.append ( ")" );

            splice_info.append ( "..." );
          }  // if

          if ( exon.isExonBeginSplice () == true )
            splice_info.append ( "|" );
          else
            splice_info.append ( "(" );
        }  // if

        splice_info.append ( exon.getGenomicStart () + "-" + 
                             exon.getGenomicEnd   () );
        
        previous_exon = exon;
      }  // for

      // Check for stop codon.
      if ( elements != null )

        if ( elements.getStopCodon () > 0 )  splice_info.append ( "*" );
    }  // if
    else  // complement strand
    {
      // Check for stop codon.
      if ( elements != null )

        if ( elements.getStopCodon () > 0 )  splice_info.append ( "*" );

      // Cycle through the exons.
      for ( int e = new_exons.size () - 1; e >= 0; e-- )
      {
        exon = (ExonTable) new_exons.elementAt ( e );

        // Check previous exon.
        if ( previous_exon != null )
        {
          // Check the distance between exons.
          if ( exon.getMrnaStart () - previous_exon.getMrnaEnd () > MAX_EXON_GAP )
          {
            if ( previous_exon.isExonEndSplice () == true )
              splice_info.append ( "|" );
            else
              splice_info.append ( ")" );

            splice_info.append ( "..." );
          }  // if

          if ( exon.isExonBeginSplice () == true )
            splice_info.append ( "|" );
          else
            splice_info.append ( "(" );
        }  // if

        splice_info.append ( exon.getGenomicStart () + "-" + 
                             exon.getGenomicEnd   () );
        
        previous_exon = exon;
      }  // for

      // Check for start codon.
      if ( elements != null )

        if ( elements.getStartCodon () > 0 )  splice_info.append ( "$" );
    }  // else

    System.out.println ();
    System.out.println ( "splice_info: " + splice_info.toString () );

    return splice_info.toString ();
  }  // method buildSpliceInfo


/******************************************************************************/
  private void sortPieces ( Piece [] pieces )
  {
    // Check for no pieces to sort.
    if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return;

    // Sort the pieces.
    for ( int i = 0; i < pieces.length - 1; i++ )

      for ( int j = i + 1; j < pieces.length; j++ )

        if ( pieces [ i ].getExonBegin () > pieces [ j ].getExonBegin () )
        {
          // Switch the position of the pieces.
          Piece temp   = pieces [ i ];
          pieces [ i ] = pieces [ j ];
          pieces [ j ] = temp;
        }  // if

    // Print out the order of the pieces.
    System.out.println ();
    System.out.println ( "sortPieces: " + pieces [ 0 ].getTargetSequenceName () );
    for ( int i = 0; i < pieces.length; i++ )

      System.out.println ( "\t" + (i+1) + " exon[" 
          + pieces [ i ].getExonBegin () + "-" 
          + pieces [ i ].getExonEnd () + "] "
          + pieces [ i ].getGenomicStrand () 
          + " Genomic[" + pieces [ i ].getGenomicBegin () + "-"
          + pieces [ i ].getGenomicEnd () + "]"
          + " Target[" + pieces [ i ].getTargetBegin () + "-"
          + pieces [ i ].getTargetEnd () + "] "
          + pieces [ i ].getPieceType ()
          );
    System.out.println ();

  }  // method sortPieces


/******************************************************************************/
  private void splicePieces ( Piece [] pieces, HitRegion region )
  {
    // Check for no pieces.
    if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return;

    // System.out.println ( "Puzzle.splicePieces called" );
    // System.out.println ( "Puzzle.splicePieces # pieces = " + pieces.length );

    StringBuffer mRNA = new StringBuffer ( 4096 );

    // Sort the pieces.
    sortPieces ( pieces );

    int first = 0;
    int last  = 0;

    // Splice together the pieces.
    while ( first < pieces.length )
    {
      int exon_end = pieces [ last ].getExonEnd ();
      int best_last = last;

      // Join multiple pieces when possible.
      while ( ( last + 1 < pieces.length ) &&
              ( pieces [ last + 1 ].getTargetSequenceName () ==
                pieces [ first ].getTargetSequenceName () ) &&
              ( pieces [ last + 1 ].getGenomicStrand () ==
                pieces [ first ].getGenomicStrand () ) &&
              ( pieces [ last + 1 ].getExonBegin () < exon_end - 20 ) )
      {
        last++;

        if ( pieces [ last ].getExonEnd () > exon_end )
        {
          exon_end = pieces [ last ].getExonEnd ();
          best_last = last;
        }  // if
      }  // while

      // Compute the current hit region.
      HitRegion current_region = new HitRegion ();
      current_region.computeHitRegion ( pieces, first, last );

      // Check for gaps in the query sequence.
/*
      if ( previous_piece != null )
        mRNA = checkForGap ( mRNA, pieces [ first ], current_region );
*/
      // Check for the stop codon.
      if ( last + 1 == pieces.length )

        checkForStopCodon ( pieces, first, last, region );

      // Check for the first exon.
      previous_region = current_region;
      if ( previous_piece == null )

        searchForPromoterElements ( pieces [ first ] );

      // Append this exon.
      mRNA = addExon ( mRNA, pieces, first, best_last );

      first = last + 1;

      // Skip pieces that overlap.
      while ( ( first < pieces.length ) &&
              ( pieces [ first ].getExonBegin () < exon_end ) )

        first++;

      previous_piece = pieces [ last ];
      last = first;
    }  // while

    new_mRNA += mRNA.toString ();
  }  // method splicePieces


/******************************************************************************/
  private void translateMrna ()
  {
    // Check for no pieces.
    if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return;

    // Check for no mRNA.
    if ( ( new_mRNA == null ) || ( new_mRNA.length () <= 0 ) )  return;

    Genomic mrna = new Genomic ();
    mrna.setSequence ( new_mRNA );

    String [] locks = getLockSequences ();

    if ( locks.length > 0 )

      new_protein = mrna.getLockedORF ( locks );

    else
    {
      new_protein = mrna.getBestORF ();

      // Check if a long enough peptide was returned (60% mRNA length minimum).
      int min_length = ( ( new_mRNA.length () * 20 ) / 100 );
      if ( new_protein.length () < min_length )  new_protein = "";
    }  // else
  }  // method translateMrna


/******************************************************************************/
  public Transcript model ()
  {
    System.out.println ( "Puzzle.model" );

    // Check if there is no pieces.
    if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return null;

    // Create a vector to represent the new exons.
    new_exons = new Vector ();

    // Check for Frameshifts.
    // checkForFrameShifts ();

    // Splice the exon pieces together.
    splicePieces ( pieces, region );

    Transcript transcript = new Transcript ();
    transcript.setMrnaSequence ( new_mRNA );
    transcript.setTranscriptType ( "similarity" );
    transcript.setExons ( new_exons );		// updated by splicePieces
    transcript.setStrand ( pieces [ 0 ].getGenomicStrand () );
    transcript.setName ( makeTranscriptName () );

    transcript.setSpliceInfo ( buildSpliceInfo () );

    // Translate the transcript.
    translateMrna ();

    // transcript.setProteinSequence ( new_protein );
    SequenceTable protein = new SequenceTable ();
    protein.setSequence ( new_protein );

    transcript.setProteinSequence ( protein );

    transcript.setElements ( elements );

    return transcript;
  }  // method model


/******************************************************************************/

}  // class Puzzle
