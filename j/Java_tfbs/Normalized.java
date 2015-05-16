
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

public class Normalized extends Object
{


/******************************************************************************/

  private   String  chromosome = "";		// Human chromosome name

  private   int  intensity = 0;			// match oligo normalized intensity

  private   String  oligo_sequence = "";	// 25-mer oligonucleotide sequence

  private   long  position = 0L;		// Human chromosome position


/******************************************************************************/
  // Constructor Normalized
  public Normalized ()
  {
    initialize ();
  }  // constructor Normalized


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    chromosome = "";
    intensity = 0;
    oligo_sequence = "";
    position = 0L;
  }  // method initialize 


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public int getIntensity ()
  {
    return intensity;
  }  // method getIntensity


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
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setIntensity ( int value )
  {
    intensity = value;
  }  // method setIntensity


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
  public void parseLine ( String line )
  {
    StringTokenizer tokens = new StringTokenizer ( line, "\t" );

    try
    {
      setPosition      ( LineTools.getInteger ( tokens.nextToken () ) );
      setChromosome    ( tokens.nextToken () );
      setIntensity     ( LineTools.getInteger ( tokens.nextToken () ) );
      setOligoSequence ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Normalized.parseLine: NoSuchElementException: " + e );
    }  // catch
  }  // method parseLine


/******************************************************************************/
  public String toString ()
  {
    String str = getPosition () + "\t" +
                 getChromosome () + "\t" +
                 getIntensity () + "\t" +
                 getOligoSequence ();
    return str;
  }  // method toString


/******************************************************************************/

}  // class Normalized
