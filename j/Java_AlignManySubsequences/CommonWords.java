
// import Alignee;
// import AminoWord2;
// import CommonWord;

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

public class CommonWords extends Object
{


/******************************************************************************/

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words

  private   CommonWord  words [] = null;	// array of words


/******************************************************************************/
  // Constructor CommonWords
  public CommonWords ()
  {
    initialize ();
  }  // constructor CommonWords


/******************************************************************************/
  // Constructor CommonWords
  public CommonWords ( Alignee [] alignees )
  {
    initialize ();

    // Determine the common words.
    mergeWordLists ( alignees );
  }  // constructor CommonWords


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
  public CommonWord [] getWords ()
  {
    return words;
  }  // method getWords


/******************************************************************************/
  private void setSize ( int value )
  {
    if ( ( size == 0 ) || ( total == 0 ) )
    {
      size = value;
      words = new CommonWord [ size ];
      total = 0;
      return;
    }  // if

    if ( value < size )  return;

    // Extend the array length.

    // Copy the existing words to the new array.
    size = value;
    CommonWord [] words2 = new CommonWord [ size ];
    for ( int i = 0; i < total; i++ ) 
    {
      words2 [ i ] = words [ i ];
      words [ i ] = null;
    }  // for
    words = words2;
  }  // method setSize


/******************************************************************************/
  private CommonWord addWord ( AminoWord2 amino_word, short seq_count )
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

    CommonWord common_word = new CommonWord ( word
                                            , amino_word.getAminos ()
                                            , amino_word.getSize ()
                                            , seq_count 
                                            );
    words [ i ] = common_word;

    return common_word;
  }  // method addWord


/******************************************************************************/
  // Initialize the positions seen array.
  private short [] makeSeen ( CommonWord word )
  {
    short total = (short) word.getPositions ().length;

    short [] seen = new short [ total ];

    for ( int i = 0; i < total; i++ )

      seen [ i ] = 0;

    return seen;
  }  // method makeSeen


/******************************************************************************/
  // Update the maximum positions seen thus far.
  private short [] maxSeen ( CommonWord word, short [] seen )
  {
    short [] positions = word.getPositions ();

    for ( int i = 0; i < positions.length; i++ )

      // Check for a higher position.
      if ( positions [ i ] > seen [ i ] )

        seen [ i ] = positions [ i ];

    return seen; 
  }  // method maxSeen


/******************************************************************************/
  // Update the minimum positions seen thus far.
  private short [] minSeen ( CommonWord word, short [] seen )
  {
    // Check for no word.
    if ( word == null )  return seen;

    short [] positions = word.getPositions ();

    // Check for no positions.
    if ( positions == null )  return seen;

    for ( int i = 0; i < positions.length; i++ )

      // Check for a position.
      if ( positions [ i ] > 0 )
      {
        // Check for nothing seen yet.
        if ( seen [ i ] == 0 )

          seen [ i ] = positions [ i ];

        else

          // Check for a lower position.
          if ( positions [ i ] < seen [ i ] ) 

            seen [ i ] = positions [ i ];
      }  // if

    return seen; 
  }  // method maxSeen


/******************************************************************************/
  private void positionSnap ( short [] seen, CommonWord word, String title )
  {
    System.out.print ( title + "\t[ " );
    for ( int i = 0; i < seen.length; i++ )
    {
      System.out.print ( seen [ i ] );

      if ( i < seen.length )  System.out.print ( ", " );
    }  // for

    System.out.print ( " ] X [ " );

    short [] pos = word.getPositions ();

    for ( int i = 0; i < pos.length; i++ )
    {
      System.out.print ( pos [ i ] );

      if ( i < pos.length )  System.out.print ( ", " );
    }  // for

    System.out.println ( " ]" );
  }  // method positionSnap


/******************************************************************************/
  private void computeAlignmentCounts ( CommonWord [] sorted )
  {
    // Initialize the maximum positions seen.
    short [] seen = makeSeen ( sorted [ 0 ] );

    for ( short i = (short) 1; i < sorted.length; i++ )
    {
      boolean found = false;
      short j = (short) ( i - 1 );

      // Compare to all previous words.
      while ( ( found == false ) && ( j >= 0 ) )
      {
        // Update the minimum positions seen.
        seen = minSeen ( sorted [ j ], seen );

        // Check for a word previous to this word.
        if ( isLower ( seen, sorted [ i ] ) )
        {
// positionSnap ( seen, sorted [ i ], "computeAlignmentsCounts" );

          sorted [ i ].setPreviousCount ( (short) ( sorted [ j ].getPreviousCount () + 1 ) );
          sorted [ i ].setPrevious ( j );
          found = true;
        }  // if

        j--;
      }  // while
    }  // for
  }  // method computeAlignmentCounts


/******************************************************************************/
  private CommonWord [] selectAlignment ( CommonWord [] sorted )
  {
    // Check for no alignment.
    if ( sorted.length <= 0 )  return sorted;

    int best_count = 0;
    int best_index = 0;

    // Find the best count.
    for ( int i = 0; i < sorted.length; i++ )

      if ( sorted [ i ].getPreviousCount () > best_count )
      {
        best_count = sorted [ i ].getPreviousCount ();
        best_index = i;
      }  // if

    // Allocate the alignment array.
    CommonWord [] alignment = new CommonWord [ best_count + 1 ];

    // Copy the alignment.
    int next_index = best_index;
    int i = best_count;
    while ( ( next_index >= 0 ) && ( i >= 0 ) )
    {
      alignment [ i ] = sorted [ next_index ];
      next_index = sorted [ next_index ].getPrevious ();
      i--;
    }  // while

    System.out.println ( "Select Alignment CommonWords" );
    for ( i = 0; i < alignment.length; i++ )
      System.out.println ( alignment [ i ].toString () );
    System.out.println ();

    return alignment;
  }  // method selectAlignment


/******************************************************************************/
  public CommonWord [] getSortedCommonWords ()
  {
    CommonWord [] sorted = new CommonWord [ total ];

    int sorted_total = 0;

    // Scan all of the words for common words.
    for ( int i = 0; i < total; i++ )
    {
      // Insert the common word.
      insert ( sorted, words [ i ], sorted_total );

      sorted_total++;
    }  // if

    System.out.println ( "Sorted Common Words" );
    System.out.println ( "sorted_total = " + sorted_total );

    if ( sorted_total == 0 )  return sorted;

    for ( int i = 0; i < sorted_total; i++ )
      System.out.println ( sorted [ i ].toString () );
    System.out.println ();

    // Compute the alignment counts.
    computeAlignmentCounts ( sorted );

    return selectAlignment ( sorted );

    // return validateWords ( sorted, sorted_total );
  }  // method getSortedCommonWords


/******************************************************************************/
  private boolean isLower ( short [] seen, CommonWord current )
  {
    // Compare the positions of the two common words.
    short [] pos_curr = current.getPositions ();

    for ( int i = 0; ( i < seen.length ) && ( i < pos_curr.length ); i++ )

      if ( ( seen [ i ] > pos_curr [ i ] ) &&
           ( pos_curr [ i ] > 0 ) )

        return false;

    return true;
  }  // method isLower


/******************************************************************************/
  private boolean isLower ( CommonWord word, CommonWord current )
  {
    // Compare the positions of the two common words.
    short [] pos_word = word.getPositions ();
    short [] pos_curr = current.getPositions ();

    for ( int i = 0; ( i < pos_word.length ) && ( i < pos_curr.length ); i++ )

      if ( ( pos_word [ i ] > pos_curr [ i ] ) &&
           ( pos_curr [ i ] > 0 ) )

        return false;

    return true;
  }  // method isLower


/******************************************************************************/
  // Insertion sort.
  private void insert ( CommonWord [] sorted, CommonWord word, int sorted_total )
  {
    // Check for the first record.
    if ( sorted_total == 0 )
    {
      sorted [ 0 ] = word;
      return;
    }  // if

    // Initialize the maximum positions seen.
    short [] seen = makeSeen ( sorted [ 0 ] );

    // Find the insertion position.
    int i = 0;
    int insert_position = 0;
    seen = maxSeen ( sorted [ i ], seen );
    while ( ( i < sorted_total ) && isLower ( seen, word ) )
    {
      insert_position = i;

      // Update the maximum positions seen.
      seen = maxSeen ( sorted [ i ], seen );

      i++;
    }  // while

    // Check if at end of sorted array.
    if ( isLower ( seen, word ) )  insert_position++;

System.out.println ( "insert: insert_position = " + insert_position );
positionSnap ( seen, word, "insert" );
System.out.println ();

    // Find the insertion position.
    for ( i = sorted_total; i > insert_position; i-- )

      sorted [ i ] = sorted [ i - 1 ];		// shift down the array

    // insert.
    sorted [ i ] = word;

    System.out.println ( "insert:" );
    for ( i = 0; i <= sorted_total; i++ )
      System.out.println ( sorted [ i ].toString () );
    System.out.println ();

  }  // method insert


/******************************************************************************/
  // This method advances the words lists that matched the current word.
  private void advanceWord ( Alignee [] alignees, int current_word )
  {
    // Check all of the lists for a match to the current word.
    for ( int i = 0; i < alignees.length; i++ )

      // Check for a match to the current word.
      if ( alignees [ i ].getCurrentWordValue () == current_word )

        // Advance to the next word in the list
        alignees [ i ].advanceNext ();
  }  // method advanceWord


/******************************************************************************/
  private int findNextWord ( Alignee [] alignees )
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
  private void checkWordCount ( Alignee [] alignees, int current_word )
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

    // Save the word if common.
    AminoWord2 match_word = alignees [ match ].getCurrentWord ();

    // Create a new common word.
    CommonWord common_word = addWord ( alignees [ match ].getCurrentWord ()
                                     , (short) alignees.length 
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
  public void mergeWordLists ( Alignee [] alignees )
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
  private CommonWord [] validateWords ( CommonWord [] sorted, int sorted_total )
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

    int valid_total = 0;		// assume the first word is valid

    boolean valid [] = new boolean [ sorted_total ];
    for ( int i = 0; i < sorted_total; i++ )

      valid [ i ] = true;

    int num_positions = sorted [ 0 ].getPositions ().length;
    int last_seen [] = new int [ num_positions ];
    for ( int i = 0; i < num_positions; i++ )
      
      last_seen [ i ] = 0;

    // Check that the positions progress.
    short [] next_positions = null;
    for ( int i = 0; i < sorted_total; i++ )
    {
      next_positions = sorted [ i ].getPositions ();

      // Compare all of the positions.
      for ( int j = 0; j < next_positions.length; j++ )

        // Check for a position going backwards.
        if ( ( next_positions [ j ] <= last_seen [ j ] ) &&
             ( last_seen [ j ] != 0 ) &&
             ( next_positions [ j ] != -1 ) )
        {
          // Invalidate this word.
          valid [ i ] = false;

System.out.println ( "CommonWords: j = " + j + ", next = " + next_positions [ j ] + ", last_seen = " + last_seen [ j ] );
        }  // if

      // Check for a valid next common word.
      if ( valid [ i ] == true )
      {
        // Advance to this position for the sequences.
        for ( int j = 0; j < num_positions; j++ )
          if ( next_positions [ j ] > last_seen [ j ] )
            last_seen [ j ] = next_positions [ j ];

        valid_total++;
      }  // if
    }  // for

    // Copy the valid words.
    CommonWord [] good_sorted = new CommonWord [ valid_total ];
    int next_index = 0; 
    for ( int i = 0; i < sorted_total; i++ )
      if ( valid [ i ] == true )
      {
        good_sorted [ next_index ] = sorted [ i ];
        next_index++;
      }  // if
      else
      {
        sorted [ i ].initialize ();
        sorted [ i ] = null;
      }  // else

    System.out.println ( "Validated Sorted Common Words" );
    for ( int i = 0; i < valid_total; i++ )
      System.out.println ( good_sorted [ i ].toString () );
    System.out.println ();

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

}  // class CommonWords
