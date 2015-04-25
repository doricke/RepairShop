
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
write_html_header_index ( html, fof )
t_text	*html;		/* HTML output file */
t_text	*fof;		/* file of input file names */
{
  char  current  [ MAX_LINE ];	/* current sequence name */
  char  previous [ MAX_LINE ];	/* previous sequence name */


  /* Write HTML line. */
  write_html_line ( html, HR, "", "" );
  write_html_line ( html, H3, "", "" );
  write_html_name ( html, INDEX );
  write_html_line ( html, "", "", sH3 );

  /* Initialization. */
  previous [ 0 ] = current [ 0 ] = '\0';

  /* Process the sequences. */
  while ( (*fof).eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( current, (*fof).line );
    current [ stridx ( current, "\n" ) ] = '\0';
    current [ stridx ( current, "*" ) ] = '\0';
    current [ stridx ( current, "-" ) ] = '\0';

    /* Print results when sequence name changes. */
    if ( strcmp ( current, previous ) != 0 )

      /* Write out a sample internal link to a name. */
      write_html_link ( html, current );

    strcpy ( previous, current );

    /* Get the next file name. */
    get_line ( fof );
  }  /* while */
}  /* write_html_header_index */


/******************************************************************************/
/* This function writes out a HTML file header. */
write_html_header ( html, name )
t_text	*html;		/* HTML output file */
char	name [];	/* main name */
{
  char  title [ MAX_LINE ];	/* HTML page title */


  /* Write out HTML & HEAD tags. */
  write_html_line ( html, HTML, HEAD, "" );

  strcpy ( title, "Output for " );
  strcat ( title, name );
  write_html_line ( html, TITLE, title, sTITLE );

  /* Start the body of the HTML document. */
  write_html_line ( html, BODY, "", "" );

  /* Sample header 1 text line. */
  write_html_line ( html, H1, title, sH1 );

  /* Start preformatted text area. */
  write_html_line ( html, PRE, "", "" );
}  /* write_html_header */


/******************************************************************************/
/* This function writes out the end of a HTML file. */
write_html_tail ( html )
t_text	*html;		/* HTML output file */
{
  /* End preformatted text area. */
  write_html_line ( html, sPRE, "", "" );

  /* Sign the WWW document. */
  write_html_signature ( html );

  /* End the body of the HTML document. */
  write_html_line ( html, sBODY, "", "" );

  /* End the HTML document. */
  write_html_line ( html, sHTML, "", "" );
}  /* write_html_tail */


/******************************************************************************/
/* This function writes out a HTML sequence name for indexing. */
write_html_name ( html, name )
t_text	*html;		/* HTML output file */
char	name [];	/* anchor name */
{
  char  lower_name [ MAX_LINE ];	/* lower case name for indexing */


  strcpy ( lower_name, name );
  strlower ( lower_name );
  fprintf ( (*html).data, "<A NAME=\"%s\">%s</A>", lower_name, name );
}  /* write_html_name */


/******************************************************************************/
/* This function writes out a HTML sequence name for linking. */
write_html_link ( html, name )
t_text	*html;		/* HTML output file */
char	name [];	/* anchor name */
{
  char  lower_name [ MAX_LINE ];	/* lower case name for indexing */


  strcpy ( lower_name, name );
  strlower ( lower_name );
  fprintf ( (*html).data, "<A HREF=#%s>%s</A>\n", lower_name, name );
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

  fprintf ( (*html).data, 
      "<A HREF=\"http://www.ncgr.org/cgi-bin/ff.get?%s\">GSDB</A> ", 
      accession );
}  /* get_GSDB_file */


/******************************************************************************/
/* This function tries to get a GSDB file accession number. */
get_NCBI_file ( html, accession, database )
t_text	*html;		/* HTML output file */
char	accession [];	/* database accession number */
char	database [];	/* database */
{
  /* Examples: 
	  ( html, "gb|M25669|", NUCLEOTIDE_DB );
	  ( html, "gb||RATCAT01", NUCLEOTIDE_DB );
	  ( html, "gb|M25669|RATCAT01", NUCLEOTIDE_DB );
	  ( html, "sp||FA9_HUMAN", SWISS_DB );
	  ( html, "sp|P00740|", SWISS_DB );
	  ( html, "sp|P00740|FA9_HUMAN", SWISS_DB );
  */

  fprintf ( (*html).data, "<A HREF=\"http://www3.ncbi.nlm.nih.gov/htbin-post/Entrez/query?" );

  if ( strcmp ( database, "Swiss" ) == 0 )
    fprintf ( (*html).data, "db=%c", SWISS_DB );
  else
    fprintf ( (*html).data, "db=%c", NUCLEOTIDE_DB );

  fprintf ( (*html).data, "&form=6&Dopt=g&uid=" );

  if ( strcmp ( database, "Swiss" ) == 0 )
    fprintf ( (*html).data, "sp|" );
  else
    fprintf ( (*html).data, "gb|" );
  fprintf ( (*html).data, "%s|\">NCBI</A> ", accession );
}  /* get_NCBI_file */


/******************************************************************************/
/* This function tests for a blank string. */
blank ( str )
char  str [];		/* text string */
{
  int  i = 0;		/* str index */

  while ( ( str [ i ] == ' ' ) || ( str [ i ] == '\t' ) )  i++;

  if ( ( str [ i ] == '\0' ) || ( str [ i ] == '\n' ) )  return ( TRUE );
  return ( FALSE );
}  /* blank */


/******************************************************************************/
/* This function capitalizes a string. */
capitalize ( s )
char s [];		/* string */
{
  int  i = 0;

  while ( ( s [ i ] != '\0' ) && ( i < MAX_LINE ) )
  {
    if ( ( s [ i ] >= 'a' ) && ( s [ i ] <= 'z' ) )

      s [ i ] = s [ i ] - 'a' + 'A';

    i++;
  }  /* while */
}  /* capitalize */


/******************************************************************************/
/* This function identifies the search algorithm, query sequence name, etc. */
identify_seq ( in, hit )
t_text  *in;		/* input text file - search algorithm output */
t_homology  *hit;		/* database homology hit */
{
  int  count = 0;	/* count of "******..." lines */
  int  lines = 0;	/* line limit to search through */


  strcpy ( (*hit).program, " " );	/* initialize */

  /* Check for FASTA output file. */
  while ( ( count < 2 ) &&
          ( (*in).eof != TRUE ) )
  {    
    get_line  ( in );
    get_token ( in );

    count += ( stridx ( (*in).token, "**********" ) == 0 ); 
  }  /* while */

  /* Get the comment line. */
  if ( (*in).eof != TRUE )
  {
    get_line ( in );
    get_line ( in );

    get_token ( in );

    /* Parse the comment line. */
    parse_comment ( in, hit );
  }  /* if */

  /* Identify the search program name. */

  /* Check for FASTA output file. */
  while ( ( count < 3 ) && ( lines < 10 ) &&
          ( (*in).eof != TRUE ) )
  {    
    get_line  ( in );
    get_token ( in );

    count += ( stridx ( (*in).token, "******" ) == 0 ); 
    lines++;
  }  /* while */

  if ( count == 3 )
  {
    if ( (*in).line [ stridx ( (*in).line, "FASTA" ) ] != '\0' )

      strcpy ( (*hit).program, "FASTA" );

    if ( (*in).line [ stridx ( (*in).line, "BLAST" ) ] != '\0' )

      strcpy ( (*hit).program, "BLAST" );
  }  /* if */
}  /* identify_seq */


/******************************************************************************/
/* This function finds the Query= line. */
find_query ( in, hit )
t_text      *in;		/* BLAST output file */
t_homology  *hit;		/* database homology hit */
{
  int     flag;			/* Boolean flag */


  /* Find the Query= line. */
  flag = FALSE;
  while ( ( flag   != TRUE ) &&
          ( (*in).eof != TRUE ) )
  {    
    get_line  ( in );
    get_token ( in );

    flag = ( strcmp ( (*in).token, "Query" ) == 0 ) &&
        ( (*in).line [ 5 ] == '=' );
  }  /* while */
}  /* find_query */


/******************************************************************************/
/* This function parses the comment line. */
parse_comment ( in, hit )
t_text      *in;		/* BLAST output file */
t_homology  *hit;		/* database homology hit */
{
  int     index;		/* line index */


  /* Save the sequence query name. */
  strcpy ( (*hit).name, (*in).token );
  (*hit).name [ stridx ( (*hit).name, "\n" ) ] = '\0';

  /* Check for the first base coordinate. */
  index = stridx ( (*in).line, "/First=" );
  if ( (*in).line [ index ] != '\0' )
    (*hit).first = get_integer ( &((*in).line [ index + 7 ]) );

  /* Check for the sequence length. */
  index = stridx ( (*in).line, "/Length=" );
  if ( (*in).line [ index ] != '\0' )
    (*hit).len = get_integer ( &((*in).line [ index + 8 ]) );

  /* Check for the sequence length. */
  (*hit).size = MAX_SEGMENT;
  index = stridx ( (*in).line, "len =" );
  if ( (*in).line [ index ] != '\0' )
    (*hit).size = get_integer ( &((*in).line [ index + 5 ]) );
  if ( (*hit).size < 0 )  (*hit).size = MAX_SEGMENT;    
}  /* parse_comment */


/******************************************************************************/
/* This function gets the next text character. */
get_char ( text )
t_text  *text;		/* ASCII text file */
{
  (*text).next = ' ';
  /* Get the next sequence character. */
  if ( ( (*text).line [ (*text).line_index ] != '\n' ) &&
       ( (*text).line [ (*text).line_index ] != '\0' ) )

    (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )

    if ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) )
      get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];

  if ( (*text).eof == TRUE )  (*text).next = EOF;
}  /* get_char */


/******************************************************************************/
/* This function gets the next integer from the current line. */
int  get_int ( text )
t_text  *text;		/* ASCII text file */
{
  int   i, sign = 1;


  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )
  {
    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  /* Check for a sign. */
  if ( ( (*text).next == '+' ) || ( (*text).next == '-' ) )
  {
    sign = ( (*text).next == '+' ) ? 1 : -1;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  /* Traverse the integer. */
  for ( i = 0; ( (*text).next >= '0' && (*text).next <= '9' );
      ( (*text).next = (*text).line [ (*text).line_index++ ] ) )

    i = i * 10 + (*text).next - '0';

  /* Set the integer sign. */
  i *= sign;

  return ( i );  /* return the integer */
}  /* get_int */


/******************************************************************************/
/* This function gets an integer. */
int  get_integer ( line )
char  line [];		/* text string */
{
  int   i, index = 0, sign = 1;


  /* skip white space */
  while ( ( line [ index ] == ' '  ) ||
          ( line [ index ] == '\t' ) )  index++;

  /* Check for a sign. */
  if ( ( line [ index ] == '+' ) || ( line [ index ] == '-' ) )

    sign = ( line [ index++ ] == '+' ) ? 1 : -1;

  /* Traverse the integer. */
  for ( i = 0; ( line [ index ] >= '0' && line [ index ] <= '9' ); index++ )

    i = i * 10 + line [ index ] - '0';

  /* Set the integer sign. */
  i *= sign;

  return ( i );  /* return the integer */
}  /* get_integer */


/******************************************************************************/
/* This function gets the next text line. */
get_line ( text )
t_text  *text;		/* ASCII text file */
{
  int  c = 0, i = 0;


  (*text).line_index = 0;
  (*text).line [ 0 ] = '\0';

  /* Check for end of file. */
  if ( (*text).eof == TRUE )  return;

  /* Get the text line. */
  while ( ( i < MAX_LINE ) && ( ( c = getc ( (*text).data ) ) != EOF ) &&
          ( c != '\n' ) && ( c != '\0' ) )

    (*text).line [ i++ ] = c;

  /* Properly terminate the text line. */
  (*text).line [ i++ ] = '\n';
  (*text).line [ i   ] = '\0';

  /* Check for end of file. */
  if ( c == EOF )  (*text).eof = TRUE;

  /* Get the first character. */
  (*text).next = (*text).line [ (*text).line_index++ ];
}  /* get_line */


/******************************************************************************/
/* This function gets the next name token. */
get_token ( text )
t_text  *text;		/* ASCII text file */
{
  int  i = 0;


  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )
  {
    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  strcpy ( (*text).token, "" );
  if ( (*text).eof == TRUE )  return;

  /* Copy the token. */
  while ( ( ( (*text).next >= '0' ) && ( (*text).next <= '9' ) ) ||
          ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
          ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
            ( (*text).next == '.' ) || ( (*text).next == '-' ) ||
            ( (*text).next == '*' ) || ( (*text).next == '_' ) ) 
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  /* Check for non-alphanumeric. */
  if ( i == 0 )
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  (*text).token [ i ] = '\0';
}  /* get_token */



/******************************************************************************/
/* This function gets the next tabbed (\t) token. */
get_tabbed_token ( text )
t_text  *text;		/* ASCII text file */
{
  int  i = 0;


  /* skip white space */
  while ( ( ( (*text).next == '\n'  ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )
  {
    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  strcpy ( (*text).token, "" );
  if ( (*text).eof == TRUE )  return;

  /* Copy the token. */
  while ( ( (*text).next != '\t' ) && ( (*text).next != '\n' ) &&
          ( (*text).next != '\0' ) ) 
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  (*text).token [ i ] = '\0';
}  /* get_tabbed_token */


/******************************************************************************/
/* This function gets the next text token. */
get_token2 ( text )
t_text  *text;		/* ASCII text file */
{
  int  i = 0;


  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )
  {
    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  strcpy ( (*text).token, "" );
  if ( (*text).eof == TRUE )  return;

  /* Copy the token. */
  while ( ( ( (*text).next >= '0' ) && ( (*text).next <= '9' ) ) ||
          ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
          ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) )
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  /* Check for non-alphanumeric. */
  if ( i == 0 )
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  (*text).token [ i ] = '\0';
}  /* get_token2 */


/******************************************************************************/
/* Convert n to characters in s. */
itoa (n, s)
char s[];
int n;
{
  int i, sign;

  if ((sign = n) < 0)  /* record sign */
    n = -n;

  /* Generate the digits in reverse order. */
  i = 0;
  do 
  {
    s[i++] = n % 10 + '0';	/* get next digit */
  }
  while ((n /= 10) > 0);	/* delete it */

  if (sign < 0)  s[i++] = '-';

  s[i] ='\0';

  reverse (s);
}  /* itoa */


/******************************************************************************/
/* Convert c to lower case; ASCII only */
lower (c)
int c;
{
  if ( c >= 'A' && c <= 'Z' )

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


/******************************************************************************/
/* This function capitalizes a string. */
strlower ( s )
char s [];		/* string */
{
  int  i = 0;


  while ( ( s [ i ] != '\0' ) && ( i < MAX_LINE ) )
  {
    s [ i ] = lower ( s [ i ] );
    i++;
  }  /* while */
}  /* strlower */


/******************************************************************************/
/* This function opens a file. */
open_text_file ( text )
t_text  *text;		/* ASCII text file */
{
  FILE  *fopen ();


  /* Initialization. */
  (*text).next = ' ';
  (*text).token [ 0 ] = '\0';
  (*text).line  [ 0 ] = '\0';
  (*text).eof         = TRUE;
  (*text).data        = NULL;

  /* Check for a valid file name. */
  if ( (*text).name [ 0 ] == '\0' )
  {
    printf ( "*WARNING* No input file text name specificed.\n" );
    return;
  }  /* if */

  /* Open the text file in read mode. */
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) == NULL )

    printf ( "*WARNING* Could not open '%s'\n", (*text).name );
  else
    (*text).eof = FALSE;

  /* Get the first line of text. */
  get_line ( text );
}  /* open_text_file */


/******************************************************************************/
/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* DNA sequence */
{
  int  count;		/* composition count */
  int  composition [ MAX_ASCII ];	/* composition array */
  int  DNA_composition;			/* A,C,G,T on current line */
  int  index;


  /* Find the beginning of the DNA sequence. */
  do
  {
    /* Initialization. */
    for ( index = 0; index < MAX_ASCII; index++ )
      composition [ index ] = 0;

    count = 0;

    /* Total the line composition. */
    for ( index = 0; ( (*text).line [ index ] != '\n' ) &&
                     ( (*text).line [ index ] != '\0' ) &&
                     ( (*text).line [ index ] != EOF ); index ++ )
    {
      composition [ (*text).line [ index ] ]++;

      /* Count the non-blank characters. */
      if ( ( (*text).line [ index ] != ' ' ) &&
           ( (*text).line [ index ] != '\t' ) )  count++;
    }  /* for */

    DNA_composition = composition [ 'A' ] + composition [ 'a' ] +
                      composition [ 'C' ] + composition [ 'c' ] +
                      composition [ 'G' ] + composition [ 'g' ] +
                      composition [ 'T' ] + composition [ 't' ];

    if ( DNA_composition * 1.0 <= count * 0.75 )

      get_line ( text );	/* get the next text line */
  }
  while ( ( (*text).eof != TRUE ) &&
          ( DNA_composition *1.0 <= count*0.75 ) );

  /* Read in the DNA sequence. */
  (*seq).total = 1;

  while ( ( (*text).eof != TRUE ) && ( (*seq).total < MAX_BASES ) )
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
    {
      (*seq).base [ (*seq).total++ ] = (*text).next;

      if ( (*seq).total == MAX_BASES - 1 )
        printf ( "*WARNING* Maximum number of sequence bases reached.\n" );
    }  /* if */

    get_char ( text );		/* get the next character */
  }  /* while */
}  /* read_DNA_seq */


/******************************************************************************/
/* Reverse string s in place. */
reverse(s)
char s[];
{
  int  c, i, j;

  for (i = 0, j = strlen(s)-1; i < j; i++, j--)
  {
    c    = s[i];
    s[i] = s[j];
    s[j] = c;
  }  /* for */
}  /* reverse */


/******************************************************************************/
/* This function concatenates t to the end of s. */
strcat (s, t)
char  s[], t[];		/* s must be big enough */
{
  int  i = 0, j = 0;


  /* Find the end of s. */
  while ( s [ i ] != '\0' )  i++;

  /* Copy t. */
  while ( ( s [ i++ ] = t [ j++ ] ) != '\0' )  
    ;
}  /* strcat */


/******************************************************************************/
/* Return <0 if s<t, 0 if s==t, >0 if s>t */
strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* strcmp */


/******************************************************************************/
/* Copy t to s */
strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* strcpy */


/******************************************************************************/
/* This function returns the index of t in s or index of '\0'. */
stridx (s, t)
char s[], t[];
{
  int  i, j, k;

  for (i = 0; s [ i ] != '\0'; i++ )
  {
    for ( j = i, k = 0; t [ k ] != '\0' && s [ j ] == t [ k ]; j++, k++ )  ;

    if ( t [ k ] == '\0' )  return ( i );
  }  /* for */

  return ( i );
}  /* stridx */


/******************************************************************************/
/* Return length of string s */
strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* strlen */

