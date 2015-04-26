

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

public class Attribute extends Object
{


/******************************************************************************/

  private   String  attribute_comment = "";	// Attribute comment

  private   String  attribute_name = "";	// Attribute name

  private   String  attribute_type = "";	// Attribute type

  private   String  initial_value = "";		// Attribute initial value

  private   String  object_initialize = "";	// Object attribute initialization code fragment

  private   String  object_name = "";		// Object attribute name


/******************************************************************************/
  // Constructor Attribute
  public Attribute ()
  {
    initialize ();
  }  // constructor Attribute


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    attribute_comment = "";
    attribute_name = "";
    attribute_type = "";
    initial_value = "";
    object_initialize = "";
    object_name = "";
  }  // method initialize 


/******************************************************************************/
  public String getAttributeComment ()
  {
    return attribute_comment;
  }  // method getAttributeComment


/******************************************************************************/
  public String getAttributeName ()
  {
    return attribute_name;
  }  // method getAttributeName


/******************************************************************************/
  public String getAttributeType ()
  {
    return attribute_type;
  }  // method getAttributeType


/******************************************************************************/
  public String getInitialValue ()
  {
    return initial_value;
  }  // method getInitialValue


/******************************************************************************/
  public String getObjectInitialize ()
  {
    return object_initialize;
  }  // method getObjectInitialize


/******************************************************************************/
  public String getObjectName ()
  {
    return object_name;
  }  // method getObjectName


/******************************************************************************/
  public void setAttributeComment ( String value )
  {
    attribute_comment = value;
  }  // method setAttributeComment


/******************************************************************************/
  public void setAttributeName ( String value )
  {
    attribute_name = value;
  }  // method setAttributeName


/******************************************************************************/
  public void setAttributeType ( String value )
  {
    attribute_type = value;
  }  // method setAttributeType


/******************************************************************************/
  public void setInitialValue ( String value )
  {
    initial_value = value;
  }  // method setInitialValue


/******************************************************************************/
  public void setObjectInitialize ( String value )
  {
    object_initialize = value;
  }  // method setObjectInitialize


/******************************************************************************/
  public void setObjectName ( String value )
  {
    object_name = value;
  }  // method setObjectName


/******************************************************************************/
  public String toString ()
  {
    return attribute_type + "\t"
        + attribute_name  + "\t"
        + initial_value + "\t"
        + attribute_comment  + "\t"
        + object_name + "\t"
        + object_initialize;
  }  // method toString


/******************************************************************************/

}  // class Attribute
