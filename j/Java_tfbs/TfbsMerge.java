
import java.io.*;
import java.util.Vector;

// import InputFile;
// import IntArray;
// import Normalized;
// import NormalizedIterator;
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

public class TfbsMerge extends Object
{

/******************************************************************************/
public TfbsMerge ()
{
  initialize ();
}  // constructor TfbsMerge


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
private Vector readNames ( String filelist_name )
{
  StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

  Vector names = new Vector ();			// Input file names

  // Get the file name of the list of output files
  InputFile name_list = new InputFile ();
  name_list.setFileName ( filelist_name );

  name_list.openFile ();

  // Process the list of Fgenesh output files.
  while ( name_list.isEndOfFile () == false )
  {
    // Read the next line from the list of names file.
    name_line = name_list.nextLine ();

    if ( name_list.isEndOfFile () == false )
    {
      String name = name_line.toString () .trim ();

      // Add the name to the list of file names.
      names.add ( name );
    }  // if
  }  // while

  // Close the input file.
  name_list.closeFile ();
  name_list = null;

  return names;
}  // method readNames


/******************************************************************************/
public void processFiles ( String filelist_name )
{
  Vector names = readNames ( filelist_name );

  // Create the merged output file.
  OutputFile merge = new OutputFile ( filelist_name + ".merge" );
  merge.openFile ();

  merge.print ( "Position\tChromosome\tBest Average\tOligo Sequence\t" );

  // Open the input files.
  NormalizedIterator iterators [] = new NormalizedIterator [ names.size () ];
  for ( int i = 0; i < names.size (); i++ )
  {
    iterators [ i ] = new NormalizedIterator ( (String) names.elementAt ( i ) );
    merge.print ( (String) names.elementAt ( i ) + "\t" );
  }  // for
  merge.println ( "Std Dev" );

  IntArray int_array = new IntArray ();
  int_array.setSize ( names.size () );
  Normalized [] normalized = new Normalized [ names.size () ];
  float std_dev = 0.0f;
  while ( iterators [ 0 ].isEndOfFile () == false )
  {
    for ( int i = 0; i < names.size (); i++ )
    {
      normalized [ i ] = iterators [ i ].next ();

      if ( normalized [ i ] != null )

        int_array.setValue ( i, normalized [ i ].getIntensity () );
    }  // for

    if ( normalized [ 0 ] != null )
    {
      int_array.computeAverage ();
      int_array.computeStandardDeviation ();
      std_dev = int_array.getStandardDeviation ();
  
      // Ignore outliers for large standard deviations.
      if ( std_dev > 200.0f )  int_array.computeBestAverage ();
  
      merge.println
          ( normalized [ 0 ].getPosition () + "\t"
          + normalized [ 0 ].getChromosome () + "\t"
          + int_array.getAverage () + "\t"
          + normalized [ 0 ].getOligoSequence () + "\t"
          + int_array.toString ()
          );
    }  // if

    int_array.initialize ();
  }  // while

  for ( int i = 0; i < names.size (); i++ )
  {
    iterators [ i ].closeFile ();
  }  // for

  merge.closeFile ();
}  // method processFiles



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The program merges experiments together." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TfbsMerge <filename_list>" );
    System.out.println ();
    System.out.print   ( "where <filename_list> is the file name of a " );
    System.out.println ( "list of Affy. DNA tiling file names" );
    System.out.println ( "of sorted normalized averages." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TfbsMerge app = new TfbsMerge ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class TfbsMerge

