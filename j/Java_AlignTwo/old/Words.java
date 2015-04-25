
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

public class Words extends Object
{


/******************************************************************************/

  private   String  peptides [] = null;		// word peptides

  private   short  positions [] = null;		// word positions

  private   int  words [] = null;		// array of words

  private   int  size = 0;			// expected array size

  private   int  total = 0;			// number of current words


/******************************************************************************/
  // Constructor Words
  public Words ()
  {
    initialize ();
  }  // constructor Words


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    peptides = null;
    positions = null;
    words = null;
    size = 0;
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public String [] getPeptides ()
  {
    return peptides;
  }  // method getPeptides


/******************************************************************************/
  public short [] getPositions ()
  {
    return positions;
  }  // method getPositions


/******************************************************************************/
  public int [] getWords ()
  {
    return words;
  }  // method getWords


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
  public void setWords ( int [] value )
  {
    words = value;
  }  // method setWords


/******************************************************************************/
  public void setSize ( int value )
  {
    size = value;

    words = new int [ size ];
    peptides = new String [ size ];
    total = 0;
  }  // method setSize


/******************************************************************************/
  public void addWord ( int word )
  {
    addWord ( word, "", 0 );
  }  // method addWord


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
    while ( ( i > 0 ) && ( words [ i - 1 ] > word ) )
    {
      words [ i ] = words [ i - 1 ];
      peptides [ i ] = peptides [ i - 1 ];
      positions [ i ] = positions [ i - 1 ];
      i--; 
    }  // while

    words [ i ] = word;
    peptides [ i ] = peptide;
    positions [ i ] = (short) pos;
  }  // method addWord


/******************************************************************************/
  public int countCommonWords ( int words2 [] )
  {
    int i1 = 0;			// index 1
    int i2 = 0;			// index 2

    int count = 0;		// number of common words
    while ( ( i1 < total ) && ( i2 < words2.length ) )
    {
      if ( words [ i1 ] < words2 [ i2 ] )
        i1++;
      else
        if ( words [ i1 ] > words2 [ i2 ] )
          i2++;
        else  // words [ i1 ] == words2 [ i2 ]
        {
          // System.out.println ( "\t" + peptides [ i1 ] );
          count++;
          i1++;
          i2++;
        }  // else
    }  // while

    return count;
  }  // method countCommonWords


/******************************************************************************/
  public void snap ()
  {
    System.out.println ( "total = " + total + ", size = " + size );
    for ( int i = 0; i < total; i++ )
      System.out.println ( (i+1) + "\t" + words [ i ] );
  }  // method snap


/******************************************************************************/
  public static void main ( String [] args )
  {
    Words words1 = new Words ();
    Words words2 = new Words ();
    words1.setSize ( 10 );
    words2.setSize ( 10 );
    for ( int i = 10; i >= 1; i-- )
    {
      words1.addWord ( i );
      words2.addWord ( i + 5 );
    }  // for

    words2.snap ();

    System.out.println ( "Common words = " + words1.countCommonWords ( words2.getWords () ) );
  }  // method main


/******************************************************************************/

}  // class Words
