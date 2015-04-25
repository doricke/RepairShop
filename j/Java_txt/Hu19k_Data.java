
import java.util.*;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

public class Hu19k_Data extends Object
{


/******************************************************************************/

  private   int  autoflag = 0;		// Autoflag

  private   int  b_pixels = 0;		// background pixels

  private   int  b532 = 0;		// B532

  private   int  b532_1sd = 0;		// Percent of pixels > 1 standard deviation

  private   int  b532_2sd = 0;		// Percent of pixels > 2 standard deviations

  private   int  b532_cv = 0;		// B532 CV

  private   int  b532_mean = 0;		// B532 mean

  private   int  b532_median = 0;	// B532 median

  private   int  b532_sd = 0;		// B532 standard deviation

  private   int  b635 = 0;		// B635

  private   int  b635_1sd = 0;		// Percent of pixels > 1 standard deviation

  private   int  b635_2sd = 0;		// Percent of pixels > 2 standard deviations

  private   int  b635_cv = 0;		// B635 CV

  private   int  b635_mean = 0;		// mean feature background intensity

  private   int  b635_median = 0;	// median feature background intensity

  private   int  b635_sd = 0;		// B635 standard deviation

  private   int  block = 0;		// block number

  private   int  circularity = 0;	// circularity

  private   int  column = 0;		// column 

  private   int  diameter = 0;		// diameter in micrometers

  private   int  f_pixels = 0;		// feature pixels

  private   int  f532_cv = 0;		// F532 CV

  private   int  f532_mean = 0;		// F532 mean feature pixel intensity

  private   float  f532_mean_b532 = 0.0f;	// F532 mean - B532 median

  private   int  f532_median = 0;	// F532 median feature pixel intensity at wavelength 532

  private   int  f532_median_b532 = 0;	// F532 median - B532 median

  private   int  f532_sat = 0;		// F532 percent pixels saturated

  private   int  f532_sd = 0;		// F532 standard deviation

  private   int  f532_total = 0;	// F532 total intensity

  private   int  f635_cv = 0;		// F635 CV

  private   int  f635_mean = 0;		// F635 mean feature pixel intensity

  private   float  f635_mean_b635 = 0.0f;	// F635 mean - B635 median

  private   int  f635_median = 0;	// F635 median feature pixel intensity

  private   int  f635_median_b635 = 0;	// F635 median - B635 median

  private   int  f635_sd = 0;		// F635 standard deviation

  private   int  f635_sat = 0;		// Percentage of feature pixels saturated

  private   int  f635_total = 0;	// F635 total intensity

  private   int  flags = 0;		// Flags

  private   String  identifier = "";	// ID - unique identifier

  private   String  log_ratio = "";	// Log2 of ratio of the medians

  private   String  p_value = "";	// p-value

  private   String  mean_of_ratios = "";	// geometrix mean of pixel-by-pixel ratios

  private   String  median_of_ratios = "";	// median of pixel-by-pixel ratios

  private   String  name = "";		// feature name

  private   int  normalize = 0;		// Normalize

  private   float  ratio_of_means = 0.0f;	// ratio of arithmetic mean intensities - median background

  private   String  ratio_of_medians = "";	// ratio of arithmetic mean intensities - median background

  private   String  ratios_sd = "";	// geometrix standard deviation

  private   String  refseq = "";	// Refseq

  private   String  rgn_r2 = "";	// coefficient of determination for the current regression

  private   String  rgn_ratio = "";	// regression ratio

  private   int  row = 0;		// row

  private   String  snr_532 = "";	// SNR 532 - signal-to-noise ratio

  private   String  snr_635 = "";	// SNR 635 - signal-to-noise ratio

  private   int  sum_of_means = 0;	// sum of means - background

  private   int  sum_of_medians = 0;	// sum of medians - background

  private   int  x = 0;			// X coordinate

  private   int  y = 0;			// Y coordinate


/******************************************************************************/
  // Constructor Hu19k_Data
  public Hu19k_Data ()
  {
    initialize ();
  }  // constructor Hu19k_Data


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    autoflag = 0;
    b_pixels = 0;
    b532 = 0;
    b532_1sd = 0;
    b532_2sd = 0;
    b532_cv = 0;
    b532_mean = 0;
    b532_median = 0;
    b532_sd = 0;
    b635 = 0;
    b635_1sd = 0;
    b635_2sd = 0;
    b635_cv = 0;
    b635_mean = 0;
    b635_median = 0;
    b635_sd = 0;
    block = 0;
    circularity = 0;
    column = 0;
    diameter = 0;
    f_pixels = 0;
    f532_cv = 0;
    f532_mean = 0;
    f532_mean_b532 = 0.0f;
    f532_median = 0;
    f532_median_b532 = 0;
    f532_sat = 0;
    f532_sd = 0;
    f532_total = 0;
    f635_cv = 0;
    f635_mean = 0;
    f635_mean_b635 = 0.0f;
    f635_median = 0;
    f635_median_b635 = 0;
    f635_sd = 0;
    f635_sat = 0;
    f635_total = 0;
    flags = 0;
    identifier = "";
    log_ratio = "";
    mean_of_ratios = "";
    median_of_ratios = "";
    name = "";
    normalize = 0;
    ratio_of_means = 0.0f;
    ratio_of_medians = "";
    ratios_sd = "";
    rgn_r2 = "";
    rgn_ratio = "";
    row = 0;
    snr_532 = "";
    snr_635 = "";
    sum_of_means = 0;
    sum_of_medians = 0;
    x = 0;
    y = 0;
  }  // method initialize 


/******************************************************************************/
  public int getAutoflag ()
  {
    return autoflag;
  }  // method getAutoflag


/******************************************************************************/
  public int getBPixels ()
  {
    return b_pixels;
  }  // method getBPixels


/******************************************************************************/
  public int getB532 ()
  {
    return b532;
  }  // method getB532


/******************************************************************************/
  public int getB5321sd ()
  {
    return b532_1sd;
  }  // method getB5321sd


/******************************************************************************/
  public int getB5322sd ()
  {
    return b532_2sd;
  }  // method getB5322sd


/******************************************************************************/
  public int getB532Cv ()
  {
    return b532_cv;
  }  // method getB532Cv


/******************************************************************************/
  public int getB532Mean ()
  {
    return b532_mean;
  }  // method getB532Mean


/******************************************************************************/
  public int getB532Median ()
  {
    return b532_median;
  }  // method getB532Median


/******************************************************************************/
  public int getB532Sd ()
  {
    return b532_sd;
  }  // method getB532Sd


/******************************************************************************/
  public int getB635 ()
  {
    return b635;
  }  // method getB635


/******************************************************************************/
  public int getB6351sd ()
  {
    return b635_1sd;
  }  // method getB6351sd


/******************************************************************************/
  public int getB6352sd ()
  {
    return b635_2sd;
  }  // method getB6352sd


/******************************************************************************/
  public int getB635Cv ()
  {
    return b635_cv;
  }  // method getB635Cv


/******************************************************************************/
  public int getB635Mean ()
  {
    return b635_mean;
  }  // method getB635Mean


/******************************************************************************/
  public int getB635Median ()
  {
    return b635_median;
  }  // method getB635Median


/******************************************************************************/
  public int getB635Sd ()
  {
    return b635_sd;
  }  // method getB635Sd


/******************************************************************************/
  public int getBlock ()
  {
    return block;
  }  // method getBlock


/******************************************************************************/
  public int getCircularity ()
  {
    return circularity;
  }  // method getCircularity


/******************************************************************************/
  public int getColumn ()
  {
    return column;
  }  // method getColumn


/******************************************************************************/
  public int getDiameter ()
  {
    return diameter;
  }  // method getDiameter


/******************************************************************************/
  public int getFPixels ()
  {
    return f_pixels;
  }  // method getFPixels


/******************************************************************************/
  public int getF532Cv ()
  {
    return f532_cv;
  }  // method getF532Cv


/******************************************************************************/
  public int getF532Mean ()
  {
    return f532_mean;
  }  // method getF532Mean


/******************************************************************************/
  public float getF532MeanB532 ()
  {
    return f532_mean_b532;
  }  // method getF532MeanB532


/******************************************************************************/
  public int getF532Median ()
  {
    return f532_median;
  }  // method getF532Median


/******************************************************************************/
  public int getF532MedianB532 ()
  {
    return f532_median_b532;
  }  // method getF532MedianB532


/******************************************************************************/
  public int getF532Sat ()
  {
    return f532_sat;
  }  // method getF532Sat


/******************************************************************************/
  public int getF532Sd ()
  {
    return f532_sd;
  }  // method getF532Sd


/******************************************************************************/
  public int getF532Total ()
  {
    return f532_total;
  }  // method getF532Total


/******************************************************************************/
  public int getF635Cv ()
  {
    return f635_cv;
  }  // method getF635Cv


/******************************************************************************/
  public int getF635Mean ()
  {
    return f635_mean;
  }  // method getF635Mean


/******************************************************************************/
  public float getF635MeanB635 ()
  {
    return f635_mean_b635;
  }  // method getF635MeanB635


/******************************************************************************/
  public int getF635Median ()
  {
    return f635_median;
  }  // method getF635Median


/******************************************************************************/
  public int getF635MedianB635 ()
  {
    return f635_median_b635;
  }  // method getF635MedianB635


/******************************************************************************/
  public int getF635Sd ()
  {
    return f635_sd;
  }  // method getF635Sd


/******************************************************************************/
  public int getF635Sat ()
  {
    return f635_sat;
  }  // method getF635Sat


/******************************************************************************/
  public int getF635Total ()
  {
    return f635_total;
  }  // method getF635Total


/******************************************************************************/
  public int getFlags ()
  {
    return flags;
  }  // method getFlags


/******************************************************************************/
  public String getIdentifier ()
  {
    return identifier;
  }  // method getIdentifier


/******************************************************************************/
  public String getLogRatio ()
  {
    return log_ratio;
  }  // method getLogRatio


/******************************************************************************/
  public String getMeanOfRatios ()
  {
    return mean_of_ratios;
  }  // method getMeanOfRatios


/******************************************************************************/
  public String getMedianOfRatios ()
  {
    return median_of_ratios;
  }  // method getMedianOfRatios


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public int getNormalize ()
  {
    return normalize;
  }  // method getNormalize


/******************************************************************************/
  public float getRatioOfMeans ()
  {
    return ratio_of_means;
  }  // method getRatioOfMeans


/******************************************************************************/
  public String getRatioOfMedians ()
  {
    return ratio_of_medians;
  }  // method getRatioOfMedians


/******************************************************************************/
  public String getRatiosSd ()
  {
    return ratios_sd;
  }  // method getRatiosSd


/******************************************************************************/
  public String getRgnR2 ()
  {
    return rgn_r2;
  }  // method getRgnR2


/******************************************************************************/
  public String getRgnRatio ()
  {
    return rgn_ratio;
  }  // method getRgnRatio


/******************************************************************************/
  public int getRow ()
  {
    return row;
  }  // method getRow


/******************************************************************************/
  public String getSnr532 ()
  {
    return snr_532;
  }  // method getSnr532


/******************************************************************************/
  public String getSnr635 ()
  {
    return snr_635;
  }  // method getSnr635


/******************************************************************************/
  public int getSumOfMeans ()
  {
    return sum_of_means;
  }  // method getSumOfMeans


/******************************************************************************/
  public int getSumOfMedians ()
  {
    return sum_of_medians;
  }  // method getSumOfMedians


/******************************************************************************/
  public int getX ()
  {
    return x;
  }  // method getX


/******************************************************************************/
  public int getY ()
  {
    return y;
  }  // method getY


/******************************************************************************/
  public void setAutoflag ( int value )
  {
    autoflag = value;
  }  // method setAutoflag


/******************************************************************************/
  public void setBPixels ( int value )
  {
    b_pixels = value;
  }  // method setBPixels


/******************************************************************************/
  public void setB532 ( int value )
  {
    b532 = value;
  }  // method setB532


/******************************************************************************/
  public void setB5321sd ( int value )
  {
    b532_1sd = value;
  }  // method setB5321sd


/******************************************************************************/
  public void setB5322sd ( int value )
  {
    b532_2sd = value;
  }  // method setB5322sd


/******************************************************************************/
  public void setB532Cv ( int value )
  {
    b532_cv = value;
  }  // method setB532Cv


/******************************************************************************/
  public void setB532Mean ( int value )
  {
    b532_mean = value;
  }  // method setB532Mean


/******************************************************************************/
  public void setB532Median ( int value )
  {
    b532_median = value;
  }  // method setB532Median


/******************************************************************************/
  public void setB532Sd ( int value )
  {
    b532_sd = value;
  }  // method setB532Sd


/******************************************************************************/
  public void setB635 ( int value )
  {
    b635 = value;
  }  // method setB635


/******************************************************************************/
  public void setB6351sd ( int value )
  {
    b635_1sd = value;
  }  // method setB6351sd


/******************************************************************************/
  public void setB6352sd ( int value )
  {
    b635_2sd = value;
  }  // method setB6352sd


/******************************************************************************/
  public void setB635Cv ( int value )
  {
    b635_cv = value;
  }  // method setB635Cv


/******************************************************************************/
  public void setB635Mean ( int value )
  {
    b635_mean = value;
  }  // method setB635Mean


/******************************************************************************/
  public void setB635Median ( int value )
  {
    b635_median = value;
  }  // method setB635Median


/******************************************************************************/
  public void setB635Sd ( int value )
  {
    b635_sd = value;
  }  // method setB635Sd


/******************************************************************************/
  public void setBlock ( int value )
  {
    block = value;
  }  // method setBlock


/******************************************************************************/
  public void setCircularity ( int value )
  {
    circularity = value;
  }  // method setCircularity


/******************************************************************************/
  public void setColumn ( int value )
  {
    column = value;
  }  // method setColumn


/******************************************************************************/
  public void setDiameter ( int value )
  {
    diameter = value;
  }  // method setDiameter


/******************************************************************************/
  public void setFPixels ( int value )
  {
    f_pixels = value;
  }  // method setFPixels


/******************************************************************************/
  public void setF532Cv ( int value )
  {
    f532_cv = value;
  }  // method setF532Cv


/******************************************************************************/
  public void setF532Mean ( int value )
  {
    f532_mean = value;
  }  // method setF532Mean


/******************************************************************************/
  public void setF532MeanB532 ( float value )
  {
    f532_mean_b532 = value;
  }  // method setF532MeanB532


/******************************************************************************/
  public void setF532Median ( int value )
  {
    f532_median = value;
  }  // method setF532Median


/******************************************************************************/
  public void setF532MedianB532 ( int value )
  {
    f532_median_b532 = value;
  }  // method setF532MedianB532


/******************************************************************************/
  public void setF532Sat ( int value )
  {
    f532_sat = value;
  }  // method setF532Sat


/******************************************************************************/
  public void setF532Sd ( int value )
  {
    f532_sd = value;
  }  // method setF532Sd


/******************************************************************************/
  public void setF532Total ( int value )
  {
    f532_total = value;
  }  // method setF532Total


/******************************************************************************/
  public void setF635Cv ( int value )
  {
    f635_cv = value;
  }  // method setF635Cv


/******************************************************************************/
  public void setF635Mean ( int value )
  {
    f635_mean = value;
  }  // method setF635Mean


/******************************************************************************/
  public void setF635MeanB635 ( float value )
  {
    f635_mean_b635 = value;
  }  // method setF635MeanB635


/******************************************************************************/
  public void setF635Median ( int value )
  {
    f635_median = value;
  }  // method setF635Median


/******************************************************************************/
  public void setF635MedianB635 ( int value )
  {
    f635_median_b635 = value;
  }  // method setF635MedianB635


/******************************************************************************/
  public void setF635Sd ( int value )
  {
    f635_sd = value;
  }  // method setF635Sd


/******************************************************************************/
  public void setF635Sat ( int value )
  {
    f635_sat = value;
  }  // method setF635Sat


/******************************************************************************/
  public void setF635Total ( int value )
  {
    f635_total = value;
  }  // method setF635Total


/******************************************************************************/
  public void setFlags ( int value )
  {
    flags = value;
  }  // method setFlags


/******************************************************************************/
  public void setIdentifier ( String value )
  {
    identifier = value;
  }  // method setIdentifier


/******************************************************************************/
  public void setLogRatio ( String value )
  {
    log_ratio = value;
  }  // method setLogRatio


/******************************************************************************/
  public void setMeanOfRatios ( String value )
  {
    mean_of_ratios = value;
  }  // method setMeanOfRatios


/******************************************************************************/
  public void setMedianOfRatios ( String value )
  {
    median_of_ratios = value;
  }  // method setMedianOfRatios


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setNormalize ( int value )
  {
    normalize = value;
  }  // method setNormalize


/******************************************************************************/
  public void setPValue ( String value )
  {
    p_value = value;
  }  // method setPValue


/******************************************************************************/
  public void setRatioOfMeans ( float value )
  {
    ratio_of_means = value;
  }  // method setRatioOfMeans


/******************************************************************************/
  public void setRatioOfMedians ( String value )
  {
    ratio_of_medians = value;
  }  // method setRatioOfMedians


/******************************************************************************/
  public void setRatiosSd ( String value )
  {
    ratios_sd = value;
  }  // method setRatiosSd


/******************************************************************************/
  public void setRefseq ( String value )
  {
    refseq = value;
  }  // method setRefseq


/******************************************************************************/
  public void setRgnR2 ( String value )
  {
    rgn_r2 = value;
  }  // method setRgnR2


/******************************************************************************/
  public void setRgnRatio ( String value )
  {
    rgn_ratio = value;
  }  // method setRgnRatio


/******************************************************************************/
  public void setRow ( int value )
  {
    row = value;
  }  // method setRow


/******************************************************************************/
  public void setSnr532 ( String value )
  {
    snr_532 = value;
  }  // method setSnr532


/******************************************************************************/
  public void setSnr635 ( String value )
  {
    snr_635 = value;
  }  // method setSnr635


/******************************************************************************/
  public void setSumOfMeans ( int value )
  {
    sum_of_means = value;
  }  // method setSumOfMeans


/******************************************************************************/
  public void setSumOfMedians ( int value )
  {
    sum_of_medians = value;
  }  // method setSumOfMedians


/******************************************************************************/
  public void setX ( int value )
  {
    x = value;
  }  // method setX


/******************************************************************************/
  public void setY ( int value )
  {
    y = value;
  }  // method setY


/******************************************************************************/
  public void parseHeaderLine ( String line, String label )
  {
    System.out.println ( "ID\tName\tRatio" + label 
        + "\tIP " + label + "\tWCE " + label );
  }  // method parseHeaderLine


/******************************************************************************/
  public void parseLine ( String line )
  {
    StringTokenizer tokens = new StringTokenizer ( line, "\t" );

    try
    {
      setBlock ( LineTools.getInteger ( tokens.nextToken () ) );	// block number
      setColumn ( LineTools.getInteger ( tokens.nextToken () ) );	// column
      setRow ( LineTools.getInteger ( tokens.nextToken () ) );		// row
      setName ( tokens.nextToken () );					// feature name
      setIdentifier ( tokens.nextToken () );				// ID - unique identifier
      setX ( LineTools.getInteger ( tokens.nextToken () ) );		// X coordinate
      setY ( LineTools.getInteger ( tokens.nextToken () ) );		// Y coordinate
      setDiameter ( LineTools.getInteger ( tokens.nextToken () ) );	// diameter
      setF635Median ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 median
      setF635Mean ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 mean
      setF635Sd ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 standard deviation
      setF635Cv ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 CV
      setB635 ( LineTools.getInteger ( tokens.nextToken () ) );		// B635
      setB635Median ( LineTools.getInteger ( tokens.nextToken () ) );	// B635 median
      setB635Mean ( LineTools.getInteger ( tokens.nextToken () ) );	// B635 mean
      setB635Sd ( LineTools.getInteger ( tokens.nextToken () ) );	// B635 standard deviation
      setB635Cv ( LineTools.getInteger ( tokens.nextToken () ) );	// B635 CV
      setB6351sd ( LineTools.getInteger ( tokens.nextToken () ) );	// B635 % > 1 SD
      setB6352sd ( LineTools.getInteger ( tokens.nextToken () ) );	// B635 % > 2 SD
      setF635Sat ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 % saturated
      setF532Median ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 median
      setF532Mean ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 mean
      setF532Sd ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 standard deviation
      setF532Cv ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 CV
      setB532 ( LineTools.getInteger ( tokens.nextToken () ) );		// B532
      setB532Median ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 median
      setB532Mean ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 mean
      setB532Sd ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 standard deviation
      setB532Cv ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 CV
      setB5321sd ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 % > 1 SD
      setB5322sd ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 % > 2 SD
      setF532Sat ( LineTools.getInteger ( tokens.nextToken () ) );	// B532 % saturated
      setRatioOfMedians ( tokens.nextToken () );			// ratio of medians
      setRatioOfMeans ( LineTools.getFloat ( tokens.nextToken () ) );	// ratio of means
      setMedianOfRatios ( tokens.nextToken () );			// median of ratios
      setMeanOfRatios ( tokens.nextToken () );				// mean of ratios
      setRatiosSd ( tokens.nextToken () );				// Ratios SD - geometrix standard devation
      setRgnRatio ( tokens.nextToken () );				// regression ratio
      setRgnR2 ( tokens.nextToken () );					// rgn r2
      setFPixels ( LineTools.getInteger ( tokens.nextToken () ) );	// feature pixels
      setBPixels ( LineTools.getInteger ( tokens.nextToken () ) );	// background pixels
      setCircularity ( LineTools.getInteger ( tokens.nextToken () ) );	// circularity
      setSumOfMedians ( LineTools.getInteger ( tokens.nextToken () ) );	// sum of medians
      setSumOfMeans ( LineTools.getInteger ( tokens.nextToken () ) );	// sum of means
      setLogRatio ( tokens.nextToken () );				// Log2 or ratio of the medians
      setF635MedianB635 ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 median - B532 median
      setF532MedianB532 ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 median - B532 median
      setF635MeanB635 ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 mean - B532 mean
      setF532MeanB532 ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 mean - B532 mean
      setF635Total ( LineTools.getInteger ( tokens.nextToken () ) );	// F635 total intensity
      setF532Total ( LineTools.getInteger ( tokens.nextToken () ) );	// F532 total intensity
      setSnr635 ( tokens.nextToken () );				// SNR 635
      setSnr532 ( tokens.nextToken () );				// SNR 532
      setFlags ( LineTools.getInteger ( tokens.nextToken () ) );	// Flags
      setNormalize ( LineTools.getInteger ( tokens.nextToken () ) );	// Normalize
      setAutoflag ( LineTools.getInteger ( tokens.nextToken () ) );	// Autoflag
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Hu19k_Data.parseLine: NoSuchElementException: " + e );
      System.out.println ( "line: '" + line + "'" );
    }  // catch
  }  // method parseLine


/******************************************************************************/
  public void parseTxtLine ( String line )
  {
    StringTokenizer tokens = new StringTokenizer ( line, "\t" );

    try
    {
      setIdentifier ( tokens.nextToken () );				// ID - unique identifier
      setName ( tokens.nextToken () );					// spot name
      setPValue ( tokens.nextToken () );				// p-value
      setRatioOfMeans ( LineTools.getFloat ( tokens.nextToken () ) );	// ratio of means
      setF635MeanB635 ( LineTools.getFloat ( tokens.nextToken () ) );	// F635 mean - B532 mean
      setF532MeanB532 ( LineTools.getFloat ( tokens.nextToken () ) );	// F532 mean - B532 mean
      setRefseq ( tokens.nextToken () );				// Refseq
    }  // try
    catch ( NoSuchElementException e )
    {
      // System.out.println ( "Hu19k_Data.parseTxtLine: NoSuchElementException: " + e );
      // System.out.println ( "line: '" + line + "'" );
    }  // catch
  }  // method parseTxtLine


/******************************************************************************/
  public String toString ()
  {
    return toString ( "\t" );
  }  // method toString


/******************************************************************************/
  public String toString ( String delimiter )
  {
    return identifier + delimiter 
        + name + delimiter 
        + ratio_of_means + delimiter 
        + f635_mean_b635 + delimiter 
        + f532_mean_b532;
  }  // method toString


/******************************************************************************/
  public String toTxtString ()
  {
    return toTxtString ( "\t" );
  }  // method toTxtString


/******************************************************************************/
  public String toTxtString ( String delimiter )
  {
    return identifier + delimiter
        + name + delimiter
        + p_value + delimiter
        + ratio_of_means + delimiter 
        + f635_mean_b635 + delimiter 
        + f532_mean_b532 + delimiter
        + refseq;
  }  // method toTxtString


/******************************************************************************/

}  // class Hu19k_Data
