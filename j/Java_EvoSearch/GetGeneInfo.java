

// import FastaIterator;
// import FastaSequence;
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

public class GetGeneInfo extends Object
{


/******************************************************************************/
public GetGeneInfo ()
{
  initialize ();
}  // constructor GetGeneInfo


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/*******************************************************************************/
  public void processHitsFile ( String file_name )
  {
    // System.out.println ( "Processing: " + file_name );

    // Set up the input file.
    FastaIterator fasta_file = new FastaIterator ( file_name );
    FastaSequence next_seq = null;

    while ( fasta_file.isEndOfFile () == false )
    {
      // Get the next FASTA sequence.
      next_seq = fasta_file.next ();
      System.out.println ( next_seq.getGeneInfo () );
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
    System.out.println ( "java GetGeneInfo <query.hits>" );
    System.out.println ();
    System.out.print   ( "where <query.hits> is the FASTA file of " );
    System.out.println ( "query protein hit sequences." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    GetGeneInfo app = new GetGeneInfo ();

    if ( args.length <= 0 )
      app.usage ();
    else
      app.processHitsFile ( args [ 0 ] );
  }  // method main

}  // class GetGeneInfo

