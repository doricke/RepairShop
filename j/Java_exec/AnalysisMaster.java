
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

public class AnalysisMaster extends Object
{


/******************************************************************************/

  private   int cpus_to_use = 2;		// CPUs to use per system

  private   String [] database_names = null;	// database names

  private   String destination_path = "";	// Destination path of results

  private   String list_file_name = "";		// List of sequence file names

  private   String [] fgenesh_model_names = null;	// Fgenesh Gene prediction model names

  private   String [] gmhmm_model_names = null;		// GeneMark.HMM Gene prediction model names

  private   String [] genscan_model_names = null;	// Genscan Gene prediction model names

  private   String [] program_names = null;	// program names

  private   String source_path = "";		// Source path location of sequences

  private   String [] system_names = null;	// computer system names


/******************************************************************************/
  // Constructor AnalysisMaster
  public AnalysisMaster ()
  {
    initialize ();
  }  // constructor AnalysisMaster


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    cpus_to_use = 0;
    database_names = null;
    destination_path = "";
    list_file_name = "";
    fgenesh_model_names = null;
    gmhmm_model_names = null;
    genscan_model_names = null;
    program_names = null;
    source_path = "";
    system_names = null;
  }  // method initialize 


/******************************************************************************/
  private void printList ( String method_name, String [] list )
  {
    System.out.print ( "AnalysisMaster." + method_name + ":" );

    if ( list == null )
    {
      System.out.println ( " null" );
      return;
    }  // if

    for ( int i = 0; i < list.length; i++ )

      System.out.print ( " " + list [ i ] );

    System.out.println ();
  }  // method printList


/******************************************************************************/
  public int getCpusToUse ()
  {
    return cpus_to_use;
  }  // method getCpusToUse


/******************************************************************************/
  public String [] getDatabaseNames ()
  {
    return database_names;
  }  // method getDatabaseNames


/******************************************************************************/
  public String getDestinationPath ()
  {
    return destination_path;
  }  // method getDestinationPath


/******************************************************************************/
  public String getListFileName ()
  {
    return list_file_name;
  }  // method getListFileName


/******************************************************************************/
  public String [] getFgeneshModelNames ()
  {
    return fgenesh_model_names;
  }  // method getFgeneshModelNames


/******************************************************************************/
  public String [] getGmhmmModelNames ()
  {
    return gmhmm_model_names;
  }  // method getGmhmmModelNames


/******************************************************************************/
  public String [] getGenscanModelNames ()
  {
    return genscan_model_names;
  }  // method getGenscanModelNames


/******************************************************************************/
  public String [] getProgramNames ()
  {
    return program_names;
  }  // method getProgramNames


/******************************************************************************/
  public String getSourcePath ()
  {
    return source_path;
  }  // method getSourcePath


/******************************************************************************/
  public String [] getSystemNames ()
  {
    return system_names;
  }  // method getSystemNames


/******************************************************************************/
  public void setCpusToUse ( int value )
  {
    cpus_to_use = value;

    System.out.println ( "AnalysisMaster.setCpusToUse: " + value );
  }  // method setCpusToUse


/******************************************************************************/
  public void setDatabaseNames ( String [] value )
  {
    database_names = value;

    printList ( "setDatabaseNames", value );
  }  // method setDatabaseNames


/******************************************************************************/
  public void setDestinationPath ( String value )
  {
    destination_path = value;

    System.out.println ( "AnalysisMaster.setDestinationPath: " + value );
  }  // method setDestinationPath


/******************************************************************************/
  public void setListFileName ( String value )
  {
    list_file_name = value;

    System.out.println ( "AnalysisMaster.setListFileName: " + value );
  }  // method setListFileName


/******************************************************************************/
  public void setFgeneshModelNames ( String [] value )
  {
    fgenesh_model_names = value;

    printList ( "setFgeneshModelNames", value );
  }  // method setFgeneshModelNames


/******************************************************************************/
  public void setGmhmmModelNames ( String [] value )
  {
    gmhmm_model_names = value;

    printList ( "setGmhmmModelNames", value );
  }  // method setGmhmmModelNames


/******************************************************************************/
  public void setGenscanModelNames ( String [] value )
  {
    genscan_model_names = value;

    printList ( "setGenscanModelNames", value );
  }  // method setGenscanModelNames


/******************************************************************************/
  public void setProgramNames ( String [] value )
  {
    program_names = value;

    printList ( "setModelNames", value );
  }  // method setProgramNames


/******************************************************************************/
  public void setSourcePath ( String value )
  {
    source_path = value;

    // System.out.println ( "AnalysisMaster.setSourcePath: " + value );
  }  // method setSourcePath


/******************************************************************************/
  public void setSystemNames ( String [] value )
  {
    system_names = value;

    printList ( "setSystemNames", value );
  }  // method setSystemNames


/******************************************************************************/
  private static String [] getCPUNames ()
  {
    String [] names = { "ares", "hades", "hestia", "demeter", "apollo" };
    return names;
  }  // method getCPUNames


/******************************************************************************/
  private String makeLogFileName ()
  {
    String log_file_name = list_file_name + "_" + program_names [ 0 ];

    if ( program_names [ 0 ].equals ( "fgenesh" ) == true )

      log_file_name += "_" + fgenesh_model_names [ 0 ];

    else if ( program_names [ 0 ].equals ( "genscan" ) == true )

      log_file_name += "_" + genscan_model_names [ 0 ];

    else if ( program_names [ 0 ].equals ( "gmhmm" ) == true )

      log_file_name += "_" + gmhmm_model_names [ 0 ];

    else if ( program_names [ 0 ].equals ( "motifs" ) == true )

      ;

    else

      log_file_name += "_" + database_names [ 0 ];

    log_file_name += ".log";

    // System.out.println ( "Log file name: " + log_file_name );

    return log_file_name;
  }  // method makeLogFileName


/******************************************************************************/
  public void startAnalysis ()
  {
    if ( ( system_names == null ) || ( system_names.length < 1 ) )
    {
      System.out.println ( "No systems selected!" );
      System.exit ( 0 );
    }  // if

    // Create the destination directory.
    RemoteCommand remote_cmd = null;
    if ( ( system_names [ 0 ].equals ( "sgi" ) == true ) ||
         ( system_names [ 0 ].equals ( "sun" ) == true ) )
    {
      remote_cmd = new RemoteCommand 
          ( system_names [ 0 ], "mkdir " + destination_path );
    }
    else
    {
      remote_cmd = new RemoteCommand 
          ( system_names [ 0 ], "rsh " + system_names [ 0 ] + " mkdir " + destination_path );
    }  // else

    remote_cmd.start ();

    RemoteCoordinator [] threads = new RemoteCoordinator [ system_names.length ];

    Names names = new Names ();
    names.setNamesFileName ( list_file_name );
    names.setLogFileName ( makeLogFileName () );

    String model_name = "";
    if ( fgenesh_model_names != null )  model_name = fgenesh_model_names [ 0 ];
    if ( gmhmm_model_names   != null )  model_name = gmhmm_model_names   [ 0 ];
    if ( genscan_model_names != null )  model_name = genscan_model_names [ 0 ];

System.out.println ( "Starting " + system_names.length + " threads!" );

    // Create the threads.
    for ( int i = 0; i < system_names.length; i++ )
    { 
      threads [ i ] = new RemoteCoordinator 
                              ( system_names [ i ]
                              , cpus_to_use
                              , program_names [ 0 ]
                              , database_names [ 0 ]
                              , model_name
                              , source_path
                              , destination_path
                              , names 
                              );
    }  // for

    // Start the threads.
    for ( int i = 0; i < system_names.length; i++ )
    {
      threads [ i ].start ();
    }  // for

    // Wait for the threads to die.
    for ( int i = 0; i < system_names.length; i++ )
    {
      try 
      {
        threads [ i ].join ();
      }  // try
      catch ( InterruptedException e )
      {
        System.out.println ( "startAnalysis: " + e );
      }  // catch
    }  // for
  }  // method startAnalysis


/******************************************************************************/
  public static void main ( String [] args )
  {
    String [] cpu_names = getCPUNames ();

    String path = "/bio/work/ricke/rsh_test/";

    String program_name = "tblastn";

    String database_name = "Myriad_V8";

    RemoteCoordinator [] threads = new RemoteCoordinator [ cpu_names.length ];

    Names names = new Names ();
    names.setNamesFileName ( "test.in" );

    for ( int i = 0; i < cpu_names.length; i++ )
    
      threads [ i ] = new RemoteCoordinator 
                              ( cpu_names [ i ]
                              , 2 			// cpus_to_use
                              , program_name
                              , database_name
                              , ""			// model_name
                              , path			// source_path
                              , path			// destination_path
                              , names 
                              );

    for ( int i = 0; i < cpu_names.length; i++ )

      threads [ i ].start ();
  }  // method main


/******************************************************************************/

}  // class AnalysisMaster
