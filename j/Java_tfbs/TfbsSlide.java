
import java.io.*;
import java.util.Vector;

// import FloatArray;
// import InputFile;
// import Tokens;
// import TokensIterator;
// import OutputFile;

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

public class TfbsSlide extends Object
{

/******************************************************************************/
public TfbsSlide ()
{
  initialize ();
}  // constructor TfbsSlide


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void processFile ( String file_name )
{
  // Create the merged output file.
  OutputFile merge = new OutputFile ( file_name + ".slide" );
  merge.openFile ();

  SlidingWindow window = new SlidingWindow ();
  window.setSize ( 2000 );		// number of maximum probes
  window.setSpan ( 200 );		// 200 bp window size

  // Open the input file.
  TokensIterator iterators = new TokensIterator ( file_name );

  Tokens tokens = iterators.next ();
  if ( tokens != null )
  {
    // Write out the title line.
    System.out.println ( tokens.getLine () + "\tHit" );

    for ( int i = 0; i < 8; i++ )
      window.setTitle ( i, tokens.getToken ( i + 4 ) );
  }  // if

  while ( iterators.isEndOfFile () == false )
  {
    tokens = iterators.next ();

    if ( tokens != null )
    {
      FloatArray float_array = new FloatArray ();
      float_array.setSize ( 10 );
      float_array.setValue ( 0, LineTools.getFloat ( tokens.getToken ( 0 ) ) );  // position
      float_array.setValue ( 1, LineTools.getFloat ( tokens.getToken ( 2 ) ) );  // average

      for ( int i = 1; i <= 8; i++ )

        float_array.setValue ( i + 1, LineTools.getFloat ( tokens.getToken ( i + 3 ) ) );

      window.addProbe ( float_array, tokens.getLine () );
    }  // if

  }  // while

  // Close the files.
  iterators.closeFile ();
  merge.closeFile ();
}  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The program scans merged experiments with a sliding window." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TfbsSlide <filename>" );
    System.out.println ();
    System.out.print   ( "where <filename> is the file name of a merged " );
    System.out.println ( "Affy. DNA tiling file" );
    System.out.println ( "of sorted normalized averages." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TfbsSlide app = new TfbsSlide ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

}  // class TfbsSlide

