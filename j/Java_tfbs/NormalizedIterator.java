
// import Normalized;

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

public class NormalizedIterator extends InputFile
{


/******************************************************************************/

  Normalized normalized = new Normalized ();		// Current normalized record


/******************************************************************************/
public NormalizedIterator ()
{
  initialize ();
}  // constructor NormalizedIterator


/******************************************************************************/
public NormalizedIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method NormalizedIterator


/*******************************************************************************/
  public Normalized getNormalized ()
  {
    return normalized;
  }  // method getNormalized


/*******************************************************************************/
  public Normalized next ()
  {
    normalized = new Normalized ();		// create a new object

    // Get the next normalized file line.
    nextLine ();

    if ( line.length () <= 0 )  return null;

    // Parse the next file line.
    normalized.parseLine ( line.toString () );

    return normalized;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  NormalizedIterator app = new NormalizedIterator ();
  Normalized tfbs;

  if ( args.length == 1 )  app.setFileName ( args [ 0 ] );
  else app.setFileName ( "test" );

  app.openFile ();

  while ( app.isEndOfFile () == false )
  {
    tfbs = app.next ();

    if ( tfbs != null )
    {
    }  // if
  }  // while
  app.closeFile ();

}  // method main


/******************************************************************************/

}  // class NormalizedIterator

