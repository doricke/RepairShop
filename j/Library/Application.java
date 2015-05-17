
import java.sql.*;		// only needed for class connection

import Database;
import Row;

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

public class Application extends Object
{

/******************************************************************************/

  private Database database;		// Database connection

  private Row row;			// Row object


final static private String newWordSQL = 
    "INSERT INTO Word ( WORD_ID, word_seq, count ) " +
    "VALUES ( ?, ?, ? )";

final static private String newSequenceWordSQL = 
    "INSERT INTO Sequence_Word ( sequence_word_id, word_id, " +
    "hash_sequence_id, position ) " +
    "VALUES ( ?, ?, ?, ? )";

final static private String newHashSequenceSQL = 
    "INSERT INTO HashSequence ( hash_sequence_id, sequence_id, sequence_name ) " +
    "VALUES ( ?, ?, ? )";

final static private String newAlignmentSQL = 
    "INSERT INTO Alignment ( alignment_id, contig_id, hash_sequence_id, " +
    "contig_start, contig_end, sequence_start, sequence_end, identities, " +
    "sequence_strand ) " +
    "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

final static private String newClusterSQL = 
    "INSERT INTO Cluster ( contig_id ) " +
    "VALUES ( ? )";


/******************************************************************************/
public Application ()
{
  // Allocate a Row to use.
  row = new Row ();
}  // constructor Application


/******************************************************************************/
  public void setDatabase ( Database db )
  {
    database = db;

    if ( db != null )
    {
      // Set up a database table row for the selected species.
      Connection connection = db.getConnection ();

      if ( connection == null )
        System.out.println ( "No connection in setDatabase" );
      else
      {
        row.setConnection ( connection );

        // Set the database to commit after every SQL statement.
        // row.setCommit ();
      }  // else
    }  // if
  }  // method setDatabase


/******************************************************************************/
  private void setIntField ( Row row, int column, int column_value )
  {
    // Check for no specified value
    if ( column_value <= 0 )
      row.setNull ( column, java.sql.Types.NUMERIC );
    else
      // Set the field.
      row.setInt ( column, column_value );
  }  // method setIntField


/******************************************************************************/
  private void setStringField ( Row row, int column, String column_value )
  {
    // Check for no specified value
    if ( column_value == null )
      row.setNull ( column, java.sql.Types.VARCHAR );
    else
      // Set the field.
      row.setString ( column, column_value );
  }  // method setStringField


/******************************************************************************/
  public void commit ( )
  {
    row.clear ();
    row.executeSQL ( "COMMIT" );
    row.clear ();
  }  // method commit


/******************************************************************************/
  public int newHashSequence ( String sequence_name )
  {
    // Check if the sequence exists.
    int hash_sequence_id = getHashSequenceId ( sequence_name );
    if ( hash_sequence_id > 0 )
    {
System.out.println ( "oldHashSequence: [" + hash_sequence_id + ", " +
    sequence_name + "]" );

      return hash_sequence_id;
    }  // if

    // Get the next dna_sequence_id value.
    hash_sequence_id = row.getNextValue ( "hash_sequence_id", "DUAL" );
    row.clear ();
    row.prepareInsert ( newHashSequenceSQL );

    // Set the hash_sequence_id field.
    setIntField ( row, 1, hash_sequence_id );

    // Set the sequence_id field to NULL.
    row.setNull ( 2, java.sql.Types.VARCHAR );

    // Set the sequence_name field.
    setStringField ( row, 3, sequence_name );

    row.executeUpdate ();
    row.clear ();

System.out.println ( "newHashSequence: [" + hash_sequence_id + ", " +
    sequence_name + "]" );

    return hash_sequence_id; 
  }  // method newHashSequence


/******************************************************************************/
  public int newAlignment 
    ( int contig_id
    , int hash_sequence_id 
    , int contig_start
    , int contig_end
    , int sequence_start
    , int sequence_end
    , int identities
    , String sequence_strand
    )
  {
    // Get the next dna_sequence_id value.
    int alignment_id = 
        row.getNextValue ( "alignment_id", "DUAL" );

    row.clear ();
    row.prepareInsert ( newAlignmentSQL );

    // Set the alignment_id field.
    setIntField ( row, 1, alignment_id );

    // Set the contig_id field.
    setIntField ( row, 2, contig_id );

    // Set the hash_sequence_id field.
    setIntField ( row, 3, hash_sequence_id );

    setIntField ( row, 4, contig_start );

    setIntField ( row, 5, contig_end );

    setIntField ( row, 6, sequence_start );

    setIntField ( row, 7, sequence_end );

    setIntField ( row, 8, identities );

    setStringField ( row, 9, sequence_strand );

    row.executeUpdate ();
    row.clear ();

    return alignment_id; 
  }  // method newAlignment


/******************************************************************************/
  public void newCluster 
      ( String contig_name
      )
  {
    row.clear ();
    row.prepareInsert ( newClusterSQL );

    // Set the user_set_id field.
    setStringField ( row, 2, contig_name );

    // Set the sequence_id field.
    setIntField ( row, 4, 0 );

    row.executeUpdate ();
    row.clear ();
  }  // method newUserSetSequence


/******************************************************************************/
  public int newWordId ( )
  {
    // Get the next word_id value.
    row.clear ();
    int word_id = row.getNextValue ( "word_id", "DUAL" );
    row.clear ();
    return word_id;
  }  // method newWordId

/******************************************************************************/
  public void newWord ( int word_id, String word_seq, int count )
  {
    row.clear ();
    row.prepareInsert ( newWordSQL );
    row.setInt ( 1, word_id );
    row.setString ( 2, word_seq );
    row.setInt ( 3, count );

    row.executeUpdate ();
    row.clear ();
  }  // method newWord


/******************************************************************************/
  public int newSequenceWord ( int word_id, int hash_sequence_id, int position )
  {
    int sequence_word_id = getSequenceWordId ( word_id, hash_sequence_id, position );

    if ( sequence_word_id > 0 )
    {
System.out.println ( "oldSequenceWord: [" + sequence_word_id + ", " +
    word_id + ", " + hash_sequence_id + ", " + position + "]" );

      return sequence_word_id;
    }  // if 

    // Get the next dna_sequence_id value.
    sequence_word_id = row.getNextValue ( "sequence_word_id", "DUAL" );

System.out.println ( "newSequenceWord: [" + sequence_word_id + ", " +
    word_id + ", " + hash_sequence_id + ", " + position + "]" );

    row.clear ();
    row.prepareInsert ( newSequenceWordSQL );
    row.setInt ( 1, sequence_word_id );

    setIntField ( row, 2, word_id );

    setIntField ( row, 3, hash_sequence_id );

    setIntField ( row, 4, position );

    row.executeUpdate ();
    row.clear ();

    return sequence_word_id; 
  }  // method newSequenceWord


/******************************************************************************/
public int getHashSequenceId ( String sequence_name )
{
  row.clear ();
  // Select the database table row for the sequence_name.
  row.selectField ( "hash_sequence_id", "HashSequence", "sequence_name", sequence_name );
  row.nextRow ();
  int sequence_id = row.getColumnInt ( 1 );	 
  row.clear ();

  return sequence_id;
}  // method getHashSequenceId


/******************************************************************************/
public int getSequenceWordId ( int word_id, int hash_sequence_id, int position )
{
  row.clear ();
  row.selectField ( "sequence_word_id", "Sequence_Word", "word_id", word_id,
      "hash_sequence_id", hash_sequence_id, "position", position );
  row.nextRow ();
  int sequence_word_id = row.getColumnInt ( 1 );	 
  row.clear ();

  return sequence_word_id;
}  // method getSequenceWordId


/******************************************************************************/
public int getWordId ( String word_seq )
{
  row.clear ();
  // Select the database table row for the word_seq.
  row.selectField ( "word_id", "Word", "word_seq", word_seq );
  row.nextRow ();
  int word_id = row.getColumnInt ( 1 );	 
  row.clear ();

  return word_id;
}  // method getWordId


/******************************************************************************/
// This method clips .scf and .bin from sequence names.
public String clipSequenceName ( String name )
{
  // Trim ".scf" from Myriad sequence names.
  int index = name.toString ().indexOf ( ".scf" );
  if ( index > 0 )  return name.substring ( 0, index );

  // Trim ".bin" from Clemson sequence names.
  index = name.toString ().indexOf ( ".bin" );
  if ( index > 0 )  return name.substring ( 0, index );

  return name;
}  // clipSequenceName


/******************************************************************************/
  public static void main ( String [] args )
  {
    Application application = new Application ();

    // Connect to the relational database.
    application.database = new Database ();
    application.database.connectDB ();

    // Disconnect from the relational database.
    application.database.disconnectDB ();
    application = null;
    System.exit ( 0 );
  }  // method main

}  // class Application

