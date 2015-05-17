

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

/*******************************************************************************/
  public int find ( String name )
  {
    int key = 0;			// comparison key
    int lower = 0;			// lower index
    int middle = 0;			// middle index
    int upper = loaded - 1;		// upper index

    while ( lower <= upper )
    {
      middle = (lower + upper) / 2;	// compute the index of the middle record

      // System.out.println ( "lower = " + lower + ", upper = " + upper + ", middle = " + middle );

      key = name.compareTo ( v8_map [ middle ].getSequenceName () );

      if ( key == 0 )  return middle;

      if ( key < 0 )
        upper = middle - 1;		// look in the lower half
      else
        lower = middle + 1;		// look in the upper half
    }  // while

    return -1;				// not found
  }  // method binarySearch


