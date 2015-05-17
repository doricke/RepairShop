

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

public class SequenceGap extends Object
{

/******************************************************************************/
// Variables:

private int alignment_id = 0;		// Source Alignment.alignment_id

private int alignment_id_ext = 0;	// External Alignment.alignment_id

private int number_of_conflicts = 0;	// SequenceGap.num_conflicts

private int number_of_spans = 0;	// SequenceGap.num_spans

private String sequence_end = null;	// SequenceGapOrder.sequence_end - Source end

private String sequence_end_ext = null;	// SequenceGapOrder.sequence_end - External end

private int sequence_gap_error = 0;	// SequenceGap.sequence_gap_error

private int sequence_gap_size = 0;	// SequenceGap.sequence_gap_size

private int sequence_id = 0;		// SequenceGapOrder.sequence_id - Source alignment

private int sequence_id_ext = 0;	// SequenceGapOrder.sequence_id - External alignment


/******************************************************************************/
  public int getAlignmentId ()
  {
    return alignment_id;
  }  // method getAlignmentId


/******************************************************************************/
  public int getAlignmentIdExt ()
  {
    return alignment_id_ext;
  }  // method getAlignmentIdExt


/******************************************************************************/
  public int getNumberOfConflicts ()
  {
    return number_of_conflicts;
  }  // method getNumberOfConflicts


/******************************************************************************/
  public int getNumberOfSpans ()
  {
    return number_of_spans;
  }  // method getNumberOfSpans


/******************************************************************************/
  public String getSequenceEnd ()
  {
    return sequence_end;
  }  // method getSequenceEnd


/******************************************************************************/
  public String getSequenceEndExt ()
  {
    return sequence_end_ext;
  }  // method getSequenceEndExt


/******************************************************************************/
  public int getSequenceGapError ()
  {
    return sequence_gap_error;
  }  // method sequence_gap_error


/******************************************************************************/
  public int getSequenceGapSize ()
  {
    return sequence_gap_size;
  }  // method getSequenceGapSize


/******************************************************************************/
  public int getSequenceId ()
  {
    return sequence_id;
  }  // method getSequenceId


/******************************************************************************/
  public int getSequenceIdExt ()
  {
    return sequence_id_ext;
  }  // method getSequenceIdExt


/******************************************************************************/
  public void setAlignmentId ( int alignmentId )
  {
    alignment_id = alignmentId;
  }  // method setAlignmentId


/******************************************************************************/
  public void setAlignmentIdExt ( int alignmentId )
  {
    alignment_id_ext = alignmentId;
  }  // method setAlignmentIdExt


/******************************************************************************/
  public void setNumberOfConflicts ( int conflicts )
  {
    number_of_conflicts = conflicts;
  }  // method setNumberOfConflicts


/******************************************************************************/
  public void setNumberOfSpans ( int spans )
  {
    number_of_spans = spans;
  }  // method setNumberOfSpans
 

/******************************************************************************/
  public void setSequenceEnd ( String sequenceEnd )
  {
    sequence_end = sequenceEnd;
  }  // method setSequenceEnd
 

/******************************************************************************/
  public void setSequenceEndExt ( String sequenceEndExt )
  {
    sequence_end_ext = sequenceEndExt;
  }  // method setSequenceEndExt
 

/******************************************************************************/
  public void setSequenceGapError ( int gap_error )
  {
    sequence_gap_error = gap_error;
  }  // method setSequenceGapError
 

/******************************************************************************/
  public void setSequenceGapSize ( int gap_size )
  {
    sequence_gap_size = gap_size;
  }  // method setSequenceGapSize


/******************************************************************************/
  public void setSequenceId ( int sequenceId )
  {
    sequence_id = sequenceId;
  }  // method setSequenceId


/******************************************************************************/
  public void setSequenceIdExt ( int sequenceId )
  {
    sequence_id_ext = sequenceId;
  }  // method setSequenceIdExt


/******************************************************************************/
  // This method prints out the variables.
  public void print_out ()
  {
    System.out.print   ( "\tali_id " + alignment_id );
    System.out.print   (               sequence_end );
    System.out.print   ( ", ali_id " + alignment_id_ext );
    System.out.print   (               sequence_end_ext );
    System.out.print   ( " ("        + sequence_gap_size );
    System.out.print   ( " +- "      + sequence_gap_error);
    System.out.print   ( ") {"       + number_of_spans );
    System.out.print   ( ","         + number_of_conflicts );
    System.out.println ( "}" );
  }  // method print_out


/******************************************************************************/

}  // class SequenceGap
