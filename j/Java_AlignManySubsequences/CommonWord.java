
// import Amino;
// import AminoWord2;

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

public class CommonWord extends Object
{


/******************************************************************************/

  private   String  aminos = "";	// amino acids

  private   short  count = 0;		// count of this common word

  private   short [] positions = null;	// amino acid word start position

  private   short  previous = -1;	// index of previous common word

  private   short  previous_count = 0;	// count of previous ordered words

  private   byte  size = 4;		// amino acids per word

  private   short  total = 0;		// number of sequence positions

  private   int  word = 0;		// amino acids word


/******************************************************************************/
  // Constructor CommonWord
  public CommonWord ()
  {
    initialize ();
  }  // constructor CommonWord


/******************************************************************************/
  // Constructor CommonWord
  public CommonWord 
      ( int word_value
      , String peptide
      , byte word_size 
      , short total_value
      )
  {
    initialize ();

    aminos = peptide;
    count = (short) 1;
    size = word_size;
    total = total_value;
    word = word_value;

    positions = new short [ total ];
    for ( int i = 0; i < total; i++ )

      positions [ i ] = 0;
  }  // constructor CommonWord


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    aminos = "";
    count = 0;
    positions = null;
    previous = -1;
    previous_count = 0;
    size = 4;
    total = 0;
    word = 0;
  }  // method initialize 


/******************************************************************************/
  public int getWord ()
  {
    return word;
  }  // method getWord


/******************************************************************************/
  public short [] getAdjPositions ()
  {
    return positions;
  }  // method getAdjPosition


/******************************************************************************/
  public String getAminos ()
  {
    return aminos;
  }  // method getAminos


/******************************************************************************/
  public short getCount ()
  {
    return count;
  }  // method getCount


/******************************************************************************/
  public short getPosition ( int i )
  {
    if ( ( i >= 0 ) && ( i < total ) )  return positions [ i ];
    return 0;
  }  // method getPosition


/******************************************************************************/
  public short [] getPositions ()
  {
    return positions;
  }  // method getPosition


/******************************************************************************/
  public short getPrevious ()
  {
    return previous;
  }  // method getPrevious


/******************************************************************************/
  public short getPreviousCount ()
  {
    return previous_count;
  }  // method getPreviousCount


/******************************************************************************/
  public byte getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  // This method checks for 3 common amino acids in a word.
  public boolean isGoodWord ( String word )
  {
    // Check for shorter word sizes.
    if ( size == 2 )
    {
      if ( word.charAt ( 0 ) == word.charAt ( 1 ) )  return false;
    }  // if

    if ( size < 3 )  return true;

    for ( int i = 0; i < size - 2; i++ )

      for ( int j = i + 1; j < size - 1; j++ )

        for ( int k = j + 1; k < size; k++ )

          if ( ( word.charAt ( i ) == word.charAt ( j ) ) &&
               ( word.charAt ( j ) == word.charAt ( k ) ) )

            return false;

    return true;
  }  // method isGoodWord


/******************************************************************************/
  public void setAminos ( String value )
  {
    aminos = value;

    computeWord ();
  }  // method setAminos


/******************************************************************************/
  public void setCount ( short value )
  {
    count = value;
  }  // method setCount


/******************************************************************************/
  public void setPosition ( short value, int index )
  { 
    if ( ( index >= 0 ) && ( index < total ) )

      positions [ index ] = value;
  }  // method setPosition


/******************************************************************************/
  public void setPrevious ( short value )
  {
    previous = value;
  }  // method setPrevious


/******************************************************************************/
  public void setPreviousCount ( short value )
  {
    previous_count = value;
  }  // method setPreviousCount


/******************************************************************************/
  public void setSize ( byte value )
  {
    size = value;
  }  // method setSize


/******************************************************************************/
  public void addAminoWord ( AminoWord2 amino_word )
  {
    // Check if this common word is blank.
    if ( count == 0 )
    {
      aminos = amino_word.getAminos ();
      size = amino_word.getSize ();
      word = amino_word.getWord ();
    }  // if
  }  // method addAminoWord


/******************************************************************************/
  private void computeWord ()
  {
    Amino amino = new Amino ();

    if ( aminos.length () != size )
    {
      System.out.println ( "Strange aminos length: '" + aminos + "', len = " + aminos.length () );
    }  // if

    // Has the current amino acids into an integer word.
    word = 0;
    for ( int i = 0; i < size; i++ )

      if ( i + 1 <= aminos.length () )
      {
        amino.setAminoAcid ( aminos.charAt ( i ) );
        word += amino.getOrdinal ();
        if ( i + 1 < size )  word *= 32;		// shift word for next amino acid
      }  // for
  }  // method computeWord


/******************************************************************************/
  public String toString ()
  {
    StringBuffer str = new StringBuffer ( 240 );
    str.append ( aminos + "\t" + word + "\tcount = " + count + "\t" );

    str.append ( "[" );
    if ( positions != null )

      for ( int i = 0; i < positions.length; i++ )
      {
        str.append ( positions [ i ] );

        if ( i + 1 < positions.length )

          str.append ( ", " );
      }  // for

    str.append ( "]" );

    str.append ( "\tprev. " + previous + "\tp.c. " + previous_count );

    return str.toString ();
  }  // method toString


/******************************************************************************/
  public static void main ( String [] args )
  {
    CommonWord amino_word = new CommonWord ();
    amino_word.setSize ( (byte) 3 );
    amino_word.setAminos ( "AAA" );
    System.out.println ( amino_word.toString () );
    amino_word.setAminos ( "ACD" );
    System.out.println ( amino_word.toString () );
    amino_word.setAminos ( "ILM" );
    System.out.println ( amino_word.toString () );
    amino_word.setAminos ( "k.z" );
    System.out.println ( amino_word.toString () );
  }  // method main


/******************************************************************************/

}  // class CommonWord
