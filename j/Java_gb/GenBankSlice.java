
import java.util.Vector;

import GenBankFeature;
import GenBankHeader;
import InputTools;
import OutputTools;
import SeqTools;

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

  private String cereals [] = 
        { "Aegilops"				// 
        , "Avena"				// oats
        , "Festuca"				// 
        , "Helictotrichon"			// 
        , "Hordeum" 				// barley
        , "Lolium"				// ryegrass
        , "Kengyilia"				// 
        , "Oryza"				// rice
        , "Pennisetum"				// (Pearl millet)
        , "Poaceae"				// all other cereals
        , "Saccharum"				// sugarcane
        , "Secale" 				// rye
        , "Sorghum"				// sorghum
        , "Tripsacum"				// (gama grass)
        , "Triticum"				// bread wheat
        , "Zea"					// corn
        , "Zizania" 				// wild rice
        };

  private String plants [] = 
        { "Arabidopsis thaliana"		// Arabidopsis
        , "Brassica napus"			// rape
        , "Brassica rapa"			// field mustard
        , "Caulerpa taxifolia"			// toxic alga
        , "Cucumis sativus"			// cucumber
        , "Daucus carota"			// carrot
        , "Glycine max"				// soybean
        , "Gossypium barbadense"		// sea-island cotton
        , "Gossypium hirsutum"			// upland cotton
        , "Helianthus annuus"			// common sunflower
        , "Ipomoea batatas"			// sweet potato
        , "Lactuca sativa"			// garden lettuce
        , "Lycopersicon esculentum"		// tomato
        , "Malus x domestica"			// apple
        , "Medicago sativa"			// alfalfa
        , "Mesembryanthemum crystallinum"	// common ice plant
        , "Musa acuminata"			// banana
        , "Nicotiana plumbaginifolia"		// tabacco curved-leaved
        , "Nicotiana sylvestris"		// wood tobacco
        , "Nicotiana tabacum"			// common tabacco
        , "Petroselinum crispum"		// parsley
        , "Petunia x hybrida"			// Petunia
        , "Phaseolus vulgaris"			// French bean
        , "Picea abies"				// Norway spruce
        , "Picea mariana"			// black spruce
        , "Pinus taeda"				// loblolly pine
        , "Pisum sativum"			// pea
        , "Solanum tuberosum"			// potato
        , "Spinacia oleracea"			// spinach
        , "Vicia faba"				// fava bean
        , "Viridiplantae"			// all other plants
        , "Vitis vinifera"			// grapevine
        };

  // List of features of interest.
  private String features [] = { "rRNA", "scRNA", "snRNA", "tRNA" };


/******************************************************************************/

  private OutputTools cereal_bacs_file = new OutputTools ();	// cereal BACS - rice

  private OutputTools rice_bacs_file = new OutputTools ();	// rice BACs

  // Cereal mRNA sequences
  private OutputTools cereal_files [] = new OutputTools [ cereals.length ];

  // Plant mRNA sequences
  private OutputTools plant_files [] = new OutputTools [ plants.length ];

  // Feature sequences
  private OutputTools feature_files [] = new OutputTools [ features.length ];

  private OutputTools cereal_repeats_file = new OutputTools ();	// repetitive sequences

  private OutputTools plant_repeats_file = new OutputTools ();	// repetitive sequences

  private OutputTools rice_repeats_file = new OutputTools ();	// repetitive sequences


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

    // Set up the individual files.
    setupFile ( cereal_bacs_file, name, "cereal_bacs" );
    setupFile ( rice_bacs_file, name, "rice_bacs" );

    setupFile ( cereal_repeats_file, name, "cereal_repeats" );
    setupFile ( plant_repeats_file, name, "plant_repeats" );
    setupFile ( rice_repeats_file, name, "rice_repeats" );

    // Set up the cereal files.
    for ( index = 0; index < cereals.length; index++ )
    {
      // Allocate the files.
      cereal_files [ index ] = new OutputTools ();

      // Set the file names.
      cereal_files [ index ].setFileName ( name + "." + cereals [ index ] );

      // Open the output files for writing.
      cereal_files [ index ].openFile ();
    }  // for

    // Set up the features files.
    for ( index = 0; index < features.length; index++ )
    {
      // Allocate the files.
      feature_files [ index ] = new OutputTools ();

      // Set the file names.
      feature_files [ index ].setFileName ( name + "." 
          + features [ index ].replace ( ' ', '_' ) );

      // Open the output files for writing.
      feature_files [ index ].openFile ();
    }  // for

    // Set up the plant files.
    for ( index = 0; index < plants.length; index++ )
    {
      // Allocate the files.
      plant_files [ index ] = new OutputTools ();

      // Set the file names.
      plant_files [ index ].setFileName ( name + "." 
          + plants [ index ].replace ( ' ', '_' ) );

      // Open the output files for writing.
      plant_files [ index ].openFile ();
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
    cereal_bacs_file.closeFile ();
    rice_bacs_file.closeFile ();

    cereal_repeats_file.closeFile ();
    plant_repeats_file.closeFile ();
    rice_repeats_file.closeFile ();

    // Close the cereal files. 
    for ( index = 0; index < cereal_files.length; index++ )

      cereal_files [ index ].closeFile ();

    // Close the feature files. 
    for ( index = 0; index < feature_files.length; index++ )

      feature_files [ index ].closeFile ();

    // Close the plant files. 
    for ( index = 0; index < plant_files.length; index++ )

      plant_files [ index ].closeFile ();
  }  // method close


/******************************************************************************/
  private void writeHeaderLine
      ( OutputTools    output_file
      , GenBankHeader  gb_header
      , GenBankFeature feature
      )
  {
    output_file.print ( ">" );

    output_file.print ( gb_header.getAccession () );
    output_file.print ( "_" + (String) feature.getStarts ().firstElement () );
    output_file.print ( "-" + (String) feature.getEnds   ().lastElement () );

    output_file.print ( " /organism=\"" );
    output_file.print ( gb_header.getOrganism () );
    output_file.print ( "\" " );
  
    output_file.println ( feature.getQualifiers () );
  }  // method writeHeaderLine


/******************************************************************************/
  private void writeHeaderLine
      ( OutputTools    output_file
      , GenBankHeader  gb_header
      )
  {
    output_file.print ( ">" );

    output_file.print ( gb_header.getAccession () );

    output_file.print ( " /organism=\"" );
    output_file.print ( gb_header.getOrganism () );
    output_file.println ( "\"" );
  }  // method writeHeaderLine


/******************************************************************************/
  private void writeFeature
      ( OutputTools    output_file
      , GenBankHeader  gb_header
      , GenBankFeature feature
      , String         sequence
      )
  {
System.out.println ( "writeFeature called: " + feature.getFeatureType () );

    StringBuffer mRNA = new StringBuffer ( 3000 );

    Vector starts = feature.getStarts ();
    Vector ends   = feature.getEnds ();

    // Set the 5' incomplete flag if known.
    if ( feature.isIncomplete5 () == true )  mRNA.append ( "<" );

    // Splice out the coordinate from the sequence.
    int index = 0;			// Vector index
    while ( ( index < starts.size () ) && ( index < ends.size () ) )
    {
      // Get the coordinates of the feature.
      int start = InputTools.getInteger ( (String) starts.elementAt ( index ) );
      int end   = InputTools.getInteger ( (String) ends  .elementAt ( index ) );

      // Check the coordinates.
      if ( ( start < 1 ) ||
           ( end   < 1 ) || 
           ( end   < start ) ||
           ( start >= sequence.length () ) ||
           ( end   >  sequence.length () ) )  
      {
System.out.println ( "\t[" + start + ".." + end + "]" );
      }  // if
      else
      {
        if ( end < sequence.length () )
          mRNA.append ( sequence.substring ( start-1, end ) );
        else
          mRNA.append ( sequence.substring ( start-1 ) );
      }  // if

      index++;

      // Insert a marker of exon boundaries.
      if ( index < starts.size () )  mRNA.append ( "|" );
    }  // while

    // Set the 3' incomplete flag if known.
    if ( feature.isIncomplete3 () == true )  mRNA.append ( ">" );

    // Check for very short sequence.
    if ( mRNA.length () < 100 )
    {
      System.out.println ( "Ignoring short mRNAs, length = " + mRNA.length () );
      return;
    }  // if

    // Write out the header line.
    writeHeaderLine ( output_file, gb_header, feature );

    // Write out the specified sequence.
    if ( feature.getStrand () == '+' )
      SeqTools.writeFasta ( output_file, mRNA.toString () );
    else
      SeqTools.writeFasta ( output_file, 
          SeqTools.reverseSequence ( mRNA.toString () ) );
  }  // method writeFeature


/******************************************************************************/
  private void processFeature
      ( GenBankHeader  gb_header
      , GenBankFeature feature
      , String         sequence
      )
  {
    // Extract the rRNA, scRNA, snRNA, and rRNA features/sequences.
    for ( int index = 0; index < feature_files.length; index++ )

      if ( feature.getFeatureType ().equals ( features [ index ] ) == true )

        writeFeature ( feature_files [ index ], gb_header, feature, sequence );


    String taxonomy = gb_header.getTaxonomy ();

    // Check for a feature type of interest.
    if ( ( feature.getFeatureType ().equals ( "mRNA" ) == true ) ||
         ( feature.getFeatureType ().equals ( "CDS" ) == true ) )
    { 
      // Ignore pseudo genes.
      if ( feature.isPseudo () == false )
      {
        // Select which output file to write to.

        // Check for a cereal species.

        if ( taxonomy.indexOf ( "Poaceae" ) > 0 )
        {
          // Search the list of selected cereal species.
          int others = 0;

          for ( int index = 0; index < cereal_files.length; index++ )

            if ( taxonomy.indexOf ( cereals [ index ] ) > 0 )
            {
              writeFeature ( cereal_files [ index ], gb_header, feature, sequence );
              
              return;
            }  // if
            else 
              if ( cereals [ index ].equals ( "Poaceae" ) == true )  others = index;

          writeFeature ( cereal_files [ others ], gb_header, feature, sequence );
        }  // if

        String organism = gb_header.getOrganism ();

        // Search the list of selected plant species.
        int others = 0;
        for ( int index = 0; index < plant_files.length; index++ )

          if ( organism.startsWith ( plants [ index ] ) == true )
          {
            writeFeature ( plant_files [ index ], gb_header, feature, sequence );

            return;
          }  // if
          else 
            if ( plants [ index ].equals ( "Viridiplantae" ) == true )  others = index;

        writeFeature ( plant_files [ others ], gb_header, feature, sequence );
      }  // if
    }  // if
    else
      if ( ( feature.getFeatureType ().equals ( "LTR" ) == true ) ||
           ( feature.getFeatureType ().equals ( "repeat_region" ) == true ) ||
           ( feature.getFeatureType ().equals ( "repeat_unit" ) == true ) ||
           ( feature.getFeatureType ().equals ( "satellite" ) == true ) ||
           ( feature.getInsertionSeq ().length () > 0 ) ||
           ( feature.isProviral () == true ) ||
           ( feature.getRptFamily ().length () > 0 ) ||
           ( feature.getRptType ().length () > 0 ) ||
           ( feature.getRptUnit ().length () > 0 ) ||
           ( feature.getTransposon ().length () > 0 ) ||
           ( feature.isVirion () == true ) )
      {
        // Check for a cereal species.
        if ( taxonomy.indexOf ( "Poaceae" ) > 0 )
        {
          // Check for rice.
          if ( taxonomy.indexOf ( "Oryza" ) > 0 )
            writeFeature ( rice_repeats_file, gb_header, feature, sequence );
          else
            writeFeature ( cereal_repeats_file, gb_header, feature, sequence );
        }  // if
        else
          writeFeature ( plant_repeats_file, gb_header, feature, sequence );
      }  // if
  }  // method processFeature


/******************************************************************************/
  public void processEntry
      ( GenBankHeader gb_header
      , Vector          gb_features
      , String          sequence
      )
  {
    // Check for a GenBank Header object.
    if ( gb_header == null )  return;

    // Check for a sequence.
    if ( sequence.length () <= 0 )  return;

    // Check for a plant sequence.
    if ( gb_header.getTaxonomy ().indexOf ( "Viridiplant" ) < 0 )  return;

    // Check for a chloroplast or mitochondrial sequence.
    if ( ( gb_header.getOrganism ().startsWith ( "Chloroplast" ) == true ) ||
         ( gb_header.getOrganism ().startsWith ( "Mitochon"    ) == true ) )

      return;

    // Collect cereal BACs.
    if ( ( sequence.length () > 30000 ) &&
         ( gb_header.getTaxonomy ().indexOf ( "Poaceae" ) > 0 ) )
    {
      // Check for a rice BAC.
      if ( gb_header.getTaxonomy ().indexOf ( "Oryza" ) > 0 )
      {
        // Write out the header line.
        writeHeaderLine ( rice_bacs_file, gb_header );

        SeqTools.writeFasta ( rice_bacs_file, sequence );
      }  // if
      else
      {
        // Write out the header line.
        writeHeaderLine ( cereal_bacs_file, gb_header );

        SeqTools.writeFasta ( cereal_bacs_file, sequence );
      }  // else
    }  // if

    // Process the Features table.
    for ( int index = 0; index < gb_features.size (); index++ )

      processFeature 
          ( gb_header
          , (GenBankFeature) gb_features.elementAt ( index )
          , sequence 
          );

    // Capture mRNA & EST sequences with no features.
    if ( ( gb_features.size () == 0 ) && ( gb_header.getTaxonomy ().indexOf ( "Poaceae" ) > 0 ) )
    {
      if ( gb_header.getTaxonomy ().indexOf ( "Oryza" ) > 0 )
      {
        writeHeaderLine ( cereal_files [ 7 ], gb_header );

        SeqTools.writeFasta ( cereal_files [ 7 ], sequence.toString () );
      }  // if
      else
      {
        writeHeaderLine ( cereal_files [ 9 ], gb_header );

        SeqTools.writeFasta ( cereal_files [ 9 ], sequence.toString () );
      }  // else
    }  // if
  }  // method processEntry


/******************************************************************************/

}  // class GenBankSlice

