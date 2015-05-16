
import java.io.*;

// import InputFile;
// import IntArray;
// import OutputFile;
// import NormalizedIterator;

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

public class TfbsCombine extends Object
{

/******************************************************************************/

private final static int REPLICATES = 3;		// number of replicates


/******************************************************************************/

private  IntArray int_array = new IntArray ();		// integer array

private  OutputFile output_file = new OutputFile ();	// output file


/******************************************************************************/
public TfbsCombine ()
{
  initialize ();
}  // constructor TfbsCombine


/******************************************************************************/
private void initialize ()
{
  int_array.setSize ( REPLICATES );
}  // method initialize


/******************************************************************************/
private String modifyName ( String file_name, char replicate )
{
  StringBuffer new_name = new StringBuffer ( file_name );

  // Search for the replicate number.
  int index = file_name.indexOf ( "_1_" );
  if ( index < 0 )  return file_name;

  // Change the replicate number.
  new_name.setCharAt ( index + 1, replicate );

  return new_name.toString ();
}  // method modifyName


/******************************************************************************/
private void processFile ( String file_name )
{
  System.out.println ( "Processing: " + file_name );

  float std_dev = 0.0f;

  Normalized [] normalized = new Normalized [ REPLICATES ];
  NormalizedIterator [] iterators = new NormalizedIterator [ REPLICATES ];

  iterators [ 0 ] = new NormalizedIterator ( file_name );
  iterators [ 1 ] = new NormalizedIterator ( modifyName ( file_name, '2' ) );
  iterators [ 2 ] = new NormalizedIterator ( modifyName ( file_name, '3' ) );

  int_array.initialize ();

  // Set up the output file for the normalized data.
  output_file.setFileName ( file_name + ".ave" );
  output_file.openFile ();

  while ( iterators [ 0 ].isEndOfFile () == false )
  {
    // Read in the next lines.
    for ( int i = 0; i < REPLICATES; i++ )
    {
      normalized [ i ] = iterators [ i ].next ();

      if ( normalized [ i ] != null )

        int_array.setValue ( i, normalized [ i ].getIntensity () );
    }  // for

    int_array.computeAverage ();
    int_array.computeStandardDeviation ();
    std_dev = int_array.getStandardDeviation ();

    // Ignore outliers for large standard deviations.
    if ( std_dev > 200.0f )  int_array.computeBestAverage ();

    if ( normalized [ 0 ] != null )

      output_file.println 
          ( normalized [ 0 ].getPosition () + "\t"
          + normalized [ 0 ].getChromosome () + "\t"
          + int_array.getAverage () + "\t"
          + normalized [ 0 ].getOligoSequence ()
          + "\t" + int_array.toString ()
          );
  }  // while

  for ( int i = 0; i < REPLICATES; i++ )
  {
    iterators [ i ].closeFile ();
    iterators [ i ] = null;
  }  // for

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
    System.out.println ( "java TfbsCombine <filename_list>" );
    System.out.println ();
    System.out.print   ( "where <filename_list> is the file name of a " );
    System.out.println ( "list of Affy. DNA tiling file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TfbsCombine app = new TfbsCombine ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class TfbsCombine

