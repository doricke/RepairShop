
import java.util.*;

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

public class Slide extends Object
{


/******************************************************************************/

  final static int MAX_SIZE = 10000;


/******************************************************************************/

  // alignment for first sequence
  private   StringBuffer alignment1 = new StringBuffer ( MAX_SIZE );

  // alignment for second sequence
  private   StringBuffer alignment2 = new StringBuffer ( MAX_SIZE );

  private   String  sequence1 = "";		// first sequence to align

  private   String  sequence2 = "";		// second sequence to align


/******************************************************************************/
  // Constructor Slide
  public Slide ()
  {
    initialize ();
  }  // constructor Slide


/******************************************************************************/
  // Constructor Slide
  public Slide ( String seq1, String seq2 )
  {
    initialize ();
    setSequence1 ( seq1 );
    setSequence2 ( seq2 );
    align ();
  }  // constructor Slide


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alignment1.setLength ( 0 );
    alignment2.setLength ( 0 );
    sequence1 = "";
    sequence2 = "";
  }  // method initialize 


/******************************************************************************/
  public String getAlignment1 ()
  {
    return alignment1.toString ();
  }  // method getAlignment1


/******************************************************************************/
  public String getAlignment2 ()
  {
    return alignment2.toString ();
  }  // method getAlignment2


/******************************************************************************/
  public String getSequence1 ()
  {
    return sequence1;
  }  // method getSequence1


/******************************************************************************/
  public String getSequence2 ()
  {
    return sequence2;
  }  // method getSequence2


/******************************************************************************/
  public void setSequence1 ( String value )
  {
    sequence1 = value;
  }  // method setSequence1


/******************************************************************************/
  public void setSequence2 ( String value )
  {
    sequence2 = value;
  }  // method setSequence2


/******************************************************************************/
  // This method aligns the two sequences.
  public void align ()
  {
  }  // method align


/******************************************************************************/
  private int countWindow ( int pos1, int pos2, int window )
  {
    int count = 0;

    for ( int i = 0; i < window; i++ )

      if ( ( pos1 + i < sequence1.length () ) &&
           ( pos2 + i < sequence2.length () ) )

        if ( Msa.equals ( sequence1.charAt ( pos1 + i )
                        , sequence2.charAt ( pos2 + i ) ) == true )

          count++;

    return count;
  }  // method countWindow


/******************************************************************************/
  private int countIdent ( String seq1, String seq2 )
  {
    int count = 0;

    for ( int i = 0; i < seq1.length (); i++ )

      if ( Msa.equals ( seq1.charAt ( i ), seq2.charAt ( i ) ) == true ) 

        count++;

    return count;
  }  // method countIdent


/******************************************************************************/
  public void printAlignment ()
  {
    for ( int i = 0; i < alignment1.length (); i += 50 )
    {
      System.out.println ();

      // Print out the first sequence.
      System.out.print ( (i+1) + "\t" );
      int end = i + 50;
      if ( end > alignment1.length () )  end = alignment1.length ();
      System.out.println ( alignment1.substring ( i, end ) + "\t" + end );

      // Print out the identity markers.
      System.out.print ( "\t" );
      for ( int j = i; j < end; j++ )
        if ( j < alignment2.length () )
        {
          if ( Msa.equals ( alignment1.charAt ( j ), alignment2.charAt ( j ) ) == true )
            System.out.print ( "|" );
          else
            System.out.print ( " " );
        }  // if
      System.out.println ();

      // Print out the second sequence.
      System.out.print ( (i+1) + "\t" );
      if ( end > alignment2.length () )
        System.out.println ( alignment2.substring ( i ) + "\t" + end );
      else
        System.out.println ( alignment2.substring ( i, end ) + "\t" + end );
    }  // for
  }  // method printAlignment


/******************************************************************************/
  public static void main ( String [] args )
  {
    //                        "---AIGK-ML-TGVE-" 
    Slide app = new Slide ( "PLVAIGKRMLSTGVEQ", "AIGKMLTGVE" );
    app.printAlignment ();
  }  // main


/******************************************************************************/

}  // class Slide
