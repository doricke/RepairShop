import java.io.*;
import java.awt.*;

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

public class BufferTest extends Frame
{
  public test ()
  {
  }  /* constructor test */
  
  static public void main ( String [] args )
  {
    System.out.println ( "class (String)Buffer Test" );
    
    String s = new String ( "ACGT" );
    StringBuffer b = new StringBuffer ( s );
    
    for ( int i = 0; i < s.length (); i++ )
    {
      b.setCharAt ( i, s.charAt ( s.length () - 1 - i ) );
    }  /* for */
    
    String t = new String ( s );
    t = b.toString ();
    
    System.out.println ( "s = " + s );
    System.out.println ( "b = " + b );
    System.out.println ( "t = " + t );    
    
  }  /* method main */

}  /* class BufferTest */
