

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

public class Replicates extends Object
{


/******************************************************************************/

  private   int  average = 0;			// data points average

  private   int []  data_points;		// data points

  private   boolean []  outlier;		// outlier flag for each data point

  private   float  standard_deviation = 0.0f;	// standard deviation


/******************************************************************************/
  // Constructor Replicates
  public Replicates ()
  {
    initialize ();
  }  // constructor Replicates


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    average = 0;
    standard_deviation = 0.0f;

    // data_points = 0;
    // outlier = false;
  }  // method initialize 


/******************************************************************************/
  public int getAverage ()
  {
    return average;
  }  // method getAverage


/******************************************************************************/
  public int [] getDataPoints ()
  {
    return data_points;
  }  // method getDataPoints


/******************************************************************************/
  public boolean getOutlier ( int index )
  {
    if ( outlier == null )  return true;
    if ( index >= outlier.length )  return true;

    return outlier [ index ];
  }  // method getOutlier


/******************************************************************************/
  public float getStandardDeviation ()
  {
    return standard_deviation;
  }  // method getStandardDeviation


/******************************************************************************/
  public boolean isOutlier ( int index )
  {
    if ( outlier == null )  return true;
    if ( index >= outlier.length )  return true;

    return outlier [ index ];
  }  // method isOutlier


/******************************************************************************/
  public void setAverage ( int value )
  {
    average = value;
  }  // method setAverage


/******************************************************************************/
  public void setDataPoints ( int [] value )
  {
    data_points = value;
  }  // method setDataPoints


/******************************************************************************/
  public void setOutlier ( boolean value, int index )
  {
    if ( outlier == null )  return;
    if ( index >= outlier.length )  return;

    outlier [ index ] = value;
  }  // method setOutlier


/******************************************************************************/
  public void setStandardDeviation ( float value )
  {
    standard_deviation = value;
  }  // method setStandardDeviation


/******************************************************************************/

}  // class Replicates
