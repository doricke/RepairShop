
import java.util.*;

// import Segment;
// import Segments;

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

public class Dot extends Object
{


/******************************************************************************/

  final static int MAX_SIZE = 10000;


/******************************************************************************/

  // alignment for first sequence
  private   StringBuffer alignment1 = new StringBuffer ( MAX_SIZE );

  // alignment for second sequence
  private   StringBuffer alignment2 = new StringBuffer ( MAX_SIZE );

  private   int  last_match = 0;		// offset to last identity match

  private   String  sequence1 = "";		// first sequence to align

  private   String  sequence2 = "";		// second sequence to align

  private   byte [] [] matrix = null;		// dot matrix

  private   Segments segments = new Segments ();	// Alignment segments


/******************************************************************************/
  // Constructor Dot
  public Dot ()
  {
    initialize ();
  }  // constructor Dot


/******************************************************************************/
  // Constructor Dot
  public Dot ( String seq1, String seq2 )
  {
    initialize ();
    setSequence1 ( seq1 );
    setSequence2 ( seq2 );

    align ();
  }  // constructor Dot


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alignment1.setLength ( 0 );
    alignment2.setLength ( 0 );
    last_match = 0;
    sequence1 = "";
    sequence2 = "";
  }  // method initialize 


/******************************************************************************/
  public String getAlignment1 ()
  {
    return alignment1.toString ();
  }  // method getAlignment1


/******************************************************************************/
  public String getAlignment2 ()
  {
    return alignment2.toString ();
  }  // method getAlignment2


/******************************************************************************/
  public String getSequence1 ()
  {
    return sequence1;
  }  // method getSequence1


/******************************************************************************/
  public String getSequence2 ()
  {
    return sequence2;
  }  // method getSequence2


/******************************************************************************/
  public void setSequence1 ( String value )
  {
    sequence1 = value;
  }  // method setSequence1


/******************************************************************************/
  public void setSequence2 ( String value )
  {
    sequence2 = value;
  }  // method setSequence2


/******************************************************************************/
  // This method aligns the two sequences.
  public void align ()
  {
    Vector amino_blocks = new Vector ();
    alignment1.setLength ( 0 );
    alignment2.setLength ( 0 );
    last_match = 0;
    segments.initialize ();

    // Assert: two sequences.
    if ( ( sequence1.length () <= 0 ) || ( sequence2.length () <= 0 ) )  return;

    matrix = new byte [ sequence2.length () ] [ sequence1.length () ];
    for ( int j = 0; j < sequence2.length (); j++ )
      for ( int i = 0; i < sequence1.length (); i++ )
        matrix [ j ] [ i ] = (byte) 0;

    int count = 0;
    for ( int j = 0; j < sequence2.length () - 1; j++ )
      for ( int i = 0; i < sequence1.length () - 1; i++ )
      {
        if ( j < sequence2.length () )
          if ( equals ( sequence2.charAt ( j ), sequence1.charAt ( i ) ) == true )
          {        
            count = countMatches ( i, j );
  
            // Avoid long alignments creating a negative count.
            if ( count <= 127 )  matrix [ j ] [ i ] = (byte) count;
            else matrix [ j ] [ i ] = (byte) 127;
  
            // Capture the larger segments.
            if ( count >= 4 )
            {
              Segment seg = new Segment ( i, j, count
                  , sequence1.substring ( i, i + last_match + 1 )
                  , sequence2.substring ( j, j + last_match + 1 ) 
                  );
              amino_blocks.add ( seg );
  
              i += last_match + 1;
              j += last_match + 1;
            }  // if
        }  // if
      }  // for

    // Add the alignment segments.
    segments.addSegments ( amino_blocks );

    segments.computeAlignmentCounts ();
    // segments.snap ();

    segments.selectAlignment ();
    // segments.snap ();

    alignSequences ();

    deGapEnds ();
  }  // method align


/******************************************************************************/
  private void alignSequences ()
  {
    int start1 = 0;
    int start2 = 0;
    String sub1 = "";
    String sub2 = "";
    Segment segment = null;

    for ( int i = 0; i < segments.getTotal (); i++ )
    {
      segment = segments.getSegment ( i );

      // Align the subsequences before the current segment.
      sub1 = sequence1.substring ( start1, segment.getStart1 () );
      sub2 = sequence2.substring ( start2, segment.getStart2 () );
      subAlign ( sub1, sub2 );

      // Add the current segment.
      start1 = segment.getStart1 () + segment.getLength () + 1;
      start2 = segment.getStart2 () + segment.getLength () + 1;

      if ( start1 < sequence1.length () )
        alignment1.append ( sequence1.substring ( segment.getStart1 (), start1 ) );
      else
        alignment1.append ( sequence1.substring ( segment.getStart1 () ) );

      if ( start2 < sequence2.length () )
        alignment2.append ( sequence2.substring ( segment.getStart2 (), start2 ) );
      else
        alignment2.append ( sequence2.substring ( segment.getStart2 () ) );
    }  // for

    // Align the ends of the sequences.
    sub1 = "";
    sub2 = "";
    segment = segments.getSegment ( segments.getTotal () - 1 );
    start1 = segment.getStart1 () + segment.getLength () + 1;
    start2 = segment.getStart2 () + segment.getLength () + 1;
    if ( start1 < sequence1.length () )  sub1 = sequence1.substring ( start1 );
    if ( start2 < sequence2.length () )  sub2 = sequence2.substring ( start2 );
    subAlign ( sub1, sub2 );
  }  // method alignSequences


/******************************************************************************/
  private void subAlign ( String sub1, String sub2 )
  {
    int delta = sub1.length () - sub2.length ();

    if ( delta == 0 )
    {
      alignment1.append ( sub1 );
      alignment2.append ( sub2 );
    }  // if
    else
    {
      // System.out.println ( "subAlign:" );
      // System.out.println ( "  -> " + sub1 );
      // System.out.println ( "  -> " + sub2 );

      int delta2 = delta;
      if ( delta < 0 )  delta2 = - delta;

      StringBuffer gaps = new StringBuffer ( delta2 );
      for ( int i = 0; i < delta2; i++ )
        gaps.append ( '-' );

      if ( delta > 0 )
      {
        alignment1.append ( sub1 );
        alignment2.append ( optimize ( sub1, sub2, gaps.toString () ) );
      }  // if
      else
      {
        alignment1.append ( optimize ( sub2, sub1, gaps.toString () ) );
        alignment2.append ( sub2 );
      }  // else
    }  // else
  }  // method subAlign


/******************************************************************************/
  // seq2.length < seq1.length
  private String optimize ( String seq1, String seq2, String gaps )
  {
    StringBuffer temp = new StringBuffer ();

    int best_i = 0;
    int best_count = 0;
    int count = 0;

    for ( int i = 0; i <= seq2.length (); i++ )
    {
      temp.setLength ( 0 );
      temp.append ( seq2 + "$" );
      temp.insert ( i, gaps );
      count = countIdent ( seq1, temp.toString () );

      if ( count > best_count )
      {
        best_count = count;
        best_i = i;
      }  // if
    }  // for

    temp.setLength ( 0 );
    temp.append ( seq2 );
    temp.insert ( best_i, gaps );
    return temp.toString ();
  }  // method optimize


/******************************************************************************/
  private int countMatches ( int i, int j )
  {
    last_match = 0;			// offset to last match
    int counts = 1;			// count of identities

    int index = 1;			// index to next residues
    boolean match = true;		// loop control variable - identity found

    // Extend alignment as long as identities can be found.
    while ( match == true )
    {
      match = false;

      // Don't index off the end of either sequence.
      if ( ( i + index < sequence1.length () ) &&
           ( j + index < sequence2.length () ) )
      {
        // Check for the next residues being identical.
        if ( equals ( sequence1.charAt ( i + index ), sequence2.charAt ( j + index ) ) == true )
        {
          match = true;
          counts++;
          last_match = index;
        }  // if
        else  // mismatch
        {
          // Extend past one mismatch if the following residues are identical.

          // Check more sequence after this position.
          if ( ( i + index + 1 < sequence1.length () ) &&
               ( j + index + 1 < sequence2.length () ) )
          {
            // Check the next position.
            if ( equals ( sequence1.charAt ( i + index + 1 ), sequence2.charAt ( j + index + 1 ) ) == true )
            {
              match = true;
              counts++;
              last_match = index + 1;
              index++;
            }  // if
          }  // if
        }  // else
      }  // if

      index++;
    }  // while

    return counts;
  }  // method countMatches


/******************************************************************************/
  private int countWindow ( int pos1, int pos2, int window )
  {
    int count = 0;

    for ( int i = 0; i < window; i++ )

      if ( ( pos1 + i < sequence1.length () ) &&
           ( pos2 + i < sequence2.length () ) )

        if ( equals ( sequence1.charAt ( pos1 + i )
                        , sequence2.charAt ( pos2 + i ) ) == true )

          count++;

    return count;
  }  // method countWindow


/******************************************************************************/
  private int countIdent ( String seq1, String seq2 )
  {
    int count = 0;

    for ( int i = 0; (i < seq1.length ()) && (i < seq2.length ()); i++ )

      if ( equals ( seq1.charAt ( i ), seq2.charAt ( i ) ) == true ) 

        count++;

    return count;
  }  // method countIdent


/*******************************************************************************/
  private void deGapEnds ()
  {
    // Degap the start of the alignment.
    int first_gap = findGap ( alignment1 );

    int count = 0;
    while ( first_gap >= 0 )
    {
      count = countIdent ( alignment1.substring ( 0, first_gap ),
                           alignment2.substring ( 0, first_gap ) );

      if ( count < 6 )
      {
        shiftGaps ( alignment1, first_gap );
        first_gap = findGap ( alignment1 );
      }  // if
      else
        first_gap = -1;
    }  // while

    first_gap = findGap ( alignment2 );
    while ( first_gap >= 0 )
    {
      count = countIdent ( alignment1.substring ( 0, first_gap ),
                           alignment2.substring ( 0, first_gap ) );

      if ( count < 6 )
      {
        shiftGaps ( alignment2, first_gap );
        first_gap = findGap ( alignment2 );
      }  // if
      else
        first_gap = -1;
    }  // while

    // Delete aligned starting gaps.
    while ( ( isGap ( alignment1.charAt ( 0 ) ) == true ) &&
            ( isGap ( alignment2.charAt ( 0 ) ) == true ) )
    {
      alignment1.deleteCharAt ( 0 );
      alignment2.deleteCharAt ( 0 );
    }  // while

    // Degap the end of the alignment.
    int last_gap = findLastGap ( alignment1 );
    count = 0;
    while ( last_gap >= 0 )
    {
      count = countIdent ( alignment1.substring ( last_gap ),
                           alignment2.substring ( last_gap ) );

      if ( count < 6 )
      {
        shiftGapsRight ( alignment1, last_gap );
        last_gap = findLastGap ( alignment1 );
      }  // if
      else
        last_gap = -1;
    }  // while

    last_gap = findLastGap ( alignment2 );
    while ( last_gap >= 0 )
    {
      count = countIdent ( alignment1.substring ( last_gap ),
                           alignment2.substring ( last_gap ) );

      if ( count < 6 )
      {
        shiftGapsRight ( alignment2, last_gap );
        last_gap = findLastGap ( alignment2 );
      }  // if
      else
        last_gap = -1;
    }  // while
  }  // method deGapEnds


/*******************************************************************************/
  public boolean isGap ( char c )
  {
    if ( ( c == '.' ) || ( c == '-' ) )  return true;
    return false;
  }  // method isGap


/*******************************************************************************/
  private void shiftGaps ( StringBuffer align, int first_gap )
  {
    while ( align.charAt ( first_gap ) == '-' )
    {
      align.deleteCharAt ( first_gap );
      align.insert ( 0, '.' );
      first_gap++;
    }  // while
  }  // method shiftGaps


/*******************************************************************************/
  private void shiftGapsRight ( StringBuffer align, int first_gap )
  {
    while ( align.charAt ( first_gap ) == '-' )
    {
      align.deleteCharAt ( first_gap );
      align.append ( '.' );
      first_gap--;
    }  // while
  }  // method shiftGapsRight


/*******************************************************************************/
  public static boolean equals ( char c1, char c2 )
  {
    if ( c1 == c2 )  return true;

    // Make sure the characters have the same case.
    char d1 = c1;
    char d2 = c2;
    if ( ( d1 >= 'a' ) && ( d1 <= 'z' ) )  d1 = (char) (d1 - 'a' + 'A');
    if ( ( d2 >= 'a' ) && ( d2 <= 'z' ) )  d2 = (char) (d2 - 'a' + 'A');
    return (d1 == d2);
  }  // method equals


/******************************************************************************/
  private int findGap ( StringBuffer seq )
  {
    for ( int i = 0; i < seq.length (); i++ )

      if ( seq.charAt ( i ) == '-' )  return i;

    return -1;		// No gap
  }  // method findGap


/******************************************************************************/
  private int findGap ( String seq, int index )
  {
    for ( int i = index; i < seq.length (); i++ )

      if ( seq.charAt ( i ) == '-' )  return i;

    return -1;		// No gap
  }  // method findGap


/******************************************************************************/
  private int findGap ( StringBuffer seq, int index )
  {
    for ( int i = index; i < seq.length (); i++ )

      if ( seq.charAt ( i ) == '-' )  return i;

    return -1;		// No gap
  }  // method findGap


/******************************************************************************/
  private int findLastGap ( StringBuffer seq )
  {
    for ( int i = seq.length () - 1; i >= 0; i-- )

      if ( seq.charAt ( i ) == '-' )  return i;

    return -1;		// No gap
  }  // method findLastGap


/******************************************************************************/
  public void printAlignment ()
  {
    for ( int i = 0; i < alignment1.length (); i += 50 )
    {
      System.out.println ();

      // Print out the first sequence.
      System.out.print ( (i+1) + "\t" );
      int end = i + 50;
      if ( end > alignment1.length () )  end = alignment1.length ();
      System.out.println ( alignment1.substring ( i, end ) + "\t" + end );

      // Print out the identity markers.
      System.out.print ( "\t" );
      for ( int j = i; j < end; j++ )
        if ( j < alignment2.length () )
        {
          if ( equals ( alignment1.charAt ( j ), alignment2.charAt ( j ) ) == true )
            System.out.print ( "|" );
          else
            System.out.print ( " " );
        }  // if
      System.out.println ();

      // Print out the second sequence.
      System.out.print ( (i+1) + "\t" );
      if ( end > alignment2.length () )
        System.out.println ( alignment2.substring ( i ) + "\t" + end );
      else
        System.out.println ( alignment2.substring ( i, end ) + "\t" + end );
    }  // for
  }  // method printAlignment


/******************************************************************************/
  public void printMatrix ()
  {
    // Assert: matrix exists.
    if ( matrix == null )  return;

    System.out.println ( " " + sequence1 );

    for ( int j = 0; j < sequence2.length (); j++ )
    {
      System.out.print ( sequence2.charAt ( j ) );

      for ( int i = 0; i < sequence1.length (); i++ )
      {
        if ( matrix [ j ][ i ] <= 2 )
          System.out.print ( " " );
        else
          if ( matrix [ j ] [ i ] < 10 )
            System.out.print ( matrix [ j ] [ i ] );
          else
            System.out.print ( "*" );
      }  // for

      System.out.println ();
    }  // for
  }  // method printMatrix


/******************************************************************************/
  public void alignFastas ( String fasta_filename1, String fasta_filename2 )
  {
    FastaIterator iterator = new FastaIterator ( fasta_filename1 );

    FastaSequence fasta1 = iterator.next ();

    iterator.closeFile ();

    iterator.setFileName ( fasta_filename2 );
    iterator.openFile ();

    FastaSequence fasta2 = iterator.next ();

    iterator.closeFile ();

    setSequence1 ( fasta1.getSequence () );
    setSequence2 ( fasta2.getSequence () );
    align ();

    printAlignment ();
    // printMatrix ();
  }  // method alignFastas


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "This program DotPlot compares two FASTA sequence files." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java Dot <fasta_filename1> <fasta_filename2>" );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    Dot app = new Dot ();

    if ( args.length <= 0 )
      app.usage ();
    else
      app.alignFastas ( args [ 0 ], args [ 1 ] );
  }  // method main


/******************************************************************************/

}  // class Dot
