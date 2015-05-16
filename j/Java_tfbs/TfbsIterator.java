
// import TfbsRecord;

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

public class TfbsIterator extends InputFile
{


/******************************************************************************/

  TfbsRecord tfbs_record = new TfbsRecord ();		// Current TFBS record


/******************************************************************************/
public TfbsIterator ()
{
  initialize ();
}  // constructor TfbsIterator


/******************************************************************************/
public TfbsIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method TfbsIterator


/*******************************************************************************/
  public TfbsRecord getTfbsRecord ()
  {
    return tfbs_record;
  }  // method getTfbsRecord


/*******************************************************************************/
  public TfbsRecord next ()
  {
    tfbs_record = new TfbsRecord ();		// create a new object

    // Get the next TFBS records file line.
    nextLine ();

    if ( line.length () <= 0 )  return null;

    // Parse the next TFBS records file line.
    tfbs_record.parseLine ( line.toString () );

    return tfbs_record;
  }  // method next


/******************************************************************************/
public static void main ( String [] args )
{
  TfbsIterator app = new TfbsIterator ();
  TfbsRecord tfbs;

  if ( args.length == 1 )  app.setFileName ( args [ 0 ] );
  else app.setFileName ( "test" );

  // Histogram histo = new Histogram ( app.getFileName (), 100, 3000 );

  app.openFile ();

  while ( app.isEndOfFile () == false )
  {
    tfbs = app.next ();

    if ( tfbs != null )
    {
      // histo.addValue ( tfbs.getMatchIntensity () );

/*
      System.out.println ( tfbs.getOligoSequence () 
          + "\t" + tfbs.getChromosome () 
          + "\t" + tfbs.getMatchIntensity () 
          );
*/
    }  // if
  }  // while
  app.closeFile ();

  // histo.printHistogram ();

}  // method main


/******************************************************************************/

}  // class TfbsIterator

