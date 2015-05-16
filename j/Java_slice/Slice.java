
import java.io.*;

// import InputTools;
// import Sequence;
// import SeqTools;

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

public class Slice extends Object
{

/******************************************************************************/

private Sequence dna_file = new Sequence ();	// DNA input file

private OutputTools slice_file = new OutputTools ();	// Sequence slices


/******************************************************************************/
public Slice ()
{
  initialize ();
}  // constructor Slice


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
  public void processFile ( String file_name )
  {
    // Open the DNA file for reading. 
    dna_file.initialize ();
    dna_file.setFileName ( file_name );
    dna_file.setSequenceType ( Sequence.DNA );
    dna_file.openFile ();

    // Open the output slice file for writing.
    slice_file.initialize ();
    slice_file.setFileName ( file_name + ".slice" );
    slice_file.openFile ();

    // Process all of the sequences in the FASTA library file.
    do
    {
      dna_file.readSequence ();

      String seq = dna_file.getSequence ().toString ();

      dna_file.writeFastaSequence ( "F_1-2k", seq.substring ( 0, 2000 ), slice_file );
      dna_file.writeFastaSequence ( "F_1k-3k", seq.substring ( 1000, 3000 ), slice_file );
      dna_file.writeFastaSequence ( "F_2k-4k", seq.substring ( 2000, 4000 ), slice_file );
      dna_file.writeFastaSequence ( "F_3k-5k", seq.substring ( 3000, 5000 ), slice_file );
      dna_file.writeFastaSequence ( "F_4k-6k", seq.substring ( 4000, 6000 ), slice_file );
      dna_file.writeFastaSequence ( "F_5k-7k", seq.substring ( 5000, 7000 ), slice_file );
      dna_file.writeFastaSequence ( "F_10k-12k", seq.substring ( 10000, 12000 ), slice_file );
      dna_file.writeFastaSequence ( "F_12k-14k", seq.substring ( 12000, 14000 ), slice_file );
      dna_file.writeFastaSequence ( "F_14k-16k", seq.substring ( 14000, 16000 ), slice_file );
      dna_file.writeFastaSequence ( "F_16k-18k", seq.substring ( 16000, 18000 ), slice_file );
      dna_file.writeFastaSequence ( "F_18k-20k", seq.substring ( 18000, 20000 ), slice_file );

      dna_file.writeFastaSequence ( "R_6k-8k", SeqTools.reverseSequence ( seq.substring ( 6000, 8000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_7k-9k", SeqTools.reverseSequence ( seq.substring ( 7000, 9000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_8k-10k", SeqTools.reverseSequence ( seq.substring ( 8000, 10000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_9k-11k", SeqTools.reverseSequence ( seq.substring ( 9000, 11000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_11k-13k", SeqTools.reverseSequence ( seq.substring ( 11000, 13000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_13k-15k", SeqTools.reverseSequence ( seq.substring ( 13000, 15000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_15k-17k", SeqTools.reverseSequence ( seq.substring ( 15000, 17000 ) ), slice_file );
      dna_file.writeFastaSequence ( "R_17k-19k", SeqTools.reverseSequence ( seq.substring ( 17000, 19000 ) ), slice_file );

      dna_file.writeFastaSequence ( "F_10k-12k", seq.substring ( 10000, 12000 ), slice_file );
      dna_file.writeFastaSequence ( "F_10k-12k", seq.substring ( 10000, 12000 ), slice_file );

      dna_file.writeFastaSequence ( "F_11k-20k", seq.substring ( 11000, 20000 ), slice_file );
    }
    while ( dna_file.isEndOfFile () == false );

    // Close input file.
    dna_file.closeFile();
    slice_file.closeFile ();
  }  // method processFile


/******************************************************************************/
public void processFiles ( String filelist_name )
{
  StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

  // Get the file name of the list of output files
  InputTools name_list = new InputTools ();
  name_list.setFileName ( filelist_name );

  name_list.openFile ();

  // Process the list of Fgenesh output files.
  while ( name_list.isEndOfFile () == false )
  {
    // Read the next line from the list of names file.
    name_line = name_list.getLine ();

    if ( name_list.isEndOfFile () == false )
    {
      String name = name_line.toString () .trim ();

      // Process the file.
      processFile ( name );
    }  // if
  }  // while

  name_list.closeFile ();
  name_list = null;
}  // method processFiles



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java Slice <genomic filename list>" );
    System.out.println ();
    System.out.print   ( "where <genomic filename list> is the file name of a " );
    System.out.println ( "list of genomic FASTA sequence file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    Slice app = new Slice ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class Slice

