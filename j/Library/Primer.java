
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
public class Primer extends Object 
{

/******************************************************************************/

protected String sequence;

protected String position;

protected int count;


/******************************************************************************/
public Primer ()
{
  sequence = "";
  position = "";
  count = 0;
}  /* constructor Primer */


/******************************************************************************/
public Primer ( String seq )
{
  sequence = seq;
  position = "";
  count = 1;
}  /* constructor Primer */


/******************************************************************************/
public Primer ( String seq, String pos )
{
  sequence = seq;
  position = pos;
  count = 1;
}  /* constructor Primer */


/******************************************************************************/
public int getCount ( )
{
  return count;
}  /* method getCount */


/******************************************************************************/
public String getPosition ( )
{
  return position;
}  /* method getPosition */


/******************************************************************************/
public String getSequence ( )
{
  return sequence;
}  /* method getSequence */


/******************************************************************************/
public void incrementCount ( )
{
  count++;
}  /* method incrementCount */


/******************************************************************************/
public void setPosition ( String pos )
{
  position = pos;
}  /* method setPosition */


/******************************************************************************/
public void setSequence ( String seq )
{
  sequence = seq;
}  /* method setSequence */

}  /* class Primer */
