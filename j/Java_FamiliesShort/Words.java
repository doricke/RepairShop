

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

public class Words extends Object
{


/******************************************************************************/

  private   short  words [] = null;		// array of words

  private   short  size = 0;			// expected array size

  private   short  total = 0;			// number of current words


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
    words = null;
    size = 0;
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public short [] getWords ()
  {
    return words;
  }  // method getWords


/******************************************************************************/
  public short getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  public short getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public void setWords ( short [] value )
  {
    words = value;
  }  // method setWords


/******************************************************************************/
  public void setSize ( short value )
  {
    size = value;

    words = new short [ size ];
    total = 0;
  }  // method setSize


/******************************************************************************/
  public void addWord ( short word )
  {
    addWord ( word, "" );
  }  // method addWord


/******************************************************************************/
  public void addWord ( short word, String peptide )
  {
    if ( total >= size )
    {
      System.out.println ( "Words.addWord: *Warning* too many words added (" + total + ")" );
      return;
    }  // if

    // Find where to insert the current word.
    short i = total;
    total++;

    // Shift the words until the insertion site is found.
    while ( ( i > 0 ) && ( words [ i - 1 ] > word ) )
    {
      words [ i ] = words [ i - 1 ];
      i--; 
    }  // while

    words [ i ] = word;
  }  // method addWord


/******************************************************************************/
  public short countCommonWords ( short words2 [] )
  {
    int i1 = 0;			// index 1
    int i2 = 0;			// index 2

    short count = 0;		// number of common words
    while ( ( i1 < total ) && ( i2 < words2.length ) )
    {
      if ( words [ i1 ] < words2 [ i2 ] )
        i1++;
      else
        if ( words [ i1 ] > words2 [ i2 ] )
          i2++;
        else  // words [ i1 ] == words2 [ i2 ]
        {
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
    words1.setSize ( (short) 10 );
    words2.setSize ( (short) 10 );
    for ( short i = 10; i >= 1; i-- )
    {
      words1.addWord ( i );
      words2.addWord ( (short) ( i + 5 ) );
    }  // for

    words2.snap ();

    System.out.println ( "Common words = " + words1.countCommonWords ( words2.getWords () ) );
  }  // method main


/******************************************************************************/

}  // class Words

