
// import InputTools;

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

public class SwissProtHeader extends Object
{


/******************************************************************************/

  private StringBuffer header = new StringBuffer ( 4096 );	// SwissProt file header

  private StringBuffer references = new StringBuffer ( 4096 );	// Reference section

  // SwissProt Header fields.

  private String accession = "";			// Accession number

  private int length = 0;				// Sequence length

  private String id_name = "";				// ID line name


  private   String ac_line;		// AC line - Accession number(s)

  private   String cc_line;		// CC line - Comments or notes

  private   String de_line;		// DE line - Description

  private   String dr_line;		// DR line - Database cross-references

  private   String dt_line;		// DT line - Date

  private   String gn_line;		// GN line - Gene name(s)

  private   String id_line;		// ID line - Identification

  private   String kw_line;		// KW line - Keywords

  private   String oc_line;		// OC line - Organism classification

  private   String og_line;		// OG line - Organelle

  private   String os_line;		// OS line - Organism species

  private   String ox_line;		// OX line - 

  private   String ra_line;		// RA line - Reference authors

  private   String rc_line;		// RC line - Reference comment(s)

  private   String rl_line;		// RL line - Reference location

  private   String rn_line;		// RN line - Reference number

  private   String rp_line;		// RP line - Reference position

  private   String rt_line;		// RT line - Reference title

  private   String rx_line;		// RX line - Reference cross-reference(s)


/******************************************************************************/
  // Constructor SwissProtHeader
  public SwissProtHeader ()
  {
    initialize ();
  }  // constructor SwissProtHeader


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    accession = "";
    length = 0;
    header.setLength ( 0 );
    id_name = "";
    references.setLength ( 0 );

    ac_line = "";
    cc_line = "";
    de_line = "";
    dr_line = "";
    dt_line = "";
    gn_line = "";
    id_line = "";
    kw_line = "";
    oc_line = "";
    og_line = "";
    os_line = "";
    ra_line = "";
    rc_line = "";
    rl_line = "";
    rn_line = "";
    rp_line = "";
    rt_line = "";
    rx_line = "";
  }  // method initialize 


/******************************************************************************/
  public String getAcLine ()
  {
    return ac_line;
  }  // method getAcLine;


/******************************************************************************/
  public String getCcLine ()
  {
    return cc_line;
  }  // method getCcLine;


/******************************************************************************/
  public String getDeLine ()
  {
    return de_line;
  }  // method getDeLine;


/******************************************************************************/
  public String getDrLine ()
  {
    return dr_line;
  }  // method getDrLine;


/******************************************************************************/
  public String getDtLine ()
  {
    return dt_line;
  }  // method getDtLine;


/******************************************************************************/
  public String getGenus ()
  {
    int index;
    String genus = os_line.substring ( 5 );

    index = genus.indexOf ( ' ' );
    if ( index > 0 )  genus = genus.substring ( 0, index );

    index = genus.indexOf ( '/' );
    if ( index > 0 )  genus = genus.substring ( 0, index );

    return genus;
  }  // method getGenus


/******************************************************************************/
  public String getGnLine ()
  {
    return gn_line;
  }  // method getGnLine;


/******************************************************************************/
  public String getIdLine ()
  {
    return id_line;
  }  // method getIdLine;


/******************************************************************************/
  public String getIdName ()
  {
    return id_name;
  }  // method getIdName;


/******************************************************************************/
  public String getKingdom ()
  {
    int index;
    String kingdom = oc_line.substring ( 5 );

    index = kingdom.indexOf ( ';' );
    if ( index > 0 )  kingdom = kingdom.substring ( 0, index );

    index = kingdom.indexOf ( '/' );
    if ( index > 0 )  kingdom = kingdom.substring ( 0, index );

    index = kingdom.indexOf ( ' ' );
    if ( index > 0 )  kingdom = kingdom.substring ( 0, index );

    index = kingdom.indexOf ( '.' );
    if ( index > 0 )  kingdom = kingdom.substring ( 0, index );

    return kingdom;
  }  // method getKingdom


/******************************************************************************/
  public String getKwLine ()
  {
    return kw_line;
  }  // method getKwLine;


/******************************************************************************/
  public String getOcLine ()
  {
    return oc_line;
  }  // method getOcLine;


/******************************************************************************/
  public String getOrganelle ()
  {
    int index;

    if ( og_line.length () < 6 )  return "";

    String organelle = og_line.substring ( 5 );

    index = organelle.indexOf ( '.' );
    if ( index > 0 )  organelle = organelle.substring ( 0, index );

    return organelle;
  }  // method getOrganelle


/******************************************************************************/
  public String getOgLine ()
  {
    return og_line;
  }  // method getOgLine;


/******************************************************************************/
  public String getOxLine ()
  {
    return ox_line;
  }  // method getOxLine;


/******************************************************************************/
  public String getOsLine ()
  {
    return os_line;
  }  // method getOsLine;


/******************************************************************************/
  public String getRaLine ()
  {
    return ra_line;
  }  // method getRaLine;


/******************************************************************************/
  public String getRcLine ()
  {
    return rc_line;
  }  // method getRcLine;


/******************************************************************************/
  public String getRlLine ()
  {
    return rl_line;
  }  // method getRlLine;


/******************************************************************************/
  public String getRnLine ()
  {
    return rn_line;
  }  // method getRnLine;


/******************************************************************************/
  public String getRpLine ()
  {
    return rp_line;
  }  // method getRpLine;


/******************************************************************************/
  public String getRtLine ()
  {
    return rt_line;
  }  // method getRtLine;


/******************************************************************************/
  public String getRxLine ()
  {
    return rx_line;
  }  // method getRxLine;


/******************************************************************************/
  public void setAcLine ( String value )
  {
    ac_line = value;
  }  // method setAcLine;


/******************************************************************************/
  public void setCcLine ( String value )
  {
    cc_line = value;
  }  // method setCcLine;


/******************************************************************************/
  public void setDeLine ( String value )
  {
    de_line = value;
  }  // method setDeLine;


/******************************************************************************/
  public void setDrLine ( String value )
  {
    dr_line = value;
  }  // method setDrLine;


/******************************************************************************/
  public void setDtLine ( String value )
  {
    dt_line = value;
  }  // method setDtLine;


/******************************************************************************/
  public void setGnLine ( String value )
  {
    gn_line = value;
  }  // method setGnLine;


/******************************************************************************/
  public void setIdLine ( String value )
  {
    id_line = value;
  }  // method setIdLine;


/******************************************************************************/
  public void setKwLine ( String value )
  {
    kw_line = value;
  }  // method setKwLine;


/******************************************************************************/
  public void setOcLine ( String value )
  {
    oc_line = value;
  }  // method setOcLine;


/******************************************************************************/
  public void setOgLine ( String value )
  {
    og_line = value;
  }  // method setOgLine;


/******************************************************************************/
  public void setOsLine ( String value )
  {
    os_line = value;
  }  // method setOsLine;


/******************************************************************************/
  public void setOxLine ( String value )
  {
    ox_line = value;
  }  // method setOsLine;


/******************************************************************************/
  public void setRaLine ( String value )
  {
    ra_line = value;
  }  // method setRaLine;


/******************************************************************************/
  public void setRcLine ( String value )
  {
    rc_line = value;
  }  // method setRcLine;


/******************************************************************************/
  public void setRlLine ( String value )
  {
    rl_line = value;
  }  // method setRlLine;


/******************************************************************************/
  public void setRnLine ( String value )
  {
    rn_line = value;
  }  // method setRnLine;


/******************************************************************************/
  public void setRpLine ( String value )
  {
    rp_line = value;
  }  // method setRpLine;


/******************************************************************************/
  public void setRtLine ( String value )
  {
    rt_line = value;
  }  // method setRtLine;


/******************************************************************************/
  public void setRxLine ( String value )
  {
    rx_line = value;
  }  // method setRxLine;


/******************************************************************************/
  public String getAccession ()
  {
    return accession;
  }  // method getAccession


/******************************************************************************/
  public String getComment ()
  {
    if ( cc_line.length () <= 5 )  return "";

    return cc_line.substring ( 5 );
  }  // method getComment


/******************************************************************************/
  public String getDescription ()
  {
    if ( de_line.length () <= 5 )  return "";

    return de_line.substring ( 5 );
  }  // method getDescription


/******************************************************************************/
  public String getEcNumber ()
  {
    int index = de_line.indexOf ( "(EC " );
    if ( index == -1 )  return "";

    int end = de_line.indexOf ( ")", index );

    if ( end == -1 )
      return de_line.substring ( index + 4 );
    else
      return de_line.substring ( index + 4, end );
  }  // method getEcNumber


/******************************************************************************/
  public String getGeneName ()
  {
    if ( gn_line.length () < 6 )  return "";

    int space = gn_line.indexOf ( ' ', 5 );
    int period = gn_line.indexOf ( '.', 5 );

    if ( ( period != -1 ) && ( period < space ) )  space = period;

    if ( space <= 5 )
      return gn_line.substring ( 5 );
    else
      return gn_line.substring ( 5, space );
  }  // method getGeneName


/******************************************************************************/
  public String getKeywords ()
  {
    if ( kw_line.length () <= 5 )  return "";

    return kw_line.substring ( 5 );
  }  // method getKeywords


/******************************************************************************/
  public StringBuffer getHeader ()
  {
    return header;
  }  // method getHeader


/******************************************************************************/
  public int getLength ()
  {
    return length;
  }  // method getLength


/******************************************************************************/
  public String getOrganism ()
  {
    int end = os_line.lastIndexOf ( "." );

    // Check for no OS line.
    if ( os_line.length () < 6 )  return "";

    // Clip off the common name.
    int clip = os_line.indexOf ( " (" );
    if ( ( clip != -1 ) && ( clip < end ) )  end = clip;

    // Clip off the leading "OS   " and tailing period.
    if ( end > 0 )
      return os_line.substring ( 5, end );
    else
      return os_line.substring ( 5 );
  }  // method getOrganism


/******************************************************************************/
  public StringBuffer getReferences ()
  {
    return references;
  }  // method getReferences


/******************************************************************************/
  public void setSection ( String section_type, String section )
  {
    header.append ( section_type );
    header.append ( "   " );
    header.append ( section );
    header.append ( "\n" );

    if ( section_type.equals ( "AC" ) == true )
      crackAcLine ( section );

    else if ( section_type.equals ( "CC" ) == true )
      cc_line = section;

    else if ( section_type.equals ( "DE" ) == true )
      de_line = section;

    else if ( section_type.equals ( "DR" ) == true )
      dr_line = section;

    else if ( section_type.equals ( "DT" ) == true )
      dt_line = section;

    else if ( section_type.equals ( "GN" ) == true )
      gn_line = section;

    else if ( section_type.equals ( "ID" ) == true )
      crackIdLine ( section );

    else if ( section_type.equals ( "KW" ) == true )
      kw_line = section;

    else if ( section_type.equals ( "OC" ) == true )
      oc_line = section;

    else if ( section_type.equals ( "OG" ) == true )
      og_line = section;

    else if ( section_type.equals ( "OS" ) == true ) 
      os_line = section;

    else if ( section_type.equals ( "OX" ) == true ) 
      ox_line = section;

    else if ( ( section_type.equals ( "RA" ) == true ) ||
              ( section_type.equals ( "RC" ) == true ) ||
              ( section_type.equals ( "RL" ) == true ) ||
              ( section_type.equals ( "RN" ) == true ) ||
              ( section_type.equals ( "RP" ) == true ) ||
              ( section_type.equals ( "RT" ) == true ) ||
              ( section_type.equals ( "RX" ) == true ) )
         {
           // Note that this should be replaced with a Vector of references.
           references.append ( section_type );
           references.append ( "   " );
           references.append ( section );
           references.append ( "\n" );
         }  // if
         else
           System.out.println ( "SwissProtHeader.setSection Unknown type '" 
               + section_type
               + "   " 
               + section
               );

  }  // method setSection


/******************************************************************************/
  private void crackAcLine ( String acc )
  {
    if ( acc.length () <= 5 )  return;

    int index = acc.indexOf ( ";" );

    if ( index < 0 )
       accession = acc.substring ( 5 );
    else
      if ( index > 5 )
        accession = acc.substring ( 5, index );

    // System.out.println ( "Accession = " + accession );
  }  // method crackAcLine


/******************************************************************************/
  private void crackIdLine ( String line )
  {
    setIdLine ( line );

    int end = line.lastIndexOf ( "AA" );
    if ( end < 0 )  return;

    int start = line.lastIndexOf ( ";", end );
    if ( start < 0 )  return;

    length = InputTools.getInteger ( line.substring ( start+1, end-1 ) );

    end = line.indexOf ( " ", 5 );
    id_name = line.substring ( 5, end );

    // System.out.println ( line );
  }  // method crackIdLine


/******************************************************************************/

}  // class SwissProtHeader
