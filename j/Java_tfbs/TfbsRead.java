
// import TfbsArray;
// import TfbsIterator;
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

public class TfbsRead extends Object
{

/******************************************************************************/

  // Array of Tfbs records.
  private TfbsArray tfbs_array = new TfbsArray ();


/******************************************************************************/
public TfbsRead ()
{
  initialize ();
}  // constructor TfbsRead


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public TfbsArray getTfbsArray ()
{
  return tfbs_array;
}  // method getTfbsArray


/******************************************************************************/
public void processFile ( String file_name )
{
  TfbsRecord tfbs;
  TfbsIterator tfbs_iterator = new TfbsIterator ( file_name );

  while ( tfbs_iterator.isEndOfFile () == false )
  {
    tfbs = tfbs_iterator.next ();

    if ( tfbs != null )  tfbs_array.add ( tfbs );
  }  // while

  tfbs_iterator.closeFile ();

}  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TfbsRead <filename>" );
    System.out.println ();
    System.out.print   ( "where <filename> is the file name of an " );
    System.out.println ( "Affy. DNA tiling file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TfbsRead app = new TfbsRead ();

    if ( args.length != 1 )
      app.usage ();
    else
    {
      app.processFile ( args [ 0 ] );

      app.tfbs_array.snap ();
    }  // else
  }  // method main

}  // class TfbsRead

