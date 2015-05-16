

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

public class SequenceAlignment extends Object
{


/******************************************************************************/

  private   StringBuffer alignment = new StringBuffer ( 500 );	// sequence alignment

  private   int best_index = -1;			// best hit sequence

  private   double best_percent = 0.0;			// best percent identity

  private   StringBuffer conserved = new StringBuffer ( 500 );	// conserved regions

  private   int [] conserved_level = null;		// conserved level of residues

  private   String description = "";			// sequence description

  private   String file_name = "";			// sequence file name

  private   int good_aligned = 0;			// number of good alignments

  private   int identities = 0;				// number of amino acid identities from a pair-wise comparison

  private   String sequence = "";			// sequence

  private   String sequence_name = "";			// sequence name


/******************************************************************************/
  // Constructor SequenceAlignment
  public SequenceAlignment ()
  {
    initialize ();
  }  // constructor SequenceAlignment


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alignment.setLength ( 0 );
    best_index = -1;
    best_percent = 0.0;
    conserved.setLength ( 0 );
    conserved_level = null;
    description = "";
    file_name = "";
    good_aligned = 0;
    identities = 0;
    sequence = "";
    sequence_name = "";
  }  // method initialize 


/******************************************************************************/
  public void clearConserved ()
  {
    identities = 0;

    for ( int i = 0; i < conserved_level.length; i++ )

      conserved_level [ i ] = 0;
  }  // method clearConserved


/******************************************************************************/
  public String getAlignment ()
  {
    return alignment.toString ();
  }  // method getAlignment


/******************************************************************************/
  public int getBestIndex ()
  {
    return best_index;
  }  // method getBestIndex


/******************************************************************************/
  public double getBestPercent ()
  {
    return best_percent;
  }  // method getBestPercent


/******************************************************************************/
  public String getConserved ()
  {
    return conserved.toString ();
  }  // method getConserved


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public String getFileName ()
  {
    return file_name;
  }  // method getFileName


/******************************************************************************/
  public int getIdentities ()
  {
    return identities;
  }  // method getIdentities


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


/******************************************************************************/
  public int getSequenceLength ()
  {
    return sequence.length ();
  }  // method getSequenceLength


/******************************************************************************/
  public String getSequenceName ()
  {
    return sequence_name;
  }  // method getSequenceName


/******************************************************************************/
  public void setAlignment ( String value )
  {
    alignment.setLength ( 0 );
    alignment.append ( value );

    // If the sequence is not set, derive it from the alignment.
    if ( sequence.length () <= 0 )
    {
      StringBuffer seq = new StringBuffer ( value.length () );

      // Scan the alignment for the amino acid residues.
      for ( int i = 0; i < alignment.length (); i++ )
      {
        char aa = alignment.charAt ( i );	// amino acid

        // Check for an amino acid residue.
        if ( ( ( aa >= 'A' ) && ( aa <= 'Z' ) ) ||
             ( ( aa >= 'a' ) && ( aa <= 'z' ) ) )

          // Copy the amino acids to the sequence.
          seq.append ( aa );
      }  // for

      setSequence ( seq.toString () );
    }  // if
  }  // method setAlignment


/******************************************************************************/
  public void setBestIndex ( int value )
  {
    best_index = value;
  }  // method setBestIndex


/******************************************************************************/
  public void setBestPercent ( double value )
  {
    best_percent = value;
  }  // method setBestPercent


/******************************************************************************/
  public void setDescription ( String value )
  {
    description = value;
  }  // method setDescription


/******************************************************************************/
  public void setFileName ( String value )
  {
    file_name = value;
  }  // method setFileName


/******************************************************************************/
  private void initAlignment ()
  {
    // Initialize the alignment and conserved residues.
    alignment.setLength ( 0 );
    for ( int i = 0; i < sequence.length (); i++ )
    
      alignment.append ( ' ' );

    // One extra character for the replace method.
    alignment.append ( ' ' );

    initConserved ();
  }  // method initAlignment


/******************************************************************************/
  private void initConserved ()
  {
    // Initialize the alignment and conserved residues.
    conserved.setLength ( 0 );
    for ( int i = 0; i < sequence.length (); i++ )
    
      conserved.append ( '-' );

    // Check if the alignment is longer than the sequence.
    if ( alignment.length () > sequence.length () )
    {
      for ( int i = sequence.length (); i < alignment.length (); i++ )

        conserved.append ( '-' );
    }  // if

    // One extra character for the replace method.
    conserved.append ( ' ' );
  }  // method initConserved


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;

    // Initialize the conserved levels.
    conserved_level = new int [ sequence.length () ];
    for ( int i = 0; i < sequence.length (); i++ )
      conserved_level [ i ] = 0;
  }  // method setSequence


/******************************************************************************/
  public void setSequenceName ( String value )
  {
    sequence_name = value;
  }  // method setSequenceName


/******************************************************************************/
// This method returns the number of conserved residues in the alignment.
private int countConserved ()
{
  int count = 0;

  for ( int i = 0; i < sequence.length (); i++ )

    if ( conserved.charAt ( i ) != '-' )  count++;

  return count;
}  // method countConserved


/******************************************************************************/
// This method computes the percentage identity between the two sequences for the conserved domain.
private int percentDomainIdentity ()
{
  int count = 0;
  int start = 0;
  int end = 0;

  for ( int i = 0; i < sequence.length (); i++ )

    if ( conserved.charAt ( i ) != '-' )
    {
      count++;

      if ( start == 0 )  start = i;
      end = i;
    }  // if

  // Validate input.
  if ( ( sequence.length () <= 0 ) ||
       ( end - start + 1 <= 0 ) ||
       ( count <= 0 ) )  return 0;

  int percent = ( count * 100 ) / (end - start + 1);

  return percent;
}  // method percentDomainIdentity


/******************************************************************************/
// This method computes the percentage identity between the two sequences.
private int percentIdentity ( String sequence2 )
{
  int percent1 = 0;
  int percent2 = 0;

  // Count the number of conserved residues.
  int count = countConserved ();

  // Validate input.
  if ( ( sequence.length () <= 0 ) ||
       ( sequence2.length () <= 0 ) ||
       ( count <= 0 ) )  return 0;

  percent1 = ( count * 100 ) / sequence.length ();
  percent2 = ( count * 100 ) / sequence2.length ();

  // Return the higher percentage (for the shorter sequence).
  if ( percent1 > percent2 )  return percent1;
  return percent2;
}  // method percentIdentity


/******************************************************************************/
private void extendBackwards ( int seq_index, String sequence2, int seq2_index )
{
  int index1 = seq_index;
  int index2 = seq2_index;
  while ( ( index1 >= 0 ) &&
          ( index2 >= 0 ) &&
          ( conserved.charAt ( index1 ) == '-' ) )
  {
    // Check for an identity amino acid.
    if ( sequence.charAt ( index1 ) == sequence2.charAt ( index2 ) )

      conserved.setCharAt ( index1, sequence.charAt ( index1 ) );

    index1--;
    index2--;
  }  // while
}  // method extendBackwards


/******************************************************************************/
private void extendForwards 
    ( int seq_index
    , int last1
    , String sequence2
    , int seq2_index 
    , int last2
    )
{
  int index1 = seq_index;
  int index2 = seq2_index;
  while ( ( index1 < last1 ) &&
          ( index2 < last2 ) &&
          ( conserved.charAt ( index1 ) == '-' ) )
  {
    // Check for an identity amino acid.
    if ( sequence.charAt ( index1 ) == sequence2.charAt ( index2 ) )

      conserved.setCharAt ( index1, sequence.charAt ( index1 ) );

    index1++;
    index2++;
  }  // while
}  // method extendForwards


/******************************************************************************/
  // This method matches the sequence2 to sequence in word_size units.
  private void matchWords 
      ( String sequence2
      , int seq2_start
      , int seq1_start
      , int seq1_end
      , int word_size 
      )
  {
    int start = seq2_start;
    int last1 = -1;
    int last2 = -1;

    for ( int i = seq1_start; i + word_size <= seq1_end; i++ )
    {
      String word = "";
      if ( i + word_size < sequence.length () )
      {
        word = sequence.substring ( i, i + word_size );
      }  // if
      else
      {
        word = sequence.substring ( i );
      }  // else

      // Check for the current word in the second sequence.
      int word_index = sequence2.indexOf ( word, start );

      // Check for two copies of the word.
      if ( sequence2.indexOf ( word, word_index + 1 ) >= 0 )  word_index = -1;

      // Check if only one copy of the word was found.
      if ( word_index >= 0 )
      {
        conserved.replace ( i, i + word_size, word );

        extendBackwards ( i - 1, sequence2, word_index - 1 );

        if ( ( last1 > 0 ) && ( last1 < i ) )
          extendForwards ( last1, i, sequence2, last2, word_index );

        last1 = i + word_size;
        last2 = word_index + word_size;
      }  // if
    }  // for

    if ( last1 > 0 )
      extendForwards ( last1, sequence.length (), sequence2, last2, sequence2.length () );

  }  // method matchWords


/******************************************************************************/
  private void cleanAlignment ()
  {
    // Clean up orphan identities.
    int before;
    int after;
    for ( int i = 0; i < sequence.length (); i++ )
    {
      // Check for an identity position.
      if ( conserved.charAt ( i ) != '-' )
      {
        // Count the gaps prior to this position.
        before = 0;
        for ( int j = i - 6; j < i; j++ )
        {
          if ( j < 0 )  before++;
          else
            if ( conserved.charAt ( j ) == '-' )  before++;
        }  // for

        // Count the gaps after this position.
        after = 0;
        for ( int j = i + 1; j <= i + 6; j++ )
        {
          if ( j >= sequence.length () )  after++;
          else
            if ( conserved.charAt ( j ) == '-' )  after++;
        }  // for

        if ( before + after >= 11 )
          conserved.setCharAt ( i, '-' );		// erase the identity!!!
      }  // if
    }  // for
  }  // method cleanAlignment


/******************************************************************************/
  private void recordConserved ()
  {
    for ( int i = 0; i < sequence.length (); i++ )

      if ( conserved.charAt ( i ) != '-' )  conserved_level [ i ]++;
  }  // recordConserved


/******************************************************************************/
  public int align ( String sequence2 )
  {
    // Initialize the conserved alignment.
    initAlignment ();

    // Align the two sequences.
    matchWords ( sequence2, 0, 0, sequence.length (), 5 );

    // System.out.println ( "5-mers #" + conserved.toString () + "#" );

    cleanAlignment ();

    int percent_identity = percentIdentity ( sequence2 );

    int domain_percent_identity = percentDomainIdentity ();

    // System.out.println ( domain_percent_identity + "%:" + percent_identity + "% " + conserved.toString () );

    if ( ( domain_percent_identity >= 10 ) || ( percent_identity >= 10 ) )
    {
      // Record the conserved residues in the conserved_identity counts.
      recordConserved ();
    }  // if
    else
    {
      initAlignment ();

      if ( percent_identity <= 5 )  percent_identity = 0;
    }  // else

    return percent_identity;
  }  // method align


/******************************************************************************/
  public void printConservedLevels ()
  {
    System.out.println ();
    System.out.println ( file_name + "\t" + sequence_name );

    for ( int i = 0; i < sequence.length (); i++ )

      System.out.println ( sequence.charAt ( i ) + "\t" + conserved_level [ i ] );

    System.out.println ();
  }  // method printConservedLevels


/******************************************************************************/
  public void printConservedLevels ( int window )
  {
    System.out.println ();
    System.out.println ( file_name + "\t" + sequence_name );

    for ( int i = 0; i < sequence.length (); i++ )
    {
      System.out.print ( sequence.charAt ( i ) + ":" + conserved_level [ i ] + " " );

      if ( ( ( i + 1 ) % window ) == 0 )
        System.out.println ();
    }  // for

    System.out.println ();
  }  // method printConservedLevels


/******************************************************************************/
  public double compareAligned ( String sequence2_alignment )
  {
    int identities = 0;		// number of identity positions
    int seq1_index = 0;		// index of sequence #1 amino acids
    int seq2_length = 0;	// number of amino acids in sequence #2


    for ( int i = 0; i < alignment.length (); i++ )
    {
      // Check for an amino acid in sequence #2.
      if ( ( ( sequence2_alignment.charAt ( i ) >= 'A' ) && ( sequence2_alignment.charAt ( i ) <= 'Z' ) ) ||
           ( ( sequence2_alignment.charAt ( i ) >= 'a' ) && ( sequence2_alignment.charAt ( i ) <= 'z' ) ) )
     
        seq2_length++; 

      // Check for an amino acid in the alignment.
      if ( ( ( alignment.charAt ( i ) >= 'A' ) && ( alignment.charAt ( i ) <= 'Z' ) ) ||
           ( ( alignment.charAt ( i ) >= 'a' ) && ( alignment.charAt ( i ) <= 'z' ) ) )
      {
        // Check for an identity position.
        if ( ( i < sequence2_alignment.length () ) &&
             ( sequence2_alignment.charAt ( i ) == alignment.charAt ( i ) ) )
        {
          identities++;
          conserved_level [ seq1_index ]++;
        }  // if

        seq1_index++;
      }  // if
    }  // for

    double percent1 = ( identities * 100.0 ) / sequence.length ();
    double percent2 = ( identities * 100.0 ) / seq2_length;

    // Count the number of good alignments (percent identity >= 50%).
    if ( ( percent1 >= 45 ) || ( percent2 >= 45 ) )  good_aligned++;

    // Return the higher percentage (for the shorter sequence).
    if ( percent1 > percent2 )  return percent1;
    return percent2;
  }  // method compareAligned


/******************************************************************************/
  public double estimateV ()
  {
    double V = 0.5;

    // Total up the number of invariant positions in the alignment.
    int invariant = 0;
    for ( int i = 0; i < sequence.length (); i++ )
    {
      // Count up the invariant residue positions.
      // if ( conserved_level [ i ] + 1 >= good_aligned )
      if ( conserved_level [ i ] >= good_aligned )
      {
        invariant++;
      }  // if
    }  // for

    V = 1.0 - (1.0 * invariant) / (1.0 * sequence.length ());

/*
    System.out.println ( sequence_name + " good = " + good_aligned 
        + " invariants " + invariant + " of " + sequence.length () + " V = " + V );
*/

    return V;
  }  // method estimateV


/******************************************************************************/

}  // class SequenceAlignment
