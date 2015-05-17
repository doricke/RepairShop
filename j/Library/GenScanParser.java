
import java.io.*;
import java.sql.*;
import XMLTools;
import InputTools;

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

public class GenScanParser extends Object
{

/******************************************************************************/

private boolean end_of_exons = false;			// end of exons table flag

private int exons_written = 0;				// Count of exons written

private String fileName = "";				// Input file name

private InputTools genscan_file = new InputTools ();	// GENSCAN input file

private StringBuffer line = new StringBuffer ( 540 );	// Current line of the file

private XMLTools xml_file = new XMLTools ();		// XML output file


// Header fields.

private String matrix;					// Parameter matrix

private String program_name;				// GENSCAN

private String program_version;				// GENSCAN version

private String sequenceName;				// sequence name

private String sequence_length;				// length of the sequence


// Individual predicted exon fields:

private StringBuffer description = new StringBuffer ( 10 );	// Type

private StringBuffer exon_end = new StringBuffer ( 10 );	// End

private StringBuffer exon_number = new StringBuffer ( 10 );	// Ex

private StringBuffer exon_start = new StringBuffer ( 10 );	// Begin

private StringBuffer gene_cluster = new StringBuffer ( 10 );	// Gn

private StringBuffer gene_score = new StringBuffer ( 10 );	// gene_score

private StringBuffer p_value = new StringBuffer ( 10 );		// p value

private StringBuffer strand = new StringBuffer ( 10 );		// strand (+, -)


/******************************************************************************/
public GenScanParser ()
{
  initialize ();
}  // constructor GenScanParser


/******************************************************************************/
private void initialize_prediction ()
{
  gene_cluster.setLength ( 0 );
  exon_number.setLength ( 0 );
  exon_start.setLength ( 0 );
  exon_end.setLength ( 0 );
  strand.setLength ( 0 );
  p_value.setLength ( 0 );
  gene_score.setLength ( 0 );
  description.setLength ( 0 );
}  // method initialize_prediction


/******************************************************************************/
private void initialize ()
{
  end_of_exons = false;
  fileName = "";
  line.setLength ( 0 );

  matrix = null;
  program_name = null;
  program_version = null;
  sequenceName = null;
  sequence_length = null;

  initialize_prediction ();
}  // method initialize


/******************************************************************************/
public void setFileName ( String name )
{
  fileName = name;
}  // method setFileName


/******************************************************************************/
public void nextPrediction ()
{
  // Check for end of file.
  if ( genscan_file.isEndOfFile () == true )  return;

  if ( xml_file == null )  return;

  initialize_prediction ();
  line = genscan_file.getLine ();

  if ( ( line.length () < 60 ) || ( line.toString ().equals ( "null" ) ) )
    return;

  // Validate line with a valid strand character in the correct position.
  if ( ( line.charAt ( 11 ) != '+' ) && ( line.charAt ( 11 ) != '-' ) )  return;

  gene_cluster.append ( line.substring ( 0, 2 ) );
  exon_number.append  ( line.substring ( 3, 5 ) );
  description.append  ( line.substring ( 6, 10 ) );
  strand.append       ( line.substring ( 11, 12 ) );
  exon_start.append   ( line.substring ( 13, 19 ) );
  exon_end.append     ( line.substring ( 20, 26 ) );
//  gene_score.append   ( line.substring ( 48, 53 ) );		// CodRg
  p_value.append      ( line.substring ( 54, 59 ) );
  gene_score.append   ( line.substring ( 60, 66 ) );
}  // method nextPrediction


/******************************************************************************/
  public void snapShot ()
  {
    // Check for no new exon.
    if ( exon_start.length () <= 0 )  return;

    System.out.print ( "Snapshot: " );
    System.out.print ( sequenceName + "\t" );
    System.out.print ( gene_cluster + "\t" );
    System.out.print ( exon_number  + "\t" );
    System.out.print ( exon_start   + "\t" );
    System.out.print ( exon_end     + "\t" );
    System.out.print ( strand       + "\t" );
    System.out.print ( p_value      + "\t" );
    System.out.print ( description );
    System.out.println ();
    System.out.println ();
  }  // method snapShot


/******************************************************************************/
  private void writeSequenceEntry ()
  {
    // Create a Sequence table XML entry.
    xml_file.write_XML_entry ( "Sequence" );

    // Set the Sequence name field.
    xml_file.write_XML_field ( "sequence_name", sequenceName );

    // Set the Sequence name field.
    if ( sequence_length != null )
      xml_file.write_XML_field ( "sequence_length", sequence_length );

    // End the Sequence table XML entry.
    xml_file.write_XML_entry_end ();
  }  // method writeSequenceEntry


/******************************************************************************/
  private void write_XML_exon ()
  {
    // Check for no new exon.
    if ( exon_start.length () <= 0 )  return;

    // Count the number of exons written to XML.
    exons_written++;

    xml_file.write_XML_entry ( "GenePrediction" );

    xml_file.write_XML_field ( "gene_cluster", gene_cluster.toString () );
    xml_file.write_XML_field ( "gene_exon_number", exon_number.toString () );
    xml_file.write_XML_field ( "gene_feature_begin", exon_start.toString () );
    xml_file.write_XML_field ( "gene_feature_end", exon_end.toString () );
    xml_file.write_XML_field ( "gene_strand", strand.toString () );

    // Check for PlyA and Prom predictions.
    if ( ( description.toString ().equalsIgnoreCase ( "PlyA" ) != true ) &&
         ( description.toString ().equalsIgnoreCase ( "Prom" ) != true ) )
    {
      xml_file.write_XML_field ( "gene_prediction_quality", p_value.toString () );
    }  // if

    xml_file.write_XML_field ( "gene_score", gene_score.toString () );
    xml_file.write_XML_field ( "gene_prediction_type", description.toString () );

    // Write a Sequence table XML entry.
    writeSequenceEntry ();

    xml_file.write_XML_entry_end ();

  }  // method write_XML_exon


/******************************************************************************/
  private void processHeader ()
  {
    int index;			// String index

    // Check for incorrect input file.
    if ( genscan_file.isEndOfFile () == true )  return;

    // GENSCAN line.
    line = genscan_file.getLine ();
    if ( genscan_file.validateStart ( "GENSCAN" ) == false )  return;
    index = line.toString ().indexOf ( '\t', 8 );
    program_version = line.toString ().substring ( 8, index ); 

    // blank line.
    line = genscan_file.getLine ();
    if ( genscan_file.blankLine () == false )  return;

    // Sequence name line.
    line = genscan_file.getLine ();
    if ( genscan_file.validateStart ( "Sequence " ) == false )  return;
    String seq_line = line.toString ();
    index = seq_line.indexOf ( ' ', 9 );
    sequenceName = seq_line.substring ( 9, index );

    // Get the sequence length.
    index = seq_line.indexOf ( ':' );
    int bp_index = seq_line.indexOf ( "bp" );
    if ( ( index >= 0 ) && ( bp_index >= 0 ) && ( bp_index + 3 > index ) )
      sequence_length = seq_line.substring ( index + 2, bp_index - 1 );

    // blank line.
    line = genscan_file.getLine ();
    if ( genscan_file.blankLine () == false )  return;

    // Parameter matrix line.
    line = genscan_file.getLine ();
    if ( genscan_file.validateStart ( "Parameter matrix: " ) == false )  return;
    matrix = line.toString ().substring ( 18 );

    // blank line.
    line = genscan_file.getLine ();
    if ( genscan_file.blankLine () == false )  return;

    // Predicted genes line.
    line = genscan_file.getLine ();
    if ( genscan_file.validateStart ( "Predicted genes/exons:" ) == false )  return;

    // blank line.
    line = genscan_file.getLine ();
    if ( genscan_file.blankLine () == false )  return;

    // Header line.
    line = genscan_file.getLine ();
    if ( genscan_file.validateStart ( "Gn.Ex Type S ." ) == false )  return;

    // Dash line.
    line = genscan_file.getLine ();
    if ( genscan_file.validateStart ( "----- ---- - " ) == false )  return;

    // blank line.
    line = genscan_file.getLine ();
    if ( genscan_file.blankLine () == false )  return;
  }  // method processHeader


/******************************************************************************/
  private void writeAnalysisEntry ()
  {
    // Create an Analysis table XML entry.
    xml_file.write_XML_entry ( "Analysis" );

    // Set the analysis_name field.
    xml_file.write_XML_field ( "analysis_name", "GENSCAN" );

    // Get the current date.
    java.sql.Date date = new java.sql.Date ( System.currentTimeMillis () );

    // Set the analysis_date field.
    xml_file.write_XML_field ( "analysis_date", date.toString (), 
        "format", "yyyy-mm-dd" );

    // End the Analysis table XML entry.
    xml_file.write_XML_entry_end ();
  }  // method writeAnalysisEntry 


/******************************************************************************/
  private void writeProgramTypeEntry ()
  {
    // Create a program type table XML entry.
    xml_file.write_XML_entry ( "ProgramType" );

    // Set the parameter_list_description field.
    xml_file.write_XML_field ( "program_type_name", "Gene Prediction" );

    // Set the field.
    xml_file.write_XML_field ( "program_type_description", "Gene Prediction program" );

    // End the program type table XML entry.
    xml_file.write_XML_entry_end ();
  }  // method writeProgramTypeEntry 


/******************************************************************************/
  private void writeParameterEntry ()
  {
    // Create a Parameter table XML entry.
    xml_file.write_XML_entry ( "Parameter" );

    // Set the parameter_list_description field.
    xml_file.write_XML_field ( "parameter_list_description", "Parameter matrix" );

    // Set the parameter_list_of_values field.

    xml_file.write_XML_field ( "parameter_list_of_values", matrix );

    // End the Parameter table XML entry.
    xml_file.write_XML_entry_end ();
  }  // method writeParameterEntry


/******************************************************************************/
  private void writeProgramEntry ()
  {
    // Create a Program table XML entry.
    xml_file.write_XML_entry ( "Program" );

    // Set the program name field.
    xml_file.write_XML_field ( "program_name", "GENSCAN" );

    // Set the program version field.
    xml_file.write_XML_field ( "program_version", program_version );

    // Set the program description field.
    xml_file.write_XML_field ( "program_description", 
        "GENSCAN gene prediction program" );

    // Write out a ProgramType XML entry.
    writeProgramTypeEntry ();

    // Write out a Parameter XML entry.
    writeParameterEntry ();

    // End the Program table XML entry.
    xml_file.write_XML_entry_end ();
  }  // method writeProgramEntry


/******************************************************************************/
  private boolean isEndOfExons ()
  {
    String current_line = line.toString ();

    // Check for a blank line.
    if ( current_line.length () <= 0 )  return end_of_exons;

    // Check for the start of a sequence.
    if ( current_line.charAt ( 0 ) == '>' )  end_of_exons = true;

    // Check for the title of the next section.
    if ( current_line.startsWith ( "Predicted" ) )  end_of_exons = true;

    // Check for the end of the file.
    if ( current_line.startsWith ( "Explanation" ) )  end_of_exons = true;

    return end_of_exons;
  }  // method isEndOfExons


/******************************************************************************/
  private void writeClob ()
  {
    // Write the result_clob field.
    xml_file.write_XML_field ( "result_clob" );

    // Close & re-open the GENSCAN file.
    genscan_file.openFile ();

    // Get the first line of the GENSCAN file.
    line = genscan_file.getLine ();

    // Copy the GENSCAN file.
    while ( genscan_file.isEndOfFile () == false )
    {
      // Write the current line.
      xml_file.println ( line.toString () );

      // Get the next line of the GENSCAN file.
      line = genscan_file.getLine ();
    }  // while

    genscan_file.closeFile ();

    // End the clob XML field.
    xml_file.write_XML_field_end ();
  }  // method writeClob


/******************************************************************************/
  public void processFile ()
  {
    // Set the input file name.
    genscan_file.setFileName ( fileName );

    // Open the input file.
    genscan_file.openFile ();

    // Set the XML file name.
    xml_file.setFileName ( fileName + ".xml" );

    // Open the XML file for writing.
    xml_file.open_XML_file ();

    // Write the XML file header. 
    xml_file.write_XML_header ();

    // Process the GENSCAN file header.
    processHeader ();

    // Create a Result table XML entry.
    xml_file.write_XML_entry ( "Result" );

    // Set the Sequence name field.
    xml_file.write_XML_field ( "result_type", "Gene Prediction" );

    // Write the Program table entry.
    writeProgramEntry ();

    // Write the Analysis table entry.
    writeAnalysisEntry ();

    while ( ( genscan_file.isEndOfFile () == false ) &&
            ( genscan_file.isValidFile () == true ) &&
            ( isEndOfExons () == false ) )
    {
      // Get the next GENSCAN prediction.
      nextPrediction ();

      // Write the GENSCAN prediction in XML format.
      write_XML_exon ();

      // snapShot ();
    }  // while 

    // Close the input file and copy to the result_clob field.
    writeClob ();

    // End the Result table XML entry.
    xml_file.write_XML_entry_end ();

    // Write the end of the XML file.
    xml_file.write_XML_footer ();

    // Close the XML file.
    xml_file.close_XML ();

    // Check for no exons written.
    if ( exons_written <= 0 )  xml_file.deleteFile ();
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java GenScanParser <sequence.genes>" );
    System.out.println ();
    System.out.print   ( "where <sequence.genes> is the file name of the " );
    System.out.println ( "GENSCAN output file." );
  }  // method usage

/******************************************************************************/
  public static void main ( String [] args )
  {
    GenScanParser app = new GenScanParser ();

    app.setFileName ( "genscan.genes" );

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.setFileName ( args [ 0 ] );

      app.processFile ();
    }  // else
  }  // method main

}  // class GenScanParser

