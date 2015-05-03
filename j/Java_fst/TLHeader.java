
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

import InputTools;


/******************************************************************************/

public class TLHeader extends Object
{


/******************************************************************************/

  private   String  algorithm = "";			// [ALGORITHM] SW, FST, BLASTP, ...

  private   String  alignment_threshold = "";		// [ALIGNMENT THRESHOLD] n, Score = n, Significance = n

  private   String  alignment_view = "";		// [ALIGNMENT VIEW] 0

  private   String  amino_output = "";			// [AMINO OUTPUT]

  private   String  begin_search_time = "";		// [BEGIN SEARCH TIME] date time

  private   String  checkpoint_file = "";		// [CHECKPOINT FILE]

  private   int     columns = 0;			// [COLUMNS] n

  private   String  comment = "";			// [COMMENT] text

  private   String  data_source = "";			// [DATA SOURCE] data_source_name

  private   String  database_password = "";		// [DATABASE PASSWORD] password

  private   String  database_table = "";		// [DATABASE TABLE] tablename

  private   String  database_user = "";			// [DATABASE USER] username

  private   int     diagonals = 0;			// [DIAGONALS] n

  private   String  end_search_time = "";		// [END SEARCH TIME] date time

  private   String  eol = "";				// [EOL] LF, CRLF, CR

  private   String  end_gaps = "";			// [END GAPS] ON, OFF

  private   String  expectation = "";			// [EXPECTATION] n.n

  private   String  extend_penalty = "";		// [EXTEND PENALTY] n.n

  private   int     extension_threshold = 0;		// [EXTENSION THRESHOLD] n

  private   String  field = "";				// [FIELD] fieldlist

  private   int     frame_penalty = 0;			// [FRAME PENALTY] n

  private   int     gap_distance = 0;			// [GAP DISTANCE] n

  private   String  gap_trigger = "";			// [GAP TRIGGER] n.n

  private   String  gapped_alignment = "";		// [GAPPED ALIGNMENT] T, F, True?, False?

  private   String  hits = "";				// [HITS] Hit = m Extended = n Returned = o

  private   String  hydro_gaps = "";			// [HYDRO GAPS] ON, OFF

  private   String  hydro_residues = "";		// [HYDRO RESIDUES] residues - GPSNDQEKR

  private   String  job_id = "";			// [JOB ID] text

  private   String  jstate = "";			// [JSTATE] 0, 1

  private   int     ktuple = 0;				// [KTUPLE] n

  private   String  matrix = "";			// [MATRIX] pathname, matrixname

  private   int     max_alignments = 0;			// [MAX ALIGNMENTS] n

  private   int     max_scores = 0;			// [MAX SCORES] n

  private   String  multiple_hits = "";			// [MULTIPLE HITS]

  private   String  negative_matrix = "";		// [NEGATIVE MATRIX] ON, OFF

  private   int     nucleic_match = 0;			// [NUCLEIC MATCH] n

  private   int     nucleic_mismatch = 0;		// [NUCLEIC MISMATCH] n (where n < 0)

  private   String  null2 = "";				// [NULL2] YES, NO

  private   String  null_character = "";		// [NULL CHARACTER] x

  private   String  open_penalty = "";			// [OPEN PENALTY] n.n

  private   String  output_format = "";			// [OUTPUT FORMAT] TEXT PERCENTAGE MATCHCHARACTER

  private   String  output_order = "";			// [OUTPUT ORDER] INPUT, ALIGNED

  private   int     pair_gap = 0;			// [PAIR GAP] n

  private   int     passes = 0;				// [PASSES] n

  private   int     processors = 0;			// [PROCESSORS] n

  private   String  pscore = "";			// [PSCORE] GAPPED

  private   int     pseudocounts = 0;			// [PSEUDOCOUNTS]

  private   String  psi_expectation = "";		// [PSI EXPECTATION] n.n

  private   String  query_filter = "";			// [QUERY FILTER] F, T

  private   String  query_format = "";			// [QUERY FORMAT] FASTA/PEARSON

  private   String  query_genetic_code = "";		// [QUERY GENETIC CODE]

  private   int     query_neighborhood = 0;		// [QUERY NEIGHBORHOOD] n

  private   String  query_path = "";			// [QUERY PATH] pathname

  private   String  query_search = "";			// [QUERY SEARCH] {1 2 3 -1 -2 -3}, {D C}, B

  private   String  query_set = "";			// [QUERY SET] filename filename ...

  private   String  query_split = "";			// [QUERY SPLIT] x, y

  private   String  query_type = "";			// [QUERY TYPE] NT, AA

  private   int     rank_no = 0;			// [RANK NO]

  private   String  required_end = "";			// [REQUIRED END]

  private   String  required_start = "";		// [REQUIRED START]

  private   String  residue_gaps = "";			// [RESIDUE GAPS] ON, OFF

  private   String  restart_file = "";			// [RESTART FILE]

  private   String  result_path = "";			// [RESULT PATH] pathname

  private   int     scale_factor = 0;			// [SCALE FACTOR] n

  private   String  score_threshold = "";		// [SCORE THRESHOLD]

  private   String  search_id = "";			// [SEARCH ID] text

  private   String  search_time = "";			// [SEARCH TIME] YES, NO?

  private   String  seqalign_file = "";			// [SEQALIGN FILE]

  private   String  sequence_numbers = "";		// [SEQUENCE NUMBERS] ON, OFF

  private   String  show_gi = "";			// [SHOW GI] T, F

  private   String  significance = "";			// [SIGNIFICANCE] {EVALUE, ZSCORE, PSCORE} GAPPED | HISTOGRAM

  private   String  target_frames = "";			// [TARGET FRAMES] {1, 2, 3, -1, -2, -3}, {D C}

  private   String  target_genetic_code = "";		// [TARGET GENETIC CODE]

  private   String  target_length = "";			// [TARGET LENGTH]

  private   String  target_path = "";			// [TARGET PATH] pathname

  private   String  target_set = "";			// [TARGET SET] filename filename

  private   String  target_type = "";			// [TARGET TYPE] AA, NT

  private   String  threshold = "";			// [THRESHOLD] SIGNIFICANCE=0.01 or -10.

  private   int     top_diagonals = 0;			// [TOP DIAGONALS] n

  private   String  transition_weight = "";		// [TRANSITION WEIGHT] n.n

  private   String  user_id = "";			// [USER ID] username

  private   String  use_system = "";			// [USE SYSTEM] name

  private   String  version = "";			// [VERSION] text

  private   String  window = "";			// [WINDOW]

  private   int     word_size = 0;			// [WORD SIZE] n

  private   String  x_dropoff = "";			// [X DROPOFF]

  private   String  x_dropoff_final = "";		// [X DROPOFF FINAL]

  private   String  y_dropoff = "";			// [Y DROPOFF]


/******************************************************************************/
  // Constructor TLHeader
  public TLHeader ()
  {
    initialize ();
  }  // constructor TLHeader


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    algorithm = "";
    alignment_threshold = "";
    alignment_view = "";
    amino_output = "";
    begin_search_time = "";
    checkpoint_file = "";
    columns = 0;
    comment = "";
    data_source = "";
    database_password = "";
    database_table = "";
    database_user = "";
    diagonals = 0;
    end_search_time = "";
    eol = "";
    end_gaps = "";
    expectation = "";
    extend_penalty = "";
    extension_threshold = 0;
    field = "";
    frame_penalty = 0;
    gap_distance = 0;
    gap_trigger = "";
    gapped_alignment = "";
    hits = "";
    hydro_gaps = "";
    hydro_residues = "";
    job_id = "";
    jstate = "";
    ktuple = 0;
    matrix = "";
    max_alignments = 0;
    max_scores = 0;
    multiple_hits = "";
    negative_matrix = "";
    nucleic_match = 0;
    nucleic_mismatch = 0;
    null2 = "";
    null_character = "";
    open_penalty = "";
    output_format = "";
    output_order = "";
    pair_gap = 0;
    passes = 0;
    processors = 0;
    pscore = "";
    pseudocounts = 0;
    psi_expectation = "";
    query_filter = "";
    query_format = "";
    query_genetic_code = "";
    query_neighborhood = 0;
    query_path = "";
    query_search = "";
    query_set = "";
    query_split = "";
    query_type = "";
    rank_no = 0;
    required_end = "";
    required_start = "";
    residue_gaps = "";
    restart_file = "";
    result_path = "";
    scale_factor = 0;
    score_threshold = "";
    search_id = "";
    search_time = "";
    seqalign_file = "";
    sequence_numbers = "";
    show_gi = "";
    significance = "";
    target_frames = "";
    target_genetic_code = "";
    target_length = "";
    target_path = "";
    target_set = "";
    target_type = "";
    threshold = "";
    top_diagonals = 0;
    transition_weight = "";
    user_id = "";
    use_system = "";
    version = "";
    window = "";
    word_size = 0;
    x_dropoff = "";
    x_dropoff_final = "";
    y_dropoff = "";
  }  // method initialize 


/******************************************************************************/
  public String getAlgorithm ()
  {
    return algorithm;
  }  // method getAlgorithm


/******************************************************************************/
  public String getAlignmentThreshold ()
  {
    return alignment_threshold;
  }  // method getAlignmentThreshold


/******************************************************************************/
  public String getAlignmentView ()
  {
    return alignment_view;
  }  // method getAlignmentView


/******************************************************************************/
  public String getAminoOutput ()
  {
    return amino_output;
  }  // method getAminoOutput


/******************************************************************************/
  public String getBeginSearchTime ()
  {
    return begin_search_time;
  }  // method getBeginSearchTime


/******************************************************************************/
  public String getCheckpointFile ()
  {
    return checkpoint_file;
  }  // method getCheckpointFile


/******************************************************************************/
  public int getColumns ()
  {
    return columns;
  }  // method getColumns


/******************************************************************************/
  public String getComment ()
  {
    return comment;
  }  // method getComment


/******************************************************************************/
  public String getDataSource ()
  {
    return data_source;
  }  // method getDataSource


/******************************************************************************/
  public String getDatabasePassword ()
  {
    return database_password;
  }  // method getDatabasePassword


/******************************************************************************/
  public String getDatabaseTable ()
  {
    return database_table;
  }  // method getDatabaseTable


/******************************************************************************/
  public String getDatabaseUser ()
  {
    return database_user;
  }  // method getDatabaseUser


/******************************************************************************/
  public int getDiagonals ()
  {
    return diagonals;
  }  // method getDiagonals


/******************************************************************************/
  public String getEndSearchTime ()
  {
    return end_search_time;
  }  // method getEndSearchTime


/******************************************************************************/
  public String getEol ()
  {
    return eol;
  }  // method getEol


/******************************************************************************/
  public String getEndGaps ()
  {
    return end_gaps;
  }  // method getEndGaps


/******************************************************************************/
  public String getExpectation ()
  {
    return expectation;
  }  // method getExpectation


/******************************************************************************/
  public String getExtendPenalty ()
  {
    return extend_penalty;
  }  // method getExtendPenalty


/******************************************************************************/
  public int getExtensionThreshold ()
  {
    return extension_threshold;
  }  // method getExtensionThreshold


/******************************************************************************/
  public String getField ()
  {
    return field;
  }  // method getField


/******************************************************************************/
  public int getFramePenalty ()
  {
    return frame_penalty;
  }  // method getFramePenalty


/******************************************************************************/
  public int getGapDistance ()
  {
    return gap_distance;
  }  // method getGapDistance


/******************************************************************************/
  public String getGapTrigger ()
  {
    return gap_trigger;
  }  // method getGapTrigger


/******************************************************************************/
  public String getGappedAlignment ()
  {
    return gapped_alignment;
  }  // method getGappedAlignment


/******************************************************************************/
  public String getHits ()
  {
    return hits;
  }  // method getHits


/******************************************************************************/
  public String getHydroGaps ()
  {
    return hydro_gaps;
  }  // method getHydroGaps


/******************************************************************************/
  public String getHydroResidues ()
  {
    return hydro_residues;
  }  // method getHydroResidues


/******************************************************************************/
  public String getJobId ()
  {
    return job_id;
  }  // method getJobId


/******************************************************************************/
  public String getJstate ()
  {
    return jstate;
  }  // method getJstate


/******************************************************************************/
  public int getKtuple ()
  {
    return ktuple;
  }  // method getKtuple


/******************************************************************************/
  public String getMatrix ()
  {
    return matrix;
  }  // method getMatrix


/******************************************************************************/
  public int getMaxAlignments ()
  {
    return max_alignments;
  }  // method getMaxAlignments


/******************************************************************************/
  public int getMaxScores ()
  {
    return max_scores;
  }  // method getMaxScores


/******************************************************************************/
  public String getMultipleHits ()
  {
    return multiple_hits;
  }  // method getMultipleHits


/******************************************************************************/
  public String getNegativeMatrix ()
  {
    return negative_matrix;
  }  // method getNegativeMatrix


/******************************************************************************/
  public int getNucleicMatch ()
  {
    return nucleic_match;
  }  // method getNucleicMatch


/******************************************************************************/
  public int getNucleicMismatch ()
  {
    return nucleic_mismatch;
  }  // method getNucleicMismatch


/******************************************************************************/
  public String getNull2 ()
  {
    return null2;
  }  // method getNull2


/******************************************************************************/
  public String getNullCharacter ()
  {
    return null_character;
  }  // method getNullCharacter


/******************************************************************************/
  public String getOpenPenalty ()
  {
    return open_penalty;
  }  // method getOpenPenalty


/******************************************************************************/
  public String getOutputFormat ()
  {
    return output_format;
  }  // method getOutputFormat


/******************************************************************************/
  public String getOutputOrder ()
  {
    return output_order;
  }  // method getOutputOrder


/******************************************************************************/
  public int getPairGap ()
  {
    return pair_gap;
  }  // method getPairGap


/******************************************************************************/
  public int getPasses ()
  {
    return passes;
  }  // method getPasses


/******************************************************************************/
  public int getProcessors ()
  {
    return processors;
  }  // method getProcessors


/******************************************************************************/
  public String getPscore ()
  {
    return pscore;
  }  // method getPscore


/******************************************************************************/
  public int getPseudocounts ()
  {
    return pseudocounts;
  }  // method getPseudocounts


/******************************************************************************/
  public String getPsiExpectation ()
  {
    return psi_expectation;
  }  // method getPsiExpectation


/******************************************************************************/
  public String getQueryFilter ()
  {
    return query_filter;
  }  // method getQueryFilter


/******************************************************************************/
  public String getQueryFormat ()
  {
    return query_format;
  }  // method getQueryFormat


/******************************************************************************/
  public String getQueryGeneticCode ()
  {
    return query_genetic_code;
  }  // method getQueryGeneticCode


/******************************************************************************/
  public int getQueryNeighborhood ()
  {
    return query_neighborhood;
  }  // method getQueryNeighborhood


/******************************************************************************/
  public String getQueryPath ()
  {
    return query_path;
  }  // method getQueryPath


/******************************************************************************/
  public String getQuerySearch ()
  {
    return query_search;
  }  // method getQuerySearch


/******************************************************************************/
  public String getQuerySet ()
  {
    return query_set;
  }  // method getQuerySet


/******************************************************************************/
  public String getQuerySplit ()
  {
    return query_split;
  }  // method getQuerySplit


/******************************************************************************/
  public String getQueryType ()
  {
    return query_type;
  }  // method getQueryType


/******************************************************************************/
  public int getRankNo ()
  {
    return rank_no;
  }  // method getRankNo


/******************************************************************************/
  public String getRequiredEnd ()
  {
    return required_end;
  }  // method getRequiredEnd


/******************************************************************************/
  public String getRequiredStart ()
  {
    return required_start;
  }  // method getRequiredStart


/******************************************************************************/
  public String getResidueGaps ()
  {
    return residue_gaps;
  }  // method getResidueGaps


/******************************************************************************/
  public String getRestartFile ()
  {
    return restart_file;
  }  // method getRestartFile


/******************************************************************************/
  public String getResultPath ()
  {
    return result_path;
  }  // method getResultPath


/******************************************************************************/
  public int getScaleFactor ()
  {
    return scale_factor;
  }  // method getScaleFactor


/******************************************************************************/
  public String getScoreThreshold ()
  {
    return score_threshold;
  }  // method getScoreThreshold


/******************************************************************************/
  public String getSearchId ()
  {
    return search_id;
  }  // method getSearchId


/******************************************************************************/
  public String getSearchTime ()
  {
    return search_time;
  }  // method getSearchTime


/******************************************************************************/
  public String getSeqalignFile ()
  {
    return seqalign_file;
  }  // method getSeqalignFile


/******************************************************************************/
  public String getSequenceNumbers ()
  {
    return sequence_numbers;
  }  // method getSequenceNumbers


/******************************************************************************/
  public String getShowGi ()
  {
    return show_gi;
  }  // method getShowGi


/******************************************************************************/
  public String getSignificance ()
  {
    return significance;
  }  // method getSignificance


/******************************************************************************/
  public String getTargetFrames ()
  {
    return target_frames;
  }  // method getTargetFrames


/******************************************************************************/
  public String getTargetGeneticCode ()
  {
    return target_genetic_code;
  }  // method getTargetGeneticCode


/******************************************************************************/
  public String getTargetLength ()
  {
    return target_length;
  }  // method getTargetLength


/******************************************************************************/
  public String getTargetPath ()
  {
    return target_path;
  }  // method getTargetPath


/******************************************************************************/
  public String getTargetSet ()
  {
    return target_set;
  }  // method getTargetSet


/******************************************************************************/
  public String getTargetType ()
  {
    return target_type;
  }  // method getTargetType


/******************************************************************************/
  public String getThreshold ()
  {
    return threshold;
  }  // method getThreshold


/******************************************************************************/
  public int getTopDiagonals ()
  {
    return top_diagonals;
  }  // method getTopDiagonals


/******************************************************************************/
  public String getTransitionWeight ()
  {
    return transition_weight;
  }  // method getTransitionWeight


/******************************************************************************/
  public String getUserId ()
  {
    return user_id;
  }  // method getUserId


/******************************************************************************/
  public String getUseSystem ()
  {
    return use_system;
  }  // method getUseSystem


/******************************************************************************/
  public String getVersion ()
  {
    return version;
  }  // method getVersion


/******************************************************************************/
  public String getWindow ()
  {
    return window;
  }  // method getWindow


/******************************************************************************/
  public int getWordSize ()
  {
    return word_size;
  }  // method getWordSize


/******************************************************************************/
  public String getXDropoff ()
  {
    return x_dropoff;
  }  // method getXDropoff


/******************************************************************************/
  public String getXDropoffFinal ()
  {
    return x_dropoff_final;
  }  // method getXDropoffFinal


/******************************************************************************/
  public String getYDropoff ()
  {
    return y_dropoff;
  }  // method getYDropoff


/******************************************************************************/
  public void setAlgorithm ( String value )
  {
    algorithm = value;
  }  // method setAlgorithm


/******************************************************************************/
  public void setAlignmentThreshold ( String value )
  {
    alignment_threshold = value;
  }  // method setAlignmentThreshold


/******************************************************************************/
  public void setAlignmentView ( String value )
  {
    alignment_view = value;
  }  // method setAlignmentView


/******************************************************************************/
  public void setAminoOutput ( String value )
  {
    amino_output = value;
  }  // method setAminoOutput


/******************************************************************************/
  public void setBeginSearchTime ( String value )
  {
    begin_search_time = value;
  }  // method setBeginSearchTime


/******************************************************************************/
  public void setCheckpointFile ( String value )
  {
    checkpoint_file = value;
  }  // method setCheckpointFile


/******************************************************************************/
  public void setColumns ( int value )
  {
    columns = value;
  }  // method setColumns


/******************************************************************************/
  public void setComment ( String value )
  {
    comment = value;
  }  // method setComment


/******************************************************************************/
  public void setDataSource ( String value )
  {
    data_source = value;
  }  // method setDataSource


/******************************************************************************/
  public void setDatabasePassword ( String value )
  {
    database_password = value;
  }  // method setDatabasePassword


/******************************************************************************/
  public void setDatabaseTable ( String value )
  {
    database_table = value;
  }  // method setDatabaseTable


/******************************************************************************/
  public void setDatabaseUser ( String value )
  {
    database_user = value;
  }  // method setDatabaseUser


/******************************************************************************/
  public void setDiagonals ( int value )
  {
    diagonals = value;
  }  // method setDiagonals


/******************************************************************************/
  public void setEndSearchTime ( String value )
  {
    end_search_time = value;
  }  // method setEndSearchTime


/******************************************************************************/
  public void setEol ( String value )
  {
    eol = value;
  }  // method setEol


/******************************************************************************/
  public void setEndGaps ( String value )
  {
    end_gaps = value;
  }  // method setEndGaps


/******************************************************************************/
  public void setExpectation ( String value )
  {
    expectation = value;
  }  // method setExpectation


/******************************************************************************/
  public void setExtendPenalty ( String value )
  {
    extend_penalty = value;
  }  // method setExtendPenalty


/******************************************************************************/
  public void setExtensionThreshold ( int value )
  {
    extension_threshold = value;
  }  // method setExtensionThreshold


/******************************************************************************/
  public void setField ( String value )
  {
    field = value;
  }  // method setField


/******************************************************************************/
  public void setFramePenalty ( int value )
  {
    frame_penalty = value;
  }  // method setFramePenalty


/******************************************************************************/
  public void setGapDistance ( int value )
  {
    gap_distance = value;
  }  // method setGapDistance


/******************************************************************************/
  public void setGapTrigger ( String value )
  {
    gap_trigger = value;
  }  // method setGapTrigger


/******************************************************************************/
  public void setGappedAlignment ( String value )
  {
    gapped_alignment = value;
  }  // method setGappedAlignment


/******************************************************************************/
  public void setHits ( String value )
  {
    hits = value;
  }  // method setHits


/******************************************************************************/
  public void setHydroGaps ( String value )
  {
    hydro_gaps = value;
  }  // method setHydroGaps


/******************************************************************************/
  public void setHydroResidues ( String value )
  {
    hydro_residues = value;
  }  // method setHydroResidues


/******************************************************************************/
  public void setJobId ( String value )
  {
    job_id = value;
  }  // method setJobId


/******************************************************************************/
  public void setJstate ( String value )
  {
    jstate = value;
  }  // method setJstate


/******************************************************************************/
  public void setKtuple ( int value )
  {
    ktuple = value;
  }  // method setKtuple


/******************************************************************************/
  public void setMatrix ( String value )
  {
    matrix = value;
  }  // method setMatrix


/******************************************************************************/
  public void setMaxAlignments ( int value )
  {
    max_alignments = value;
  }  // method setMaxAlignments


/******************************************************************************/
  public void setMaxScores ( int value )
  {
    max_scores = value;
  }  // method setMaxScores


/******************************************************************************/
  public void setMultipleHits ( String value )
  {
    multiple_hits = value;
  }  // method setMultipleHits


/******************************************************************************/
  public void setNegativeMatrix ( String value )
  {
    negative_matrix = value;
  }  // method setNegativeMatrix


/******************************************************************************/
  public void setNucleicMatch ( int value )
  {
    nucleic_match = value;
  }  // method setNucleicMatch


/******************************************************************************/
  public void setNucleicMismatch ( int value )
  {
    nucleic_mismatch = value;
  }  // method setNucleicMismatch


/******************************************************************************/
  public void setNull2 ( String value )
  {
    null2 = value;
  }  // method setNull2


/******************************************************************************/
  public void setNullCharacter ( String value )
  {
    null_character = value;
  }  // method setNullCharacter


/******************************************************************************/
  public void setOpenPenalty ( String value )
  {
    open_penalty = value;
  }  // method setOpenPenalty


/******************************************************************************/
  public void setOutputFormat ( String value )
  {
    output_format = value;
  }  // method setOutputFormat


/******************************************************************************/
  public void setOutputOrder ( String value )
  {
    output_order = value;
  }  // method setOutputOrder


/******************************************************************************/
  public void setPairGap ( int value )
  {
    pair_gap = value;
  }  // method setPairGap


/******************************************************************************/
  public void setPasses ( int value )
  {
    passes = value;
  }  // method setPasses


/******************************************************************************/
  public void setProcessors ( int value )
  {
    processors = value;
  }  // method setProcessors


/******************************************************************************/
  public void setPscore ( String value )
  {
    pscore = value;
  }  // method setPscore


/******************************************************************************/
  public void setPseudocounts ( int value )
  {
    pseudocounts = value;
  }  // method setPseudocounts


/******************************************************************************/
  public void setPsiExpectation ( String value )
  {
    psi_expectation = value;
  }  // method setPsiExpectation


/******************************************************************************/
  public void setQueryFilter ( String value )
  {
    query_filter = value;
  }  // method setQueryFilter


/******************************************************************************/
  public void setQueryFormat ( String value )
  {
    query_format = value;
  }  // method setQueryFormat


/******************************************************************************/
  public void setQueryGeneticCode ( String value )
  {
    query_genetic_code = value;
  }  // method setQueryGeneticCode


/******************************************************************************/
  public void setQueryNeighborhood ( int value )
  {
    query_neighborhood = value;
  }  // method setQueryNeighborhood


/******************************************************************************/
  public void setQueryPath ( String value )
  {
    query_path = value;
  }  // method setQueryPath


/******************************************************************************/
  public void setQuerySearch ( String value )
  {
    query_search = value;
  }  // method setQuerySearch


/******************************************************************************/
  public void setQuerySet ( String value )
  {
    query_set = value;
  }  // method setQuerySet


/******************************************************************************/
  public void setQuerySplit ( String value )
  {
    query_split = value;
  }  // method setQuerySplit


/******************************************************************************/
  public void setQueryType ( String value )
  {
    query_type = value;
  }  // method setQueryType


/******************************************************************************/
  public void setRankNo ( int value )
  {
    rank_no = value;
  }  // method setRankNo


/******************************************************************************/
  public void setRequiredEnd ( String value )
  {
    required_end = value;
  }  // method setRequiredEnd


/******************************************************************************/
  public void setRequiredStart ( String value )
  {
    required_start = value;
  }  // method setRequiredStart


/******************************************************************************/
  public void setResidueGaps ( String value )
  {
    residue_gaps = value;
  }  // method setResidueGaps


/******************************************************************************/
  public void setRestartFile ( String value )
  {
    restart_file = value;
  }  // method setRestartFile


/******************************************************************************/
  public void setResultPath ( String value )
  {
    result_path = value;
  }  // method setResultPath


/******************************************************************************/
  public void setScaleFactor ( int value )
  {
    scale_factor = value;
  }  // method setScaleFactor


/******************************************************************************/
  public void setScoreThreshold ( String value )
  {
    score_threshold = value;
  }  // method setScoreThreshold


/******************************************************************************/
  public void setSearchId ( String value )
  {
    search_id = value;
  }  // method setSearchId


/******************************************************************************/
  public void setSearchTime ( String value )
  {
    search_time = value;
  }  // method setSearchTime


/******************************************************************************/
  public void setSeqalignFile ( String value )
  {
    seqalign_file = value;
  }  // method setSeqalignFile


/******************************************************************************/
  public void setSequenceNumbers ( String value )
  {
    sequence_numbers = value;
  }  // method setSequenceNumbers


/******************************************************************************/
  public void setShowGi ( String value )
  {
    show_gi = value;
  }  // method setShowGi


/******************************************************************************/
  public void setSignificance ( String value )
  {
    significance = value;
  }  // method setSignificance


/******************************************************************************/
  public void setTargetFrames ( String value )
  {
    target_frames = value;
  }  // method setTargetFrames


/******************************************************************************/
  public void setTargetGeneticCode ( String value )
  {
    target_genetic_code = value;
  }  // method setTargetGeneticCode


/******************************************************************************/
  public void setTargetLength ( String value )
  {
    target_length = value;
  }  // method setTargetLength


/******************************************************************************/
  public void setTargetPath ( String value )
  {
    target_path = value;
  }  // method setTargetPath


/******************************************************************************/
  public void setTargetSet ( String value )
  {
    target_set = value;
  }  // method setTargetSet


/******************************************************************************/
  public void setTargetType ( String value )
  {
    target_type = value;
  }  // method setTargetType


/******************************************************************************/
  public void setThreshold ( String value )
  {
    threshold = value;
  }  // method setThreshold


/******************************************************************************/
  public void setTopDiagonals ( int value )
  {
    top_diagonals = value;
  }  // method setTopDiagonals


/******************************************************************************/
  public void setTransitionWeight ( String value )
  {
    transition_weight = value;
  }  // method setTransitionWeight


/******************************************************************************/
  public void setUserId ( String value )
  {
    user_id = value;
  }  // method setUserId


/******************************************************************************/
  public void setUseSystem ( String value )
  {
    use_system = value;
  }  // method setUseSystem


/******************************************************************************/
  public void setVersion ( String value )
  {
    version = value;
  }  // method setVersion


/******************************************************************************/
  public void setWindow ( String value )
  {
    window = value;
  }  // method setWindow


/******************************************************************************/
  public void setWordSize ( int value )
  {
    word_size = value;
  }  // method setWordSize


/******************************************************************************/
  public void setXDropoff ( String value )
  {
    x_dropoff = value;
  }  // method setXDropoff


/******************************************************************************/
  public void setXDropoffFinal ( String value )
  {
    x_dropoff_final = value;
  }  // method setXDropoffFinal


/******************************************************************************/
  public void setYDropoff ( String value )
  {
    y_dropoff = value;
  }  // method setYDropoff


/******************************************************************************/
  public void setSection ( String name, String value )
  {
    // System.out.println ( name + "\t'" + value + "'" );

         if ( name.equals ( "ALGORITHM" ) == true )  algorithm = value;

    else if ( name.equals ( "ALIGNMENT THRESHOLD" ) == true )  
        alignment_threshold = value;

    else if ( name.equals ( "ALIGNMENT VIEW" ) == true )  alignment_view = value;

    else if ( name.equals ( "AMINO OUTPUT" ) == true )  amino_output = value;

    else if ( name.equals ( "BEGIN SEARCH TIME" ) == true )  begin_search_time = value;

    else if ( name.equals ( "CHECKPOINT FILE" ) == true )  checkpoint_file = value;

    else if ( name.equals ( "COLUMNS" ) == true )  
        columns = InputTools.getInteger ( value );

    else if ( name.equals ( "COMMENT" ) == true )  comment = value;

    else if ( name.equals ( "DATA SOURCE" ) == true )  data_source = value;

    else if ( name.equals ( "DATABASE PASSWORD" ) == true )  database_password = value;

    else if ( name.equals ( "DATABASE TABLE" ) == true )  database_table = value;

    else if ( name.equals ( "DATABASE USER" ) == true )  database_user = value;

    else if ( name.equals ( "DIAGONALS" ) == true )
        diagonals = InputTools.getInteger ( value );

    else if ( name.equals ( "END SEARCH TIME" ) == true )  end_search_time = value;

    else if ( name.equals ( "EOL" ) == true )  eol = value;

    else if ( name.equals ( "END GAPS" ) == true )  end_gaps = value;

    else if ( name.equals ( "EXPECTATION" ) == true )  expectation = value;

    else if ( name.equals ( "EXTEND PENALTY" ) == true )  extend_penalty = value;

    else if ( name.equals ( "EXTENSION THRESHOLD" ) == true )
        extension_threshold = InputTools.getInteger ( value );

    else if ( name.equals ( "FIELD" ) == true )  field = value;

    else if ( name.equals ( "FRAME PENALTY" ) == true )
        frame_penalty = InputTools.getInteger ( value );

    else if ( name.equals ( "GAP DISTANCE" ) == true )
        gap_distance = InputTools.getInteger ( value );

    else if ( name.equals ( "GAP TRIGGER" ) == true )  gap_trigger = value;

    else if ( name.equals ( "GAPPED ALIGNMENT" ) == true )  gapped_alignment = value;

    else if ( name.equals ( "HITS" ) == true )  hits = value;

    else if ( name.equals ( "HYDRO GAPS" ) == true )  hydro_gaps = value;

    else if ( name.equals ( "HYDRO RESIDUES" ) == true )  hydro_residues = value;

    else if ( name.equals ( "JOB ID" ) == true )  job_id = value;

    else if ( name.equals ( "JSTATE" ) == true )  jstate = value;

    else if ( name.equals ( "KTUPLE" ) == true )
        ktuple = InputTools.getInteger ( value );

    else if ( name.equals ( "MATRIX" ) == true )  matrix = value;

    else if ( name.equals ( "MAX ALIGNMENTS" ) == true )
        max_alignments = InputTools.getInteger ( value );

    else if ( name.equals ( "MAX SCORES" ) == true )
        max_scores = InputTools.getInteger ( value );

    else if ( name.equals ( "MULTIPLE HITS" ) == true )  multiple_hits = value;

    else if ( name.equals ( "NEGATIVE MATRIX" ) == true )  negative_matrix = value;

    else if ( name.equals ( "NUCLEIC MATCH" ) == true )
        nucleic_match = InputTools.getInteger ( value );

    else if ( name.equals ( "NUCLEIC MISMATCH" ) == true )
        nucleic_mismatch = InputTools.getInteger ( value );

    else if ( name.equals ( "NULL2" ) == true )  null2 = value;

    else if ( name.equals ( "NULL CHARACTER" ) == true )  null_character = value;

    else if ( name.equals ( "OPEN PENALTY" ) == true )  open_penalty = value;

    else if ( name.equals ( "OUTPUT FORMAT" ) == true )  output_format = value;

    else if ( name.equals ( "OUTPUT ORDER" ) == true )  output_order = value;

    else if ( name.equals ( "PAIR GAP" ) == true )
        pair_gap = InputTools.getInteger ( value );

    else if ( name.equals ( "PASSES" ) == true )
        passes = InputTools.getInteger ( value );

    else if ( name.equals ( "PROCESSORS" ) == true )
        processors = InputTools.getInteger ( value );

    else if ( name.equals ( "PSCORE" ) == true )  pscore = value; 

    else if ( name.equals ( "PSEUDOCOUNTS" ) == true )
        pseudocounts = InputTools.getInteger ( value );

    else if ( name.equals ( "PSI EXPECTATION" ) == true )  psi_expectation = value; 

    else if ( name.equals ( "QUERY FILTER" ) == true )  query_filter = value;

    else if ( name.equals ( "QUERY FORMAT" ) == true )  query_format = value;

    else if ( name.equals ( "QUERY GENETIC CODE" ) == true )  query_genetic_code = value;

    else if ( name.equals ( "QUERY NEIGHBORHOOD" ) == true )
        query_neighborhood = InputTools.getInteger ( value );

    else if ( name.equals ( "QUERY PATH" ) == true )  query_path = value;

    else if ( name.equals ( "QUERY SEARCH" ) == true )  query_search = value;

    else if ( name.equals ( "QUERY SET" ) == true )  query_set = value;

    else if ( name.equals ( "QUERY SPLIT" ) == true )  query_split = value;

    else if ( name.equals ( "QUERY TYPE" ) == true )  query_type = value;

    else if ( name.equals ( "RANK NO" ) == true )
        rank_no = InputTools.getInteger ( value );

    else if ( name.equals ( "REQUIRED END" ) == true )  required_end = value;

    else if ( name.equals ( "REQUIRED START" ) == true )  required_start = value; 

    else if ( name.equals ( "RESIDUE GAPS" ) == true )  residue_gaps = value;

    else if ( name.equals ( "RESTART FILE" ) == true )  restart_file = value;

    else if ( name.equals ( "RESULT PATH" ) == true )  result_path = value;

    else if ( name.equals ( "SCALE FACTOR" ) == true )
        scale_factor = InputTools.getInteger ( value );

    else if ( name.equals ( "SCORE THRESHOLD" ) == true )  score_threshold = value;

    else if ( name.equals ( "SEARCH ID" ) == true )  search_id = value;

    else if ( name.equals ( "SEARCH TIME" ) == true )  search_time = value;

    else if ( name.equals ( "SEQALIGN FILE" ) == true )  seqalign_file = value;

    else if ( name.equals ( "SEQUENCE NUMBERS" ) == true )  sequence_numbers = value;

    else if ( name.equals ( "SHOW GI" ) == true )  show_gi = value;

    else if ( name.equals ( "SIGNIFICANCE" ) == true )  significance = value;

    else if ( name.equals ( "TARGET FRAMES" ) == true )  target_frames = value;

    else if ( name.equals ( "TARGET GENETIC CODE" ) == true )  target_genetic_code = value;

    else if ( name.equals ( "TARGET LENGTH" ) == true )  target_length = value;

    else if ( name.equals ( "TARGET PATH" ) == true )  target_path = value;

    else if ( name.equals ( "TARGET SET" ) == true )  target_set = value;

    else if ( name.equals ( "TARGET TYPE" ) == true )  target_type = value; 

    else if ( name.equals ( "THRESHOLD" ) == true )  threshold = value;

    else if ( name.equals ( "TOP DIAGONALS" ) == true )
        top_diagonals = InputTools.getInteger ( value );

    else if ( name.equals ( "TRANSITION WEIGHT" ) == true )  transition_weight = value;

    else if ( name.equals ( "USER ID" ) == true )  user_id = value;

    else if ( name.equals ( "USE SYSTEM" ) == true )  use_system = value;

    else if ( name.equals ( "VERSION" ) == true )  version = value;

    else if ( name.equals ( "WINDOW" ) == true )  window = value;

    else if ( name.equals ( "WORD SIZE" ) == true )
        word_size = InputTools.getInteger ( value );

    else if ( name.equals ( "X DROPOFF" ) == true )  x_dropoff = value;

    else if ( name.equals ( "X DROPOFF FINAL" ) == true )  x_dropoff_final = value;

    else if ( name.equals ( "Y DROPOFF" ) == true )  y_dropoff = value;

    else  System.out.println ( "Unknown keyword [" + name + "] " + value );
  }  // method setSection


/******************************************************************************/

}  // class TLHeader
