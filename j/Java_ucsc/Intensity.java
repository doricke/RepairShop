

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
  public void parse ( String value )
  {
    line = value;

    // Assert CEL file probe intensity line.
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setProbeX ( LineTools.getInteger ( tokens.nextToken () ) );
      setProbeY ( LineTools.getInteger ( tokens.nextToken () ) );
      setIntensity ( (int) (LineTools.getFloat ( tokens.nextToken () ) + 0.5) );
      setStdv ( (int) (LineTools.getFloat ( tokens.nextToken () ) + 0.5) );
      setNPixels ( LineTools.getInteger ( tokens.nextToken () ) );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Intensity.parse: " + e );
    }  // catch
  }  // method parse


/******************************************************************************/
public String toString ()
{
  // return probe_x + "\t" + probe_y + "\t" + intensity + "\t" + stdv + "\t" + npixels;
  return line;
}  // method toString

/******************************************************************************/

