
// import Html;

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

public class Peptide extends Object
{


/******************************************************************************/

  private   int [] identity = null;		// Protein identities for each residue

  private   String peptide = "";		// Protein peptide sequence

  private   int start = 0;			// Peptide start position in protein


/******************************************************************************/
  // Constructor Peptide
  public Peptide ()
  {
    initialize ();
  }  // constructor Peptide


/******************************************************************************/
  // Constructor Peptide
  public Peptide ( String pep, int start_pos )
  {
    initialize ();
    setPeptide ( pep );
    setStart ( start_pos );
  }  // constructor Peptide


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    identity = null;
    peptide = "";
    start = 0;
  }  // method initialize 


/******************************************************************************/
  public int [] getIdentity ()
  {
    return identity;
  }  // method getIdentity


/******************************************************************************/
  public int getLength ()
  {
    return peptide.length ();
  }  // method getLength


/******************************************************************************/
  public String getPeptide ()
  {
    return peptide;
  }  // method getPeptide


/******************************************************************************/
  public int getStart ()
  {
    return start;
  }  // method getStart


/******************************************************************************/
  public void setIdentity ( int [] value )
  {
    identity = value;
  }  // method setIdentity


/******************************************************************************/
  public void setIdentity ( int residue, int value )
  {
    if ( ( residue >= 0 ) && ( residue < peptide.length () ) )

      identity [ residue ] = value;

    else

      System.out.println ( "*Warning* Peptide.setIdentity invalid residue position: "
          + residue + ", set to " + value );

  }  // method setIdentity


/******************************************************************************/
  public void setPeptide ( String value )
  {
    peptide = value;

    // Allocate the identity array.
    if ( peptide.length () > 0 )

      identity = new int [ peptide.length () ];
  }  // method setPeptide


/******************************************************************************/
  public void setStart ( int value )
  {
    start = value;
  }  // method setStart


/******************************************************************************/
  public String colorPeptide ()
  {
    StringBuffer pep = new StringBuffer ( peptide.length () * 2 );

    for ( int i = 0; i < peptide.length(); i++ )
    {
      if ( identity [ i ] > 20 )

        pep.append ( peptide.charAt ( i ) );

      else
      {
        pep.append ( Html.fontColor ( Html.red ) );
        pep.append ( peptide.charAt ( i ) );
        pep.append ( Html.htmlEndTag ( Html.font ) );
      }  // else
    }  // for

    return pep.toString ();
  }  // method colorPeptide


/******************************************************************************/
  public String toHtml ()
  {
    return (start+1) + "  " 
        + colorPeptide () + "  " 
        + (start+peptide.length ());
  }  // method toHtml


/******************************************************************************/
  public String toString ()
  {
    return (start+1) + "  " 
        + peptide + "  " 
        + (start+peptide.length ());
  }  // method toString


/******************************************************************************/
  public static void main ( String [] args )
  {
    String seq = "ACDEFGHIKLMNPQRSTV";
    Peptide app = new Peptide ( seq, 1 );

    System.out.println ( app.toString () );

    for ( int i = 0; i < seq.length (); i++ )

      app.setIdentity ( i, i + 14 );

    System.out.println ( app.colorPeptide () );
  }  // method main


/******************************************************************************/

}  // class Peptide
