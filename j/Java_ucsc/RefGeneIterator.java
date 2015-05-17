
// import RefGene;

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

public class RefGeneIterator extends InputFile
{


/******************************************************************************/

  RefGene ref_gene = null;		// UCSC refGene annotation data


/******************************************************************************/
public RefGeneIterator ()
{
  initialize ();
}  // constructor RefGeneIterator


/******************************************************************************/
public RefGeneIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method RefGeneIterator


/*******************************************************************************/
  public RefGene getRefGene ()
  {
    return ref_gene;
  }  // method getRefGene


/*******************************************************************************/
  public RefGene next ()
  {
    // Check for end of file.
    if ( isEndOfFile () == true )  return null;

    ref_gene = new RefGene ();		// create a new object

    // Get the next RefGene file line.
    nextLine ();

    // Check for a blank line.
    if ( line.length () <= 0 )  return null;

    // Parse the current line.
    ref_gene.parse ( line.toString () );

    return ref_gene;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  RefGeneIterator application = new RefGeneIterator ( "refGene.chr21" );
  RefGene ref_gene = null;

  while ( application.isEndOfFile () == false )
  {
    ref_gene = application.next ();

    if ( ref_gene != null )
      System.out.println ( ref_gene.toString () );    
  }  // while

}  // method main


/******************************************************************************/

}  // class RefGeneIterator

