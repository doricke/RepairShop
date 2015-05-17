
// import Align3;
// import CodonUsage;
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

public class PepMrna extends Object
{


/******************************************************************************/

  private   Align3 align3 = new Align3 ();	// Peptide3 Alignment

  private   CodonUsage codon_usage = new CodonUsage ();		// codon usage

  private   String [] frames = null;		// mRNA forward translation frames

  private   StringBuffer mrna = new StringBuffer ( 3000 );	// mRNA sequence

  private   StringBuffer mrna_alignment = new StringBuffer ( 3000 );	// mRNA sequence alignment

  private   StringBuffer peptide = new StringBuffer ( 1000 );	// peptide sequence (1 letter codes)

  private   StringBuffer peptide3 = new StringBuffer ( 3000 );	// peptide sequence (3 letter codes)

  // peptide sequence alignment (3 letter codes)
  private   StringBuffer peptide3_alignment = new StringBuffer ( 3000 );


/******************************************************************************/
  // Constructor PepMrna
  public PepMrna ()
  {
    initialize ();
  }  // constructor PepMrna


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    align3.initialize ();
    codon_usage.initialize ();
    frames = null;
    mrna.setLength ( 0 );
    mrna_alignment.setLength ( 0 );
    peptide.setLength ( 0 );
    peptide3.setLength ( 0 );
    peptide3_alignment.setLength ( 0 );
  }  // method initialize 


/******************************************************************************/
  public String [] getFrames ()
  {
    return frames;
  }  // method getFrames


/******************************************************************************/
  public StringBuffer getMrna ()
  {
    return mrna;
  }  // method getMrna


/******************************************************************************/
  public StringBuffer getMrnaAlignment ()
  {
    return mrna_alignment;
  }  // method getMrnaAlignment


/******************************************************************************/
  public StringBuffer getPeptide ()
  {
    return peptide;
  }  // method getPeptide


/******************************************************************************/
  public StringBuffer getPeptide3 ()
  {
    return peptide3;
  }  // method getPeptide3


/******************************************************************************/
  public StringBuffer getPeptide3Alignment ()
  {
    return peptide3_alignment;
  }  // method getPeptide3Alignment


/******************************************************************************/
  public void setMrna ( String value )
  {
    // Assert: value not null.
    if ( value == null )  return;

    mrna.setLength ( 0 );
    mrna.append ( value );

    align3.setMrna ( value );
  }  // method setMrna


/******************************************************************************/
  public void setPeptide ( String value )
  {
    peptide.setLength ( 0 );
    peptide.append ( value );

    align3.setPeptide ( value );

    peptide3.setLength ( 0 );
    peptide3.append ( align3.getPeptide3 () );
  }  // method setPeptide


/******************************************************************************/
private void tallyCodonUsage 
    ( int  pep_start
    , int  mrna_start
    , int  length
    )
{
  for ( int i = mrna_start; i < mrna_start + length; i += 3 )
  {
    // Compute which codon.
    int codon_index = 0;
    if ( i + 3 < mrna.length () )
      codon_index = SeqTools.getCodonIndex ( mrna.substring ( i, i+3 ) );
    else
      codon_index = SeqTools.getCodonIndex ( mrna.substring ( i ) );

    // Add this codon.
    codon_usage.addCodon ( codon_index );
  }  // for
}  // method tallyCodonUsage


/******************************************************************************/
public void align ( String file_name )
{
  int length = 0;
  int mrna_start = 0;
  int pep_start = 0;
  do
  {
    align3.setMrnaStart ( mrna_start );
    align3.setPeptide3Start ( pep_start );
    align3.alignNextSegment ();

    length = align3.getLength ();
    mrna_start = align3.getFrameStart ();
    pep_start = align3.getPeptide3Start ();

    if ( length > 0 )
    {
/*
      System.out.println ( "\tPeptide start = " + pep_start +
        ", mRNA start = " + mrna_start + ", length = " + length );
*/

      // Tally the codon usage.
      tallyCodonUsage ( pep_start, mrna_start, length );
    }  // if

    mrna_start += length;
    pep_start += length;
  }
  while ( ( pep_start < peptide3.length () ) && ( length > 0 ) );

  codon_usage.computeFrequencies ();
  codon_usage.printFrequencies ( file_name );
}  // method align


/******************************************************************************/

}  // class PepMrna
