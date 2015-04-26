
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

// import InputFile;
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

public class NewCode extends Object
{

/******************************************************************************/

  String input_file_name = "";				// Input file name

  InputFile input_file = new InputFile ();		// Input file

  String output_file_name = "";				// Output file name

  OutputFile output_file = new OutputFile ();		// Output file

  Vector names = new Vector ();				// Variable names

  Vector types = new Vector ();				// Variable types
 

/******************************************************************************/
  public NewCode ()
  {
    initialize ();
  }  // constructor NewCode


/******************************************************************************/
  public void initialize ()
  {
    input_file_name = "";
  }  // method initialize


/******************************************************************************/
  public void close ()
  {
  }  // method close


/******************************************************************************/
  private String getPrefix ( String name )
  {
    int index = name.indexOf ( "." );

    if ( index < 0 )  return name;

    return ( name.substring ( 0, index ) );
  }  // method getPrefix


/******************************************************************************/
  public void setInputName ( String name )
  {
    input_file_name = name;
  }  // method setInputName


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
  private void writeGet ( String type, String name )
  {
    writeComment ();

    output_file.print ( "  public " + type );
    output_file.println ( " get" + functionName ( name ) + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return " + name + ";" );
    output_file.println ( "  }  // method get" + functionName ( name ) );
  }  // method writeGet


/******************************************************************************/
  private void writeIs ( String type, String name )
  {
    writeComment ();

    output_file.print ( "  public " + type );
    output_file.println ( " is" + functionName ( name ) + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    return " + name + ";" );
    output_file.println ( "  }  // method is" + functionName ( name ) );
  }  // method writeIs


/******************************************************************************/
  private void writeSet ( String type, String name )
  {
    writeComment ();

    output_file.print ( "  public void set" );
    output_file.println ( functionName ( name ) + " ( " + type + " value )" );
    output_file.println ( "  {" );
    output_file.println ( "    " + name + " = value;" );
    output_file.println ( "  }  // method set" + functionName ( name ) );
  }  // method writeSet


/******************************************************************************/
  private void parseLine ()
  {
    // Get the next line of the input file.
    String line = input_file.getLine ().toString ();

    // Check for the end of the input file.
    if ( input_file.isEndOfFile () == true )  return;

    // Copy the line to the variable declaration section.
    output_file.println ();
    output_file.print ( "  private " );
    output_file.println ( line );

    // Remove initialization text.
    int index = line.indexOf ( "=" );
    if ( index > 0 )  line = line.substring ( 0, index );

    index = line.indexOf ( ";" );
    if ( index > 0 )  line = line.substring ( 0, index );

    // Extract the type and name from the current line.
    StringTokenizer tokens = new StringTokenizer ( line );
    try
    {
      types.addElement ( tokens.nextToken () );
      names.addElement ( tokens.nextToken () );
    }
    catch ( NoSuchElementException e )
    {
      System.out.println ( "Invalid input line '" + line + "'" );
    }  // catch
  }  // method parseLine


/******************************************************************************/
  private void startClass ()
  {
    writeComment ();

    output_file.println ();

    output_file.print ( "public class " );

    // Write the class name
    output_file.print ( getPrefix ( output_file_name ) );

    output_file.println ( " extends Object" );
    output_file.println ( "{" );

    writeComment ();
  }  // method startClass


/******************************************************************************/
  private void endClass ()
  {
    writeComment ();

    output_file.println ();

    output_file.println ( "}  // class " + getPrefix ( output_file_name ) );
  }  // method endClass


/******************************************************************************/
  private void writeConstructor ()
  {
    writeComment ();
    output_file.println ( "  // Constructor " + getPrefix ( output_file_name ) );
    output_file.println ( "  public " + getPrefix ( output_file_name ) + " ()" );
    output_file.println ( "  {" );
    output_file.println ( "    initialize ();" );
    output_file.println ( "  }  // constructor " + getPrefix ( output_file_name ) );
  }  // method writeConstructor


/******************************************************************************/
  private void writeInitialization ()
  {
    writeComment ();
    output_file.println ( "  // Initialize class variables." );
    output_file.println ( "  public void initialize ()" );
    output_file.println ( "  {" );

    // Initialize the variables.
    for ( int index = 0; index < names.size (); index++ )
    {
      String type = (String) types.elementAt ( index );

      output_file.print ( "    " );
      output_file.print ( (String) names.elementAt ( index ) );

      if ( type.equals ( "String" ) == true )

        output_file.println ( " = \"\";" );

      else if ( ( type.equals ( "int"  ) == true ) || 
                ( type.equals ( "short" ) == true ) ||
                ( type.equals ( "long" ) == true ) )

        output_file.println ( " = 0;" );

      else if ( type.equals ( "boolean" ) == true )

        output_file.println ( " = false;" );

      else if ( type.equals ( "char" ) == true )

        output_file.println ( " = ' ';" );

      else if ( type.equals ( "Vector" ) == true )

        output_file.println ( ".removeAllElements ();" );

      else if ( type.equals ( "StringBuffer" ) == true )

        output_file.println ( ".setLength ( 0 );" );

      else

        output_file.println ( " = null;" );

    }  // for

    output_file.println ( "  }  // method initialize " );
  }  // method writeInitialization


/******************************************************************************/
  public void processFile ()
  {
    // Set the input file name.
    input_file.initialize ();
    input_file.setFileName ( input_file_name );
    input_file.openFile ();

    // Set the output file name.
    output_file.initialize ();
    output_file.setFileName ( output_file_name );
    output_file.openFile ();

    // Start the Java class file.
    startClass ();

    // Process the input file.
    while ( input_file.isEndOfFile () != true )

      parseLine ();

    // Write the constructor.
    writeConstructor ();

    // Write the initialization routine.
    writeInitialization ();

    // Write the get routines.
    for ( int index = 0; index < names.size (); index++ )
    
      writeGet ( (String) types.elementAt ( index )
          , (String) names.elementAt ( index) );

    // Write the is routines.
    for ( int index = 0; index < names.size (); index++ )
   
      if ( ((String) types.elementAt ( index )).equals ( "boolean" ) == true )
 
        writeIs ( (String) types.elementAt ( index )
            , (String) names.elementAt ( index) );

    // Write the set routines.
    for ( int index = 0; index < names.size (); index++ )

      writeSet ( (String) types.elementAt ( index )
          , (String) names.elementAt ( index) );

    // End the Java class file.
    endClass ();
	
    // Close files.
    input_file.closeFile ();
    output_file.closeFile ();
  }  // method processFile


/******************************************************************************/
  public void newTag ( String tag_name, String attributes )
  {
    System.out.println ( "Tag: " + tag_name + ", attributes: " + attributes );
  }  // method newTag


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "This program generates new class shell templates." );
    System.out.println ();
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java NewCode <input_file>" );
    System.out.println ();
    System.out.println ( "where <input_file> is the file containing lines:" );
    System.out.println ();
    System.out.println ( "<data type><tab><variable name><tab><initial value>" 
        + "<tab><comment>" );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    NewCode app = new NewCode ();

    if ( args.length != 2 )
      app.usage ();
    else
    {
      app.setInputName  ( args[ 0 ] );
      app.setOutputName ( args[ 1 ] );
      app.processFile ();
    }  // else
  }  // method main


/******************************************************************************/

}  // class NewCode

