

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
// This method copies one text file to another.
public void textCopy ( DataInputStream in_file, PrintStream out_file )
{
  try
  {
    boolean end_of_file = false;			// End of file flag 
    String line;

    // Copy until the end of the file.
    while ( end_of_file == false )
    {
      try
      {
        line = in_file.readLine ();


        if ( line == null )  end_of_file = true;
        else
        {
          out_file.println ( line );
        }  /* else */
      }  /* try */
      catch ( EOFException e1 )
      {
        System.out.println ( "method textCopy: End of file reached: " + e1 );
        end_of_file = true;
      }  /* catch */
    }  /* while */

    /* Close files */
    try 
    {
      out_file.flush ();
      out_file.close ();

      in_file.close ();
    }
    catch ( IOException e3 )
    {
      System.out.println ( "method textCopy: IOException while closing files: " + e3 );
      return;
    }  /* catch */
  }  /* try */
  catch ( IOException e2 )
  {
    System.out.println ( "method textCopy IOException: " + e2 );
  }  /* catch */

}  /* method textCopy */


