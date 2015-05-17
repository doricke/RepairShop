import java.io.*;
import java.sql.*;
import InputTools;
import XMLTools;

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
public class ParseAce_old extends Object
{


/******************************************************************************/

private StringBuffer contigName = new StringBuffer ( 64 );	// Contig name to process

private StringBuffer contig = new StringBuffer ( 10000 );	// current Contig

private StringBuffer contig_lower = new StringBuffer ( 10000 );		// Contig sequence lower case

private String fileName = "";			// Current file name

private StringBuffer quality = new StringBuffer ( 10000 );	// Contig sequence quality array

private StringBuffer sequence = new StringBuffer ( 2000 );	// DNA sequence

private StringBuffer sequence_lower = new StringBuffer ( 2000 );	// DNA sequence lower case

private StringBuffer sequenceName = new StringBuffer ( 64 );	// Individual read name


private boolean in_alignment = false;		// in an alignment flag

private long total_contigs = 0;			// Number of contigs in the assembly

private long contig_length = 0;			// Length of the contig

private int contig_sequences = 0;		// Number of sequences in the contig

private int rank = 1;				// Rank of the current sequence in the alignment

private long total_sequences = 0;		// Number of sequences in the assembly


private InputTools ace_file = new InputTools ();	// Phrap .ace input file

private StringBuffer line = new StringBuffer ( 128 );	// Current .ace line

private XMLTools xml_file = new XMLTools ();		// XML output file


private StringBuffer [] readNames = null;	// Names of sequences in an assembly

private char [] strands = null;			// Strand orientation of the sequence

private int [] startPositions = null;		// Start positions in an assembly

private int current_sequence = 0;		// Current sequence in assembly

// Get the current date.
java.sql.Date date = new java.sql.Date ( System.currentTimeMillis () );


/******************************************************************************/
// Parameters:

private int minimum_members = 1000;		// Allocation minimum for seqs in contig


/******************************************************************************/
public ParseAce_old ()
{
  initialize ();
}  /* constructor ParseAce_old */


/******************************************************************************/
private void initialize ()
{
  contig.setLength ( 0 );
  contig_sequences = 0;
  contig_length = 0;
  contigName.setLength ( 0 );
  current_sequence = 0;
  in_alignment = false;
  line.setLength ( 0 );
  quality.setLength ( 0 );
  sequence.setLength ( 0 );
  sequenceName.setLength ( 0 );
}  // method initialize


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  /* method setFileName */


/******************************************************************************/
private void readSequence ( StringBuffer seq )
{
  // Clear old sequence.
  seq.setLength ( 0 );

  // Read in the sequence until the first blank line.
  while ( line.length () > 0 )
  {
    // Append the current line to the sequence.
    seq.append ( line.toString ().trim () );

    // Get the next line of the ace file.
    line = ace_file.getLine ();
  }  /* while */
}  // method readSequence 


/******************************************************************************/
// This function counts the non-gap characters in a sequence.
private int countLength ( StringBuffer sequence )
{
  int bases = 0;

  // Count up the non-gap base characters.
  for ( int i = 0; i < sequence.length (); i++ )

    if ( sequence.charAt ( i ) != '*' )  bases++;

  return bases;
}  // method countLength


/******************************************************************************/
/* This method reads in the BaseQuality (BQ) array following a sequence. */
private void readQuality ()
{
  // Reset the quality length.
  quality.setLength ( 0 );

  // Skip the BQ line.
  line = ace_file.getLine ();

  // Read in the quality array until the first blank line.
  while ( line.length () > 0 )
  {
    // Append the current line to the sequence.
    quality.append ( line.toString ().trim () + " " );

    // Get the next line of the ace file.
    line = ace_file.getLine ();
  }  /* while */

  // Create a SequenceQuality entry for the Contig sequence.
  xml_file.write_XML_entry ( "SequenceQuality" );
  xml_file.write_XML_field ( "sequence_quality_type", "Phrap" );

  // Create the sequence_quality field.
  xml_file.write_XML_qual_field ( "sequence_quality", quality.toString () );

  // End the SequenceQuality table entry.
  xml_file.write_XML_entry_end ();

  // End the Sequence table entry.
  xml_file.write_XML_entry_end ();

  // Write the AlignmentSequence entry for the Contig sequence.l
  writeAlignmentSequence ( null, contigName.toString (), 1, contig.length (),
      contig.length (), "+", 1, contig.length () );
}  // method readQuality 


/******************************************************************************/
private void writeSequenceClob ( String entry_name, String field_name, String clob )
{
  // Create the SequenceClob XML entry.
  xml_file.write_XML_entry ( entry_name );

  // Create the sequence_clob field.
  xml_file.write_XML_clob_field ( field_name, clob );

  // End the SequenceClob entry.
  xml_file.write_XML_entry_end ();
}  // method writeSequenceClob


/******************************************************************************/
private void readContig ()
{
System.out.println ( line.toString () );
// printSizes ();

  current_sequence = 0;
  rank = 1;
  contig.setLength ( 0 );

  // Get the contig name.
  contigName.setLength ( 0 );
  int index = line.toString ().indexOf ( " ", 3 );
  contigName.append ( line.substring ( 3, index ) );

  // Get the contig length.
  contig_length = ace_file.getInteger ( line.substring ( index ) );

  // Get the number of sequences in the contig.
  index = line.toString ().indexOf ( " ", index + 1 );
  contig_sequences = ace_file.getInteger ( line.substring ( index ) );

  // Check if the arrays are large enough.
  if ( readNames != null )
  {
    // Check the array lengths.
    if ( readNames.length < contig_sequences )
    {
      // Deallocate the current array space.
      for ( int i = 0; i < readNames.length; i++ )
      {
        readNames [ i ].setLength ( 0 );
        readNames [ i ] = null;
      }  // for

      readNames = null;
      strands = null;
      startPositions = null;
    }  // if
  }  // if

  // Check if new space allocation is needed.
  if ( readNames == null )
  {
    // Allocate the names and offsets arrays.
    int min_size = contig_sequences;
    if ( min_size < minimum_members )  min_size = minimum_members;

    readNames = new StringBuffer [ min_size ];
    strands = new char [ min_size ];
    startPositions = new int [ min_size ];

    for ( int i = 0; i < min_size; i++ )
    {
      readNames [ i ] = new StringBuffer ( 40 );
      readNames [ i ].setLength ( 0 );
      strands [ i ] = ' ';
      startPositions [ i ] = 0;
    }  // for 
  }  // if

  // Get the first line of the consensus sequence.
  ace_file.getLine ();

  // Complete the last Alignment table entry.
  if ( in_alignment == true )
    xml_file.write_XML_entry_end ();

  // Start an alignment.
  in_alignment = true;
  xml_file.write_XML_entry ( "Alignment" );

  // Read in the contig sequence.
  readSequence ( contig );

  // Create a Sequence entry for the Contig sequence.
  xml_file.write_XML_entry ( "Sequence" );
  xml_file.write_XML_field ( "sequence_description", "Phrap contig consensus sequence" );
  xml_file.write_XML_field ( "sequence_length", "" + contig_length );
  xml_file.write_XML_field ( "sequence_name", contigName.toString () );
  // Set the analysis_date field.
  xml_file.write_XML_field ( "date_created", date.toString (), 
      "format", "yyyy-MM-dd" );

  // Write the SequenceClob XML entry.
//  writeSequenceClob ( "SequenceClob", "sequence_clob", contig.toString () );

  // Create the sequence_clob field.
  xml_file.write_XML_clob_field ( "sequence_clob", contig.toString () );
}  // method readContig 


/******************************************************************************/
private void readAFLine ()
{
  // Get the sequence name.
  sequenceName.setLength ( 0 );
  int index = line.toString ().indexOf ( " ", 3 );
  sequenceName.append ( line.substring ( 3, index ) );

  // Get the Strand.
  char strand = line.charAt ( index + 1 );

  // Get the consensus start position.
  int seq_offset = ace_file.getInteger ( line.substring ( index + 2 ) );

  if ( current_sequence >= contig_sequences )
  {
    System.out.println ( "Too many AF lines for current contig:" );
    System.out.println ( "Total sequences = " + contig_sequences + 
        ", reached " + current_sequence );
    return;
  }  // if 

  readNames [ current_sequence ].setLength ( 0 );
  readNames [ current_sequence ].append ( sequenceName.toString () );
  strands [ current_sequence ] = strand;
  startPositions [ current_sequence ] = seq_offset;

  current_sequence++;
}  // method readAFLine


/******************************************************************************/
private boolean baseIdentity ( char base1, char base2 )
{
  switch ( base1 )
  {
    case 'a':
    case 'A':
      if ( ( base2 == 'a' ) || ( base2 == 'A' ) )  return true;
      else return false;

    case 'c':
    case 'C':
      if ( ( base2 == 'c' ) || ( base2 == 'C' ) )  return true;
      else return false;

    case 'g':
    case 'G':
      if ( ( base2 == 'g' ) || ( base2 == 'G' ) )  return true;
      else return false;

    case 't':
    case 'T':
      if ( ( base2 == 't' ) || ( base2 == 'T' ) )  return true;
      else return false;

    default:  return false;
  }  // switch ( base1 )
}  // method baseIdentity


/******************************************************************************/
private int countIdentities ( StringBuffer seq, int segment_start, int segment_end,
    StringBuffer con, int con_seg_start, int con_seg_end )
{
  // Count up the identities in the current alignment segment.
  int identities = 0;
  for ( int i = segment_start; i <= segment_end; i++ )

    // Compare the DNA sequence bases.
    if ( baseIdentity ( seq.charAt ( i ), 
             con.charAt ( con_seg_start + (i - segment_start) ) ) == true )

      identities++;

  return identities;
}  // method countIdentities


/******************************************************************************/
private void writeAlignmentSequence 
    ( String sequence 
    , String sequence_name
    , int alignment_start
    , int alignment_end
    , int identities
    , String sequence_strand
    , int sequence_start
    , int sequence_end
    )
{
  xml_file.write_XML_entry ( "AlignmentSequence" );
  xml_file.write_XML_field ( "alignment_start", "" + alignment_start );
  xml_file.write_XML_field ( "alignment_end",   "" + alignment_end );
  xml_file.write_XML_field ( "rank",            "" + rank );
  xml_file.write_XML_field ( "sequence_strand", sequence_strand );
  xml_file.write_XML_field ( "identities",      "" + identities );
  xml_file.write_XML_field ( "sequence_start",  "" + sequence_start );
  xml_file.write_XML_field ( "sequence_end",    "" + sequence_end );

  // Embed a reference to the current sequence.
  xml_file.write_XML_entry ( "Sequence" );
  xml_file.write_XML_field ( "sequence_name", sequence_name );
  xml_file.write_XML_entry_end ();		// Sequence end

  // Write the SequenceClob XML entry.
  if ( sequence != null )
  writeSequenceClob ( "AlignmentSequenceClob", "alignment_sequence_clob", sequence );

  xml_file.write_XML_entry_end ();		// AlignmentSequence end
}  // method writeAlignmentSequence


/******************************************************************************/
private void alignmentTable ( StringBuffer seq, int seq_start, int seq_end, char strand,
    StringBuffer con, int con_start, int con_end )
{
  // Count up the identities in the alignment.
  int identities = countIdentities ( seq, seq_start, seq_end, 
                        con, con_start, con_end );

  // The sequence coordinates should not include gap characters.
  int segment_start = seq_start;
  int segment_end = seq_start;
  int index = 0;
  while ( index < segment_start )
  {
    if ( seq.charAt ( index ) == '*' )
    {
      segment_start--;
      segment_end--;
    }  // if

    index++;
  }  // while

  // segment_end
  index = segment_start + 1;
  while ( index <= seq_end )
  {
    if ( seq.charAt ( index ) != '*' )  segment_end++;

    index++;
  }  // while

  // Write the sequence strand.
  String sequence_strand = " ";
  if ( strand == 'U' )
  {
    sequence_strand = "+";
  }  // if
  else
    if ( strand == 'C' )
    {
      sequence_strand = "-";

      // These need to be converted to the forward strand coordinates.
      index = segment_start;
      int length = countLength ( seq );
      segment_start = (length-1) - segment_end;
      segment_end = (length-1) - index;
    }  // if 
    else
        System.out.println ( "alignmentTable: unknown strand '" + strand + "'" );

  // Write out the current segment.
  writeAlignmentSequence 
      ( seq.substring ( seq_start, seq_end+1 ) 
      , sequenceName.toString ()
      , con_start+1, con_end+1, identities, sequence_strand
      , segment_start+1, segment_end+1 );
}  // method alignmentTable


/******************************************************************************/
// This method works best if both sequences are the same case!
private boolean matchSequences ( String sequence1, String sequence2 )
{
  int matches = 0;

  // Check for sequences with less than 20 characters.
  if ( ( sequence1.length () <= 20 ) ||
       ( sequence2.length () <= 20 ) )
  {
    return sequence1.equalsIgnoreCase ( sequence2 );
  }  // if

  // Count the matching bases in the first 20 characters.
  for ( int i = 0; i < 20; i++ )

    // Count the matching characters (but not N bases).
    if ( ( sequence1.charAt ( i ) == sequence2.charAt ( i ) ) &&
         ( sequence1.charAt ( i ) != 'x' ) &&
         ( sequence1.charAt ( i ) != 'n' ) )

      matches++;

  // Check for 90% identity (18/20) for a valid match
  if ( matches >= 18 )  return true;
  return false;
}  // method matchSequences


/******************************************************************************/
private void alignSequences ( int offset, char strand )
{
  int contig_start = 0;
  int read_start = 0; 

  // Check for a negative offset for the start of the sequence.
  if ( offset < 0 )
  {
    read_start = - offset;
    offset = 0;
  }  // ifa
  else
  {
    if ( offset > 0 )
      contig_start = offset - 1;
    else
    {
      contig_start = 0;
      read_start = 1;
    }  // else
  }  // else

  // Find the start of the alignment identities.
  int start = -1;
  contig_lower.setLength ( 0 );
  contig_lower.append ( contig.toString ().toLowerCase () );

  sequence_lower.setLength ( 0 );
  sequence_lower.append ( sequence.toString ().toLowerCase () );

  while ( ( matchSequences ( sequence_lower.substring ( read_start ),
                contig_lower.substring ( contig_start ) ) == false ) &&
          ( read_start+20 < sequence.length () ) &&
          ( contig_start+20 < contig.length () ) )
  {
    read_start++;

    // Use a substring lookup
    if ( read_start + 20 < sequence.length () )

      start = contig_lower.toString ().indexOf ( 
          sequence_lower.substring ( read_start, read_start+20 ) );

    if ( start >= 0 )  contig_start = start;
    else contig_start++;
  }  // while

  // Find the end of the alignment.

  // Compute the upper limit on the alignment length.
  int alignment_length = sequence.length () - read_start;
  // Check if contig is shorter.
  if ( contig.length () - contig_start < alignment_length )
    alignment_length = contig.length () - contig_start;

  int read_end = read_start + alignment_length - 1;
  int contig_end = contig_start + alignment_length - 1;

  // Backup on last base mismatches.
  while ( ( read_end > read_start ) &&
          ( ( sequence_lower.charAt ( read_end ) != contig_lower.charAt ( contig_end ) ) ||
            ( sequence_lower.charAt ( read_end ) == 'n' ) ||
            ( sequence_lower.charAt ( read_end ) == 'x' ) ) )
  {
    read_end--;
    contig_end--;
    alignment_length--;
  }  // while

  // Backup on last region mismatches.
  while ( ( read_end > read_start+19 ) &&
          ( matchSequences ( sequence_lower.substring ( read_end - 19 ),
                contig_lower.substring ( contig_end - 19 ) ) == false ) )
  {
    read_end--;
    contig_end--;
    alignment_length--;
  }  // while

  // Check if a valid alignment was found.
  if ( alignment_length >= 40 )

    // Generate alignment tables.
    alignmentTable ( sequence_lower, read_start, read_end, strand,
                     contig_lower, contig_start, contig_end );

  contig_lower.setLength ( 0 );
  sequence_lower.setLength ( 0 );
}  // method alignSequences


/******************************************************************************/
private void readRD ()
{
  rank++;

  // Get the sequence name.
  sequenceName.setLength ( 0 );
  int index = line.toString ().indexOf ( " ", 3 );
  sequenceName.append ( line.substring ( 3, index ) );

  // Get the first line of the sequence.
  ace_file.getLine ();

  // Read in the individual sequence.
  readSequence ( sequence );

//  countLength ( sequence ) );

  // Find the offset for this sequence in the contig.
  index = 0;

  while ( ( index < contig_sequences ) &&
          ( sequenceName.toString ().equalsIgnoreCase 
                ( readNames [ index ].toString () ) == false ) )

    index++;

  if ( index == contig_sequences )

    System.out.println ( "Can't find " + sequenceName.toString () + " offset." );

  else
  {
    // Align the sequence to the consensus.
    alignSequences ( startPositions [ index ], strands [ index ] );
  }  // else
}  // method readRD


/******************************************************************************/
private void skipRT ()
{
  // Search for the end of the RT section.
  while ( ( line.charAt ( 0 ) != '}' ) && 
          ( ace_file.isEndOfFile () == false ) )

    // Get the next line.
    line = ace_file.getLine ();
}  // method skipRT


/******************************************************************************/
private void processContigs ()
{
  // Process the contigs in the Phrap .ace assembly file.
  while ( ace_file.isEndOfFile () == false )
  {
    // Get the next line of the Phrap .ace file.
    line = ace_file.getLine ();

    // Check for end of line.
    if ( ace_file.isEndOfFile () == true )
    {
      // Complete the last Alignment table entry.
      if ( in_alignment == true )
        xml_file.write_XML_entry_end ();

      return;
    }  // if

    // Process the line based up the line type.
    if ( line.length () >= 2 )
    if ( ( line.charAt ( 0 ) == 'C' ) && ( line.charAt ( 1 ) == 'O' ) )
      readContig ();
    else
      if ( ( line.charAt ( 0 ) == 'B' ) && ( line.charAt ( 1 ) == 'Q' ) )
        readQuality ();
      else
        if ( ( line.charAt ( 0 ) == 'A' ) && ( line.charAt ( 1 ) == 'F' ) )
          readAFLine ();
        else
          if ( ( line.charAt ( 0 ) == 'R' ) && ( line.charAt ( 1 ) == 'D' ) )
            readRD ();
          else
            if ( ( line.charAt ( 0 ) == 'R' ) && ( line.charAt ( 1 ) == 'T' ) )
              skipRT ();
            else
              if ( ( ( line.charAt ( 0 ) == 'Q' ) && ( line.charAt ( 1 ) == 'A' ) ) ||
                   ( ( line.charAt ( 0 ) == 'D' ) && ( line.charAt ( 1 ) == 'S' ) ) ||
                   ( ( line.charAt ( 0 ) == 'B' ) && ( line.charAt ( 1 ) == 'S' ) ) )
                ;  // ignore line
              else
                ace_file.blankLine ();	// assert line is blank
  }  // while

  // Complete the last Alignment table entry.
  if ( in_alignment == true )
    xml_file.write_XML_entry_end ();
}  // method processContigs


/******************************************************************************/
private void processSummary ()
{
  if ( ace_file.isEndOfFile () == true )  return;

  // Get the first line of the file; AS line.
  line = ace_file.getLine ();

  // Validate the AS line.
  if ( line.length () >= 2 )
    if ( ( line.charAt ( 0 ) != 'A' ) || ( line.charAt ( 1 ) != 'S' ) )
      System.out.println ( "AS first line of Phrap file expected; saw: " +
          line.toString () );

  // Extract the number of contigs from the line.
  if ( line.length () >= 3 )
  {
    total_contigs = ace_file.getInteger ( line.substring ( 3 ) );
    System.out.println ( "Number of contigs = " + total_contigs );
  }  // if

  // Extract the number of sequences in the assembly.
  int index = line.toString ().indexOf ( " ", 3 );

  total_sequences = ace_file.getInteger ( line.substring ( index ) );
  System.out.println ( "Number of sequences = " + total_sequences );
  System.out.println ();

  // Get the second line of the file; blank line.
  line = ace_file.getLine ();
  ace_file.blankLine ();
}  // method processSummary


/******************************************************************************/
// This method processes the Phrap .ace file.
private void processAce ()
{
  // Set the .ace input file name.
  ace_file.setFileName ( fileName );

  // Open the .ace input file.
  ace_file.openFile ();

  // Set the XML file name.
  xml_file.setFileName ( fileName + ".xml" );

  // Open the XML file for writing.
  xml_file.open_XML_file ();

  // Write the XML file header.
  xml_file.write_XML_header ();

  // Process the .ace summary line.
  processSummary ();

  // Process the Phrap assembled contigs.
  processContigs ();

  // Complete the XML file.
  xml_file.write_XML_footer ();
  xml_file.close_XML ();

  // Close the .ace input file.
  ace_file.closeFile ();

  // Close the XML file.
  xml_file.closeFile ();
}  // method processAce


/******************************************************************************/
private void printSizes ()
{
  System.out.println ( "contigName     length = " + contigName.length () );
  System.out.println ( "contig         length = " + contig.length () );
  System.out.println ( "contig_lower   length = " + contig_lower.length () );
  System.out.println ( "quality        length = " + quality.length () );
  System.out.println ( "sequence       length = " + sequence.length () );
  System.out.println ( "sequence_lower length = " + sequence_lower.length () );
  System.out.println ( "sequenceName   length = " + sequenceName.length () );
  System.out.println ( "line           length = " + line.length () );
  if ( readNames != null )
    System.out.println ( "readNames      length = " + readNames.length );
  System.out.println ();
}  // method printSizes


/******************************************************************************/
public static void usage ()
{
  System.out.println ( "This is the ParseAce_old program." );
  System.out.println ();
  System.out.println ( "This program converts a Phrap .ace file to XML." );
  System.out.println ();
  System.out.println ( "To run type:" );
  System.out.println ();
  System.out.println ( "java ParseAce_old <name.ace>" );
  System.out.println ();
  System.out.println ( "Where <name.ace> is a Phrap .ace output filename." );
  System.out.println ();
  System.out.println ( "The XML output file name will be named name.ace.xml" );
}  // method usage


/******************************************************************************/
public static void main ( String [] args )
{
  /* Check for parameters. */
  if ( args.length == 0 )
  {
    usage ();
  }
  else
  {
    ParseAce_old application = new ParseAce_old ();

    // The .ace file name is the first parameter.
    application.setFileName ( args [ 0 ] );

    // Process the .ace contigs.
    application.processAce ();
  }  /* else */
}  /* method main */

}  /* class ParseAce_old */
