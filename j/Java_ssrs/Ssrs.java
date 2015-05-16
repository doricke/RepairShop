
import java.io.*;
import java.util.StringTokenizer;

import InputTools;

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

public class Ssrs extends Object
{


/******************************************************************************/

  private Ssr mono [] = new Ssr [ 4 ];

  private Ssr di [] = new Ssr [ 16 ];

  private Ssr tri [] = new Ssr [ 64 ];

  private Ssr quad [] = new Ssr [ 256 ];


/******************************************************************************/
public Ssrs ()
{
  initialize ();
}  // constructor Ssrs


/******************************************************************************/
public void initialize ()
{
  int i;

  // Allocate mono-nucleotide SSR objects.
  for ( i = 0; i < 4; i++ )

    mono [ i ] = new Ssr ( i );

  // Allocate di-nucleotide SSR objects.
  for ( i = 0; i < 16; i++ )

    di [ i ] = new Ssr ( i );

  // Allocate tri-nucleotide SSR objects.
  for ( i = 0; i < 64; i++ )

    tri [ i ] = new Ssr ( i );

  // Allocate quad-nucleotide SSR objects.
  for ( i = 0; i < 256; i++ )

    quad [ i ] = new Ssr ( i );
}  // method initialize


/******************************************************************************/
  private int computeIndex ( String pattern )
  {
    int index = 0;

    // Traverse the pattern.
    int i = 0;

    while ( i < pattern.length () )
    {
      index *= 4;
      if ( pattern.charAt ( i ) == 'A' )  index += 0;
      else if ( pattern.charAt ( i ) == 'C' )  index += 1;
      else if ( pattern.charAt ( i ) == 'G' )  index += 2;
      else if ( pattern.charAt ( i ) == 'T' )  index += 3;
      i++;
    }  // while

System.out.println ( "pattern = " + pattern + ", index = " + index );

    return index;
  }  // method computeIndex


/******************************************************************************/
  private void addSsr ( String line )
  {
    StringTokenizer tokens = new StringTokenizer ( line );

    if ( tokens.countTokens () < 3 )
    {
      System.out.println ( "*Warning* only " 
          + tokens.countTokens () 
          + " tokens on input line: " + line );
      return;
    }  // if

    int count = InputTools.getInteger ( tokens.nextToken () );

    String pattern = tokens.nextToken ();

    int units = InputTools.getInteger ( tokens.nextToken () );

    System.out.println ( line + " count = " + count + "'" + pattern + "', units = " + units );

    // Check for mono-nucleotide SSR repeat.
    if ( pattern.length () == 1 )
    {
      mono [ computeIndex ( pattern ) ].add ( count, pattern, units );
      return;
    }  // if

    // Check for di-nucleotide SSR repeat.
    if ( pattern.length () == 2 )
    {
      di [ computeIndex ( pattern ) ].add ( count, pattern, units );
      return;
    }  // if

    // Check for tri-nucleotide SSR repeat.
    if ( pattern.length () == 3 )
    {
      tri [ computeIndex ( pattern ) ].add ( count, pattern, units );
      return;
    }  // if

    // Check for quad-nucleotide SSR repeat.
    if ( pattern.length () == 4 )
    {
      quad [ computeIndex ( pattern ) ].add ( count, pattern, units );
      return;
    }  // if
  }  // method addSsr


/******************************************************************************/
  private void readSsrs ( String filename )
  {
    InputTools ssrs = new InputTools ();

    // Set the file name.
    ssrs.setFileName ( filename );

    // Open the file.
    ssrs.openFile ();

    // Read in the SSRs.
    while ( ssrs.isEndOfFile () == false )
    {
      String line = ssrs.getLine ().toString ();

      if ( line.length () > 0 )

        // Add the SSR count to the arrays.
        addSsr ( line );
    }  // while

    // Clse the file.
    ssrs.closeFile ();

    printMatrix ( mono );
    printMatrix ( di );
    printMatrix ( tri );
    printMatrix ( quad );
  }  // method readSsrs


/******************************************************************************/
private void printMatrix ( Ssr[] matrix )
{
  System.out.print ( "Length\t" );

  // Compute the maximum length;
  int max_unit = 0;
  for ( int i = 0; i < matrix.length; i++ )
  {
    int largest = matrix [ i ].getMaxLength ();

    if ( largest > max_unit )  max_unit = largest;

    // Print out the column names.
    System.out.print ( matrix [ i ].getName () + "\t" );
  }  // for
  System.out.println ();


  // System.out.println ( "Maximum length for matrix[" + matrix.length + "]" + max_unit );

  // Traverse the matrix.
  for ( int unit = 0; unit <= max_unit; unit++ )
  {
    System.out.print ( unit * ( matrix [ 0 ].getName ().length () ) + "\t" );

    for ( int i = 0; i < matrix.length; i++ )

      System.out.print ( matrix [ i ].getCount ( unit ) + "\t" );

    System.out.println ();
  }  // for

  System.out.println ();
}  // method printMatrix


/******************************************************************************/
  public static void main ( String [] args )
  {
    Ssrs app = new Ssrs ();

    app.readSsrs ( "v8.ssrs" );
  }  // method main

}  // class Ssrs

