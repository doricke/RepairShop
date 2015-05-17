
import java.io.*;
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

  String fileName = "";			// input file name

  Document doc = null;			// XML document

  int nesting = 1;			// nesting level


/******************************************************************************/


/******************************************************************************/
// Parameters:


/******************************************************************************/
public ParseXml ()
{
  initialize ();
}  /* constructor ParseXml */


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  // method setFileName


/******************************************************************************/
// This method prints out the spacing for the nesting level.
private void spacing ()
{
  for ( int i = 1; i < nesting; i++ )

    System.out.print ( "  " );
}  // method spacing


/******************************************************************************/
private void printNode ( Node node )
{
  spacing ();

  System.out.print ( nesting +
    " Type " + node.getNodeType () +
    ", Name " + node.getNodeName () );

  if ( node.getNodeValue () != null )

    System.out.print ( ", Value '" + node.getNodeValue () + "'" );

  if ( node.getNodeType () == org.w3c.dom.Node.ELEMENT_NODE )

    if ( node.getAttributes ().getLength () > 0 )

      System.out.print ( ", Attr '" + node.getAttributes () + "'" );

  System.out.println ();
}  // method printNode


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
      printNode ( children.item ( i ) );
    }  // if

    NodeList kids = children.item ( i ).getChildNodes ();

    if ( kids != null )  processDocument ( kids );
  }  // for

  nesting--;
}  // method processDocument


/******************************************************************************/
public void parseStream ( DataInputStream in )
{
  try
  {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance ();

    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder ();

    doc = docBuilder.parse ( in );

    doc.getDocumentElement ().normalize ();

    printNode ( doc.getDocumentElement () );

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
}  // method parseStream


/******************************************************************************/
// This method processes the Phrap .ace file.
public void processXml ()
{
  try
  {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance ();

    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder ();

    doc = docBuilder.parse ( new File ( fileName ) );

    doc.getDocumentElement ().normalize ();

    printNode ( doc.getDocumentElement () );

    processDocument ( doc.getDocumentElement ().getChildNodes () );
  }  // try
  catch ( SAXParseException err )
  {
    System.out.println ("** Parsing error"
    + ", line " + err.getLineNumber ()
    + ", uri " + err.getSystemId ());
    System.out.println("   " + err.getMessage ());
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
}  // method processXml


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
  /* Check for parameters. */
  if ( args.length == 0 )
  {
    usage ();
  }
  else
  {
    ParseXml application = new ParseXml ();

    // The .ace file name is the first parameter.
    application.setFileName ( args [ 0 ] );

    // Process the .ace contigs.
    application.processXml ();
  }  /* else */
}  /* method main */

}  /* class ParseXml */
