
import java.io.*;
import OutputTools;

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
public class Sequence extends InputTools
{

/******************************************************************************/

private StringBuffer description = new StringBuffer ( 140 );	// sequence description

private StringBuffer header_line = new StringBuffer ( 140 );	// header line

private boolean iub_bases = false;				// IUB base found flag

private StringBuffer sequenceName = new StringBuffer ( 140 );	// sequence name

private StringBuffer sequence = new StringBuffer ( 3000 );	// sequence

private int sequenceType = 0;					// sequence type

private boolean trimFlag = false;				// Sequence triming flag


/******************************************************************************/

// Public sequence types.
public final static int DNA  = 1;
public final static int RNA  = 2;
public final static int AA   = 3;
public final static int EST  = 4;
public final static int SAGE = 5;
public final static int mRNA = 6;

public final static String sequenceTypeNames [] = 
    { "DNA", "RNA", "AA", "EST", "SAGE", "mRNA" };


/******************************************************************************/
public Sequence ()
{
}  // constructor Sequence


/******************************************************************************/
public Sequence ( String file_name )
{
  // Capture the command line parameters.
  fileName = file_name;
}  // constructor Sequence


/******************************************************************************/
public StringBuffer getHeaderLine ()
{
  return header_line;
}  // method getHeaderLine


/******************************************************************************/
public String getSequenceDescription ()
{
  return description.toString ();
}  // method getSequenceDescription


/******************************************************************************/
public String getSequence ()
{
  return sequence.toString ();
}  // method getSequence


/******************************************************************************/
public int getSequenceLength ()
{
  return sequence.length ();
}  // method getSequenceLength


/******************************************************************************/
public String getSequenceName ()
{
  return sequenceName.toString ();
}  // method getSequenceName


/******************************************************************************/
public int getSequenceType ()
{
  return sequenceType;
}  // method getSequenceType


/******************************************************************************/
public boolean getTrimFlag ()
{
  return trimFlag;
}  // method getTrimFlag


/******************************************************************************/
public void setSequence ( String seq )
{
  sequence.setLength ( 0 );
  sequence.append ( seq );
}  // method setSequence


/******************************************************************************/
public void setSequenceType ( int sequence_type )
{
  if ( ( sequence_type == DNA ) || ( sequence_type == RNA ) ||
       ( sequence_type == AA  ) || ( sequence_type == SAGE ) ||
       ( sequence_type == EST ) || ( sequence_type == mRNA ) )
    sequenceType = sequence_type;
  else
    System.out.println ( "Sequence.setSequenceType: invalid sequence type: " +
        sequence_type );
}  // method setSequenceType


/******************************************************************************/
public void setTrim ( boolean new_value )
{
  trimFlag = new_value;
}  // method setTrim


/******************************************************************************/
// This method checks if a character is a valid amino acid.
public static boolean isAA ( char base )
{
  // Check for amino acid.
  if ( ( base == 'A' ) || ( base == 'a' ) ||
       ( base == 'B' ) || ( base == 'b' ) ||
       ( base == 'C' ) || ( base == 'c' ) ||
       ( base == 'D' ) || ( base == 'd' ) ||
       ( base == 'E' ) || ( base == 'e' ) ||
       ( base == 'F' ) || ( base == 'f' ) ||
       ( base == 'G' ) || ( base == 'g' ) ||
       ( base == 'H' ) || ( base == 'h' ) ||
       ( base == 'I' ) || ( base == 'i' ) ||
       ( base == 'K' ) || ( base == 'k' ) ||
       ( base == 'L' ) || ( base == 'l' ) ||
       ( base == 'V' ) || ( base == 'v' ) ||
       ( base == 'M' ) || ( base == 'm' ) ||
       ( base == 'N' ) || ( base == 'n' ) ||
       ( base == 'P' ) || ( base == 'p' ) ||
       ( base == 'Q' ) || ( base == 'q' ) ||
       ( base == 'R' ) || ( base == 'r' ) ||
       ( base == 'S' ) || ( base == 's' ) ||
       ( base == 'T' ) || ( base == 't' ) ||
       ( base == 'V' ) || ( base == 'v' ) ||
       ( base == 'W' ) || ( base == 'w' ) ||
       ( base == 'X' ) || ( base == 'x' ) ||
       ( base == 'Y' ) || ( base == 'y' ) )
  {
    return true;
  }
  else
  {
//    System.out.println ( "Unknown character in protein sequence: '" + base + "', " +
//        sequenceName.toString () + ", " + line.toString () );

    return false;
  }  // else
}  // method isAA


/******************************************************************************/
// This method checks if a character is a valid amino acid.
public static boolean isAAAlign ( char base )
{
  // Check for amino acid.
  if ( ( base == 'A' ) || ( base == 'a' ) ||
       ( base == 'B' ) || ( base == 'b' ) ||
       ( base == 'C' ) || ( base == 'c' ) ||
       ( base == 'D' ) || ( base == 'd' ) ||
       ( base == 'E' ) || ( base == 'e' ) ||
       ( base == 'F' ) || ( base == 'f' ) ||
       ( base == 'G' ) || ( base == 'g' ) ||
       ( base == 'H' ) || ( base == 'h' ) ||
       ( base == 'I' ) || ( base == 'i' ) ||
       ( base == 'K' ) || ( base == 'k' ) ||
       ( base == 'L' ) || ( base == 'l' ) ||
       ( base == 'V' ) || ( base == 'v' ) ||
       ( base == 'M' ) || ( base == 'm' ) ||
       ( base == 'N' ) || ( base == 'n' ) ||
       ( base == 'P' ) || ( base == 'p' ) ||
       ( base == 'Q' ) || ( base == 'q' ) ||
       ( base == 'R' ) || ( base == 'r' ) ||
       ( base == 'S' ) || ( base == 's' ) ||
       ( base == 'T' ) || ( base == 't' ) ||
       ( base == 'V' ) || ( base == 'v' ) ||
       ( base == 'W' ) || ( base == 'w' ) ||
       ( base == 'X' ) || ( base == 'x' ) ||
       ( base == 'Y' ) || ( base == 'y' ) ||
       ( base == '-' ) || ( base == '*' ) )
  {
    return true;
  }
  else
  {
//    System.out.println ( "Unknown character in protein sequence: '" + base + "', " +
//        sequenceName.toString () + ", " + line.toString () );

    return false;
  }  // else
}  // method isAAAlign


/******************************************************************************/
// This method checks if a character is a valid DNA base or DNA IUB code.
public boolean isDNA ( char base )
{
  // Check for IUB characters.
  if ( ( base == 'B' ) || ( base == 'b' ) ||
       ( base == 'D' ) || ( base == 'd' ) ||
       ( base == 'H' ) || ( base == 'h' ) ||
       ( base == 'V' ) || ( base == 'v' ) ||
       ( base == 'R' ) || ( base == 'r' ) ||
       ( base == 'Y' ) || ( base == 'y' ) ||
       ( base == 'K' ) || ( base == 'k' ) ||
       ( base == 'M' ) || ( base == 'm' ) ||
       ( base == 'S' ) || ( base == 's' ) ||
       ( base == 'W' ) || ( base == 'w' ) ||
       ( base == 'N' ) || ( base == 'n' ) ||
       ( base == 'X' ) || ( base == 'x' ) )
  {
    iub_bases = true;
    return true;
  }
  else
    // Check for the DNA bases.
    if ( ( base == 'A' ) || ( base == 'a' ) ||
         ( base == 'C' ) || ( base == 'c' ) ||
         ( base == 'G' ) || ( base == 'g' ) ||
         ( base == 'T' ) || ( base == 't' ) )

       return true;

    else
    {
      // Check for an unexpected letter.
      if ( ( ( base >= 'A' ) && ( base <= 'Z' ) ) ||
           ( ( base >= 'a' ) && ( base <= 'z' ) ) )
        System.out.println ( "Unknown character in nucleotide sequence: '" + base + "', " +
            sequenceName.toString () + ", " + line.toString () );

      return false;
    }  // else
}  // method isDNA


/******************************************************************************/
private StringBuffer readSeq ()
{
  boolean end_of_data = false;			// End of sequence flag
  sequence.setLength ( 0 );

  // Read in the sequence.
  while ( end_of_data == false )
  {
    try 
    {
      // Get the next line of the file.
      line.setLength ( 0 );
      line.append ( input_file.readLine () );

      // Check for end of file.
      if ( line == null )  end_of_file = true;
      else
        if ( line.toString ().equals ( "null" ) )   end_of_file = true;

      if ( line.length () >= 1 )
        if ( line.charAt ( 0 ) == '>' )  end_of_data = true;

      if ( ( end_of_data == true ) || ( end_of_file == true ) )
        return sequence;
      else
      {
        // Ignore valid characters.
        for ( int i = 0; i < line.length (); i++ )
        {
          if ( sequenceType != AA )
          {
            if ( isDNA ( line.charAt ( i ) ) == true )
              sequence.append ( line.substring ( i, i+1 ) );
          }  // if
          else
          {
            if ( isAA ( line.charAt ( i ) ) == true )
              sequence.append ( line.substring ( i, i+1 ) );
          }  // else
        }  // for

        if ( line.length () == 0 )
        {
          end_of_data = true;
          return sequence;
        }  /* if */
      }  /* else */
    }  /* try */
    catch ( IOException e1 )
    {
        System.out.println ( "readSeq: IOException on input file " + e1 );
        end_of_data = true;
        end_of_file = true;
    }  /* catch */
  }  /* while */

  return sequence;
}  // method readSeq 


/******************************************************************************/
public StringBuffer readSeq( InputTools file )
{
  boolean end_of_data = false;			// End of sequence flag
  sequence.setLength ( 0 );

  // Read in the sequence.
  while ( end_of_data == false )
  {
   // try 
   // {
      // Get the next line of the file.
      line.setLength ( 0 );
      line.append ( file.getLine () );

      // Check for end of file.
      if ( line == null )  end_of_file = true;
      else
        if ( line.toString ().equals ( "null" ) )   end_of_file = true;

      if ( line.length () >= 1 )
        if ( line.charAt ( 0 ) == '>' )  end_of_data = true;

      if ( ( end_of_data == true ) || ( end_of_file == true ) )
        return sequence;
      else
      {
        // Ignore valid characters.
        for ( int i = 0; i < line.length (); i++ )
        {
          if ( sequenceType != AA )
          {
            if ( isDNA ( line.charAt ( i ) ) == true )
              sequence.append ( line.substring ( i, i+1 ) );
          }  // if
          else
          {
            if ( isAA ( line.charAt ( i ) ) == true )
              sequence.append ( line.substring ( i, i+1 ) );
          }  // else
        }  // for

        if ( line.length () == 0 )
        {
          end_of_data = true;
          return sequence;
        }  /* if */
      }  /* else */
  //  }  /* try */
  /*  catch ( IOException e1 )
    {
        System.out.println ( "readSeq: IOException on input file " + e1 );
        end_of_data = true;
        end_of_file = true;
    }   */ /* catch */

  }  /* while */

  return sequence;
}  // method readSeq


/******************************************************************************/
public static void writeFastaSequence ( String seq, OutputTools file )
{
  for ( int index = 0; index < seq.length (); index += 50 )
  {
    if ( index + 50 >= seq.length () )
      file.println ( seq.substring ( index ) );
    else
      file.println ( seq.substring ( index, index + 50 ) );
  }  // for
}  // method writeFastaSequence


/******************************************************************************/
public static void writeFastaSequence ( String name, String seq, OutputTools file )
{
  if ( ( name.length () <= 0 ) || ( seq.length () <= 0 ) ) return;

  file.println ( ">" + name + " .." );

  writeFastaSequence ( seq, file );
}  // method writeFastaSequence


/******************************************************************************/
public void writeFastaSequence ( OutputTools file )
{
  writeFastaSequence ( sequence.toString (), file );
}  // method writeFastaSequence


/******************************************************************************/
// This method reads in a sequence from a text file.
public void readSequence ()
{
  // Initialize.
  sequence.setLength ( 0 );
  header_line.setLength ( 0 );

  iub_bases = false;

  /* Read in the contig. */
  int index;						// index
  int line_pos = 1;					// line position

  if ( end_of_file )  return;

  while ( line.length () <= 0 )
  {
    line.setLength ( 0 );
    try
    {
      line.append ( input_file.readLine () );
    }
    catch ( IOException e )
    {
      System.out.println ( "readSequence: IOException: " + e );
      end_of_file = true;
      return;
    }  // catch
  }  // while

  // Check for the last line.
  if ( ( line == null ) || ( line.equals ( "null" ) == true ) )
  {
    end_of_file = true;
    return;
  }  // if
    else
    {
      // Check for FASTA style sequence file. 
      if ( ( line.length () > 1 ) && ( line.charAt ( 0 ) == '>' ) )
      {
        // Save the FASTA description line.
        description.setLength ( 0 );
        description.append ( line.substring ( 1 ) );
        header_line.append ( line.substring ( 0 ) );

        // Extract the sequence name from the description line.
        index = -1;
        for ( int i = 0; (i < description.length ()) && (index < 0); i++ )
          if ( description.charAt ( i ) == ' ' )  index = i;

        // Trim ".scf" from Myriad sequence names.
        int index2 = description.toString ().indexOf ( ".scf" );
        if ( ( index2 < index ) && ( index2 > 0 ) )  index = index2;

        // Trim ".bin" from Clemson sequence names.
        index2 = description.toString ().indexOf ( ".bin" );
        if ( ( index2 < index ) && ( index2 > 0 ) )  index = index2;

        sequenceName.setLength ( 0 );
        if ( ( index >= 0 ) && ( index < description.length () ) )
          sequenceName.append ( description.substring ( 0, index ) );
        else
          sequenceName.append ( description.toString () );

        // Read in the sequence. 
        sequence = readSeq ();
      }  // if
    }  /* else */
}  /* method readSequence */


/******************************************************************************/
public void readSequence ( String filename )
{
  /* Set the .ace file name. */
  fileName = filename;

  /* Read in the contig. */
  readSequence ();
}  /* method readSequence */


/******************************************************************************/
public StringBuffer nextSequence ()
{
  // Read in the next DNA sequence.
  readSequence ();

  return sequence;
}  // method nextSequence


/******************************************************************************/
// This method replace X's with N's and removes trailing N's.
public static String clipSequence ( String seq )
{
  StringBuffer clip = new StringBuffer ( seq );

  // Replace X's with N's.
  for ( int i = 0; i < clip.length (); i++ )
  {
    if ( clip.charAt ( i ) == 'X' )  clip.setCharAt ( i, 'N' );
    if ( clip.charAt ( i ) == 'x' )  clip.setCharAt ( i, 'n' );
  }  // for

  // Clip trailing N's.
  while ( ( clip.length () > 0 ) &&
          ( ( clip.charAt ( clip.length () -1 ) == 'N' ) ||
            ( clip.charAt ( clip.length () -1 ) == 'n' ) ) )
  {
    clip.deleteCharAt ( clip.length () -1 );
  }  // while 

  return clip.toString ();
}  // method clipSequence


/******************************************************************************/
public String trimSequence ( String seq, char base )
{
  String trim = seq;

  // Check for a null sequence.
  if ( seq.length () <= 0 )  return "";

  // Trim the base from the beginning of the sequence.
  int index = seq.indexOf ( base );
  int first_base = index;

  // Advance along the poly base stretch.
  if ( ( index >= 0 ) && ( seq.length () >= 4 ) )
    while ( ( seq.charAt ( index + 1 ) == base ) && ( index < seq.length () - 2 ) )
      index++;
  // Check for long vector sequence.
  if ( index - first_base + 1 >= 200 )  return "";

  // Check if near the end of the sequence.
  if ( index >= seq.length () - 2 )
  {
    trim = seq.substring ( 0, first_base );
    return trim;
  } // if 
  
  // Clip the beginning of the sequence.
  if ( index >= 0 )  trim = seq.substring ( index + 1, seq.length () );

  // Trim the end of the sequence.
  index = trim.indexOf ( base );

  // Clip the end of the sequence.
  if ( index >= 0 )  trim = trim.substring ( 0, index );

  // Clip low quality regions.
  index = trim.indexOf ( "NNNNNNNNNNNNNNNN" );
  if ( index >= 0 )  trim = trim.substring ( 0, index );
  index = trim.indexOf ( "CCCCCCCCCCCCCCCCCCCC" );
  if ( index >= 0 )  trim = trim.substring ( 0, index );
  index = trim.indexOf ( "TTTTTTTTTTTTTTTTTTTT" );
  if ( index >= 0 )  trim = trim.substring ( 0, index );
  
  // Check for nothing left of value
  if ( trim.length () < 50 )  return "";

  return trim;
}  // method trimSequence


/******************************************************************************/
public String getTrimmedSequence ()
{
  // Trim 'X' and 'x' bases from the sequence.
  return trimSequence ( trimSequence ( sequence.toString (), 'x' ), 'X' );
}  // method getTrimmedSequence


/******************************************************************************/
/* class written by Ernst Gassman */

public static String Complement4BaseDna( String Dna )
/*------------------------------------------------------------------------------------
 Returns the complemented (double strand) DNA of given DNA
*/
{
 String sBases     = "ACGT";

 char []adsBase = {'T','G','C','A'};    //Complement to Bases string
 StringBuffer sbufDna = new StringBuffer();

 sbufDna.setLength(Dna.length());

 for (int i = 0; i < Dna.length(); i++)
 {
  // get base from Dna, then get index in sBases string,
  // which points to complement base in char array adsBase
  sbufDna.setCharAt(i,adsBase[sBases.indexOf(Dna.charAt(i))]);
 }
 return sbufDna.toString();

}  // method Complement4BaseDna


/******************************************************************************/
// This method complements and reverses a given string of dna in IUB basecodes,
// degenerate 2 and 3 base codes can be handled
public static String ReverseCompDna( String Dna )
{
 // Check for good string
 if( Dna.length() <= 0 ) return null;

 // char array will hold all complements of desired bases and their index in the
 // array of the complement base will be the unicode value of the original base
 // thus allowing access of the complement by using the char of the original as the index
 char[ ] complement = {
 /*		1		2		3		4		5		6		7		8		9		10	*/
 ' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //20
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //30
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //40
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //50
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //60
 		' ',	' ',	' ',	' ',												 //64
 /*		A		B		C		D		E		F		G		H		I		J		K	*/
 		'T',	'V',	'G',	'H',	' ',	' ',	'C',	'D',	' ',	' ',	'M',

 /*		L		M		N		O		P		Q		R		S		T		U		V	*/
 		' ',	'K',	'N',	' ',	' ',	' ',	'Y',	'S',	'A',	'A',	'B',

 /*		W		X		Y		Z															*/
 		'W',	'N',	'R',	' ',	' ',	' ',	' ',	' ',	' ',	' ',

 /*		a		b		c		d		e		f		g		h		i		j		k	*/
		't',	'v',	'g',	'h',	' ',	' ',	'c',	'd',	' ',	' ',	'm',

 /*		l		m		n		o		p		q		r		s		t		u		v	*/
 		' ',	'k',	'n',	' ',	' ',	' ',	'y',	's',	'a',	'a',	'b',

 /*		w		x		y		z															*/
 		'w',	'n',	'r',	' ' };

  StringBuffer reverse = new StringBuffer( Dna.length() );
  reverse.setLength( Dna.length() );
  int index = 0;

  for( index = 0; index < Dna.length(); index++ )
  {
   reverse.setCharAt( reverse.length() - index - 1, complement[ Dna.charAt( index ) ] );
  } // end: for

  return reverse.toString();

 } // end: method ReverseCompDna


/******************************************************************************/
// This method complements and reverses a given string of dna in IUB basecodes,
// degenerate 2 and 3 base codes can be handled
public static String ComplementDna( String Dna )
{
 // Check for good string
 if( Dna.length() <= 0 ) return null;

 // char array will hold all complements of desired bases and their index in the
 // array of the complement base will be the unicode value of the original base
 // thus allowing access of the complement by using the char of the original as the index
 char[ ] complement = {
 /*		1		2		3		4		5		6		7		8		9		10	*/
 ' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //20
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //30
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //40
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //50
 		' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ',	' ', //60
 		' ',	' ',	' ',	' ',												 //64
 /*		A		B		C		D		E		F		G		H		I		J		K	*/
 		'T',	'V',	'G',	'H',	' ',	' ',	'C',	'D',	' ',	' ',	'M',

 /*		L		M		N		O		P		Q		R		S		T		U		V	*/
 		' ',	'K',	'N',	' ',	' ',	' ',	'Y',	'S',	'A',	'A',	'B',

 /*		W		X		Y		Z															*/
 		'W',	'N',	'R',	' ',	' ',	' ',	' ',	' ',	' ',	' ',

 /*		a		b		c		d		e		f		g		h		i		j		k	*/
		't',	'v',	'g',	'h',	' ',	' ',	'c',	'd',	' ',	' ',	'm',

 /*		l		m		n		o		p		q		r		s		t		u		v	*/
 		' ',	'k',	'n',	' ',	' ',	' ',	'y',	's',	'a',	'a',	'b',

 /*		w		x		y		z															*/
 		'w',	'n',	'r',	' ' };

  StringBuffer comp = new StringBuffer( Dna.length() );
  comp.setLength( Dna.length() );
  int index = 0;

  for( index = 0; index < Dna.length(); index++ )
  {
   comp.setCharAt( index, complement[ Dna.charAt( index ) ] );
  } // end: for

  return comp.toString();

 } // end: method ComplementDna


/******************************************************************************/
// this method validates the given string as containg only DNA bases or degenerate
// IUB code (upper or lower case)

public boolean validateDna( String Dna )
{
 for( int i = 0; i < Dna.length (); i++ )
 {
  if( isDNA( Dna.charAt( i ) ) == false )
  {
   System.out.println( "\tInvalid character in DNA: '" + Dna.charAt( i ) + "'" );
   return false;
  } // end: if
 } // end: for

 return true;

} // end: method validateDna


/******************************************************************************/
public static void showParameters ( )
{
  System.out.println ( "use: java Sequence <seq>" );
  System.out.println ( );
  System.out.println ( "e.g.: java Sequence H109.seq" );
}  // showParameters


/******************************************************************************/
public static void main ( String [] args )
{
  if ( args [ 0 ].length () <= 1 )
  {
    showParameters ();
  }
  else
  {
    // Capture the command line parameters.
    Sequence readSeq = new Sequence ( args [ 0 ] );
    readSeq.openFile ();

    while ( readSeq.isEndOfFile () == false )
    {
      readSeq.nextSequence ();
      System.out.println ( readSeq.getSequenceName () );

      if ( readSeq.getTrimmedSequence ().length () < 100 )
      {
        System.out.println ( "orig: " + readSeq.getSequence () );
        System.out.println ( );
        System.out.println ( "trim: " + readSeq.getTrimmedSequence () );
        System.out.println ( );
      }  // if 
    }  // while

    readSeq.closeFile ();
  }  /* else */
}  /* method main */

}  /* class Sequence */
