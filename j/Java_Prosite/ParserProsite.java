

// import Prosite;

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

public class ParserProsite extends Object
{

/******************************************************************************/

private  OutputTools  perl_file = new OutputTools ();		// perl patterns output file


/******************************************************************************/
public ParserProsite ()
{
  initialize ();
}  // constructor ParserProsite


/******************************************************************************/
private void initialize ()
{
}  // method initialize


/******************************************************************************/
public void close ()
{
  // Close the files.
  perl_file.closeFile ();
}  // method close


/*******************************************************************************/
  public void processFile ()
  {
    // Create the Prosite file parser object.
    Prosite prosite = new Prosite ();

    perl_file.setFileName ( "prosite.dat.perl" );
    perl_file.openFile ();

    while ( prosite.isEndOfFile () == false )
    {
      // Read in the next Prosite file entry.
      prosite.next ();

      // Check for a motif pattern.
      if ( ( prosite.getPaLines ().length () > 0 ) &&
           ( prosite.getCcSkipFlag ().equals ( "TRUE" ) == false ) )
      {
        // Write out the prosite pattern as a Perl regular expression.
        perl_file.print ( prosite.getName () + "\t" );
        perl_file.print ( prosite.getPatternPerl () + "\t" );
        perl_file.print ( prosite.getAccession () + "\t" );
        perl_file.println ( prosite.getDeLines () );
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
    System.out.println ( "java ParserProsite" );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    ParserProsite app = new ParserProsite ();

    // app.usage ();
    app.processFile ();
  }  // method main

}  // class ParserProsite

