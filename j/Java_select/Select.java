
import java.util.Vector;

import InputTools;
import SeqInfo;
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

public class Select extends Object
{


/******************************************************************************/

private InputTools headers_file = new InputTools ();	// Motifs input file

private OutputTools unique_names = new OutputTools ();	// Unigene names


/******************************************************************************/
public Select ()
{
  initialize ();
} // Select constructor


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

  // testing.
  // seq_info.print ();

  return seq_info; 
}  // method crackLine


/******************************************************************************/
private SeqInfo [] covert ( Vector info )
{
  if ( ( info == null ) || ( info.size () <= 0 ) )  return null;

  SeqInfo seq_info [] = new SeqInfo [ info.size () ];

  for ( int i = 0; i < info.size (); i++ )

    seq_info [ i ] = (SeqInfo) info.elementAt ( i );

  return seq_info;
}  // method covert


/******************************************************************************/
private void writeUnique ( SeqInfo [] seq_info )
{
  // Validate seq_info.
  if ( ( seq_info == null ) || ( seq_info.length <= 0 ) )  return;

  // Scan the sequences for selected sequences.
  for ( int i = 0; i < seq_info.length; i++ )

    // Check for a selected sequence.
    if ( seq_info [ i ] != null )

      unique_names.println ( seq_info [ i ].getFileName () );
}  // method writeUnique


/******************************************************************************/
// This method selects the better sequence.
private int select ( SeqInfo [] seq_info, int i, int j )
{
  // Compare the evidence level.
  if ( seq_info [ i ].getEvidence () > seq_info [ j ].getEvidence () )
    return i;
  else
  {
    if ( seq_info [ i ].getEvidence () < seq_info [ j ].getEvidence () )
      return j;
    else  // same level of evidence
    {
      // Check for a Motif on only one of the sequences.
      if ( ( seq_info [ i ].isMotifs () == true ) &&
           ( seq_info [ j ].isMotifs () == false ) )  return i;

      if ( ( seq_info [ j ].isMotifs () == true ) &&
           ( seq_info [ i ].isMotifs () == false ) )  return j;

      // Check if one of the sequences is complete.
      if ( ( seq_info [ i ].isComplete () == true ) &&
           ( seq_info [ j ].isComplete () == false ) )  return i;

      if ( ( seq_info [ j ].isComplete () == true ) &&
           ( seq_info [ i ].isComplete () == false ) )  return j;

      // Select the longer sequence.
      if ( seq_info [ i ].getLength () >= seq_info [ j ].getLength () )
        return i;
      else
        return j;
    }  // else
  }  // else
}  // method select


/******************************************************************************/
private SeqInfo [] sort ( SeqInfo [] seq_info )
{
  SeqInfo temp = null;

  // Validate seq_info.
  if ( ( seq_info == null ) || ( seq_info.length <= 1 ) )  return seq_info;

  // Bubble sort.
  for ( int i = 0; i < seq_info.length - 1; i++ )
  {
    for ( int j = i + 1; j < seq_info.length; j++ )

      // Sort on the genomic_begin field.
      if ( seq_info [ j ].getGenomicBegin () < seq_info [ i ].getGenomicBegin () )
      {
        // Swap the two objects.
        temp           = seq_info [ j ];
        seq_info [ j ] = seq_info [ i ];
        seq_info [ i ] = temp;
      }  // if

  }  // for

  return seq_info;
}  // method sort


/******************************************************************************/
private SeqInfo [] selectBest ( SeqInfo [] seq_info ) 
{
  // Validate seq_info.
  if ( ( seq_info == null ) || ( seq_info.length <= 0 ) )  return seq_info;

  // Compare the sequences.
  for ( int i = 0; i < seq_info.length - 1; i++ )
  {
   if ( seq_info [ i ] != null )
   {
    for ( int j = i + 1; j < seq_info.length; j++ )

     if ( seq_info [ j ] != null )
     {
      // Check for an overlap & same strand.
      if ( ( seq_info [ j ].getGenomicBegin () < seq_info [ i ].getGenomicEnd () ) &&
           ( seq_info [ j ].getGenomicEnd () > seq_info [ i ].getGenomicBegin () ) )
//           ( seq_info [ j ].getStrand () == seq_info [ i ].getStrand () ) )
      {
        // Determine the region of overlap.
        int begin = seq_info [ i ].getGenomicBegin ();
        if ( begin < seq_info [ j ].getGenomicBegin () )
          begin = seq_info [ j ].getGenomicBegin ();

        int end   = seq_info [ i ].getGenomicEnd ();
        if ( end < seq_info [ j ].getGenomicEnd () )
          end = seq_info [ j ].getGenomicEnd ();

        int length = end - begin + 1;

        if ( ( length * 100 >= ( seq_info [ i ].getGenomicEnd () -
                                 seq_info [ i ].getGenomicBegin () + 1 ) * 70 ) ||
             ( length * 100 >= ( seq_info [ j ].getGenomicEnd () -
                                 seq_info [ j ].getGenomicBegin () + 1 ) * 70 ) )
        {
          // Select the better sequence.
          int k = select ( seq_info, i, j );

          // Erase the alternate name.
          if ( k != j )  seq_info [ j ] = null;
          else
          {
            seq_info [ i ] = null;
            j = seq_info.length;	// exit inner loop
          }  // else
        }  // if
      }  // if
     }  // if
   }  // if
  }  // for

  return seq_info;
}  // method selectBest


/******************************************************************************/
private void selectUnique ( Vector info )
{
  SeqInfo [] seq_info = covert ( info );

  // Sort the sequences.
  seq_info = sort ( seq_info );

  // Set the unique sequences flags.
  seq_info = selectBest ( seq_info );

  // Write out the unique sequences.
  writeUnique ( seq_info );
}  // method selectUnique


/******************************************************************************/
private void readHeaders ()
{
  String contig_name = "";
  Vector info = new Vector ();
  StringBuffer line = headers_file.getLine ();

  // Read in the header lines.
  while ( headers_file.isEndOfFile () != true )
  {
    if ( line.length () > 0 )
    {
      // Crack the header line.
      SeqInfo seq_info = crackLine ( line.toString () );

      // Check for a header line.
      if ( seq_info != null )
      {
        // Check for a new contig name.
        if ( seq_info.getContigName ().equals ( contig_name ) )
          info.add ( seq_info );
        else
        {
          selectUnique ( info );
          info.removeAllElements ();
          info.add ( seq_info );
          contig_name = seq_info.getContigName ();
          System.out.println ( "Processing: " + contig_name );
        }  // else
      }  // if
    }  // if

    line = headers_file.getLine ();
  }  // while

  // Check for last contig.
  if ( info.size () > 0 )
  {
    selectUnique ( info );
    info.removeAllElements ();
  }  // if
}  // method readHeaders


/******************************************************************************/
// Process the fasta header lines file.
private void processFile ( String headers_filename )
{
  // Set input file name
  headers_file.setFileName ( headers_filename );

  // Set up the unigene names file.
  unique_names.setFileName ( "unigene.names" );
  unique_names.openFile ();

  // Open input file
  headers_file.openFile ();

  // read in the FASTA header lines.
  readHeaders ();

  // Close input file
  headers_file.closeFile ();
  unique_names.closeFile ();

  close ();
} // method processFile


/******************************************************************************/
private void usage ()
{
  System.out.println ( "The command line syntax for this program is:" );
  System.out.println ();
  System.out.println ( "java Select <header_lines>" );
  System.out.println ();
  System.out.print   ( "where <header_lines> is the file name of the " );
  System.out.println ( "grepped FASTA header lines file." );
}  // method usage


/******************************************************************************/
public static void main ( String[] args )
{
  Select app = new Select ();

  if ( args.length == 0 ) 
    app.usage ();
  else
    app.processFile ( args [ 0 ]);
} // method main

} // class Select
