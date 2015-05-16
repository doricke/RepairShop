
// import Affy500;

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

public class Affy500Iterator extends InputFile
{


/******************************************************************************/

  Affy500 affy500 = null;		// Affy500 record


/******************************************************************************/
public Affy500Iterator ()
{
  initialize ();
}  // constructor Affy500Iterator


/******************************************************************************/
public Affy500Iterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method Affy500Iterator


/*******************************************************************************/
  public Affy500 getAffy500 ()
  {
    return affy500;
  }  // method getAffy500


/*******************************************************************************/
  public Affy500 next ()
  {
    // Check for end of file.
    if ( isEndOfFile () == true )  return null;

    affy500 = new Affy500 ();		// create a new object

    // Get the next Affy500 file line.
    nextLine ();

    // Check for a blank line.
    if ( line.length () <= 0 )  return null;

    // Parse the current line.
    affy500.parse ( line.toString () );

    return affy500;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  Affy500Iterator application = new Affy500Iterator ( "test.affy500" );
  Affy500 affy500 = null;

  while ( application.isEndOfFile () == false )
  {
    affy500 = application.next ();

    if ( affy500 != null )
      System.out.println ( affy500.toString () );    
  }  // while

}  // method main


/******************************************************************************/

}  // class Affy500Iterator

