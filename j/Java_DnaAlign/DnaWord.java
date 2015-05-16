
// import Base;
// import SeqTools;

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

public class DnaWord extends Object
{


/******************************************************************************/

  private   String  dna_word = "";		// DNA sequence word

  private   boolean  good_word = true;		// good word flag

  private   int  position = 0;			// word start position

  private   boolean  skip_base_3 = false;	// flag for skipping third base (bbn)m

  private   byte  word_size = 6;		// DNA bases per word

  private   int  word = 0;			// hashed DNA word


/******************************************************************************/
  // Constructor DnaWord
  public DnaWord ()
  {
    initialize ();
  }  // constructor DnaWord


/******************************************************************************/
  // Constructor DnaWord
  public DnaWord 
      ( int word_value
      , String word_dna
      , int pos
      , byte size 
      , boolean skip_third_base
      )
  {
    initialize ();

    dna_word = word_dna;
    position = pos;
    skip_base_3 = skip_third_base;
    word_size = size;
    word = word_value;
  }  // constructor DnaWord


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    dna_word = "";
    good_word = true;
    position = 0;
    skip_base_3 = false;
    word_size = 6;
    word = 0;
  }  // method initialize 


/******************************************************************************/
  public int getWord ()
  {
    return word;
  }  // method getWord


/******************************************************************************/
  public String getDnaWord ()
  {
    return dna_word;
  }  // method getDnaWord


/******************************************************************************/
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public boolean getSkipBase3 ()
  {
    return skip_base_3;
  }  // method getSkipBase3


/******************************************************************************/
  public byte getWordSize ()
  {
    return word_size;
  }  // method getWordSize


/******************************************************************************/
  // This method checks for a DNA only word (and not low complexity).
  public boolean isGoodWord ()
  {
    // Check for Low DNA complexity.
    if ( SeqTools.isLowDnaComplexity ( dna_word ) == true )  good_word = false;

    return good_word;
  }  // method isGoodWord


/******************************************************************************/
  // This method checks for too many common base pairs in a word.
  public boolean isGoodWord ( String word )
  {
    // Check for Low DNA complexity.
    if ( SeqTools.isLowDnaComplexity ( word ) == true )  good_word = false;

    return good_word;
  }  // method isGoodWord


/******************************************************************************/
  public boolean isSkipBase3 ()
  {
    return skip_base_3;
  }  // method isSkipBase3


/******************************************************************************/
  public void setDnaWord ( String value )
  {
    dna_word = value;
    good_word = true;

    computeWord ();
  }  // method setDnaWord


/******************************************************************************/
  public void setGoodWord ( boolean value )
  {
    good_word = value;
  }  // method setGoodWord


/******************************************************************************/
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setSkipBase3 ( boolean value )
  {
    skip_base_3 = value;
  }  // method setSkipBase3


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  private void computeWord ()
  {
    Base base = new Base ();

    if ( dna_word.length () != word_size )
    {
      System.out.println ( "Strange dna_word length: '" + dna_word + "', len = " + dna_word.length () );
    }  // if

    // Has the current DNA word into an integer word.
    word = 0;
    for ( int i = 0; i < word_size; i++ )

      if ( i + 1 <= dna_word.length () )
      {
        base.setDnaBase ( dna_word.charAt ( i ) );
        word += base.getOrdinal ();

        // Shift the word to make room for the next base.
        if ( i + 1 < word_size )
        {
          if ( skip_base_3 == true ) 
          {
            // Don't shift the last position if need to skip it.
            if ( ( i + 2 < word_size ) || ( ( ( i + 1 ) % 3 ) != 2 ) )   

              word = word << 2;
          }  // if
          else
            word = word << 2;		// shift word for next DNA base 
        }  // if

        // Check if need to skip every third base.
        if ( skip_base_3 == true )
        
          if ( ( ( i + 1 ) % 3 ) == 2 )  i++;

        // Check for a DNA base (versus a gap or IUB base)
        if ( base.isDnaBase () == false )
        {
          System.out.println ( "Bad base: '" + base.getDnaBase () + "'" );
          good_word = false;
        }  // if
      }  // for
  }  // method computeWord


/******************************************************************************/
  public String toString ()
  {
    return dna_word + "\t" + word + "\t" + position + "\t" + word_size;
  }  // method toString


/******************************************************************************/
  public static void main ( String [] args )
  {
    DnaWord dna_word = new DnaWord ();

    String [] words = { "AAAAAAA", "AYGCT", "CCCCC", "GGGGGG", "ATATATATAT",
        "TTTTTTTT", "ATGCATG", "ACGTACG", "ACGT-A.T", "AATAATAATAAT",
        "TTATTATTATTA",
        "TTTTTTTTTTTTTTT", "TTTTTTTTTTTTTTTA", "TTTTTTTTTTTTTTTT" };

    for ( int i = 0; i < words.length; i++ )
    {
      dna_word.setWordSize ( (byte) words [ i ].length () );
      dna_word.setDnaWord ( words [ i ] );
      System.out.println ( dna_word.toString () );
      if ( dna_word.isGoodWord () == false )
        System.out.println ( "\tnot a good DNA word" );
      dna_word.initialize ();
    }  // for

    // Test skipping 3rd base.
    System.out.println ();
    System.out.println ( "Test for skipping every third base:" );
    for ( int i = 0; i < words.length; i++ )
    {
      dna_word.setWordSize ( (byte) words [ i ].length () );
      dna_word.setSkipBase3 ( true );
      dna_word.setDnaWord ( words [ i ] );
      System.out.println ( dna_word.toString () );
      if ( dna_word.isGoodWord () == false )
        System.out.println ( "\tnot a good DNA word" );
      dna_word.initialize ();
    }  // for
    
  }  // method main


/******************************************************************************/

}  // class DnaWord
