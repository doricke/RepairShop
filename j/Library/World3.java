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

class World3 extends Frame
{
  public boolean handleEvent ( Event e )
  {
    if ( e.id == Event.WINDOW_DESTROY )
      System.exit ( 0 );
    
    return super.handleEvent ( e );
  }  /* handleEvent */
  
  public void paint ( Graphics g )
  {
    g.drawString ( "Hello World 3 program", 75, 100 );
  }  /* paint */
  
  public static void main ( String [] args )
  {
    Frame f = new World3();
    f.resize ( 300, 200 );
    f.show ();
  }  /* main */
}  /* class World3 */
