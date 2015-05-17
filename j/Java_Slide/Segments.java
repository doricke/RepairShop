
import java.util.*;

// import Segment;

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

public class Segments extends Object
{


/******************************************************************************/

  private   int  common = 0;			// number of common segments

  private   int  total = 0;			// number of current segments

  private   Segment [] segments = null;		// array of segments


/******************************************************************************/
  // Constructor Segments
  public Segments ()
  {
    initialize ();
  }  // constructor Segments


/******************************************************************************/
  // Initialize the class variables.
  public void initialize ()
  {
    common = 0;
    total = 0;
    segments = null;
  }  // method initialize 


/******************************************************************************/
  public void addSegments ( Vector amino_blocks )
  {
    if ( amino_blocks == null )  return;
    if ( amino_blocks.size () <= 0 )  return;

    segments = new Segment [ amino_blocks.size () ];
    for ( int i = 0; i < amino_blocks.size (); i++ )
      segments [ i ] = (Segment) amino_blocks.elementAt ( i );

    total = amino_blocks.size ();
  }  // method addSegments


/******************************************************************************/
  public int getCommonSegmentsCount ()
  {
    return common;
  }  // method getCommonSegmentsCount


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public Segment getSegment ( int index )
  {
    if ( ( index < 0 ) || ( index >= total ) || ( index >= segments.length ) )  return null;

    return segments [ index ];
  }  // method getSegment


/******************************************************************************/
  public Segment [] getSegments ()
  {
    return segments;
  }  // method getSegments


/******************************************************************************/
  public void setSize ( int value )
  {
    segments = new Segment [ value ];
  }  // method setSize


/******************************************************************************/
  public void setSegments ( Segment [] value )
  {
    segments = value;
    total = value.length;
  }  // method setSegments


/******************************************************************************/
  public void zeroTotal ()
  {
    total = 0;
  }  // method zeroTotal


/******************************************************************************/
  public void clearCommonSegments ()
  {
    if ( segments == null )  return;

    for ( int i = 0; i < segments.length; i++ )

      if ( segments [ i ] != null )
      {
        segments [ i ].setCount ( segments [ i ].getIdentities () );
        segments [ i ].setPrevious ( -1 );
      }  // if
  }  // method clearCommonSegments


/******************************************************************************/
  private void clearCounts ( Segment [] sorted )
  {
    if ( sorted == null )  return;

    for ( int i = 0; i < sorted.length; i++ )
      if ( sorted [ i ] != null )
        sorted [ i ].setCount ( sorted [ i ].getIdentities () );
  }  // method clearCounts


/******************************************************************************/
  public void computeAlignmentCounts ()
  {
    int best_j = -1;
    int best_count = 0;

    for ( int i = 1; i < total; i++ )
    {
      int j = i - 1;
      best_j = -1;
      best_count = 0;

      while ( j >= 0 )
      {
        if ( ( segments [ i ].getStart2 () > segments [ j ].getStart2 () + segments [ j ].getLength () ) &&
             ( segments [ i ].getStart1 () > segments [ j ].getStart1 () + segments [ j ].getLength () ) )
        {
          if ( segments [ j ].getCount () > best_count )
          {
            best_j = j;
            best_count = segments [ j ].getCount ();
          }  // if
        }  // if

        j--;
      }  // while

      if ( best_j >= 0 )
      {
        segments [ i ].addCount ( best_count );
        segments [ i ].setPrevious ( best_j );
      }  // if
    }  // for
  }  // method computeAlignmentCounts


/******************************************************************************/
  private void insert ( Segment [] sorted, Segment segment, int sorted_total )
  {
    // Check for the first record.
    if ( sorted_total == 0 )
    {
      sorted [ 0 ] = segment;
      return;
    }  // if

    // Find the insertion position.
    int i = sorted_total;
    while ( ( i > 0 ) &&
            ( segment.getStart1 () < sorted [ i - 1 ].getStart1 () ) )
    {
      sorted [ i ] = sorted [ i - 1 ];		// shift down the array
      i--;					// new insertion position
    }  // while

    // insert.
    sorted [ i ] = segment;
  }  // method insert


/******************************************************************************/
  private int countChainLength ( Segment [] sorted, int index )
  {
    int count = 0;

    int next_index = index;
    while ( next_index >= 0 )
    {
      count++;
      next_index = sorted [ next_index ].getPrevious ();
    }  // while

    return count;
  }  // method countChainLength


/******************************************************************************/
  public void selectAlignment ()
  {

// System.out.println ( "selectAlignment: segments array" );
// snap();

    // Check for no alignment.
    if ( total <= 0 )  return;

    int best_count = 0;
    int best_index = 0;

    // Find the best count.
    for ( int i = 0; i < total; i++ )

      if ( segments [ i ].getCount () > best_count )
      {
        best_count = segments [ i ].getCount ();
        best_index = i;
      }  // if

    // Allocate the alignment array.
    int new_count = countChainLength ( segments, best_index );
    Segment [] alignment = new Segment [ new_count ];

    // Copy the alignment.
    int next_index = best_index;
    int i = new_count - 1;
    while ( ( next_index >= 0 ) && ( i >= 0 ) )
    {
      alignment [ i ] = segments [ next_index ];
      next_index = segments [ next_index ].getPrevious ();
      // alignment [ i ].setCount ( i );
      alignment [ i ].setPrevious ( i - 1 );
      i--;
    }  // while

    // System.out.println ( "selectAlignment:" );
    // snap ( alignment );

    if ( i > 0 )
      System.out.println ( "*Warning* improper previous chain!" );

    segments = alignment;
    total = new_count;
  }  // method selectAlignment


/******************************************************************************/
  public void snap ()
  {
    snap ( segments );
  }  // method snap


/******************************************************************************/
  public static void snap ( Segment [] segments )
  {
    System.out.println ();

    System.out.println ( "length = " + segments.length );
    for ( int i = 0; i < segments.length; i++ )

      if ( segments [ i ] != null )

        System.out.println ( (i+1) + "\t" + segments [ i ].toString () );
  }  // method snap


/******************************************************************************/

}  // class Segments
