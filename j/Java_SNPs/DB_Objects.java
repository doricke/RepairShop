
import java.sql.*;

// import Database;
// import Plink;
// import Row;

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

public class DB_Objects extends Object
{

  private Database database;		// Database connection

  private Row row;			// database row object

  final static private String newDatasetSQL =
      "INSERT INTO ( dataset_id, organism_id, dataset_name, qtl, description, " +
      "platform, date_updated ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";

  final static private String newSnpProbeSQL =
      "INSERT INTO SNP_probe ( snp_probe_id, dataset_id, snp_id, p_value, " +
      "allele1, allele2, maf, nmiss, or_beta, se, r2, rank ) VALUES " +
      "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";      

  final static private String newSnpSQL =
      "INSERT INTO SNP ( snp_id, affy_id, rs_id, gene_symbols, gene_names, " +
      "ncbi_gene_ids ) VALUES ( ?, ?, ?, ?, ?, ? )";

  final static private String newSnpMapSQL =
      "INSERT INTO SNP_Map ( snp_map_id, chromosome, position, genome_id, " +
      "snp_id ) VALUES ( ?, ?, ?, ?, ? )";

  final static private String newSnpGeneSQL =
      "INSERT INTO SNP_Gene ( snp_gene_id, snp_probe_id, gene_id, distance ) " +
      "VALUES ( ?, ?, ?, ? )";

  final static private String newGeneSQL =
      "INSERT INTO Gene ( gene_id, gene_symbol, gene_name, ncbi_gene_id, gene_description ) " +
      "VALUES ( ?, ?, ?, ?, ? )";

  final static private String newGeneMapSQL =
      "INSERT INTO Gene_Map ( mapping_id, genome_id, gene_id, chromosome, " +
      "start_position, end_position ) VALUES ( ?, ?, ?, ?, ?, ? )";

  final static private String newOrganismSQL = 
      "INSERT INTO Organism ( organism_id, organism_name ) VALUES ( ?, ? )";


/******************************************************************************/
  public DB_Objects ()
  {
    // Allocate a Row to use.
    row = new Row ();
  }  // constructor DB_Objects


/******************************************************************************/
  public Database getDatabase ()
  {
    return database;
  }  // method getDatabase


/******************************************************************************/
  public int getDatasetId ( String dataset_name )
  {
    // Select the database table row for the selected document.
    row.selectRow ( "Dataset", "dataset_name", dataset_name );
    row.nextRow ();

    int dataset_id = row.getColumnInt ( 1 );	 

    row.clear ();
    return dataset_id;
  }  // method getDatasetId


/******************************************************************************/
  public void setDatabase ( Database db )
  {
    database = db;
    row.setConnection ( database.getConnection () );
  }  // method setDatabase


/******************************************************************************/
  public int newDataset 
      ( int organism_id
      , String dataset_name
      , String qtl
      , String description
      , String platform
      )
  {
    int dataset_id = row.getNextValue ( "dataset_id", "DUAL" );
    row.clear ();

    row.prepareInsert ( newDatasetSQL );

    row.setIntField ( 1, dataset_id );
    row.setIntField ( 2, organism_id );
    row.setString   ( 3, dataset_name );
    row.setString   ( 4, qtl );
    row.setString   ( 5, description );
    row.setString   ( 6, platform );
    row.setDate     ( 7 );
    row.executeUpdate ();
    row.commit ();
 
    return dataset_id;
  }  // method newDataset


/******************************************************************************/
  public int getOrganismId ( String organism_name )
  {
    // Select the database table row for the selected document.
    row.selectRow ( "Organism", "organism_name", organism_name );
    row.nextRow ();

    int organism_id = row.getColumnInt ( 1 );	 

    row.clear ();
    return organism_id;
  }  // method getOrganismId


/******************************************************************************/
  public int getSnpId ( String snp_name )
  {
    // Select the SNP table row for the selected SNP.
    row.selectRow ( "SNP", "rs_id", snp_name );
    row.nextRow ();

    int snp_id = row.getColumnInt ( 1 );

    row.clear ();
    return snp_id;
  }  // method getSnpId


/******************************************************************************/
  public int newGene 
      ( String gene_symbol
      , String gene_name
      , int ncbi_gene_id
      , String gene_description
      )
  {
    int gene_id = row.getNextValue ( "gene_id", "DUAL" );
    row.clear ();

    row.prepareInsert ( newGeneSQL );

    row.setIntField ( 1, gene_id );
    row.setString   ( 2, gene_symbol );
    row.setString   ( 3, gene_name );
    row.setIntField ( 4, ncbi_gene_id );
    row.setString   ( 5, gene_description );
    row.executeUpdate ();
    row.commit ();

    return gene_id;
  }  // method newGene


/******************************************************************************/
  public int newGeneMap
      ( int genome_id
      , int gene_id
      , String chromosome
      , int start_position
      , int end_position
      )
  {
    int gene_map_id = row.getNextValue ( "gene_map_id", "DUAL" );
    row.clear ();

    row.prepareInsert ( newGeneMapSQL );

    row.setIntField ( 1, gene_map_id );
    row.setIntField ( 2, genome_id );
    row.setIntField ( 3, gene_id );
    row.setString   ( 4, chromosome );
    row.setIntField ( 5, start_position );
    row.setIntField ( 6, end_position );
    row.executeUpdate ();
    row.commit ();

    return gene_map_id;
  }  // method newGeneMap


/******************************************************************************/
  public int newOrganism ( String organism_name )
  {
    int organism_id = row.getNextValue ( "organism_id", "DUAL" );
    row.clear ();

    row.prepareInsert ( newOrganismSQL );

    row.setIntField ( 1, organism_id );
    row.setString   ( 2, organism_name );
    row.executeUpdate ();
    row.commit ();

    return organism_id;
  }  // method newOrganism


/******************************************************************************/
  public int newSnpProbe ( int dataset_id, Plink plink )
  {
    int snp_probe_id = row.getNextValue ( "snp_probe_id", "DUAL" );
    row.clear ();

    int snp_id = getSnpId ( plink.getRsId () );

    row.prepareInsert ( newSnpProbeSQL );

    row.setIntField ( 1, snp_probe_id );
    row.setIntField ( 2, dataset_id );
    row.setIntField ( 3, snp_id );
    row.setFloat    ( 4, plink.getPValue () );
    row.setString   ( 5, plink.getAllele1 () + "" );
    row.setString   ( 6, plink.getAllele2 () + "" );
    row.setFloat    ( 7, plink.getMaf () );
    row.setIntField ( 8, plink.getNmiss () );
    row.setFloat    ( 9, plink.getOrBeta () );
    row.setFloat    ( 10, plink.getSe () );
    row.setFloat    ( 11, plink.getR2 () );
    row.setIntField ( 12, 0 );					// rank
    row.executeUpdate ();
    row.commit ();

    return snp_probe_id;
  }  // method newSnpProbe

/******************************************************************************/
  public int newSnp 
      ( String affy_id
      , String rs_id
      , String gene_symbols
      , String gene_names
      , String ncbi_gene_ids
      )
  {
    int snp_id = row.getNextValue ( "snp_id", "DUAL" );
    row.clear ();

    row.prepareInsert ( newSnpSQL );
    row.setIntField ( 1, snp_id );
    row.setString ( 2, affy_id );
    row.setString ( 3, rs_id );
    row.setString ( 4, gene_symbols );
    row.setString ( 5, gene_names );
    row.setString ( 6, ncbi_gene_ids );
    row.executeUpdate ();
    row.commit ();

    return snp_id;
  }  // method newSnp


/******************************************************************************/
  public int newSnpMap
     ( String chromosome
     , int position
     , int genome_id
     , int snp_id
     )
  {
    int snp_map_id = row.getNextValue ( "snp_map_id", "DUAL" );
    row.clear ();

    row.prepareInsert ( newSnpMapSQL );
    row.setIntField ( 1, snp_map_id );
    row.setString   ( 2, chromosome );
    row.setIntField ( 3, position );
    row.setIntField ( 4, genome_id );
    row.setIntField ( 5, snp_id );
    row.executeUpdate ();
    row.commit ();

    return snp_map_id;
  }  // method newSnpMap


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/



}  // class DB_Objects

