import java.awt.*;
import java.io.*;
import java.net.*;

// The fetch() method in this class only works for fetching text/plain
// data, and a few standard image types.  The standard Java distribution
// doesn't contain content handlers for other types (such as text/html),
// so this code exists with an exception.

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

public class Fetch
{
  // Get the contents of a URL and return it as a string.
  public static String fetch ( String address )
      throws MalformedURLException, IOException
  {
    URL url = new URL ( address );
    return ( String ) url.getContent ();
  }  /* method fetch */


  // Get the contents of a URL and return it as an image.
  public static Image fetchimage ( String address, Component c )
      throws MalformedURLException, IOException
  {
    URL url = new URL ( address );
    return c.createImage ( ( java.awt.image.ImageProducer ) url.getContent () );
  }  /* method fetch image */


  // Test out the fetch() method.
  public static void main ( String [] args )
      throws MalformedURLException, IOException
  {
    System.out.println ( fetch ( args [ 0 ] ) );
  }  /* method main */
}  /* class Fetch */
