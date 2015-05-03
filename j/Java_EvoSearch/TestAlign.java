

// import AlignTwo;
// import FastaSequence;
// import InputFile;
// import OutputFile;

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

public class TestAlign extends Object
{

/******************************************************************************/

// Matched Protein Families file.
private  OutputFile  family_file = new OutputFile ();


/******************************************************************************/
public TestAlign ()
{
  initialize ();
}  // constructor TestAlign


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
  public String getSequence ( String file_name )
  {
    // Set up the input file.
    FastaIterator fasta_file = new FastaIterator ( file_name );

    // Get the next FASTA sequence.
    FastaSequence fasta_sequence = fasta_file.next ();

    // Close file.
    fasta_file.closeFile ();

    return fasta_sequence.getSequence ();
  }  // method getSequence


/******************************************************************************/
  public void processFiles ( String file_name )
  {
    StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

    // Get the file name of the list of GeneMark.HMM output files.
    InputFile name_list = new InputFile ();
    name_list.setFileName ( file_name );
    name_list.openFile ();

    family_file.setFileName ( file_name + ".families" );
    family_file.openFile ();

    // Process the list of FASTA protein sequences.
    while ( name_list.isEndOfFile () == false )
    {
      // Read the next line from the list of names file.
      name_line = name_list.nextLine ();
      String name1 = name_line.toString ().trim ();

      // Read the next line from the list of names file.
      name_line = name_list.nextLine ();
      String name2 = name_line.toString ().trim ();

      // Check for end of file.
      if ( ( name1.length () > 0 ) && 
           ( name2.length () > 0 ) )
      {
        String seq1 = getSequence ( name1 );
        String seq2 = getSequence ( name2 );

        // Align the two sequences.
        AlignTwo align2 = new AlignTwo ( seq1, seq2, (byte) 3, AlignTwo.FULL_LENGTH );
        System.out.println ( "Sequence #1: " + name1 );
        System.out.println ( "Sequence #2: " + name2 );
        align2.writeAlignment ();
        align2.initialize ();
        System.out.println ();
      }  // if
    }  // while

    // Close the files.
    name_list.closeFile ();
    close ();
  }  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TestAlign <file.list>" );
    System.out.println ();
    System.out.print   ( "where <file.list> is the file name of the " );
    System.out.println ( "list of protein FASTA file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TestAlign app = new TestAlign ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.processFiles ( args [ 0 ] );
    }  // else
  }  // method main

}  // class TestAlign

