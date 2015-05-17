
// import AlignMany;

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

public class SubSequences extends Object
{


/******************************************************************************/

  private   byte  location = 0;				// MSA location of subsequences

  private   int  max_length = 0;			// maximum subsequence length in the set

  private   String [] sequences = null;			// original subsequences

  private   StringBuffer [] sub_alignments = null;	// sequence alignments

  private   byte [] sub_map = null;			// subsequences map

  private   String [] sub_sequences = null;		// sequences

  private   byte  word_size = 3;			// word size


/******************************************************************************/
  // Constructor SubSequences
  public SubSequences ()
  {
    initialize ();
  }  // constructor SubSequences


/******************************************************************************/
  // Constructor SubSequences
  public SubSequences ( String [] sequences, byte word_size, byte loc )
  {
    initialize ();

/*
System.out.println ( "SubSequences created: word_size = " + word_size + ", loc = " + loc );
for ( int i = 0; i < sequences.length; i++ )
  System.out.println ( i + "  +" + sequences [ i ] + "+" );
*/

    location = loc;
    setSubSequences ( sequences, word_size );
    align ();
  }  // constructor SubSequences


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    location = 0;
    max_length = 0;
    sequences = null;
    sub_alignments = null;
    sub_sequences = null;
    word_size = 3;
  }  // method initialize 


/******************************************************************************/
  public byte getLocation ()
  {
    return location;
  }  // method getLocation


/******************************************************************************/
  public StringBuffer [] getSubAlignments ()
  {
    return sub_alignments;
  }  // method getSubAlignments


/******************************************************************************/
  public String [] getSubSequences ()
  {
    return sub_sequences;
  }  // method getSubSequences


/******************************************************************************/
  public void setLocation ( byte value )
  {
    location = value;
  }  // method setLocation


/******************************************************************************/
  public void setSubSequences ( String [] value, byte new_word_size )
  {
    // Save the original sequences.
    sequences = value;

    // Set the word size.
    word_size = new_word_size;

    // Determine the maximum subsequence length.
    max_length = 0;
    for ( byte i = 0; i < value.length; i++ )

      // Save the longest length.
      if ( value [ i ].length () > max_length )  

        max_length = value [ i ].length ();

    // Compare maximum length versus word size.
    if ( max_length < word_size )  word_size = (byte) max_length;

    // Skip short tail sequences.
    int min_length = word_size;
    if ( max_length > 20 )  min_length = 10;

// System.out.println ( "setSubSequences: max_length = " + max_length + ", min_length = " + min_length + ", word_size = " + word_size );

    // Determine the number of usuable subsequences.
    sub_map = new byte [ value.length ];
    int current = 0;
    for ( byte i = 0; i < value.length; i++ )
    {
      if ( value [ i ].length () >= min_length )
      {
        sub_map [ current ] = i;
        current++;
      }  // if
    }  // for

    // Check for no valid subsequences.
    if ( current == 0 )
    {
      if ( word_size > 1 )
        // Try again with smaller word size.
        setSubSequences ( value, (byte) ( word_size - 1 ) );
      else
      {
        System.out.println ( "*Warning* SubSequences.setSubSequences - no subsequences." );
        return;
      }  // else
    }  // if

    sub_alignments = new StringBuffer [ value.length ];
    for ( int i = 0; i < value.length; i++ )
      sub_alignments [ i ] = new StringBuffer ( max_length * 2 );

    sub_sequences = new String [ current ];
    for ( int i = 0; i < current; i++ )
    
      sub_sequences [ i ] = value [ sub_map [ i ] ];
  }  // method setSubSequences


/******************************************************************************/
  public void setWordSize ( byte value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  private void fillAlign ()
  {
    // Determine the maximum sequence length.
    int max_length = 0;
    for ( int i = 0; i < sequences.length; i++ )

      if ( sequences [ i ].length () > max_length )

        max_length = sequences [ i ].length ();

    // Pad the alignments.
    for ( int i = 0; i < sequences.length; i++ )

      if ( ( sequences [ i ].length () > 0 ) && 
           ( sub_alignments [ i ].length () < max_length ) )
  
        padAlign ( i, max_length );
  }  // method fillAlign


/******************************************************************************/
  private void padAlign ( int index, int align_length )
  {
    // Determine the number of pad characters.
    int pad_length = align_length - sub_alignments [ index ].length ();

    // Check if the sequence needs to be copied.
    boolean copy_seq = false;
    if ( sub_alignments [ index ].length () <= 0 )
    {
      copy_seq = true;
      pad_length = align_length - sequences [ index ].length ();
    }  // if

    // Check for a 5' MSA position.
    if ( location == AlignMany.MSA_START )
      for ( int i = 0; i < pad_length; i++ )
        sub_alignments [ index ].append ( "~" );

    // Copy the sequence if it was not aligned.
    if ( copy_seq == true )
      sub_alignments [ index ].append ( sequences [ index ] );

    // Check for a 3' or internal MSA position.
    if ( location != AlignMany.MSA_START )
      for ( int i = 0; i < pad_length; i++ )
        sub_alignments [ index ].append ( "~" );
  }  // method padAlign


/******************************************************************************/
  public void align ()
  {
    // Validate.
    if ( ( sub_sequences == null ) || ( sub_alignments == null ) )  return;

    // Check for a word size of one.
    if ( word_size == 1 )
    {
      fillAlign ();
      return;
    }  // if

    // Align the subsequences using a smaller word size.
    AlignMany smaller = new AlignMany ( sub_sequences, word_size );

    // Get the subsequence alignments.
    StringBuffer [] segments = smaller.getAlignments ();

    // Copy the alignments.
    int align_length = 0;
    for ( int i = 0; i < sub_sequences.length; i++ )
    {
      sub_alignments [ sub_map [ i ] ].append ( segments [ i ] );

      // Check for an increase in alignment length.
      if ( segments [ i ].length () > max_length )

        max_length = segments [ i ].length ();

      // Check for an increase in alignment length.
      if ( segments [ i ].length () > align_length )

        align_length = segments [ i ].length ();
    }  // for

    // Align the remaining subsequences.
/*
    for ( int i = 0; i < sub_alignments.length; i++ )

      if ( sub_alignments [ i ].length () < align_length )

        padAlign ( i, align_length );
*/
    // printSubAlignments ();
  }  // method align


/******************************************************************************/
  public void printSubAlignments ()
  {
    System.out.println ();
    System.out.println ( "SubSequenes Alignment: word_size = " + word_size );
    for ( int i = 0; i < sub_alignments.length; i++ )
    {
      if ( sub_alignments [ i ].length () > 0 )
        System.out.println ( i + "\t" + sequences [ i ] + "\t'" + sub_alignments [ i ].toString () + "'" );
      else
        System.out.println ( i + "\tEmpty subsequence" );
    }  // for
  }  // method printSubAlignments


/******************************************************************************/

}  // class SubSequences
