
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

public class Affy500 extends Object
{


/******************************************************************************/

  private   String  affy_id = "";		// Affymetrix SNP identifier

  private   String  chromosome = "";		// Chromosome

  private   String  gene_ids = "";		// NCBI gene identifiers

  private   String  gene_names = "";		// SNP gene names

  private   String  gene_symbols = "";		// SNP gene symbols

  private   int  position = 0;			// SNP chromosome position

  private   String  rs_id = "";			// HapMap SNP identifier


/******************************************************************************/
  // Constructor Affy500
  public Affy500 ()
  {
    initialize ();
  }  // constructor Affy500


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    affy_id = "";
    chromosome = "";
    gene_ids = "";
    gene_names = "";
    gene_symbols = "";
    position = 0;
    rs_id = "";
  }  // method initialize 


/******************************************************************************/
  public String getAffyId ()
  {
    return affy_id;
  }  // method getAffyId


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public String getGeneIds ()
  {
    return gene_ids;
  }  // method getGeneIds


/******************************************************************************/
  public String getGeneNames ()
  {
    return gene_names;
  }  // method getGeneNames


/******************************************************************************/
  public String getGeneSymbols ()
  {
    return gene_symbols;
  }  // method getGeneSymbols


/******************************************************************************/
  public int getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public String getRsId ()
  {
    return rs_id;
  }  // method getRsId


/******************************************************************************/
  public void setAffyId ( String value )
  {
    affy_id = value;
  }  // method setAffyId


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setGeneIds ( String value )
  {
    gene_ids = value;
  }  // method setGeneIds


/******************************************************************************/
  public void setGeneNames ( String value )
  {
    gene_names = value;
  }  // method setGeneNames


/******************************************************************************/
  public void setGeneSymbols ( String value )
  {
    gene_symbols = value;
  }  // method setGeneSymbols


/******************************************************************************/
  public void setPosition ( int value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setRsId ( String value )
  {
    rs_id = value;
  }  // method setRsId


/******************************************************************************/
  public void parse ( String value )
  {
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setAffyId ( tokens.nextToken () );
      setRsId ( tokens.nextToken () );
      setChromosome ( tokens.nextToken () );
      setPosition ( LineTools.getInteger ( tokens.nextToken () ) );
      setGeneSymbols ( tokens.nextToken () );
      setGeneNames ( tokens.nextToken () );
      setGeneIds ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      // System.out.println ( "Affy500.parse: " + e );
    }  // catch
  }  // parse


/******************************************************************************/
  public String toString ()
  {
    return  affy_id + "\t"
        + rs_id + "\t"
        + chromosome + "\t"
        + position + "\t"
        + gene_symbols + "\t"
        + gene_names + "\t"
        + gene_ids;
  }  // method toString


/******************************************************************************/

}  // class Affy500
