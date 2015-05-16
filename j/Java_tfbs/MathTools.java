
import java.lang.Math;

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

public class MathTools extends Object
{


/******************************************************************************/

  private static  double  average = 0.0d;			// array average

  private static  double  standard_deviation = 0.0d;		// standard deviation


/******************************************************************************/
  // Constructor MathTools
  public MathTools ()
  {
    initialize ();
  }  // constructor MathTools


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    average = 0.0d;
    standard_deviation = 0.0d;
  }  // method initialize 


/******************************************************************************/
public double getAverage ()
{
  return average;
}  // method getAverage


/******************************************************************************/
public static double computeAverage ( float [] float_array )
{
  average = 0.0d;
  double sum = 0.0d;		// cumulative sum of values

  // Assert: numbers in float_array
  if ( float_array == null )  return average;
  if ( float_array.length <= 0 )  return average;

  for ( int i = 0; i < float_array.length; i++ )
    sum += float_array [ i ];

  average = sum / (float_array.length * 1.0d);

  return average;
}  // method computeAverage


/******************************************************************************/
public static double computeAverage 
    ( float [] float_array
    , int      lower_range
    , int      upper_range 
    )
{
  average = 0.0d;
  double count = 0.0d;		// number of values within range
  double sum = 0.0d;		// cumulative sum of values

  // Assert: numbers in float_array
  if ( float_array == null )  return average;
  if ( float_array.length <= 0 )  return average;

  for ( int i = 0; i < float_array.length; i++ )

    if ( ( float_array [ i ] >= lower_range ) && 
         ( float_array [ i ] <= upper_range ) )
    {
      count++;
      sum += float_array [ i ];
    }  // if

  average = sum / count;

  return average;
}  // method computeAverage


/******************************************************************************/
public static double computeBestAverage ( float [] float_array )
{
  computeStandardDeviation ( float_array );

  // Check for reasonable standard deviation.
  if ( standard_deviation <= 1.0f )  return average;

  // Check the ratio of the standard deviation to the average.
  if ( ( standard_deviation / average ) <= 0.5d )  return average;

  double count = 0.0d;
  double sum = 0.0d;
  for ( int i = 0; i < float_array.length; i++ )

    if ( float_array [ i ] < average + standard_deviation )
    {
      sum += float_array [ i ];
      count++;
    }  // if

  average = sum / count;
  return average;
}  // method computeBestAverage


/******************************************************************************/
public static double computeStandardDeviation ( float [] float_array )
{
  double count = 0.0d;		// number of values within range
  double sum = 0.0d;

  // Assert: numbers in float_array
  if ( float_array == null )  return standard_deviation;
  if ( float_array.length <= 0 )  return standard_deviation;

  // Compute the average.
  average = computeAverage ( float_array );
  double ave2 = average * average;

  for ( int i = 0; i < float_array.length; i++ )
  {
    count++;
    sum += float_array [ i ] * float_array [ i ] - ave2;
  }  // if

  standard_deviation = java.lang.Math.sqrt ( sum / count );

  return standard_deviation;
}  // method computeStandardDeviation


/******************************************************************************/
public static double computeStandardDeviation 
    ( float [] float_array
    , int lower_range
    , int upper_range 
    )
{
  double count = 0.0d;		// number of values within range
  double sum = 0.0d;

  // Assert: numbers in float_array
  if ( float_array == null )  return standard_deviation;
  if ( float_array.length <= 0 )  return standard_deviation;

  // Compute the average.
  average = computeAverage ( float_array, lower_range, upper_range );
  double ave2 = average * average;

  for ( int i = 0; i < float_array.length; i++ )

    if ( ( float_array [ i ] >= lower_range ) && 
         ( float_array [ i ] <= upper_range ) )
    {
      count++;
      sum += float_array [ i ] * float_array [ i ] - ave2;
    }  // if

  standard_deviation = java.lang.Math.sqrt ( sum / count );

  return standard_deviation;
}  // method computeStandardDeviation


/******************************************************************************/
public static void main ( String [] args )
{
  float [] test = new float [ 3 ];
  test [ 0 ] = 1.0f;
  test [ 1 ] = 21.2f;
  test [ 2 ] = 20.0f;

  System.out.println ( "Average: " + computeAverage ( test ) );
  System.out.println ( "Standard Deviation: " + computeStandardDeviation ( test ) );
}  // method main


/******************************************************************************/

}  // class MathTools
