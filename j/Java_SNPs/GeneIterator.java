
// import Gene;

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

public class GeneIterator extends InputFile
{


/******************************************************************************/

  Gene gene = null;		// Gene record


/******************************************************************************/
public GeneIterator ()
{
  initialize ();
}  // constructor GeneIterator


/******************************************************************************/
public GeneIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method GeneIterator


/*******************************************************************************/
  public Gene getGene ()
  {
    return gene;
  }  // method getGene


/*******************************************************************************/
  public Gene next ()
  {
    // Check for end of file.
    if ( isEndOfFile () == true )  return null;

    gene = new Gene ();		// create a new object

    // Get the next Gene file line.
    nextLine ();

    // Check for a blank line.
    if ( line.length () <= 0 )  return null;

    // Parse the current line.
    gene.parse ( line.toString () );

    return gene;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  GeneIterator application = new GeneIterator ( "genes" );
  Gene gene = null;

  while ( application.isEndOfFile () == false )
  {
    gene = application.next ();

    if ( gene != null )
      System.out.println ( gene.toString () );    
  }  // while

}  // method main


/******************************************************************************/

}  // class GeneIterator

