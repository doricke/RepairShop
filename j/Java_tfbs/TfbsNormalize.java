
import java.io.*;

// import InputFile;
// import IntArray;
// import OutputFile;
// import TfbsIterator;

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

public class TfbsNormalize extends Object
{

/******************************************************************************/

private final static int ARRAY_SIZE = 374792;		// Chip size


/******************************************************************************/

private  IntArray int_array = new IntArray ();		// integer array

private  OutputFile output_file = new OutputFile ();	// output file


/******************************************************************************/
public TfbsNormalize ()
{
  initialize ();
}  // constructor TfbsNormalize


/******************************************************************************/
private void initialize ()
{
  int_array.setSize ( ARRAY_SIZE );
}  // method initialize


/******************************************************************************/
private void processFile ( String file_name )
{
  TfbsRecord tfbs;
  TfbsIterator tfbs_iterator = new TfbsIterator ( file_name );
  int_array.initialize ();
  int array_index = 0;

  // Set up the output file for the normalized data.
  output_file.setFileName ( file_name + ".norm" );
  output_file.openFile ();

  while ( tfbs_iterator.isEndOfFile () == false )
  {
    tfbs = tfbs_iterator.next ();

    if ( tfbs != null )
    {
      int_array.setValue ( array_index, tfbs.getMatchIntensity () );
      array_index++;
    }  // if
  }  // while

  tfbs_iterator.closeFile ();

  int average = int_array.getAverage ( 300, 3000 );

  int_array.setScaleFactor ( 600.0f / ( average * 1.0f) );

  // Re-open the input file.
  tfbs_iterator.openFile ();

  int index = 0;
  while ( tfbs_iterator.isEndOfFile () == false )
  {
    tfbs = tfbs_iterator.next ();

    if ( tfbs != null )
    {
      output_file.println 
          ( tfbs.getPosition () + "\t" 
          + tfbs.getChromosome () + "\t" 
          + int_array.getScaledValue ( index ) + "\t"
          + tfbs.getOligoSequence ()
          );

      index++;
    }  // if
  }  // while

  tfbs_iterator.closeFile ();
  output_file.closeFile ();
}  // method processFile


/******************************************************************************/
private void processFiles ( String filelist_name )
{
  StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

  // Get the file name of the list of output files
  InputFile name_list = new InputFile ();
  name_list.setFileName ( filelist_name );
  name_list.openFile ();

  // Process the list of output files.
  while ( name_list.isEndOfFile () == false )
  {
    // Read the next line from the list of names file.
    name_line = name_list.nextLine ();

    if ( name_list.isEndOfFile () == false )
    {
      String name = name_line.toString ().trim ();

      // Process this file.
      processFile ( name );
    }  // if
  }  // while

  // Close the input file.
  name_list.closeFile ();
  name_list = null;
}  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TfbsNormalize <filename_list>" );
    System.out.println ();
    System.out.print   ( "where <filename_list> is the file name of a " );
    System.out.println ( "list of Affy. DNA tiling file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TfbsNormalize app = new TfbsNormalize ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class TfbsNormalize

