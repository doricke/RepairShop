
import java.util.Vector;

// import GenBankFeature;
// import GenBankHeader;
// import InputTools;
// import OutputTools;
// import SeqTools;
// import Species;

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

public class GenBankSlice extends Object
{


/******************************************************************************/

  private OutputTools species_seq_files [] = new OutputTools [ Species.species.length ];

  private OutputTools species_genomic_files [] = new OutputTools [ Species.species.length ];


/******************************************************************************/
  public GenBankSlice ()
  {
    initialize ();
  }  // constructor GenBankSlice


/******************************************************************************/
  public void initialize ()
  {
  }  // method initialize


/******************************************************************************/
  public String makeFileName ( String name, String suffix )
  {
    return name + "." + suffix;
  }  // method makeFileName


/******************************************************************************/
  private void setupFiles ( String name )
  {
    int index;					// String array index

    // Set up the output files.
    for ( index = 0; index < Species.species.length; index++ )
    {
      // Allocate the files.
      species_seq_files [ index ] = new OutputTools ();
      species_genomic_files [ index ] = new OutputTools ();

      // Set the file names.
      species_seq_files [ index ].setFileName ( name + "." 
          + Species.species [ index ].replace ( ' ', '_' ) + ".seq" );

      species_genomic_files [ index ].setFileName ( name + "." 
          + Species.species [ index ].replace ( ' ', '_' ) + ".genomic" );

      // Open the output files for writing.
      species_seq_files [ index ].openFile ();
      species_genomic_files [ index ].openFile ();
    }  // for
  }  // method setupFiles


/******************************************************************************/
  private void setupFile ( OutputTools file, String name, String suffix )
  {
    // Truncate the name at the first period.
    int index = name.indexOf ( "." );
    if ( index > 0 )  name = name.substring ( 0, index );

    // Set the file names.
    file.setFileName ( name + "." + suffix );

    // Open the output files for writing.
    file.openFile ();
  }  // method setupFile


/******************************************************************************/
  public void setName ( String name )
  {
    // Truncate the name at the first period.
    int index = name.indexOf ( "." );
    if ( index > 0 )  name = name.substring ( 0, index );

    // Set up the output files using name as the prefix.
    setupFiles ( name );
  }  // method setName


/******************************************************************************/
  public void close ()
  {
    int index;					// array index

    // Close the output files.

    // Close the species files. 
    for ( index = 0; index < species_seq_files.length; index++ )
    {
      species_seq_files [ index ].closeFile ();
      species_genomic_files [ index ].closeFile ();
    }  // for
  }  // method close


/******************************************************************************/
  private void writeHeaderLine
      ( OutputTools    output_file
      , GenBankHeader  gb_header
      )
  {
    output_file.print ( ">" );

    output_file.print ( gb_header.getVersionName () );

    output_file.print ( " /organism=\"" );
    output_file.print ( gb_header.getOrganism () );
    output_file.println ( "\"" );
  }  // method writeHeaderLine


/******************************************************************************/
  private void processSpecies
      ( GenBankHeader  gb_header
      , String         sequence
      , OutputTools [] species_files
      )
  {
    // Search the list of selected species.
    String organism = gb_header.getOrganism ();
    for ( int index = 0; index < species_files.length; index++ )

      if ( organism.startsWith ( Species.species [ index ] ) == true )
      {
        writeHeaderLine ( species_files [ index ], gb_header );
        SeqTools.writeFasta ( species_files [ index ], sequence.toString () );
      }  // if
  }  // method processSpecies


/******************************************************************************/
  public void processEntry
      ( GenBankHeader  gb_header
      , String         sequence
      )
  {
    // Check for a GenBank Header object.
    if ( gb_header == null )  return;

    // Check for a sequence.
    if ( sequence.length () <= 0 )  return;

    // Ignore chloroplast and mitochondrial sequences.
    if ( ( gb_header.getOrganism ().startsWith ( "Chloroplast" ) == true ) ||
         ( gb_header.getOrganism ().startsWith ( "Mitochon"    ) == true ) ||
         ( gb_header.getOrganism ().startsWith ( "Plastid"    ) == true ) )

      return;

    // Capture all sequences for species of interest.
    processSpecies ( gb_header, sequence, species_seq_files );

    // Capture all genomic sequences for species of interest.
    if ( gb_header.getSequenceType ().equals ( "DNA" ) == true )

      processSpecies ( gb_header, sequence, species_genomic_files );
  }  // method processEntry


/******************************************************************************/

}  // class GenBankSlice

