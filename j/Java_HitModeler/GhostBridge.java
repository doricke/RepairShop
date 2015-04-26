
import java.util.Vector;

// import GeneSpan;
// import Ghost;

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

public class GhostBridge extends Object
{

/******************************************************************************/

private  String  file_name_prefix = "";				// file name prefix

private  Ghost ghost = new Ghost ();				// Ghost modeler

private  OutputTools  protein_file = new OutputTools ();	// Translations output file


/******************************************************************************/
public GhostBridge ()
{
  initialize ();
}  // constructor GhostBridge


/******************************************************************************/
private void initialize ()
{
  file_name_prefix = "";
  ghost.initialize ();
}  // method initialize


/******************************************************************************/
  public void setFileNamePrefix ( String name )
  {
    file_name_prefix = name;
  }  // method setFileName


/******************************************************************************/
  public void openFiles ()
  {
    protein_file.initialize ();
    protein_file.setFileName ( file_name_prefix + ".aas" );
    protein_file.openFile ();
  }  // method openFiles


/******************************************************************************/
  public void closeFiles ()
  {
    protein_file.closeFile ();
  }  // method closeFiles


/******************************************************************************/
private Vector selectHits ( Vector hits )
{
  GeneSpan gene_span = new GeneSpan ();

  // Validate hits.
  if ( ( hits == null ) || ( hits.size () <= 0 ) )
    return hits;

  int h = 0;
  while ( h < hits.size () )
  {
    Hit hit = (Hit) hits.elementAt ( h );

    // Check if the genomic span is too large.
    if ( gene_span.inRange ( hit ) == true )
    {
      if ( gene_span.canAddHit ( hit ) == true )
      {
        hit.setSelected ( true );
      }  // if
    }  // if
    else 
      h = hits.size ();	// terminate while loop

    h++;
  }  // while

  return hits;
}  // method selectHits


/******************************************************************************/
private void model ( Vector hits )
{
  // Validate parameters.
  if ( hits == null )  return;

  // Check for nothing to add.
  if ( hits.size () <= 0 )  return;

  int length = 0;
  while ( hits.size () > 0 )
  {
    // Sum the length of the hits, discard if fairly small.
    length = 0;

    // Shift the selected hits to new_hits. 
    hits = selectHits ( hits );

    Vector new_hits  = new Vector ();
    for ( int e = hits.size () - 1; e >= 0; e-- )
    {
      Hit hit = (Hit) hits.elementAt ( e );
      if ( hit.isSelected () == true )
      {
        hits.removeElementAt ( e );
        new_hits.add ( hit );
        // hit.print ();
        length += hit.getTargetEnd () - hit.getTargetStart () + 1;
      }  // if
    }  // for

    // Check that a new hit was seen & the transcript has some size to it. 
    if ( length >= 150 )

      modelHits ( new_hits );
  }  // while
}  // method model


/******************************************************************************/
  public void modelHits ( Vector v_hits )
  {
    // System.out.println ( "GhostBridge called with " + v_hits.size () + " hits." );

    Hit hits [] = new Hit [ v_hits.size () ];
    for ( int i = 0; i < v_hits.size (); i++ )
    {
      hits [ i ] = (Hit) v_hits.elementAt ( i );
      // System.out.println ( hits [ i ].toResult () );
    }  // for

    ghost.model ( hits );
  }  // method modelHits


/******************************************************************************/

}  // class GhostBridge

