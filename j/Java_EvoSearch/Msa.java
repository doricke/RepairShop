
// import AminoWord;
// import AminoWords;
// import AlignTwo;
// import Best;
// import Consensus;
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

public class Msa extends Object
{


/******************************************************************************/

  private   Best [] bests = null;		// Sequence & Hash words

  // Consensus sequence.
  private   StringBuffer consensus = new StringBuffer ();

  private   StringBuffer [] msa = null;		// Protein MSA

  private   int  size = 0;			// Number of protein sequences

  private   int  total = 0;			// number of current words

  private   byte  word_size = 3;		// word size


/******************************************************************************/
  // Constructor Msa
  public Msa ()
  {
    initialize ();
  }  // constructor Msa


/******************************************************************************/
  // Initialize the class variables.
  public void initialize ()
  {
    size = 0;
    total = 0;
    consensus.setLength ( 0 );
  }  // method initialize 


/******************************************************************************/
  public StringBuffer getConsensus ()
  {
    return consensus;
  }  // method getConsensus


/******************************************************************************/
  public StringBuffer [] getMsa ()
  {
    return msa;
  }  // method getMsa


/******************************************************************************/
  public int getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public void setSize ( int value )
  {
    // Assert: valid value.
    if ( value <= 0 )
      System.out.println ( "Msa.setSize: *Warning* invalid size:" + value );
    else
    {
      size = value;
      bests = new Best [ value ];
      msa = new StringBuffer [ value ];

      // Initialize the array.
      for ( int i = 0; i < value; i++ )
      {
        bests [ i ] = null;
        msa [ i ] = new StringBuffer ();
      }  // for
    }  // else
  }  // method setSize


/******************************************************************************/
  public void addBest ( Best next_best, boolean identity )
  {
    // Assert: size set first.
    if ( size == 0 )
    {
      System.out.println ( "Msa.addBest: *Warning* size not set." );
      return;
    }  // if

    // Check if first sequence in the alignment.
    if ( total == 0 )
    {
      bests [ 0 ] = next_best;
      msa [ 0 ].append ( next_best.getFastaSequence ().getSequence () );
      total++;
      return;
    }  // if

    // Check for room for this sequence.
    if ( total + 1 > size )
    {
      System.out.println ( "Msa.addBest: *Warning* too many sequences." );
      return;
    }  // if

    // Add the new sequence to the alignment.
    bests [ total ] = next_best;
    total++;

    alignLastSequence ( identity );

    // printMsa ();
  }  // method addBest


/******************************************************************************/
  private void alignLastSequence ( boolean identity )
  {
    // Set of common words between two sequences being aligned.
    AminoWord []  common_words = null;

    // Align the last sequence to the first sequence.
    AminoWords target = new AminoWords ( msa [ 0 ].toString (), word_size );

    target.matchCommonWords ( bests [ total - 1 ].getWords () );
    common_words = target.getSortedCommonWords ();

    // Copy the identity words.
    setAlignments ( common_words );

    if ( common_words != null )
    {
      // Add the sub-alignments.
      setSubAlignments ( common_words, identity );
    }  // if
  }  // method alignLastSequence


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
  private void setAlignments ( AminoWord [] common_words )
  {
    String new_seq = bests [ total - 1 ].getFastaSequence ().getSequence ();

    int length = getLongest ( total - 1 );

    // Select the longer of the two sequences.
    if ( new_seq.length () > length )  length = new_seq.length ();
   
    // Initialize this sequence to blanks. 
    msa [ total - 1 ].setLength ( 0 );	
    for ( int i = 0; i < length; i++ )
      msa [ total - 1 ].append ( ' ' );

    // Assert: common_words is not null.
    if ( common_words == null )  return;

    // Fill in the identity residues from the common words.
    int position = 0;
    String aminos = "";
    for ( int j = 0; j < common_words.length; j++ )
    {
      position = -1;

      if ( common_words [ j ] != null )
      {
        position = common_words [ j ].getPosition ();
        aminos = common_words [ j ].getAminos ();
      }  // if

      if ( position >= 0 )
        // Copy the amino word
        for ( int k = 0; k < aminos.length (); k++ )
          msa [ total - 1 ].setCharAt ( position + k - 1, aminos.charAt ( k ) );
    }  // for

    // System.out.println ( "Msa.setAlignments:" );
    // printMsa ();

}  // method setAlignments


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
  public void countIdentities ()
  {
    if ( total < 1 )  return;

    bests [ 0 ].setPercentFirst ( (byte) 100 ); 
    // Count the identities to the first sequence.
    int identities = 0;
    int residues = 0;
    for ( int i = 1; i < total; i++ )
    {
      identities = 0;
      residues = 0;

      for ( int j = 0; j < msa [ 0 ].length (); j++ )

        if ( j < msa [ i ].length () )

          if ( isResidue ( msa [ i ].charAt ( j ) ) == true )
          {
            residues++;

            if ( equals ( msa [ 0 ].charAt ( j ), msa [ i ].charAt ( j ) ) == true )
          
              identities++;
          }  // if 

      if ( residues <= 0 )  bests [ i ].setPercentFirst ( (byte) 0 );
      else  bests [ i ].setPercentFirst ( (byte) ( (identities * 100) / residues ) );
    }  // for
    
  }  // method countIdentities


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
        if ( msa [ i ].charAt ( j ) == '-' )
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
      while ( ( msa [ i ].charAt ( j ) == '-' ) && ( j > 0 ) )
      {
        msa [ i ].setCharAt ( j, '.' );
        j--;
      }  // while
    }  // for
  }  // method deGapEnds


/*******************************************************************************/
  private void gapMsa ( String seq1, int pos1, String sub1 )
  {
    // System.out.println ( "gapMsa: seq1 = " + seq1 + ", sub1 = " + sub1 );

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
  }  // method gapMsa


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
  public void trimMsaEnds ()
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
    
  }  // method trimMsaEnds


/*******************************************************************************/
  private void addSubAlignment ( String seq1, int pos1, AlignTwo align, boolean identity )
  {
    // System.out.println ( "position1 = " + pos1 );
    // align.writeAlignment ();

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
    if ( sub1.length () > seq1.length () )  gapMsa ( seq1, pos1, sub1 );

    // Copy the alignment of the second sequence to the MSA.
    for ( int i = 0; i < sub2.length (); i++ )

      if ( pos1 + i >= msa [ total - 1 ].length () )
        msa [ total - 1 ].append ( sub2.charAt ( i ) );
      else
        msa [ total - 1 ].setCharAt ( pos1 + i, sub2.charAt ( i ) );

// System.out.println ( "addSubAlignment: msa[total-1] = '" + msa [ total-1 ] + "'" );

    // printMsa ();
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


/*******************************************************************************/
  private void setSubAlignments ( AminoWord [] common_words, boolean identity )
  {
    // Assert: common words.
    if ( common_words.length <= 0 )  return;

    byte anchor = AlignTwo.ANCHOR_3;
    byte word_size = (byte) 2;
    String seq1 = msa [ 0 ].toString ();
    String seq2 = bests [ total - 1 ].getFastaSequence ().getSequence ();

    AlignTwo align = new AlignTwo ();
    align.setWordSize ( (byte) 3 );
    align.setAnchor ( AlignTwo.ANCHOR_3 );

    // Get the end of the first sequence.
    int pos1 = common_words [ common_words.length - 1 ].getPosition () + word_size - 1;
    String sub1 = "";
    if ( seq1.length () > pos1 )  sub1 = seq1.substring ( pos1 ); 

    // Get the end of the second sequence.
    int pos2 = common_words [ common_words.length - 1 ].getCommon ()
        .getPosition () + word_size - 1;
    String sub2 = "";
    if ( seq2.length () > pos2 )  sub2 = seq2.substring ( pos2 ); 

    // Align the ends of the sequences.
    align.align ( sub1, sub2 );
    addSubAlignment ( sub1, pos1, align, identity );
    align.reset ();

    // Align from end to the start.
    align.setAnchor ( AlignTwo.INTERNAL );
    for ( int i = common_words.length - 2; i >= 0; i-- )
    {
/*
System.out.println ( "Msa.setSubAlignments: i = " + i );
System.out.println ( "common_words[i] = " + common_words [ i ].toString () );
System.out.println ( "common_words[i+1] = " + common_words [ i+1 ].toString () );
System.out.println ();
*/
      pos1 = common_words [ i ].getPosition () - 1;
      sub1 = seq1.substring ( pos1, common_words [ i+1 ].getPosition () - 1 );
      sub2 = seq2.substring ( common_words [ i ].getCommon ().getPosition () - 1,
                              common_words [ i+1 ].getCommon ().getPosition () - 1 );
      align.align ( sub1, sub2 );
      addSubAlignment ( sub1, pos1, align, identity );
      align.reset ();
    }  // for

    // Align the start of the sequence.
    align.setAnchor ( AlignTwo.ANCHOR_5 );
    sub1 = "";
    pos1 = common_words [ 0 ].getPosition () - 1;
    if ( pos1 > 0 )  sub1 = seq1.substring ( 0, pos1 );
    sub2 = "";
    if ( common_words [ 0 ].getCommon ().getPosition () > 1 )
      sub2 = seq2.substring ( 0, common_words [ 0 ].getCommon ().getPosition () - 1 );
    align.align ( sub1, sub2 );
    addSubAlignment ( sub1, 0, align, identity );
  }  // method setSubAlignments


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
  public void printMsa ()
  {
    // System.out.println ( "Msa.printMsa: total = " + total );
    setConsensus ();

    countIdentities ();

    FastaSequence sequence = null;

    System.out.print ( "  #    % Seq. Name   Gene     ID   Taxon.     Species   " );
    System.out.println ( "           Product" );
    for ( int i = 0; i < total; i++ )
    {
      sequence = bests [ i ].getFastaSequence ();

      Format.intWidth ( (i+1), 3 );
      System.out.print ( " " );
      Format.intWidth ( bests [ i ].getPercentFirst (), 3 );
      System.out.print ( "% " + sequence.getShortName ( 10 ) );
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
        sequence = bests [ j ].getFastaSequence ();

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
  }  // method printMsa


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
      sequence = bests [ j ].getFastaSequence ();
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

}  // class Msa
