
import java.util.Vector;

import InputTools;
import SeqInfo;
import SeqTools;
import Sequence;
import OutputTools;

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

public class SelectInfo extends Object
{


/******************************************************************************/

private Sequence fasta_file = new Sequence ();	// FASTA sequence input file

private OutputTools unique_names = new OutputTools ();	// Unigene names


/******************************************************************************/
public SelectInfo ()
{
  initialize ();
} // SelectInfo constructor


/******************************************************************************/
private void initialize ()
{
} // method initialize


/******************************************************************************/
public void close ()
{
}  // method close


/******************************************************************************/
private SeqInfo crackLine ( String line )
{
  // Validate line.
  if ( line.length () < 10 )
  {
    System.out.println ( "Short line: " + line );
    return null;
  }  // if

  SeqInfo seq_info = new SeqInfo ();

  // Get the sequence name.
  String sequence_name = "";
  int index = line.indexOf ( ' ' );
  if ( index != -1 )
  {
    sequence_name = line.substring ( 1, index );
    seq_info.setSequenceName ( sequence_name );
  }  // if
  else
  {
    System.out.println ( "*Warning* invalid line - no space after sequence name: " + line );
    return null;
  }  // else

  // Get the contig name.
  index = sequence_name.indexOf ( '_' );
  if ( index != -1 )
  {
    seq_info.setContigName ( sequence_name.substring ( 0, index ) );
  }  // if
  else
    System.out.println ( "*Warning* couldn't find contig name: " + sequence_name );

  // Get the file name.
  index = sequence_name.indexOf ( ':' );
  if ( index == -1 )
    seq_info.setFileName ( sequence_name );
  else
    seq_info.setFileName ( sequence_name.substring ( 0, index ) );

  // Get the genomic coordinates details.
  index = line.indexOf ( "protein[" );
  if ( index > 0 )
  {
    // Get the Complete/Partial indicator.
    if ( line.substring ( index+8, index+16 ).equals ( "Complete" ) == true )
    {
      seq_info.setComplete ( true );
      index = index + 17;
    }  // if
    else
      index = index + 16;

    // Get the genomic strand.
    seq_info.setStrand ( line.charAt ( index ) );

    // Get the genomic region start position.
    seq_info.setGenomicBegin ( 
        InputTools.getInteger ( line.substring ( index+2 ) ) );

    // Get the genomic region end position.
    int index2 = line.indexOf ( '-', index+1 );

    if ( ( index2 > 0 ) && ( index2 < index + 10 ) )
    {
      seq_info.setGenomicEnd (
          InputTools.getInteger ( line.substring ( index2+1 ) ) );
    }  // if
    else
      System.out.println ( "*Warning* could not find genomic end: " + line );

  }  // if

  // Get the Evidence percentage (if present)
  index = line.indexOf ( "Evidence[" );
  if ( index > 0 )
    seq_info.setEvidence ( InputTools.getInteger ( line.substring ( index+9 ) ) );

  if ( line.indexOf ( "Motifs{" ) != -1 )
    seq_info.setMotifs ( true );

  // Check for a start codon.
  index = line.indexOf ( "ATG@" );
  if ( index > 0 )  seq_info.setStartCodon (
      InputTools.getInteger ( line.substring ( index+4 ) ) );

  // Check for a stop codon.
  index = line.indexOf ( "Stop@" );
  if ( index > 0 )  seq_info.setStopCodon (
      InputTools.getInteger ( line.substring ( index+5 ) ) );

  // testing.
  // seq_info.print ();

  return seq_info; 
}  // method crackLine


/******************************************************************************/
private void writeInfo ( SeqInfo seq_info )
{
  // Validate seq_info.
  if ( seq_info == null )  return;

  unique_names.print ( seq_info.getContigName () );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getSequenceName () );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getMrnaLength () );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getGcPercent () );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getEvidence () );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getMrnaType () );

  if ( seq_info.isMotifs() == true )
    unique_names.print ( "\tMotifs\t" );
  else
    unique_names.print ( "\tNo_Motifs\t" );

  unique_names.print ( seq_info.getStrand() );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getGenomicBegin() );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getGenomicEnd() );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getStartCodon () );
  unique_names.print ( "\t" );
  unique_names.print ( seq_info.getStopCodon () );
  unique_names.println (); 
}  // method writeInfo


/******************************************************************************/
private void readSequences ()
{
  StringBuffer seq = null;
  StringBuffer line = null;

  // Read in the header lines.
  while ( fasta_file.isEndOfFile () != true )
  {
    seq = fasta_file.nextSequence ();
    line = fasta_file.getHeaderLine ();

    if ( line.length () > 0 )
    {
      // Crack the header line.
      SeqInfo seq_info = crackLine ( line.toString () );
      seq_info.setGcPercent ( SeqTools.computeGcComposition ( seq.toString () ) );
      seq_info.setMrnaLength ( seq.length () );
      writeInfo ( seq_info );
    }  // if
  }  // while
}  // method readSequences


/******************************************************************************/
// Process the fasta header lines file.
private void processFile ( String fasta_filename, String output_name )
{
  // Set input file name
  fasta_file.setFileName ( fasta_filename );
  fasta_file.setSequenceType ( Sequence.mRNA );

  // Set up the unigene names file.
  unique_names.setFileName ( output_name );
  unique_names.openFile ();

  // Open input file
  fasta_file.openFile ();

  // read in the FASTA sequences.
  readSequences ();

  // Close input file
  fasta_file.closeFile ();
  unique_names.closeFile ();

  close ();
} // method processFile


/******************************************************************************/
private void usage ()
{
  System.out.println ( "The command line syntax for this program is:" );
  System.out.println ();
  System.out.println ( "java SelectInfo <FASTA file>" );
  System.out.println ();
  System.out.print   ( "where <FASTA file> is the file name of a " );
  System.out.println ( "unigene mRNA FASTA file." );
}  // method usage


/******************************************************************************/
public static void main ( String[] args )
{
  SelectInfo app = new SelectInfo ();

  if ( args.length <= 1 ) 
    app.usage ();
  else
    app.processFile ( args [ 0 ], args [ 1 ]);
} // method main

} // class SelectInfo
