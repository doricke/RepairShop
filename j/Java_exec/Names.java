
import java.util.Vector;


/******************************************************************************/
/**
 *
 * @author	Darrell O. Ricke, PhD
 * @version	1.0
 */

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

public class Names
{
  private Vector active = new Vector ();

  private OutputTools completed_file = new OutputTools ();

  private Vector names = new Vector ();

  private InputTools names_file = new InputTools ();	// Names file

  private String names_file_name = null;	// Input file name of names to process

  private int queue_position = 0;		// position in the queue


/******************************************************************************/
  public Names ()
  {
  }  // constructor Names 

 
/******************************************************************************/
  private void done ()
  {
    completed_file.closeFile ();

    names_file.closeFile ();

    System.out.println ( "All done!" );
    System.exit ( 0 );
  }  // method done

 
/******************************************************************************/
  public synchronized String getNextName ( String server_name )
  {
    // System.out.println ( "getNextName called" );

    if ( names == null )  return "";

    // Check if the end of the block of names had been reached.
    if ( names.size () <= 0 )  readBlockOfNames ();

    if ( names.size () <= 0 )  
    {
      // Check if any names remain in the active queue.
      if ( active.size () <= 0 )  

        done ();

      // Check for older, orphan requests.
      if ( orphanFound () == false )
      {
        System.out.println ( "getNextName: No orphans found:" );
        printActive ();
        return "";
      }  // if
    }  // if

    queue_position++;
    String name = (String) names.elementAt ( 0 );
    names.removeElementAt ( 0 );

    // Create a new ActiveName
    ActiveName active_name = new ActiveName ( server_name, name, queue_position );

    active.add ( active_name );

    // System.out.println ( "getNextName returning: " + name );
    return name;
  }  // method getNextName


/******************************************************************************/
  private void printActive ()
  {
    System.out.println ();
    System.out.println ( "Remaining active names:" );

    for ( int i = 0; i < active.size (); i++ )
    {
      ActiveName active_name = (ActiveName) active.elementAt ( i );
      System.out.println ( "\t" + active_name.getName () + "\t" + active_name.getComputeHostName () );
    }  // for

    System.out.println ();
  }  // method printActive


/******************************************************************************/
  public synchronized void failed ( String server_name, String name )
  {
    System.out.println ( server_name + " failed: " + name );
    names.add ( name );
  }  // method failed


/******************************************************************************/
  public synchronized void finished ( String server_name, String name )
  {
    System.out.println ( server_name + "\t " + name );
    removeActiveName ( name );

    if ( completed_file != null )
    {
      completed_file.println ( name );
      completed_file.flushFile ();
    }  // if

    // Check if all of the work is completed.
    if ( ( active.size () <= 0 ) && 
         ( names.size () <= 0 ) &&
         ( names_file.isEndOfFile () == true ) )  done ();

    // System.out.println ( "finished completed" );
  }  // method finished


/******************************************************************************/
  private boolean orphanFound ()
  {
    boolean found = false;

    // Search for orphan jobs.
    ActiveName active_name = null;

    for ( int i = 0; i < active.size (); i++ )
    {
      active_name = (ActiveName) active.elementAt ( i );

      // Check for an old position.
      if ( active_name.getPosition () + 100 < queue_position )
      {
        // Add the name to the queue again.
        names.add ( active_name.getName () );
        active.removeElementAt ( i );
        active_name = null;
        return true;
      }  // if

      active_name = null;
    }  // for

    return found;
  }  // method orphanFound


/******************************************************************************/
  private void removeActiveName ( String name )
  {
    ActiveName active_name = null;

    for ( int i = 0; i < active.size (); i++ )
    {
      active_name = (ActiveName) active.elementAt ( i );

      if ( name.equals ( active_name.getName () ) == true )
      {
        active.remove ( i );
        return;
      }  // if

      active_name = null;
    }  // for

    System.out.println ( "Names.removeActiveName: couldn't find active name: " + name );
  }  // method removeActiveName


/******************************************************************************/
  private void readBlockOfNames ()
  {
    // System.out.println ( "readBlockOfNames called" );

    // Check if the end of the input file has been reached.
    if ( names_file.isEndOfFile () == true )  return;

    // Read in the next 1,000 names from the Names file.
    for ( int i = 0; i < 1000; i++ )
    {
      String name = names_file.getLine ().toString ();

      // System.out.println ( "Reading " + name );

      // Check if a name was read in.
      if ( name.length () > 0 )  names.add ( name );

      // Check for end of file.
      if ( names_file.isEndOfFile () == true )  return;
    }  // for

    // System.out.println ( "readBlockOfNames exiting" );
  }  // method readBlockOfNames


/******************************************************************************/
  public void setLogFileName ( String log_file_name )
  {
    completed_file.setFileName ( log_file_name );
    completed_file.openFile ();
  }  // method setLogFileName


/******************************************************************************/
  public void setNamesFileName ( String value )
  {
    names_file_name = value;

    names_file.setFileName ( value );
    names_file.openFile ();

    // Read in the first block of names.
    readBlockOfNames ();
  }  // method setNamesFileName

}  // class Names
