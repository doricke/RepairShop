
import java.io.*;
import java.awt.*;
import java.util.*;
// import Histogram;

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
public class HistoLengths extends Object
{

/******************************************************************************/

protected byte datasets = 1;		// Number of histogram datasets.

protected String project_name;		// Name of current clone

// Histograms of lengths.
private Histogram [] histos;


/******************************************************************************/
public HistoLengths 
    ( String    projects_name
    , String [] names
    , int       bin_size
    , int       max_length 
    )
{
  init ();
  datasets = (byte) names.length;
  project_name = projects_name;
  init_histograms ( names, bin_size, max_length );
}  /* constructor HistoLengths */


/******************************************************************************/
private void init ()
{
  datasets = 1;
  project_name = "";
  histos = null;
}  /* method init */


/******************************************************************************/
private void init_histograms ( String [] names, int bin_size, int max_length )
{
  // Allocate the histograms.
  histos = new Histogram [ names.length ];

  for ( int i = 0; i < names.length; i++ )
  {
    if ( i < names.length )
      histos [ i ] = new Histogram ( names [ i ], bin_size, max_length, bin_size, max_length );
    else
      histos [ i ] = new Histogram ( "" + i, bin_size, max_length, bin_size, max_length );
  }  // for
}  /* method init_histograms */


/******************************************************************************/
public byte getDatasets ()
{
  return datasets;
}  // method getDatasets


/******************************************************************************/
public void setDatasets ( byte value )
{
  datasets = value;
}  // method setDatasets


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
private void write_html_y_data ( PrintStream html_data, String set_name, 
    Histogram histogram )
{
  // Get the y coordinate values.
  int [] y_values = histogram.getRangeValues ();

  // Write out the histogram y coordinate values.
  for ( int i = 0; i < histogram.getNumberOfRanges (); i++ )

    write_html_parameter ( html_data, set_name + "_y" + i, y_values [ i ] );

}  /* method write_html_y_data */


/******************************************************************************/
private void write_html_y_values ( PrintStream html_data )
{
  int set_index = 1;

  write_html_parameter ( html_data, "name_set" + set_index, project_name );
  write_html_y_data ( html_data, "set" + set_index, histos [ 0 ] );
  set_index++;
}  /* method write_html_y_values */


/******************************************************************************/
public void makeHTML ()
{
  int i;					// index

  String htmlFileName = new String ( project_name + ".html" );

  System.out.println ( "Writing HTML file \t" + htmlFileName );
  try 
  {
    FileOutputStream html_file = new FileOutputStream ( htmlFileName );
    PrintStream html_data = new PrintStream ( html_file );

    html_data.println ( "<HTML>" );
    html_data.println ( "<TITLE>Histogram lengths for " + project_name + "</TITLE>" );
    html_data.println ( "<BODY>");

    html_data.println ( "<APPLET CODE=\"Graph.class\" WIDTH=400 HEIGHT=300>" );
    write_html_parameter ( html_data, "title", "lengths for " + project_name );
    write_html_parameter ( html_data, "values", histos [ 0 ].getNumberOfRanges () );
    write_html_parameter ( html_data, "sets", datasets );

    // Get the X coordinate values and names.
    String [] x_names = histos [ 0 ].getRangeNames ();

    // Write out the histogram x coordinate names.
    for ( i = 0; i < histos [ 0 ].getNumberOfRanges (); i++ )

      write_html_parameter ( html_data, "name_x" + i, x_names [ i ] );

    // Write out the histogram x coordinate values.
    for ( i = 0; i < histos [ 0 ].getNumberOfRanges (); i++ )

      write_html_parameter ( html_data, "point_x" + i, x_names [ i ] );

    // Write out the y values and names for all non-zero histograms.
    write_html_y_values ( html_data );


    html_data.println ( "</APPLET>" );

    html_data.println ( "<PRE>" );
    html_data.println ( "<BR>" );

    // Print out the histogram data.
    printHistograms ( html_data );

    html_data.println ( "</PRE>" );
    html_data.println ( "</BODY>" );
    html_data.println ( "</HTML>" );

    html_data.flush ();
    html_data.close ();
  }
  catch ( IOException e1 )
  {
    System.out.println ( "HistoLengths.makeHTML htmlFile IOException: " + e1 );
  }  /* catch */

}  /* method makeHTML */


/******************************************************************************/
public void printHistograms ()
{
  // For each histogram:
  for ( int i = 0; i < histos.length; i++ )

    // Assert: the histogram (i) exists.
    if ( histos [ i ] != null )

      // Print out the individual histogram.
      histos [ i ].printHistogram ();
}  /* method printHistograms */


/******************************************************************************/
public void printHistograms ( PrintStream html_data )
{
  // For each histogram:
  for ( int i = 0; i < histos.length; i++ )

    // Assert: the histogram (i) exists.
    if ( histos [ i ] != null )

      // Print out the individual histogram.
      histos [ i ].printHistogram ( html_data );
}  /* method printHistograms */


/******************************************************************************/
public void addToHistogram ( int index, int value )
{
  // Assert: valid index
  if ( index < histos.length )

    // Add the value to the specified histogram.
    histos [ index ].addValue ( value );
}  // method addToHistograms


/******************************************************************************/

}  /* class HistoLengths */
