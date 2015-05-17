
import java.io.*;

// import CodonUsage;
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

public class PepMrnaAlign extends Object
{

/******************************************************************************/

private Sequence mrna_file = new Sequence ();	// mRNA input file

private Sequence pep_file = new Sequence ();	// Peptide input file

private PepMrna pep_mrna = new PepMrna ();	// Peptide:mRNA alignment


/******************************************************************************/
public PepMrnaAlign ()
{
  initialize ();
}  // constructor PepMrnaAlign


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
private String makeMrnaName ( String peptide_filename )
{
  int index = peptide_filename.indexOf ( ".aa" );

  if ( index < 0 )
    return peptide_filename + ".mrna";
  else
    return peptide_filename.substring ( 0, index ) + ".mrna";
}  // method makeMrnaName


/******************************************************************************/
  public void processFile ( String file_name )
  {
    // Set the input file name.
    pep_file.initialize ();
    pep_file.setFileName ( file_name );
    pep_file.setSequenceType ( Sequence.AA );
    pep_file.openFile ();

    // Read in the sequences.
    pep_file.readSequence ();

    // System.out.println ( "Protein sequence length = " + pep_file.getSequenceLength () );

    pep_mrna.setPeptide ( pep_file.getSequence ().toString () );

    mrna_file.initialize ();
    mrna_file.setFileName ( makeMrnaName ( file_name ) );
    pep_file.setSequenceType ( Sequence.mRNA );
    mrna_file.openFile ();

    mrna_file.readSequence ();

    // System.out.println ( "mRNA sequence length = " + mrna_file.getSequenceLength () );

    // Ignore short mRNAs.
    // if ( mrna_file.getSequenceLength () >= 100 )
    {
      pep_mrna.setMrna ( mrna_file.getSequence ().toString () );

      // Align the peptide sequence to the mRNA sequence.
      pep_mrna.align ( file_name );
    }  // if

    // Close input file.
    pep_file.closeFile();
    mrna_file.closeFile();
  }  // method processFile


/******************************************************************************/
public void processFiles ( String filelist_name )
{
  CodonUsage.printHeader ();

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

      // System.out.println ( "Processing: " + name );

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
    System.out.println ( "java PepMrnaAlign <peptides filename list>" );
    System.out.println ();
    System.out.print   ( "where <peptides filename list> is the file name of a " );
    System.out.println ( "list of peptide file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    PepMrnaAlign app = new PepMrnaAlign ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class PepMrnaAlign

