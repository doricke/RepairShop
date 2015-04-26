

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

public class GenePrediction extends Object
{


/******************************************************************************/

  private   int  gene_prediction_id = 0;	// gene_prediciton_id

  private   int  genomic_begin = 0;		// genomic_begin

  private   int  genomic_end = 0;		// genomic_end

  private   int  genomic_id = 0;		// genomic_id

  private   char  genomic_strand = '+';		// genomic_strand

  private   int  sequence_id = 0;		// sequence_id

  private   String  sequence_name = "";		// sequence_name


/******************************************************************************/
  // Constructor GenePrediction
  public GenePrediction ()
  {
    initialize ();
  }  // constructor GenePrediction


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    gene_prediction_id = 0;
    genomic_begin = 0;
    genomic_end = 0;
    genomic_id = 0;
    genomic_strand = '+';
    sequence_id = 0;
    sequence_name = "";
  }  // method initialize 


/******************************************************************************/
  public int getGenePredictionId ()
  {
    return gene_prediction_id;
  }  // method getGenePredictionId


/******************************************************************************/
  public int getGenomicBegin ()
  {
    return genomic_begin;
  }  // method getGenomicBegin


/******************************************************************************/
  public int getGenomicEnd ()
  {
    return genomic_end;
  }  // method getGenomicEnd


/******************************************************************************/
  public int getGenomicId ()
  {
    return genomic_id;
  }  // method getGenomicId


/******************************************************************************/
  public char getGenomicStrand ()
  {
    return genomic_strand;
  }  // method getGenomicStrand


/******************************************************************************/
  public int getSequenceId ()
  {
    return sequence_id;
  }  // method getSequenceId


/******************************************************************************/
  public String getSequenceName ()
  {
    return sequence_name;
  }  // method getSequenceName


/******************************************************************************/
  public void setGenePredictionId ( int value )
  {
    gene_prediction_id = value;
  }  // method setGenePredictionId


/******************************************************************************/
  public void setGenomicBegin ( int value )
  {
    genomic_begin = value;
  }  // method setGenomicBegin


/******************************************************************************/
  public void setGenomicEnd ( int value )
  {
    genomic_end = value;
  }  // method setGenomicEnd


/******************************************************************************/
  public void setGenomicId ( int value )
  {
    genomic_id = value;
  }  // method setGenomicId


/******************************************************************************/
  public void setGenomicStrand ( char value )
  {
    genomic_strand = value;
  }  // method setGenomicStrand


/******************************************************************************/
  public void setGenomicStrand ( String value )
  {
    if ( value.length () > 0 )
      genomic_strand = value.charAt ( 0 );
  }  // method setGenomicStrand


/******************************************************************************/
  public void setSequenceId ( int value )
  {
    sequence_id = value;
  }  // method setSequenceId


/******************************************************************************/
  public void setSequenceName ( String value )
  {
    sequence_name = value;
  }  // method setSequenceName


/******************************************************************************/
  public String toString ()
  {
    return gene_prediction_id + " " + sequence_name + " (" + sequence_id + ") " 
        + genomic_id + " " + genomic_strand + " [" 
        + genomic_begin + "-" + genomic_end + "] ";
  }  // method toString


/******************************************************************************/
  public void print ()
  {
    System.out.println ( toString () );
  }  // method print


/******************************************************************************/

}  // class GenePrediction
