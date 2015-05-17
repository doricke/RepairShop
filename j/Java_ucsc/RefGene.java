
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

public class RefGene extends Object
{


/******************************************************************************/

  private   String chromosome = "";		// chromosome (e.g., "chr1", ...)

  private   int  coding_end = 0;		// Coding region end

  private   int  coding_start = 0;		// Coding region start

  private   short  exon_count = 0;		// Number of exons

  private   Vector exon_ends = new Vector ();	// End of exons

  private   Vector exon_starts = new Vector ();	// Start of exons

  private   String name = "";			// name

  private   char strand = ' ';			// strand ('+' or '-')

  private   int  transcript_end = 0;		// End of transcript

  private   int  transcript_start = 0;		// Start of transcript


/******************************************************************************/
  // Constructor RefGene
  public RefGene ()
  {
    initialize ();
  }  // constructor RefGene


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    chromosome = "";
    coding_end = 0;
    coding_start = 0;
    exon_count = 0;
    exon_ends.removeAllElements ();
    exon_starts.removeAllElements ();
    name = "";
    strand = ' ';
    transcript_end = 0;
    transcript_start = 0;
  }  // method initialize 


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public int getCodingEnd ()
  {
    return coding_end;
  }  // method getCodingEnd


/******************************************************************************/
  public int getCodingStart ()
  {
    return coding_start;
  }  // method getCodingStart


/******************************************************************************/
  public short getExonCount ()
  {
    return exon_count;
  }  // method getExonCount


/******************************************************************************/
  public Vector getExonEnds ()
  {
    return exon_ends;
  }  // method getExonEnds


/******************************************************************************/
  public Vector getExonStarts ()
  {
    return exon_starts;
  }  // method getExonStarts


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public char getStrand ()
  {
    return strand;
  }  // method getStrand


/******************************************************************************/
  public int getTranscriptEnd ()
  {
    return transcript_end;
  }  // method getTranscriptEnd


/******************************************************************************/
  public int getTranscriptStart ()
  {
    return transcript_start;
  }  // method getTranscriptStart


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setCodingEnd ( int value )
  {
    coding_end = value;
  }  // method setCodingEnd


/******************************************************************************/
  public void setCodingStart ( int value )
  {
    coding_start = value;
  }  // method setCodingStart


/******************************************************************************/
  public void setExonCount ( short value )
  {
    exon_count = value;
  }  // method setExonCount


/******************************************************************************/
  private void setExonEnds ( String list )
  {
    exon_ends = splitList ( list );
  }  // method setExonEnds


/******************************************************************************/
  public void setExonEnds ( Vector value )
  {
    exon_ends = value;
  }  // method setExonEnds


/******************************************************************************/
  private void setExonStarts ( String list )
  {
    exon_starts = splitList ( list );
  }  // method setExonStarts


/******************************************************************************/
  public void setExonStarts ( Vector value )
  {
    exon_starts = value;
  }  // method setExonStarts


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setStrand ( char value )
  {
    strand = value;
  }  // method setStrand


/******************************************************************************/
  public void setTranscriptEnd ( int value )
  {
    transcript_end = value;
  }  // method setTranscriptEnd


/******************************************************************************/
  public void setTranscriptStart ( int value )
  {
    transcript_start = value;
  }  // method setTranscriptStart


/******************************************************************************/
  public Vector splitList ( String list )
  {
    Vector values = new Vector ();		// values from list

    if ( ( list.length () < 1 ) || ( exon_count < 1 ) )  return values;

    StringTokenizer tokens = new StringTokenizer ( list, "," );

    try
    {
      for ( int i = 0; i < exon_count; i++ )

        values.add ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "RefGene.splitList: " + e );
    }  // catch

    return values;
  }  // method splitList


/******************************************************************************/
  public void parse ( String value )
  {
    if ( ( value.length () < 1 ) || ( value.equals ( "null" ) ) )  return;

    StringTokenizer tokens = new StringTokenizer ( value, "\t" );

    try
    {
      setName ( tokens.nextToken () );
      setChromosome ( tokens.nextToken () );
      setStrand ( tokens.nextToken ().charAt ( 0 ) );
      setTranscriptStart ( LineTools.getInteger ( tokens.nextToken () ) );
      setTranscriptEnd ( LineTools.getInteger ( tokens.nextToken () ) );
      setCodingStart ( LineTools.getInteger ( tokens.nextToken () ) );
      setCodingEnd ( LineTools.getInteger ( tokens.nextToken () ) );
      setExonCount ( (short) LineTools.getInteger ( tokens.nextToken () ) );
      setExonStarts ( tokens.nextToken () );      
      setExonEnds ( tokens.nextToken () );      
    }  // try
    catch ( NoSuchElementException e )
    {
      System.out.println ( "RefGene.parse: " + e );
    }  // catch
  }  // method parse


/******************************************************************************/
public String toInfo ()
{
  return name + "\t" 
      + strand + "\t" 
      + transcript_start + "\t" 
      + transcript_end;
}  // method toInfo


/******************************************************************************/
public String toString ()
{
  return name + "\t" 
      + chromosome + "\t" 
      + strand + "\t" 
      + transcript_start + "\t" 
      + transcript_end;
}  // method toString


/******************************************************************************/

}  // class RefGene
