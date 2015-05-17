import java.io.*;
import java.awt.*;
import java.util.*;
import Contig.*;
import Histogram.*;
import ParseName.*;

/******************************************************************************/
public class LowQuality extends Frame
{

/******************************************************************************/

static private Frame aceFrame;			// Frame for this object

static private String contigName;		// Contig name to process

static private String fileName;			// Current file name

static private int minQuality = 40;		// Phrap minimum quality value

static private int minSeqLength = 3000;		// Minimum sequence length to process

static private String sequence;			// DNA sequence

static private byte quality [];			// DNA sequence quality array

static private TextArea textarea;		// Text area for Frame

static private List selectedContig;		

// protected Frame contigFrame;

protected Contig contig;

// protected Contigs contigs;


/******************************************************************************/
public LowQuality ()
{
}  /* constructor LowQuality */


/******************************************************************************/
public void setFileName ( String filename )
{
  fileName = filename;
}  /* method setFileName */


/******************************************************************************/
public static void init ( )
{
  Font font = new Font ( "Courier", Font.PLAIN, 12 );
  textarea = new TextArea ( 24, 80 );
  // textarea.setFont = ( new Font ( "Courier", Font.PLAIN, 12 ) );
  textarea.setFont ( font );

  aceFrame.add ( "Center", textarea );
  // aceFrame.pack ();
}  /* method init */


/******************************************************************************/
public static void fileControl ( )
{
  MenuBar controlMenuBar = new MenuBar ();
  Menu fileMenu = new Menu ( "File" );
  fileMenu.add ( new MenuItem ( "Open" ) );
  fileMenu.addSeparator ();
  fileMenu.add ( new MenuItem ( "Quit" ) );
  controlMenuBar.add ( fileMenu );

  Menu showMenu = new Menu ( "Show" );
  showMenu.add ( new MenuItem ( "Forward Strand" ) );
  showMenu.add ( new MenuItem ( "Reverse Strand" ) );
  showMenu.add ( new MenuItem ( "Both Strands" ) );
  controlMenuBar.add ( showMenu );

  aceFrame.setMenuBar ( controlMenuBar );

  selectedContig = new List ();
}  /* method fileControl */


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
private void openFile ( )
{
  FileDialog dialog = new FileDialog ( aceFrame, "Select a .ace file", FileDialog.LOAD );
  dialog.setDirectory ( "." );
  dialog.setFile ( "*.ace" );
  dialog.show ();

  fileName = dialog.getFile ();

  if ( fileName != null )
    System.out.println ( "openFile: file name selected is '" + fileName + "'" );
}  /* method openFile */


/******************************************************************************/
private String readSequence ( DataInputStream in_file )
{
  String line;
  boolean end_of_data = false;
  String seq = new String ();

  while ( end_of_data == false )
  {
    try 
    {
      line = in_file.readLine ();

      if ( line == null )
      {
        end_of_data = true;
      }  /* if */
      else
      {
        line.trim ();			// Remove blanks

        if ( line.length () == 0 )
        {
          end_of_data = true;
        }  /* if */
        else
        {
          seq = seq.concat ( line );
        }  /* else */
      }  /* else */
    }  /* try */
    catch ( IOException e1 )
    {
      System.out.println ( "readSequence: IOException on input file " + e1 );
      end_of_data = true;
    }  /* catch */
  }  /* while */

// System.out.println ( "seq = '" + seq + "'" );

  return seq;
}  /* method readSequence */


/******************************************************************************/
/* This method reads in the BaseQuality array following a sequence. */
/* Returns true if end of in_file reached. */
private boolean readQuality ( DataInputStream in_file, int length )
{
  boolean found_start = false;		// Found BaseQuality line
  int index;				// index 
  int i;
  String line;				// Text string from in_file


  // Range check the sequence length.
  if ( length < 1 )  return ( false );

  // Allocate the quality byte array.
  quality = new byte [ length ];

  // Search for the beginning of the quality array.
  index = 1;		// Count the number of lines read from in_file
  try
  {
    line = in_file.readLine ();

    while ( ( line != null ) && ( found_start == false ) && ( index < 10 ) )
    {
      // Search for the BaseQuality line.
      if ( line.startsWith ( "BaseQuality" ) == true )  found_start = true;
      else
        line = in_file.readLine ();

      index++;
    }  /* while */
  }  /* try */
  catch ( EOFException e1 )
  {
    System.out.println ( "End of file reached: " + e1 );
    return ( true );
  }  /* catch */
  catch ( IOException e2 )
  {
    System.out.println ( "IOException: " + e2 );
    return ( true );
  }  /* catch */

  // Check for missing BaseQuality section. */
  if ( found_start == false )  return ( false );

  // Read in the quality numbers for the length of the sequence.
  try
  {
    // Get the first line of quality numbers.
    line = in_file.readLine ();

    // Parse out the integers with StringTokenizer.
    StringTokenizer t = new StringTokenizer ( line, " " );

    for ( index = 0; index < length; index++ )
    {
      while ( t.hasMoreTokens () == false )
      {
        // Get the next line of quality numbers.
        line = in_file.readLine ();
        t = new StringTokenizer ( line, " " );
      }

      // No quality values for alignment '*' positions.
      while ( sequence.charAt ( index ) == '*' )
      {
        quality [ index ] = 0;
        index++;
      }  /* while */
      
      i = getInteger ( t.nextToken () );
      quality [ index ] = (byte) i;
    }  /* for */
  }  /* try */
  catch ( EOFException e3 )
  {
    System.out.println ( "End of file reached: " + e3 );
    return ( true );
  }  /* catch */
  catch ( IOException e4 )
  {
    System.out.println ( "IOException: " + e4 );
    return ( true );
  }  /* catch */

  return ( false );
}  /* method readQuality */


/******************************************************************************/
public void printQuality ( )
{
  int  index;						// Array index
  int  position = 1;					// Position in sequence


  // Print out the index, sequence character, and quality for each DNA base.
  for ( index = 0; index < sequence.length (); index++ )
  {
    System.out.println ( position + " " + sequence.charAt ( index ) + "  " + 
        quality [ index ] );

    // Don't advance the position index for gap '*' positions in the sequence.
    if ( sequence.charAt ( index ) != '*' )  position++;
  }  /* for */
}  /* method printQuality */


/******************************************************************************/
public void printLowQuality ( )
{
  int  index;						// Array index
  int  position = 1;					// Position in sequence


  System.out.println ( );
  System.out.println ( "Bases with quality below Phrap " + minQuality );

  // Print out the index, sequence character, and quality for each DNA base.
  for ( index = 0; index < sequence.length (); index++ )
  {
    // Print out the quality values less than minQuality.
    if ( ( quality [ index ] < minQuality ) && ( sequence.charAt (index ) != '*' ) )
      System.out.println ( position + " " + sequence.charAt ( index ) + "  " + 
          quality [ index ] );

    // Don't advance the position index for gap '*' positions in the sequence.
    if ( sequence.charAt ( index ) != '*' )  position++;
  }  /* for */

}  /* method printLowQuality */


/******************************************************************************/
/* This method computes a score for bases below Phrap minQuality over a window. */
private int evalQualityRange ( int start, int length )
{
  int  score = 0;						// score for window

  // Range check start.
  if ( ( start < 0 ) || ( start >= sequence.length () ) )  return ( score );

  // Traverse the selected sequence range.
  for ( int index = start; ( index < start + length ) && ( index < sequence.length () ); 
      index++ )

    // Score higher for values below Phrap minQuality.
    if ( ( quality [ index ] < minQuality ) && ( sequence.charAt ( index ) != '*' ) )

      score += minQuality - quality [ index ];

  return ( score );
}  /* method evalQualityRange */


/******************************************************************************/
/* This method counts the number of bases below Phrap minQuality over a window. */
private int countLowSites ( int start, int length )
{
  int  sites = 0;						// sites in window

  // Range check start.
  if ( ( start < 0 ) || ( start >= sequence.length () ) )  return ( sites );

  // Traverse the selected sequence range.
  for ( int index = start; ( index < start + length ) && ( index < sequence.length () ); 
      index++ )

    // Score higher for values below Phrap minQuality.
    if ( ( quality [ index ] < minQuality ) && ( sequence.charAt ( index ) != '*' ) )

      sites++;

  return ( sites );
}  /* method countLowSites */


/******************************************************************************/
/* This method finds windows of low quality Phrap consensus values. */
private void findLowAreas ( )
{
  int  position = 1;					// Position in sequence


  System.out.println ( );
  System.out.println ( );
  System.out.println ( "Low Quality Regions in Phrap consensus" );
  System.out.println ( );
  System.out.println ( "Start\tLow bases\tScore" );

  // Traverse the sequence looking for low quality regions.
  for ( int index = 0; index < sequence.length (); index++ )
  {
    /* Check for a low quality consensus base. */
    if ( ( quality [ index ] < minQuality ) && ( sequence.charAt ( index ) != '*' ) )
    {
      System.out.println ( position + "\t" + countLowSites ( index, 400 ) + "\t" +
          evalQualityRange ( index, 400 ) );

      // Advance the sliding window by 50 bases.
      for ( int i = 1; i < 50; i++ )
      {
        // Check if at the end of the sequence.
        if ( index < sequence.length () )

          // Advance position if not a gap.
          if ( sequence.charAt ( index ) != '*' )  position++;

        index++;
      }  /* for */
    }  /* if */

    // Check if at the end of the sequence.
    if ( index < sequence.length () )

      // Advance position if not a gap.
      if ( sequence.charAt ( index ) != '*' )  position++;
  }  /* for */
}  /* method findLowAreas */


/******************************************************************************/
/* This method finds windows of low quality Phrap consensus values. */
private void findLowRanges ( )
{
  int  position = 1;					// Position in sequence
  int  range_size  = 100;
  int  range_start = 1;
  int  range_end   = range_size;

  // Set up histogram for 100 base pair ranges.
  Histogram histoRange = new Histogram ( "Range", 10, minQuality );

  System.out.println ( );
  System.out.println ( );
  System.out.println ( "Low Quality Range Analysis for Phrap consensus" );
  System.out.println ( );
  System.out.println ( "Range" );

  // Traverse the sequence looking for low quality regions.
  for ( int index = 0; index < sequence.length (); index++ )
  {
    /* Check for a low quality consensus base. */
    if ( ( quality [ index ] < minQuality ) && ( sequence.charAt ( index ) != '*' ) )

      histoRange.addValue ( quality [ index ] );

    // Check for end of current range.
    if ( position == range_end )
    {
      System.out.print ( range_start + "-" + range_end + ": \t" );
      histoRange.shortSummary ();
      histoRange.reset ( "Range", 10, minQuality );

      range_start = range_end + 1;
      range_end += range_size;
      if ( range_end >= sequence.length () )  range_end = sequence.length () - 1;
    }  /* if */

    // Check if at the end of the sequence.
    if ( index < sequence.length () )

      // Advance position if not a gap.
      if ( sequence.charAt ( index ) != '*' )  position++;
  }  /* for */
}  /* method findLowRanges */


/******************************************************************************/
/* This method finds the start of a Contig if one was specified. */
/* This method return the current line that was read. */
private String findContig ( DataInputStream in_file )
{
  String line = "";				// text line from in_file

  // Check if no contig name was specified.
  if ( contigName.length () == 0 )  return ( "" );

  // Check if valid contig name was specified.
  if ( contigName.startsWith ( "Contig" ) == false )  return ( "" );

  // Search for the specified contig name.
  try
  {
    while ( line != null )
    {
      line = in_file.readLine ();

      if ( line == null )  return ( line );
      if ( line.startsWith ( "DNA " + contigName )  == true )
      {
        return ( line );
      }  /* if */
    }  /* while */
  }  /* try */
  catch ( IOException e1 )
  {
    System.out.println ( "IOException on input file " + e1 );
  }  /* catch */

  return ( line );
}  /* method findContig */


/******************************************************************************/
public void readContigs ( )
{
  try
  {
    DataInputStream in_file = new DataInputStream ( new FileInputStream ( fileName ) );

    /* Read in the contig. */
    boolean end_of_file = false;			// End of file flag 
    String contig_name = "";				// Name of current contig
    String line;
    int line_pos = 1;

    // Check if a specific contig name was specified.
    line = findContig ( in_file );

    while ( end_of_file == false )
    {
      try
      {
        // Check for 'Base_segment' line. 
        if ( line != null )
          while ( line.startsWith ( "Base_segment" ) == true )
            line = in_file.readLine ();

        if ( line == null )  end_of_file = true;
        else
        {
          // Contig or individual sequence.
          if ( line.startsWith ( "DNA" ) == true )
          {
            // Check for Consensus sequence for a contig.
            if ( line.regionMatches ( 4, "Contig", 0, 6 ) == true )
            {
              contig_name = line.substring ( 4 );	// Get the name of the current contig
              System.out.println ( );
              System.out.println ( );
              System.out.println ( contig_name );	// Print the contig name

              // Read in the Consensus sequence.
              sequence = readSequence ( in_file );

              // Read in the quality of the consensus sequence.
              end_of_file = readQuality ( in_file, sequence.length () );

              // Print out the low quality bases (less than Phrap minQuality).
              if ( ( end_of_file != true ) && ( sequence.length () >= minSeqLength ) )
              {
                printLowQuality ( );

                findLowRanges ( );

                // Check if a specific contig name was specified.
                if ( contigName.startsWith ( "Contig" ) == true )  return;
              }  /* if */

            }  /* if */
            else
            {
              // Read in individual sequence.
              String single = readSequence ( in_file );
            }  /* else */
          }  /* if */

          // Quality numbers for last contig.
          if ( line.startsWith ( "BaseQuality" ) == true )
          {
            // System.out.println ( "BaseQuality line: " + line );
          }  /* if */

          // Assembly name list.
          if ( line.startsWith ( "Assembled_from" ) == true )
          {
            // System.out.println ( "AF: " + line );
          }  /* if */

          // Assembly information.
          if ( line.startsWith ( "Sequence" ) == true )
          {
            // System.out.println ( "Sequence line: " + line );
          }  /* if */

        }  /* else */

        line = in_file.readLine ();
      }  /* try */
      catch ( EOFException e1 )
      {
        System.out.println ( "End of file reached: " + e1 );
        end_of_file = true;
      }  /* catch */
    }  /* while */

    /* Close the input file */
    try 
    {
      in_file.close ();
    }
    catch ( IOException e3 )
    {
      System.out.println ( "IOException while closing input file '" + fileName + "'" + e3 );
      return;
    }  /* catch */
  }  /* try */
  catch ( IOException e2 )
  {
    System.out.println ( "IOException on input file " + e2 );
  }  /* catch */
}  /* method readContigs */


/******************************************************************************/
public void readContigs ( String filename )
{
  /* Set the .ace file name. */
  fileName = filename;

  /* Read in the contig. */
  readContigs ();
}  /* method readContigs */


/******************************************************************************/
public boolean action ( Event evt, Object arg )
{
  if ( evt.target instanceof MenuItem )
  {
    if ( arg.equals ( "Quit" ) )
    {
      aceFrame.hide ();
      // System.out.println ( "action method Quit MenuItem selected." );
      System.exit ( 0 );
      return true;
    }  /* if */

    if ( arg.equals ( "Open" ) )
    {
      openFile ();			// Select a .ace file
      readContigs ();			// Read in the contigs
      return true;
    }  /* if */

    if ( arg.equals ( "Forward Strand" ) )
    {
      // reference currently selected contig
//      textarea = getForward ();
      aceFrame.show ();
      return true;
    }  /* if */

    if ( arg.equals ( "Reverse Strand" ) )
    {
      return true;
    }  /* if */

    if ( arg.equals ( "Both Strands" ) )
    {
      return true;
    }  /* if */
  }  /* if */
  else return false;

  return true;
}  /* method action */


/******************************************************************************/
public boolean handleEvent ( Event evt )
{
  if ( evt.id == Event.ACTION_EVENT )
  {
    if ( evt.target == selectedContig )
    {
      String contigName = (String) evt.arg;

//      int contig_index = contigs.getIndex ( contigName );

//      Contig = contigs.getContig ( contigIndex );

//      textarea = contig.getAligment ();

      aceFrame.show ();
      return true;
    }  /* if */
  }  /* if */

  return super.handleEvent ( evt );
}  /* method handleEvent */


/******************************************************************************/
public static void main ( String [] args )
{
  // Initialize main variables.
  contigName = "";			// default Contig name - none

  fileName = "test2.ace";		// default .ace file name


  /* Check for parameters. */
  if ( args.length == 0 )
  {
    aceFrame = new LowQuality ();
    aceFrame.resize ( 300, 300 );
    aceFrame.setTitle ( ".ace Read Frame Title" );

    init ();
    fileControl ();

    aceFrame.show ();
  }
  else
  {
    // The .ace file name is the first parameter.
    fileName = args [ 0 ];
    LowQuality readContig = new LowQuality ();

    // Check for Contig# second parameter. 
    if ( args.length >= 2 )
      contigName = args [ 1 ];

    // Check for minimum Phrap quality number as third parameter.
    if ( args.length >= 3 )
      minQuality = readContig.getInteger ( args [ 2 ] );

System.out.println ( "Parameters: file: '" + fileName + "', contig '" + contigName +
    "', minQuality = " + minQuality );

    // Read in the .ace contigs.
    readContig.readContigs ();
  }  /* else */

}  /* method main */

}  /* class LowQuality */
