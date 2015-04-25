
import java.io.*;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

public class XMLTools extends OutputTools
{

/******************************************************************************/

  private int nesting_level = 0;		// XML nesting level

  private PrintStream xml_file = null;	// XML output print stream file


/******************************************************************************/
  public XMLTools ()
  {
    init ();
  }  // constructor XMLTools


/******************************************************************************/
  private void init ()
  {
    initialize ();

    nesting_level = 0;

    xml_file = null;
  }  // method init


/******************************************************************************/
  public void open_XML_file ()
  {
    openFile ();

    xml_file = getPrintStream ();
  }  // method open_XML_file


/******************************************************************************/
  public void close_XML ()
  {
    closeFile ();

    xml_file = null;
  }  // method close_XML


/******************************************************************************/
  public void write_XML_header ()
  {
    xml_file.print ( "<?xml version='1.0' encoding='ISO-8859-1'" );
    xml_file.print ( " standalone='no'?>" );
    xml_file.println ();

    xml_file.println ( "<!DOCTYPE Entries SYSTEM 'xmlloader.dtd'>" );

    xml_file.println ();
    xml_file.println ( "<Entries>" );
    nesting_level++;
  }  // method write_XML_header


/******************************************************************************/
  public void write_XML_footer ()
  {
    nesting_level--;
    xml_file.println ( "</Entries>" );
  }  // method write_XML_footer


/******************************************************************************/
  private void spacing ()
  {
    for ( int i = 0; i < nesting_level; i++ )
      xml_file.print ( "  " );
  }  // method spacing


/******************************************************************************/
  public void write_XML_entry ( String name )
  {
    spacing ();
    xml_file.println ( "<Entry name=\"" + name + "\">" );
    nesting_level++;
  }  // method write_XML_entry


/******************************************************************************/
  public void write_XML_entry_end ()
  {
    nesting_level--;
    spacing ();
    xml_file.println ( "</Entry>" );
    xml_file.println ();
  }  // method write_XML_entry_end


/******************************************************************************/
  public void write_XML_field ( String name )
  {
    spacing ();
    xml_file.println ( "<Field name=\"" + name + "\">" );
  }  // method write_XML_field


/******************************************************************************/
  public void write_XML_field ( String name, String name_value )
  {
    spacing ();
    xml_file.print ( "<Field name=\"" + name + "\">" + name_value );
    xml_file.println ( "</Field>" );
  }  // method write_XML_field


/******************************************************************************/
  public void write_XML_field 
      ( String name
      , String name_value 
      , String parameter
      , String parameter_value
      )
  {
    spacing ();
    xml_file.print ( "<Field name=\"" + name + "\"" );
    xml_file.print ( " " + parameter + "=\"" + parameter_value );
    xml_file.print ( "\">" + name_value );
    xml_file.println ( "</Field>" );
  }  // method write_XML_field


/******************************************************************************/
  public void write_XML_field_end ()
  {
    spacing ();
    xml_file.println ( "</Field>" );
  }  // method write_XML_filed_end


/******************************************************************************/
  public void write_XML_clob_field ( String field_name, String clob )
  {
    // Create the sequence_clob field.
    write_XML_field ( field_name );

    // Write the sequence.
    xml_file.println ( clob );

    // End the sequence_clob field.
    write_XML_field_end ();
  }  // method write_XML_clob_field


/******************************************************************************/
  public void write_XML_clob_field2 ( String field_name, String clob )
  {
    // Create the sequence_clob field.
    write_XML_field ( field_name );

    // Write the sequence.
    for ( int index = 0; index < clob.length (); index += 50 )
    {
      if ( index + 50 >= clob.length () )
        xml_file.println ( clob.substring ( index ) );
      else
        xml_file.println ( clob.substring ( index, index+50 ) );
    }  // for

    // End the sequence_clob field.
    write_XML_field_end ();
  }  // method write_XML_clob_field2


/******************************************************************************/
// This method writes out a quality array of integer numbers.
  public void write_XML_qual_field ( String field_name, String clob )
  {
    // Create the sequence_clob field.
    write_XML_field ( field_name );

    // Write the sequence.
    for ( int index = 0; index < clob.length (); )
    {
      int end = index + 50;
      if ( end >= clob.length () )  end = clob.length () - 1;
      else
      {
        // Check for the end of the quality string.
        if ( end < clob.length () - 1 )

          // Break the line at a space.
          while ( clob.charAt ( end ) != ' ' )  end--;
      }  // else

      end++;
      xml_file.println ( clob.substring ( index, end ) );
      index = end;
    }  // for

    // End the sequence_clob field.
    write_XML_field_end ();
  }  // method write_XML_qual_field


/******************************************************************************/
  public static void main ( String [] args )
  {
    XMLTools app = new XMLTools ();

    app.setFileName ( "test.xml" );

    app.open_XML_file ();

    app.write_XML_header ();

    app.write_XML_entry ( "Clone" );

    app.write_XML_field ( "clone_name", "BCD421" );

    app.write_XML_entry_end ();

    app.write_XML_footer ();

    app.close_XML ();
  }  // method main

}  // class XMLTools

