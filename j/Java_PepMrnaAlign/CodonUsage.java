
// import SeqTools;

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

public class CodonUsage extends Object
{


/******************************************************************************/

 private static int MAX_CODONS = 65;		// Maximum number of codons + 1

 public static String [] Phe = { "TTT", "TTC" };				// Phe
 public static String [] Ser = { "TCT", "TCC", "TCA", "TCG", "AGT", "AGC" };	// Ser
 public static String [] Tyr = { "TAT", "TAC" };				// Tyr
 public static String [] Ter = { "TAA", "TAG", "TGA" };				// Ter
 public static String [] Cys = { "TGT", "TGC" };				// Cys
 public static String [] Trp = { "TGG" };					// Trp

 public static String [] Leu = { "TTA", "TTG", "CTT", "CTC", "CTA", "CTG" };	// Leu
 public static String [] Pro = { "CCT", "CCC", "CCA", "CCG" };			// Pro
 public static String [] His = { "CAT", "CAC" };				// His
 public static String [] Gln = { "CAA", "CAG" };				// Gln
 public static String [] Arg = { "CGT", "CGC", "CGA", "CGG", "AGA", "AGG" };	// Arg

 public static String [] Ile = { "ATT", "ATC", "ATA" };				// Ile
 public static String [] Met = { "ATG" };					// Met
 public static String [] Thr = { "ACT", "ACC", "ACA", "ACG" };			// Thr
 public static String [] Asn = { "AAT", "AAC" };				// Asn
 public static String [] Lys = { "AAA", "AAG" };				// Lys

 public static String [] Val = { "GTT", "GTC", "GTA", "GTG" };			// Val
 public static String [] Ala = { "GCT", "GCC", "GCA", "GCG" };			// Ala
 public static String [] Asp = { "GAT", "GAC" };				// Asp
 public static String [] Glu = { "GAA", "GAG" };				// Glu
 public static String [] Gly = { "GGT", "GGC", "GGA", "GGG" };			// Gly


/******************************************************************************/

  private   int total_codons = 0;			// Number of codons evaluated

  private   int [] codons_used = new int [ MAX_CODONS ];	// Counts by codon of usage

  private   float [] frequencies = new float [ MAX_CODONS ];	// Frequencies of usage


/******************************************************************************/
  // Constructor CodonUsage
  public CodonUsage ()
  {
    initialize ();
  }  // constructor CodonUsage


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    total_codons = 0;

    for ( int i = 0; i < MAX_CODONS; i++ )
    {
      codons_used [ i ] = 0;
      frequencies [ i ] = 0.0f;
    }  // for
  }  // method initialize 


/******************************************************************************/
  public int getTotalCodons ()
  {
    return total_codons;
  }  // method getTotalCodons


/******************************************************************************/
  public int [] getCodonsUsed ()
  {
    return codons_used;
  }  // method getCodonUsed


/******************************************************************************/
  public float [] getFrequencies ()
  {
    return frequencies;
  }  // method getFrequencies


/******************************************************************************/
  public void addCodon ( int index )
  {
    if ( ( index < 0 ) || ( index > 64 ) )
    {
      System.out.println ( "*Warning* codon index (" + index + ") out of range!" );
      return;
    }  // if

    codons_used [ index ]++;
  }  // method addCodon


/******************************************************************************/
  private void computeAminoFrequencies ( String [] codon_set )
  {
    // Compute the sum for this set.
    int sum = 0;
    for ( int i = 0; i < codon_set.length; i++ )
    {
      int codon_index = SeqTools.getCodonIndex ( codon_set [ i ] );

      // Check for a valid codon index.
      if ( ( codon_index >= 0 ) && ( codon_index < 64 ) )

        sum += codons_used [ codon_index ];
    }  // for

    // Check if no codons for this amino acid.
    if ( sum == 0 )  return;

    // Compute the frequency for each codon.
    for ( int i = 0; i < codon_set.length; i++ )
    {
      int codon_index = SeqTools.getCodonIndex ( codon_set [ i ] );

      // Check for a valid codon index.
      if ( ( codon_index >= 0 ) && ( codon_index < 64 ) )

        frequencies [ codon_index ] = ( (float) codons_used [ codon_index ] ) / ( (float) sum );
    }  // for

  }  // method computeAminoFrequencies


/******************************************************************************/
  public void computeFrequencies ()
  {
    computeAminoFrequencies ( Ala );
    computeAminoFrequencies ( Arg );
    computeAminoFrequencies ( Asn );
    computeAminoFrequencies ( Asp );
    computeAminoFrequencies ( Cys );
    computeAminoFrequencies ( Gln );
    computeAminoFrequencies ( Glu );
    computeAminoFrequencies ( Gly );
    computeAminoFrequencies ( His );
    computeAminoFrequencies ( Ile );
    computeAminoFrequencies ( Leu );
    computeAminoFrequencies ( Lys );
    computeAminoFrequencies ( Phe );
    computeAminoFrequencies ( Pro );
    computeAminoFrequencies ( Ser );
    computeAminoFrequencies ( Thr );
    computeAminoFrequencies ( Tyr );
    computeAminoFrequencies ( Val );
  }  // method computeFrequencies


/******************************************************************************/
  public void print ()
  {
    System.out.println ();
    System.out.println ( "Codon usage:" );
    for ( int i = 0; i < 64; i++ )
      System.out.println ( SeqTools.triplets [ i ] + " " + codons_used [ i ] );

    System.out.println ( "others " + codons_used [ 64 ] );
  }  // method print


/******************************************************************************/
  public void print ( String file_name )
  {
    System.out.print ( file_name );
    for ( int i = 0; i < 64; i++ )
      System.out.print ( "\t" + codons_used [ i ] );

    System.out.println ();
  }  // method print


/******************************************************************************/
  public void printFrequencies ( String file_name )
  {
    System.out.print ( file_name );
    for ( int i = 0; i < 64; i++ )
      System.out.print ( "\t" + frequencies [ i ] );

    System.out.println ();
  }  // method printFrequencies


/******************************************************************************/
  public static void printHeader ()
  {
    for ( int i = 0; i < 64; i++ )
      System.out.print ( "\t" + SeqTools.triplets [ i ] );

    System.out.println ();
  }  // method printHeader


/******************************************************************************/

}  // class CodonUsage
