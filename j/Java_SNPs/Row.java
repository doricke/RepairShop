
// import oracle.jdbc2.*;
// import oracle.sql.*;
import java.io.*;
import java.sql.*;
import java.util.*;			// Properties class

// import oracle.*;
// import Database;			// Relational database connection class

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


/*******************************************************************************/
public class Row
{


/*******************************************************************************/

private Connection  connection;		// JDBC database connection

private int expected_rows = 0;		// Expected number of result rows

private ResultSet  resultSet;		// JDBC database ResultSet

private int resultSetColumn = 0;	// resultSet - current column

private int resultSetRow = 0;		// resultSet - current row

private Statement  statement;		// JDBC database statement

private PreparedStatement  preparedStatement;	// SQL prepared statement

private String  tableName;		// Name of data warehouse table


/*******************************************************************************/

private boolean display_messages = true;	// Show messages toggle

private boolean debug = false;			// Debug toggle


/*******************************************************************************/

private java.sql.Date date;		// Date


/*******************************************************************************/
public Row ()
{
  clear ();

  // Get the date once.
  date = new java.sql.Date ( System.currentTimeMillis () );
}  // constructor Row


/*******************************************************************************/
// This method clears the parameters.
public void clear ()
{
  if ( debug == true )
    System.out.println ( "Row.clear called" );

  // close releases the results.
  close ();

  tableName = null;
  resultSetRow = 0;
  resultSetColumn = 0;
}  // method clear


/*******************************************************************************/
// This function removes all occurances of c from value.
public String cleanString ( String value, char c )
{
  int index = value.indexOf ( c );

  if ( index == -1 )  return value;

  StringBuffer temp = new StringBuffer ( value );

  while ( index >= 0 )
  {
    temp.deleteCharAt ( index );
    index = temp.toString ().indexOf ( c );
  }  // while

  return temp.toString ();
}  // method cleanString


/*******************************************************************************/
public void setConnection ( Connection conn )
{
  connection = conn;
}  // method setConnection


/*******************************************************************************/
public void setAutoCommit ( boolean state )
{
  try
  {
    connection.setAutoCommit ( state );
  }  // try
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.setAutoCommit: " + e );
  }  // catch
}  // method setAutoCommit


/*******************************************************************************/
public void setDisplayMessages ( boolean flag )
{
  display_messages = flag;
}  // method setDisplayMessages


/*******************************************************************************/
public void setFetchSize ( int rows )
{
  expected_rows = rows;
}  // method setFetchSize


/******************************************************************************/
  public void setIntField ( int column, int column_value )
  {
    // Check for no specified value
    if ( column_value < 0 )
      setNull ( column, java.sql.Types.NUMERIC );
    else
      // Set the field.
      setInt ( column, column_value );
  }  // method setIntField


/******************************************************************************/
  public void setStringField ( int column, String column_value )
  {
    // Check for no specified value
    if ( column_value == null )
      setNull ( column, java.sql.Types.VARCHAR );
    else
      // Set the field.
      setString ( column, column_value );
  }  // method setStringField


/*******************************************************************************/
public void releaseConnection ()
{
  connection = null;
}  // method releaseConnection


/*******************************************************************************/
public void releaseResults ()
{
  // Release the database resources.
  try
  {
    if ( statement != null ) statement.close ();

    if ( resultSet != null ) resultSet.close ();
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.releaseResults SQLException " + e );
  }  // catch

  resultSet = null;
  statement = null;
}  // method releaseResults


/*******************************************************************************/
public void executeSQL ( String command )
{
  if ( debug == true )
    System.out.println ( "Row.executeSQL: " + command );

  try
  {
    // Release the database resources.
    // releaseResults ();

    // Create a simple Statement object.
    statement = connection.createStatement ();

    // Pass the SQL command string to the DBMS and execute the SQL statement.
    int row = statement.executeUpdate ( command );

    statement.close ();
  }  // try
  catch ( SQLException e1 )
  {
    System.err.println ( ".executeSQL SQLException " + e1 );
  }  // catch
}  // method executeSQL


/*******************************************************************************/
  public void commit ()
  {
    executeSQL ( "COMMIT" );
  }  // method commit


/*******************************************************************************/
// This method sets the database to commit on every SQL statement.
public void setCommit ()
{
  setCommit ( 1 );
}  // method setCommit


/*******************************************************************************/
// This method sets the database to commit every N SQL statements.
public void setCommit ( int statements )
{
  executeSQL ( "SET AUTOCOMMIT " + statements );
}  // method setCommit


/*******************************************************************************/
public void selectRow ( String column_name, int column_value )
{
  selectRow ( tableName, column_name, column_value );
}  // method selectRow


/*******************************************************************************/
public void selectRow ( String column_name, String column_value )
{
  if ( column_value.indexOf ( "'" ) >= 0 )

    column_value = cleanString ( column_value, '\'' );

  selectRow ( tableName, column_name, column_value );
}  // method selectRow


/*******************************************************************************/
public void selectRow ( String table_name, String column_name, int column_value )
{
  String sql = "SELECT * FROM " + table_name + " WHERE " + column_name + " = " +
      column_value;

  selectSQL ( sql );
}  // method selectRow


/*******************************************************************************/
public void selectRow ( String table_name, String column_name, String column_value )
{
  if ( column_value.indexOf ( "'" ) >= 0 )

    column_value = cleanString ( column_value, '\'' );

  String sql = "SELECT * FROM " + table_name + " WHERE " + column_name + " = '" +
      column_value + "'";

  selectSQL ( sql );
}  // method selectRow


/*******************************************************************************/
public void selectRow 
    ( String  table_name
    , String  column_name1
    , long    column_value1 
    , String  column_name2
    , long    column_value2 
    , String  column_name3
    , long    column_value3 
    , String  column_name4
    , long    column_value4 
    )
{
  String sql = "SELECT * FROM " + table_name + " WHERE ( " 
      + "( " + column_name1 + " = " + column_value1 + " ) AND "
      + "( " + column_name2 + " = " + column_value2 + " ) AND "
      + "( " + column_name3 + " = " + column_value3 + " ) AND "
      + "( " + column_name4 + " = " + column_value4 + " ) )";

  selectSQL ( sql );
}  // method selectRow


/*******************************************************************************/
public void selectLikeRow 
    ( String field_name
    , String table_name
    , String column_name
    , String column_value 
    )
{
  if ( column_value.indexOf ( "'" ) >= 0 )

    column_value = cleanString ( column_value, '\'' );

  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE (" + column_name + " LIKE '" +
      column_value + "' )";

  selectSQL ( sql );
}  // method selectLikeRow


/*******************************************************************************/
public void selectField
    ( String field_name
    , String table_name
    , String column_name
    , String column_value 
    )
{
  if ( column_value.indexOf ( "'" ) >= 0 )

    column_value = cleanString ( column_value, '\'' );

  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE (" + column_name + " = '" +
      column_value + "' )";

  selectSQL ( sql );
}  // method selectField


/*******************************************************************************/
// Select a column from a table with an interger field.
public void selectField
    ( String field_name
    , String table_name
    , String column_name
    , int    column_value 
    )
{
  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE (" + column_name + " = " +
      column_value + " )";

  selectSQL ( sql );
}  // method selectField


/*******************************************************************************/
// Select a column from a table with a long field.
public void selectField
    ( String field_name
    , String table_name
    , String column_name
    , long   column_value 
    )
{
  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE (" + column_name + " = " +
      column_value + " )";

  selectSQL ( sql );
}  // method selectField


/*******************************************************************************/
public void updateClob
    ( String clob_name
    , Clob   clob_value
    , String table_name
    , String column_name
    , int    column_value 
    )
{
  String sql = "UPDATE " + table_name + " SET " + clob_name +
      " = ? WHERE " + column_name + " = " + column_value;

  System.out.println ( "updateClob: " + sql );

  try
  {
    PreparedStatement pstmt = connection.prepareStatement ( sql );
    pstmt.setClob ( 1, clob_value );
    pstmt.executeUpdate ();
  }  // try
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.updateClob: " + e );
  }  // catch
}  // method updateClob


/*******************************************************************************/
public void selectFieldUpdate
    ( String field_name
    , String table_name
    , String column_name
    , String column_value 
    )
{
  if ( column_value.indexOf ( "'" ) >= 0 )

    column_value = cleanString ( column_value, '\'' );

  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE " + column_name + " = '" +
      column_value + "' FOR UPDATE";

  selectSQL ( sql );
}  // method selectFieldUpdate


/*******************************************************************************/
public void selectFieldUpdate
    ( String field_name
    , String table_name
    , String column_name
    , int    column_value 
    )
{
  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE " + column_name + " = '" +
      column_value + "' FOR UPDATE";

  selectSQL ( sql );
}  // method selectFieldUpdate


/*******************************************************************************/
// Select a column from a table with an interger field.
public void selectFieldAsc
    ( String field_name
    , String table_name
    , String column_name
    , int    column_value 
    )
{
  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE (" + column_name + " = " +
      column_value + " ) ORDER BY " + column_name + " ASC";

  selectSQL ( sql );
}  // method selectField


/*******************************************************************************/
public void selectField
    ( String field_name
    , String table_name
    , String column_name1
    , String column_value1 
    , String column_name2
    , String column_value2 
    )
{
  if ( column_value1.indexOf ( "'" ) >= 0 )

    column_value1 = cleanString ( column_value1, '\'' );

  if ( column_value2.indexOf ( "'" ) >= 0 )

    column_value2 = cleanString ( column_value2, '\'' );

  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE ( " + 
      "( " + column_name1 + " = '" + column_value1 + "' ) AND " +
      "( " + column_name2 + " = '" + column_value2 + "' ) " +
      ")";

  // System.out.println ( "Row.selectField: sql = " + sql );

  selectSQL ( sql );
}  // method selectField


/*******************************************************************************/
public void selectFieldC
    ( String field_name
    , String table_name
    , String column_name1
    , String column_value1 
    , String column_name2a
    , String column_name2b
    , String column_name3
    , int    column_value3 
    )
{
  if ( column_value1.indexOf ( "'" ) >= 0 )

    column_value1 = cleanString ( column_value1, '\'' );

  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE ( " + 
      "( " + column_name1 + " = '" + column_value1 + "' ) AND " +
      "( " + column_name2a + " = " + column_name2b + " ) AND " +
      "( " + column_name3 + " = " + column_value3 + " ) " +
      ")";

  selectSQL ( sql );
}  // method selectFieldC


/*******************************************************************************/
public void selectField
    ( String field_name
    , String table_name
    , String column_name1
    , int    column_value1 
    , String column_name2
    , int    column_value2 
    , String column_name3
    , int    column_value3 
    )
{
  String sql = "SELECT " + field_name + " FROM " + table_name + 
      " WHERE ( " + 
      "( " + column_name1 + " = '" + column_value1 + "' ) AND " +
      "( " + column_name2 + " = '" + column_value2 + "' ) AND " +
      "( " + column_name3 + " = '" + column_value3 + "' ) " +
      ")";

  selectSQL ( sql );
}  // method selectField


/*******************************************************************************/
public void selectSQL ( String command )
{
  // System.out.println ( "Row.selectSQL: SQL = " + command );

  if ( debug == true )
    System.out.println ( "Row.selectSQL: SQL = " + command );

  if ( connection == null )
  {
    System.out.println ( "Null connection in Row.selectSQL" );
    return;
  }  // if 

  try
  {
    // Release the database resources.
    releaseResults ();

    // Create a simple Statement object.
    statement = connection.createStatement ();

    resultSetRow = 0;
    resultSetColumn = 0;

    // Check if number of expected rows is set.
    if ( expected_rows > 0 )
      statement.setFetchSize ( expected_rows );

    // Pass the SQL command string to the DBMS and execute the SQL statement.
    resultSet = statement.executeQuery ( command );
  }  // try
  catch ( SQLException e1 )
  {
    System.err.println ( "Row.selectSQL SQLException " + e1 );
  }  // catch
}  // method selectSQL


/*******************************************************************************/
// This method advances the result set to the next row.
public boolean next ()
{
  try
  {
    return resultSet.next ();
  }
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.next: " + e );
    return false;
  }  // catch
}  // method next


/*******************************************************************************/
// This method gets an object from the result set.
public java.sql.Clob getClobObject ( int field )
{
  // Check for no result set.
  if ( resultSet == null )  return null;

  // Return the requested object.
  try
  {
    // java.sql.Clob my_clob = (java.sql.Clob) resultSet.getObject ( field );
    java.sql.Clob my_clob = resultSet.getClob ( field );
    return my_clob;
  }  // try
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.getClobObject: " + e );
    return null;
  }  // catch
}  // method getClobObject


/*******************************************************************************/
// This method gets a clob from the result set.
public String nextClobObject ( long length )
{
  resultSetColumn++;
  // Check for no result set.
  if ( resultSet == null )  return "";

  // Return the requested object.
  try
  {
    java.sql.Clob my_clob = (java.sql.Clob) resultSet.getObject ( resultSetColumn );

    // System.out.println ( "clob length = " + length );

    if ( length > Integer.MAX_VALUE )
    {
      System.out.println ( "Clob too long for getSubString method (" + length + ")" );
      length = Integer.MAX_VALUE;
    }  // if

    return my_clob.getSubString ( 1L, (int) length );
  }  // try
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.nextClobObject: " + e );
    return "";
  }  // catch
}  // method nextClobObject


/*******************************************************************************/
// This method gets a clob from the result set.
public String nextClobObject ()
{
  resultSetColumn++;
  // Check for no result set.
  if ( resultSet == null )  return "";

  // Return the requested object.
  try
  {
    java.sql.Clob my_clob = (java.sql.Clob) resultSet.getObject ( resultSetColumn );

    return my_clob.getSubString ( 1L, (int) my_clob.length () );
  }  // try
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.nextClobObject: " + e );
    return "";
  }  // catch
}  // method nextClobObject


/*******************************************************************************/
// This method gets an object from the result set.
public Object getObject ( int field )
{
  // Check for no result set.
  if ( resultSet == null )  return null;

  // Return the requested object.
  try
  {
    return resultSet.getObject ( field );
  }  // try
  catch ( Exception e )
  {
    if ( display_messages == true )
      System.out.println ( "Row.getObject: " + e );
    return null;
  }  // catch
}  // method getObject


/*******************************************************************************/
public boolean nextRow ()
{
  if ( resultSet == null )  return false;

  try
  {
    // Advance to the next row - if there is one
    resultSet.next ();
    resultSetRow++;
  }  
  catch ( SQLException e )
  {
    resultSetRow = 0;
    return false;
  }  // catch

  resultSetColumn = 0;
  return true;
}  // method nextRow


/*******************************************************************************/
public String nextColumnString ()
{
  resultSetColumn++;
  try
  {
    // Try to return the String value for the next column.
    return resultSet.getString ( resultSetColumn );
  }
  catch ( SQLException e )
  {
    resultSetColumn = 0;
    return null;
  }  // catch
}  // method nextColumnString


/*******************************************************************************/
public String getColumnString ( int column_number )
{
  try
  {
    // Try to return the String value for the selected column.
    return resultSet.getString ( column_number );
  }
  catch ( SQLException e )
  {
    resultSetColumn = 0;
    return null;
  }  // catch
}  // method getColumnString


/*******************************************************************************/
public long nextColumnLong ()
{
  resultSetColumn++;

  try
  {
    // Try to return the int value for the next column.
    return resultSet.getLong ( resultSetColumn );
  }
  catch ( SQLException e )
  {
    resultSetColumn = 0;
    return 0;
  }  // catch
}  // method nextColumnLong 


/*******************************************************************************/
public long getColumnLong ( int column_number )
{
  try
  {
    // Try to return the int value for the selected column.
    return resultSet.getLong ( column_number );
  }
  catch ( SQLException e )
  {
    return 0;
  }  // catch
}  // method getColumnLong 


/*******************************************************************************/
public int nextColumnInt ()
{
  resultSetColumn++;
  try
  {
    // Try to return the int value for the next column.
    return resultSet.getInt ( resultSetColumn );
  }
  catch ( SQLException e )
  {
    resultSetColumn = 0;
    return 0;
  }  // catch
}  // method nextColumnInt 


/*******************************************************************************/
public int getColumnInt ( int column_number )
{
  try
  {
    // Try to return the int value for the selected column.
    return resultSet.getInt ( column_number );
  }
  catch ( SQLException e )
  {
    return 0;
  }  // catch
}  // method getColumnInt 


/*******************************************************************************/
public int countRows ( String table_name )
{
  int numberRows = 0;


  // Count the number of rows.
  try
  {
    selectSQL ( "SELECT count(*) from " + table_name );
    resultSet.next ();
    resultSetRow++;
    numberRows = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.countRows(1) SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return numberRows;
}  // method countRows


/*******************************************************************************/
public int countRows ( String table_name, String column_name, int column_value )
{
  int numberRows = 0;


  // Count the number of rows.
  try
  {
    selectSQL ( "SELECT count(*) from " + table_name + " WHERE ( " +
        column_name + " = " + column_value + " )" );
    resultSet.next ();
    resultSetRow++;
    numberRows = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.countRows(2) SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return numberRows;
}  // method countRows


/*******************************************************************************/
public int countRows ( String table_name, String column_name, String column_value )
{
  int numberRows = 0;


  // Count the number of rows.
  try
  {
    selectSQL ( "SELECT count(*) from " + table_name + " WHERE ( " +
        column_name + " = '" + column_value + "' )" );
    resultSet.next ();
    resultSetRow++;
    numberRows = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.countRows(3) SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return numberRows;
}  // method countRows


/*******************************************************************************/
public int countRowsLess ( String table_name, String column_name, int column_value )
{
  int numberRows = 0;


  // Count the number of rows.
  try
  {
    selectSQL ( "SELECT count(*) from " + table_name + " WHERE ( " +
        column_name + " <= " + column_value + " )" );
    resultSet.next ();
    resultSetRow++;
    numberRows = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.countRowsLess SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return numberRows;
}  // method countRowsLess


/*******************************************************************************/
// Count rows Equal Not Less than
public int countRowsENL 
    ( String  table_name
    , String  equal_name
    , int     equal_value 
    , String  not_name
    , int     not_value 
    , String  less_name
    , int     less_value 
    )
{
  int numberRows = 0;


  // Count the number of rows.
  try
  {
    selectSQL ( "SELECT count(*) from " + table_name + " WHERE ( ( " 
        + equal_name + " = " + equal_value + " ) AND ( " 
        + not_name + " != " + not_value + ") AND ( "
        + less_name + " < " + less_value + " ) )" );
    resultSet.next ();
    resultSetRow++;
    numberRows = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.countRowsLess SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return numberRows;
}  // method countRowsENL


/*******************************************************************************/
// Count rows (Equal) (Not) (Greater_than or equal)
public int countRowsENGe
    ( String  table_name
    , String  equal_name
    , int     equal_value 
    , String  not_name
    , int     not_value 
    , String  ge_name
    , int     ge_value 
    )
{
  int numberRows = 0;


  // Count the number of rows.
  try
  {
    selectSQL ( "SELECT count(*) from " + table_name + " WHERE ( ( " 
        + equal_name + " = " + equal_value + " ) AND ( " 
        + not_name + " != " + not_value + ") AND ( "
        + ge_name + " >= " + ge_value + " ) )" );
    resultSet.next ();
    resultSetRow++;
    numberRows = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.countRowsLess SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return numberRows;
}  // method countRowsENGe


/*******************************************************************************/
public int getMaxValue ( String columnName, String table_name )
{
  int maximumValue = 0;

  // Get the maximum value for a database column.
  try
  {
    selectSQL ( "SELECT max(" + columnName + ") from " + table_name );
    resultSet.next ();
    resultSetRow++;
    maximumValue = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.getMaxValue SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return maximumValue;
}  // method getMaxValue


/*******************************************************************************/
public int getNextValue ( String columnName, String table_name )
{
  int maximumValue = 0;

  // Get the maximum value for a database column.
  try
  {
    selectSQL ( "SELECT " + columnName + " FROM " + table_name );
    if ( resultSet == null )  return 1;

    resultSet.next ();
    maximumValue = resultSet.getInt ( 1 );

    if ( table_name.equals ( "DUAL" ) == true )
      updateField ( table_name, columnName, maximumValue + 1, "dual_id", 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.getNextValue SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return maximumValue;
}  // method getNextValue


/*******************************************************************************/
public int getNextValueOracle ( String columnName, String table_name )
{
  int maximumValue = 0;

  // Get the maximum value for a database column.
  try
  {
    selectSQL ( "SELECT " + columnName + ".NEXTVAL FROM " + table_name );
    if ( resultSet == null )  return 1;

    resultSet.next ();
    maximumValue = resultSet.getInt ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.getNextValue SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return maximumValue;
}  // method getNextValueOracle


/*******************************************************************************/
public String getNextValue2 ( String columnName, String table_name )
{
  String maximumValue = "";

  // Get the maximum value for a database column.
  try
  {
    selectSQL ( "SELECT " + columnName + ".NEXTVAL FROM " + table_name );
    resultSet.next ();
    maximumValue = resultSet.getString ( 1 );
  }
  catch ( SQLException e )
  {
    System.err.println ( "Row.getNextValue2 SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return maximumValue;
}  // method getNextValue2


/*******************************************************************************/
// rows  = number of rows in the resultSet
public String [] getColumn ( int rows )
{
  String [] column = new String [ rows ];
  int row = 0;

  // Retrieve the selected String column
  try
  {
    while ( resultSet.next () )
    {
      // Get the column values into Java variables.
      column [ row++ ] = resultSet.getString ( 1 ).trim ();
    }  // while 

  }  // try
  catch ( SQLException e )
  {
    System.err.println ( "Row.getColumn SQLException " + e );
  }  // catch

  // Release the database resources.
  releaseResults ();

  return column;
}  // method getColumn


/*******************************************************************************/
public void prepareInsert ( String sql )
{
  if ( debug == true )
    System.out.println ( "Row.prepareInsert: SQL = " + sql );

  if ( connection == null )
  {
    System.out.println ( "Row.prepareInsert: no database connection" );
    return;
  }  // if 

  if ( connection == null )
  {
    System.out.println ( "prepareInsert: no connection to database" );
    return;
  }  // if 

  try
  {
    preparedStatement = connection.prepareStatement ( sql );
  }
  catch ( SQLException e )
  {
    if ( display_messages == true )
      System.out.println ( "prepareInsert: SQLException " + e );
  }  // catch
}  // method prepareInsert


/*******************************************************************************/
public void setTableName ( String table_name )
{
  tableName = table_name;
}  // method setTableName


/*******************************************************************************/
public void setDate ( int column )
{
  if ( preparedStatement == null )
  {
    System.out.println ( "setDate: no preparedStatement" );
    return;
  }  // if

  try
  {
    preparedStatement.setDate ( column, date );
  }
  catch ( SQLException e )
  {
    if ( display_messages == true )
      System.out.println ( "setDate: SQLException " + e );
  }  // catch
}  // method setDate


/*******************************************************************************/
public void setNull ( int column, int column_type )
{
  if ( preparedStatement == null )
  {
    System.out.println ( "setIntNull: no preparedStatement " );
    return;
  }  // if

  try
  {
    preparedStatement.setNull ( column, column_type );
  }  // try
  catch ( SQLException e )
  {
    if ( display_messages == true )
      System.out.println ( "setNull: SQLException " + e );
  }  // catch
}  // method setNull


/*******************************************************************************/
public void setString ( int column, String str )
{
  if ( preparedStatement == null )
  {
    System.out.println ( "setString: no preparedStatement " + str );
    return;
  }  // if

  try
  {
    preparedStatement.setString ( column, str );
  } // try
  catch ( SQLException e )
  {
    System.out.println ( "setString: SQLException " + e );
  }  // catch
}  // method setString


/*******************************************************************************/
public void setStringClob ( int column, String str )
{
  if ( preparedStatement == null )
  {
    System.out.println ( "setStringClob: no preparedStatement " + str );
    return;
  }  // if

  try
  {
    byte [] bytes = str.getBytes ();
    ByteArrayInputStream clob = new ByteArrayInputStream ( bytes );

    preparedStatement.setAsciiStream ( column, clob, bytes.length );
  } // try
  catch ( SQLException e )
  {
    System.out.println ( "setString: SQLException " + e );
    e.printStackTrace ();
  }  // catch
}  // method setString


/*******************************************************************************/
public void setFloat ( int column, float number )
{
  if ( preparedStatement == null )
  {
    System.out.println ( "setFloat: no preparedStatement; column " + column + 
        ", value " + number );
    return;
  }  // if 

  try
  {
    preparedStatement.setFloat ( column, number );
  }
  catch ( SQLException e )
  {
    System.out.println ( "setFloat: SQLException " + e );
  }  // catch
}  // method setFloat


/*******************************************************************************/
public void setInt ( int column, int number )
{
  if ( preparedStatement == null )
  {
    System.out.println ( "setInt: no preparedStatement; column " + column + 
        ", value " + number );
    return;
  }  // if 

  try
  {
    preparedStatement.setInt ( column, number );
  }
  catch ( SQLException e )
  {
    System.out.println ( "setInt: SQLException " + e );
  }  // catch
}  // method setInt


/*******************************************************************************/
public void setBlob ( int column, java.sql.Blob blob )
{
  if ( preparedStatement == null )  return;

  try
  {
    preparedStatement.setBlob ( column, blob );
  }
  catch ( SQLException e )
  {
    System.out.println ( "setBlob: SQLException " + e );
  }  // catch
}  // method setBlob


/*******************************************************************************/
public void executeUpdate ()
{
  if ( debug == true )
    System.out.println ( "Row.executeUpdate called" );

  if ( preparedStatement == null )  return;

  // Execute the statement.
  try
  {
    preparedStatement.executeUpdate ();
  }
  catch ( SQLException e )
  {
    if ( display_messages == true )
      System.out.println ( "executeUpdate: SQLException " + e );
  }  // catch

  // Release the preparedStatement.
  try
  {
    preparedStatement.close ();
  }
  catch ( SQLException e )
  {
    if ( display_messages == true )
      System.out.println ( "executeUpdate: SQLException " + e );
  }  // catch
}  // method executeUpdate


/*******************************************************************************/
public void close ()
{
  // Release the resultSet and statement resources.
  releaseResults ();

  try
  {
    if ( preparedStatement != null )  preparedStatement.close ();
  }  // try
  catch ( SQLException e )
  {
    System.err.println ( "Row.close SQLException " + e );
  }  // catch

  preparedStatement = null;
}  // method close


/*******************************************************************************/
private void listColumns ()
{
  String column1, column2, column3;


  try
  {
    while ( resultSet.next () )
    {
      // Get the column values into Java variables.
      column1 = resultSet.getString ( 1 );
      column2 = resultSet.getString ( 2 );
      column3 = resultSet.getString ( 3 );
      System.out.print ( column1 + "\t" );
      System.out.print ( column2 + "\t" );
      System.out.print ( column3 );
      System.out.println ( );
    }  // while
  }  // try
  catch ( SQLException e1 )
  {
    System.err.println ( "Row.listColumns SQLException " + e1 );
  }  // catch

}  // method listColumns


/*******************************************************************************/
public void updateField 
    ( String tableName
    , String fieldName
    , int    fieldValue
    , String keyName
    , int    keyValue
    )
{
  // Construct the update SQL statment.
  String sql = "UPDATE " + tableName + " SET " + fieldName + 
      " = ? WHERE ( " + keyName + " = ? )";

  // Prepare the update SQL statement.
  prepareInsert ( sql );
  setIntField ( 1, fieldValue );
  setIntField ( 2, keyValue );
  executeUpdate ();
  clear ();
}  // method updateField


/*******************************************************************************/
public void updateField 
    ( String tableName
    , String fieldName
    , String fieldValue
    , String keyName
    , int    keyValue
    )
{
  // Construct the update SQL statment.
  String sql = "UPDATE " + tableName + " SET " + fieldName + 
      " = ? WHERE ( " + keyName + " = ? )";

  // Prepare the update SQL statement.
  prepareInsert ( sql );
  setStringField ( 1, fieldValue );
  setIntField ( 2, keyValue );
  executeUpdate ();
  clear ();
}  // method updateField


/*******************************************************************************/
private void listAll ()
{
  Database db = new Database ();
  db.connectDB ();

  connection = db.getConnection ();

  selectSQL ( "SELECT * FROM project" );
  listColumns ();

  // Release the database resources.
  releaseResults ();

  int rows = countRows ( "Folder" );

  // Release the database resources.
  releaseResults ();

  selectSQL ( "SELECT project_name FROM project" );
  String [] projects = getColumn ( rows );

  // Release the database resources.
  releaseResults ();

  db.disconnectDB ();
}  // method listAll


/*******************************************************************************/
static public void main ( String argv [] )
{
  Row test = new Row ();

  test.listAll ();
}  // method main


/*******************************************************************************/

}  // class Row 

