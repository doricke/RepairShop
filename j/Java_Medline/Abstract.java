

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

public class Abstract extends Object
{


/******************************************************************************/

  private   String  pmid = "";			// PMID

  private   String  title = "";			// Journal article title

  private   String article_abstract = "";	// Article abstract

  private   String names = "";			// Author names


/******************************************************************************/
  // Constructor Abstract
  public Abstract ()
  {
    initialize ();
  }  // constructor Abstract


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    pmid = "";
    title = "";
    article_abstract = "";
    names = "";
  }  // method initialize 


/******************************************************************************/
  public void addName ( String value )
  {
    if ( names.length () <= 0 )
      names = value;
    else
      names += " " + value;
  }  // method addName

/******************************************************************************/
  public String getPmid ()
  {
    return pmid;
  }  // method getPmid


/******************************************************************************/
  public String getTitle ()
  {
    return title;
  }  // method getTitle


/******************************************************************************/
  public String getAbstract ()
  {
    return article_abstract;
  }  // method getAbstract


/******************************************************************************/
  public String getNames ()
  {
    return names;
  }  // method getNames


/******************************************************************************/
  public void setPmid ( String value )
  {
    pmid = value;
  }  // method setPmid


/******************************************************************************/
  public void setTitle ( String value )
  {
    title = value;
  }  // method setTitle


/******************************************************************************/
  public void setAbstract ( String value )
  {
    article_abstract = value;
  }  // method setAbstract


/******************************************************************************/
  public void setNames ( String value )
  {
    names = value;
  }  // method setNames


/******************************************************************************/
  public void setTag ( String tag_name, String tag_value )
  {
    if ( tag_name.equals ( "PMID" ) == true )
      setPmid ( tag_value );
    else if ( tag_name.equals ( "TI" ) == true )
           setTitle ( tag_value );
    else if ( tag_name.equals ( "AB" ) == true )
           setAbstract ( tag_value );
    else if ( tag_name.equals ( "FAU" ) == true )
           addName ( tag_value );
    
  }  // method setTag


/******************************************************************************/
  public String toString ()
  {
    return "PMID- " + pmid + "\nTI  - " + title + "\nAB  - " 
        + article_abstract + "\nFAU - " + names;
  }  // method toString


/******************************************************************************/

}  // class Abstract
