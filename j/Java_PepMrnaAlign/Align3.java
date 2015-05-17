

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

public class Align3 extends Object
{


/******************************************************************************/

  private   int frame = 0;		// Current matching frame index

  private   int frame_start = 0;	// Start of alignment in current frame

  private   String frames [] = null;	// Translated mRNA frames (3 letter code)

  private   int length = 0;		// Length of the current alignment segment

  private   String mrna = null;		// mRNA sequence

  private   String peptide = null;	// peptide sequence (1 letter codes)

  private   String peptide3 = null;	// Translate peptide sequence (3 letter code)

  private   int peptide3_start = 0;	// Start of alignment in peptide sequence


/******************************************************************************/
  // Constructor Align3
  public Align3 ()
  {
    initialize ();
  }  // constructor Align3


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    frame = 0;
    frame_start = 0;
    frames = null;
    length = 0;
    mrna = null;
    peptide = null;
    peptide3 = null;
    peptide3_start = 0;
  }  // method initialize 


/******************************************************************************/
  public int getFrame ()
  {
    return frame;
  }  // method getFrame;


/******************************************************************************/
  public int getFrameStart ()
  {
    return frame_start;
  }  // method getFrameStart


/******************************************************************************/
  public String [] getAlign3 ()
  {
    return frames;
  }  // method getAlign3


/******************************************************************************/
  public int getLength ()
  {
    return length;
  }  // method getLength


/******************************************************************************/
  public String getMrna ()
  {
    return mrna;
  }  // method getMrna


/******************************************************************************/
  public String getPeptide ()
  {
    return peptide;
  }  // method getPeptide


/******************************************************************************/
  public String getPeptide3 ()
  {
    return peptide3;
  }  // method getPeptide3


/******************************************************************************/
  public int getPeptide3Start ()
  {
    return peptide3_start;
  }  // method getPeptide3Start


/******************************************************************************/
  public void setMrna ( String value )
  {
    // Assert: mRNA is a least 3 base pairs.
    if ( value == null )  return;
    if ( value.length () < 3 )  return;

    mrna = value;

    frames = new String [ 3 ];
    frames [ 0 ] = SeqTools.translate3 ( value );
    frames [ 1 ] = "-" + SeqTools.translate3 ( value.substring ( 1 ) );
    frames [ 2 ] = "--" + SeqTools.translate3 ( value.substring ( 2 ) );
  }  // method setMrna


/******************************************************************************/
  public void setMrnaStart ( int value )
  {
    frame_start = value;
  }  // method setMrnaStart


/******************************************************************************/
  public void setPeptide ( String value )
  {
    peptide = value;

    peptide3 = SeqTools.convert1to3 ( peptide );
  }  // method setPeptide


/******************************************************************************/
  public void setPeptide3Start ( int value )
  {
    peptide3_start = value;
  }  // method setPeptide3Start


/******************************************************************************/
public void print ()
{
  System.out.println ( "frame[0] " + frames [ 0 ] );
  System.out.println ( "frame[1] " + frames [ 1 ] );
  System.out.println ( "frame[2] " + frames [ 2 ] );
  System.out.println ( "peptide3 " + peptide3 );
  System.out.println ();
}  // method print


/******************************************************************************/
// This method extends the current alignment segment.
private void extendSegment ()
{
  boolean extend = false;

  do
  {
    extend = false;

    if ( ( peptide3_start + length + 3 < peptide3.length () )  &&
         ( frame_start + length + 3 < frames [ frame ].length () ) )
    {
      if ( peptide3.substring ( peptide3_start + length, peptide3_start + length + 3 ).equals
               ( frames [ frame ].substring ( frame_start + length, frame_start + length + 3 ) ) == true )
      {
        length += 3;
        extend = true;
      }  // if
    }  // if
  }
  while ( extend == true );
}  // method extendSegment


/******************************************************************************/
public void alignNextSegment ()
{
  boolean found = false;
  length = 0;

  do
  {
    int start = peptide3.length ();		// best start see thus far

    // Check if too close to the end of the peptide sequence.
    int window = 15;
    if ( peptide3_start + window >= peptide3.length () )
    {
      window = peptide3.length () - peptide3_start;

      if ( window < 9 )  return;
    }  // if

    // Check each of the three frames.
    for ( int i = 0; i < frames.length; i++ )
    {
      int new_start = frames [ i ].indexOf 
          ( peptide3.substring ( peptide3_start, peptide3_start + window )
          , peptide3_start 
          );

      if ( ( new_start >= 0 ) && ( new_start < start ) )
      {
        frame = i;
        start = new_start;
        frame_start = new_start;
        found = true;
      }  // if
    }  // for

    if ( found == true )
    {
      length = window;

      // Extend this alignment segment.
      extendSegment ();
      return;
    }  // if

    peptide3_start += 3;	// Try again starting at the next codon
  }
  while ( ( found == false ) && ( peptide3_start < peptide3.length () ) );
}  // method alignNextSegment


/******************************************************************************/

}  // class Align3

