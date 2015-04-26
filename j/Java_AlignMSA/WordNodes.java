
// import WordNode;

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

public class WordNodes extends Object
{


/******************************************************************************/

  // Maximum number of WordNode records.
  private  static  final  int  MAX_WORD_EDGES = 20000;


/******************************************************************************/

  private   int  total = 0;			// total number of nodes

  // All word nodes
  private   WordNode [] word_nodes = new WordNode [ MAX_WORD_EDGES ];	


/******************************************************************************/
  // Constructor WordNodes
  public WordNodes ()
  {
    initialize ();
  }  // constructor WordNodes


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public boolean checkAminos ( int index, String aminos )
  {
    // Assert: valid index.
    if ( ( index < 0 ) || ( index >= total ) )  return false;

    return aminos.equals ( word_nodes [ index ].getAminos () );
  }  // method checkAminos


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public WordNode getWordNode ( int index )
  {
    // Assert: valid index.
    if ( ( index < 0 ) || ( index >= total ) )  return null;

    return word_nodes [ index ];
  }  // method getWordNode


/******************************************************************************/
  public WordNode [] getWordNodes ()
  {
    return word_nodes;
  }  // method get[]


/******************************************************************************/
  public int addNode ( WordNode word_node )
  {
    if ( total + 1 >= MAX_WORD_EDGES )
    {
      System.out.println ( "*Warning* not enough WordNodes allocated." );
      return -1;
    }  // if

    word_nodes [ total ] = word_node;
    total++;

    return (total - 1);
  }  // method addNode


/******************************************************************************/
  public String toString ( int index )
  {
    // Assert: valid index
    if ( ( index < 0 ) || ( index >= total ) )  return "";

    return word_nodes [ index ].toString2 ();
  }  // method toString


/******************************************************************************/
  public void summarize ()
  {
    System.out.println ();
    System.out.println ( "WordNodes:" );

    for ( int i = 0; i < total; i++ )

      System.out.println ( i + "\t" + word_nodes [ i ].toString2 () );
  }  // method summarize


/******************************************************************************/

}  // class WordNodes
