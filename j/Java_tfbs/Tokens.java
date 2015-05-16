
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

public class Tokens extends Object
{


/******************************************************************************/

  private   String line = "";		// current token line

  private   int size = 0;		// maximum number of tokens

  private   String [] tokens;		// String tokens

  private   int total = 0;		// current number of tokens


/******************************************************************************/
  // Constructor Tokens
  public Tokens ()
  {
    initialize ();
  }  // constructor Tokens


/******************************************************************************/
  // Constructor Tokens
  public Tokens ( int value )
  {
    initialize ();
    setSize ( value );
    initializeTokens ();
  }  // constructor Tokens


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    line = "";
    size = 0;
    total = 0;
  }  // method initialize 


/******************************************************************************/
  // Initialize token variables.
  public void initializeTokens ()
  {
    // Assert: size > 0
    if ( size <= 0 )  return;

    tokens = new String [ size ];

    for ( int i = 0; i < size; i++ )

      tokens [ i ] = "";
  }  // method initializeTokens 


/******************************************************************************/
  public String getLine ()
  {
    return line;
  }  // method getLine


/******************************************************************************/
  public int getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  public String getToken ( int index )
  {
    // Assert: index valid.
    if ( tokens == null )
    {
      System.out.println ( "*Error* Tokens.getToken: no tokens array; index: " + index );
      return "";
    }  // if
    if ( ( index < 0 ) || ( index >= size ) || ( index >= total ) )
    {
      System.out.println ( "*Error* Tokens.getToken: invalid index: " + index );
      System.out.println ( toString () );
      return "";
    }  // if

    return tokens [ index ];
  }  // method getTokens


/******************************************************************************/
  public String [] getTokens ()
  {
    return tokens;
  }  // method getTokens


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public void setLine ( String value )
  {
    line = value;
  }  // method setLine


/******************************************************************************/
  public void setSize ( int value )
  {
    initialize ();
    size = value;
    initializeTokens ();
  }  // method setSize


/******************************************************************************/
  public void setToken ( int index, String value )
  {
    // Assert: index valid.
    if ( tokens == null )
    {
      System.out.println ( "*Error* Tokens.setToken: no tokens array; index: " + index );
      return;
    }  // if
    if ( ( index < 0 ) || ( index >= size ) )
    {
      System.out.println ( "*Error* Tokens.setToken: invalid index: " + index );
      return;
    }  // if

    tokens [ index ] = value;
  }  // method setTokens


/******************************************************************************/
  public void setTokens ( String [] value )
  {
    tokens = value;
  }  // method setTokens


/******************************************************************************/
  public void setTotal ( int value )
  {
    total = value;
  }  // method setTotal


/******************************************************************************/
  public void parseLine ( String new_line )
  {
    setLine ( new_line );

    StringTokenizer str_tokens = new StringTokenizer ( new_line, "\t" );

    int count = str_tokens.countTokens ();

    total = 0;

    try
    {
      for ( int i = 0; (i < count) && (i < size); i++ )
      {
        tokens [ i ] = str_tokens.nextToken ();
        total++;
      }  // for
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Normalized.parseLine: NoSuchElementException: " + e );
    }  // catch
  }  // method parseLine


/******************************************************************************/
  public String toString ()
  {
    if ( total <= 0 )  return "";

    StringBuffer str = new StringBuffer ( 132 );
    for ( int i = 0; i < total; i++ )
    {
      str.append ( tokens [ i ] );

      if ( i < total - 1 )  str.append ( "\t" );
    }  // for

    return str.toString ();
  }  // method toString


/******************************************************************************/

}  // class Tokens
