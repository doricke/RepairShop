
// import Piece;
// import Hit;

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

public class HitRegion extends Object
{


/******************************************************************************/

  private   int  genomic_length = 0;		// hit sequence length

  private   int  genomic_maximum = 0;		// hit region end

  private   int  genomic_minimum = 0;		// hit region begin

  private   int  target_aa_maximum = 0;		// target protein hit region end

  private   int  target_aa_minimum = 0;		// target protein hit region begin

  private   int  target_dna_maximum = 0;	// target DNA hit region end

  private   int  target_dna_minimum = 0;	// target DNA hit region begin


/******************************************************************************/
  // Constructor HitRegion
  public HitRegion ()
  {
    initialize ();
  }  // constructor HitRegion


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    genomic_length = 0;
    genomic_maximum = 0;
    genomic_minimum = 0;
    target_aa_maximum = 0;
    target_aa_minimum = 0;
    target_dna_maximum = 0;
    target_dna_minimum = 0;
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    initialize ();
  }  // method close


/******************************************************************************/
  public int getGenomicLength ()
  {
    return genomic_length;
  }  // method getGenomicLength


/******************************************************************************/
  public int getGenomicMaximum ()
  {
    return genomic_maximum;
  }  // method getGenomicMaximum


/******************************************************************************/
  public int getGenomicMinimum ()
  {
    return genomic_minimum;
  }  // method getGenomicMinimum


/******************************************************************************/
  public int getTargetAaMaximum ()
  {
    return target_aa_maximum;
  }  // method getTargetAaMaximum


/******************************************************************************/
  public int getTargetAaMinimum ()
  {
    return target_aa_minimum;
  }  // method getTargetAaMinimum


/******************************************************************************/
  public int getTargetDnaMaximum ()
  {
    return target_dna_maximum;
  }  // method getTargetDnaMaximum


/******************************************************************************/
  public int getTargetDnaMinimum ()
  {
    return target_dna_minimum;
  }  // method getTargetDnaMinimum


/******************************************************************************/
  public void setGenomicLength ( int value )
  {
    genomic_length = value;
  }  // method setGenomicLength


/******************************************************************************/
  public void setGenomicMaximum ( int value )
  {
    genomic_maximum = value;
  }  // method setGenomicMaximum


/******************************************************************************/
  public void setGenomicMinimum ( int value )
  {
    genomic_minimum = value;
  }  // method setGenomicMinimum


/******************************************************************************/
  public void setTargetAaMaximum ( int value )
  {
    target_aa_maximum = value;
  }  // method setTargetAaMaximum


/******************************************************************************/
  public void setTargetAaMinimum ( int value )
  {
    target_aa_minimum = value;
  }  // method setTargetAaMinimum


/******************************************************************************/
  public void setTargetDnaMaximum ( int value )
  {
    target_dna_maximum = value;
  }  // method setTargetDnaMaximum


/******************************************************************************/
  public void setTargetDnaMinimum ( int value )
  {
    target_dna_minimum = value;
  }  // method setTargetDnaMinimum


/******************************************************************************/
public void computeHitRegion ( Piece [] pieces )
{
  // Validate pieces.
  if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return;

  computeHitRegion ( pieces, 0, pieces.length - 1 );
}  // method computeHitRegion


/******************************************************************************/
public void computeHitRegion ( Piece [] pieces, int first, int last )
{
  // Validate pieces.
  if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return;

  if ( first < 0 )  first = 0;
  if ( last > pieces.length )  last = pieces.length - 1;

  // Collect the region limits of the target sequence similarities.
  for ( int p = first; p <= last; p++ )
  {
    if ( ( genomic_minimum == 0 ) || ( pieces [ p ].getGenomicBegin () < genomic_minimum ) )
  
      genomic_minimum = pieces [ p ].getGenomicBegin ();

    if ( ( genomic_maximum == 0 ) || ( pieces [ p ].getGenomicEnd () > genomic_maximum ) )
  
      genomic_maximum = pieces [ p ].getGenomicEnd ();

    Hit hit = pieces [ p ].getSimilarity ();
    if ( ( hit != null ) && 
         ( ( hit.getProgramName ().equals ( "TBLASTN" ) == true ) || 
           ( hit.getProgramName ().equals ( "BLASTX" ) == true  ) ) )
    {
      if ( ( target_aa_minimum == 0 ) || 
           ( pieces [ p ].getTargetBegin () < target_aa_minimum ) )
  
        target_aa_minimum = pieces [ p ].getTargetBegin ();
  
      if ( ( target_aa_maximum == 0 ) || 
           ( pieces [ p ].getTargetEnd () > target_aa_maximum ) )
  
        target_aa_maximum = pieces [ p ].getTargetEnd ();
    }  // if
    else  // DNA coordinates
    {
      if ( ( target_dna_minimum == 0 ) || 
           ( pieces [ p ].getTargetBegin () < target_dna_minimum ) )
  
        target_dna_minimum = pieces [ p ].getTargetBegin ();
  
      if ( ( target_dna_maximum == 0 ) || 
           ( pieces [ p ].getTargetEnd () > target_dna_maximum ) )
  
        target_dna_maximum = pieces [ p ].getTargetEnd ();
    }  // else
  }  // for
}  // method computeHitRegion


/******************************************************************************/
  public void print ( String region_name )
  {
    System.out.println ();
    System.out.println ( "HitRegion: " + region_name );
    System.out.println ( "\thit: " + genomic_minimum + "-" + genomic_maximum );
    if ( target_aa_minimum > 0 )
      System.out.println ( "\tQaa: " + target_aa_minimum + "-" + target_aa_maximum  );
    if ( target_dna_minimum > 0 )
      System.out.println ( "\tQdna " + target_dna_minimum + "-" + target_dna_maximum );
  }  // method print


/******************************************************************************/

}  // class HitRegion
