
// import Plink;

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

public class PlinkIterator extends InputFile
{


/******************************************************************************/

  Plink plink = null;		// Plink record


/******************************************************************************/
public PlinkIterator ()
{
  initialize ();
}  // constructor PlinkIterator


/******************************************************************************/
public PlinkIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method PlinkIterator


/*******************************************************************************/
  public Plink getPlink ()
  {
    return plink;
  }  // method getPlink


/*******************************************************************************/
  public Plink next ()
  {
    // Check for end of file.
    if ( isEndOfFile () == true )  return null;

    plink = new Plink ();		// create a new object

    // Get the next Plink file line.
    nextLine ();

    // Check for a blank line.
    if ( line.length () <= 0 )  return null;

    // Parse the current line.
    plink.parse ( line.toString () );

    return plink;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  PlinkIterator application = new PlinkIterator ( "DGI_full_table_HDL_1106_cases_all_sexes.txt" );
  Plink plink = null;

  while ( application.isEndOfFile () == false )
  {
    plink = application.next ();

    if ( plink != null )
      System.out.println ( plink.toString () );    
  }  // while

}  // method main


/******************************************************************************/

}  // class PlinkIterator

