
// import OutputTools;
// import SeqTools;
// import Sequence;

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

public class Hit extends Object
{


/******************************************************************************/

  private   int     alignment_length = 0;		// alignment length

  private   String  description = "";			// FASTA header line

  private   String  error = "";				// error message

  private   String  expect = "";			// Expect

  private   int     identities = 0;			// identities

  private   int     length = 0;				// query sequence length

  private   int     percent = 0;			// percent

  private   String  query_alignment = "";		// query alignment sequence

  private   int     query_end = 0;			// query end

  private   int     query_frame = 0;			// query frame

  private   String  query_name = "";			// query name

  private   int     query_start = 0;			// query start

  private   char    query_strand = '+';			// query strand

  private   String  score = "";				// Score 

  private   String  target_alignment = "";		// target alignment sequence

  private   int     target_end = 0;			// target end

  private   int     target_frame = 0;			// target frame

  private   int     target_length = 0;			// target length

  private   String  target_name = "";			// target name

  private   int     target_start = 0;			// target start

  private   char    target_strand = '+';		// target strand


  private   String  program_name = "";			// program name

  private   int     q_factor = 3;			// query amino acid numbering system

  private   int     t_factor = 3;			// target amino acid numbering system


/******************************************************************************/
  // Constructor Hit
  public Hit ()
  {
    initialize ();
  }  // constructor Hit


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alignment_length = 0;
    description = "";
    error = "";
    expect = "";
    identities = 0;
    length = 0;
    percent = 0;
    query_alignment = "";
    query_end = 0;
    query_frame = 0;
    query_name = "";
    query_start = 0;
    query_strand = '+';
    score = "";
    target_alignment = "";
    target_end = 0;
    target_frame = 0;
    target_length = 0;
    target_name = "";
    target_start = 0;
    target_strand = '+';

    program_name = "";
    q_factor = 3;
    t_factor = 3;
  }  // method initialize 


/******************************************************************************/
  public int getAlignmentLength ()
  {
    return alignment_length;
  }  // method getAlignmentLength


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public String getError ()
  {
    return error;
  }  // method getError


/******************************************************************************/
  public String getExpect ()
  {
    return expect;
  }  // method getExpect


/******************************************************************************/
  public int getIdentities ()
  {
    return identities;
  }  // method getIdentities


/******************************************************************************/
  public int getLength ()
  {
    return length;
  }  // method getLength


/******************************************************************************/
  public int getPercent ()
  {
    return percent;
  }  // method getPercent


/******************************************************************************/
  public String getQueryAlignment ()
  {
    return query_alignment;
  }  // method getQueryAlignment


/******************************************************************************/
  public int getQueryEnd ()
  {
    return query_end;
  }  // method getQueryEnd


/******************************************************************************/
  public int getQueryFrame ()
  {
    return query_frame;
  }  // method getQueryFrame


/******************************************************************************/
  public String getQueryName ()
  {
    return query_name;
  }  // method getQueryName


/******************************************************************************/
  public int getQueryStart ()
  {
    return query_start;
  }  // method getQueryStart


/******************************************************************************/
  public char getQueryStrand ()
  {
    return query_strand;
  }  // method getQueryStrand


/******************************************************************************/
  public String getScore ()
  {
    return score;
  }  // method getScore


/******************************************************************************/
  public String getTargetAlignment ()
  {
    return target_alignment;
  }  // method getTargetAlignment


/******************************************************************************/
  public int getTargetEnd ()
  {
    return target_end;
  }  // method getTargetEnd


/******************************************************************************/
  public int getTargetFrame ()
  {
    return target_frame;
  }  // method getTargetFrame


/******************************************************************************/
  public int getTargetLength ()
  {
    return target_length;
  }  // method getTargetLength


/******************************************************************************/
  public String getTargetName ()
  {
    return target_name;
  }  // method getTargetName


/******************************************************************************/
  public int getTargetStart ()
  {
    return target_start;
  }  // method getTargetStart


/******************************************************************************/
  public char getTargetStrand ()
  {
    return target_strand;
  }  // method getTargetStrand


/******************************************************************************/
  public void setAlignmentLength ( int value )
  {
    alignment_length = value;
  }  // method setAlignmentLength


/******************************************************************************/
  public void setDescription ( String value )
  {
    description = value;

    // Extract the target name from the description line.
    int index = value.indexOf ( ' ' );

    // Check for no target name.
    if ( value.length () < 2 )  return;

    if ( index == -1 )
      target_name = value.substring ( 1 );
    else
      target_name = value.substring ( 1, index );
  }  // method setDescription


/******************************************************************************/
  public void setError ( String value )
  {
    error = value;
  }  // method setError


/******************************************************************************/
  public void setExpect ( String value )
  {
    expect = value;
  }  // method setExpect


/******************************************************************************/
  public void setIdentities ( int value )
  {
    identities = value;
  }  // method setIdentities


/******************************************************************************/
  public void setLength ( int value )
  {
    length = value;
  }  // method setLength


/******************************************************************************/
  public void setPercent ( int value )
  {
    percent = value;
  }  // method setPercent


/******************************************************************************/
  public void setProgramName ( String value )
  {
    program_name = value;

    // System.out.println ( "Program name = " + program_name );

    // Set the numbering system factors.
    if ( program_name.equals ( "BLASTN"  ) == true ) 
    {
      q_factor = 1;
      t_factor = 1;
      return;
    }  // if

    // Set the numbering system factors.
    if ( program_name.equals ( "BLASTP"  ) == true ) 
    {
      q_factor = 1;
      t_factor = 1;
      return;
    }  // if

    // Set the numbering system factors.
    if ( program_name.equals ( "BLASTX"  ) == true ) 
    {
      q_factor = 3;
      t_factor = 1;
      return;
    }  // if

    // Set the numbering system factors.
    if ( program_name.equals ( "TBLASTN" ) == true ) 
    {
      q_factor = 1;
      t_factor = 3;
      return;
    }  // if

    // Set the numbering system factors.
    if ( program_name.equals ( "TBLASTX"  ) == true ) 
    {
      q_factor = 3;
      t_factor = 3;
      return;
    }  // if

    System.out.println ( "Unknown BLAST program name: '" + program_name + "'" );
  }  // method setProgramName


/******************************************************************************/
  public void setQueryAlignment ( String value )
  {
    query_alignment = value;
  }  // method setQueryAlignment


/******************************************************************************/
  public void setQueryEnd ( int value )
  {
    query_end = value;

    // System.out.println ( "Hit.setQueryEnd to " + value );
  }  // method setQueryEnd


/******************************************************************************/
  public void setQueryFrame ( int value )
  {
    query_frame = value;

    // Check for complement strand.
    if ( value < 0 )  query_strand = '-';
  }  // method setQueryFrame


/******************************************************************************/
  public void setQueryName ( String value )
  {
    query_name = value;
  }  // method setQueryName


/******************************************************************************/
  public void setQueryStart ( int value )
  {
    query_start = value;

    // System.out.println ( "Hit.setQueryStart to " + value );
  }  // method setQueryStart


/******************************************************************************/
  public void setQueryStrand ( char value )
  {
    query_strand = value;
  }  // method setQueryStrand


/******************************************************************************/
  public void setScore ( String value )
  {
    score = value;
  }  // method setScore


/******************************************************************************/
  public void setTargetAlignment ( String value )
  {
    target_alignment = value;
  }  // method setTargetAlignment


/******************************************************************************/
  public void setTargetEnd ( int value )
  {
    target_end = value;
  }  // method setTargetEnd


/******************************************************************************/
  public void setTargetFrame ( int value )
  {
    target_frame = value;

    // Check for complement strand.
    if ( value < 0 )  target_strand = '-';
  }  // method setTargetFrame


/******************************************************************************/
  public void setTargetLength ( int value )
  {
    target_length = value;
  }  // method setTargetLength


/******************************************************************************/
  public void setTargetName ( String value )
  {
    target_name = value;
  }  // method setTargetName


/******************************************************************************/
  public void setTargetStart ( int value )
  {
    target_start = value;
  }  // method setTargetStart


/******************************************************************************/
  public void setTargetStrand ( char value )
  {
    target_strand = value;
  }  // method setTargetStrand


/******************************************************************************/
  // This method trims one amino acid from the begining of the query alignment.
  public void advanceQuery ()
  {
    // Check for a gap character.
    if ( query_alignment.charAt ( 0 ) != '-' )
    {
      // Check which strand the query alignment is from.
      if ( query_strand == '+' )

        query_start += q_factor;

      else

        query_end -= q_factor;
    }  // if

    // Shorten the query alignment.
    query_alignment = query_alignment.substring ( 1 );
  }  // method advanceQuery


/******************************************************************************/
  // This method trims one amino acid from the beginning of the target alignment.
  public void advanceTarget ()
  {
    // Check for a gap character.
    if ( target_alignment.charAt ( 0 ) != '-' )
    {
      // Check which strand the query alignment is from.
      if ( target_strand == '+' )

        target_start += t_factor;

      else

        target_end -= t_factor;
    }  // if

    // Shorten the query alignment.
    target_alignment = target_alignment.substring ( 1 );
  }  // method advanceTarget


/******************************************************************************/
  public void shortenQuery ()
  {
    // System.out.println ( "shortenQuery: query = [" + query_start + " to " + query_end + " ]" );

    // Check for a gap character.
    if ( query_alignment.charAt ( query_alignment.length () - 1 ) != '-' )
    {
      // Check which strand the query alignment is from.
      if ( query_strand == '+' )

        query_end -= q_factor;

      else

        query_start += q_factor;
    }  // if

    // Shorten the query alignment.
    query_alignment = query_alignment.substring 
        ( 0, query_alignment.length () - 1 );
  }  // method shortenQuery


/******************************************************************************/
  public void shortenTarget ()
  {
    // Check for a gap character.
    if ( target_alignment.charAt ( target_alignment.length () - 1 ) != '-' )
    {
      // Check which strand the target alignment is from.
      if ( target_strand == '+' )

        target_end -= t_factor;

      else

        target_start += t_factor;
    }  // if

    // Shorten the target alignment.
    target_alignment = target_alignment.substring 
        ( 0, target_alignment.length () - 1 );
  }  // method shortenTarget


/******************************************************************************/
  private void trimMismatchEnd ()
  {
    int align_end = query_alignment.length () - 1;

    while ( ( align_end > 0 ) &&
            ( query_alignment.charAt ( align_end ) !=
              target_alignment.charAt ( align_end ) ) )
    {
      shortenQuery ();
      shortenTarget ();
      align_end = query_alignment.length () - 1;
    }  // while
  }  // method trimMismatchEnd


/******************************************************************************/
  private void trimMismatchStart ()
  {
    while ( ( query_alignment.length () > 0 ) &&
            ( ( query_alignment.charAt ( 0 ) !=
              target_alignment.charAt ( 0 ) ) ||
            ( query_alignment.charAt ( 0 ) == '*' ) ) )
    {
      advanceQuery ();
      advanceTarget ();
    }  // while
  }  // method trimMismatchStart 


/******************************************************************************/
  public void retotal ()
  {
    identities = 0;				// number of identities
    int aa_count = 0;				// query amino acids

    // Traverse the alignment.
    for ( int i = 0; i < query_alignment.length (); i++ )
    {
      // Check for an amino acid at this position.
      // if ( Sequence.isAA ( query_alignment.charAt ( i ) ) == true )a
      char q = query_alignment.charAt ( i );

      if ( ( ( q >= 'a' ) && ( q <= 'z' ) ) ||
           ( ( q >= 'A' ) && ( q <= 'Z' ) ) )
      {
        aa_count++;

        // Check for identity amino acids.
        if ( q == target_alignment.charAt ( i ) )

          identities++;
      }  // if
    }  // for

    // Reset the length of the alignment.
    alignment_length = query_alignment.length ();

    // Recompute the percent identity of the alignment.
    if ( aa_count > 0 )

      percent = ( identities * 100 ) / aa_count;
  }  // method retotal


/******************************************************************************/
  public void trimAAEnds ()
  {
    // System.out.println ( "trimAAEnds: query  = " + query_alignment );
    // System.out.println ( "trimAAEnds: target = " + target_alignment );
    // System.out.println ();

    // Trim the alignment end until the amino acids are identical.
    trimMismatchEnd ();

    // Check if the end of the alignment needs to be trimmed.
    int count = 0;
    int count2 = 0;
    while ( ( count < 3 ) && ( query_alignment.length () > 6 ) )
    {
      int end = query_alignment.length () - 1;
      int start = end - 5;
      count = SeqTools.countIdentities 
          ( query_alignment .substring ( start, end ),
            target_alignment.substring ( start, end ) );

      count2 = SeqTools.countIdentities 
          ( query_alignment .substring ( end-2, end ),
            target_alignment.substring ( end-2, end ) );

      if ( ( count < 3 ) || ( count2 < 1 ) )
      {
        // Shorten the alignment.
        shortenQuery ();
        shortenTarget ();
        trimMismatchEnd ();
      }  // if
    }  // while

    // Check if the begining of the alignment needs to be trimmed.
    trimMismatchStart ();

    count = 0;
    while ( ( count < 3 ) && ( query_alignment.length () > 6 ) )
    {
      count = SeqTools.countIdentities 
          ( query_alignment .substring ( 1, 6 ),
            target_alignment.substring ( 1, 6 ) );

      count2 = SeqTools.countIdentities 
          ( query_alignment .substring ( 1, 3 ),
            target_alignment.substring ( 1, 3 ) );

      if ( ( count < 3 ) || ( count2 < 1 ) )
      {
        // Shorten the alignment.
        advanceQuery ();
        advanceTarget ();
        trimMismatchStart ();
      }  // if
    }  // while

    // Retotal the number of identities, percent, etc.
    retotal ();

    // System.out.println ( "trimAAEnds: (end) query  = " + query_alignment );
    // System.out.println ( "trimAAEnds: (end) target = " + target_alignment );
    // System.out.println ();
  }  // method trimAAEnds


/******************************************************************************/
  // This method returns a copy of this hit.
  public Hit copyOf ()
  {
    Hit copy = new Hit ();

    copy.setAlignmentLength ( alignment_length );
    copy.setDescription     ( description );
    copy.setError           ( error );
    copy.setExpect          ( expect );
    copy.setIdentities      ( identities );
    copy.setLength          ( length );
    copy.setPercent         ( percent );
    copy.setQueryAlignment  ( query_alignment );
    copy.setQueryEnd        ( query_end );
    copy.setQueryFrame      ( query_frame );
    copy.setQueryName       ( query_name );
    copy.setQueryStart      ( query_start );
    copy.setQueryStrand     ( query_strand );
    copy.setScore           ( score );
    copy.setTargetAlignment ( target_alignment );
    copy.setTargetEnd       ( target_end );
    copy.setTargetFrame     ( target_frame );
    copy.setTargetLength    ( target_length );
    copy.setTargetName      ( target_name );
    copy.setTargetStart     ( target_start );
    copy.setTargetStrand    ( target_strand );
    copy.setProgramName     ( program_name );

    return copy;
  }  // method copyOf


/******************************************************************************/
  public void printHit ()
  {
    System.out.println ();
    System.out.println ( "Hit:" );
    OutputTools.write ( "alignment length", alignment_length );
    OutputTools.write ( "description", description );
    OutputTools.write ( "error", error );
    OutputTools.write ( "expect", expect );
    OutputTools.write ( "identities", identities );
    OutputTools.write ( "length", length );
    OutputTools.write ( "percent", percent );
    OutputTools.write ( "query end", query_end );
    OutputTools.write ( "query frame", query_frame );
    OutputTools.write ( "query start", query_start );
    OutputTools.write ( "query strand", query_strand );
    OutputTools.write ( "score", score );
    OutputTools.write ( "target end", target_end );
    OutputTools.write ( "target frame", target_frame );
    OutputTools.write ( "target_length", target_length );
    OutputTools.write ( "target_name", target_name );
    OutputTools.write ( "target_start", target_start );
    OutputTools.write ( "target_strand", target_strand );
    OutputTools.write ( "query alignment ", query_alignment );
    OutputTools.write ( "target alignment", target_alignment );
  } // method printHit


/******************************************************************************/
  private String getAccession ()
  {
    int index = description.indexOf ( ' ' );
    int tab_index = description.indexOf ( '\t' );
    if ( ( tab_index > 0 ) && ( tab_index < index ) )  index = tab_index;

    if ( index > 0 )  return description.substring ( 1, index );

    return "-";
  }  // method getAccession


/******************************************************************************/
  public String toResult ( String database_name )
  {
    return toResult ( database_name, 0 );
  }  // method toResult


/******************************************************************************/
  public String toResult ( String database_name, int start_offset )
  {
    StringBuffer result = new StringBuffer ( 4096 );

    result.append ( query_name );			// query name
    result.append ( "\t" );
    result.append ( query_start + start_offset );	// query start
    result.append ( "\t" );
    result.append ( query_end + start_offset );		// query end
    result.append ( "\t" );
    result.append ( query_strand );			// query strand
    result.append ( "\t" );
    result.append ( target_strand );			// target strand
    result.append ( "\t" );
    result.append ( identities );			// identities
    result.append ( "\t" );
    result.append ( score );				// score
    result.append ( "\t" );
    result.append ( expect );				// expect
    result.append ( "\t" );
    result.append ( alignment_length );			// hit length
    result.append ( "\t" );
    result.append ( percent );				// percent
    result.append ( "\t" );
    result.append ( "similarity" );			// seq_type
    result.append ( "\t" );
    result.append ( program_name );			// program name
    result.append ( "\t" );
    result.append ( database_name );			// database name
    result.append ( "\t" );
    result.append ( getAccession () );			// accession
    result.append ( "\t" );
    result.append ( target_start );			// target start
    result.append ( "\t" );
    result.append ( target_end );			// target end
    result.append ( "\t" );
    result.append ( description );			// description
    result.append ( "\t" );
    result.append ( query_alignment );			// query alignment
    result.append ( "\t" );
    result.append ( target_alignment );			// target alignment

    return result.toString ();
  }  // method toString


/******************************************************************************/

}  // class Hit
