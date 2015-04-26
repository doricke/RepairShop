
// import Best;
// import FastaSequence;
// import Words;

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

public class Families extends Object
{


/******************************************************************************/

  // Maximum number of protein sequences. 
  private final static int MAX_PROTEINS = 60000;


/******************************************************************************/

  // Summary of best matches between proteins
  private   Best best [] = new Best [ MAX_PROTEINS ];

  private   int  families = 0;				// number of familes

  private   int  super_families = 0;			// number of super protein familes

  private   int  total = 0;				// number of protein families


/******************************************************************************/
  // Constructor Families
  public Families ()
  {
    initialize ();
  }  // constructor Families


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    families = 0;
    super_families = 0;
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public void addNext ( Best next )
  {
    if ( total >= MAX_PROTEINS )
    {
      System.out.println ( ".addBest: *Warning* too many proteins." );
      return;
    }  // if

    best [ total ] = next;
    total++;
  }  // method addNext


/******************************************************************************/
  public Best [] getBest ()
  {
    return best;
  }  // method getBest


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public int getFamilies ()
  {
    return families;
  }  // method getFamilies


/******************************************************************************/
  public int getNewFamily ()
  {
    families++;
    return families;
  }  // method getNewFamily


/******************************************************************************/
  public int getNewSuperFamily ()
  {
    super_families++;
    return super_families;
  }  // method getNewSuperFamily


/******************************************************************************/
  public int getSuperFamilies ()
  {
    return super_families;
  }  // method getSuperFamilies


/******************************************************************************/
  public void setTotal ( int value )
  {
    total = value;
  }  // method setTotal


/******************************************************************************/
  public void addFasta ( FastaSequence fasta_sequence )
  {
    Best best = new Best ();
    best.setFastaSequence ( fasta_sequence );

    // Find the best match.
    findBest ( best );

    // Add this protein to the list.
    addNext ( best );
  }  // method addFasta


/******************************************************************************/
  public int countFamilyMembers ( int family )
  {
    int count = 0;

    // Count the members of this family.
    for ( int i = 0; i < total; i++ )

     if ( best [ i ].getFamily () == family )  count++;

    return count;
  }  // method countFamilyMembers


/******************************************************************************/
  private void findBest ( Best next )
  {
    // Check for the first protein.
    if ( total <= 0 )
    {
      families++;				// New protein family
      next.setFamily ( families );		// Set the protein family number
      super_families++;				// New protein super family
      next.setSuperFamily ( super_families );	// Set the protein super family number
      return;
    }  // if

    // Compare this protein to all other proteins.
    int best_count = 0;
    int best_match = 0;
    AminoWords amino_words = next.getAminoWords ();
    AminoWords best_words = null;
    for ( int i = 0; i < total; i++ )
    {
      best_words = best [ i ].getAminoWords ();
      // Count the number of words common to both sequences.
     
      amino_words.matchCommonWords ( best_words.getWords () );
      // sorted_words = amino_words.getSortedCommonWords ();
      int count = amino_words.getCommonWordsCount ();

      // Check for a better match.
      if ( count > best_count )
      {
        best_count = count;
        best_match = i;
      }  // if

/* This code finds the best possible match for all sequences.
      if ( count > best [ i ].getBestCount () )
      {
        best [ i ].setBestCount ( count );
        best [ i ].setBestMatch ( total );
      }  // if
*/
    }  // for

    // Record the number of common words with the best match. 
    next.setBestCount ( best_count ); 
    next.setBestMatch ( best_match );

    // Check for a family or super-family match.
    if ( best_count > 9 )
    {
      // Check for a family match.
      if ( best_count > 49 )
      {
        next.setFamily ( best [ best_match ].getFamily () );
        next.setSuperFamily ( best [ best_match ].getSuperFamily () );

/* Don't slect best; select best previous sequence.
        // Check for a better match for the previous sequence.
        if ( best_count > best [ best_match ].getBestCount () )
        {
          best [ best_match ].setBestCount ( best_count );
          best [ best_match ].setBestMatch ( total );
        }  // if
*/
      }  // if
      else  // super family member
      {
        families++;				// New protein family
        next.setFamily ( families );		// Set the protein family number

        // Set the protein super family number
        if ( best_count > 19 )
        {
          next.setSuperFamily ( best [ best_match ].getSuperFamily () );
        }  // if
        else
        {
          super_families++;				// New protein super family
          next.setSuperFamily ( super_families );	// Set the protein super family number
        }  // else
      }  // else
    }  // if
    else
    {
      families++;				// New protein family
      next.setFamily ( families );		// Set the protein family number

      super_families++;				// New protein super family
      next.setSuperFamily ( super_families );	// Set the protein super family number
    }  // else
  }  // method findBest


/******************************************************************************/
  public void summarize ()
  {
    System.out.println ();
    System.out.println ( total + " protein sequences" );
    System.out.println ( families + " protein families" );
    System.out.println ( super_families + " protein super-families" );
    System.out.println ();
    for ( int i = 0; i < total; i++ )
      System.out.println ( (i+1) + "\t" + best [ i ].toString () );
  }  // method summarize


/******************************************************************************/

}  // class Families
