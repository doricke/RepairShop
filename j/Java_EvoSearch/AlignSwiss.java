

// import Families;
// import FastaIterator;
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

public class AlignSwiss extends Object
{


/******************************************************************************/

// Protein gene families.
private  Families  families = new Families ();

private  int total = 0;			// Current number of query proteins


/******************************************************************************/
public AlignSwiss ()
{
  initialize ();
}  // constructor AlignSwiss


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/*******************************************************************************/
  public void processHitsFile ( String file_name )
  {
    System.out.println ( "Processing: " + file_name );

    String id_name = "";		// current group ID name
    String next_id = "";		// next group ID name

    // Set up the input file.
    FastaIterator fasta_file = new FastaIterator ( file_name );
    FastaSequence next_seq = null;

    total = 0;
    while ( fasta_file.isEndOfFile () == false )
    {
      // Get the next FASTA sequence.
      next_seq = fasta_file.next ();
      next_id = next_seq.getId ();

      // System.out.println ( "-> " + next_seq.getName () );

      // Check if another member of this ID group.
      if ( id_name.equals ( next_id ) == true )
      {
        families.addFastaSequence ( next_seq );
      }  // if
      else
      {
        // Summarize the current ID group.
        if ( id_name.length () > 0 )
          families.alignGroup ( 0, id_name );

        // Start the next group.
        id_name = next_id;
        total = 0;
        families.initialize ();
        families.addFastaSequence ( next_seq );
      }  // else

      total++;
    }  // while

    // Close file.
    fasta_file.closeFile ();

/*
      // Write the MSA to the file.
      // OutputFile align_file = new OutputFile ();
      align_file.setFileName ( group_name + ".fam" );
      align_file.openFile ();
      align_file.print ( families.getMsa ( group_name ) );
      align_file.closeFile ();
*/
  }  // method processHitsFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "This program aligns the .hits files from EvoSearch." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java AlignSwiss <query.hits>" );
    System.out.println ();
    System.out.print   ( "where <query.hits> is the FASTA file of " );
    System.out.println ( "query protein hit sequences." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    AlignSwiss app = new AlignSwiss ();

    if ( args.length <= 0 )
      app.usage ();
    else
      app.processHitsFile ( args [ 0 ] );
  }  // method main

}  // class AlignSwiss

