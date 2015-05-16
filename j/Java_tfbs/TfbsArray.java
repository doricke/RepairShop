

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

public class TfbsArray extends Object
{


/******************************************************************************/

  private static final int ARRAY_SIZE = 374792;


/******************************************************************************/

  // Tfbs records array.
  private   TfbsRecord []  tfbs_array = new TfbsRecord [ ARRAY_SIZE ];

  int total = 0;		// number of current records


/******************************************************************************/
  // Constructor TfbsArray
  public TfbsArray ()
  {
    initialize ();
  }  // constructor TfbsArray


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    total = 0;

    for ( int i = 0; i < tfbs_array.length; i++ )
      
      tfbs_array [ i ] = null;
  }  // method initialize 


/******************************************************************************/
  public void add ( TfbsRecord tfbs )
  {
    if ( total == 0 )
    {
      tfbs_array [ 0 ] = tfbs;
      total++;
      return;
    }  // if

    // Shift records down from the end until insertion place is found.
    int index = total;
    boolean shift = true;
    while ( shift == true )
    {
      
      if ( tfbs.getMatchX () > tfbs_array [ index - 1 ].getMatchX () ) 
        shift = false;

      if ( tfbs.getMatchX () == tfbs_array [ index - 1 ].getMatchX () )
      {
        if ( tfbs.getMatchY () > tfbs_array [ index - 1 ].getMatchY () )  
          shift = false;
      }  // if

      if ( shift == true )
      {
        tfbs_array [ index ] = tfbs_array [ index - 1 ];
        index--;
      }  // if

      if ( index == 0 )  shift = false;
    }  // while

    tfbs_array [ index ] = tfbs;
    total++;
  }  // method add


/******************************************************************************/
  public int getLength ()
  {
    return getSize ();
  }  // method getLength


/******************************************************************************/
  public int getSize ()
  {
    if ( tfbs_array == null )  return 0;

    return total;
  }  // method getSize


/******************************************************************************/
  public void setSize ( int value )
  {
    tfbs_array = new TfbsRecord [ value ];

    initialize ();
  }  // method setSize


/******************************************************************************/
  public TfbsRecord getTfbsRecord ( int index )
  {
    if ( ( index < 0 ) || ( index >= total ) )  return null;

    return tfbs_array [ index ];
  }  // method getTfbsRecord


/******************************************************************************/
  public void snap ()
  {
    for ( int i = 0; i < total; i++ )
     
      if ( tfbs_array [ i ] != null )

        System.out.println ( tfbs_array [ i ].toString () ); 
  }  // method snap


/******************************************************************************/

}  // class TfbsArray
