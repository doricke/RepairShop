

// import AminoWord3;
// import OutputTools;
// import WordIterator;

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

public class Words extends Object
{


/******************************************************************************/
public Words ()
{
  initialize ();
}  // constructor Words


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void close ()
{
  // family_file.closeFile ();
}  // method close


/*******************************************************************************/
  public void processFile ( String file_name )
  {
    System.out.println ( "Processing: " + file_name );

    // Set up the input file.
    WordIterator word_file = new WordIterator ( file_name );

    OutputTools new_words = new OutputTools ();
    new_words.setFileName ( file_name + ".s" );
    new_words.openFile ();

    while ( word_file.isEndOfFile () == false )
    {
      // Get the next FASTA sequence.
      AminoWord3 amino_word = word_file.next ();

      // Add the FASTA sequence to the protein families.
      if ( amino_word.isGoodWord () == true )

        new_words.println ( amino_word.toString () );
    }  // while

    // Close file.
    word_file.closeFile ();
    new_words.closeFile ();
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java Words <file.list>" );
    System.out.println ();
    System.out.print   ( "where <file.list> is the file name of the " );
    System.out.println ( "list of protein FASTA file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    Words app = new Words ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.processFile ( args [ 0 ] );
    }  // else
  }  // method main

}  // class Words

