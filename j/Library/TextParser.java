// Generic text parser class - to be extended

import InputTools;

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

public abstract class TextParser extends Parser
{

/******************************************************************************/

protected InputTools input_file;				// text input file

protected StringBuffer line;					// current line of file

protected Parameters parameters;				// parameters object

/******************************************************************************/

public TextParser()
{
 super();
 input_file = new InputTools();
 line = new StringBuffer( 540 );
 line.setLength( 0 );
} // end: constructor TextParser

/******************************************************************************/

public abstract void processFile();

/******************************************************************************/

public abstract void usage();

/******************************************************************************/

// parameters object stores filename etc.
public void setParameters( Parameters parameters_object )
{
 parameters = parameters_object;
} // end: method setParameters

/******************************************************************************/

protected void writeClob()
{
 // Write the result_clob field
 xml_file.write_XML_field( "result_clob" );

 // Close & re-open the input file
 input_file.openFile();

 // Get the first line of the input file
 line = input_file.getLine();

 // Copy the input file
 while( input_file.isEndOfFile() == false )
 {
  // Write the current line
  xml_file.println( line.toString() );

  // Get the next line of the input file
  line = input_file.getLine();
 }  // end: while

 input_file.closeFile();

 // End the clob XML field
 xml_file.write_XML_field_end();

}  // end: method writeClob

/******************************************************************************/

// Finds the next line starting with input string.  Returns true if
// encounters end of file, false otherwise.

public boolean findLine( String check )
{
 line = input_file.getLine();
 while( line.toString() .startsWith( check ) == false )
 {
  if( input_file.isEndOfFile() == true ) return !input_file.isEndOfFile();
  line = input_file.getLine();
 } // end: while
 return !input_file.isEndOfFile();
} // end: method findLine

/******************************************************************************/

public int skipWhiteSpace( String s, int index )
{
 if( ( index >= 0 ) && ( index < s.length() ) )
 {
  while( ( ( s.charAt( index ) == ' ' ) ||
           ( s.charAt( index ) == '\t' ) ) &&
         ( index < s.length() - 1 ) )index++;
 } // end: if

 // return index if it is with in the string's bounds and is not a blank character
 if( ( index < s.length() ) && ( index >= 0 ) )
 {
  if( ( s.charAt( index ) != ' ' ) && ( s.charAt( index ) != '\t' ) ) return index;
  else return -1;
 } // end: if
 else return -1;
} // end: method skipWhiteSpace

/******************************************************************************/


} // end: abstract class TextParser
