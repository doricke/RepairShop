
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

public class IntArray extends Object
{


/******************************************************************************/

  private   int  average = 0;			// integer array average

  private   int [] int_array;			// integer array

  private   float  scale_factor = 1.0f;		// scale factor

  private   float  standard_deviation = 0.0f;	// standard deviation


/******************************************************************************/
  // Constructor IntArray
  public IntArray ()
  {
    initialize ();
  }  // constructor IntArray


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    average = 0;
    scale_factor = 1.0f;
    standard_deviation = 0.0f;

    if ( int_array != null )

      if ( int_array.length > 0 )
      {
        for ( int i = 0; i < int_array.length; i++ )
        {  
          int_array [ i ] = 0;
        }  // for
      }  // if
  }  // method initialize 


/******************************************************************************/
public int getAverage ()
{
  return average;
}  // method getAverage


/******************************************************************************/
public int getAverage ( int lower_range, int upper_range )
{
  // Check if the average has been computed.
  if ( average == 0 )  computeAverage ( lower_range, upper_range );

  return average;
}  // method getAverage


/******************************************************************************/
public float getScaleFactor ()
{
  return scale_factor;
}  // method getScaleFactor


/******************************************************************************/
public float getStandardDeviation ()
{
  // Check if standard deviation calculated.
  if ( standard_deviation == 0.0f )  computeStandardDeviation ();

  return standard_deviation;
}  // method getStandardDeviation


/******************************************************************************/
public float getStandardDeviation ( int lower_range, int upper_range )
{
  // Check if standard deviation calculated.
  if ( standard_deviation == 0.0f )  
    computeStandardDeviation ( lower_range, upper_range );

  return standard_deviation;
}  // method getStandardDeviation


/******************************************************************************/
  public int [] getIntArray ()
  {
    return int_array;
  }  // method getIntArray


/******************************************************************************/
  public int getLength ()
  {
    return getSize ();
  }  // method getLength


/******************************************************************************/
  public float getNormalized ( int index )
  {
    if ( int_array == null )  return 0.0f;
    if ( ( index < 0 ) || ( index >= int_array.length ) )  return 0.0f;

    // Assert: average > 0
    if ( average == 0 )  computeAverage ();
    if ( average == 0 )  return 0.0f;

    return ((int_array [ index ] + 0.5f) / (average * 1.0f));
  }  // method getNormalized


/******************************************************************************/
  public int getNormalizedValue ( int index )
  {
    if ( int_array == null )  return 0;
    if ( ( index < 0 ) || ( index >= int_array.length ) )  return 0;

    // Assert: average > 0
    if ( average == 0 )  computeAverage ();
    if ( average == 0 )  return 0;

    return (int) ((int_array [ index ] + 0.5f) / (average * 1.0f));
  }  // method getNormalizedValue


/******************************************************************************/
  public int getScaledValue ( int index )
  {
    if ( int_array == null )  return 0;
    if ( ( index < 0 ) || ( index >= int_array.length ) )  return 0;

    return (int) ((int_array [ index ] * 1.0f) * scale_factor);
  }  // method getScaledValue


/******************************************************************************/
  public int getSize ()
  {
    if ( int_array == null )  return 0;

    return int_array.length;
  }  // method getSize


/******************************************************************************/
  public int getValue ( int index )
  {
    if ( int_array == null )  return 0;
    if ( ( index < 0 ) || ( index >= int_array.length ) )  return 0;

    return int_array [ index ];
  }  // method getValue


/******************************************************************************/
  public void setIntArray ( int [] value )
  {
    int_array = value;
  }  // method setIntArray


/******************************************************************************/
  public void setLength ( int value )
  {
    setSize ( value );
  }  // method setLength


/******************************************************************************/
  public void setScaleFactor ( float value )
  {
    scale_factor = value;
  }  // method setScaleFactor


/******************************************************************************/
  public void setSize ( int value )
  {
    int_array = new int [ value ];

    initialize ();
  }  // method setSize


/******************************************************************************/
  public void setValue ( int index, int value )
  {
    if ( int_array == null )
    {
      System.out.println ( "*Warning* null int_array." );
      return;
    }  // if

    if ( ( index < 0 ) || ( index > int_array.length ) )
    {
      System.out.println ( "*Warning* Invalid index IntArray.setValue: index = " + index );
      return;
    }  // if 

    int_array [ index ] = value;
  }  // method setValue


/******************************************************************************/
public int computeAverage ()
{
  average = 0;
  long sum = 0L;		// cumulative sum of values

  // Assert: integers in int_array
  if ( int_array == null )  return average;
  if ( int_array.length <= 0 )  return average;

  for ( int i = 0; i < int_array.length; i++ )
  {
    sum += int_array [ i ];
  }  // if

  average = (int) (sum / int_array.length);

  return average;
}  // method computeAverage


/******************************************************************************/
public int computeAverage ( int lower_range, int upper_range )
{
  average = 0;
  long count = 0L;		// number of values within range
  long sum = 0L;		// cumulative sum of values

  // Assert: integers in int_array
  if ( int_array == null )  return average;
  if ( int_array.length <= 0 )  return average;

  for ( int i = 0; i < int_array.length; i++ )

    if ( ( int_array [ i ] >= lower_range ) && 
         ( int_array [ i ] <= upper_range ) )
    {
      count++;
      sum += int_array [ i ];
    }  // if

  average = (int) (sum / count);

  return average;
}  // method computeAverage


/******************************************************************************/
public int computeBestAverage ()
{
  int count = 0;		// number of numbers summed
  long sum = 0L;		// cumulative sum of values

  // Assert: integers in int_array
  if ( int_array == null )  return average;
  if ( int_array.length <= 0 )  return average;

  for ( int i = 0; i < int_array.length; i++ )
  {
    if ( ( average - standard_deviation < int_array [ i ] ) &&
         ( average + standard_deviation > int_array [ i ] ) )
    {
      sum += int_array [ i ];
      count++;
    }  // if
  }  // if

  if ( count > 0 )  average = (int) (sum / count);

  return average;
}  // method computeBestAverage


/******************************************************************************/
public float computeStandardDeviation ()
{
  float count = 0.0f;		// number of values within range
  double sum = 0.0d;

  // Assert: integers in int_array
  if ( int_array == null )  return standard_deviation;
  if ( int_array.length <= 0 )  return standard_deviation;

  // Compute the average.
  long ave2 = average * average;

  for ( int i = 0; i < int_array.length; i++ )
  {
    count++;
    sum += int_array [ i ] * int_array [ i ] - ave2;
  }  // if

  standard_deviation = (float) java.lang.Math.sqrt ( sum / count );

  return standard_deviation;
}  // method computeStandardDeviation


/******************************************************************************/
public float computeStandardDeviation ( int lower_range, int upper_range )
{
  float count = 0.0f;		// number of values within range
  double sum = 0.0d;

  // Compute the average.
  int ave = getAverage ( lower_range, upper_range );
  long ave2 = ave * ave;

  // Assert: integers in int_array
  if ( int_array == null )  return standard_deviation;
  if ( int_array.length <= 0 )  return standard_deviation;

  for ( int i = 0; i < int_array.length; i++ )

    if ( ( int_array [ i ] >= lower_range ) && 
         ( int_array [ i ] <= upper_range ) )
    {
      count++;
      sum += int_array [ i ] * int_array [ i ] - ave2;
    }  // if

  standard_deviation = (float) java.lang.Math.sqrt ( sum / count );

  return standard_deviation;
}  // method computeStandardDeviation


/******************************************************************************/
public String toString ()
{
  StringBuffer str = new StringBuffer ();

  for ( int i = 0; i < int_array.length; i++ )

    str.append ( int_array [ i ] + "\t" );

  str.append ( ((int) standard_deviation) + "" );

  return str.toString ();
}  // method toString


/******************************************************************************/

}  // class IntArray
