
import Align3;
import SeqTools;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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
private void alignPeptide ( int mrna_start, int pep_start, int length )
{
  // Synchronize the peptide to the alignment.
  while ( ( peptide3_alignment.length () < pep_start ) || 
          ( peptide3_alignment.length () < mrna_start ) )

    peptide3_alignment.append ( "-" );

  if ( pep_start + length < peptide3.length () )
    peptide3_alignment.append ( peptide3.substring ( pep_start, pep_start + length ) );
  else
    peptide3_alignment.append ( peptide3.substring ( pep_start ) );

}  // method alignPeptide


/******************************************************************************/
private void alignSegment ( int mrna_start, int pep_start, int length )
{
  // Synchronize the mRNA to the alignment.
  if ( mrna_alignment.length () < mrna_start )
    mrna_alignment.append ( mrna.substring ( mrna_alignment.length (), mrna_start ) );

  // Synchronize the peptide to the alignment.
  while ( peptide3_alignment.length () < pep_start )
    peptide3_alignment.append ( "-" );

  // Append the alignments.
  if ( mrna_start + length < mrna.length () )
    mrna_alignment.append ( mrna.substring ( mrna_start, mrna_start + length ) );
  else
    mrna_alignment.append ( mrna.substring ( mrna_start ) );

  if ( pep_start + length < peptide3.length () )
    peptide3_alignment.append ( peptide3.substring ( pep_start, pep_start + length ) );
  else
    peptide3_alignment.append ( peptide3.substring ( pep_start ) );

}  // method alignSegment


/******************************************************************************/
public void printAlignment ()
{
  for ( int i = 0; i < mrna_alignment.length (); i += 60 )
  {
    // Write out the mRNA alignment segment.
    System.out.print ( (i + 1) + "\t" );
    if ( i + 60 < mrna_alignment.length () )
      System.out.println ( mrna_alignment.substring ( i, i + 60 ) );
    else
      System.out.println ( mrna_alignment.substring ( i ) );

    // Write out the peptide alignment segment.
    System.out.print ( (i + 1) + "\t" );
    if ( i + 60 < peptide3_alignment.length () )
      System.out.println ( peptide3_alignment.substring ( i, i + 60 ) );
    else
      System.out.println ( peptide3_alignment.substring ( i ) );

    System.out.println ();
  }  // for
}  // method printAlignment


/******************************************************************************/
public void align ( String file_name )
{
  int length = 0;
  int mrna_start = 0;
  int pep_start = 0;

  // Set the mRNA sequence into the alignment.
  mrna_alignment.setLength ( 0 );
  mrna_alignment.append ( mrna.toString () );

  peptide3_alignment.setLength ( 0 );

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

      // Set this segment into the alignment.
      // alignSegment ( mrna_start, pep_start, length );
      alignPeptide ( mrna_start, pep_start, length );
    }  // if

    mrna_start += length;
    pep_start += length;
  }
  while ( ( pep_start < peptide3.length () ) && ( length > 0 ) );

}  // method align


/******************************************************************************/

}  // class PepMrna
