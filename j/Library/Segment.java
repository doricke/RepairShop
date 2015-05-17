

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

public class Segment extends Object
{


/******************************************************************************/

  private   int  identities = 0;		// number of identities

  private   int  length = 0;			// length of the segments

  private   String  segment1 = "";		// sequence segment 1

  private   String  segment2 = "";		// sequence segment 2

  private   int  start1 = 0;			// start of segment 1 in sequence 1

  private   int  start2 = 0;			// start of segment 2 in sequence 2


/******************************************************************************/
  // Constructor Segment
  public Segment ()
  {
    initialize ();
  }  // constructor Segment


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    identities = 0;
    length = 0;
    segment1 = "";
    segment2 = "";
    start1 = 0;
    start2 = 0;
  }  // method initialize 


/******************************************************************************/
  public int getIdentities ()
  {
    return identities;
  }  // method getIdentities


/******************************************************************************/
  public int getLength ()
  {
    return length;
  }  // method getLength


/******************************************************************************/
  public String getSegment1 ()
  {
    return segment1;
  }  // method getSegment1


/******************************************************************************/
  public String getSegment2 ()
  {
    return segment2;
  }  // method getSegment2


/******************************************************************************/
  public int getStart1 ()
  {
    return start1;
  }  // method getStart1


/******************************************************************************/
  public int getStart2 ()
  {
    return start2;
  }  // method getStart2


/******************************************************************************/
  public void setIdentities ( int value )
  {
    identities = value;
  }  // method setIdentities


/******************************************************************************/
  public void setLength ( int value )
  {
    length = value;
  }  // method setLength


/******************************************************************************/
  public void setSegment1 ( String value )
  {
    segment1 = value;
  }  // method setSegment1


/******************************************************************************/
  public void setSegment2 ( String value )
  {
    segment2 = value;
  }  // method setSegment2


/******************************************************************************/
  public void setStart1 ( int value )
  {
    start1 = value;
  }  // method setStart1


/******************************************************************************/
  public void setStart2 ( int value )
  {
    start2 = value;
  }  // method setStart2


/******************************************************************************/

}  // class Segment
