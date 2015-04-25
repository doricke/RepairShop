
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

public class Length extends Object
{


/******************************************************************************/

  private   int length = 0;		// sequence length

  private   String name = "";		// sequence name


/******************************************************************************/
  // Constructor Length
  public Length ()
  {
    initialize ();
  }  // constructor Length


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    length = 0;
    name = "";
  }  // method initialize 


/******************************************************************************/
  public int getLength ()
  {
    return length;
  }  // method getLength


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public void setLength ( int value )
  {
    length = value;
  }  // method setLength


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public String toString ()
  {
    return name + "\t" + length;
  }  // method toString


/******************************************************************************/

}  // class Length
