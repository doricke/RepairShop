
import java.io.*;
import java.util.Vector;

// import Names.*;


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

public class RemoteCoordinator extends Thread
{
  private int completed = 0;			// Number of completed jobs

  private String database_name = "Myriad_V8";	// Database name to search

  private String destination_path = "";		// Output file path location

  private String model_name = "";		// Gene Prediction model name

  private Names names = null;			// Source of file names

  private int number_of_cpus = 2;		// Number of CPUs to use

  private int problems = 0;			// Number of problem jobs

  private String program_name = "";		// Program name to use

  private String server_name = "";		// compute server name

  private String source_path = "";		// Input file path location

  private boolean success = true;		// Status flag


/******************************************************************************/
  public RemoteCoordinator 
      ( String  compute_server_name
      , int     cpu_number_of
      , String  name_program
      , String  name_database
      , String  name_model
      , String  path_source
      , String  path_destination
      , Names   name_list 
      )
  {
    // Set the name of the thread for display purposes.
    server_name = compute_server_name;

    setName ( server_name );
    // System.out.println ( server_name + " has been created" );

    number_of_cpus   = cpu_number_of;
    program_name     = name_program;
    database_name    = name_database;
    model_name       = name_model;
    source_path      = path_source;
    destination_path = path_destination;
    names            = name_list;
  }  // constructor RemoteCoordinator


/******************************************************************************/
  public void initialize ()
  {
    completed = 0;			// Number of completed jobs
    database_name = "";			// Database name to search
    destination_path = "";		// Output file path location
    model_name = "";			// Gene Prediction model name
    names = null;			// Source of file names
    number_of_cpus = 0;			// Number of CPUs to use
    problems = 0;			// Number of problem jobs
    program_name = "";			// Program name to use
    source_path = "";			// Input file path location
    success = true;			// Status flag
  }  // method initialize


/******************************************************************************/
  private void sleepy ()
  {
    try 
    {
      // System.out.println ( server_name + " is going to sleep..." );
      // Tell this thread to go to sleep for 300 milliseconds.
      sleep ( 300 );
    }  // try
    catch ( InterruptedException e )
    {
      System.out.println ( "RemoteCoordinator.run: " + e );
    }  // catch

    // System.out.println ( server_name + " woke up from sleep" );
  }  // method sleepy


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
        {
          // System.out.println ( server_name + ": " + line );

          results.add ( line );

          if ( ( line.indexOf ( "FATAL ERROR" ) >= 0 ) ||
               ( line.indexOf ( "ERROR" ) >= 0 ) ||
               ( line.indexOf ( "Command not found" ) >= 0 ) )
          {
            problems++;
            success = false;
          }  // if
        }  // if
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
  private String getBlastallName ()
  {
    // if ( server_name.startsWith ( "morpheus" ) == true )  return "cblast";

    return getShellPrefix () + getLocation () + "blastall";
  }  // method getBlastallName


/******************************************************************************/
  private String getOutputOption ()
  {
    if ( server_name.startsWith ( "morpheus" ) == true )  return " -o ";

    return " -o ";

    // return " | " + getLocation () + "chomp.pl > ";
  }  // method getOutputOption


/******************************************************************************/
  private String getShellPrefix ()
  {
    // Check for a local machine job.
    if ( ( server_name.equals ( "sgi" ) == true ) ||
         ( server_name.equals ( "sun" ) == true ) )  return "";

    return "rsh " + server_name + " ";
  }  // method getShellPrefix


/******************************************************************************/
  private String getSmpOption ()
  {
    // Check for a local machine job.
    if ( ( server_name.equals ( "sgi" ) == true ) ||
    //     ( server_name.startsWith ( "morpheus" ) == true ) ||
         ( server_name.equals ( "sun" ) == true ) )  return "";

    return " -a " + number_of_cpus;
  }  // method getSmpOption


/******************************************************************************/
  private String getDecypherCommand ( String name )
  {
    String command 
        = "dc_template_rt -priority 9 -query " 
        + name
        + " -template "
        + program_name
        + " -target " 
        + database_name
        + " -mach " 
        + server_name
        + " > "
        + program_name 
        + ".Searches/"
        + name
        + "."
        + program_name
        ;

    return command; 
  }  // method getDecypherCommand


/******************************************************************************/
  private String getBlastCommand ( String name )
  {
    // System.out.println ( server_name + " got " + name );
    String command  
        = getBlastallName ()
        + " -p " 
        + program_name 
        + " -d "
        + getLocationDb ()
        + database_name 
        + " -i " 
        + source_path 
        + name 
        + getSmpOption ()
        + " -F F"
        + getOutputOption ()
        + destination_path 
        + name 
        + "_-"
        + program_name
        + "_"
        + database_name
        ;

    // ###
    // System.out.println ( command );

    return command;
  }  // method getBlastCommand


/******************************************************************************/
  private String getFastaCommand ( String name )
  {
    char database_letter = 'M';

    // System.out.println ( server_name + " got " + name );
    String command  
        = getShellPrefix ()
        + program_name 
        + " -o -b 20 -d 20 -Q -z -1 -m 0 "
        + source_path 
        + name 
        + " %"
        + database_letter 
        + " ktup 2 "
        + " > " 
        + destination_path 
        + name 
        + "_-"
        + program_name
        ;

    return command;
  }  // method getFastaCommand


/******************************************************************************/
  private String getFgeneshCommand ( String name, String model )
  {
    // System.out.println ( server_name + " got " + name );
    String command  
        = getShellPrefix ()
        + "fgenesh /data6/progs/bin/"
        + model
        + " "
        + source_path 
        + name
        + " > " 
        + destination_path 
        + name 
        + "_-fg"
        + model
        ;

    return command;
  }  // method getFgeneshCommand


/******************************************************************************/
  private String getGenscanCommand ( String name, String model )
  {
    String command  
        = getShellPrefix ()
        + "genscan /data6/progs/genscan/"
        + model
        + ".smat "
        + source_path 
        + name
        + " -v -cds > " 
        + destination_path 
        + name 
        + "_-gs"
        + model
        ;

    return command;
  }  // method getGenscanCommand


/******************************************************************************/
  private String getGmhmmCommand ( String name, String model )
  {
    String command  
        = getShellPrefix ()
        + "gmhmme -o "
        + destination_path 
        + name 
        + "_-gmhmm"
        + model
        + " -m /data6/progs/share/matrices/athalian.mtx "
        + source_path 
        + name
        ;

    return command;
  }  // method getGmhmmCommand


/******************************************************************************/
  private String getLocation ()
  {
    if ( server_name.startsWith ( "morpheus" ) == false )  return "";

    return "~/bin/";
  }  // method getLocation


/******************************************************************************/
  private String getLocationDb ()
  {
    if ( server_name.startsWith ( "morpheus" ) == true )  return "/usr/local/blastdb/";

    if ( server_name.equals ( "sun" ) == true )  return "/banana1/ricke/blast/";

    return "";
  }  // method getLocationDb


/******************************************************************************/
  private String getMotifsCommand ( String name )
  {
    String command  
        = getShellPrefix ()
        + "motifs -def -in=" 
        + source_path 
        + name
        + " -out=" 
        + destination_path 
        + name 
        + "_-motifs"
        ;

    return command;
  }  // method getMotifsCommand


/******************************************************************************/
  private String getPfamCommand ( String name )
  {
    String command  
        = getShellPrefix ()
        + "motifs -def -in=" 
        + source_path 
        + name
        + " -o " 
        + destination_path 
        + name 
        + "_-motifs"
        ;

    return command;
  }  // method getPfamCommand


/******************************************************************************/
// This method returns the current command based upon program name.
private String getCommand ( String name )
{
  if ( ( program_name.equals ( "blastn" ) == true ) ||
       ( program_name.equals ( "blastp" ) == true ) || 
       ( program_name.equals ( "blastx" ) == true ) || 
       ( program_name.equals ( "tblastn" ) == true ) || 
       ( program_name.equals ( "tblastx" ) == true ) ) 
    return getBlastCommand ( name );

  if ( ( program_name.equals ( "S-W" ) == true ) ||
       ( program_name.startsWith ( "Tera-" ) == true ) ||
       ( program_name.equals ( "Pfam" ) == true ) )
    return getDecypherCommand ( name );

  if ( ( program_name.equals ( "fasta33_t" ) == true ) ||
       ( program_name.equals ( "fasty33_t" ) == true ) || 
       ( program_name.equals ( "tfasta33_t" ) == true ) || 
       ( program_name.equals ( "tfasty33_t" ) == true ) ) 
    return getFastaCommand ( name );

  if ( program_name.equals ( "fgenesh" ) == true )  
    return getFgeneshCommand ( name, model_name );

  if ( program_name.equals ( "gmhmm" ) == true )  
    return getGmhmmCommand ( name, model_name );

  if ( program_name.equals ( "genscan" ) == true )  
    return getGenscanCommand ( name, model_name );

  if ( program_name.equals ( "motifs" ) == true )  
    return getMotifsCommand ( name );

  if ( program_name.equals ( "pfam" ) == true )  
    return getPfamCommand ( name );

  return "echo unknown program name";
}  // method getCommand


/******************************************************************************/
  private int countIdles ( Vector out_messages )
  {
    int idle_cpus = 0;

    if ( ( out_messages != null ) && ( out_messages.size () > 0 ) )
    {
      for ( int i = 0; i < out_messages.size (); i++ )
      {
        int idle = InputTools.getInteger ( (String) out_messages.elementAt ( i ) );

        if ( idle >= 50 )  idle_cpus++;
      }  // for
    }  // if

    return idle_cpus;
  }  // method countIdles


/******************************************************************************/
  private int loadTest ( Process exeProcess, Runtime runtime )
  {
    // Check if the computer name is sgi.
    if ( server_name.equals ( "sgi" ) == true )  return number_of_cpus;
    if ( server_name.equals ( "sun" ) == true )  return number_of_cpus;

    String command = getShellPrefix () + "top -b -n 1 | grep idle | cut -f 4 -d,";

    try
    {
      exeProcess = runtime.exec ( command );
    }
    catch ( IOException e )
    {
      System.out.println ( "loadTest: exeProcess error: " + e );
    }  // catch

    try
    {
      int exit_value = exeProcess.waitFor ();

      if ( exit_value != 0 )
      {
        System.out.println ( "loadTest: Abnormal exit value: " + exit_value );
      }  // if
    }  // try
    catch ( InterruptedException e )
    {
      System.out.println ( "loadTest: exeProcess error: " + e );
    }  // catch

    // Flush the standard output and standard err files.
    Vector out_msgs = getMessages ( exeProcess );

    int idle_cpus = countIdles ( out_msgs );

    out_msgs.removeAllElements ();
    out_msgs = null;

    return idle_cpus;
  }  // method loadTest


/******************************************************************************/
  private Vector getMessages ( Process exeProcess )
  {
    Vector out_msgs = null;

    if ( exeProcess != null )
    {
      // Consume the standard err results.
      BufferedReader std_err = new BufferedReader ( new InputStreamReader ( exeProcess.getErrorStream () ) );
      Vector err_msgs = drain ( std_err );
      err_msgs.removeAllElements ();
      err_msgs = null;

      try
      {
        std_err.close ();
      }  // try
      catch ( IOException e )
      {
        System.out.println ( "std_err close error: " + e );
      }  // catch
      std_err = null;

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

      std_out = null;
    }  // if

    return out_msgs;
  }  // method getMessages


/******************************************************************************/
  // Override Thread class run method to get the work done
  public void run ()
  {
    // Do the thread work here

    // Check for a null thread.
    if ( program_name.length () <= 0 )  return;

    Process exeProcess = null;
    Runtime runtime = Runtime.getRuntime ();

    // Evaluate the current load on the target system.
/*
    int available_cpus = loadTest ( exeProcess, runtime );

    if ( available_cpus == 0 )  return;
    if ( available_cpus < number_of_cpus )  number_of_cpus = available_cpus;
*/
    String name = "";
    do
    {
      name = names.getNextName ( server_name );

      if ( name.length () > 0 )
      {
        String command = getCommand ( name );

        System.out.println ( command );

        success = true;
        try
        {
          exeProcess = runtime.exec ( command );
        }
        catch ( IOException e )
        {
          System.out.println ( "RemoteCoordinator.exeProcess error #1: " + e );
          problems++;
          success = false;
        }  // catch

        try
        {
          int exit_value = exeProcess.waitFor ();

          if ( exit_value != 0 )
          {
            problems++;
            System.out.println ( "Abnormal exit value: " + exit_value );
            success = false;
          }  // if
          else
          {
            success = true;
            // if ( problems > 0 )  problems--;
          }  // else
        }  // try
        catch ( InterruptedException e )
        {
          System.out.println ( "RemoteCoordinator.exeProcess error #2: " + e );
          problems++;
          success = false;
        }  // catch

        // Flush the standard output and standard err files.
/*
        Vector msgs = getMessages ( exeProcess );
        msgs.removeAllElements ();
        msgs = null;
*/

        if ( success == true )
        {
          completed++;
          names.finished ( server_name, name );
        }
        else
          names.failed ( server_name, name );

        success = true;
      }  // if
    }
    while ( ( name.length () > 0 ) && ( problems <= 10 ) );

    String message = server_name + " is done: ";
    if ( problems > 0 )
      message += " problems " + problems;
    System.out.println ( message + " completed " + completed );

    initialize ();	// prevent restarting by accident???
  }  // method run


/******************************************************************************/

}  // class RemoteCoordinator

