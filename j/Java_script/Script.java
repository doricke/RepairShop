
import InputTools;
import OutputTools;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

/******************************************************************************/

public class Script extends Object
{

/******************************************************************************/

  private  OutputTools []  cmds = null;		// Output commands files

  private  String  directory_path = "";		// Directory path

  private  InputTools  list_file = new InputTools ();	// Input file

  private  int first_node = 33;			// First node to use

  private  int last_node = first_node + 7;	// Last node to use

  private  String  program_name = "tfasty33_t";	// FASTA program name


/******************************************************************************/
public Script ()
{
  initialize ();
}  // constructor Script


/******************************************************************************/
public void initialize ()
{
  first_node = 33;
  last_node  = first_node + 7;
  directory_path = "";
}  // method initialize


/******************************************************************************/
public void close ()
{
  // Close the input list of file names file.
  list_file.closeFile ();

  // Check if any commands files were created.
  if ( cmds == null )  return;

  for ( int i = 0; i < cmds.length; i++ )
  {
    cmds [ i ].closeFile ();
    cmds [ i ] = null;
  }  // for

  cmds = null;
}  // method close


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
  private void processFile ()
  {
    StringBuffer line = list_file.getLine ();
    int node = first_node;

    cmds [ 0 ].println ( "mkdir /bio" + directory_path );

    // Process the list of sequence file names.
    while ( list_file.isEndOfFile () == false )
    {
      String file_name = line.toString ();

      // Check for a file name.
      if ( line.length () > 0 )
      {
        cmds [ node - first_node ].println 
            ( "bpsh "
            + node
            + " "
            + program_name
            + " -o -b 20 -d 20 -Q -z -1 -m 0 "
            + directory_path
            + file_name
            + " %M ktup 2 > "
            + directory_path
            + file_name
            + "_-"
            + program_name
            );

        cmds [ node - first_node ].println 
            ( "mv "
            + directory_path
            + file_name
            + "_-"
            + program_name
            + " "
            + "/bio"
            + directory_path
            ); 

        cmds [ node - first_node ].println ( "echo " + file_name + " complete." );

        // cmds [ node - first_node ].println ( "rm " + file_name );
      }  // if

      line = list_file.getLine ();
      node++;
      if ( node > last_node )  node = first_node;
    }  // while
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "usage called" );
  }  // method usage


/******************************************************************************/
  private void crackParameters ( String [] args )
  {
    // Check for no parameters.
    if ( args.length <= 0 )  
    {
      usage ();
      return;
    }  // if

    // Check for the detailed path name to use.
    if ( args.length >= 1 )
    {
      int index = args [ 0 ].lastIndexOf ( "/" );
      if ( index == -1 )
      {
        list_file.setFileName ( args [ 0 ] );
      }  // if
      else
      {
        list_file.setFileName ( args [ 0 ].substring ( index + 1 ) );
        directory_path = args [ 0 ].substring ( 0, index + 1 );
      }  // else

      list_file.openFile ();
    }  // if

    // Check for the first node number to use.
    if ( args.length >= 2 )
    {
      first_node = InputTools.getInteger ( args [ 1 ] );
    }  // if

    // Check for the last node number to use.
    if ( args.length >= 3 )
    {
      last_node = InputTools.getInteger ( args [ 2 ] );
    }  // if

    // Allocate the commands files.
    if ( ( first_node >= 0 ) && ( first_node <= 62 ) &&
         ( last_node  >= 0 ) && ( last_node  <= 62 ) &&
         ( first_node <= last_node ) )
    {
      // Create the output commands files.
      cmds = new OutputTools [ last_node - first_node + 1 ];

      String name_prefix = list_file.getFileName ();
      int period = name_prefix.indexOf ( '.' );
      if ( period > 0 )  name_prefix = name_prefix.substring ( 0, period );
 
      for ( int i = first_node; i <= last_node; i++ )
      {
        cmds [ i - first_node ] = new OutputTools ();
        cmds [ i - first_node ].setFileName ( name_prefix + ".cmds_" + i );
        cmds [ i - first_node ].openFile ();
      }  // for
    }  // if
  }  // method crackParameters


/******************************************************************************/
  public static void main ( String [] args )
  {
    Script app = new Script ();

    // Parse the command line parameters.
    app.crackParameters ( args );

    // Process the input file.
    app.processFile ();

    app.close ();
  }  // method main

}  // class Script

