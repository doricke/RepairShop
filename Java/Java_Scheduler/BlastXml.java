import java.io.*;
import XMLTools;

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
public class BlastXml extends Object
{


/******************************************************************************/


/******************************************************************************/
// Parameters:


/******************************************************************************/
public BlastXml ()
{
  initialize ();
}  /* constructor BlastXml */


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/


/******************************************************************************/
private void startProgram 
    ( XMLTools xml_file
    , String prefix
    , String label
    , String program_name
    , String program_version 
    )
{
  // Create the XML entry.
  xml_file.write_XML_entry ( "Program" );

  // Create the fields.
  xml_file.write_XML_field ( "prefix",          prefix );
  xml_file.write_XML_field ( "label",           label );
  xml_file.write_XML_field ( "program_name",    program_name );
  xml_file.write_XML_field ( "program_version", program_version );
}  // method startProgram


/******************************************************************************/
private void writeParameter 
    ( XMLTools xml_file
    , String prefix
    , String label
    , String value
    , String default_value
    , String required
    , String multiple
    , String type 
    , String gui_type 
    )
{
  // Create the XML entry.
  xml_file.write_XML_entry ( "Parameter" );

  // Create the fields.
  xml_file.write_XML_field ( "prefix",        prefix );
  xml_file.write_XML_field ( "label",         label );
  xml_file.write_XML_field ( "value",         value );
  xml_file.write_XML_field ( "default_value", default_value );
  xml_file.write_XML_field ( "required",      required );
  xml_file.write_XML_field ( "multiple",      multiple );
  xml_file.write_XML_field ( "type",          type );
  xml_file.write_XML_field ( "gui_type",      gui_type );

  // End the entry.
  xml_file.write_XML_entry_end ();
}  // method writeParameter


/******************************************************************************/


/******************************************************************************/
private void generateBlast2Parameters ( XMLTools xml_file )
{
  //  ( xml_file, prefix, label, value, default, required, multiple, type, gui_type )
  writeParameter ( xml_file, "-i", "Query sequence", "", "", "true", "false", "", "TextField" );
  writeParameter ( xml_file, "-v", "Max Alignments", "20", "20", "true", "false", "", "TextField" );
  writeParameter ( xml_file, "-b", "Max Scores:", "20", "20", "true", "false", "", "TextField" );
  writeParameter ( xml_file, "-E", "Expectation: ", "", "", "false", "false", "", "TextField" );
  writeParameter ( xml_file, "-d", "Database", "plantfungal", "plantfungal", "true", "false", "", "TextField" );


//  writeParameter ( xml_file, "-altscore", "altscore", "", "", "false", "false", "", "TextField" );
//  writeParameter ( xml_file, "-asn1", "ASN.1 text", "", "", "false", "false", "", "CheckBox" );
//  writeParameter ( xml_file, "-asn1bin", "ASN.1 binary", "", "", "false", "false", "", "CheckBox" );
//  writeParameter ( xml_file, "-bottom", "bottom", "", "", "false", "false", "", "CheckBox" );
//  writeParameter ( xml_file, "-codoninfo", "codoninfo", "", "", "false", "false", "", "TextField" );
//  writeParameter ( xml_file, "-compat1.3", "BLAST 1.3 compatibility", "", "", "false", "false", "", "CheckBox" );
//  writeParameter ( xml_file, "-consistency", "consistency", "", "", "false", "false", "", "CheckBox" );
}  // method generateBlast2Parameters


/******************************************************************************/
public void generateBlastXml ( String program_name, String version )
{
  XMLTools xml_file = new XMLTools ();		// XML output file

  // Set the XML file name.
  xml_file.setFileName ( program_name + "_" + version + ".xml" );

  // Open the XML file for writing.
  xml_file.open_XML_file ();

  // Write the XML file header.
  xml_file.write_XML_header ();

  // Complete the XML file.
  startProgram ( xml_file, "blastall -p", program_name, program_name, version );

  // Generate the parameters.
  generateBlast2Parameters ( xml_file );

  // End the Program entry.
  xml_file.write_XML_entry_end ();

  // Complete the XML file.
  xml_file.write_XML_footer ();
  xml_file.close_XML ();
  xml_file = null;
}  // method generateBlastXml


/******************************************************************************/
public static void usage ()
{
  System.out.println ( "This is the BlastXml program." );
  System.out.println ();
  System.out.println ( "This program writes a BLAST XML file." );
  System.out.println ();
  System.out.println ( "To run type:" );
  System.out.println ();
  System.out.println ( "java BlastXml" );
}  // method usage


/******************************************************************************/
public static void main ( String [] args )
{
  /* Check for parameters. */
  BlastXml application = new BlastXml ();

  application.generateBlastXml ( "blastn", "2.0" );
  application.generateBlastXml ( "blastx", "2.0" );
  application.generateBlastXml ( "tblastn", "2.0" );
}  /* method main */

}  /* class BlastXml */
