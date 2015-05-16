
// import DnaWord;

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

public class DnaAlignee extends Object
{


/******************************************************************************/

  private   int  next_index = 0;		// index of next word

  private   String  sequence = "";		// sequence

  private   boolean  skip_third_base = false;	// skip third base flag

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words

  private   byte  word_size = 6;		// word size

  private   DnaWord  words [] = null;		// array of words


/******************************************************************************/
  // Constructor DnaAlignee
  public DnaAlignee ()
  {
    initialize ();
  }  // constructor DnaAlignee


/******************************************************************************/
  // Constructor DnaAlignee
  public DnaAlignee ( String sequence, byte w_size )
  {
    initialize ();
    word_size = w_size;

    // Set up the words array.
    createWords ( sequence );

    // snap ();
  }  // constructor DnaAlignee


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    next_index = 0;
    sequence = "";
    skip_third_base = false;
    size = 0;
    total = 0;
    word_size = 6;
    words = null;
  }  // method initialize 


/******************************************************************************/
  public void advanceNext ()
  {
    next_index++;
  }  // method getNext


/******************************************************************************/
  public DnaWord getCurrentWord ()
  {
    if ( next_index >= total )  return null;

    return words [ next_index ];
  }  // method getCurrentWord


/******************************************************************************/
  public int getCurrentWordPosition ()
  {
    if ( next_index >= total )  return 0;

    return words [ next_index ].getPosition ();
  }  // method getCurrentWordPosition


/******************************************************************************/
  public int getCurrentWordValue ()
  {
    if ( next_index >= total )  return -1;

    return words [ next_index ].getWord ();
  }  // method getCurrentWord


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


/******************************************************************************/
  public boolean getSkipThirdBase ()
  {
    return skip_third_base;
  }  // method getSkipThirdBase


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
  public DnaWord [] getWords ()
  {
    return words;
  }  // method getWords


/******************************************************************************/
  public byte getWordSize ()
  {
    return word_size;
  }  // method getWordSize


/******************************************************************************/
  public void resetNextIndex ()
  {
    next_index = 0;
  }  // method resetNextIndex


/******************************************************************************/
  public void setSkipThirdBase ( boolean value )
  {
    skip_third_base = value;
  }  // method setSkipThirdBase


/******************************************************************************/
  public void setSize ( int value )
  {
    size = value;

    words = new DnaWord [ size ];
    total = 0;
  }  // method setSize


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  // Sort by hash word value.
  public void addWord ( DnaWord dna_word )
  {
    // Check for more space in the array.
    if ( total >= size )
    {
      System.out.println ( "DnaAlignee.addWord: *Warning* too many words added (" + total + ")" );
      return;
    }  // if

    words [ total ] = dna_word;
    total++;
  }  // method addWord


/******************************************************************************/
  // Sort by hash word value.
  public void addWord ( int word, String dna_bases, int pos )
  {
    // System.out.println ( "DnaAlignee.addWord: dna_bases = " + dna_bases + " at " + pos );

    if ( total >= size )
    {
      System.out.println ( "DnaAlignee.addWord: *Warning* too many words added (" + total + ")" );
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

    DnaWord dna_word = new DnaWord ( word, dna_bases, pos, word_size, skip_third_base );
    words [ i ] = dna_word;
  }  // method addWord


/******************************************************************************/
  public void createWords ( String seq )
  {
    sequence = seq;

    // Validate the sequence length.
    int length = seq.length ();
    if ( length <= word_size )  return;

    // Compute the number of words in this sequence.
    setSize ( length - word_size + 1 );

    // Amino acid hash word.
    DnaWord dna_word = new DnaWord ();
    dna_word.setWordSize ( word_size );

    // Hash the sequence.
    String word;
    for ( int i = 0; i < length - word_size + 1; i++ )
    {
      if ( i + word_size < length )
        word = seq.substring ( i, i + word_size );
      else
        word = seq.substring ( i );

      // Check for a good word.
      dna_word.setGoodWord ( true );
      // if ( dna_word.isGoodWord ( word ) == true )
      {
        dna_word.setDnaWord ( word );
        addWord ( dna_word.getWord (), word, i + 1 );
      }  // if

      dna_word.setGoodWord ( true );
    }  // for
  }  // method createWords


/******************************************************************************/
  public void snap ()
  {
    System.out.println ( "total = " + total + ", size = " + size );
    for ( int i = 0; i < total; i++ )
      System.out.println ( (i+1) + "\t" + words [ i ].toString () );
  }  // method snap


/******************************************************************************/

}  // class DnaAlignee
