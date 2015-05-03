
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
public class Segment extends Object
{

/******************************************************************************/

  int alignment_start;			// start of segment in the alignment

  int alignment_end;			// end of segment in the alignment

  int identities;			// number of identities in the alignment

  String sequence_name;			// Sequence name

  int sequence_start;			// start of alignment in the sequence

  int sequence_end;			// end of alignment in the sequence

  char sequence_strand;			// strand of alignment in the sequence

  String sequence_segment;		// Aligned sequence segment


/******************************************************************************/
public Segment ()
{
  clear ();
}  // constructor Segment


/******************************************************************************/
public void clear ()
{
  alignment_end = 0;
  alignment_start = 0;
  identities = 0;
  sequence_end = 0;
  sequence_name = null;
  sequence_segment = null;
  sequence_start = 0;
  sequence_strand = '+';
}  // method clear


/******************************************************************************/
public int getAlignmentEnd ()
{
  return alignment_end;
}  // method getAlignmentEnd


/******************************************************************************/
public int getAlignmentStart ()
{
  return alignment_start;
}  // method getAlignmentStart


/******************************************************************************/
public int getIdentities ()
{
  return identities;
}  // method getIdentities


/******************************************************************************/
public int getSequenceEnd ()
{
  return sequence_end;
}  // method getSequenceEnd


/******************************************************************************/
public String getSequenceName ()
{
  return sequence_name;
}  // method getSequenceName


/******************************************************************************/
public String getSequenceSegment ()
{
  return sequence_segment;
}  // method getSequenceSegment


/******************************************************************************/
public int getSequenceStart ()
{
  return sequence_start;
}  // method getSequenceStart


/******************************************************************************/
public char getSequenceStrand ()
{
  return sequence_strand;
}  // method getSequenceStrand


/******************************************************************************/
public void setAlignmentEnd ( int align_end )
{
  alignment_end = align_end;
}  // method setAlignmentEnd


/******************************************************************************/
public void setAlignmentStart ( int align_start )
{
  alignment_start = align_start;
}  // method setAlignmentStart


/******************************************************************************/
public void setIdentities ( int idents )
{
  identities = idents;
}  // method setIdentities


/******************************************************************************/
public void setSequenceEnd ( int seq_end )
{
  sequence_end = seq_end;
}  // method setSequenceEnd


/******************************************************************************/
public void setSequenceName ( String name )
{
  sequence_name = name;
}  // method setSequenceName


/******************************************************************************/
public void setSequenceSegment ( String sequence )
{
  sequence_segment = sequence;
}  // method setSequenceSegment


/******************************************************************************/
public void setSequenceStart ( int start )
{
  sequence_start = start;
}  // method setSequenceStart


/******************************************************************************/
public void setSequenceStrand ( char strand )
{
  sequence_strand = strand;
}  // method setSequenceStrand


/******************************************************************************/

}  // class Segment
