

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

public class SlidingWindow extends Object
{


/******************************************************************************/

  private   float cluster = 0.7f;	// fraction of positive probes 

  private   String [] data;		// current data lines

  private   FloatArray [] probes;	// current probes

  private   int size = 0;		// maximum number of positions

  private   int span = 0;		// window span in base pairs

  private   float threshold = 1.35f;	// threshold for positive value

  private   String [] titles = new String [ 20 ];	// probe titles

  private   int total = 0;		// number of current positions


/******************************************************************************/
  // Constructor SlidingWindow
  public SlidingWindow ()
  {
    initialize ();
  }  // constructor SlidingWindow


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    cluster = 0.6f;
    size = 0;
    span = 0;
    threshold = 1.3f;
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public float getCluster ()
  {
    return cluster;
  }  // method getCluster


/******************************************************************************/
  public FloatArray [] getProbes ()
  {
    return probes;
  }  // method getProbes


/******************************************************************************/
  public int getSize ()
  {
    return size;
  }  // method getSize


/******************************************************************************/
  public int getSpan ()
  {
    return span;
  }  // method getSpan


/******************************************************************************/
  public float getThreshold ()
  {
    return threshold;
  }  // method getThreshold


/******************************************************************************/
  public String [] getTitles ()
  {
    return titles;
  }  // method getTitles


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public void setCluster ( float value )
  {
    cluster = value;
  }  // method setCluster


/******************************************************************************/
  public void setProbe ( int index, FloatArray value1, String value2 )
  {
    // Assert: valid titles.
    if ( probes == null )
    {
      System.out.println ( "*Warning* SlidingWindow.setProbe: probes null." );
      return;
    }  // if

    // Assert: valid index.
    if ( ( index < 0 ) || ( index >= total ) || ( index >= size ) )
    {
      System.out.println ( "*Warning* SlidingWindow.setProbe: invalid index: " + index );
      return;
    }  // if

    probes [ index ] = value1;
    data [ index ] = value2;
  }  // method setProbe


/******************************************************************************/
  public void setProbes ( FloatArray [] value )
  {
    probes = value;
  }  // method setProbes


/******************************************************************************/
  public void setSize ( int value )
  {
    size = value;

    probes = new FloatArray [ value ];
    data = new String [ value ];
  }  // method setSize


/******************************************************************************/
  public void setSpan ( int value )
  {
    span = value;
  }  // method setSpan


/******************************************************************************/
  public void setThreshold ( float value )
  {
    threshold = value;
  }  // method setThreshold


/******************************************************************************/
  public void setTitle ( int index, String value )
  {
    // Assert: valid titles.
    if ( titles == null )
    {
      System.out.println ( "*Warning* SlidingWindow.setTitle: titles null." );
      return;
    }  // if

    // Assert: valid index.
    if ( ( index < 0 ) || ( index >= size ) )
    {
      System.out.println ( "*Warning* SlidingWindow.setTitle: invalid index: " + index );
      return;
    }  // if

    titles [ index ] = value;
  }  // method setTitle


/******************************************************************************/
  public void setTitles ( String [] value )
  {
    titles = value;
  }  // method setTitles


/******************************************************************************/
  public void setTotal ( int value )
  {
    total = value;
  }  // method setTotal


/******************************************************************************/
  private int dropFrom ( int new_pos )
  {
    int last = -1;
    for ( int i = 0; i < total; i++ )
    {
      if ( ((int) probes [ i ].getValue ( 0 )) + span < new_pos )
        last = i;
      else  return last;
    }  // for

    return last;
  }  // method dropFrom


/******************************************************************************/
  private void dropTooFar ( int new_pos )
  {
    int delta = dropFrom ( new_pos );
    if ( delta == -1 )  return;

    for ( int i = delta + 1; i < total; i++ )
    {
      probes [ i - delta ] = probes [ i ];
      data [ i - delta ] = data [ i ];
    }  // for

    total -= (delta + 1);
  }  // method dropTooFar


/******************************************************************************/
  public void addProbe ( FloatArray float_array, String line )
  {
    int new_pos = (int) float_array.getValue ( 0 );
    dropTooFar ( new_pos );

    if ( total < size )
    {
      probes [ total ] = float_array;
      data [ total ] = line;
      total++;
    }  // if
    else
    {
      System.out.println ( "*Warning* too not enough room for more probes." );
      return;
    }  // else

    checkWindow ();
  }  // method addProbe


/******************************************************************************/
  public void checkWindow ()
  {
    // Assert: probes to check
    if ( ( total <= 0 ) || ( probes == null ) )
    {
      System.out.println ( "*Warning* checkWindow: no probes; total = " + total );
      return;
    }  // if

    int experiments = probes [ 0 ].getSize ();
    for ( int exp_index = 2; exp_index <= experiments - 4; exp_index++ )
    {
      // Count the oligo probles above threshold for each experiment.
      float count = 0.0f;
      for ( int i = 0; i < total; i++ )

        if ( ( probes [ i ].getValue ( exp_index ) / 
               probes [ i ].getValue ( 1 ) /* average */ ) >= threshold )

          count += 1.0f;

      if ( ( count / ( total * 1.0f) >= cluster ) && ( count >= 3.9f ) )
      {
        snapWindow ( exp_index );
        return;
      }  // if
    }  // for
  }  // method checkWindow


/******************************************************************************/
public void snapWindow ( int exp_index )
{
  // Write out the data lines.
  for ( int i = 0; i < total; i++ )

    System.out.println ( data [ i ] + "\t" + titles [ exp_index - 2 ] );
}  // method snapWindow


/******************************************************************************/

}  // class SlidingWindow
