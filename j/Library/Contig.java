import java.awt.*;
import ParseName.*;

/******************************************************************************/
/* Object representing an alignment of individual sequences with a consensus sequence. */
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

public class Contig extends Object
{

/******************************************************************************/
/* Name of the consensus sequence. */
protected String name;				// Consensus sequence name

/* Strand character representation of DNA consensus sequence. */
protected String consensus;			// Consensus sequence or alignment

/* Quality positions for each consensus base. */
protected byte [] quality;			// Consensus quality array

protected int numberSequences;			// Number of sequences in alignment

/* Strand coverage for each position in consensus sequence. */
/* 0 = not set yet; 1 = forward strand; 2 = reverse strand; 3 = both strands */
protected byte [] strands;			// Strand coverage

/* Coverage of consensus sequence by dye primer sequencing chemistries. */
protected byte [] primer;			// Dye primer chemistry coverage

/* Coverage of consensus sequence by dye terminator sequencing chemistries. */
protected byte [] terminator;

/* Coverage of forward strand - best Phred quality number for this strand. */
protected byte [] forward;

/* Coverage of reverse strand - best Phred quality number for reverse strand. */
protected byte [] reverse;

/* Current position within consensus sequence. */
protected int currentPosition;			// Current index position

/* Alignment of forward strand sequences. */
protected TextArea forwardAlignment;

/* Alignment of reverse strand sequences. */
protected TextArea reverseAlignment;

/* Alignment of both forward and reverse strand sequences. */
protected TextArea alignment;


/******************************************************************************/
/* Initialize the Contig object. */
public Contig ()
{
  consensus   = null;
  name        = null;
  numberSequences = 0;
  currentPosition = 0;
}  /* constructor Contig */


/******************************************************************************/
/* Set the consensus DNA alignment sequence and initialize related Object arrays. */
public void setConsensus ( String cons )
{
  int index;					// array index

  // Save the consensus sequence.
  consensus = cons;

  // Initialize arrays. */
  quality = new byte [ cons.length () ];
  strands = new byte [ cons.length () ];
  forward = new byte [ cons.length () ];
  reverse = new byte [ cons.length () ];
  primer  = new byte [ cons.length () ];
  terminator = new byte [ cons.length () ];

  // Initialize string buffer array to '0'.
  for ( index = 0; index < cons.length (); index++ )
  {
    quality [ index ] = 0;
    strands [ index ] = 0;
    forward [ index ] = 0;
    reverse [ index ] = 0;
    primer  [ index ] = 0;
    terminator [ index ] = 0;
  }  /* for */

}  /* method setConsensus */


/******************************************************************************/
/* Add a sequence to the alignment for the consensus sequence (count only). */
public void addSequence ( )
{
  numberSequences++;
}  /* method add_Sequence */


/******************************************************************************/
/* Return the consensus sequence. */
public String getConsensus ( )
{
  return consensus;
}  /* method getConsensus */


/******************************************************************************/
/* Return the quality array. */
public byte [] getQuality ( )
{
  return quality;
}  /* method getQuality */


/******************************************************************************/
/* Return the number of sequences in the alignment for the consensus sequence. */
public int getNumberSequences ( )
{
  return numberSequences;
}  /* method getNumberSequences */


/******************************************************************************/
/* Return the strands array. */
public byte [] getStrands ( )
{
  return strands;
}  /* method getStands */


/******************************************************************************/
public TextArea getForwardAlignment ( )
{
  return forwardAlignment;
}  /* method getForwardAlignment */


/******************************************************************************/
public TextArea getReverseAlignment ( )
{
  return reverseAlignment;
}  /* method getReverseAlignment */


/******************************************************************************/
public TextArea getAlignment ( )
{
  return alignment;
}  /* method getAlignment */

/******************************************************************************/
/* Get the consensus sequence quality at this position. */
public int qualityAt ( int position )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return 0;

  // Range check position
  if ( checkPosition ( position ) == false )  return 0;

  return (int) quality [ position ];
}  /* method qualityAt */


/******************************************************************************/
/* Get the number of strands at the consensus position. */
public int strandsAt ( int position )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return 0;

  // Range check position
  if ( checkPosition ( position ) == false )  return 0;

  return (int) strands [ position ];
}  /* method StrandsAt */

 
/******************************************************************************/
/* Get the best Phred quality at the forward strand position. */
public int forwardAt ( int position )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return 0;

  // Range check position
  if ( checkPosition ( position ) == false )  return 0;

  return (int) forward [ position ];
}  /* method forwardAt*/

 
/******************************************************************************/
/* Get the best Phred quality at the reverse strand position. */
public int reverseAt ( int position )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return 0;

  // Range check position
  if ( checkPosition ( position ) == false )  return 0;

  return (int) reverse [ position ];
}  /* method reverseAt*/

 
/******************************************************************************/
/* Get the number of chemistries at a position. */
public int chemistriesAt ( int position )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return 0;

  // Range check position
  if ( checkPosition ( position ) == false )  return 0;

  int total = 0;
  if ( primer [ position ] > 0 )  total++;
  if ( terminator [ position ] > 0 )  total++;
  return total;
}  /* method chemistriesAt */


/******************************************************************************/
/* Set the quality at the position. */
public void setQualityAt ( int position, int qual )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return;

  // Range check position
  if ( checkPosition ( position ) == false )  return;


  // Range check qual.
  if ( checkRange ( qual, 0, 100, "*Warning* from setQualityAt; quality" ) == false )  return;

  quality [ position ] = (byte) qual;
}  /* method setQualityAt */


/******************************************************************************/
public void setNextQuality ( int qual )
{
  // Range check qual.
  if ( checkRange ( qual, 0, 100, "*Warning* from setNextQuality; quality" ) == false )  return;

  quality [ currentPosition ] = (byte) qual;
  currentPosition++;
}  /* method setNextQuality */


/******************************************************************************/
/* Check if the consensus sequence has been set yet. */
protected boolean checkConsensusSet ( )
{
  if ( consensus == null )  return false;

  if ( consensus.length () <= 0 )  return false;

  return true;
}  /* method checkConsensusSet */


/******************************************************************************/
/* Range check consensus position index. */
protected boolean checkPosition ( int position )
{
  // Check that the consensus sequence has been set.
  if ( checkConsensusSet () == false )  return false;

  // Range check position
  return checkRange ( position, 0, consensus.length () - 1, "*Warning* from checkPosition; position " );
}  /* method checkPosition */


/******************************************************************************/
/* Range check an integer value. */
protected boolean checkRange ( int val, int lower, int upper, String message )
{
  // Range check value.
  if ( ( val < lower ) || ( val > upper ) )
  {
    System.out.println ( message + ": value = " + val + 
        " out of range [" + lower + ", " + upper + "] in object Contig." );
    return false;
  }  /* if */

  return true;
}  /* method checkRange */


/******************************************************************************/
/* This method spans a sequence for strand and chemistry coverage. */
protected void spanSequence ( int start, int end, String sequenceName )
// start = starting position in consensus sequence of this sequence.
// end   = ending position in consensus sequence of this sequence.
{
  // Range check start.
  if ( checkRange ( start, 0, end, "*Warning* from spanSequence; start" ) == false )
    return;

  // Range check end.
  if ( checkRange ( end, start, consensus.length () - 1, "*Warning* from spanSequence; start" ) == false )
    return;

  // Parse the Phred/Phrap name into component parts.
  ParseName nameparse = new ParseName ( sequenceName );

  // Parse the sequence name for direction and chemistry.
  boolean forward_direction = nameparse.isForward ( );

  boolean is_primer = nameparse.isPrimer ( );

  // Increment the number of sequences in the alignment. ???
  addSequence ();

  // Set the consensus positions for both strand and chemistry.
  for ( int index = start; index <= end; index++ )
  {
    // Increment the direction count for this position.
    if ( forward_direction == true )
      forward [ index ]++;
    else
      reverse [ index ]++;

    // Increment the chemistry count for this position.
    if ( is_primer == true )
      primer [ index ]++;
    else
      terminator [ index ]++;
  }  /* for */
}  /* method spanSequence */


/******************************************************************************/
/* This method adds the sequence data to the text area views.
public void addSequenceData ( int start, String name, String sequence )
{
  // Range check start.
  if ( checkRange ( start, 0, consensus.length () - 1, "*Warning* from addSequenceData; start" ) == false )
    return;

  // Range check computed end.
  int end = start + sequence.length ();
  if ( checkRange ( end, start, consensus.length () - 1, "*Warning* from addSequenceData; start" ) == false )
    return;

  // Add sequence to text areas.
}  /* method addSequenceData */


/******************************************************************************/
}  /* class Contig */
