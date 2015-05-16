
// import KeggEntry;

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

public class KeggIterator extends InputFile
{


/******************************************************************************/

  KeggEntry kegg_entry = null;		// Current KEGG entry 


/******************************************************************************/
public KeggIterator ()
{
  initialize ();
}  // constructor KeggIterator


/******************************************************************************/
public KeggIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method KeggIterator


/*******************************************************************************/
  private String getLineHeader ()
  {
    String kegg_line = line.toString ();

    if ( kegg_line.startsWith ( "AASEQ" ) == true )  return "AASEQ";
    if ( kegg_line.startsWith ( "CODON_USAGE" ) == true )  return "CODON_USAGE";
    if ( kegg_line.startsWith ( "DBLINKS" ) == true )  return "DBLINKS";
    if ( kegg_line.startsWith ( "DEFINITION" ) == true )  return "DEFINITION";
    if ( kegg_line.startsWith ( "ENTRY" ) == true )  return "ENTRY";
    if ( kegg_line.startsWith ( "MOTIF" ) == true )  return "MOTIF";
    if ( kegg_line.startsWith ( "NAME" ) == true )  return "NAME";
    if ( kegg_line.startsWith ( "NTSEQ" ) == true )  return "NTSEQ";
    if ( kegg_line.startsWith ( "ORTHOLOG" ) == true )  return "ORTHOLOG";
    if ( kegg_line.startsWith ( "PATHWAY" ) == true )  return "PATHWAY";
    if ( kegg_line.startsWith ( "POSITION" ) == true )  return "POSITION";
    if ( kegg_line.startsWith ( "///" ) == true )  return "///";

    return kegg_line.substring ( 0, 11 ).trim ();
  }  // method getLineHeader


/*******************************************************************************/
  private void readEntry ()
  {
    String header = "";				// KEGG line header

    kegg_entry = new KeggEntry ();		// create a new object

    while ( ( header.equals ( "///" ) == false ) &&
            ( isEndOfFile () == false ) )
    {
      nextLine ();				// get the next KEGG line

      // Check for a new header.
      if ( line.length () > 0 )
        if ( line.charAt ( 0 ) != ' ' )
          header = getLineHeader ();

      String text = "";
      if ( line.length () > 12 )
      {
        text = line.substring ( 12 );

        if ( header.equals ( "AASEQ" ) == true )  kegg_entry.setAaSequence ( text );
        if ( header.equals ( "CODON_USAGE" ) == true )  kegg_entry.setCodonUsage ( text );
        if ( header.equals ( "DBLINKS" ) == true )  kegg_entry.setDblinks( text );
        if ( header.equals ( "DEFINITION" ) == true )  kegg_entry.setDefinition ( text );
        if ( header.equals ( "ENTRY" ) == true )  kegg_entry.setEntry ( text );
        if ( header.equals ( "MOTIF" ) == true )  kegg_entry.setMotif( text );
        if ( header.equals ( "NAME" ) == true )  kegg_entry.setName( text );
        if ( header.equals ( "NTSEQ" ) == true )  kegg_entry.setNtSequence ( text );
        if ( header.equals ( "PATHWAY" ) == true )  kegg_entry.setPathway ( text );
        if ( header.equals ( "POSITION" ) == true )  kegg_entry.setPosition ( text );
        if ( header.equals ( "ORTHOLOG" ) == true )  kegg_entry.setOrtholog ( text );
      }  // if

      // System.out.println ( header + ":\t" + text );
    }  // while

    if ( kegg_entry.getName ().length () > 0 )
      System.out.println ( kegg_entry.toString () );
  }  // method readEntry


/*******************************************************************************/
  public KeggEntry next ()
  {
    // Check for end of file.
    if ( isEndOfFile () == true )  return null;

    // Read in the sequence.
    readEntry ();

    return kegg_entry;
  }  // method next


/******************************************************************************/
  public static void main ( String [] args )
  {
    KeggIterator app = new KeggIterator ();

    // Open the KEGG test file.
    if ( args.length != 1 )
      // app.setFileName ( "kegg_h.sapiens" );
      app.setFileName ( "kegg_m.musculus" );
    else
      app.setFileName ( args [ 0 ] );
    app.openFile ();

    // Parse the test entries.
    while ( app.isEndOfFile () == false )

      app.next ();

    app.closeFile ();		// Close the test file
  }  // method main


/******************************************************************************/

}  // class KeggIterator

