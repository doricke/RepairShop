

// import FastaIterator;
// import FastaSequence;
// import OutputTools;

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

public class FastaSelect extends Object
{


/******************************************************************************/
public FastaSelect ()
{
  initialize ();
}  // constructor FastaSelect


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/*******************************************************************************/
  public void processFile ( String file_name )
  {
    // Set up the input file.
    FastaIterator old_fasta_file = new FastaIterator ( file_name );

    // Set up the output file.
    OutputTools new_fasta_file = new OutputTools ( file_name + ".Select" );

    // Process the sequences in the FASTA file.
    while ( old_fasta_file.isEndOfFile () == false )
    {
      // Get the next FASTA sequence.
      FastaSequence fasta_sequence = old_fasta_file.next ();

      // System.out.println ( fasta_sequence.getName + " len = " + fasta_sequence.getLength () );

      // Add the FASTA sequence to the protein families.
      if ( fasta_sequence.getLength () >= 1000 )

        // Write out this sequence.
        new_fasta_file.print ( fasta_sequence.toString () );
    }  // while

    // Close files.
    old_fasta_file.closeFile ();
    new_fasta_file.closeFile ();
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

    // Close the files.
    name_list.closeFile ();
  }  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java FastaSelect <FASTA.file>" );
    System.out.println ();
    System.out.print   ( "where <FASTA.file> is the file name of the " );
    System.out.println ( "FASTA file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    FastaSelect app = new FastaSelect ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.processFile ( args [ 0 ] );
    }  // else
  }  // method main

}  // class FastaSelect

