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

public class ScrollTest extends Frame
{
  public ScrollTest ()
  {
    setTitle ( "ScrollTest setTitle" );
    add ( "East", vert = new Scrollbar ( Scrollbar.VERTICAL ) );
    add ( "South", horiz = new Scrollbar ( Scrollbar.HORIZONTAL ) );
    add ( "Center", canvas = new SquareCanvas () );
  }  /* ScrollTest */
  
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY )  System.exit ( 0 );
    else
      if ( evt.id == Event.WINDOW_MOVED )
      {
        Dimension d = canvas.size ();
        horiz.setValues ( horiz.getValue (), d.width, 0, 600 );
        vert.setValues ( vert.getValue (), d.height, 0, 400 );
      }  /* if */
      else
        if ( evt.id == Event.SCROLL_ABSOLUTE ||
             evt.id == Event.SCROLL_LINE_DOWN ||
             evt.id == Event.SCROLL_LINE_UP ||
             evt.id == Event.SCROLL_PAGE_DOWN ||
             evt.id == Event.SCROLL_PAGE_UP )
        {
          canvas.translate ( horiz.getValue (), vert.getValue () );
          return true;
        }  /* if */
    
    return super.handleEvent ( evt );
  }  /* handleEvent */
  
  public void show ()
  {
    super.show ();
    Dimension d = canvas.size ();
    horiz.setValues ( 0, d.width, 0, 600 );
    vert.setValues ( 0, d.height, 0, 400 );
  }  /* show */
  
  public static void main ( String args [] )
  {
    Frame f = new ScrollTest ();
    f.resize ( 300, 200 );
    f.show ();
  }  /* main */
  
  private Scrollbar horiz;
  private Scrollbar vert;
  private SquareCanvas canvas;
}  /* class ScrollTest */


class SquareCanvas extends Canvas
{
  public void translate ( int x, int y )
  {
    dx = x;
    dy = y;
    repaint ();
  }  /* translate */
  
  public void paint ( Graphics g )
  {
    g.translate ( -dx, -dy );
    for ( int i = 0; i < nsquares; i++ )
      draw ( g, i );
  }  /* paint */
  
  public int find ( int x, int y )
  {
    for ( int i = 0; i < nsquares; i++ )
    
      if ( squares [ i ].x <= x &&
           x <= squares [ i ].x + SQUARELENGTH &&
           squares [ i ].y <= y &&
           y <= squares [ i ].y + SQUARELENGTH )
           
         return i;
         
     return -1;
   }  /* find */
   
   public void draw ( Graphics g, int i )
   {
     g.drawRect ( squares [ i ].x, squares [ i ].y, SQUARELENGTH, 
         SQUARELENGTH );
   }  /* draw */
   
   public void add ( int x, int y )
   {
     if ( nsquares < MAXNSQUARES )
     {
       squares [ nsquares ] = new Point ( x, y );
       nsquares++;
       repaint ();
     }  /* if */
   }  /* add */
   
   public void remove ( int n )
   {
     nsquares--;
     squares [ n ] = squares [ nsquares ];
     
     if ( current == n )  current = -1;
     repaint ();
   }  /* remove */
   
   public boolean mouseDown ( Event evt, int x, int y )
   {
     x += dx; 
     y += dy;
     current = find ( x, y );
     
     if ( current < 0 )  // not inside a square
     {
       add ( x, y );
     }
     else
       if ( evt.clickCount >= 2 )
       {
         remove ( current );
       }  /* if */
       
     return true;
   }  /* mouseDown */
   
   public boolean mouseDrag ( Event evt, int x, int y )
   {
     x += dx;
     y += dy;
     
     if ( current >= 0 )
     {
       Graphics g = getGraphics ();
       g.translate ( -dx, -dy );
       g.setXORMode ( getBackground () );
       draw ( g, current );
       squares [ current ].x = x;
       squares [ current ].y = y;
       draw ( g, current );
       g.dispose ();
     }  /* if */
     
     return true;
   }  /* mouseDrag */
   
   private static final int SQUARELENGTH = 10;
   
   private static final int MAXNSQUARES = 100;
   private Point [] squares = new Point [ MAXNSQUARES ];
   private int nsquares = 0;
   private int current = -1;
   private int dx = 0;
   private int dy = 0;
}  /* class SquareCanvas */
