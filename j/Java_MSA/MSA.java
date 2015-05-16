
import java.io.*;
import java.util.Vector;

// import InputTools;
// import Sequence;
// import SequenceAlignment;

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

public class MSA extends Object
{

/******************************************************************************/

private Vector alignments = new Vector ();	// individual alignments

private Sequence seq_file = new Sequence ();	// Peptide input file


/******************************************************************************/
public MSA ()
{
  initialize ();
}  // constructor MSA


/******************************************************************************/
private void initialize ()
{
  alignments.removeAllElements ();
  seq_file.initialize ();
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
    seq_file.initialize ();
    seq_file.setFileName ( file_name );
    seq_file.setSequenceType ( Sequence.AA );
    seq_file.openFile ();

    // Read in the sequences.
    seq_file.readSequence ();

    // System.out.println ( "Protein sequence length = " + seq_file.getSequenceLength () );

    SequenceAlignment seq_align = new SequenceAlignment ();
    seq_align.setFileName ( file_name );
    seq_align.setSequenceName ( seq_file.getSequenceName () );
    seq_align.setDescription ( seq_file.getSequenceDescription () );
    seq_align.setSequence ( seq_file.getSequence () );

    alignments.add ( seq_align );

    // Close input file.
    seq_file.closeFile();
  }  // method processFile


/******************************************************************************/
private void alignSequences ()
{
  for ( int i = 0; i < alignments.size (); i++ )
  {
    SequenceAlignment align1 = (SequenceAlignment) alignments.elementAt ( i );

    int best_percent = 0;
    int best_index = -1;

    System.out.print ( align1.getSequenceName () + "\t" );

    for ( int j = 0; j < alignments.size (); j++ ) 

      if ( i != j )
      {
        SequenceAlignment align2 = (SequenceAlignment) alignments.elementAt ( j );
  
        // System.out.println ();
        // System.out.println ( align1.getSequenceName () + " versus " + align2.getSequenceName () );
  
        int percent = align1.align ( align2.getSequence () );

        System.out.print ( percent + "%\t" );

        if ( ( j < i ) && ( percent > best_percent ) )
        {
          best_percent = percent;
          best_index = j;
        }  // if
      }  // for
      else
        System.out.print ( "100%\t" );

    System.out.println ();

    align1.setBestIndex ( best_index );
    align1.setBestPercent ( best_percent );

    alignments.setElementAt ( align1, i );
  }  // for

  System.out.println ();

  // Print out the conservation levels.
  for ( int i = 0; i < alignments.size (); i++ )
  {
    SequenceAlignment align1 = (SequenceAlignment) alignments.elementAt ( i );

    int best_index = align1.getBestIndex ();
    System.out.print ( align1.getFileName () + "\t" );
    if ( best_index >= 0 )
    {
      SequenceAlignment align2 = (SequenceAlignment) alignments.elementAt ( best_index );
      System.out.print ( align2.getFileName () + "\t" );
      System.out.print ( align1.getBestPercent () + "%" );
    }  // if
    System.out.println ();
    align1.printConservedLevels ();
  }  // for
}  // method alignSequences


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

      // System.out.println ( "Processing: " + name );

      // Process the file.
      processFile ( name );
    }  // if
  }  // while

  name_list.closeFile ();
  name_list = null;

  // System.out.println ( "alignments has " + alignments.size () + " sequences." );

  alignSequences ();

}  // method processFiles



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java MSA <peptides filename list>" );
    System.out.println ();
    System.out.print   ( "where <peptides filename list> is the file name of a " );
    System.out.println ( "list of peptide file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    MSA app = new MSA ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class MSA

