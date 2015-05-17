
import java.util.Vector;
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


/******************************************************************************/
public class Command
{
  private String command = "echo nothing to do!";	// Remote command


/******************************************************************************/
  public Command ()
  {
  }  // constructor Command


/******************************************************************************/
  public void setCommand ( String new_command )
  {
    command = new_command;
  }  // method setCommand


/******************************************************************************/
  private void printResults ( Vector results )
  {
    // Check for results.
    if ( ( results == null ) || ( results.size () <= 0 ) )  return;

    // Traverse the results.
    for ( int i = 0; i < results.size (); i++ )

      System.out.println ( (String) results.elementAt ( i ) );

  }  // method printResults


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

    // printResults ( results );

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
  public Vector run ( String new_command )
  {
    command = new_command;
    return run ();
  }  // method run


/******************************************************************************/
  // Override Thread class run method to get the work done
  public Vector run ()
  {
    Process exeProcess = null;
    Runtime runtime = Runtime.getRuntime ();

    // System.out.println ( "Attempting: " + command );

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
    return getMessages ( exeProcess );
  }  // method run


/******************************************************************************/
  public static void main ( String [] args )
  {
    Command app = new Command ();

    app.printResults ( app.run ( "pwd" ) );
    app.printResults ( app.run ( "ls" ) );
    app.printResults ( app.run ( "date" ) );
  }  // method main


/******************************************************************************/

}  // class Command

