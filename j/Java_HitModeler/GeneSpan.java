
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

public class GeneSpan extends Object
{


/******************************************************************************/

  private   long  genomic_dna_begin = 0;	// start of genomic sequence DNA similarity region

  private   long  genomic_dna_end = 0;		// end of genomic sequence DNA similarity region

  private   boolean  genomic_increase = true;	// genomic sequence region coordinates are increasing

  private   long  target_aa_begin = 0;		// start of target sequence protein similarity region

  private   long  target_aa_end = 0;		// end of target sequence protein similarity region

  private   long  target_dna_begin = 0;		// start of target sequence DNA similarity region

  private   long  target_dna_end = 0;		// end of target sequence DNA similarity region

  private   String  target_name = "";		// target sequence name


/******************************************************************************/
  // Constructor GeneSpan
  public GeneSpan ()
  {
    initialize ();
  }  // constructor GeneSpan


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    genomic_dna_begin = 0;
    genomic_dna_end = 0;
    genomic_increase = true;
    target_aa_begin = 0;
    target_aa_end = 0;
    target_dna_begin = 0;
    target_dna_end = 0;
    target_name = "";
  }  // method initialize 


/******************************************************************************/
  public long getGenomicDnaBegin ()
  {
    return genomic_dna_begin;
  }  // method getGenomicDnaBegin


/******************************************************************************/
  public long getGenomicDnaEnd ()
  {
    return genomic_dna_end;
  }  // method getGenomicDnaEnd


/******************************************************************************/
  public long getTargetAaBegin ()
  {
    return target_aa_begin;
  }  // method getTargetAaBegin


/******************************************************************************/
  public long getTargetAaEnd ()
  {
    return target_aa_end;
  }  // method getTargetAaEnd


/******************************************************************************/
  public long getTargetDnaBegin ()
  {
    return target_dna_begin;
  }  // method getTargetDnaBegin


/******************************************************************************/
  public long getTargetDnaEnd ()
  {
    return target_dna_end;
  }  // method getTargetDnaEnd


/******************************************************************************/
  public String getTargetName ()
  {
    return target_name;
  }  // method getTargetName


/******************************************************************************/
  public boolean getGenomicIncrease ()
  {
    return genomic_increase;
  }  // method getGenomicIncrease


/******************************************************************************/
  public boolean inRange ( ExonTable exon )
  {
    // Check if first exon.
    if ( genomic_dna_begin == 0 )  return true; 

    // Check if the start of this exon is close to the last exon.
    if ( exon.getGenomicStart () < genomic_dna_end + 4000 )  
      return true;
    else
      return false;
  }  // method inRange


/******************************************************************************/
  public boolean inRange ( Hit hit )
  {
    // Check if first hit.
    if ( genomic_dna_begin == 0 )
      return true; 

    // Check if the start of this hit is close to the last hit.
    if ( hit.getTargetStart () < genomic_dna_end + 4000 )  
      return true;
    else
      return false;
  }  // method inRange


/******************************************************************************/
  public boolean isGenomicIncrease ()
  {
    return genomic_increase;
  }  // method isGenomicIncrease


/******************************************************************************/
  public void setGenomicDnaBegin ( long value )
  {
    genomic_dna_begin = value;
  }  // method setGenomicDnaBegin


/******************************************************************************/
  public void setGenomicDnaEnd ( long value )
  {
    genomic_dna_end = value;
  }  // method setGenomicDnaEnd


/******************************************************************************/
  public void setTargetAaBegin ( long value )
  {
    target_aa_begin = value;
  }  // method setTargetAaBegin


/******************************************************************************/
  public void setTargetAaEnd ( long value )
  {
    target_aa_end = value;
  }  // method setTargetAaEnd


/******************************************************************************/
  public void setTargetDnaBegin ( long value )
  {
    target_dna_begin = value;
  }  // method setTargetDnaBegin


/******************************************************************************/
  public void setTargetDnaEnd ( long value )
  {
    target_dna_end = value;
  }  // method setTargetDnaEnd


/******************************************************************************/
  public void setTargetName ( String value )
  {
    target_name = value;
  }  // method setTargetName


/******************************************************************************/
  public void setGenomicIncrease ( boolean value )
  {
    genomic_increase = value;
  }  // method setGenomicIncrease


/******************************************************************************/
  private boolean isExonIncreasing ( ExonTable exon )
  {
    if ( exon.getGenomicStrand () == exon.getMrnaStrand () )  return true;
    return false;
  }  // method isExonIncreasing


/******************************************************************************/
  private boolean isHitIncreasing ( Hit hit )
  {
    if ( hit.getTargetStrand () == hit.getQueryStrand () )  return true;
    return false;
  }  // method isHitIncreasing


/******************************************************************************/
  private boolean isTargetIncreasing ( ExonTable exon )
  {
    String program_name = exon.getProgramName ();

    if ( ( program_name.equals ( "BLASTX" ) == true ) ||
         ( program_name.equals ( "TBLASTN" ) == true ) )
    {
      if ( exon.getMrnaStart () < target_aa_begin )  return false;
      if ( exon.getMrnaEnd () < target_aa_end )  return false;
    }  // if
    else
    {
      if ( exon.getMrnaStart () < target_dna_begin )  return false;
      if ( exon.getMrnaEnd () < target_dna_end )  return false;
    }  // else

    return true;
  }  // method isTargetIncreasing


/******************************************************************************/
  private boolean isHitTargetIncreasing ( Hit hit )
  {
    String program_name = hit.getProgramName ();

    if ( ( program_name.equals ( "BLASTX" ) == true ) ||
         ( program_name.equals ( "TBLASTN" ) == true ) )
    {
      if ( hit.getTargetStart () < target_aa_begin )  return false;
      if ( hit.getTargetEnd () < target_aa_end )  return false;
    }  // if
    else
    {
      if ( hit.getTargetStart () < target_dna_begin )  return false;
      if ( hit.getTargetEnd () < target_dna_end )  return false;
    }  // else

    return true;
  }  // method isHitTargetIncreasing


/******************************************************************************/
  private void addExon ( ExonTable exon )
  {
    String program_name = exon.getProgramName ();

    // Check if first exon.
    if ( genomic_dna_begin == 0 )
    {
      genomic_dna_begin = exon.getGenomicStart ();
      genomic_dna_end   = exon.getGenomicEnd ();

      if ( ( program_name.equals ( "BLASTX" ) == true ) ||
           ( program_name.equals ( "TBLASTN" ) == true ) )
      {
        target_aa_begin = exon.getMrnaStart ();
        target_aa_end   = exon.getMrnaEnd ();
      }  // if
      else
      {
        target_dna_begin = exon.getMrnaStart ();
        target_dna_end   = exon.getMrnaEnd ();
      }  // else

      genomic_increase = isExonIncreasing ( exon );
    }  // if
    else
    {
      // Check target direction.
      if ( genomic_increase == true )
      {
        if ( genomic_dna_end < exon.getGenomicEnd () )
          genomic_dna_end = exon.getGenomicEnd ();

        if ( ( program_name.equals ( "BLASTX" ) == true ) ||
             ( program_name.equals ( "TBLASTN" ) == true ) )
        {
          if ( target_aa_end < exon.getMrnaEnd () )
            target_aa_end = exon.getMrnaEnd ();
        }  // if
        else
        {
          if ( target_dna_end < exon.getMrnaEnd () )
            target_dna_end = exon.getMrnaEnd ();
        }  // else
      }  // if
      else
      {
        if ( genomic_dna_begin > exon.getGenomicStart () == true )
          genomic_dna_begin = exon.getGenomicStart ();

        if ( ( program_name.equals ( "BLASTX" ) == true ) ||
             ( program_name.equals ( "TBLASTN" ) == true ) )
        {
          if ( target_aa_begin < exon.getMrnaStart () == true )
            target_aa_begin = exon.getMrnaStart ();
        }  // if
        else
        {
          if ( target_dna_begin < exon.getMrnaStart () == true )
            target_dna_begin = exon.getMrnaStart ();
        }  // else
      }  // else
    }  // else
  }  // method addExon


/******************************************************************************/
  private void addHit ( Hit hit )
  {
    String program_name = hit.getProgramName ();

    // Check if first hit.
    if ( genomic_dna_begin == 0 )
    {
      genomic_dna_begin = hit.getTargetStart ();
      genomic_dna_end   = hit.getTargetEnd ();

      if ( ( program_name.equals ( "BLASTX" ) == true ) ||
           ( program_name.equals ( "TBLASTN" ) == true ) )
      {
        target_aa_begin = hit.getQueryStart ();
        target_aa_end   = hit.getQueryEnd ();
      }  // if
      else
      {
        target_dna_begin = hit.getQueryStart ();
        target_dna_end   = hit.getQueryEnd ();
      }  // else

      genomic_increase = isHitIncreasing ( hit );
    }  // if
    else
    {
      // Check target direction.
      if ( genomic_increase == true )
      {
        if ( genomic_dna_end < hit.getTargetEnd () )
          genomic_dna_end = hit.getTargetEnd ();

        if ( ( program_name.equals ( "BLASTX" ) == true ) ||
             ( program_name.equals ( "TBLASTN" ) == true ) )
        {
          if ( target_aa_end < hit.getQueryEnd () )
            target_aa_end = hit.getQueryEnd ();
        }  // if
        else
        {
          if ( target_dna_end < hit.getQueryEnd () )
            target_dna_end = hit.getQueryEnd ();
        }  // else
      }  // if
      else
      {
        if ( genomic_dna_begin > hit.getTargetStart () == true )
          genomic_dna_begin = hit.getTargetStart ();

        if ( ( program_name.equals ( "BLASTX" ) == true ) ||
             ( program_name.equals ( "TBLASTN" ) == true ) )
        {
          if ( target_aa_begin < hit.getQueryStart () == true )
            target_aa_begin = hit.getQueryStart ();
        }  // if
        else
        {
          if ( target_dna_begin < hit.getQueryStart () == true )
            target_dna_begin = hit.getQueryStart ();
        }  // else
      }  // else
    }  // else
  }  // method addHit


/******************************************************************************/
  public boolean canAddExon ( ExonTable exon )
  {
    // Check if first exon.
    if ( genomic_dna_begin == 0 )
    {
      addExon ( exon );
      return true;
    }  // if

    // Check if strand orientation is consistent.
    if ( genomic_increase == true )
    {
      if ( isExonIncreasing ( exon ) == false )  return false;

      // Check that target coordinates is consistent with previous hits.
      if ( isTargetIncreasing ( exon ) == false )  return false;
    }  // if
    else
    {
      if ( isExonIncreasing ( exon ) == true )  return false;

      // Check that target coordinates is consistent with previous hits.
      if ( isTargetIncreasing ( exon ) == false )  return false;
    }  // else

    addExon ( exon );
    return true;
  }  // method canAddExon


/******************************************************************************/
  public boolean canAddHit ( Hit hit )
  {
    // Check if first hit.
    if ( genomic_dna_begin == 0 )
    {
      addHit ( hit );
      return true;
    }  // if

    // Check if strand orientation is consistent.
    if ( genomic_increase == true )
    {
      if ( isHitIncreasing ( hit ) == false )  return false;

      // Check that target coordinates is consistent with previous hits.
      if ( isHitTargetIncreasing ( hit ) == false )  return false;
    }  // if
    else
    {
      if ( isHitIncreasing ( hit ) == true )
      {
        System.out.println ( "canAddHit: isHitIncreasing () = true" );
        print ();
        return false;
      }  // if

      // Check that target coordinates is consistent with previous hits.
      if ( isHitTargetIncreasing ( hit ) == false )
      {
        System.out.println ( "canAddHit: isHitTargetIncreasing () = false" );
        print ();
        return false;
      }  // if
    }  // else

    addHit ( hit );
    return true;
  }  // method canAddHit


/******************************************************************************/
  public void print ()
  {
    System.out.print ( "GeneSpan: " );

    if ( genomic_increase == true )
      System.out.print ( "increasing " );
    else
      System.out.print ( "decreasing " );
 

    System.out.println 
        ( "genomic ["
        + genomic_dna_begin
        + "-"
        + genomic_dna_end
        + "] "
        + target_name
        + " aa ["
        + target_aa_begin
        + "-"
        + target_aa_end
        + "] dna ["
        + target_dna_begin
        + "-"
        + target_dna_end
        + "]"
        );
  }  // method print


/******************************************************************************/

}  // class GeneSpan
