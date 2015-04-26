
// import Evidence;
// import ExonTable;
// import FeatureTable;
// import GeneTable;
// import Genomic;
// import HitRegion;
// import InputTools;
// import GenePrediction;
// import Piece;
// import Puzzle;
// import SequenceTable;
// import Hit;
// import Transcript;
// import WriteGhost;

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

public class Ghost extends Object
{

/******************************************************************************/
public Ghost ()
{
  initialize ();
}  // constructor Ghost


/******************************************************************************/
public void initialize ()
{
}  // method initialize


/******************************************************************************/
private HitRegion setupRegion ( Puzzle puzzle, Piece [] pieces )
{
  HitRegion region = new HitRegion ();

  // Validate pieces.
  if ( ( pieces == null ) || ( pieces.length <= 0 ) )  return region;

  // Set the coordinate limits of the DNA and protein hits.
  region.computeHitRegion ( pieces );

  region.setGenomicLength ( pieces [ 0 ].getGenomicSeq ().getSequence ().length () );

  return region;
}  // method setupRegion


/******************************************************************************/
// This method sets up a new puzzle piece from a similarity hit.
public Piece newPiece ( SequenceTable contig, Genomic genomic, Hit hit )
{
  Piece piece = new Piece ();

  piece.setGenomic ( genomic );
  piece.setGenomicSeq ( contig );
  piece.setTargetSequenceName ( hit.getQueryName () );
  piece.setGenomicSequenceId ( hit.getQuerySequenceId () );
  piece.setSimilarity ( hit );
  piece.setPieceType ( hit.getProgramName () );

  piece.setGenomicBegin ( (int) hit.getTargetStart () );
  piece.setGenomicEnd ( (int) hit.getTargetEnd () );

  piece.setTargetBegin ( (int) hit.getQueryStart () );
  piece.setTargetEnd   ( (int) hit.getQueryEnd () );

  if ( ( hit.getProgramName ().equals ( "TBLASTN" ) == true ) ||
       ( hit.getProgramName ().equals ( "BLASTX" ) == true  ) )
  {
    piece.setGenomicAlignment ( hit.getTargetAlignment () );
    piece.setTargetAlignment ( hit.getQueryAlignment () );
  }  // if

  piece.setGenomicStrand ();

  return piece;
}  // method newPiece


/******************************************************************************/
public Piece [] setupPieces 
    ( SequenceTable  contig
    , Genomic        genomic
    , Hit []         hits 
    )
{
  // Check for similarities.
  if ( ( hits == null ) || ( hits.length <= 0 ) )  return null;

  // Set up the pieces.
  Piece pieces [] = new Piece [ hits.length ];

  for ( int s = 0; s < hits.length; s++ )

    pieces [ s ] = newPiece ( contig, genomic, hits [ s ] );

  for ( int s = 0; s < hits.length; s++ )
  {
    // Evaluate the piece for Open Reading Frames.
    pieces [ s ].evaluateFrames ();
  }  // for

  return pieces;
}  // method setupPieces


/******************************************************************************/
private Transcript setupPuzzle ( Hit [] similarities, SequenceTable contig, Genomic genomic )
{
  Transcript model = null;

    // Check for similarities.
    if ( ( similarities != null ) && ( similarities.length > 0 ) )
    {
      System.out.println ();
      System.out.print   ( "----------------------------------------" );
      System.out.println ( "----------------------------------------" );
      System.out.println ( "New Puzzle for transcript" );

      Puzzle puzzle = new Puzzle ();

      puzzle.setPieces ( setupPieces ( contig, genomic, similarities ) );

      // Check if a repetitive element has erased this puzzle.
      if ( puzzle != null )
      {
        // Model the similarities into a transcript.
        model = puzzle.model ();
  
        System.out.println ();
        System.out.println ( "setupPuzzle: modeled transcript:" );
        model.sumEvidence ();
        model.print ();

        puzzle.close ();
      }  // if
    }  // if

  return model;
}  // method setupPuzzle


/******************************************************************************/
private SequenceTable getGenomic ( String file_name )
{
  SequenceTable genomic = new SequenceTable ();

  Sequence dna = new Sequence ();
  dna.setFileName ( "genomic/" + file_name );
  dna.setSequenceType ( Sequence.DNA );
  dna.openFile ();
  dna.readSequence ();

  // System.out.println ( "Sequence name = " + dna.getSequenceName () );
  // System.out.println ( "Sequence = " + dna.getSequence () );

  genomic.setSequence ( dna.getSequence () );
  genomic.setSequenceLength ( (long) dna.getSequenceLength () );
  genomic.setSequenceName ( dna.getSequenceName () );
  genomic.setSequenceDescription ( dna.getSequenceDescription () );

  dna.closeFile ();
  dna = null;

  return genomic;
}  // method getGenomic


/******************************************************************************/
public Transcript model ( Hit [] hits )
{
  initialize ();

  // Assert: hits to process.
  if ( hits == null )  return null;


  if ( hits.length <= 0 )  return null;

  // Validate the contig name.
  SequenceTable contig = getGenomic ( hits [ 0 ].getTargetName () );

System.out.println ( "genomic sequence length = " + contig.getSequenceLength () );

  // Check for no genomic sequence.
  if ( contig.getSequenceLength () <= 0 )  return null;

  Genomic genomic = new Genomic ();
  genomic.setSequence ( contig.getSequence () );

  // Setup puzzle for similarity hits.
  Transcript transcript = setupPuzzle ( hits, contig, genomic );

  // Write the transcripts to the output files.
  // write_shadows.writeTranscripts ( transcript );

  return transcript;
}  // method model


/******************************************************************************/

}  //class Ghost
