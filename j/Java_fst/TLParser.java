
import java.io.*;
// import java.sql.*;

import Alignment;
import TLQuery;
import TLHeader;
// import TLSlice;
import InputTools;
import SeqTools;
import Shift;

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

public class TLParser extends Object
{

/******************************************************************************/

private  InputTools tl_file = new InputTools ();	// TL input file

private  String line = null; 				// Current line of the file


// TL file objects.
private  TLHeader tl_header = new TLHeader ();


private  StringBuffer header = new StringBuffer ( 1024 );	// Individual header section

// Query alignment sequence
private  StringBuffer query_alignment = new StringBuffer ( 4096 );

// Identity characters.
private  StringBuffer target_identities = new StringBuffer ( 4096 );

// Target alignment sequence
private  StringBuffer target_alignment = new StringBuffer ( 4096 );

private  StringBuffer section = new StringBuffer ( 1024 );	// Individual header section

private  TLQuery tl_query = new TLQuery ();			// Query sequence & alignments

private  Shift shift = new Shift ();				// Frameshift processor


/******************************************************************************/
public TLParser ()
{
  initialize ();
}  // constructor TLParser


/******************************************************************************/
private void initialize ()
{
  line = null;

}  // method initialize


/******************************************************************************/
  private void parseHeader ()
  {
    header.setLength ( 0 );
    tl_header.initialize ();

    String header_type = "";			// current line type
    String previous_type = "";			// previous line type

    // Get the first line of the input file.
    line = null;
    line = tl_file.getLine ().toString ();

    while ( ( tl_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "[QUERY SEQUENCE]" ) != true ) &&
            ( line.startsWith ( "[SCORES]" ) != true ) &&
            ( line.startsWith ( "[BEGIN ALIGNMENTS TIME]" ) != true ) &&
            ( line.startsWith ( "[ALIGNMENTS]" ) != true ) )
    {
      if ( line.length () > 0 )
      {
        // Check for a continuation of the previous line.
        if ( line.charAt ( 0 ) != '[' )
        { 
          // Check for a current header line.
          if ( header.length () > 0 )

            // Check if the current line is not blank.
            if ( line.trim ().length () > 0 )

              // Insert a space between text on different lines.
              header.append ( " " );

          header.append ( line.trim () );
        }  // if
        else  // not continuation of previous header line
        {
          // Check for previous header.
          if ( previous_type.length () > 0 )
            tl_header.setSection ( previous_type, header.toString () );

          // Extract the header type of the current header.
          header.setLength ( 0 );
          header_type = "";
          int index = line.indexOf ( "]" );
          if ( index > 0 )
          {
            header_type = line.substring ( 1, index );
            if ( line.length () > index + 1 )
              header.append ( line.substring ( index + 1 ).trim () );
          }  // if

          previous_type = header_type;
        }  // else
      }  // if

      line = null;
      line = tl_file.getLine ().toString ();
    }  // while

    // Check for previous header.
    if ( header.length () > 0 )
      tl_header.setSection ( previous_type, header.toString () );
  }  // method parseHeader


/******************************************************************************/
  private void copyRank ( TLAlignment old, TLAlignment new_align )
  {
    new_align.setRank          ( old.getRank () );
    new_align.setScore         ( old.getScore () );
    new_align.setPScore        ( old.getPScore () );
    new_align.setQueryName     ( old.getQueryName () );
    new_align.setQueryFrame    ( old.getQueryFrame () );
    new_align.setQuerySymbols  ( old.getQuerySymbols () );
    new_align.setTargetName    ( old.getTargetName () );
    new_align.setTargetFrame   ( old.getTargetFrame () );
    new_align.setTargetSymbols ( old.getTargetSymbols () );
    new_align.setDescription   ( old.getDescription () );
  }  // method copyRank


/******************************************************************************/
  private TLAlignment parseRank 
      ( TLQuery tl_query
      , TLAlignment previous_alignment 
      )
  {
    int index1 = 0;				// line index 1
    int index2 = 0;				// line index 2
    int rank_count = 0;				// count of RANK lines

    TLAlignment tl_alignment = new TLAlignment ();
    tl_alignment.initialize ();

    query_alignment.setLength ( 0 );
    target_identities.setLength ( 0 );
    target_alignment.setLength ( 0 );

    while ( ( tl_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "_____________________" ) != true ) &&
            ( line.startsWith ( "[END" ) != true ))
    {
      // Check for the Domain line.
      if ( line.startsWith ( "Domain" ) == true )
      {
        // Check if an alignment was found.
        if ( query_alignment.length () > 0 )
        {
          tl_alignment.setQueryAlignment ( query_alignment.toString () );
          tl_alignment.setTargetIdentities ( target_identities.toString () );
          tl_alignment.setTargetAlignment ( target_alignment.toString () );
          return tl_alignment;
        }  // if

        if ( previous_alignment != null )

          // Copy the RANK header information.
          copyRank ( previous_alignment, tl_alignment );
      }  // if

      // Check for the RANK line.
      if ( line.startsWith ( "RANK" ) == true )
      {
        rank_count++;

        tl_alignment.setRank ( InputTools.getInteger ( line.substring ( 4, 9 ) ) );

        if ( rank_count > 1 )
        {
          // Check if an alignment was found.
          if ( query_alignment.length () > 0 )
          {
            tl_alignment.setQueryAlignment ( query_alignment.toString () );
            tl_alignment.setTargetIdentities ( target_identities.toString () );
            tl_alignment.setTargetAlignment ( target_alignment.toString () );
          }  // if

          return tl_alignment;
        }  // if

        index1 = line.indexOf ( "Score =" );
        index2 = line.indexOf ( "P_Score = " );

        // Check for "Score =".
        if ( index1 > 0 )
        {
          if ( index2 > 0 )
            tl_alignment.setScore ( InputTools.substring ( line, index1+8, index2 ) );
        }  // if

        // Check for "P_Score ="
        if ( index2 > 0 )
          tl_alignment.setPScore ( InputTools.substring ( line, index2+10, line.length () ) );
      }  // if

      // Check for the Q = line.
      else if ( line.startsWith ( " Q = " ) == true )
           {
             if ( line.length () > 5 )
               tl_alignment.setQueryName ( line.substring ( 5 ) );
           }  // if

      // Check for the QF = line.
      else if ( line.startsWith ( " QF = " ) == true )
           {
             index1 = line.indexOf ( "#Q Symbols =" );

             if ( index1 > 0 )
             {
               tl_alignment.setQueryFrame ( InputTools.substring ( line, 6, index1 ) );
               tl_alignment.setQuerySymbols ( InputTools.getInteger ( line.substring ( index1+13 ) ) );
             }  // if
           }  // if

      // Check for the "T =" line.
      else if ( line.startsWith ( " T = " ) == true )
           {
             if ( line.length () > 5 )
               tl_alignment.setTargetName ( line.substring ( 5 ) );
           }  // if

      // Check for the "TF =" line.
      else if ( line.startsWith ( " TF = " ) == true )
           {
             index1 = line.indexOf ( "#T Symbols =" );

             if ( index1 > 0 )
             {
               tl_alignment.setTargetFrame ( InputTools.substring ( line, 6, index1 ) );
               tl_alignment.setTargetSymbols ( InputTools.getInteger ( line.substring ( index1+13 ) ) );
             }  // if
           }  // if

      // Check for the "D =" line.
      else if ( line.startsWith ( " D = " ) == true )
           {
             if ( line.length () > 5 )
               tl_alignment.setDescription ( line.substring ( 5 ) );
           }  // if

      // Check for the "Identical Match" line.
      else if ( line.startsWith ( " Identical Match" ) == true )
           {
             tl_alignment.setIdenticalMatch ( InputTools.getInteger ( line.substring ( 19 ) ) );

             index1 = line.indexOf ( "Similar =" );
             if ( index1 > 0 )
               tl_alignment.setSimilar ( InputTools.getInteger ( line.substring ( index1+10 ) ) );

             index2 = line.indexOf ( "Total # Of Gaps =" );
             if ( index2 > 0 )
               tl_alignment.setTotalGaps ( InputTools.getInteger ( line.substring ( index2+18 ) ) );
           }  // if

      // Check for the "Identity: Alignment" line.
      else if ( line.startsWith ( " Identity: Alignment" ) == true )
           {
             tl_alignment.setIdentityAlignment ( InputTools.getInteger ( line.substring ( 23 ) ) );

             index1 = line.indexOf ( "Query" );
             if ( index1 > 0 )
               tl_alignment.setIdentityQuery ( InputTools.getInteger ( line.substring ( index1+8 ) ) );

             index2 = line.indexOf ( "Target" );
             if ( index2 > 0 )
               tl_alignment.setIdentityTarget ( InputTools.getInteger ( line.substring ( index2+9 ) ) );
           }  // if

      // Check for the "Similarity:" line.
      else if ( line.startsWith ( " Similarity:" ) == true )
           {
             tl_alignment.setSimilarityAlignment ( InputTools.getInteger ( line.substring ( 25 ) ) );

             index1 = line.indexOf ( "Query" );
             if ( index1 > 0 )
               tl_alignment.setSimilarityQuery ( InputTools.getInteger ( line.substring ( index1+8 ) ) );

             index2 = line.indexOf ( "Target" );
             if ( index2 > 0 )
               tl_alignment.setSimilarityTarget ( InputTools.getInteger ( line.substring ( index2+9 ) ) );
           }  // if

      // Check for the "QS =" line.
      else if ( line.startsWith ( " QS = " ) == true )
           {
             tl_alignment.setQueryStart ( InputTools.getInteger ( line.substring ( 6 ) ) );

             index1 = line.indexOf ( "QE =" );
             if ( index1 > 0 )
               tl_alignment.setQueryEnd ( InputTools.getInteger ( line.substring ( index1+5 ) ) );

             index2 = line.indexOf ( "TS =" );
             if ( index2 > 0 )
               tl_alignment.setTargetStart ( InputTools.getInteger ( line.substring ( index2+5 ) ) );

             index1 = line.indexOf ( "TE =" );
             if ( index1 > 0 )
               tl_alignment.setTargetEnd ( InputTools.getInteger ( line.substring ( index1+5 ) ) );
           }  // if

      // Check for an error message.
      else if ( line.startsWith ( " Error =" ) == true )
      {
        tl_alignment.setError ( line );
      }  // else

      // Check for an alignment segment.
      else if ( line.startsWith ( "Q " ) == true )
           {
             query_alignment.append ( line.substring ( 10 ) );

             // Get the Identities line.
             line = null;
             line = tl_file.getLine ().toString ();
             target_identities.append ( line.substring ( 10 ) );

             // Get the Target line.
             line = null;
             line = tl_file.getLine ().toString ();

             // Check for the target line.
             if ( line.startsWith ( "T " ) == true )

               target_alignment.append ( line.substring ( 10 ) );
           }  // if

      line = null;
      line = tl_file.getLine ().toString ();
    }  // while

    // Check if an alignment was found.
    if ( query_alignment.length () > 0 )
    {
      tl_alignment.setQueryAlignment ( query_alignment.toString () );
      tl_alignment.setTargetIdentities ( target_identities.toString () );
      tl_alignment.setTargetAlignment ( target_alignment.toString () );
    }  // if

    return tl_alignment;
  }  // method parseRank


/******************************************************************************/
  private void parseAlignments ( TLQuery tl_query )
  {
    TLAlignment previous_alignment = null;	// Previous alignment

    while ( ( tl_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "[END" ) != true ) )
    {
      // Check for a new alignment.
      if ( line.startsWith ( "RANK" ) == true )
      {
        TLAlignment tl_alignment = parseRank ( tl_query, previous_alignment );
        previous_alignment = tl_alignment;
        tl_alignment.retotal ();

        // Check for query name within the target name.
        if ( tl_alignment.getTargetName ().indexOf 
                 ( tl_alignment.getQueryName () ) > 0 )
        {
          tl_alignment.initialize ();
          tl_alignment = null;
        }  // if

        // Determine the alignment type.
        else if ( ( tl_header.getTargetType ().startsWith ( "AA" ) == true ) ||
                  ( tl_header.getQueryType ().startsWith ( "AA" ) == true ) )
        {
          if ( tl_header.getAlgorithm ().equals ( "FST" ) == true )
          {
            if ( tl_alignment.getRank () == 1 )
            { 
              // tl_alignment.printAlignment ();

              // Process the frameshift alignment.
              shift.process ( tl_alignment, tl_query, tl_header );
            }  // if
          }  // if FST

          // Check for low amino acid complexity.
          else if ( SeqTools.isLowAAComplexity2 ( tl_alignment.getQueryAlignment (), 
                   tl_alignment.getTargetAlignment () ) == false )
          {
              // Check for a Pfam alignment.
              if ( tl_header.getAlgorithm ().equals ( "HMM" ) == true )
              {
                // Check if above thresholds.
                if ( ( tl_alignment.getIdentityAlignment () > Alignment.MIN_PFAM_AA_PERCENT ) &&
                     ( tl_alignment.getIdenticalMatch () > Alignment.MIN_PFAM_AA_IDENTITIES ) &&
                     ( ( ( tl_alignment.getTargetEnd () - tl_alignment.getTargetStart () + 1 ) * 100 ) /
                           tl_alignment.getTargetSymbols () > Alignment.MIN_PFAM_REGION_PERCENT ) )
                {
                  // tl_query.addAlignment ( tl_alignment );

                  // loader_motif.loadMotif ( tl_alignment );
                }
                else
                {
                  tl_alignment.initialize ();
                  tl_alignment = null;
                }  // else
              }  // if
              else
              {
                // Check if above thresholds.
                if ( ( tl_alignment.getIdentityAlignment () > Alignment.MIN_AA_PERCENT ) &&
                     ( tl_alignment.getIdenticalMatch () > Alignment.MIN_AA_IDENTITIES ) )

                  tl_query.addAlignment ( tl_alignment );

                else
                {
                  tl_alignment.initialize ();
                  tl_alignment = null;
                }  // else
              }  // else
          }  // if
          else
          {
            tl_alignment.initialize ();
            tl_alignment = null;
          }  // else
        }  // if
        else  // nucleotide alignment
        {
          // Check if above thresholds.
          if ( ( tl_alignment.getIdentityAlignment () > Alignment.MIN_DNA_PERCENT ) &&
               ( tl_alignment.getIdenticalMatch () > Alignment.MIN_DNA_IDENTITIES ) )

            tl_query.addAlignment ( tl_alignment );

          else
          {
            tl_alignment.initialize ();
            tl_alignment = null;
          }  // else
        }  // else
      }  // if

      // Check for a second RANK line.
      if ( ( line.startsWith ( "RANK" ) != true ) &&
           ( line.startsWith ( "[END" ) != true ) )
      {
        line = null;
        line = tl_file.getLine ().toString ();
      }  // if
    }  // while
  }  // method parseAlignments


/******************************************************************************/
  private void parseQuery ()
  {
    tl_query.initialize ();

    section.setLength ( 0 );

    String section_type = "";			// current line type
    String previous_type = "";			// previous line type

    while ( ( tl_file.isEndOfFile () != true ) &&
            ( line.startsWith ( "[ALIGNMENTS]" ) != true ) &&
            ( line.startsWith ( "[END ALIGNMENTS TIME]" ) != true ) )
    {
      if ( line.length () > 0 )
      {
        // Check for a continuation of the previous line.
        if ( line.charAt ( 0 ) != '[' )
        { 
          // Check for a current section line.
          if ( section.length () > 0 )

            // Check if the current line is not blank.
            if ( line.trim ().length () > 0 ) 
            {
              // Check for [SCORES] section.
              if ( previous_type.equals ( "SCORES" ) == true )
                section.append ( "\n" );
              else
                 // Avoid inserting spaces into sequence data.
                 if ( previous_type.equals ( "QUERY DATA" ) != true ) 

                  // Insert a space between text on different lines.
                  section.append ( " " );
            }  // if

          if ( previous_type.equals ( "SCORES" ) == true )
            section.append ( line );
          else
            section.append ( line.trim () );
        }  // if
        else  // not continuation of previous section line
        {
          // Check for previous section.
          if ( previous_type.length () > 0 )
            tl_query.setSection ( previous_type, section.toString () );

          // Extract the section type of the current section.
          section.setLength ( 0 );
          section_type = "";
          int index = line.indexOf ( "]" );
          if ( index > 0 )
          {
            section_type = line.substring ( 1, index );
            if ( line.length () > index + 1 )
              section.append ( line.substring ( index + 1 ).trim () );
          }  // if

          previous_type = section_type;
        }  // else
      }  // if

      line = null;
      line = tl_file.getLine ().toString ();
    }  // while

    // Check for previous section.
    if ( section.length () > 0 )
      tl_query.setSection ( previous_type, section.toString () );

    if ( line.startsWith ( "[ALIGNMENTS]" ) == true ) 
      parseAlignments ( tl_query );

    if ( line.startsWith ( "[END AL" ) == true )
      line = tl_file.getLine ().toString ();
  }  // method parseQuery


/******************************************************************************/
  public void processFile ( String file_name )
  {
    // Set the input file name.
    tl_file.initialize ();
    tl_file.setFileName ( file_name );

    // Open the input file.
    tl_file.openFile ();

    // Process the TimeLogic file header.
    parseHeader ();

    // Process the input file.
    while ( tl_file.isEndOfFile () != true )
    {
       // Parse the current query sequence & alignments.
       parseQuery ();

      // Extract domains from the entry.
      // slice.processEntry ( tl_header, features, sequence.toString () );
    }  // while

    // Close input file.
    tl_file.closeFile();
  }  // method processFile


/******************************************************************************/
public void processFiles ( String filelist_name )
{
    // Setup TL entry processor.
    // TLSlice slice = new TLSlice ();
    // slice.setName ( tl_file_name );

  StringBuffer name_line = new StringBuffer ( 80 );	// Current line of the file

  // Get the file name of the list of output files
  InputTools name_list = new InputTools ();
  name_list.setFileName ( filelist_name );

  name_list.openFile ();

  // Process the list of Fgenesh output files.
  while ( name_list.isEndOfFile () == false )
  {
    // Read the next line from the list of names file.
    name_line = name_list.getLine ();

    if ( name_list.isEndOfFile () == false )
    {
      String name = name_line.toString () .trim ();

      System.out.println ( "Processing: " + name );

      // Process the file.
      processFile ( name );
    }  // if
  }  // while

  name_list.closeFile ();
  name_list = null;

  // loader_motif.close ();
  // loader_motif = null;

  // Close the output files.
  // slice.close ();
  // slice = null;
}  // method processFiles



/******************************************************************************/
  private void usage ()
  {
    System.out.println ( "The command line syntax for this program is:" );
    System.out.println ();
    System.out.println ( "java TLParser <TL_filename_list>" );
    System.out.println ();
    System.out.print   ( "where <TL_filename_list> is the file name of a " );
    System.out.println ( "list of TimeLogic output file names." );
  }  // method usage


/******************************************************************************/
  public static void main ( String [] args )
  {
    TLParser app = new TLParser ();

    if ( args.length != 1 )
      app.usage ();
    else
      app.processFiles ( args [ 0 ] );
  }  // method main

}  // class TLParser

