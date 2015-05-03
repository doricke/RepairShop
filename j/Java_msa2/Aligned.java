
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

public class Aligned extends Object
{

/******************************************************************************/

  String sequence_1;			// DNA sequence #1

  String sequence_2;			// DNA sequence #2


/******************************************************************************/
public Aligned ()
{
  initialize ();
}  // constructor Aligned


/******************************************************************************/
public void initialize ()
{
  sequence_1 = null;
  sequence_2 = null;
}  // method initialize


/******************************************************************************/
public String getSequence1 ()
{
  return sequence_1;
}  // method getSequence1


/******************************************************************************/
public String getSequence2 ()
{
  return sequence_2;
}  // method getSequence2


/******************************************************************************/
public void setSequence1 ( String seq_1 )
{
  sequence_1 = seq_1;
}  // method setSequence1


/******************************************************************************/
public void setSequence2 ( String seq_2 )
{
  sequence_2 = seq_2;
}  // method setSequence2


/******************************************************************************/


/******************************************************************************/


}  // class Aligned
