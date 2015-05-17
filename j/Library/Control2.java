import java.io.*;
// import java.applet.*;
import java.awt.*;
import Sequence;
import FileLister;
import ViewFile;

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
public class Control extends Frame
{


  /************************************************************************/
  public Control ()
  {
    this.init ();
  }  /* Control */
  
  
  /************************************************************************/
  /* Applet method called the first time this applet is called. */
  public void init ()
  {    
    MenuBar controlMenuBar = new MenuBar ();
    Menu fileMenu = new Menu ( "File" );
//    this.add ( fileMenu, true );
    
    fileMenu.add ( new MenuItem ( "New Sequence" ) );
    fileMenu.add ( new MenuItem ( "Select Files" ) );
    
    MenuItem openMenuItem = new MenuItem ( "Open Sequence" );
    fileMenu.add ( openMenuItem );

    fileMenu.add ( new MenuItem ( "Edit Sequence" ) );    
    fileMenu.add ( new MenuItem ( "Save Sequence" ) );
    MenuItem saveMenuItem = new MenuItem ( "Save Sequence As" );
    fileMenu.add ( saveMenuItem );
    saveMenuItem.disable ();    
    fileMenu.addSeparator ();
    fileMenu.add ( "Quit" );
    controlMenuBar.add ( fileMenu );
    
    
    Menu printObject = new Menu ( "Print" );
    printObject.add ( "Print Sequence" );
    printObject.add ( "Print Sequence Map" );
    printObject.addSeparator ();
    printObject.add ( new CheckboxMenuItem ( "Test" ) );
    printObject.add ( new CheckboxMenuItem ( "Check" ) );
    controlMenuBar.add ( printObject );
    
    setMenuBar ( controlMenuBar );
    
    selectedFiles = null;
  }  /* init */


  /************************************************************************/  
  public static void GetFile ( String filename )
  {
    System.out.println ( "GetFile method called with filename '" + filename + "'" );
    
    FileDialog d = new FileDialog 
        ( controlFrame, "Select a file", FileDialog.LOAD );
    d.setDirectory ( "." );
    d.setFile ( "*.java" );
    d.show ();
    
    filename = d.getFile ();
    fileName = filename;

    if ( filename != null )
      System.out.println ( "File name '" + filename + "'" );
  }  /* GetFile */
  
  
  /************************************************************************/
  public static void SaveFile ( String filename )
  {
    System.out.println ( "SaveFile method called with filename '" + filename + "'" );
    
    FileDialog d = new FileDialog 
        ( controlFrame, "Select a file", FileDialog.SAVE );     
    d.setDirectory ( "." );
    d.setFile ( filename );
    d.show ();    
    filename = d.getFile ();
                                         
    if ( filename != null )
      System.out.println ( "SaveFile: filename = '" + filename + "'" );
  }  /* SaveFile */


  /************************************************************************/
  /* Applet method called after init() and each time browser returns to page. */
  public void start ( )
  {
    System.out.println ( "Control.start () called." );
  }  /* start */
  
  
  /************************************************************************/
  /* Applet method called any time that a browser leaves a Web page. */
  public void stop ()
  {
    System.out.println ( "Control.stop () called." );
  }  /* stop */
  
  
  /************************************************************************/
  /* Applet method called before browser completely shuts down. */
  public void destroy ()
  {
    System.out.println ( "Control.destroy () called." );
  }  /* destroy */
      
  
  /************************************************************************/
  public boolean action ( Event evt, Object arg )
  {
    // System.out.println ( "action arg = '" + arg + "'" );

    if ( evt.target instanceof MenuItem )
    {
      if ( arg.equals ( "Quit" ) )  System.exit ( 0 );
  
      if ( arg.equals ( "Open Sequence" ) )
      {
        GetFile ( fileName );
        ViewFile viewFile = new ViewFile ( fileName );
      }  /* if */
      
      if ( arg.equals ( "Select Files" ) )
      {
        FilenameFilter filter = null;
        
        getList = new FileLister 
            ( System.getProperty ( "user.dir" ), filter );
       
        // this.hide ();
            
        selectedFiles = getList.getSelected ();
        this.add ( "Center", selectedFiles );
        this.show (); 
      
        return true;
      }  /* if */
      
      if ( arg.equals ( "Save Sequence" ) )
        SaveFile ( fileName );
      
      if ( arg.equals ( "Print Sequence" ) )
        System.out.println ( "Print menu option selected." );
        
      if ( arg.equals ( "New Sequence" ) )
        System.out.println ( "New menu option selected." );
    }
    else  return false;
    
    return true;
  }  /* action */
  
  
  /************************************************************************/
  public boolean postEvent ( Event evt, Object arg )
  {
    // System.out.println ( "Control.postEvent: evt.id = " + evt.id );
    // System.out.println ( "Control.postEvent arg = '" + arg + "'" );
  
    if ( getList == null )  return super.postEvent ( evt );
  
    if ( evt.id == Event.GOT_FOCUS )
    {     
      System.out.println ( "Control.postEvent GOT_FOCUS" );  
      selectedFiles = getList.getSelected ();
      getList.dispose ();
      this.add ( "Center", selectedFiles );
      this.show ();
      return true;
    }  /* if */
    
    return super.postEvent ( evt );
  }  /* method postEvent */
  
  
  /************************************************************************/
  public boolean handleEvent ( Event evt )
  {
//    System.out.println ( "handleEvent called." );
//    System.out.println ( "Message: " + evt.getMessage () );
//    System.out.println ( evt.toString () );
//    evt.printStackTrace ();
//    System.out.println ( );

    if ( evt.target == this )
    {
      switch ( evt.id )
      {
        case Event.WINDOW_DESTROY:
          // System.out.println ( "WINDOW_DESTROY" );
          // Pass the destroy event along to the components.
          Component c[] = getComponents ();
          for ( int i=0; i < c.length; i++ )
            c[i].handleEvent ( evt );
      
          // Destroy the current Frame.
          controlFrame.hide ();
          this.dispose ();
          this.stop ();
          this.destroy ();
          System.exit ( 0 );
          return false;           // return true;
        
        case Event.WINDOW_ICONIFY:
          System.out.println ( "WINDOW_ICONIFY" );
          controlFrame.hide ();
          return false;
        
        default:
          return super.handleEvent ( evt );        
      }  /* switch */
    }  /* if */
    
    return super.handleEvent ( evt );
  }  /* handleEvent */

  
  /************************************************************************/ 
/*  public void actionPerformed ( ActionEvent event )
  {
      if ( event.getSource () == saveMenuComponent )
      {
        System.out.println ( "Save menu component called." );
      }
      else
        System.out.println ( "actionPerformed " + event.getSource () );   
  }  /* actionPerformed */
  
  
  /************************************************************************/
  public void run ()
  {
    System.out.println ( "Control.run method called." );
  }  /* run */
  
  
  /************************************************************************/
  protected void finalize ()
  {
    System.out.println ( "Control.finalize method called." );
    this.dispose ();
  }  /* finalize */
  
  
  /************************************************************************/
  static public void main ( String [] args )
  {
    fileName = "HUMCERP.SEQ";

    controlFrame = new Control ();
    controlFrame.resize ( 300, 300 );
    controlFrame.setTitle ( "Control Frame Title" );
    controlFrame.show ();
  }  /* main */
  
  /************************************************************************/
  static private Frame controlFrame;
  static private FileLister getList = null; 
  static private String fileName;  
  static private List selectedFiles;
  
}  /* class Control */


