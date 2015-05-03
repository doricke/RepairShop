
// import FastaSequence;
// import FastaIterator;

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

public class FastaHeaders extends Object
{


/******************************************************************************/

  FastaSequence  headers [] = null;		// FASTA headers

  private  int total = 0;			// total headers added

  private  int size = 0;			// maximum Fasta headers


/******************************************************************************/
  public FastaHeaders ()
  {
    initialize ();
  }  // constructor FastaHeaders


/******************************************************************************/
  private void initialize ()
  {
    total = 0;
    size = 0;
  }  // method initalize


/*******************************************************************************/
  public FastaSequence [] getHeaders ()
  {
    return headers;
  }  // method getHeaders


/*******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/*******************************************************************************/
  public void setSize ( int value )
  {
    size = value;
    headers = new FastaSequence [ size ];
  }  // method setSize


/*******************************************************************************/
  public int compareTo ( String a, String b )
  {
    int i = 0;
    int a_length = a.length ();
    int b_length = b.length ();

    // Compare the strings one character at a time.
    while ( ( i < a_length ) &&
            ( i < b_length ) )
    {
      if ( a.charAt ( i ) < b.charAt ( i ) )  return -1;	// a < b
      if ( a.charAt ( i ) > b.charAt ( i ) )  return  1;	// b > a
      i++;
    }  // while

    // Check if the strings are identical.
    if ( ( i >= a_length ) && ( i >= b_length ) )  return 0;	// a == b

    if ( i >= a_length )  return -1;		// a < b
    return 1;		// b > a
  }  // method compareTo


/*******************************************************************************/
  // Binary search.
  public int find ( String name )
  {
    int key = 0;			// comparison key
    int lower = 0;			// lower index
    int middle = 0;			// middle index
    int upper = total - 1;		// upper index

    while ( lower <= upper )
    {
      middle = (lower + upper) / 2;	// compute the index of the middle record

      key = compareTo ( name, headers [ middle ].getShortName () );

      if ( key == 0 )  return middle;

      if ( key < 0 )
        upper = middle - 1;		// look in the lower half
      else
        lower = middle + 1;		// look in the upper half
    }  // while

    System.out.println ( "FastaHeaders.find: No name found for " + name );
    System.out.println ( "key = " + key + ", lower = " + lower + ", upper = " + upper );

    printHeaders ();

    return -1;				// not found
  }  // method find


/*******************************************************************************/
  public FastaSequence getHeader ( int index )
  {
    // Assert: valid index
    if ( ( index < 0 ) || ( index >= total ) )  return null;

    return headers [ index ];
  }  // method getHeader


/*******************************************************************************/
  private void add ( FastaSequence header )
  {
    if ( header == null )  return;

    // Check for the first record.
    if ( total == 0 )
    {
      headers [ 0 ] = header;
      total++;
      return;
    }  // if

    for ( int i = total - 1; i >= 0; i-- )
    {
      if ( compareTo ( header.getShortName (), headers [ i ].getShortName () ) < 0 )

        headers [ i + 1 ] = headers [ i ];		// shift down the array

      else
      {
        headers [ i + 1 ] = header;
        total++;
        return;
      }  // else
    }  // for

    headers [ 0 ] = header;
    total++;
  }  // method add


/*******************************************************************************/
  public void loadHeaders ( String file_name )
  {
    FastaIterator headers_file = new FastaIterator ( file_name );

    for ( int i = 0; (i < size) && (headers_file.isEndOfFile () != true); i++ )
    
      add ( headers_file.next () );

    headers_file.closeFile ();
  }  // method loadHeaders


/******************************************************************************/
  public void printHeaders ()
  {
    for ( int i = 0; i < total; i++ )
    {
      System.out.println ( headers [ i ].getGeneInfo () );
    }  // for
  }  // method printHeaders


/******************************************************************************/
  public static void main ( String [] args )
  {
    FastaHeaders app = new FastaHeaders ();
    app.setSize ( 953 );
    app.loadHeaders ( "ref.headers" );
    app.printHeaders ();
  }  // method main


/******************************************************************************/

}  // class FastaHeaders

