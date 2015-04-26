
// import AminoWords;
// import FastaSequence;

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

  private final static int WORD_SIZE = 4;	// Amino acid peptide word size


/******************************************************************************/

  // Sequence alignment.
  private   StringBuffer  alignment = new StringBuffer ( 384 );

  private   int  best_count = 0;	// best matching count of common words

  private   int  best_match = 0;	// best protein match

  private   int  family = 0;		// protein family

  private   FastaSequence  fasta_sequence = null;	// FASTA sequence

  private   int  super_family = 0;	// super-family

  // hashed FASTA sequence
  private   AminoWords  amino_words = new AminoWords ();	


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
    alignment.setLength ( 0 );
    best_count = 0;
    best_match = 0;
    family = 0;
    fasta_sequence = null;
    super_family = 0;
    amino_words.initialize ();
  }  // method initialize 


/******************************************************************************/
  public StringBuffer getAlignment ()
  {
    return alignment;
  }  // method getAlignment


/******************************************************************************/
  public int getBestCount ()
  {
    return best_count;
  }  // method getBestCount


/******************************************************************************/
  public int getBestMatch ()
  {
    return best_match;
  }  // method getBestMatch


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
  public int getPercentWords ()
  {
    if ( amino_words.getTotal () > 0 )

      return ( best_count * 100 ) / amino_words.getTotal ();

    return 0;
  }  // method getPercentWords


/******************************************************************************/
  public int getSuperFamily ()
  {
    return super_family;
  }  // method getSuperFamily


/******************************************************************************/
  public AminoWords getAminoWords ()
  {
    return amino_words;
  }  // method getAminoWords


/******************************************************************************/
  public void setBestCount ( int value )
  {
    best_count = value;
  }  // method setBestCount


/******************************************************************************/
  public void setBestMatch ( int value )
  {
    best_match = value;
  }  // method setBestMatch


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
    // Assert: valid sequence
    if ( fasta_sequence.getLength () <= 0 )  return;

    // Create the hash words.
    amino_words.createWords ( fasta_sequence.getSequence () );
  }  // method computeWords


/******************************************************************************/
  public String toString ()
  {
    return fasta_sequence.getName () 
        + "\tf_" 
        + family 
        + "\ts_" 
        + super_family 
        + "\t"
        + best_count
        + "\t" 
        + getPercentWords ()
        + "%\t" 
        + (best_match+1)
        + "\tlen "
        + fasta_sequence.getLength ()
        + "\ttotal " 
        + amino_words.getTotal ()
        ;
  }  // method toString


/******************************************************************************/

}  // class Best
