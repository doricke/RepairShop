
// import WordEdge;
// import WordNodes;


/******************************************************************************/

public class WordEdges extends Object
{


/******************************************************************************/

  // Maximum number of WordEdge records.
  private  static  final  int  MAX_WORD_EDGES = 20000;


/******************************************************************************/

  private   int  total = 0;			// total number of edges

  // All word edges
  private   WordEdge [] word_edges = new WordEdge [ MAX_WORD_EDGES ];	


/******************************************************************************/
  // Constructor WordEdges
  public WordEdges ()
  {
    initialize ();
  }  // constructor WordEdges


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public WordEdge [] getWordEdges ()
  {
    return word_edges;
  }  // method get[]


/******************************************************************************/
  public WordEdge getWordEdges ( int index )
  {
    // Assert: valid index.
    if ( ( index < 0 ) || ( index >= total ) )  return null;

    return word_edges [ index ];
  }  // method get[]


/******************************************************************************/
  public void addEdge ( WordEdge word_edge )
  {
    if ( total + 1 >= MAX_WORD_EDGES )
    {
      System.out.println ( "*Warning* not enough WordEdges allocated." );
      return;
    }  // if

    word_edges [ total ] = word_edge;
    total++;
  }  // method addEdge


/******************************************************************************/
  public WordEdge findEdge ( int left, int right )
  {
    // Validate left & right.
    if ( ( left < 0 ) || ( right < 0 ) )
    {
      int zero = 0;
      int three = 3 / zero;
    }  // if

    for ( int i = 0; i < total; i++ )

      if ( ( word_edges [ i ].getPreviousNode () == left ) &&
           ( word_edges [ i ].getNextNode () == right ) )

        return word_edges [ i ]; 

    return null;
  }  // method findEdge


/******************************************************************************/
  public WordEdge findEdge 
      ( int left_index
      , String aminos
      , WordNodes word_nodes 
      )
  {
    int next_node = -1;

    for ( int i = 0; i < total; i++ )

      if ( word_edges [ i ].getPreviousNode () == left_index )
      {
        next_node = word_edges [ i ].getNextNode ();

        if ( next_node >= 0 )
        {
          if ( word_nodes.checkAminos ( next_node, aminos ) == true )

            return word_edges [ i ];

          else

            System.out.println ( "Alt. edge: " + aminos + " " 
                + word_nodes.toString ( next_node ) );
        }  // if
      } // if

    return null;
  }  // method findEdge


/******************************************************************************/
  public void summarize ()
  {
    System.out.println ();
    System.out.println ( "WordEdges:" );

    for ( int i = 0; i < total; i++ )

      System.out.println ( i + "\t" + word_edges [ i ].toString2 () );
  }  // method summarize


/******************************************************************************/

}  // class WordEdges
