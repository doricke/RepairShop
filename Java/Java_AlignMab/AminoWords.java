
// import AminoWord;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

public class AminoWords extends Object
{


/******************************************************************************/

  private final static byte WORD_SIZE = 3;	// default word_size


/******************************************************************************/

  private   int  common = 0;			// number of common words

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words

  private   byte  word_size = WORD_SIZE;	// amino word size

  private   AminoWord [] words = null;		// array of words


/******************************************************************************/
  // Constructor AminoWords
  public AminoWords ()
  {
    initialize ();
  }  // constructor AminoWords


/******************************************************************************/
  // Constructor AminoWords
  public AminoWords ( String sequence, byte w_size )
  {
    initialize ();
    word_size = w_size;

    // Set up the amino acids words array.
    createWords ( sequence );
  }  // constructor AminoWords


/******************************************************************************/
  // Initialize the class variables.
  public void initialize ()
  {
    common = 0;
    size = 0;
    total = 0;
    word_size = WORD_SIZE;
    words = null;
  }  // method initialize 


/******************************************************************************/
  // Reset the class variables.
  public void reset ()
  {
    clearCommonWords ();
    total = 0;
  }  // method reset


/******************************************************************************/
  public int getCommonWordsCount ()
  {
    return common;
  }  // method getCommonWordsCount


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
  public AminoWord getWord ( int index )
  {
    if ( ( index < 0 ) || ( index >= total ) || ( index >= words.length ) )  return null;

    return words [ index ];
  }  // method getWord


/******************************************************************************/
  public AminoWord [] getWords ()
  {
    return words;
  }  // method getWords


/******************************************************************************/
  public byte getWordSize ()
  {
    return word_size;
  }  // method getWordSize


/******************************************************************************/
  public void setSize ( int value )
  {
    // System.out.println ( "AminoWords.setSize: size = " + value );

    size = value;

    words = new AminoWord [ size ];
    total = 0;
  }  // method setSize


/******************************************************************************/
  public void setTotal ( int value )
  {
    total = value;
  }  // method setTotal


/******************************************************************************/
  public void setWords ( AminoWord [] value )
  {
    total = 0;
    words = value;
    if ( value == null )  return;

    size = value.length;
  }  // method setWords


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  public void zeroTotal ()
  {
    total = 0;
  }  // method zeroTotal


/******************************************************************************/
  public void addWord ( AminoWord word )
  {
    if ( total >= size )
    {
      System.out.println ( "AminoWords.addWord: *Warning* too many words added (" 
          + total + ") of " + size );
      int err = total / 0;
      return;
    }  // if

    // Find where to insert the current word.
    int i = total;
    total++;

    // Shift the words until the insertion site is found.
    while ( ( i > 0 ) && ( words [ i - 1 ].getWord () > word.getWord () ) )
    {
      words [ i ] = words [ i - 1 ];
      i--; 
    }  // while

    words [ i ] = word;
  }  // method addWord


/******************************************************************************/
  public void addWord ( int word, String peptide, int pos )
  {
    if ( total >= size )
    {
      System.out.println ( "Words.addWord: *Warning* too many words added (" + total + ")" );
      int err = total / 0;
      return;
    }  // if

    // Find where to insert the current word.
    int i = total;
    total++;

    // Shift the words until the insertion site is found.
    while ( ( i > 0 ) && ( words [ i - 1 ].getWord () > word ) )
    {
      words [ i ] = words [ i - 1 ];
      i--; 
    }  // while

    AminoWord amino_word = new AminoWord ( word, peptide, pos, word_size );
    words [ i ] = amino_word;
  }  // method addWord


/******************************************************************************/
  public int countCommonWords ( AminoWords words2 )
  {
    int i1 = 0;			// index 1
    int i2 = 0;			// index 2

    if ( words2 == null )  return 0;

    int count = 0;		// number of common words
    while ( ( i1 < total ) && ( i2 < words2.getTotal () ) )
    {
      int words1 = words [ i1 ].getWord ();

      if ( words1 < words2.getWord ( i2 ).getWord () )
        i1++;
      else
        if ( words1 > words2.getWord ( i2 ).getWord () )
          i2++;
        else  // words1 == words2 [ i2 ]
        {
          count++;
          i1++;
          i2++;
        }  // else
    }  // while

    return count;
  }  // method countCommonWords


/******************************************************************************/
  public void clearCommonWords ()
  {
    common = 0;				// initialize

    if ( words == null )  return;

    for ( int i = 0; i < words.length; i++ )

      if ( words [ i ] != null )
      {
        words [ i ].setCommon ( null );
        words [ i ].setCount ( -1 );
        words [ i ].setPrevious ( -1 );
      }  // if
  }  // method clearCommonWords


/******************************************************************************/
  private void clearCounts ( AminoWord [] sorted )
  {
    if ( sorted == null )  return;

    for ( int i = 0; i < sorted.length; i++ )
      if ( sorted [ i ] != null )
        sorted [ i ].setCount ( 0 );
  }  // method clearCounts


/******************************************************************************/
  private void computeAlignmentCounts ( AminoWord [] sorted )
  {
    for ( int i = 1; i < sorted.length; i++ )
    {
      // boolean found = false;
      int j = i - 1;

      // while ( ( found == false ) && ( j >= 0 ) )
      while ( j >= 0 )
      {
        if ( ( sorted [ i ].getCommon ().getPosition () > 
               sorted [ j ].getCommon ().getPosition () ) &&
             ( sorted [ i ].getPosition () > sorted [ j ].getPosition () ) )
        {
          if ( sorted [ j ].getCount () + 1 > sorted [ i ].getCount () )
          {
            sorted [ i ].setCount ( sorted [ j ].getCount () + 1 );
            sorted [ i ].setPrevious ( j );
          }  // if
          // found = true;
        }  // if

        j--;
      }  // while
    }  // for
  }  // method computeAlignmentCounts


/******************************************************************************/
  public void createWords ( String seq )
  {
    // Validate the sequence length.
    int length = seq.length ();
    if ( length <= 0 )  return;

    // Compute the number of amino acid words in this sequence.
    if ( ( size < length - word_size + 1 ) || ( words == null ) )
      setSize ( length - word_size + 1 );

    // Amino acid hash word.
    AminoWord amino_word = new AminoWord ();
    amino_word.setSize ( word_size );

    // Hash the sequence.
    String word;
    for ( int i = 0; i < length - word_size + 1; i++ )
    {
      if ( i + word_size < length )
        word = seq.substring ( i, i + word_size );
      else
        word = seq.substring ( i );

      // Check for a word with no duplicate amino acids.
      if ( amino_word.isGoodWord ( word ) == true )
      {
        amino_word.setAminos ( word );
        addWord ( amino_word.getWord (), word, i + 1 );
      }  // if
      // else
        // System.out.println ( "*Bad Amino Word* " + word );
    }  // for
  }  // method createWords


/******************************************************************************/
  public AminoWord [] getSortedCommonWords ()
  {
    // Check for no common words.
    if ( total <= 0 )  return null;

    AminoWord [] sorted = new AminoWord [ common ];

    int sorted_total = 0;

    // Scan all of the words for common words.
    for ( int i = 0; i < total; i++ )

      // Check for a common word.
      if ( words [ i ].getCommon () != null )
      {
        // Insert the common word.
        insert ( sorted, words [ i ], sorted_total );

        sorted_total++;
      }  // if

    // Compute the alignment counts.
    computeAlignmentCounts ( sorted );

    return selectAlignment ( sorted );
  }  // method getSortedCommonWords


/******************************************************************************/
  private void insert ( AminoWord [] sorted, AminoWord word, int sorted_total )
  {
    // Check for the first record.
    if ( sorted_total == 0 )
    {
      sorted [ 0 ] = word;
      return;
    }  // if

    // Find the insertion position.
    int i = sorted_total;
    while ( ( i > 0 ) &&
            ( word.getPosition () < sorted [ i - 1 ].getPosition () ) )
    {
      sorted [ i ] = sorted [ i - 1 ];		// shift down the array
      i--;					// new insertion position
    }  // while

    // insert.
    sorted [ i ] = word;
  }  // method insert


/******************************************************************************/
  public void matchCommonWords ( AminoWords words2 )
  {
    common = 0;			// initialize
    int i1 = 0;			// index 1
    int i2 = 0;			// index 2

    while ( ( i1 < total ) && ( i2 < words2.getTotal () ) )
    {
      // Check for end of words2 array.
      if ( words2.getWord ( i2 ) == null )  return;

      if ( words [ i1 ].getWord () < words2.getWord ( i2 ).getWord () )
        i1++;
      else
        if ( words [ i1 ].getWord () > words2.getWord ( i2 ).getWord () )
          i2++;
        else  // words [ i1 ] == words2 [ i2 ]
        {
          words [ i1 ].setCommon ( words2.getWord ( i2 ) );
          i1++;
          i2++;
          common++;
        }  // else
    }  // while
  }  // method matchCommonWords


/******************************************************************************/
  private AminoWord []  cleanGaps ( AminoWord [] sorted )
  {
    // Check for no alignment.
    if ( sorted.length <= 0 )  return sorted;

    int best_count = 0;
    int best_index = 0;

    // Find the best count.
    for ( int i = 0; i < sorted.length; i++ )

      if ( sorted [ i ].getCount () > best_count )
      {
        best_count = sorted [ i ].getCount ();
        best_index = i;
      }  // if

    // Drop extra alignment gaps..
    int delta1 = 100;
    int delta2 = 100;
    int previous = -1;
    int prior = best_index;
    int current = sorted [ best_index ].getPrevious ();
    boolean match = false;
    if ( current >= 0 )
    {
      previous = sorted [ current ].getPrevious ();
      delta1 = sorted [ prior   ].getPosition ()
             - sorted [ current ].getPosition () + 1;
      delta2 = sorted [ prior   ].getCommon ().getPosition ()
             - sorted [ current ].getCommon ().getPosition () + 1;
      match = ( delta1 == delta2 );
      if ( match == false )
      {
        sorted [ prior ].setCount ( 0 );  // drop prior
        sorted [ prior ].setPrevious ( -1 );
      }  // if
    }  // if

    while ( previous >= 0 )
    {
      delta1 = sorted [ current ].getPosition ()
             - sorted [ previous ].getPosition () + 1;
      delta2 = sorted [ current  ].getCommon ().getPosition () 
             - sorted [ previous ].getCommon ().getPosition ();

      match = ( delta1 == delta2 );
      if ( ( delta1 != delta2 ) && ( match == false ) )
      {
/*
System.out.println ( sorted [ previous ].getAminos () + " " + new_delta + " " +
    sorted [ current ].getAminos () + " " + delta + 
    sorted [ prior ].getAminos () );
*/
        sorted [ prior ].setPrevious ( previous );	// drop current
        sorted [ current ].setPrevious ( -1 );
        sorted [ current ].setCount ( 0 );
      }  // if
      else
        prior = current;

      match = ( delta1 == delta2 );
      current = previous;
      previous = sorted [ previous ].getPrevious ();
    }  // if

    return sorted;
  }  // method cleanGaps


/******************************************************************************/
  private AminoWord []  cleanPath ( AminoWord [] sorted )
  {
    // Check for no alignment.
    if ( sorted.length <= 0 )  return sorted;

    int best_count = 0;
    int best_index = 0;

    // Find the best count.
    for ( int i = 0; i < sorted.length; i++ )

      if ( sorted [ i ].getCount () > best_count )
      {
        best_count = sorted [ i ].getCount ();
        best_index = i;
      }  // if

    // Clean the path.
    int delta = 100;
    int new_delta = 100;
    int previous = -1;
    int prior = best_index;
    int current = sorted [ best_index ].getPrevious ();
    if ( current >= 0 )
    {
      previous = sorted [ current ].getPrevious ();
      delta = sorted [ prior   ].getPosition ()
            - sorted [ current ].getPosition () + 1;
      if ( delta >= 10 )
      {
        sorted [ prior ].setCount ( 0 );  // drop prior
        sorted [ prior ].setPrevious ( -1 );
      }  // if
    }  // if

    while ( previous >= 0 )
    {
      delta = sorted [ prior   ].getPosition ()
            - sorted [ current ].getPosition () + 1;
      new_delta = sorted [ current  ].getPosition () 
                - sorted [ previous ].getPosition ();

/*
System.out.println ();
System.out.println ( "cleanPath: delta1 = " + delta + ", delta2 = " + new_delta );
System.out.println ( "previous = " + previous + ", current = " + current + ", prior = " + prior );
*/
/*
System.out.println ( "Comparing positions:" );
System.out.println ( sorted [ current ].toString () );
System.out.println ( sorted [ prior ].toString () );
*/
      if ( ( delta >= 15 ) && ( new_delta >= 15 ) ) 
      {

/*
System.out.println ( sorted [ previous ].getAminos () + " " + new_delta + " " +
    sorted [ current ].getAminos () + " " + delta + 
    sorted [ prior ].getAminos () );
*/
        sorted [ prior ].setPrevious ( previous );	// drop current
        sorted [ current ].setPrevious ( -1 );
        sorted [ current ].setCount ( 0 );

// System.out.println ( "deleting: " + sorted [ current ].toString () );
// System.out.println ( "prior: " + sorted [ prior ].toString () );
      }  // if
      else
        prior = current;

      current = previous;
      previous = sorted [ previous ].getPrevious ();
    }  // if

    // Check if at end of list.
    if ( current == -1 )  return sorted;

    // Check this distance on the first word.
    // System.out.println ( "prior = " + prior + ", current = " + current );

    delta = sorted [ prior   ].getPosition ()
          - sorted [ current ].getPosition () + 1;

    if ( delta >= 10 ) 
    {
      sorted [ prior ].setPrevious ( -1 );	// drop current
      sorted [ current ].setPrevious ( -1 );
      sorted [ current ].setCount ( 0 );
    }  // if

    return sorted;
  }  // method cleanPath


/******************************************************************************/
  private int countChainLength ( AminoWord [] sorted, int index )
  {
    int count = 0;

    int next_index = index;
    while ( next_index >= 0 )
    {
      count++;
      next_index = sorted [ next_index ].getPrevious ();
    }  // while

    return count;
  }  // method countChainLength


/******************************************************************************/
  private AminoWord [] selectAlignment ( AminoWord [] sorted )
  {

// System.out.println ( "selectAlignment: words array" );
// snap();

    // Check for no alignment.
    if ( sorted.length <= 0 )  return sorted;

    int best_count = 0;
    int best_index = 0;

    // Find the best count.
    for ( int i = 0; i < sorted.length; i++ )

      if ( sorted [ i ].getCount () > best_count )
      {
        best_count = sorted [ i ].getCount ();
        best_index = i;
      }  // if

    // Allocate the alignment array.
    int new_count = countChainLength ( sorted, best_index );
    AminoWord [] alignment = new AminoWord [ new_count ];

    // Copy the alignment.
    int next_index = best_index;
    int i = new_count - 1;
    while ( ( next_index >= 0 ) && ( i >= 0 ) )
    {
      alignment [ i ] = sorted [ next_index ];
      next_index = sorted [ next_index ].getPrevious ();
      alignment [ i ].setCount ( i );
      alignment [ i ].setPrevious ( i - 1 );
      i--;
    }  // while

    // System.out.println ( "selectAlignment:" );
    // snap ( alignment );

    if ( i > 0 )
    {
      System.out.println ( "*Warning* improper previous chain!" );
    }  // if

    return alignment;
  }  // method selectAlignment


/******************************************************************************/
  public void snap ()
  {
    snap ( words );
  }  // method snap


/******************************************************************************/
  public static void snap ( AminoWord [] words )
  {
    System.out.println ( "length = " + words.length );
    for ( int i = 0; i < words.length; i++ )

      if ( words [ i ] != null )

        System.out.println ( (i+1) + "\t" + words [ i ].toString () );
  }  // method snap


/******************************************************************************/

}  // class AminoWords
