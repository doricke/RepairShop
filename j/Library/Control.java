import java.io.*;
// import java.applet.*;
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

/**************************************************************************/
// public class Control extends Applet implements ActionListener
public class Control extends Thread
{
  Frame controlFrame;
  String name;

  public Control ()
  {  
    System.out.println ( "Control.constructor method called." );
  }  /* Control */
  
  public void init ()
  {
    System.out.println ( "Control.init method called." );

    name = "Control Class name";

    controlFrame.resize ( 300, 300 );
    controlFrame.setTitle ( "Control Frame Title" );
    controlFrame.show ();
    
    MenuBar controlMenuBar = new MenuBar ();
    
    controlFrame.setMenuBar ( controlMenuBar );
    Menu fileMenu = new Menu ( "File" );
    controlMenuBar.add ( fileMenu, true );
    
    fileMenu.add ( "Open" );
    
    MenuItem saveMenuItem = new MenuItem ( "Save" );
    fileMenu.add ( saveMenuItem );
    saveMenuItem.disable ();
    
    fileMenu.addSeparator ();
    
    Menu printObject = new Menu ( "Print" );
    fileMenu.add ( printObject );
    printObject.add ( "Print Sequence" );
    printObject.add ( "Print Sequence Map" );
    
    fileMenu.add ( "Quit" );
  }  /* init */
  
  public boolean action ( Event evt, Object arg )
  {
    System.out.println ( "action arg = '" + arg + "'" );

    if ( evt.target instanceof MenuItem )
    {
      if ( arg.equals ( "Quit" ) )  System.exit ( 0 );
    }
    else  return false;
    
    return true;
  }  /* action */
   
  public void actionPerformed ( ActionEvent event )
  {
      if ( event.getSource () == saveMenuComponent )
      {
        System.out.println ( "Save menu component called." );
      }  /* if */
      else
        System.out.println ( "actionPerformed " + event.getSource () );   
   }  /* actionPerformed */
  
  public void run ()
  {
    System.out.println ( "Control.run method called." );
  }  /* run */
  
  protected void finalize ()
  {
    System.out.println ( "Control.finalize method called." );
    controlFrame.dispose ();
  }  /* finalize */
  
  public void main ()
  {
    control = new Control ();
    
    System.out.println ( "End of main method." );
  }
  
}  /* class Control */


