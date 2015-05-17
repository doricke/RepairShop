import java.io.*;

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

public class JavaIO extends Object
{
  public JavaIO ()
  {  
  }  /* JavaIO */

  public static void GetFileNames ( String path, String [] currFiles )
  {
    // Create a File instance for the current directory
    File currDir = new File ( path );
    
    // Get a filtered list of the files in the current directory
    currFiles = currDir.list ();
    
//    ListNames ( currFiles );
   }  /* GetFileNames */
  
  public static void ListNames ( String [] nameList )
  {
    // Print out the contents of the nameList array
    for ( int i = 0; i < nameList.length; i++ )
    {
      System.out.println ( nameList [ i ] );
    }  /* for */
  }  /* ListNames */

  public static void ReadFile ( String filename )
  {
    try
    {
      DataInputStream in_file = new DataInputStream 
          ( new FileInputStream ( filename ) );
    
      /* Read in the entire file. */
      boolean end_of_file = false;
      int i = 0;
      while ( ( end_of_file == false ) && ( i < 20 ) )
      {
        String line;
        
        try
        {
          line = in_file.readLine ();
          
          if ( line == null )
            end_of_file = true;
          else
            System.out.println ( line );
        }
        catch ( EOFException e1 )
        {
          System.out.println ( "End of file reached." + e1 );
          end_of_file = true;
        }  /* catch */
        catch ( IOException e2 )
        {
          System.out.println ( "IOException on input file." + e2 );
        }  /* catch */
        
        i++;  
      }  /* while */ 
      
      // Close the input file.
      try
      {
        in_file.close ();
      }
      catch ( IOException e4 )
      {
        System.out.println ( "IOException on clone file " + e4 );
      }  /* catch */ 
    }
    catch ( FileNotFoundException e3 )
    {
      System.out.println ( "File not found '" + filename + "'" + e3 );
      return;
    }  /* catch */
  }  /* ReadFile */

  public static void main ( String [] args )
  {
    File currDir = new File ( "." );
    String [] fileList = currDir.list ();
//    ListNames ( fileList );
    System.out.println ( "----------------------------------------------" );
    
    // Get the list of file names for the specified directory
    GetFileNames ( "..", fileList );
    
    // List the names in fileList
//    ListNames ( fileList );

    // Read in a test file. 
    ReadFile ( "Test.file" );

    // Make a subdirectory "testDir".
//    File newDir = new File ( "testDir" );
//    newDir.mkdir ();

    control = new Control ();

  }  /* main */

}  /* class JavaIO */

/**************************************************************************/
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
    controlFrame.resize ( 300, 300 );
    controlFrame.setTitle ( "Control Frame Title" );
    controlFrame.show ();
    
    MenuBar controlMenuBar = new MenuBar ();
    
    controlFrame.setMenuBar ( controlMenuBar );
    Menu fileMenu = new Menu ( "File" );
    controlMenuBar.add ( fileMenu, allowTearoff );
    
    fileMenu.add ( "Open" );
    
    MenuItem saveMenuItem = new MenuItem ( "Save" );
    fileMenu.add ( saveMenuItem );
    
    fileMenu.addSeparator ();
    
  }  /* init */
  
  public void run ()
  {
    System.out.println ( "Control.run method called." );
  }  /* run */
  
  protected void finalize ()
  {
    System.out.println ( "Control.finalize method called." );
    controlFrame.dispose ();
  }  /* finalize */
  
}  /* class Control */


