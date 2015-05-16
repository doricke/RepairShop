
// import ScanResult;

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

public class ScanIterator extends InputTools
{


/******************************************************************************/

  ScanResult scan_result = new ScanResult ();		// Current SCAN result


/******************************************************************************/
public ScanIterator ()
{
  initialize ();
}  // constructor ScanIterator


/******************************************************************************/
public ScanIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method ScanIterator


/*******************************************************************************/
  public ScanResult getResult ()
  {
    return scan_result;
  }  // method getResult


/*******************************************************************************/
  public ScanResult next ()
  {
    scan_result = new ScanResult ();		// create a new object

    // Get the next SCAN .Results file line.
    getLine ();

    // Parse the next SCAN .Results file line.
    scan_result.nextResult ( line );

    return scan_result;
  }  // method next


/******************************************************************************/

}  // class ScanIterator

