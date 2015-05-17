import java.io.*;
import java.awt.*;
import java.util.*;

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
public class GraphTest extends Object
{

/******************************************************************************/

static protected String cloneName;		// Name of current clone

static protected int insertSize;		// Size of clone insert

static protected int minCount = 19;		// Minimum values to count

static protected int minGood;			// Minimum good quality



/******************************************************************************/
public GraphTest ()
{
  init ();
  init_histograms ();
}  /* constructor GraphTest */


/******************************************************************************/
public GraphTest ( String clone_name, int minimum_good )
{
  init ();
  cloneName = clone_name;
  minGood   = minimum_good;
  init_histograms ();
}  /* constructor GraphTest */


/******************************************************************************/
public GraphTest ( String clone_name, int minimum_good, int insert_size )
{
  init ();
  cloneName = clone_name;
  minGood   = minimum_good;
  insertSize = insert_size;
  init_histograms ();
}  /* constructor GraphTest */


/******************************************************************************/
private void init ( )
{
  cloneName = "";
  insertSize = 0;
  minGood = 51;
}  /* method init */


/******************************************************************************/
private void init_histograms ( )
{
  int binSize = 100;
  int maxLength = 900;
}  /* method init_histograms */


/******************************************************************************/


/******************************************************************************/
private void write_html_parameter ( PrintStream html_data, String name,
    String value )
{
  html_data.println ( "<PARAM NAME=\"" + name + "\" VALUE=\"" + value + "\">" );
}  /* method write_html_parameter */


/******************************************************************************/
private void write_html_parameter ( PrintStream html_data, String name,
    int value )
{
  html_data.println ( "<PARAM NAME=\"" + name + "\" VALUE=\"" + value + "\">" );
}  /* method write_html_parameter */


/******************************************************************************/
private void write_html_y_data ( PrintStream html_data, String set_name ) 
{
  // Write out the histogram y coordinate values.
  for ( int i = 0; i < 10; i++ )

    write_html_parameter ( html_data, set_name + "_y" + i, i );

}  /* method write_html_y_data */


/******************************************************************************/
private void write_html_y_values ( PrintStream html_data )
{
  int set_index = 1;


    write_html_parameter ( html_data, "name_set" + set_index, "All" );
    write_html_y_data ( html_data, "set" + set_index );
    set_index++;
}  /* method write_html_y_values */


/******************************************************************************/
public void makeHTML ()
{
  int i;					// index


  String htmlFileName = new String ( cloneName + ".html" );

  System.out.println ( "Writing HTML file \t" + htmlFileName );
  try 
  {
    FileOutputStream html_file = new FileOutputStream ( htmlFileName );
    PrintStream html_data = new PrintStream ( html_file );

    html_data.println ( "<HTML>" );
    html_data.println ( "<TITLE>Read lengths for clone" + cloneName + "</TITLE>" );
    html_data.println ( "<BODY>");

    html_data.println ( "<APPLET CODE=\"Graph.class\" WIDTH=400 HEIGHT=300>" );
    write_html_parameter ( html_data, "title", "Clear read lengths for clone " + cloneName );
    write_html_parameter ( html_data, "values", 2 );
    write_html_parameter ( html_data, "sets", 2 );


    // Write out the histogram x coordinate names.
    for ( i = 0; i < 10; i++ )

      write_html_parameter ( html_data, "name_x" + i, "name_x" + i );

    // Write out the histogram x coordinate values.
    for ( i = 0; i < 10; i++ )

      write_html_parameter ( html_data, "point_x" + i, i );

    // Write out the y values and names for all non-zero histograms.
    write_html_y_values ( html_data );


    html_data.println ( "</APPLET>" );

    html_data.println ( "<PRE>" );
    html_data.println ( "<BR>" );

    html_data.println ( "</PRE>" );
    html_data.println ( "</BODY>" );
    html_data.println ( "</HTML>" );

    html_data.flush ();
    html_data.close ();
  }
  catch ( IOException e1 )
  {
    System.out.println ( "GraphTest.makeHTML htmlFile IOException: " + e1 );
  }  /* catch */

}  /* method makeHTML */


/******************************************************************************/


/******************************************************************************/
static public void main ( String [] args )
{
  System.out.println ( "GraphTest started" );

  cloneName = "test";

  GraphTest demo = new GraphTest ();
  demo.makeHTML ();
}  // method main


}  /* class GraphTest*/
