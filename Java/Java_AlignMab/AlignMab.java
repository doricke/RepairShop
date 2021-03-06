
// import Families;
// import FastaIterator;
// import FastaSequence;
// import InputFile;
// import OutputFile;

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

public class AlignMab extends Object
{


/******************************************************************************/

// Protein gene families.
private  Families  families = new Families ();

private  int total = 0;			// Current number of query proteins


/******************************************************************************/
public AlignMab ()
{
  initialize ();
}  // constructor AlignMab


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/*******************************************************************************/
  public void processHitsFile ( String file_name )
  {
    // System.out.println ( "Processing: " + file_name );

    // families.setIdentity ( true );		// For good alignment
    families.setIdentity ( false );		// For good matching count

    // Set up the input file.
    FastaIterator fasta_file = new FastaIterator ( file_name );
    FastaSequence fasta_seq = null;

    total = 0;
    while ( fasta_file.isEndOfFile () == false )
    {
      fasta_seq = fasta_file.next ();

      if ( fasta_seq != null )

        if ( families.isDuplicate ( fasta_seq ) == false )
        {
          // Get the next FASTA sequence.
          families.addFastaSequence ( fasta_seq );
          total++;
        }  // if
    }  // while

    // Close file.
    fasta_file.closeFile ();

    System.out.println ( total + " sequences read." );

    families.sortHits ();

    families.alignGroup ( 0, "All Sequences" );
  }  // method processHitsFile

/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "This program aligns the .hits files from EvoSearch." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java AlignMab <query.hits>" );
    System.out.println ();
    System.out.print   ( "where <query.hits> is the FASTA file of " );
    System.out.println ( "query protein hit sequences." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    AlignMab app = new AlignMab ();

    if ( args.length <= 0 )
      app.usage ();
    else
      app.processHitsFile ( args [ 0 ] );
  }  // method main

}  // class AlignMab

