
import java.sql.*;
import java.io.*;

// import Database;
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

public class Update extends Object
{

/******************************************************************************/

  private Database database = null;	// Database connection

  private Row row = new Row ();		// Row object


  private String clob_name = null;	// Clob field name

  private int current_value = 1;	// index of current column value

  private String key_name = null;	// key for current row

  private int key_value = 0;		// key value for current row

  private boolean last_clob = false;	// is last field a Clob

  private String sql = null;		// SQL prepared statement

  private String table_name = null;	// Database table name

  private int total_columns = 0;	// total number of columns

  private String update_sql = null;	// UPDATE SQL statement


/******************************************************************************/
  private static final boolean debug = false;		// debugging flag


/******************************************************************************/
  public Update ()
  {
  }  // constructor Update


/******************************************************************************/
  public void clear ()
  {
    row.clear ();
    clob_name = null;
    current_value = 1;
    key_name = null;
    key_value = 0;
    last_clob = false;
    sql = null;
    table_name = null;
    total_columns = 0;
    update_sql = null;
  }  // method clear


/******************************************************************************/
  public void close ()
  {
    clear ();
  }  // method close


/******************************************************************************/
  private void checkLastColumn ()
  {
    if ( debug == true )
      System.out.println ( "Update.checkLastColumn: current_value = " + current_value +
          ", total_columns = " + total_columns );

    // Check if this is the last column.
    if ( current_value == total_columns )
    {
      if ( debug == true )
        System.out.println ( "Update.checkLastColumn calling executeUpdate" );

      row.executeUpdate ();
      row.commit ();
    }  // if
  }  // method checkLastColumn


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
        row.setAutoCommit ( false );
      }  // else
    }  // if
  }  // method setDatabase


/******************************************************************************/
  public void setDate ( )
  {
    if ( debug == true )
      System.out.println ( "Update.setDate called" );

    // Set today's date.
    row.setDate ( current_value );

    // Check if this is the last column.
    checkLastColumn ();

    current_value++;
  }  // method setDate 


/******************************************************************************/
  public void setTable ( String tableName )
  {
    table_name = tableName;
  }  // method setTable


/******************************************************************************/
  public void setTable 
      ( String tableName
      , int columns
      , boolean lastClob 
      , String field_names
      )
  {
    if ( debug == true )
      System.out.println ( "Update.setTable: table_name = " + tableName );

    // clear ();
    table_name = tableName;
    total_columns = columns;
    last_clob = lastClob;
    current_value = 1;

    // INSERT INTO table ( ?, ? ) VALUES ( ?, ? )
    sql = "INSERT INTO " + table_name + " ( " + field_names + " ) VALUES (";

    // Add the field values.
    for ( int index = 1; index < total_columns; index++ )
      sql += "?, ";
    if ( lastClob == true )
      sql += "Empty_Clob () )";
    else
      sql += "? )";

    if ( debug == true )
      System.out.println ( "Update.setTable: sql = " + sql );
  }  // method setTable


/******************************************************************************/
  public void setTable 
      ( String tableName
      , int columns
      , boolean lastClob 
      , String [] field_names
      )
  {
    if ( debug == true )
      System.out.println ( "Update.setTable (2): table_name = " + tableName );

    // clear ();
    table_name = tableName;
    total_columns = columns;
    last_clob = lastClob;
    current_value = 1;

    // INSERT INTO table ( ?, ? ) VALUES ( ?, ? )
    sql = "INSERT INTO " + table_name + " ( ";

    // Add the field names.
    for ( int index = 0; index < total_columns; index++ )
    {
      if ( index < total_columns - 1 )
        sql += field_names [ index ] + ", ";
      else
        sql += field_names [ index ];
    }  // for

    sql += " ) VALUES ( ";

    // Add the field values.
    for ( int index = 1; index < total_columns; index++ )
      sql += "?, ";

    if ( lastClob == true )
      sql += "Empty_Clob () )";
    else
      sql += "? )";
  }  // method setTable


/******************************************************************************/
  public void setTableUpdate 
      ( String tableName
      , int columns
      , boolean lastClob 
      , String [] field_names
      , String key_name
      )
  {
    if ( debug == true )
      System.out.println ( "Update.setTableUpdate: called" );

    // clear ();
    table_name = tableName;
    total_columns = columns + 1;	// extra column for key value
    last_clob = lastClob;
    current_value = 1;

    // UPDATE table SET field1 = ?, field2 = ?, ... WHERE ( field(n) = ? )
    sql = "UPDATE " + table_name + " SET ";

    // Add the field names.
    for ( int index = 0; index < columns - 1; index++ )
    {
      if ( index < columns - 1 )
        sql += field_names [ index ] + " = ?, ";
      else
        sql += field_names [ index ] + " = ? ";
    }  // for

    if ( lastClob == false )
      sql += field_names [ columns - 1 ] + " = ? ";

    sql += " WHERE " + key_name + " = ?";

    if ( debug == true )
      System.out.println ( "Update.setTableUpdate: sql = " + sql );
  }  // method setTableUpdate


/******************************************************************************/
  private void isPresent ()
  {
    if ( debug == true )
      System.out.println ( "Update.isPresent: called" );

    row.prepareInsert ( sql );
  }  // method isPresent


/******************************************************************************/
  public void setKey ( String column_name, int column_value )
  {
    if ( debug == true )
      System.out.println ( "Update.setKey: key_name = " + column_name + 
          ", value = " + column_value );

    key_name = column_name;
    key_value = column_value;
  }  // method setKey


/******************************************************************************/
  public void setKeyName ( String column_name )
  {
    if ( debug == true )
      System.out.println ( "Update.setKeyName: key_name = " + column_name ); 

    key_name = column_name;
  }  // method setKey


/******************************************************************************/
  public void setKeyValue ( int column_value )
  {
    if ( debug == true )
      System.out.println ( "Update.setKeyValue: key_name = " + key_name + 
          ", value = " + column_value );

    key_value = column_value;
  }  // method setKey


/******************************************************************************/
  public void setPair ( String column_name, int column_value )
  {
    if ( debug == true )
      System.out.println ( "Update.setPair: column_name = " + column_name + 
          ", value = " + column_value );

    // Check if this is the first pair.
    if ( current_value == 1 )
      isPresent ();

    // Set the value of the column
    row.setIntField ( current_value, column_value );

    // Check if this is the last column.
    checkLastColumn ();

    current_value++;
  }  // method setPair 


/******************************************************************************/
  public void setPair ( String column_name, String column_value )
  {
    if ( debug == true )
      System.out.println ( "Update.setPair: column_name = " + column_name + 
          ", value = " + column_value );

    // Check for a single quote in the column_value.
    int index = column_value.indexOf ( "'" );
    if ( index >= 0 )
    {
      StringBuffer value = new StringBuffer ( column_value );

      while ( index >= 0 )
      {
        value.deleteCharAt ( index );
        index = value.toString ().indexOf ( "'" );
      }  // while

      column_value = value.toString ();
    }  // if

    // Set the value of the column
    row.setStringField ( current_value, column_value );

    // Check if this is the last column.
    checkLastColumn ();

    current_value++;
  }  // method setPair 


/******************************************************************************/
  public void setPair ( String column_name, String column_value, int column_width )
  {
    if ( debug == true )
      System.out.println ( "Update.setPair3: column_name = " + column_name + 
          ", value = " + column_value );

    // Check the column_value width.
    if ( column_value.length () > column_width )
    {
      System.out.println ( "*Warning* column " 
          + column_name 
          + "'s value is too long (" 
          + column_value.length ()
          + "/" + column_width 
          + "): '" 
          + column_value 
          + "'" );

      setPair ( column_name, column_value.substring ( 0, column_width ) );
    }  // if
    else

      setPair ( column_name, column_value );
  }  // method setPair 


/******************************************************************************/
// This method writes a clob to the database.
protected void setClob 
    ( String clob_name
    , String clob_value
    )
{
  if ( debug == true )
  {
    System.out.println ( "Update.setClob: clob_name = " + clob_name + 
        ", value = " + clob_value );
    System.out.println ( "Update.setClob: key_name = " + key_name +
        ", key_value = " + key_value );
  }  // if

  // last column.
  checkLastColumn ();

  // Select the database row.
  row.selectFieldUpdate ( clob_name, table_name, key_name, key_value );

  // Advance to the first row returned.
  row.next ();

  // Get the LOB object pointer.
  java.sql.Clob my_clob = row.getClobObject ( 1 );

  try
  {
    Writer writer = my_clob.setCharacterStream ( 1L );

    // Write the clob.
    writer.write ( clob_value );
    writer.flush ();
    writer.close ();
    writer = null;
  }  // try
  catch ( Exception e )
  {
    System.out.println ( "Update.setClob: " + e );
  }  // catch

  // Clean up the result set and statement.
  row.clear ();
  my_clob = null;

  // Commit the update.
  row.commit ();
}  // setClob 


/******************************************************************************/
// This method writes a clob to the database.
protected void writeClob 
    ( String clob_name
    , String clob_value
    )
{
  if ( debug == true )
  {
    System.out.println ( "Update.writeClob: clob_name = " + clob_name + 
        ", value = " + clob_value );
    System.out.println ( "Update.writeClob: key_name = " + key_name +
        ", key_value = " + key_value );
  }  // if

  // Select the database row.
  row.selectFieldUpdate ( clob_name, table_name, key_name, key_value );

  // Advance to the first row returned.
  row.next ();

  // Get the LOB object pointer.
  java.sql.Clob my_clob = row.getClobObject ( 1 );

  try
  {
    Writer writer = my_clob.setCharacterStream ( 1L );

    // Write the clob.
    writer.write ( clob_value );
    writer.flush ();
    writer.close ();
    writer = null;
  }  // try
  catch ( Exception e )
  {
    System.out.println ( "Update.setClob: " + e );
  }  // catch

  // Clean up the result set and statement.
  row.clear ();
  my_clob = null;

  // Commit the update.
  row.commit ();
}  // writeClob 


/******************************************************************************/
// This method writes a clob to the database.
protected void updateClob 
    ( String clob_name
    , String clob_value
    )
{
  if ( debug == true )
  {
    System.out.println ( "Update.updateClob: clob_name = " + clob_name + 
        ", value = " + clob_value );
    System.out.println ( "Update.updateClob: key_name = " + key_name +
        ", key_value = " + key_value );
  }  // if

  // Select the database row.
  row.selectFieldUpdate ( clob_name, table_name, key_name, key_value );

  // Advance to the first row returned.
  row.next ();

  // Get the LOB object pointer.
  Clob my_clob = row.getClobObject ( 1 );

  try
  {
    Writer writer = my_clob.setCharacterStream ( 1L );

    // Write the clob.
    writer.write ( clob_value );
    writer.flush ();
    writer.close ();
    writer = null;
  }  // try
  catch ( Exception e )
  {
    System.out.println ( "Update.setClob: " + e );
  }  // catch

  // Clean up the result set and statement.
  row.clear ();

  row.updateClob ( "sequence", my_clob, "BioSequence", key_name, key_value );

  // Commit the update.
  row.commit ();
}  // updateClob 


/******************************************************************************/
  public void update 
    ( String column_name
    , int    column_value
    )
  {
    // Update this column.
    row.updateField ( table_name, column_name, column_value, key_name, key_value );
  }  // method update


/******************************************************************************/
  public void update 
    ( String column_name
    , String column_value
    )
  {
    // Update this column.
    row.updateField ( table_name, column_name, column_value, key_name, key_value );
  }  // method update


/******************************************************************************/
  public static void main ( String [] args )
  {
    Update update = new Update ();

    // Connect to the relational database.
    update.database = new Database ();
    update.database.connectDB ();

    // Disconnect from the relational database.
    update.database.disconnectDB ();
    update = null;
    System.exit ( 0 );
  }  // method main

}  // class Update

