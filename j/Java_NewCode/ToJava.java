
import java.util.Vector;

// import Attribute;
// import Class;
// import Method;
// import OutputFile;

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

public class ToJava extends Object
{

/******************************************************************************/

  String output_file_name = "";				// Output file name

  OutputFile output_file = new OutputFile ();		// Output file
 

/******************************************************************************/
  public ToJava ()
  {
    initialize ();
  }  // constructor ToJava


/******************************************************************************/
  public void initialize ()
  {
  }  // method initialize


/******************************************************************************/
  public void close ()
  {
  }  // method close


/******************************************************************************/
  private String addDelimiters ( String type, String value )
  {
    if ( type.equals ( "String" ) == true )

      return "\"" + value + "\"";

    else if ( type.equals ( "char" ) == true )

      return "'" + value + "'";

    return value;		// default action
  }  // method addDelimiters


/******************************************************************************/
  private String getDefaultValue ( String type )
  {
    if ( type.equals ( "String" ) == true )

      output_file.println ( "\"\"" );

    else if ( ( type.equals ( "int"  ) == true ) || 
              ( type.equals ( "short" ) == true ) ||
              ( type.equals ( "long" ) == true ) )

      return "0";

    else if ( type.equals ( "boolean" ) == true )

      return "false";

    else if ( type.equals ( "char" ) == true )

      return "' '";

    else if ( type.equals ( "double" ) == true )

      return "0.0";

    else if ( type.equals ( "float" ) == true )

      return "0.0";

    else if ( type.equals ( "Vector" ) == true )

      return ".removeAllElements ()";

    else if ( type.equals ( "StringBuffer" ) == true )

      return ".setLength ( 0 )";

    return "null";
  }  // method getDefaultValue


/******************************************************************************/
  private String getPrefix ( String name )
  {
    int index = name.indexOf ( "." );

    if ( index < 0 )  return name;

    return ( name.substring ( 0, index ) );
  }  // method getPrefix


/******************************************************************************/
  public void setOutputName ( String name )
  {
    output_file_name = name;
  }  // method setOutputName


/******************************************************************************/
  private void writeComment ()
  {
    output_file.println ();
    output_file.println ();

    output_file.print ( "/" );

    for ( int i = 1; i <= 78; i++ )

      output_file.print ( "*" );

    output_file.println ( "/" );
  }  // method writeComment


/******************************************************************************/
  private String convertName ( String name )
  {
    int index = name.lastIndexOf ( '.' );
    if ( index <= 0 )  return name + ".java";

    return name.substring ( 0, index ) + ".java";
  }  // method convertName


/******************************************************************************/
  private String functionName ( String name )
  {
    StringBuffer new_name = new StringBuffer ( name );

    // Upper case the first letter for the function name.
    if ( ( name.charAt ( 0 ) >= 'a' ) && ( name.charAt ( 0 ) <= 'z' ) )
    {
      char upper = (char) (new_name.charAt ( 0 ) + 'A' - 'a');
      new_name.setCharAt ( 0, upper );
    }  // if

    // Search for underscore characters.
    for ( int index = new_name.length () - 1; index >= 0; index-- )

      if ( new_name.charAt ( index ) == '_' )
      {
        new_name.deleteCharAt ( index );

        // Upper case the first letter after the underscore for the function name.
        if ( ( new_name.charAt ( index ) >= 'a' ) && 
             ( new_name.charAt ( index ) <= 'z' ) )
        {
          char upper = (char) (new_name.charAt ( index ) + 'A' - 'a');
          new_name.setCharAt ( index, upper );
        }  // if
      }  // if

    return new_name.toString ();
  }  // method functionName


/******************************************************************************/
  private void writeAttribute ( Attribute attribute )
  {
    String name = attribute.getAttributeName ();
    String type = attribute.getAttributeType ();
    String value = addDelimiters ( type, attribute.getInitialValue () );

    if ( value.length () <= 0 )  value = getDefaultValue ( type );

    String comment = attribute.getAttributeComment ();
    String object_name = attribute.getObjectName ();

    output_file.println ( "\n  /** " + comment + " */" );

    if ( object_name.length () <= 0 )

      output_file.println ( "  private " + type + " " + name + " = " 
          + value + ";" );

    else

      output_file.println ( "  private " + object_name + " " + name + " = new " 
          + object_name + " ();" );
  }  // method writeAttribute


/******************************************************************************/
  private void writeGet ( Attribute attribute )
  {
    writeComment ();

    String comment = attribute.getAttributeComment ();
    String name = attribute.getAttributeName ();
    String type = attribute.getAttributeType ();

    output_file.println ( "/**" );
    output_file.println ( "  This method returns the attribute " + name + "."  );
    output_file.println ();
    output_file.println ( "  @return " + type + " " + name + " - " + comment );
    output_file.println ( "*/" );

    output_file.print ( "  public " + type );
    output_file.println ( " get" + functionName ( name ) + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return " + name + ";" );
    output_file.println ( "  }  // method get" + functionName ( name ) );
  }  // method writeGet


/******************************************************************************/
  private void writeIs ( Attribute attribute )
  {
    writeComment ();

    String comment = attribute.getAttributeComment ();
    String name = attribute.getAttributeName ();
    String type = attribute.getAttributeType ();

    output_file.println ( "/**" );
    output_file.println ( "  This method returns the attribute " + name + "."  );
    output_file.println ();
    output_file.println ( "  @return " + type + " " + name + " - " + comment );
    output_file.println ( "*/" );

    output_file.print ( "  public " + type );
    output_file.println ( " is" + functionName ( name ) + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return " + name + ";" );
    output_file.println ( "  }  // method is" + functionName ( name ) );
  }  // method writeIs


/******************************************************************************/
  private void writeMethod ( Method method )
  {
    writeComment ();

    String name = method.getMethodName ();
    String type = method.getMethodType ();

    output_file.println ( "/** " + method.getMethodComment () + " */"  );
    output_file.println ( "  public " + type + " " + name + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return " + method.getMethodValue () + ";" );
    output_file.println ( "  }  // method " + name );
  }  // method writeSet


/******************************************************************************/
  private void writeSet ( Attribute attribute )
  {
    writeComment ();

    String comment = attribute.getAttributeComment ();
    String name = attribute.getAttributeName ();
    String type = attribute.getAttributeType ();

    output_file.println ( "/** This method sets the attribute " + name 
        + " to the specified value. */"  );

    output_file.print ( "  public void set" );
    output_file.println ( functionName ( name ) + " ( " + type + " value )" );
    output_file.println ( "  {" );
    output_file.println ( "    " + name + " = value;" );
    output_file.println ( "  }  // method set" + functionName ( name ) );
  }  // method writeSet


/******************************************************************************/
  private void startClass ( Class new_class )
  {
    writeComment ();

    output_file.println ( "/**" );
    output_file.println ( "  " + new_class.getClassComment () );
    output_file.println ( "  @author        Darrell O. Ricke, Ph.D." );
    output_file.println ( "  Copyright:     Copyright (c) 2003-2004 Paragon Software" );
    output_file.println ( "  License:       GNU GPL license (http://www.gnu.org/licenses/gpl.html)" );
    output_file.println ( "*/" );

    output_file.print ( "public class " );

    // Write the class name
    output_file.print ( new_class.getClassName () );

    if ( new_class.getExtendsName ().length () > 0 )
      output_file.print ( " extends " + new_class.getExtendsName () );
    else
      output_file.print ( " extends Object" );

    output_file.println ( " implements java.io.Serializable" );
    output_file.println ( "{" );

    writeComment ();
  }  // method startClass


/******************************************************************************/
  private void endClass ( Class new_class )
  {
    writeComment ();

    output_file.println ();

    output_file.println ( "}  // class " + new_class.getClassName () );

    output_file.println ();
  }  // method endClass


/******************************************************************************/
  private void writeConstructor ( Class new_class )
  {
    writeComment ();
    output_file.println ( "  // Constructor " + new_class.getClassName () );
    output_file.println ( "  public " + new_class.getClassName () + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    initialize ();" );
    output_file.println ( "  }  // constructor " + new_class.getClassName () );
  }  // method writeConstructor


/******************************************************************************/
  private void writeInitialization ( Attribute [] attributes )
  {
    writeComment ();
    output_file.println ( "  /** Initialize class variables. */" );
    output_file.println ( "  public void initialize ()" );
    output_file.println ( "  {" );

    // Initialize the variables.
    int count = attributes.length;
    String comment;
    String type;
    String value;
    for ( int index = 0; index < count; index++ )
    {
      comment = attributes [ index ].getAttributeComment ();
      type = attributes [ index ].getAttributeType ();
      value = addDelimiters ( type, attributes [ index ].getInitialValue () );

      output_file.print ( "    " );
      output_file.print ( attributes [ index ].getAttributeName () );

      if ( attributes [ index ].getObjectName ().length () > 0 )
      {
        // Check if initialization value specified.
        if ( value.length () <= 0 )  

          value = getDefaultValue ( attributes [ index ].getObjectName () );
       
        output_file.println ( value + ";\t\t// " + comment );
      }  // if
      else
      {
        // Check if initialization value specified.
        if ( value.length () <= 0 )  value = getDefaultValue ( type );
       
        output_file.println ( " = " + value + ";\t\t// " + comment );
      }  // else
    }  // for

    output_file.println ( "  }  // method initialize " );
  }  // method writeInitialization


/******************************************************************************/
  public void writeFromResultSet ( Attribute [] attributes )
  {
    writeComment ();
    output_file.println ( "  /** Extract this object from a JDBC ResultSet. */" );
    output_file.println ( "  public void fromResultSet ()" );
    output_file.println ( "  {" );
    output_file.println ( "    try" );
    output_file.println ( "    {" );
    output_file.println ( "    }  // try" );
    output_file.println ( "    catch ( Exception e )" );
    output_file.println ( "    {" );
    output_file.println ( "    }" );
    output_file.println ( "  }  // method fromResultSet" );
  }  // method writeFromResultSet


/******************************************************************************/
  public void writeToSqlInsert ( String class_name, Attribute [] attributes )
  {
    writeComment ();
    output_file.println ( "  /** Prepare a SQL INSERT statement to insert" 
        + " this object into an SQL database. */" );
    output_file.println ( "  public String toSqlInsert ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return \"INSERT INTO " + class_name + "\";" );
    output_file.println ( "  }  // method toSqlInsert" );
  }  // method writeToSqlInsert


/******************************************************************************/
  public void writeToSqlSelect ( String class_name, Attribute [] attributes )
  {
    writeComment ();
    output_file.println ( "  /** Select this object from an SQL database. */" );
    output_file.println ( "  public String toSqlSelect ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return \"SELECT * FROM " + class_name + "\";" );
    output_file.println ( "  }  // method toSqlSelect" );
  }  // method writeToSqlSelect


/******************************************************************************/
  public void writeToString ( Attribute [] attributes )
  {
    writeComment ();
  }  // method writeToString


/******************************************************************************/
  public void writeToXml ( Attribute [] attributes )
  {
    writeComment ();
  }  // method writeToXml


/******************************************************************************/
  public void writeJava 
      ( Class         new_class		// New class
      , Attribute []  attributes	// Class attributes
      , Vector        methods		// Class methods
      )
  {
    setOutputName ( new_class.getClassName () + ".java" );

    // Set the output file name.
    output_file.initialize ();
    output_file.setFileName ( output_file_name );
    output_file.openFile ();

    // Start the Java class file.
    startClass ( new_class );

    // Write the attributes.
    for ( int index = 0; index < attributes.length; index++ )
    
      writeAttribute ( attributes [ index ] );
    

    // Write the constructor.
    writeConstructor ( new_class );

    // Write the initialization routine.
    writeInitialization ( attributes );

    // Write the get routines.
    for ( int index = 0; index < attributes.length; index++ )
    
      writeGet ( attributes [ index ] );

    // Write the is routines.
    for ( int index = 0; index < attributes.length; index++ )
    { 
      if ( attributes [ index ].getAttributeType ().equals ( "boolean" ) == true )
 
        writeIs ( attributes [ index ] );
    }  // for

    // Write the set routines.
    for ( int index = 0; index < attributes.length; index++ )

      writeSet ( attributes [ index ] );

    // Write the methods.
    for ( int index = 0; index < methods.size (); index++ )

      writeMethod ( (Method) methods.elementAt ( index ) );

    // Write the method to convert the object to a delimited string.
    writeToString ( attributes );

    // Write the method to convert the object to a delimited string.
    writeToXml ( attributes );

    // Write the method to insert the object into a SQL database.
    writeToSqlInsert ( new_class.getClassName (), attributes );

    // Write the method to select the object from a SQL database.
    writeToSqlSelect ( new_class.getClassName (), attributes );

    // Write the method to extract the object from a JDBC ResultSet.
    writeFromResultSet ( attributes );

    // End the Java class file.
    endClass ( new_class );
	
    // Close the output file.
    output_file.closeFile ();
  }  // method writeJava


/******************************************************************************/

}  // class ToJava

