

// import Families;
// import FastaIterator;
// import FastaSequence;
// import InputTools;
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

public class EvoSearch extends Object
{

/******************************************************************************/

// Matched Protein Families file.
private  OutputTools  family_file = new OutputTools ();

// Protein families information.
private  Families families = new Families ();


/******************************************************************************/
public EvoSearch ()
{
  initialize ();
}  // constructor EvoSearch


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void close ()
{
  family_file.closeFile ();
}  // method close


/*******************************************************************************/
  public void processFile ( String file_name )
  {
    System.out.println ( "Processing: " + file_name );

    // Set up the input file.
    FastaIterator fasta_file = new FastaIterator ( file_name );

    while ( fasta_file.isEndOfFile () == false )
    {
      // Get the next FASTA sequence.
      FastaSequence fasta_sequence = fasta_file.next ();

      // Add the FASTA sequence to the protein families.
      if ( fasta_sequence.getLength () > 0 )

        families.addFasta ( fasta_sequence );
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

    family_file.setFileName ( file_name + ".families" );
    family_file.openFile ();

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

    // Summarize the protein family information.
    families.summarize ();

    // Close the files.
    name_list.closeFile ();
    close ();
  }  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java EvoSearch <file.list>" );
    System.out.println ();
    System.out.print   ( "where <file.list> is the file name of the " );
    System.out.println ( "list of protein FASTA file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    EvoSearch app = new EvoSearch ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.processFiles ( args [ 0 ] );
    }  // else
  }  // method main

}  // class EvoSearch

