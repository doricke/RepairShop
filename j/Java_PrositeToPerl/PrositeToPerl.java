

// import Pattern;

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

public class PrositeToPerl extends Object
{

/******************************************************************************/

private  OutputTools  perl_file = new OutputTools ();		// perl patterns output file

private  InputTools  prosite_file = new InputTools ();		// GCG prosite patterns file

private  StringBuffer  line = new StringBuffer ( 540 );		// Current line of the file


/******************************************************************************/
public PrositeToPerl ()
{
  initialize ();
}  // constructor PrositeToPerl


/******************************************************************************/
private void initialize ()
{
  line.setLength ( 0 );
}  // method initialize


/******************************************************************************/
public void close ()
{
  // Close the files.
  perl_file.closeFile ();
  prosite_file.closeFile ();
}  // method close


/*******************************************************************************/
  public void processFile ( String file_name )
  {
    // Create a new pattern object.
    Pattern pattern = new Pattern ();

    // Set up the input file.
    prosite_file.setFileName ( file_name );
    prosite_file.openFile ();

    perl_file.setFileName ( file_name + ".perl" );
    perl_file.openFile ();

    while ( prosite_file.isEndOfFile () == false )
    {
      // Get the next GCG prosite pattern file line.
      line = prosite_file.getLine ();

      // Check for a line with data.
      if ( line.length () > 0 )
      {
        // Parse the new GCG prosite pattern line.
        pattern.newPattern ( line.toString () );

        // Write out the prosite pattern as a Perl regula expression.
        perl_file.print ( pattern.getName () + "\t" );
        perl_file.print ( pattern.getPatternPerl () + "\t" );
        perl_file.println ( pattern.getPdocName () );
      }  // if
    }  // while

    // Close files.
    close ();
  }  // method processFile


/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java PrositeToPerl prosite.patterns" );
    System.out.println ();
    System.out.println ( "where prosite.patterns is the GCG prosite.patterns file." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    PrositeToPerl app = new PrositeToPerl ();

    if ( args.length == 0 )
    {
      app.usage ();
      app.processFile ( "prosite.patterns" );
    }  // if
    else
      app.processFile ( args [ 0 ] );
  }  // method main

}  // class PrositeToPerl

