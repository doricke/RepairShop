
import java.io.*;
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
public class ViewFile extends Frame
{
  /************************************************************************/
  public ViewFile ()
  {
    System.out.println ( "ViewFile.constructor method called." );
    this.init ();
  }  /* ViewFile */


  /************************************************************************/
  public ViewFile ( String filename )
  {
    System.out.println ( "ViewFile.constructor filename = " + filename );
    fileName = filename;
    this.init ();
    fileViewer ( filename );
  }  /* ViewFile */
    
  
  /************************************************************************/
  private protected void init ()
  {
    System.out.println ( "ViewFile.init method called." );

    this.resize ( 300, 300 );
    this.setTitle ( "ViewFile Frame Title" );
    this.show ();    
    
    MenuBar viewMenuBar = new MenuBar ();
    Menu fileMenu = new Menu ( "File" );
    
    fileMenu.add ( new MenuItem ( "New Sequence" ) );
    
    MenuItem openMenuItem = new MenuItem ( "Open Sequence" );
    fileMenu.add ( openMenuItem );
    MenuItem listMenuItem = new MenuItem ( "List Sequences" );
    fileMenu.add ( listMenuItem );

    fileMenu.add ( new MenuItem ( "Edit Sequence" ) );    
    fileMenu.add ( new MenuItem ( "Save Sequence" ) );
    MenuItem saveMenuItem = new MenuItem ( "Save Sequence As" );
    fileMenu.add ( saveMenuItem );
    saveMenuItem.disable ();    
    fileMenu.addSeparator ();
    fileMenu.add ( "Close" );
    viewMenuBar.add ( fileMenu );
    
    
    Menu printObject = new Menu ( "Print" );
    printObject.add ( "Print Sequence" );
    printObject.add ( "Print Sequence Map" );
    printObject.addSeparator ();
    printObject.add ( new CheckboxMenuItem ( "Test" ) );
    printObject.add ( new CheckboxMenuItem ( "Check" ) );
    viewMenuBar.add ( printObject );
    
    setMenuBar ( viewMenuBar );
  }  /* init */


  /************************************************************************/  
  private protected static void getFile ( String filename )
  {
    System.out.println ( "GetFile method called with filename '" + filename + "'" );
    
    FileDialog d = new FileDialog 
        ( viewFrame, "Select a file", FileDialog.LOAD );
    d.setDirectory ( "." );
    d.setFile ( "*.java" );
    d.show ();
    
    filename = d.getFile ();
    fileName = filename;

    if ( filename != null )
      System.out.println ( "File name '" + filename + "'" );
  }  /* getFile */
  
  
  /************************************************************************/
  private protected static void saveFile ( String filename )
  {
    System.out.println ( "SaveFile method called with filename '" + filename + "'" );
    
    FileDialog d = new FileDialog 
        ( viewFrame, "Select a file", FileDialog.SAVE );     
    d.setDirectory ( "." );
    d.setFile ( filename );
    d.show ();    
    filename = d.getFile ();
                                         
    if ( filename != null )
      System.out.println ( "SaveFile: filename = '" + filename + "'" );
  }  /* saveFile */


  /************************************************************************/
  private protected void fileViewer ( String filename )
  {
    this.setTitle ( "FileViewer: " + filename );
    File f = new File ( filename );
    int size = (int) f.length ();
    int bytes_read = 0;
    
    try
    {
      FileInputStream in = new FileInputStream ( f );
      byte [] data = new byte [ size ];
      while ( bytes_read < size )
        bytes_read += in.read ( data, bytes_read, size - bytes_read );
          
      TextArea textarea = new TextArea ( new String ( data, 0 ), 24, 80 );
      textarea.setFont ( new Font ( "Helvetica", Font.PLAIN, 12 ) );
      textarea.setEditable ( false );
      this.add ( "Center", textarea );
        
      viewClose = new Button ( "Close" );
      this.add ( "South", viewClose );
      this.pack ();
      this.show ();
    }
    catch ( IOException e )
    {
      System.out.println ( "FileViewer IOException: " + e );
    }  /* catch */
  }  /* fileViewer method */
      
  
  /************************************************************************/
  public boolean action ( Event evt, Object arg )
  {
    System.out.println ( "ViewFile: action arg = '" + arg + "'" );

    if ( evt.target == viewClose )
    {
      this.hide ();
      this.dispose ();
      return true;
    }  /* if */

    if ( evt.target instanceof MenuItem )
    {
      if ( arg.equals ( "Close" ) )
      {
        this.hide ();
        this.dispose ();
        return true;
      }  /* if */
  
      if ( arg.equals ( "Open Sequence" ) )
      {
        getFile ( fileName );
        fileViewer ( fileName );
      }  /* if */
      
      if ( arg.equals ( "Save Sequence" ) )
        saveFile ( fileName );
        
/*      if ( arg.equals ( "List Sequences" ) )
        FileLister fileLister = new FileLister ();
*/
    }
    else  return false;
    
    return true;
  }  /* action */
  
  
  /************************************************************************/
  public boolean handleEvent ( Event evt )
  {
    if ( evt.target == this )
    {
      switch ( evt.id )
      {
        case Event.WINDOW_DESTROY:
          System.out.println ( "WINDOW_DESTROY" );
          // Pass the destroy event along to the components.
          Component c[] = getComponents ();
          for ( int i=0; i < c.length; i++ )
            c[i].handleEvent ( evt );
      
          // Destroy the current Frame.
          this.hide ();
          this.dispose ();
          return true;
        
        case Event.WINDOW_ICONIFY:
          System.out.println ( "WINDOW_ICONIFY" );
          this.hide ();
          return false;
        
        default:
          return super.handleEvent ( evt );        
      }  /* switch */
    }  /* if */
    
    return super.handleEvent ( evt );
  }  /* handleEvent */

  
  /************************************************************************/
  static public void main ( String [] args )
  {
    fileName = "HUMCERP.SEQ";

    viewFrame = new ViewFile ();

    System.out.println ( "End of main method." );
  }  /* main */
  
  /************************************************************************/
  static private String fileName;
  static private Frame viewFrame;
  Button viewClose;
  
}  /* class ViewFile */


