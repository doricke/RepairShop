import java.awt.*;
import java.io.*;

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

public class FileViewer extends Frame
{
  Button close;
  // Query the size of the specified file, create an array of bytes big
  // enough, and read it in.  Then create a TextArea to display the text
  // and a "Close" button to pop the window down.
  
  public FileViewer ( String filename ) throws IOException
  {
  	super ( "FileViewer." + filename );
  	File f = new File ( filename );
  	int size = (int) f.length ();
  	int bytes_read = 0;
  	FileInputStream in = new FileInputStream ( f );
  	byte [] data = new byte [ size ];
  	while ( bytes_read < size )
  	  bytes_read += in.read ( data, bytes_read, size - bytes_read );
  	  
  	TextArea textarea = new TextArea ( new String ( data, 0 ), 24, 80 );
  	textarea.setFont ( new Font ( "Helvetica", Font.PLAIN, 12 ) );
  	textarea.setEditable ( false );
  	this.add ( "Center", textarea );
  	
  	close = new Button ( "Close" );
  	this.add ( "South", close );
  	this.pack ();
  	this.show ();
  }  /* FileViewer method */
  
  // Handle the close button by popping this window down
  public boolean action ( Event e, Object what )
  {
  	if ( e.target == close )
  	{
  	  this.hide ();
  	  this.dispose ();
  	  return true;	
  	}  /* if */
  	return false;
  }  /* action method */
  
  // The FileViewer can be used by other classes, or it can be
  // used standalone with this main () method.
  static public void main ( String [] args ) throws IOException
  {
  	if ( args.length != 1 )
  	{
  	  System.out.println ( "Usage: java FileViwer <filename>" );
  	  System.exit ( 0 );
  	}  /* if */
  	try
        {
          Frame f = new FileViewer ( args [ 0 ] );
        }  // try
  	catch ( IOException e )
        {
          System.out.println ( e );
        }  // catch
  }  /* main method */	
}  /* FileViewer class */
