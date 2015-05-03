

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

public class AlignHits extends Object
{


/******************************************************************************/

// Protein gene families.
private  Families  families = new Families ();

private  int total = 0;			// Current number of query proteins


/******************************************************************************/
public AlignHits ()
{
  initialize ();
}  // constructor AlignHits


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/*******************************************************************************/
  public void processHitsFile ( String file_name )
  {
    System.out.println ( "Processing: " + file_name );

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

    families.groupGenes ();

    families.summarize ();

    int group_count = families.getGroups ();

    // Superfamily alignments.
    Families super_family = new Families ();
    super_family.setIdentity ( true );

    // Add the first sequence to the multi-group alignment.
    super_family.addFastaSequence ( families.getFastaSequence ( 0 ) );

    // OutputFile align_file = new OutputFile ();
    String group_name = "";
    for ( int i = 1; i <= group_count; i++ )
    {
      group_name = families.getGroupName ( i );

      families.alignGroup ( i, group_name );

      // Write the MSA to the file.
/*
      align_file.setFileName ( group_name + ".fam" );
      align_file.openFile ();
      align_file.print ( families.getMsa ( group_name ) );
      align_file.closeFile ();
*/
      FastaSequence cons = new FastaSequence ();
      cons.setName ( group_name );
      cons.setSequence ( Msa.deGap ( Msa.deSpace ( families.getConsensus () ) ) );

      super_family.addFastaSequence ( cons );
    }  // for

    super_family.alignGroup ( 0, "super" );

    // super_family.zipperGroup ( 0, "supper" );
  }  // method processHitsFile

/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "This program aligns the .hits files from EvoSearch." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java AlignHits <query.hits>" );
    System.out.println ();
    System.out.print   ( "where <query.hits> is the FASTA file of " );
    System.out.println ( "query protein hit sequences." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    AlignHits app = new AlignHits ();

    if ( args.length <= 0 )
      app.usage ();
    else
      app.processHitsFile ( args [ 0 ] );
  }  // method main

}  // class AlignHits

