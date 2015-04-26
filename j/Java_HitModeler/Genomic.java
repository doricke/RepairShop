
// import Frame;
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

public class Genomic extends Object
{


/******************************************************************************/

  private   int  frame = 0;			// current frame

  private   String  frames [] = new String [ 6 ];	// peptide translation frames

  private   int  length = 0;			// genomic sequence length

  private   String  reverse = null;		// reverse genomic sequence strand

  private   String  sequence = null;		// genomic sequence

  private   boolean  use_reverse = false;	// use compliment strand flag


/******************************************************************************/
  // Constructor Genomic
  public Genomic ()
  {
    initialize ();
  }  // constructor Genomic


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    frame = 0;
    frames = new String [ 6 ];
    length = 0;
    reverse = null;
    sequence = null;
    use_reverse = false;
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    if ( ( frames != null ) && ( frames.length > 0 ) )
    {
      for ( int f = 0; f < frames.length; f++ )

        frames [ f ] = null;
    }  // if

    initialize ();
  }  // method close


/******************************************************************************/
  // This method returns the best ORF of the three possible reading frames.
  public String getBestORF ()
  {
    frames [ 0 ] = SeqTools.translate ( sequence );
    frames [ 1 ] = SeqTools.translate ( sequence.substring ( 1 ) );
    frames [ 2 ] = SeqTools.translate ( sequence.substring ( 2 ) );

    // Trim the translations.
    for ( int f = 0; f < 3; f++ )
    {
      // Check for stop codons in the translation frame.
      int index = frames [ f ].indexOf ( '*' );
      while ( index != -1 )
      {
        int last = frames [ f ].lastIndexOf ( '*' );

        if ( (frames [ f ].length () - 1) - last < index )

          frames [ f ] = frames [ f ].substring ( 0, last );

        else
        {
          if ( index + 1 < frames [ f ].length () )
            frames [ f ] = frames [ f ].substring ( index + 1 );
          else
          {
            if ( index == 0 )  frames [ f ] = "";
            else  frames [ f ] = frames [ f ].substring ( 0, index );
          }
        }
        index = frames [ f ].indexOf ( '*' );
      }  // while

      // Trim to the first start codon.
      index = frames [ f ].indexOf ( 'M' );
      if ( ( index != -1 ) && ( index < 50 ) )
        frames [ f ] = frames [ f ].substring ( index );
    }  // for

    // Select the longest open reading frame.
    int best_frame = 0;
    int length = 0;    
    for ( int f = 0; f < 3; f++ )
    {
      // Look for a better frame.
      if ( frames [ f ].length () > length )
      {
        best_frame = f;
        length = frames [ f ].length ();
      }  // if
    }  // for

    // System.out.println ( "Genomic.getBestORF: ORF = " + frames [ best_frame ] );

    return frames [ best_frame ];
  }  // method getBestORF


/******************************************************************************/
  public String getExon ( int begin, int end )
  {
    if ( sequence == null )
    {
      System.out.println ( "*Warning* No sequence in Genomic.getExon!" );
      return "";
    }  // if

    if ( ( begin < 0 ) || 
         ( end <= 0 ) || 
         ( end < begin ) ||
         ( begin >= sequence.length () ) ||
         ( end >= sequence.length () ) )
    {
      System.out.println ( "*** Genomic.getExon: invalid coordinates [" 
          + begin + "-" + end + "] !!!" );
      return "";
    }  // if

    // Check which strand to use.
    if ( use_reverse == false )
    {
      if ( end + 1 >= sequence.length () )
        return ( sequence.substring ( begin ) );
      else
        return ( sequence.substring ( begin, end + 1 ) );
    }
    else  // use the reverse strand
    {
      if ( end + 1 >= reverse.length () )
        return ( reverse.substring ( begin ) );
      else
        return ( reverse.substring ( begin, end + 1 ) );
    }  // else
  }  // method getExon


/******************************************************************************/
  public String getFrame ( int frame )
  {
    if ( frames == null )  return null;
    if ( ( frame < 0 ) || ( frame >= 6 ) )
    {
      System.out.println ( "Genomic.getFrame: *Warning* unknown frame - " + frame );
      return null;
    }  // if

    return frames [ frame ];
  }  // method getFrame


/******************************************************************************/
  public int getFrameIndex ()
  {
    return frame;
  }  // method getFrameIndex


/******************************************************************************/
  public String [] getFrames ()
  {
    return frames;
  }  // method getFrames


/******************************************************************************/
  public int getLength ()
  {
    return length;
  }  // method getLength


/******************************************************************************/
  // This method sets the lock sequence into the peptide.
  private StringBuffer setLock ( String lock, StringBuffer peptide )
  {
    if ( ( lock == null ) || ( peptide == null ) )  return peptide;

    // Find the lock sequence in the translation frames.
    for ( int start = 0; start < lock.length (); start += 5 )
    {
      // Construct sublock sequences because of stop codons & unknown.
      String sublock = "";
      if ( start + 10 < lock.length () )
        sublock = lock.substring ( start, start + 10 );
      else
        sublock = lock.substring ( start );
      int index = sublock.indexOf ( '*' );
      if ( index != -1 )  sublock = sublock.substring ( 0, index );
      index = sublock.indexOf ( 'X' );
      if ( index != -1 )  sublock = sublock.substring ( 0, index );

      // Check if the sublock is long enough.
      if ( sublock.length () >= 5 )
      {
        for ( int f = 0; f < 3; f++ )
        {
          // Search for the sublock in the current frame.
          index = frames [ f ].indexOf ( sublock );

          // Check if the sublock was found.
          if ( index != -1 )
          {
            // Copy from this frame to the peptide.
            while ( peptide.length () < index + lock.length () )
              peptide.append ( ' ' );

            // Copy the lock sequence.
            for ( int s = start; s < lock.length (); s++ )
            {
              if ( ( peptide.charAt ( index + s ) == ' ' ) &&
                   ( index + s < frames [ f ].length () ) )

                peptide.setCharAt ( index + s, frames [ f ].charAt ( index + s ) );

              start++;
            }  // for
          }  // if
        }  // for
      }  // if
    }  // for

    return peptide;
  }  // method setLock


/******************************************************************************/
  // This method selects the best strand to fill spaces with.
  private StringBuffer fillBest ( StringBuffer peptide, int begin, int end )
  {
    int best_frame = 0;			// best frame to use - default frame 0
    int best_count = 9999;		// number of stop codons

    // Evaluate each of the three possible frames.
    for ( int f = 0; f < 3; f++ )
    {
      int frame_count = 0;
      for ( int p = begin; p <= end; p++ )

        if ( p < frames [ f ].length () )
        {
          if ( frames [ f ].charAt ( p ) == '*' )  frame_count++;
        }  // if

      // Check for a better frame.
      if ( frame_count <= best_count )
      {
        best_frame = f;
        best_count = frame_count;
      }  // if
    }  // for

    // Fill in the space(s) using the best frame.
    for ( int p = begin; p <= end; p++ )
    {
      if ( p < frames [ best_frame ].length () )

        peptide.setCharAt ( p, frames [ best_frame ].charAt ( p ) );
    }  // for

    return peptide;
  }  // method fillBest


/******************************************************************************/
  // This method tries to use the best possible frame to fill in an peptide holes.
  private StringBuffer fillSpaces ( StringBuffer peptide )
  {
    int begin = -1;
    int end = -1;

    // Traverse the peptide.
    for ( int p = 0; p < peptide.length (); p++ )
    {
      if ( peptide.charAt ( p ) == ' ' )
      {
        // Check for the start of some space(s).
        if ( begin == -1 )
        {
          begin = p;
          end = p;
        }  // if
        else
          end = p;
      }  // if
      else
      {
        // Check for space(s) to fill.
        if ( begin != -1 )
        {
          peptide = fillBest ( peptide, begin, end );
        }  // if

        begin = -1;
        end = -1;
      }  // else
    }  // for

    return peptide;
  }  // method fillSpaces


/******************************************************************************/
  private StringBuffer fillInternal ( StringBuffer peptide, int start, int end )
  {
    // Validate peptide.
    if ( peptide == null )  return peptide;

    // Extend lock sequences into internal regions.
    for ( int index = start + 1; index < end; index++ )
    {
      // Check for a gap sequence.
      if ( peptide.charAt ( index ) == ' ' )
      {
        String sublock = peptide.substring ( index - 5, index );
        for ( int f = 0; f < 3; f++ )
        {
          if ( frames [ f ].substring ( index - 5, index ).equals ( sublock ) == true )
          {
            // Extend this frame forward.
            while ( ( index < end ) &&
                    ( index < frames [ f ].length () ) &&
                    ( peptide.charAt ( index ) == ' ' ) &&
                    ( frames [ f ].charAt ( index ) != '*' ) )
            {
              peptide.setCharAt ( index, frames [ f ].charAt ( index ) );
              index++;
            }  // while

            f = 3;  // exit the loop
          }  // if
        }  // for
      }  // if

      // Check if a stop codon was encountered.
      if ( ( index < end ) && ( peptide.charAt ( index ) == ' ' ) )
      {
        int next = index + 1;
        while ( ( next < end ) && ( peptide.charAt ( next ) == ' ' ) )  next++;

        // Check if the next locked sequence was found.
        if ( next <= end - 5 )
        {
          // Find the frame for the next lock.
          String sublock = peptide.substring ( next, next + 5 );
          for ( int f = 0; f < 3; f++ )
          {
            if ( frames [ f ].substring ( next, next + 5 ).equals ( sublock ) == true )
            {
              next--;

              // Copy this frame to the left.
              while ( ( next >= index )  &&
                      ( frames [ f ].charAt ( next ) != '*' ) )
              {
                peptide.setCharAt ( next, frames [ f ].charAt ( next ) );
                next--;
              }  // while

              f = 3;		// exit the loop
            }  // if
          }  // for
        }  // if
      }  // if
    }  // for
    return peptide;
  }  // method fillInternal


/******************************************************************************/
  private StringBuffer extendLocks ( StringBuffer peptide )
  {
    // Validate peptide.
    if ( peptide == null )  return peptide;

    // Find the first amino acid.
    int start = 0;
    while ( ( start < peptide.length () ) &&
            ( peptide.charAt ( start ) == ' ' ) )  start++;

    // Check if peptide is empty.
    if ( start == peptide.length () )  return peptide;

    // System.out.println ( "Genomic.extendLocks: start = " + start );

    // Extend towards the start of the mRNA.
    if ( peptide.charAt ( start ) != 'M' )
    {
      // Determine the frame.
      String sublock = peptide.substring ( start, start + 5 );
      int start_codon = -1;
      for ( int f = 0; f < 3; f++ )
      {
        int index = frames [ f ].indexOf ( sublock );

        // System.out.println ( "Genomic.extendLocks: sublock index = " + index + " for sublock: " + sublock );

        if ( index == start )
        {
          // Copy this frame to the left.
          while ( ( index >= 0 ) &&
                  ( index < frames [ f ].length () ) &&
                  ( frames [ f ].charAt ( index ) != '*' ) )
          {
            char amino = frames [ f ].charAt ( index );
            if ( amino == 'M' )  start_codon = index;
            peptide.setCharAt ( index, amino );

            // System.out.println ( "setting " + index + " to " + amino );

            index--;
          }  // while

          f = 3;  // exit the loop
        }  // if
      }  // for

      // System.out.println ( "start_codon = " + start_codon );

      // Trim to the first M reside.
      if ( start_codon > 0 )
      {
        for ( int i = 0; i < start_codon; i++ )

          peptide.setCharAt ( i, ' ' );
      }  // if

    }  // if

    // Extend to stop codon or end of mRNA.
    int end = peptide.length () - 1;
    while ( peptide.charAt ( end ) == ' ' )  end--;
    end++;

    // Find the last locked segment.
    String sublock = peptide.substring ( end - 5, end );
    for ( int f = 0; f < 3; f++ )
    {
      int index = frames [ f ].lastIndexOf ( sublock );

      if ( index == end - 5 )
      {
        while ( ( end < peptide.length () ) &&
                ( end < frames [ f ].length () ) &&
                ( frames [ f ].charAt ( end ) != '*' ) )
        {
          peptide.setCharAt ( end, frames [ f ].charAt ( end ) );
          end++;
        }  // while

        // Check for the end of the peptide.
        if ( ( end == peptide.length () - 1 ) &&
             ( end < frames [ f ].length () ) )
        {
          // Check for the stop codon.
          if ( frames [ f ].charAt ( end ) == '*' )
            peptide.setCharAt ( end, '*' );
        }  // if

        f = 3;  // exit the loop
      }  // if
    }  // for

    // Extend internal regions.
    return fillInternal ( peptide, start, end );
  }  // method extendLocks


/******************************************************************************/
  public String getLockedORF ( String [] locks )
  {
    if ( ( sequence == null ) || ( sequence.length () <= 0 ) )  return "";

    frames [ 0 ] = SeqTools.translate ( sequence );
    frames [ 1 ] = SeqTools.translate ( sequence.substring ( 1 ) );
    frames [ 2 ] = SeqTools.translate ( sequence.substring ( 2 ) );
    StringBuffer peptide = new StringBuffer ( frames [ 0 ].length () );

/*
    for ( int f = 0; f < 3; f++ )
    {
      System.out.println ();
      System.out.println ( "Frame: " + f );
      SeqTools.writeFasta ( frames [ f ] );
    }  // for
*/

    // System.out.println ( "Genomic.getLockedORF: lock sequences are:" );

    // Blank the peptide.
    for ( int i = 0; i < frames [ 0 ].length (); i++ )

      peptide.append ( ' ' );

    // Extend each of the lock sequences.
    for ( int l = 0; l < locks.length; l++ )
    {
      peptide = setLock ( locks [ l ], peptide );

      // System.out.println ( "lock\t" + locks [ l ] );
      // SeqTools.writeFasta ( peptide.toString () );
    }  // for

    // Extend the translation around the lock sequences.
    peptide = extendLocks ( peptide );

    // Fill in the remaining spaces with the best possible frame.
    peptide = fillSpaces ( peptide );

    System.out.println ();
    System.out.println ( "Translation:" );
    SeqTools.writeFasta ( peptide.toString () );

    return peptide.toString ().trim ();
  }  // method getLockedORF


/******************************************************************************/
  public String getSequence ()
  {
    if ( use_reverse == false )
      return sequence;
    else
      return reverse;
  }  // method getSequence


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;

    if ( sequence == null )
    {
      System.out.println ( "*Warning* Genomic.setSequence called with null sequence!" );
      return;
    }  // if

    length = sequence.length ();

    // System.out.println ( "\tGenomic.setSequence called: length = " + length );

    // Create 3 forward frame translations of the DNA sequence.
    frames [ 0 ] = SeqTools.translate3 ( sequence );
    frames [ 1 ] = "X" + SeqTools.translate3 ( sequence.substring ( 1 ) );
    frames [ 2 ] = "XX" + SeqTools.translate3 ( sequence.substring ( 2 ) );

    // Create the compliment sequence strand.
    reverse = SeqTools.reverseSequence ( sequence );

    // Create 3 reverse frame translations of the DNA sequence.
    frames [ 3 ] = SeqTools.translate3 ( reverse );
    frames [ 4 ] = "X" + SeqTools.translate3 ( reverse.substring ( 1 ) );
    frames [ 5 ] = "XX" + SeqTools.translate3 ( reverse.substring ( 2 ) );
  }  // method setSequence


/******************************************************************************/
  public void setUseForwardStrand ()
  {
    // System.out.println ( "Genomic: using the forward strand" );

    use_reverse = false;
  }  // setUseForwardStrand


/******************************************************************************/
  public void setUseReverseStrand ()
  {
    // System.out.println ( "Genomic: using the reverse strand" );

    use_reverse = true;
  }  // setUseReverseStrand


/******************************************************************************/
public int checkFivePrimeFrame ( Frame [] fr, int begin )
{
  // Validate fr.
  if ( fr == null )  return -1;

  // Check each of the frames.
  for ( int f = 0; f < fr.length; f++ )

    if ( fr [ f ] != null )
    {
      int frame = -1;

      if ( frames [ fr [ f ].getFrame () ] != null )

        frame = findFivePrimeFrame
          ( frames [ fr [ f ].getFrame () ], begin );

      if ( frame >= 0 )  return frame;
    }  // if

  return -1;
}  // method checkFivePrimeFrame


/******************************************************************************/
public int checkThreePrimeFrame ( Frame [] fr, int end )
{
  // Validate fr.
  if ( fr == null )  return -1;

  // Check each of the frames.
  for ( int f = 0; f < fr.length; f++ )

    if ( fr [ f ] != null )
    {
      int frame = -1;

      if ( frames [ fr [ f ].getFrame () ] != null )

        frame = findStartCodon 
          ( frames [ fr [ f ].getFrame () ], end );

      if ( frame >= 0 )  return frame;
    }  // if

  return -1;
}  // method checkThreePrimeFrame


/******************************************************************************/
public int checkForStartCodon ( Frame [] fr, int begin )
{
  // Validate fr.
  if ( fr == null )  return -1;

  // Check each of the frames.
  for ( int f = 0; f < fr.length; f++ )

    if ( fr [ f ] != null )
    {
      int start_codon = -1;

      if ( frames [ fr [ f ].getFrame () ] != null )

        start_codon = findStartCodon 
          ( frames [ fr [ f ].getFrame () ], begin );

      if ( start_codon >= 0 )  return start_codon;
    }  // if

  return -1;
}  // method checkForStartCodon


/******************************************************************************/
public int checkForStopCodon ( Frame [] fr, int end )
{
  int best_stop = -1;

  // Validate fr.
  if ( fr == null )  return -1;

  // Check each of the frames.
  for ( int f = 0; f < fr.length; f++ )

    if ( fr [ f ] != null )
    {
      int stop_codon = -1;

      if ( frames [ fr [ f ].getFrame () ] != null )

        stop_codon = findStopCodon ( frames [ fr [ f ].getFrame () ], end );

      if ( stop_codon >= 0 )
      {
        // Keep the closest stop codon as the best stop codon. 
        if ( ( best_stop == -1 ) || ( stop_codon < best_stop ) )  

          best_stop = stop_codon;
      }  // if
    }  // if

  // Check if the stop codon is too far away.
  if ( best_stop > end + 100 )  best_stop = -1;

  return best_stop;
}  // method checkForStopCodon


/******************************************************************************/
// This method finds the start of a codon in this frame.
private int findCodonStart ( String frame, int begin )
{
  // Validate frame.
  if ( frame == null )  return -1;

  int start = begin;

  // Validate begin.
  if ( ( begin < 0 ) || ( begin >= frame.length () ) )  return 0;

  // Find the start of a codon for this frame.
  if ( start < frame.length () - 1 )
    if ( ( frame.charAt ( start ) < 'A' ) || 
         ( frame.charAt ( start ) > 'Z' ) )  start++;
  
  if ( start < frame.length () - 1 )
    if ( ( frame.charAt ( start ) < 'A' ) || 
         ( frame.charAt ( start ) > 'Z' ) )  start++;

  return start;
}  // method findCodonStart


/******************************************************************************/
private int findFivePrimeFrame ( String frame, int begin )
{
  for ( int offset = 0; offset < 3; offset++ )

    if ( ( frame.charAt ( begin + offset ) >= 'A' ) &&
         ( frame.charAt ( begin + offset ) <= 'Z' ) )
    {
      System.out.println ( "\t 5' frame " + frame.substring ( begin + offset, begin + offset + 9 ) );

      return offset;
    }  // if

  System.out.println ( "*Warning* no frame found in findFivePrimeFrame" );
  return -1;
}  // method findFivePrimeFrame


/******************************************************************************/
private int findThreePrimeFrame ( String frame, int end )
{
  for ( int offset = 0; offset < 3; offset++ )

    if ( ( frame.charAt ( end - 2 + offset ) >= 'A' ) &&
         ( frame.charAt ( end - 2 + offset ) <= 'Z' ) )
    {
      // System.out.println ( "\t 3' frame " + frame.substring ( end - 2 + offset, end ) );
      return offset;
    }  // if

  System.out.println ( "*Warning* no frame found in findThreePrimeFrame" );
  return -1;
}  // method findThreePrimeFrame


/******************************************************************************/
// This method searches the frame towards the left for the start codon.
private int findStartCodon ( String frame, int begin )
{
  // Validate frame.
  if ( frame == null )  return -1;

  int start = findCodonStart ( frame, begin );

  String codon = "";
  while ( start >= 0 )
  {
    if ( start + 3 <= frame.length () )
      codon = frame.substring ( start, start + 3 );
    else
      codon = frame.substring ( start );

    // Check for a start codon.
    if ( codon.equals ( "Met" ) == true )

      return start;

    // Check for a stop codon.
    if ( codon.equals ( "Ter" ) == true )

      return -1;

    start -= 3;
  }  // while

  return -1;
}  // method findStartCodon


/******************************************************************************/
// This method searches the frame towards the right for the stop codon.
private int findStopCodon ( String frame, int end )
{
  // Validate frame.
  if ( frame == null )  return -1;

  end -= 3;
  if ( end < 0 )  end = 0;
  int start = findCodonStart ( frame, end );

  String codon = "";
  while ( start < frame.length () )
  {
    // Check for a stop codon.
    if ( start + 3 < frame.length () )
      codon = frame.substring ( start, start + 3 );
    else
      codon = frame.substring ( start );

    if ( codon.equals ( "Ter" ) == true )

      return start;

    start += 3;
  }  // while

  return -1;
}  // method findStopCodon


/******************************************************************************/
public int extendLeft ( String frame, int begin )
{
  // Validate frame.
  if ( frame == null )  return -1;

  // Find the minimum limit on this exon.
  int min_begin = begin - 2;

  // Validate starting parameters.
  if ( frame.length () < 3 )  return 0;
  if ( begin < 0 )  return 0;
  if ( begin >= frame.length () )  return 0;

  int start = findCodonStart ( frame, begin );
  String codon = "";

  boolean done = false;
  while ( done == false )
  {
    start -= 3;

    if ( start < 0 )
    {
      min_begin = 0;
      done = true;
    }  // if
    else
    {
      // Check for a stop codon.
      if ( start + 3 < frame.length () )  codon = frame.substring ( start, start + 3 );
      else 
      {
        if ( start < frame.length () )
          codon = frame.substring ( start );
        else codon = "";
      }  // else
      if ( codon.equals ( "Ter" ) == true )

        done = true;

      else
      
        min_begin = start - 2;
    }  // else
  }  // while

  if ( min_begin < 0 )  min_begin = 0;

  // System.out.println ( "extendLeft: min_begin = " + min_begin );

  return min_begin;
}  // method extendLeft


/******************************************************************************/
public int extendRight ( String frame, int begin )
{
  // Validate frame.
  if ( frame == null )  return -1;

  // Find the maximum limit on this exon.
  int max_end = begin;

  int start = findCodonStart ( frame, begin );

  String codon = "";
  boolean done = false;
  while ( done == false )
  {
    start += 3;

    if ( start >= frame.length () -2 )
    {
      max_end = frame.length () - 1;
      done = true;
    }  // if
    else
    {
      // Check for a stop codon.
      if ( start + 3 >= frame.length () )
        codon = frame.substring ( start );
      else
        codon = frame.substring ( start, start + 3 );

      if ( codon.equals ( "Ter" ) == true )

        done = true;

      else
      
        max_end = start + 2;
    }  // else
  }  // while

  // System.out.println ( "extendRight: max_end = " + max_end );

  return max_end;
}  // method extendRight


/******************************************************************************/
public Frame [] getORFs ( long begin, long end )
{
  return getORFs ( (int) begin, (int) end );
}  // method getORFs


/******************************************************************************/
public Frame [] getORFs ( int begin, int end )
{
  // System.out.println ();
  if ( ( begin < 0 ) || ( end < 0 ) )
  {
    System.out.println ( "*Warning* Genomic.getORFs called [" + begin + " to " + end + "]" );
    return null;
  }  // if

  if ( ( sequence == null ) || ( reverse == null ) )
  {
    System.out.println ( "*Warning* No genomic sequence in Genomic.getORFs" );
    return null;
  }  // if

  Frame orfs [] = new Frame [ 3 ];

  boolean is_open = false;		// Seen an open reading frame yet.

  // System.out.println ( "Genomic.getORFs called [" + begin + " to " + end + "]" );

  if ( use_reverse == false )
  {
    begin--;				// Use 0 .. (N-1) index system.
    end--;

    for ( int f = 0; f < 3; f++ )
    {
      Frame fr = new Frame ();
      fr.setFrame ( f );
      fr.setTranslation ( frames [ f ] );
      fr.setHitBegin ( begin );
      fr.setHitEnd ( end );
      fr.setFrameMinimum ( extendLeft ( frames [ f ], begin ) );
      fr.setFrameMaximum ( extendRight ( frames [ f ], begin ) );

      // Compute the extra codon bases at the exon ends.
      fr = setExtraBases ( fr );

      orfs [ f ] = fr;
/*
      System.out.println ( "\tHit [" + begin + "-" + end + "]" + 
           " Min-Max [" + fr.getFrameMinimum () + "-" + fr.getFrameMaximum () + "]" );
*/
      if ( fr.getFrameMaximum () >= end )
      {
        is_open = true;
      }  // if
    }  // for
  }  // if
  else
  {
    int start = reverse.length () - end;
    end = reverse.length () - begin;

    // System.out.println ( "\tHit.rev [" + start + "-" + end + "]" ); 

    for ( int f = 3; f < 6; f++ )
    {
      Frame fr = new Frame ();
      fr.setFrame ( f );
      fr.setTranslation ( frames [ f ] );
      fr.setHitBegin ( start );
      fr.setHitEnd ( end );
      fr.setFrameMinimum ( extendLeft ( frames [ f ], start ) );
      fr.setFrameMaximum ( extendRight ( frames [ f ], start ) );

      // Compute the extra codon bases at the exon ends.
      fr = setExtraBases ( fr );

      orfs [ f - 3 ] = fr;
/*
      System.out.println ( "\tHit [" + start + "-" + end + "]" + 
           " Min-Max [" + fr.getFrameMinimum () + "-" + fr.getFrameMaximum () + "]" );
*/
      if ( fr.getFrameMaximum () >= end )
      {
        is_open = true;
      }  // if
    }  // for
  }  // else

  // Check if at least one frame is open.
  if ( is_open == true )

    // Review each of the possible open reading frames.
    for ( int f = 0; f < 3; f++ )
    {
      // Eliminate closed frames.
      if ( orfs [ f ].getFrameMaximum () < end )
      {
         // System.out.println ( "\tClosing frame " + orfs [ f ].getFrame () );
         orfs [ f ] = null;
      }  // if
    }  // for

  // else

    // System.out.println ( "\tClosed all open reading frames." );

  return orfs;
}  // method getORFs


/******************************************************************************/
public int findPeptideStart ( String peptide )
{
  // Validate peptide.
  if ( peptide == null )  return -1;

  int  g_start = -1;				// genomic sequence start

  // Find the start of the first identity region.
  String pep = SeqTools.convert1to3 ( peptide );

  if ( use_reverse == false )
  {
    for ( frame = 0; frame < 3; frame++ )
    {
      // Search for the peptide
      g_start = frames [ frame ].indexOf ( pep );
  
      // Check if peptide found.
      if ( g_start >= 0 )  return g_start;
    }  // for
  }  // if

  else  // use the reverse frames
  {
    for ( frame = 3; frame < 6; frame++ )
    {
      // Search for the peptide
      g_start = frames [ frame ].indexOf ( pep );
  
      // Check if peptide found.
      if ( g_start >= 0 )  return g_start;
    }  // for
  }  // else 

  return -1;
}  // method findPeptideStart


/******************************************************************************/
public String findPeptideFrame ( String peptide )
{
  // Validate peptide.
  if ( peptide == null )  return null;

  int  g_start = -1;				// genomic sequence start

  // Find the start of the first identity region.
  String pep = SeqTools.convert1to3 ( peptide );

  if ( use_reverse == false )
  {
    for ( frame = 0; frame < 3; frame++ )
    {
      // Search for the peptide
      g_start = frames [ frame ].indexOf ( pep );
  
      // Check if peptide found.
      if ( g_start >= 0 )  return frames [ frame ];
    }  // for
  }  // if

  else  // use the reverse frames
  {
    for ( frame = 3; frame < 6; frame++ )
    {
      // Search for the peptide
      g_start = frames [ frame ].indexOf ( pep );
  
      // Check if peptide found.
      if ( g_start >= 0 )  return frames [ frame ];
    }  // for
  }  // else
 
  return null;
}  // method findPeptideFrame


/******************************************************************************/
public Frame lockFrame ( String frame_lock, long begin, long end )
{
  return lockFrame ( frame_lock, begin, end );
}  // method lockFrame


/******************************************************************************/
public Frame lockFrame ( String frame_lock, int begin, int end )
{
  // System.out.println ( "lockFrame: [" + begin + "-" + end + "]" );
  // System.out.println ( "lockFrame: lock seq = " + frame_lock );

  if ( ( sequence == null ) || 
       ( reverse == null ) ||
       ( frame_lock == null ) )  

    return null;

  // Find the start of the frame lock sequence in the genomic sequence.
  int start = findPeptideStart ( frame_lock );

  String sublock = frame_lock;
  while ( ( start == -1 ) && ( sublock.length () > 0 ) )
  {
    int index = sublock.indexOf ( 'X' );
    if ( index != -1 )
    {
      if ( index >= 10 )
      {
        sublock = sublock.substring ( 0, index );
        start = findPeptideStart ( sublock );
      }  // if
      else
      {
        if ( index + 1 < sublock.length () )
        {
          sublock = sublock.substring ( index + 1 );
          if ( sublock.length () >= 5 )
            start = findPeptideStart ( sublock );
        }
        else
          sublock = "";		// exit the loop
      }  // else
    }  // if
    else  sublock = "";		// exit the loop
  }  // if

  if ( start == -1 )
  {
/*
    System.out.println ( "*Warning* frame lock sequence not found in the genomic sequence:" );
    System.out.println ( "begin = " + begin + ", end = " + end + ", sublock = " + sublock );
    SeqTools.writeFasta ( frame_lock );
    System.out.println ();

    System.out.println ( "Framelock:" );
    String pep = SeqTools.convert1to3 ( frame_lock );
    SeqTools.writeFasta ( pep );
    System.out.println ();

    if ( use_reverse == false )
    {
      for ( int f = 0; f < 3; f++ )
      {
        System.out.println ( "Frame " + f );
        SeqTools.writeFasta ( frames [ f ] );
      }  // for
    }  // if
    else
    {
      for ( int f = 3; f < 6; f++ )
      {
        System.out.println ( "Frame " + f );
        SeqTools.writeFasta ( frames [ f ] );
      }  // for
    }  // else
*/
    return null;
  }  // if

  Frame fr = new Frame ();
  fr.setFrame ( frame );
  fr.setTranslation ( frames [ frame ] );
  fr.setHitBegin ( begin );
  fr.setHitEnd ( end );
  fr.setFrameMinimum ( extendLeft ( frames [ frame ], start ) );
  fr.setFrameMaximum ( extendRight ( frames [ frame ], start ) );

  // Compute the extra codon bases at the exon ends.
  fr = setExtraBases ( fr );

  return fr;
}  // method lockFrame


/******************************************************************************/
private Frame setExtraBases ( Frame fr )
{
  if ( fr == null )  return fr;

  int begin = fr.getHitBegin ();
  int start = begin;
  int index = fr.getFrame ();

  // Validate fr.
  if ( fr == null )  return fr;

  if ( start < 0 )
  {
    System.out.println ( "*Warning* invalid frame.getHitBegin (" + fr.getHitBegin () +
      ") in Genomic.setExtraBases!" );
    return fr;
  }  // if

  // Find the start of a codon for this frame.
  if ( start < frames [ index ].length () - 1 )
    if ( ( frames [ index ].charAt ( start ) < 'A' ) || 
         ( frames [ index ].charAt ( start ) > 'Z' ) )  start++;
  
  if ( start < frames [ index ].length () - 1 )
    if ( ( frames [ index ].charAt ( start ) < 'A' ) || 
         ( frames [ index ].charAt ( start ) > 'Z' ) )  start++;

  String seq = sequence;
  if ( use_reverse == true )  seq = reverse;

  // Check that the frame does not start on a codon.
  if ( start > begin )
  {
    if ( start < frames [ index ].length () )
      fr.setBases5 ( seq.substring ( begin, start ) );
    else
      fr.setBases5 ( seq.substring ( begin ) );
  }  // if

  // Find the start of the last codon for this frame.
  int end = fr.getHitEnd ();
  start = end;
  if ( start >= frames [ index ].length () )
  {
    System.out.println ( "Genomic.setExtraBases *** Info: hit_end = " + 
      end + ", frames[].length () = " +
      frames [ index ].length () );

    start = frames [ index ].length () - 1;
  }  // if

  if ( start > 0 )
    if ( ( frames [ index ].charAt ( start ) < 'A' ) || 
         ( frames [ index ].charAt ( start ) > 'Z' ) )  start--;

  if ( start > 0 )
    if ( ( frames [ index ].charAt ( start ) < 'A' ) || 
         ( frames [ index ].charAt ( start ) > 'Z' ) )  start--;

  // Check if the last base is the end of a codon.
  if ( start > end - 2 )
  {
    if ( end + 1 < frames [ index ].length () )
      fr.setBases3 ( seq.substring ( start, end+1 ) );
    else
      fr.setBases3 ( seq.substring ( start ) );
  }  // if

  return fr;
}  // method setExtraBases


/******************************************************************************/

}  // class Genomic
