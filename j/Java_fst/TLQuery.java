
import java.util.Vector;

import InputTools;

import TLAlignment;

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

public class TLQuery extends Object
{


/******************************************************************************/

  private   Vector  alignments = new Vector ();		// [ALIGNMENTS] {top N alignments}

  private   String  begin_alignments_time = "";		// [BEGIN ALIGNMENTS TIME] date time

  private   String  end_alignments_time = "";		// [END ALIGNMENTS TIME] date time

  private   String  extend_penalty = "";		// [EXTEND PENALTY] n.n

  private   String  matrix = "";			// [MATRIX] 

  private   String  maximal_score = "";			// [MAXIMAL SCORE]

  private   String  maximal_scores = "";		// [MAXIMAL SCORES]

  private   String  open_penalty = "";			// [OPEN PENALTY] n.n

  private   String  qtext = "";				// [QTEXT] querytext

  private   String  query_data = "";			// [QUERY DATA] sequence

  private   String  query_locus = "";			// [QUERY LOCUS] sequencename

  private   String  query_sequence = "";		// [QUERY SEQUENCE] pathname

  private   int     scale_factor = 0;			// [SCALE FACTOR] n

  private   String  search_type = "";			// [SEARCH TYPE] NT (NUCLEOTIDE), AA (AMINO ACID)

  private   String  scores = "";			// [SCORES] {top N scores}

  private   static int count = 1;


/******************************************************************************/
  // Constructor TLQuery
  public TLQuery ()
  {
    // System.out.println ( "new TLQuery " + count );
    count++;

    initialize ();
  }  // constructor TLQuery


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    alignments.removeAllElements ();
    begin_alignments_time = "";
    end_alignments_time = "";
    extend_penalty = "";
    matrix = "";
    maximal_score = "";
    maximal_scores = "";
    open_penalty = "";
    qtext = "";
    query_data = "";
    query_locus = "";
    query_sequence = "";
    scale_factor = 0;
    search_type = "";
    scores = "";
  }  // method initialize 


/******************************************************************************/
  public Vector getAlignments ()
  {
    return alignments;
  }  // method getAlignments


/******************************************************************************/
  public String getBeginAlignmentsTime ()
  {
    return begin_alignments_time;
  }  // method getBeginAlignmentsTime


/******************************************************************************/
  public String getEndAlignmentsTime ()
  {
    return end_alignments_time;
  }  // method getEndAlignmentsTime


/******************************************************************************/
  public String getExtendPenalty ()
  {
    return extend_penalty;
  }  // method getExtendPenalty


/******************************************************************************/
  public String getMatrix ()
  {
    return matrix;
  }  // method getMatrix


/******************************************************************************/
  public String getMaximalScore ()
  {
    return maximal_score;
  }  // method getMaximalScore


/******************************************************************************/
  public String getMaximalScores ()
  {
    return maximal_scores;
  }  // method getMaximalScores


/******************************************************************************/
  public String getOpenPenalty ()
  {
    return open_penalty;
  }  // method getOpenPenalty


/******************************************************************************/
  public String getQtext ()
  {
    return qtext;
  }  // method getQtext


/******************************************************************************/
  public String getQueryData ()
  {
    return query_data;
  }  // method getQueryData


/******************************************************************************/
  public String getQueryLocus ()
  {
    return query_locus;
  }  // method getQueryLocus


/******************************************************************************/
  public String getQuerySequence ()
  {
    return query_sequence;
  }  // method getQuerySequence


/******************************************************************************/
  public int getScaleFactor ()
  {
    return scale_factor;
  }  // method getScaleFactor


/******************************************************************************/
  public String getSearchType ()
  {
    return search_type;
  }  // method getSearchType


/******************************************************************************/
  public String getScores ()
  {
    return scores;
  }  // method getScores


/******************************************************************************/
  public void addAlignment ( TLAlignment tl_alignment )
  {
    alignments.add ( tl_alignment );

    tl_alignment.printAlignment ();
  }  // method setAlignments


/******************************************************************************/
  public void setBeginAlignmentsTime ( String value )
  {
    begin_alignments_time = value;
  }  // method setBeginAlignmentsTime


/******************************************************************************/
  public void setEndAlignmentsTime ( String value )
  {
    end_alignments_time = value;
  }  // method setEndAlignmentsTime


/******************************************************************************/
  public void setExtendPenalty ( String value )
  {
    extend_penalty = value;
  }  // method setExtendPenalty


/******************************************************************************/
  public void setMatrix ( String value )
  {
    matrix = value;
  }  // method setMatrix


/******************************************************************************/
  public void setMaximalScore ( String value )
  {
    maximal_score = value;
  }  // method setMaximalScore


/******************************************************************************/
  public void setMaximalScores ( String value )
  {
    maximal_scores = value;
  }  // method setMaximalScores


/******************************************************************************/
  public void setOpenPenalty ( String value )
  {
    open_penalty = value;
  }  // method setOpenPenalty


/******************************************************************************/
  public void setQtext ( String value )
  {
    qtext = value;
  }  // method setQtext


/******************************************************************************/
  public void setQueryData ( String value )
  {
    query_data = value;
  }  // method setQueryData


/******************************************************************************/
  public void setQueryLocus ( String value )
  {
    query_locus = value;
  }  // method setQueryLocus


/******************************************************************************/
  public void setQuerySequence ( String value )
  {
    query_sequence = value;
  }  // method setQuerySequence


/******************************************************************************/
  public void setScaleFactor ( int value )
  {
    scale_factor = value;
  }  // method setScaleFactor


/******************************************************************************/
  public void setSearchType ( String value )
  {
    search_type = value;
  }  // method setSearchType


/******************************************************************************/
  public void setScores ( String value )
  {
    scores = value;
  }  // method setScores


/******************************************************************************/
  public void setSection ( String name, String value )
  {
    // System.out.println ( "TLQuery: " + name + "\t'" + value + "'" );

         if ( name.equals ( "BEGIN ALIGNMENTS TIME" ) == true )  begin_alignments_time = value;

    else if ( name.equals ( "END ALIGNMENTS TIME" ) == true )  end_alignments_time = value;

    else if ( name.equals ( "EXTEND PENALTY" ) == true )  extend_penalty = value;

    else if ( name.equals ( "MATRIX" ) == true )  matrix = value;

    else if ( name.equals ( "MAXIMAL SCORE" ) == true )  maximal_score = value;

    else if ( name.equals ( "MAXIMAL SCORES" ) == true )  maximal_scores = value;

    else if ( name.equals ( "OPEN PENALTY" ) == true )  open_penalty = value;

    else if ( name.equals ( "QTEXT" ) == true )  qtext = value;

    else if ( name.equals ( "QUERY DATA" ) == true )  query_data = value;

    else if ( name.equals ( "QUERY LOCUS" ) == true )  query_locus = value;

    else if ( name.equals ( "QUERY SEQUENCE" ) == true )  query_sequence = value;

    else if ( name.equals ( "SCALE FACTOR" ) == true )
        scale_factor = InputTools.getInteger ( value );

    else if ( name.equals ( "SCORES" ) == true )  scores = value;

    else if ( name.equals ( "SEARCH TYPE" ) == true )  search_type = value;

    else  System.out.println ( "Unknown keyword [" + name + "] " + value );
  }  // method setSection


/******************************************************************************/

}  // class TLQuery
