

// import AminoWord;
// import AminoWords;
// import Best;
// import Families;

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

public class AlignFamily extends Object
{

/******************************************************************************/

private  StringBuffer [] alignments = null;	// protein MSA

// Protein families information.
private  Families families = null;

private  WordEdges  word_edges = new WordEdges ();	// Alignment graph edges

private  WordNodes word_nodes = new WordNodes ();	// Alignment graph nodes


/******************************************************************************/

private  int  end_index = -1;		// "3' end" word node index

private  int  start_index = -1;		// "5' end" word node index


/******************************************************************************/
public AlignFamily ()
{
  initialize ();


  WordNode end_node = new WordNode ( "3' end" );
  WordNode start_node = new WordNode ( "5' end" );
  start_index = word_nodes.addNode ( start_node );
  end_index = word_nodes.addNode ( end_node );
}  // constructor AlignFamily


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void close ()
{
}  // method close


/*******************************************************************************/
public void setFamilies ( Families value )
{
  families = value;

  // Allocate the alignments.
  int total = families.getTotal ();
  alignments = new StringBuffer [ total ];
  for ( int i = 0; i < total; i++ )
    alignments [ i ] = new StringBuffer ( 384 );
}  // method setFamilies


/******************************************************************************/
public int [] sortCounts ( int family, int family_total )
{
  int [] order = new int [ family_total ];

  Best [] best = families.getBest ();
  int total = families.getTotal ();

  // Initialize the order.
  int next = 0;
  for ( int i = 0; i < total; i++ )

    if ( best [ i ].getFamily () == family )
    {
      order [ next ] = i;
      next++;
    }  // if

/* Don't sort the alignment order. 
  // Sort order.
  int temp = 0;
  for ( int i = 1; i < total; i++ )

    for ( int j = 0; j < i; j++ )

      if ( best [ order [ i ] ].getBestCount () > 
           best [ order [ j ] ].getBestCount () )
      {
        // Swap order positions.
        temp = order [ i ];
        order [ i ] = order [ j ];
        order [ j ] = temp;
      }  // if
*/

  System.out.println ( "Sorted order to align:" );
  for ( int i = 0; i < total; i++ )

    System.out.println ( (i+1) + "\t" + (order [ i ]+1) ); 
  return order;
}  // method sortCounts


/******************************************************************************/
public void alignEachFamily ()
{
  // Assert: families set.
  if ( families == null )  return;

  System.out.println ( "alignEachFamily called" );

  int family_count = families.getFamilies ();

  for ( int i = 1; i <= family_count; i++ )
  {
    int family_total = families.countFamilyMembers ( i );

    System.out.println ( "family " +i + " members " + family_total );

    int order [] = sortCounts ( i, family_total );

    buildGraph ( order, family_total );
  }  // for
}  // method alignEachFamily


/******************************************************************************/
  public void addWordsToGraph ( AminoWord [] sorted_words )
  {
    WordNode left = word_nodes.getWordNode ( start_index );
    int left_index = start_index;

    WordNode right = null;
    int right_index = -1;

    WordEdge edge = null;

    // Check for first alignment.
    if ( word_nodes.getTotal () <= 2 )
    {
      for ( int i = 0; i < sorted_words.length; i++ )
      {
        right = new WordNode ( sorted_words [ i ].getAminos () );
        right_index = word_nodes.addNode ( right );

        // Link the common word to the WordNode
        sorted_words [ i ].setWordNode ( right_index );

        edge = new WordEdge ( left_index, right_index );
        word_edges.addEdge ( edge );

        left = right;
        left_index = right_index;
      }  // for

      edge = new WordEdge ( left_index, end_index );
      word_edges.addEdge ( edge );
      
      return;
    }  // if

    // Add the new common words.
    left.incrementCount ();
    for ( int i = 0; i < sorted_words.length; i++ )
    {
      // Search the graph for an existing edge.
      edge = word_edges.findEdge ( left_index, sorted_words [ i ].getAminos (), word_nodes );
      if ( edge != null )
      {
        right_index = edge.getNextNode ();
        if ( right_index >= 0 )  right = word_nodes.getWordNode ( right_index );
      }  // if
      else
      {
        right_index = sorted_words [ i ].getWordNode ();
        if ( right_index >= 0 )  right = word_nodes.getWordNode ( right_index );
      }  // else

      // Create a new right node if a match is not found.
      if ( right == null )
      {
        right = new WordNode ( sorted_words [ i ].getAminos () );
        right_index = word_nodes.addNode ( right );
      }  // if

      // Try to find the matching edge.
      if ( edge == null )
        edge = word_edges.findEdge ( left_index, right_index );

      // Create a new edge if one doesn't exist.
      if ( edge == null )
      {
        edge = new WordEdge ( left_index, right_index );
        word_edges.addEdge ( edge );
      }  // if

      edge.incrementCount ();
      right.incrementCount ();

      left = right;
      left_index = right_index;
      right = null;
      right_index = -1;
    }  // for

    edge = word_edges.findEdge ( left_index, end_index );
    if ( edge == null )
    {
      edge = new WordEdge ( left_index, end_index );
      word_edges.addEdge ( edge );
    }  // if

    edge.incrementCount ();
    right = word_nodes.getWordNode ( end_index );
    right.incrementCount ();
  }  // method addWordsToGraph


/******************************************************************************/
  public void buildGraph ( int [] order, int family_total )
  {
    // Align each of the sequences in order.
    Best [] best = families.getBest ();
    AminoWords words1 = best [ order [ 0 ] ].getAminoWords ();
    AminoWords words2 = null;

    for ( int i = 1; i < family_total; i++ )
    {
      words1 = best [ best [ order [ i ] ].getBestMatch () ].getAminoWords ();
      words1.clearCommonWords ();

      words2 = best [ order [ i ] ].getAminoWords ();
      words2.clearCommonWords ();

      // Match the common words between the two sequences.
      words1.matchCommonWords ( words2.getWords () );
      AminoWord [] sorted_words = words1.getSortedCommonWords ();

      // Add the common words to the graph.
      addWordsToGraph ( words1.getSortedCommonWords () );
    }  // for

    word_nodes.summarize ();

    word_edges.summarize ();
  }  // method buildGraph


/******************************************************************************/

}  // class AlignFamily

