
// import BlastHeader;
// import Hit;
// import InputTools;
// import OutputTools;

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

public class BlastParserLow extends Object
{

/******************************************************************************/

private  InputTools  blast_file = new InputTools ();	// Blast input file

private  String  blast_file_name;			// Blast input file name

private  String  line = null; 				// Current line of the file

private  OutputTools  hits_file = new OutputTools ();	// .Hits output file


// Blast file objects.
private  BlastHeader  blast_header = new BlastHeader ();


private  StringBuffer  query_alignment = new StringBuffer ( 4096 );

private  StringBuffer  target_alignment = new StringBuffer ( 4096 );

private  StringBuffer  description = new StringBuffer ( 1024 );	// Description line



private final String large_gap = "-----";		// Gap size to split alignments


/******************************************************************************/
public BlastParserLow ()
{
  initialize ();
}  // constructor BlastParserLow


/******************************************************************************/
private void initialize ()
{
  blast_file_name = null;
  line = null;
}  // method initialize


/******************************************************************************/
  public void setFileName ( String name )
  {
    blast_file_name = name;
  }  // method setFileName


/******************************************************************************/
  private void parseHeader ()
  {
    blast_header.initialize ();

    // Get the first line of the input file.
    line = blast_file.getLine ().toString ();

    // System.out.println ( "parseHeader: Header line: " + line );

    // Extract the program name and version from the first line.
    int index1 = line.indexOf ( ' ' );
    if ( index1 > 0 )
    {
      blast_header.setProgramName ( line.substring ( 0, index1 ) );

      // Check for BLAST in the program name.
      if ( line.substring ( 0, index1 ).indexOf ( "BLAST" ) == -1 )
          System.out.println ( "BLAST program name expected in '"  
              + line.substring ( 0, index1 ).trim () 
              + "' on line: " + line );

      // Search for the start of the version date [Oct-19-2000]
      int index2 = line.indexOf ( '[' );
      if ( index2 > 0 )
      {
          blast_header.setProgramVersion ( line.substring ( index1, index2 ).trim () );
      }  // if
    }  // if

    while ( ( blast_file.isEndOfFile () != true ) &&
            ( line.startsWith ( ">" ) != true ) )
    {
      if ( line.trim ().length () > 0 )
      {
        // Check for the Query name.
        if ( line.startsWith ( "Query=" ) == true )

          blast_header.setQueryName ( line.substring ( 7 ).trim () );

        else if ( line.startsWith ( "Database: " ) == true )

          blast_header.setDatabaseName ( line.substring ( 10 ).trim () );

        else if ( line.charAt ( 0 ) == '>' )

          return;
      }  // if

      line = blast_file.getLine ().toString ();
    }  // while

  }  // method parseHeader


/******************************************************************************/
  private Hit parseAlignment ( Hit hit )
  {
    int index1 = 0;				// line index 1
    int index2 = 0;				// line index 2

    query_alignment.setLength ( 0 );
    target_alignment.setLength ( 0 );

    while ( ( blast_file.isEndOfFile () != true ) &&
            ( blast_file.startsWith ( "BLAST:" ) == false ) &&
            ( blast_file.startsWith ( "TBLAST" ) == false ) &&
            ( blast_file.startsWith ( "Query=" ) == false ) &&
            ( blast_file.startsWith ( "S1:" ) == false ) &&
            ( blast_file.startsWith ( "S2:" ) == false ) )
    {
      // Check for the > line.
      if ( line.startsWith ( ">" ) == true )
      {
        // Check if an alignment was found previously.
        if ( query_alignment.length () > 0 )
        {
          hit.setQueryAlignment ( query_alignment.toString () );
          hit.setTargetAlignment ( target_alignment.toString () );
          return hit;
        }  // if

        hit.initialize ();
        hit.setProgramName ( blast_header.getProgramName () );

        // Read in the description line.
        description.setLength ( 0 );
        while ( ( line.indexOf ( "Length =" ) < 1 ) &&
                ( blast_file.isEndOfFile () != true ) )
        {
          if ( description.length () > 0 )  description.append ( ' ' );
          description.append ( line.trim () );
          line = blast_file.getLine ().toString ();
        }  // while

        hit.setDescription ( description.toString () );

        // Get the target length.
        index1 = line.indexOf ( "Length =" );
        if ( index1 > 0 )
          hit.setTargetLength ( InputTools.getInteger ( line.substring ( index1 + 8 ) ) );
      }  // if

      // Check for the Score = line.
      else if ( line.startsWith ( " Score =" ) == true )
           {
             // Check if an alignment was found previously.
             if ( query_alignment.length () > 0 )
             {
               hit.setQueryAlignment ( query_alignment.toString () );
               hit.setTargetAlignment ( target_alignment.toString () );

               return hit;
             }  // if

             // Use the previous description.
             hit.setDescription ( description.toString () );

             index1 = line.indexOf ( "Score =" );
             index2 = line.indexOf ( "bits" );
             if ( ( index1 > 0 ) && ( index2 > 0 ) )
               hit.setScore ( line.substring ( index1 + 7, index2 ).trim () );

             // Extract the Expect value.
             index1 = line.indexOf ( "Expect" );
             if ( index1 > 0 )
             {
               index1 = line.indexOf ( "=", index1 );
               if ( index1 > 0 )
               {
                 if ( line.length () > index1 + 2 )
                   hit.setExpect ( line.substring ( index1 + 2 ).trim () );
               }  // if
             }  // if
           }  // if

      // Check for an error message.
      else if ( line.startsWith ( "FATAL" ) == true )
      {
        hit.setError ( line );
      }  // else

      // Check for an Identities line.
      else if ( line.startsWith ( " Identities =" ) == true )
           {
             // Get the number of identities.
             hit.setIdentities ( InputTools.getInteger ( line.substring ( 13 ) ) );

             // Get the alignment length.
             int index = line.indexOf ( '/' );
             if ( index > 0 )
               hit.setAlignmentLength ( InputTools.getInteger ( line.substring ( index + 1 ) ) );

             // Get the alignment percentage.
             index = line.indexOf ( "(" );
             if ( index > 0 )
               hit.setPercent ( InputTools.getInteger ( line.substring ( index + 1 ) ) );
           }  // if

      // Check for a Frame line.
      else if ( line.startsWith ( " Frame =" ) == true )
           {
             // Check if two frames are specified.
             int index = line.indexOf ( '/' );
             if ( index == -1 )
             {
               // Set the target frame.
               if ( blast_header.getProgramName ().equals ( "BLASTX" ) == false ) 
                 hit.setTargetFrame ( InputTools.getInteger ( line.substring ( 8 ) ) );
               else
                 hit.setQueryFrame ( InputTools.getInteger ( line.substring ( 8 ) ) );
             }  // if
             else
             {
               // Get the query frame.
               hit.setQueryFrame ( InputTools.getInteger ( line.substring ( 8, index ) ) );
               hit.setTargetFrame ( InputTools.getInteger ( line.substring ( index + 1 ) ) );
             }  // else
           }  // if

      // Check for a Strand line.
      else if ( line.startsWith ( " Strand =" ) == true )
           {
             // Check if two strands are specified.
             int index = line.indexOf ( '/' );
             if ( index == -1 )
             {
               // Check for complement query strand.
               if ( line.indexOf ( "Minus" ) > 0 )

                 hit.setQueryStrand ( '-' );
             }  // if
             else
             {
               // Check for complement query strand.
               if ( line.substring ( 9, index ).indexOf ( "Minus" ) > 0 )

                 hit.setQueryStrand ( '-' );

               // Check for complement query strand.
               if ( line.substring ( index + 1 ).indexOf ( "Minus" ) > 0 )

                 hit.setTargetStrand ( '-' );
             }  // else
           }  // if

      // Check for an alignment segment.
      else if ( line.startsWith ( "Query:" ) == true )
           {
             String query = line;

             // Get the Identities line.
             line = blast_file.getLine ().toString ();

             // Get the Target line.
             line = null;
             line = blast_file.getLine ().toString ();

             // Check for the target line.
             if ( line.startsWith ( "Sbjct:" ) == true )
             {
               // Find the start of the alignment.
               index1 = 6;
               while ( ( index1 < query.length () ) &&
                       ( index1 < line.length () ) &&
                       ( Sequence.isAAAlign ( query.charAt ( index1 ) ) == false ) &&
                       ( Sequence.isAAAlign ( line.charAt ( index1 ) ) == false ) )

                 index1++;

               // Find the end of the alignment.
               index2 = query.length () - 1;
               if ( index2 > line.length () - 1 )  index2 = line.length () - 1;

               while ( ( index2 > 0 ) &&
                       ( Sequence.isAAAlign ( query.charAt ( index2 ) ) == false ) &&
                       ( Sequence.isAAAlign ( line.charAt ( index2 ) ) == false ) )

                 index2--;

               if ( index1 < index2 )
               {
                 target_alignment.append ( line.substring ( index1, index2+1 ) );
                 query_alignment.append ( query.substring ( index1, index2+1 ) );
               }  // if
               else
               {
                 if ( index1 == index2 )
                 {
                   target_alignment.append ( line.charAt ( index1 ) );
                   query_alignment .append ( query.charAt ( index1 ) );
                 }  // if
               }  // else

               // Get the query coordinates.
               int start = InputTools.getInteger ( query.substring ( 6, index1 ) );
               int end   = InputTools.getInteger ( query.substring ( index2+1 ) );

               // System.out.println ( "query strand = " + hit.getQueryStrand () + "[" + start + "," + end + "]" );

               if ( start > 0 ) 
               {
                 if ( hit.getQueryStrand () == '+' )
                 {
                   if ( ( start < hit.getQueryStart () ) ||
                      ( hit.getQueryStart () == 0 ) )

                   hit.setQueryStart ( start );
                 }  // if
                 else
                 {
                   if ( start > hit.getQueryEnd () )

                     hit.setQueryEnd ( start );
                 }  // if
               }  // if

               if ( end > 0 ) 
               {
                 if ( hit.getQueryStrand () == '-' )
                 {
                   if ( ( end < hit.getQueryStart () ) ||
                      ( hit.getQueryStart () == 0 ) )

                   hit.setQueryStart ( end );
                 }  // if
                 else
                 {
                   if ( end > hit.getQueryEnd () )

                     hit.setQueryEnd ( end );
                 }  // if
               }  // if

               // Get the Sbjct coordinates.
               start = InputTools.getInteger ( line.substring ( 6, index1 ) );
               end   = InputTools.getInteger ( line.substring ( index2+1 ) );

               if ( start > 0 ) 
               {
                 if ( hit.getTargetStrand () == '+' )
                 {
                   if ( ( start < hit.getTargetStart () ) ||
                      ( hit.getTargetStart () == 0 ) )

                   hit.setTargetStart ( start );
                 }  // if
                 else
                 {
                   if ( start > hit.getTargetEnd () )

                     hit.setTargetEnd ( start );
                 }  // if
               }  // if

               if ( end > 0 ) 
               {
                 if ( hit.getTargetStrand () == '-' )
                 {
                   if ( ( end < hit.getTargetStart () ) ||
                      ( hit.getTargetStart () == 0 ) )

                   hit.setTargetStart ( end );
                 }  // if
                 else
                 {
                   if ( end > hit.getTargetEnd () )

                     hit.setTargetEnd ( end );
                 }  // if
               }  // if
             }  // if

             query = null;
           }  // if

      line = null;
      line = blast_file.getLine ().toString ();
    }  // while

    // Check if an alignment was found.
    if ( query_alignment.length () > 0 )
    {
      hit.setQueryAlignment ( query_alignment.toString () );
      hit.setTargetAlignment ( target_alignment.toString () );
    }  // if
    else
    {
      hit.initialize ();
      hit.setProgramName ( blast_header.getProgramName () );
    }  // else

    return hit;
  }  // method parseAlignment


/******************************************************************************/
  // This method extracts the first alignment segment from hit.
  private Hit getFirstSegment ( Hit hit, int gap )
  {
    Hit first = new Hit ();

    // Copy the original hit.
    first = hit.copyOf ();

    // Shorten the alignment to the first alignment segment.
    while ( first.getQueryAlignment ().length () > gap )
    {
      first.shortenQuery ();
      first.shortenTarget ();
    }  // while

    // Retotal the hit statistics.
    first.retotal ();

    return first;
  }  // method getFirstSegment


/******************************************************************************/
  // This method advances the hit to the alignment after the first gap.
  private Hit getNextSegment ( Hit hit, int gap )
  {
    // Advance past the first alignment segment & start of gap.
    for ( int i = 0; i < gap + large_gap.length (); i++ )
    {
      hit.advanceQuery ();
      hit.advanceTarget ();
    }  // for

    // Advance past the alignment gap.
    boolean done = true;
    String query_alignment = hit.getQueryAlignment ();
    if ( query_alignment.length () > 0 )
    {
      if ( query_alignment.charAt ( 0 ) == '-' )  done = false;
    }  // if
    while ( done == false )
    {
      hit.advanceQuery ();
      hit.advanceTarget ();

      done = true;
      query_alignment = hit.getQueryAlignment ();
      if ( query_alignment.length () > 0 )
      {
        if ( query_alignment.charAt ( 0 ) == '-' )  done = false;
      }  // if
    }  // while

    // Retotal the hit statistics.
    hit.retotal ();

    return hit;
  }  // method getNextSegment


/******************************************************************************/
  private void addAlignment ( Hit hit, BlastHeader blast_header )
  {
    // Check for the wrong query frame.
    if ( hit.getQueryAlignment ().indexOf ( "*" ) != -1 )  return;

    // Check for low amino acid complexity.
    if ( SeqTools.isLowAAComplexity ( hit.getQueryAlignment (),
             hit.getTargetAlignment () ) == true )
    {
      if ( ( hit.getAlignmentLength () < Alignment.MIN_LONG_AA_ALIGNMENT ) ||
           ( hit.getPercent () < Alignment.HIGH_AA_PERCENT ) )

        return;
    }  // if

    // Trim the alignment ends.
    hit.trimAAEnds ();

    // Check if above thresholds.
    if ( ( hit.getPercent () > Alignment.MIN_AA_PERCENT ) &&
         ( hit.getIdentities () > Alignment.MIN_AA_SEGMENT_IDENTITIES ) )
    {
      hit.setQueryName ( blast_header.getQueryName () );
      hits_file.println ( hit.toResult ( blast_header.getDatabaseName () ) );
    }  // if
    else
    {
      hit.initialize ();
      hit = null;
    }  // else
  }  // method addAlignment


/******************************************************************************/
  private void addAlignments ( Hit hit, BlastHeader blast_header )
  {
    // Check the alignment for long gaps in the query sequence.
    int gap = hit.getQueryAlignment ().indexOf ( large_gap );

    // Split on large gaps.
    while ( gap > 0 )
    {
      // Extract the first alignment segment.
      Hit first = getFirstSegment ( hit, gap );

      // Check if the first alignment segment should be saved.
      addAlignment ( first, blast_header );

      first = null;

      // Reset the hit to the alignment after the first alignment segment.
      hit = getNextSegment ( hit, gap );

      // Check for remaining gaps.
      gap = hit.getQueryAlignment ().indexOf ( large_gap );
    }  // while

    // Check the last alignment segment.
    addAlignment ( hit, blast_header );
  }  // method addAlignments


/******************************************************************************/
  private void parseAlignments ()
  {
    while ( ( blast_file.isEndOfFile () != true ) &&
            ( blast_file.startsWith ( "BLAST:" ) == false ) &&
            ( blast_file.startsWith ( "TBLAST" ) == false ) &&
            ( blast_file.startsWith ( "Query=" ) == false ) &&
            ( blast_file.startsWith ( "S1:" ) == false ) &&
            ( blast_file.startsWith ( "S2:" ) == false ) )
    {
        Hit hit = new Hit ();
        hit.setProgramName ( blast_header.getProgramName () );

        // Parse the alignment.
        parseAlignment ( hit  );

        // Check for query name within the target name.
        if ( hit.getTargetName ().toLowerCase ().indexOf 
                 ( blast_header.getQueryName ().toLowerCase () ) > 0 )
        {
          System.out.println ( hit.getTargetName () + " is same as query name: " + blast_header.getQueryName () );
          hit.initialize ();
        }  // if

        // Determine the alignment type.
        else if ( blast_header.getProgramName ().equals ( "BLASTN" ) == false ) 
        {
          // Check for good alignments to save.
          addAlignments ( hit, blast_header );
        }
        else  // nucleotide alignment
        {
          // Check if above thresholds.
          if ( hit.getPercent () > Alignment.MIN_DNA_PERCENT ) 
          {
            // Drop low complexity DNA alignments.
            if ( ( ( hit.getAlignmentLength () >= Alignment.MIN_LONG_DNA_ALIGNMENT ) &&
                   ( hit.getPercent () >= Alignment.HIGH_DNA_PERCENT ) ) ||
                 ( SeqTools.isLowDNAComplexity ( hit.getQueryAlignment (),
                       hit.getTargetAlignment () ) == false ) )
            {
              // Check for long alignments.
              if ( hit.getAlignmentLength () > 100 )
              {
                hit.setQueryName ( blast_header.getQueryName () );
                hits_file.println ( hit.toResult ( blast_header.getDatabaseName () ) );
              }  // if
            }  // if
          }  // if
        }  // else

      hit.initialize ();
      hit = null;
    }  // while
  }  // method parseAlignments


/******************************************************************************/
  private StringBuffer addBases ( Hit hit, StringBuffer base_counts )
  {
    // Ensure that the StringBuffer is long enough for the current range.
    int query_start = hit.getQueryStart ();
    int query_end   = hit.getQueryEnd ();
    if ( query_end < query_start + query_alignment.length () )

      query_end = query_start + query_alignment.length ();

    while ( base_counts.length () <= query_end )

      base_counts.append ( ' ' );

    while ( base_counts.length () <= query_start )

      base_counts.append ( ' ' );

    // Set the bases.
    String query_alignment  = hit.getQueryAlignment ();
    String target_alignment = hit.getTargetAlignment ();
    int base_index = query_start;

    // Set the identity bases.
    for ( int i = 0; i < query_alignment.length (); i++ )
    {
      char b = query_alignment.charAt ( i );		// base pair

      if ( b == target_alignment.charAt ( i ) )
      {
        if ( blast_header.getProgramName ().equals ( "BLASTN" ) == true )
        {
          if ( ( b == 'a' ) || ( b == 'c' ) || ( b == 'g' ) || ( b == 't' ) ||
               ( b == 'A' ) || ( b == 'C' ) || ( b == 'G' ) || ( b == 'T' ) )

            base_counts.setCharAt ( base_index, b );
        }  // if
        else
        {
          // Check for an amino acid.
          // if ( Sequence.isAA ( b ) == true )
          if ( ( ( b >= 'a' ) && ( b <= 'z' ) ) ||
               ( ( b >= 'A' ) && ( b <= 'Z' ) ) )

            base_counts.setCharAt ( base_index, b );
        }  // else
      }  // if

      if ( b != '-' )  base_index++;
    }  // for

    return base_counts;
  }  // method addBases


/******************************************************************************/
  // This method counts the number of bases in the StringBuffer.
  private int countBases ( StringBuffer base_counts )
  {
    int count = 0;

    // Traverse the base_counts StringBuffer array.
    for ( int i = 0; i < base_counts.length (); i++ )

      // Check for a base.
      if ( base_counts.charAt ( i ) != ' ' )  count++;

    return count;
  }  // method countBases


/******************************************************************************/
  // Check if the alignment has too few identities.
  private boolean isTooFew ( int base_counts )
  {
    // Check for a DNA alignment.
    if ( blast_header.getProgramName ().equals ( "BLASTN" ) == true ) 
    {
      if ( base_counts < Alignment.MIN_DNA_IDENTITIES )

        return true;

      return false;
    }
    else
    {
      if ( base_counts < Alignment.MIN_AA_IDENTITIES )

        return true;

      return false;
    }  // else
  }  // method isTooFew


/******************************************************************************/
  // This method computes the overlap size between two alignments.
  private int computeOverlapSize ( Hit hit1, Hit hit2 )
  {
    int q_start = hit1.getQueryStart ();
    int q_end   = hit1.getQueryEnd ();

    int t_start = hit1.getTargetStart ();
    int t_end   = hit1.getTargetEnd ();

    if ( hit2.getQueryStart () > q_start )  q_start = hit2.getQueryStart ();
    if ( hit2.getQueryEnd   () < q_end   )  q_end   = hit2.getQueryEnd   ();

    if ( hit2.getTargetStart () > t_start )  t_start = hit2.getTargetStart ();
    if ( hit2.getTargetEnd   () < t_end   )  t_end   = hit2.getTargetEnd   ();

    // Check if the alignments overlap.
    if ( ( q_start < q_end ) && ( t_start < t_end ) )
    
      return ( q_end - q_start + 1 );

    return 0;
  }  // method computeOverlapSize


/******************************************************************************/
  public void processFile ( String file_name )
  {
    blast_file_name = file_name;

    processFile ();
  }  // method processFile


/******************************************************************************/
  public void processFile ()
  {
    // Set the input file name.
    blast_file.initialize ();
    blast_file.setFileName ( blast_file_name );

    // Open the input file.
    blast_file.openFile ();

    // Process the input file.
    while ( blast_file.isEndOfFile () != true )
    {
      // Process the BLAST file header.
      parseHeader ();

      // Parse the current query sequence & alignments.
      parseAlignments ();
    }  // while

    // Close input file.
    blast_file.closeFile();
  }  // method processFile


/******************************************************************************/
  public void processFiles ( String file_name )
  {
    hits_file.setFileName ( file_name + ".Hits" );
    hits_file.openFile ();

    StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

    // Get the file name of the list of BLAST output files.
    InputTools name_list = new InputTools ();
    name_list.setFileName ( file_name );
    name_list.openFile ();

    // Process the list of BLAST output files.
    while ( name_list.isEndOfFile () == false )
    {
      // Read the next line from the list of names file.
      name_line = name_list.getLine ();

      if ( name_list.isEndOfFile () == false )
      {
        String name = name_line.toString ().trim ();

        System.out.println ( "Processing: " + name );

        // Process the BLAST file
        processFile ( name );
      }  // if
    }  // while

    name_list.closeFile ();
    hits_file.closeFile ();
  }  // method processFiles



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java BlastParserLow <list_of_BLAST_files>" );
    System.out.println ();
    System.out.print   ( "where <Blast_file> is the file name of a " );
    System.out.println ( "list of BLAST output filenames." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    BlastParserLow app = new BlastParserLow ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class BlastParserLow

