
#include <stdio.h>
#include <signal.h>

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

#define  TRUE		1
#define  FALSE		0

#define  GAP_AA_PENALTY   3	/* percentage penalty for each gap in homology region */

#define  GAP_AA_IDENTITIES  1	/* identity penalty for each gap in homology region */

#define  LINE_LENGTH	100	/* maximum number of sequence characters on a line */

#define  MAX_AA_PERCENT  40	/* maximum amino acid percentage in a homology hit */

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      110	/* maximum DNA sequence length */

#define  MAX_FEATURES	100	/* maximum number of regions or features per sequence */

#define  MAX_LINE	512	/* maximum line length */

#define  MAX_NAME       20	/* maximum name length */

#define  MIN_OVERLAP    50	/* minimum repeat overlap with other hit */

#define  MIN_AA_IDENTITIES 17   /* minimum amino acid identities to report */

#define  MIN_DNA_IDENTITIES 50   /* minimum DNA identities to report */

#define  MIN_AA_PERCENT    40   /* minimum percent amino acid homology to report */

#define  MIN_DNA_PERCENT  60	/* minimum percent DNA homology to report */

#define  OVERLAP_MIN    0.80	/* minimum overlap percentage */

#define  QUERY_START    12      /* offset past query and start on Query: line */

#define  SLEEP_TIME     15	/* number of seconds to sleep after sending mail */


/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  base [ MAX_BASES ];	/* DNA bases */
} t_seq;


/* Homology hit. */
typedef  struct {
  long  first;			/* coordinate of first base of query seq. */
  long  len;			/* length of query sequence */
  long  start;			/* start of homology */
  long  end;			/* end of homology */
  long  hit_start;		/* start of homology in database hit */
  long  hit_end;		/* end of homology in database hit */
  char  strand;			/* + = Plus DNA strand, - = Minus DNA stand */
  int   frame;			/* translation frame */
  long  identities;		/* number of identity bases */
  long  gaps;			/* number of gaps in the alignment */
  long  percent;		/* percent identity */
  long  length;			/* length of homology */
  char  name [ MAX_LINE ];	/* name of query sequence */
  char  code [ MAX_NAME ];	/* search codes */
  char  accession [ MAX_NAME ];	/* accession number of homology hit */
  char  program  [ MAX_LINE ];	/* search program name */
  char  database [ MAX_LINE ];	/* database name */
  char  seq_type [ MAX_LINE ];	/* sequence classification type */
  char  desc_1 [ MAX_LINE ];	/* Description line 1 */
  char  desc_2 [ MAX_LINE ];	/* Description line 2 */
  t_seq region;			/* sequence in area of homology */
  t_seq ident;			/* sequence identity bases */
  t_seq dashes;			/* sequence gaps */
} t_homology;


/* Multiple homology hits table. */
typedef  struct {
  int   total;			/* number of significant homologies */
  int   repeats;		/* number of repetitive homologies */
  long  start;			/* lowest start of homologies */
  long  end;			/* highest end of homologies */
  t_homology  hit [ MAX_FEATURES ]; /* single homologies */
} t_hits;


/* GenBank file sequence region or feature. */
typedef  struct {
  long  start;				/* first base of region */
  long  end;				/* last base of region */
  char  seq_type [ MAX_NAME ];		/* sequence classification type */
  char  note [ MAX_LINE ];		/* associated comment or note */
} t_region;


/* GenBank annotation information. */
typedef  struct {
  int   RNA;				/* is RNA flag */
  int   in_feature;			/* in feature flag */
  char  locus [ MAX_NAME ];		/* locus name */
  char  accession [ MAX_NAME ];		/* accession number */
  char  definition [ MAX_LINE ];	/* defintion line */
  char  organism [ MAX_LINE ];		/* species organism */
  long  current;			/* current annotation record */
  long  length;				/* sequence length */
  long  regions;			/* number of feature regions */
  t_region  region [ MAX_FEATURES ];	/* regions and features table */
} t_annotation;


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



static  int  Amino_Acid [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0,

/*51 52 53 54 55 56 57 58 59 60 61 62 63 64 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/* a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z */
   1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1,
   0, 0, 0, 0, 0, 0,
   1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1
};


static  int  DNA_base [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*51 52 53 54 55 56 57 58 59 60 61 62 63 64 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/* a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z */
   2, 6, 1, 6, 0, 0, 3, 6, 0, 0, 6, 0, 6, 5, 0, 0, 0, 6, 6, 4, 0, 6, 6, 0, 6, 0,
   0, 0, 0, 0, 0, 0,
   2, 6, 1, 6, 0, 0, 3, 6, 0, 0, 6, 0, 6, 5, 0, 0, 0, 6, 6, 4, 0, 6, 6, 0, 6, 0 
};


static  char  reverse_DNA [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*51 52 53 54 55 56 57 58 59 60 61 62 63 64 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*  a    b    c    d   e  f   g    h   i  j   k   l   m    n   o  p  q   r    s    t   u   v    w    x    y   z */
   'T', 'V', 'G', 'H', 0, 0, 'C', 'D', 0, 0, 'M', 0, 'K', 'N', 0, 0, 0, 'Y', 'S', 'A', 0, 'B', 'W', 'X', 'R', 0,
   0, 0, 0, 0, 0, 0,
   't', 'v', 'g', 'h', 0, 0, 'c', 'd', 0, 0, 'm', 0, 'k', 'n', 0, 0, 0, 'y', 's', 'a', 0, 'b', 'w', 'x', 'r', 0
};


/******************************************************************************/

#define S_BAD_PAIR	8	/* bad coordinate pair - no . . */
#define S_MISMATCH	9	/* mismatch */
#define S_NORMAL	6	/* normal function termination */
#define S_TABLE_OVERFLOW  4	/* table overflow */

#define S_AFTER_COMP	16	/* pair extents after & overlaps */
#define S_BEFORE_COMP	15	/* pair extends before & overlaps */
#define S_START_COMP	11	/* pair matches start of feature */
#define S_START_OVERLAP	22	/* start < .start & end < .end */
#define S_DATA		14	/* start of sequence data ".." */
#define S_EMPTY_TABLE	18	/* table empty */
#define	S_END_COMP	10	/* pair matches end of feature */
#define S_END_OVERLAP	20	/* start > .start & end > .end */
#define S_ERROR		21	/* error found */
#define S_FOUND		13	/* pair exists in feature table */
#define S_NOT_FOUND	19	/* pair past end of table */
#define S_SUB_COMP	17	/* pair is contained within feature */
#define S_SUPER_COMP	12	/* pair contains current feature */

#define CR		13	/* carriage return */

/* Sequence features classification categories. */
#define	UNSPECIFIED	0	/* unspecified region */
#define	cDNA_5_PRIME	1	/* cDNA 5' untranslated region */
#define	cDNA_CODE	2	/* cDNA coding region */
#define	cDNA_3_PRIME	3	/* cDNA 3' untranslated region */
#define	gDNA_5_PRIME	4	/* genomic DNA 5' untranslated region */
#define	gDNA_EXON	5	/* genomic DNA exon region */
#define	gDNA_INTRON	6	/* genomic DNA intron region */
#define	gDNA_CODE	7	/* genomic DNA coding region */
#define	gDNA_3_PRIME	8	/* genomic DNA 3' untranslated region */
#define	REPEAT		9	/* repeat region */
#define TOTAL		10	/* entire sequence(s) */

#define	MAX_DNA_TYPES	TOTAL + 1	/* DNA classification categories */

#define	NO_BITS		0	/* zero bit mask */


/******************************************************************************/

/* DNA or RNA base */
typedef struct {
  unsigned	bits : 4;	/* bit map of nucleotide base */
} t_base;


/******************************************************************************/

/* DNA/RNA bit representation - allows IUB nucleotide ambiguity */
static	int	base_masks [] = {

/* A    B    C    D   E  F   G    H   I  J   K   L   M */
  0x1, 0xE, 0x4, 0xB, 0, 0, 0x2, 0xD, 0, 0, 0xA, 0, 0x5,

/* N  O  P  Q   R    S    T    U    V    W    X   Y   Z */
   0, 0, 0, 0, 0x3, 0x6, 0x8, 0x8, 0x7, 0x9,  0, 0xC, 0 };


/* Feature names in priority order from lowest to highest. */
static  char  *feature_names [] = {
  "ignore", "satellite", "homology", "RNA", "intron", "3'UTR", "5'UTR",
  "rRNA", "tRNA", "snRNA", "scRNA", "exon", "coding", "repeat", "L1",
  "Alu", "rodent", "yeast", "E.coli", "vector", "" };


/******************************************************************************/

main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  t_text  done;			/* received e-mail responses */
  t_text  fof;			/* file of filenames */
  t_text  repeat;		/* repeat list for FATAL: errors */
  FILE    *fopen ();		/* file open function */

  char    sent [ MAX_LINE ];		/* name of sent e-mail message */
  char    received  [ MAX_LINE ];	/* name of received e-mail message */
  char    message [ MAX_LINE ];		/* e-mail send command */

  printf ( "This is the Blast/Fasta e-mail response list checker program.\n\n" );

  /* Prompt for the input file names. */
  prompt_file ( &fof, "What is the list of sent e-mail messages file name?" );

  /* Set up the repeat file. */
  strcpy ( repeat.name, fof.line );
  repeat.name [ stridx ( repeat.name, "\n" ) ] = '\0';
  repeat.name [ stridx ( repeat.name, "." ) ] = '\0';
  strcat ( repeat.name, ".redo" );
  repeat.data = fopen ( repeat.name, "w" );

  /* Compare the name lists. */
  while ( fof.eof != TRUE )
  {
    strcpy ( sent, fof.line );
    sent [ stridx ( sent, "\n" ) ] = '\0';
    strlower ( sent );				/* convert to lower case */
    strlower ( received);				/* convert to lower case */

    fof.line [ stridx ( fof.line, "\n" ) ] = '\0';
    send_email ( fof.line );
    fprintf ( repeat.data, "mail Q@ORNL.GOV < %s", fof.line );
    get_line ( &fof );
  }  /* while */

  /* Close the commands file. */
  fclose ( fof.data );
  fclose ( repeat.data );

  printf ( "\nEnd of program.\n" );
}  /* main */


/* This function tests for a blank string. */
blank ( str )
char  str [];		/* text string */
{
  int  i = 0;		/* str index */

  while ( ( str [ i ] == ' ' ) || ( str [ i ] == '\t' ) )  i++;

  if ( ( str [ i ] == '\0' ) || ( str [ i ] == '\n' ) )  return ( TRUE );
  return ( FALSE );
}  /* blank */


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

  if ( (*in).eof == TRUE )  return;

  /* Get the name. */
  get_token ( in );	/* consume the = */

  /* Get the query sequence name. */
  get_token ( in );

  /* Parse the comment line. */
  parse_comment ( in, hit );
}  /* find_query */


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
}  /* parse_comment */


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


/* This function gets the next integer from the current line. */
int  get_int ( text )
t_text  *text;		/* ASCII text file */
{
  char  c;
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


/* This function gets an integer. */
int  get_integer ( line )
char  line [];		/* text string */
{
  char  c;
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


/* This function gets the next text line. */
get_line ( text )
t_text  *text;		/* ASCII text file */
{
  int  c = 0, i = 0;


  (*text).line_index = 0;

  /* Get the text line. */
  while ( ( i < MAX_LINE ) && ( ( c = getc ( (*text).data ) ) != EOF ) &&
          ( c != '\n' ) )

    (*text).line [ i++ ] = c;

  /* Properly terminate the text line. */
  (*text).line [ i++ ] = '\n';
  (*text).line [ i   ] = '\0';

  /* Check for end of file. */
  if ( c == EOF )  (*text).eof = TRUE;

  /* Get the first character. */
  (*text).next = (*text).line [ (*text).line_index++ ];
}  /* get_line */


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


/* Convert c to lower case; ASCII only */
lower (c)
int c;
{
  if ( c >= 'A' && c <= 'Z' )

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


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
  if ( (*text).name [ 0 ] == '\0' )  return;

  /* Open the text file in read mode. */
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) == NULL )

    printf ( "Could not open '%s'\n", (*text).name );
  else
    (*text).eof = FALSE;

  /* Get the first line of text. */
  get_line ( text );
}  /* open_text_file */


/* This function classifies a homology hit based on the description line. */
classify_hit ( hit )
t_homology  *hit;		/* current homology hit */
{
  char  desc [ MAX_LINE ];	/* Complete description line. */


  strcpy ( desc, (*hit).desc_1 );
  strcat ( desc, (*hit).desc_2 );
  strcpy ( (*hit).seq_type, " " );

  /* Search for keywords. */
  if ( desc [ stridx ( desc, " EST" ) ] != '\0' )  strcpy ( (*hit).seq_type, "EST" );
  if ( desc [ stridx ( desc, "cDNA" ) ] != '\0' )  strcpy ( (*hit).seq_type, "EST" );
  if ( desc [ stridx ( desc, " STS" ) ] != '\0' )  strcpy ( (*hit).seq_type, "STS" );
  if ( desc [ stridx ( desc, "microsatellite" ) ] != '\0' )  strcpy ( (*hit).seq_type, "microsatellite" );
  if ( desc [ stridx ( desc, " repetitive" ) ] != '\0' )  strcpy ( (*hit).seq_type, "repeat" );
  if ( desc [ stridx ( desc, "endogenous" ) ] != '\0' )  strcpy ( (*hit).seq_type, "repeat" );
  if ( desc [ stridx ( desc, "retroviral" ) ] != '\0' )  strcpy ( (*hit).seq_type, "repeat" );
  if ( desc [ stridx ( desc, "MER" ) ] != '\0' )  strcpy ( (*hit).seq_type, "MER" );
  if ( desc [ stridx ( desc, "HIV" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "IMMUNODEFICIENCY" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "vector" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "Cloning" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "cloning" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "Synthetic" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "synthetic" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "plasmid" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "Plasmid" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "Bacteriophage" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "bacteriophage" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "pBluescript" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "SCOS" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "sCos" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "pSPL3" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, " alu" ) ] != '\0' )  strcpy ( (*hit).seq_type, "Alu" );
  if ( desc [ stridx ( desc, " Alu" ) ] != '\0' )  strcpy ( (*hit).seq_type, "Alu" );
  if ( desc [ stridx ( desc, "ALU" ) ] != '\0' )  strcpy ( (*hit).seq_type, "Alu" );
  if ( ( desc [ 7 ] == 'A' ) && ( desc [ 8 ] == 'L' ) && ( desc [ 9 ] == 'U' ) )
    strcpy ( (*hit).seq_type, "Alu" );

  if ( desc [ stridx ( desc, " L1 " ) ] != '\0' )  strcpy ( (*hit).seq_type, "L1" );
  if ( ( desc [ 0 ] == 'L' ) && ( desc [ 1 ] == '1' ) &&
       ( ( desc [ 2 ] == '\n' ) || ( desc [ 2 ] == '\0' ) ) )  strcpy ( (*hit).seq_type, "L1" );
  if ( strcmp ( desc, "L1" ) == 0 )  strcpy ( (*hit).seq_type, "L1" );
  if ( desc [ stridx ( desc, " Line" ) ] != '\0' )  strcpy ( (*hit).seq_type, "LINE" );
  if ( desc [ stridx ( desc, " LINE" ) ] != '\0' )  strcpy ( (*hit).seq_type, "LINE" );
  if ( desc [ stridx ( desc, "LINE-1" ) ] != '\0' )  strcpy ( (*hit).seq_type, "L1" );
  if ( desc [ stridx ( desc, "LIN1_HUMAN" ) ] != '\0' )  strcpy ( (*hit).seq_type, "L1" );
  if ( desc [ stridx ( desc, " Sine" ) ] != '\0' )  strcpy ( (*hit).seq_type, "SINE" );
  if ( desc [ stridx ( desc, " sine" ) ] != '\0' )  strcpy ( (*hit).seq_type, "SINE" );
  if ( desc [ stridx ( desc, " SINE" ) ] != '\0' )  strcpy ( (*hit).seq_type, "SINE" );
  if ( desc [ stridx ( desc, "THE" ) ] != '\0' )  strcpy ( (*hit).seq_type, "THE" );
  if ( desc [ stridx ( desc, "O-family" ) ] != '\0' )  strcpy ( (*hit).seq_type, "O-family" );
  if ( desc [ stridx ( desc, "E.coli" ) ] != '\0' )  strcpy ( (*hit).seq_type, "E.coli" );
  if ( desc [ stridx ( desc, "E. coli" ) ] != '\0' )  strcpy ( (*hit).seq_type, "E.coli" );
  if ( desc [ stridx ( desc, "Escherichia" ) ] != '\0' )  strcpy ( (*hit).seq_type, "E.coli" );
  if ( desc [ stridx ( desc, "ESCHERICHIA" ) ] != '\0' )  strcpy ( (*hit).seq_type, "E.coli" );
  if ( desc [ stridx ( desc, "MLT1d" ) ] != '\0' )  strcpy ( (*hit).seq_type, "MLT1d" );
  if ( desc [ stridx ( desc, "MSTb" ) ] != '\0' )  strcpy ( (*hit).seq_type, "MSTb" );


  if ( desc [ stridx ( desc, "HUMLAMPA02" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "I03356" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "I03343" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "I02541" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "I01644" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );
  if ( desc [ stridx ( desc, "KPBLACAZ6" ) ] != '\0' )  strcpy ( (*hit).seq_type, "vector" );

  if ( desc [ stridx ( desc, "B1con" ) ] != '\0' )  strcpy ( (*hit).seq_type, "rodent" );
  if ( desc [ stridx ( desc, "musl1a1" ) ] != '\0' )  strcpy ( (*hit).seq_type, "rodent" );

  if ( desc [ stridx ( desc, "HPNIXAGN" ) ] != '\0' )  strcpy ( (*hit).seq_type, "E.coli" );

  if ( desc [ stridx ( desc, "HUMCDHI014" ) ] != '\0' )  strcpy ( (*hit).seq_type, "cs16" );
  if ( desc [ stridx ( desc, "HSECADH" ) ] != '\0' )  strcpy ( (*hit).seq_type, "cs16" );
}  /* classify_hit */


/* This function checks a DNA sequence homology hit identity composition. */
check_composition ( hit )
t_homology  *hit;		/* homology hit */
{
  int  composition [ MAX_ASCII ];	/* composition array */
  int  count;				/* total character count */

  int As = 0, Cs = 0, Gs = 0, Ts = 0;	/* DNA bases */


  /* Check if sequence already has a defined type. */
  if ( (*hit).seq_type [ 0 ] != ' ' )  return;

  /* Tally up the sequence composition by character. */
  seq_composition ( &((*hit).ident), composition, &count );

  As = composition [ 'A' ] + composition [ 'a' ];
  Cs = composition [ 'C' ] + composition [ 'c' ];
  Gs = composition [ 'G' ] + composition [ 'g' ];
  Ts = composition [ 'T' ] + composition [ 't' ];

  count = As + Cs + Gs + Ts;

  if ( ( As * 2 > count ) || ( Cs * 2 > count ) || 
       ( Gs * 2 > count ) || ( Ts * 2 > count ) ||
       ( (As + Cs) * 3 > count * 2 ) || ( (As + Gs) * 3 > count * 2 ) ||
       ( (As + Ts) * 3 > count * 2 ) || ( (Cs + Gs) * 3 > count * 2 ) ||
       ( (Cs + Ts) * 3 > count * 2 ) || ( (Gs + Ts) * 3 > count * 2 ) )

    strcpy ( (*hit).seq_type, "Low Complexity" );
}  /* check_composition */


/* This function checks a amino acid sequence homology hit identity composition. */
low_aa_complexity ( hit )
t_homology  *hit;		/* homology hit */
{
  int  composition [ MAX_ASCII ];	/* composition array */
  int  count;				/* total character count */
  int  index;				/* composition index */


  /* Tally up the sequence composition by character. */
  seq_composition ( &((*hit).ident), composition, &count );

  if ( count > 0 )
    for ( index = 'A'; index <= 'Z'; index++ )
    {
      if ( ( ( ( composition [ index ] + composition [ index - 'A' + 'a' ] ) * 100 ) / count ) >
                 MAX_AA_PERCENT )
      {
        /* Check if sequence already has a defined type. */
        if ( (*hit).seq_type [ 0 ] != ' ' )
          strcpy ( (*hit).seq_type, "Low Complexity" );
        return ( TRUE );
      }  /* if */
    }  /* for */
  return ( FALSE );
}  /* low_aa_complexity */


/* This function checks for a repeat hit smaller than the GSDB hit. */
check_repeat ( hit, best )
t_homology  *hit;		/* current homology hit */
t_homology  *best;		/* best homology hit */
{
  int         end;		/* overlap end */
  int         index;		/* loop index */
  t_homology  *other;		/* non-repeat hit */
  t_homology  *repeat;		/* repeat hit */
  int         start;		/* overlap start */


  if ( ( (*hit).identities == 0 ) || ( (*best).identities == 0 ) )  return;

  if ( strcmp ( (*hit).database, "REPEAT" ) == 0 )
  {
    repeat = hit;  other = best;
  }  /* if */
  else
    if ( strcmp ( (*best).database, "REPEAT" ) == 0 )
    {
      repeat = best;  other = hit;
    }  /* if */
    else  return;	/* no repetitive homology hit */

  /* Check for an overlap. */
  start = (*repeat).start;
  if ( start < (*other).start )  start = (*other).start;

  end = (*repeat).end;
  if ( end > (*other).end )  end = (*other).end;

  if ( end - start + 1 >= MIN_OVERLAP )
  {
    if ( (*other).start < (*repeat).start )
    {
      for ( index = (*other).start; index < (*repeat).start; index++ )
      {
        (*repeat).region.base [ index ] = (*other).region.base [ index ];
        (*repeat).ident.base  [ index ] = (*other).ident.base [ index ];
        if ( (*other).ident.base [ index ] != ' ' )  (*repeat).identities++;
      }  /* for */
      (*repeat).start = (*other).start;
    }  /* if */

    if ( (*other).end > (*repeat).end )
    {
      for ( index = (*repeat).end + 1; index <= (*other).end; index++ )
      {
        (*repeat).region.base [ index ] = (*other).region.base [ index ];
        (*repeat).ident.base  [ index ] = (*other).ident.base [ index ];
        if ( (*other).ident.base [ index ] != ' ' )  (*repeat).identities++;
      }  /* for */
      (*repeat).end = (*other).end;
    }  /* if */

    if ( (*other).identities > (*repeat).identities )
    {
      (*repeat).identities = (*other).identities;
      (*other).identities--;				/* force repeat larger */
    }  /* if */
    (*repeat).length = (*repeat).end - (*repeat).start + 1;
  }  /* if */
}  /* check_repeat */


/* This function adds a homology hit to the homology hits table. */
add_hit ( hit, hits )
t_homology  *hit;		/* homology hit to add */
t_hits      *hits;		/* homology hits table */
{
  if ( (*hits).total + 1 >= MAX_FEATURES )  return;

  /* Add the homology hit to the homology hits table. */
  copy_homology ( hit, &((*hits).hit [ (*hits).total ]) );

  /* Check if the homology hit is a repetitive element. */
  if ( is_repeat ( hit ) == TRUE )  (*hits).repeats++;

  /* Record the lowest start and highest end of hits in the table. */
  if ( (*hits).start == 0 )  (*hits).start = (*hit).start;

  if ( ( (*hits).start > (*hit).start ) && ( (*hit).start > 0 ) )
    (*hits).start = (*hit).start;

  if ( (*hit).end > (*hits).end )  (*hits).end = (*hit).end;

  (*hits).total++;
}  /* add_hit */


/* This function copies the current homology hit to the best homology hit. */
copy_homology ( hit, best )
t_homology  *hit;		/* current homology hit */
t_homology  *best;		/* best homology hit */
{
  int  index;


  (*best).first = (*hit).first;
  (*best).len   = (*hit).len;
  (*best).start = (*hit).start;
  (*best).end   = (*hit).end;

  (*best).hit_start = (*hit).hit_start;
  (*best).hit_end   = (*hit).hit_end;
  (*best).strand    = (*hit).strand;
  (*best).frame     = (*hit).frame;

  (*best).identities = (*hit).identities;
  (*best).gaps       = (*hit).gaps;
  (*best).percent    = (*hit).percent;
  (*best).length     = (*hit).length;

  strcpy ( (*best).name, (*hit).name );
  strcpy ( (*best).database, (*hit).database );
  strcpy ( (*best).program, (*hit).program );
  strcpy ( (*best).seq_type, (*hit).seq_type );
  strcpy ( (*best).desc_1, (*hit).desc_1 );
  strcpy ( (*best).desc_2, (*hit).desc_2 );

  (*best).region.total = (*hit).region.total;
  (*best).ident.total  = (*hit).ident.total;

  for ( index = 0; ( index < MAX_BASES ) && ( index < (*hit).region.total );
      index++ )

    (*best).region.base [ index ] = (*hit).region.base [ index ];

  for ( index = 0; ( index < MAX_BASES ) && ( index < (*hit).ident.total ); 
      index++ )

    (*best).ident.base [ index ] = (*hit).ident.base [ index ];
}  /* copy_homology */


/* This function intializes the homology hits table. */
init_hits ( hits )
t_hits  *hits;			/* homology hits table */
{
  (*hits).total = 0;
  (*hits).repeats = 0;
  (*hits).start = 0;
  (*hits).end = 0;
}  /* init_hits */


/* This function intializes an homology hit record. */
init_homology ( hit )
t_homology  *hit;		/* homology hit */
{
  int  index;


  /* Initialize. */
  (*hit).start = (*hit).end = (*hit).identities = (*hit).length = 0;
  (*hit).gaps = (*hit).percent = (*hit).hit_start = (*hit).hit_end = 0;
  (*hit).first = 1;
  (*hit).strand = '+';

  (*hit).region.total = (*hit).ident.total = 0;

  for ( index = 0; index < MAX_BASES; index++ )
  {
    (*hit).region.base [ index ] = ' ';
    (*hit).ident.base  [ index ] = ' ';
    (*hit).dashes.base [ index ] = ' ';
  }  /* for */
}  /* init_homology */


/* This function tests if the homology hit is to a repetitive sequence. */
is_repeat ( hit )
t_homology  *hit;		/* homology hit to check */
{
  if ( ( strcmp ( (*hit).seq_type, "Alu" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "MER" ) == 0 ) ||
       ( strcmp ( (*hit).database, "REPEAT" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "THE" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "MLT1d" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "MSTb" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "microsatellite" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "L1" ) == 0 ) )  return ( TRUE );
  return ( FALSE );
}  /* is_repeat */


/* Record no homology hit. */
print_no_homology ( hit, out )
t_homology  *hit;		/* homology hit to print */
t_text      *out;		/* Summary output file */
{
  /* Write the homology hit to the summary output file. */
  fprintf ( (*out).data, "%s\t", (*hit).name );
  fprintf ( (*out).data, "--+-------\t" );
  fprintf ( (*out).data, "%d\t", (*hit).first );			/* begin */
  fprintf ( (*out).data, "%d\t", (*hit).first + (*hit).len - 1 );	/* end */
  fprintf ( (*out).data, "+\t" );	/* strand */
  fprintf ( (*out).data, "0\t" );	/* identities */
  fprintf ( (*out).data, " \t" );	/* feature type */
  fprintf ( (*out).data, " \t" );	/* database */
  fprintf ( (*out).data, " \t" );	/* accession number */
  fprintf ( (*out).data, "0\t" );	/* subject begin */
  fprintf ( (*out).data, "0\t" );	/* subject end */
  fprintf ( (*out).data, " \n" );
}  /* print_no_homology */


/* Print out the homology hit. */
print_homology ( hit, hits, out )
t_homology  *hit;		/* homology hit to print */
t_hits      *hits;		/* homology hits table */
t_text      *out;		/* Summary output file */
{
  int  base;			/* base index */
  int  index;			/* loop index */


  /* Write the homology hit to the summary output file. */
  fprintf ( (*out).data, "%s\t", (*hit).name );
  fprintf ( (*out).data, "--+-------\t" );
  fprintf ( (*out).data, "%d\t", (*hit).start + (*hit).first - 1 );
  fprintf ( (*out).data, "%d\t", (*hit).end + (*hit).first - 1 );
  /* Print out the strand orientation (+ = Plus Strand, - = Minus Strand) */
  if ( (*hit).strand == '-' )
    fprintf ( (*out).data, "-\t" );
  else
    fprintf ( (*out).data, "+\t" );
  fprintf ( (*out).data, "%d\t", (*hit).identities );
/*  fprintf ( (*out).data, "%d\t", (*hit).length ); */
  fprintf ( (*out).data, "%s\t", (*hit).seq_type );
  fprintf ( (*out).data, "%s\t", (*hit).database );
  fprintf ( (*out).data, "ACCNUM\t" );
  fprintf ( (*out).data, "%d\t", (*hit).hit_start );
  fprintf ( (*out).data, "%d\t", (*hit).hit_end );
  fprintf ( (*out).data, "%s",   (*hit).desc_1 );
  fprintf ( (*out).data, "%s\t", (*hit).desc_2 );

  fprintf ( (*out).data, "\n" );

  /* Don't report E. coli, vector, and repeat hits in summary. */
  if ( ( strcmp ( (*hit).seq_type, "vector" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "E.coli" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "cs16" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "rodent" ) == 0 ) ||
       ( is_repeat ( hit ) == TRUE ) )  return;

  /* Check the DNA composition of the homology hit. */
  /* check_composition ( hit ); */

  printf ( "----------------------------------------" );
  printf ( "----------------------------------------\n" );
  printf ( "%s  ", (*hit).name );
  if ( (*hit).length > 0 )
  { 
    if ( strcmp ( (*hit).database, "Swiss" ) != 0 )
    {
      printf ( "%d%% identity,  ", ( (*hit).identities * 100 / (*hit).length ) );
      printf ( "Size %d,  ", (*hit).length );
    }
    else 
    {
      printf ( "%d%% identity,  ", (*hit).identities * 100 / ((*hit).length/3) );
      printf ( "Size %d,  ", ((*hit).length/3) );
    }  /* else */
  }  /* if */
  if ( (*hit).len > 1 )
    printf ( "Length %d,  ", (*hit).len );
  printf ( "Database '%s',  ", (*hit).database );
  printf ( "Sequence type '%s'", (*hit).seq_type );
  if ( (*hit).gaps > 0 )  printf ( ", Gaps %d", (*hit).gaps );
  if ( (*hit).strand == '-' )  printf ( ", Minus Strand" );
  printf ( "\n\n" );

/*  printf ( "Highest Homology: %s", (*hit).desc_1 );
  if ( (*hit).desc_2 [ 0 ] != '\0' )
    printf ( "%s", (*hit).desc_2 ); */

  /* Print out the DNA sequence homology hit. */
  if ( strcmp ( (*hit).database, "Swiss" ) != 0 )
    print_DNA_msa ( hits );
  else
    print_aa_msa ( hits );
}  /* print_homology */


/* This function trims off the low homology ends on amino acid hits. */
trim_aa_ends ( hit )
t_homology  *hit;		/* homology hit to trim */
{
  int  base;			/* small region index */
  int  hit_count = 0;		/* number of identity bases in small region */
  int  index;			/* end index */
  int  in_gap;			/* flag indicating if within a gap */


  /* Check for small alignment. */
  if ( (*hit).identities < MIN_AA_IDENTITIES )  return;

  /* Trim the beginning of the homology hit. */
  index = (*hit).start;
  while ( ( hit_count < 6 ) && ( index < (*hit).end ) )
  {
    /* Add up the identity amino acids in an amino acids window. */
    for ( base = index, hit_count = 0; (base < (*hit).end) && (base < index + 33); base += 3 )
    {
      if ( (*hit).dashes.base [ base/3 ] == '-' )  hit_count--;
      else  if ( (*hit).ident.base [ base/3 ] != ' ' )  hit_count++;
    }  /* for */

    /* Check for low homology. */
    if ( ( hit_count < 6 ) && ( index < (*hit).end ) )
    {
      /* Check if erasing an identity character. */
      if ( (*hit).ident.base [ index/3 ] != ' ' )
      {
        (*hit).identities--;
        (*hit).ident.base [ index/3 ] = ' ';
      }  /* if */

      index = (*hit).start += 3;
    }  /* if */
  }  /* while */

  /* Trim beginning blanks. */
  while ( ( index < (*hit).end ) && ( (*hit).ident.base [ index/3 ] == ' ' ) )

    index = (*hit).start += 3;

  /* Trim the end of the homology hit. */
  index = (*hit).end;
  hit_count = 0;
  while ( ( hit_count < 6 ) && ( index > (*hit).start ) )
  {
    /* Add up the identity amino acids in an amino acids window. */
    for ( base = index, hit_count = 0; (base > (*hit).start) && (base > index - 33); base -= 3 )
    {
      if ( (*hit).dashes.base [ base/3 ] == '-' )  hit_count--;
      else  if ( (*hit).ident.base [ base/3 ] != ' ' )  hit_count++;
    }  /* for */

    /* Check for low homology. */
    if ( ( hit_count < 6 ) && ( index > (*hit).start ) )
    {
      /* Check if erasing an identity character. */
      if ( (*hit).ident.base [ index/3 ] != ' ' )
      {
        (*hit).identities--;
        (*hit).ident.base [ index/3 ] = ' ';
      }  /* if */

      index = (*hit).end -= 3;
    }  /* if */
  }  /* while */

  /* Trim trailing blanks. */
  while ( ( index > (*hit).start ) && ( (*hit).ident.base [ index/3 ] == ' ' ) )

    index = (*hit).end -= 3;

  /* Count the gaps in the highest homology region. */
  for ( base = (*hit).start, in_gap = FALSE; base <= (*hit).end; base += 3 )

    if ( (*hit).dashes.base [ base/3 ] == ' ' )  in_gap = FALSE;
    else
      if ( in_gap == FALSE )
      {
        in_gap = TRUE;
        (*hit).gaps++;
      }  /* if */

/* printf ( "trim_aa_ends, start = %d, end = %d, gaps = %d, identities = %d, %s\n",
  (*hit).start, (*hit).end, (*hit).gaps, (*hit).identities, (*hit).desc_1 );
print_hit ( hit );  */

}  /* trim_aa_ends */


/* This function trims off the low homology ends on DNA hits. */
trim_DNA_ends ( hit )
t_homology  *hit;		/* homology hit to trim */
{
  int  base;			/* small region index */
  int  hit_count = 0;		/* number of identity bases in small region */
  int  index;			/* end index */
  int  in_gap;			/* flag indicating if within a gap */


  /* Check for small alignment. */
  if ( (*hit).identities < MIN_DNA_IDENTITIES )  return;

  /* Trim the beginning of the homology hit. */
  index = (*hit).start;
  while ( ( hit_count < 6 ) && ( index < (*hit).end ) )
  {
    /* Add up the identity amino acids in an amino acids window. */
    for ( base = index, hit_count = 0; (base < (*hit).end) && (base < index + 11); base++ )
    {
      if ( (*hit).dashes.base [ base ] == '-' )  hit_count--;
      else  if ( (*hit).ident.base [ base ] != ' ' )  hit_count++;
    }  /* for */

    /* Check for low homology. */
    if ( ( hit_count < 6 ) && ( index < (*hit).end ) )
    {
      /* Check if erasing an identity character. */
      if ( (*hit).ident.base [ index ] != ' ' )
      {
        (*hit).identities--;
        (*hit).ident.base [ index ] = ' ';
      }  /* if */

      index = (*hit).start++;
    }  /* if */
  }  /* while */

  /* Trim beginning blanks. */
  while ( ( index < (*hit).end ) && ( (*hit).ident.base [ index ] == ' ' ) )

    index = (*hit).start++;

  /* Trim the end of the homology hit. */
  index = (*hit).end;
  hit_count = 0;
  while ( ( hit_count < 6 ) && ( index > (*hit).start ) )
  {
    /* Add up the identity amino acids in an amino acids window. */
    for ( base = index, hit_count = 0; (base > (*hit).start) && (base > index - 11); base-- )
    {
      if ( (*hit).dashes.base [ base ] == '-' )  hit_count--;
      else  if ( (*hit).ident.base [ base ] != ' ' )  hit_count++;
    }  /* for */

    /* Check for low homology. */
    if ( ( hit_count < 6 ) && ( index > (*hit).start ) )
    {
      /* Check if erasing an identity character. */
      if ( (*hit).ident.base [ index ] != ' ' )
      {
        (*hit).identities--;
        (*hit).ident.base [ index ] = ' ';
      }  /* if */

      index = (*hit).end--;
    }  /* if */
  }  /* while */

  /* Trim trailing blanks. */
  while ( ( index > (*hit).start ) && ( (*hit).ident.base [ index ] == ' ' ) )

    index = (*hit).end--;

  /* Count the gaps in the highest homology region. */
  for ( base = (*hit).start, in_gap = FALSE; base <= (*hit).end; base++ )

    if ( (*hit).dashes.base [ base ] == ' ' )  in_gap = FALSE;
    else
      if ( in_gap == FALSE )
      {
        in_gap = TRUE;
        (*hit).gaps++;
      }  /* if */

/* printf ( "trim_DNA_ends, start = %d, end = %d, gaps = %d, identities = %d, %s\n",
  (*hit).start, (*hit).end, (*hit).gaps, (*hit).identities, (*hit).desc_1 );
print_hit ( hit ); */ 

}  /* trim_DNA_ends */


/* This function prints out a hit. */
print_hit ( hit )
t_homology  *hit;		/* homology hit */
{
  int  base;
  int  index;			/* amino acid index */
  char name [ MAX_LINE ];	/* sequence name */


  index = (*hit).start;

  /* Print the amino acid query sequence. */
  printf ( "\n\n  %8d  ", index  + (*hit).first  - 1 );

  for ( base = index; (base <= (*hit).end); base++ )

    printf ( "%c", (*hit).region.base [ base ] );

  printf ( "\n" );

/*   strcpy ( name, &((*hit).desc_1 [ 7 ]) ); */
  strcpy ( name, (*hit).desc_1 );
  name [ stridx ( name, " " ) ] = '\0';

  printf ( "%10s  ", name );

  for ( base = index; (base <= (*hit).end); base++ )

    printf ( "%c", (*hit).ident.base [ base ] );

  printf ( "\n" );
}  /* print_hit */


/* This function prints out a multiple hit multiple sequence alignment. */
print_aa_msa ( hits )
t_hits  *hits;			/* homology hits table */
{
  int  base;
  int  index;			/* amino acid index */
  int  hit_index;		/* hits table index */
  char name [ MAX_LINE ];	/* Swiss protein sequence name */

/*  printf ( "\nhits.repeats = %d\n", (*hits).repeats ); */

  /* Print out the descriptor lines. */
  printf ( "\n" );
  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )

    printf ( "%10s  %s\n", (*hits).hit [ hit_index ].seq_type, (*hits).hit [ hit_index ].desc_1 );

  index = (*hits).start;

  /* Print the amino acid query sequence. */
  hit_index = 0;
  printf ( "\n\n  %8d  ", index  + (*hits).hit [ hit_index ].first  - 1 );

  for ( base = index; (base <= (*hits).end); base += 3 )
  {
    hit_index = 0;
    while ( ( (*hits).hit [ hit_index ].region.base [ base/3 ] == ' ' ) &&
            ( hit_index < (*hits).total ) )  hit_index++;

    if ( hit_index == (*hits).total )  printf ( " " );
    else  printf ( "%c", (*hits).hit [ hit_index ].region.base [ base/3 ] );
  }  /* for */
  printf ( "\n" );

  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )
  {
    strcpy ( name, &((*hits).hit [ hit_index ].desc_1 [ 7 ]) );
    name [ stridx ( name, " " ) ] = '\0';

    /* Print only the database sequence hit bases identical to query seq. */
/*    printf ( "(%7d) ", index  - (*hits).hit [ hit_index ].start + 
          (*hits).hit [ hit_index ].hit_start ); */
    printf ( "%10s  ", name );

    for ( base = index; (base <= (*hits).end); base += 3 )

      printf ( "%c", (*hits).hit [ hit_index ].ident.base [ base/3 ] );
    printf ( "\n" );
  }  /* for */
}  /* print_aa_msa */


/* This function prints out a multiple hit multiple sequence alignment. */
print_DNA_msa ( hits )
t_hits  *hits;			/* homology hits table */
{
  int  base;
  int  index;			/* sequence index */
  int  hit_index;		/* hits table index */
  char name [ MAX_LINE ];	/* Swiss protein sequence name */


  index = (*hits).start;

  /* Print out the descriptor lines. */
  printf ( "\n" );
  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )

    printf ( "%10s  %s\n", (*hits).hit [ hit_index ].seq_type, (*hits).hit [ hit_index ].desc_1 );

  /* Print the query sequence. */
  for ( index = (*hits).start; index <= (*hits).end; index += LINE_LENGTH )
  {
    hit_index = 0;
    printf ( "\n\n  %8d  ", index  + (*hits).hit [ hit_index ].first  - 1 );

    for ( base = index; (base <= (*hits).end) && (base - index < LINE_LENGTH); base++ )
    {
      hit_index = 0;
      while ( ( (*hits).hit [ hit_index ].region.base [ base ] == ' ' ) &&
              ( hit_index < (*hits).total ) )  hit_index++;

      if ( hit_index == (*hits).total )  printf ( " " );
      else  printf ( "%c", (*hits).hit [ hit_index ].region.base [ base ] );
    }  /* for */
    printf ( "\n" );

    for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )
    {
      /* Identify the target sequence. */
      strcpy ( name, &((*hits).hit [ hit_index ].desc_1 [ 1 ]) );
      name [ stridx ( name, " " ) ] = '\0';

      /* Trim off prefixes on dbEST sequences. */
      while ( name [ stridx ( name, "|" ) ] == '|' )

        strcpy ( name, &(name [ stridx ( name, "|" ) + 1 ]) );

      /* Print only the database sequence hit bases identical to query seq. */
      printf ( "%10s  ", name );

      for ( base = index; (base <= (*hits).end) && (base - index < LINE_LENGTH); base++ )

        printf ( "%c", (*hits).hit [ hit_index ].ident.base [ base ] );
      printf ( "\n" );
    }  /* for */
  }  /* for */
}  /* print_DNA_msa */


/* This function processes a homology search results output file. */
process_results ( in, out, repeat, trace )
t_text  *in;		/* search results output file */
t_text  *out;		/* Summary output file */
t_text  *repeat;	/* repeat list for FATAL: errors */
t_text  *trace;		/* debug trace file */
{
  t_homology  hit;	/* homology hit */

  /* Initialization. */
  init_homology ( &hit );
  hit.seq_type [ 0 ] = hit.desc_1 [ 0 ] = hit.desc_2 [ 1 ] = '\0';

  /* Get the query sequence name. */
  /* find_query ( in, &hit ); */
  identify_seq ( in, &hit );

  if ( strcmp ( hit.program, "BLAST" ) == 0 )
    process_blast ( in, out, repeat, trace, &hit );
  else
    if ( strcmp ( hit.program, "FASTA" ) == 0 )
      process_fasta ( in, out, repeat, trace, &hit );
    else
      printf ( "Unknown search program '%s'\n", hit.program );
}  /* process_results */


/* This function identifies the current database. */
identify_database ( in, hit )
t_text  *in;		/* search program output file */
t_homology  *hit;	/* homology hits */
{
  if ( (*in).line [ stridx ( (*in).line, "GSDB" ) ] != '\0' )
    strcpy ( (*hit).database, "GSDB" );

  if ( (*in).line [ stridx ( (*in).line, "REPEAT" ) ] != '\0' )
    strcpy ( (*hit).database, "REPEAT" );

  if ( (*in).line [ stridx ( (*in).line, "sprot" ) ] != '\0' )
  {
    strcpy ( (*hit).database, "Swiss" );
    (*hit).frame++;
  }  /* if */

  if ( (*in).line [ stridx ( (*in).line, "dbEST" ) ] != '\0' )
    strcpy ( (*hit).database, "dbEST" );
}  /* identify_database */


/* This function processes the BLAST output file. */
process_blast ( in, out, repeat, trace, hit )
t_text  *in;		/* BLAST output file */
t_text  *out;		/* Summary output file */
t_text  *repeat;	/* repeat list for FATAL: errors */
t_text  *trace;		/* debug trace file */
t_homology  *hit;	/* homology hits */
{
  int  fatal = 0;				/* count of FATAL reports */
  char fatal_line [ MAX_LINE ];			/* fatal line */
  t_homology  best;				/* homology hits */
  t_hits  hits;					/* homology hits table */

  int    get_int ();				/* get integer function */


  /* Initialization. */
  init_hits ( &hits );
  init_homology ( &best );
  best.seq_type [ 0 ] = best.desc_1 [ 0 ] = best.desc_2 [ 1 ] = '\0';
  fatal_line [ 0 ] = '\0';

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    get_line  ( in );
    get_token ( in );

    /* Check for FATAL error message. */
    if ( strcmp ( (*in).token, "FATAL" ) == 0 )
    {
      strcpy ( fatal_line, (*in).line );

      fatal++;
    }  /* if */

    /* Check for Database: line. */
    if ( ( strcmp ( (*in).token, "Database" ) == 0 ) && 
         ( (*in).line [ 8 ] == ':' ) )
    {
      /* Check if recent hit is better than best hit. */
/*      check_repeat ( hit, &best ); */
      if ( high_homology ( hit ) == TRUE )
      {
        add_hit ( hit, &hits );

        if ( (*hit).identities * (*hit).percent > best.identities * best.percent )

          copy_homology ( hit, &best );
      }  /* if */

      init_homology ( hit );

      /* Identify the current database. */
      identify_database ( in, hit );
    }  /* if */

    /* Check for next hit. */
    if ( ( strcmp ( (*in).token, "Sequences" ) == 0 ) ||
         ( strcmp ( (*in).token, "Score" ) == 0 ) ||
         ( strcmp ( (*in).token, "MINUS" ) == 0 ) ||
         ( (*in).line [ 0 ] == '>' ) )
    {
      /* Check if recent hit is better than best hit. */
/*      check_repeat ( hit, &best ); */
      if ( high_homology ( hit ) == TRUE )
      {
        add_hit ( hit, &hits );

        if ( (*hit).identities * (*hit).percent > best.identities * best.percent )
          copy_homology ( hit, &best );
      }  /* if */

      init_homology ( hit );
    }  /* if */

    /* Check for a description line */
    if ( (*in).line [ 0 ] == '>' ) 
    {
      strcpy ( (*hit).desc_1, &((*in).line [ 1 ]) );
      (*hit).desc_1 [ stridx ( (*hit).desc_1, "\n" ) ] = '\0';

      /* Check for a continued description line. */
      get_line ( in );
      get_token ( in );

      if ( strcmp ( (*in).token, "Length" ) != 0 )
      {
        strcpy ( (*hit).desc_2, &((*in).line [ 0 ]) );

        (*hit).desc_2 [ stridx ( (*hit).desc_2, "\n" ) ] = '\0';
      }  /* if */

      /* Classify the homology sequence type. */
      classify_hit ( hit );
    }  /* if */

    /* Check for Query line. */
    if ( ( strcmp ( (*in).token, "Query" ) == 0 ) &&
         ( (*in).line [ 5 ] == ':' ) )
    {
      /* Process the query line. */
      if ( strcmp ( (*hit).database, "Swiss" ) != 0 )
        query_line ( in, hit );
      else
        aa_query_line ( in, hit );
    }  /* if */
  }  /* while */

  /* Check if recent hit is better than best hit. */
/*  check_repeat ( hit, &best ); */
  if ( high_homology ( hit ) == TRUE )
  {
    add_hit ( hit, &hits );

    if ( (*hit).identities * (*hit).percent > best.identities * best.percent )

      copy_homology ( hit, &best );
  }  /* if */

  init_homology ( hit );

  if ( high_homology ( &best ) == TRUE )
    print_homology ( &best, &hits, out );
  else
  {
    /* Check for FATAL error message. */
    if ( fatal > 0 )
      if ( (*repeat).data != NULL )
        fprintf ( (*repeat).data, "%s\n", (*hit).name );

    print_no_homology ( hit, out );
  }  /* else */
}  /* process_blast */


/* This function processes the FASTA output file. */
process_fasta ( in, out, repeat, trace, hit )
t_text  *in;		/* FASTA output file */
t_text  *out;		/* Summary output file */
t_text  *repeat;	/* repeat list for FATAL: errors */
t_text  *trace;		/* debug trace file */
t_homology  *hit;	/* homology hits */
{
  int  frame = 0;				/* translation frame */
  t_homology  best;				/* homology hits */
  t_hits  hits;					/* homology hits table */

  int    get_int ();				/* get integer function */


  /* Initialization. */
  init_hits ( &hits );
  init_homology ( &best );
  best.seq_type [ 0 ] = best.desc_1 [ 0 ] = best.desc_2 [ 1 ] = '\0';

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    if ( ( (*in).line [ stridx ( (*in).line, "aa)" ) ] == '\0' ) &&
         ( (*in).line [ stridx ( (*in).line, "nt)" ) ] == '\0' ) )
    {
      get_line  ( in );
      get_token ( in );
    }  /* if */

    /* Check for Database: line. */
    if ( strcmp ( (*in).token, "searching" ) == 0 ) 

      identify_database ( in, hit );

    /* Check for a description line */
    if ( ( (*in).line [ stridx ( (*in).line, "aa)" ) ] != '\0' ) ||
         ( (*in).line [ stridx ( (*in).line, "nt)" ) ] != '\0' ) )
    {
      strcpy ( (*hit).desc_1, (*in).line );
      (*hit).desc_1 [ stridx ( (*hit).desc_1, "\n" ) ] = '\0';

      /* Classify the homology sequence type based on description line. */
      classify_hit ( hit );

      /* Process the query line. */
      if ( strcmp ( (*hit).database, "Swiss" ) != 0 )
        fasta_DNA_hit ( in, hit );
      else
      {
        init_homology ( hit );
        fasta_aa_hit ( in, hit );
      }  /* else */

      /* Check if recent hit is better than best hit. */
/*      check_repeat ( hit, &best ); */
      if ( high_homology ( hit ) == TRUE )
      {
        add_hit ( hit, &hits );

        if ( (*hit).identities * (*hit).percent > best.identities * best.percent )
          copy_homology ( hit, &best );
      }  /* if */

      init_homology ( hit );
    }  /* if */
  }  /* while */

  if ( high_homology ( &best ) == TRUE )
    print_homology ( &best, &hits, out );
  else
    print_no_homology ( hit, out );
}  /* process_fasta */


/* This function checks if the homology is significant enough to record. */
high_homology ( best )
t_homology *best;		/* best homology result */
{
  /* Check for no alignments. */
  if ( (*best).length < MIN_AA_IDENTITIES )  return ( FALSE );

  /* Check for protein database search results. */
  if ( strcmp ( (*best).database, "Swiss" ) == 0 )
  {
    /* Check for low amino acid homology complexity. */
    if ( low_aa_complexity ( best ) == TRUE )  return ( FALSE );

    /* Check for significant amino acid homology. */
    if ( ( ( ( (*best).identities - (*best).gaps * GAP_AA_PENALTY ) * 100.0 ) / 
               ((*best).length/3.0) >= MIN_AA_PERCENT ) &&
           ( (*best).identities - (*best).gaps * GAP_AA_IDENTITIES >= MIN_AA_IDENTITIES ) )
          return ( TRUE );
    else  return ( FALSE );
  }
  else
    /* Evalute best DNA homology. */
    if ( ( (*best).identities >= MIN_DNA_IDENTITIES ) &&
         ( ( ( (*best).identities * 100 ) / (*best).length ) >= MIN_DNA_PERCENT ) )
      return ( TRUE );
    else  return ( FALSE );
}  /* high_homology */


/* This function processes the Blast Query: line. */
query_line ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  char DNA_line [ MAX_LINE ];	/* current DNA line */
  int  end;			/* end of homology segment */
  int  index;			/* loop index */
  int  offset;			/* offset from start of line */
  int  start;			/* start of homology segment */

  int  get_int ();		/* get integer function */


  get_token ( in );		/* consume ':' */
  start = get_int ( in );
  get_token ( in );		/* consume DNA sequence */
  end = get_int ( in );

  /* Determine the sequence orientation. */
  if ( start <= end )
  {
    (*hit).strand = '+';

    if ( ( start < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = start;

    if ( end > (*hit).end )  (*hit).end = end;
  }
  else
  {
    (*hit).strand = '-';

    if ( ( end < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = end;

    if ( start > (*hit).end )  (*hit).end = start;
  }  /* else */

  /* Save the DNA line. */
  strcpy ( DNA_line, (*in).line );

  /* Get the identity line. */
  get_line ( in );

  /* Copy the identity bases. */
  for ( index = QUERY_START, offset = 0; index < strlen ( DNA_line ); index++ )

    /* Check for a DNA base. */
    if ( DNA_base [ DNA_line [ index ] ] != 0 ) 
    {
      if ( (*hit).strand == '+' )
      {
        (*hit).region.base [ start + offset ] = DNA_line [ index ];

        if ( (*in).line [ index ] != ' ' )
        {
          if ( (*hit).ident.base [ start + offset ] == ' ' )

            (*hit).identities++;

          (*hit).ident.base [ start + offset ] = DNA_line [ index ];
        }  /* if */
      }
      else
      {
        (*hit).region.base [ start - offset ] = 
            reverse_DNA [ DNA_line [ index ] ];

        if ( (*in).line [ index ] != ' ' )
        {
          if ( (*hit).ident.base [ start - offset ] == ' ' )

            (*hit).identities++;

          (*hit).ident.base [ start - offset ] = 
              reverse_DNA [ DNA_line [ index ] ];
        }  /* if */
      }  /* else */

      offset++;
    }  /* if */

  /* Record the database hit coordinates. */
  get_line ( in );		/* get the Sbjct: line */
  get_token ( in );		/* consume 'Sbjct' */
  get_token ( in );		/* consume ':' */
  start = get_int ( in );

  get_token ( in );		/* consume DNA sequence */
  end = get_int ( in );
  if ( ( (*hit).hit_start == 0 ) || ( start < (*hit).hit_start ) )
    (*hit).hit_start = start;
  if ( end > (*hit).hit_end )  (*hit).hit_end = end;


  if ( (*hit).end >= MAX_BASES )  (*hit).end = MAX_BASES - 1;

  (*hit).region.total = (*hit).end;
  (*hit).ident.total  = (*hit).end;
  (*hit).length = (*hit).end - (*hit).start + 1;
  if ( (*hit).length <= 0 )  (*hit).percent = 0;
  else (*hit).percent = ( (*hit).identities * 100 ) / (*hit).length;
}  /* query_line */


/* This function finds the start and end of homology regions on fasta hits. */
find_ends ( seq_line, coordinates, identities, start, end, start_pos )
char  seq_line [];		/* homology hit sequence line */
char  coordinates [];		/* coordinates of seq_line */
char  identities [];		/* identity sequence characters */
long  *start;			/* start of homology */
long  *end;			/* end of homology */
int   *start_pos;		/* index to start of homology */
{
  long current_pos;	/* current position in sequence */
  int  index;		/* identities index */
  int  pos;		/* line position */

  *start = get_integer ( coordinates );
  *start_pos = *end = 0;

  /* Check for no coordinates. */
  if ( blank ( coordinates ) )  return;

  /* Find the index position of the last digit of the first coordinate. */
  for ( pos = 0; (pos < strlen ( coordinates )) && (coordinates [ pos ] == ' '); pos++ )  ;
  for ( ; (pos < strlen ( coordinates )) && 
          ( ( coordinates [ pos ] >= '0' ) && ( coordinates [ pos ] <= '9' ) ); 
          pos++ )  ;

  if ( ( pos < strlen ( coordinates ) ) && ( pos > 5 ) )
  {
    pos--;    /* set to last digit of first coordinate */

    /* Find the first identity character. */
    for ( index = 0; (index < strlen ( identities )) && 
                     ( (identities [ index ] != 'X' ) && (identities [ index ] != ':') );
          index++ )  ;

    if ( ( identities [ index ] != ':' ) && ( identities [ index ] != 'X' ) )
      return;

    /* If index < *start_pos then backup */
    *start_pos = pos;
    while ( ( index < *start_pos ) && ( *start_pos >= 0 ) )
    {
      if ( seq_line [ *start_pos ] != '-' )  (*start)--;
      (*start_pos)--;
    }  /* if */

    /* If *start_pos < index then advance forward */
    while ( ( *start_pos < index ) && ( *start_pos < strlen ( seq_line ) ) )
    {
      if ( seq_line [ *start_pos ] != '-' )  (*start)++;
      (*start_pos)++;
    }  /* while */
    pos = *start_pos;
    current_pos = *end = *start;

    /* Find the last homology coordinate. */
    while ( ( pos < strlen ( seq_line ) ) && ( pos < strlen ( identities ) ) )
    {
      if ( ( identities [ pos ] == ':' ) || ( identities [ pos ] == 'X' ) )  *end = current_pos;
      if ( seq_line [ pos ] != '-' )  current_pos++;
      pos++;
    }  /* while */
  }  /* if */

/*  printf ( "find_ends: start = %d, end = %d, start_pos = %d\n", *start, *end, *start_pos ); */
/*  printf ( "%s\n%s\n%s\n", coordinates, seq_line, identities ); */
}  /* find_ends */


/* This function processes a single FASTA amino acid hit. */
single_fasta_aa_hit ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  int  index;			/* loop index */
  int  offset;			/* offset from start of line */

  long  query_start;		/* start of query region */
  long  query_end;		/* end of query region */
  int   query_start_pos;	/* line position of query_start */

  long  target_start;		/* start of target region */
  long  target_end;		/* end of target region */
  int   target_start_pos;	/* line position of target_start */

  char  coord_query  [ MAX_LINE ];	/* query coordinates */
  char  query_seq    [ MAX_LINE ];	/* query sequence */
  char  identities   [ MAX_LINE ];      /* identity character line */
  char  target_seq   [ MAX_LINE ];	/* target sequence */
  char  coord_target [ MAX_LINE ];	/* target coordinates */

  int  get_int ();		/* get integer function */


  /* Read and copy the FASTA homology hit. */
  strcpy ( coord_query, (*in).line );
  get_line ( in );
  strcpy ( query_seq, (*in).line );
  get_line ( in );
  strcpy ( identities, (*in).line );
  get_line ( in );
  strcpy ( target_seq, (*in).line );

  /* Check for no identities, target sequences, and target coordinates. */
  if ( (*in).line [ stridx ( (*in).line, "aa)" ) ] != '\0' )  return;
  if ( (*in).line [ stridx ( (*in).line, "Library" ) ] != '\0' )  return;
  get_line ( in );
  strcpy ( coord_target, (*in).line );

  find_ends ( query_seq, coord_query, identities, 
      &query_start, &query_end, &query_start_pos );
  find_ends ( target_seq, coord_target, identities, 
      &target_start, &target_end, &target_start_pos );

  /* Check for line with no coordinates - ignore hit. */
  if ( query_start_pos != target_start_pos )  return;

  if ( ( query_start * 3 < (*hit).start ) || ( (*hit).start == 0 ) )
    (*hit).start = query_start * 3;

  if ( query_end * 3 > (*hit).end )  (*hit).end = query_end * 3;

  /* Copy the identity bases. */
  for ( index = query_start_pos, offset = 0; index < strlen ( query_seq ); index++ )
  {
    /* Check for a gap. */
    if ( ( query_seq [ index ] == '-' ) || ( target_seq [ index ] == '-' ) )

      (*hit).dashes.base [ query_start + offset ] = '-';

    /* Check for an amino acid. */
    if ( Amino_Acid [ query_seq [ index ] ] > 0 ) 
    {
      (*hit).region.base [ query_start + offset ] = query_seq [ index ];

      if ( Amino_Acid [ target_seq [ index ] ] > 0 )
      {
        if ( ( identities [ index ] != ' ' ) && ( query_seq [ index ] == target_seq [ index ] ) )
        {
          (*hit).identities++;
          (*hit).ident.base [ query_start + offset ] = target_seq [ index ];
        }  /* if */
      }  /* if */
    }  /* if */

    if ( query_seq [ index ] != '-' )  offset++;
  }  /* for */

  /* Record the database hit coordinates. */
  if ( ( (*hit).hit_start == 0 ) || ( target_start < (*hit).hit_start ) )
    (*hit).hit_start = target_start;
  if ( target_end > (*hit).hit_end )  (*hit).hit_end = target_end;

}  /* single_fasta_aa_hit */


/* This function processes a FASTA amino acid hit. */
fasta_aa_hit ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  int  end;			/* end of homology segment */
  int  index;			/* loop index */
  int  offset;			/* offset from start of line */
  int  start;			/* start of homology segment */

  int  get_int ();		/* get integer function */


  get_line ( in );		/* consume "initn: ..." line */
  if ( stridx ( (*in).line, "initn" ) != 0 )
    printf ( "initn: line expected; saw '%s'\n", (*in).line );

  get_line ( in );		/* consume "Smith-Waterman ..." line */
  if ( stridx ( (*in).line, "Smith-Waterman" ) != 0 )
    printf ( "Smit-Waterman: line expected; saw '%s'\n", (*in).line );

  get_line ( in );		/* consume blank line */
  if ( blank ( (*in).line ) != TRUE )
    printf ( "blank line expected; saw '%s'\n", (*in).line );

  get_line ( in );		/* first coordinate line */
  (*hit).strand = '+';

  while ( ( (*in).eof != TRUE ) && ( (*in).line [ 0 ] == ' ' ) )
  {
    /* Process a single fasta amino acid hit. */
    single_fasta_aa_hit ( in, hit );
 
    if ( (*in).line [ stridx ( (*in).line, "aa)" ) ] == '\0' ) 
    {
      get_line ( in );		/* get blank line */
      if ( blank ( (*in).line ) != TRUE )
        printf ( "blank line expected; saw '%s'\n", (*in).line );

      get_line ( in );		/* get next line */
    }  /* if */
  }  /* while */

  /* Trim the low homology ends of FASTA hits. */
  trim_aa_ends ( hit );

  if ( (*hit).end >= MAX_BASES )  (*hit).end = MAX_BASES - 1;

  (*hit).region.total = (*hit).end;
  (*hit).ident.total  = (*hit).end;
  (*hit).length = (*hit).end - (*hit).start + 1;
  if ( (*hit).length <= 3 )  (*hit).percent = 0;
  else (*hit).percent = ( (*hit).identities * 100 ) / ( (*hit).length / 3 );
}  /* fasta_aa_hit */


/* This function processes a single FASTA DNA hit. */
single_fasta_DNA_hit ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  int  index;			/* loop index */
  int  offset;			/* offset from start of line */

  long  query_start;		/* start of query region */
  long  query_end;		/* end of query region */
  int   query_start_pos;	/* line position of query_start */

  long  target_start;		/* start of target region */
  long  target_end;		/* end of target region */
  int   target_start_pos;	/* line position of target_start */

  char  coord_query  [ MAX_LINE ];	/* query coordinates */
  char  query_seq    [ MAX_LINE ];	/* query sequence */
  char  identities   [ MAX_LINE ];      /* identity character line */
  char  target_seq   [ MAX_LINE ];	/* target sequence */
  char  coord_target [ MAX_LINE ];	/* target coordinates */

  int  get_int ();		/* get integer function */


  /* Read and copy the FASTA homology hit. */
  strcpy ( coord_query, (*in).line );
  get_line ( in );
  strcpy ( query_seq, (*in).line );

  get_line ( in );
  strcpy ( identities, (*in).line );
  get_line ( in );
  strcpy ( target_seq, (*in).line );

  /* Check for no identities, target sequences, and target coordinates. */
  if ( (*in).line [ stridx ( (*in).line, "nt)" ) ] != '\0' )  return;
  if ( (*in).line [ stridx ( (*in).line, "Library" ) ] != '\0' )  return;
  get_line ( in );
  strcpy ( coord_target, (*in).line );

  find_ends ( query_seq, coord_query, identities, 
      &query_start, &query_end, &query_start_pos );
  find_ends ( target_seq, coord_target, identities, 
      &target_start, &target_end, &target_start_pos );

  /* Check for line with no coordinates - ignore hit. */
  if ( query_start_pos != target_start_pos )  return;

  if ( ( query_start < (*hit).start ) || ( (*hit).start == 0 ) )
    (*hit).start = query_start;

  if ( query_end > (*hit).end )  (*hit).end = query_end;

  /* Copy the identity bases. */
  for ( index = query_start_pos, offset = 0; index < strlen ( query_seq ); index++ )
  {
    /* Check for a gap. */
    if ( ( query_seq [ index ] == '-' ) || ( target_seq [ index ] == '-' ) )

      (*hit).dashes.base [ query_start + offset ] = '-';

    /* Check for a DNA base. */
    if ( DNA_base [ query_seq [ index ] ] > 0 ) 
    {
      (*hit).region.base [ query_start + offset ] = query_seq [ index ];

      if ( DNA_base [ target_seq [ index ] ] > 0 )
      {
        if ( ( identities [ index ] != ' ' ) && ( query_seq [ index ] == target_seq [ index ] ) )
        {
          (*hit).identities++;
          (*hit).ident.base [ query_start + offset ] = target_seq [ index ];
        }  /* if */
      }  /* if */
    }  /* if */

    if ( query_seq [ index ] != '-' )  offset++;
  }  /* for */

  /* Record the database hit coordinates. */
  if ( ( (*hit).hit_start == 0 ) || ( target_start < (*hit).hit_start ) )
    (*hit).hit_start = target_start;
  if ( target_end > (*hit).hit_end )  (*hit).hit_end = target_end;

}  /* single_fasta_DNA_hit */


/* This function processes a FASTA DNA hit. */
fasta_DNA_hit ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  int  end;			/* end of homology segment */
  int  index;			/* loop index */
  int  offset;			/* offset from start of line */
  int  start;			/* start of homology segment */

  int  get_int ();		/* get integer function */


  get_line ( in );		/* consume "initn: ..." line */
  if ( stridx ( (*in).line, "initn" ) != 0 )
    printf ( "initn: line expected; saw '%s'\n", (*in).line );

  get_line ( in );		/* consume "Smith-Waterman ..." line */
  if ( stridx ( (*in).line, "Smith-Waterman" ) != 0 )
    printf ( "Smit-Waterman: line expected; saw '%s'\n", (*in).line );

  get_line ( in );		/* consume blank line */
  if ( blank ( (*in).line ) != TRUE )
    printf ( "1st blank line expected; saw '%s'\n", (*in).line );

  get_line ( in );		/* first coordinate line */
  (*hit).strand = '+';

  while ( ( (*in).eof != TRUE ) && ( (*in).line [ 1 ] == ' ' ) &&
          ( (*in).line [ stridx ( (*in).line, "nt)" ) ] == '\0' ) )
  {
    /* Process a single fasta amino acid hit. */
    single_fasta_DNA_hit ( in, hit );

    if ( (*in).line [ stridx ( (*in).line, "nt)" ) ] == '\0' ) 
    {
      get_line ( in );		/* get blank line */
      if ( blank ( (*in).line ) != TRUE )
        printf ( "2nd blank line expected; saw '%s'\n", (*in).line );

      get_line ( in );		/* get next line */
    }  /* if */
  }  /* while */

  /* Trim the low homology ends of FASTA hits. */
  trim_DNA_ends ( hit );

  if ( (*hit).end >= MAX_BASES )  (*hit).end = MAX_BASES - 1;

  (*hit).region.total = (*hit).end;
  (*hit).ident.total  = (*hit).end;
  (*hit).length = (*hit).end - (*hit).start + 1;
  if ( (*hit).length <= 3 )  (*hit).percent = 0;
  else (*hit).percent = ( (*hit).identities * 100 ) / (*hit).length;
}  /* fasta_DNA_hit */


/* This function processes the Blast amino acid Query: line. */
aa_query_line ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  char aa_line [ MAX_LINE ];	/* current amino acid line */
  int  end;			/* end of homology segment */
  int  index;			/* loop index */
  int  offset;			/* offset from start of line */
  int  start;			/* start of homology segment */

  int  get_int ();		/* get integer function */


  get_token ( in );		/* consume ':' */
  start = get_int ( in );
  get_token ( in );		/* consume sequence */
  end = get_int ( in );

  /* Determine the sequence orientation. */
  if ( start <= end )
  {
    (*hit).strand = '+';

    if ( ( start < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = start;

    if ( end > (*hit).end )  (*hit).end = end;
  }
  else
  {
    (*hit).strand = '-';

    if ( (*hit).len >= start )
    {
      start = (*hit).len - start + 1;
      end = (*hit).len - end + 1;

      if ( ( start < (*hit).start ) || ( (*hit).start == 0 ) )
        (*hit).start = start;

      if ( end > (*hit).end )  (*hit).end = end;
    }
    else
    {
      if ( ( end < (*hit).start ) || ( (*hit).start == 0 ) )
        (*hit).start = end;

      if ( start > (*hit).end )  (*hit).end = start;
      start = end;
    }  /* else */
  }  /* else */

  /* Save the amino acid line. */
  strcpy ( aa_line, (*in).line );

  /* Get the identity line. */
  get_line ( in );

  /* Copy the identity bases. */
  for ( index = QUERY_START, offset = 0; index < strlen ( aa_line ); index++ )

    /* Check for an amino acid. */
    if ( Amino_Acid [ aa_line [ index ] ] > 0 ) 
    {
      (*hit).region.base [ start/3 + offset ] = aa_line [ index ];

      if ( Amino_Acid [ (*in).line [ index ] ] > 0 )
      {
        if ( (*hit).ident.base [ start/3 + offset ] == ' ' )

          (*hit).identities++;

        (*hit).ident.base [ start/3 + offset ] = aa_line [ index ];
      }  /* if */

      offset++;
    }  /* if */

  /* Record the database hit coordinates. */
  get_line ( in );		/* get the Sbjct: line */
  get_token ( in );		/* consume 'Sbjct' */
  get_token ( in );		/* consume ':' */
  start = get_int ( in );

  get_token ( in );		/* consume DNA sequence */
  end = get_int ( in );
  if ( ( (*hit).hit_start == 0 ) || ( start < (*hit).hit_start ) )
    (*hit).hit_start = start;
  if ( end > (*hit).hit_end )  (*hit).hit_end = end;


  if ( (*hit).end >= MAX_BASES )  (*hit).end = MAX_BASES - 1;

  (*hit).region.total = (*hit).end;
  (*hit).ident.total  = (*hit).end;
  (*hit).length = (*hit).end - (*hit).start + 1;
  if ( (*hit).length <= 3 )  (*hit).percent = 0;
  else (*hit).percent = ( (*hit).identities * 100 ) / ( (*hit).length / 3 );
}  /* aa_query_line */


/* This function promts for a file name. */
prompt_file ( text, prompt )
t_text  *text;		/* ASCII text file */
char    prompt [];	/* request prompt */
{
  (*text).name [ 0 ] = '\0';
  (*text).eof = TRUE;

  /* Prompt for a valid file name. */
  while ( ( (*text).eof == TRUE ) && ( strcmp ( (*text).name, "exit" ) != 0 ) )
  {
    printf ( "%s or 'exit' ", prompt );

    scanf ( "%s", (*text).name );

    if ( strcmp ( (*text).name, "exit" ) != 0 )

      open_text_file ( text );
  }  /* while */

  printf ( "\n" );
}  /* promp_file */


/* This function tallies a sequence composition. */
seq_composition ( seq, composition, count )
t_seq  *seq;		/* sequence */
int    composition [];	/* composition */
int    *count;		/* DNA bases or amino acid residues total */
{
  int  index;


  /* Initialization. */
  for ( index = 0; index < MAX_ASCII; index++ )
    composition [ index ] = 0;

    *count = 0;

  /* Total the line composition. */
  for ( index = 0; index < (*seq).total; index++ )
  {
    composition [ (*seq).base [ index ] ]++;

    /* Count the non-blank characters. */
    if ( ( ( (*seq).base [ index ] >= 'A' ) && ( (*seq).base [ index ] <= 'Z' ) ) ||
         ( ( (*seq).base [ index ] >= 'a' ) && ( (*seq).base [ index ] <= 'z' ) ) )  (*count)++;
  }  /* for */
}  /* seq_composition */


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
        printf ( "Maximum number of sequence bases reached.\n" );
    }  /* if */

    get_char ( text );		/* get the next character */
  }  /* while */
}  /* read_DNA_seq */


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


/* Return <0 if s<t, 0 if s==t, >0 if s>t */
strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* strcmp */


/* Copy t to s */
strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* strcpy */


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


/* Return length of string s */
strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* strlen */


/* This function writes out a DNA sequence block. */
write_DNA_seq ( seq, first, last, text )
t_seq	*seq;		/* DNA sequence */
long	first;		/* first base of block */
long	last;		/* last base of block */
t_text  *text;		/* output text file */
{
  int   flag;
  long  index;		/* sequence index */

  /* Print out the DNA block. */
  for ( index = first; index <= last; index++ )
  {
    fprintf ( (*text).data, "%c", (*seq).base [ index ] );
    flag = 1;

    if ( ( ( index - first + 1 ) % 50 ) == 0 )
    {
      fprintf ( (*text).data, "\n" );

      flag = 0;
    }  /* if */
  }  /* for */

  if ( flag == 1 )

    fprintf ( (*text).data, "\n" );

}  /* write_DNA_seq */


/* This function sends mail message to send a file. */
send_email ( name )
char  name [];		/* file name to e-mail */
{
  char  command [ MAX_LINE ];		/* fetch command */


  strcpy ( command, "mail Q@ORNL.GOV < " );
  strcat ( command, name );		/* fetch name */
  printf ( "Sending: %s", name );
  printf ( ", status = %d\n", system ( command ) );
}  /* fetch_seq */


/* This function calls the GCG fetch command to fetch a sequence. */
fetch_seq ( name )
char  name [];		/* sequence name or accession number to fetch */
{
  char  command [ MAX_LINE ];		/* fetch command */


  strcpy ( command, "fetch " );
  strcat ( command, name );		/* fetch name */
  strcat ( command, " -out=TeMp." );	/* fetch name -out= */
  strcat ( command, name );		/* fetch name -out=name */
  strcat ( command, " -Default" );	/* fetch name -out=name -Default */
  printf ( "%s\n", command );
  return ( system ( command ) );
}  /* fetch_seq */


/* This is function "system" from page 229 of the book "The Unix Programming Environment". */
system ( command )
char  *command;		/* command to execute */
{
  int  status, pid, w, tty;
  int (*istat)(), (*qstat)();

  status = 0;
  fflush ( stdout );
  tty = open ( "/dev/tty", 2 );
  if ( tty == -1 )
  {
    printf ( "Could not open '/dev/tty'\n" );
    return -1;
  }  /* if */

  if ((pid = fork ()) == 0 )
  {
    close ( 0 );  dup ( tty );
    close ( 1 );  dup ( tty );
    close ( 2 );  dup ( tty );
    close ( tty );
    execlp ( "sh", "sh", "-c", command, (char *) 0 );
    exit ( 127 );
  }

  close ( tty );
  istat = signal ( SIGINT, SIG_IGN );
  qstat = signal ( SIGQUIT, SIG_IGN );
  while ((w = wait (&status)) != pid && w != -1)  ;

  if ( w == -1 )  status = -1;
  signal ( SIGINT, istat );
  signal ( SIGQUIT, qstat );
  return status;
}  /* system */


/******************************************************************************/
/* This function parses feature qualifiers. */
feature_qualifier ( in, start, end, doc )
t_text  *in;			/* GenBank input flatfile */
long    start;			/* start of region of interest */
long    end;			/* end of region of interest */
t_annotation  *doc;		/* overlapping features */
{
  char  note [ MAX_LINE ];	/* text note */


  /* Check if within a known feature. */
  if ( (*doc).in_feature == FALSE )  return;

  get_token ( in );

  if ( ( strcmp ( (*in).token, "note" ) == 0 ) ||
       ( strcmp ( (*in).token, "function" ) == 0 ) ||
       ( strcmp ( (*in).token, "gene" ) == 0 ) ||
       ( strcmp ( (*in).token, "label" ) == 0 ) ||
       ( strcmp ( (*in).token, "organism" ) == 0 ) ||
       ( strcmp ( (*in).token, "product" ) == 0 ) ||
       ( strcmp ( (*in).token, "rpt_family" ) == 0 ) ||
       ( strcmp ( (*in).token, "standard_name" ) == 0 ) )
  {
    strcpy ( note, (*in).line );
    strcpy ( note, &(note [ stridx ( note, "\"" ) + 1 ]) );
    note [ stridx ( note, "\"" ) ] = '\0';
    if ( (*doc).regions > 0 ) 
    {  
      strcpy ( (*doc).region [ (*doc).current ].note, note );

      if ( strcmp ( (*in).token, "rpt_family" ) == 0 )
        strcpy ( (*doc).region [ (*doc).current ].seq_type, "repeat" );
    }  /* if */
  }  /* if */
}  /* feature_qualifier */


/******************************************************************************/
/* This function parses feature key names. */
check_location ( name, start, end, first, second, doc )
char    name [ ];		/* feature name */
long    start;			/* start of region of interest */
long    end;			/* end of region of interest */
t_annotation  *doc;		/* overlapping features */
{
  long  begin;			/* beginning of feature */
  int   doc_index;		/* annotation table index */
  long  finish;			/* end of feature */


  if ( ( first > end ) || ( second < start ) )  return;    /* not in range */

  /* Check for room in the annotation table. */
  if ( (*doc).regions >= MAX_FEATURES - 1 )
  {
    printf ( "The annotation table filled up.\n" );
    return;
  }  /* if */

  /* Mark the start of a new feature. */
  (*doc).in_feature = TRUE;

  /* Set up the annotation record. */
  strcpy ( (*doc).region [ (*doc).regions ].seq_type, name );

  begin = start;
  if ( start < first )  begin = first;

  finish = end;
  if ( second < end )  finish = second;

printf ( "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" );
printf ( "Before add_pair call in check_location\n" );
print_doc ( doc );

  /* Add the feature to the annotation table. */
  add_pair ( begin, finish, name, "", doc );

  /* Set the current index. */
  if ( find_pair ( begin, finish, doc, &doc_index ) == S_FOUND )

    (*doc).current = doc_index;

printf ( "\nAfter add_pair call in check_location\n" );
print_doc ( doc );
}  /* check_location */


/******************************************************************************/
/* This function parses feature key names. */
feature_location ( in, start, end, doc, name )
t_text  *in;			/* GenBank input flatfile */
long    start;			/* start of region of interest */
long    end;			/* end of region of interest */
t_annotation  *doc;		/* overlapping features */
char    name [ ];		/* feature name */
{
  int   first_coordinate = TRUE;	/* flag if first coordinate */
  long  first = 0;			/* first coordinate of a location */
  int   in_list = FALSE;		/* in multiple coordinates list */
  long  second = 0;			/* second coordinate of a location */


  /* Loop if there are more than one locations listed. */
  do
  {
    get_token2 ( in );

    /* Check for items to ignore. */
    if ( ( strcmp ( (*in).token, "complement" ) == 0 ) ||
         ( strcmp ( (*in).token, "one-of" ) == 0 ) ||
         ( strcmp ( (*in).token, "replace" ) == 0 ) ||
         ( (*in).line [ stridx ( (*in).line, "\"" ) ] == '"' ) )  return;    /* literal sequence */

    /* Check for beyond range specifiers. */
    if ( ( strcmp ( (*in).token, "<" ) == 0 ) || 
         ( strcmp ( (*in).token, ">" ) == 0 ) )  get_token2 ( in );

    /* Check for multiple location lists. */
    if ( ( strcmp ( (*in).token, "join" ) == 0 ) ||
         ( strcmp ( (*in).token, "order" ) == 0 ) ||
         ( strcmp ( (*in).token, "group" ) == 0 ) ) 
    {
      in_list = TRUE;
      get_token2 ( in );		
      if ( strcmp ( (*in).token, "(" ) != 0 )
        printf ( "( expected on join/order/group feature: %s", (*in).line );
      else
        get_token2 ( in );
    }  /* if */

    /* Check for coordinate(s). */
    if ( ( (*in).token [ 0 ] >= '0' ) && ( (*in).token [ 0 ] <= '9' ) )
    {
      if ( first_coordinate == TRUE )
      {
        first = get_integer ( (*in).token ); 
        first_coordinate = FALSE;
      }
      else
      {
        second = get_integer ( (*in).token );
        first_coordinate = TRUE;
        check_location ( name, start, end, first, second, doc );
        first = second = 0;
      }  /* else */
    }  /* if */

    /* Check for a range specification. */
    if ( strcmp ( (*in).token, "." ) == 0 )  get_token2 ( in );    /* ignore second value */

    /* Check for the end of "join", "order", or "group". */
    if ( strcmp ( (*in).token, ")" ) == 0 )  in_list = FALSE;
  }
  while ( ( in_list == TRUE ) || ( first_coordinate == FALSE ) );
}  /* feature_location */


/******************************************************************************/
/* This function parses feature key names. */
feature_name ( in, start, end, doc )
t_text  *in;			/* GenBank input flatfile */
long    start;			/* start of region of interest */
long    end;			/* end of region of interest */
t_annotation  *doc;		/* overlapping features */
{
  char  name [ MAX_LINE ];	/* feature name */


  strcpy ( name, (*in).token );
  if ( ( strcmp ( name, "CDS" ) == 0 ) ||
       ( strcmp ( name, "exon" ) == 0 ) ||
       ( strcmp ( name, "intron" ) == 0 ) ||
       ( strcmp ( name, "insertion_seq" ) == 0 ) ||
       ( strcmp ( name, "LTR" ) == 0 ) ||
       ( strcmp ( name, "mat_peptide" ) == 0 ) ||
       ( strcmp ( name, "misc_feature" ) == 0 ) ||
       ( strcmp ( name, "misc_RNA" ) == 0 ) ||
       ( strcmp ( name, "mRNA" ) == 0 ) ||
       ( strcmp ( name, "precursor_RNA" ) == 0 ) ||
       ( strcmp ( name, "prim_transcript" ) == 0 ) ||
       ( strcmp ( name, "provirus" ) == 0 ) ||
       ( strcmp ( name, "repeat_region" ) == 0 ) ||
       ( strcmp ( name, "rRNA" ) == 0 ) ||
       ( strcmp ( name, "satellite" ) == 0 ) ||
       ( strcmp ( name, "scRNA" ) == 0 ) ||
       ( strcmp ( name, "sig_peptide" ) == 0 ) ||
       ( strcmp ( name, "snRNA" ) == 0 ) ||
       ( strcmp ( name, "transposon" ) == 0 ) ||
       ( strcmp ( name, "tRNA" ) == 0 ) ||
       ( strcmp ( name, "3'UTR" ) == 0 ) ||
       ( strcmp ( name, "5'UTR" ) == 0 ) )
  {
    (*doc).in_feature = FALSE;

    /* Map the standard names into sequence type names. */
    /* Same names: exon, intron, rRNA, satellite, scRNA, snRNA, tRNA, 3'UTR, 5'UTR */
    /* New names: coding, repeat, RNA */
    if ( strcmp ( name, "CDS" ) == 0 )  		strcpy ( name, "coding" );
    if ( strcmp ( name, "insertion_seq" ) == 0 )  	strcpy ( name, "repeat" );
    if ( strcmp ( name, "LTR" ) == 0 )  		strcpy ( name, "repeat" );
    if ( strcmp ( name, "mat_peptide" ) == 0 )  	strcpy ( name, "coding" );
    if ( strcmp ( name, "misc_RNA" ) == 0 )  		strcpy ( name, "RNA" );
    if ( strcmp ( name, "mRNA" ) == 0 )  		strcpy ( name, "exon" );
    if ( strcmp ( name, "precursor_RNA" ) == 0 )  	strcpy ( name, "RNA" );
    if ( strcmp ( name, "prim_transcript" ) == 0 )  	strcpy ( name, "RNA" );
    if ( strcmp ( name, "provirus" ) == 0 )  		strcpy ( name, "repeat" );
    if ( strcmp ( name, "repeat_region" ) == 0 )  	strcpy ( name, "repeat" );
    if ( strcmp ( name, "sig_peptide" ) == 0 )  	strcpy ( name, "exon" );
    if ( strcmp ( name, "transposon" ) == 0 )  		strcpy ( name, "repeat" );

    /* Identify the feature location(s). */
    feature_location ( in, start, end, doc, name );
  }  /* if */
  else  (*doc).in_feature = FALSE;
}  /* feature_name */


/******************************************************************************/
/* This function prints out the annotation table. */
print_doc ( doc )
t_annotation  *doc;		/* overlapping features */
{
  int  index;

  printf ( "%s  ", (*doc).locus );
  printf ( "%s  ", (*doc).accession );

  if ( (*doc).RNA == TRUE )  printf ( "RNA  " );
  else  printf ( "DNA  " );

  printf ( "%s  ", (*doc).organism );
  printf ( "Length %d, ", (*doc).length );
  printf ( "Current %d\n\n", (*doc).current );

  /* Print out each feature/region. */
  for ( index = 0; index < (*doc).regions; index++ )
  {
    printf ( "%10s  ",    (*doc).region [ index ].seq_type );
    printf ( "%d-%d  ", (*doc).region [ index ].start, (*doc).region [ index ].end );
    printf ( "%s\n",    (*doc).region [ index ].note );
  }  /* for */

}  /* print_doc */


/******************************************************************************/
/* This function scans a GenBank file for features in a defined region. */
#define OFFSET_ORGANISM	10	/* index to the organism name position */
scan_GenBank ( in, start, end, doc )
t_text  *in;			/* GenBank input flatfile */
long    start;			/* start of region of interest */
long    end;			/* end of region of interest */
t_annotation  *doc;		/* overlapping features */
{
  int  in_features;		/* in features table flag */


  /* Initialize the annotation table. */
  (*doc).RNA = (*doc).in_feature = FALSE;
  (*doc).locus [ 0 ] = (*doc).accession [ 0 ] = '\0';
  (*doc).definition [ 0 ] = (*doc).organism [ 0 ] = '\0';
  (*doc).current = (*doc).length = (*doc).regions = 0;

  /* Scan the documentation. */
  while ( (*in).eof != TRUE )
  {
    get_token ( in );

    /* Check for locus line. */
    if ( strcmp ( (*in).token, "LOCUS" ) == 0 )
    {
      get_token ( in );
      strcpy ( (*doc).locus, (*in).token );
      (*doc).length = get_int ( in );

      get_token ( in );
      if ( (*in).token [ stridx ( (*in).token, "RNA" ) ] == 'R' )  (*doc).RNA = TRUE;
    }  /* if */

    /* Check for definition line. */
    if ( strcmp ( (*in).token, "DEFINITION" ) == 0 )
      strcpy ( (*doc).definition, (*in).line );

    /* Check for the accession number(s). */
    if ( strcmp ( (*in).token, "ACCESSION" ) == 0 )
    {
      get_token ( in );
      strcpy ( (*doc).accession, (*in).token );
    }  /* if */

    /* Check for the organism identification. */
    if ( strcmp ( (*in).token, "ORGANISM" ) == 0 )
      strcpy ( (*doc).organism, &((*in).line [ OFFSET_ORGANISM ]) );

    /* Check if within the features table. */
    if ( in_features == TRUE )
    {
      /* Check for feature qualifier. */
      if ( (*in).token [ 0 ] == '/' )
        feature_qualifier ( in, start, end, doc );
      else
        if ( ( (*in).line [ 0 ] == ' ' ) && ( (*in).line [ 1 ] == ' ' ) &&
             ( (*in).line [ 2 ] == ' ' ) && ( (*in).line [ 3 ] == ' ' ) &&
             ( (*in).line [ 4 ] == ' ' ) && ( (*in).line [ 5 ] != ' ' ) )
          feature_name ( in, start, end, doc );
    }  /* if */


    /* Check for the beginning of the DNA sequence. */
    if ( ( (*in).line [ stridx ( (*in).line, ".." ) ] == '.' ) ||
         ( ( (*in).line [ 8 ] >= '0' ) && ( (*in).line [ 8 ] <= '9' ) ) )
    {
      in_features = FALSE;
      (*in).eof = TRUE;			/* force exit */
    }  /* if */

    /* Check for the beginning of the features table. */
    if ( strcmp ( (*in).token, "FEATURES" ) == 0 )  in_features = TRUE;

    /* Get the next line of the file. */
    if ( (*in).eof != TRUE )  get_line ( in );
  }  /* while */
}  /* scan_GenBank */


/******************************************************************************/
/* This function sets the mask bit(s) for the current sequence character. */
set_mask ( masks, character, mask )
int		masks [];	/* bit representation look up table */
char		character;	/* current sequence characeter */
unsigned	*mask;		/* conservative substitution mask bits */
{
  if ( (character < 'A') || (character > 'Z') )
    *mask = 0;
  else
    *mask = masks [ character - 'A' ];
  if ( character == '.' )  *mask = NO_BITS;
}  /* set_mask */


/******************************************************************************/
/* This function returns the priority of a feature name. */
priority ( name )
char  name [];		/* feature name */
{
  int  index;		/* feature name list index */


  /* Check all of the feature names. */
  for ( index = 0; strcmp ( feature_names [ index ], "" ) != 0; index++ )

    /* Check for the feature name. */
    if ( strcmp ( feature_names [ index ], name ) == 0 )  return ( index );

  printf ( "priority: feature name '%s' not found.\n", name );

  return ( 0 );		/* lowest priority - not found */
}  /* priority */


/******************************************************************************/
/* This function searches the feature table for the current pair. */
find_feature ( name, start, end, features, index )
char            name [];	/* sequence name */
long		start;		/* start of feature */
long		end;		/* end of feature */
t_hits      *features;	/* features table */
int		*index;		/* table index */
{
  *index = 0;
  if ( (*features).total == 0 )  return;

  /* Find the first occurance of the sequence name. */
  while ( ( (*index) < (*features).total ) &&
          ( strcmp ( name, (*features).hit [ *index ].name ) > 0 ) )

    (*index)++;

  /* Find the first possible overlapping record. */
  while ( ( (*index) < (*features).total ) &&
          ( strcmp ( name, (*features).hit [ *index ].name ) == 0 ) &&
          ( start > (*features).hit [ *index ].end ) )

    (*index)++;
}  /* find_feature */


/******************************************************************************/
/* This function reads in one feature line. */
read_feature ( in, new )
t_text		*in;		/* features file */
t_homology	*new;		/* new feature */
{
  if ( (*in).eof == TRUE )  return;

  /* Read in one feature record. */
  get_tabbed_token ( in );
  strcpy ( (*new).name, (*in).token );			/* name */

  if ( (*in).eof == TRUE )  return;

  /* Truncate the name at the '-' character. */
  (*new).name [ stridx ( (*new).name, "-" ) ] = '\0';

  get_tabbed_token ( in );
  strcpy ( (*new).code, (*in).token );			/* code */

  (*new).start = get_int ( in );			/* start */

  (*new).end = get_int ( in );				/* end */

  get_tabbed_token ( in );
  (*new).strand = (*in).token [ 0 ];			/* strand */

  (*new).identities = get_int ( in );			/* identities */

  get_tabbed_token ( in );
  strcpy ( (*new).seq_type, (*in).token );		/* seq_type */

  get_tabbed_token ( in );
  strcpy ( (*new).database, (*in).token );		/* database */

  get_tabbed_token ( in );
  strcpy ( (*new).accession, (*in).token );		/* accession number */

  (*new).hit_start = get_int ( in );			/* hit_start */

  (*new).hit_end = get_int ( in );			/* hit_end */

  get_tabbed_token ( in );
  strcpy ( (*new).desc_1, (*in).token );		/* description */

  (*new).percent = 0;

  (*new).len = (*new).frame = (*new).gaps = (*new).length = 0;
  (*new).region.total = (*new).ident.total = (*new).dashes.total = 0;
  (*new).region.base [ 0 ] = (*new).ident.base [ 0 ] = (*new).dashes.base [ 0 ] = ' ';
  (*new).accession [ 0 ] = (*new).desc_2 [ 0 ] = (*new).program [ 0 ] = '\0';
}  /* read_feature */


/******************************************************************************/
/* This function load a previous features table. */
reload_features ( in, features )
t_text		*in;		/* previous features table tabbed text file */
t_hits	*features;	/* features table */
{
  int		index;		/* new feature index in features table */
  t_homology	new;		/* new feature */

  (*features).total = 0;

  /* Reload the entire features table. */
  while ( ( (*in).eof != TRUE ) && ( (*features).total + 1 < MAX_FEATURES ) )
  {
    /* Read in the next feature record. */
    read_feature ( in, &new );

    /* Check for end of input file. */
    if ( (*in).eof != TRUE )
    {
      /* Find the insertion point for the new feature. */
      find_feature ( new.name, new.start, new.end, features, &index );

      /* Check for overlapping features. */
      if ( add_feature ( index, &new, features ) == TRUE )

        /* Insert the new feature into the features table. */
        insert_feature ( index, &new, features );
    }  /* if */
  }  /* while */
}  /* reload_features */


/******************************************************************************/
/* This function prints out the features table. */
print_features ( features )
t_hits	*features;	/* features table */
{
  int  index;			/* features table index */


  /* Print out the features table. */
  printf ( "==========================================================================\n" );
  printf ( "Features Table\n\n" );

  for ( index = 0; index < (*features).total; index++ )
  {
    printf ( "%s  ", (*features).hit [ index ].name );
    printf ( "%s  ", (*features).hit [ index ].code );
    printf ( "%d  ", (*features).hit [ index ].start );
    printf ( "%d  ", (*features).hit [ index ].end );
    printf ( "%c  ", (*features).hit [ index ].strand );
    printf ( "%d  ", (*features).hit [ index ].identities );
    printf ( "%s  ", (*features).hit [ index ].seq_type );
    printf ( "%s  ", (*features).hit [ index ].database );
    printf ( "%s  ", (*features).hit [ index ].accession );
    printf ( "%d  ", (*features).hit [ index ].hit_start );
    printf ( "%d  ", (*features).hit [ index ].hit_end );
    printf ( "%s\n", (*features).hit [ index ].desc_1 );

    printf ( "%d  ", (*features).hit [ index ].first );
    printf ( "%d  ", (*features).hit [ index ].len );
    printf ( "%d  ", (*features).hit [ index ].frame );
    printf ( "%d  ", (*features).hit [ index ].gaps );
    printf ( "%d  ", (*features).hit [ index ].percent );
    printf ( "%d  ", (*features).hit [ index ].length );
    printf ( "%s  ", (*features).hit [ index ].accession );
    printf ( "%s  ", (*features).hit [ index ].program );
    printf ( "%s  ", (*features).hit [ index ].desc_2 );
    printf ( "%d  ", (*features).hit [ index ].region.total );
    printf ( "%d  ", (*features).hit [ index ].ident.total );
    printf ( "%d\n", (*features).hit [ index ].dashes.total );
  }  /* for */
  printf ( "\n" );
}  /* print_features */


/******************************************************************************/
/* This function determines if the new feature should be added to the features table. */
add_feature ( index, new, features )
int         index;		/* insertion index */
t_homology   *new;		/* new feature to insert */
t_hits  *features;		/* features table */
{
  long  start, end;		/* overlap start and end */
  long  new_len, old_len;	/* feature lengths */


  /* If the sequences names are different then insert the new feature. */
  if ( strcmp ( (*new).name, (*features).hit [ index ].name ) != 0 )  return ( TRUE );

  /* Determine if the features overlap. */
  start = ( (*new).start > (*features).hit [ index ].start ) ?
          (*new).start : (*features).hit [ index ].start;

  end = ( (*new).end < (*features).hit [ index ].end ) ?
          (*new).end : (*features).hit [ index ].end;

  new_len = (*new).end - (*new).start + 1;
  old_len = (*features).hit [ index ].end - (*features).hit [ index ].start + 1;

  if ( ( ( end - start + 1 ) * OVERLAP_MIN < new_len ) &&
       ( ( end - start + 1 ) * OVERLAP_MIN < old_len ) )  return ( TRUE );

  /* Check for identical feature types for: homology, repeat, L1, Alu, contaminants. */
  if ( strcmp ( (*new).seq_type, (*features).hit [ index ].seq_type ) == 0 )
  {
    if ( ( strcmp ( (*new).seq_type, "homology" ) == 0 ) ||
         ( priority ( (*new).seq_type ) >= priority ( "repeat" ) ) )
    {
      /* Select the best feature. */
      if ( (*new).identities * new_len > (*features).hit [ index ].identities *
          old_len )
      {
        /* Overwrite the old feature. */
        copy_feature ( new, &((*features).hit [ index ]) );
        return ( FALSE );
      }  /* if */
    }  /* if */
  }  /* if */

/* ### */

  return ( TRUE );
}  /* add_feature */


/******************************************************************************/
/* This function copies a sequence. */
copy_seq ( new, old )
t_seq	*new;		/* new sequence */
t_seq	*old;		/* old sequence */
{
  int  index;		/* sequence index */


  /* Copy the sequence length. */
  (*old).total = (*new).total;

  /* Check for no sequence. */
  if ( (*new).total <= 0 )  return;

  /* Copy the sequence. */
  for ( index = 0; index < (*new).total; index++ )

    (*old).base [ index ] = (*new).base [ index ];
}  /* copy_seq */


/******************************************************************************/
/* This function copies a feature. */
copy_feature ( new, old )
t_homology   *new;		/* new feature */
t_homology   *old;		/* old feature to overwrite */
{
  /* copy the new feature on top of the old feature. */
  /* copy the new feature on top of the old feature. */
  (*old).first      = (*new).first;
  (*old).len        = (*new).len;
  (*old).start      = (*new).start;
  (*old).end        = (*new).end;
  (*old).hit_start  = (*new).hit_start;
  (*old).hit_end    = (*new).hit_end;
  (*old).strand     = (*new).strand;
  (*old).frame      = (*new).frame;
  (*old).identities = (*new).identities;
  (*old).gaps       = (*new).gaps;
  (*old).percent    = (*new).percent;
  (*old).length     = (*new).length;

  strcpy ( (*old).name,      (*new).name );
  strcpy ( (*old).code,      (*new).code );
  strcpy ( (*old).seq_type,  (*new).seq_type );
  strcpy ( (*old).program,   (*new).program );
  strcpy ( (*old).database,  (*new).database );
  strcpy ( (*old).accession, (*new).accession );
  strcpy ( (*old).desc_1,    (*new).desc_1 );
  strcpy ( (*old).desc_2,    (*new).desc_2 );

  copy_seq ( (*new).region, (*old).region );
  copy_seq ( (*new).ident,  (*old).ident );
  copy_seq ( (*new).dashes, (*old).dashes );
}  /* copy_feature */


/******************************************************************************/
/* This function inserts a new feature into the features table. */
insert_feature ( index, new, features )
int         index;		/* insertion index */
t_homology   *new;		/* new feature to insert */
t_hits  *features;		/* features table */
{
  int  move;		/* features table moving index */

  /* Check for a full features table. */
  if ( (*features).total + 1 == MAX_FEATURES )
  {
    printf ( "The features table is full.\n" );
    return;
  }  /* if */

  /* Move all of the records from index to the end of the table. */
  for ( move = (*features).total; move >= index;  move-- )

    /* Check for the beginning of the table. */
    if ( move > 0 )
    {
      (*features).hit [ move ].first      = (*features).hit [ move - 1 ].first;
      (*features).hit [ move ].len        = (*features).hit [ move - 1 ].len;
      (*features).hit [ move ].start      = (*features).hit [ move - 1 ].start;
      (*features).hit [ move ].end        = (*features).hit [ move - 1 ].end;
      (*features).hit [ move ].hit_start  = (*features).hit [ move - 1 ].hit_start;
      (*features).hit [ move ].hit_end    = (*features).hit [ move - 1 ].hit_end;
      (*features).hit [ move ].strand     = (*features).hit [ move - 1 ].strand;
      (*features).hit [ move ].frame      = (*features).hit [ move - 1 ].frame;
      (*features).hit [ move ].identities = (*features).hit [ move - 1 ].identities;
      (*features).hit [ move ].gaps       = (*features).hit [ move - 1 ].gaps;
      (*features).hit [ move ].percent    = (*features).hit [ move - 1 ].percent;
      (*features).hit [ move ].length     = (*features).hit [ move - 1 ].length;

      strcpy ( (*features).hit [ move ].name, 
          (*features).hit [ move - 1 ].name );
      strcpy ( (*features).hit [ move ].code, 
          (*features).hit [ move - 1 ].code );
      strcpy ( (*features).hit [ move ].seq_type, 
          (*features).hit [ move - 1 ].seq_type );
      strcpy ( (*features).hit [ move ].program, 
          (*features).hit [ move - 1 ].program);
      strcpy ( (*features).hit [ move ].database, 
          (*features).hit [ move - 1 ].database );
      strcpy ( (*features).hit [ move ].accession, 
          (*features).hit [ move - 1 ].accession );
      strcpy ( (*features).hit [ move ].desc_1, 
          (*features).hit [ move - 1 ].desc_1 );
      strcpy ( (*features).hit [ move ].desc_2, 
          (*features).hit [ move - 1 ].desc_2 );

      copy_seq ( &((*features).hit [ move - 1 ].region),
                 &((*features).hit [ move ].region) );
      copy_seq ( &((*features).hit [ move - 1 ].ident),
                 &((*features).hit [ move ].ident) );
      copy_seq ( &((*features).hit [ move - 1 ].dashes),
                 &((*features).hit [ move ].dashes) );
    }  /* if */

  /* Copy the feature into the features table. */
  copy_feature ( new, &((*features).hit [ index ]) );

  (*features).total++;
}  /* insert_feature */


/******************************************************************************/
/* This function searches the sequence table for the current pair. */
find_pair ( start, end, doc, doc_index )
long		start;		/* start of feature */
long		end;		/* end of feature */
t_annotation	*doc;		/* annotation table */
int		*doc_index;	/* sequence annotation table index */
{
  *doc_index = 0;
  if ( (*doc).regions == 0 )  return ( S_NOT_FOUND );

  while (( (*doc_index) < (*doc).regions ) &&
      ( start > (*doc).region [ *doc_index ].end ))
    (*doc_index)++;

  if ( (*doc_index) == (*doc).regions )  return ( S_NOT_FOUND );

  if ( end < (*doc).region [ *doc_index ].start )  return ( S_NOT_FOUND );

  /* Begin's match */
  if ( start == (*doc).region [ *doc_index ].start )
  {
    if ( end == (*doc).region [ *doc_index ].end )  return ( S_FOUND );
    if ( end < (*doc).region [ *doc_index ].end )
      return ( S_START_COMP );
    else  return ( S_AFTER_COMP );
  }  /* if */

  /* End's match */
  if ( end == (*doc).region [ *doc_index ].end )
  {
    if ( start > (*doc).region [ *doc_index ].start )
      return ( S_END_COMP );
    else  return ( S_BEFORE_COMP );
  }  /* if */

  if (( start < (*doc).region [ *doc_index ].start ) &&
      ( end > (*doc).region [ *doc_index ].end ))
    return ( S_SUPER_COMP );

  if (( start > (*doc).region [ *doc_index ].start ) &&
      ( end > (*doc).region [ *doc_index ].end ))
    return ( S_END_OVERLAP );

  if (( start < (*doc).region [ *doc_index ].start ) &&
      ( end < (*doc).region [ *doc_index ].end ))
    return ( S_START_OVERLAP );

  if (( start > (*doc).region [ *doc_index ].start ) &&
      ( end < (*doc).region [ *doc_index ].end ))
    return ( S_SUB_COMP );

  return ( S_ERROR );
}  /* find_pair */


/******************************************************************************/
/* This function adds a coordinate pair to the sequence. */
add_pair ( start, end, name, note, doc )
long		start;		/* start of feature */
long		end;		/* end of feature */
char            name [];	/* sequence name/type */
char            note [];	/* sequence description */
t_annotation    *doc;		/* annotation table */
{
  int	doc_index;		/* sequence annotation table index */
  long	index;			/* table index */
  long	new_start = 0;		/* split feature old start */
  long	new_end = 0;		/* split feature new end */
  char  new_name [ MAX_NAME ];	/* split feature old name */
  char  new_note [ MAX_NAME ];	/* split feature old note */
  int	status;			/* function return status */


  new_name [ 0 ] = new_note [ 0 ] = '\0';

  if ( end < start )
  {
    printf ( "*WARN* file '%s' (add_pair) start = %d > end = %d.\n", 
        (*doc).accession, start, end );
    return ( S_BAD_PAIR );
  }  /* if */

  if ( (*doc).regions == MAX_FEATURES )
  {
    printf ( "*WARN* Components table overflow; accession number '%s'.\n", 
        (*doc).accession );
    return ( S_TABLE_OVERFLOW );
  }  /* if */

  /* search the sequence annotation table for the coordinate pair. */
  status = find_pair ( start, end, doc, &doc_index );

  if ( status == S_ERROR )
  {
    printf ( "*WARN* error returned from find_pair; accession number '%s'.\n",
        (*doc).accession );
    return ( S_NORMAL );
  }  /* if */

  if ( status == S_FOUND )
  {
    if ( priority ( name ) > priority ( (*doc).region [ doc_index ].seq_type ) )
/*    if ( strcmp ( name, "coding" ) == 0 )  */
      strcpy ( (*doc).region [ doc_index ].seq_type, name );
    return ( S_NORMAL );
  }  /* if */

  /* Insert a blank record into the annotation table. */
  if ( status != S_AFTER_COMP )
  {
    (*doc).regions++;
    if ( (*doc).regions > 0 )
      for ( index = (*doc).regions; index > doc_index; 
          index-- )
      {
        (*doc).region [ index ].start =
            (*doc).region [ index - 1 ].start;
        (*doc).region [ index ].end =
            (*doc).region [ index - 1 ].end;
        strcpy ( (*doc).region [ index ].seq_type, 
            (*doc).region [ index - 1 ].seq_type );
        strcpy ( (*doc).region [ index ].note, 
            (*doc).region [ index - 1 ].note);
    }  /* for */
  }  /* if */

  switch ( status )
  {
    case S_AFTER_COMP:		/* start == .start && end > .end */
      strcpy ( (*doc).region [ doc_index ].seq_type, name );
      new_start = (*doc).region [ doc_index ].end + 1;
      new_end = end;
      strcpy ( new_name, name );
      strcpy ( new_note, note );
      break;

    case S_BEFORE_COMP:		/* start < .start  && end == .end */
      end = (*doc).region [ doc_index ].start - 1;
      break;

    case S_START_COMP:		/* start == .start && end < .end */
      (*doc).region [ doc_index + 1 ].start = end + 1;
      break;

    case S_EMPTY_TABLE:  break;

    case S_END_COMP:		/* start > .start & end == .end */
      doc_index++;		/* insert after current */
      (*doc).region [ doc_index ].end = start - 1;
      break;

    case S_START_OVERLAP:	/* start < .start && end < .end */
      new_start = (*doc).region [ doc_index ].start;
      new_end = end;
      strcpy ( new_name, name );
      strcpy ( new_note, note );
      end = (*doc).region [ doc_index ].start - 1;
      break;

    case S_END_OVERLAP:		/* start > .start && end > .end */
      new_end = end;
      end = (*doc).region [ doc_index ].end;
      (*doc).region [ doc_index ].end = start - 1;
      new_start = end + 1;
      strcpy ( new_name, name );
      strcpy ( new_note, note );
      doc_index++;		/* insert after current */
      break;

    case S_NORMAL:  break;

    case S_NOT_FOUND:  break;

    case S_SUB_COMP:		/* start > .start && end < .end */
      new_start = (*doc).region [ doc_index + 1 ].start;
      new_end = start - 1;
      strcpy ( new_name, (*doc).region [ doc_index + 1 ].seq_type );
      strcpy ( new_note, (*doc).region [ doc_index + 1 ].note );
      (*doc).region [ doc_index + 1 ].start = end + 1;
      break;

    case S_SUPER_COMP:		/* start < .start && end > .end */
      strcpy ( new_name, name );
      strcpy ( new_note, note );
      new_start = (*doc).region [ doc_index + 1 ].end + 1;
      new_end = end;
      end = (*doc).region [ doc_index + 1 ].start - 1;
      break;

    default:
      printf ( "*WARN* accession number '%s': unknown status from find_pair '%d'.\n", 
          (*doc).accession, status );
      break;
  }  /* switch */

  /* Initialize the new record. */
  if ( status != S_AFTER_COMP )
  {
    (*doc).region [ doc_index ].start             = start;
    (*doc).region [ doc_index ].end               = end;
    strcpy ( (*doc).region [ doc_index ].seq_type, name );
    strcpy ( (*doc).region [ doc_index ].note, note ); 
  }  /* if */

  /* Split the old record into two parts. */
  if (( status == S_SUB_COMP ) || ( status == S_SUPER_COMP ) ||
      ( status == S_START_OVERLAP ) || ( status == S_END_OVERLAP ) ||
      ( status == S_AFTER_COMP ))
    add_pair ( new_start, new_end, new_name, new_note, doc );

  return ( S_NORMAL );
}  /* add_pair */
