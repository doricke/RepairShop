

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

public class SeqInfo extends Object
{


/******************************************************************************/

  private   String  contig_name = "";		// contig name

  private   boolean  complete = false;		// Partial/Complete flag

  private   int  evidence = 0;			// Percent evidence

  private   String  file_name = "";		// file name

  private   int  gc_percent = 0;		// mRNA percent GC composition

  private   int  genomic_begin = 0;		// genomic begin

  private   int  genomic_end = 0;		// genomic end

  private   boolean  motifs = false;		// motifs flag

  private   int  mRNA_length = 0;		// mRNA sequence length

  private   String  sequence_name = "";		// sequence name

  private   int  start_codon = 0;		// start codon position

  private   int  stop_codon = 0;		// stop codon position

  private   char  strand = '+';			// genomic strand

  private   boolean  unigene = false;		// In unigene set flag


/******************************************************************************/
  // Constructor SeqInfo
  public SeqInfo ()
  {
    initialize ();
  }  // constructor SeqInfo


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    contig_name = "";
    complete = false;
    evidence = 0;
    file_name = "";
    gc_percent = 0;
    genomic_begin = 0;
    genomic_end = 0;
    motifs = false;
    mRNA_length = 0;
    sequence_name = "";
    start_codon = 0;
    stop_codon = 0;
    strand = '+';
    unigene = false;
  }  // method initialize 


/******************************************************************************/
  public String getContigName ()
  {
    return contig_name;
  }  // method getContigName


/******************************************************************************/
  public boolean getComplete ()
  {
    return complete;
  }  // method getComplete


/******************************************************************************/
  public int getEvidence ()
  {
    return evidence;
  }  // method getEvidence


/******************************************************************************/
  public String getFileName ()
  {
    return file_name;
  }  // method getFileName


/******************************************************************************/
  public int getGcPercent ()
  {
    return gc_percent;
  }  // method getGcPercent


/******************************************************************************/
  public int getGenomicBegin ()
  {
    return genomic_begin;
  }  // method getGenomicBegin


/******************************************************************************/
  public int getGenomicEnd ()
  {
    return genomic_end;
  }  // method getGenomicEnd


/******************************************************************************/
  public int getLength ()
  {
    return genomic_end - genomic_begin + 1;
  }  // method getLength


/******************************************************************************/
  public boolean getMotifs ()
  {
    return motifs;
  }  // method getMotifs


/******************************************************************************/
  public int getMrnaLength ()
  {
    return mRNA_length;
  }  // method getMrnaLength


/******************************************************************************/
  public String getMrnaType ()
  {
    if ( complete == true )  return "Complete";

    if ( ( start_codon > 0 ) && ( stop_codon > 0 ) )  return "Complete";

    if ( start_codon > 0 )  return "Start";

    if ( stop_codon > 0 )  return "End";

    return "Inner";
  }  // method getMrnaType


/******************************************************************************/
  public String getSequenceName ()
  {
    return sequence_name;
  }  // method getSequenceName


/******************************************************************************/
  public int getStartCodon ()
  {
    return start_codon;
  }  // method getStartCodon


/******************************************************************************/
  public int getStopCodon ()
  {
    return stop_codon;
  }  // method getStopCodon


/******************************************************************************/
  public char getStrand ()
  {
    return strand;
  }  // method getStrand


/******************************************************************************/
  public boolean getUnigene ()
  {
    return unigene;
  }  // method getUnigene


/******************************************************************************/
  public boolean isComplete ()
  {
    return complete;
  }  // method isComplete


/******************************************************************************/
  public boolean isMotifs ()
  {
    return motifs;
  }  // method isMotifs


/******************************************************************************/
  public boolean isUnigene ()
  {
    return unigene;
  }  // method isUnigene


/******************************************************************************/
  public void setContigName ( String value )
  {
    contig_name = value;
  }  // method setContigName


/******************************************************************************/
  public void setComplete ( boolean value )
  {
    complete = value;
  }  // method setComplete


/******************************************************************************/
  public void setEvidence ( int value )
  {
    // Validate the percent value.
    if ( value > 100 )
    {
      System.out.println ( "SeqInfo.setEvidence: value too high: " + value );
      value = 100;
    }  // if

    if ( value < 0 )
    {
      System.out.println ( "SeqInfo.setEvidence: value too low: " + value );
      value = 0;
    }  // if

    evidence = value;
  }  // method setEvidence


/******************************************************************************/
  public void setFileName ( String value )
  {
    file_name = value;
  }  // method setFileName


/******************************************************************************/
  public void setGcPercent ( int value )
  {
    // Validate the percent value.
    if ( value > 100 )
    {
      System.out.println ( "SeqInfo.setGcPercent: value too high: " + value );
      value = 100;
    }  // if

    if ( value < 0 )
    {
      System.out.println ( "SeqInfo.setGcPercent: value too low: " + value );
      value = 0;
    }  // if

    gc_percent = value;
  }  // method setGcPercent


/******************************************************************************/
  public void setGenomicBegin ( int value )
  {
    genomic_begin = value;
  }  // method setGenomicBegin


/******************************************************************************/
  public void setGenomicEnd ( int value )
  {
    genomic_end = value;
  }  // method setGenomicEnd


/******************************************************************************/
  public void setMotifs ( boolean value )
  {
    motifs = value;
  }  // method setMotifs


/******************************************************************************/
  public void setMrnaLength ( int value )
  {
    mRNA_length = value;
  }  // method setMrnaLength


/******************************************************************************/
  public void setSequenceName ( String value )
  {
    sequence_name = value;
  }  // method setSequenceName


/******************************************************************************/
  public void setStartCodon ( int value )
  {
    start_codon = value;
  }  // method setStartCodon


/******************************************************************************/
  public void setStopCodon ( int value )
  {
    stop_codon = value;
  }  // method setStopCodon


/******************************************************************************/
  public void setStrand ( char value )
  {
    strand = value;
  }  // method setStrand


/******************************************************************************/
  public void setUnigene ( boolean value )
  {
    unigene = value;
  }  // method setUnigene


/******************************************************************************/
  public void print ()
  {
    System.out.println
        ( contig_name
        + " "
        + sequence_name
        + " "
        + strand
        + " ["
        + genomic_begin
        + "-"
        + genomic_end
        + "] Evidence "
        + evidence
        + "% unigene: "
        + unigene
        + " Complete: "
        + complete
        + " Motifs: " 
        + motifs
        );
  }  // method print 


/******************************************************************************/

}  // class SeqInfo
