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

class DrawPoly extends Frame
{
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY )  System.exit ( 0 );
    return false;
  }  /* handleEvent */
  
  public void paint ( Graphics g )
  {
    int r = 45;         // radius of cicle bounding PacMan (R)
    int cx = 50;        // center of that circle
    int cy = 100;
    int angle = 30;     // opening angle of mouth
    
    int dx = (int) (r * Math.cos ( angle * Math.PI / 180 ) );
    int dy = (int) (r * Math.sin ( angle * Math.PI / 180 ) );
    
    g.drawLine ( cx, cy, cx + dx, cy + dy );            // lower jaw
    g.drawLine ( cx, cy, cx + dx, cy - dy );            // upper jaw
    g.drawArc ( cx - r, cy - r, 2 * r, 2 * r, angle, 360 - 2 * angle );
    
    Polygon p = new Polygon ();
    cx = 150;
    int i;
    for ( i = 0; i < 5; i++ )
      p.addPoint ( (int) (cx + r * Math.cos ( i * 2 * Math.PI / 5 ) ),
          (int) (cy + r * Math.sin ( i * 2 * Math.PI / 5 ) ) );
    g.drawPolygon ( p );
    
    Polygon s = new Polygon ();
    cx = 250;
    for ( i = 0; i < 360; i++ )
    {
      double t = i / 360.0;
      s.addPoint ( (int) (cx + r * t * Math.cos ( 8 * t * Math.PI ) ),
          (int) (cy + r * t * Math.sin (8 * t * Math.PI ) ) );
    }  /* for */
    g.drawPolygon ( s );
  }  /* paint */
    
  public static void main ( String args [] )
  {
    Frame f = new DrawPoly ();
    f.resize ( 300, 200 );
    f.show ();
  }  /* main */

}  /* class DrawPoly */
