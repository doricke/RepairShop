
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

public class FloatArray extends Object
{


/******************************************************************************/

  private   float  average = 0.0f;			// array average

  private   float [] float_array;			// float array

  private   float  scale_factor = 1.0f;			// scale factor

  private   float  standard_deviation = 0.0f;		// standard deviation


/******************************************************************************/
  // Constructor FloatArray
  public FloatArray ()
  {
    initialize ();
  }  // constructor FloatArray


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    average = 0.0f;
    scale_factor = 1.0f;
    standard_deviation = 0.0f;

    if ( float_array != null )

      if ( float_array.length > 0 )
      {
        for ( int i = 0; i < float_array.length; i++ )
        {  
          float_array [ i ] = 0.0f;
        }  // for
      }  // if
  }  // method initialize 


/******************************************************************************/
public float getAverage ()
{
  return average;
}  // method getAverage


/******************************************************************************/
public float getAverage ( int lower_range, int upper_range )
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
public float getStandardDeviation ( int lower_range, int upper_range )
{
  // Check if standard deviation calculated.
  if ( standard_deviation == 0.0f )  
    computeStandardDeviation ( lower_range, upper_range );

  return standard_deviation;
}  // method getStandardDeviation


/******************************************************************************/
  public float [] getFloatArray ()
  {
    return float_array;
  }  // method getFloatArray


/******************************************************************************/
  public int getLength ()
  {
    return getSize ();
  }  // method getLength


/******************************************************************************/
  public float getNormalizedValue ( int index )
  {
    if ( float_array == null )  return 0.0f;
    if ( ( index < 0 ) || ( index >= float_array.length ) )  return 0.0f;

    // Assert: average > 0
    if ( average == 0 )  computeAverage ();
    if ( average == 0 )  return 0.0f;

    return (float) ((float_array [ index ] + 0.5f) / (average * 1.0f));
  }  // method getNormalizedValue


/******************************************************************************/
  public float getScaledValue ( int index )
  {
    if ( float_array == null )  return 0.0f;
    if ( ( index < 0 ) || ( index >= float_array.length ) )  return 0.0f;

    return (float) (float_array [ index ] * scale_factor);
  }  // method getScaledValue


/******************************************************************************/
  public int getSize ()
  {
    if ( float_array == null )  return 0;

    return float_array.length;
  }  // method getSize


/******************************************************************************/
  public float getValue ( int index )
  {
    if ( float_array == null )  return 0.0f;
    if ( ( index < 0 ) || ( index >= float_array.length ) )  return 0.0f;

    return float_array [ index ];
  }  // method getValue


/******************************************************************************/
  public void setFloatArray ( float [] value )
  {
    float_array = value;
  }  // method setFloatArray


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
    float_array = new float [ value ];

    initialize ();
  }  // method setSize


/******************************************************************************/
  public void setValue ( int index, float value )
  {
    if ( float_array == null )
    {
      System.out.println ( "*Warning* null float_array." );
      return;
    }  // if

    if ( ( index < 0 ) || ( index > float_array.length ) )
    {
      System.out.println ( "*Warning* Invalid index FloatArray.setValue: index = " + index );
      return;
    }  // if 

    float_array [ index ] = value;
  }  // method setValue


/******************************************************************************/
  public void setValue ( int index, float value, float minimum )
  {
    if ( float_array == null )
    {
      System.out.println ( "*Warning* null float_array." );
      return;
    }  // if

    if ( ( index < 0 ) || ( index > float_array.length ) )
    {
      System.out.println ( "*Warning* Invalid index FloatArray.setValue: index = " + index );
      return;
    }  // if 

    if ( value < minimum )  float_array [ index ] = minimum;
    else
      float_array [ index ] = value;
  }  // method setValue


/******************************************************************************/
public float computeAverage ()
{
  average = 0.0f;
  double sum = 0.0d;		// cumulative sum of values

  // Assert: integers in float_array
  if ( float_array == null )  return average;
  if ( float_array.length <= 0 )  return average;

  for ( int i = 0; i < float_array.length; i++ )
  {
    sum += float_array [ i ];
  }  // if

  average = (float) (sum / (float_array.length * 1.0d));

  return average;
}  // method computeAverage


/******************************************************************************/
public float computeAverage ( int lower_range, int upper_range )
{
  average = 0.0f;
  double count = 0.0f;		// number of values within range
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

  average = (float) (sum / count);

  return average;
}  // method computeAverage


/******************************************************************************/
public float computeStandardDeviation ( int lower_range, int upper_range )
{
  double count = 0.0f;		// number of values within range
  double sum = 0.0d;

  // Compute the average.
  double ave = getAverage ( lower_range, upper_range );
  double ave2 = ave * ave;

  // Assert: numbers in float_array
  if ( float_array == null )  return standard_deviation;
  if ( float_array.length <= 0 )  return standard_deviation;

  for ( int i = 0; i < float_array.length; i++ )

    if ( ( float_array [ i ] >= lower_range ) && 
         ( float_array [ i ] <= upper_range ) )
    {
      count++;
      sum += float_array [ i ] * float_array [ i ] - ave2;
    }  // if

  standard_deviation = (float) java.lang.Math.sqrt ( sum / count );

  return standard_deviation;
}  // method computeStandardDeviation


/******************************************************************************/

}  // class FloatArray
