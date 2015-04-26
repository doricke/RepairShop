

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

public class Method extends Object
{


/******************************************************************************/

  private   String  method_comment = "";	// Method comment

  private   String  method_name = "";		// Method name

  private   String  method_type = "";		// Method return type

  private   String  method_value = "";		// Initial method return value


/******************************************************************************/
  // Constructor Method
  public Method ()
  {
    initialize ();
  }  // constructor Method


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    method_comment = "";
    method_name = "";
    method_type = "";
    method_value = "";
  }  // method initialize 


/******************************************************************************/
  public String getMethodComment ()
  {
    return method_comment;
  }  // method getMethodComment


/******************************************************************************/
  public String getMethodName ()
  {
    return method_name;
  }  // method getMethodName


/******************************************************************************/
  public String getMethodType ()
  {
    return method_type;
  }  // method getMethodType


/******************************************************************************/
  public String getMethodValue ()
  {
    return method_value;
  }  // method getMethodValue


/******************************************************************************/
  public void setMethodComment ( String value )
  {
    method_comment = value;
  }  // method setMethodComment


/******************************************************************************/
  public void setMethodName ( String value )
  {
    method_name = value;
  }  // method setMethodName


/******************************************************************************/
  public void setMethodType ( String value )
  {
    method_type = value;
  }  // method setMethodType


/******************************************************************************/
  public void setMethodValue ( String value )
  {
    method_value = value;
  }  // method setMethodType


/******************************************************************************/
  public String toString ()
  {
    return method_name + "\t"  
        + method_type + "\t" 
        + method_value + "\t" 
        + method_comment;
  }  // method toString


/******************************************************************************/

}  // class Method
