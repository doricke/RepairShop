
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

import Alignment;

/******************************************************************************/
// Multiple Sequence Alignment (MSA) Object
public class MSA extends Object
{


/******************************************************************************/
// Data structures:

private StringBuffer [] msa_names = null;	// Names of MSA sequences

private StringBuffer [] msa_seqs = null;	// MSA sequences

private int total_sequences = 0;		// Number of aligned sequences


/******************************************************************************/
// Parameters:

private int alignment_length = 1000;		// Default alignment sequence length

private int alignment_width = 100;		// Default alignment width for printing

private int max_alignments = 1001;		// Maximum number of alignments

private int max_name_length = 25;		// Maximum name length to print

private int name_length = 50;			// Default sequence name length


/******************************************************************************/
private static final char gap_char = '-';	// Gap character


/******************************************************************************/
public MSA ()
{
  initialize ();
}  // constructor MSA


/******************************************************************************/
private void initialize ()
{
  total_sequences = 0;
}  // method initialize


/******************************************************************************/
public void clear ()
{
  initialize ();

  // Clear the MSA.
  if ( msa_seqs.length > 0 )
  {
    for ( int i = 0; i < msa_seqs.length; i++ )

      msa_seqs [ i ].setLength ( 0 );
  }  // if

  // Clear the MSA sequence names.
  if ( msa_names.length > 0 )
  {
    for ( int i = 0; i < msa_names.length; i++ )

      msa_names [ i ].setLength ( 0 ); 
  }  // if
}  // method clear


/******************************************************************************/
public void close ()
{
  clear ();

  // Release the MSA.
  if ( msa_seqs.length > 0 )
  {
    for ( int i = 0; i < msa_seqs.length; i++ )

      msa_seqs [ i ] = null;
  }  // if

  // Release the MSA names.
  if ( msa_names.length > 0 )
  {
    for ( int i = 0; i < msa_names.length; i++ )

      msa_names [ i ] = null;
  }  // if

  msa_seqs = null;
  msa_names = null;
}  // method close


/******************************************************************************/
public void allocateMSA ()
{
  msa_seqs = new StringBuffer [ max_alignments ];

  msa_names = new StringBuffer [ max_alignments ];

  // Initialize each of the alignment sequences.
  for ( int i = 0; i < max_alignments; i++ )
  {
    msa_seqs [ i ] = new StringBuffer ( alignment_length );
    msa_seqs [ i ].setLength ( 0 );

    msa_names [ i ] = new StringBuffer ( name_length );
    msa_names [ i ].setLength ( 0 );
  }  // for
}  // method allocateMSA


/******************************************************************************/
// This method counts the occurances of "letter" in a MSA column.
private int countChar ( int position, char letter )
{
  int count = 0;

  // Check for no alignments.
  if ( ( msa_seqs == null ) || ( msa_seqs.length <= 0 ) )  return count;

  // Loop through the set of sequences.
  for ( int i = 0; i < msa_seqs.length; i++ )

    // When matched, count the target letter occurances.
    if ( msa_seqs [ i ].charAt ( position ) == letter )  count++;

  return count;
}  // method countChar


/******************************************************************************/
// This method determines the consensus amino acid for a position.
private char consensusChar ( int position )
{
  int letters = 28;		// A-Z, gap character, and space character
  int gap_index = 26;		// index of gap character
  int space_index = 27;		// index of ' '
  int letters_seen = 0;		// Count of letters seen

  int counts [] = new int [ letters ];		// Count of each amino acid frequency


  // Check for no sequences.
  if ( ( msa_seqs == null ) || ( msa_seqs.length <= 0 ) )  return '?';

  for ( int i = 0; i < letters; i++ )  counts [ i ] = 0;

  // Traverse each of the sequences.
  for ( int i = 0; i < msa_seqs.length; i++ )
  {
    // Check if this sequence is this long.
    if ( msa_seqs [ i ].length () > position )
    {
      char letter = msa_seqs [ i ].charAt ( position );

      // Check for an upper case letter.
      if ( ( letter >= 'A' ) &&
           ( letter <= 'Z' ) )
      {
        counts [ letter - 'A' ]++;
        letters_seen++;
      }  // if
      else
      {
        // Check for a lower case letter.
        if ( ( letter >= 'a' ) &&
             ( letter <= 'z' ) )
        {
          counts [ letter - 'a' ]++;
          letters_seen++;
        }  // if
        else
        {
          // Check for a gap character.
          if ( letter == gap_char )
          {
            counts [ gap_index ]++;
            letters_seen++;
          }
          else
            // Check for a space character.
            if ( letter == ' ' )  counts [ space_index ]++;
            else
              System.out.println ( "MSA.consensusChar: unknown sequence character '" +
                  letter + " in sequence " + msa_names [ i ] + " at position " +
                  position );
        }  // else
      }  // else
    }  // if
  }  // for

  // Determine the maximum count seen.
  int max_count = 0;
  int max_index = 0;
  for ( int i = 0; i < letters; i++ )
  {
    if ( counts [ i ] > max_count )
    {
      max_count = counts [ i ];
      max_index = i;
    }  // if
  }  // for

  // Check if no letter represents the majority.
  if ( max_count * 2 < letters_seen )  return ' ';

  if ( max_index < 26 )  return (char) ('A' + max_index);
  if ( max_index == gap_index )  return gap_char;
  return ' ';
}  // method consensusChar


/******************************************************************************/
// This method adds a consensus sequences to the MSA.
public void addConsensus ()
{
  // Set the name of the consensus sequence.
  msa_names [ total_sequences ].append ( "Consensus" );

  // Build the consensus sequence.
  for ( int i = 0; i < msa_seqs [ 0 ].length (); i++ )

    // Append one consensus character at a time.
    msa_seqs [ total_sequences ].append ( consensusChar ( i ) );

  // The consensus sequence is complete.
  total_sequences++;
}  // method addConsensus


/******************************************************************************/
private void addQuery ( Alignment pair_alignment )
{
  // Leading space pad the query sequence if needed.
  int query_start = pair_alignment.getQueryStart ();

  // Check if the query starts at the beginning of the sequence.
  if ( query_start > 1 )
  {
    for ( int i = 1; i < query_start; i++ )

      // Add a space filling space.
      msa_seqs [ 0 ].append ( " " );
  }  // if 

  // Add the query sequence to the MSA.
  msa_seqs [ 0 ].append ( pair_alignment.getQuery ().toString () );

  // Set the name of the query sequence.
  msa_names [ 0 ].append ( pair_alignment.getQueryName ().toString () );

  // First sequence added to the MSA.
  total_sequences++;
}  // method addQuery


/******************************************************************************/
private void setGap ( int sequence_index, int position )
{
  // Check if sequence_index is valid.
  if ( ( sequence_index < 0 ) || ( sequence_index >= msa_seqs.length ) )
  {
    System.out.println ( "MSA.setGap: sequence_index " + sequence_index +
        " value is out of range [0-" + msa_seqs.length + "]" );
    return;
  }  // if

  // Check if the MSA sequence is this long.
  if ( position >= msa_seqs [ sequence_index ].length () )  return;

  char gap_letter = gap_char;

  // Check if at the beginning of a sequence.
  if ( position == 0 ) 

    // Insert a space rather than a dash if the start of a sequence.
    gap_letter = ' ';

  else

    if ( position > 0 )

      // Check if the start of the sequence.
      if ( msa_seqs [ sequence_index ].charAt ( position-1 ) == ' ' )
        gap_letter = ' ';

  // Insert the gap letter.
  msa_seqs [ sequence_index ].insert ( position, gap_letter );
}  // method setGap


/******************************************************************************/
private void alignQueries ( Alignment pair_alignment )
{
  // Determine where the current query sequence starts.
  int query_start = pair_alignment.getQueryStart () - 1;

  // Determine the corresponding position in the MSA.
  int msa_position = 0;

  // Check if the query sequence starts down sequence.
  int position = 0;
  if ( query_start > 0 )
  {
    for ( int i = 0; i < query_start; i++ )
    {
      // Advance past gaps in the MSA.
      while ( msa_seqs [ 0 ].charAt ( msa_position ) == gap_char )
      {
        msa_position++;

        // Insert a space into the target sequence.
        msa_seqs [ total_sequences-1 ].insert ( 0, ' ' );
      }  // while

      // Advance for each non-gapped position.
      msa_position++;
    }  // for
  }  // if

  int query_position = 0;

  // Align the two query sequences.
  StringBuffer query = pair_alignment.getQuery ();

  while ( ( query_position < query.length () ) && 
          ( msa_position < msa_seqs [ 0 ].length () ) )
  {
    // Case 1: bases match
    if ( query.charAt ( query_position ) == msa_seqs [ 0 ].charAt ( msa_position ) )
    {
      query_position++;
      msa_position++;
    }
    else

      // Case 2: gap in query sequence.
      if ( ( query.charAt ( query_position ) == gap_char ) &&
           ( msa_seqs [ 0 ].charAt ( msa_position ) != gap_char ) )
      {
        // Insert a gap into the MSA.
        for ( int i = 0; i < total_sequences-1; i++ )

          // Check if the MSA sequence is this long.
          if ( msa_position < msa_seqs [ i ].length () )
                 
            // Insert an alignment gap into the sequence.
            setGap ( i, msa_position );

        msa_position++;		// advance because inserted gap...
        query_position++;
      }  // if
      else

        // Case 3: gap in MSA sequence.
        if ( ( query.charAt ( query_position ) != gap_char ) &&
             ( msa_seqs [ 0 ].charAt ( msa_position ) == gap_char ) )
        {
          // Check if the target sequence is too short.
          if ( msa_position < msa_seqs [ total_sequences-1 ].length () )

            // Insert a gap into the current target sequence.
            setGap ( total_sequences-1, msa_position );

          else
          {
System.out.println ( "Can't insert gap into target sequence at " + msa_position );
System.out.println ( "MSA query = " + msa_seqs [ 0 ].toString () );
System.out.println ( "target    = " + msa_seqs [ total_sequences-1 ].toString () );
          }  // else

          msa_position++;
        }
        else
        {
          // Case 4: shouldn't happen...  Alignments are out of sync. 
          System.out.println ( "query alignment out of sync.: msa.i = " + msa_position +
              ", query.i = " + query_position + ", MSA Q=" + msa_seqs [ 0 ].charAt ( msa_position ) +
              ", new Query=" + query.charAt ( query_position ) );

          query_position = query.length ();	// exit the loop...
        }  // else
  }  // while

  // Check if the new query is longer the the MSA query sequence.
  if ( ( query_position < query.length () ) &&
       ( msa_position >= msa_seqs [ 0 ].length () ) )
  {
    System.out.println ( "Extending MSA query line." );

    // Append the rest of the query sequence.
    msa_seqs [ 0 ].append ( query.substring ( query_position ) );
  }  // if
}  // method alignQueries


/******************************************************************************/
private void addTarget ( Alignment pair_alignment )
{
  // Leading space pad the target sequence.
  int query_start = pair_alignment.getQueryStart ();

  // If the target does not align with the query start.
  if ( query_start > 1 )
  {
    for ( int i = 1; i < query_start; i++ )

      // Add a space filling space.
      msa_seqs [ total_sequences ].append ( " " );
  }  // if 

  // Add the target sequence to the MSA.
  msa_seqs [ total_sequences ].append ( pair_alignment.getTarget ().toString () );

  // Add the target sequence name to the MSA.
  msa_names [ total_sequences ].append ( pair_alignment.getTargetName ().toString () );

  // New sequence added to the end of the alignment.
  total_sequences++;

  // Align the current query sequence with the MSA query sequence.
  alignQueries ( pair_alignment );
}  // method addTarget


/******************************************************************************/
public void addAlignment ( Alignment pair_alignment )
{
  // Check if alignments have been allocated.
  if ( ( msa_seqs == null ) || ( msa_seqs.length == 0 ) )

    allocateMSA ();

  // Check if first pair-wise alignment.
  if ( total_sequences == 0 )
  {
    // Add the query sequence.
    addQuery ( pair_alignment );

    // Add the target sequence.
    addTarget ( pair_alignment );
  }  // if
  else
  {
    // Add the target sequence.
    addTarget ( pair_alignment );
  }  // else
}  // method addAlignment


/******************************************************************************/
public void setMaxAlignments ( int maximum_number_of_alignments )
{
  max_alignments = maximum_number_of_alignments;
}  // method setMaxAlignments


/******************************************************************************/
public void printMSA ()
{
  // Check for no alignments.
  if ( total_sequences <= 0 )  return;

  // Print out the MSA.
  for ( int start = 0; start < msa_seqs [ 0 ].length (); start += alignment_width )
  {
    for ( int i = 0; i < total_sequences; i++ )
    {
      // Determine the end of sequence segment.
      int end = start + alignment_width;
      if ( end > msa_seqs [ 0 ].length () )  end = msa_seqs [ 0 ].length ();

      // Check if the current sequence is to be printed.
      if ( start < msa_seqs [ i ].length () )
      {
        // Check for the first sequence.
        if ( i == 0 )
        {
           for ( int space = 1; space <= max_name_length; space++ )

             System.out.print ( " " );

          System.out.print ( "  " );

           System.out.println ( start + 1 );
        }  // if 

        // Print out the first N characters of the sequence name.
        if ( msa_names [ i ].length () >= max_name_length )
          System.out.print ( msa_names [ i ].substring ( 0, max_name_length ) );
        else
        {
          System.out.print ( msa_names [ i ].toString () );

          // Pad the sequence name with spaces.
          for ( int space = msa_names [ i ].length () + 1; space <= max_name_length; space++ )

            System.out.print ( " " );
        }  // else

        System.out.print ( "  " );

        // Print out the sequence segment for the current sequence.
        int base = start;
        while ( ( base < end ) && ( base < msa_seqs [ i ].length () ) )
        {
          System.out.print ( msa_seqs [ i ].charAt ( base ) );
          base++;
        }  // while

        // End the sequence line.
        System.out.println ();
      }  // if
    }  // for

    // Separate the alignment blocks.
    System.out.println ();
  }  // for
}  // method printMSA


/******************************************************************************/
  private void score ( )
  {
  }  // method score


/******************************************************************************/
  private void checkShift 
      ( int seq_index			// index of sequence
      , int seq_gap			// index of start of local gap in sequence
      , int con_index			// index of consensus sequence
      , int gap_start			// start of consensus gap
      , int gap_end 			// end of consensus gap
      )
  {
    int seq_start = seq_gap;		// start of the sequence.

    // Check if sequence gap is before the consensus gap.
    if ( seq_gap < gap_start )
    {
      seq_start++;
      while ( msa_seqs [ seq_index ].charAt ( seq_start ) == gap_char )  seq_start++;
     
      // Compare leaving alignment alone to shifting the alignment.
      if ( score ( msa_seqs [ seq_index ].substring ( seq_start, gap_start ),
                   msa_seqs [ con_index ].substring ( seq_start, gap_start ) ) >
           score ( msa_seqs [ seq_index ].substring ( seq_start, gap_start ),
                   msa_seqs [ con_index ].substring ( seq_gap, gap_start ) ) )
      {
        msa_seqs [ seq_index ].replace ( seq_gap, gap_end - (seq_start - seq_gap),
          msa_seqs [ seq_index ].substring ( seq_start, gap_start + (seq_start - seq_gap) - 1 ) );
      }  // if
    }  // if
    else
    {
    }  // else
  }  // method checkShift


/******************************************************************************/
  public void findNonGaps ()
  {
    int con_index = total_sequences - 1;	// Consensus index
    int end      = 0;				// End of range to scan
    int prev_gap = 0;				// Index of previous gap
    int next_gap = 0;				// Index of next gap
    int start    = 0;				// Start of range to scan


    // Scan the consensus for gaps.
    for ( int gap_index = 0; gap_index < msa_seqs [ con_index ].length (); gap_index++ )
    {
      // Find the start of a gap.
      if ( msa_seqs [ con_index ].charAt ( gap_index ) == gap_char )
      {
        // Find the end of the gap.
        int gap_end = gap_index;
        while ( ( gap_end + 1 < msa_seqs [ con_index ].length () ) &&
                ( msa_seqs [ con_index ].charAt ( gap_end + 1 ) == gap_char ) )

          gap_end++;

        // Find the start of the next gap.
        next_gap = gap_end + 1;
        while ( ( next_gap < msa_seqs [ con_index ].length () ) &&
                ( msa_seqs [ con_index ].charAt ( next_gap ) != gap_char ) )

          next_gap++;

        System.out.println ( "Gap [" + gap_index + "-" + gap_end + 
            "], next_gap " + next_gap );

        // Check each sequence for non-gap in gap columns.
        for ( int msa_index = 0; msa_index < con_index; msa_index++ )
        {
          // Check if the sequence is long enough.
          if ( gap_end < msa_seqs [ msa_index ].length () )
          {
            // Determine the end of the range to scan.
            end = next_gap - 1;
            if ( end > msa_seqs [ msa_index ].length () )
              end = msa_seqs [ msa_index ].length ();

            // Determine the start of the range to scan.
            start = prev_gap;
            while ( msa_seqs [ msa_index ].charAt ( start ) == ' ' )  start++;

            // Check for Non-gap characters.
            for ( int check_index = gap_index; check_index <= gap_end; check_index++ )
            {
              if ( ( msa_seqs [ msa_index ].charAt ( check_index ) != gap_char ) &&
                   ( msa_seqs [ msa_index ].charAt ( check_index ) != ' ' ) )
              {
                System.out.println ( "Non-gap: msa_index " + msa_index + 
                    " position " + check_index + " '" + 
                    msa_seqs [ msa_index ].charAt ( check_index ) + "'" +
                    " scan [" + start + "," + end + "]" );

                // Scan for a gap in this sequence before this gap.
                  for ( int j = start; j < gap_index; j++ )

                    if ( msa_seqs [ msa_index ].charAt ( j ) == gap_char )
                    {
                      // Check if can shift the gap over.
                      checkShift ( msa_index, j, con_index, gap_index, gap_end );

                      System.out.println ( "\tlocal gap at " + j );
                    }  // if

                // Scan for a gap in this sequence after this gap.
                if ( gap_end + 1 < msa_seqs [ msa_index ].length () )

                  for ( int j = gap_end + 1; j <= end; j++ )

                    if ( ( j < msa_seqs [ msa_index ].length () ) &&
                         ( msa_seqs [ msa_index ].charAt ( j ) == gap_char ) )
                    {
                      // Check if can shift the gap over.
                      checkShift ( msa_index, j, con_index, gap_index, gap_end );

                      System.out.println ( "\tlocal gap at " + j );
                    }  // if
              }  // if 
            }  // for
          }  // if
        }  // for

        // Skip to the end of this gap.
        gap_index = next_gap - 1;
        prev_gap = gap_end + 1;
      }  // if
    }  // for
  }  // method findNonGaps
 

/******************************************************************************/
  public void findCloseGaps ()
  {
    int con_index = total_sequences - 1;	// Consensus index
    int end      = 0;				// End of range to scan
    int prev_gap = 0;				// Index of previous gap
    int next_gap = 0;				// Index of next gap
    int start    = 0;				// Start of range to scan


    // Scan the consensus for gaps.
    for ( int gap_index = 0; gap_index < msa_seqs [ con_index ].length (); gap_index++ )
    {
      // Find the start of a gap.
      if ( msa_seqs [ con_index ].charAt ( gap_index ) == gap_char )
      {
        // Find the end of the gap.
        int gap_end = gap_index;
        while ( ( gap_end + 1 < msa_seqs [ con_index ].length () ) &&
                ( msa_seqs [ con_index ].charAt ( gap_end + 1 ) == gap_char ) )

          gap_end++;

        // Find the start of the next gap.
        next_gap = gap_end + 1;
        while ( ( next_gap < msa_seqs [ con_index ].length () ) &&
                ( msa_seqs [ con_index ].charAt ( next_gap ) != gap_char ) )

          next_gap++;

        if ( next_gap - gap_index + 1 <= 10 )

          System.out.println ( "Close Gaps [" + gap_index + "-" + gap_end + 
              "], next_gap " + next_gap );

        // Skip to the end of this gap.
        gap_index = next_gap - 1;
        prev_gap = gap_end + 1;
      }  // if
    }  // for
  }  // method findCloseGaps


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
}  // class MSA

