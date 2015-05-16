
// import Abstract;
// import AbstractIterator;
// import SeqTools;
// import WordTools;

import java.lang.Integer;
import java.util.*;

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

public class ParserMedline extends Object
{

/******************************************************************************/

  Abstract article_abstract = null;		// Current article abstract

  // Medline file abstract iterator.
  AbstractIterator abstract_iterator = new AbstractIterator ();

  // Sorted words.
  TreeSet sorted_words = new TreeSet ();

  // Word processing tools.
  WordTools word_tools = new WordTools ();

  StringBuffer word_sequence = new StringBuffer ( 10000 );


/******************************************************************************/
public ParserMedline ()
{
  initialize ();
}  // constructor ParserMedline


/******************************************************************************/
private void initialize ()
{
  sorted_words.clear ();
  word_sequence.setLength ( 0 );
}  // method initialize


/******************************************************************************/
private String toSequence ()
{
  String word = "";

  word_sequence.setLength ( 0 );
  Object [] words = sorted_words.toArray ();
  if ( words.length <= 0 )  return "";

  word = (String) words [ 0 ];
  word_sequence.append ( word );

  for ( int i = 1; i < words.length; i++ )
  {
    word = (String) words [ i ];
    word_sequence.append ( "_" + word );
  }  // for

  return word_sequence.toString ();
}  // method toSequence


/******************************************************************************/
private void processAbstract ( OutputTools fasta_file, OutputTools words_file )
{
  sorted_words.clear ();

  sorted_words = word_tools.split ( article_abstract.getTitle (), sorted_words );
  word_tools.writeWords ( words_file );

  sorted_words = word_tools.split ( article_abstract.getAbstract (), sorted_words );
  word_tools.writeWords ( words_file );

  // Write the words to the FASTA file.
  fasta_file.println ( ">PMID_" + article_abstract.getPmid () );
  SeqTools.writeFasta ( fasta_file, toSequence () );
}  // method processAbstract


/******************************************************************************/
  public void processFile 
      ( String       medline_filename
      , OutputTools  fasta_file 
      , OutputTools  words_file 
      )
  {
    // Initialize.
    initialize ();

    // Set input filename.
    abstract_iterator.setFileName ( medline_filename );
  
    // Open the input file.
    abstract_iterator.openFile ();

    abstract_iterator.nextLine ();

    // Read in each gene prediction.
    while ( abstract_iterator.isEndOfFile () == false )  
    {
      article_abstract = abstract_iterator.next ();

      processAbstract ( fasta_file, words_file );
    }  // while
 
    // Close input file.
    abstract_iterator.closeFile ();
  }  // method processFile 


/******************************************************************************/
  public void processFiles ( String file_name )
  {
    StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

    // Get the file name of the list of GeneMark.HMM output files.
    InputTools name_list = new InputTools ();
    name_list.setFileName ( file_name );
    name_list.openFile ();

    // Open the peptide sequences output file.
    OutputTools fasta_file = new OutputTools ();
    fasta_file.setFileName ( file_name + ".fasta" );
    fasta_file.openFile ();

    // Open the mRNA sequences output file.
    OutputTools words_file = new OutputTools ();
    words_file.setFileName ( file_name + ".words" );
    words_file.openFile ();

    // Process the list of GeneMark.HMM output files.
    while ( name_list.isEndOfFile () == false )
    {
      // Read the next line from the list of names file.
      name_line = name_list.nextLine ();

      if ( name_list.isEndOfFile () == false )
      {
        String name = name_line.toString ().trim ();

        System.out.println ( "Processing: " + name );

        // Process the GeneMark.HMM file
        processFile ( name, fasta_file, words_file );
      }  // if
    }  // while

    // Close the files.
    name_list.closeFile ();
    fasta_file.closeFile ();
    words_file.closeFile ();
  }  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java ParserMedline <file.medline>" );
    System.out.println ();
    System.out.print   ( "where <file.medline> is the file name of the " );
    System.out.println ( "Medline abstracts file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    ParserMedline app = new ParserMedline ();

    if ( args.length == 0 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

/******************************************************************************/

}  //class ParserMedline
