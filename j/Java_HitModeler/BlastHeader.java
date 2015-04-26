
// import OutputTools;

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

public class BlastHeader extends Object
{


/******************************************************************************/

  private   String  program_name = "";			// program name

  private   String  program_version = "";		// program version

  private   String  query_name = "";			// query sequence name

  private   String  database_name = "";			// target database name

  private   String  scores = "";			// top N scores


/******************************************************************************/
  // Constructor BlastHeader
  public BlastHeader ()
  {
    initialize ();
  }  // constructor BlastHeader


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    program_name = "";
    program_version = "";
    query_name = "";
    database_name = "";
    scores = "";
  }  // method initialize 


/******************************************************************************/
  public String getProgramName ()
  {
    return program_name;
  }  // method getProgramName


/******************************************************************************/
  public String getProgramVersion ()
  {
    return program_version;
  }  // method getProgramVersion


/******************************************************************************/
  public String getQueryName ()
  {
    return query_name;
  }  // method getQueryName


/******************************************************************************/
  public String getDatabaseName ()
  {
    return database_name;
  }  // method getDatabaseName


/******************************************************************************/
  public String getScores ()
  {
    return scores;
  }  // method getScores


/******************************************************************************/
  public void setProgramName ( String value )
  {
    program_name = value;
  }  // method setProgramName


/******************************************************************************/
  public void setProgramVersion ( String value )
  {
    program_version = value;
  }  // method setProgramVersion


/******************************************************************************/
  public void setQueryName ( String value )
  {
    // Trim the query name at the first space.
    int index = value.indexOf ( " " );
    if ( index > 0 )
      query_name = value.substring ( 0, index );
    else
      query_name = value;

    // Check for a SwissProt name.
    index = query_name.indexOf ( "sp|" );
    if ( index == 0 )
    {
      index = query_name.indexOf ( "|", 3 );
      if ( index != -1 )
        query_name = query_name.substring ( 3, index );
      else
        query_name = query_name.substring ( 3 );
    }  // if
  }  // method setQueryName


/******************************************************************************/
  public void setDatabaseName ( String value )
  {
    database_name = value;
  }  // method setDatabaseName


/******************************************************************************/
  public void setScores ( String value )
  {
    scores = value;
  }  // method setScores


/******************************************************************************/
  public void printHeader ()
  {
    System.out.println ( "BLAST file header information:" );
    System.out.println ();
    OutputTools.write ( "program name", program_name );
    OutputTools.write ( "program version", program_version );
    OutputTools.write ( "query name", query_name );
    OutputTools.write ( "database name", database_name );
    OutputTools.write ( "scores", scores );
  }  // method printHeader


/******************************************************************************/
  public void printQuery ()
  {
    System.out.println ( "Processing: " + query_name );
  }  // method printQuery


/******************************************************************************/

}  // class BlastHeader
