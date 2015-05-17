
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

public class Sort extends Object
{

/******************************************************************************/

  // Maximum number of text lines that can be sorted.
  private final static int MAX_LINES = 1000000;	


/******************************************************************************/

  String [] lines = new String [ MAX_LINES ];		// text lines

  int total = 0;		// Number of lines read in


/******************************************************************************/
public Sort ()
{
  initialize ();
}  // constructor Sort


/******************************************************************************/
private void initialize ()
{
  total = 0;
}  // method initialize


/******************************************************************************/
private void addLine ( String line )
{
  // Assert: line contains data.
  if ( line.length () <= 0 )  return;

  // Check for the first line.
  if ( total == 0 )
  {
    lines [ 0 ] = line;
    total++;
    return;
  }  // if

  // Find the insertion place.
  int index = total;
  while ( ( index >= 1 ) &&
          ( InputTools.compareTo ( line, lines [ index-1 ] ) < 0 ) )
  {  
    lines [ index ] = lines [ index - 1 ];
    index--;
  }  // while

  lines [ index ] = line;
  total++;
}  // method addLine


/******************************************************************************/
public void sortFile ( InputTools file ) 
{
  // Read in the text file.
  while ( ( file.isEndOfFile () == false ) && ( total < MAX_LINES ) )
  {
    // Add the line toe the sorted lines.
    addLine ( file.getLine ().toString () );
  }  // while

  // Print out the text file.
  for ( int i = 0; i < total; i++ )
    System.out.println ( lines [ i ] );
}  // method sortFile 


/******************************************************************************/
  public void processFile ( String file_name )
  {
    // Get the file name of the list of Cel output files.
    InputTools text_file = new InputTools ();
    text_file.setFileName ( file_name );
    text_file.openFile ();

    sortFile ( text_file );

    // Close the files.
    text_file.closeFile ();
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java Sort <text_file" );
    System.out.println ();
    System.out.println ( "where <text_file> is the file name of a text file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    Sort app = new Sort ();

    if ( args.length == 0 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

/******************************************************************************/

}  //class Sort
