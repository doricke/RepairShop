
// import AminoWord3;

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

public class WordIterator extends InputTools
{


/******************************************************************************/

  AminoWord3 amino_word = null;		// Current amino word


/******************************************************************************/
public WordIterator ()
{
  initialize ();
}  // constructor WordIterator


/******************************************************************************/
public WordIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method WordIterator


/*******************************************************************************/
  public AminoWord3 getAminoWord ()
  {
    return amino_word;
  }  // method getAminoWord


/*******************************************************************************/
  public AminoWord3 next ()
  {
    amino_word = new AminoWord3 ();		// create a new object

    // Check for first or blank line.
    if ( line.length () <= 0 )  getLine ();

    // Parse the line.
    amino_word.parseLine ( line.toString () );

    return amino_word;
  }  // method next


/******************************************************************************/

}  // class WordIterator

