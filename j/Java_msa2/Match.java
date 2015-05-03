

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
public class Match extends Object
{

/******************************************************************************/

  int align_length;			// alignment length

  int frame_index;			// current frame

  int frame_start;			// start of alignment in the current frame

  String frames [];			// Translation frames

//  String genomic;			// genomic sequence

//  int genomic_start;			// start of alignment to genomic sequence

  int length;				// length of the current alignment

  String peptide;			// peptide sequence

  int peptide_align;			// start of peptide alignment

  int peptide_start;			// start of peptide segment


/******************************************************************************/
public Match ()
{
  clear ();
}  // constructor Match


/******************************************************************************/
public void clear ()
{
  align_length = 0;
  frame_index = 0;
  frame_start = -1;
  frames = null;
//  genomic = null;
//  genomic_start = -1;
  length = -1;
  peptide = null;
  peptide_align = -1;
  peptide_start = -1;
}  // method clear 


/******************************************************************************/
public int getAlignLength ()
{
  return align_length;
}  // method getAlignLength


/******************************************************************************/
public int getFrameStart ()
{
  return frame_start;
}  // getFrameStart


/******************************************************************************/
public int getFrameIndex ()
{
  return frame_index;
}  // method getFrameIndex


/******************************************************************************/
public int getLength ()
{
  return length;
}  // method getLength


/******************************************************************************/
public int getPeptideAlignStart ()
{
  return peptide_align;
}  // method getPeptideAlignStart


/******************************************************************************/
public int getPeptideStart ()
{
  return peptide_start;
}  // method getPeptideStart


/******************************************************************************/
public void setAlignLength ( int length )
{
  align_length = length;
}  // method setAlignLength


/******************************************************************************/
public void setPeptideStart ( int p_start )
{
  peptide_start = p_start;
}  // method setPeptideStart


/******************************************************************************/
public int extent ( String seq1, String seq2 )
{
  // Extend the alignment
  int length = 0;
  while ( ( length < seq1.length () ) && 
          ( length < seq2.length () ) &&
          ( seq1.charAt ( length ) == seq2.charAt ( length ) ) )

    length++;

  return length;
}  // method extent


/******************************************************************************/
private int scoreSplice ( String genomic, int exon_end, int next_start )
{
  int score = 0;				// splice site score


  if ( genomic.charAt ( exon_end - 1 ) == 'A' )  score++;
  if ( genomic.charAt ( exon_end     ) == 'G' )  score++;
  if ( genomic.charAt ( exon_end + 1 ) == 'G' )  score++;
  if ( genomic.charAt ( exon_end + 2 ) == 'T' )  score++;
  if ( genomic.charAt ( exon_end + 3 ) == 'A' )  score++;
  if ( genomic.charAt ( exon_end + 4 ) == 'A' )  score++;
  if ( genomic.charAt ( exon_end + 5 ) == 'G' )  score++;
  if ( genomic.charAt ( exon_end + 6 ) == 'T' )  score++;

  if ( genomic.charAt ( next_start - 3 ) == 'C' )  score++;
  if ( genomic.charAt ( next_start - 2 ) == 'A' )  score++;
  if ( genomic.charAt ( next_start - 1 ) == 'G' )  score++;

  return score;
}  // method scoreSplice


/******************************************************************************/
// Compute the optimum length of this exon.
private int spliceOptimize 
    ( String genomic				// Genomic sequence
    , int length				// length of current exon
    , int exon_end				// end of current exon
    , int next_start 				// start of next exon
    )
{
  int new_length = length;			// next length of current exon

  // Score the 3' most possibility.
  int score = scoreSplice ( genomic, exon_end, next_start );

  // Evaluate alternatives in the 5' direction.
  int i = 1;
  while ( genomic.charAt ( exon_end - i + 1 ) == genomic.charAt ( next_start - i ) )
  {
    int new_score = scoreSplice ( genomic, exon_end - i, next_start - i );
    if ( new_score > score )
    {
      new_length = length - i;
      score = new_score;
    }  // if

    i++;
  }  // while

  return new_length;
}  // method spliceOptimize


/******************************************************************************/
// Compute the optimum length of this exon.
private int spliceOptimizeRegion 
    ( String genomic				// Genomic sequence
    , int length				// length of current exon
    , int exon_end				// end of current exon
    , int next_start 				// start of next exon
    , int limit					// length of region to examine
    )
{
  int new_length = length;			// next length of current exon

  // Score the 3' most possibility.
  int score = scoreSplice ( genomic, exon_end, next_start );

  // Evaluate alternatives in the 5' direction.
  for ( int i = 1; i < limit; i++ )
  {
    int new_score = scoreSplice ( genomic, exon_end - i, next_start - i );
    if ( new_score > score )
    {
      new_length = length - i;
      score = new_score;
    }  // if
  }  // for

  return new_length;
}  // method spliceOptimizeRegion


/******************************************************************************/
public int findStart 
    ( String frame []
    , int g_start
    , String peptide_sequence
    , int p_start
    )
{
  // Initialize the object.
  frames = frame;
  frame_start = g_start;
  peptide = peptide_sequence;
  peptide_start = p_start;
  peptide_align = p_start;

  int  start = -1;
  int  window = 15;

  // Check if no more sequences to align.
  if ( ( peptide_start >= peptide.length () - window ) ||
       ( frame_start >= frame [ 0 ].length () ) )  return start;

  while ( peptide_align < peptide.length () - window ) 
  {
    start = -1;		// reset for each attempt at peptide_align

    // Search for the start of the genomic sequence.
    for ( int i = 0; (i < 3) && (start == -1); i++ ) 
    { 
      start = frame [ i ].indexOf ( 
          peptide.substring ( peptide_align, peptide_align + window ), frame_start );

      // Check if found.
      if ( start != -1 )
      {
        frame_index = i;
        frame_start = start;
        length = extent ( frame [ i ].substring ( start ), peptide.substring ( peptide_align ) );
        align_length = (peptide_align - peptide_start) + length;
        return start - (peptide_align - peptide_start);
      }  // if
    }  // for

    peptide_align++;
  }  // while

  return -1;  // No start found
}  // method findStart


/******************************************************************************/

}  // class Match

