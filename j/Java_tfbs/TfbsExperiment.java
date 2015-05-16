
// import FloatArray;
// import TfbsArray;
// import TfbsRead;
// import TfbsRecord;

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

public class TfbsExperiment extends Object
{


/******************************************************************************/

  private static final int ARRAY_SIZE = 374792;


/******************************************************************************/

  private   int  chips = 0;			// number of chip experiments

  private   String []  file_name;		// Chip file names

  private   FloatArray []  float_arrays;	// value arrays

  private   float []  range_average;		// range average (low, high)

  private   float []  scale_factor;		// Chip scale factor

  private   float []  standard_deviation;	// range standard deviation


/******************************************************************************/
  // Constructor TfbsExperiment
  public TfbsExperiment ()
  {
    initialize ();
  }  // constructor TfbsExperiment


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    if ( chips <= 0 )  return;

    for ( int i = 0; i < chips; i++ )
    {
      file_name [ i ] = "";
      range_average [ i ] = 0;
      scale_factor [ i ] = 0.0f;
      standard_deviation [ i ] = 0;

      float_arrays [ i ].setSize ( ARRAY_SIZE );
    }  // for
  }  // method initialize 


/******************************************************************************/
  public int getChips ()
  {
    return chips;
  }  // method getChips


/******************************************************************************/
  public String getFileName ( int index )
  {
    if ( index < chips )  return file_name [ index ];

    return "";
  }  // method getFileName


/******************************************************************************/
  public String [] getFileNames ()
  {
    return file_name;
  }  // method getFileNames


/******************************************************************************/
  public FloatArray getFloatArray ( int index )
  {
    if ( float_arrays == null )  return null;
    if ( ( index < 0 ) || ( index > float_arrays.length ) )  return null;

    return float_arrays [ index ];
  }  // method getFloatArray


/******************************************************************************/
  public FloatArray [] getFloatArrays ()
  {
    return float_arrays;
  }  // method getFloatArrays


/******************************************************************************/
  public float getRangeAverage ( int index )
  {
    if ( index < chips )  return range_average [ index ];

    return 0.0f;
  }  // method getRangeAverage


/******************************************************************************/
  public float getScaleFactor ( int index )
  {
    if ( index < chips )  return scale_factor [ index ];

    return 0.0f;
  }  // method getScaleFactor


/******************************************************************************/
  public float getStandardDeviation ( int index )
  {
    if ( index < chips )  return standard_deviation [ index ];

    return 0.0f;
  }  // method getStandardDeviation


/******************************************************************************/
  public void setChips ( int value )
  {
    chips = value;

    file_name = new String [ chips ];
    range_average = new float [ chips ];
    scale_factor = new float [ chips ];
    standard_deviation = new float [ chips ];
   
    float_arrays = new FloatArray [ chips ];
    for ( int i = 0; i < chips; i++ )
      float_arrays [ i ] = new FloatArray ();
 
    initialize ();
  }  // method setChips


/******************************************************************************/
  public void setFileName ( String value, int index )
  {
    if ( index < chips )  file_name [ index ] = value;
  }  // method setFileName


/******************************************************************************/
  public void setFloatArray ( FloatArray value, int index )
  {
    if ( float_arrays == null )  return;
    if ( ( index < 0 ) || ( index >= chips ) )  return;

    float_arrays [ index ] = value;
  }  // method setFloatArray


/******************************************************************************/
  public void setRangeAverage ( float value, int index )
  {
    if ( index < chips )  range_average [ index ] = value;
  }  // method setRangeAverage


/******************************************************************************/
  public void setScaleFactor ( float value, int index )
  {
    if ( index < chips )  scale_factor [ index ] = value;
  }  // method setScaleFactor


/******************************************************************************/
  public void setStandardDeviation ( float value, int index )
  {
    if ( index < chips )  standard_deviation [ index ] = value;
  }  // method setStandardDeviation


/******************************************************************************/
  public void snap ()
  {
    if ( chips <= 0 )  return;

    // System.out.println ();
    // System.out.println ( "Number of chip experiments: " + chips );
    // System.out.println ();
    // System.out.println ( "Filename\trange average\tstd. dev.\tscale factor" );

/*
    for ( int i = 0; i < chips; i++ )
    {
      System.out.print ( file_name [ i ] + "\t" );
      System.out.print ( range_average [ i ] + "\t" );
      System.out.print ( standard_deviation [ i ] + "\t" );
      System.out.print ( scale_factor [ i ] );
      System.out.println ();
    }  // for
    System.out.println ();
*/
    // Read in the first experiment (again).
    TfbsRead tfbs_data = new TfbsRead ();
    tfbs_data.processFile ( file_name [ 0 ] );
    TfbsArray tfbs_array = tfbs_data.getTfbsArray ();

    // Print out the data column headers.
    System.out.print ( "chromosome\tposition\t" );
    for ( int j = 0; j < chips; j++ )
    {
      System.out.print ( file_name [ j ] );
      if ( j < chips - 1 )  System.out.print ( "\t" );
    }  // for
    System.out.println ();

    TfbsRecord tfbs_record; 
    for ( int i = 0; i < tfbs_array.getLength (); i++ )
    {
      tfbs_record = tfbs_array.getTfbsRecord ( i );
      if ( tfbs_record != null )
      {
        System.out.print ( tfbs_record.getChromosome () + "\t" );
        System.out.print ( tfbs_record.getPosition () + "\t" );
      }  // if
      else
        System.out.print ( "null\tnull\t" );

      for ( int j = 0; j < chips; j++ )
      {
        System.out.print ( float_arrays [ j ].getNormalizedValue ( i ) );
        if ( j < chips - 1 )  System.out.print ( "\t" );
        else  System.out.println ();
      }  // for
    }  // for
  }  // method snap


/******************************************************************************/
  public void snapAverages ()
  {
    if ( chips <= 0 )  return;

    // Read in the first experiment (again).
    TfbsRead tfbs_data = new TfbsRead ();
    tfbs_data.processFile ( file_name [ 0 ] );
    TfbsArray tfbs_array = tfbs_data.getTfbsArray ();

    // Print out the data column headers.
    System.out.print ( "chromosome\tposition\t" );
    for ( int j = 0; j < chips; j += 3 )
    {
      System.out.print ( file_name [ j ] );
      if ( j < chips - 1 )  System.out.print ( "\t" );
    }  // for
    System.out.println ();

    TfbsRecord tfbs_record; 
    float [] f3 = new float [ 3 ];
    for ( int i = 0; i < tfbs_array.getLength (); i++ )
    {
      tfbs_record = tfbs_array.getTfbsRecord ( i );
      if ( tfbs_record != null )
      {
        System.out.print ( tfbs_record.getChromosome () + "\t" );
        System.out.print ( tfbs_record.getPosition () + "\t" );
      }  // if
      else
        System.out.print ( "null\tnull\t" );

      for ( int j = 0; j < chips; j += 3 )
      {
        f3 [ 0 ] = float_arrays [ j ].getNormalizedValue ( i );
        f3 [ 1 ] = float_arrays [ j + 1 ].getNormalizedValue ( i );
        f3 [ 2 ] = float_arrays [ j + 2 ].getNormalizedValue ( i );

        System.out.print ( MathTools.computeBestAverage ( f3 ) );
        if ( j < chips - 2 )  System.out.print ( "\t" );
      }  // for

      System.out.println ();
    }  // for
  }  // method snapAverages


/******************************************************************************/

}  // class TfbsExperiment

