
// import Consensus;
// import Dot;
// import FastaSequence;
// import Format;
// import LineTools;

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

public class Alignments extends Object
{


/******************************************************************************/

  // Consensus sequence.
  private   StringBuffer consensus = new StringBuffer ();

  private   FastaSequence [] fastas = null;	// FASTA sequences

  private   StringBuffer [] msa = null;		// Protein MSA

  private   int  total = 0;			// number of current words


/******************************************************************************/
  // Constructor Alignments
  public Alignments ()
  {
    initialize ();
  }  // constructor Alignments


/******************************************************************************/
  // Initialize the class variables.
  public void initialize ()
  {
    total = 0;
    consensus.setLength ( 0 );
  }  // method initialize 


/******************************************************************************/
  public StringBuffer getConsensus ()
  {
    return consensus;
  }  // method getConsensus


/******************************************************************************/
  public StringBuffer [] getAlignments ()
  {
    return msa;
  }  // method getAlignments


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/*******************************************************************************/
  // This function returns the length of the longest aligned sequence in the MSA.
  private int getLongest ( int last_index )
  {
    int length = msa [ 0 ].length ();

    for ( int i = 0; i < last_index; i++ )

      if ( msa [ i ].length () > length )  length = msa [ i ].length ();

    return length;
  }  // method getLongest


/*******************************************************************************/
  public void setFastas ( FastaSequence [] value )
  {
    fastas = value;

    msa = new StringBuffer [ value.length ];
    for ( int i = 0; i < value.length; i++ )
      msa [ i ] = new StringBuffer ();

    total = value.length;
  }  // method setFastas


/*******************************************************************************/
  public void addAlignment ( String seq1, String seqn, int index )
  {
    // Copy the alignment.
    int msa_i = 0;
    for ( int i = 0; i < seq1.length (); i++ )
    {
      if ( msa_i >= msa [ 0 ].length () )
      {
        msa [ 0 ].append ( seq1.substring ( i ) );
        msa [ index ].append ( seqn.substring ( i ) );
        i = seq1.length ();				// terminate the loop
      }  // if
      else
      if ( seq1.charAt ( i ) == msa [ 0 ].charAt ( msa_i ) )
      {
        if ( i < seqn.length () )
          msa [ index ].append ( seqn.charAt ( i ) );
        else
          msa [ index ].append ( "." );

        msa_i++;
      }  // if
      else
      {
        if ( isGap ( msa [ 0 ].charAt ( msa_i ) ) == true )
        {
          while ( isGap ( msa [ 0 ].charAt ( msa_i ) ) == true )
          {
            msa_i++;
            // msa [ index ].append ( "-" );
          }  // while

          i--;			// match sequence residue
        }  // if
        else
          if ( isGap ( seq1.charAt ( i ) ) == true )
          {
            int end_gap = i;
            while ( ( end_gap + 1 < seq1.length () ) && 
                    ( isGap ( seq1.charAt ( end_gap + 1 ) ) == true ) )

              end_gap++;

            // System.out.println ( "bubble: seq " + index + " [" + i + "-" + end_gap + "]" );
            i = end_gap;
          }  // if
/*
        else
          if ( isGap ( seq1.charAt ( i ) ) == true )
          {
            for ( int j = 0; j < index; j++ )
              msa [ j ].insert ( msa_i, seq1.charAt ( i ) );
            msa [ index ].append ( seqn.charAt ( i ) );
            msa_i++;
          }  // if
*/
      }  // else

    }  // for

    // Append the tail of sequence(n).
    if ( seqn.length () > seq1.length () )
      msa [ index ].append ( seqn.substring ( seq1.length () ) );

    // Resolve new gaps.
    // resolveGaps ( seq1 );
  }  // method addAlignment


/*******************************************************************************/
  private int endGap ( String seq1, int index )
  {
    if ( index < 0 )  return index;

    int gap_end = index;
    while ( ( gap_end < seq1.length () ) &&
            ( isGap ( seq1.charAt ( gap_end ) ) == true ) )

      gap_end++;

    return gap_end;
  }  // method endGap


/*******************************************************************************/
  private int nextGap ( String seq1, int index )
  {
    for ( int i = index; i < seq1.length (); i++ )
      if ( isGap ( seq1.charAt ( i ) ) == true )  return i;

    return -1;			// No gap found.
  }  // method nextGap


/*******************************************************************************/
  private void resolveGaps ( String seq1 )
  {
    int gap_start = nextGap ( seq1, 0 );
    int gap_end = endGap ( seq1, gap_start );

    while ( gap_start >= 0 )
    {

      gap_start = nextGap ( seq1, gap_end + 1 );
      gap_end = endGap ( seq1, gap_start );
    }  // while
  }  // method resolveGaps


/*******************************************************************************/
  public boolean isGap ( char c )
  {
    if ( ( c == '.' ) || ( c == '-' ) )  return true;
    return false;
  }  // method isGap


/*******************************************************************************/
  public void align ()
  {
    // Assert: something to align.
    if ( total <= 0 )  return;
    if ( ( msa == null ) || ( fastas == null ) )  return;

    msa [ 0 ].append ( fastas [ 0 ].getSequence () );

    // Check for only one sequence.
    if ( total == 1 )  return;

    // Align the first two sequences.
    msa [ 0 ].setLength ( 0 );		// reset
    Dot dot = new Dot ();
    dot.setSequence1 ( fastas [ 0 ].getSequence () );
    dot.setSequence2 ( fastas [ 1 ].getSequence () );
    dot.align ();
    msa [ 0 ].append ( dot.getAlignment1 () );
    msa [ 1 ].append ( dot.getAlignment2 () );

    // Align all the remain sequences to the first sequence.
    for ( int i = 2; i < total; i++ )
    {
      dot.setSequence2 ( fastas [ i ].getSequence () );
      dot.align ();
      addAlignment ( dot.getAlignment1 (), dot.getAlignment2 (), i );

      // printAlignments ();
    }  // for
  }  // method align


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


/*******************************************************************************/
  public static String deGap ( String seq )
  {
    // Check if the sequence has no gaps.
    if ( seq.indexOf ( '-' ) < 0 )  return seq;

    // Degap the sequence.
    StringBuffer new_seq = new StringBuffer ();
    new_seq.append ( seq );
    for ( int i = new_seq.length () - 1; i >= 0; i-- )

      if ( new_seq.charAt ( i ) == '-' )  new_seq.deleteCharAt ( i );

    // System.out.println ( "deGap: " + new_seq.toString () );

    return new_seq.toString ();
  }  // method deGap


/*******************************************************************************/
  public void deGapEnds ()
  {
    // Check if any sequences.
    if ( total <= 0 )  return;

    int j = 0;
    for ( int i = 0; i < total; i++ )
    {
      // Degap the start of each sequence.
      j = 0;
      do
      {
        if ( ( msa [ i ].charAt ( j ) == '-' ) ||
             ( msa [ i ].charAt ( j ) == '.' ) )
        {
          msa [ i ].setCharAt ( j, '.' );
          j++;
        }  // if 
      }
      while ( msa [ i ].charAt ( j ) == '-' );

      j = msa [ i ].length () - 1;

      // Despace the end of each sequence.
/*
      while ( ( msa [ i ].charAt ( j ) == ' ' ) && ( j > 0 ) )
      {
        msa [ i ].deleteCharAt ( j );
        j--;
      }  // while
*/
      // Degap the end of each sequence.
      while ( ( isGap ( msa [ i ].charAt ( j ) ) == true ) && ( j > 0 ) )
      {
        msa [ i ].setCharAt ( j, '.' );
        j--;
      }  // while
    }  // for
  }  // method deGapEnds


/*******************************************************************************/
  private void gapAlignments ( String seq1, int pos1, String sub1 )
  {
    // System.out.println ( "gapAlignments: seq1 = " + seq1 + ", sub1 = " + sub1 );

    // Check for no seq1.
    if ( seq1.length () == 0 )
    {
      // Insert the same gap into the MSA.
      for ( int j = 0; j < total; j++ )
        msa [ j ].insert ( pos1, sub1 );
      return;
    }  // if

    StringBuffer template = new StringBuffer ();
    template.append ( seq1 );

    // Pad the template.
    while ( template.length () < sub1.length () )  template.append ( ' ' );

    // Search for gaps.
    for ( int i = 0; i < sub1.length (); i++ )
    {
      if ( template.charAt ( i ) != sub1.charAt ( i ) )
      {
        template.insert ( i, '-' );

        // Insert the same gap into the MSA.
        for ( int j = 0; j < total; j++ )
        {
          if ( pos1 + i > msa [ j ].length () )  msa [ j ].append ( '-' );
          else
            msa [ j ].insert ( pos1 + i, '-' );
        }  // for
      }  // if
    }  // for
  }  // method gapAlignments


/*******************************************************************************/
  private boolean isResidue ( char residue )
  {
    // Check for an amino acid residue letter.
    if ( ( ( residue >= 'a' ) && ( residue <= 'z' ) ) ||
         ( ( residue >= 'A' ) && ( residue <= 'Z' ) ) )  return true;

    return false;
  }  // method isResidue


/*******************************************************************************/
  private int countResidues ( StringBuffer msa_seq, int start, int end )
  { 
    int count = 0;

    if ( msa_seq.length () <= 0 )  return count;

    if ( start < 0 )  start = 0;

    for ( int i = start; i <= end; i++ )

      if ( i < msa_seq.length () )
      
        if ( isResidue ( msa_seq.charAt ( i ) ) == true )  count++;

    return count;
  }  // method countResidues


/*******************************************************************************/
  private int findEnd ( StringBuffer msa_seq )
  {
    int end = msa_seq.length () - 1;

    // Look for the last residue.
    while ( ( end >= 1 ) && ( isResidue ( msa_seq.charAt ( end ) ) == false ) )  

      end--;

    return end;
  }  // method findEnd


/*******************************************************************************/
  private int findStart ( StringBuffer msa_seq )
  {
    int start = 0;

    while ( ( start < msa_seq.length () - 1 ) &&
            ( isResidue ( msa_seq.charAt ( start ) ) == false ) )  start++;

    return start;
  }  // method findStart


/*******************************************************************************/
  public void trimAlignmentsEnds ()
  {
    // Check if MSA to trim.
    if ( total <= 1 )  return;

    // Trim the start of each aligned sequence, if it needs it.
    boolean done = false;
    int start = 0;
    for ( int i = 1; i < total; i++ )
    {
      start = findStart ( msa [ i ] );

      done = false;
      do
      {
        if ( countResidues ( msa [ i ], start, start + 20 ) >= 10 )  done = true;
        else
        {
          msa [ i ].setCharAt ( start, ' ' );		// blank residue
          start++;
        }  // else
      }
      while  ( ( done == false ) && ( start < msa [ i ].length () - 1 ) );
    }  // for 
  
    // Trim the end of each aligned sequence, if it needs it.
    int end = 0;
    for ( int i = 1; i < total; i++ )
    {
      end = findEnd ( msa [ i ] );

      done = false;
      do
      {
        if ( countResidues ( msa [ i ], end - 20, end ) >= 10 )  done = true;
        else
        {
          msa [ i ].setCharAt ( end , ' ' );		// blank residue
          end--;
        }  // else
      }
      while ( ( done == false ) && ( end > 1 ) );
    }  // for 
    
  }  // method trimAlignmentsEnds


/*******************************************************************************/
  private void addSubAlignment ( String seq1, int pos1, Dot align, boolean identity )
  {
    String sub1 = align.getAlignment1 ();
    String sub2 = align.getAlignment2 ();

    if ( identity == true )
    {
      // Copy the alignment of the second sequence to the MSA.
      for ( int i = 0; i < sub2.length (); i++ )

        if ( equals ( sub1.charAt ( i ), sub2.charAt ( i ) ) == true )
        {
          if ( pos1 + i < msa [ total - 1 ].length () )
            msa [ total - 1 ].setCharAt ( pos1 + i, sub2.charAt ( i ) );
        }  // if

      return;
    }  // if

    // Check if gaps need to be added to the MSA.
    if ( sub1.length () > seq1.length () )  gapAlignments ( seq1, pos1, sub1 );

    // Copy the alignment of the second sequence to the MSA.
    for ( int i = 0; i < sub2.length (); i++ )

      if ( pos1 + i >= msa [ total - 1 ].length () )
        msa [ total - 1 ].append ( sub2.charAt ( i ) );
      else
        msa [ total - 1 ].setCharAt ( pos1 + i, sub2.charAt ( i ) );
  }  // method addSubAlignment


/*******************************************************************************/
  public void setConsensus ()
  {
    consensus.setLength ( 0 );
    Consensus cons = new Consensus ();

    // Check if no sequences.
    if ( total <= 0 )  return;

    // Determine the longest length in the alignment.
    int length = getLongest ( total );

    for ( int i = 0; i < length; i++ )
    {
      for ( int j = 0; j < total; j++ )

        // Check if within this sequence.
        if ( i < msa [ j ].length () )

          cons.addLetter ( msa [ j ].charAt ( i ) );

      // Add the consensus letter.
      consensus.append ( cons.getConsensus () );
      cons.initialize ();
    }  // for
  }  // method setConsensus


/******************************************************************************/
  private boolean hasResidues ( String fragment )
  {
    // Scan the length of the fragment.
    for ( int i = 0; i < fragment.length (); i++ )

      // Search for an amino acid residue letter.
      if ( ( ( fragment.charAt ( i ) >= 'a' ) && ( fragment.charAt ( i ) <= 'z' ) ) ||
           ( ( fragment.charAt ( i ) >= 'A' ) && ( fragment.charAt ( i ) <= 'Z' ) ) )

        return true; 

    return false;		// no residues found
  }  // method hasResidues


/******************************************************************************/
  // This method tries to move sequences out of alignment gap positions.
  private void outsource ( int i )
  {
    // Check each of the sequences.
    for ( int j = 0; j < total; j++ )

      if ( i < msa [ j ].length () )

        // Look for non-gap letters.
        if ( isGap ( msa [ j ].charAt ( i ) ) == false )
          tryShift ( i, j );
  }  // method outsource


/******************************************************************************/
  // This method tries to shift a residue "msa [ seq_i ].charAt ( pos )" into a gap.
  private void tryShift ( int pos, int seq_i )
  {
    int ident1 = 0;		// count of identities to previous gap
    int ident2 = 0;		// count of identities to following gap
    int gap1 = -1;		// index of previous gap
    int gap2 = -1;		// index of following gap

    // Count identities before this position.
    boolean found_gap = false;
    if ( pos > 0 )
      for ( int k = pos - 1; (k >= 0) && (found_gap == false); k-- )
      {
        if ( isGap ( msa [ seq_i ].charAt ( k ) ) == true )
        {
          found_gap = true;
          gap1 = k;
        }  // if
        else
          // Check for identity to the first sequence.
          if ( equals ( msa [ seq_i ].charAt ( k ), msa [ 0 ].charAt ( k ) ) == true )
            ident1++;
      }  // for

    // Count identities after this position.
    found_gap = false;
    if ( pos + 1 < msa [ seq_i ].length () )
      for ( int k = pos + 1; (k < msa [ seq_i ].length ()) && (found_gap == false); k++ )
      {
        if ( isGap ( msa [ seq_i ].charAt ( k ) ) == true )
        {
          found_gap = true;
          gap2 = k;
        }  // if
        else
          // Check for identity to the first sequence.
          if ( equals ( msa [ seq_i ].charAt ( k ), msa [ 0 ].charAt ( k ) ) == true )
            ident2++;
      }  // for

    if ( ident1 >= 5 )  gap1 = -1;		// too many identities
    if ( ident2 >= 5 )  gap2 = -1;		// ...

    if ( ( ident2 < ident1 ) && ( gap2 >= 0 ) && ( ident2 < 5 ) ) 
      gap1 = -1;

    if ( ( ident1 < 5 ) && ( gap1 >= 0 ) )
    {
      System.out.println ( "shifted seq " + seq_i + " pos " + pos + " to " + gap1 );
      msa [ seq_i ].deleteCharAt ( gap1 );
      msa [ seq_i ].insert ( pos, '-' );
    }  // if
    else
      if ( ( ident2 < 5 ) && ( gap2 >= 0 ) )
      {
        System.out.println ( "shifted seq " + seq_i + " pos " + pos + " to " + gap2 );
        msa [ seq_i ].deleteCharAt ( gap2 );
        msa [ seq_i ].insert ( pos, '-' );
      }  // if
  }  // method tryShift


/******************************************************************************/
  // This method checks for MSA positions that are all gaps.
  private void killCheck ( int i )
  {
    boolean non_gap = false;
    for ( int j = 0; (j < total) && (non_gap == false); j++ )
      if ( i < msa [ j ].length () )
        if ( isGap ( msa [ j ].charAt ( i ) ) == false )
          non_gap = true;

    if ( non_gap == false )
    {
      // Delete this position.
      for ( int j = 0; (j < total) && (non_gap == false); j++ )
        if ( i < msa [ j ].length () )
          msa [ j ].deleteCharAt ( i );

      consensus.deleteCharAt ( i );
    }  // if
  }  // method killCheck


/******************************************************************************/
  private void shiftGaps ()
  {
    // Shift sequences out of consensus gap positions.
    for ( int i = 0; i < consensus.length (); i++ )
      if ( consensus.charAt ( i ) == '-' )
        outsource ( i );

    // Check for alignment positions to delete.
    for ( int i = consensus.length () - 1; i >= 0; i-- )
      if ( consensus.charAt ( i ) == '-' )
        killCheck ( i );
  }  // method shiftGaps


/******************************************************************************/
  public void printAlignments ()
  {
    // System.out.println ( "Alignments.printAlignments: total = " + total );
    setConsensus ();

    // countIdentities ();

    FastaSequence sequence = null;

    System.out.print ( "  # Seq. Name   Gene     ID   Taxon.     Species   " );
    System.out.println ( "           Product" );
    for ( int i = 0; i < total; i++ )
    {
      sequence = fastas [ i ];

      Format.intWidth ( (i+1), 3 );
      System.out.print ( " " );
      // Format.intWidth ( bests [ i ].getPercentFirst (), 3 );
      // System.out.print ( "% " );
      System.out.print ( sequence.getShortName ( 10 ) );
      System.out.print ( "  " + LineTools.pad ( sequence.getGene (), 8 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getId (), 4 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getTaxonKey (), 10 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getSpecies (), 20 ) );
      System.out.print ( " " + sequence.getDef () );
      System.out.println ();
    }  // for

    // Check for only a single sequence.
    if ( total <= 1 )  return;

    String fragment = "";		// alignment substring fragment

    // Find the longest sequence in the alignment.
    int length = getLongest ( total );
    
    for ( int i = 0; i < length; i += 50 )
    {
      System.out.println ();

      // Print out the position.
      System.out.println ( "            " + (i+1) );
      int end = i + 50;
      if ( end > length )  end = length;

      for ( int j = 0; j < total; j++ )
      {
        fragment = "";
        sequence = fastas [ j ];

        // Print out each subsequence.
        if ( ( end < length ) && ( end < msa [ j ].length () ) )
          fragment = msa [ j ].substring ( i, end );
        else
          if ( ( i > 0 ) && ( i < msa [ j ].length () ) )
            fragment = msa [ j ].substring ( i );

        if ( fragment.trim ().length () > 0 )
          if ( hasResidues ( fragment ) == true )
            System.out.println ( sequence.getShortName ( 10 ) + "  " + fragment );
      }  // for

      // Print out the consensus line.
      fragment = "";
      if ( i < consensus.length () )
      {
        if ( end < consensus.length () )
          fragment = consensus.substring ( i, end );
        else
          fragment = consensus.substring ( i );
      }  // if
      System.out.println ( "Consensus   " + fragment );
    }  // for
  }  // method printAlignments


/******************************************************************************/
  public static String deSpace ( String str )
  {
    StringBuffer line = new StringBuffer ();

    for ( int i = 0; i < str.length (); i++ )

      if ( str.charAt ( i ) != ' ' )  line.append ( str.charAt ( i ) );
      else line.append ( 'x' );

    return line.toString ();
  }  // method deSpace


/******************************************************************************/
  public String toString ( String group_name )
  {
    StringBuffer cons = new StringBuffer ();
    FastaSequence sequence = null;

    setConsensus ();

    String fragment = "";		// alignment substring fragment

    int end = 0;
    int length = 0;
    for ( int j = 0; j < total; j++ )
    {
      sequence = fastas [ j ];
      cons.append ( ">" + sequence.getName () + " " + sequence.getDescription () + "\n" );

      length = msa [ j ].length ();
      for ( int i = 0; i < length; i += 50 )
      {
        end = i + 50;
        if ( end > length )  end = length;

        fragment = "";

        // Print out each subsequence.
        if ( ( end < length ) && ( end < msa [ j ].length () ) )
          fragment = msa [ j ].substring ( i, end );
        else
          if ( ( i > 0 ) && ( i < msa [ j ].length () ) )
            fragment = msa [ j ].substring ( i );

        cons.append ( fragment + "\n" );
      }  // for
    }  // for

    // Add the consensus sequence.
    cons.append ( ">" + group_name + "\n" );
    for ( int i = 0; i < consensus.length (); i += 50 )
    {
      if ( i + 50 > consensus.length () )
        fragment = consensus.substring ( i );
      else
        fragment = consensus.substring ( i, i + 50 );

      cons.append ( deSpace ( fragment ) + "\n" );
    }  // for

    return cons.toString ();
  }  // method toString


/******************************************************************************/

}  // class Alignments
