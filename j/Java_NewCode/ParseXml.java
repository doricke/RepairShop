
import java.io.*;
import java.util.Vector;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
public class ParseXml extends Object
{

  String file_name = "";		// input file name

  Document doc = null;			// XML document

  int nesting = 1;			// nesting level

  Vector nodes = new Vector ();		// XML nodes


/******************************************************************************/
public ParseXml ()
{
  initialize ();
}  // constructor ParseXml


/******************************************************************************/
public void initialize ()
{
  nodes.removeAllElements ();
}  // method initialize


/******************************************************************************/
public void close ()
{
  nodes.removeAllElements ();
}  // method close


/******************************************************************************/
public String getFileName ()
{
  return file_name;
}  // method getFileName


/******************************************************************************/
public int getNesting ()
{
  return nesting;
}  // method getNesting


/******************************************************************************/
public Vector getNodes ()
{
  return nodes;
}  // method getNodes


/******************************************************************************/
public void printNodes ()
{
  String xml_node;

  for ( int i = 0; i < nodes.size (); i++ )
  {
    xml_node = (String) nodes.elementAt ( i );

    System.out.println ( xml_node );
  }  // for
}  // method printNodes


/******************************************************************************/
public void setFileName ( String filename )
{
  file_name = filename;
}  // method setFileName 


/******************************************************************************/
// This method prints out the spacing for the nesting level.
private void spacing ()
{
  for ( int i = 1; i < nesting; i++ )

    System.out.print ( "  " );
}  // method spacing


/******************************************************************************/
private void processNode ( Node node )
{
  if ( node.getNodeType () == org.w3c.dom.Node.ELEMENT_NODE )

    if ( node.getAttributes ().getLength () > 0 )
    
      nodes.add ( node.getNodeName () + " " + node.getAttributes () );
}  // method processNode


/******************************************************************************/
public void processDocument ( NodeList children )
{
  int count = children.getLength ();

  nesting++;

  for ( int i = 0; i < count; i++ )
  {
    if ( ( children.item ( i ).getNodeValue () == null ) ||
         ( children.item ( i ).getNodeValue ().equals ( " " ) != true ) )
    {
      processNode ( children.item ( i ) );
    }  // if

    NodeList kids = children.item ( i ).getChildNodes ();

    if ( kids != null )  processDocument ( kids );
  }  // for

  nesting--;
}  // method processDocument


/******************************************************************************/
// This method parses the XML file.
public void parseXml ()
{
  try
  {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance ();

    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder ();

    doc = docBuilder.parse ( new File ( file_name ) );

    doc.getDocumentElement ().normalize ();

    // processNode ( doc.getDocumentElement () );

    processDocument ( doc.getDocumentElement ().getChildNodes () );
  }  // try
  catch ( SAXParseException err )
  {
    System.out.println ( "Parsing error" );
    System.out.println ( err.getMessage () );
  }  // catch
  catch ( SAXException e )
  {
    Exception x = e.getException ();

    if ( x == null )
      e.printStackTrace ();
    else
      x.printStackTrace ();
  }  // catch
  catch (Throwable t)
  {
    t.printStackTrace ();
  }  // catch
}  // method parseXml


/******************************************************************************/
public static void usage ()
{
  System.out.println ( "This is the ParseXml program." );
  System.out.println ();
  System.out.println ( "This program reads an XML file." );
  System.out.println ();
  System.out.println ( "To run type:" );
  System.out.println ();
  System.out.println ( "java ParseXml <name.xml>" );
  System.out.println ();
  System.out.println ( "Where <name.xml> is an XML filename." );
}  // method usage


/******************************************************************************/
public static void main ( String [] args )
{
  // Check for parameters.
  if ( args.length == 0 )
  {
    usage ();
  }
  else
  {
    ParseXml application = new ParseXml ();

    // The XML file name is the first parameter.
    application.setFileName ( args [ 0 ] );

    // Process the XML file.
    application.parseXml ();

    application.printNodes ();

    application.close ();
  }  // else
}  // method main

}  // class ParseXml
