
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

public class RefInfo extends Object
{


/******************************************************************************/

  private   String location = "";		// location (e.g., "intergenic", ...)

  private   String name = "";			// name

  private   int  position = 0;			// chromosome position

  private   char strand = ' ';			// strand ('+' or '-')

  private   int  transcript_end = 0;		// End of transcript

  private   int  transcript_start = 0;		// Start of transcript


/******************************************************************************/
  // Constructor RefInfo
  public RefInfo ()
  {
    initialize ();
  }  // constructor RefInfo


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    location = "";
    name = "";
    position = 0;
    strand = ' ';
    transcript_end = 0;
    transcript_start = 0;
  }  // method initialize 


/******************************************************************************/
  public String getLocation ()
  {
    return location;
  }  // method getLocation


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public char getStrand ()
  {
    return strand;
  }  // method getStrand


/******************************************************************************/
  public int getTranscriptEnd ()
  {
    return transcript_end;
  }  // method getTranscriptEnd


/******************************************************************************/
  public int getTranscriptStart ()
  {
    return transcript_start;
  }  // method getTranscriptStart


/******************************************************************************/
  public void setLocation ( String value )
  {
    location = value;
  }  // method setLocation


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setStrand ( char value )
  {
    strand = value;
  }  // method setStrand


/******************************************************************************/
  public void setTranscriptEnd ( int value )
  {
    transcript_end = value;
  }  // method setTranscriptEnd


/******************************************************************************/
  public void setTranscriptStart ( int value )
  {
    transcript_start = value;
  }  // method setTranscriptStart


/******************************************************************************/
  public void parse ( String value )
  {
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setPosition ( LineTools.getInteger ( tokens.nextToken () ) );
      setLocation ( tokens.nextToken () );
      if ( location.equals ( "intergenic" ) == false )
      {
        setName ( tokens.nextToken () );
        setStrand ( tokens.nextToken ().charAt ( 0 ) );
        setTranscriptStart ( LineTools.getInteger ( tokens.nextToken () ) );
        setTranscriptEnd ( LineTools.getInteger ( tokens.nextToken () ) );
      }  // if
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "RefInfo.parse: " + e );
    }  // catch
  }  // method parse


/******************************************************************************/
public String toInfo ()
{
  return name + "\t" 
      + strand + "\t" 
      + transcript_start + "\t" 
      + transcript_end;
}  // method toInfo


/******************************************************************************/
public String toString ()
{
  return name + "\t" 
      + location + "\t" 
      + strand + "\t" 
      + transcript_start + "\t" 
      + transcript_end;
}  // method toString


/******************************************************************************/

}  // class RefInfo
