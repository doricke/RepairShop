
#include <stdio.h>

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

#define  MAX_LINE	132		/* Maximum text line length */

#define  ADDRESS	"<ADDRESS>"	/* HTML signature block start tag */
#define  sADDRESS	"</ADDRESS>"	/* HTML signature block end tag */

#define  BASEFONT	"<BASEFONT>"	/* HTML default font size for page tag */

#define  BLOCKQUOTE	"<BLOCKQUOTE>"	/* HTML long quote start tag */
#define  sBLOCKQUOTE	"</BLOCKQUOTE>"	/* HTML long quote end tag */

#define  BODY		"<BODY>"	/* HTML body start tag */
#define  sBODY		"</BODY>"	/* HTML body end tag */

#define  BOLD		"<B>"		/* HTML bold text start tag */
#define  sBOLD		"</B>"		/* HTML bold text end tag */

#define	 BR		"<BR>"		/* HTML line break */

#define  CAPTION	"<CAPTION>"	/* HTML table caption start tag */
#define  sCAPTION	"</CAPTION>"	/* HTML table caption end tag */

#define  CITE		"<CITE>"	/* HTML citation start tag */
#define  sCITE		"</CITE>"	/* HTML citation end tag */

#define  CODE		"<CODE>"	/* HTML code sample start tag */
#define  sCODE		"</CODE>"	/* HTML code sample end tag */

#define  COMMENT	"<--"		/* HTML comment start tag */
#define  sCOMMENT	"-->"		/* HTML comment end tag */

#define  DD		"<DD>"		/* HTML definition part start tag */

#define  DL		"<DL>"		/* HTML glossary or definition list start tag */
#define  sDL		"</DL>"		/* HTML glossary or definition list end tag */

#define  DT		"<DT>"		/* HTML term part of an item in glossary list */

#define  DFN		"<DFN>"		/* HTML definition start tag */
#define  sDFN		"</DFN>"	/* HTML definition end tag */

#define  EM		"<EM>"		/* HTML emphasized text start tag */
#define  sEM		"</EM>"		/* HTML emphasized text end tag */

#define  FORM		"<FORM>"	/* HTML form start tag */
#define  sFORM		"</FORM>"	/* HTML form end tag */

#define  FONT		"<FONT>"	/* HTML font size for enclosed text start tag */
#define  sFONT		"</FONT>"	/* HTML font size for enclosed text end tag */

#define  H1		"<H1>"		/* HTML header 1 start tag */
#define  sH1		"</H1>"		/* HTML header 1 end tag */

#define  H2		"<H2>"		/* HTML header 2 start tag */
#define  sH2		"</H2>"		/* HTML header 2 end tag */

#define  H3		"<H3>"		/* HTML header 3 start tag */
#define  sH3		"</H3>"		/* HTML header 3 end tag */

#define  H4		"<H4>"		/* HTML header 4 start tag */
#define  sH4		"</H4>"		/* HTML header 4 end tag */

#define  H5		"<H5>"		/* HTML header 5 start tag */
#define  sH5		"</H5>"		/* HTML header 5 end tag */

#define  H6		"<H6>"		/* HTML header 6 start tag */
#define  sH6		"</H6>"		/* HTML header 6 end tag */

#define  HEAD		"<HEAD>"	/* HTML header start tag */
#define  sHEAD		"</HEAD>"	/* HTML header end tag */

#define  HR		"<HR>"		/* HTML horizontal rule line */

#define  HTML		"<HTML>"	/* HTML document start tag */
#define  sHTML		"</HTML>"	/* HTML document end tag */

#define  KBD		"<KBD>"		/* HTML text to be typed in by user start tag */
#define  sKBD		"</KBD>"	/* HTML text to be typed in by user end tag */

#define  IMG		"<IMG>"		/* HTML inline image tag */

#define  INPUT		"<INPUT>"	/* HTML form input tag */

#define  ISINDEX	"<ISINDEX>"	/* HTML gateway script which allows searches */

#define  ITALIC		"<I>"		/* HTML italic text start tag */
#define  sITALIC	"</I>"		/* HTML italic text end tag */

#define  LI		"<LI>"		/* HTML individual list item */

#define  MENU		"<MENU>"	/* HTML menu list start tag */
#define  sMENU		"</MENU>"	/* HTML menu list end tag */

#define  OL		"<OL>"		/* HTML ordered list start tag */
#define  sOL		"</OL>"		/* HTML ordered list end tag */

#define  OPTION		"<OPTION>"	/* HTML item within a <SELECT> list tag */

#define  PARAGRAPH	"<P>"		/* HTML paragraph start tag */
#define  sPARAGRAPH	"</P>"		/* HTML paragraph end tag */

#define  PRE		"<PRE>"		/* HTML preformatted text start tag */
#define  sPRE		"</PRE>"	/* HTML preformatted text end tag */

#define  SAMP		"<SAMP>"	/* HTML sample text start tag */
#define  sSAMP		"</SAMP>"	/* HTML sample text end tag */

#define  SELECT		"<SELECT>"	/* HTML menu list start tag */
#define  sSELECT	"</SELECT>"	/* HTML menu list end tag */

#define  STRONG		"<STRONG>"	/* HTML strongly emphasized text start tag */
#define  sSTRONG	"</STRONG>"	/* HTML strongly emphasized text end tag */

#define  TABLE		"<TABLE>"	/* HTML table start tag */
#define  sTABLE		"</TABLE>"	/* HTML table end tag */

#define  TD		"<TD>"		/* HTML table data cell start tag */
#define  sTD		"</TD>"		/* HTML table data cell end tag */

#define  TEXTAREA	"<TEXTAREA>"	/* HTML multiline text entry start tag */
#define  sTEXTAREA	"</TEXTAREA>"	/* HTML multiline text entry end tag */

#define  TH		"<TH>"		/* HTML table heading cell start tag */
#define  sTH		"</TH>"		/* HTML table heading cell end tag */

#define  TITLE		"<TITLE>"	/* HTML title start tag */
#define  sTITLE		"</TITLE>"	/* HTML title end tag */

#define  TR		"<TR>"		/* HTML table row start tag */
#define  sTR		"</TR>"		/* HTML table row end tag */

#define  TT		"<TT>"		/* HTML text in typewriter font start tag */
#define  sTT		"</TT>"		/* HTML text in typewriter font end tag */

#define  UL		"<UL>"		/* HTML unordered list start tag */
#define  sUL		"</UL>"		/* HTML unordered list end tag */

#define  VAR		"<VAR>"		/* HTML variable name start tag */
#define  sVAR		"</VAR>"	/* HTML variable name end tag */


#define  SWISS_DB       'p'		/* NCBI database code for protein database */
#define  NUCLEOTIDE_DB  'n'		/* NCBI database code for nucleotide database */

/******************************************************************************/
/* Text file. */
typedef struct {
  char  name [ MAX_LINE ];	/* file name */
  char  next;			/* current character */
  char  token [ MAX_LINE ];	/* current token */
  char  line  [ MAX_LINE ];	/* current line */
  int   line_index;		/* line index */
  FILE  *data;			/* data file */
  short eof;			/* end of file flag */
} t_text;



/******************************************************************************/
main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  t_text  html;				/* HTML output file */
  FILE    *fopen ();			/* file open function */

  printf ( "This is the HTML page test program.  Version 0.1\n\n" );

  /* Open the HTML output file. */
/*  strcpy ( html.name, "test.html" ); */
  html.data = fopen ( "test6.html", "w" );

  write_html_header ( &html, "Output" );

  /* Start the body of the HTML document. */
  write_html_line ( &html, BODY, "", "" );

  /* Sample header 1 text line. */
  write_html_line ( &html, H1, "Sample HTML header H1 line.", sH1 );

  /* Sample text line. */
  write_html_line ( &html, "", "Sample text line.", BR );

  /* Write out a sample internal link to a name. */
  write_html_link ( &html, "373C8.04.r.1.sa" );

  /* Start preformatted text area. */
  write_html_line ( &html, PRE, "", "" );

  get_NCBI_file ( &html, "dbj|D10631|MUSZFP92", NUCLEOTIDE_DB );
  get_NCBI_file ( &html, "emb|X65231|HSZNFPT3", NUCLEOTIDE_DB );

  get_NCBI_file ( &html, "gb|X65231|HSZNFPT3", NUCLEOTIDE_DB );

  get_NCBI_file ( &html, "gb|M74186|SYNPEZZ18A", NUCLEOTIDE_DB );
  get_NCBI_file ( &html, "gb|M74186|", NUCLEOTIDE_DB );

  /* These work */
  get_NCBI_file ( &html, "pir|S44207|", SWISS_DB );
  get_NCBI_file ( &html, "pir||S44207", SWISS_DB );

  /* These do not work */
  get_NCBI_file ( &html, "pir||F40201", SWISS_DB );
  get_NCBI_file ( &html, "pir|F40201|", SWISS_DB );
  get_NCBI_file ( &html, "pir||D31201", SWISS_DB );
  get_NCBI_file ( &html, "pir|D31201|", SWISS_DB );

  /* End preformatted text area. */
  write_html_line ( &html, sPRE, "", "" );

  /* Write out a sample internal linkable name. */
  write_html_name ( &html, "373C8.04.r.1.sa" );

  /* Start preformatted text area. */
  write_html_line ( &html, PRE, "", "" );

  get_GSDB_file ( &html, "X74367" );
  get_GSDB_file ( &html, "M25669" );
  get_GSDB_file ( &html, "MUS5SRR4" );
  get_GSDB_file ( &html, "MUS5SRD1" );

  get_NCBI_file ( &html, "gb|M25669|", NUCLEOTIDE_DB );
  get_NCBI_file ( &html, "gb||RATCAT01", NUCLEOTIDE_DB );
  get_NCBI_file ( &html, "gb|M25669|RATCAT01", NUCLEOTIDE_DB );
  get_NCBI_file ( &html, "sp||FA9_HUMAN", SWISS_DB );
  get_NCBI_file ( &html, "sp|P00740|", SWISS_DB );
  get_NCBI_file ( &html, "sp|P00740|FA9_HUMAN", SWISS_DB );

  /* End preformatted text area. */
  write_html_line ( &html, sPRE, "", "" );

  /* Sign the WWW document. */
  write_html_signature ( &html );

  /* End the body of the HTML document. */
  write_html_line ( &html, sBODY, "", "" );

  /* End the HTML document. */
  write_html_line ( &html, sHTML, "", "" );

  /* Close the HTML output file. */
  fclose ( html.data );

  printf ( "\nEnd of HTML page test program.\n" );

}  /* main */


/******************************************************************************/
/* This function writes out a HTML sequence name for indexing. */
write_html_name ( html, name )
t_text	*html;		/* HTML output file */
char	name [];	/* anchor name */
{
  fprintf ( (*html).data, "<H2><A NAME=\"%s\">%s</A></H2>\n", name, name );
}  /* write_html_name */


/******************************************************************************/
/* This function writes out a HTML sequence name for linking. */
write_html_link ( html, name )
t_text	*html;		/* HTML output file */
char	name [];	/* anchor name */
{
  fprintf ( (*html).data, "<A HREF=#%s>%s</A>\n", name, name );
}  /* write_html_link */


/******************************************************************************/
/* This function writes out a HTML signature for Darrell Ricke. */
write_html_signature ( html )
t_text	*html;		/* HTML output file */
{
 write_html_line ( html, HR, "", "" );
 write_html_line ( html, ADDRESS, "", "" );
 write_html_line ( html, "", "Darrell O. Ricke, Ph.D. <A HREF=\"mailto:d_ricke@yahoo.com\">d_ricke@yahoo.com</A>", BR );
 write_html_line ( html, "", "&#169; Copyright Darrell Ricke 2000 all rights reserved", BR );
 write_html_line ( html, sADDRESS, "", "" );
}  /* write_html_signature */


/******************************************************************************/
/* This function writes out a HTML header. */
write_html_header ( html, title )
t_text	*html;		/* HTML output file */
char	title [];	/* Page title */
{
  write_html_line ( html, HTML, HEAD, "" );
  write_html_line ( html, TITLE, title, sTITLE );
}  /* write_html_header */


/******************************************************************************/
/* This function writes out a HTML line. */
write_html_line ( html, start_tag, line, end_tag )
t_text	*html;		/* HTML output file */
char	start_tag [];	/* HTML start tag */
char	line [];	/* HTML output text line */
char	end_tag [];	/* HTML end tag */
{
  fprintf ( (*html).data, "%s%s%s\n", start_tag, line, end_tag );
}  /* write_html_line */


/******************************************************************************/
/* This function tries to get a GSDB file accession number. */
get_GSDB_file ( html, accession )
t_text	*html;		/* HTML output file */
{

  /* "<A HREF=\"http://www.ncgr.org/cgi-bin/ff */

  fprintf ( (*html).data, "GSDB file: " );
  fprintf ( (*html).data, "<A HREF=\"http://www.ncgr.org/cgi-bin/ff.get?%s\">%s</A>\n", 
      accession, accession );
}  /* get_GSDB_file */


/******************************************************************************/
/* This function tries to get a GSDB file accession number. */
get_NCBI_file ( html, uid, database )
t_text	*html;		/* HTML output file */
char	uid [];		/* NCBI Unique Identifier */
char	database;	/* NCBI database code letter */
{
  fprintf ( (*html).data, "NCBI file: " );

  fprintf ( (*html).data, "<A HREF=\"http://www3.ncbi.nlm.nih.gov/htbin-post/Entrez/query?" );

  fprintf ( (*html).data, "db=%c&form=6&Dopt=g&uid=%s\">%s</A>\n", database, uid, uid );

  fprintf ( (*html).data, "\n" );
}  /* get_NCBI_file */

