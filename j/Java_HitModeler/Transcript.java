
import java.util.Vector;

// import Domain;
// import Elements;
// import ExonTable;
// import SequenceTable;
// import Hit;

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

public class Transcript extends Object
{


/******************************************************************************/

  private   boolean  complete = false;			// is complete flag

  private   int  contigs_spanned = 1;			// count of genomic sequences spanned

  private   String  description = "";			// transcript description

  private   Elements elements = null;			// Gene elements

  private   int  evidence_length = 0;			// bp sum of evidence

  private   ExonTable [] exons = null;			// Main contig exons for this transcript

  private   String  gene_description = "";		// gene description

  private   int  genomic_begin = 0;			// start of transcript in genomic sequence

  private   int  genomic_end = 0;			// end of transcript in genomic sequence

  private   Domain []  domains = null;			// protein domains

  private   GenePrediction  gene_prediction = null;	// mRNA sequence table

  private   SequenceTable  gene_sequence = null;	// gene prediction sequence

  private   String  mRNA_sequence = null;		// mRNA sequence

  private   String  name = "";				// transcript name

  private   SequenceTable  protein_sequence = null;	// protein sequence 

  private   long  query_sequence_id = 0L;		// query_sequence_id

  private   Hit [] similarities = null;		// similarities for this transcript

  private   String  splice_info = "";			// splicing shorthand notation

  private   char  strand = '+';				// transcript strand

  private   String  transcript_type = "";		// transcript type


/******************************************************************************/
  // Constructor Transcript
  public Transcript ()
  {
    initialize ();
  }  // constructor Transcript


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    complete = false;
    contigs_spanned = 1;
    description = "";
    elements = null;
    evidence_length = 0;
    exons = null;
    gene_description = "";
    gene_sequence = null;
    genomic_begin = 0;
    genomic_end = 0;
    domains = null;
    gene_prediction = null;
    mRNA_sequence = null;
    name = "";
    protein_sequence = null;
    query_sequence_id = 0L;
    similarities = null;
    splice_info = "";
    strand = '+';
    transcript_type = "";
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    if ( exons != null )
      for ( int e = 0; e < exons.length; e++ )
        exons [ e ] = null;

    if ( domains != null )
      for ( int m = 0; m < domains.length; m++ )
        domains [ m ] = null;

    if ( similarities != null )
      for ( int s = 0; s < similarities.length; s++ )
        similarities [ s ] = null;

    initialize ();
  }  // method close 


/******************************************************************************/
  private void checkForRepeat ( String description )
  {
    if ( ( description == null ) || ( description.length () < 0 ) )  return;

    String desc = description;

    int index = desc.indexOf ( "some members" );
    if ( index != -1 )  desc= desc.substring ( 0, index );

    index = desc.indexOf ( "have a weak" );
    if ( index != -1 )  desc= desc.substring ( 0, index );

    if ( desc.indexOf ( "retroelement" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "reverse transcriptase" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "reversetranscriptase" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "retrovirus" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "retroviral" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "retrotrans" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "transposon" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "gypsy" ) != -1 )  transcript_type = "repeat";
    if ( desc.indexOf ( "copia" ) != -1 )  transcript_type = "repeat";
  }  // method checkForRepeat


/******************************************************************************/
  public boolean getComplete ()
  {
    // Check for the start & stop codons.
    if ( elements != null )
    {
      if ( ( elements.getStartCodon () > 0 ) &&
           ( elements.getStopCodon () > 0 ) )

        complete = true;

      else

        complete = false;
    }  // if

    return complete;
  }  // method getComplete


/******************************************************************************/
  public int getContigsSpanned ()
  {
    return contigs_spanned;
  }  // method getContigsSpanned


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public Elements getElements ()
  {
    return elements;
  }  // method getElements


/******************************************************************************/
  public int getEvidenceLength ()
  {
    return evidence_length;
  }  // method getEvidenceLength


/******************************************************************************/
  public ExonTable [] getExons ()
  {
    return exons;
  }  // method getExons


/******************************************************************************/
  public String getGeneDescription ()
  {
    return gene_description;
  }  // method getGeneDescription


/******************************************************************************/
  public SequenceTable getGeneSequence ()
  {
    return gene_sequence;
  }  // method getGeneSequence


/******************************************************************************/
  public int getGenomicBegin ()
  {
    return genomic_begin;
  }  // method getGenomicBegin


/******************************************************************************/
  public int getGenomicEnd ()
  {
    return genomic_end;
  }  // method getGenomicEnd


/******************************************************************************/
  public int getLength ()
  {
    if ( mRNA_sequence != null )  return mRNA_sequence.length ();

    return 0;
  }  // method getLength


/******************************************************************************/
  public Domain getDomain ( int index )
  {
    // Validate domains.
    if ( domains == null )  return null;

    // Validate index.
    if ( ( index < 0 ) || ( index >= domains.length ) )  return null;

    return domains [ index ];
  }  // method getDomain


/******************************************************************************/
  public String getDomainDescriptions ()
  {
    StringBuffer descriptions = new StringBuffer ( 1024 );
    descriptions.setLength ( 0 );
    descriptions.append ( "Domains{" );

    if ( ( domains != null ) && ( domains.length > 0 ) )
    {
      for ( int m = 0; m < domains.length; m++ )
      {
        descriptions.append ( domains [ m ].toString () );
        if ( m < domains.length - 1 )  descriptions.append ( "; " );
      }  // for
    }  // if

    descriptions.append ( "}" );

    if ( descriptions.length () < 10 )  descriptions.setLength ( 0 );

    return descriptions.toString ();
  }  // method getDomainDescriptions


/******************************************************************************/
  public Domain [] getDomains ()
  {
    return domains;
  }  // method getDomains


/******************************************************************************/
  public GenePrediction getGenePrediction ()
  {
    return gene_prediction;
  }  // method getGenePrediction


/******************************************************************************/
  public String getMrnaSequence ()
  {
    return mRNA_sequence;
  }  // method getMrnaSequence


/******************************************************************************/
  public String getName ()
  {
    if ( ( name == null ) || ( name.length () <= 0 ) )  return "";

    int index = name.indexOf ( ".mrna" );
    if ( index != -1 )  name = name.substring ( 0, index );

    return name;
  }  // method getName


/******************************************************************************/
  public SequenceTable getProteinSequence ()
  {
    return protein_sequence;
  }  // method getProteinSequence


/******************************************************************************/
  public long getQuerySequenceId ()
  {
    return query_sequence_id;
  }  // method getQuerySequenceId


/******************************************************************************/
  public Hit [] getSimilarities ()
  {
    return similarities;
  }  // method getSimilarities


/******************************************************************************/
  public String getSpliceInfo ()
  {
    return splice_info;
  }  // method getSpliceInfo


/******************************************************************************/
  private int getStart ()
  {
    // return start;
    return 0;
  }  // method getStart


/******************************************************************************/
  public char getStrand ()
  {
    if ( ( exons != null ) && ( exons.length > 0 ) )
    
      strand = exons[ 0 ].getGenomicStrand ();

    return strand;
  }  // method getStrand


/******************************************************************************/
  public String getTranscriptType ()
  {
    return transcript_type;
  }  // method getTranscriptType


/******************************************************************************/
  public boolean isComplete ()
  {
    return getComplete ();
  }  // method isComplete


/******************************************************************************/
  // This method returns true if Domains are present.
  public boolean isDomains ()
  {
    if ( domains == null )  return false;
    if ( domains.length <= 0 )  return false;

    return true;
  }  // method isDomains


/******************************************************************************/
  public void setComplete ( boolean value )
  {
    complete = value;
  }  // method setComplete


/******************************************************************************/
  public void setDescription ( String value )
  {
    description = value;

    if ( description == null )  description = "";

    checkForRepeat ( description );
  }  // setDescription


/******************************************************************************/
  public void setElements ( Elements value )
  {
    elements = value;
  }  // method setElements


/******************************************************************************/
  public void setEvidenceLength ( int value )
  {
    evidence_length = value;
  }  // method setEvidenceLength


/******************************************************************************/
  public void setExons ( ExonTable [] value )
  {
    exons = value;
  }  // method setExons


/******************************************************************************/
  public void setExons ( Vector value )
  {
    exons = null;

    if ( value != null )
    {
      if ( value.size () > 0 )
      {
        exons = new ExonTable [ value.size () ];
        for ( int e = 0; e < value.size (); e++ )
        {
          exons [ e ] = (ExonTable) value.elementAt ( e );
        }  // for
      }  // if
    }  // if
  }  // method setExons


/******************************************************************************/
  public void setGeneDescription ( String value )
  {
    gene_description = value;

    checkForRepeat ( gene_description );
  }  // method setGeneDescription


/******************************************************************************/
  public void setGeneSequence ( SequenceTable value )
  {
    gene_sequence = value;

    // Set the complete flag.
    if ( gene_sequence != null )

      complete = gene_sequence.is5Complete () && gene_sequence.is3Complete ();
  }  // method setGeneSequence


/******************************************************************************/
  public void setDomains ( Domain [] value )
  {
    domains = value;
  }  // method setDomains


/******************************************************************************/
  public void setGenePrediction ( GenePrediction value )
  {
    gene_prediction = value;
  }  // method setGenePrediction


/******************************************************************************/
  public void setMrnaSequence ( String value )
  {
    mRNA_sequence = value;
  }  // method setMrnaSequence


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;

    if ( name == null )  name = "";
  }  // method setName


/******************************************************************************/
  public void setProteinSequence ( SequenceTable value )
  {
    protein_sequence = value;
  }  // method setProteinSequence


/******************************************************************************/
  public void setQuerySequenceId ( long value )
  {
    query_sequence_id = value;
  }  // method setQuerySequenceId


/******************************************************************************/
  public void setSimilarities ( Hit [] value )
  {
    similarities = value;
  }  // method setSimilarities


/******************************************************************************/
  public void setSimilarities ( Vector value )
  {
    if ( ( value == null ) || ( value.size () <= 0 ) )
    {
      similarities = null;
      return;
    }  // if

    // Convert the vector to an array.
    similarities = new Hit [ value.size () ];

    for ( int s = 0; s < value.size (); s++ )

      similarities [ s ] = (Hit) value.elementAt ( s );
  }  // method setSimilarities


/******************************************************************************/
  public void setSpliceInfo ( String value )
  {
    splice_info = value;
  }  // method setSpliceInfo


/******************************************************************************/
  public void setStrand ( char value )
  {
    strand = value;
  }  // method setStrand


/******************************************************************************/
  public void setTranscriptType ( String value )
  {
    if ( ( transcript_type == null ) || ( transcript_type.length () <= 0 ) )
    {
      transcript_type = value;
    }
    else
    {
      if ( value.equals ( "bacterial" ) == true )  transcript_type = value;

      if ( transcript_type.equals ( "bacterial" ) != true )

        transcript_type = value;
    }  // else
  }  // method setTranscriptType


/******************************************************************************/
  public void sumEvidence ()
  {
    evidence_length = 0;
    Evidence evidence = null;

    // Sum the evidence from the main contig.
    if ( ( exons != null ) && ( exons.length > 0 ) )

      for ( int e = 0; e < exons.length; e++ )
      {
        if ( exons [ e ] != null )

          evidence = exons [ e ].getEvidence ();
  
        if ( evidence != null )
        {
          if ( evidence.getBegin () > 0 )
  
            evidence_length += evidence.getEnd () - evidence.getBegin () + 1;
        }  // if
      }  // for
  }  // method sumEvidence


/******************************************************************************/
  // This method computes the genomic span of this transcript
  public void setGenomicEnds ()
  {
    // Check if exons exist for this transcript.
    if ( ( exons == null ) || ( exons.length <= 0 ) )  return;

    // Traverse the exons for this transcript.
    for ( int e = 0; e < exons.length; e++ )

      if ( exons [ e ] != null )
      {
        // Check for a lower genomic start.
        if ( ( genomic_begin == 0 ) || 
             ( exons [ e ].getGenomicStart () < genomic_begin ) )
  
          genomic_begin = (int) exons [ e ].getGenomicStart ();
  
        if ( exons [ e ].getGenomicEnd () < genomic_begin )
  
          genomic_begin = (int) exons [ e ].getGenomicEnd ();
  
  
        // Check for a higher genomic end.
        if ( ( genomic_end == 0 ) || 
             ( exons [ e ].getGenomicEnd () > genomic_end ) )

          genomic_end = (int) exons [ e ].getGenomicEnd ();

        if ( exons [ e ].getGenomicStart () > genomic_end )

          genomic_end = (int) exons [ e ].getGenomicStart ();
      }  // if
  }  // method setGenomicEnds


/******************************************************************************/
  public void searchForElements ()
  {
    // Check if start or stop codon is known.
    if ( gene_sequence == null )  return;
    if ( ( gene_sequence.is5Complete () == false ) &&
         ( gene_sequence.is3Complete () == false ) )  return;

    if ( elements == null )  elements = new Elements ();

    // Check for an exon.
    if ( ( exons == null ) || ( exons.length <= 0 ) )  return;

    // Check if genomic coordinates are known.
    if ( ( genomic_begin == 0 ) || ( genomic_end == 0 ) )
      setGenomicEnds ();

    SequenceTable genomic_sequence = exons [ 0 ].getGenomicSequence ();
    if ( genomic_sequence == null )  return;

    elements.setPromoterSequence ( genomic_sequence );
    char strand = exons [ 0 ].getGenomicStrand ();

/*
    System.out.println ();
    System.out.println ( "Transcript.searchForElements: " + name + " strand " + strand
        + " {" + genomic_begin + "-" + genomic_end + "}" );
*/
    if ( strand == '+' )
    {
      String sequence = genomic_sequence.getSequence ();
      elements.setSequence ( sequence );

      if ( gene_sequence.is5Complete () == true )
      {
        elements.findPromoterElements ( sequence, genomic_begin );
        elements.checkStartCodon ( genomic_begin - 1 );
      }  // if

      if ( gene_sequence.is3Complete () == true )
      {
        elements.setEndSequence ( genomic_sequence.getSequence () );
        elements.checkStopCodon ( genomic_end - 3 );
        elements.findPolyASite ( genomic_end );
      }  // if
    }  // if
    else  // minus strand
    {
      String reverse = SeqTools.reverseSequence ( genomic_sequence.getSequence () );

      if ( gene_sequence.is5Complete () == true )
      {
        elements.setSequence ( reverse );
        elements.findPromoterElements ( reverse, reverse.length () - genomic_end );
        elements.checkStartCodon ( reverse.length () - genomic_end );
        elements.reversePromoterCoordinates ();
      }  // if

      if ( gene_sequence.is3Complete () == true )
      {
        elements.setEndSequence ( reverse );
        elements.checkStopCodon ( reverse.length () - genomic_begin - 2 );
        elements.findPolyASite ( reverse.length () - genomic_begin );
        elements.reverseEndCoordinates ();
      }  // if
    }  // else
  }  // method searchForElements


/******************************************************************************/
  public String getExonsDetails ( ExonTable [] exons )
  {
    StringBuffer details = new StringBuffer ( 160 );

    if ( ( exons == null ) || ( exons.length <= 0 ) )  return details.toString ();

    details.append ( "\t" + exons [ 0 ].getGenomicStrand () + " [" );
  
    for ( int e = 0; e < exons.length; e++ )
    {
      if ( exons [ e ] != null )

        details.append ( exons [ e ].getGenomicStart () + "-" 
            + exons [ e ].getGenomicEnd () );
  
      if ( e < exons.length - 1 )  details.append ( ", " );
    }  // for
    details.append ( "] " );
  
    return details.toString ();
  }  // method getExonsDetails


/******************************************************************************/
  public void print ()
  {
    System.out.print ( "Transcript: " );

    if ( gene_sequence != null )
    {
      System.out.print ( "5' " + gene_sequence.getIs5Complete () + "-" );
      System.out.print ( gene_sequence.getIs5Complete () + " 3' complete, " );
      System.out.print ( gene_sequence.getEvidence () );
      System.out.println ();
    }  // if

    System.out.print ( "\t" + transcript_type );

    if ( ( mRNA_sequence != null ) && ( mRNA_sequence.length () > 0 ) )
    {
      int percent = ( evidence_length * 100 ) / mRNA_sequence.length ();
      System.out.print ( " Evidence " + percent + "% (" 
          + evidence_length + "/" + mRNA_sequence.length () + ")" );
    }  // if

    if ( query_sequence_id != 0L )
      System.out.print ( " qid = " + query_sequence_id );

    if ( gene_sequence != null )
      System.out.print ( ", " + gene_sequence.getSequenceName () );

    System.out.print ( " Exons[" + splice_info + "]" );

    System.out.println ();

    // List the Gene structure elements.
    if ( elements != null )  elements.print ();

    // List relevant domains.
    if ( domains != null )
    {
      if ( domains.length > 0 )
      {
        for ( int m = 0; m < domains.length; m++ )
        
          domains [ m ].print ();
      }  // if
    }  // if

    String details = getExonsDetails ( exons );
    if ( details.length () > 0 )  System.out.println ( details );

    if ( ( exons == null ) || ( exons.length <= 0 ) )
      System.out.println ( "*** No exons in Transcript." );

    if ( gene_description.length () > 0 )  
      System.out.println ( "\tGene: " + gene_description );

    // System.out.println ( "\tmRNA: " + mRNA_sequence );
    System.out.println ();
  }  // method print


/******************************************************************************/

}  // class Transcript
