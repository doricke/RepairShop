
import java.io.*;
import java.util.*;

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

public class OligoParser extends Object
{

/******************************************************************************/

private boolean end_of_file = false;	// End of file flag

private String fileName = "";		// Input file name

private BufferedReader oligosFile;	// oligos file

private StringBuffer line = new StringBuffer ( 540 );			// Current line of the file

private StringBuffer previous_line = new StringBuffer ( 540 );		// Previous line of the file


private StringBuffer sequenceName = new StringBuffer ( 80 );	// sequence name

private int position;						// position of word in sequence 

private StringBuffer word_seq = new StringBuffer ( 40 );	// Sequence hash word


/******************************************************************************/
public OligoParser ()
{
  initialize ();
}  // constructor OligoParser


/******************************************************************************/
private void initialize ()
{
  sequenceName.setLength ( 0 );
  position = 0;
  word_seq.setLength ( 0 );
  line.setLength ( 0 );
}  // method initialize


/******************************************************************************/
public void close ()
{
  closeInputFile ();
}  // method close


/******************************************************************************/
public int getPosition ()
{
  return position;
}  // method getPosition


/******************************************************************************/
public String getWordSeq ()
{
  return word_seq.toString ();
}  // method getWordSeq


/******************************************************************************/
private int getInteger ( String line )
{
  int i = 0;
  int index = 0;
  int sign = 1;					// Default sign = +

  // Skip leading white space.
  while ( ( line.charAt ( index ) == ' ' ) ||
          ( line.charAt ( index ) == '\t' ) )  index++;

  // Check for a sign.
  if ( line.charAt ( index ) == '+' )
    index++;
  else
    if ( line.charAt ( index ) == '-' )
    {
      sign = -1;
      index++;
    }  /* if */

  // Traverse the integer.
  while ( index < line.length () )
  {
    if ( ( line.charAt ( index ) >= '0' ) && ( line.charAt ( index ) <= '9' ) )

      i = i * 10 + (int) line.charAt ( index ) - (int) '0';

    else  index = line.length ();		// Terminate loop

    index++;
  }  /* while */

  // Set the sign.
  i *= sign;

  return ( i );					// Return the integer
}  /* method getInteger */


/******************************************************************************/
public String getFileName ()
{
  return fileName;
}  // method getFilename


/******************************************************************************/
public String getSequenceName ()
{
  return sequenceName.toString ();
}  // method getSequenceName


/******************************************************************************/
public boolean isEndOfFile ()
{
  return end_of_file;
}  // method isEndOfFile


/******************************************************************************/
public void setFileName ( String file_name )
{
  fileName = file_name;

  end_of_file = false;

  openInputFile ( file_name );
}  /* method setFileName */


/******************************************************************************/
private void openInputFile ( String file_name )
{
  try
  {
    oligosFile = new BufferedReader ( new InputStreamReader 
        ( new FileInputStream ( file_name ) ) );
  }  // try 
  catch ( IOException e )
  {
    System.out.println ( "OligoParser.openFile: IOException on input file " +
        file_name + ": " + e );
  }  /* catch */
}  // method openInputFile


/******************************************************************************/
private void closeInputFile ()
{
  if ( oligosFile == null )  return;

  /* Close the input file */
  try 
  {
    oligosFile.close ();
  }
  catch ( IOException e  )
  {
    System.out.println ( "OligoParser.closeInputFile: IOException while closing input file: " 
        + e  );
    return;
  }  // catch
}  // method closeInputFile 


/******************************************************************************/
public void nextWord ()
{
  // Check for end of file.
  if ( end_of_file == true )  return;

  if ( oligosFile == null )  return;


  try 
  {
    initialize ();
    line.setLength ( 0 );
    line.append ( oligosFile.readLine () );
    while ( previous_line.toString ().equalsIgnoreCase ( line.toString () ) == true )
    {
      line.setLength ( 0 );
      line.append ( oligosFile.readLine () );
    }  // if

    previous_line.setLength ( 0 );
    previous_line.append ( line.toString () );

    if ( ( line.length () < 1 ) || ( line.toString ().equals ( "null" ) ) )
    {
      end_of_file = true;
      return;
    }  // if

    StringTokenizer tokens = new StringTokenizer ( line.toString (), " " );

    try
    {
      word_seq.append     ( tokens.nextToken () );
      sequenceName.append ( tokens.nextToken () );
      position = getInteger ( tokens.nextToken () );
    }  // try
    catch ( NoSuchElementException e )
    {
System.out.println ( "NoSuchElementException: " + e );
snapShot ();
      end_of_file = true;
      return;
    }  // catch
  }  // try
  catch ( IOException e )
  {
    System.out.println ( "OligoParse.readResult: IOException: " + e );
    end_of_file = true;
  }  // method catch
}  // method nextWord


/******************************************************************************/
  public void snapShot ()
  {
    if ( end_of_file == true )  return;

    System.out.print ( sequenceName + "\t" );
    System.out.print ( position + "\t" );
    System.out.print ( word_seq );
    System.out.println ();
  }  // method snapShot


/******************************************************************************/
  public static void main ( String [] args )
  {
    OligoParser app = new OligoParser ();

    app.setFileName ( "test.sort" );

    while ( app.isEndOfFile () == false )
    {
      app.nextWord ();
      app.snapShot ();
    }  // while 

    app.close ();
  }  // method main

}  // class OligoParser

