
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

public class Pattern extends Object
{


/******************************************************************************/

  private   String  name = "";		// Motif name

  private   String  pattern = "";	// Motif pattern (GCG format)

  private   String  pdoc_name = "";	// PDoc_Name - Prosite documentation name


/******************************************************************************/
  // Constructor Pattern
  public Pattern ()
  {
    initialize ();
  }  // constructor Pattern


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    name = "";
    pattern = "";
    pdoc_name = "";
  }  // method initialize 


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public String getPattern ()
  {
    return pattern;
  }  // method getPattern


/******************************************************************************/
  public String getPatternPerl ()
  {
    StringBuffer pat = new StringBuffer ( pattern );

    // Substitute characters.
    for ( int index = 0; index < pat.length (); index++ )
    {
      if ( pat.charAt ( index ) == 'x' )  pat.setCharAt ( index, '.' );
      if ( pat.charAt ( index ) == '(' )  pat.setCharAt ( index, '[' );
      if ( pat.charAt ( index ) == ')' )
      {
        pat.setCharAt ( index, ']' );

        // Delete commas between amino acids.
        int i = index - 2;
        do
        {
          // Check for a comma between amino acids.
          if ( pat.charAt ( i ) == ',' )
          {
            pat.deleteCharAt ( i );	// delete the comma
            i--;			// skip preceeding amino acid
            index--;			// pattern was shortened
          }  // if

          if ( pat.charAt ( i ) != '[' )  i--;
        }  // while
        while ( pat.charAt ( i ) != '[' );
      }  // if
    }  // for

    // Set up negative pattern matches.
    int index = pat.indexOf ( "~[" );
    while ( index >= 0 )
    {
      pat.setCharAt ( index, '[' );
      pat.setCharAt ( index+1, '^' );
      index = pat.indexOf ( "~[" );
    }  // while

    // Check for repetition counts without curly braces {}.
    for ( index = pat.length () - 1; index >= 0; index-- )
    {
      if ( ( pat.charAt ( index ) == '.' ) ||
           ( pat.charAt ( index ) == ']' ) )
      {
        // Check if the next character is a digit.
        if ( ( index + 1 < pat.length () ) &&
             ( ( pat.charAt ( index + 1 ) >= '0' ) &&
               ( pat.charAt ( index + 1 ) <= '9' ) ) )
        {
          // Find the end of the number.
          int i = index + 2;
          while ( ( i < pat.length () ) &&
                  ( ( pat.charAt ( i ) >= '0' ) &&
                    ( pat.charAt ( i ) <= '9' ) ) )
          {
            i++;
          }  // while
          
          if ( i == pat.length () )
            pat.append ( '}' );
          else
            pat.insert ( i, '}' );

          pat.insert ( index+1, '{' );
        }  // if
      }  // if
    }  // for

    // Check for the start of sequence character.
    if ( pat.charAt ( 0 ) == '<' )  pat.setCharAt ( 0, '^' );

    // Check for the end of sequence character.
    if ( pat.charAt ( pat.length () - 1 ) == '>' )
      pat.setCharAt ( pat.length () - 1, '$' );

    return pat.toString ();
  }  // method getPatternPerl


/******************************************************************************/
  public String getPdocName ()
  {
    return pdoc_name;
  }  // method getPdocName


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setPattern ( String value )
  {
    pattern = value;
  }  // method setPattern


/******************************************************************************/
  public void setPdocName ( String value )
  {
    pdoc_name = value;
  }  // method setPdocName


/******************************************************************************/
  public void newPattern ( String line )
  {
    // Split the line into tokens.
    StringTokenizer tokens = new StringTokenizer ( line, " " );

    // Capture the tokens.
    try
    {
      name          = tokens.nextToken ();
      String offset = tokens.nextToken ();
      pattern       = tokens.nextToken ();
      pdoc_name     = tokens.nextToken ();
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "newPattern: NoSuchElementException: (" + line + ") " +  e );
      return;
    }  // catch
  }  // method newPattern


/******************************************************************************/

}  // class Pattern
