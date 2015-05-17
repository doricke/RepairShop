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

public class MenuTest extends Frame
{
  private String menufilename;


  public MenuTest ( String filename )
  {
    System.out.println ( "MenuTest: filename = '" + filename + "'" );
    fileName = filename;
  
    setTitle ( "MenuTest" );
    
    MenuBar mbar = new MenuBar ();
    Menu m = new Menu ( "File" );
    m.add ( new MenuItem ( "New" ) );
    m.add ( new MenuItem ( "Open" ) );
    m.addSeparator ();

    m.add ( new MenuItem ( "Save" ) );
/*
    m.add ( new MenuItem ( "Save As" ) );
    m.addSeparator ();
*/
    m.add ( new MenuItem ( "Print" ) );
    m.addSeparator ();
    m.add ( new MenuItem ( "Quit" ) );
    mbar.add ( m );
    m.addSeparator ();
    
/*
    Menu f = new Menu ( "Options" );
    f.add ( new CheckboxMenuItem ( "Insert mode" ) );
    f.add ( new CheckboxMenuItem ( "Auto indent" ) );
    m.add ( f );
    mbar.add ( m );
    
    m = new Menu ( "Help" );
    m.add ( new MenuItem ( "Index" ) );
    m.add ( new MenuItem ( "About" ) );
    mbar.add ( m );
*/    
    setMenuBar ( mbar );
  }  /* MenuTest */
  
  public static void GetFile ( String filename )
  {
    System.out.println ( "GetFile method called with filename '" + filename + "'" );
    
    FileDialog d = new FileDialog 
        ( f, "Select a file", FileDialog.LOAD );
    d.setDirectory ( "." );
    d.setFile ( "*.java" );
    d.show ();
    
    filename = d.getFile ();
    fileName = filename;

    if ( filename != null )
      System.out.println ( "File name '" + filename + "'" );
  }  /* GetFile */
  
  public static void SaveFile ( String filename )
  {
    System.out.println ( "SaveFile method called with filename '" + filename + "'" );
    
    FileDialog d = new FileDialog 
        ( f, "Select a file", FileDialog.SAVE );     
    d.setDirectory ( "." );
    d.setFile ( filename );
    d.show ();    
    filename = d.getFile ();
                                         
    if ( filename != null )
      System.out.println ( "SaveFile: filename = '" + filename + "'" );
  }  /* SaveFile */
  
  public boolean action ( Event evt, Object arg )
  {
    System.out.println ( "action method called arg = '" + arg + "'" );
    
    if ( evt.target instanceof MenuItem )
    {
      if ( arg.equals ( "Quit" ) )  System.exit ( 0 );
      
      if ( arg.equals ( "Open" ) )
        GetFile ( fileName );
      
      if ( arg.equals ( "Save" ) )
        SaveFile ( fileName );
      
      if ( arg.equals ( "Print" ) )
        System.out.println ( "Print menu option selected." );
        
      if ( arg.equals ( "New" ) )
        System.out.println ( "New menu option selected." );
    }
    else  return false;
    
    return true;
  }  /* action */
  
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY
        && evt.target == this )
      System.exit ( 0 );
      
    return super.handleEvent ( evt );
  }  /* handleEvent */
  
  public static void main ( String args [] )
  {
    fileName = "Test2.file";
    f = new MenuTest ( fileName );
    f.resize ( 300, 200 );
    f.show ();
  }  /* main */

  static private Frame f;
  static private String fileName;
}  /* class MenuTest */
