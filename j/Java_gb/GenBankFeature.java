
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

public class GenBankFeature extends Object
{


/******************************************************************************/

  private String  chromosome = "";		// /chromosome=

  private int     citation = 0;			// /citation=

  private String  clone = "";			// /clone=

  private int     codon_start = 0;		// /codon_start=1 or 2 or 3

  private String  db_xref = "";			// /db_xref="<database>:<identifier>"

  private String  ec_number = "";		// /EC_number=

  private String  evidence = "";		// /evidence=

  private String  function = "";		// /function=

  private String  gene = "";			// /gene=

  private String  insertion_seq = "";		// /insertion_seq=

  private String  label = "";			// /label=

  private String  map = "";			// /map=

  private String  note = "";			// /note=

  private String  number = "";			// /number=

  private String  organelle = "";		// /organelle=

  private boolean partial = false;		// /partial

  private String  product = "";			// /product=

  private String  protein_id = "";		// /protein_id=

  private boolean proviral = false;		// /proviral

  private boolean pseudo = false;		// /pseudo

  private String  rpt_family = "";		// /rpt_family=

  private String  rpt_type = "";		// /rpt_type=

  private String  rpt_unit = "";		// /rpt_unit=

  private String  standard_name = "";		// /standard_name=

  private String  sub_species = "";		// /sub_species=

  private String  translation = "";		// /translation=

  private String  transposon = "";		// /transposon=

  private boolean virion = false;		// /virion


  private String feature_type = "";		// feature type

  private char strand = '+';			// strand - '+' or '-'

  private boolean incomplete_5 = false;		// '<' seen on location

  private boolean incomplete_3 = false;		// '>' seen on location

  private Vector starts = new Vector ();	// start locations

  private Vector ends = new Vector ();		// end locations

  private int start = 0;			// start location

  private int end = 0;				// end location


/******************************************************************************/
  // Constructor GenBankFeature
  public GenBankFeature ()
  {
    initialize ();
  }  // constructor GenBankFeature


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    chromosome = "";
    citation = 0;
    clone = "";
    codon_start = 0;
    db_xref = "";
    ec_number = "";
    evidence = "";
    function = "";
    gene = "";
    insertion_seq = "";
    label = "";
    map = "";
    note = "";
    number = "";
    organelle = "";
    partial = false;
    product = "";
    protein_id = "";
    proviral = false;
    pseudo = false;
    rpt_family = "";
    rpt_type = "";
    rpt_unit = "";
    standard_name = "";
    sub_species = "";
    translation = "";
    transposon = "";
    virion = false;

    feature_type = "";
    strand = '+';
    incomplete_5 = false;
    incomplete_3 = false;
    starts.removeAllElements ();
    ends.removeAllElements ();
    start = 0;
    end = 0;
  }  // method initialize 


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public int getCitation ()
  {
    return citation;
  }  // method getCitation


/******************************************************************************/
  public String getClone ()
  {
    return clone;
  }  // method getClone


/******************************************************************************/
  public int getCodonStart ()
  {
    return codon_start;
  }  // method getCodonStart


/******************************************************************************/
  public String getDbXref ()
  {
    return db_xref;
  }  // method getDbXref


/******************************************************************************/
  public String getEcNumber ()
  {
    return ec_number;
  }  // method getEcNumber


/******************************************************************************/
  public int getEnd ()
  {
    return end;
  }  // method getEnd


/******************************************************************************/
  public Vector getEnds ()
  {
    return ends;
  }  // method getEnds


/******************************************************************************/
  public String getEvidence ()
  {
    return evidence;
  }  // method getEvidence


/******************************************************************************/
  public String getFeatureType ()
  {
    return feature_type;
  }  // method getFeatureType


/******************************************************************************/
  public String getFunction ()
  {
    return function;
  }  // method getFunction


/******************************************************************************/
  public String getGene ()
  {
    return gene;
  }  // method getGene


/******************************************************************************/
  public String getInsertionSeq ()
  {
    return insertion_seq;
  }  // method getInsertionSeq


/******************************************************************************/
  public String getLabel ()
  {
    return label;
  }  // method getLabel


/******************************************************************************/
  public String getMap ()
  {
    return map;
  }  // method getMap


/******************************************************************************/
  public String getNote ()
  {
    return note;
  }  // method getNote


/******************************************************************************/
  public String getNumber ()
  {
    return number;
  }  // method getNumber


/******************************************************************************/
  public String getOrganelle ()
  {
    return organelle;
  }  // method getOrganelle


/******************************************************************************/
  public boolean getPartial ()
  {
    return partial;
  }  // method getPartial


/******************************************************************************/
  public String getProduct ()
  {
    return product;
  }  // method getProduct


/******************************************************************************/
  public String getProteinId ()
  {
    return protein_id;
  }  // method getProteinId


/******************************************************************************/
  public boolean getProviral ()
  {
    return proviral;
  }  // method getProviral


/******************************************************************************/
  public boolean getPseudo ()
  {
    return pseudo;
  }  // method getPseudo


/******************************************************************************/
  public String getRptFamily ()
  {
    return rpt_family;
  }  // method getRptFamily


/******************************************************************************/
  public String getRptType ()
  {
    return rpt_type;
  }  // method getRptType


/******************************************************************************/
  public String getRptUnit ()
  {
    return rpt_unit;
  }  // method getRptUnit


/******************************************************************************/
  public String getStandardName ()
  {
    return standard_name;
  }  // method getStandardName


/******************************************************************************/
  public int getStart ()
  {
    return start;
  }  // method getStart


/******************************************************************************/
  public Vector getStarts ()
  {
    return starts;
  }  // method getStarts


/******************************************************************************/
  public char getStrand ()
  {
    return strand;
  }  // method getStrand


/******************************************************************************/
  public String getSubSpecies ()
  {
    return sub_species;
  }  // method getSubSpecies


/******************************************************************************/
  public String getTranslation ()
  {
    return translation;
  }  // method getTranslation


/******************************************************************************/
  public String getTransposon ()
  {
    return transposon;
  }  // method getTransposon


/******************************************************************************/
  public boolean getVirion ()
  {
    return virion;
  }  // method getVirion


/******************************************************************************/
  public String getQualifiers ()
  {
    StringBuffer qualifiers = new StringBuffer ( 1024 );

    qualifiers.append ( getValue ( "chromosome", chromosome ) );
    qualifiers.append ( getValue ( "citation", citation ) );
    qualifiers.append ( getValue ( "clone", clone ) );
    qualifiers.append ( getValue ( "codon_start", codon_start ) );
    qualifiers.append ( getValue ( "db_xref", db_xref ) );
    qualifiers.append ( getValue ( "ec_number", ec_number ) );
    qualifiers.append ( getValue ( "evidence", evidence ) );
    qualifiers.append ( getValue ( "function", function ) );
    qualifiers.append ( getValue ( "gene", gene ) );
    qualifiers.append ( getValue ( "insertion_seq", insertion_seq ) );
    qualifiers.append ( getValue ( "label", label ) );
    qualifiers.append ( getValue ( "map", map ) );
    qualifiers.append ( getValue ( "number", number ) );
    qualifiers.append ( getValue ( "organelle", organelle ) );
    qualifiers.append ( getValue ( "partial", partial ) );
    qualifiers.append ( getValue ( "product", product ) );
    qualifiers.append ( getValue ( "protein_id", protein_id ) );
    qualifiers.append ( getValue ( "proviral", proviral ) );
    qualifiers.append ( getValue ( "pseudo", pseudo ) );
    qualifiers.append ( getValue ( "rpt_family", rpt_family ) );
    qualifiers.append ( getValue ( "rpt_type", rpt_type ) );
    qualifiers.append ( getValue ( "rpt_unit", rpt_unit ) );
    qualifiers.append ( getValue ( "standard_name", standard_name ) );
    qualifiers.append ( getValue ( "sub_species", sub_species ) );
    qualifiers.append ( getValue ( "transposon", transposon ) );
    qualifiers.append ( getValue ( "virion", virion ) );

    qualifiers.append ( getValue ( "note", note ) );
//    qualifiers.append ( getValue ( "translation", translation ) );

    return qualifiers.toString ();
  }  // method getQualifiers


/******************************************************************************/
  public boolean isIncomplete5 ()
  {
    return incomplete_5;
  }  // method isIncomplete5


/******************************************************************************/
  public boolean isIncomplete3 ()
  {
    return incomplete_3;
  }  // method isIncomplete3


/******************************************************************************/
  public boolean isPartial ()
  {
    return partial;
  }  // method isPartial


/******************************************************************************/
  public boolean isProviral ()
  {
    return proviral;
  }  // method isProviral


/******************************************************************************/
  public boolean isPseudo ()
  {
    return pseudo;
  }  // method isPseudo


/******************************************************************************/
  public boolean isVirion ()
  {
    return virion;
  }  // method isVirion


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setCitation ( int value )
  {
    citation = value;
  }  // method setCitation


/******************************************************************************/
  public void setClone ( String value )
  {
    clone = value;
  }  // method setClone


/******************************************************************************/
  public void setCodonStart ( int value )
  {
    codon_start = value;
  }  // method setCodonStart


/******************************************************************************/
  public void setDbXref ( String value )
  {
    db_xref = value;
  }  // method setDbXref


/******************************************************************************/
  public void setEcNumber ( String value )
  {
    ec_number = value;
  }  // method setEcNumber


/******************************************************************************/
  public void setEnd ( int value )
  {
    end = value;
  }  // method setEnd


/******************************************************************************/
  public void setEvidence ( String value )
  {
    evidence = value;
  }  // method setEvidence


/******************************************************************************/
  public void setFeatureType ( String value )
  {
    feature_type = value;
  }  // method setFeatureType


/******************************************************************************/
  public void setFunction ( String value )
  {
    function = value;
  }  // method setFunction


/******************************************************************************/
  public void setGene ( String value )
  {
    gene = value;
  }  // method setGene


/******************************************************************************/
  public void setIncomplete5 ( boolean value )
  {
    incomplete_5 = value;
  }  // method setIncomplete5


/******************************************************************************/
  public void setIncomplete3 ( boolean value )
  {
    incomplete_3 = value;
  }  // method setIncomplete3


/******************************************************************************/
  public void setInsertionSeq ( String value )
  {
    insertion_seq = value;
  }  // method setInsertionSeq


/******************************************************************************/
  public void setLabel ( String value )
  {
    label = value;
  }  // method setLabel


/******************************************************************************/
  public void setMap ( String value )
  {
    map = value;
  }  // method setMap


/******************************************************************************/
  public void setNote ( String value )
  {
    note = value;
  }  // method setNote


/******************************************************************************/
  public void setNumber ( String value )
  {
    number = value;
  }  // method setNumber


/******************************************************************************/
  public void setOrganelle ( String value )
  {
    organelle = value;
  }  // method setOrganelle


/******************************************************************************/
  public void setPartial ( boolean value )
  {
    partial = value;
  }  // method setPartial


/******************************************************************************/
  public void setProduct ( String value )
  {
    product = value;
  }  // method setProduct


/******************************************************************************/
  public void setProteinId ( String value )
  {
    protein_id = value;
  }  // method setProteinId


/******************************************************************************/
  public void setProviral ( boolean value )
  {
    proviral = value;
  }  // method setProviral


/******************************************************************************/
  public void setPseudo ( boolean value )
  {
    pseudo = value;
  }  // method setPseudo


/******************************************************************************/
  public void setRptFamily ( String value )
  {
    rpt_family = value;
  }  // method setRptFamily


/******************************************************************************/
  public void setRptType ( String value )
  {
    rpt_type = value;
  }  // method setRptType


/******************************************************************************/
  public void setRptUnit ( String value )
  {
    rpt_unit = value;
  }  // method setRptUnit


/******************************************************************************/
  public void setStandardName ( String value )
  {
    standard_name = value;
  }  // method setStandardName


/******************************************************************************/
  public void setStart ( int value )
  {
    start = value;
  }  // method setStart


/******************************************************************************/
  public void setSubSpecies ( String value )
  {
    sub_species = value;
  }  // method setSubSpecies


/******************************************************************************/
  public void setTranslation ( String value )
  {
    translation = value;
  }  // method setTranslation


/******************************************************************************/
  public void setTransposon ( String value )
  {
    transposon = value;
  }  // method setTransposon


/******************************************************************************/
  public void setVirion ( boolean value )
  {
    virion = value;
  }  // method setVirion


/******************************************************************************/
  private int getCoordinate1 ( String coord )
  {
    if ( ( coord == null ) || ( coord.length () <= 0 ) )  return 0;

    // Check for "<number"
    if ( coord.charAt ( 0 ) == '<' )
    {
      partial = true;
      incomplete_5 = true;
      coord = coord.substring ( 1 );
    }  // if

    // Check for "(number.number)"
    if ( coord.charAt ( 0 ) == '(' )
    {
      incomplete_5 = true;
      coord = coord.substring ( 1 );
    }  // if

    return InputTools.getInteger ( coord );
  }  // method getCoordinate1


/******************************************************************************/
  private int getCoordinate2 ( String coord )
  {
    // Check for ">number"
    if ( coord.charAt ( 0 ) == '>' )
    {
      partial = true;
      incomplete_3 = true;
      coord = coord.substring ( 1 );
    }  // if

    // Check for "(number.number)"
    if ( coord.charAt ( 0 ) == '(' )
    {
      incomplete_5 = true;

      coord = coord.substring ( coord.indexOf ( "." ) + 1 );
    }  // if

    return InputTools.getInteger ( coord );
  }  // method getCoordinate2


/******************************************************************************/
  // This method sets the coordinate limits for this feature.
  private void newCoordinate ( int coordinate )
  {
    if ( ( coordinate < start ) || ( start == 0 ) )  start = coordinate;

    if ( ( coordinate > end ) || ( end == 0 ) )  end = coordinate;
  }  // method newCoordinate


/******************************************************************************/
  private void setPair ( String pair )
  {
    // System.out.println ( "\t" + pair );

    // Check for an indirect reference:
    if ( pair.indexOf ( ':' ) > 0 )
    {
      partial = true;
      return;
    }  // if

    int index = pair.indexOf ( ".." );

    // Check for a single location.
    if ( index == -1 )
    {
      int location = getCoordinate1 ( pair );
      starts.addElement ( "" + location );
      ends.addElement ( "" + location );
    }  // if
    else
    {
      int pair_start = getCoordinate1 ( pair.substring ( 0, index ) );
      int pair_end = getCoordinate2 ( pair.substring ( index + 2 ) );

      starts.addElement ( "" + pair_start );
      ends.addElement ( "" + pair_end );

      // Set the start and end coordinates of this feature.
      newCoordinate ( pair_start );
      newCoordinate ( pair_end );
    }  // else
  }  // method setPair


/******************************************************************************/
  public void setLocation ( String feature_name, String value )
  {
    int index;				// value index

    feature_type = feature_name;

    // Remove operators.
    while ( ( value.indexOf ( "complement" ) >= 0 ) ||
            ( value.indexOf ( "group" ) >= 0 ) ||
            ( value.indexOf ( "order" ) >= 0 ) ||
            ( value.indexOf ( "join" ) >= 0 ) ||
            ( value.indexOf ( "one-of" ) >= 0 ) )
    {
      if ( ( value.indexOf ( "group" ) >= 0 ) ||
           ( value.indexOf ( "order" ) >= 0 ) ||
           ( value.indexOf ( "one-of" ) >= 0 ) )  partial = true;

      if ( value.indexOf ( "complement" ) >= 0 )  strand = '-';

      index = value.indexOf ( "(" );
      value = value.substring ( index + 1, value.length () - 1 );
    }  // while

    // Process each location pair.
    do
    {
      index = value.indexOf ( "," );

      if ( index == -1 )
      {
        setPair ( value );
        return;
      }  // if
      else
        setPair ( value.substring ( 0, index ) );

      value = value.substring ( index+1 );
    }
    while ( value.length () > 0 );
  }  // setLocation


/******************************************************************************/
  private String getValue ( String value )
  {
    int index = value.indexOf ( "=" );

    if ( index < 0 )  return "";

    // Remove the quote after the equal sign.
    if ( value.charAt ( index + 1 ) == '"' )  index++;
    value = value.substring ( index+1 );

    // Remove trailing double quote.
    if ( value.charAt ( value.length () - 1 ) == '"' )
      return value.substring ( 0, value.length () - 1 );
    else
      return value;
  }  // method getValue


/******************************************************************************/
  public void setQualifier ( String feature_name, String value )
  {
    String qualifier_name;		// /qualifier_name
    int index = value.indexOf ( '=' );

    if ( feature_name.equals ( feature_type ) != true )
    {
      System.out.println ( "*Warning* setQualifier feature name (" 
          + feature_name + ") different from setLocation name (" 
          + feature_type + ")" );
    }  // if

    if ( index < 0 )
      qualifier_name = value;
    else
      qualifier_name = value.substring ( 0, index );

    // System.out.println ( "qual\t" + qualifier_name + ":\t" + value );

         if ( qualifier_name.equals ( "chromosome" ) == true )
           chromosome = getValue ( value );

    else if ( qualifier_name.equals ( "citation" ) == true )
           citation = InputTools.getInteger ( getValue ( value ).substring ( 1 ) );

    else if ( qualifier_name.equals ( "clone" ) == true )
           clone = getValue ( value );

    else if ( qualifier_name.equals ( "codon_start" ) == true )
           codon_start = InputTools.getInteger ( getValue ( value ) );

    else if ( qualifier_name.equals ( "db_xref" ) == true )
           db_xref = getValue ( value );

    else if ( qualifier_name.equals ( "EC_number" ) == true )
           ec_number = getValue ( value );

    else if ( qualifier_name.equals ( "evidence" ) == true )
           evidence = getValue ( value );

    else if ( qualifier_name.equals ( "function" ) == true )
           function = getValue ( value );

    else if ( qualifier_name.equals ( "gene" ) == true )
           gene = getValue ( value );

    else if ( qualifier_name.equals ( "insertion_seq" ) == true )
           insertion_seq = getValue ( value );

    else if ( qualifier_name.equals ( "label" ) == true )
           label = getValue ( value );

    else if ( qualifier_name.equals ( "map" ) == true )
           map = getValue ( value );

    else if ( qualifier_name.equals ( "note" ) == true )
           note = getValue ( value );

    else if ( qualifier_name.equals ( "number" ) == true )
           number = getValue ( value );

    else if ( qualifier_name.equals ( "organelle" ) == true )
           organelle = getValue ( value );

    else if ( qualifier_name.equals ( "partial" ) == true )
           partial = true;

    else if ( qualifier_name.equals ( "product" ) == true )
           product = getValue ( value );

    else if ( qualifier_name.equals ( "protein_id" ) == true )
           protein_id = getValue ( value );

    else if ( qualifier_name.equals ( "proviral" ) == true )
           proviral = true;

    else if ( qualifier_name.equals ( "pseudo" ) == true )
           pseudo = true;

    else if ( qualifier_name.equals ( "rpt_family" ) == true )
           rpt_family = getValue ( value );

    else if ( qualifier_name.equals ( "rpt_type" ) == true )
           rpt_type = getValue ( value );

    else if ( qualifier_name.equals ( "rpt_unit" ) == true )
           rpt_unit = getValue ( value );

    else if ( qualifier_name.equals ( "standard_name" ) == true )
           standard_name = getValue ( value );

    else if ( qualifier_name.equals ( "sub_species" ) == true )
           sub_species = getValue ( value );

    else if ( qualifier_name.equals ( "translation" ) == true )
           translation = getValue ( value );

    else if ( qualifier_name.equals ( "transposon" ) == true )
           transposon = getValue ( value );

    else if ( qualifier_name.equals ( "virion" ) == true )
           virion = true;

    else if ( ( qualifier_name.equals ( "allele" ) == true ) ||
              ( qualifier_name.equals ( "anticodon" ) == true ) ||
              ( qualifier_name.equals ( "bound_moiety" ) == true ) ||
              ( qualifier_name.equals ( "codon" ) == true ) ||
              ( qualifier_name.equals ( "cons_splice" ) == true ) ||
              ( qualifier_name.equals ( "country" ) == true ) ||
              ( qualifier_name.equals ( "cell_line" ) == true ) ||
              ( qualifier_name.equals ( "cell_type" ) == true ) ||
              ( qualifier_name.equals ( "clone_lib" ) == true ) ||
              ( qualifier_name.equals ( "cultivar" ) == true ) ||
              ( qualifier_name.equals ( "dev_stage" ) == true ) ||
              ( qualifier_name.equals ( "direction" ) == true ) ||
              ( qualifier_name.equals ( "exception" ) == true ) ||
              ( qualifier_name.equals ( "focus" ) == true ) ||
              ( qualifier_name.equals ( "germline" ) == true ) ||
              ( qualifier_name.equals ( "haplotype" ) == true ) ||
              ( qualifier_name.equals ( "isolate" ) == true ) ||
              ( qualifier_name.equals ( "lab_host" ) == true ) ||
              ( qualifier_name.equals ( "macronuclear" ) == true ) ||
              ( qualifier_name.equals ( "mod_base" ) == true ) ||
              ( qualifier_name.equals ( "organism" ) == true ) ||
              ( qualifier_name.equals ( "PCR_conditions" ) == true ) ||
              ( qualifier_name.equals ( "phenotype" ) == true ) ||
              ( qualifier_name.equals ( "plasmid" ) == true ) ||
              ( qualifier_name.equals ( "pop_variant" ) == true ) ||
              ( qualifier_name.equals ( "rearranged" ) == true ) ||
              ( qualifier_name.equals ( "replace" ) == true ) ||
              ( qualifier_name.equals ( "serotype" ) == true ) ||
              ( qualifier_name.equals ( "sex" ) == true ) ||
              ( qualifier_name.equals ( "sequenced_mol" ) == true ) ||
              ( qualifier_name.equals ( "specific_host" ) == true ) ||
              ( qualifier_name.equals ( "specimen_voucher" ) == true ) ||
              ( qualifier_name.equals ( "sub_clone" ) == true ) ||
              ( qualifier_name.equals ( "sub_strain" ) == true ) ||
              ( qualifier_name.equals ( "strain" ) == true ) ||
              ( qualifier_name.equals ( "tissue_lib" ) == true ) ||
              ( qualifier_name.equals ( "tissue_type" ) == true ) ||
              ( qualifier_name.equals ( "transl_except" ) == true ) ||
              ( qualifier_name.equals ( "transl_table" ) == true ) ||
              ( qualifier_name.equals ( "usedin" ) == true ) ||
              ( qualifier_name.equals ( "variety" ) == true ) )
           ;

    else
      System.out.println ( "\tIgnoring: '" + qualifier_name + "' = " + value );
  }  // setQualifier


/******************************************************************************/
  private String getValue ( String name, boolean value )
  {
    // Check if the value is not set.
    if ( value == false )  return "";

    return "/" + name + "=" + value + " ";
  }  // method getValue


/******************************************************************************/
  private String getValue ( String name, int value )
  {
    // Check if the value is not set.
    if ( value == 0 )  return "";

    return "/" + name + "=" + value + " ";
  }  // method getValue


/******************************************************************************/
  private String getValue ( String name, String value )
  {
    // Check if the value is not set.
    if ( value.length () == 0 )  return "";

    return "/" + name + "=\"" + value + "\" ";
  }  // method getValue


/******************************************************************************/

}  // class GenBankFeature
