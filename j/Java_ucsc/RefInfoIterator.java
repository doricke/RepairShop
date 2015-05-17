
// import RefInfo;

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

public class RefInfoIterator extends InputFile
{


/******************************************************************************/

  RefInfo ref_info = null;		// UCSC refGene annotation data


/******************************************************************************/
public RefInfoIterator ()
{
  initialize ();
}  // constructor RefInfoIterator


/******************************************************************************/
public RefInfoIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method RefInfoIterator


/*******************************************************************************/
  public RefInfo getRefInfo ()
  {
    return ref_info;
  }  // method getRefInfo


/*******************************************************************************/
  public RefInfo next ()
  {
    // Check for end of file.
    if ( isEndOfFile () == true )  return null;

    ref_info = new RefInfo ();		// create a new object

    // Get the next RefInfo file line.
    nextLine ();

    // Check for a blank line.
    if ( line.length () <= 0 )  return null;

    // Parse the current line.
    ref_info.parse ( line.toString () );

    return ref_info;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  RefInfoIterator application = new RefInfoIterator ( "Chip_A_pos.info" );
  RefInfo ref_info = null;

  while ( application.isEndOfFile () == false )
  {
    ref_info = application.next ();

    if ( ref_info != null )
      System.out.println ( ref_info.toString () );    
  }  // while

}  // method main


/******************************************************************************/

}  // class RefInfoIterator

