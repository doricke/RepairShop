
// import Coordinate;
// import RefGene;
// import RefGeneIterator;

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

public class RefGenes extends Object
{


/******************************************************************************/

  private static final int MAX_GENES = 600;		// Maximum number of genes


/******************************************************************************/

  RefGene  genes [] = new RefGene [ MAX_GENES ];	// UCSC Reference genes

  private  int total_genes = 0;				// total genes added


/******************************************************************************/
  public RefGenes ()
  {
    initialize ();
  }  // constructor RefGenes


/******************************************************************************/
  private void initialize ()
  {
    total_genes = 0;
  }  // method initalize


/*******************************************************************************/
  public RefGene [] getGenes ()
  {
    return genes;
  }  // method getGenes


/*******************************************************************************/
  public int getTotal ()
  {
    return total_genes;
  }  // method getTotal


/*******************************************************************************/
  // Binary search.
  public int find ( int position )
  {
    int key = 0;			// comparison key
    int lower = 0;			// lower index
    int middle = 0;			// middle index
    int upper = total_genes - 1;	// upper index

    while ( lower <= upper )
    {
      middle = (lower + upper) / 2;	// compute the index of the middle record

      key = position - genes [ middle ].getTranscriptStart (); 

      if ( middle == total_genes - 1 )  return middle;			// At end

      // if ( key == 0 )  return middle;
      if ( ( position >= genes [ middle ].getTranscriptStart () ) &&
           ( position <  genes [ middle+1 ].getTranscriptStart () ) )  return middle;

      if ( key < 0 )
        upper = middle - 1;		// look in the lower half
      else
        lower = middle + 1;		// look in the upper half
    }  // while

    // System.out.println ( "No position found for " + position );
    // System.out.println ( "key = " + key + ", lower = " + lower + ", upper = " + upper );

    return 0;				// not found
  }  // method find


/*******************************************************************************/
  public RefGene getGene ( int index )
  {
    // Assert: valid index
    if ( ( index < 0 ) || ( index >= total_genes ) )  return null;

    return genes [ index ];
  }  // method getGene


/*******************************************************************************/
  private void add ( RefGene gene )
  {
    if ( gene == null )  return;

    // Check for the first record.
    if ( total_genes == 0 )
    {
      genes [ 0 ] = gene;
      total_genes++;
      return;
    }  // if

    for ( int i = total_genes - 1; i >= 0; i-- )
    {
      // Check for overlapping genes.
      if ( gene.getStrand () == genes [ i ].getStrand () )
      {
        if ( ( gene.getTranscriptStart () < genes [ i ].getTranscriptEnd () ) &&
             ( genes [ i ].getTranscriptStart () < gene.getTranscriptEnd () ) )
        {
          // Select the longer gene.
          if ( gene.getTranscriptEnd () - gene.getTranscriptStart () >
               genes [ i ].getTranscriptEnd () - genes [ i ].getTranscriptStart () )
          {
            // System.out.println ( "smaller: " + genes [ i ].toString () );
            // System.out.println ( "larger: " + gene.toString () );
            // System.out.println ();
            genes [ i ] = gene;
          }  // if
/*
          else
          {
            System.out.println ( "smaller: " + gene.toString () );
            System.out.println ( "larger: " + genes [ i ].toString () );
            System.out.println ();
          }  // else
*/
          return;
        }  // if
      }  // if
    }  // for

    for ( int i = total_genes - 1; i >= 0; i-- )
    {
      if ( gene.getTranscriptStart () < genes [ i ].getTranscriptStart () )

        genes [ i + 1 ] = genes [ i ];		// shift down the array

      else
      {
        genes [ i + 1 ] = gene;
        total_genes++;
        return;
      }  // else
    }  // for

    genes [ 0 ] = gene;
    total_genes++;
  }  // method add


/*******************************************************************************/
  public void loadGenes ( String file_name )
  {
    RefGeneIterator genes_file = new RefGeneIterator ( file_name );

    for ( int i = 0; (i < MAX_GENES) && (genes_file.isEndOfFile () != true); i++ )
    
      add ( genes_file.next () );

    genes_file.closeFile ();
  }  // method loadGenes


/******************************************************************************/
  public void printGenes ()
  {
    for ( int i = 0; i < total_genes; i++ )

      System.out.println ( genes [ i ].toString () );
  }  // method printGenes


/******************************************************************************/
  public static void main ( String [] args )
  {
    RefGenes app = new RefGenes ();
    app.loadGenes ( "refGene.chr22" );
    app.printGenes ();
  }  // method main


/******************************************************************************/

}  // class RefGenes

