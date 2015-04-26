
// import AminoWord;
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

public class Best extends Object
{


/******************************************************************************/

  private final static int MIN_LENGTH = 50;	// Minimum sequence length

  private final static int WORD_SIZE = 3;	// Amino acid peptide word size


/******************************************************************************/

  private   int  best_match = 0;	// best protein match

  private   int  common_words = 0;	// common amino acid words

  private   int  family = 0;		// protein family

  private   FastaSequence  fasta_sequence = null;	// FASTA sequence

  private   int  super_family = 0;	// super-family

  private   Words  words = new Words ();		// hashed FASTA sequence


/******************************************************************************/
  // Constructor Best
  public Best ()
  {
    initialize ();
  }  // constructor Best


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    best_match = 0;
    common_words = 0;
    family = 0;
    fasta_sequence = null;
    super_family = 0;
    words.initialize ();
  }  // method initialize 


/******************************************************************************/
  public int getBestMatch ()
  {
    return best_match;
  }  // method getBestMatch


/******************************************************************************/
  public int getCommonWords ()
  {
    return common_words;
  }  // method getCommonWords


/******************************************************************************/
  public int getFamily ()
  {
    return family;
  }  // method getFamily


/******************************************************************************/
  public FastaSequence getFastaSequence ()
  {
    return fasta_sequence;
  }  // method getFastaSequence


/******************************************************************************/
  public int [] getIntWords ()
  {
    return words.getWords ();
  }  // method getIntWords


/******************************************************************************/
  public int getPercentWords ()
  {
    if ( words.getTotal () > 0 )

      return ( common_words * 100 ) / words.getTotal ();

    return 0;
  }  // method getPercentWords


/******************************************************************************/
  public int getSuperFamily ()
  {
    return super_family;
  }  // method getSuperFamily


/******************************************************************************/
  public Words getWords ()
  {
    return words;
  }  // method getWords


/******************************************************************************/
  public void setBestMatch ( int value )
  {
    best_match = value;
  }  // method setBestMatch


/******************************************************************************/
  public void setCommonWords ( int value )
  {
    common_words = value;
  }  // method setCommonWords


/******************************************************************************/
  public void setFamily ( int value )
  {
    family = value;
  }  // method setFamily


/******************************************************************************/
  public void setFastaSequence ( FastaSequence value )
  {
    fasta_sequence = value;

    // Compute the amino acids hash words.
    computeWords ();
  }  // method setFastaSequence


/******************************************************************************/
  public void setSuperFamily ( int value )
  {
    super_family = value;
  }  // method setSuperFamily


/******************************************************************************/
  private void computeWords ()
  {
    int length = fasta_sequence.getLength ();

    if ( length < MIN_LENGTH )  return;

    // number of triplet amino acids in sequence
    words.setSize ( length - WORD_SIZE + 1 );

    // Amino acid triplet hash word.
    AminoWord amino_word = new AminoWord ();
    amino_word.setSize ( (byte) WORD_SIZE );

    // Hash the sequence.
    String sequence = fasta_sequence.getSequence ();
    String word;
    for ( int i = 0; i < length - WORD_SIZE; i++ )
    {
      word = sequence.substring ( i, i + WORD_SIZE );

      // Check for a word with no duplicate amino acids.
      if ( amino_word.isGoodWord ( word ) == true )
      {
        amino_word.setAminos ( word );
        words.addWord ( amino_word.getWord (), word );
      }  // if
    }  // for

    // Hash the last word of amino acids.
    word = sequence.substring ( length - WORD_SIZE );

    // Check for a word with no duplicate amino acids.
    if ( amino_word.isGoodWord ( word ) == true )
    {
      amino_word.setAminos ( word );
      words.addWord ( amino_word.getWord (), word );
    }  // if

/*
    System.out.println ( "Words for: " + fasta_sequence.getName () );
    String peps [] = words.getPeptides ();
    for ( int i = 0; i < peps.length; i++ )
      System.out.println ( "\tword\t" + peps [ i ] );
*/
  }  // method computeWords


/******************************************************************************/
  public String toString ()
  {
    return fasta_sequence.getName () 
        + "\t" 
        + fasta_sequence.getLength ()
        + "\t" 
        + family 
        + "\t" 
        + super_family 
        + "\t" 
        + common_words 
        + "\t" 
        + getPercentWords ()
        + "%\t" 
        + best_match;
  }  // method toString


/******************************************************************************/

}  // class Best
