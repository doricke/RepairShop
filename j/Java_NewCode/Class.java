

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

public class Class extends Object
{


/******************************************************************************/

  private   String  class_name = "";		// Class name

  private   String  class_comment = "";		// Class comment

  private   String  extends_name = "";		// Class extended


/******************************************************************************/
  // Constructor Class
  public Class ()
  {
    initialize ();
  }  // constructor Class


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    class_name = "";
    class_comment = "";
    extends_name = "";
  }  // method initialize 


/******************************************************************************/
  public String getClassName ()
  {
    return class_name;
  }  // method getClassName


/******************************************************************************/
  public String getClassComment ()
  {
    return class_comment;
  }  // method getClassComment


/******************************************************************************/
  public String getExtendsName ()
  {
    return extends_name;
  }  // method getExtendsName


/******************************************************************************/
  public void setClassName ( String value )
  {
    class_name = value;
  }  // method setClassName


/******************************************************************************/
  public void setClassComment ( String value )
  {
    class_comment = value;
  }  // method setClassComment


/******************************************************************************/
  public void setExtendsName ( String value )
  {
    extends_name = value;
  }  // method setExtendsName


/******************************************************************************/
  public String toString ()
  {
    return class_name + "\t" + extends_name + "\t" + class_comment;
  }  // method toString


/******************************************************************************/

}  // class Class
