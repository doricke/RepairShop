
import java.io.*;
import java.util.Vector;

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

public class TokensSuffle extends Object
{

/******************************************************************************/
public TokensSuffle ()
{
  initialize ();
}  // constructor TokensSuffle


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void processFile ( String file_name )
{
  // Open the input file.
  TokensIterator iterators = new TokensIterator ( file_name );

  Tokens tokens = null;

  while ( iterators.isEndOfFile () == false )
  {
    tokens = iterators.next ();

    if ( tokens != null )
    {
      System.out.println
          ( tokens.getToken ( 1 ) + "\t"
          + tokens.getToken ( 0 ) + "\t"
          + tokens.getToken ( 2 ) + "\t"
          + tokens.getToken ( 3 ) + "\t"
          + tokens.getToken ( 4 ) + "\t"
          + tokens.getToken ( 5 ) + "\t"
          + tokens.getToken ( 6 ) + "\t"
          + tokens.getToken ( 7 ) // + "\t"
          );

      tokens.getLine ();
    }  // if

  }  // while

  // Close the files.
  iterators.closeFile ();
}  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The program suffles file columns." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TokensSuffle <filename>" );
    System.out.println ();
    System.out.print   ( "where <filename> is the file name of a tab" );
    System.out.println ( "delimited file to suffle." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TokensSuffle app = new TokensSuffle ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

}  // class TokensSuffle

