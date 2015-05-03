
// import Tokens;

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

public class TokensIterator extends InputFile
{


/******************************************************************************/

  Tokens tokens = null;			// Current String tokens 


/******************************************************************************/
public TokensIterator ()
{
  initialize ();
}  // constructor TokensIterator


/******************************************************************************/
public TokensIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method TokensIterator


/*******************************************************************************/
  public Tokens getTokens ()
  {
    return tokens;
  }  // method getTokens


/*******************************************************************************/
  public Tokens next ()
  {
    tokens = new Tokens ();		// create a new object

    // Get the next tokens file line.
    nextLine ();

    if ( line.length () <= 0 )  return null;

    // Parse the next file line.
    tokens.parseLine ( line.toString () );

    return tokens;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  TokensIterator app = new TokensIterator ();
  Tokens tokens;

  if ( args.length == 1 )  app.setFileName ( args [ 0 ] );
  else app.setFileName ( "test" );

  app.openFile ();

  while ( app.isEndOfFile () == false )
  {
    tokens = app.next ();
  }  // while
  app.closeFile ();

}  // method main


/******************************************************************************/

}  // class TokensIterator

