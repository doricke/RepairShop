
// import AminoWord2;

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

public class Alignee extends Object
{


/******************************************************************************/

  private   int  next_index = 0;		// index of next word

  private   String  sequence = "";		// sequence

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words

  private   byte  word_size = 4;		// amino word size

  private   AminoWord2  words [] = null;	// array of words


/******************************************************************************/
  // Constructor Alignee
  public Alignee ()
  {
    initialize ();
  }  // constructor Alignee


/******************************************************************************/
  // Constructor Alignee
  public Alignee ( String sequence, byte w_size )
  {
    initialize ();
    word_size = w_size;

    // Set up the amino acids words array.
    createWords ( sequence );
  }  // constructor Alignee


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    next_index = 0;
    sequence = "";
    size = 0;
    total = 0;
    word_size = 4;
    words = null;
  }  // method initialize 


/******************************************************************************/
  public void advanceNext ()
  {
    next_index++;
  }  // method getNext


/******************************************************************************/
  public AminoWord2 getCurrentWord ()
  {
    if ( next_index >= total )  return null;

    return words [ next_index ];
  }  // method getCurrentWord


/******************************************************************************/
  public short getCurrentWordPosition ()
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
  public AminoWord2 [] getWords ()
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
  public void setSize ( int value )
  {
    size = value;

    words = new AminoWord2 [ size ];
    total = 0;
  }  // method setSize


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  // Sort by hash word value.
  public void addWord ( AminoWord2 amino_word )
  {
    // Check for more space in the array.
    if ( total >= size )
    {
      System.out.println ( "Alignee.addWord: *Warning* too many words added (" + total + ")" );
      return;
    }  // if

    words [ total ] = amino_word;
    total++;
  }  // method addWord


/******************************************************************************/
  // Sort by hash word value.
  public void addWord ( int word, String peptide, int pos )
  {
    // System.out.println ( "Alignee.addWord: peptide = " + peptide + " at " + pos );

    if ( total >= size )
    {
      System.out.println ( "Alignee.addWord: *Warning* too many words added (" + total + ")" );
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

    AminoWord2 amino_word = new AminoWord2 ( word, peptide, pos, word_size );
    words [ i ] = amino_word;
  }  // method addWord


/******************************************************************************/
  public void createWords ( String seq )
  {
    sequence = seq;

    // Validate the sequence length.
    int length = seq.length ();
    if ( length <= word_size )  return;

    // Compute the number of amino acid words in this sequence.
    setSize ( length - word_size + 1 );

    // Amino acid hash word.
    AminoWord2 amino_word = new AminoWord2 ();
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
  public void snap ()
  {
    System.out.println ( "total = " + total + ", size = " + size );
    for ( int i = 0; i < total; i++ )
      System.out.println ( (i+1) + "\t" + words [ i ].toString () );
  }  // method snap


/******************************************************************************/
  public static void main ( String [] args )
  {
  }  // method main


/******************************************************************************/

}  // class Alignee
