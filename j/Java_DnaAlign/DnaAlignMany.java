
// import DnaAlignee;
// import CommonDnaWord;
// import CommonDnaWords;

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

public class DnaAlignMany extends Object
{


/******************************************************************************/

  public  final static byte MSA_START = 5;		// 5' end of MSA

  public  final static byte MSA_END = 3;		// 3' end of MSA

  public  final static byte MSA_INTERNAL = 1;		// MSA internal region


/******************************************************************************/

  private   StringBuffer [] alignments = null;	// alignments

  private   String  [] sequences = null;	// sequences to align

  private   byte  word_size = 12;		// comparison word size 


/******************************************************************************/
  // Constructor DnaAlignMany
  public DnaAlignMany ()
  {
    initialize ();
  }  // constructor DnaAlignMany


/******************************************************************************/
  // Constructor DnaAlignMany
  public DnaAlignMany ( String [] seqs, byte w_size )
  {
    initialize ();
    setWordSize ( w_size );
    setSequences ( seqs );
    align ();
  }  // constructor DnaAlignMany


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alignments = null;
    sequences = null;
    word_size = 12;
  }  // method initialize 


/******************************************************************************/
  public StringBuffer [] getAlignments ()
  {
    return alignments;
  }  // method getAlignments


/******************************************************************************/
  public String [] getSequences ()
  {
    return sequences;
  }  // method getSequences


/******************************************************************************/
  public byte getWordSize ()
  {
    return word_size;
  }  // method getWordSize


/******************************************************************************/
  public void setSequences ( String [] value )
  {
    sequences = value;

    // Allocate the alignments array.
    alignments = new StringBuffer [ value.length ];
    for ( int i = 0; i < value.length; i++ )
      alignments [ i ] = new StringBuffer ( 3000 );
  }  // method setSequences


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  private int computeEndLength ()
  {
    // Determine the current alignment length.
    int end_length = 0;
    
    for ( int i = 0; i < alignments.length; i++ )

      if ( alignments [ i ].length () > end_length )  

        end_length = alignments [ i ].length ();

    return end_length;
  }  // method computeEndLength


/******************************************************************************/
  private void evenLengths ()
  {
    // Determine the current alignment length.
    int max_length = computeEndLength ();

    for ( int i = 0; i < alignments.length; i++ )

      for ( int j = alignments [ i ].length (); j < max_length; j++ )

        alignments [ i ].append ( "-" );
  }  // method evenLengths


/******************************************************************************/
  private void copyWord ( int [] starts, int [] ends, int [] positions, int w_size )
  {
    System.out.println ( "copyWord called" );
    // printLimits ( starts, ends, positions );

    // Even out the alignment lengths.
    evenLengths ();

    // Process each sequence.
    for ( int i = 0; i < starts.length; i++ )
    {
      // alignments [ i ].append ( "[" );

      // Check for valid coordinates (some sequences may be truncated).
      if ( ( starts [ i ] >= 0 ) && ( positions [ i ] > 0 ) )
      {
        // Copy the current word to the alignments.
        for ( int j = starts [ i ]; j < ends [ i ] + w_size; j++ )
  
          alignments [ i ].append ( sequences [ i ].charAt ( j - 1 ) );
      }  // if

      // alignments [ i ].append ( "]" );
    }  // for
  }  // method copyWord


/******************************************************************************/
  private void printLimits ( int [] starts, int [] ends, int [] positions )
  {
    // Print out header.
    System.out.println ();
    System.out.print ( "i\tstarts[]\tends[]" );
    if ( positions != null )
      System.out.print ( "\tpositions[]" );
    System.out.println ();

    for ( int i = 0; i < starts.length; i++ )
    {
      System.out.print ( i + "\t" + starts [ i ] + "\t" + ends [ i ] );
      if ( positions != null )
        System.out.print ( "\t" + positions [ i ] );
      System.out.println ();
    }  // for

    System.out.println ();
  }  // method printLimits


/******************************************************************************/
  private int [] matchEnds 
      ( int [] starts
      , int [] ends
      , int [] positions
      , byte location 
      )
  {
    // Determine the maximum alignment length for the subsequences.
    int max_len = computeEndLength ();

    // Back fill shorter alignments.
    for ( int i = 0; i < starts.length; i++ )

      // Only check alignment lengths for the subsequences.
      if ( ends [ i ] >= starts [ i ] )
      {
System.out.println ( "Backfill: i = " + i + ", max_len = " + max_len + ", alignment.len = " + alignments [ i ].length () );

        if ( alignments [ i ].length () < max_len )  
        {
          // Check if at the start of the alignment.
          if ( alignments [ i ].length () == 0 )
          {
            int seq_len = ends [ i ] - starts [ i ];

            for ( int j = 0; j < max_len - seq_len; j ++ )

              alignments [ i ].append ( "+" );
          }  // if

          int to_copy = max_len - alignments [ i ].length ();

          for ( int j = 0; j < to_copy; j++ )

            if ( starts [ i ] - 1 < sequences [ i ].length () )
            {
System.out.println ( "Backfilling: " + sequences [ i ].charAt ( starts [ i ] - 1 ) );

              alignments [ i ].append ( sequences [ i ].charAt ( starts [ i ] - 1 ) );

              starts [ i ]++;
            }  // if
        }  // if 
      }  // if

    return starts;
  }  // method matchEnds


/******************************************************************************/
  private void subAlign 
      ( int [] starts
      , int [] ends
      , int [] positions
      , byte location 
      )
  {
    // System.out.println ( "subAlign called" );
    // printLimits ( starts, ends, positions );

    // Compute the subsequence lengths.
    boolean same_lengths = true;
    int [] lens = new int [ starts.length ];
    int common_length = 0;
    int max_length = 0;
    for ( int i = 0; i < starts.length; i++ )
    {
      lens [ i ] = ends [ i ] - starts [ i ];
      if ( ends [ i ] == -1 )  lens [ i ] = 0;

      // Check for a subsequence.
      if ( lens [ i ] > 0 )
      {
        // Check if first length.
        if ( common_length == 0 )  common_length = lens [ i ];

        // Check if all lengths are the same length.
        if ( lens [ i ] != common_length )

          same_lengths = false;

        // Save the maximum length observed.
        if ( alignments [ i ].length () + lens [ i ] > max_length )  

          max_length = alignments [ i ].length () + lens [ i ];
      }  // if
    }  // for

    // Check for no subsequences.
    if ( common_length == 0 )  return;


    // Minimize gaps rule.
    if ( ( same_lengths == true ) && 
         ( common_length <= 10 ) )
    {
      // Process each sequence. 
      for ( int i = 0; i < starts.length; i++ )
      {
        // Check for a valid subsequence.
        if ( lens [ i ] > 0 )

          // Copy the subsequence.
          alignments [ i ].append ( sequences [ i ].substring ( starts [ i ] - 1, ends [ i ] - 1 ) );
      }  // for

      return;
    }  // if


    // Check for smallest word size.
    if ( word_size == 1 )
    {
      // Just copy the sequence.
      for ( int i = 0; i < starts.length; i++ )

        if ( lens [ i ] > 0 )

          alignments [ i ].append ( sequences [ i ].substring ( starts [ i ] - 1, ends [ i ] - 1 ) );

      // Even out the sequence lengths with gaps.
      for ( int i = 0; i < starts.length; i++ )
      {
        if ( lens [ i ] > 0 )

          // Pad the sequence.
          for ( int j = alignments [ i ].length () + lens [ i ]; j < max_length; j++ )

            alignments [ i ].append ( "." );
      }  // for

      return;
    }  // if

    // Fill in sequences to match the end points.
    starts = matchEnds ( starts, ends, positions, location );

    // Align the subsequences.
    String [] subsequences = new String [ starts.length ];
    int subsequences_count = 0;
    for ( int i = 0; i < starts.length; i++ )
    {
      if ( ends [ i ] > starts [ i ] )
      {
        subsequences [ i ] = sequences [ i ].substring ( starts [ i ] - 1, ends [ i ] - 1 );
        subsequences_count++;
      }  // if
      else
        subsequences [ i ] = "";
    }  // for

    // Check for no subsequences.
    if ( subsequences_count == 0 )  return;

    // Check for only one subsequence.
    if ( subsequences_count == 1 )
    {
      for ( int i = 0; i < starts.length; i++ )
    
        if ( ends [ i ] > starts [ i ] )
    
          alignments [ i ].append ( sequences [ i ].substring ( starts [ i ] - 1, ends [ i ] - 1 ) );
      return;
    }  // if

    // Align the subsequences using a smaller word size.
    DnaAlignMany smaller = new DnaAlignMany ( subsequences, (byte) ( word_size / 2 ) );

    // Get the subsequence alignments.
    StringBuffer [] segments = smaller.getAlignments ();

    // Copy the alignments.
    for ( int i = 0; i < starts.length; i++ )

      alignments [ i ].append ( segments [ i ] );

    segments = null;
    smaller.initialize ();
    smaller = null;
  }  // method subAlign


/******************************************************************************/
  private void processTails ( int [] starts, int [] ends )
  {
    // System.out.println ( "processTails called" );
    // printLimits ( starts, ends, null );

    boolean found_tail = false;
    for ( int i = 0; i < sequences.length; i++ )
    {
      ends [ i ] = sequences [ i ].length () + 1;

      // Check for a sequence tail.
      if ( starts [ i ] < ends [ i ] )  found_tail = true;
    }  // for

    // Align the tail sequences.
    if ( found_tail == true )

      subAlign ( starts, ends, null, MSA_END );
  }  // method processTails


/******************************************************************************/
  public void align ()
  {
    // Align sequences using hash words.
    DnaAlignee [] alignees = new DnaAlignee [ sequences.length ];
    for ( int i = 0; i < sequences.length; i++ )
      alignees [ i ] = new DnaAlignee ( sequences [ i ], word_size );

    // Match the common words between the many sequences.
    CommonDnaWords common_words = new CommonDnaWords ( alignees );

    CommonDnaWord [] sorted_words = common_words.getSortedCommonDnaWords ();
    common_words.initialize ();
    common_words = null;

System.out.println ( "DnaAlignMany.align: word_size = " + word_size + ", sorted_words = " + sorted_words.length );

    // Traverse the sequences.
    int [] starts = new int [ sequences.length ];
    int [] ends = new int [ sequences.length ];
    for ( int i = 0; i < sequences.length; i++ )
    {
      starts [ i ] = 1;
      ends [ i ] = -1;
    }  // for

    byte location = MSA_START;

    int [] positions = null;
    for ( int i = 0; i < sorted_words.length; i++ )
    {
      positions = sorted_words [ i ].getPositions ();

      // Check for common words for all sequences.
      if ( sorted_words [ i ].getCount () == sequences.length )
      {
System.out.println ( "align: sorted_words[i].count = " + sorted_words [ i ].getCount () );

        // Set the ends to the start of the next common word.
        int next_position;
        for ( int j = 0; j < sequences.length; j++ )
        {
          next_position = positions [ j ];
          if ( next_position > ends [ j ] )
            ends [ j ] = next_position; 
        }  // for
 
        // Check for letters before the common word.
        subAlign ( starts, ends, positions, location );
        location = MSA_INTERNAL;
 
        snapAlignment ( "after subAlign" );
  
        // Set the start positions to the start of this word.
        for ( int j = 0; j < sequences.length; j++ )
 
          if ( starts [ j ] < ends [ j ] )
 
            starts [ j ] = ends [ j ];
 
System.out.println ( "current word = " + sorted_words [ i ].toString () );
 
        // Copy this word.
        copyWord ( starts, ends, positions, sorted_words [ i ].getWordSize () );
 
        snapAlignment ( "after copyWord" );
 
        // Advance the start positions.
        for ( int j = 0; j < sequences.length; j++ )
 
          if ( positions [ j ] > 0 )
 
            starts [ j ] = ends [ j ] + word_size;
      }  // if
    }  // for


    // Check for tails at the end of the sequences.
    processTails ( starts, ends );
  }  // method align


/******************************************************************************/
  public void align ( String [] seqs )
  {
    setSequences ( seqs );
    align ();
  }  // method align


/******************************************************************************/
  private void snapAlignment ( String where )
  {
    System.out.println ();
    System.out.println ( "Main Alignments: " + where );
    for ( int i = 0; i < alignments.length; i++ )

      System.out.println ( i + "\t\"" + alignments [ i ].toString () +  "\"" );
  }  // method snapAlignment


/******************************************************************************/
  public void printAlignment ()
  {
    System.out.println ();

    // Determine the alignment length.
    int max_length = 0;
    for ( int j = 0; j < alignments.length; j++ )
    {
      if ( alignments [ j ].length () > max_length )

        max_length = alignments [ j ].length ();
    }  // for

    for ( int i = 0; i < max_length; i += 50 )
    {
      // Print out the first sequence.
      System.out.print ( (i+1) );

      for ( int j = 0; j < alignments.length; j++ )

        if ( i + 50 < alignments [ j ].length () )

            System.out.println ( "\t" + alignments [ j ].substring ( i, i + 50 ) );

        else
        {
          if ( i < alignments [ j ].length () )
            System.out.println ( "\t" + alignments [ j ].substring ( i ) );
          else
            System.out.println ();
        }  // else

      System.out.println ();
    }  // for
  }  // method printAlignment


/******************************************************************************/
  public void printAlignmentVertical ()
  {
    System.out.println ();

    int max_length = 0;
    for ( int j = 0; j < alignments.length; j++ )
    {
      if ( alignments [ j ].length () > max_length )

        max_length = alignments [ j ].length ();
    }  // for

    for ( int i = 0; i < max_length; i++ )
    {
      // Print out the first sequence.
      System.out.print ( (i+1) + "\t" );

      for ( int j = 0; j < alignments.length; j++ )

        if ( i < alignments [ j ].length () )

          System.out.print ( alignments [ j ].charAt ( i ) );

        else

          System.out.print ( " " );

      System.out.println ();
    }  // for
  }  // method printAlignmentVertical


/******************************************************************************/
  public static void main ( String [] args )
  {
    String [] strs = new String [ 3 ];
    strs [ 0 ] = "ACGTAAAACCCGGTCTATGCA";
    strs [ 1 ] = "ACGTAAAAACCGGTCTATGCATGA";
    strs [ 2 ] = "ACGTAAAAACCCGGTCTATGCA";

    DnaAlignMany app = new DnaAlignMany ( strs, (byte) 5 );
    app.printAlignment ();
  }  // main


/******************************************************************************/

}  // class DnaAlignMany
