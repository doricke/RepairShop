

// import InputTools;

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

public class SSRsCount extends Object
{


/******************************************************************************/
public SSRsCount ()
{
}  // constructor SSRsCount


/*******************************************************************************/
  public void processFile ( String file_name )
  {
    int di_count = 0;			// count of di-nucleotide SSRs
    int tri_count = 0;			// count of tri-nucleotide SSRs
    int quad_count = 0;			// count of quad-nucleotide SSRs
    String line;			// current SSR file line

    // Set up the input file.
    InputTools ssrs_file = new InputTools ();
    ssrs_file.setFileName ( file_name );
    ssrs_file.openFile ();

    while ( ssrs_file.isEndOfFile () == false )
    {
      line = ssrs_file.getLine ().toString ();

      if ( line.indexOf ( "\tdi\t" ) > 0 )  di_count++;
      if ( line.indexOf ( "\ttri\t" ) > 0 )  tri_count++;
      if ( line.indexOf ( "\tquad\t" ) > 0 )  quad_count++;
    }  // while

    System.out.println ( file_name + "\t" + di_count + "\t" + tri_count + "\t" + quad_count );

    // Close file.
    ssrs_file.closeFile ();
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java SSRsCount <file.list>" );
    System.out.println ();
    System.out.println ( "where <file.list> is the file name of the SSRs file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    SSRsCount app = new SSRsCount ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.processFile ( args [ 0 ] );
    }  // else
  }  // method main

}  // class SSRsCount

