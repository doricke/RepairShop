
import java.io.*;
import java.util.*;

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

public class TaxonParser extends Object
{

/******************************************************************************/

private boolean end_of_file = false;	// End of file flag

private String fileName = "";		// Input file name

private BufferedReader resultsFile;	// sequence file

private StringBuffer line = new StringBuffer ( 540 );			// Current line of the file


private StringBuffer sequenceName = new StringBuffer ( 80 );	// sequence name

private int query_start;					// query hit start

private int query_end;						// query hit end

private char strand;						// strand (+, -)

private int identities;						// identities

private int hit_length;						// hit length

private StringBuffer p_value = new StringBuffer ( 40 );		// p value

private StringBuffer seq_type = new StringBuffer ( 40 );	// seq_type

private StringBuffer databaseName = new StringBuffer ( 40 );	// database name

private StringBuffer accession = new StringBuffer ( 40 );	// accession

private int db_start;						// database hit start

private int db_end;						// database hit end

private StringBuffer description = new StringBuffer ( 240 );	// description


/******************************************************************************/
public TaxonParser ()
{
  initialize ();
}  // constructor TaxonParser


/******************************************************************************/
private void initialize ()
{
  sequenceName.setLength ( 0 );
  query_start = 0;
  query_end = 0;
  strand = ' ';
  identities = 0;
  hit_length = 0;
  p_value.setLength ( 0 );
  seq_type.setLength ( 0 );
  databaseName.setLength ( 0 );
  accession.setLength ( 0 );
  db_start = 0;
  db_end = 0;
  description.setLength ( 0 );
  line.setLength ( 0 );
}  // method initialize


/******************************************************************************/
public void close ()
{
  closeInputFile ();
}  // method close


/******************************************************************************/
public int getQueryStart ()
{
  return query_start;
}  // method getQueryStart


/******************************************************************************/
public int getQueryEnd ()
{
  return query_end;
}  // method getQueryEnd


/******************************************************************************/
public char getStrand ()
{
  return strand;
}  // method getStrand


/******************************************************************************/
public int getIdentities ()
{
  return identities;
}  // method getIdentities


/******************************************************************************/
public int getHitLength ()
{
  return hit_length;
}  // method getHitLength


/******************************************************************************/
public String getPvalue ()
{
  return p_value.toString ();
}  // method getPvalue


/******************************************************************************/
public String getSeqType ()
{
  return seq_type.toString ();
}  // method getSeqType


/******************************************************************************/
public String getDatabaseName ()
{
  return databaseName.toString ();
}  // method getDatabaseName


/******************************************************************************/
public String getAccession ()
{
  return accession.toString ();
}  // method getAccession


/******************************************************************************/
public int getDatabaseStart ()
{
  return db_start;
}  // method getDatabaseStart


/******************************************************************************/
public int getDatabaseEnd ()
{
  return db_end;
}  // method getDatabaseEnd


/******************************************************************************/
public String getDescription ()
{
  return description.toString ();
}  // method getDescription


/******************************************************************************/
private int getInteger ( String line )
{
  int i = 0;
  int index = 0;
  int sign = 1;					// Default sign = +

  // Skip leading white space.
  while ( ( line.charAt ( index ) == ' ' ) ||
          ( line.charAt ( index ) == '\t' ) )  index++;

  // Check for a sign.
  if ( line.charAt ( index ) == '+' )
    index++;
  else
    if ( line.charAt ( index ) == '-' )
    {
      sign = -1;
      index++;
    }  /* if */

  // Traverse the integer.
  while ( index < line.length () )
  {
    if ( ( line.charAt ( index ) >= '0' ) && ( line.charAt ( index ) <= '9' ) )

      i = i * 10 + (int) line.charAt ( index ) - (int) '0';

    else  index = line.length ();		// Terminate loop

    index++;
  }  /* while */

  // Set the sign.
  i *= sign;

  return ( i );					// Return the integer
}  /* method getInteger */


/******************************************************************************/
public String getFileName ()
{
  return fileName;
}  // method getFilename


/******************************************************************************/
public String getSequenceName ()
{
  return sequenceName.toString ();
}  // method getSequenceName


/******************************************************************************/
public boolean isEndOfFile ()
{
  return end_of_file;
}  // method isEndOfFile


/******************************************************************************/
public void setFileName ( String file_name )
{
  fileName = file_name;

  end_of_file = false;

  openInputFile ( file_name );
}  /* method setFileName */


/******************************************************************************/
private void openInputFile ( String file_name )
{
  try
  {
    resultsFile = new BufferedReader ( new InputStreamReader 
        ( new FileInputStream ( file_name ) ) );
  }  // try 
  catch ( IOException e )
  {
    System.out.println ( "TaxonParser.openFile: IOException on input file " +
        file_name + ": " + e );
  }  /* catch */
}  // method openInputFile


/******************************************************************************/
private void closeInputFile ()
{
  if ( resultsFile == null )  return;

  /* Close the input file */
  try 
  {
    resultsFile.close ();
  }
  catch ( IOException e  )
  {
    System.out.println ( "TaxonParser.closeInputFile: IOException while closing input file: " 
        + e  );
    return;
  }  // catch
}  // method closeInputFile 


/******************************************************************************/
private void eliminateDoubleTabs ( StringBuffer string_buffer )
{
  int index = string_buffer.toString ().indexOf ( "\t\t" );

  while ( index >= 0 )
  {
System.out.println ( "eliminateDoubleTabs: before: " + string_buffer.toString () );

    // Insert a seperator between the two tabs.
    string_buffer.insert ( index + 1, "-" );

System.out.println ( "eliminateDoubleTabs: after: " + string_buffer.toString () );

    index = string_buffer.toString ().indexOf ( "\t\t" );
  }  // while

}  // method eliminateDoubleTabs


/******************************************************************************/
public void nextResult ()
{
  // Check for end of file.
  if ( end_of_file == true )  return;

  if ( resultsFile == null )  return;

  try 
  {
    initialize ();
    line.append ( resultsFile.readLine () );

    if ( ( line.length () < 1 ) || ( line.toString ().equals ( "null" ) ) )
    {
      end_of_file = true;
      return;
    }  // if

    eliminateDoubleTabs ( line );

    StringTokenizer tokens = new StringTokenizer ( line.toString (), "\t" );

    try
    {
      sequenceName.append ( tokens.nextToken () );

      // Truncate at the SCAN delimiter.
      int index = sequenceName.toString ().indexOf ( '-' );
      if ( index > 0 )  sequenceName.setLength ( index );

      query_start = getInteger ( tokens.nextToken () );
      query_end   = getInteger ( tokens.nextToken () );
      strand      = tokens.nextToken ().charAt ( 0 );
      identities  = getInteger ( tokens.nextToken () );
      hit_length  = getInteger ( tokens.nextToken () );
      p_value.append      ( tokens.nextToken () );
      seq_type.append     ( tokens.nextToken () );
      databaseName.append ( tokens.nextToken () );
      accession.append    ( tokens.nextToken () );
      db_start    = getInteger ( tokens.nextToken () );
      db_end      = getInteger ( tokens.nextToken () );
      description.append  ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
System.out.println ( "NoSuchElementException: " + e );
snapShot ();
      end_of_file = true;
      return;
    }  // catch
  }  // try
  catch ( IOException e )
  {
    System.out.println ( "TaxonParse.readResult: IOException: " + e );
    end_of_file = true;
  }  // method catch
}  // method nextResult


/******************************************************************************/
  public void snapShot ()
  {
    if ( end_of_file == true )  return;

    System.out.print ( sequenceName + "\t" );
    System.out.print ( query_start + "\t" );
    System.out.print ( query_end    + "\t" );
    System.out.print ( strand       + "\t" );
    System.out.print ( identities   + "\t" );
    System.out.print ( hit_length   + "\t" );
    System.out.print ( p_value      + "\t" );
    System.out.print ( seq_type     + "\t" );
    System.out.print ( databaseName + "\t" );
    System.out.print ( accession    + "\t" );
    System.out.print ( db_start     + "\t" );
    System.out.print ( db_end       + "\t" );
    System.out.print ( description );
    System.out.println ();
  }  // method snapShot


/******************************************************************************/
  public static void main ( String [] args )
  {
    TaxonParser app = new TaxonParser ();

    app.setFileName ( "000.Results" );

    while ( app.isEndOfFile () == false )
    {
      app.nextResult ();
      app.snapShot ();
    }  // while 

    app.close ();
  }  // method main

}  // class TaxonParser

