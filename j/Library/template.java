import java.io.*;

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
public class ParseXml extends Object
{


/******************************************************************************/


/******************************************************************************/
// Parameters:


/******************************************************************************/
public ParseXml ()
{
  initialize ();
}  /* constructor ParseXml */


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  // method setFileName 


/******************************************************************************/


/******************************************************************************/
// This method processes the Phrap .ace file.
public void processXml ()
{
}  // method processXml


/******************************************************************************/
public static void usage ()
{
  System.out.println ( "This is the ParseXml program." );
  System.out.println ();
  System.out.println ( "This program reads an XML file." );
  System.out.println ();
  System.out.println ( "To run type:" );
  System.out.println ();
  System.out.println ( "java ParseXml <name.xml>" );
  System.out.println ();
  System.out.println ( "Where <name.xml> is an XML filename." );
}  // method usage


/******************************************************************************/
public static void main ( String [] args )
{
  /* Check for parameters. */
  if ( args.length == 0 )
  {
    usage ();
  }
  else
  {
    ParseXml application = new ParseXml ();

    // The .ace file name is the first parameter.
    application.setFileName ( args [ 0 ] );

    // Process the .ace contigs.
    application.processXml ();
  }  /* else */
}  /* method main */

}  /* class ParseXml */
