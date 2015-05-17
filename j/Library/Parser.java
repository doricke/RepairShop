// Generic Parser class - to be extended

import InputTools;
import XMLTools;

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

public abstract class Parser
{

/******************************************************************************/

protected XMLTools xml_file;

/******************************************************************************/

public Parser()
{
 xml_file = new XMLTools();
} // end: constructor Parser

/******************************************************************************/
// This method searches the argument array for the argument string, searching
// elements 0 (inclusive) through index (exclusive).  Argument array must be of type String[]

public int arraySearch( String[] array, String s, int index )
{
 for( int t = 0; t < index; t ++ )
 {
  if( index < array.length )
  {
   if( array[ t ].equals( s ) ) return t;
  } // end: if
 } // end: for

 return -1;

} // end: method arraySearch

/******************************************************************************/

} // end: abstract class Parser
