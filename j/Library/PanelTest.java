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

public class PanelTest extends Frame
{
  public PanelTest ()
  {
    setTitle ( "PanelTest" );
    Panel p = new Panel ();
    p.setLayout ( new FlowLayout () );
    p.add ( new Button ( "Tick" ) );
    p.add ( new Button ( "Reset" ) );
    p.add ( new Button ( "Close" ) );
    add ( "South", p );
    
    clock = new ClockCanvas ();
    add ( "Center", clock );
  }  /* constructor PanelTest */
  
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY )  System.exit ( 0 );
    return super.handleEvent ( evt );
  }  /* method handleEvent */
  
  public boolean action ( Event evt, Object arg )
  {
    if ( arg.equals ( "Tick" ) ) clock.tick ();
    else if ( arg.equals ( "Reset" ) )  clock.reset ();
    else if ( arg.equals ( "Close" ) )  System.exit ( 0 ) ;
    else return false;
    
    return true;
  }  /* method action */
  
  public boolean mouseDown ( Event evt, int x, int y )
  {
    System.out.println ( "mouseDown at " + x + ", " + y );
    return true;
  }  /* method mouseDown */
  
  public boolean mouseMove ( Event evt, int x, int y )
  {
    System.out.println ( "mouseMove at " + x + ", " + y );
    return true;
  }  /* method mouseMove */
  
  public boolean mouseDrag ( Event evt, int x, int y )
  {
    System.out.println ( "mouseDrag at " + x + ", " + y );
    return true;
  }  /* method mouseDrag */
  
  public static void main ( String [] args )
  {
    Frame f = new PanelTest ();
    f.resize ( 300, 200 );
    f.show ();
  }  /* method main */
  
  private ClockCanvas clock;
}  /* class PanelTest */

class ClockCanvas extends Canvas
{
  public void paint ( Graphics g )
  {
    g.drawOval ( 0, 0, 100, 100 );
    double hourAngle = 2 * Math.PI * (minutes - 3 * 60) / (12 * 60);
    double minuteAngle = 2 * Math.PI * (minutes - 15) / 60;
    g.drawLine ( 50, 50, 50 + (int)(30 * Math.cos (hourAngle) ),
                         50 + (int)(30 * Math.sin (hourAngle) ) );
    g.drawLine ( 50, 50, 50 + (int)(45 * Math.cos (minuteAngle) ),
                         50 + (int)(45 * Math.sin (minuteAngle) ) );
  }  /* method paint */
  
  public void reset ()
  {
    minutes = 0;
    repaint ();
  }  /* method reset */
  
  public void tick ()
  {
    minutes++;
    repaint ();
  }  /* method tick */
  
  private int minutes = 0; 
}  /* class ClockCanvas */
