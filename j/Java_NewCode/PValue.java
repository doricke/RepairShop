

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

public class PValue extends Object
{


/******************************************************************************/

  private   int  control = 0;			// Control intensity value

  private   float  p_value = 0.0f;		// Wilcoxon p-value for this position

  private   int  position = 0;			// chromosome position

  private   int  sample = 0;			// Sample intensity value


/******************************************************************************/
  // Constructor PValue
  public PValue ()
  {
    initialize ();
  }  // constructor PValue


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    control = 0;
    p_value = null;
    position = 0;
    sample = 0;
  }  // method initialize 


/******************************************************************************/
  public int getControl ()
  {
    return control;
  }  // method getControl


/******************************************************************************/
  public float getPValue ()
  {
    return p_value;
  }  // method getPValue


/******************************************************************************/
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public int getSample ()
  {
    return sample;
  }  // method getSample


/******************************************************************************/
  public void setControl ( int value )
  {
    control = value;
  }  // method setControl


/******************************************************************************/
  public void setPValue ( float value )
  {
    p_value = value;
  }  // method setPValue


/******************************************************************************/
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setSample ( int value )
  {
    sample = value;
  }  // method setSample


/******************************************************************************/

}  // class PValue
