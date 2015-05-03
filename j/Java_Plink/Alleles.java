

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

public class Alleles extends Object
{


/******************************************************************************/

  // Genotype allele names.
  private final static String [] allele_name = { "00", "AA", "AB", "BB", "??" };

  private final static int ALLELE_TYPES = 5;	// Number of allele types


/******************************************************************************/

  private   int []  alleles = null;		// Alleles summary

  private   float  allele1_frequency = 0.0f;	// Allele 1 frequency

  private   float  allele2_frequency = 0.0f;	// Allele 2 frequency

  private   boolean  hardy = false;		// Hardy-Weinberg equilibrium

  private   float  maf = 0.0f;			// minor allele frequency

  private   boolean  multiple = false;		// multiple alleles

  private   String  snp_name = "";		// SNP name

  private   int  total = 0;			// Total alleles


/******************************************************************************/
  // Constructor Alleles
  public Alleles ()
  {
    initialize ();
  }  // constructor Alleles


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alleles = null;
    allele1_frequency = 0.0f;
    allele2_frequency = 0.0f;
    hardy = false;
    maf = 0.0f;
    multiple = false;
    snp_name = "";
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public float getAllele1Frequency ()
  {
    return allele1_frequency;
  }  // method getAllele1Frequency


/******************************************************************************/
  public float getAllele2Frequency ()
  {
    return allele2_frequency;
  }  // method getAllele2Frequency


/******************************************************************************/
  public int [] getAlleles ()
  {
    return alleles;
  }  // method getAlleles


/******************************************************************************/
  public float getMaf ()
  {
    return maf;
  }  // method getMaf


/******************************************************************************/
  public boolean getMultiple ()
  {
    return multiple;
  }  // method getMultiple


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public boolean isHardy ()
  {
    return hardy;
  }  // method isHardy


/******************************************************************************/
  public boolean isMultiple ()
  {
    return multiple;
  }  // method isMultiple


/*******************************************************************************/
  public int getAlleles ( String geno )
  {
    total += 2;			// another 2 alleles

    return getAllelesType ( geno );
  }  // method getAlleles


/*******************************************************************************/
  public static int getAllelesType ( String geno )
  {
    if ( geno.equals ( "1 1" ) )  return 1;
    if ( geno.equals ( "1 2" ) )  return 2;
    if ( geno.equals ( "2 1" ) )  return 2;
    if ( geno.equals ( "2 2" ) )  return 3;
    if ( geno.equals ( "0 0" ) )  return 0;
    return 4;
  }  // method getAllelesType


/*******************************************************************************/
  private void computeFrequencies ()
  {
    int allele1 = 0;			// Count of allele 1
    int allele2 = 0;			// Count of allele 2

    // Total the alleles
    allele1 = alleles [ 1 ] * 2 + alleles [ 2 ];
    allele2 = alleles [ 3 ] * 2 + alleles [ 2 ];
    total = (alleles [ 1 ] + alleles [ 2 ] + alleles [ 3 ]) * 2;

    // Calculate the allele frequencies
    allele1_frequency = 0.0f;
    allele2_frequency = 0.0f;
    if ( total > 0.0f )
    {
      allele1_frequency = (allele1 * 1.0f) / (total * 1.0f);
      allele2_frequency = (allele2 * 1.0f) / (total * 1.0f);
    }  // if
    maf = allele1_frequency;
    if ( allele2_frequency < maf )  maf = allele2_frequency;

    // Check for multiple alleles
    if ( ( allele1 > 0 ) && ( allele2 > 0 ) )  multiple = true;
  }  // computeFrequencies


/*******************************************************************************/
  public void genotype ( Tokens genotype )
  {
    if ( genotype == null )  return;

    alleles = typeAlleles ( genotype );

    snp_name = genotype.getToken ( 1 );

    computeFrequencies ();
  }  // method genotype


/*******************************************************************************/
  public void genotype ( Tokens genotype, int [] pheno_class, int allele_type )
  {
    if ( genotype == null )  return;

    alleles = typeAlleles ( genotype, pheno_class, allele_type );

    snp_name = genotype.getToken ( 1 );

    computeFrequencies ();
  }  // method genotype


/******************************************************************************/
  public int[] typeAlleles ( Tokens tped )
  {
    int alleles [] = new int [ ALLELE_TYPES ];

    // Initialize genotype summary.
    for ( int i = 0; i < ALLELE_TYPES; i++ )  alleles [ i ] = 0;

    for ( int i = 4; i < tped.getTotal (); i++ )
      alleles [ getAlleles ( tped.getToken ( i ) ) ]++;

    return alleles;
  }  // method typeAlleles


/******************************************************************************/
  public int[] typeAlleles ( Tokens tped, int [] pheno_class, int allele_type )
  {
    int alleles [] = new int [ ALLELE_TYPES ];

    // Initialize genotype summary.
    for ( int i = 0; i < ALLELE_TYPES; i++ )  alleles [ i ] = 0;

    for ( int i = 4; i < tped.getTotal (); i++ )

      if ( i < pheno_class.length )
      
        // Only count alleles for this phenotype classification.
        if ( pheno_class [ i ] == allele_type )

          alleles [ getAlleles ( tped.getToken ( i ) ) ]++;

    return alleles;
  }  // method typeAlleles


/******************************************************************************/
  public String getFrequencies ()
  {
    StringBuffer info = new StringBuffer ( 20 );

    for ( int i = 1; i < ALLELE_TYPES - 1; i++ )
    {
      info.append ( alleles [ i ] );

      if ( i < ALLELE_TYPES - 2 )  info.append ( "/" );
    }  // for

    return info.toString ();
  }  // method getFrequencies


/******************************************************************************/
  public String toString ()
  {
    StringBuffer info = new StringBuffer ( 160 );
    // info.append ( snp_name + ": " );

    info.append ( getFrequencies () );

    // Calculated random expected allele frequencies
    int people = (alleles [ 1 ] + alleles [ 2 ] + alleles [ 3 ]);
    int aa = (int) (allele1_frequency * allele1_frequency * people);
    int ab = (int) (2.0f * allele1_frequency * allele2_frequency * people);
    int bb = (int) (allele2_frequency * allele2_frequency * people);
    info.append ( " " + aa + "/" + ab + "/" + bb + " " );

    // Check Observed-Expected differences
    int delta1 = alleles [ 1 ] - aa;
    if ( aa > alleles [ 1 ] )  delta1 = (aa - alleles [ 1 ]);
    int delta2 = alleles [ 3 ] - bb;
    if ( bb > alleles [ 3 ] )  delta2 = (bb - alleles [ 3 ]);
    // info.append ( " delta(" + delta1 + ", " + delta2 + ")" );
    // int delta3 = alleles [ 2 ] - ab;

    // float sigma = 0.0f;
    // if ( aa > 0 ) sigma  = (delta1 * delta1 * 1.0f) / (aa * 1.0f);
    // if ( bb > 0 ) sigma += (delta2 * delta2 * 1.0f) / (bb * 1.0f); 
    // if ( ab > 0 ) sigma += (delta3 * delta3 * 1.0f) / (ab * 1.0f);

    // info.append ( "sigma " + sigma + "\t" );
    // if ( sigma > 3.84 )  info.append ( "H-W. " );

    info.append ( " " + allele1_frequency + ", " + allele2_frequency );

    if ( delta1 + delta2 >= 50 )  info.append ( " HW" );
    if ( maf < 0.01f )  info.append ( " MAF" );
    if ( multiple == false )  info.append ( " UI" );
    return info.toString ();
  }  // method toString


/******************************************************************************/

}  // class Alleles
