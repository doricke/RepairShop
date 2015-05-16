
import java.io.*;
import java.util.Vector;

// import HistoLengths;
// import InputFile;
// import IntArray;
// import TfbsExperiment;

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

public class TfbsSummary extends Object
{

/******************************************************************************/

private final static int ARRAY_SIZE = 374792;			// Chip size


/******************************************************************************/

private  HistoLengths histo_lengths;

private  IntArray int_array = new IntArray ();		// integer array

private  TfbsExperiment chips = new TfbsExperiment ();	// DNA tiling chips


/******************************************************************************/
public TfbsSummary ()
{
  initialize ();
}  // constructor TfbsSummary


/******************************************************************************/
private void initialize ()
{
  int_array.setSize ( ARRAY_SIZE );
}  // method initialize


/******************************************************************************/
private void processFile ( String file_name, int index )
{
  TfbsRecord tfbs;
  TfbsIterator tfbs_iterator = new TfbsIterator ( file_name );
  int_array.initialize ();
  int array_index = 0;

  while ( tfbs_iterator.isEndOfFile () == false )
  {
    tfbs = tfbs_iterator.next ();

    if ( tfbs != null )
    {
      // histo_lengths.addToHistogram ( index , tfbs.getMatchIntensity () );

      int_array.setValue ( array_index, tfbs.getMatchIntensity () );
      array_index++;
    }  // if
  }  // while

  tfbs_iterator.closeFile ();

  // int average = int_array.getAverage ( 300, 3000 );
  int_array.computeAverage ();
  int average = int_array.getAverage ();
  float std_dev = int_array.getStandardDeviation ( 300, 3000 );

  chips.setRangeAverage ( average, index );
  chips.setStandardDeviation ( std_dev, index );
  chips.setScaleFactor ( 600.0f / (average * 1.0f), index );
  int_array.setScaleFactor ( 600.0f / ( average * 1.0f) );

  // Histogram the scaled values.
  for ( int i = 0; i < int_array.getLength (); i++ )

    histo_lengths.addToHistogram ( index, int_array.getNormalizedValue ( i ) );

}  // method processFile


/******************************************************************************/
private void readNames ( String filelist_name )
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

      System.out.println ( ": " + name );

      // Add the name to the list of file names.
      names.add ( name );
    }  // if
  }  // while

  // Close the input file.
  name_list.closeFile ();
  name_list = null;

  // Allocate the number of chip experiments (files).
  chips.setChips ( names.size () );

  for ( int i = 0; i < names.size (); i++ )
    chips.setFileName ( (String) names.elementAt ( i ), i );
}  // method readNames


/******************************************************************************/
public void processFiles ( String filelist_name )
{
  readNames ( filelist_name );

  histo_lengths = new HistoLengths ( filelist_name, chips.getFileNames (), 1, 30 );

  for ( int i = 0; i < chips.getChips (); i++ )
  {
    System.out.println ( "Processing: " + chips.getFileName ( i ) );
    processFile ( chips.getFileName ( i ), i );
  }  // for

  chips.snap ();

  histo_lengths.makeHTML ();

  histo_lengths.printHistograms ();
}  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TfbsSummary <filename_list>" );
    System.out.println ();
    System.out.print   ( "where <filename_list> is the file name of a " );
    System.out.println ( "list of Affy. DNA tiling file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TfbsSummary app = new TfbsSummary ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class TfbsSummary

