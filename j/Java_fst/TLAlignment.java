

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

public class TLAlignment extends Object
{


/******************************************************************************/

  private   int     rank = 0;			// RANK

  private   String  score = "";			// Score

  private   String  p_score = "";		// P_Score

  private   String  query_name = "";		// Q

  private   String  query_frame = "";		// QF

  private   int     query_symbols = 0;		// #Q Symbols

  private   String  target_name = "";		// T

  private   String  target_frame = "";		// TF

  private   int     target_symbols = 0;		// #T Symbols

  private   String  description = "";		// D

  private   int     identical_match = 0;	// Identical Match

  private   int     similar = 0;		// Similar

  private   int     total_gaps = 0;		// Total # Of Gaps

  private   int     identity_alignment = 0;	// Identity: Alignment

  private   int     identity_query = 0;		// Identity: Query

  private   int     identity_target = 0;	// Identity: Target

  private   int     similarity_alignment = 0;	// Similarity: Alignment

  private   int     similarity_query = 0;	// Similarity: Query

  private   int     similarity_target = 0;	// Similarity: Target

  private   int     query_start = 0;		// QS

  private   int     query_end = 0;		// QE

  private   int     target_start = 0;		// TS

  private   int     target_end = 0;		// TE

  private   String  query_alignment = "";	// Q n sequence

  private   String  target_alignment = "";	// T n sequence

  private   String  target_identities = "";	// Target identities 

  private   String  error = "";			// Error


  private   static int count = 1;


/******************************************************************************/
  // Constructor TLAlignment
  public TLAlignment ()
  {
    // System.out.println ( "new TLAlignment " + count );
    count++;
    initialize ();
  }  // constructor TLAlignment


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    rank = 0;
    score = "";
    p_score = "";
    query_name = "";
    query_frame = "";
    query_symbols = 0;
    target_name = "";
    target_frame = "";
    target_symbols = 0;
    description = "";
    identical_match = 0;
    similar = 0;
    total_gaps = 0;
    identity_alignment = 0;
    identity_query = 0;
    identity_target = 0;
    similarity_alignment = 0;
    similarity_query = 0;
    similarity_target = 0;
    query_start = 0;
    query_end = 0;
    target_start = 0;
    target_end = 0;
    query_alignment = "";
    target_alignment = "";
    target_identities = "";
    error = "";
  }  // method initialize 


/******************************************************************************/
  public int getRank ()
  {
    return rank;
  }  // method getRank


/******************************************************************************/
  public String getScore ()
  {
    return score;
  }  // method getScore


/******************************************************************************/
  public String getPScore ()
  {
    return p_score;
  }  // method getPScore


/******************************************************************************/
  public String getQueryName ()
  {
    return query_name;
  }  // method getQueryName


/******************************************************************************/
  public String getQueryFrame ()
  {
    return query_frame;
  }  // method getQueryFrame


/******************************************************************************/
  public int getQuerySymbols ()
  {
    return query_symbols;
  }  // method getQuerySymbols


/******************************************************************************/
  public String getTargetName ()
  {
    return target_name;
  }  // method getTargetName


/******************************************************************************/
  public String getTargetFrame ()
  {
    return target_frame;
  }  // method getTargetFrame


/******************************************************************************/
  public int getTargetSymbols ()
  {
    return target_symbols;
  }  // method getTargetSymbols


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public int getIdenticalMatch ()
  {
    return identical_match;
  }  // method getIdenticalMatch


/******************************************************************************/
  public int getSimilar ()
  {
    return similar;
  }  // method getSimilar


/******************************************************************************/
  public int getTotalGaps ()
  {
    return total_gaps;
  }  // method getTotalGaps


/******************************************************************************/
  public int getIdentityAlignment ()
  {
    return identity_alignment;
  }  // method getIdentityAlignment


/******************************************************************************/
  public int getIdentityQuery ()
  {
    return identity_query;
  }  // method getIdentityQuery


/******************************************************************************/
  public int getIdentityTarget ()
  {
    return identity_target;
  }  // method getIdentityTarget


/******************************************************************************/
  public int getSimilarityAlignment ()
  {
    return similarity_alignment;
  }  // method getSimilarityAlignment


/******************************************************************************/
  public int getSimilarityQuery ()
  {
    return similarity_query;
  }  // method getSimilarityQuery


/******************************************************************************/
  public int getSimilarityTarget ()
  {
    return similarity_target;
  }  // method getSimilarityTarget


/******************************************************************************/
  public int getQueryStart ()
  {
    return query_start;
  }  // method getQueryStart


/******************************************************************************/
  public int getQueryEnd ()
  {
    return query_end;
  }  // method getQueryEnd


/******************************************************************************/
  public int getTargetStart ()
  {
    return target_start;
  }  // method getTargetStart


/******************************************************************************/
  public int getTargetEnd ()
  {
    return target_end;
  }  // method getTargetEnd


/******************************************************************************/
  public String getQueryAlignment ()
  {
    return query_alignment;
  }  // method getQueryAlignment


/******************************************************************************/
  public String getTargetAlignment ()
  {
    return target_alignment;
  }  // method getTargetAlignment


/******************************************************************************/
  public String getTargetIdentities ()
  {
    return target_identities;
  }  // method getTargetIdentities


/******************************************************************************/
  public String getError ()
  {
    return error;
  }  // method getError


/******************************************************************************/
  public void setRank ( int value )
  {
    rank = value;
  }  // method setRank


/******************************************************************************/
  public void setScore ( String value )
  {
    score = value;
  }  // method setScore


/******************************************************************************/
  public void setPScore ( String value )
  {
    p_score = value;
  }  // method setPScore


/******************************************************************************/
  public void setQueryName ( String value )
  {
    query_name = value;
  }  // method setQueryName


/******************************************************************************/
  public void setQueryFrame ( String value )
  {
    query_frame = value;
  }  // method setQueryFrame


/******************************************************************************/
  public void setQuerySymbols ( int value )
  {
    query_symbols = value;
  }  // method setQuerySymbols


/******************************************************************************/
  public void setTargetName ( String value )
  {
    target_name = value.trim ();
  }  // method setTargetName


/******************************************************************************/
  public void setTargetFrame ( String value )
  {
    target_frame = value;
  }  // method setTargetFrame


/******************************************************************************/
  public void setTargetSymbols ( int value )
  {
    target_symbols = value;
  }  // method setTargetSymbols


/******************************************************************************/
  public void setDescription ( String value )
  {
    if ( value.trim ().length () <= 0 )  return;

    description = value;
  }  // method setDescription


/******************************************************************************/
  public void setIdenticalMatch ( int value )
  {
    identical_match = value;
  }  // method setIdenticalMatch


/******************************************************************************/
  public void setSimilar ( int value )
  {
    similar = value;
  }  // method setSimilar


/******************************************************************************/
  public void setTotalGaps ( int value )
  {
    total_gaps = value;
  }  // method setTotalGaps


/******************************************************************************/
  public void setIdentityAlignment ( int value )
  {
    identity_alignment = value;
  }  // method setIdentityAlignment


/******************************************************************************/
  public void setIdentityQuery ( int value )
  {
    identity_query = value;
  }  // method setIdentityQuery


/******************************************************************************/
  public void setIdentityTarget ( int value )
  {
    identity_target = value;
  }  // method setIdentityTarget


/******************************************************************************/
  public void setSimilarityAlignment ( int value )
  {
    similarity_alignment = value;
  }  // method setSimilarityAlignment


/******************************************************************************/
  public void setSimilarityQuery ( int value )
  {
    similarity_query = value;
  }  // method setSimilarityQuery


/******************************************************************************/
  public void setSimilarityTarget ( int value )
  {
    similarity_target = value;
  }  // method setSimilarityTarget


/******************************************************************************/
  public void setQueryStart ( int value )
  {
    query_start = value;
  }  // method setQueryStart


/******************************************************************************/
  public void setQueryEnd ( int value )
  {
    query_end = value;
  }  // method setQueryEnd


/******************************************************************************/
  public void setTargetStart ( int value )
  {
    target_start = value;
  }  // method setTargetStart


/******************************************************************************/
  public void setTargetEnd ( int value )
  {
    target_end = value;
  }  // method setTargetEnd


/******************************************************************************/
  public void setQueryAlignment ( String value )
  {
    query_alignment = value;
  }  // method setQueryAlignment


/******************************************************************************/
  public void setTargetAlignment ( String value )
  {
    target_alignment = value;
  }  // method setTargetAlignment


/******************************************************************************/
  public void setTargetIdentities ( String value )
  {
    target_identities = value;
  }  // method setTargetIdentities


/******************************************************************************/
  public void setError ( String value )
  {
    System.out.println ( value );

    error = value;
  }  // method setError


/******************************************************************************/
  public void retotal ()
  {
    identical_match = 0;

    // Traverse the alignment counting up the identical amino acids.
    for ( int i = 0; i < query_alignment.length (); i++ )

      // Check if the amino acids are the same.
      // Might need to capitalize the sequences???
      if ( query_alignment.charAt ( i ) == target_alignment.charAt ( i ) )
      {
        identical_match++;
      }  // if

    // identity_alignment = ( identical_match * 100 ) / ( query_end - query_start + 1 );

    if ( query_symbols > 0 )

      identity_query = ( identical_match * 100 ) / query_symbols;

    else  identity_query = 0;

    if ( target_symbols > 0 )

      identity_target = ( identical_match * 100 ) / target_symbols;

    else  identity_target = 0;

    identity_alignment = identity_target;
  }  // method retotal


/******************************************************************************/
  public void printAlignment ()
  {
    System.out.println ();
    System.out.print ( query_name + " : " + target_name );
    System.out.print ( ", identities = " + identical_match );
    System.out.print ( ", target percent = " + identity_target );
    System.out.println ();

    System.out.println ( "Q: " + query_alignment );
    System.out.println ( "   " + target_identities );
    System.out.println ( "T: " + target_alignment );

/*
    for ( int i = 0; ( i < query_alignment.length () ) && 
                     ( i < target_alignment.length () ); i++ )
    {
      if ( query_alignment.charAt ( i ) == target_alignment.charAt ( i ) )

        System.out.print ( query_alignment.charAt ( i ) );
    }  // for
*/

    System.out.println ();
  }  // method printAlignment

/******************************************************************************/

}  // class TLAlignment
