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

public class TextEditTest extends Frame
{
  public TextEditTest ()
  {
    setTitle ( "TextEditTest" );
    
    Panel p = new Panel ();
    p.setLayout ( new FlowLayout () );
    p.add ( new Button ( "Replace" ) );
    from = new TextField ( 10 );
    p.add ( from );
    p.add ( new Label ( "with" ) );
    to = new TextField ( 10 );
    p.add ( to );
    add ( "South", p );
    ta = new TextArea ( 20, 60 );
    add ( "Center", ta );
  }  /* TextEditTest */
  
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY )  System.exit ( 0 );
    return super.handleEvent ( evt );
  }  /* handleEvent */
  
  public boolean action ( Event evt, Object arg )
  {
    if ( arg.equals ( "Replace" ) )
    {
      String f = from.getText ();
      int n = ta.getText ().indexOf ( f );
      if ( n >= 0 && f.length () > 0 )
        ta.replaceText (to.getText (), n, n + f.length () );
    }
    else
      return super.action ( evt, arg );
    return true;
  }  /* action */
  
  public static void main ( String [] args )
  {
    Frame f = new TextEditTest ();
    f.resize ( 400, 300 );
    f.show ();
  }  /* main */
  
  private TextArea ta;
  private TextField from, to;
}  /* class TextEditTest */
