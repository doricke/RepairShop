
import java.util.*;

// import LineTool;

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

public class TfbsRecord extends Object
{


/******************************************************************************/

  private   String  chromosome = "";		// Human chromosome

  private   int     match_intensity = 0;	// Match oligo intensity

  private   byte    match_pixels = 0;		// Number of match oligo pixels

  private   short   match_std_dev = 0;		// Match oligo standard deviation

  private   short   match_x = 0;		// Match oligo X position

  private   short   match_y = 0;		// Match oligo Y position

  private   int     mismatch_intensity = 0;	// Mismatch oligo intensity

  private   byte    mismatch_pixels = 0;	// Number of mismatch oligo pixels

  private   short   mismatch_std_dev = 0;	// Mismatch oligo standard deviation

  private   short   mismatch_x = 0;		// Mismatch oligo X position

  private   short   mismatch_y = 0;		// Mismatch oligo Y position

  private   String  oligo_sequence = "";	// 25-mer oligonucleotide sequence

  private   long    position = 0;		// Human chromosome position

  private   String  top_strand = "";		// Top strand flag 


/******************************************************************************/
  // Constructor TfbsRecord
  public TfbsRecord ()
  {
    initialize ();
  }  // constructor TfbsRecord


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    chromosome = "";
    match_intensity = 0;
    match_pixels = 0;
    match_std_dev = 0;
    match_x = 0;
    match_y = 0;
    mismatch_intensity = 0;
    mismatch_pixels = 0;
    mismatch_std_dev = 0;
    mismatch_x = 0;
    mismatch_y = 0;
    oligo_sequence = "";
    position = 0;
    top_strand = "";
  }  // method initialize 


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public int getMatchIntensity ()
  {
    return match_intensity;
  }  // method getMatchIntensity


/******************************************************************************/
  public byte getMatchPixels ()
  {
    return match_pixels;
  }  // method getMatchPixels


/******************************************************************************/
  public short getMatchStdDev ()
  {
    return match_std_dev;
  }  // method getMatchStdDev


/******************************************************************************/
  public short getMatchX ()
  {
    return match_x;
  }  // method getMatchX


/******************************************************************************/
  public short getMatchY ()
  {
    return match_y;
  }  // method getMatchY


/******************************************************************************/
  public int getMismatchIntensity ()
  {
    return mismatch_intensity;
  }  // method getMismatchIntensity


/******************************************************************************/
  public byte getMismatchPixels ()
  {
    return mismatch_pixels;
  }  // method getMismatchPixels


/******************************************************************************/
  public short getMismatchStdDev ()
  {
    return mismatch_std_dev;
  }  // method getMismatchStdDev


/******************************************************************************/
  public short getMismatchX ()
  {
    return mismatch_x;
  }  // method getMismatchX


/******************************************************************************/
  public short getMismatchY ()
  {
    return mismatch_y;
  }  // method getMismatchY


/******************************************************************************/
  public String getOligoSequence ()
  {
    return oligo_sequence;
  }  // method getOligoSequence


/******************************************************************************/
  public long getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public String getTopStrand ()
  {
    return top_strand;
  }  // method getTopStrand


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setMatchIntensity ( int value )
  {
    match_intensity = value;
  }  // method setMatchIntensity


/******************************************************************************/
  public void setMatchPixels ( byte value )
  {
    match_pixels = value;
  }  // method setMatchPixels


/******************************************************************************/
  public void setMatchStdDev ( short value )
  {
    match_std_dev = value;
  }  // method setMatchStdDev


/******************************************************************************/
  public void setMatchX ( short value )
  {
    match_x = value;
  }  // method setMatchX


/******************************************************************************/
  public void setMatchY ( short value )
  {
    match_y = value;
  }  // method setMatchY


/******************************************************************************/
  public void setMismatchIntensity ( int value )
  {
    mismatch_intensity = value;
  }  // method setMismatchIntensity


/******************************************************************************/
  public void setMismatchPixels ( byte value )
  {
    mismatch_pixels = value;
  }  // method setMismatchPixels


/******************************************************************************/
  public void setMismatchStdDev ( short value )
  {
    mismatch_std_dev = value;
  }  // method setMismatchStdDev


/******************************************************************************/
  public void setMismatchX ( short value )
  {
    mismatch_x = value;
  }  // method setMismatchX


/******************************************************************************/
  public void setMismatchY ( short value )
  {
    mismatch_y = value;
  }  // method setMismatchY


/******************************************************************************/
  public void setOligoSequence ( String value )
  {
    oligo_sequence = value;
  }  // method setOligoSequence


/******************************************************************************/
  public void setPosition ( long value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setTopStrand ( String value )
  {
    top_strand = value;
  }  // method setTopStrand


/******************************************************************************/
  public void parseLine ( String line )
  {
    StringTokenizer tokens = new StringTokenizer ( line, "\t" );

    try
    {
      setOligoSequence     ( tokens.nextToken () );
      setTopStrand         ( tokens.nextToken () );
      setChromosome        ( tokens.nextToken () );
      setPosition          ( LineTools.getInteger ( tokens.nextToken () ) );
      setMatchX            ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setMatchY            ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setMismatchX         ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setMismatchY         ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setMatchIntensity    ( LineTools.getInteger ( tokens.nextToken () ) );
      setMatchStdDev       ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setMatchPixels       ( (byte) LineTools.getInteger ( tokens.nextToken () ) );
      setMismatchIntensity ( LineTools.getInteger ( tokens.nextToken () ) );
      setMismatchStdDev    ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setMismatchPixels    ( (byte) LineTools.getInteger ( tokens.nextToken () ) );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "TfbsRecord.parseLine: NoSuchElementException: " + e );
    }  // catch
  }  // method parseLine


/******************************************************************************/
  public String toString ()
  {
    String str = getMatchX () + "\t" +
                 getMatchY () + "\t" +
                 getChromosome () + "\t" +
                 getPosition () + "\t" +
                 getOligoSequence () + "\t" +
                 getMismatchX () + "\t" +
                 getMismatchY () + "\t" +
                 getTopStrand ();
    return str;
  }  // method toString


/******************************************************************************/

}  // class TfbsRecord
