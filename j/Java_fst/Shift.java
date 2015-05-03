
import OutputTools;
import Sequence;
import SeqTools;
import TLAlignment;
import TLHeader;
import TLQuery;

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

public class Shift extends Object
{

  Sequence old_mrna_seq = new Sequence ();		// original mRNA sequence

  Sequence old_aa_seq = new Sequence ();		// original peptide sequence

  StringBuffer new_mrna = new StringBuffer ( 4096 );	// new mRNA sequence

  StringBuffer new_aa = new StringBuffer ( 4096 );	// new peptide sequence

  OutputTools new_mrna_file = new OutputTools ();	// new mRNA file

  OutputTools new_aa_file = new OutputTools ();		// new peptide file


/******************************************************************************/
public void initialize ()
{
  old_mrna_seq.initialize ();
  old_aa_seq.initialize ();
  new_mrna.setLength ( 0 );
  new_aa.setLength ( 0 );
  new_mrna_file.initialize ();
  new_aa_file.initialize ();
}  // method initialize


/******************************************************************************/
// This method closes the original mRNA and peptide sequence files.
private void closeSeqFiles ()
{
  old_mrna_seq.closeSequenceFile ();

  old_aa_seq.closeSequenceFile ();
}  // method closeSeqFiles


/******************************************************************************/
// This method reads in the original mRNA and peptide sequence files.
private void readSeqFiles ( String qtext )
{
  // Extract the mRNA sequence name from the QTEXT line.
  int index = qtext.indexOf ( ' ' );
  String file_name = qtext;
  if ( index > 0 )  file_name = qtext.substring ( 0, index );

  // Read in the mRNA sequence.
  old_mrna_seq.setSequenceType ( Sequence.mRNA );
  old_mrna_seq.setFileName ( "old/" + file_name );
  old_mrna_seq.openSequenceFile ();
  old_mrna_seq.readSequence ();

  // Set up the new mRNA sequence file name.
  new_mrna_file.setFileName ( "new/" + file_name );

  // Copy the old mRNA sequence to the new mRNA sequence.
  new_mrna.setLength ( 0 );
  new_mrna.append ( old_mrna_seq.getSequence () );

  // System.out.println ( "Shift.readSeqFiles: mRNA length = " + 
  //   old_mrna_seq.getSequence ().length () );

  // Convert the mRNA file name to the peptide name.
  index = file_name.indexOf ( ".mrna" );
  if ( index > 0 )
    file_name = file_name.substring ( 0, index ) + ".aa";

  // Read in the peptide sequence (if it exists).
  old_aa_seq.setSequenceType ( Sequence.AA );
  old_aa_seq.setFileName ( "old/" + file_name );
  old_aa_seq.openSequenceFile ();
  old_aa_seq.readSequence ();

  // Set up the new peptide sequence file name.
  new_aa_file.setFileName ( "new/" + file_name );

  // System.out.println ( "Shift.readSeqFiles: peptide length = " + 
  //   old_aa_seq.getSequence ().length () );

  // Copy the old peptide sequence to the new peptide sequence.
  new_aa.setLength ( 0 );
  // new_aa.append ( old_aa_seq.getSequence () );

}  // method readSeqFiles


/******************************************************************************/
// This method writes out the new mRNA and peptide sequence files.
private void writeSeqFiles ()
{
  if ( new_mrna.length () > 0 )
  {
    new_mrna_file.openFile ();
    new_mrna_file.println ( ">" + old_mrna_seq.getSequenceDescription () );
    Sequence.writeFastaClob ( new_mrna.toString (), new_mrna_file );
    new_mrna_file.closeFile ();
  }  // if
 
  if ( new_aa.length () > 0 )
  {
    new_aa_file.openFile ();

    // Check for no previous protein sequence file.
    if ( old_aa_seq.getSequenceDescription ().length () < 10 )
    {
      String desc = old_mrna_seq.getSequenceDescription ();
      int index = desc.indexOf ( ".mrna" );
      if ( index > 0 )
      {
        new_aa_file.println ( ">" + desc.substring ( 0, index ) + ".aa" 
            + desc.substring ( index + 5 ) );
      }
      else
        new_aa_file.println ( ">" + desc );
    }
    else
      new_aa_file.println ( ">" + old_aa_seq.getSequenceDescription () );

    Sequence.writeFastaClob ( new_aa.toString (), new_aa_file );
    new_aa_file.closeFile ();
  }  // if
}  // method writeSeqFiles


/******************************************************************************/
// This function counts the number of gap characters in a codon.
private int countGaps ( String codon )
{
  int gaps = 0;

  // Traverse the codon.
  for ( int i = 0; i < codon.length (); i++ )

    // Count the gap characters.
    if ( codon.charAt ( i ) == '-' )  gaps++;

  return gaps;
}  // method countGaps


/******************************************************************************/
// This function finds the base in the codon to delete.
private int baseIndex ( String codon )
{
  // Search the codon.
  for ( int index = 0; index < codon.length (); index++ )

    // Return the index of the first non-gap character.
    if ( codon.charAt ( index ) != '-' )  return index;

  return 0;
}  // method baseIndex


/******************************************************************************/
// This function finds the "optimal" site to insert an "n" base into the mRNA.
private int findSite ( StringBuffer mRNA, int index )
{
  // Find the nearest IUB code.
  int site;

  for ( site = index + 3; ( site >= 0 ) && ( site >= index - 10 ); site-- )
  {
    // Check if site is within bounds.
    if ( ( site < mRNA.length () ) &&
         ( site >= 0 ) )
    {
      if ( SeqTools.isIUB ( mRNA.charAt ( site ) ) == true )

        return site;
    }  // if
  }  // for

  // No IUB site found, select end of codon.
  site = index + 2;
  if ( index + 2 > mRNA.length () )  site = mRNA.length () - 1;

  return site;
}  // method findSite


/******************************************************************************/
// This function gets the conserved residues from the FST identity line.
private String getWindow ( StringBuffer pep_ident, int index )
{
  StringBuffer window = new StringBuffer ( 4 );
  window.setLength ( 0 );
  window.append ( "   " );

  // Set the first character of the window.
  window.setCharAt ( 0, pep_ident.charAt ( index ) );
 
  // Set the second character of the window.
  if ( index + 3 < pep_ident.length () )
 
    window.setCharAt ( 1, pep_ident.charAt ( index + 3 ) );
 
  // Set the third character of the window.
  if ( index + 6 < pep_ident.length () )
 
    window.setCharAt ( 2, pep_ident.charAt ( index + 6 ) ); 

  return window.toString ();
}  // method getWindow


/******************************************************************************/
// This function counts the number of conserved residues in a window.
private int countConserved ( StringBuffer window )
{
  int count = 0;		// number of conserved residues

  for ( int i = 0; i < window.length (); i++ )

    if ( ( window.charAt ( i ) >= 'A' ) && 
         ( window.charAt ( i ) <= 'Z' ) )  count++;

  return count;
}  // method countConserved


/******************************************************************************/
// This function sets the conserved residues into the template.
private StringBuffer setConserved 
    ( StringBuffer template
    , int          index
    , StringBuffer window
    )
{
  // Copy the conserved residues in the window to the template.
  for ( int i = 0; i < window.length (); i++ )

    if ( ( window.charAt ( i ) >= 'A' ) && 
         ( window.charAt ( i ) <= 'Z' ) )  

      template.setCharAt ( index + i, window.charAt ( i ) );

  return template;
}  // method setConserved


/******************************************************************************/
// This function builds a translation template from FST identity line.
private StringBuffer translationTemplate ( StringBuffer pep_ident )
{
  StringBuffer template = new StringBuffer ( pep_ident.length () );

  // Initialize the template.
  template.setLength ( 0 );
  for ( int i = 0; i < pep_ident.length () + 2; i += 3 )

    template.append ( ' ' );

  // Copy conserved blocks to the translation template.
  StringBuffer window = new StringBuffer ( 4 );
  for ( int i = 0; i < pep_ident.length (); i += 3 )
  {
    window.setLength ( 0 );
    window.append ( getWindow ( pep_ident, i ) );

    // Count the number of conserved residues in the window.
    int conserved = countConserved ( window );

    // Check that at least two residues are conserved in the window.
    if ( conserved > 1 )

      // Copy the conserved residues to the template.
      template = setConserved ( template, i / 3, window );
  }  // for

  return template;
}  // method translationTemplate


/******************************************************************************/
// This function returns the next codon from the mRNA.
private String getCodon ( StringBuffer mRNA, int index )
{
  String codon = "";

  // Validate index.
  if ( ( index < 0 ) || ( index >= mRNA.length () ) )  return codon;

  if ( index + 3 < mRNA.length () )
    codon = mRNA.substring ( index, index + 3 );
  else
    codon = mRNA.substring ( index );

  return codon;
}  // method getCodon


/******************************************************************************/
private StringBuffer shiftIUB ( StringBuffer mRNA, int index )
{
  // Scan the preceeding codon for a preceeding n shift base.
  int n_index = -1;

  for ( int i = index - 1; i >= index - 3; i-- )

    if ( mRNA.charAt ( i ) == 'n' )  n_index = i;

  // Check if n shift base found.
  if ( n_index != -1 )
  {
    mRNA.deleteCharAt ( n_index );
    mRNA.insert ( index + 2, 'n' );
  }  // if

  return mRNA;
}  // method shiftIUB


/******************************************************************************/
// This method completes the translation template.
private StringBuffer fillGaps ( StringBuffer template, StringBuffer fst_mrna )
{
  // Traverse the template looking for translation holes.
  for ( int i = 0; i < fst_mrna.length (); i += 3 )
  {
    // Check if template is long enough.
    while ( i / 3 >= template.length () )  template.append ( ' ' );

    // Check for a translation gap.
    if ( template.charAt ( i / 3 ) == ' ' )
    {
      String codon = getCodon ( fst_mrna, i );

      // Check for the stop codon.
      if ( ( codon.length () == 3 ) && ( SeqTools.mapCodon ( codon ) == '*' ) )
      {
        fst_mrna = shiftIUB ( fst_mrna, i );
        codon = getCodon ( fst_mrna, i-3 );
        template.setCharAt ( (i-3) / 3, SeqTools.mapCodon ( codon ) );

        codon = getCodon ( fst_mrna, i );
      }  // if

      if ( codon.length () == 3 )

        template.setCharAt ( i / 3, SeqTools.mapCodon ( codon ) );
    }  // if
  }  // for

  return template;
}  // method fillGaps


/******************************************************************************/
// Find the frame of the next peptide segment in the translations.
private int findFrame ( StringBuffer peptide, int pep_index, String [] frame )
{
  String key = peptide.substring ( pep_index );
  if ( key.length () > 5 )  key = key.substring ( 0, 5 );

  // Check the tree translation frames.
  for ( int i = 0; i < 3; i++ )
  {
    if ( pep_index < frame [ i ].length () )

      if ( frame [ i ].indexOf ( key, pep_index ) >= 0 )  return i;
  }  // for

  return -1;
}  // method findFrame


/******************************************************************************/
// Find the start of the next peptide segment in the translations.
private int findStart ( StringBuffer peptide, int pep_index, String [] frame )
{
  // Assert: pep_index < peptide.length
  if ( ( pep_index < 0 ) || ( pep_index >= peptide.length () ) )  return -1;

  if ( peptide.length () < 10 )  return -1;

  String key = peptide.substring ( pep_index );
  if ( key.length () > 5 )  key = key.substring ( 0, 5 );

  // Check the tree translation frames.
  for ( int i = 0; i < 3; i++ )
  {
    int start = frame [ i ].indexOf ( key, pep_index );

    if ( start >= 0 )  return start;
  }  // for

  return -1;
}  // method findStart


/******************************************************************************/
// This method aligns the peptide sequence with the mRNA (as well as possible)
private StringBuffer realign ( StringBuffer mRNA, StringBuffer peptide )
{
  StringBuffer align = new StringBuffer ( ( mRNA.length () + 2 ) / 3 );
  align.setLength ( 0 );

  // Check for no peptide sequence to re-align.
  if ( peptide.length () <= 0 )  return align;

  // Blank the translation.
  for ( int i = 0; i < mRNA.length (); i += 3 )

    align.append ( ' ' );

  // Translate the mRNA in all three possible forward translation frames.
  String frame [] = new String [ 3 ];
  frame [ 0 ] = SeqTools.translate ( mRNA.toString () );
  frame [ 1 ] = SeqTools.translate ( mRNA.substring ( 1 ) );
  frame [ 2 ] = SeqTools.translate ( mRNA.substring ( 2 ) );

for ( int i = 0; i < 3; i++ )
{
  System.out.println ();
  System.out.println ( "Translation frame " + i );
  printSequence ( frame [ i ] );
}  // for

  // Align the peptide with the mRNA sequence.
  int pep_index = 0;
  int mrna_index = 0;
  while ( ( mrna_index < mRNA.length () ) && ( pep_index < peptide.length () ) )
  {
    // Find the next peptide segment in the translations.
    int start = findStart ( peptide, pep_index, frame );

    // Check if the start of the peptide segment was found.
    if ( start >= 0 )
    {
      // Check which of the translation frames to use.
      int fr = findFrame ( peptide, pep_index, frame );

      // Copy the peptide to the translation.
      while ( ( pep_index < peptide.length () ) &&
              ( start < frame [ fr ].length () ) &&
              ( peptide.charAt ( pep_index ) == frame [ fr ].charAt ( start ) ) )
      {
        align.setCharAt ( start, peptide.charAt ( pep_index ) );
        pep_index++;
        start++;
      }  // while
    }  // if

    pep_index++;
    mrna_index += 3;
  }  // while

  return align;
}  // method realign


/******************************************************************************/
private void printSequence ( String sequence )
{
  System.out.println ();
  for ( int i = 0; i < sequence.length (); i += 60 )
  {
    int end = i + 60;
    if ( end > sequence.length () )  end = sequence.length ();

    for ( int j = i; j < end; j++ )
      System.out.print ( sequence.charAt ( j ) );
    System.out.println ();
  }  // for
}  // method printSequence


/******************************************************************************/
// This method splices the new translation with the original translation.
private void spliceProteins ( StringBuffer template, String old_aa )
{
  String translation = template.toString ().trim ();
  String key = translation;
  if ( key.length () > 10 )  key = key.substring ( 0, 10 );

  // Check if the start of the original protein preceeds this template.
  int index = old_aa.indexOf ( key );

  if ( ( index > 0 ) && ( key.length () >= 5 ) )
  {
    translation = old_aa.substring ( 0, index ) + translation;
  }  // if

  // Check if the end of the original protein extends past this template.
  key = translation;
  if ( key.length () >= 15 )  

    key = key.substring ( key.length () - 15, key.length () - 4 );

  index = old_aa.indexOf ( key );

  if ( ( index > 0 ) && ( key.length () >= 5 ) )
  {
    // Found last segment, check if extends past key.
    if ( old_aa.length () > index + 15 )

      translation += old_aa.substring ( index + 15 );
  }  // if

  // Save the spliced protein sequence.
  new_aa.append ( translation );
}  // method spliceProteins


/******************************************************************************/
private void printSequence ( StringBuffer sequence )
{
  System.out.println ();
  for ( int i = 0; i < sequence.length (); i += 60 )
  {
    int end = i + 60;
    if ( end > sequence.length () )  end = sequence.length ();

    for ( int j = i; j < end; j++ )
      System.out.print ( sequence.charAt ( j ) );
    System.out.println ();
  }  // for
}  // method printSequence


/******************************************************************************/
// This function replace internal stop codons with 'X' residues.
private StringBuffer zapStops ( StringBuffer peptide )
{
  // Check for no peptide.
  if ( peptide.length () <= 0 )  return peptide;

  // Traverse the peptide looking for stop codons.
  for ( int i = 0; i < peptide.length () - 1; i++ )

    // Check for an internal stop codon.
    if ( peptide.charAt ( i ) == '*' )

      // Replace the translated stop codon with an 'X' residue.
      peptide.setCharAt ( i, 'X' );

  return peptide;
}  // method zapStops


/******************************************************************************/
// This function searches a frame for an in-frame Met. residue.
private int findMet ( String frame, int pep_start )
{
  int start = pep_start;

  while ( start > 0 )
  {
    if ( frame.charAt ( start ) == '*' )
    {
      int met_index = frame.indexOf ( 'M', start );

      if ( met_index <= start + 10 )  return met_index;

      return (start + 1);	// Amino acid after the stop codon
    }  // if

    start--;
  }  // while

  return start;
}  // method findMet


/******************************************************************************/
private void extendTranslation ( StringBuffer mRNA )
{
  // Assert: that a new translation exists.
  if ( new_aa.length () <= 0 )  return;

  // Assert: valid mRNA sequence.
  if ( mRNA.length () < 10 )  return;

  // Translate the mRNA in all three possible forward translation frames.
  String frame [] = new String [ 3 ];
  frame [ 0 ] = SeqTools.translate ( mRNA.toString () );
  frame [ 1 ] = SeqTools.translate ( mRNA.substring ( 1 ) );
  frame [ 2 ] = SeqTools.translate ( mRNA.substring ( 2 ) );

  int pep_index = 0;

  // Check if the first amino acid residue is an Met.
  if ( new_aa.charAt ( 0 ) != 'M' )
  {
    // Align the peptide with the mRNA sequence.
    
    // Find the next peptide segment in the translations.
    int pep_start = findStart ( new_aa, pep_index, frame );

    // Check if the start of the peptide segment was found.
    if ( pep_start > 0 )
    {
      // Check which of the translation frames to use.
      int fr = findFrame ( new_aa, pep_index, frame );

      // Find the in-frame Met. if possible.
      int start = findMet ( frame [ fr ], pep_start );

      // Check for an amino acid segment to add to the translation.
      if ( ( start >= 0 ) && ( start < pep_start ) )

        // Insert the leading segment.
        new_aa.insert ( 0, frame [ fr ].substring ( start, pep_start ) );
    }  // if
  }  // if

  // Check if the translation stops at a stop codon or end of the mRNA.
  pep_index = new_aa.length () - 15;
  if ( pep_index < 0 )  pep_index = 0;
    
  // Find the next peptide segment in the translations.
  int pep_start = findStart ( new_aa, pep_index, frame );

  // Check if the start of the peptide segment was found.
  if ( pep_start > 0 )
  {
    // Check which of the translation frames to use.
    int fr = findFrame ( new_aa, pep_index, frame );

    pep_index = new_aa.length ();

    // Copy this translation frame until a stop codon is reached.
    while ( ( pep_index < frame [ fr ].length () ) &&
            ( frame [ fr ].charAt ( pep_index ) != '*' ) )
    {
      new_aa.append ( frame [ fr ].charAt ( pep_index ) );
      pep_index++;
    }  // while

    // Add the stop codon.
    if ( ( pep_index < frame [ fr ].length () ) &&
          ( frame [ fr ].charAt ( pep_index ) == '*' ) )

      new_aa.append ( '*' );
  }  // if
}  // method extendTranslation


/******************************************************************************/
// This method adjust the mRNA sequence for frameshift errors.
private void shiftMrna ( TLAlignment tl_alignment )
{
  // Find the start of the FST alignment in the mRNA.
  int mrna_index = tl_alignment.getQueryStart () - 1;
  if ( mrna_index < 0 )  mrna_index = 0;

  int fst_mrna_index = 0;		// FST mRNA alignment index
  int fst_pep_index = 0;		// FST peptide alignment index

  // System.out.println ( "mRNA: " + old_mrna_seq.getSequence ().substring ( mrna_index, mrna_index + 12 ) );
  // System.out.println ( "FST:  " + tl_alignment.getQueryAlignment ().substring ( 0, 12 ) );

  StringBuffer mRNA = new StringBuffer ( old_mrna_seq.getSequence () );

  StringBuffer fst_mRNA = new StringBuffer ( tl_alignment.getQueryAlignment () );
  StringBuffer fst_ident = new StringBuffer ( tl_alignment.getTargetIdentities () );
  StringBuffer fst_pep3 = new StringBuffer ( tl_alignment.getTargetAlignment () );

  // Traverse the FST alignment checking for alignment gaps.
  while ( ( fst_pep_index < fst_mRNA.length () - 2 ) &&
          ( mrna_index < mRNA.length () - 2 ) )
  {
    String codon = "";
    if ( fst_mrna_index + 3 < fst_mRNA.length () )
      codon = fst_mRNA.substring ( fst_mrna_index, fst_mrna_index + 3 );
    else
      codon = fst_mRNA.substring ( fst_mrna_index );

    String amino = "";
    if ( fst_pep_index + 3 < fst_mRNA.length () )
      amino = fst_pep3.substring ( fst_pep_index, fst_pep_index + 3 );
    else
      amino = fst_pep3.substring ( fst_pep_index );

    String residue = "";
    if ( mrna_index + 3 < mRNA.length () )
      residue = mRNA.substring ( mrna_index, mrna_index + 3 );
    else
      residue = mRNA.substring ( mrna_index );

/*
System.out.println ();
System.out.println ( "Comparing: " + codon + ":" + residue + " -> " + amino );
System.out.println ( "Indices: mRNA - " + mrna_index + " FST mRNA - " + fst_mrna_index + " FST pep - " + fst_pep_index );
*/

    // Count the number of gap characters in the codon.
    int gaps = countGaps ( codon );
    if ( gaps > 0 )
    {
      if ( gaps == 1 )
      {
        // Insert mRNA base - use amino acid as template.
        System.out.println ( "codon: " + codon + ", gaps = 1; insert base" );

        mRNA.insert ( findSite ( mRNA, mrna_index ), 'n' );

        mrna_index += 3;
        fst_mrna_index += 3;
        fst_pep_index += 3;
      }  // if
      else 
        if ( gaps == 2 )
        {
          // Delete mRNA base.
          System.out.println ( "codon: " + codon + ", gaps = 2; delete base" );

          int offset = baseIndex ( amino );

          System.out.println ( "\tdeleting: mRNA - " + mRNA.charAt ( mrna_index + offset ) +
              " fst mRNA - " + fst_mRNA.charAt ( fst_mrna_index + offset ) +
              " fst pep3 - " + fst_pep3.charAt ( fst_pep_index + offset ) );

          mRNA.deleteCharAt ( mrna_index + offset );

          fst_mRNA.delete ( fst_mrna_index, fst_mrna_index + 3 );
          fst_ident.delete ( fst_pep_index, fst_pep_index + 3 );
          fst_pep3.delete ( fst_pep_index, fst_pep_index + 3 );
        }  // if
        else
        {
          fst_pep_index += 3;
          fst_mrna_index += 3;
          mrna_index += 3;
        }  // else
    }  // if
    else
    {
      // Count the number of gap characters in the amino acid codon.
      gaps = countGaps ( amino );

      if ( gaps == 1 )
      {
        // Delete mRNA base.
        System.out.println ( "amino: " + amino + ", gaps = 1; delete base" );

        int offset = amino.indexOf ( '-' );

        System.out.println ( "\tdeleting: mRNA - " + mRNA.charAt ( mrna_index + offset ) +
            " fst mRNA - " + fst_mRNA.charAt ( fst_mrna_index + offset ) +
            " fst pep3 - " + fst_pep3.charAt ( fst_pep_index + offset ) );

        fst_mRNA.deleteCharAt ( fst_mrna_index + offset );
        fst_ident.deleteCharAt ( fst_pep_index + offset ); 
        fst_pep3.deleteCharAt ( fst_pep_index + offset ); 
        mRNA.deleteCharAt ( mrna_index + offset );
      }  // if
      else 
        if ( gaps == 2 )
        {
          // Insert mRNA base - (1) not stop codon, (2) unknown amino acid.
          System.out.println ( "amino: " + amino + ", gaps = 2; insert base" );

          mRNA.insert ( findSite ( mRNA, mrna_index ), 'n' );
          int site = findSite ( fst_mRNA, fst_mrna_index );
          fst_mRNA.insert ( site, 'n' );
          fst_ident.insert ( site, 'n' );
          fst_pep3.insert ( site, 'n' );

          fst_pep_index += 3;
          fst_mrna_index += 3;
          mrna_index += 3;
        }  // if
        else
        {
          fst_pep_index += 3;
          fst_mrna_index += 3;
          mrna_index += 3;
        }  // else
    }  // else
  }  // while


  // Print out the alignment.
  System.out.println ();
  mrna_index = tl_alignment.getQueryStart () - 1;
  for ( int i = 0; i < fst_mRNA.length (); i += 60 )
  {
    int end = i + 60;
    if ( end > fst_mRNA.length () )  end = fst_mRNA.length ();

    for ( int j = i; ( j < end ) && ( mrna_index + j < mRNA.length () ); j++ )
      System.out.print ( mRNA.charAt ( mrna_index + j ) );
    System.out.println ();

    for ( int j = i; j < end; j++ )
      System.out.print ( fst_mRNA.charAt ( j ) );
    System.out.println ();

    for ( int j = i; ( j < end ) && ( j < fst_ident.length () ); j++ )
      System.out.print ( fst_ident.charAt ( j ) );
    System.out.println ();

    for ( int j = i; j < end; j++ )
      System.out.print ( fst_pep3.charAt ( j ) );
    System.out.println ();

    System.out.println ();
  }  // for


  // Build the translation template.
  StringBuffer template = translationTemplate ( fst_ident );

  // Fill in the holes in the translation template.
  template = fillGaps ( template, fst_mRNA );

  System.out.println ();
  System.out.println ( "Best guess translation using public sequence" );
  printSequence ( template );

  System.out.println ();
  System.out.println ( "Original translation sequence" );
  String old_aa = old_aa_seq.getSequence ();
  printSequence ( old_aa );

  // Align the old peptide sequence to the new mRNA sequence.
  // StringBuffer old_align = realign ( mRNA, old_aa );
/*
  System.out.println ();
  System.out.println ( "Best realignment of the previous translation" );
  printSequence ( old_align );
*/

  // Splice the translations together.
  // new_aa.append ( template.toString ().trim () );		// ####!!!!
  spliceProteins ( template, old_aa );		// ####!!!!

  // Extend the translation (if not complete yet).
  extendTranslation ( mRNA );

  // Replace internal stop codons with 'X' residues.
  new_aa = zapStops ( new_aa );

  System.out.println ();
  System.out.println ( "Final translation" );
  printSequence ( new_aa );

  // Save the adjusted mRNA sequence.

  System.out.println ();
}  // method shiftMrna


/******************************************************************************/
public void process 
    ( TLAlignment  tl_alignment
    , TLQuery      tl_query
    , TLHeader     tl_header 
    )
{
  // System.out.println ( "Shift.process: qtext: " + tl_query.getQtext () );

  // Reset object.
  initialize ();

  // Read in the original mRNA and peptide sequence files.
  readSeqFiles ( tl_query.getQtext () );

  // Adjust mRNA for frameshift errors.
  shiftMrna ( tl_alignment );

  // Write out the new mRNA and peptide sequence files.
  writeSeqFiles ();

  // Close the original mRNA and peptide sequence files.
  closeSeqFiles ();
}  // method process


/******************************************************************************/
}  // class Shift
