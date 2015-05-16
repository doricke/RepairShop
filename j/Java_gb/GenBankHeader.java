
import java.util.Vector;

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

public class GenBankHeader extends Object
{


/******************************************************************************/

  private String locus = "";			// LOCUS

  private String definition = "";		// DEFINITION

  private String accession = "";		// ACCESSION

  private String nid = "";			// NID

  private String version = "";			// VERSION

  private String keywords = "";			// KEYWORDS

  private String segment = "";			// SEGMENT

  private String source = "";			// SOURCE

  private String organism = "";			// ORGANISM

  private String taxonomy = "";			// Lines following ORGANISM line

  private Vector references = new Vector ();	// REFERENCE, AUTHORS, TITLE, JOURNAL, MEDLINE

  private String comment = "";			// COMMENT

  private Vector features = new Vector ();	// (Location/Qualifiers) features

  private String base_count = "";		// BASE COUNT

  private String origin = "";			// ORIGIN

  private String sequence = "";			// sequence 


  private String accession_number = "";		// First accession number on ACCESSION line

  private int sequence_length = 0;		// Length of sequence from LOCUS line

  private String sequence_type = "";		// Sequence type from LOCUS line


/******************************************************************************/
  // Constructor GenBankHeader
  public GenBankHeader ()
  {
    initialize ();
  }  // constructor GenBankHeader


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    locus = "";
    definition = "";
    accession = "";
    nid = "";
    version = "";
    keywords = "";
    segment = "";
    source = "";
    organism = "";
    taxonomy = "";
    references.removeAllElements ();
    comment = "";
    features.removeAllElements ();
    base_count = "";
    origin = "";
    sequence = "";

    accession_number = "";
    sequence_length = 0;
    sequence_type = "";
  }  // method initialize 


/******************************************************************************/
  public String getLocus ()
  {
    return locus;
  }  // method getLocus


/******************************************************************************/
  public String getLocusName ()
  {
    int index = locus.indexOf ( ' ' );
    if ( index < 0 )
      return locus;
    else return locus.substring ( 0, index );
  }  // method getLocusName


/******************************************************************************/
  public int getSequenceLength ()
  {
    return sequence_length;
  }  // method getSequenceLength


/******************************************************************************/
  public String getDefinition ()
  {
    return definition;
  }  // method getDefinition


/******************************************************************************/
  public String getAccession ()
  {
    return accession;
  }  // method getAccession


/******************************************************************************/
  public String getAccessionNumber ()
  {
    return accession_number;
  }  // method getAccessionNumber


/******************************************************************************/
  public String getNid ()
  {
    return nid;
  }  // method getNid


/******************************************************************************/
  public String getVersion ()
  {
    return version;
  }  // method getVersion


/******************************************************************************/
  public String getKeywords ()
  {
    return keywords;
  }  // method getKeywords


/******************************************************************************/
  public String getSegment ()
  {
    return segment;
  }  // method getSegment


/******************************************************************************/
  public String getSource ()
  {
    return source;
  }  // method getSource


/******************************************************************************/
  public String getOrganism ()
  {
    return organism;
  }  // method getOrganism


/******************************************************************************/
  public String getTaxonomy ()
  {
    return taxonomy;
  }  // method getTaxonomy


/******************************************************************************/
  public Vector getReferences ()
  {
    return references;
  }  // method getReferences


/******************************************************************************/
  public String getComment ()
  {
    return comment;
  }  // method getComment


/******************************************************************************/
  public Vector getFeatures ()
  {
    return features;
  }  // method getFeatures


/******************************************************************************/
  public String getBaseCount ()
  {
    return base_count;
  }  // method getBaseCount


/******************************************************************************/
  public String getOrigin ()
  {
    return origin;
  }  // method getOrigin


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


/******************************************************************************/
  public void setLocus ( String value )
  {
    locus = value;
  }  // method setLocus


/******************************************************************************/
  public void setSequenceLength ( int value )
  {
    sequence_length = value;
  }  // method setSequenceLength


/******************************************************************************/
  public void setDefinition ( String value )
  {
    definition = value;
  }  // method setDefinition


/******************************************************************************/
  public void setAccession ( String value )
  {
    accession = value;
  }  // method setAccession


/******************************************************************************/
  public void setNid ( String value )
  {
    nid = value;
  }  // method setNid


/******************************************************************************/
  public void setVersion ( String value )
  {
    version = value;
  }  // method setVersion


/******************************************************************************/
  public void setKeywords ( String value )
  {
    // Ignore "." keywords fields.
    if ( value.length () > 1 )  keywords = value;
  }  // method setKeywords


/******************************************************************************/
  public void setSegment ( String value )
  {
    segment = value;
  }  // method setSegment


/******************************************************************************/
  public void setSource ( String value )
  {
    source = value;
  }  // method setSource


/******************************************************************************/
  public void setOrganism ( String value )
  {
    organism = value;

    // Remove any single quotes in the organism name.
    int index = value.indexOf ( "'" );
    if ( index >= 0 )
    {
      StringBuffer temp = new StringBuffer ( value );

      while ( index >= 0 )
      {
        temp.deleteCharAt ( index );
        index = temp.toString ().indexOf ( "'" );
      }  // while

      organism = temp.toString ();
    }  // if
  }  // method setOrganism


/******************************************************************************/
  public void setTaxonomy ( String value )
  {
    taxonomy = value;
  }  // method setTaxonomy


/******************************************************************************/
  public void setReferences ( Vector value )
  {
    references = value;
  }  // method setReferences


/******************************************************************************/
  public void setComment ( String value )
  {
    comment = value;
  }  // method setComment


/******************************************************************************/
  public void setFeatures ( Vector value )
  {
    features = value;
  }  // method setFeatures


/******************************************************************************/
  public void setBaseCount ( String value )
  {
    base_count = value;
  }  // method setBaseCount


/******************************************************************************/
  public void setOrigin ( String value )
  {
    origin = value;
  }  // method setOrigin


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;
  }  // method setSequence


/******************************************************************************/
  public String getSequenceType ()
  {
    return sequence_type;
  }  // method getSequenceType


/******************************************************************************/
  private void crackAccession ( String value )
  {
    accession = value;

    // Clip after the first accession number.
    int index = value.indexOf ( " " );
    if ( index > 0 )
      accession_number = value.substring ( 0, index );
    else
      accession_number = value;

    // System.out.println ( "\taccession number = " + accession_number );
  }  // method crackAccession


/******************************************************************************/
  private void crackLocus ( String value )
  {
    locus = value;

    // Extract the sequence type from the LOCUS line.
    if ( locus.length () > 30 )
      sequence_type = locus.substring ( 24, 30 ).trim ();

    // Extract the sequence length from the LOCUS line.
    if ( locus.length () > 30 )
      sequence_length = InputTools.getInteger ( locus.substring ( 10, 17 ) );

    // System.out.println ( "crackLocus: type = '" + sequence_type 
    //    + "', length = " + sequence_length );
  }  // method crackLocus


/******************************************************************************/
  public void setSection ( String name, String value )
  {
    // System.out.println ( name + "\t'" + value + "'" );

         if ( name.equals ( "LOCUS"      ) == true )  crackLocus ( value );
    else if ( name.equals ( "DEFINITION" ) == true )  definition = value;
    else if ( name.equals ( "ACCESSION"  ) == true )  crackAccession ( value );
    else if ( name.equals ( "NID"        ) == true )  nid = value;
    else if ( name.equals ( "VERSION"    ) == true )  version = value;
    else if ( name.equals ( "KEYWORDS"   ) == true )  keywords = value;
    else if ( name.equals ( "SEGMENT"    ) == true )  segment = value;
    else if ( name.equals ( "SOURCE"     ) == true )  source = value;
    else if ( name.equals ( "ORGANISM"   ) == true )  organism = value;
    else if ( name.equals ( "Taxonomy"   ) == true )  taxonomy = value;
    else if ( name.equals ( "COMMENT"    ) == true )  comment = value;
    else if ( name.equals ( "BASE COUNT" ) == true )  base_count = value;
    else if ( name.equals ( "ORIGIN"     ) == true )  origin = value;
  }  // method setSection


/******************************************************************************/

}  // class GenBankHeader
