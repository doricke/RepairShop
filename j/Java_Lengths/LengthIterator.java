
// import InputTools;
// import Length;

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

public class LengthIterator extends InputTools
{


/******************************************************************************/

  Length length = null;				// sequence length object


/******************************************************************************/
public LengthIterator ()
{
  initialize ();
}  // constructor LengthIterator


/******************************************************************************/
public LengthIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method LengthIterator


/*******************************************************************************/
  public Length getLength ()
  {
    return length;
  }  // method getLength


/*******************************************************************************/
  private void parse ( String line, Length length )
  {
    // Validate line.
    if ( line.length () <= 0 )  return;

    int index = line.indexOf ( "\t" );
    if ( index == -1 )
    {
      System.out.println ( "Invalid line: " + line );
      return;
    }  // if

    length.setName ( line.substring ( 0, index ) );
    length.setLength ( getInteger ( line.substring ( index ) ) );
  }  // method parse


/*******************************************************************************/
  public Length next ()
  {
    length = new Length ();		// create a new object

    // Get the next file line.
    getLine ();

    // Check for end of input file.
    if ( ( isEndOfFile () == true ) && ( line.length () <= 0 ) )  return null;

    // Parse the next file line.
    parse ( line.toString (), length );

    return length;
  }  // method next


/******************************************************************************/

}  // class LengthIterator

