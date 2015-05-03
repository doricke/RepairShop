

/******************************************************************************/
/**
 *
 * @author	Darrell O. Ricke, PhD
 * @version	1.0
 */

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

public class ActiveName
{
  private String compute_host_name = "";	// Current compute host

  private String name = "";			// Current name

  int position = 0;				// queue position


/******************************************************************************/
public ActiveName ()
{
  initialize ();
}  // constructor ActiveName


/******************************************************************************/
public ActiveName ( String server_name, String file_name, int queue_order )
{
  initialize ();
  compute_host_name = server_name;
  name = file_name;
  position = queue_order;
}  // constructor ActiveName


/******************************************************************************/
public void initialize ()
{
  compute_host_name = "";
  name = "";
  position = 0;
}  // method initialize


/******************************************************************************/
public String getComputeHostName ()
{
  return compute_host_name;
}  // method getComputeHostName


/******************************************************************************/
public String getName ()
{
  return name;
}  // method getName


/******************************************************************************/
public int getPosition ()
{
  return position;
}  // method getPosition


/******************************************************************************/
public void setComputeHostName ( String value )
{
  compute_host_name = value;
}  // method setComputeHostName


/******************************************************************************/
public void setName ( String value )
{
  name = value;
}  // method setName


/******************************************************************************/
public void setPosition ( int value )
{
  position = value;
}  // method setPosition


/******************************************************************************/

}  // class ActiveName
