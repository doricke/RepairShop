/* This class creates a hash table for all field and entry names in
    the Enzyme.ace_xml.xml file						 */

import java.util.*;
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

public class EnzymeHash
{


 private InputTools ace_file;

 private Hashtable hash;

 private StringBuffer line;


 public EnzymeHash()
 {
  ace_file = new InputTools();
  hash = new Hashtable();
  line = new StringBuffer( 540 );
 } // end: constuctor EnzymeHash

 public boolean containsKey( Object key )
 {
  return hash.containsKey( key );
 } // end: method containsKey

 public void processFile()
 {
  int index = 0;

  ace_file.setFileName( "Enzyme.ace_xml.xml" );
  ace_file.openFile();

  while( ace_file.isEndOfFile() == false )
  {
   line = ace_file.getLine();

   if( line.toString() .indexOf( "<Field name=" ) > 0 )
   {
	String field_line = line.toString();

	index = field_line.indexOf( "Field" );
	String field_value = field_line.substring( index, field_line.indexOf( ' ', index ) );

	index = field_line.indexOf( '"', index );
	String field_key = field_line.substring( index + 1, field_line.indexOf( '"', index + 1 ) );

	hash.put( field_key, field_value );
   } // end: if

  } // end: while


  System.out.println( hash.toString() );

  ace_file.closeFile();

 } // end: method processFile

 public static void main( String[] args )
 {
  EnzymeHash app = new EnzymeHash();
  app.processFile();
 } // end: main

} // end: class EnzymeHash
