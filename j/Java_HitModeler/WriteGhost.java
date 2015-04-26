
import java.util.Vector;

// import Elements;
// import OutputTools;
// import SeqTools;
// import SequenceTable;
// import Transcript;

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

public class WriteGhost extends Object
{

/******************************************************************************/


/******************************************************************************/
  // Constructor WriteGhost
  public WriteGhost ()
  {
    initialize ();
  }  // constructor WriteGhost


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
  }  // method close


/******************************************************************************/
  private void writeTranslation 
      ( OutputTools  file
      , Transcript   transcript 
      )
  {
    SequenceTable protein_sequence = transcript.getProteinSequence ();
    String protein = null;
    if ( protein_sequence != null )  protein = protein_sequence.getSequence ();
    if ( protein == null )  return;
    if ( protein.length () < 30 )  return;

    file.print ( ">" + transcript.getName () );

    // Check for a complete transcript.
    if ( transcript.isComplete () == true )
      file.print ( " protein[Complete " );
    else
      file.print ( " protein[Partial " );

    file.print ( transcript.getStrand () + " " );
    file.print ( transcript.getGenomicBegin () );
    file.print ( "-" );
    file.print ( transcript.getGenomicEnd () ); 
    file.print ( "]" );

    if ( transcript.getSpliceInfo ().length () > 0 )
      file.print ( " Exons[" + transcript.getSpliceInfo () + "]" );

    file.print ( " " + transcript.getDescription () );

    file.println ();

    SeqTools.writeFasta ( file, protein );
  }  // method writeTranslation


/******************************************************************************/
  private void writeTranscript 
      ( OutputTools  file
      , Transcript   transcript 
      )
  {
    String mRNA_sequence = transcript.getMrnaSequence ();
    if ( mRNA_sequence == null )  return;
    // if ( mRNA_sequence.length () < 80 )  return;

    if ( mRNA_sequence.length () > 0 )
    {
      file.print ( ">" );
      file.print ( transcript.getName () + ".mrna" );

      // Check for a complete transcript.
      if ( transcript.isComplete () == true )
        file.print ( " protein[Complete " );
      else
        file.print ( " protein[Partial " );

      if ( transcript.getSpliceInfo ().length () > 0 )
        file.print ( " Exons[" + transcript.getSpliceInfo () + "]" );
  
      file.print ( transcript.getStrand () + " " );
      file.print ( transcript.getGenomicBegin () );
      file.print ( "-" );
      file.print ( transcript.getGenomicEnd () ); 
      file.print ( "]" );

      Elements elements = transcript.getElements ();
      if ( elements != null )
      {
        if ( elements.getDetails ().length () > 0 )
          file.print ( " " + elements.getDetails () );
      }  // if

      file.print ( " " + transcript.getDescription () );

      file.println ();
      SeqTools.writeFasta ( file, mRNA_sequence );
    }  // if
  }  // method writeTranscript


/******************************************************************************/

}  // class WriteGhost
