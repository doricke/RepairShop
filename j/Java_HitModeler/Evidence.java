

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

public class Evidence extends Object
{


/******************************************************************************/

  private   long  begin = 0;			// evidence start

  private   String description = "";		// evidence description/type

  private   long  end = 0;			// evidence end


/******************************************************************************/
  // Constructor Evidence
  public Evidence ()
  {
    initialize ();
  }  // constructor Evidence


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    begin = 0;
    description = "";
    end = 0;
  }  // method initialize 


/******************************************************************************/
  public long getBegin ()
  {
    return begin;
  }  // method getBegin


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public long getEnd ()
  {
    return end;
  }  // method getEnd


/******************************************************************************/
  public void setBegin ( long value )
  {
    begin = value;
  }  // method setBegin


/******************************************************************************/
  public void setDescription ( String value )
  {
    // Validate value.
    if ( value == null )  return;

    // Check for no current description.
    if ( description.length () <= 0 )
    {
      description = value;
      return;
    }  // if

    // Motif is the lowest priority evidence.
    if ( description.equals ( "motif" ) == true )
    {
      description = value;
      return;
    }  // if

    // Highest priority evidence.
    if ( ( value.equals ( "bacterial" ) == true ) ||
         ( value.equals ( "repeat" ) == true ) ||
         ( value.equals ( "chloroplast" ) == true ) ||
         ( value.equals ( "mitochondrial" ) == true ) )

      description = value;
  }  // method setDescription


/******************************************************************************/
  public void setEnd ( long value )
  {
    end = value;
  }  // method setEnd


/******************************************************************************/
  public void print ()
  {
    System.out.println 
        ( "\t[" 
        + begin
        + "-"
        + end
        + "] "
        + description
        );
  }  // method print


/******************************************************************************/

}  // class Evidence
