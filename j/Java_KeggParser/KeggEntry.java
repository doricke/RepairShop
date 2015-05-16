

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

public class KeggEntry extends Object
{


/******************************************************************************/

  private   String  aa_sequence = "";	// Amino acid sequence

  private   String  codon_usage = "";	// Codon_usage lines

  private   String  dblinks = "";	// DBLINKS lines

  private   String  definition = "";	// Gene description

  private   String  entry = "";		// Entry header

  private   String  gene_id = "";	// NCBI Entrez gene_id

  private   String  motif = "";		// Motif lines

  private   String  name = "";		// Gene symbol name

  private   String  ncbi_gi = "";	// NCBI-GI identifier

  private   String  nt_sequence = "";	// Nucleotide sequence

  private   String  omim_id = "";	// OMIM identifier

  private   String  ortholog = "";	// Ortholog

  private   String  pathway = "";	// Pathway

  private   String  position = "";	// Chromosome map position

  private   String  uniprot_id = "";	// Uniprot identifier


/******************************************************************************/
  // Constructor KeggEntry
  public KeggEntry ()
  {
    initialize ();
  }  // constructor KeggEntry


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    aa_sequence = "";
    codon_usage = "";
    dblinks = "";
    definition = "";
    entry = "";
    gene_id = "";
    motif = "";
    name = "";
    ncbi_gi = "";
    nt_sequence = "";
    omim_id = "";
    ortholog = "";
    pathway = "";
    position = "";
    uniprot_id = "";
  }  // method initialize 


/******************************************************************************/
  public String getAaSequence ()
  {
    return aa_sequence;
  }  // method getAaSequence


/******************************************************************************/
  public String getCodonUsage ()
  {
    return codon_usage;
  }  // method getCodonUsage


/******************************************************************************/
  public String getDblinks ()
  {
    return dblinks;
  }  // method getDblinks


/******************************************************************************/
  public String getDefinition ()
  {
    return definition;
  }  // method getDefinition


/******************************************************************************/
  public String getEntry ()
  {
    return entry;
  }  // method getEntry


/******************************************************************************/
  public String getGeneId ()
  {
    return gene_id;
  }  // method getGeneId


/******************************************************************************/
  public String getMotif ()
  {
    return motif;
  }  // method getMotif


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public String getNcbiGi ()
  {
    return ncbi_gi;
  }  // method getNcbiGi


/******************************************************************************/
  public String getNtSequence ()
  {
    return nt_sequence;
  }  // method getNtSequence


/******************************************************************************/
  public String getOmimId ()
  {
    return omim_id;
  }  // method getOmimId


/******************************************************************************/
  public String getOrtholog ()
  {
    return ortholog;
  }  // method getOrtholog


/******************************************************************************/
  public String getPosition ()
  {
    return position;
  }  // method getPosition


/******************************************************************************/
  public String getUniprotId ()
  {
    return uniprot_id;
  }  // method getUniprotId


/******************************************************************************/
  public void setAaSequence ( String value )
  {
    if ( ( value.charAt ( 0 ) >= '0' ) && ( value.charAt ( 0 ) <= '9' ) )  return;

    aa_sequence += value;
  }  // method setAaSequence


/******************************************************************************/
  public void setCodonUsage ( String value )
  {
    codon_usage += value;
  }  // method setCodonUsage


/******************************************************************************/
  public void setDblinks ( String value )
  {
    dblinks += value + " ";
    if ( value.startsWith ( "OMIM" ) == true )  
      setOmimId ( value.substring ( 6 ) );
    if ( value.startsWith ( "NCBI-GI" ) == true )  
      setNcbiGi ( value.substring ( 9 ) );
    if ( value.startsWith ( "NCBI-GeneID" ) == true )  
      setGeneId ( value.substring ( 13 ) );
    if ( value.startsWith ( "UniProt" ) == true )  
      setUniprotId ( value.substring ( 9 ) );
  }  // method setDblinks


/******************************************************************************/
  public void setDefinition ( String value )
  {
    definition += value;
  }  // method setDefinition


/******************************************************************************/
  public void setEntry ( String value )
  {
    entry = value;
  }  // method setEntry


/******************************************************************************/
  public void setGeneId ( String value )
  {
    gene_id = value;
  }  // method setGeneId


/******************************************************************************/
  public void setMotif ( String value )
  {
    motif += value;
  }  // method setMotif


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setNcbiGi ( String value )
  {
    ncbi_gi = value;
  }  // method setNcbiGi


/******************************************************************************/
  public void setNtSequence ( String value )
  {
    if ( ( value.charAt ( 0 ) >= '0' ) && ( value.charAt ( 0 ) <= '9' ) )  return;

    nt_sequence += value;
  }  // method setNtSequence


/******************************************************************************/
  public void setOmimId ( String value )
  {
    omim_id = value;
  }  // method setOmimId


/******************************************************************************/
  public void setOrtholog ( String value )
  {
    ortholog += value;
  }  // method setOrtholog


/******************************************************************************/
  public void setPathway ( String value )
  {
    pathway += value;
  }  // method setPathway


/******************************************************************************/
  public void setPosition ( String value )
  {
    position = value;
  }  // method setPosition


/******************************************************************************/
  public void setUniprotId ( String value )
  {
    uniprot_id = value;
  }  // method setUniprotId


/******************************************************************************/
  public void snap ()
  {
    System.out.println ( "AASEQ\t" + aa_sequence );
    System.out.println ( "CODON_USAGE\t" + codon_usage );
    System.out.println ( "DBLINKS\t" + dblinks );
    System.out.println ( "DEFINITION\t" + definition );
    System.out.println ( "ENTRY\t" + entry );
    System.out.println ( "Gene-id\t" + gene_id );
    System.out.println ( "MOTIF\t" + motif );
    System.out.println ( "NAME\t" + name );
    System.out.println ( "ncbi_gi\t" + ncbi_gi );
    System.out.println ( "NTSEQ\t" + nt_sequence );
    System.out.println ( "OMIM\t" + omim_id );
    System.out.println ( "ORTHOLOG\t" + ortholog );
    System.out.println ( "PATHWAY\t" + pathway );
    System.out.println ( "POSITION\t" + position );
    System.out.println ( "UniProt\t" + uniprot_id );
  }  // method snap


/******************************************************************************/
  public String toString ()
  {
    String line = name + "\t" + position + "\t" + uniprot_id + "\t" + gene_id 
        + "\t" + ncbi_gi + "\t" + omim_id + "\t"  + "\t" + definition;
    return line;
  }  // method toString


/******************************************************************************/

}  // class KeggEntry
