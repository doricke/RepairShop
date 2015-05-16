
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

  // Current line of the file
  private StringBuffer current_line = new StringBuffer ( 540 );		


  private   StringBuffer  accession = new StringBuffer ( 40 );		// accession

  private   StringBuffer  database_name = new StringBuffer ( 40 );	// database name

  private   StringBuffer  description = new StringBuffer ( 240 );	// description

  private   int  hit_end = 0;						// database hit end

  private   int  hit_length = 0;					// hit length

  private   int  hit_start = 0;						// database hit start

  private   char  hit_strand = '+';					// strand (+, -)

  private   int  identities = 0;					// identities

  private   StringBuffer  p_value = new StringBuffer ( 40 );		// p value

  private   int  percent = 0;						// percent

  private   StringBuffer  program_name = new StringBuffer ( 40 );	// program name

  private   StringBuffer  query_alignment = new StringBuffer ( 4096 );	// query alignment

  private   int  query_end = 0;						// query hit end

  private   int  query_start = 0;					// query hit start

  private   StringBuffer  score_value = new StringBuffer ( 40 );	// score value

  private   StringBuffer  seq_type = new StringBuffer ( 40 );		// seq_type

  private   StringBuffer  sequence_name = new StringBuffer ( 80 ); 	// sequence name

  private   char  strand = '+';						// strand (+, -)

  private   StringBuffer  target_alignment = new StringBuffer ( 4096 );	// target alignment


/******************************************************************************/
public ScanResult ()
{
  initialize ();
}  // constructor ScanResult


/******************************************************************************/
public void initialize ()
{
  current_line.setLength ( 0 );

  accession.setLength ( 0 );
  database_name.setLength ( 0 );
  description.setLength ( 0 );
  hit_end = 0;
  hit_length = 0;
  hit_start = 0;
  hit_strand = '+';
  identities = 0;
  p_value.setLength ( 0 );
  percent = 0;
  program_name.setLength ( 0 );
  query_alignment.setLength ( 0 );
  query_end = 0;
  query_start = 0;
  score_value.setLength ( 0 );
  seq_type.setLength ( 0 );
  sequence_name.setLength ( 0 );
  strand = '+';
  target_alignment.setLength ( 0 );
}  // method initialize


/******************************************************************************/
  public String getAccession ()
  {
    String acc = accession.toString ();

    int index = acc.indexOf ( "_" );
    if ( index > 0 )  return acc.substring ( 0, index );

    // Check for GenBank gb| or embl| accession number.
    index = acc.indexOf ( "gb|" );
    if ( index == -1 )  index = acc.indexOf ( "mb|" );

    if ( index > 0 )
    {
      acc = acc.substring ( index + 3 );

      index = acc.indexOf ( "|" );
      if ( index > 0 )  acc = acc.substring ( 0, index );
    }  // if

    return acc;
  }  // method getAccession


/******************************************************************************/
  public String getDatabaseName ()
  {
    return database_name.toString ();
  }  // method getDatabaseName


/******************************************************************************/
  public String getDescription ()
  {
    return description.toString ();
  }  // method getDescription


/******************************************************************************/
  public int getHitEnd ()
  {
    return hit_end;
  }  // method getHitEnd


/******************************************************************************/
  public int getHitLength ()
  {
    return hit_length;
  }  // method getHitLength


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
  public int getHitStart ()
  {
    return hit_start;
  }  // method getHitStart


/******************************************************************************/
  public char getHitStrand ()
  {
    return hit_strand;
  }  // method getHitStrand


/******************************************************************************/
  public int getIdentities ()
  {
    return identities;
  }  // method getIdentities


/******************************************************************************/
// This method returns the current line.
public StringBuffer getLine ()
{
  return current_line;
}  // method getLine


/******************************************************************************/
  public String getPValue ()
  {
    return p_value.toString ();
  }  // method getPValue


/******************************************************************************/
  public int getPercent ()
  {
    return percent;
  }  // method getPercent


/******************************************************************************/
  public String getProgramName ()
  {
    return program_name.toString ();
  }  // method getProgramName


/******************************************************************************/
  public String getQueryAlignment ()
  {
    return query_alignment.toString ();
  }  // method getQueryAlignment


/******************************************************************************/
  public int getQueryEnd ()
  {
    return query_end;
  }  // method getQueryEnd


/******************************************************************************/
  public int getQueryStart ()
  {
    return query_start;
  }  // method getQueryStart


/******************************************************************************/
  public String getScoreValue ()
  {
    return score_value.toString ();
  }  // method getScoreValue


/******************************************************************************/
  public String getSeqType ()
  {
    return seq_type.toString ();
  }  // method getSeqType


/******************************************************************************/
  public String getSequenceName ()
  {
    return sequence_name.toString ();
  }  // method getSequenceName


/******************************************************************************/
  public String getSequenceType ()
  {
    if ( program_name.equals ( "BLASTN" ) == true )  return "DNA";
    if ( program_name.equals ( "BLASTP" ) == true )  return "AA";
    if ( program_name.equals ( "BLASTX" ) == true )  return "DNA";
    if ( program_name.equals ( "TBLASTN" ) == true )  return "AA";
    if ( program_name.equals ( "TBLASTX" ) == true )  return "DNA";

    // Unknown
    return "DNA";
  }  // method getSequenceType


/******************************************************************************/
  public char getStrand ()
  {
    return strand;
  }  // method getStrand


/******************************************************************************/
  public String getTargetAlignment ()
  {
    return target_alignment.toString ();
  }  // method getTargetAlignment


/******************************************************************************/
  public String getTargetType ()
  {
    if ( program_name.equals ( "BLASTN" ) == true )  return "DNA";
    if ( program_name.equals ( "BLASTP" ) == true )  return "AA";
    if ( program_name.equals ( "BLASTX" ) == true )  return "AA";
    if ( program_name.equals ( "TBLASTN" ) == true )  return "DNA";
    if ( program_name.equals ( "TBLASTX" ) == true )  return "DNA";

    // Unknown
    return "DNA";
  }  // method getTargetType


/******************************************************************************/
  public void setAccession ( String value )
  {
    accession.setLength ( 0 );
    accession.append ( value );
  }  // method setAccession


/******************************************************************************/
  public void setDatabaseName ( String value )
  {
    database_name.setLength ( 0 );
    database_name.append ( value );
  }  // method setDatabaseName


/******************************************************************************/
public void setDescription ( String value )
{
  description.setLength ( 0 );

  // Validate value.
  if ( value.length () <= 0 )  return;

  // Drop the FASTA header line marker.
  if ( value.charAt ( 0 ) == '>' )
    description.append ( value.substring ( 1 ) );
  else
    description.append ( value );
}  // method setDescription


/******************************************************************************/
  public void setHitEnd ( int value )
  {
    hit_end = value;
  }  // method setHitEnd


/******************************************************************************/
  public void setHitLength ( int value )
  {
    hit_length = value;
  }  // method setHitLength


/******************************************************************************/
  public void setHitStart ( int value )
  {
    hit_start = value;
  }  // method setHitStart


/******************************************************************************/
  public void setHitStrand ( char value )
  {
    hit_strand = value;
  }  // method setHitStrand


/******************************************************************************/
  public void setIdentities ( int value )
  {
    identities = value;
  }  // method setIdentities


/******************************************************************************/
  public void setPValue ( String value )
  {
    p_value.setLength ( 0 );
    p_value.append ( value );
  }  // method setPValue


/******************************************************************************/
  public void setPercent ( int value )
  {
    percent = value;
  }  // method setPercent


/******************************************************************************/
  public void setProgramName ( String value )
  {
    program_name.setLength ( 0 );
    program_name.append ( value );
  }  // method setProgramName


/******************************************************************************/
  public void setQueryAlignment ( String value )
  {
    query_alignment.setLength ( 0 );
    query_alignment.append ( value );
  }  // method setQueryAlignment


/******************************************************************************/
  public void setQueryEnd ( int value )
  {
    query_end = value;
  }  // method setQueryEnd


/******************************************************************************/
  public void setQueryStart ( int value )
  {
    query_start = value;
  }  // method setQueryStart


/******************************************************************************/
  public void setScoreValue ( String value )
  {
    score_value.setLength ( 0 );
    score_value.append ( value );
  }  // method setScoreValue


/******************************************************************************/
  public void setSeqType ( String value )
  {
    seq_type.setLength ( 0 );
    seq_type.append ( value );
  }  // method setSeqType


/******************************************************************************/
  public void setSequenceName ( String value )
  {
    sequence_name.setLength ( 0 );
    sequence_name.append ( value );
  }  // method setSequenceName


/******************************************************************************/
  public void setStrand ( char value )
  {
    strand = value;
  }  // method setStrand


/******************************************************************************/
  public void setTargetAlignment ( String value )
  {
    target_alignment.setLength ( 0 );
    target_alignment.append ( value );
  }  // method setTargetAlignment


/******************************************************************************/
private void eliminateDoubleTabs ( StringBuffer line )
{
  int index = line.toString ().indexOf ( "\t\t" );

  while ( index >= 0 )
  {
    // Insert a seperator between the two tabs.
    line.insert ( index + 1, "-" );

    index = line.toString ().indexOf ( "\t\t" );
  }  // while
}  // method eliminateDoubleTabs


/******************************************************************************/
public static String eliminateDoubleTabs ( String line )
{
  StringBuffer scr = new StringBuffer ( line.length () + 10 );

  scr.append ( line );
  int index = line.indexOf ( "\t\t" );

  while ( index >= 0 )
  {
    // Insert a seperator between the two tabs.
    scr.insert ( index + 1, "-" );

    index = scr.toString ().indexOf ( "\t\t" );
  }  // while

  return scr.toString ();
}  // method eliminateDoubleTabs


/******************************************************************************/
  public void nextResult ( StringBuffer line )
  {
    eliminateDoubleTabs ( line );
    nextResult ( line.toString () );
  }  // method nextResult


/******************************************************************************/
  public void nextResult ( String line )
  {
    initialize ();

    if ( ( line.length () < 1 ) || ( line.equals ( "null" ) ) )
      return;

    line = eliminateDoubleTabs ( line );
    current_line.append ( line );

    StringTokenizer tokens = new StringTokenizer ( line, "\t" );

    try
    {
      sequence_name.append ( tokens.nextToken () );

/*
      // Truncate at the SCAN delimiter.
      int index = sequence_name.toString ().indexOf ( '-' );
      if ( index > 0 )  sequence_name.setLength ( index );
*/
      query_start = InputTools.getInteger ( tokens.nextToken () );
      query_end   = InputTools.getInteger ( tokens.nextToken () );
      strand      = tokens.nextToken ().charAt ( 0 );
      hit_strand   = tokens.nextToken ().charAt ( 0 );
      identities  = InputTools.getInteger ( tokens.nextToken () );
      score_value.append  ( tokens.nextToken () );
      p_value.append      ( tokens.nextToken () );
      hit_length  = InputTools.getInteger ( tokens.nextToken () );
      percent     = InputTools.getInteger ( tokens.nextToken () );
      seq_type.append     ( tokens.nextToken () );
      program_name.append  ( tokens.nextToken () );
      database_name.append ( tokens.nextToken () );
      accession.append    ( tokens.nextToken () );
      hit_start    = InputTools.getInteger ( tokens.nextToken () );
      hit_end      = InputTools.getInteger ( tokens.nextToken () );
      setDescription ( tokens.nextToken () );
      query_alignment.append ( tokens.nextToken () );
      target_alignment.append ( tokens.nextToken () );

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
    return toString ( "\t" );
  }  // method toString 


/******************************************************************************/
  public String toString ( String delimiter )
  {
    StringBuffer line = new StringBuffer ( 8192 );

    line.append ( sequence_name );
    line.append ( delimiter );
    line.append ( query_start );
    line.append ( delimiter );
    line.append ( query_end );
    line.append ( delimiter );
    line.append ( strand );
    line.append ( delimiter );
    line.append ( hit_strand );
    line.append ( delimiter );
    line.append ( identities );
    line.append ( delimiter );
    line.append ( score_value );
    line.append ( delimiter );
    line.append ( p_value );
    line.append ( delimiter );
    line.append ( hit_length );
    line.append ( delimiter );
    line.append ( percent );
    line.append ( delimiter );
    line.append ( seq_type );
    line.append ( delimiter );
    line.append ( program_name );
    line.append ( delimiter );
    line.append ( database_name );
    line.append ( delimiter );
    line.append ( accession );
    line.append ( delimiter );
    line.append ( hit_start );
    line.append ( delimiter );
    line.append ( hit_end );
    line.append ( delimiter );
    line.append ( description );
    line.append ( delimiter );
    line.append ( query_alignment.toString () );
    line.append ( delimiter );
    line.append ( target_alignment.toString () );

    return line.toString ();
  }  // method toString


/******************************************************************************/

}  // class ScanResult

