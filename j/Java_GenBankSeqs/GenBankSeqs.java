
import java.io.*;
import java.sql.*;

// import GenBankHeader;
// import GenBankSlice;
// import InputTools;

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

public class GenBankSeqs extends Object
{

/******************************************************************************/

private InputTools genbank_file = new InputTools ();	// GenBank input file

private String genbank_file_name;			// GenBank input file name

private String line = null; 				// Current line of the file


// GenBank file objects.
private GenBankHeader genbank_header = new GenBankHeader ();

private StringBuffer sequence = new StringBuffer ( 140000 );	// sequence data


/******************************************************************************/
public GenBankSeqs ()
{
  initialize ();
}  // constructor GenBankSeqs


/******************************************************************************/
public void initialize ()
{
  genbank_file_name = null;
  line = null;

  // references.removeAllElements ();
  sequence.setLength ( 0 );
}  // method initialize


/******************************************************************************/
public void setFileName ( String name )
{
  genbank_file_name = name;
}  // method setFileName


/******************************************************************************/
  private void parseHeader ()
  {
    StringBuffer header = new StringBuffer ( 1024 );	// Individual header section
    header.setLength ( 0 );
    genbank_header.initialize ();

    String header_type = "";			// current line type
    String previous_type = "";			// previous line type

    while ( ( genbank_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "//" ) != true ) &&
            ( line.startsWith ( "FEATURES" ) != true ) &&
            ( line.startsWith ( "BASE COUNT" ) != true ) &&
            ( line.startsWith ( "ORIGIN" ) != true ) &&
            ( line.startsWith ( "        1" ) != true ) )
    {
      if ( line.length () > 12 )
      {
        header_type = line.substring ( 0, 12 ).trim ();

        // Check for a continuation of the previous line.
        if ( header_type.equals ( "" ) == true )
        {
          if ( header.length () > 0 )
            header.append ( " " );
          header.append ( line.substring ( 12 ).trim () );
        }  // if
        else  // not continuation of previous header line
        {
          // Check for previous header.
          if ( previous_type.length () > 0 )
            genbank_header.setSection ( previous_type, header.toString () );

          previous_type = header_type;
          header.setLength ( 0 );
          header.append ( line.substring ( 12 ).trim () );

          // Check for the ORGANISM header line.
          if ( header_type.equals ( "ORGANISM" ) == true )
          {
            genbank_header.setSection ( header_type, header.toString () );
            previous_type = "Taxonomy";
            header.setLength ( 0 );
          }  // if
        }  // else

      }  // if

      line = genbank_file.getLine ().toString ();
    }  // while

    // Check for previous header.
    if ( header.length () > 0 )
      genbank_header.setSection ( previous_type, header.toString () );
  }  // method parseHeader


/******************************************************************************/
  private void parseSequence ()
  {
    // Reset the DNA sequence for the next sequence.
    sequence.setLength ( 0 );

    // Read in the DNA sequence.
    while ( ( genbank_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "//" ) != true ) )
    {
      // Add the current sequence line to the DNA sequence.
      int start = 10;
      while ( start < line.length () )
      {
        if ( line.length () >= start + 10 )
          sequence.append ( line.substring ( start, start + 10 ) );
        else
          sequence.append ( line.substring ( start ).trim () );

        start += 11;
      }  // while 

      line = genbank_file.getLine ().toString ();
    }  // while

    if ( sequence.length () != genbank_header.getSequenceLength () )
    {
      System.out.println ();
      System.out.println ( "*Warning* sequence length (" 
          + sequence.length () 
          + ") is different from header length ("
          + genbank_header.getSequenceLength () 
          + ")" );
      System.out.println ();
    }  // if

    // writeSequence ( sequence.toString () );
  }  // method parseSequence


/******************************************************************************/
  private void writeSequence ( String sequence )
  {
    for( int index = 0; index < sequence.length(); index += 50 )
    {
      if( index + 50 >= sequence.length() )
        System.out.println( sequence.substring( index ) );
      else
        System.out.println( sequence.substring( index, index + 50 ) );
    } // end: for
  }  // method writeSequence

 
/******************************************************************************/
  private void parseEntry ()
  {
    if ( genbank_file == null )  return;

    line = genbank_file.getLine ().toString ();

    // Process the GenBank entry.
    while ( ( genbank_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "//" ) != true ) )
    {
      if ( line.startsWith ( "LOCUS" ) )
      {
        // System.out.println ( line.toString () );
        parseHeader ();
      }  // if

      if ( line.startsWith ( "        1" ) == true )
      {
        parseSequence ();
      }  // if

      if ( line.startsWith ( "//" ) != true )
        line = genbank_file.getLine ().toString ();
    }  // while
  }  // method parseEntry


/******************************************************************************/
  public void processFile ()
  {
    // Setup GenBank entry processor.
    GenBankSlice gb_slice = new GenBankSlice ();
    gb_slice.setName ( genbank_file_name );

    // Set the input file name.
    genbank_file.initialize ();
    genbank_file.setFileName ( genbank_file_name );

    // Open the input file.
    genbank_file.openFile ();

    // Process the input file.
    while ( genbank_file.isEndOfFile () != true )
    {
      parseEntry ();

      gb_slice.processEntry ( genbank_header, sequence.toString () );

      // Extract domains from the entry.
      genbank_header.initialize ();
      sequence.setLength ( 0 );
    }  // while

    // Close input file.
    genbank_file.closeFile();

    gb_slice.close ();
    gb_slice = null;
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java GenBankSeqs <GenBank_file>" );
    System.out.println ();
    System.out.print   ( "where <GenBank_file> is the file name of a " );
    System.out.println ( "GenBank DNA sequence file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    GenBankSeqs app = new GenBankSeqs ();

    if ( args.length != 1 )
      app.usage ();
    else
    {
	 app.setFileName ( args[ 0 ] );
	 app.processFile ();
    }  // else
  }  // method main
}  // class GenBankSeqs

