
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

public class Gene extends Object
{


/******************************************************************************/

  private   String  chromosome = "";		// Chromosome

  private   int  chromosome_start = 0;		// gene start position on chromosome

  private   int  chromosome_end = 0;		// gene end position on chromosome

  private   String  description = "";		// gene description

  private   int  gene_id = 0;			// NCBI Gene identifier

  private   String  symbol = "";		// Gene symbol


/******************************************************************************/
  // Constructor Gene
  public Gene ()
  {
    initialize ();
  }  // constructor Gene


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    chromosome = "";
    chromosome_start = 0;
    chromosome_end = 0;
    description = "";
    gene_id = 0;
    symbol = "";
  }  // method initialize 


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public int getChromosomeStart ()
  {
    return chromosome_start;
  }  // method getChromosomeStart


/******************************************************************************/
  public int getChromosomeEnd ()
  {
    return chromosome_end;
  }  // method getChromosomeEnd


/******************************************************************************/
  public String getDescription ()
  {
    if ( description != null )
      if ( description.length () > 160 )  return description.substring ( 0, 160 );

    return description;
  }  // method getDescription


/******************************************************************************/
  public int getGeneId ()
  {
    return gene_id;
  }  // method getGeneId


/******************************************************************************/
  public String getSymbol ()
  {
    return symbol;
  }  // method getSymbol


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setChromosomeStart ( int value )
  {
    chromosome_start = value;
  }  // method setChromosomeStart


/******************************************************************************/
  public void setChromosomeEnd ( int value )
  {
    chromosome_end = value;
  }  // method setChromosomeEnd


/******************************************************************************/
  public void setDescription ( String value )
  {
    description = value;
  }  // method setDescription


/******************************************************************************/
  public void setGeneId ( int value )
  {
    gene_id = value;
  }  // method setGeneId


/******************************************************************************/
  public void setSymbol ( String value )
  {
    symbol = value;
  }  // method setSymbol


/******************************************************************************/
  public void parse ( String value )
  {
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setSymbol ( tokens.nextToken () );
      setChromosomeStart ( LineTools.getInteger ( tokens.nextToken () ) );
      setChromosomeEnd ( LineTools.getInteger ( tokens.nextToken () ) );
      setChromosome ( tokens.nextToken () );
      setGeneId ( LineTools.getInteger ( tokens.nextToken () ) );
      setDescription ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Gene.parse: " + e );
    }  // catch

  }  // method parse


/******************************************************************************/
  public String toString ()
  {
    return symbol + "\t" 
        + chromosome_start + "\t"
        + chromosome_end + "\t"
        + chromosome + "\t"
        + gene_id + "\t"
        + description;
  }  // method to_String


/******************************************************************************/

}  // class Gene
