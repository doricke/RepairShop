
// import Hu19k_Data;

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

public class Hu19kIterator extends InputFile
{


/******************************************************************************/

  Hu19k_Data hu19k_row = new Hu19k_Data ();		// Current ChIp spot record


/******************************************************************************/
public Hu19kIterator ()
{
  initialize ();
}  // constructor Hu19kIterator


/******************************************************************************/
public Hu19kIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method Hu19kIterator


/*******************************************************************************/
  public Hu19k_Data getHu19k_Row ()
  {
    return hu19k_row;
  }  // method getHu19k_Row


/*******************************************************************************/
  public Hu19k_Data next ()
  {
    hu19k_row = new Hu19k_Data ();	// create a new object

    // Get the next ChIp promoter spot file line.
    nextLine ();

    if ( line.length () <= 0 )  return null;

    // Parse the next ChIp spot file line.
    hu19k_row.parseLine ( line.toString () );

    return hu19k_row;
  }  // method next


/******************************************************************************/
  public void findData ()
  {
    // Find the data header line.
    do
    {
      nextLine ();

      if ( ( line.toString ().indexOf ( "Block" ) == 0 ) ||
           ( line.toString ().indexOf ( "Block" ) == 1 ) )  return;
    }
    while ( isEndOfFile () == false );
  }  // method findData


/******************************************************************************/
public static void main ( String [] args )
{
  Hu19kIterator app = new Hu19kIterator ();
  Hu19k_Data hu19k_row = new Hu19k_Data ();

  if ( args.length >= 1 )  app.setFileName ( args [ 0 ] );
  else
  {
     System.out.println ( "usage: java Hu19kIterator <gpr_file>" );
     System.exit ( 0 );
  }  // else

  String label = "";
  if ( args.length >= 2 )  label = args [ 1 ];

  app.openFile ();

  // Find the data header line.
  app.findData ();

  hu19k_row.parseHeaderLine ( app.line.toString (), label );

  String name = "";
  while ( app.isEndOfFile () == false )
  {
    hu19k_row = app.next ();

    if ( hu19k_row != null )
    {
      if ( hu19k_row.getFlags () >= 0 )
      {
        if ( hu19k_row.getF532MeanB532 () < 25 )
        {
          hu19k_row.setF532MeanB532 ( 25 );
          hu19k_row.setRatioOfMeans ( hu19k_row.getF635MeanB635 () / 25.0f );
        }  // if

        name = hu19k_row.getName ();
       
        if ( ( name.length () > 0 ) && 
             ( name.startsWith ( "\"Arabidopsis" ) == false ) && 
             ( name.startsWith ( "\"Buffer" ) == false ) && 
             ( name.startsWith ( "\"HIV Replicate" ) == false ) && 
             ( name.startsWith ( "\"C.mir-" ) == false ) && 
             ( name.startsWith ( "\"C.miR-" ) == false ) && 
             ( name.startsWith ( "\"\"" ) == false ) && 
             ( name.startsWith ( "\"mir-" ) == false ) )

          System.out.println ( hu19k_row.toString () );
      }  // if
    }  // if
  }  // while
  app.closeFile ();

}  // method main


/******************************************************************************/

}  // class Hu19kIterator

