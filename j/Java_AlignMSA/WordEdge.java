
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

public class WordEdge extends Object
{


/******************************************************************************/

  private   short  count = 0;			// number of sequences sharing this edge

  private   int  next_node = -1;		// next word node

  private   int  previous_node = -1;		// previous word node

  private   boolean  visited = false;		// graph traversal visited flag


/******************************************************************************/
  // Constructor WordEdge
  public WordEdge ()
  {
    initialize ();
  }  // constructor WordEdge


/******************************************************************************/
  public WordEdge ( int left, int right )
  {
    initialize ();
    previous_node = left;
    next_node = right;
    count = 1;
  }  // constructor WordEdge


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    count = 0;
    next_node = -1;
    previous_node = -1;
    visited = false;
  }  // method initialize 


/******************************************************************************/
  public short getCount ()
  {
    return count;
  }  // method getCount


/******************************************************************************/
  public int getNextNode ()
  {
    return next_node;
  }  // method getNextNode


/******************************************************************************/
  public int getPreviousNode ()
  {
    return previous_node;
  }  // method getPreviousNode


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
  public void setCount ( short value )
  {
    count = value;
  }  // method setCount


/******************************************************************************/
  public void setNextNode ( int value )
  {
    next_node = value;
  }  // method setNextNode


/******************************************************************************/
  public void setPreviousNode ( int value )
  {
    previous_node = value;
  }  // method setPreviousNode


/******************************************************************************/
  public void setVisited ( boolean value )
  {
    visited = value;
  }  // method setVisited


/******************************************************************************/
  public String toString2 ()
  {
    return "(" 
        + count 
        + ") [" 
        + previous_node 
        + "-" 
        + next_node 
        + "]\t" 
        + visited
        ;
  }  // method toString


/******************************************************************************/

}  // class WordEdge
