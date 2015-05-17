
// import FastaHeaders;
// import FastaSequence;
// import Position;
// import PositionIterator;
// import RefGenes;

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

public class RefLookup extends Object
{


/******************************************************************************/

  FastaHeaders fasta_headers = new FastaHeaders ();	// FASTA headers

  RefGenes ref_21_genes = new RefGenes ();		// UCSC reference genes

  RefGenes ref_22_genes = new RefGenes ();		// UCSC reference genes


/******************************************************************************/
public RefLookup ()
{
  initialize ();
  fasta_headers.setSize ( 954 );
  fasta_headers.loadHeaders ( "ref.headers" );
}  // constructor RefLookup


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void loadGenes ()
{
  ref_21_genes.loadGenes ( "refGene.chr21" );
  ref_22_genes.loadGenes ( "refGene.chr22" );
}  // method loadGenes


/******************************************************************************/
private String headerInfo ( String name )
{
  int hdr_index = fasta_headers.find ( name );
  FastaSequence header = fasta_headers.getHeader ( hdr_index );
  if ( header != null )  return ( "\t" + header.getHeaderInfo () );
  return "-\t-";
}  // method headerInfo


/******************************************************************************/
private String figureLocation ( int position, RefGenes ref_genes )
{
  int index = ref_genes.find ( position );

  // Check for the position within a gene.
  RefGene before_gene = ref_genes.getGene ( index );
  RefGene check_gene = null;
  int before_delta = 99999999;			// distance to end of previous gene
  int after_delta = before_delta;		// distance to start of next gene
  for ( int i = index; (i > index - 5) && (i >= 0); i-- )
  {
    check_gene = ref_genes.getGene ( i );
    if ( ( position >= check_gene.getTranscriptStart () ) &&
         ( position <= check_gene.getTranscriptEnd () ) )
    
      return "internal\t" + check_gene.toInfo () + headerInfo ( check_gene.getName () );

    if ( position - check_gene.getTranscriptEnd () < before_delta )  
    {
      before_delta = position - check_gene.getTranscriptEnd ();
      before_gene = check_gene;
    }  // if
  }  // for

  // Check if position before the first gene.
  if ( (index == 0 ) && ( position < before_gene.getTranscriptStart () ) )
  {
    after_delta = before_gene.getTranscriptStart () - position;

    if ( after_delta > 2000 )  return "intergenic\t-";
    if ( before_gene.getStrand () == '+' )
    {
      if ( after_delta <= 1000 )  
        return "5' gene 1K\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
      return "5' gene 2K\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
    }  // if
    else  // '-' strand
      return "3' gene\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
  }  // if

  RefGene after_gene = null;
  if ( index + 1 < ref_genes.getTotal () )
  {
    after_gene = ref_genes.getGene ( index + 1 );
    after_delta = after_gene.getTranscriptStart () - position;

    if ( before_delta < after_delta )
    {
      if ( before_delta > 2000 )  return "intergenic\t-";
      if ( before_gene.getStrand () == '-' )
      {
        if ( before_delta <= 1000 )  
          return "5' gene 1K\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
        return "5' gene 2K\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
      }  // if
      else  // '+' strand
        return "3' gene\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
    }  // if
    else  // following gene closer
    {
      if ( after_delta > 2000 )  return "intergenic\t-";
      if ( after_gene.getStrand () == '+' )
      {
        if ( after_delta <= 1000 )  
          return "5' gene 1K\t" + after_gene.toInfo () + headerInfo ( after_gene.getName () );
        return "5' gene 2K\t" + after_gene.toInfo () + headerInfo ( after_gene.getName () );
      }  // if
      else  // '-' strand
        return "3' gene\t" + after_gene.toInfo () + headerInfo ( after_gene.getName () );
    }  // else
  }  // if

  // At last gene.
  if ( before_delta > 2000 )  return "intergenic\t-";
  if ( before_gene.getStrand () == '-' )
  {
    if ( before_delta <= 1000 )  
      return "5' gene 1K\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
    return "5' gene 2K\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
  }  // if
  else  // '+' strand
    return "3' gene\t" + before_gene.toInfo () + headerInfo ( before_gene.getName () );
}  // method figureLocation


/******************************************************************************/
public void processFile ( String cel_filename ) 
{
  PositionIterator iterator = new PositionIterator ( cel_filename );
  Position position = null;

  int index = 0;
  RefGene ref_gene = null;
  while ( iterator.isEndOfFile () == false )
   {
     position = iterator.next ();

     if ( position != null )
     {
       System.out.print ( position.toString () + "\t" + index + "\t" );

       if ( position.getChromosome ().equals ( "chr21" ) == true )
         System.out.println ( figureLocation ( position.getPosition (), ref_21_genes ) );
       else
         System.out.println ( figureLocation ( position.getPosition (), ref_22_genes ) );
     }  // if
   }  // while

  // Close the files.
  iterator.closeFile ();
}  // method processFile 


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java RefLookup <filename>" );
    System.out.println ();
    System.out.print   ( "where <filename> is the filename of the " );
    System.out.println ( "Affymetrix CEL position information file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    RefLookup app = new RefLookup ();

    if ( args.length == 0 )
      app.usage ();
    else
    {
      app.loadGenes ();
      app.processFile ( args [ 0 ] );
    }  // else
  }  // method main

/******************************************************************************/

}  //class RefLookup
