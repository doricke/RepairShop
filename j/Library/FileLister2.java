import java.awt.*;
import java.io.*;
import Control;

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

public class FileLister extends Control
{
  private boolean active = false;
  private List dir_list;
  private List selected;
  private List names;
  private int list_items;
  private TextField infoarea;
  private Panel buttons;
  private Button parent, close;
  private FilenameFilter filter;
  private File cwd;
  private String [] entries;
  

  /************************************************************************/
  // Create the graphical user interface, and list the initial directory.
  public FileLister ( String directory, FilenameFilter filter )
  {
    this.setTitle ( "File Lister" );
    active = true;
    this.filter = filter;
    dir_list = new List ( 12, false );
    selected = new List ( 12, false );
    names = new List ( 12, false );
    infoarea = new TextField ();
    infoarea.setEditable ( false );
    buttons = new Panel ();
    parent = new Button ( "Up a Directory" );
    close = new Button ( "Close" );
    buttons.add ( parent );
    buttons.add ( close );
    this.add ( "West", dir_list );
    this.add ( "Center", selected );
    this.add ( "East", names );
    this.add ( "South", infoarea );
    this.add ( "North", buttons );
    this.pack ();
    this.resize ( 550, 350 );
    this.show ();
    
    list_items = 0;

    // list the initial directory.
    list_directory ( directory );
  }  /* method FileLister */


  /************************************************************************/
  public boolean getActive ( )
  {
    return active;
  }  /* method getActive */
  

  /************************************************************************/
  public List getSelected ( )
  {
    return selected;
  }  /* method getSelected */
  

  /************************************************************************/
  // This method uses the list() method to get all entries in a directory
  // and then displays them in the List component.
  public void list_directory ( String directory )
  {
    File dir = new File ( directory );

    if ( ! dir.isDirectory () )
      System.out.println ( "FileLister: no such directory: " + directory );

    // dir_list.clear ();
    if ( list_items > 0 )
      dir_list.delItems ( 0, list_items - 1 );
      
    cwd = dir;
    this.setTitle ( directory );

    entries = cwd.list ( filter );
    list_items = entries.length;
    for ( int i = 0; i < entries.length; i++ )
      dir_list.addItem ( entries [ i ] );
  }  /* method list_directory */


  /************************************************************************/
  // This method uses various File methods to obtain information about
  // a file or directory.  Then it displays that info in a TextField.
  public void show_info ( String filename ) throws IOException
  {
    File f = new File ( cwd, filename );
    String info;

    if ( ! f.exists () )
      throw new IllegalArgumentException ( "FileLister.show_info(): " +
          "no such file or directory" );

    if ( f.isDirectory () ) info = "Directory:";
    else
    {
      info = "File: ";
      selected.addItem ( f.getAbsolutePath () );
      names.addItem ( f.getName () );
    }  /* else */

    info += filename + "  ";

    info += ( f.canRead () ? "read  ":"    " ) +
            ( f.canWrite() ? "write ":"    " ) +
            f.length () + "  " +
            new java.util.Date ( f.lastModified () );

    infoarea.setText ( info );
    
    return;
  }  /* method show_info */


  /************************************************************************/
  // This method handles the buttons and list events.
  public boolean handleEvent ( Event e )
  {
    if ( e.target == close )
    { 
      // Signal the calling method.      System.exit ( 0 );
      active = false;
      this.hide ();
      
      Event event = new Event ( this, Event.GOT_FOCUS, "FileLister Done" );
      deliverEvent ( event );
      return true;
    }
    else
      if ( e.target == parent )
      {
        String parent = cwd.getParent ();
        if ( parent == null )  parent = "..";    // Bug workaround
        if ( parent.equals ( "C:" ) )  parent = "C:\\";
                
        list_directory ( parent );
        return true;
      }  /* if */
      else
        if ( e.target == dir_list )
        {
          // when an item is selected, show its info.
          if ( e.id == Event.LIST_SELECT )
          {
            try 
            {
              int item = ( ( Integer ) e.arg ).intValue ();
              
              show_info ( entries [ item ] );
              return true;
            }
            catch ( IOException ex ) infoarea.setText ( "I/O Error" );
          }  /* if */

          // When the user double-clicks, change to the selected directory
          // or display the selected file.
          else
            if ( e.id == Event.ACTION_EVENT )
            {
              try
              {
                String item = new File ( cwd, ( String ) e.arg ).getAbsolutePath ();
                list_directory ( item );
                new FileViewer ( item );
              }  /* try */
              catch ( IOException ex )  infoarea.setText ( "I/O Error" );
            }  /* if */
            return true;

        }  /* if */
        else
          if ( e.target == selected )
          {
            // when an item is selected, show its info.
            if ( ( e.id == Event.LIST_SELECT ) ||
                 ( e.id == Event.ACTION_EVENT ) )
            {
              int item = ( ( Integer ) e.arg ).intValue ();
             
              selected.delItem ( item );
              return true;
            }  /* if */
            else
              System.out.println ( "selected List unknown event.id: " + e.id );
              
            return super.handleEvent ( e );
          }  /* if */
          
        return super.handleEvent ( e );
  }  /* method handleEvent */


  /************************************************************************/
  public static void usage ()
  {
    System.out.println ( "Usage: java FileLIster [directory_name] " +
        "[-e file_extension]" );
    System.exit ( 0 );
  }  /* method usage */ 


  /************************************************************************/
  // Parse command line arguments and create the FileLister object.
  // If an extension is specified, create a FilenameFilter for it.
  // If no directory is specified, use the current directory.
  public static void main ( String args [] )
  {
    FileLister f;
    FilenameFilter filter = null;
    String directory = null;

    for ( int i = 0; i < args.length; i++ )
    {
      if ( args [ i ].equals ( "-e" ) )
      {
        i++;
        if ( i >= args.length ) usage ();
        filter = new EndsWithFilter ( args [ i ] );
      }  /* if */
      else
      {
        if ( directory != null )  usage ();    // Already set
        else directory = args [ i ];
      }  /* else */
    }  /* for */

    // if no directory specified, use the current directory
    if ( directory == null )  directory = System.getProperty ( "user.dir" );

    // Create the FileLister object
    f = new FileLister ( directory, filter );
  }  /* method main */

}  /* class FileLister */


////////////////////////////////////////////////////////////////////////////
// This class is a simple FilenameFilter.  It defines the required accept ()
// method to determine whether a specified file should be listed.  A file
// will be listed if its name ends with the specified extension, or if
// it is a directory.
class EndsWithFilter implements FilenameFilter
{
  private String extension;

  /************************************************************************/
  public EndsWithFilter ( String extension )
  {
    this.extension = extension;
  }  /* method EndsWithFilter */

  /************************************************************************/
  public boolean accept ( File dir, String name )
  {
    if ( name.endsWith ( extension ) )  return true;
    else return ( new File ( dir, name ) ).isDirectory ();
  }  /* method accept */
  
}  /* class EndsWithFilter */

