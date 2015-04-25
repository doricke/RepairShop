
import java.util.Vector;
// import Peptide;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

public class Digest extends Object
{


/******************************************************************************/

  private   int [] identity = null;			// Percent identity matches

  private   Vector  peptides = new Vector ();		// Digested peptides

  private   String sequence = "";			// Protein sequence

  private   int  total = 0;				// number of peptides


/******************************************************************************/
  // Constructor Digest
  public Digest ()
  {
    initialize ();
  }  // constructor Digest


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    identity = null;
    peptides.removeAllElements ();
    sequence = "";
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public int [] getIdentity ()
  {
    return identity;
  }  // method getIdentity


/******************************************************************************/
  public int getIdentity ( int index )
  {
    if ( identity == null )  return -1;
    if ( ( index < 0 ) || ( index >= sequence.length () ) )  return -1;

    return identity [ index ];
  }  // method getIdentity


/******************************************************************************/
  public Vector getPeptides ()
  {
    return peptides;
  }  // method getPeptides


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public void setIdentity ( int [] value )
  {
    identity = value;
  }  // method setIdentity


/******************************************************************************/
  public void setIdentity ( int residue, int value )
  {
    if ( ( residue >= 0 ) && ( residue < sequence.length () ) )

      identity [ residue ] = value;

    else

      System.out.println ( "*Warning* Digest.setIdentity invalid residue " 
          + residue + " with value " + value );
  }  // method setIdentity


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;

    if ( value.length () > 0 )
    {
      identity = new int [ value.length () ];

      for ( int i = 0; i < value.length (); i++ )

        identity [ i ] = 0;
    }  // if
  }  // method setSequence


/******************************************************************************/
  public void setTotal ( int value )
  {
    total = value;
  }  // method setTotal


/******************************************************************************/
  private int trypsinSite ( int start )
  {
    char amino = ' ';
    for ( int i = start; i < sequence.length (); i++ )
    {
      amino = sequence.charAt ( i );
      if ( ( amino == 'R' ) || ( amino == 'r' ) || 
           ( amino == 'K' ) || ( amino == 'k' ) )

        return i;
    }  // for

    return sequence.length () - 1;		// Last residue
  }  // method trypsinSite


/******************************************************************************/
  public void digestSequence ()
  {
    int start = 0;
    int clip = trypsinSite ( start );
    System.out.println ( "\nDigest\n" );

    while ( start < sequence.length () )
    {
      Peptide pep = new Peptide ( sequence.substring ( start, clip + 1 ), start );
      for ( int j = start; j <= clip; j++ )  
        pep.setIdentity ( j - start, identity [ j ] );
      peptides.add ( pep );
      total++;

      System.out.println ( pep.toHtml () );

      // Advance to the next peptide
      start = clip + 1;
      clip = trypsinSite ( start );
    }  // while
  }  // method digestSequence


/******************************************************************************/
  public void colorSequence ()
  {
    System.out.println ( "\nSequence\n" );
    Peptide pep = new Peptide ( sequence, 0 );
    pep.setIdentity ( identity );
    System.out.println ( pep.toHtml () );
  }  // method colorSequence


/******************************************************************************/
  public static void main ( String [] args )
  {
    Digest app = new Digest ();
    String seq = "ILVKSTAGMRFPYNQ";
    app.setSequence ( seq );
    for ( int i = 0; i < seq.length (); i++ )
      app.setIdentity ( i, i + 15 );
    app.digestSequence ();
  }  // method main


/******************************************************************************/

}  // class Digest
