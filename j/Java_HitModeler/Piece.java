
// import Genomic;
// import SequenceTable;
// import Hit;

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

public class Piece extends Object
{


/******************************************************************************/

  private   int  exon_begin = 0;				// exon begin

  private   boolean  exon_begin_splice = false;			// 5' splice site flag

  private   int  exon_end = 0;					// exon end

  private   boolean  exon_end_splice = false;			// 3' splice site flag

  private   String  frame_lock = null;				// frame lock sequence

  private   Frame [] frames = null;				// open reading frames

  private   Genomic  genomic = null;				// genomic sequence

  private   char  genomic_strand = '+';				// genomic strand

  private   SequenceTable  target = null;			// target sequence

  private   String target_alignment = "";			// target alignment

  private   int  target_begin = 0;				// target begin

  private   int  target_end = 0;				// target end

  private   String target_piece = "";				// target piece

  private   String target_sequence_name = "";			// target sequence name

  private   int  max_end = 0;					// maximum end

  private   int  min_begin = 0;					// minimum begin

  private   SequenceTable  genomic_seq = null;			// genomic sequence

  private   String  genomic_alignment = "";			// genomic alignment

  private   int  genomic_begin = 0;				// genomic begin

  private   int  genomic_end = 0;				// genomic end

  private   String  genomic_piece = "";				// genomic_piece

  private   int  mRNA_begin = 0;				// mRNA begin

  private   int  mRNA_end = 0;					// mRNA end

  private   String  piece_type = "";				// type of piece

  private   long  genomic_sequence_id = 0L;			// genomic_sequence_id

  private   Hit  similarity = null;			// similarity


/******************************************************************************/
  // Constructor Piece
  public Piece ()
  {
    initialize ();
  }  // constructor Piece


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    exon_begin = 0;
    exon_begin_splice = false;
    exon_end = 0;
    exon_end_splice = false;
    frames = null;
    frame_lock = null;
    genomic = null;
    genomic_strand = '+';
    // target = null;
    target_alignment = "";
    target_begin = 0;
    target_end = 0;
    target_piece = "";
    target_sequence_name = "";
    max_end = 0;
    min_begin = 0;
    genomic_seq = null;
    genomic_alignment = "";
    genomic_begin = 0;
    genomic_end = 0;
    genomic_piece = "";
    mRNA_begin = 0;
    mRNA_end = 0;
    piece_type = "";
    genomic_sequence_id = 0L;
    similarity = null;
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    if ( ( frames != null ) && ( frames.length > 0 ) )
    {
      for ( int f = 0; f < frames.length; f++ )

        if ( frames [ f ] != null )
        {
          frames [ f ].close ();
          frames [ f ] = null;
        }  // if
    }  // if

    initialize ();
  }  // method close


/******************************************************************************/
  // This method returns the exon DNA sequence.
  public String getExon ()
  {
    setGenomicStrand ();

    // return the exon sequence.
    return genomic.getExon ( exon_begin, exon_end );
  }  // method getExon


/******************************************************************************/
  // This method returns the exon DNA sequence.
  public String getExon ( int begin, int end )
  {
    System.out.println ( "splicing exon [" + begin + "-" + end + "]" );

    setGenomicStrand ();

    // return the exon sequence.
    return genomic.getExon ( begin, end );
  }  // method getExon


/******************************************************************************/
  public int getExonBegin ()
  {
    return exon_begin;
  }  // method getExonBegin


/******************************************************************************/
  public boolean getExonBeginSplice ()
  {
    return exon_begin_splice;
  }  // method getExonBeginSplice


/******************************************************************************/
  public int getExonEnd ()
  {
    return exon_end;
  }  // method getExonEnd


/******************************************************************************/
  public boolean getExonEndSplice ()
  {
    return exon_end_splice;
  }  // method getExonEndSplice


/******************************************************************************/
  public String getFrameLock ()
  {
    return frame_lock;
  }  // method getFrameLock


/******************************************************************************/
  public Frame [] getFrames ()
  {
    return frames;
  }  // method getFrames


/******************************************************************************/
  public Genomic getGenomic ()
  {
    return genomic;
  }  // method getGenomic


/******************************************************************************/
  public char getGenomicStrand ()
  {
    return genomic_strand;
  }  // method getGenomicStrand


/******************************************************************************/
  public SequenceTable getTarget ()
  {
    return target;
  }  // method getTarget


/******************************************************************************/
  public String getTargetAlignment ()
  {
    return target_alignment;
  }  // method getTargetAlignment


/******************************************************************************/
  public int getTargetBegin ()
  {
    return target_begin;
  }  // method getTargetBegin


/******************************************************************************/
  public int getTargetEnd ()
  {
    return target_end;
  }  // method getTargetEnd


/******************************************************************************/
  public String getTargetPiece ()
  {
    return target_piece;
  }  // method getTargetPiece


/******************************************************************************/
  public String getTargetSequenceName ()
  {
    return target_sequence_name;
  }  // method getTargetSequenceName


/******************************************************************************/
  public SequenceTable getGenomicSeq ()
  {
    return genomic_seq;
  }  // method getGenomicSeq


/******************************************************************************/
  public int getGenomicBegin ()
  {
    return genomic_begin;
  }  // method getGenomicBegin


/******************************************************************************/
  public int getGenomicEnd ()
  {
    return genomic_end;
  }  // method getGenomicEnd


/******************************************************************************/
  public String getGenomicPiece ()
  {
    return genomic_piece;
  }  // method getGenomicPiece


/******************************************************************************/
  public int getMaxEnd ()
  {
    return max_end;
  }  // method getMaxEnd


/******************************************************************************/
  public int getMinBegin ()
  {
    return min_begin;
  }  // method getMinBegin


/******************************************************************************/
  public int getMrnaBegin ()
  {
    return mRNA_begin;
  }  // method getMrnaBegin


/******************************************************************************/
  public int getMrnaEnd ()
  {
    return mRNA_end;
  }  // method getMrnaEnd


/******************************************************************************/
  public String getPieceType ()
  {
    return piece_type;
  }  // method getPieceType


/******************************************************************************/
  public String getGenomicAlignment ()
  {
    return genomic_alignment;
  }  // method getGenomicAlignment


/******************************************************************************/
  public long getGenomicSequenceId ()
  {
    return genomic_sequence_id;
  }  // method getGenomicSequenceId


/******************************************************************************/
  public Hit getSimilarity ()
  {
    return similarity;
  }  // method getSimilarity


/******************************************************************************/
  public void setExonBegin ( int value )
  {
    exon_begin = value;
  }  // method setExonBegin


/******************************************************************************/
  public void setExonEnd ( int value )
  {
    exon_end = value;
  }  // method setExonEnd


/******************************************************************************/
  public void setGenomic ( Genomic value )
  {
    genomic = value;
  }  // method setGenomic


/******************************************************************************/
  public void setTarget ( SequenceTable value )
  {
    target = value;
  }  // method setTarget


/******************************************************************************/
  public void setTargetAlignment ( String value )
  {
    target_alignment = value;
  }  // method setTargetAlignment


/******************************************************************************/
  public void setTargetSequenceName ( String value )
  {
    target_sequence_name = value;
  }  // setTargetSequenceName


/******************************************************************************/
  public void setGenomicSeq ( SequenceTable value )
  {
    genomic_seq = value;
  }  // method setGenomicSeq


/******************************************************************************/
  public void setGenomicAlignment ( String value )
  {
    genomic_alignment = value;

    // Setup up the frame lock sequence for TBLASTN similarity alignments.
    if ( ( similarity != null ) && 
         ( ( similarity.getProgramName ().equals ( "TBLASTN" ) == true ) || 
           ( similarity.getProgramName ().equals ( "BLASTX" ) == true  ) ) )
    {
      frame_lock = SeqTools.cleanSequence ( genomic_alignment );

      extendFrameLock ();
    }  // if

  }  // method setGenomicAlignment


/******************************************************************************/
  public void setGenomicBegin ( int value )
  {
    genomic_begin = value;
  }  // method setGenomicBegin


/******************************************************************************/
  public void setGenomicEnd ( int value )
  {
    genomic_end = value;
  }  // method setGenomicEnd


/******************************************************************************/
  public void setMrnaBegin ( int value )
  {
    mRNA_begin = value;
  }  // method setMrnaBegin


/******************************************************************************/
  public void setMrnaEnd ( int value )
  {
    mRNA_end = value;
  }  // method setMrnaEnd


/******************************************************************************/
  public void setPieceType ( String value )
  {
    piece_type = value;
  }  // method setPieceType


/******************************************************************************/
  public void setGenomicSequenceId ( long value )
  {
    genomic_sequence_id = value;
  }  // method setGenomicSequenceId


/******************************************************************************/
  public void setSimilarity ( Hit value )
  {
    similarity = value;

    if ( similarity != null )
    {
      target_begin = (int) similarity.getTargetStart ();
      target_end   = (int) similarity.getTargetEnd ();

      if ( target_begin > target_end )
      {
        int temp  = target_begin;
        target_begin = target_end;
        target_end   = temp;
      }  // if

      genomic_strand = similarity.getTargetStrand ();
 
      // Check for a TBLASTX double negative strand.
      if ( ( genomic_strand == '-' ) && 
           ( similarity.getQueryStrand () == '-' ) )
  
        genomic_strand = '+';
    }  // if

    // Set the genomic strand.
    setGenomicStrand ();
  }  // method setSimilarity


/******************************************************************************/
public void setGenomicStrand ()
{
  // Check if genomic has been created yet.
  if ( genomic == null )  return;
 
  // Check if on the complement of the genomic strand.
  if ( genomic_strand == '-' )
    genomic.setUseReverseStrand ();
  else
    genomic.setUseForwardStrand ();
}  // method setGenomicStrand


/******************************************************************************/
public void setGenomicStrand ( char value )
{
  if ( ( value == '+' ) || ( value == '-' ) )  genomic_strand = value;
  else
    System.out.println ( "*Warning* invalid strand '" + value + 
        "' in Piece.setGenomicStrand!" );

  setGenomicStrand ();
}  // method setGenomicStrand


/******************************************************************************/
public void setTargetBegin ( int value )
{
  target_begin = value;
}  // method setTargetBegin


/******************************************************************************/
public void setTargetEnd ( int value )
{
  target_end = value;
}  // method setTargetEnd


/******************************************************************************/
public int getFrameBegin ( int exon_begin )
{
  return genomic.checkFivePrimeFrame ( frames, exon_begin );
}  // method getFrameBegin


/******************************************************************************/
public int getFrameEnd ( int exon_end )
{
  return genomic.checkThreePrimeFrame ( frames, exon_end );
}  // method getFrameEnd


/******************************************************************************/
public int checkForStartCodon ()
{
  // Set the genomic strand to use.
  setGenomicStrand ();

  int start_codon = genomic.checkForStartCodon ( frames, exon_begin );

  if ( start_codon >= 0 )
  {
    exon_begin = start_codon;

    System.out.println ( "ATG found at " + exon_begin );
  }  // if

  return start_codon;
}  // method checkForStartCodon


/******************************************************************************/
public int checkForStopCodon ()
{
  // Set the genomic strand to use.
  setGenomicStrand ();

  int stop_codon = genomic.checkForStopCodon ( frames, exon_end );

  if ( stop_codon + 1 > exon_end )
  {
    exon_end = stop_codon + 2;

    System.out.println ( "Stop codon found at " + exon_end );
  }  // if

  return stop_codon;
}  // method checkForStopCodon


/******************************************************************************/
public void extendFrameLock ()
{
  if ( ( frame_lock == null ) || ( frame_lock.length () <= 0 ) )
  {
    System.out.println ( "No frame_lock sequence in extendFrameLock" );
    return;
  }  // if

  if ( ( genomic == null ) || ( genomic.getLength () <= 0 ) )
  {
    System.out.println ( "No genomic sequence in extendFrameLock" );
    return;
  }  // if

  // Set the genomic strand to use.
  setGenomicStrand ();

  System.out.println ( genomic_strand + " [" + genomic_begin + "-" + genomic_end + "] " + frame_lock );

  Frame fr = genomic.lockFrame ( frame_lock, genomic_begin, genomic_end );

  if ( fr == null )  return;

  frames = new Frame [ 1 ];
  frames [ 0 ] = fr;

  min_begin = fr.getFrameMinimum ();
  max_end   = fr.getFrameMaximum ();

  int begin = 0;
  int end = 0;

  if ( genomic_strand == '+' )
  {
    // 0 .. (N-1) numbering system.
    begin = genomic_begin - 1;
    end = genomic_end - 1;
  }  // if
  else
  {
    begin = genomic.getLength () - genomic_end;
    end   = genomic.getLength () - genomic_begin;
  }  // else

  // System.out.println ( "\textendFrameLock: begin = " + begin + ", end = " + end );
  // System.out.println ( "\textendFrameLock: min_begin = " + min_begin + ", max_end = " + max_end );

  int acceptor = SeqTools.findAcceptor 
      ( genomic.getSequence ()
      , begin
      , min_begin
      );
  exon_begin = acceptor;

  int donor = SeqTools.findDonor 
      ( genomic.getSequence ()
      , end
      , max_end
      );
  exon_end = donor;

  int exon_middle = (begin + end) / 2;
  if ( exon_begin >= exon_middle )  exon_begin = begin;
  if ( exon_end <= exon_middle )  exon_end = end;

  if ( exon_begin == acceptor )  exon_begin_splice = true;
  if ( exon_end == donor )  exon_end_splice = true;

  // System.out.println ( "\tPiece.extendFrameLock: exon [" + exon_begin + "-" + exon_end + "]" );
}  // method extendFrameLock


/******************************************************************************/
public void evaluateFrames ()
{
  int begin = 0;
  int end = 0;

  // Check if this sequence is frame locked.
  if ( frames != null )  return;

  // Set the genomic strand to use.
  setGenomicStrand ();

  // Get the three possible open reading frames.
  frames = genomic.getORFs ( genomic_begin, genomic_end );

  // Check if no frames were returned.
  if ( ( frames == null ) || ( frames.length <= 0 ) )  return;

  // Evaluate the different frames.
  for ( int f = 0; f < frames.length; f++ )

    // Ignore closed frames.
    if ( frames [ f ] != null )
    {
      // Keep the best appearing frame.
      begin = frames [ f ].getFrameMinimum ();
      end   = frames [ f ].getFrameMaximum ();
  
      if ( ( min_begin == 0 ) || 
           ( end - begin > max_end - min_begin ) )
      {
        min_begin = begin;
        max_end   = end;
      }  // if
    }  // if

  if ( genomic_strand == '+' )
  {
    // 0 .. (N-1) numbering system.
    begin = genomic_begin - 1;
    end = genomic_end - 1;
  }  // if
  else
  {
    begin = genomic.getLength () - genomic_end;
    end   = genomic.getLength () - genomic_begin;
  }  // else

  // System.out.println ( "\tevaluateFrames: begin = " + begin + ", end = " + end );
  // System.out.println ( "\tevaluateFrames: genomic_begin = " + genomic_begin + ", genomic_end = " + genomic_end );

  int acceptor = SeqTools.findAcceptor 
      ( genomic.getSequence ()
      , begin
      , min_begin
      );
  exon_begin = acceptor;

  int donor = SeqTools.findDonor 
      ( genomic.getSequence ()
      , end
      , max_end
      );
  exon_end = donor;

  int exon_middle = (begin + end) / 2;
  if ( exon_begin >= exon_middle )  exon_begin = begin;
  if ( exon_end <= exon_middle )  exon_end = end;

  if ( exon_begin == acceptor )  exon_begin_splice = true;
  if ( exon_end == donor )  exon_end_splice = true;

/*
  System.out.println ( "\tPiece.evaluateFrames: exon [" + exon_begin + 
    "-" + exon_end + "]" );
*/
}  // method evaluateFrames


/******************************************************************************/
public void print ()
{
  System.out.println 
      ( "genomic  "
      + genomic_strand
      + " ["
      + genomic_begin
      + "-" 
      + genomic_end
      + "] hit [" 
      + " [" 
      + target_begin 
      + "-" 
      + target_end 
      + "] "
      + piece_type
      + " "
      + target_sequence_name
      + " qsi "
      + genomic_sequence_id
      );
}  // method print


/******************************************************************************/

}  // class Piece
