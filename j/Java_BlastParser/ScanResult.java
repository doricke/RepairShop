
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

public class ScanResult extends Object
{

/******************************************************************************/


private StringBuffer current_line = new StringBuffer ( 540 );			// Current line of the file


private StringBuffer sequenceName = new StringBuffer ( 80 );	// sequence name

private int query_start;					// query hit start

private int query_end;						// query hit end

private char strand;						// strand (+, -)

private char db_strand;						// strand (+, -)

private int identities;						// identities

private StringBuffer score_value = new StringBuffer ( 40 );	// score value

private StringBuffer p_value = new StringBuffer ( 40 );		// p value

private int hit_length;						// hit length

private int percent;						// percent

private StringBuffer seq_type = new StringBuffer ( 40 );	// seq_type

private StringBuffer programName = new StringBuffer ( 40 );	// program name

private StringBuffer databaseName = new StringBuffer ( 40 );	// database name

private StringBuffer accession = new StringBuffer ( 40 );	// accession

private int db_start;						// database hit start

private int db_end;						// database hit end

private StringBuffer description = new StringBuffer ( 240 );	// description


/******************************************************************************/
public ScanResult ()
{
  initialize ();
}  // constructor ScanResult


/******************************************************************************/
public void initialize ()
{
  sequenceName.setLength ( 0 );
  query_start = 0;
  query_end = 0;
  strand = ' ';
  db_strand = ' ';
  identities = 0;
  percent = 0;
  hit_length = 0;
  p_value.setLength ( 0 );
  score_value.setLength ( 0 );
  seq_type.setLength ( 0 );
  programName.setLength ( 0 );
  databaseName.setLength ( 0 );
  accession.setLength ( 0 );
  db_start = 0;
  db_end = 0;
  description.setLength ( 0 );
  current_line.setLength ( 0 );
}  // method initialize


/******************************************************************************/
// This method returns the current line.
public StringBuffer getLine ()
{
  return current_line;
}  // method getLine


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
public char getDatabaseStrand ()
{
  return db_strand;
}  // method getDatabaseStrand


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
public int getPercent ()
{
  return percent;
}  // method getPercent


/******************************************************************************/
public String getProgramName ()
{
  return programName.toString ();
}  // method getProgramName


/******************************************************************************/
public String getPvalue ()
{
  return p_value.toString ();
}  // method getPvalue


/******************************************************************************/
public String getScore ()
{
  return score_value.toString ();
}  // method getScore


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
public String getDescription ()
{
  return description.toString ();
}  // method getDescription


/******************************************************************************/
public int getHitBegin ()
{
  return db_start;
}  // method getHitBegin


/******************************************************************************/
public int getHitEnd ()
{
  return db_end;
}  // method getHitEnd


/******************************************************************************/
public String getHitName ()
{
  String desc = description.toString ();
  int index = desc.indexOf ( ' ' );

  int index2 = desc.indexOf ( ".mrna" );
  if ( ( ( index == -1 ) || ( index2 < index ) ) && ( index2 != -1 ) )  
    index = index2;

  index2 = desc.indexOf ( ':' );
  if ( ( ( index == -1 ) || ( index2 < index ) ) && ( index2 != -1 ) )  
    index = index2;

  if ( index == -1 )  return description.toString ();
  return description.substring ( 0, index );
}  // method getHitName


/******************************************************************************/
public String getSequenceName ()
{
  return sequenceName.toString ();
}  // method getSequenceName


/******************************************************************************/
private void eliminateDoubleTabs ( StringBuffer string_buffer )
{
  int index = string_buffer.toString ().indexOf ( "\t\t" );

  while ( index >= 0 )
  {
    // Insert a seperator between the two tabs.
    string_buffer.insert ( index + 1, "-" );

    index = string_buffer.toString ().indexOf ( "\t\t" );
  }  // while
}  // method eliminateDoubleTabs


/******************************************************************************/
  public void nextResult ( StringBuffer line )
  {
    initialize ();

    if ( ( line.length () < 1 ) || ( line.toString ().equals ( "null" ) ) )
      return;

    eliminateDoubleTabs ( line );
    current_line.append ( line.toString () );

    StringTokenizer tokens = new StringTokenizer ( current_line.toString (), "\t" );

    try
    {
      sequenceName.append ( tokens.nextToken () );

/*
      // Truncate at the SCAN delimiter.
      int index = sequenceName.toString ().indexOf ( '-' );
      if ( index > 0 )  sequenceName.setLength ( index );
*/
      query_start = InputTools.getInteger ( tokens.nextToken () );
      query_end   = InputTools.getInteger ( tokens.nextToken () );
      strand      = tokens.nextToken ().charAt ( 0 );
      db_strand   = tokens.nextToken ().charAt ( 0 );
      identities  = InputTools.getInteger ( tokens.nextToken () );
      score_value.append  ( tokens.nextToken () );
      p_value.append      ( tokens.nextToken () );
      hit_length  = InputTools.getInteger ( tokens.nextToken () );
      percent     = InputTools.getInteger ( tokens.nextToken () );
      seq_type.append     ( tokens.nextToken () );
      programName.append  ( tokens.nextToken () );
      databaseName.append ( tokens.nextToken () );
      accession.append    ( tokens.nextToken () );
      db_start    = InputTools.getInteger ( tokens.nextToken () );
      db_end      = InputTools.getInteger ( tokens.nextToken () );
      description.append  ( tokens.nextToken () );

      if ( hit_length < 0 )  hit_length = 0;
      if ( percent < 0 )  percent = 0;
      if ( percent > 100 )  percent = 100;
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "nextResult: NoSuchElementException: " + e );
      snapShot ();
      return;
    }  // catch
  }  // method nextResult


/******************************************************************************/
  private void snapShot ()
  {
    System.out.println ( toString () );
  }  // method snapShot


/******************************************************************************/
  public String toString ()
  {
    StringBuffer line = new StringBuffer ( 240 );

    line.append ( sequenceName );
    line.append ( "\t" );
    line.append ( query_start );
    line.append ( "\t" );
    line.append ( query_end );
    line.append ( "\t" );
    line.append ( strand );
    line.append ( "\t" );
    line.append ( db_strand );
    line.append ( "\t" );
    line.append ( identities );
    line.append ( "\t" );
    line.append ( score_value );
    line.append ( "\t" );
    line.append ( p_value );
    line.append ( "\t" );
    line.append ( hit_length );
    line.append ( "\t" );
    line.append ( percent );
    line.append ( "\t" );
    line.append ( seq_type );
    line.append ( "\t" );
    line.append ( programName );
    line.append ( "\t" );
    line.append ( databaseName );
    line.append ( "\t" );
    line.append ( accession );
    line.append ( "\t" );
    line.append ( db_start );
    line.append ( "\t" );
    line.append ( db_end );
    line.append ( "\t" );
    line.append ( description );
    line.append ( "\t" );

    return line.toString ();
  }  // method toString


/******************************************************************************/

}  // class ScanResult

