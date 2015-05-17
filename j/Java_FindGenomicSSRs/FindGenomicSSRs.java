
import java.io.*;

// import InputTools;
// import PepMrna;
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

public class FindGenomicSSRs extends Object
{

/******************************************************************************/

private Sequence dna_file = new Sequence ();	// DNA input file


/******************************************************************************/
public FindGenomicSSRs ()
{
  initialize ();
}  // constructor FindGenomicSSRs


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
  private void findSSRs ( String dna_seq )
  {
    // Validate the dna sequence.
    if ( dna_seq.length () <= 0 )  return;

    // Search for dinucelotide SSR repeats.
    int ssr_start = 0;
    do 
    {
      // Search for the next dinucleotide Simple Sequence Repeat.
      ssr_start = SeqTools.findDiSSR ( dna_seq, ssr_start );

      if ( ssr_start != -1 )
      {
        String pattern = dna_seq.substring ( ssr_start, ssr_start + 2 );
        int length = SeqTools.findSSRLength 
                         ( dna_seq
                         , ssr_start
                         , pattern
                         );

        System.out.print ( dna_file.getSequenceName () + "\t" + ssr_start + "\t" + pattern + "\t(" + pattern + ")" 
            + (length / 2) + "\t" );

        // Write out the SSR sequence.
        if ( ssr_start + length < dna_seq.length () )
          System.out.print ( dna_seq.substring ( ssr_start, ssr_start + length ) );
        else
          System.out.print ( dna_seq.substring ( ssr_start ) );
        System.out.println ( "\tdi\t" + length );

        ssr_start += length;
      }  // if
    }
    while ( ssr_start >= 0 );


    // Search for trinucelotide SSR repeats.
    ssr_start = 0;
    do 
    {
      // Search for the next trinucleotide Simple Sequence Repeat.
      ssr_start = SeqTools.findTriSSR ( dna_seq, ssr_start );

      if ( ssr_start != -1 )
      {
        String pattern = dna_seq.substring ( ssr_start, ssr_start + 3 );
        int length = SeqTools.findSSRLength 
                         ( dna_seq
                         , ssr_start
                         , pattern
                         );

        System.out.print ( dna_file.getSequenceName () + "\t" + ssr_start + "\t" + pattern + "\t(" + pattern + ")" 
            + (length / 3) + "\t" );

        // Write out the SSR sequence.
        if ( ssr_start + length < dna_seq.length () )
          System.out.print ( dna_seq.substring ( ssr_start, ssr_start + length ) );
        else
          System.out.print ( dna_seq.substring ( ssr_start ) );
        System.out.println ( "\ttri\t" + length );

        ssr_start += length;
      }  // if
    }
    while ( ssr_start >= 0 );


    // Search for quadnucelotide SSR repeats.
    ssr_start = 0;
    do 
    {
      // Search for the next quadnucleotide Simple Sequence Repeat.
      ssr_start = SeqTools.findQuadSSR ( dna_seq, ssr_start );

      if ( ssr_start != -1 )
      {
        String pattern = dna_seq.substring ( ssr_start, ssr_start + 4 );
        int length = SeqTools.findSSRLength 
                         ( dna_seq
                         , ssr_start
                         , pattern
                         );

        System.out.print ( dna_file.getSequenceName () + "\t" + ssr_start + "\t" + pattern + "\t(" + pattern + ")" 
            + (length / 4) + "\t" );

        // Write out the SSR sequence.
        if ( ssr_start + length < dna_seq.length () )
          System.out.print ( dna_seq.substring ( ssr_start, ssr_start + length ) );
        else
          System.out.print ( dna_seq.substring ( ssr_start ) );
        System.out.println ( "\tquad\t" + length );

        ssr_start += length;
      }  // if
    }
    while ( ssr_start >= 0 );

  }  // method findSSRs


/******************************************************************************/
  public void processFile ( String file_name )
  {
    dna_file.initialize ();
    dna_file.setFileName ( file_name );
    dna_file.setSequenceType ( Sequence.DNA );
    dna_file.openFile ();

    // Process all of the sequences in the FASTA library file.
    do
    {
      dna_file.readSequence ();

      // Check the DNA for Simple Sequence Repeats.
      findSSRs ( dna_file.getSequence ().toString () );
    }
    while ( dna_file.isEndOfFile () == false );

    // Close input file.
    dna_file.closeFile();
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
    System.out.println ( "java FindGenomicSSRs <genomic filename list>" );
    System.out.println ();
    System.out.print   ( "where <genomic filename list> is the file name of a " );
    System.out.println ( "list of genomic FASTA sequence file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    FindGenomicSSRs app = new FindGenomicSSRs ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class FindGenomicSSRs

