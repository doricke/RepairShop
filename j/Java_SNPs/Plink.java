
import java.util.*;

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

public class Plink extends Object
{


/******************************************************************************/

  private   char    allele1 = ' ';		// A1 - SNP allele 1

  private   char    allele2 = ' ';		// A2 - SNP allele 2

  private   String  chromosome = "";		// CHR - chromosome

  private   String  gene_list = "";		// GENE_LIST - Broad gene list

  private   float   maf = 0.0f;			// MAF - Minor Allele Frequency

  private   int     nmiss = 0;			// NMISS - number of non-missing genotypes

  private   float   or_beta = 0.0f;		// OR, BETA - Odds Ratio, Regression coefficient

  private   float   p_value = 0.0f;		// GC, EMP1 - P-value

  private   int     permutations = 0;		// NP - number of permutations

  private   int     position = 0;		// PHYS_POS - chromosome physical position

  private   float   r2 = 0.0f;			// R2 - Regression r-squared

  private   String  rs_id = "";			// SNP - HapMap SNP identifier

  private   float   se = 0.0f;			// SE - 

  private   float   wald = 0.0f;		// WALD, CHISQ - statistic


/******************************************************************************/
  // Constructor Plink
  public Plink ()
  {
    initialize ();
  }  // constructor Plink


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    allele1 = ' ';
    allele2 = ' ';
    chromosome = "";
    gene_list = "";
    maf = 0.0f;
    nmiss = 0;
    or_beta = 0.0f;
    p_value = 0.0f;
    permutations = 0;
    position = 0;
    r2 = 0.0f;
    rs_id = "";
    se = 0.0f;
    wald = 0.0f;
  }  // method initialize 


/******************************************************************************/
  public char getAllele1 ()
  {
    return allele1;
  }  // method getAllele1


/******************************************************************************/
  public char getAllele2 ()
  {
    return allele2;
  }  // method getAllele2


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public String getGeneList ()
  {
    return gene_list;
  }  // method getGeneList


/******************************************************************************/
  public float getMaf ()
  {
    return maf;
  }  // method getMaf


/******************************************************************************/
  public int getNmiss ()
  {
    return nmiss;
  }  // method getNmiss


/******************************************************************************/
  public float getOrBeta ()
  {
    return or_beta;
  }  // method getOrBeta


/******************************************************************************/
  public float getPValue ()
  {
    return p_value;
  }  // method getPValue


/******************************************************************************/
  public int getPermutations ()
  {
    return permutations;
  }  // method getPermutations


/******************************************************************************/
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public float getR2 ()
  {
    return r2;
  }  // method getR2


/******************************************************************************/
  public String getRsId ()
  {
    return rs_id;
  }  // method getRsId


/******************************************************************************/
  public float getSe ()
  {
    return se;
  }  // method getSe


/******************************************************************************/
  public float getWald ()
  {
    return wald;
  }  // method getWald


/******************************************************************************/
  public void setAllele1 ( String value )
  {
    if ( value.length () >= 1 )

      allele1 = value.charAt ( 0 );
  }  // method setAllele1


/******************************************************************************/
  public void setAllele2 ( String value )
  {
    if ( value.length () >= 1 )

      allele2 = value.charAt ( 0 );
  }  // method setAllele2


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setGeneList ( String value )
  {
    if ( value.equals ( "-9" ) == true )  value = "";	// Plink null list

    gene_list = value;
  }  // method setGeneList


/******************************************************************************/
  public void setMaf ( float value )
  {
    maf = value;
  }  // method setMaf


/******************************************************************************/
  public void setNmiss ( int value )
  {
    nmiss = value;
  }  // method setNmiss


/******************************************************************************/
  public void setOrBeta ( float value )
  {
    or_beta = value;
  }  // method setOrBeta


/******************************************************************************/
  public void setPValue ( float value )
  {
    p_value = value;
  }  // method setPValue


/******************************************************************************/
  public void setPermutations ( int value )
  {
    permutations = value;
  }  // method setPermutations


/******************************************************************************/
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setR2 ( float value )
  {
    r2 = value;
  }  // method setR2


/******************************************************************************/
  public void setRsId ( String value )
  {
    rs_id = value;
  }  // method setRsId


/******************************************************************************/
  public void setSe ( float value )
  {
    se = value;
  }  // method setSe


/******************************************************************************/
  public void setWald ( float value )
  {
    wald = value;
  }  // method setWald


/******************************************************************************/
  public void parse ( String value )
  {
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    // StringTokenizer tokens = new StringTokenizer ( value, "\t" );
    StringTokenizer tokens = new StringTokenizer ( value, " " );

    try
    {
      setChromosome ( tokens.nextToken () );				// CHR
      setRsId ( tokens.nextToken () );					// SNP
      setPosition ( LineTools.getInteger ( tokens.nextToken () ) );	// PHYS_POS
      setPValue ( LineTools.getFloat ( tokens.nextToken () ) );		// GC
      setWald ( LineTools.getFloat ( tokens.nextToken () ) );		// WALD 
      setAllele1 ( tokens.nextToken () );				// A1 
      setAllele2 ( tokens.nextToken () );				// A2
      setMaf ( LineTools.getFloat ( tokens.nextToken () ) );		// MAF
      setNmiss ( LineTools.getInteger ( tokens.nextToken () ) );	// NMISS
      setOrBeta ( LineTools.getFloat ( tokens.nextToken () ) );		// OR, BETA
      setSe ( LineTools.getFloat ( tokens.nextToken () ) );		// SE
      setR2 ( LineTools.getFloat ( tokens.nextToken () ) );		// GENE_LIST
      setGeneList ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "SNPs.parse: " + e );
    }  // catch
  }  // method parse


/******************************************************************************/
  public String toString ()
  {
    return chromosome + "\t" 
        + position + "\t"
        + rs_id + "\t"
        + p_value + "\t"
        + wald + "\t"
        + allele1 + "\t"
        + allele2 + "\t"
        + maf + "\t"
        + nmiss + "\t"
        + or_beta + "\t"
        + se + "\t"
        + r2 + "\t"
        + gene_list;
  }  // method toString


/******************************************************************************/

}  // class Plink
