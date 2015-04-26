
// import Evidence;
// import SequenceTable;

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

public class ExonTable extends Object
{


/******************************************************************************/

  private  String  description = "";		// similarity description

  private  Evidence  evidence = null;		// exon evidence

  private  boolean  exon_begin_splice = false;	// 5' splice site flag

  private  boolean  exon_end_splice = false;	// 3' splice site flag

  private  int  exon_number = 0;		// exon number

  private  int  frame_begin = -1;		// 5' exon frame

  private  int  frame_end = -1;			// 3' exon frame

  private  long  genomic_end = 0;		// exon end

  private  SequenceTable  genomic_sequence = null;	// genomic sequence

  private  long  genomic_start = 0;		// exon start

  private  long  mrna_end = 0;			// mRNA end of exon

  private  long  mrna_start = 0;		// mRNA start of exon

  private  char  mrna_strand = '+';		// exon mRNA strand

  private  String  program_name = "";		// similarity program name

  private  boolean  selected = false;		// exon selection flag

  private  char  genomic_strand = '+';		// exon genomic_strand


/******************************************************************************/
  // Constructor ExonTable
  public ExonTable ()
  {
    initialize ();
  }  // constructor ExonTable


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    description = "";
    evidence = null;
    exon_begin_splice = false;
    exon_end_splice = false;
    exon_number = 0;
    frame_begin = -1;
    frame_end = -1;
    genomic_end = 0;
    genomic_sequence = null;
    genomic_start = 0;
    genomic_strand = '+';
    mrna_end = 0;
    mrna_start = 0;
    mrna_strand = '+';
    program_name = "";
    selected = false;
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    initialize ();
  }  // method close


/******************************************************************************/
  public void addEvidence ( Evidence new_evidence )
  {
    // Validate new_evidence.
    if ( new_evidence == null )  return;

    // Check if no existing evidence.
    if ( evidence == null )
    {
      // Check for overlap of evidence with this exon.
      long overlap_start = new_evidence.getBegin ();
      long overlap_end = new_evidence.getEnd ();
      if ( genomic_start > overlap_start )  overlap_start = genomic_start;
      if ( genomic_end < overlap_end )  overlap_end = genomic_end;
      if ( ( overlap_start >= genomic_start ) &&
           ( overlap_end   <= genomic_end ) )
      {
        // Check if coordinates the same as evidence.
        if ( ( overlap_start == new_evidence.getBegin () ) &&
             ( overlap_end   == new_evidence.getEnd   () ) )
        {
          evidence = new_evidence;
        }  // if
        else
        {
          evidence = new Evidence ();
          evidence.setBegin ( overlap_start );
          evidence.setEnd ( overlap_end );
          evidence.setDescription ( new_evidence.getDescription () );
        }  // else
      }  // if
    }
    else  // merge the two evidences.
    {
      // Check for overlap of evidence with this exon.
      long overlap_start = new_evidence.getBegin ();
      long overlap_end = new_evidence.getEnd ();
      if ( genomic_start > overlap_start )  overlap_start = genomic_start;
      if ( genomic_end < overlap_end )  overlap_end = genomic_end;

      // Update the coordinates of the current evidence.
      if ( overlap_end > evidence.getEnd () )  evidence.setEnd ( overlap_end );
      if ( overlap_start < evidence.getBegin () )  evidence.setBegin ( overlap_start );
      evidence.setDescription ( new_evidence.getDescription () );
    }  // else
  }  // method addEvidence


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public Evidence getEvidence ()
  {
    return evidence;
  }  // method getEvidence


/******************************************************************************/
  public int getExonNumber ()
  {
    return exon_number;
  }  // method getExonNumber


/******************************************************************************/
  public int getFrameBegin ()
  {
    return frame_begin;
  }  // method getFrameBegin


/******************************************************************************/
  public int getFrameEnd ()
  {
    return frame_end;
  }  // method getFrameEnd


/******************************************************************************/
  public long getGenomicEnd ()
  {
    return genomic_end;
  }  // method getGenomicEnd


/******************************************************************************/
  public SequenceTable getGenomicSequence ()
  {
    return genomic_sequence;
  }  // method getGenomicSequence


/******************************************************************************/
  public long getGenomicStart ()
  {
    return genomic_start;
  }  // method getGenomicStart


/******************************************************************************/
  public char getGenomicStrand ()
  {
    return genomic_strand;
  }  // method getGenomicStrand


/******************************************************************************/
  public long getMrnaEnd ()
  {
    return mrna_end;
  }  // method getMrnaEnd


/******************************************************************************/
  public long getMrnaStart ()
  {
    return mrna_start;
  }  // method getMrnaStart


/******************************************************************************/
  public char getMrnaStrand ()
  {
    return mrna_strand;
  }  // method getMrnaStrand


/******************************************************************************/
  public String getProgramName ()
  {
    return program_name;
  }  // method getProgramName


/******************************************************************************/
  public boolean getSelected ()
  {
    return selected;
  }  // method getSelected


/******************************************************************************/
  public boolean isExonBeginSplice ()
  {
    return exon_begin_splice;
  }  // isExonBeginSplice


/******************************************************************************/
  public boolean isExonEndSplice ()
  {
    return exon_end_splice;
  }  // isExonEndSplice


/******************************************************************************/
  public boolean isSelected ()
  {
    return selected;
  }  // method isSelected


/******************************************************************************/
  public void setDescription ( String value )
  {
    description = value;
  }  // method setDescription


/******************************************************************************/
  public void setEvidence ( Evidence value )
  {
    evidence = value;
  }  // method evidence


/******************************************************************************/
  public void setExonBeginSplice ( boolean value )
  {
    exon_begin_splice = value;
  }  // method setExonBeginSplice


/******************************************************************************/
  public void setExonEndSplice ( boolean value )
  {
    exon_end_splice = value;
  }  // method setExonEndSplice


/******************************************************************************/
  public void setExonNumber ( int value )
  {
    exon_number = value;
  }  // method setExonNumber


/******************************************************************************/
  public void setFrameBegin ( int value )
  {
    frame_begin = value;
  }  // method setFrameBegin


/******************************************************************************/
  public void setFrameEnd ( int value )
  {
    frame_end = value;
  }  // method setFrameEnd


/******************************************************************************/
  public void setGenomicEnd ( long value )
  {
    genomic_end = value;
  }  // method setGenomicEnd


/******************************************************************************/
  public void setGenomicSequence ( SequenceTable value )
  {
    genomic_sequence = value;
  }  // method setGenomicSequence


/******************************************************************************/
  public void setGenomicStart ( long value )
  {
    genomic_start = value;
  }  // method setGenomicStart


/******************************************************************************/
  public void setMrnaEnd ( long end )
  {
    mrna_end = end;
  }  // method setMrnaEnd


/******************************************************************************/
  public void setMrnaStart ( long start )
  {
    mrna_start = start;
  }  // method setMrnaStart


/******************************************************************************/
  public void setMrnaStrand ( char value )
  {
    mrna_strand = value;
  }  // method setMrnaStrand


/******************************************************************************/
  public void setGenomicStrand ( char value )
  {
    genomic_strand = value;
  }  // method setGenomicStrand


/******************************************************************************/
  public void setGenomicStrand ( String value )
  {
    if ( value == null )  return;

    // Check if the String is long enough.
    if ( value.length () > 0 )
      genomic_strand = value.charAt ( 0 );
  }  // method setGenomicStrand


/******************************************************************************/
  public void setProgramName ( String value )
  {
    program_name = value;
  }  // method setProgramName


/******************************************************************************/
  public void setSelected ( boolean value )
  {
    selected = value;
  }  // method setSelected


/******************************************************************************/
  public void printExon ()
  {
    System.out.println 
        ( exon_number
        + " "
        + genomic_strand
        + " f"
        + frame_begin
        + " ["
        + genomic_start
        + "-"
        + genomic_end
        + "] f"
        + frame_end
        + " mRNA "
        + mrna_strand
        + " ["
        + mrna_start
        + "-"
        + mrna_end
        + "] "
        + program_name
        );
  }  // method printExon


/******************************************************************************/
  public void print ()
  {
    System.out.println 
        ( "Exon: "
        + genomic_strand
        + " f"
        + frame_begin
        + " ["
        + genomic_start
        + "-"
        + genomic_end
        + "] f"
        + frame_end
        + " mRNA "
        + mrna_strand
        + " ["
        + mrna_start
        + "-"
        + mrna_end
        + "] "
        + program_name
        );

    if ( evidence != null )  evidence.print ();
  }  // method print


/******************************************************************************/

}  // class ExonTable
