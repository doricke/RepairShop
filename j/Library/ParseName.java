

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
public class ParseName extends Object
{

/******************************************************************************/
protected String cloneName;		// Name of the clone

protected boolean forward;		// Forward consensus direction

protected String method;		// Sequencing method 

protected String subclone;		// Subclone identifier

protected String direction;		// Sequence direction

protected String version;		// Sequence version


/******************************************************************************/
public ParseName ( )
{
  Init ();
}  /* constructor ParseName */


/******************************************************************************/
public ParseName ( String name )
{
  setName ( name );

  System.out.println ( "ParseName: " + name );
}  /* constructor ParseName */


/******************************************************************************/
private void Init ( )
{
  cloneName = "";
  forward   = false;
  method    = "";
  subclone  = "";
  direction = "";
  version   = "";
}  /* method Init */


/******************************************************************************/
/* This constructor sets up the ParseName Object from a Phred/Phrap sequence name. */
public void setName ( String name )
{
  if ( name.endsWith ( ".comp" ) )  forward = false;
  else
    forward = true;

  int underscore = name.indexOf ( '_' );
  if ( underscore < 0 )  underscore = name.length ();

  int period = name.indexOf ( '.' );
  if ( period < 0 )  period = name.length ();

  cloneName = name.substring ( 0, underscore );

  int digit = underscore + 1;
  while ( ( digit < period ) && 
          ( ( name.charAt ( digit ) < '0' ) || ( name.charAt ( digit ) > '9' ) ) )
    digit++;

  method = name.substring ( underscore+1, digit );

  subclone = name.substring ( digit, period );

  direction = name.substring ( period+1, period+2 );

  // Find the end of the version.
  int end = period + 2;
  while ( ( end < name.length () ) && 
          ( ( name.charAt ( end ) >= '0' ) && ( name.charAt ( end ) <= '9' ) ) )
    end++;

  version = name.substring ( period+2, end );

}  /* method setName */


/******************************************************************************/
/* Is this sequence a dye primer sequence? */
public boolean isPrimer ( )
{
  if ( direction.equals ( "r" ) || direction.equals ( "s" ) )
    return true;

  return false;
}  /* method isPrimer */


/******************************************************************************/
/* Is this sequence a dye terminator sequence? */
public boolean isTerminator ( )
{
  if ( direction.equals ( "x" ) || direction.equals ( "y" ) )
    return true;

  return false;
}  /* method isTerminator */


/******************************************************************************/
/* Is this sequence aligned in the forward consensus direction? */
public boolean isForward ( )
{
  return forward;
}  /* method isForward */


/******************************************************************************/
/* Is this sequence aligned in the reverse consensus direction? */
public boolean isReverse ( )
{
  if ( forward == true )  return false;

  return true;
}  /* isReverse */


/******************************************************************************/
/* Is this sequence a dye terminator forward sequence? */
public boolean is_x ( )
{
  if ( direction.equals ( "x" ) )
    return true;

  return false;
}  /* method is_x */


/******************************************************************************/
/* Is this sequence a dye terminator reverse sequence? */
public boolean is_y ( )
{
  if ( direction.equals ( "y" ) )
    return true;

  return false;
}  /* method is_y */


/******************************************************************************/
/* Is this sequence a dye primer reverse sequence? */
public boolean is_s ( )
{
  if ( direction.equals ( "s" ) )
    return true;

  return false;
}  /* method is_s */


/******************************************************************************/
/* Is this sequence a dye primer forward sequence? */
public boolean is_r ( )
{
  if ( direction.equals ( "r" ) )
    return true;

  return false;
}  /* method is_r */


/******************************************************************************/
/* Is this sequence a Sample Sequence (SaSe)? */
public boolean isSaSe ( )
{
  if ( method.equals ( "sa" ) )
    return true;

  return false;
}  /* method isSaSe */


/******************************************************************************/
/* Is this sequence a nebulized dye terminator sequence? */
public boolean is_ne ( )
{
  if ( method.equals ( "ne" ) || method.equals ( "nb" ) )
    return true;

  return false;
}  /* method is_ne */


/******************************************************************************/
/* Is this sequence a Restriction Enzyme Cocktail sequence? */
public boolean is_rc ( )
{
  if ( method.equals ( "rc" ) || method.equals ( "ba" ) )
    return true;

  return false;
}  /* method is_rc */


/******************************************************************************/
/* Is this sequence a Blunt End sequence? */
public boolean is_bla ( )
{
  if ( method.equals ( "bla" ) || method.equals ( "blv" ) )
    return true;

  return false;
}  /* method is_bla */


/******************************************************************************/
/* Is this sequence a Primer walk sequence? */
public boolean is_pr ( )
{
  if ( direction.equals ( "pr" ) )
    return true;

  return false;
}  /* method is_pr */


/******************************************************************************/
public String getClone ( )
{
  return cloneName;
}  /* method getClone */


/******************************************************************************/
public String getDirection ( )
{
  return direction;
}  /* method getDirection */


/******************************************************************************/
public String getMethod ( )
{
  return method;
}  /* method getMethod */


/******************************************************************************/
public String getVersion ( )
{
  return version;
}  /* method getVersion */


/******************************************************************************/
/* This method checks if the clone names match. */
public boolean sameClone ( String clone_name )
{
  if ( cloneName.equals ( clone_name ) )  return true;

  return false;
}  /* method sameClone */


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/

}  /* class ParseName */
