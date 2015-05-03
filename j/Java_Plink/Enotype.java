
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

// import Alleles;
// import OutputFile;
// import PhenotypeIterator;
// import Tokens;
// import TpedIterator;

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

public class Enotype extends Object
{

  int [] pheno_class = null;		// phenotype classification

  float [] phenotypes = null;		// phenotype values


/******************************************************************************/
public Enotype ()
{
  initialize ();
}  // constructor Enotype


/******************************************************************************/
private void initialize ()
{
  pheno_class = null;
  phenotypes = null;
}  // method initialize


/******************************************************************************/
private void readPhenotypes ( String file_name, int phenotype_column )
{
  // Read in the phenotypes for the specified column.
  PhenotypeIterator reader = new PhenotypeIterator ();
  reader.setFileName ( file_name );
  reader.openFile ();
  reader.readPhenotypes ( phenotype_column );
  reader.closeFile ();

  phenotypes = reader.getPhenotypes ();
  pheno_class = reader.classifyPhenotypes ();

  reader.initialize ();
}  // method readPhenotypes


/******************************************************************************/
private void typeBlocks 
    ( int block
    , Vector genotypes
    , int [] pheno_class
    , OutputFile out_blocks 
    )
{
  int cur_allele = 0;			// current allele type
  int count = 0;			// key value - count of this key
  StringBuffer haploblock = new StringBuffer ( 20 );
  String block_key = "";		// block hash key 
  int block_value = 0;			// block hash value
  boolean skip_flag = false;		// skip haplotype flag

  // Assert: genotypes.
  if ( genotypes.size () <= 0 )  return;

  HashMap<String,Integer> block_types = new HashMap<String,Integer>();
  Tokens [] gtypes = new Tokens [ genotypes.size () ];

  out_blocks.print ( block + " " );
  for ( int i = 0; i < genotypes.size (); i++ )
  {
    gtypes [ i ] = (Tokens) genotypes.elementAt ( i );
    out_blocks.print ( gtypes [ i ].getToken ( 1 ) );
    if ( i < genotypes.size () - 1 )  out_blocks.print ( "," );
  }  // for
  out_blocks.print ( " " );

  for ( int allele_type = 1; allele_type <= 6; allele_type++ )
  {
    block_types.clear ();
    out_blocks.print ( " Type" + allele_type + "[ " );

    // Add the haploblocks for this allele type.
    for ( int i = 4; i < gtypes [ 0 ].getTotal (); i++ )

      if ( pheno_class [ i ] == allele_type )
      {
        haploblock.setLength ( 0 );
        skip_flag = false;
        for ( int j = 0; j < genotypes.size (); j++ )
        {
          cur_allele = Alleles.getAllelesType ( gtypes [ j ].getToken ( i ) );
          haploblock.append ( cur_allele );
          if ( cur_allele == 0 )  skip_flag = true;
        }  // for

        if ( skip_flag == false )
        {
          block_key = haploblock.toString ();
          if ( block_types.containsKey ( block_key ) == true )
          {
            block_value = block_types.get ( block_key) + 1;
            block_types.put ( block_key, block_value );
          }
          else
            block_types.put( block_key, 1 );
        }  // if
      }  // if

    // Write out the haploblocks for this allele type.
    Set<String> keys = block_types.keySet();
    for ( String key : keys )
    {
      count = block_types.get ( key );
      out_blocks.print ( key + ":" + count + " " );
    }  // for

    out_blocks.print ( "]" );
  }  // for

  out_blocks.println ();
}  // method typeBlocks


/******************************************************************************/
private void typeBlocks2 
    ( int block
    , Vector genotypes
    , int [] pheno_class
    , OutputFile out_blocks 
    )
{
  int cur_allele = 0;			// current allele type
  int count = 0;			// key value - count of this key
  StringBuffer haploblock = new StringBuffer ( 20 );
  String block_key = "";		// block hash key 
  int block_value = 0;			// block hash value
  boolean skip_flag = false;		// skip haplotype flag
  StringBuffer snps = new StringBuffer ( 256 );		// SNP identifiers
  boolean snps_written = false;		// SNP identifiers written to file flag


  // Assert: genotypes.
  if ( genotypes.size () <= 0 )  return;

  // HashMap<String,Integer> [] block_types = new HashMap<String,Integer> [ 7 ];
  HashMap<String,Integer> [] block_types = new HashMap [ 7 ];
  for ( int allele_type = 0; allele_type <= 6; allele_type++ )
    block_types [ allele_type ] = new HashMap<String,Integer>();

  Tokens [] gtypes = new Tokens [ genotypes.size () ];

  // Write out the block number and SNP identifiers.
  snps.append ( block + " " );
  for ( int i = 0; i < genotypes.size (); i++ )
  {
    gtypes [ i ] = (Tokens) genotypes.elementAt ( i );
    snps.append ( gtypes [ i ].getToken ( 1 ) );
    if ( i < genotypes.size () - 1 )  snps.append ( "," );
  }  // for
  snps.append ( " " );

  for ( int allele_type = 1; allele_type <= 6; allele_type++ )
  {
    // block_types [ allele_type ].clear ();
    // out_blocks.print ( " Type" + allele_type + "[ " );

    // Add the haploblocks for this allele type.
    for ( int i = 4; i < gtypes [ 0 ].getTotal (); i++ )

      if ( pheno_class [ i ] == allele_type )
      {
        haploblock.setLength ( 0 );
        skip_flag = false;
        for ( int j = 0; j < genotypes.size (); j++ )
        {
          cur_allele = Alleles.getAllelesType ( gtypes [ j ].getToken ( i ) );
          haploblock.append ( cur_allele );
          if ( cur_allele == 0 )  skip_flag = true;
        }  // for

        if ( skip_flag == false )
        {
          block_key = haploblock.toString ();
          if ( block_types [ allele_type ].containsKey ( block_key ) == true )
          {
            block_value = block_types [ allele_type ].get ( block_key) + 1;
            block_types [ allele_type ].put ( block_key, block_value );
          }
          else
            block_types [ allele_type ].put( block_key, 1 );

          // Combine all allele types in index 0.
          if ( block_types [ 0 ].containsKey ( block_key ) == true )
          {
            block_value = block_types [ 0 ].get ( block_key) + 1;
            block_types [ 0 ].put ( block_key, block_value );
          }
          else
            block_types [ 0 ].put( block_key, 1 );
        }  // if
      }  // if
  }  // for

  // Write out the haploblocks for this allele type.
  Set<String> keys = block_types [ 0 ].keySet();
  float mean = 0.0f;
  float sum = 0.0f;
  for ( String key : keys )
  {
    count = block_types [ 0 ].get ( key );
    sum = 0.0f; 
    for ( int allele_type = 1; allele_type <= 6; allele_type++ )
      if ( block_types [ allele_type ].containsKey ( key ) == true )
        sum += block_types [ allele_type ].get ( key ) * allele_type * 1.0f;
    mean = sum / (count * 1.0f);

    if ( ( count >= 10 ) && ( ( mean <= 3.0f ) || ( mean >= 4.0f ) ) )
    {
      if ( snps_written == false )
      {
        out_blocks.print ( snps.toString () );
        snps_written = true;
      }  // if

      out_blocks.print ( key + ":" + count + ":" );
  
      for ( int allele_type = 1; allele_type <= 6; allele_type++ )
      {
        if ( block_types [ allele_type ].containsKey ( key ) == true )
          out_blocks.print ( block_types [ allele_type ].get ( key ) + "" );
        else
          out_blocks.print ( "0" );
  
        if ( allele_type < 6 )
          out_blocks.print ( "-" );
        else
        {
          if ( mean <= 3.0f )  out_blocks.print( "<" + mean );
          if ( mean >= 4.0f )  out_blocks.print( ">" + mean );
          out_blocks.print ( " " );
        }  // else
      }  // if
    }  // for
  }  // for

  if ( snps_written == true )  out_blocks.println ();
}  // method typeBlocks2


/******************************************************************************/
public void processFiles 
    ( String tped_file_name
    , String phenotype_file_name
    , int phenotype_column 
    )
{
  Alleles alleles = new Alleles ();
  int block = 0;				// haplotype block number
  Tokens genotype = null;
  Vector genotypes = null;

  // Read in the phenotypes for the individuals.
  readPhenotypes ( phenotype_file_name, phenotype_column );

  OutputFile out_snps = new OutputFile ();
  out_snps.setFileName ( tped_file_name + ".out" + phenotype_column );
  out_snps.openFile ();

  OutputFile out_blocks = new OutputFile ();
  out_blocks.setFileName ( tped_file_name + ".hb" + phenotype_column );
  out_blocks.openFile ();

  // Open the input file.
  TpedIterator iterators = new TpedIterator ( tped_file_name );

  Tokens tokens = null;

  while ( iterators.isEndOfFile () == false )
  {
    genotypes = iterators.next ();

    if ( genotypes != null )
    {
      // iterators.listSnps ( genotypes );
      block++;
      typeBlocks2 ( block, genotypes, pheno_class, out_blocks );

      for ( int g = 0; g < genotypes.size (); g++ )
      {
        genotype = (Tokens) genotypes.elementAt ( g );

        out_snps.print ( block + " " + genotype.getToken ( 1 ) + " " );

        for ( int allele_type = 1; allele_type <= 6; allele_type++ )
        {
          alleles.initialize ();
          alleles.genotype ( genotype, pheno_class, allele_type );
          out_snps.print ( alleles.getFrequencies () + " " );
        }  // for

        alleles.initialize ();
        alleles.genotype ( genotype );
        out_snps.print ( alleles.toString () );

        out_snps.println ();
      }  // for
    }  // if
  }  // while

  // Close the files.
  iterators.closeFile ();
  out_snps.closeFile ();
  out_blocks.closeFile ();
}  // method processFiles


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The program integrates Phenotypes with Genotypes." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java Enotype <tped.filename> <phenotyes.filename> <phenotye.column>" );
    System.out.println ();
    System.out.print   ( "where <tped.filename> is the file name of a tab" );
    System.out.println ( "delimited Plink tped file, etc." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    Enotype app = new Enotype ();

    if ( ( args.length < 3 ) || ( args.length > 3 ) )
      app.usage ();
    else
      app.processFiles ( args [ 0 ], args [ 1 ],
          LineTools.getInteger ( args [ 2 ] ) );
  }  // method main

}  // class Enotype


