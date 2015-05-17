
import java.io.*;
import java.util.Vector;

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


/******************************************************************************/
public class RemoteCommand extends Thread
{
  private String command = "echo nothing to do!";	// Remote command

  private String server_name = "";		// compute server name

  private boolean status = false;		// not done yet


/******************************************************************************/
  public RemoteCommand ( String compute_server_name, String cmd )
  {
    // Set the name of the thread for display purposes.
    server_name = compute_server_name;

    setName ( server_name );

    command = cmd;

    status = false;
  }  // constructor RemoteCommand


/******************************************************************************/
  public boolean getStatus ()
  {
    return status;
  }  // method getStatus


/******************************************************************************/
  private Vector ignoreGCG ( Vector results )
  {
    // Check for results.
    if ( ( results == null ) || ( results.size () <= 0 ) )  return results;

    // Traverse the results checking for GCG messages.
    boolean in_gcg_message = false;
    int i = 0;
    while ( i < results.size () )
    {
      String line = (String) results.elementAt ( i );

      if ( line.indexOf ( "WISCONSIN PACKAGE" ) >= 0 )  in_gcg_message = true;

      // System.out.println ( "ignoreGCG: in_gcg: " + in_gcg_message + " " + line );

      if ( in_gcg_message == true )  results.removeElementAt ( i );

      if ( line.indexOf ( "Wisconsin Package" ) >= 0 )  in_gcg_message = false;

      if ( in_gcg_message == false )  i++;
    }  // if

    return results;
  }  // if


/******************************************************************************/
  private Vector drain ( BufferedReader in )
  {
    Vector results = new Vector ();

    try
    {
      while ( in.ready () == true )
      {
        String line = in.readLine ();
  
        if ( line.length () > 0 )
        
          results.add ( line );
      }  // while
    }  // try
    catch ( IOException e )
    {
      System.out.println ( "drain error: " + e );
    }  // catch

    // Ignore GCG messages.
    results = ignoreGCG ( results );

    // Print the results.
    if ( results.size () > 0 )

      for ( int i = 0; i < results.size (); i++ )

        System.out.println ( server_name + ": " + (String) results.elementAt ( i ) );

    return results;
  }  // method drain


/******************************************************************************/
  private Vector getMessages ( Process exeProcess )
  {
    Vector out_msgs = null;

    if ( exeProcess != null )
    {
      // Consume the standard err results.
      BufferedReader std_err = new BufferedReader ( new InputStreamReader ( exeProcess.getErrorStream () ) );
      Vector err_msgs = drain ( std_err );
      try
      {
        std_err.close ();
      }  // try
      catch ( IOException e )
      {
        System.out.println ( "std_err close error: " + e );
      }  // catch

      // Consume the standard output results.
      BufferedReader std_out = new BufferedReader ( new InputStreamReader ( exeProcess.getInputStream () ) );
      out_msgs = drain ( std_out );
      try
      {
        std_out.close ();
      }  // try
      catch ( IOException e )
      {
        System.out.println ( "std_out close error: " + e );
      }  // catch
    }  // if

    return out_msgs;
  }  // method getMessages


/******************************************************************************/
  // Override Thread class run method to get the work done
  public void run ()
  {
    status = false;
    Process exeProcess = null;
    Runtime runtime = Runtime.getRuntime ();

    // System.out.println ( command );

    try
    {
      exeProcess = runtime.exec ( command );
    }
    catch ( IOException e )
    {
      System.out.println ( "exeProcess error: " + e );
    }  // catch

    try
    {
      int exit_value = exeProcess.waitFor ();

      if ( exit_value != 0 )
      {
        System.out.println ( "Abnormal exit value: " + exit_value );
      }  // if
    }  // try
    catch ( InterruptedException e )
    {
      System.out.println ( "exeProcess error: " + e );
    }  // catch

    // Flush the standard output and standard err files.
    getMessages ( exeProcess );

    status = true;

    // System.out.println ( server_name + " done" );
  }  // method run


/******************************************************************************/

}  // class RemoteCommand

