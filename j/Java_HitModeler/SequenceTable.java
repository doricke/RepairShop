

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

public class SequenceTable extends Object
{


/******************************************************************************/

  private   String  accession = "";		// accession

  private   String  chromosome = "";		// chromosome

  private   long  dataset_id = 0;		// dataset_id

  private   String  evidence = "";		// evidence

  private   long  gene_id = 0;			// gene_id

  private   String  is_3_complete = "";		// is_3_complete

  private   String  is_5_complete = "";		// is_5_complete

  private   String  is_inside_complete = "";	// is_inside_complete

  private   long  organism_id = 0;		// organism_id

  private   String  sequence = "";		// sequence

  private   String  sequence_description = "";	// sequence_description

  private   long  sequence_id = 0;		// sequence_id

  private   long  sequence_length = 0;		// sequence_length

  private   String  sequence_name = "";		// sequence_name

  private   String  sequence_type = "";		// sequence_type

  private   long  source_id = 0;		// source_id

  private   String  splice_info = "";		// splice_info


/******************************************************************************/
  // Constructor SequenceTable
  public SequenceTable ()
  {
    initialize ();
  }  // constructor SequenceTable


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    accession = "";
    chromosome = "";
    dataset_id = 0;
    evidence = "";
    gene_id = 0;
    is_3_complete = "";
    is_5_complete = "";
    is_inside_complete = "";
    organism_id = 0;
    sequence = "";
    sequence_description = "";
    sequence_id = 0;
    sequence_length = 0;
    sequence_name = "";
    sequence_type = "";
    source_id = 0;
    splice_info = "";
  }  // method initialize 


/******************************************************************************/
  public String getAccession ()
  {
    return accession;
  }  // method getAccession


/******************************************************************************/
  public String getChromosome ()
  {
    return chromosome;
  }  // method getChromosome


/******************************************************************************/
  public long getDatasetId ()
  {
    return dataset_id;
  }  // method getDatasetId


/******************************************************************************/
  public String getEvidence ()
  {
    return evidence;
  }  // method getEvidence


/******************************************************************************/
  public long getGeneId ()
  {
    return gene_id;
  }  // method getGeneId


/******************************************************************************/
  public char getGenomicStrand ()
  {
    int index = sequence_description.indexOf ( "Complete" );
    if ( index > 0 )
      if ( sequence_description.length () > index + 9 )
        return sequence_description.charAt ( index + 9 );

    index = sequence_description.indexOf ( "Partial" );
      
    if ( index > 0 )  
      if ( sequence_description.length () > index + 8 )
        return sequence_description.charAt ( index + 8 );

    System.out.println ( "SequenceTable.getGenomicStrand: " + 
        "*Warning* no strand found for " + sequence_name + " " + sequence_description );
    return '+';
  }  // method getGenomicStrand


/******************************************************************************/
  public String getIs3Complete ()
  {
    return is_3_complete;
  }  // method getIs3Complete


/******************************************************************************/
  public String getIs5Complete ()
  {
    return is_5_complete;
  }  // method getIs5Complete


/******************************************************************************/
  public String getIsInsideComplete ()
  {
    return is_inside_complete;
  }  // method getIsInsideComplete


/******************************************************************************/
  public long getOrganismId ()
  {
    return organism_id;
  }  // method getOrganismId


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


/******************************************************************************/
  public String getSequenceDescription ()
  {
    return sequence_description;
  }  // method getSequenceDescription


/******************************************************************************/
  public long getSequenceId ()
  {
    return sequence_id;
  }  // method getSequenceId


/******************************************************************************/
  public long getSequenceLength ()
  {
    return sequence_length;
  }  // method getSequenceLength


/******************************************************************************/
  public String getSequenceName ()
  {
    return sequence_name;
  }  // method getSequenceName


/******************************************************************************/
  public String getSequenceType ()
  {
    return sequence_type;
  }  // method getSequenceType


/******************************************************************************/
  public long getSourceId ()
  {
    return source_id;
  }  // method getSourceId


/******************************************************************************/
  public String getSpliceInfo ()
  {
    return splice_info;
  }  // method getSpliceInfo


/******************************************************************************/
  public boolean is3Complete ()
  {
    if ( ( is_3_complete.equals ( "TRUE" ) == true ) ||
         ( is_3_complete.equals ( "true" ) == true ) )

      return true;

    return false;
  }  // method is3Complete


/******************************************************************************/
  public boolean is5Complete ()
  {
    if ( ( is_5_complete.equals ( "TRUE" ) == true ) ||
         ( is_5_complete.equals ( "true" ) == true ) )

      return true;

    return false;
  }  // method is5Complete


/******************************************************************************/
  public void setAccession ( String value )
  {
    accession = value;
  }  // method setAccession


/******************************************************************************/
  public void setChromosome ( String value )
  {
    chromosome = value;
  }  // method setChromosome


/******************************************************************************/
  public void setDatasetId ( long value )
  {
    dataset_id = value;
  }  // method setDatasetId


/******************************************************************************/
  public void setDescriptionSpliceInfo ()
  {
    if ( sequence_description == null )  return;

    int index = sequence_description.indexOf ( "Exons[" );
    if ( index < 0 )  return;

    splice_info = sequence_description.substring ( index + 6 );

    index = splice_info.indexOf ( "]" );
    if ( index > 0 )  splice_info = splice_info.substring ( 0, index );
  }  // method setDescriptionSpliceInfo


/******************************************************************************/
  public void setEvidence ( String value )
  {
    evidence = value;
  }  // method setEvidence


/******************************************************************************/
  public void setGeneId ( long value )
  {
    gene_id = value;
  }  // method setGeneId


/******************************************************************************/
  public void setIs3Complete ( String value )
  {
    is_3_complete = value;
  }  // method setIs3Complete


/******************************************************************************/
  public void setIs5Complete ( String value )
  {
    is_5_complete = value;
  }  // method setIs5Complete


/******************************************************************************/
  public void setIsInsideComplete ( String value )
  {
    is_inside_complete = value;
  }  // method setIsInsideComplete


/******************************************************************************/
  public void setOrganismId ( long value )
  {
    organism_id = value;
  }  // method setOrganismId


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;
  }  // method setSequence


/******************************************************************************/
  public void setSequenceDescription ( String value )
  {
    sequence_description = value;
  }  // method setSequenceDescription


/******************************************************************************/
  public void setSequenceId ( long value )
  {
    sequence_id = value;
  }  // method setSequenceId


/******************************************************************************/
  public void setSequenceLength ( long value )
  {
    sequence_length = value;
  }  // method setSequenceLength


/******************************************************************************/
  public void setSequenceName ( String value )
  {
    sequence_name = value;
  }  // method setSequenceName


/******************************************************************************/
  public void setSequenceType ( String value )
  {
    sequence_type = value;
  }  // method setSequenceType


/******************************************************************************/
  public void setSourceId ( long value )
  {
    source_id = value;
  }  // method setSourceId


/******************************************************************************/
  public void setSpliceInfo ( String value )
  {
    splice_info = value;
  }  // method setSpliceInfo


/******************************************************************************/
  public void print ()
  {
    System.out.println 
        ( "Seq: "
        + sequence_id
        + " "
        + sequence_name
        + " len "
        + sequence_length
        + " " 
        + sequence_type
        + " " 
        + sequence_description
        );
    // SeqTools.writeFasta ( sequence );
    System.out.println ();
  }  // method print


/******************************************************************************/

}  // class SequenceTable
