import java.io.*;		// For input & output classes

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

public class TryFile
{
  private void directory ( String dirName )
  {
    // Create an object that is a directory.

    File myDir = new File ( dirName );

    System.out.println ( myDir + (myDir.isDirectory() ? " is" : " is not") + " a directory." );
  }  // method directory


  private void file ( String fileName )
  {
    // Create an object that is a file.
    File myFile = new File ( fileName );

    System.out.println ( );
    System.out.println ( myFile + (myFile.exists() ? " does" : " does not") + " exist" );
    System.out.println ( "You can" + (myFile.canRead() ? " " : "not ") + "read " + myFile );
    System.out.println ( "You can"+ (myFile.canWrite() ? " " : "not ") + "write " + myFile );
  }  // method file

  public static void main ( String [] args )
  {
    TryFile tryFile = new TryFile ();

    tryFile.directory ( "." );

    tryFile.file ( "TryJava.java" );
    tryFile.file ( "TryFile.java" );
  }  // method main
}  // class TryFile

