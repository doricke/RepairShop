
// import OutputTools;

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

public class WordTools extends Object
{

/******************************************************************************/

protected StringBuffer line = new StringBuffer ( 10000 );	// current line


/******************************************************************************/
public WordTools ()
{
  initialize ();
}  // constructor WordTools


/******************************************************************************/
public void initialize ()
{
  line.setLength ( 0 );
}  // method initialize


/******************************************************************************/
  public void cleanLine ()
  {
    for ( int i = 0; i < line.length (); i++ )
    {
      switch ( line.charAt ( i ) )
      {
        case '(':
        case ')':
        case '[':
        case ']':
        case '{':
        case '}':
        case ',':
        case '.':
        case '\'':
        case '\\':
        case '"':
        case '!':
        case '@':
        case '#':
        case '$':
        case '%':
        case '^':
        case '&':
        case '*':
        case '+':
        case '=':
        case ':':
        case ';':
        case '<':
        case '>':
        case '?':
        case '/':
        case '|':
        case '`':
        case '~':
          line.setCharAt ( i, ' ' );
        default: ;
      }  // switch
    }  // if
  }  // method cleanLine


/******************************************************************************/
  public StringBuffer getLine ()
  {
    return line;
  }  // method getLine


/******************************************************************************/
  public void setLine ( String value )
  {
    line.setLength ( 0 );
    line.append ( value );
  }  // method setInvalidFile


/******************************************************************************/
  public void setLine ( StringBuffer value )
  {
    line.setLength ( 0 );
    line.append ( value );
  }  // method setInvalidFile


/******************************************************************************/
  private boolean isGoodWord ( String word )
  {
    // Check for a short word.
    if ( word.length () <= 2 )  return false;

    // Check for a non-letter first character.
    char first_char = word.charAt ( 0 );

    if ( ( ( first_char >= 'a' ) && ( first_char <= 'z' ) ) ||
         ( ( first_char >= 'A' ) && ( first_char <= 'Z' ) ) )

      return true;

    return false;
  }  // method isGoodWord


/******************************************************************************/
  private String uncapitalize ( String word )
  {
    if ( word.length () <= 2 )  return word;

    // Check for a capitalized word.
    char first_char = word.charAt ( 0 );
    char second_char = word.charAt ( 1 );

    // Check for a capitalized word.
    if ( ( ( first_char >= 'A' ) && ( first_char <= 'Z' ) ) &&
         ( ( second_char >= 'a' ) && ( second_char <= 'z' ) ) )

      return word.toLowerCase ();

    return word;
  }  // method uncapitalize


/******************************************************************************/
  public TreeSet split ( String text, TreeSet words )
  {
    setLine ( text );

    return split ( words );
  }  // method split


/******************************************************************************/
  public TreeSet split ( TreeSet words )
  {
    cleanLine ();

    StringTokenizer tokens = new StringTokenizer ( line.toString (), " \t" );

    // Add the tokens to the sorted list.
    int token_count = tokens.countTokens ();
    for ( int i = 0; i < token_count; i++ )
    {
      try
      {
        String token = tokens.nextToken ();

        // Ignore poor words.
        if ( isGoodWord ( token ) == true )  

          words.add ( token.toLowerCase () );
      }  // try
      catch ( NoSuchElementException e )
      {
        System.out.println ( "WordTools.split: " + e );
      }  // catch
    }  // for

    return words;
  }  // method split


/******************************************************************************/
  public void writeWords ( String text, OutputTools word_file )
  {
    setLine ( text );

    writeWords ( word_file );
  }  // method writeWords


/******************************************************************************/
  public void writeWords ( OutputTools word_file )
  {
    cleanLine ();

    StringTokenizer tokens = new StringTokenizer ( line.toString (), " \t" );

    // Add the tokens to the sorted list.
    int token_count = tokens.countTokens ();
    for ( int i = 0; i < token_count; i++ )
    {
      try
      {
        String token = tokens.nextToken ();

        // Ignore poor words.
        if ( isGoodWord ( token ) == true )  

          word_file.println ( token.toLowerCase () );
      }  // try
      catch ( NoSuchElementException e )
      {
        System.out.println ( "WordTools.writeWords: " + e );
      }  // catch
    }  // for
  }  // method writeWords


/******************************************************************************/
  public static void main ( String [] args )
  {
    WordTools app = new WordTools ();

    app.setLine ( "Hello. (this) is, a: test! [of] {sorts}" );
    app.cleanLine ();
    System.out.println ( app.getLine ().toString () );

    TreeSet words = new TreeSet ();
    app.split ( words );
  }  // method main

}  // class WordTools

