
import java.io.*;
import java.lang.Math.*;
import java.util.Vector;

// import InputTools;
// import Sequence;
// import SequenceAlignment;

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

public class MSF extends Object
{

/******************************************************************************/

private SequenceAlignment alignments [] = null;		// sequence alignments

private InputTools msf_file = new InputTools ();	// GCG .msf input file


/******************************************************************************/
public MSF ()
{
  initialize ();
}  // constructor MSF


/******************************************************************************/
private void initialize ()
{
  msf_file.initialize ();
}  // method initialize


/******************************************************************************/
private double averageV ()
{
  double V_sum = 0.0;

  // Sum up all estimates for V.
  for ( int i = 0; i < alignments.length; i++ )

    V_sum += alignments [ i ].estimateV ();

  return V_sum / (alignments.length * 1.0);
}  // method averageV


/******************************************************************************/
private Vector readNames ()
{
  String line = "";
  Vector names = new Vector ();

  do
  {
    line = msf_file.getLine ().toString ();

    // Check for a name line.
    if ( line.indexOf ( " Name:" ) == 0 )
    {
      int index = line.indexOf ( "Len:" );
      if ( index == -1 )  index = line.length () - 1;
      String name = line.substring ( 7, index ).trim ();

      names.add ( name );
    }  // if
  }
  while ( ( line.indexOf ( "//" ) != 0 ) ||
          ( msf_file.isEndOfFile () == true ) );

  return names;
}  // method readNames


/******************************************************************************/
private int findName ( String name, String [] names )
{
  // Check for no name.
  if ( name.length () <= 0 )  return -1;

  // System.out.println ( "Looking for '" + name + "'" );

  for ( int i = 0; i < names.length; i++ )
  {
    if ( names [ i ].equals ( name ) == true )  return i;
  }  // for

  return -1;
}  // method findName


/******************************************************************************/
private StringBuffer extractSequence ( String line )
{
  StringBuffer seq_data = new StringBuffer ( 80 );

  // Validate line.
  if ( line.length () < 13 )  return seq_data;

  // Copy the sequence data but not the spaces.
  for ( int i = 12; i < line.length (); i++ )
  {
    if ( line.charAt ( i ) != ' ' )

      seq_data.append ( line.charAt ( i ) );
  }  // for

  return seq_data;
}  // method extractSequence


/******************************************************************************/
private void readAlignments ( Vector names )
{
  StringBuffer temp_seqs [] = new StringBuffer [ names.size () ];
  String name_tags [] = new String [ names.size () ];

  for ( int i = 0; i < names.size (); i++ )
  {
    temp_seqs [ i ] = new StringBuffer ( 500 );
    name_tags [ i ] = (String) names.elementAt ( i );

    // Truncate long names to match the name tags.
    if ( name_tags [ i ].length () > 10 )

      name_tags [ i ] = name_tags [ i ].substring ( 0, 11 ).trim ();

    // System.out.println ( "tag " + i + " = '" + name_tags [ i ] + "'" );
  }  // for

  // Read in the alignments.
  String line;
  do
  {
    line = msf_file.getLine ().toString ();

    // Check for a line long enough to hold sequence data.
    if ( line.length () > 12 )
    {
      String name = line.substring ( 0, 10 ).trim ();

      int name_index = findName ( name, name_tags );
      if ( name_index != -1 )
      {
        temp_seqs [ name_index ].append ( extractSequence ( line ) );
      }  // if
    }  // if
  }  
  while ( msf_file.isEndOfFile () == false );

/*
  for ( int i = 0; i < names.size (); i++ )
  {
    System.out.print ( (String) names.elementAt ( i ) + "  " );
    System.out.println ( temp_seqs [ i ].toString () );
  }  // for
*/

  msf_file.closeFile ();

  // Set up the alignments.
  for ( int i = 0; i < names.size (); i++ )
  {
    alignments [ i ].setSequenceName ( (String) names.elementAt ( i ) );
    alignments [ i ].setAlignment ( temp_seqs [ i ].toString () );
  }  // for
}  // method readAlignments


/******************************************************************************/
private void compareSequences ()
{
  for ( int i = 0; i < alignments.length; i++ )
    System.out.print ( "\t" + alignments [ i ].getSequenceName () );
  System.out.println ();

  double mean_total = 0;
  int mean_count = 0;

  for ( int i = 0; i < alignments.length; i++ )
  {
    double best_percent = 0.0;
    int best_index = -1;

    System.out.print ( alignments [ i ].getSequenceName () );

    for ( int j = 0; j < alignments.length; j++ ) 

      // Don't compare a sequence to itself.
      if ( i != j )
      {
        double percent = alignments [ i ].compareAligned ( alignments [ j ].getAlignment () );

        System.out.print ( "\t" + ( (int) percent) + "%" );

        if ( ( j < i ) && ( percent > best_percent ) )
        {
          best_percent = percent;
          best_index = j;
        }  // if

        // Compute the mean total.
        if ( i < j )
        {
          mean_total += percent;
          mean_count++;
        }  // if
      }  // if
      else
        System.out.print ( "\t100%" );

    System.out.println ();

    alignments [ i ].setBestIndex ( best_index );
    alignments [ i ].setBestPercent ( best_percent );
  }  // for

  System.out.println ();

  int mean = 0;
  if ( mean_count > 0 )
    mean = ( (int) mean_total) / mean_count;
  System.out.println ( "Mean percent = " + mean + "%" );

  System.out.println ();

  // Print out the conservation levels.
  for ( int i = 0; i < alignments.length; i++ )
  {
    // SequenceAlignment align1 = (SequenceAlignment) alignments.elementAt ( i );

    int best_index = alignments [ i ].getBestIndex ();
    System.out.print ( alignments [ i ].getSequenceName () + "\t" );
    if ( best_index >= 0 )
    {
      System.out.print ( alignments [ best_index ].getSequenceName () + "\t" );
      System.out.print ( ( (int) alignments [ i ].getBestPercent () ) + "%" );

      double V = averageV ();

      // q^N = [ I/R - (1-V) ] / V
      double q_N = ( ( alignments [ i ].getBestPercent () / 100.0 ) - ( 1.0 - V ) ) / V;

      System.out.print ( " V = " + InputTools.formatDouble ( V, 3 ) );

      if ( q_N > 1.0 )  q_N = 1.0;
      System.out.print ( " q^N = " + InputTools.formatDouble ( q_N, 3 ) );

      double N = java.lang.Math.log ( q_N ) / java.lang.Math.log ( 0.998 );
      if ( q_N == 1.0 )  N = 0.0;
      int iN = (int) N;
      System.out.print ( " N = " + iN );

      int R = alignments [ i ].getSequenceLength ();
      int M = R - (int) ( (alignments [ i ].getBestPercent () * R) / 100.0 );
      System.out.print ( "\t M = " + M );

      q_N = ( V * R - M ) / ( V * R );
      if ( q_N > 1.0 )  q_N = 1.0;
      System.out.print ( " q^N = " + InputTools.formatDouble ( q_N, 3 ) );

      N = java.lang.Math.log ( q_N ) / java.lang.Math.log ( 0.998 );
      if ( q_N == 1.0 )  N = 0.0;
      iN = (int) N;
      System.out.print ( " N = " + iN );

    }  // if

    System.out.println ();
    // alignments [ i ].printConservedLevels ();
  }  // for
}  // method compareSequences


/******************************************************************************/
private String getSpecies ( String swiss_name )
{
  String name = "";

  int index = swiss_name.indexOf ( "_" );
  if ( index > 0 )

    return swiss_name.substring ( index+1 );

  return name;
}  // method getSpecies


/******************************************************************************/
private String getGeneName ( String swiss_name )
{
  String name = "";

  int index = swiss_name.indexOf ( "_" );
  if ( index > 0 )

    return swiss_name.substring ( 0, index );

  return name;
}  // method getGeneName


/******************************************************************************/
private void writeComparisons ( String msf_name )
{
  int index = msf_name.indexOf ( ".msf" );
  String q_N_name = msf_name;
  if ( index > 0 )  q_N_name = msf_name.substring ( 0, index );
  q_N_name += ".q_N";

  OutputTools q_N_file = new OutputTools ();
  q_N_file.setFileName ( q_N_name );
  q_N_file.openFile ();

  double V = averageV ();

  for ( int i = 1; i < alignments.length; i++ )
  {
    // alignments [ i ].clearConserved ();

    for ( int j = 0; j < i; j++ )
      if ( i != j )
      {
        String species1 = getSpecies ( alignments [ i ].getSequenceName () );
        String species2 = getSpecies ( alignments [ j ].getSequenceName () );

        if ( species1.compareTo ( species2 ) <= 0 )
        {
          q_N_file.print ( species1 + "\t" );
          q_N_file.print ( species2 + "\t" );
        }  // if
        else
        {
          q_N_file.print ( species2 + "\t" );
          q_N_file.print ( species1 + "\t" );
        }  // else

        q_N_file.print ( getGeneName ( alignments [ i ].getSequenceName () ) + "\t" );
        q_N_file.print ( InputTools.formatDouble ( V, 3 ) + "\t" );

        // q^N = [ I/R - (1-V) ] / V
        double percent = alignments [ i ].compareAligned ( alignments [ j ].getAlignment () );
        double q_N = ( ( percent / 100.0 ) - ( 1.0 - V ) ) / V;
        if ( q_N > 1.0 )  q_N = 1.0;
        q_N_file.print ( InputTools.formatDouble ( q_N, 3 ) + "\t" );

        double N = java.lang.Math.log ( q_N ) / java.lang.Math.log ( 0.998 );
        if ( q_N == 1.0 )  N = 0.0;
        int iN = (int) N;
        q_N_file.println ( iN + "" );
      }  // if
  }  // for

  q_N_file.closeFile (); 
}  // method writeComparisons


/******************************************************************************/
public void processFile ( String msf_name )
{
  StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

  // Open the GCG .msf file.
  msf_file.setFileName ( msf_name );
  msf_file.openFile ();

  // Read in the sequence names.
  Vector names = readNames ();

  alignments = new SequenceAlignment [ names.size () ];
  for ( int i = 0; i < names.size (); i++ )
  {
    alignments [ i ] = new SequenceAlignment ();
    alignments [ i ].setSequenceName ( (String) names.elementAt ( i ) );
  }  // for

  // Read in the alignments.
  readAlignments ( names );

  msf_file.closeFile ();
  msf_file = null;

  // Compare the sequences.
  compareSequences ();

  // Write out the comparisons.
  writeComparisons ( msf_name );

}  // method processFile



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java MSF <GCG .msf file>" );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    MSF app = new MSF ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFile ( args [ 0 ] );
  }  // method main

}  // class MSF

