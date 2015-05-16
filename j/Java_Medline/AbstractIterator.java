
// import Abstract;

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

public class AbstractIterator extends InputTools
{


/******************************************************************************/

  Abstract article_abstract = null;		// Current 


/******************************************************************************/
public AbstractIterator ()
{
  initialize ();
}  // constructor AbstractIterator


/******************************************************************************/
public AbstractIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
  nextLine ();		// get the first line of the file
}  // method AbstractIterator


/*******************************************************************************/
  public Abstract getAbstract ()
  {
    return article_abstract;
  }  // method getAbstract


/*******************************************************************************/
  private void readAbstract ()
  {
    boolean ui_found = false;
    String  tag_name = "";
    StringBuffer tag_value = new StringBuffer ( 10000 );

    do
    {
      String next_tag_name = "";
      if ( line.length () > 4 )  next_tag_name = line.substring ( 0, 4 ).trim ();

      // Check for a tag name (UI, PMID, AB, etc.).
      if ( next_tag_name.trim ().length () > 0 )
      {
        // Check for a previous tag.
        if ( tag_name.length () > 0 )
        {
          article_abstract.setTag ( tag_name, tag_value.toString () );
          tag_value.setLength ( 0 );
        }  // if

        // Update the current tag name.
        tag_name = next_tag_name;
      }  // if

      // Update the current tag value.
      if ( line.length () > 6 )
        tag_value.append ( line.substring ( 6 ) + " " );

      nextLine ();		// get the next line

      // Check for the start of the next abstract.
      if ( line.length () > 5 )
      {
        if ( ( line.charAt ( 0 ) == 'U' ) && ( line.charAt ( 1 ) == 'I' ) )

          ui_found = true;
      }  // if
    }  
    while ( ( ui_found == false ) && ( isEndOfFile () == false ) );
  }  // method readAbstract


/*******************************************************************************/
  public Abstract next ()
  {
    article_abstract = new Abstract ();		// create a new object

    // Read in the next article abstract.
    readAbstract ();

    return article_abstract;
  }  // method next


/******************************************************************************/
  public static void main ( String [] args )
  {
    AbstractIterator application = new AbstractIterator ( "test.medline" );

    do
    {
      Abstract article = application.next ();
      System.out.println ( article.toString () );
      System.out.println ();
    }
    while ( application.isEndOfFile () == false );

  }  // main


/******************************************************************************/

}  // class AbstractIterator

