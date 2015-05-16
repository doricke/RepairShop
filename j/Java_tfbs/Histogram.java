
import java.io.*;
// import Format;

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
public class Histogram extends Object
{

/******************************************************************************/

protected int good_maximum;		// Maximum good value

protected int good_average;		// good average

protected int good_minimum;		// Minimum good value

protected int maxRange;			// Upper limit on the size range

protected String name;			// Histogram name

protected int num_good_values;		// Number of good values

protected int num_values;		// Number of values in histogram

protected int range;			// Range size for each bin

protected int rangeLength;		// Length of the range bins

protected long std_dev_sum = 0L;	// sum for standard deviation calculation

protected float total;			// Sum of values added to histogram

protected long total_good;		// Sum of good values

protected int values [];		// Histogram values


/******************************************************************************/
public Histogram ()
{
  Init ();
}  /* constructor Histogram */


/******************************************************************************/
public void reset ( String histogram_name, int bin, int maximum )
{
  Init ();
  name = histogram_name;
  range = bin;
  maxRange = maximum;
  rangeLength = (maxRange / range) + 2;	// room for zero & maximum

  // Check for final partial range.
  if ( maxRange > (rangeLength - 2) * range )  rangeLength++;

  values = new int [ rangeLength ];

  // Initialize the histogram range values.
  for ( int i = 0; i < rangeLength; i++ )
    values [ i ] = 0;
}  /* method Setup */


/******************************************************************************/
public int getNumberOfRanges ()
{
  return rangeLength;
}  /* method getNumberOfRanges */


/******************************************************************************/
public Histogram ( String histogram_name, int bin, int maximum )
{
  reset ( histogram_name, bin, maximum );
}  /* constructor Histogram */


/******************************************************************************/
public Histogram ( String histogram_name, int bin, int maximum, 
    int goodMinimum )
{
  reset ( histogram_name, bin, maximum );

  good_minimum = goodMinimum;
}  /* constructor Histogram */


/******************************************************************************/
public Histogram ( String histogram_name, int bin, int maximum, 
    int goodMinimum, int goodMaximum )
{
  reset ( histogram_name, bin, maximum );

  good_minimum = goodMinimum;
  good_maximum = goodMaximum;
}  /* constructor Histogram */


/******************************************************************************/
private void Init ()
{
  good_maximum = 0;
  good_minimum = 0;
  maxRange    = 0;
  name        = "";
  num_good_values = 0;
  num_values  = 0;
  range       = 0;
  rangeLength = 0;
  total       = 0.0f;
  total_good  = 0;
}  /* method Init */


/******************************************************************************/
/* This constructor sets up the ParseName Object from a Phred/Phrap sequence name. */
public void addValue ( int value )
{
  num_values++;

  /* Check if value is below the histogram range. */
  if ( value <= 0 )
  {
    values [ 0 ]++;
    return;
  }  /* if */

  total += value;		// Sum all values > zero

  // Check for value greater than maximum for histogram range.
  if ( value > maxRange )
  {
    values [ rangeLength - 1 ]++;
    return;
  }  /* if */
  else
    // Value in histogram range.
    values [ ( (value - 1) / range ) + 1 ]++;

  // Check if value is above the good minimum.
  if ( value >= good_minimum )
  {
    num_good_values++;
    total_good += value;

    // Check if good average is set.
    if ( good_average > 0 )

      std_dev_sum += (value * value) - good_average_squared;
  }  /* if */
}  /* method addValue */


/******************************************************************************/
public int getGoodAverage ()
{
  if ( good_average == 0 )

    if ( num_good_values > 0 )

      good_average = (int) (total_good / num_good_values);

  good_average_squared = good_average * good_average;

  return good_average;
}  // method getGoodAverage


/******************************************************************************/
public String [] getRangeNames ()
{
  String [ ] names = new String [ rangeLength ];

  for ( int i = 0; i <= rangeLength - 1; i++ )
  {
    if ( i == 0 )
      names [ i ] = names [ i ].valueOf (i * range);
    else
      names [ i ] = names [ i ].valueOf ( ( (i * range) + ((i-1) * range) ) / 2 );
  }  /* for */

  return names;
}  /* method getRangeNames */


/******************************************************************************/
public int [] getRangeValues ()
{
  return values;
}  /* method getRangeValues */


/******************************************************************************/
public float getStandardDeviation ()
{
  float std_dev = 0.0f;

  float s2 = 0.0f;

  if ( num_good_values > 0 )
  {
    s2 = std_dev_sum / num_good_values;

    std_dev = s2;	// ### square_root of s2 !!!
  }  // if

  return std_dev;
}  // getStandardDeviation


/******************************************************************************/
public void setGoodAverage ( int value )
{
  good_average = value;

  good_average_squared = value * value;
}  // method setGoodAverage


/******************************************************************************/
/* This method prints out the histogram data. */
public void printHistogram ()
{
  Format format = new Format ();

  System.out.println ();
  System.out.println ( "Histogram " + name + " lengths" );
  System.out.println ();

  System.out.print ( "    <=    0: \t" );
  format.intWidth ( values [ 0 ], 5 );
  System.out.println ();

  for ( int i = 1; i < rangeLength - 1; i++ )
  {
    format.intWidth ( (1 + (i - 1) * range), 5 );
    System.out.print ( "-" );
    format.intWidth ( (i * range), 5 );
    System.out.print ( ": \t" );
    format.intWidth ( values [ i ], 5 );
    System.out.println ();
  }  /* for */

  System.out.print ( "    >=" );
  format.intWidth ( maxRange, 5 );
  System.out.print ( ": \t" );
  format.intWidth ( values [ rangeLength - 1 ], 5 );
  System.out.println ();

  System.out.println ();
  System.out.println ( "Total: \t\t" + (int) total );  
  System.out.println ( "Number: \t" + num_values );

  if ( num_values > 0 )
    System.out.println ( "Average: \t" + (int) (total / num_values) );

  // Check if a good minimum was specified. 
  if ( good_minimum > 0 )
  {
    System.out.println ();
    System.out.println ( "Minimum good value: \t" + good_minimum );
    System.out.println ( "Good Total: \t\t" + total_good );  
    System.out.println ( "Good Number: \t\t" + num_good_values );

    if ( num_good_values > 0 )
      System.out.println ( "Good Average: \t\t" + (int) (total_good / num_good_values) );
  }  /* if */
}  /* method printHistogram */


/******************************************************************************/
/* This method prints out the histogram data. */
public void printHistogram ( PrintStream html_data )
{
  double cummulative = num_values;
  double percent = 100;
  Format format = new Format ();

  if ( num_values <= 0 )  return;

  html_data.println ();
  html_data.println ( "<H1>Histogram " + name + " lengths</H1>" );
  html_data.println ();
  html_data.println ( "   Range\tNumber\tPercent\tCummulative" );

  html_data.print ( "   <=   0: \t" );
  format.intWidth ( html_data, values [ 0 ], 5 );
  html_data.print ( "\t" );
  percent = (values [ 0 ] * 100) / num_values;
  format.intWidth ( html_data, (int) percent, 3 );
  html_data.print ( "%\t" );
  percent = (cummulative * 100) / num_values;
  cummulative -= values [ 0 ];
  format.intWidth ( html_data, (int) percent, 3 );
  html_data.println ( "%" );

  for ( int i = 1; i < rangeLength - 1; i++ )
  {
    format.intWidth ( html_data, (1 + (i - 1) * range), 5 );
    html_data.print ( "-" );
    format.intWidth ( html_data, (i * range), 5 );
    html_data.print ( ": \t" );
    format.intWidth ( html_data, values [ i ], 5 );
    html_data.print ( "\t" );
    percent = (values [ i ] * 100) / num_values;
    format.intWidth ( html_data, (int) percent, 3 );
    html_data.print ( "%\t" );
    percent = (cummulative * 100) / num_values;
    cummulative -= values [ i ];
    format.intWidth ( html_data, (int) percent, 3 );
    html_data.println ( "%" );
  }  /* for */

  html_data.print ( "    >" );
  format.intWidth ( html_data, maxRange, 5 );
  html_data.print ( ": \t" );
  format.intWidth ( html_data, values [ rangeLength - 1 ], 5 );
  html_data.print ( "\t" );
  percent = (values [ rangeLength - 1 ] * 100) / num_values;
  format.intWidth ( html_data, (int) percent, 3 );
  html_data.print ( "%\t" );
  percent = (cummulative * 100) / num_values;
  cummulative -= values [ rangeLength - 1 ];
  format.intWidth ( html_data, (int) percent, 3 );
  html_data.println ( "%" );

  html_data.println ();
  html_data.print ( "Total: \t\t" );
  format.intWidth ( html_data, (int) total, 8 ); 
  html_data.println ();
  html_data.print ( "Number: \t" );
  format.intWidth ( html_data, num_values, 8 );
  html_data.println ();

  if ( num_values > 0 )
  {
    html_data.print ( "Average: \t" );
    format.intWidth ( html_data, (int) (total / num_values), 8 );
    html_data.println ();
  }  // if 

  // Check if a good minimum was specified. 
  if ( good_minimum > 0 )
  {
    html_data.println ();
    html_data.print ( "Minimum good value: \t" );
    format.intWidth ( html_data, good_minimum, 8 );
    html_data.println ();

    html_data.print ( "Good Total: \t\t" );
    format.longWidth ( html_data, total_good, 8 );
    html_data.println ();

    html_data.print ( "Good Number: \t\t" );
    format.intWidth ( html_data, num_good_values, 8 );
    html_data.println ();

    if ( num_good_values > 0 )
    {
      html_data.print ( "Good Average: \t\t" );
      format.intWidth ( html_data, (int) (total_good / num_good_values), 8 );
      html_data.println ();
    }  // if 
  }  /* if */
}  /* method printHistogram */


/******************************************************************************/
/* This method returns the number of values added to the histogram. */
public int numberOfValues ()
{
  return num_values;
}  /* method numberOfValues */


/******************************************************************************/
public void shortSummary ()
{
  System.out.print ( "0: " + values [ 0 ] );

  for ( int i = 1; i <= 3; i++ )
  {
    System.out.print ( " \t" + (1 + (i - 1) * range) + "-" + (i * range) + ": " +
      values [ i ] );
  }  /* for */

  System.out.print ( "\tAverage: " );

  if ( num_values == 0 )
    System.out.print ( 0 );
  else
    System.out.print (  ((int) (total / num_values)) );

  System.out.println ( "\tCount: " + num_values );
}  /* method shortSummary */


}  /* class Histogram */
