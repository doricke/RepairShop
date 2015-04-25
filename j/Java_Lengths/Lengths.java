
// import Length;
// import LengthIterator;


/******************************************************************************/

public class Lengths extends Object
{


/******************************************************************************/

  private static final int CONTIGS = 146185;		// Number of contigs with length data

  private int loaded = 0;

  Length lengths [] = new Length [ CONTIGS ];		// Sequence Map


/******************************************************************************/
public Lengths ()
{
  initialize ();
}  // constructor Lengths


/******************************************************************************/
private void initialize ()
{
}  // method initalize


/*******************************************************************************/
  public Length [] getLengths ()
  {
    return lengths;
  }  // method getLengths


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
    int upper = loaded - 1;		// upper index

    while ( lower <= upper )
    {
      middle = (lower + upper) / 2;	// compute the index of the middle record

      key = compareTo ( name, lengths [ middle ].getName () );

      // System.out.println ();
      // System.out.println ( "lower = " + lower + ", middle = " + middle + ", upper = " + upper );
      // System.out.println ( name + " # " + key + " # " + lengths [ middle ].getName () );

      if ( key == 0 )  return middle;

      if ( key < 0 )
        upper = middle - 1;		// look in the lower half
      else
        lower = middle + 1;		// look in the upper half
    }  // while

    System.out.println ( "No length found for " + name );
    System.out.println ( "key = " + key + ", lower = " + lower + ", upper = " + upper );

    return -1;				// not found
  }  // method binarySearch


/*******************************************************************************/
  public int linearSearch ( String name )
  {
    for ( int i = 0; i < CONTIGS; i++ )

      if ( name.equals ( lengths [ i ].getName () ) == true )

        return i;

    return -1;
  }  // method linearSearch


/*******************************************************************************/
  public int getLength ( String name )
  {
    int index = find ( name );
    if ( index >= 0 )  return lengths [ index ].getLength ();

    System.out.println ( "No length found for " + name );

    return 0;
  }  // method getLength


/*******************************************************************************/
  private void insert ( Length length )
  {
    if ( length == null )  return;

    // Check for the first record.
    if ( loaded == 0 )
    {
      lengths [ 0 ] = length;
      loaded++;
      return;
    }  // if

    int i = loaded;					// default: insert at the end
    loaded++;

    String name = length.getName ();

    // System.out.println ( "Inserting: " + name + ", loaded = " + i );

    while ( ( i > 0 ) &&
            ( compareTo ( name, lengths [ i - 1 ].getName () ) < 0 ) )
    {
      lengths [ i ] = lengths [ i - 1 ];		// shift down the array
      i--;						// new insertion position
    }  // while

    lengths [ i ] = length;
  }  // method insert


/*******************************************************************************/
  public void loadLengths ( String file_name )
  {
    LengthIterator lengths_file = new LengthIterator ( file_name );

    for ( int i = 0; (i < CONTIGS) && (lengths_file.isEndOfFile () != true); i++ )

      insert ( lengths_file.next () );

    lengths_file.closeFile ();
  }  // method loadLengths


/******************************************************************************/
  public void sortLengths ()
  {
    // Bubble sort.
    int key;
    String name;
    Length temp;

    for ( int i = loaded - 1; i >= 1; i-- )
    {
      name = lengths [ i ].getName ();

      for ( int j = i - 1; j >= 0; j-- )
      {
        key = compareTo ( lengths [ j ].getName (), name );

        if ( key > 0 )
        {
          temp = lengths [ j ];
          lengths [ j ] = lengths [ i ];
          lengths [ i ] = temp;
        }  // if
      }  // for
    }  // for
  }  // method sortLengths


/******************************************************************************/
  public void printLengths ()
  {
    for ( int i = 0; i < loaded; i++ )

      System.out.println ( lengths [ i ].toString () );
  }  // method printLengths


/******************************************************************************/
  public static void main ( String [] args )
  {
    Lengths app = new Lengths ();
    app.loadLengths ( "lengths" );
    app.sortLengths ();
    app.printLengths ();
  }  // method main


/******************************************************************************/

}  // class Lengths

