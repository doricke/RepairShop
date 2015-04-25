

// import AlignMany;
// import FastaSequence;
// import InputTools;
// import OutputTools;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

public class Msa extends Object
{


/******************************************************************************/

  final static int MAX_SEQUENCES = 9;


/******************************************************************************/

// Matched Protein Families file.
// private  OutputTools  family_file = new OutputTools ();


/******************************************************************************/

  int current = 0;

  String [] sequences = new String [ MAX_SEQUENCES ];	// array of sequences


/******************************************************************************/
public Msa ()
{
  initialize ();
}  // constructor Msa


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void close ()
{
  // family_file.closeFile ();
}  // method close


/*******************************************************************************/
  private void addFasta ( FastaSequence fasta_sequence )
  {
    if ( current < MAX_SEQUENCES )

      // Create a list of amino words for the fasta sequence.
      sequences [ current ] = fasta_sequence.getSequence ();

    current++;
  }  // method addFasta


/*******************************************************************************/
  private void msa_align ()
  {
    AlignMany align_many = new AlignMany ( sequences, (byte) 5 );

    align_many.printAlignment ();
  }  // method msa_align


/*******************************************************************************/
  public void processFile ( String file_name )
  {
    System.out.println ( (current+1) + ": " + file_name );

    // Set up the input file.
    FastaIterator fasta_file = new FastaIterator ( file_name );

    while ( fasta_file.isEndOfFile () == false )
    {
      // Get the next FASTA sequence.
      FastaSequence fasta_sequence = fasta_file.next ();

      // Add the FASTA sequence to the protein families.
      if ( fasta_sequence.getLength () > 0 )

        addFasta ( fasta_sequence );
    }  // while

    // Close file.
    fasta_file.closeFile ();
  }  // method processFile


/******************************************************************************/
  public void processFiles ( String file_name )
  {
    StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

    // Get the file name of the list of GeneMark.HMM output files.
    InputTools name_list = new InputTools ();
    name_list.setFileName ( file_name );
    name_list.openFile ();

    // family_file.setFileName ( file_name + ".families" );
    // family_file.openFile ();

    // Process the list of FASTA protein sequences.
    while ( name_list.isEndOfFile () == false )
    {
      // Read the next line from the list of names file.
      name_line = name_list.getLine ();

      if ( name_list.isEndOfFile () == false )
      {
        String name = name_line.toString ().trim ();

        // Process this file
        processFile ( name );
      }  // if
    }  // while

    // Align the multiple sequences. 
    msa_align ();

    // Close the files.
    name_list.closeFile ();
    close ();
  }  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java Msa <file.list>" );
    System.out.println ();
    System.out.print   ( "where <file.list> is the file name of the " );
    System.out.println ( "list of protein FASTA file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    Msa app = new Msa ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.processFiles ( args [ 0 ] );
    }  // else
  }  // method main

}  // class Msa

