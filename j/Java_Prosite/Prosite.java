
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

public class Prosite extends Object
{


/******************************************************************************/

  private   StringBuffer ac_lines = new StringBuffer ();	// Accession

  private   StringBuffer cc_lines = new StringBuffer ();	// Comments

  private   String cc_skip_flag = "";				// CC /SKIP-FLAG

  private   String cc_taxo_range = "";				// CC /TAXO-RANGE

  private   StringBuffer d3_lines = new StringBuffer ();	// 3D-structure

  private   StringBuffer de_lines = new StringBuffer ();	// Description

  private   StringBuffer do_lines = new StringBuffer ();	// Documentation

  private   StringBuffer dr_lines = new StringBuffer ();	// Data bank Reference

  private   StringBuffer dt_lines = new StringBuffer ();	// Date

  private   StringBuffer id_lines = new StringBuffer ();	// Identification

  private   StringBuffer ma_lines = new StringBuffer ();	// Matrix

  private   StringBuffer nr_lines = new StringBuffer ();	// Numerical Results

  private   String nr_total = "";				// NR /TOTAL

  private   String nr_positive = "";				// NR /POSITIVE

  private   String nr_unknown = "";				// NR /UNKNOWN

  private   String nr_false_pos = "";				// NR /FALSE-POS

  private   String nr_false_neg = "";				// NR /FALSE-NEG

  private   String nr_partial = "";				// NR /PARTIAL

  private   String nr_release = "";				// NR /RELEASE

  private   StringBuffer pa_lines = new StringBuffer ();	// Pattern

  private   StringBuffer ru_lines = new StringBuffer ();	// Rules


/******************************************************************************/

  private  InputTools prosite_file = new InputTools ();		// prosite.dat file

  private  StringBuffer line = new StringBuffer ( 128 );	// current line


/******************************************************************************/
  // Constructor Prosite
  public Prosite ()
  {
    initialize ();

    prosite_file.setFileName ( "prosite.dat" );
    prosite_file.openFile ();

    skipHeader ();
  }  // constructor Prosite


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    ac_lines.setLength ( 0 );
    cc_lines.setLength ( 0 );
    cc_skip_flag = "";
    cc_taxo_range = "";
    d3_lines.setLength ( 0 );
    de_lines.setLength ( 0 );
    do_lines.setLength ( 0 );
    dr_lines.setLength ( 0 );
    dt_lines.setLength ( 0 );
    id_lines.setLength ( 0 );
    ma_lines.setLength ( 0 );
    nr_lines.setLength ( 0 );
    nr_total = "";
    nr_positive = "";
    nr_unknown = "";
    nr_false_pos = "";
    nr_false_neg = "";
    nr_partial = "";
    nr_release = "";
    pa_lines.setLength ( 0 );
    ru_lines.setLength ( 0 );
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    prosite_file.closeFile ();
  }  // method close


/******************************************************************************/
  public String getAccession ()
  {
    // Return the name as the string up to the first semicolon.
    int index = ac_lines.indexOf ( ";" );

    if ( index > 0 )
      return ac_lines.substring ( 0, index );

    return ac_lines.toString ();
  }  // method getAccession


/******************************************************************************/
  public String getAcLines ()
  {
    return ac_lines.toString ();
  }  // method getAcLines


/******************************************************************************/
  public String getCcLines ()
  {
    return cc_lines.toString ();
  }  // method getCcLines


/******************************************************************************/
  public String getCcSkipFlag ()
  {
    return cc_skip_flag;
  }  // method getCcSkipFlag


/******************************************************************************/
  public String getCcTaxoRange ()
  {
    return cc_taxo_range;
  }  // method getCcTaxoRange


/******************************************************************************/
  public String getD3Lines ()
  {
    return d3_lines.toString ();
  }  // method getD3Lines


/******************************************************************************/
  public String getDeLines ()
  {
    return de_lines.toString ();
  }  // method getDeLines


/******************************************************************************/
  public String getDoLines ()
  {
    return do_lines.toString ();
  }  // method getDoLines


/******************************************************************************/
  public String getDrLines ()
  {
    return dr_lines.toString ();
  }  // method getDrLines


/******************************************************************************/
  public String getDtLines ()
  {
    return dt_lines.toString ();
  }  // method getDtLines


/******************************************************************************/
  public String getIdLines ()
  {
    return id_lines.toString ();
  }  // method getIdLines


/******************************************************************************/
  public String getMaLines ()
  {
    return ma_lines.toString ();
  }  // method getMaLines


/******************************************************************************/
  public String getName ()
  {
    // Return the name as the string up to the first semicolon.
    int index = id_lines.indexOf ( ";" );

    if ( index > 0 )
      return id_lines.substring ( 0, index );

    return id_lines.toString ();
  }  // method getName


/******************************************************************************/
  public String getNrLines ()
  {
    return nr_lines.toString ();
  }  // method getNrLines


/******************************************************************************/
  public String getNrTotal ()
  {
    return nr_total;
  }  // method getNrTotal


/******************************************************************************/
  public String getNrPositive ()
  {
    return nr_positive;
  }  // method getNrPositive


/******************************************************************************/
  public String getNrUnknown ()
  {
    return nr_unknown;
  }  // method getNrUnknown


/******************************************************************************/
  public String getNrFalsePos ()
  {
    return nr_false_pos;
  }  // method getNrFalsePos


/******************************************************************************/
  public String getNrFalseNeg ()
  {
    return nr_false_neg;
  }  // method getNrFalseNeg


/******************************************************************************/
  public String getNrPartial ()
  {
    return nr_partial;
  }  // method getNrPartial


/******************************************************************************/
  public String getPaLines ()
  {
    return pa_lines.toString ();
  }  // method getPaLines


/******************************************************************************/
// This method returns the Prosite motif as a Perl regular expression.
  public String getPatternPerl ()
  {
    // Check for a Prosite pattern.
    if ( pa_lines.length () <= 0 )  return "";

    StringBuffer pat = new StringBuffer ( 240 );

    // Remove the terminating period from the prosite pattern.
    if ( pa_lines.charAt ( pa_lines.length () - 1 ) == '.' )
      pat.append ( pa_lines.substring ( 0, pa_lines.length () - 1 ) );
    else
      pat.append ( pa_lines.substring ( 0 ) );

    // Traverse the pattern {} -> [^], - -> null, and x -> .
    for ( int i = pat.length () - 1; i >= 0; i-- )
    {
      if ( pat.charAt ( i ) == '{' )
      {
        pat.setCharAt ( i, '[' );
        pat.insert ( i+1, '^' );
      }  // if
      if ( pat.charAt ( i ) == '}' )  pat.setCharAt ( i, ']' );
      if ( pat.charAt ( i ) == 'x' )  pat.setCharAt ( i, '.' );
      if ( pat.charAt ( i ) == '-' )  pat.deleteCharAt ( i );
    }  // for

    // Traverse the pattern () -> {}
    for ( int i = pat.length () - 1; i >= 0; i-- )
    {
      if ( pat.charAt ( i ) == '(' )  pat.setCharAt ( i, '{' );
      if ( pat.charAt ( i ) == ')' )  pat.setCharAt ( i, '}' );
    }  // for

    // Check for the start of sequence character.
    if ( pat.charAt ( 0 ) == '<' )  pat.setCharAt ( 0, '^' );

    // Check for the end of sequence character.
    if ( pat.charAt ( pat.length () - 1 ) == '>' )
      pat.setCharAt ( pat.length () - 1, '$' );

    return pat.toString ();
  }  // method getPatternPerl


/******************************************************************************/
  public String getRuLines ()
  {
    return ru_lines.toString ();
  }  // method getRuLines


/******************************************************************************/
  public boolean isEndOfFile ()
  {
    return prosite_file.isEndOfFile ();
  }  // method isEndOfFile


/******************************************************************************/
  public void setAcLines ( StringBuffer value )
  {
    ac_lines = value;
  }  // method setAcLines


/******************************************************************************/
  public void setCcLines ( StringBuffer value )
  {
    cc_lines = value;
  }  // method setCcLines


/******************************************************************************/
  public void setCcSkipFlag ( String value )
  {
    cc_skip_flag = value;
  }  // method setCcSkipFlag


/******************************************************************************/
  public void setCcTaxoRange ( String value )
  {
    cc_taxo_range = value;
  }  // method setCcTaxoRange


/******************************************************************************/
  public void setD3Lines ( StringBuffer value )
  {
    d3_lines = value;
  }  // method setD3Lines


/******************************************************************************/
  public void setDeLines ( StringBuffer value )
  {
    de_lines = value;
  }  // method setDeLines


/******************************************************************************/
  public void setDoLines ( StringBuffer value )
  {
    do_lines = value;
  }  // method setDoLines


/******************************************************************************/
  public void setDrLines ( StringBuffer value )
  {
    dr_lines = value;
  }  // method setDrLines


/******************************************************************************/
  public void setDtLines ( StringBuffer value )
  {
    dt_lines = value;
  }  // method setDtLines


/******************************************************************************/
  public void setIdLines ( StringBuffer value )
  {
    id_lines = value;
  }  // method setIdLines


/******************************************************************************/
  public void setMaLines ( StringBuffer value )
  {
    ma_lines = value;
  }  // method setMaLines


/******************************************************************************/
  public void setNrLines ( StringBuffer value )
  {
    nr_lines = value;
  }  // method setNrLines


/******************************************************************************/
  public void setNrTotal ( String value )
  {
    nr_total = value;
  }  // method setNrTotal


/******************************************************************************/
  public void setNrPositive ( String value )
  {
    nr_positive = value;
  }  // method setNrPositive


/******************************************************************************/
  public void setNrUnknown ( String value )
  {
    nr_unknown = value;
  }  // method setNrUnknown


/******************************************************************************/
  public void setNrFalsePos ( String value )
  {
    nr_false_pos = value;
  }  // method setNrFalsePos


/******************************************************************************/
  public void setNrFalseNeg ( String value )
  {
    nr_false_neg = value;
  }  // method setNrFalseNeg


/******************************************************************************/
  public void setNrPartial ( String value )
  {
    nr_partial = value;
  }  // method setNrPartial


/******************************************************************************/
  public void setPaLines ( StringBuffer value )
  {
    pa_lines = value;
  }  // method setPaLines


/******************************************************************************/
  public void setRuLines ( StringBuffer value )
  {
    ru_lines = value;
  }  // method setRuLines


/******************************************************************************/
  private void skipHeader ()
  {
    // Skip the header comments.
    do
    {
      // Read in the next line from the file.
      line = prosite_file.getLine ();
    }
    while ( ( prosite_file.isEndOfFile () == false ) &&
            ( line.charAt ( 0 ) != '/' ) );
  }  // method skipHeader


/******************************************************************************/
// This method extracts the first two characters from a line as the line type.
  private String parseLineType ()
  {
    // Check the line length.
    if ( line.length () >= 3 )
      return line.substring ( 0, 2 );
    else
      return line.toString ();
  }  // method parseLineType


/******************************************************************************/
// This method extracts the data from the current line.
  private String parseLineData ()
  {
    // Check for data on the current line.
    if ( line.length () >= 6 )

      return line.substring ( 5 );

    // No data.
    return "";
  }  // method parseLineData


/******************************************************************************/
// This method appends the current data to the line type buffer.
  private void appendData 
      ( StringBuffer  line_type
      , String  data
      , String  line_seperator 
      )
  {
    // Check if this is the first line for this line type.
    if ( line_type.length () <= 0 )
    {
      line_type.append ( data );
    }  // if
    else
    {
      line_type.append ( line_seperator + data );
    }  // else
  }  // method appendData


/******************************************************************************/
// This method cracks the /qualifier=value; pairs from the CC line.
  private void crackCcLine ( String data )
  {
    String value = "";

    do
    {
      int equals = data.indexOf ( "=" );
      if ( equals <= 0 )  return;		// no value(s) remaining

      // Extract the qualifier name.
      String qualifier = data.substring ( 1, equals );

      int end = data.indexOf ( ";" );
      value = data.substring ( equals+1 );
      if ( end > 0 )
      {
        value = data.substring ( equals+1, end );

        // Shorten data for the next pair.
        if ( data.length () > end+2 )
          data = data.substring ( end+2 );
        else  
          data = "";
      }  // if
      else
        data = "";

      if ( qualifier.equals ( "TAXO-RANGE" ) == true )  cc_taxo_range = value;
      if ( qualifier.equals ( "SKIP-FLAG"  ) == true )  cc_skip_flag  = value;
    }  
    while ( ( value.length () > 0 ) && ( data.length () > 0 ) );
  }  // method crackCcLine


/******************************************************************************/
// This method cracks the /qualifier=value; pairs from the NR line.
  private void crackNrLine ( String data )
  {
    String value = "";

    do
    {
      int equals = data.indexOf ( "=" );
      if ( equals <= 0 )  return;		// no value(s) remaining

      // Extract the qualifier name.
      String qualifier = data.substring ( 1, equals );

      int end = data.indexOf ( ";" );
      value = data.substring ( equals+1 );
      if ( end > 0 )
      {
        value = data.substring ( equals+1, end );

        // Shorten data for the next pair.
        if ( data.length () > end+2 )
          data = data.substring ( end+2 );
        else  
          data = "";
      }  // if
      else
        data = "";

      if ( qualifier.equals ( "TOTAL"     ) == true )  nr_total     = value;
      if ( qualifier.equals ( "POSITIVE"  ) == true )  nr_positive  = value;
      if ( qualifier.equals ( "UNKNOWN"   ) == true )  nr_unknown   = value;
      if ( qualifier.equals ( "FALSE-POS" ) == true )  nr_false_pos = value;
      if ( qualifier.equals ( "FALSE-NEG" ) == true )  nr_false_neg = value;
      if ( qualifier.equals ( "PARTIAL"   ) == true )  nr_partial   = value;
      if ( qualifier.equals ( "RELEASE"   ) == true )  nr_release   = value;
    }  
    while ( ( value.length () > 0 ) && ( data.length () > 0 ) );
  }  // method crackNrLine


/******************************************************************************/
// This method adds the current line data to the correct line type.
  private void addData ( String line_type, String data )
  {
    // Check the data length.
    if ( data.length () <= 0 )  return;		// nothing to add

    if ( line_type.equals ( "ID" ) == true )  appendData ( id_lines, data, " " );
    if ( line_type.equals ( "AC" ) == true )  appendData ( ac_lines, data, " " );
    if ( line_type.equals ( "DT" ) == true )  appendData ( dt_lines, data, " " );
    if ( line_type.equals ( "DE" ) == true )  appendData ( de_lines, data, " " );
    if ( line_type.equals ( "PA" ) == true )  appendData ( pa_lines, data, "" );
    if ( line_type.equals ( "MA" ) == true )  appendData ( ma_lines, data, " " );
    if ( line_type.equals ( "RU" ) == true )  appendData ( ru_lines, data, " " );
    if ( line_type.equals ( "DR" ) == true )  appendData ( dr_lines, data, " " );
    if ( line_type.equals ( "3D" ) == true )  appendData ( d3_lines, data, " " );
    if ( line_type.equals ( "DO" ) == true )  appendData ( do_lines, data, " " );

    if ( line_type.equals ( "CC" ) == true )
    {
      appendData ( cc_lines, data, " " );

      // Parse the /qualifier=data fields
      crackCcLine ( data );
    }  // if

    if ( line_type.equals ( "NR" ) == true )
    {
      appendData ( nr_lines, data, " " );

      // Parse the /qualifier=data fields
      crackNrLine ( data );
    }  // if
  }  // method addData


/******************************************************************************/
// This method reads in the next entry from the prosite.dat file.
  public void next ()
  {
    // Initialize the current entry.
    initialize ();

    // Read in the next entry from the Prosite file.
    String line_type;		// line type for the current line
    String data;		// data value for the current line
    do
    {
      // Read in the next line from the file.
      line = prosite_file.getLine ();

      line_type = parseLineType ();
      data = parseLineData ();

      // Add the data to the line type.
      addData ( line_type, data );
    }
    while ( ( prosite_file.isEndOfFile () == false ) && 
            ( line_type.equals ( "//" ) == false ) ); 
  }  // method next


/******************************************************************************/

}  // class Prosite
