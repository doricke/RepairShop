

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

public class AlignmentSequence extends Object
{

/******************************************************************************/
// Variables:

private int alignment_end = 0;		// AlignmentSequence.alignment_end

private int alignment_id = 0;		// AlignmentSequence.alignment_id

private int alignment_start = 0;	// AlignmentSequence.alignment_start

private int sequence_end = 0;		// AlignmentSequence.sequence_end

private int sequence_id = 0;		// AlignmentSequence.sequence_id

private String sequence_name = null;	// Sequence.sequence_name

private int sequence_start = 0;		// AlignmentSequence.sequence_start

private String sequence_strand = null;	// AlignmentSequence.sequence_strand


/******************************************************************************/
  public int getAlignmentEnd ()
  {
    return alignment_end;
  }  // method getAlignmentEnd


/******************************************************************************/
  public int getAlignmentId ()
  {
    return alignment_id;
  }  // method getAlignmentId


/******************************************************************************/
  public int getAlignmentStart ()
  {
    return alignment_start;
  }  // method getAlignmentStart


/******************************************************************************/
  public int getSequenceEnd ()
  {
    return sequence_end;
  }  // method getSequenceEnd


/******************************************************************************/
  public int getSequenceId ()
  {
    return sequence_id;
  }  // method getSequenceId


/******************************************************************************/
  public String getSequenceName ()
  {
    return sequence_name;
  }  // method getSequenceName


/******************************************************************************/
  public int getSequenceStart ()
  {
    return sequence_start;
  }  // method getSequenceStart


/******************************************************************************/
  public String getSequenceStrand ()
  {
    return sequence_strand;
  }  // method getSequenceStrand


/******************************************************************************/
  public void setAlignmentId ( int alignmentId )
  {
    alignment_id = alignmentId;
  }  // method setAlignmentId


/******************************************************************************/
  public void setAlignmentEnd ( int alignmentEnd )
  {
    alignment_end = alignmentEnd;
  }  // method setAlignmentEnd
 

/******************************************************************************/
  public void setAlignmentStart ( int alignmentStart )
  {
    alignment_start = alignmentStart;
  }  // method setAlignmentStart


/******************************************************************************/
  public void setSequenceEnd ( int sequenceEnd )
  {
    sequence_end = sequenceEnd;
  }  // method setSequenceEnd


/******************************************************************************/
  public void setSequenceId ( int sequenceId )
  {
    sequence_id = sequenceId;
  }  // method setSequenceId


/******************************************************************************/
  public void setSequenceName ( String sequenceName )
  {
    sequence_name = sequenceName;
  }  // method setSequenceName


/******************************************************************************/
  public void setSequenceStart ( int sequenceStart )
  {
    sequence_start = sequenceStart;
  }  // method setSequenceStart


/******************************************************************************/
  public void setSequenceStrand ( String sequenceStrand )
  {
    sequence_strand = sequenceStrand;
  }  // method setSequenceStrand


/******************************************************************************/
  // This method prints out the variables.
  public void print_out ()
  {
    System.out.print   ( "\tali_id " + alignment_id );
    System.out.print   ( ", seq_id " + sequence_id );
    System.out.print   ( "["         + sequence_start );
    System.out.print   ( ","         + sequence_end );
    System.out.print   ( "] X ["     + alignment_start );
    System.out.print   ( ","         + alignment_end );
    System.out.print   ( "] strand " + sequence_strand );
    System.out.println ( " "         + sequence_name );

  }  // method print_out


/******************************************************************************/

}  // class AlignmentSequence
