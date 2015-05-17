
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

public class Position extends Object
{


/******************************************************************************/

  private   String chromosome = "";		// Chromosome 21 or 22

  private   int intensity = 0;			// probe intensity value

  private   int position = 0;			// probe position on chromosome

  private   String probe = "";			// Affymetrix probe sequence 

  private   short probe_x = 0;			// Probe X position

  private   short probe_y = 0;			// Probe Y position


/******************************************************************************/
  // Constructor Position
  public Position ()
  {
    initialize ();
  }  // constructor Position


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    chromosome = "";
    intensity = 0;
    position = 0;
    probe = "";
    probe_x = 0;
    probe_y = 0;
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
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public String getProbe ()
  {
    return probe;
  }  // method getProbe


/******************************************************************************/
  public short getProbeX ()
  {
    return probe_x;
  }  // method getProbeX


/******************************************************************************/
  public short getProbeY ()
  {
    return probe_y;
  }  // method getProbeY


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
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setProbe ( String value )
  {
    probe = value;
  }  // method setProbe


/******************************************************************************/
  public void setProbeX ( short value )
  {
    probe_x = value;
  }  // method setProbeX


/******************************************************************************/
  public void setProbeY ( short value )
  {
    probe_y = value;
  }  // method setProbeY


/******************************************************************************/
  public void parse ( String value )
  {
    // Assert Position file line.
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setProbeX ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setProbeY ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setChromosome ( tokens.nextToken () );
      setPosition ( LineTools.getInteger ( tokens.nextToken () ) );
      setProbe ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Position.parse: " + e );
    }  // catch
  }  // method parse


/******************************************************************************/
  public void parseChip ( String value )
  {
    // Assert Position file line.
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setPosition ( LineTools.getInteger ( tokens.nextToken () ) );
      setChromosome ( tokens.nextToken () );
      setProbeX ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setProbeY ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setIntensity ( LineTools.getInteger ( tokens.nextToken () ) );
      setProbe ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Position.parse: " + e );
    }  // catch
  }  // method parseChip


/******************************************************************************/
public String toString ()
{
  return probe_x + "\t" 
       + probe_y + "\t" 
       + chromosome + "\t" 
       + position + "\t" 
       + probe;
}  // method toString


/******************************************************************************/
public String toStringChip ()
{
  return position + "\t" 
       + chromosome + "\t" 
       + probe_x + "\t" 
       + probe_y + "\t" 
       + intensity + "\t" 
       + probe;
}  // method toStringChip


/******************************************************************************/

}  // class Position
