
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

public class WordNode extends Object
{


/******************************************************************************/

  private   String  aminos = "";	// amino acids

  private   short  count = 0;		// count of ordered previous WordNodes

  private   boolean  visited = false;	// graph traversal visited flag


/******************************************************************************/
  // Constructor WordNode
  public WordNode ()
  {
    initialize ();
  }  // constructor WordNode


/******************************************************************************/
  // Constructor WordNode
  public WordNode ( String peptide )
  {
    initialize ();

    aminos = peptide;
    setCount ( (short) 2 );
  }  // constructor WordNode


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    aminos = "";
    count = 0;
    visited = false;
  }  // method initialize 


/******************************************************************************/
  public String getAminos ()
  {
    return aminos;
  }  // method getAminos


/******************************************************************************/
  public short getCount ()
  {
    return count;
  }  // method getCount


/******************************************************************************/
  public boolean getVisited ()
  {
    return visited;
  }  // method getVisited


/******************************************************************************/
  public void incrementCount ()
  {
    count++;
  }  // method incrementCount


/******************************************************************************/
  public boolean isVisited ()
  {
    return visited;
  }  // method isVisited


/******************************************************************************/
  public void setAminos ( String value )
  {
    aminos = value;
  }  // method setAminos


/******************************************************************************/
  public void setCount ( short value )
  {
    count = value;
  }  // method setCount


/******************************************************************************/
  public void setVisited ( boolean value )
  {
    visited = value;
  }  // method setVisited


/******************************************************************************/
  public String toString2 ()
  {
    return aminos + "\tc " + count + "\t" + visited;
  }  // method toString


/******************************************************************************/

}  // class WordNode
