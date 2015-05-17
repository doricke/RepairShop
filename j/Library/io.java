import java.io.*;
import java.util.StringTokenizer;

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

public class io extends Object
{
  /************************************************************************/
  public io ()
  {  
  }  /* io */

  /************************************************************************/
  public static void GetFileNames ( String path, String [] currFiles )
  {
    // Create a File instance for the current directory
    File currDir = new File ( path );
    
    // Get a filtered list of the files in the current directory
    currFiles = currDir.list ();
    
//    ListNames ( currFiles );
   }  /* GetFileNames */
  
  /************************************************************************/
  public static void ListNames ( String [] nameList )
  {
    // Print out the contents of the nameList array
    for ( int i = 0; i < nameList.length; i++ )
    {
      System.out.println ( nameList [ i ] );
    }  /* for */
  }  /* ListNames */

  /************************************************************************/
  public static void ReadFile ( String filename )
  {
    try
    {
      DataInputStream in_file = new DataInputStream 
          ( new FileInputStream ( filename ) );
    
      /* Read in the entire file. */
      boolean end_of_file = false;
      int i = 0;
      while ( ( end_of_file == false ) && ( i < 20 ) )
      {
        String line;
        
        try
        {
          line = in_file.readLine ();
          
          if ( line == null )
            end_of_file = true;
          else
            System.out.println ( line );
        }
        catch ( EOFException e1 )
        {
          System.out.println ( "End of file reached." + e1 );
          end_of_file = true;
        }  /* catch */
        catch ( IOException e2 )
        {
          System.out.println ( "IOException on input file." + e2 );
        }  /* catch */
        
        i++;  
      }  /* while */ 
      
      // Close the input file.
      try
      {
        in_file.close ();
      }
      catch ( IOException e4 )
      {
        System.out.println ( "IOException on clone file " + e4 );
      }  /* catch */ 
    }
    catch ( FileNotFoundException e3 )
    {
      System.out.println ( "File not found '" + filename + "'" + e3 );
      return;
    }  /* catch */
  }  /* ReadFile */
   
  /************************************************************************/
  public static String ReadSeq ( String filename )
  {
    short Unknown = 0;                  // Unknown file type
    short GenBank = 1;                  // GenBank sequence file type
    short Fasta   = 2;                  // Fasta format file type
    
    String sequence = "";               // Initialize the sequence
    
    try
    {
      DataInputStream in_file = new DataInputStream 
          ( new FileInputStream ( filename ) );
    
      /* Read in the entire file. */
      boolean end_of_file = false;      // End of file flag
      boolean in_data = false;          // Flag to mark within sequence
      String line;                      // Current text line
      int line_pos = 1;                 // Line position in file
      short file_type = Unknown;        // Unknown file type

      while ( end_of_file == false )
      { 
        try
        {
          line = in_file.readLine ();
          
          if ( line == null )
            end_of_file = true;
          else
          {
            // System.out.println ( line );
            
            // Determine the file type.
            if ( line_pos == 1 )
            {
              if ( line.startsWith ( ">" ) == true )
              {
                System.out.println ( "Fasta file type '" + line + "'" );
                file_type = Fasta;
              }
              else
              {
                if ( line.startsWith ( "LOCUS" ) == true )
                {
                  System.out.println ( "GenBank file type " + line );
                  file_type = GenBank;
                  
                  // Find the GCG ".." marker of start of data
                  while ( line.endsWith ( ".." ) == false )
                  {
                    line = in_file.readLine ();
                  }  /* while */
                  
                  if ( line.endsWith ( ".." ) == true )
                  {
                    System.out.println ( "GCG .. line found '" + line + "'" );
                    line = in_file.readLine ();
                    in_data = true;
                  }  /* if */
                }  /* if */
              }  /* else */
            }  /* if */
            else
            {
              // Check for GenBank sequence seperator.
              if ( line.startsWith ( "//" ) == true )
              {
                in_data = false;
                System.out.println ( "GenBank file sequence seperator found: '" + line + "'" );
              }  /* if */
              
              // Check for multiple Fasta type files.
              if ( line.startsWith ( ">" ) == true )
              {
                in_data = false;
                System.out.println ( "Multiple Fasta sequences found: '" + line + "'" );
              }  /* if */
            }  /* else */
            
            // Check for start of Fasta sequence data.
            if ( ( line_pos == 2 ) && ( file_type == Fasta ) )
              in_data = true;
              
            if ( in_data == true )
            {
              /* Remove non-characters from line. */
              int i = line.length () - 1;
              while ( i >= 0 )
              {
                char c = line.charAt ( i );
                if ( ( ( c >= 'a' ) && ( c <= 'z' ) ) ||
                     ( ( c >= 'A' ) && ( c <= 'Z' ) ) )
                {
                }
                else
                  line = line.replace ( c, ' ' );
                  
                i--;
              }  /* while */
              
              line = line.trim ();
              StringTokenizer tokenizer = new StringTokenizer ( line );
              
              int token_count = tokenizer.countTokens ();
              int token_index = 0;

if ( token_count > 0 )           
System.out.println ( "tokens " + token_count + " " + line );
              
              while ( token_index < token_count )
              {
                String token = tokenizer.nextToken ();
                
                // Check for beginning line numer.
                if ( token_index == 0 )
                {
                  if ( ( token.charAt ( 0 ) < '0' ) ||
                       ( token.charAt ( 0 ) > '9' ) )
                     
                    sequence = sequence.concat ( token );
                }
                else
                  sequence = sequence.concat ( token );
                  
                // sequence = sequence.concat ( line.trim () );
                token_index++;
              }  /* while */
            }  /* if */
            
            line_pos++;         // increment the line position
          }  /* else */
        }
        catch ( EOFException e1 )
        {
          System.out.println ( "End of file reached." + e1 );
          end_of_file = true;
        }  /* catch */
        catch ( IOException e2 )
        {
          System.out.println ( "IOException on input file." + e2 );
          return ( sequence );
        }  /* catch */
      }  /* while */ 
      
      // Close the input file.
      try
      {
        in_file.close ();
      }
      catch ( IOException e4 )
      {
        System.out.println ( "IOException on sequence file " + e4 );
        return ( sequence );
      }  /* catch */ 
    }
    catch ( FileNotFoundException e3 )
    {
      System.out.println ( "File not found '" + filename + "'" + e3 );
      return ( sequence );
    }  /* catch */
    
    return ( sequence );
  }  /* ReadSeq */

  /************************************************************************/
  public static void main ( String [] args )
  {
    String DNA_sequence;           /* DNA sequence read in */
    
    
    File currDir = new File ( "." );
    String [] fileList = currDir.list ();
//    ListNames ( fileList );
    System.out.println ( "----------------------------------------------" );
    
    // Get the list of file names for the specified directory
    GetFileNames ( "..", fileList );
    
    // List the names in fileList
//    ListNames ( fileList );

    // Read in a test file. 
    DNA_sequence = ReadSeq ( "HUMCERP.FSA" );

    System.out.println ( "----------------------------------------------" );

    DNA_sequence = ReadSeq ( "HUMCEPA.SEQ" );

    System.out.println ( "----------------------------------------------" );
    System.out.println ( "DNA sequence read in by ReadSeq 'HUMCEPA.SEQ'" );
    System.out.println ( DNA_sequence );

    // Make a subdirectory "testDir".
//    File newDir = new File ( "testDir" );
//    newDir.mkdir ();

  }  /* main */

}  /* class JavaIO */


