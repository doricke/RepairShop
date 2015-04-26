
// import Amino;

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

public class AminoWord extends Object
{


/******************************************************************************/

  private   int  word = 0;		// amino acids word

  private   String  aminos = "";	// amino acids

  private   byte  size = 4;		// amino acids per word


/******************************************************************************/
  // Constructor AminoWord
  public AminoWord ()
  {
    initialize ();
  }  // constructor AminoWord


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    word = 0;
    aminos = "";
    size = 4;
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
  public byte getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  // This method checks for 3 common amino acids in a word.
  public boolean isGoodWord ( String word )
  {
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
  public String toString ()
  {
    return aminos + "\t" + word;
  }  // method toString


/******************************************************************************/
  public static void main ( String [] args )
  {
    AminoWord amino_word = new AminoWord ();
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

}  // class AminoWord
