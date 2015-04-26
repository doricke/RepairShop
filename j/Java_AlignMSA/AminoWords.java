
// import AminoWord;

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

public class AminoWords extends Object
{


/******************************************************************************/

  private final static byte WORD_SIZE = 3;	// default word_size


/******************************************************************************/

  private   int  common = 0;			// number of common words

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words

  private   byte  word_size = WORD_SIZE;	// amino word size

  private   AminoWord  words [] = null;		// array of words


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
  // Initialize class variables.
  public void initialize ()
  {
    common = 0;
    size = 0;
    total = 0;
    word_size = WORD_SIZE;
    words = null;
  }  // method initialize 


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
    size = value;

    words = new AminoWord [ size ];
    total = 0;
  }  // method setSize


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  public void addWord ( int word, String peptide, int pos )
  {
    if ( total >= size )
    {
      System.out.println ( "Words.addWord: *Warning* too many words added (" + total + ")" );
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
  public int countCommonWords ( int words2 [] )
  {
    int i1 = 0;			// index 1
    int i2 = 0;			// index 2

    int count = 0;		// number of common words
    while ( ( i1 < total ) && ( i2 < words2.length ) )
    {
      int words1 = words [ i1 ].getWord ();

      if ( words1 < words2 [ i2 ] )
        i1++;
      else
        if ( words1 > words2 [ i2 ] )
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
    common = 0;
    if ( total <= 0 )  return;

    for ( int i = 0; i < total; i++ )

      words [ i ].setCommon ( null );
  }  // method clearCommonWords


/******************************************************************************/
  private void computeAlignmentCounts ( AminoWord [] sorted )
  {
    for ( short i = (short) 1; i < sorted.length; i++ )
    {
      boolean found = false;
      short j = (short) ( i - 1 );

      while ( ( found == false ) && ( j >= 0 ) )
      {
        if ( sorted [ i ].getCommon ().getPosition () > 
             sorted [ j ].getCommon ().getPosition () )
        {
          sorted [ i ].setCount ( (short) ( sorted [ j ].getCount () + 1 ) );
          sorted [ i ].setPrevious ( j );
          found = true;
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
    }  // for
  }  // method createWords


/******************************************************************************/
  public AminoWord [] getSortedCommonWords ()
  {
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
  public void matchCommonWords ( AminoWord [] words2 )
  {
    common = 0;
    int i1 = 0;			// index 1
    int i2 = 0;			// index 2

    while ( ( i1 < total ) && ( i2 < words2.length ) )
    {
      // Check for end of words2 array.
      if ( words2 [ i2 ] == null )  return;

      if ( words [ i1 ].getWord () < words2 [ i2 ].getWord () )
        i1++;
      else
        if ( words [ i1 ].getWord () > words2 [ i2 ].getWord () )
          i2++;
        else  // words [ i1 ] == words2 [ i2 ]
        {
          words [ i1 ].setCommon ( words2 [ i2 ] );
          i1++;
          i2++;
          common++;
        }  // else
    }  // while
  }  // method matchCommonWords


/******************************************************************************/
  private AminoWord [] selectAlignment ( AminoWord [] sorted )
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

    // Allocate the alignment array.
    AminoWord [] alignment = new AminoWord [ best_count + 1 ];

    // Copy the alignment.
    int next_index = best_index;
    int i = best_count;
    while ( ( next_index >= 0 ) && ( i >= 0 ) )
    {
      alignment [ i ] = sorted [ next_index ];
      next_index = sorted [ next_index ].getPrevious ();
      i--;
    }  // while

System.out.println ( "Alignment" );
for ( int j=0; j<=best_count; j++ )
  System.out.println ( alignment [ j ].toString () );
System.out.println ();

    if ( i > 0 )
    {
      System.out.println ( "*Warning* improper previous chain!" );
    }  // if

    return alignment;
  }  // method setlectAlignment


/******************************************************************************/
  public void snap ()
  {
    System.out.println ( "total = " + total + ", size = " + size );
    for ( int i = 0; i < total; i++ )
      System.out.println ( (i+1) + "\t" + words [ i ].toString () );
  }  // method snap


/******************************************************************************/

}  // class AminoWords
