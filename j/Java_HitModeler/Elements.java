
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

public class Elements extends Object
{


/******************************************************************************/

  private   int  caat_box = 0;			// CAAT box

  private   String  end_sequence = null;	// end genomic sequence

  private   int  gc_box = 0;			// GC box

  private   int  poly_A = 0;			// PolyA site

  private   long  promoter_begin = 0L;		// start of promoter region

  private   long  promoter_end = 0L;		// end of promoter region

  private   SequenceTable  promoter_sequence = null;	// Genomic promoter sequence

  private   String  sequence = null;		// promoter genomic sequence

  private   int  start_codon = 0;		// start codon

  private   int  stop_codon = 0;		// stop codon

  private   int  tata_box = 0;			// TATA box 

  private   int  transcript_start = 0;		// transcription start site


/******************************************************************************/
  // Constructor Elements
  public Elements ()
  {
    initialize ();
  }  // constructor Elements


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    caat_box = 0;
    end_sequence = null;
    gc_box = 0;
    poly_A = 0;
    promoter_begin = 0L;
    promoter_end = 0L;
    promoter_sequence = null;
    sequence = null;
    start_codon = 0;
    stop_codon = 0;
    tata_box = 0;
    transcript_start = 0;
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    initialize ();
  }  // method close


/******************************************************************************/
  public int getCaatBox ()
  {
    return caat_box;
  }  // method getCaatBox


/******************************************************************************/
  public String getDetails ()
  {
    StringBuffer details = new StringBuffer ( 80 );
    details.setLength ( 0 );

    details.append ( "Elements[" );

    if ( gc_box != 0 )
      details.append ( "GC_box@" + (gc_box + 1) + " " );

    if ( caat_box != 0 )
      details.append ( "CAAT_box@" + (caat_box + 1) + " " );

    if ( tata_box != 0 )
      details.append ( "TATA_box@" + (tata_box + 1) + " " );

    if ( transcript_start != 0 )
      details.append ( "TSS@" + (transcript_start + 1) + " " );

    if ( start_codon > 0 )
      details.append ( "ATG@" + (start_codon + 1) + " " );

    if ( stop_codon > 0 )
      details.append ( "Stop@" + (stop_codon + 1) + " " );

    if ( poly_A != 0 )
      details.append ( "PolyA@" + (poly_A + 1) );

    details.append ( "]" );

    if ( details.length () < 15 )  details.setLength ( 0 );

    return details.toString ();
  }  // method getDetails


/******************************************************************************/
  public int getGcBox ()
  {
    return gc_box;
  }  // method getGcBox


/******************************************************************************/
  public int getPolyA ()
  {
    return poly_A;
  }  // method getPolyA


/******************************************************************************/
  public long getPromoterBegin ()
  {
    return promoter_begin;
  }  // method getPromoterBegin


/******************************************************************************/
  public long getPromoterEnd ()
  {
    return promoter_end;
  }  // method getPromoterEnd


/******************************************************************************/
  public SequenceTable getPromoterSequence ()
  {
    return promoter_sequence;
  }  // method getPromoterSequence


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


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
  public int getTataBox ()
  {
    return tata_box;
  }  // method getTataBox


/******************************************************************************/
  public int getTranscriptStart ()
  {
    return transcript_start;
  }  // method getTranscriptStart


/******************************************************************************/
  public void setCaatBox ( int value )
  {
    caat_box = value;
  }  // method setCaatBox


/******************************************************************************/
  public void setEndSequence ( String value )
  {
    end_sequence = value;
  }  // method setEndSequence


/******************************************************************************/
  public void setGcBox ( int value )
  {
    gc_box = value;
  }  // method setGcBox


/******************************************************************************/
  public void setPolyA ( int value )
  {
    poly_A = value;
  }  // method setPolyA


/******************************************************************************/
  public void setPromoterBegin ( long value )
  {
    promoter_begin = value;
  }  // method setPromoterBegin


/******************************************************************************/
  public void setPromoterEnd ( long value )
  {
    promoter_end = value;
  }  // method setPromoterEnd


/******************************************************************************/
  public void setPromoterSequence ( SequenceTable value )
  {
    promoter_sequence = value;
  }  // method setPromoterSequence


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;
  }  // method setSequence


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
  public void setTataBox ( int value )
  {
    tata_box = value;
  }  // method setTataBox


/******************************************************************************/
  public void setTranscriptStart ( int value )
  {
    transcript_start = value;
  }  // method setTranscriptStart


/******************************************************************************/
public void checkStartCodon ( int start )
{
  promoter_begin = ((long) start) - 1500L;
  if ( promoter_begin < 0L )  promoter_begin = 0L;
  promoter_end = (long) start;

  // Check for the sequence.
  if ( ( sequence == null ) || ( sequence.length () <= 0 ) )  return;
 
  // Validate start.
  if ( ( start < 0 ) || ( start + 2 >= sequence.length () ) )  return;

  String codon = "";
  if ( start + 3 < sequence.length () )
    codon = sequence.substring ( start, start + 3 );
  else
    codon = sequence.substring ( start );

  if ( codon.toLowerCase ().equals ( "atg" ) == true )

    start_codon = start;
/*
  else
  {
    System.out.println ( "codon '" + codon 
        + "' found rather than start codon position " + start );
    snap ( sequence, start );
  }  // else
*/
}  // method checkStartCodon


/******************************************************************************/
public void checkStopCodon ( int end )
{
  // Check for the sequence.
  if ( ( end_sequence == null ) || ( end_sequence.length () <= 0 ) )  return;

  // Validate end.
  if ( ( end < 0 ) || ( end + 2 > end_sequence.length () ) )  return;

  String codon = "";
  if ( end + 3 < end_sequence.length () )
    codon = end_sequence.substring ( end, end + 3 ).toLowerCase ();
  else
    codon = end_sequence.substring ( end ).toLowerCase ();

  if ( ( codon.equals ( "taa" ) == true ) ||
       ( codon.equals ( "tga" ) == true ) ||
       ( codon.equals ( "tag" ) == true ) )

    stop_codon = end;
/*
  else
  {
    System.out.println ( "codon '" + codon 
        + "' found rather than stop codon position " + end );
    snap ( end_sequence, end );
  }  // else 
*/
}  // method checkStopCodon


/******************************************************************************/
public void findPolyASite ( int gene_end )
{
  poly_A = SeqTools.findPolyA ( end_sequence, gene_end );

  // Check if no PolyA site was found.
  if ( poly_A < 0 )  poly_A = 0;

  // Ignore polyA sites that might be too far away.
  if ( poly_A > gene_end + 3000 )  poly_A = 0;
}  // method findPolyASite


/******************************************************************************/
// Find the promoter sequence patterns.
public void findPromoterElements ( String genomic, int gene_start )
{
  promoter_begin = ((long) gene_start) - 1500L;
  if ( promoter_begin < 0L )  promoter_begin = 0L;
  promoter_end = (long) gene_start;

  // Find the TATA box.
  int start;
  start = SeqTools.findTATAbox ( genomic, (int) promoter_begin, gene_start );

  if ( start >= 0 )  
    tata_box = start;

  // start = SeqTools.findBestPattern ( genomic, (int) promoter_begin, gene_start, "TATAAAA" );
  // if ( start >= 0 )  
  //   System.out.println ( "TATA box found at " + start + " using findBestPattern" );

  // Find the CAAT box.
  start = SeqTools.findBestPattern ( genomic, (int) promoter_begin, gene_start, "GGNCAATCT" );

  if ( start >= 0 )  
    caat_box = start;

  // Find a GC box.
  start = SeqTools.findBestPattern ( genomic, (int) promoter_begin, gene_start, "GGGCGG" );

  if ( start >= 0 )  
    gc_box = start;

  // Find Transcription Start Site.
  int promoter_start = gene_start - 50;
  if ( promoter_start < 0 )  promoter_start = 0;

  start = SeqTools.findBestPattern ( genomic, promoter_start, gene_start, "CTCATCA" );

  if ( start >= 0 )  
    transcript_start = start;
}  // method findPromoterElements


/******************************************************************************/
  public void reverseEndCoordinates ()
  {
    if ( end_sequence == null )
    {
      System.out.println ( "Elements.reverseEndCoordinates: *Warning* no end_sequence set." );
      return;
    }  // if

    int length = end_sequence.length ();

    if ( stop_codon > 0 )
      stop_codon = length - stop_codon + 1;

    if ( poly_A > 0 )
      poly_A   = length - poly_A   + 1;

  }  // method reverseEndCoordinates


/******************************************************************************/
  public void reversePromoterCoordinates ()
  {
    if ( sequence == null )
    {
      System.out.println ( "Elements.reverseCoordinates: *Warning* no sequence set." );
      return;
    }  // if

    reversePromoterCoordinates ( sequence.length () );
  }  // method reversePromoterCoordinates


/******************************************************************************/
  public void reversePromoterCoordinates ( int length )
  {
    if ( caat_box > 0 )
      caat_box = length - caat_box + 1;

    if ( tata_box > 0 )
      tata_box = length - tata_box + 1;

    if ( gc_box > 0 )
      gc_box   = length - gc_box   + 1;

    if ( start_codon > 0 )
      start_codon = length - start_codon + 1;

    if ( transcript_start > 0 )
      transcript_start = length - transcript_start + 1;
  }  // method reversePromoterCoordinates


/******************************************************************************/
  private void snap ( String sequence, int index )
  {
    int start = index - 10;
    int end = index + 15;
    if ( start < 0 )  start = 0;
    if ( end > sequence.length () )  end = sequence.length () - 1;

    System.out.print ( "\t" );
    for ( int i = start; i <= end; i++ )
    {
      if ( ( i == index ) || ( i == index + 3 ) )  System.out.print ( "  " );
      System.out.print ( sequence.charAt ( i ) );
    }  // for
    System.out.println ();
  }  // method snap


/******************************************************************************/
  public void print ()
  {
    String details = getDetails ();

    if ( details.length () > 0 )  System.out.println ( "\t" + details );
  }  // method print


/******************************************************************************/

}  // class Elements
