
import java.util.Vector;

// import Attribute;
// import Class;
// import Method;
// import ParseXml;
// import ToJava;

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

public class ParseCode extends Object
{

/******************************************************************************/

  String input_file_name = "";			// Input file name

  Vector attributes = new Vector ();		// Class attributes

  Class current_class = null;			// Current Class object

  Vector methods = new Vector ();		// Class methods 

  ToJava to_java = new ToJava ();		// Java class writer


/******************************************************************************/
  public ParseCode ()
  {
    initialize ();
  }  // constructor ParseCode


/******************************************************************************/
  public void initialize ()
  {
    input_file_name = "";
    attributes.removeAllElements ();
    methods.removeAllElements ();
  }  // method initialize


/******************************************************************************/
  public void close ()
  {
    attributes.removeAllElements ();
    methods.removeAllElements ();
  }  // method close


/******************************************************************************/
  private String getValue ( String line )
  {
    int index1 = line.indexOf ( "=\"" );

    if ( index1 < 0 )  return "";

    int index2 = line.indexOf ( '"', index1 + 2 );

    if ( index2 < 0 )  return "";

    return ( line.substring ( index1 + 2, index2 ) );
  }  // method getValue


/******************************************************************************/
  public void setInputName ( String name )
  {
    input_file_name = name;
  }  // method setInputName


/******************************************************************************/
  private void closeClass ()
  {
    // Check for no current class.
    if ( current_class == null )  return;

    Attribute [] attributes_list = new Attribute [ attributes.size () ];
    for ( int i = 0; i < attributes.size (); i++ )
      attributes_list [ i ] = (Attribute) attributes.elementAt ( i );

    // Write the code for the current class.
    to_java.writeJava ( current_class, attributes_list, methods );

    // Release the memory objects.
    for ( int i = 0; i < attributes.size (); i++ )
      attributes_list [ i ] = null;
    attributes.removeAllElements ();
    methods.removeAllElements ();
  }  // method closeClass


/******************************************************************************/
  private Attribute parseAttribute ( String xml_line )
  {
    Attribute attribute = new Attribute ();

    // Check for the name of the current class.
    int index = xml_line.indexOf ( "name=" );
    if ( index < 0 )  return null;

    attribute.setAttributeName ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method comment.
    index = xml_line.indexOf ( "comment=" );
    if ( index > 0 )
      attribute.setAttributeComment ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method type.
    index = xml_line.indexOf ( "object_name=" );
    if ( index > 0 )
      attribute.setObjectName ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method value.
    index = xml_line.indexOf ( "object_initialize=" );
    if ( index > 0 )
      attribute.setObjectInitialize ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method type.
    index = xml_line.indexOf ( "type=" );
    if ( index > 0 )
      attribute.setAttributeType ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method value.
    index = xml_line.indexOf ( "value=" );
    if ( index > 0 )
      attribute.setInitialValue ( getValue ( xml_line.substring ( index ) ) );

    return attribute;
  }  // method parseAttribute


/******************************************************************************/
  private Class parseClass ( String xml_line )
  {
    Class new_class = new Class ();

    // Check for the name of the current class.
    int index = xml_line.indexOf ( "name=" );
    if ( index < 0 )  return null;

    new_class.setClassName ( getValue ( xml_line.substring ( index ) ) );

    // Check for a class comment.
    index = xml_line.indexOf ( "comment=" );
    if ( index > 0 )
      new_class.setClassComment ( getValue ( xml_line.substring ( index ) ) );

    // Check for an extends name.
    index = xml_line.indexOf ( "extends=" );
    if ( index > 0 )
      new_class.setExtendsName ( getValue ( xml_line.substring ( index ) ) );

    return new_class;
  }  // method parseClass


/******************************************************************************/
  private Method parseMethod ( String xml_line )
  {
    Method method = new Method ();

    // Check for the name of the current class.
    int index = xml_line.indexOf ( "name=" );
    if ( index < 0 )  return null;

    method.setMethodName ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method comment.
    index = xml_line.indexOf ( "comment=" );
    if ( index > 0 )
      method.setMethodComment ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method type.
    index = xml_line.indexOf ( "type=" );
    if ( index > 0 )
      method.setMethodType ( getValue ( xml_line.substring ( index ) ) );

    // Check for a method value.
    index = xml_line.indexOf ( "value=" );
    if ( index > 0 )
      method.setMethodValue ( getValue ( xml_line.substring ( index ) ) );

    return method;
  }  // method parseMethod


/******************************************************************************/
  private void parseLine ( String xml_line )
  {
    // Check for the start of a new software object.
    if ( xml_line.startsWith ( "Class" ) == true )
    {
      // Write any previous classes.
      closeClass ();

      current_class = parseClass ( xml_line );
    }  // if

    // Check for a new class attribute.
    if ( xml_line.startsWith ( "Attribute" ) == true )
    {
      Attribute new_attribute = parseAttribute ( xml_line );

      if ( new_attribute != null )  attributes.add ( new_attribute );
    }  // if

    // Check for a new class method.
    if ( xml_line.startsWith ( "Method" ) == true )
    {
      Method new_method = parseMethod ( xml_line );

      if ( new_method != null )  methods.add ( new_method );
    }  // if
  }  // method parseLine


/******************************************************************************/
  public void processFile ()
  {
    // Parse the XML input file.
    ParseXml xml_parser = new ParseXml ();
    xml_parser.setFileName ( input_file_name );
    xml_parser.parseXml ();

    Vector nodes = xml_parser.getNodes ();

    // xml_parser.printNodes ();

    int xml_records = nodes.size ();

    for ( int i = 0; i < xml_records; i++ )

      parseLine ( (String) nodes.elementAt ( i ) );

    closeClass ();

    xml_parser.close ();
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "This program generates new class shell templates." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java ParseCode <new_code.xml> where" );
    System.out.println ();
    System.out.println ( "<new_code.xml> is the XML code description file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    ParseCode app = new ParseCode ();

    if ( args.length != 1 )
      app.usage ();
    else
    {
      app.setInputName  ( args[ 0 ] );
      app.processFile ();
    }  // else
  }  // method main


/******************************************************************************/

}  // class ParseCode

