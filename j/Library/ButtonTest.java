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

public class ButtonTest extends Frame
{
  public ButtonTest ()
  {
    setTitle ( "ButtonTest" );
    setLayout ( new FlowLayout () );
    add ( new Button ( "Yellow" ) );
    add ( new Button ( "Blue" ) );
    add ( new Button ( "Orange" ) );
    add ( new Button ( "Cyan" ) );
    add ( new Button ( "Pink" ) );
    add ( new Button ( "Red" ) );
    add ( new Button ( "White" ) );
  }  /* ButtonTest */
  
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY )  System.exit ( 0 );
    return super.handleEvent ( evt );
  }  /* handleEvent */
  
  public boolean action ( Event evt, Object arg )
  {
    if ( arg.equals ( "Yellow" ) ) setBackground ( Color.yellow );
    else if ( arg.equals ( "Blue" ) ) setBackground ( Color.blue );
    else if ( arg.equals ( "Orange" ) ) setBackground ( Color.orange );
    else if ( arg.equals ( "Cyan" ) ) setBackground ( Color.cyan );
    else if ( arg.equals ( "Pink" ) ) setBackground ( Color.pink );
    else if ( arg.equals ( "Red" ) ) setBackground ( Color.red );
    else if ( arg.equals ( "White" ) ) setBackground ( Color.white );
    else return false;
    
    repaint ();
    return true;
  }  /* action */
  
  public static void main ( String [] args )
  {
    Frame f = new ButtonTest ();
    f.resize ( 320, 200 );
    f.show ();
  }  /* main */
                
}  /* class ButtonTest */
