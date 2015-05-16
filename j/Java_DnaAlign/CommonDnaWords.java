
// import DnaAlignee;
// import DnaWord;
// import CommonDnaWord;

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

public class CommonDnaWords extends Object
{


/******************************************************************************/

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words

  private   CommonDnaWord  words [] = null;	// array of words


/******************************************************************************/
  // Constructor CommonDnaWords
  public CommonDnaWords ()
  {
    initialize ();
  }  // constructor CommonDnaWords


/******************************************************************************/
  // Constructor CommonDnaWords
  public CommonDnaWords ( DnaAlignee [] alignees )
  {
    initialize ();

    // Determine the common words.
    mergeWordLists ( alignees );
  }  // constructor CommonDnaWords


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    size = 0;
    total = 0;
    words = null;
  }  // method initialize 


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
  public CommonDnaWord [] getWords ()
  {
    return words;
  }  // method getWords


/******************************************************************************/
  private void setSize ( int value )
  {
    if ( ( size == 0 ) || ( total == 0 ) )
    {
      size = value;
      words = new CommonDnaWord [ size ];
      total = 0;
      return;
    }  // if

    if ( value < size )  return;

    // Extend the array length.

    // Copy the existing words to the new array.
    size = value;
    CommonDnaWord [] words2 = new CommonDnaWord [ size ];
    for ( int i = 0; i < total; i++ ) 
    {
      words2 [ i ] = words [ i ];
      words [ i ] = null;
    }  // for
    words = words2;
  }  // method setSize


/******************************************************************************/
  private CommonDnaWord addWord ( DnaWord amino_word, int seq_count )
  {
    int word = amino_word.getWord ();

    if ( total >= size )  setSize ( size * 4 );

    // Find where to insert the current word.
    int i = total;
    total++;

    // Shift the words until the insertion site is found.
    while ( ( i > 0 ) && ( words [ i - 1 ].getWord () > word ) )
    {
      words [ i ] = words [ i - 1 ];
      i--; 
    }  // while

    CommonDnaWord common_word = new CommonDnaWord ( word
                                            , amino_word.getDnaWord ()
                                            , amino_word.getWordSize ()
                                            , seq_count 
                                            );
    words [ i ] = common_word;

    return common_word;
  }  // method addWord


/******************************************************************************/
  public CommonDnaWord [] getSortedCommonDnaWords ()
  {
    CommonDnaWord [] sorted = new CommonDnaWord [ total ];

    int sorted_total = 0;

    // Scan all of the words for common words.
    for ( int i = 0; i < total; i++ )
    {
      // Insert the common word.
      insert ( sorted, words [ i ], sorted_total );

      sorted_total++;
    }  // if
/*
    System.out.println ( "Sorted Common Words" );
    for ( int i = 0; i < sorted_total; i++ )
      System.out.println ( sorted [ i ].toString () );
    System.out.println ();
*/
    return joinAdjacentWords ( validateWords ( sorted, sorted_total ) );
  }  // method getSortedCommonDnaWords


/******************************************************************************/
  private boolean isLower ( CommonDnaWord word, CommonDnaWord current )
  {
    // Compare the positions of the two common words.
    int [] pos_word = word.getPositions ();
    int [] pos_curr = current.getPositions ();

    // Sort on only the first sequence.
    for ( int i = 0; ( i < pos_word.length ) && ( i < pos_curr.length ); i++ )

      if ( ( pos_word [ i ] > pos_curr [ i ] ) &&
           ( pos_curr [ i ] > 0 ) )

        return false;

    return true;
  }  // method isLower


/******************************************************************************/
  private boolean isFirstLower ( CommonDnaWord word, CommonDnaWord current )
  {
    // Compare the positions of the two common words.
    int [] pos_word = word.getPositions ();
    int [] pos_curr = current.getPositions ();

    // Sort on only the first sequence.
    if ( ( pos_word [ 0 ] > pos_curr [ 0 ] ) &&
         ( pos_curr [ 0 ] > 0 ) )

        return false;

    return true;
  }  // method isFirstLower


/******************************************************************************/
  // Insertion sort.
  private void insert ( CommonDnaWord [] sorted, CommonDnaWord word, int sorted_total )
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
            ( isFirstLower ( word, sorted [ i - 1 ] ) == true ) )
    {
      sorted [ i ] = sorted [ i - 1 ];		// shift down the array
      i--;					// new insertion position
    }  // while

    // insert.
    sorted [ i ] = word;
  }  // method insert


/******************************************************************************/
  // This method advances the words lists that matched the current word.
  private void advanceWord ( DnaAlignee [] alignees, int current_word )
  {
    // Check all of the lists for a match to the current word.
    for ( int i = 0; i < alignees.length; i++ )

      // Check for a match to the current word.
      if ( alignees [ i ].getCurrentWordValue () == current_word )

        // Advance to the next word in the list
        alignees [ i ].advanceNext ();
  }  // method advanceWord


/******************************************************************************/
  private int findNextWord ( DnaAlignee [] alignees )
  {
    int current_word = alignees [ 0 ].getCurrentWordValue ();
    int next_value;

    for ( int i = 1; i < alignees.length; i++ )
    {
      next_value = alignees [ i ].getCurrentWordValue ();

      // Check for a smaller word value.
      if ( ( next_value < current_word ) && ( next_value >= 0 ) )

        current_word = next_value;
    }  // for

    return current_word;
  }  // method findNextWord


/******************************************************************************/
  private void checkWordCount ( DnaAlignee [] alignees, int current_word )
  {
    // Count the number of occurances of this word.
    int count = 0;
    int match = 0;
    for ( int i = 0; i < alignees.length; i++ )

      // Check for a match.
      if ( alignees [ i ].getCurrentWordValue () == current_word )
      {
        count++;
        match = i;
      }  // if

    if ( count <= 1 )  return;		// no need to save this word

    // Drop all words short of the full set.
    if ( count < alignees.length )  return;

    // Save the word if common.
    DnaWord match_word = alignees [ match ].getCurrentWord ();

    // Create a new common word.
    CommonDnaWord common_word = addWord ( alignees [ match ].getCurrentWord ()
                                     , alignees.length 
                                     );
    common_word.setCount ( (short) count );

    // Calculate the offsets to right justify the sequences.
    short max_length = 0;
    short seq_length = 0;
    for ( int i = 0; i < alignees.length; i++ )
    {
      seq_length = (short) ( alignees [ i ].getSequence ().length () );
      if ( seq_length > max_length )  max_length = seq_length;
    }  // for

    short offset [] = new short [ alignees.length ];
    for ( int i = 0; i < alignees.length; i++ )
      offset [ i ] = (short) ( max_length - alignees [ i ].getSequence ().length () );

    // Add the positions of the other matches.
    for ( int i = 0; i < alignees.length; i++ )
    {
      // Check for a match.
      if ( alignees [ i ].getCurrentWordValue () == current_word )
      {
        // Count each match and save maximum position.
        common_word.setPosition ( alignees [ i ].getCurrentWordPosition (), i );
      }  // if
      else

        common_word.setPosition ( (short) -1, i );
    }  // for
  }  // method checkWordCount


/******************************************************************************/
  public void mergeWordLists ( DnaAlignee [] alignees )
  {
    // Determine the largest size.
    int max_size = 0;
    for ( int i = 0; i < alignees.length; i++ )
      if ( alignees [ i ].getTotal () > max_size )  
        max_size = alignees [ i ].getTotal ();
    setSize ( max_size * 4 );

    // Determine the common words between the sorted lists.
    int current_word = 0;
    while ( current_word >= 0 )
    {
      // Find the next word.
      current_word = findNextWord ( alignees );

      // Check if at the end of the lists.
      if ( current_word != -1 )
      {
        // Check if the current word is common.
        checkWordCount ( alignees, current_word );

        // Advance to the next word in the lists.
        advanceWord ( alignees, current_word );
      }  // if
    }  // while
  }  // method mergeWordLists


/******************************************************************************/
  private boolean isAdjacentWords ( CommonDnaWord word1, CommonDnaWord word2 )
  {
    // Compare the positions of the two common words.
    int [] pos1 = word1.getPositions ();
    int [] pos2 = word2.getPositions ();

    // Sort on only the first sequence.
    for ( int i = 0; i < pos1.length ; i++ )

      if ( pos1 [ i ] + 1 != pos2 [ i ] )  return false;

    return true;
  }  // method isAdjacentWords


/******************************************************************************/
  private CommonDnaWord [] joinAdjacentWords ( CommonDnaWord [] words )
  {
    // Check for too few words.
    if ( words.length < 2 )  return words;

    // Merge adjacent words.
    int dropped = 0;
    CommonDnaWord tail_word = words [ words.length - 1 ]; 
    for ( int i = words.length - 2; i >= 0; i-- )
    {
      // Compare to the next position.
      if ( isAdjacentWords ( words [ i ], tail_word ) == true )
      {
        tail_word.grow5Prime ();
        words [ i ].initialize ();
        words [ i ] = null;
        dropped++;
      }  // if
      else
        tail_word = words [ i ];
    }  // for

    // Copy the valid words.
    CommonDnaWord [] good_sorted = new CommonDnaWord [ words.length - dropped ];
    int next_index = 0; 
    for ( int i = 0; i < words.length; i++ )
      if ( words [ i ] != null )
      {
        good_sorted [ next_index ] = words [ i ];
        next_index++;
      }  // if

    System.out.println ( "Merged Sorted Common Words" );
    for ( int i = 0; i < good_sorted.length; i++ )
      System.out.println ( good_sorted [ i ].toString () );
    System.out.println ();

    return good_sorted;
  }  // method joinAdjacentWords


/******************************************************************************/
  private CommonDnaWord [] validateWords ( CommonDnaWord [] sorted, int sorted_total )
  {
    // Validate sorted.
    if ( ( sorted == null ) || ( sorted_total <= 0 ) )
    {
      System.out.println ( "validateWords: null sorted or zero sorted_total = " + sorted_total );
      return sorted;
    }  // if

    if ( sorted.length <= 0 )
    {
      System.out.println ( "validateWords: sorted.length = 0" );
      return sorted;
    }  // if


    // Check that the positions progress.
    int valid_total = 1;		// assume the last word is valid
    for ( int i = 0; i < sorted_total - 1; i++ )
    {
      // Compare to the next position.
      if ( isLower ( sorted [ i ], sorted [ i + 1 ] ) == false )
      {
        sorted [ i ].initialize ();
        sorted [ i ] = null;
      }  // if
      else
        valid_total++;
    }  // for

    // Copy the valid words.
    CommonDnaWord [] good_sorted = new CommonDnaWord [ valid_total ];
    int next_index = 0; 
    for ( int i = 0; i < sorted_total; i++ )
      if ( sorted [ i ] != null )
      {
        good_sorted [ next_index ] = sorted [ i ];
        next_index++;
      }  // if
/*
    System.out.println ( "Validated Sorted Common Words" );
    for ( int i = 0; i < valid_total; i++ )
      System.out.println ( good_sorted [ i ].toString () );
    System.out.println ();
*/
    return good_sorted;
  }  // method validateWords


/******************************************************************************/
  public void snap ()
  {
    System.out.println ( "total = " + total + ", size = " + size );
    for ( int i = 0; i < total; i++ )
      System.out.println ( (i+1) + "\t" + words [ i ].toString () );
  }  // method snap


/******************************************************************************/

}  // class CommonDnaWords
