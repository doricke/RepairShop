
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

public class Domain extends Object
{


/******************************************************************************/

  private   int  domain_id = 0;			// domain_id

  private   int  sequence_begin = 0;		// sequence_begin

  private   int  sequence_end = 0;		// sequence_end

  private   int  sequence_id = 0;		// sequence_id

  private   String  sequence_name = "";		// sequence_name

  private   String  sequence_segment = "";	// sequence_segment

  private   String  target_accession = "";	// target_accession

  private   String  target_description = "";	// target_description

  private   String  target_name = "";		// target_name

  private   String  target_segment = "";	// target_segment


/******************************************************************************/
  // Constructor Domain
  public Domain ()
  {
    initialize ();
  }  // constructor Domain


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    domain_id = 0;
    sequence_begin = 0;
    sequence_end = 0;
    sequence_id = 0;
    sequence_name = "";
    sequence_segment = "";
    target_accession = "";
    target_description = "";
    target_name = "";
    target_segment = "";
  }  // method initialize 


/******************************************************************************/
  public int getDomainId ()
  {
    return domain_id;
  }  // method getDomainId


/******************************************************************************/
  public int getSequenceBegin ()
  {
    return sequence_begin;
  }  // method getSequenceBegin


/******************************************************************************/
  public int getSequenceEnd ()
  {
    return sequence_end;
  }  // method getSequenceEnd


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
  public String getSequenceSegment ()
  {
    return sequence_segment;
  }  // method getSequenceSegment


/******************************************************************************/
  public String getTargetAccession ()
  {
    return target_accession;
  }  // method getTargetAccession


/******************************************************************************/
  public String getTargetDescription ()
  {
    return target_description;
  }  // method getTargetDescription


/******************************************************************************/
  public String getTargetName ()
  {
    return target_name;
  }  // method getTargetName


/******************************************************************************/
  public String getTargetSegment ()
  {
    return target_segment;
  }  // method getTargetSegment


/******************************************************************************/
  public void setDomainId ( int value )
  {
    domain_id = value;
  }  // method setDomainId


/******************************************************************************/
  public void setSequenceBegin ( int value )
  {
    sequence_begin = value;
  }  // method setSequenceBegin


/******************************************************************************/
  public void setSequenceEnd ( int value )
  {
    sequence_end = value;
  }  // method setSequenceEnd


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
  public void setSequenceSegment ( String value )
  {
    sequence_segment = value;
  }  // method setSequenceSegment


/******************************************************************************/
  public void setTargetAccession ( String value )
  {
    target_accession = value;
  }  // method setTargetAccession


/******************************************************************************/
  public void setTargetDescription ( String value )
  {
    target_description = value;
  }  // method setTargetDescription


/******************************************************************************/
  public void setTargetName ( String value )
  {
    target_name = value;
  }  // method setTargetName


/******************************************************************************/
  public void setTargetSegment ( String value )
  {
    target_segment = value;
  }  // method setTargetSegment


/******************************************************************************/
  public String toString ()
  {
    return target_name + " " + target_accession + " " + target_description;
  }  // method toString


/******************************************************************************/
  public void print ()
  {
    System.out.println ( toString () );
  }  // method print


/******************************************************************************/

}  // class Domain
