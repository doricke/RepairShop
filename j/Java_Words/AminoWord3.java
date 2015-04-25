
import java.util.*;

// import Amino;
// import InputTools;

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

public class AminoWord3 extends Object
{


/******************************************************************************/

  private   String  aminos = "";	// amino acids

  private   String  name = "";		// sequence name

  private   int  position = 0;	// amino acid word start position

  private   byte  size = 4;		// amino acids per word

  private   int  word = 0;		// amino acids word


/******************************************************************************/
  // Constructor AminoWord3
  public AminoWord3 ()
  {
    initialize ();
  }  // constructor AminoWord3


/******************************************************************************/
  // Constructor AminoWord3
  public AminoWord3 ( String peptide, String seq_name, int pos )
  {
    initialize ();

    aminos = peptide;
    name = seq_name;
    position = pos;
    size = (byte) peptide.length ();
  }  // constructor AminoWord3


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    aminos = "";
    name = "";
    position = 0;
    size = 4;
    word = 0;
  }  // method initialize 


/******************************************************************************/
  public int getWord ()
  {
    return word;
  }  // method getWord


/******************************************************************************/
  public String getAminos ()
  {
    return aminos;
  }  // method getAminos


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public byte getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  // This method checks for common amino acids in a word.
  public boolean isGoodWord ()
  {
    // Check for three identical amino acids.
    for ( int i = 0; i < size - 2; i++ )

      for ( int j = i + 1; j < size - 1; j++ )

        for ( int k = j + 1; k < size; k++ )

          if ( ( aminos.charAt ( i ) == aminos.charAt ( j ) ) &&
               ( aminos.charAt ( j ) == aminos.charAt ( k ) ) )

            return false;

    // Check for non-standard amino acids (B, X, Z).
    char c;
    for ( int i = 0; i < aminos.length (); i++ )
    {
      c = aminos.charAt ( i );
      if ( ( c == 'X' ) || ( c == 'x' ) ||
           ( c == 'B' ) || ( c == 'b' ) ||
           ( c == 'Z' ) || ( c == 'z' ) )  return false; 
    }  // for

    // Check for two pairs of identical amino acids.
    if ( aminos.length () >= 4 )
    {
      if ( ( ( aminos.charAt ( 0 ) == aminos.charAt ( 1 ) ) &&
             ( aminos.charAt ( 2 ) == aminos.charAt ( 3 ) ) ) ||
           ( ( aminos.charAt ( 0 ) == aminos.charAt ( 2 ) ) &&
             ( aminos.charAt ( 1 ) == aminos.charAt ( 3 ) ) ) ||
           ( ( aminos.charAt ( 0 ) == aminos.charAt ( 3 ) ) &&
             ( aminos.charAt ( 1 ) == aminos.charAt ( 2 ) ) ) )

        return false;
    }  // if

    return true;
  }  // method isGoodWord


/******************************************************************************/
  public void setAminos ( String value )
  {
    aminos = value;

    computeWord ();
  }  // method setAminos


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setSize ( byte value )
  {
    size = value;
  }  // method setSize


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
  public void parseLine ( String line )
  {
    StringTokenizer tokens = new StringTokenizer ( line, "\t" );

    try
    {
      setAminos ( tokens.nextToken () );
      setName ( tokens.nextToken () );
      position = InputTools.getInteger ( tokens.nextToken () );
      size = (byte) aminos.length ();
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "parseLine: NoSuchElementException: " + e );
    }  // catch
  }  // method parseLine


/******************************************************************************/
  public String toString ()
  {
    return aminos + "\t" + name + "\t" + position;
  }  // method toString


/******************************************************************************/
  public static void main ( String [] args )
  {
    AminoWord3 amino_word = new AminoWord3 ();
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

}  // class AminoWord3
