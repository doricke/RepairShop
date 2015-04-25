
/******************************************************************************/
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

/******************************************************************************/


#define  TRUE		1
#define  FALSE		0

#define  INDEX		"Index"	/* Index name */

#define  LINE_LENGTH	70	/* maximum number of sequence characters on a line */

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      111	/* maximum DNA sequence length */

#define  MAX_DNA_SEQ   501111	/* maximum DNA sequence size */

#define  MAX_FEATURES	10000	/* maximum number of regions or features */

#define  MAX_LINE	1012	/* maximum line length */

#define  MAX_LONG_LINE	5012	/* maximum line length */

#define  MAX_NAME       30	/* maximum name length */

#define  MAX_PREFIX     40	/* maximum dbEST prefix name length */

#define  MAX_SEQUENCES  1002	/* maximum number of DNA sequences */

#define	 NO_BITS	0	/* zero bit mask */



/******************************************************************************/

#define  DNA            1		/* DNA database */
#define  PROTEIN	2		/* protein (amino acid) database */


/******************************************************************************/

#define	MAX_PATTERN	125	/* maximum search pattern size */

#define	MAX_PATTERNS	5000	/* maximum number of patterns */

#define	MAX_PER_LINE	600	/* maximum bases printed per line */

#define MIN_UNITS	2	/* maximum unit length to search up to */
#define MAX_UNITS	4	/* maximum unit length to search up to */

#define AFTER_BASES	10	/* bases to print after a pattern match */
#define BEFORE_BASES	10	/* bases to print before a pattern match */

#define	P_SEQUENCE	1	/* search for unique sequence only */
#define	P_SUBUNIT	2	/* repeat pattern subunit minimum times */

/******************************************************************************/

static  char  complement [ 123 ] = {

/*    1    2    3    4    5    6    7    8    9   10 */
' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 20 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 30 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 40 */
     ' ', ' ', ' ', ' ', '-', '.', ' ', ' ', ' ', ' ',  /* 50 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 60 */
     ' ', ' ', ' ', ' ',                                /* 64 */

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   'T', 'V', 'G', 'H', ' ', ' ', 'C', 'D', ' ', ' ', 'M', ' ', 'K',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'N', ' ', ' ', ' ', 'Y', 'S', 'A', 'A', 'B', 'W', 'N', 'R', ' ',

   ' ', ' ', ' ', ' ', ' ', ' ',

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   't', 'v', 'g', 'h', ' ', ' ', 'c', 'd', ' ', ' ', 'm', ' ', 'k',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'n', ' ', ' ', ' ', 'y', 's', 'a', 'a', 'b', 'w', 'n', 'r', ' '
};

/******************************************************************************/


/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  name [ MAX_LINE ];	/* sequence name */
  char  base [ MAX_DNA_SEQ ];	/* DNA bases */
} t_DNA_seq;

/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  base [ MAX_BASES ];	/* DNA bases */
} t_seq;


/* Homology hit. */
typedef  struct {
  long  first;			/* coordinate of first base of query seq. */
  long  len;			/* length of query sequence */
  long  size;			/* size of query sequence */
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
  char  program  [ MAX_NAME ];	/* search program name */
  char  database [ MAX_NAME ];	/* database name */
  char  seq_type [ MAX_NAME ];	/* sequence classification type */
  char  desc_1 [ MAX_LINE ];	/* Description line 1 */
  char  desc_2 [ MAX_LINE ];	/* Description line 2 */
} t_feature;


/* Homology hit. */
typedef  struct {
  long  first;			/* coordinate of first base of query seq. */
  long  len;			/* length of query sequence */
  int   size;			/* size of query sequence */
  int   start;			/* start of homology */
  int   end;			/* end of homology */
  int   hit_start;		/* start of homology in database hit */
  int   hit_end;		/* end of homology in database hit */
  char  strand;			/* + = Plus DNA strand, - = Minus DNA stand */
  int   frame;			/* translation frame */
  int   identities;		/* number of identity bases */
  int   gaps;			/* number of gaps in the alignment */
  int   percent;		/* percent identity */
  int   length;			/* length of homology */
  char  name [ MAX_LINE ];	/* name of query sequence */
  char  code [ MAX_NAME ];	/* search codes */
  char  accession [ MAX_NAME ];	/* accession number of homology hit */
  char  program  [ MAX_NAME ];	/* program name */
  char  version  [ MAX_NAME ];	/* program version */
  char  database [ MAX_NAME ];	/* database name */
  char  seq_type [ MAX_NAME ];	/* sequence classification type */
  char  desc_1 [ MAX_LINE ];	/* Description line 1 */
  char  desc_2 [ MAX_LINE ];	/* Description line 2 */
  t_seq region;			/* sequence in area of homology */
  t_seq ident;			/* sequence identity bases */
  t_seq dashes;			/* sequence gaps */
  char  doc_type [ MAX_NAME ];	/* documentation sequence type */
  char  kingdom  [ MAX_NAME ];	/* organism kingdom */
  char  organism [ MAX_LINE ];	/* organism of database homology hit */
  char  doc_note [ MAX_LINE ];	/* documentation associated with homology region */
  int   fetched;		/* sequence fetched flag */
  int   db_type;		/* database type */
  char  expect [ MAX_NAME ];	/* Expect value */
  char  score  [ MAX_NAME ];	/* score value */
} t_homology;


/* Multiple homology hits table. */
typedef  struct {
  int   total;			/* number of significant homologies */
  char  seq_name [ MAX_LINE ];	/* sequence name */
  long  start;			/* lowest start of homologies */
  long  end;			/* highest end of homologies */
  t_homology  hit [ MAX_FEATURES ]; /* single homologies */
} t_hits;


/* Multiple homology hits table. */
typedef  struct {
  int   total;			/* number of significant homologies */
  long  start;			/* lowest start of homologies */
  long  end;			/* highest end of homologies */
  t_homology  hit [ MAX_FEATURES ]; /* single homologies */
} t_hits_all;


/* Text file. */
typedef struct {
  char  name [ MAX_LINE ];		/* file name */
  char  next;				/* current character */
  char  token [ MAX_LINE ];		/* current token */
  char  line  [ MAX_LONG_LINE ];	/* current line */
  int   line_index;			/* line index */
  FILE  *data;				/* data file */
  short eof;				/* end of file flag */
} t_text;



/* Search pattern information */
typedef struct {
  int		length;			/* pattern length */
  int		minimum;		/* minimum number of repeats */
  int		type;			/* type of pattern */
  int		count;			/* number of patterns found */
  long          start;			/* start of hit within sequence */
  float		match;			/* percentage of match required */
  char		name [ MAX_NAME ];	/* pattern name */
  char		base [ MAX_PATTERN ];	/* search pattern */
} t_pattern;

/* table of patterns to search for */
typedef struct {
  int		total;				/* total number of patterns */
  t_pattern	pat [ MAX_PATTERNS ];		/* each pattern information */
} t_patterns;


/******************************************************************************/

/* Feature names in priority order from lowest to highest. */
static  char  *feature_names [] = {
  "not found", " ", "-", "ignore", 
  "no_align", 
  "repeats", 
  "simple", 
  "repeat", 
  "last_repeat", 
  "" };


/******************************************************************************/


static  int  dna_mask [ 123 ] = { 0,

/*    1    2    3    4    5    6    7    8    9   10 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 20 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 30 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 40 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 50 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 60 */
      0,   0,   0,   0,   

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1,  14,   4,  11,   0,   0,   2,  13,   0,   0,  10,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   15,   0,   0,   0,   3,   6,   8,   8,   7,   9,   0,  12,   0,

    0,   0,   0,   0,   0,   0,

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1,  14,   4,  11,   0,   0,   2,  13,   0,   0,  10,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   15,   0,   0,   0,   3,   6,   8,   8,   7,   9,   0,  12,   0 };


/******************************************************************************/

static  t_text  summary;			/* Summary of search results */
static  char    last_fetched [ MAX_LINE ];	/* Last accession number fetched */


/******************************************************************************/
main ( argc, argv )
int  argc;			/* number of command line arguments */
char *argv [];			/* command line arguments */
{
  char        current  [ MAX_LINE ];	/* current sequence name */
  FILE        *fopen ();		/* file open function */
  t_text      flanks;			/* file of flanking sequences */
  t_patterns  known_patterns;		/* repeat patterns */
  t_text      patterns;			/* repeat patterns */
  t_text      patterns_file;		/* known patterns input file */
  t_hits      hits;			/* homology hits table */
  char        main_name [ MAX_LINE ];	/* main input sequence key name */
  t_text      results;			/* Tab delimited formatted search results */
  t_DNA_seq   rev_seq;			/* DNA sequence */
  t_DNA_seq   seq;			/* DNA sequence */
  t_text      seq_in;			/* DNA sequence input file */


  /* Initialization. */
  current [ 0 ] = '\0';
  last_fetched [ 0 ] = '\0';
  known_patterns.total = 0;
  init_hits ( &hits );

  /* Issue the Copyright notice. */
  copyright ();

  printf ( "This is the Find_Patterns program.  Version 1.0b  (10/04/2000)\n\n" );

  /* Check for command line parameters. */
  if ( argc > 1 )
  {
    /* Assume the first parameter is the file name of the FASTA sequences to process. */
    d_strcpy ( seq_in.name, argv [ 1 ] );
    open_text_file ( &seq_in );
  }  /* if */
  else
  {
    /* Prompt for the input file name. */
    prompt_file ( &seq_in, "What is the name of the FASTA file?" );
  }  /* else */

  /* Check for the patterns input file name on the command line. */
  if ( argc > 2 )
  {
    /* Assume the second parameter is the file name of the patterns sequences to process. */
    d_strcpy ( patterns_file.name, argv [ 2 ] );
    open_text_file ( &patterns_file );
  }  /* if */
  else
  {
    /* Prompt for the input file name. */
    prompt_file ( &patterns_file, "What is the name of the patterns file?" );
  }  /* else */

  /* Open the the Results output file (tab delimited). */
  d_strcpy ( results.name, seq_in.name );
  results.name [ d_stridx ( results.name, "." ) ] = '\0';
  results.name [ d_stridx ( results.name, " " ) ] = '\0';
  results.name [ d_stridx ( results.name, "\n" ) ] = '\0';
  results.name [ d_stridx ( results.name, "\t" ) ] = '\0';
  d_strcpy ( main_name, results.name );
  d_strcat ( results.name, ".Repeats" );
  results.data = fopen ( results.name, "w" );

  /* Open the text Summary output file. */
  d_strcpy ( summary.name, main_name );
  d_strcat ( summary.name, ".Report" ); 
  summary.data = fopen ( summary.name, "w" );

  /* Open the flanking sequences output file. */
  d_strcpy ( flanks.name, main_name );
  d_strcat ( flanks.name, ".Flanks" ); 
  flanks.data = fopen ( flanks.name, "w" );

  /* Open the repeat patterns output file. */
  d_strcpy ( patterns.name, main_name );
  d_strcat ( patterns.name, ".Patterns" ); 
  patterns.data = fopen ( patterns.name, "w" );

  printf ( "Output files being generated are:\n\n" ); 
  printf ( "\tText report                    \t%s\n", summary.name );
  printf ( "\tPatterns report                \t%s\n", patterns.name );
  printf ( "\tTab delimited results          \t%s\n", results.name );
  printf ( "\tFlanking DNA sequences         \t%s\n", flanks.name );
  printf ( "\n" );

  /* Read in the known patterns. */
  read_patterns ( &patterns_file, &known_patterns );

  /* Open the DNA sequence file for reading. */
  open_text_file ( &seq_in );

  /* Process the sequences. */
  while ( seq_in.eof != TRUE )
  {
    /* Get the sequence name from the description line. */ 
    d_strcpy ( current, &(seq_in.line [ 1 ]) );
    current [ d_stridx ( current, " " ) ] = '\0';
    current [ d_stridx ( current, "\n" ) ] = '\0';
    current [ d_stridx ( current, ".scf" ) ] = '\0';
    current [ d_stridx ( current, ".bin" ) ] = '\0';
 
    /* Advance to the first line of the sequence. */
    get_line ( &seq_in );

    /* Process the results file. */
    if ( seq_in.eof != TRUE )
    {
      /* Read in the DNA sequence. */
      if ( seq_in.data != NULL )

        read_DNA_seq ( &seq_in, &seq );

      /* Place the current sequence name into seq.name for unit_patterns. */
      d_strcpy ( seq.name, current );
      d_strcpy ( hits.hit [ 0 ].name, current );
      d_strcpy ( hits.seq_name, current );

      printf ( "%s forward strand\n", current );

      /* Search for known patterns. */
      fixed_patterns ( &seq, &known_patterns, &hits, &patterns, &flanks, '+' );

      printf ( "\n" );

      /* Complement the sequence. */
      reverse_seq ( &seq, &rev_seq );
/*      d_strcat ( rev_seq.name, "_r" ); */

      printf ( "%s\n", rev_seq.name );

      /* Search for known patterns. */
      fixed_patterns ( &rev_seq, &known_patterns, &hits, &patterns, NULL, '-' );

      printf ( "\n" );

      if ( hits.total > 0 )
      {
        sort_hits ( &hits );

        print_homology ( &hits, &results, &summary );
      }  /* if */

      /* Initialize the hits table for the next sequence. */
      init_hits ( &hits );
      d_strcpy ( hits.hit [ 0 ].name, current );

      d_strcpy ( hits.seq_name, current );
    }  /* if */
  }  /* while */


  /* Close the commands file. */
  fclose ( seq_in.data );
  fclose ( flanks.data );
  fclose ( patterns.data );
  fclose ( results.data );
  fclose ( summary.data );

  printf ( "\nFind_Patterns completed.\n" );
}  /* main */


/******************************************************************************/
/* This function prints the Copyright notice. */
copyright ()
{

  printf ( "Copyright (c) 1992-2000 Darrell O. Ricke, Ph.D.\n" );
  printf ( "\n" );
  printf ( "Darrell O. Ricke DOES NOT MAKE ANY WARRANTY, EXPRESS\n" );
  printf ( "OR IMPLIED, OR ASSUMES ANY LEGAL LIABILITY OR RESPONSIBILITY FOR THE ACCURACY,\n" );
  printf ( "COMPLETENESS, OR USEFULNESS OF ANY INFORMATION, APPARATUS, PRODUCT, OR PROCESS\n" );
  printf ( "DISCLOSED, OR REPRESENTS THAT ITS USE WOULD NOT INFRINGE PRIVATELY OWNED RIGHTS.\n" );
  printf ( "\n" );
  printf ( "\n" );
}  /* copyright */


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
long  get_integer ( line )
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

  d_strcpy ( (*text).token, "" );
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
/* This function gets the next tab delimited (\t) token. */
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

  d_strcpy ( (*text).token, "" );
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

  d_strcpy ( (*text).token, "" );
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
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) != NULL )
    (*text).eof = FALSE;
  else
    return;

  /* Get the first line of text. */
  get_line ( text );
}  /* open_text_file */


/******************************************************************************/
/* This function writes out a DNA sequence block. */
write_DNA_seq ( seq, first, last, text )
t_DNA_seq  *seq;		/* DNA sequence */
long	   first;		/* first base of block */
long	   last;		/* last base of block */
t_text     *text;		/* output text file */
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


/******************************************************************************/
/* This function reduces similarity regions to the annotated region of overlap. */
reduce_hit ( hit, start, end )
t_homology  *hit;	/* current homology hit */
long        end;	/* end of overlap */
long        start;	/* start of overlap */
{
  if ( start > (*hit).hit_start )
  {
    (*hit).start += start - (*hit).hit_start;
    (*hit).hit_start = start;
  }  /* if */

  if ( end < (*hit).hit_end )
  {
    (*hit).end -= (*hit).hit_end - end;
    (*hit).hit_end = end;
  }  /* if */
}  /* reduce_hit */


/******************************************************************************/
/* This function adds a homology hit to the homology hits table. */
add_hit ( hit, hits )
t_homology  *hit;		/* homology hit to add */
t_hits      *hits;		/* homology hits table */
{
  int       ignore_hit = FALSE;
  t_homology  new_hit;		/* merged homology hit */


  if ( (*hits).total + 1 >= MAX_FEATURES )
  {
    printf ( "*WARNING* Maximum features (%d) reached in add_hit.\n", MAX_FEATURES );
    return;
  }  /* if */

  /* Check if current hit is continuation of previous hit. */
  {
    if ( ignore_hit != TRUE )
    {
      /* Add the homology hit to the homology hits table. */
      copy_homology ( hit, &((*hits).hit [ (*hits).total ]) );

      (*hits).total++;
    }  /* if */
    else  if ( ignore_hit == TRUE )  return;
  }  /* if */

  /* Record the lowest start and highest end of hits in the table. */
  if ( (*hits).start == 0 )  (*hits).start = (*hit).start;

  if ( ( (*hits).start > (*hit).start ) && ( (*hit).start > 0 ) )
    (*hits).start = (*hit).start;

  if ( (*hit).end > (*hits).end )  (*hits).end = (*hit).end;
}  /* add_hit */


/******************************************************************************/
/* This function adds a homology hit to the homology hits table. */
add_hit2 ( hit, hits )
t_homology  *hit;		/* homology hit to add */
t_hits      *hits;		/* homology hits table */
{
  int         done;		/* loop termination flag */
  int         hit_index;	/* hits table index */
  int	      ignore_hit = FALSE;
  t_homology  new_hit;		/* merged homology hit */


  /* Check table limit. */
  if ( (*hits).total + 1 >= MAX_FEATURES )
  {
    printf ( "*WARNING* Maximum features (%d) reached in add_hit2.\n", MAX_FEATURES );
    return;
  }  /* if */

  /* Check if current hit is continuation of previous hit. */
  {
    if ( ignore_hit != TRUE )
    {
      /* Shift all of the homology hits up one position. */
      hit_index = (*hits).total - 1;
      done = FALSE;
      if ( (*hit).start >= (*hits).hit [ hit_index ].start ) done = TRUE;
      while ( ( hit_index >= 0 ) && ( done != TRUE ) )
      {
        copy_homology ( &((*hits).hit [ hit_index     ]), 
                        &((*hits).hit [ hit_index + 1 ]) );

        hit_index--;
        if ( hit_index >= 0 )
          if ( (*hit).start >= (*hits).hit [ hit_index ].start ) done = TRUE;
      }  /* while */
      if ( ( (*hit).start == (*hits).hit [ hit_index ].start ) &&
           ( (*hit).end   <  (*hits).hit [ hit_index ].end   ) )  done = FALSE;
      while ( ( hit_index >= 0 ) && ( done != TRUE ) )
      {
        copy_homology ( &((*hits).hit [ hit_index     ]), 
                        &((*hits).hit [ hit_index + 1 ]) );

        hit_index--;
        if ( hit_index >= 0 )
          if ( ( (*hit).start >  (*hits).hit [ hit_index ].start ) ||
               ( (*hit).end   >= (*hits).hit [ hit_index ].end   ) )
             done = TRUE;
      }  /* while */
      if ( hit_index < 0 )  hit_index = 0;

      /* Add the homology hit to the homology hits table. */
      copy_homology ( hit, &((*hits).hit [ hit_index ]) );

      (*hits).total++;
    }  /* if */
    else  if ( ignore_hit == TRUE )  return;
  }  /* if */
}  /* add_hit2 */


/******************************************************************************/
/* This function sorts the hits table. */
sort_hits ( hits )
t_hits      *hits;		/* homology hits table */
{
  t_homology  hit;		/* homology hit */
  int         hit_index;	/* hits table index */
  int         index;		/* hits table index */


  /* Check for an empty hits table. */
  if ( (*hits).total <= 1 )  return;

  /* Check all of the hits in the hits table. */
  for ( hit_index = (*hits).total - 1; hit_index >= 0; hit_index-- )

    /* Search for duplicate accession numbers. */
    for ( index = hit_index - 1; index >= 0; index-- )

      /* Check for hits to swap. */
      if ( (*hits).hit [ index ].first + (*hits).hit [ index ].start >
           (*hits).hit [ hit_index ].first + (*hits).hit [ hit_index ].start )
      {
        /* Copy the hit from the hits table to a separate record. */
        copy_homology ( &((*hits).hit [ hit_index ]), &hit );
        copy_homology ( &((*hits).hit [ index ]), 
                        &((*hits).hit [ hit_index ]) );
        copy_homology ( &hit, &((*hits).hit [ index ]) );
      }  /* if */
}  /* sort_hits */


/******************************************************************************/
/* This function adds a score to the N best scores table. */
add_score ( score, best, limit )
int	score;		/* score to add to the best table */
int	best [];	/* best N scores */
int	limit;		/* N */
{
  int  index;		/* loop/array index */


  /* Add the score if large enough. */
  index = limit - 1;
  while ( index >= 0 )
  {
    if ( best [ index ] < score )
    {
      if ( index < limit - 1 )
        best [ index + 1 ] = best [ index ];
      else
        best [ index ] = score;

      if ( index == 0 )  best [ index ] = score;
    }
    else
    {
      if ( index < limit - 1 )
        if ( best [ index + 1 ] < score )  best [ index + 1 ] = score;
      index = 0;
    }  /* else */

    index--;
  }  /* while */
}  /* add_score */


/******************************************************************************/
/* This function retotals the identities, length, percent, and gaps for a hit. */
retotal ( hit )
t_homology  *hit;		/* homology hit */
{
  long  end = 0;		/* end of homology region */
  int   factor = 1;		/* identity & dashes location factor */
  long  gaps = 0;		/* total number of gaps */
  long  ident = 0;		/* identity bases */
  long  index;			/* sequence index */
  int   in_gap = FALSE;		/* in gap flag */
  long  start = MAX_BASES;	/* start of homology region */

/*
printf ( "\nretotal: " );
printf ( "query = [%d-", (*hit).start );
printf ( "%d] ", (*hit).end );
printf ( "(%d/", (*hit).identities );
printf ( "%d) ", (*hit).length );
printf ( "percent = %d, ", (*hit).percent );
printf ( "target [%d-", (*hit).hit_start );
printf ( "%d]\n", (*hit).hit_end );
*/

  if ( ( d_strcmp ( (*hit).database, "Swiss"    ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "PIR"      ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "nr"       ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "ecoli.aa" ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "GenPept"  ) == 0 ) ||
       ( (*hit).db_type == PROTEIN                   ) )  factor = 3;

  /* Traverse the ident & dashes sequences. */
  for ( index = 0; index < MAX_BASES; index++ )
  {
    /* Find the start and the end of the homology region. */
    if ( (*hit).ident.base [ index ] != ' ' )
    {
      ident++;
      if ( index < start )  start = index;
      if ( index > end )    end = index;
    }  /* if */

    /* Count the total number of gaps. */
    if ( (*hit).dashes.base [ index ] == '-' )
    {
      if ( in_gap != TRUE )  gaps++;
      in_gap = TRUE;
    }  /* if */
  }  /* for */
  (*hit).ident.total = ident;

  if ( start == MAX_BASES )
  {
    printf ( "*WARNING* retotal: no identity bases found for:\n" );
    printf ( "%s vs %s\n", (*hit).name, (*hit).desc_1 );
    printf ( "start %d; end %d; identities %d\n\n", 
        (*hit).start, (*hit).end, (*hit).identities );
    return;
  }  /* if */

  (*hit).length = (*hit).end - (*hit).start + 1;
  (*hit).gaps   = gaps;
  (*hit).identities = ident;

  if ( end - start + 1 > 0 )

      (*hit).percent = ( ident * 100 ) / ( (*hit).length / factor );

  else
    printf ( "*WARNING* retotal: end (%d) < start (%d)\n", end, start );

  if ( (*hit).percent > 100 )  (*hit).percent = 100;

/*
printf ( "retotal: " );
printf ( "start = %d, ", start );
printf ( "end = %d, ", end );
printf ( "identities = %d, ", (*hit).identities );
printf ( "percent = %d, ", (*hit).percent );
printf ( "\n" );
*/
}  /* retotal */


/******************************************************************************/
/* This function copies the current homology hit to the best homology hit. */
copy_homology ( hit, best )
t_homology  *hit;		/* current homology hit */
t_homology  *best;		/* best homology hit */
{
  int  index;


  (*best).first = (*hit).first;
  (*best).len   = (*hit).len;
  (*best).size  = (*hit).size;
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

  d_strcpy ( (*best).name,      (*hit).name );
  d_strcpy ( (*best).database,  (*hit).database );
  d_strcpy ( (*best).accession, (*hit).accession );
  d_strcpy ( (*best).program,   (*hit).program );
  d_strcpy ( (*best).version,   (*hit).version );
  d_strcpy ( (*best).seq_type,  (*hit).seq_type );
  d_strcpy ( (*best).code,      (*hit).code );
  d_strcpy ( (*best).desc_1,    (*hit).desc_1 );
  d_strcpy ( (*best).desc_2,    (*hit).desc_2 );

  (*best).region.total = (*hit).region.total;
  (*best).ident.total  = (*hit).ident.total;
  (*best).dashes.total = (*hit).dashes.total;

  for ( index = 0; index < MAX_BASES; index++ )
  {
    (*best).region.base [ index ] = (*hit).region.base [ index ];
    (*best).ident.base  [ index ] = (*hit).ident.base  [ index ];
    (*best).dashes.base [ index ] = (*hit).dashes.base [ index ];
  }  /* for */

  d_strcpy ( (*best).doc_type, (*hit).doc_type );
  d_strcpy ( (*best).kingdom,  (*hit).kingdom  );
  d_strcpy ( (*best).organism, (*hit).organism );
  d_strcpy ( (*best).doc_note, (*hit).doc_note );
  d_strcpy ( (*best).expect,   (*hit).expect );
  d_strcpy ( (*best).score,    (*hit).score );

  (*best).fetched = (*hit).fetched;
  (*best).db_type = (*hit).db_type;
}  /* copy_homology */


/******************************************************************************/
/* This function intializes the homology hits table. */
init_hits ( hits )
t_hits  *hits;			/* homology hits table */
{
  (*hits).total = 0;
  (*hits).seq_name [ 0 ] = '\0';
  (*hits).start = 0;
  (*hits).end = 0;
}  /* init_hits */


/******************************************************************************/
/* This function intializes a hit record. */
init_homology ( hit )
t_homology  *hit;		/* homology hit */
{
  int  index;


  /* Initialize. */
  (*hit).start = (*hit).end = (*hit).identities = (*hit).length = 0;
  (*hit).gaps = (*hit).percent = (*hit).hit_start = (*hit).hit_end = 0;

  (*hit).region.total = (*hit).ident.total = (*hit).dashes.total = 0;

  for ( index = 0; index < MAX_BASES; index++ )
  {
    (*hit).region.base [ index ] = ' ';
    (*hit).ident.base  [ index ] = ' ';
    (*hit).dashes.base [ index ] = ' ';
  }  /* for */

  (*hit).accession [ 0 ] = (*hit).organism [ 0 ] = 
                           (*hit).kingdom  [ 0 ] =
                           (*hit).expect   [ 0 ] =
                           (*hit).score    [ 0 ] = 
                           (*hit).doc_note [ 0 ] = '\0';
  d_strcpy ( (*hit).doc_type, "similarity" );
  d_strcpy ( (*hit).code, "FFFF" );
}  /* init_homology */


/******************************************************************************/
/* Record no homology hit. */
print_no_homology ( hit, out )
t_homology  *hit;		/* homology hit to print */
t_text      *out;		/* Summary output file */
{
  /* Write the homology hit to the summary output file. */
  (*hit).name [ d_stridx ( (*hit).name, "-" ) ] = '\0';
  fprintf ( (*out).data, "%s\t", (*hit).name );
  fprintf ( (*out).data, "+-+-------\t" );
  fprintf ( (*out).data, "%d\t", (*hit).first );			/* begin */
  fprintf ( (*out).data, "%d\t", (*hit).first + (*hit).size - 1 );	/* end */
  fprintf ( (*out).data, "+\t" );	/* strand */
  fprintf ( (*out).data, "0\t" );	/* identities */
  fprintf ( (*out).data, " \t" );	/* feature type */
  fprintf ( (*out).data, " \t" );	/* database */
  fprintf ( (*out).data, " \t" );	/* accession number */
  fprintf ( (*out).data, "0\t" );	/* subject begin */
  fprintf ( (*out).data, "0\t" );	/* subject end */
  fprintf ( (*out).data, " \n" );
}  /* print_no_homology */


/******************************************************************************/
/* This function records a homology hit. */
record_homology ( hit, out )
t_homology  *hit;		/* homology hit to record */
t_text      *out;		/* Summary output file */
{
  char  desc [ MAX_LINE ];	/* description */

  /* Write the homology hit to the summary output file. */
  fprintf ( (*out).data, "%s\t", (*hit).name );
  fprintf ( (*out).data, "%d\t", (*hit).start + (*hit).first - 1 );
  fprintf ( (*out).data, "%d\t", (*hit).end + (*hit).first - 1 );
  fprintf ( (*out).data, "%c\t", (*hit).strand );
  fprintf ( (*out).data, "%d\t", (*hit).identities );

  if ( (*hit).score [ 0 ] == '\0' )
    fprintf ( (*out).data, "-\t" );
  else
    fprintf ( (*out).data, "%s\t", (*hit).score );

  if ( (*hit).expect [ 0 ] == '\0' )
    fprintf ( (*out).data, "-\t" );
  else
    fprintf ( (*out).data, "%s\t", (*hit).expect );

/*  fprintf ( (*out).data, "%d\t", (*hit).length ); */

  fprintf ( (*out).data, "%s\t", (*hit).seq_type );

  if ( (*hit).database [ 0 ] == '\0' )
    fprintf ( (*out).data, "-\t" );
  else
    fprintf ( (*out).data, "%s\t", (*hit).database );

  if ( (*hit).accession [ 0 ] == '\0' )
    fprintf ( (*out).data, "-\t" );
  else
    fprintf ( (*out).data, "%s\t", (*hit).accession );

  fprintf ( (*out).data, "%d\t", (*hit).hit_start );
  fprintf ( (*out).data, "%d\t", (*hit).hit_end );

  fprintf ( (*out).data, "%s",   (*hit).desc_1 );
  fprintf ( (*out).data, "%s",   (*hit).desc_2 );
  fprintf ( (*out).data, "\n" );
}  /* record_homology */


/******************************************************************************/
/* Print out the homology hit. */
print_homology ( hits, out, summary )
t_hits      *hits;		/* homology hits table */
t_text      *out;		/* Summary report output file (tab delimited) */
t_text      *summary;		/* Summary report output file */
{
  int  index = 0;		/* table index */


  if ( (*hits).total <= 0 )  return;

  /* Don't report E. coli, vector, and repeat hits in summary. */
/*  if ( ( d_strcmp ( (*hit).seq_type, "vector" ) == 0 ) ||
       ( d_strcmp ( (*hit).seq_type, "E.coli" ) == 0 ) ||
       ( d_strcmp ( (*hit).seq_type, "cs16" ) == 0 ) ||
       ( d_strcmp ( (*hit).seq_type, "rodent" ) == 0 ) ||
       ( is_repeat ( hit ) == TRUE ) )  return;
*/

  /* Record the significant homologies. */
  for ( index = 0; index < (*hits).total; index++ )

    record_homology ( &((*hits).hit [ index ]), out );

  fprintf ( (*summary).data, "----------------------------------------" );
  fprintf ( (*summary).data, "----------------------------------------\n" );
  fprintf ( (*summary).data, "%s  ", (*hits).hit [ 0 ].name );
  if ( (*hits).hit [ 0 ].len > 1 )
    fprintf ( (*summary).data, "  Length %d", (*hits).hit [ 0 ].len );
  fprintf ( (*summary).data, "\n" );

  fprintf ( (*summary).data, "\n" );

  /* Print out all of the description lines. */
  print_descriptions ( summary, hits );

  /* Print out the DNA sequence homology hit. */
  print_aa_msa_plus ( summary, hits );
  print_aa_msa_minus ( summary, hits );
  print_DNA_msa ( summary, hits );
}  /* print_homology */


/******************************************************************************/
/* This function prints out a hit. */
print_hit ( hit )
t_homology  *hit;		/* homology hit */
{
  int  base;
  long end;			/* end of print region for each line */
  char name [ MAX_LINE ];	/* sequence name */


  /* Print the amino acid query sequence. */
  printf ( "%10s  ",     (*hit).seq_type );
  printf ( "%3d%%  ",    (*hit).percent );
  printf ( "(%3d/%3d)  ", (*hit).identities, 
      (*hit).end - (*hit).start + 1 );

  if ( (*hit).strand == '-' )
    printf ( "Minus " );
  else
    printf ( " Plus  " );

  printf ( "[%5d-",       (*hit).start + (*hit).first - 1 );
  printf ( "%5d] X ",     (*hit).end   + (*hit).first - 1 );
  printf ( "%6s  ",       (*hit).database );
  printf ( "[%6d-",       (*hit).hit_start );
  printf ( "%6d]\n",      (*hit).hit_end );
  printf ( "\t%s\n",      (*hit).desc_1 );

  end = (*hit).start + 100;

end = (*hit).end; 

  if ( end > (*hit).end )  end = (*hit).end;

  printf ( "  %18d  ", (*hit).start + (*hit).first - 1 );

  for ( base = (*hit).start; (base <= end); base += 3 )

    printf ( "%c", (*hit).region.base [ base/3 ] );

  printf ( "\n" );

  d_strcpy ( name, (*hit).desc_1 );
  name [ d_stridx ( name, " " ) ] = '\0';

  printf ( "%20s  ", name );

  for ( base = (*hit).start; (base <= end); base += 3 )

    printf ( "%c", (*hit).ident.base [ base/3 ] );

  printf ( "\n\n" );
}  /* print_hit */


/******************************************************************************/
/* This function prints out a DNA sequence homology hit. */
print_DNA_hit ( hit )
t_homology  *hit;		/* homology hit */
{
  int  base;
  char name [ MAX_LINE ];	/* sequence name */


  /* Check for no homology hit. */
  if ( (*hit).identities <= 0 )  return;

  /* Print out the description line. */
/*  print_description ( &summary, hit, 1 ); */

  /* Print the DNA query sequence. */
  printf ( "\n\n  %8d  ", (*hit).start + (*hit).first - 1 );

  for ( base = (*hit).start; (base <= (*hit).end); base++ )

    printf ( "%c", (*hit).region.base [ base ] );

  printf ( "\n" );

  d_strcpy ( name, (*hit).desc_1 );

  /* Trim off prefixes on dbEST sequences. */
  while ( ( name [ d_stridx ( name, "|" ) ] == '|' ) &&
          ( d_stridx ( name, "|" ) < MAX_PREFIX ) )

    d_strcpy ( name, &(name [ d_stridx ( name, "|" ) + 1 ]) );

  /* Check for new format with accession number separated. */
  if ( ( name [ 0 ] >= '0' ) && ( name [ 0 ] <= '9' ) )

    d_strcpy ( name, &(name [ d_stridx ( name, " " ) + 1 ]) );

  name [ d_stridx ( name, " " ) ] = '\0';

  printf ( "%10s  ", name );

  for ( base = (*hit).start; (base <= (*hit).end); base++ )

    printf ( "%c", (*hit).ident.base [ base ] );

  printf ( "\n" );
}  /* print_DNA_hit */


/******************************************************************************/
/* This function prints out the coordinates for one homology hit. */
write_coordinates ( summary, hit, hit_number )
t_text      *summary;		/* Summary report output file */
t_homology  *hit;		/* homology hit */
int         hit_number;		/* number of current hit */
{
  if ( (*hit).percent > 100 )  (*hit).percent = 100;

  fprintf ( (*summary).data, "   %10s ",   (*hit).seq_type );
  fprintf ( (*summary).data, " %8s ",      (*hit).accession );
  fprintf ( (*summary).data, "%3d%% ",     (*hit).percent );
  fprintf ( (*summary).data, "%c ", (*hit).strand );
  fprintf ( (*summary).data, "%5s ",    (*hit).program );
  fprintf ( (*summary).data, "[%5d-",   (*hit).start + (*hit).first - 1 );
  fprintf ( (*summary).data, "%5d] X ", (*hit).end   + (*hit).first - 1 );
  fprintf ( (*summary).data, "%s ",     (*hit).database );
  fprintf ( (*summary).data, "[%5d-",   (*hit).hit_start );
  fprintf ( (*summary).data, "%5d] ",   (*hit).hit_end );
  fprintf ( (*summary).data, "\n" );
}  /* write_coordinates */


/******************************************************************************/
/* This function prints out the descriptor lines for one homology hit. */
print_description ( summary, hit, hit_number )
t_text      *summary;		/* Summary report output file */
t_homology  *hit;		/* homology hit */
int         hit_number;		/* number of current hit */
{
  fprintf ( (*summary).data, "%3d  ",      hit_number );
  fprintf ( (*summary).data, "%10s  ",     (*hit).seq_type );
  fprintf ( (*summary).data, "%d%% ",      (*hit).percent );
  fprintf ( (*summary).data, "(%3d/%3d) ", (*hit).identities, 
      (*hit).end - (*hit).start + 1 );

  fprintf ( (*summary).data, "%c strand; ", (*hit).strand );

  fprintf ( (*summary).data, "%s ",    (*hit).program );
  fprintf ( (*summary).data, "[%d-",   (*hit).start + (*hit).first - 1 );
  fprintf ( (*summary).data, "%d] X ", (*hit).end   + (*hit).first - 1 );
  fprintf ( (*summary).data, "%s ",    (*hit).database );
  fprintf ( (*summary).data, "[%d-",   (*hit).hit_start );
  fprintf ( (*summary).data, "%d] ",   (*hit).hit_end );

  fprintf ( (*summary).data, "\n\t  %s %s\n", (*hit).desc_1, (*hit).desc_2 );

  if ( ( (*hit).doc_type [ 0 ] != '\0' ) && ( (*hit).doc_note [ 0 ] != '\0' ) )
  {
    fprintf ( (*summary).data, "\t\t%s  ", (*hit).doc_type );

    if ( (*hit).kingdom [ 0 ] != '\0' )
      fprintf ( (*summary).data, "%s  ", (*hit).kingdom );

    if ( (*hit).organism [ 0 ] != '\0' )
      fprintf ( (*summary).data, "(%s)  ", (*hit).organism );

    fprintf ( (*summary).data, "%s\n", (*hit).doc_note );
  }  /* if */

  fprintf ( (*summary).data, "\n" );
}  /* print_description */


/******************************************************************************/
/* This function prints out the descriptor lines. */
print_descriptions ( summary, hits )
t_text	*summary;		/* Summary report output file */
t_hits  *hits;			/* homology hits table */
{
  int  hit_index;		/* hits table index */


  if ( (*hits).total <= 0 )  return;

  /* Print out the descriptor lines. */
  fprintf ( (*summary).data, "\n" );
  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )
  {
    /* Retotal the identity and percentage. */
    /* retotal ( &((*hits).hit [ hit_index ]) ); */

    /* Print out each description header. */
    print_description ( summary, &((*hits).hit [ hit_index ]), hit_index + 1 );
  }  /* for */
}  /* print_descriptions */


/******************************************************************************/
/* This function writes out the descriptor lines associated with ignoring a hit. */
ignoring_hit ( ignore, ignore_hit, blocking_hit, why )
t_text		*ignore;		/* Summary of ignored hits output file */
t_homology	*ignore_hit;		/* homology hit to ignore */
t_homology	*blocking_hit;		/* blocking homology hit */
char		why [];			/* text description of blocking hit */
{
  if ( (*ignore).data == NULL )  return;

  fprintf ( (*ignore).data, "\n%s\n", why );

  /* Print out the description header. */
  fprintf ( (*ignore).data, "Ignoring:    " );
  print_description ( ignore, ignore_hit, 1 );

  if ( blocking_hit != NULL )
  {
    /* Print out the description header. */
    fprintf ( (*ignore).data, "Blocked by:  " );
    print_description ( ignore, blocking_hit, 1 );
  }  /* if */
}  /* ignoring_hit */


/******************************************************************************/
/* This function prints out a multiple hit multiple sequence alignment. */
print_aa_header ( out, hits, protein_index, index, strand, program )
t_text  *out;			/* output file */
t_hits  *hits;			/* homology hits table */
int     protein_index;		/* first protein sequence index in hits table */
int     index;			/* amino acid sequence index */
char	strand;			/* DNA strand orientation '+' or '-' */
char	program [];		/* program name "BLAST" or "FASTA" */
{
  int  base;			/* current DNA base index of amino acid */
  int  found;			/* amino acid search flag */
  int  hit_index;		/* hits table index */


  /* Print only protein homology hits. */
  if ( ( ( d_strcmp ( (*hits).hit [ protein_index ].database, "Swiss"    ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "PIR"      ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "nr"       ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "ecoli.aa" ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "GenPept"  ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].program,  "TBLAST"   ) != 0 ) ) ||
       ( (*hits).hit [ protein_index ].strand != strand ) )  return;

  /* Print the amino acid query sequence. */
  fprintf ( (*out).data, "\n %8d %c ", index, strand );

  if ( strand == '+' )
    for ( base = index; (base <= (*hits).end) && (base - index < LINE_LENGTH * 3); base += 3 )
    {
      hit_index = protein_index;
      found = FALSE;
      while ( ( found == FALSE ) &&
              ( hit_index < (*hits).total ) )  
      {
        if ( ( priority ( (*hits).hit [ hit_index ].seq_type ) 
                    >= priority ( "no_align" ) ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "GSDB"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenBank"   ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "nt"        ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "Ath_mito"        ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "rice_chloro"        ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpri"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpln"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "REPEAT"    ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "RepBase"   ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "SELF"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbEST"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "Arab. EST" ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbSTS"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "Fugu"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "vector"    ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].program, program      ) != 0 ) ||
             ( (*hits).hit [ hit_index ].strand == '-' ) ) 
          hit_index++;
        else 
          if ( ( (base - (*hits).hit [ hit_index ].first + 1) / 3 >= 0 ) &&
               ( (base - (*hits).hit [ hit_index ].first + 1) / 3 < MAX_BASES ) )
          {
            if ( ( base - (*hits).hit [ hit_index ].first + 1 < 
                          (*hits).hit [ hit_index ].start ) ||
                 ( base - (*hits).hit [ hit_index ].first + 1 >
                          (*hits).hit [ hit_index ].end ) )
              hit_index++;
            else
              if ( (*hits).hit [ hit_index ].region.base 
                   [ (base - (*hits).hit [ hit_index ].first + 1) / 3 ] != ' ' ) found = TRUE;
              else  hit_index++;
          }
          else  hit_index++;
      }  /* while */

      if ( hit_index == (*hits).total )  fprintf ( (*out).data, " " );
      else
      {
        fprintf ( (*out).data, "%c", 
            (*hits).hit [ hit_index ].region.base 
            [ (base - (*hits).hit [ hit_index ].first + 1) / 3 ] );
      }  /* else */
    }  /* for */
  else
    for ( base = index; (base >= (*hits).start) && (index - base < LINE_LENGTH * 3); base -= 3 )
    {
      hit_index = protein_index;
      found = FALSE;
      while ( ( found == FALSE ) &&
              ( hit_index < (*hits).total ) )  
      {
        if ( ( priority ( (*hits).hit [ hit_index ].seq_type ) 
                    >= priority ( "no_align" ) ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "GSDB"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenBank"   ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "nt"        ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "Ath_mito"        ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "rice_chloro"        ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpri"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpln"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "REPEAT"    ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "RepBase"   ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "SELF"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbEST"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "Arab. EST" ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbSTS"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "Fugu"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "vector"    ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli"     ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].program, program      ) != 0 ) ||
             ( (*hits).hit [ hit_index ].strand == '+' ) ) 
          hit_index++;
        else 
          if ( ( (base - (*hits).hit [ hit_index ].first + 1) / 3 >= 0 ) &&
               ( (base - (*hits).hit [ hit_index ].first + 1) / 3 < MAX_BASES ) )
          {
            if ( ( base - (*hits).hit [ hit_index ].first + 1 < 
                          (*hits).hit [ hit_index ].start ) ||
                 ( base - (*hits).hit [ hit_index ].first + 1 >
                          (*hits).hit [ hit_index ].end ) )
              hit_index++;
            else
              if ( (*hits).hit [ hit_index ].region.base 
                   [ (base - (*hits).hit [ hit_index ].first + 1) / 3 ] != ' ' ) found = TRUE;
              else  hit_index++;
          }
          else  hit_index++;
      }  /* while */

      if ( hit_index == (*hits).total )  fprintf ( (*out).data, " " );
      else
      {
        fprintf ( (*out).data, "%c", 
            (*hits).hit [ hit_index ].region.base 
            [ (base - (*hits).hit [ hit_index ].first + 1) / 3 ] );
      }  /* else */
    }  /* for */
  fprintf ( (*out).data, "\n" );
}  /* print_aa_header */


/******************************************************************************/
/* This function prints out a multiple hit multiple sequence alignment. */
print_aa_msa_plus ( out, hits )
t_text	*out;			/* output file */
t_hits  *hits;			/* homology hits table */
{
  int  base;			/* current DNA base index of amino acid */
  int  factor = 3;		/* numbering system factor */
  int  found;			/* found flag */
  int  hit_index;		/* hits table index */
  int  index;			/* amino acid index */
  int  line_index;		/* line index */
  char name [ MAX_LINE ];	/* Swiss protein sequence name */
  char previous [ MAX_NAME ];	/* Previous search program name */
  int  protein_index;		/* first protein sequence index in hits table */


  previous [ 0 ] = '\0';

  /* Search for a protein sequence homology hit. */
  protein_index = 0;
  while ( ( ( d_strcmp ( (*hits).hit [ protein_index ].database, "GSDB"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "GenBank" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "nt"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "Ath_mito"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "rice_chloro"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "gbpri"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "gbpln"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "dbEST"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "Arab. EST" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "dbSTS"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "Fugu"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "REPEAT"  ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "RepBase" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "SELF"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "vector"  ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "ecoli"   ) == 0 ) ||
            ( (*hits).hit [ protein_index ].strand == '-' ) || 
            ( priority ( (*hits).hit [ protein_index ].seq_type ) >= 
              priority ( "no_align" ) ) ) &&
          ( protein_index < (*hits).total - 1 ) )
    protein_index++;

  /* If protein sequence homology hit is not found then return. */
  if ( ( ( d_strcmp ( (*hits).hit [ protein_index ].database, "Swiss"    ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "PIR"      ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "nr"       ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "ecoli.aa" ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "GenPept"  ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].program,  "TBLAST"   ) != 0 ) ) || 
       ( (*hits).hit [ protein_index ].strand == '-' ) || 
       ( priority ( (*hits).hit [ protein_index ].seq_type ) >= 
         priority ( "no_align" ) ) )  return;

  /* Reset the hit range. */
  (*hits).start = MAX_DNA_SEQ;
  (*hits).end   = 0;
  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )

    /* Check for protein sequence homology hits. */
    if ( ( ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) == 0 ) ) && 
         ( (*hits).hit [ hit_index ].strand == '+' ) && 
         ( priority ( (*hits).hit [ hit_index ].seq_type ) < priority ( "no_align" ) ) )
    {
      if ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1 
          < (*hits).start )

        (*hits).start = (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1;

      if ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1 > (*hits).end )
        (*hits).end = (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1;
    }  /* if */

  /* Print out all of the protein sequence homology hits. */
  for ( index = (*hits).start; index <= (*hits).end; index += LINE_LENGTH * 3 )
  {
    /* Check for break between homology hits. */
    if ( index > (*hits).start )
    {
      found = FALSE;
      for ( base = index; (base <= (*hits).end) && (found != TRUE); base += 3 )
      {
        hit_index = protein_index;
        while ( ( ( (*hits).hit [ hit_index ].region.base 
                      [ (base - (*hits).hit [ hit_index ].first + 1) / 3 ] == ' ' ) ||

                  ( base < (*hits).hit [ hit_index ].start + 
                      (*hits).hit [ hit_index ].first - 1 ) ||

                  ( base > (*hits).hit [ hit_index ].end + 
                      (*hits).hit [ hit_index ].first - 1 ) ||

                  ( priority ( (*hits).hit [ hit_index ].seq_type ) 
                      >= priority ( "no_align" ) ) ||

                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "GSDB"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenBank" ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "nt"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Ath_mito"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "rice_chloro"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpri"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpln"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbEST"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Arab. EST" ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbSTS"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Fugu"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "SELF"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "vector"  ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "RepBase" ) == 0 ) ||
                  ( (*hits).hit [ hit_index ].strand == '-' ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "REPEAT"  ) == 0 ) ) && 
                ( hit_index < (*hits).total ) )  hit_index++;

        if ( hit_index < (*hits).total )  found = TRUE;
      }  /* for */

      if ( ( base - 3 > index ) && ( found == TRUE ) )
      {
        fprintf ( (*out).data, "\n            ------------------------------------" );
        fprintf ( (*out).data, "------------------------------------------------\n" );
        index = base - 3;
      }  /* if */
      else
        if ( found == FALSE )  index = base;
    }  /* if */

    /* For each hit in the table. */
    d_strcpy ( previous, "xx" );
    for ( hit_index = protein_index; hit_index < (*hits).total; hit_index++ )

      /* If protein sequence homology hit is not found then return. */
      if ( ( ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) == 0 ) ) && 
             ( (*hits).hit [ hit_index ].strand == '+' ) &&
           ( priority ( (*hits).hit [ hit_index ].seq_type ) < priority ( "no_align" ) ) )
      {
        /* Check if amino acids on this line. */
        if ( ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1 
                 < index + LINE_LENGTH * 3 ) &&
             ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1 > index ) )
        {
          /* Check for change in search program names. */
          if ( ( previous [ 0 ] != '\0' ) && 
               ( d_strcmp ( previous, (*hits).hit [ hit_index ].program ) != 0 ) )
          {
            fprintf ( (*out).data, "\n%s Search Results:\n", 
                (*hits).hit [ hit_index ].program );
            print_aa_header ( out, hits, hit_index, index,
                (*hits).hit [ hit_index ].strand,
                (*hits).hit [ hit_index ].program );
          }  /* if */
          d_strcpy ( previous, (*hits).hit [ hit_index ].program );

          /* Identify the target sequence. */
          get_name ( &((*hits).hit [ hit_index ]), name );
          name [ 10 ] = '\0';		/* truncate long SELF names */

          /* Print only the database sequence hit bases identical to query seq. */
          fprintf ( (*out).data, "%10s  ", name );

          /* TBLAST numbering system factor. */
          factor = 3;
          if ( d_strcmp ( (*hits).hit [ hit_index ].program, "TBLAST" ) == 0 )  factor = 1;

          /* For each amino acid on this line. */
          for ( base = index; (base <= (*hits).end) && (base - index < LINE_LENGTH * factor); 
                base += factor )
          {
            if ( ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1
                   <= base ) &&
                 ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1
                   >= base ) )

              fprintf ( (*out).data, "%c", 
                  (*hits).hit [ hit_index ].ident.base 
                  [ (base - (*hits).hit [ hit_index ].first + 1) / factor ] );

            else
              fprintf ( (*out).data, " " );
          }  /* for */

          fprintf ( (*out).data, "\n" );
        }  /* if */
      }  /* if */
  }  /* for */
}  /* print_aa_msa_plus */


/******************************************************************************/
/* This function prints out a multiple hit multiple sequence alignment. */
print_aa_msa_minus ( out, hits )
t_text	*out;			/* output file */
t_hits  *hits;			/* homology hits table */
{
  int  base;			/* current DNA base index of amino acid */
  int  factor;			/* translation factor */
  int  found;			/* found flag */
  int  hit_index;		/* hits table index */
  int  index;			/* amino acid index */
  int  line_index;		/* line index */
  char name [ MAX_LINE ];	/* Swiss protein sequence name */
  char previous [ MAX_NAME ];	/* Previous search program name */
  int  protein_index;		/* first protein sequence index in hits table */


  previous [ 0 ] = '\0';

  /* Search for a protein sequence homology hit. */
  protein_index = 0;
  while ( ( ( d_strcmp ( (*hits).hit [ protein_index ].database, "GSDB"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "GenBank" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "nt"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "Ath_mito"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "rice_chloro"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "gbpri"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "gbpln"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "dbEST"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "Arab. EST" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "dbSTS"   ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "Fugu"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "REPEAT"  ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "RepBase" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "SELF"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "vector"  ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ protein_index ].database, "ecoli"   ) == 0 ) ||
            ( (*hits).hit [ protein_index ].strand == '+' ) || 
            ( priority ( (*hits).hit [ protein_index ].seq_type ) >= 
              priority ( "no_align" ) ) ) &&
          ( protein_index < (*hits).total - 1 ) )
    protein_index++;

  /* If protein sequence homology hit is not found then return. */
  if ( ( ( d_strcmp ( (*hits).hit [ protein_index ].database, "Swiss"    ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "PIR"      ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "nr"       ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "ecoli.aa" ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].database, "GenPept"  ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ protein_index ].program,  "TBLAST"   ) != 0 ) ) || 
       ( (*hits).hit [ protein_index ].strand == '+' ) || 
       ( priority ( (*hits).hit [ protein_index ].seq_type ) >= 
         priority ( "no_align" ) ) )  return;

  /* Reset the hit range. */
  (*hits).start = MAX_DNA_SEQ;
  (*hits).end   = 0;
  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )

    /* Check for protein sequence homology hits. */
    if ( ( ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) == 0 ) ||
           ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) == 0 ) ) && 
         ( (*hits).hit [ hit_index ].strand == '-' ) && 
         ( priority ( (*hits).hit [ hit_index ].seq_type ) < priority ( "no_align" ) ) )
    {
      if ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1 
          < (*hits).start )

        (*hits).start = (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1;

      if ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1 > (*hits).end )
        (*hits).end = (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1;
    }  /* if */

  /* Print out all of the protein sequence homology hits. */
  for ( index = (*hits).end; index >= (*hits).start; index -= LINE_LENGTH * 3 )
  {
    /* Check for break between homology hits. */
    if ( index < (*hits).end )
    {
      found = FALSE;
      for ( base = index; (base >= (*hits).start) && (found != TRUE); base -= 3 )
      {
        hit_index = protein_index;
        while ( ( ( (*hits).hit [ hit_index ].region.base 
                      [ (base - (*hits).hit [ hit_index ].first + 1) / 3 ] == ' ' ) ||

                  ( base < (*hits).hit [ hit_index ].start + 
                      (*hits).hit [ hit_index ].first - 1 ) ||

                  ( base > (*hits).hit [ hit_index ].end + 
                      (*hits).hit [ hit_index ].first - 1 ) ||

                  ( priority ( (*hits).hit [ hit_index ].seq_type ) 
                      >= priority ( "no_align" ) ) ||

                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "GSDB"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenBank" ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "nt"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Ath_mito"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "rice_chloro"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpri"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "gbpln"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbEST"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Arab. EST" ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "dbSTS"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Fugu"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "SELF"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "vector"  ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli"   ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "RepBase" ) == 0 ) ||
                  ( (*hits).hit [ hit_index ].strand == '+' ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "REPEAT"  ) == 0 ) ) && 
                ( hit_index < (*hits).total ) )  hit_index++;

        if ( hit_index < (*hits).total )  found = TRUE;
      }  /* for */

      if ( ( base + 3 < index - LINE_LENGTH * 3 ) && ( found == TRUE ) )
      {
        fprintf ( (*out).data, "\n            ------------------------------------" );
        fprintf ( (*out).data, "------------------------------------------------\n" );
        index = base + 3;
      }  /* if */
      else
        if ( found == FALSE )  index = base;
    }  /* if */


    /* For each hit in the table. */
    d_strcpy ( previous, "xx" );
    for ( hit_index = protein_index; hit_index < (*hits).total; hit_index++ )

      /* If protein sequence homology hit is not found then return. */
      if ( ( ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) == 0 ) ||
             ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) == 0 ) ) && 
             ( (*hits).hit [ hit_index ].strand == '-' ) &&
           ( priority ( (*hits).hit [ hit_index ].seq_type ) < priority ( "no_align" ) ) )
      {
        /* Check if amino acids on this line. */
        if ( ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1 
                 <= index ) &&
             ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1 >= 
               index - LINE_LENGTH * 3 ) )
        {
          /* Check for change in search program names. */
          if ( ( previous [ 0 ] != '\0' ) && 
               ( d_strcmp ( previous, (*hits).hit [ hit_index ].program ) != 0 ) )
          {
            fprintf ( (*out).data, "\n%s Search Results:\n", 
                (*hits).hit [ hit_index ].program );
            print_aa_header ( out, hits, hit_index, index,
                (*hits).hit [ hit_index ].strand,
                (*hits).hit [ hit_index ].program );
          }  /* if */
          d_strcpy ( previous, (*hits).hit [ hit_index ].program );

          /* Identify the target sequence. */
          get_name ( &((*hits).hit [ hit_index ]), name );
          name [ 10 ] = '\0';		/* truncate long SELF names */

          factor = 3;
          if ( d_strcmp ( (*hits).hit [ hit_index ].program, "TBLAST" ) == 0 )  factor = 1;

          /* Print only the database sequence hit bases identical to query seq. */
          fprintf ( (*out).data, "%10s  ", name );

          /* For each amino acid on this line. */
          for ( base = index; (base >= (*hits).start) && (index - base < LINE_LENGTH * factor ); 
                base -= factor )
          {
            if ( ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1
                   <= base ) &&
                 ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1
                   >= base ) )

              fprintf ( (*out).data, "%c", 
                  (*hits).hit [ hit_index ].ident.base 
                  [ (base - (*hits).hit [ hit_index ].first + 1) / factor ] );

            else
              fprintf ( (*out).data, " " );
          }  /* for */

          fprintf ( (*out).data, "\n" );
        }  /* if */
      }  /* if */
  }  /* for */
}  /* print_aa_msa_minus */


/******************************************************************************/
/* This function prints out a multiple hit multiple sequence alignment. */
print_DNA_msa ( out, hits )
t_text	*out;			/* output file */
t_hits  *hits;			/* homology hits table */
{
  int  base;			/* DNA base index */
  int  DNA_index = 0;		/* DNA sequence index in hits table */
  int  found;			/* found flag */
  int  index;			/* sequence index */
  int  hit_index;		/* hits table index */
  char name [ MAX_LINE ];	/* Swiss protein sequence name */


  if ( (*hits).total <= 0 )  return;

  /* Search for a DNA sequence homology hit. */
  while ( ( ( d_strcmp ( (*hits).hit [ DNA_index ].database, "Swiss"    ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ DNA_index ].database, "PIR"      ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ DNA_index ].database, "nr"       ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ DNA_index ].database, "ecoli.aa" ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ DNA_index ].database, "GenPept"  ) == 0 ) ||
            ( d_strcmp ( (*hits).hit [ DNA_index ].program,  "TBLAST"   ) == 0 ) ||
            ( priority ( (*hits).hit [ DNA_index ].seq_type ) >= priority ( "no_align" ) ) ) &&
          ( DNA_index < (*hits).total - 1 ) )
    DNA_index++;

  /* If protein sequence homology hit is not found then return. */
/*  if ( ( d_strcmp ( (*hits).hit [ DNA_index ].database, "Swiss"    ) == 0 ) ||
       ( d_strcmp ( (*hits).hit [ DNA_index ].database, "PIR"      ) == 0 ) ||
       ( d_strcmp ( (*hits).hit [ DNA_index ].database, "nr"       ) == 0 ) ||
       ( d_strcmp ( (*hits).hit [ DNA_index ].database, "ecoli.aa" ) == 0 ) ||
       ( d_strcmp ( (*hits).hit [ DNA_index ].database, "GenPept"  ) == 0 ) ||
       ( d_strcmp ( (*hits).hit [ DNA_index ].program,  "TBLAST"   ) == 0 ) )  return;
*/
  /* Reset the hit range. */
  (*hits).start = MAX_DNA_SEQ;
  (*hits).end   = 0;
  for ( hit_index = 0; hit_index < (*hits).total; hit_index++ )

    /* Check for DNA homology hit. */
    if ( ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) != 0 ) &&
         ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) != 0 ) && 
         ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) != 0 ) && 
         ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) != 0 ) && 
         ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) != 0 ) && 
         ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) != 0 ) && 
         ( priority ( (*hits).hit [ hit_index ].seq_type ) < priority ( "no_align" ) ) )
    {
      if ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1 
          < (*hits).start )

        (*hits).start = (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1;

      if ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1 > (*hits).end )

        (*hits).end = (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1;
    }  /* if */

  /* Print the query and subject sequences. */
  for ( index = (*hits).start; index <= (*hits).end; index += LINE_LENGTH )
  {
    /* Check for break between homology hits. */
    if ( index > (*hits).start )
    {
      found = FALSE;
      for ( base = index; (base <= (*hits).end) && (found != TRUE); base++ )
      {
        hit_index = DNA_index;
        while ( ( ( (*hits).hit [ hit_index ].region.base 
                      [ base - (*hits).hit [ hit_index ].first + 1 ] == ' ' ) ||

                  ( base < (*hits).hit [ hit_index ].start + 
                      (*hits).hit [ hit_index ].first - 1 ) ||

                  ( base > (*hits).hit [ hit_index ].end + 
                      (*hits).hit [ hit_index ].first - 1 ) ||

                  ( priority ( (*hits).hit [ hit_index ].seq_type ) 
                      >= priority ( "no_align" ) ) ||

                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) == 0 ) ||
                  ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) == 0 ) ) && 
                ( hit_index < (*hits).total ) )  hit_index++;

        if ( hit_index < (*hits).total )  found = TRUE;
      }  /* for */

      if ( ( base - 1 > index ) && ( found == TRUE ) )
      {
        fprintf ( (*out).data, "\n            ------------------------------------" );
        fprintf ( (*out).data, "------------------------------------------------\n" );
        index = base - 1;
      }  /* if */
      else
        if ( found == FALSE )  index = base;
    }  /* if */

    /* Print out the query sequence. */
    fprintf ( (*out).data, "\n  %8d  ", index );

    for ( base = index; (base <= (*hits).end) && (base - index < LINE_LENGTH); base++ )
    {
      hit_index = DNA_index;
      while ( ( ( (*hits).hit [ hit_index ].region.base 
                    [ base - (*hits).hit [ hit_index ].first + 1 ] == ' ' ) ||

                ( base < (*hits).hit [ hit_index ].start + 
                    (*hits).hit [ hit_index ].first - 1 ) ||

                ( base > (*hits).hit [ hit_index ].end + 
                    (*hits).hit [ hit_index ].first - 1 ) ||

                ( priority ( (*hits).hit [ hit_index ].seq_type ) 
                    >= priority ( "no_align" ) ) ||

                ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) == 0 ) ||
                ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) == 0 ) ||
                ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) == 0 ) ||
                ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) == 0 ) ||
                ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) == 0 ) ||
                ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) == 0 ) ) && 
              ( hit_index < (*hits).total ) )  hit_index++;

      if ( hit_index == (*hits).total )  fprintf ( (*out).data, " " );
      else  fprintf ( (*out).data, "%c", 
          (*hits).hit [ hit_index ].region.base 
              [ base - (*hits).hit [ hit_index ].first + 1 ] );
    }  /* for */
    fprintf ( (*out).data, "\n" );

    /* Print out the subject sequences. */
    for ( hit_index = DNA_index; hit_index < (*hits).total; hit_index++ )

      /* Check for DNA sequence homology hit. */
      if ( ( d_strcmp ( (*hits).hit [ hit_index ].database, "Swiss"    ) != 0 ) &&
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "PIR"      ) != 0 ) && 
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "nr"       ) != 0 ) && 
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "ecoli.aa" ) != 0 ) && 
           ( d_strcmp ( (*hits).hit [ hit_index ].database, "GenPept"  ) != 0 ) && 
           ( d_strcmp ( (*hits).hit [ hit_index ].program,  "TBLAST"   ) != 0 ) && 
           ( priority ( (*hits).hit [ hit_index ].seq_type ) < priority ( "no_align" ) ) )
      {
        /* Identify the target sequence. */
        get_name ( &((*hits).hit [ hit_index ]), name );
        name [ 10 ] = '\0';		/* truncate long SELF names */

        /* Determine if this hit overlaps with the current base range. */
        if ( ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1
                 <= index + LINE_LENGTH - 1 ) &&

             ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1
                 >= index ) ) 
        {
          /* Print only the database sequence hit bases identical to query seq. */
          fprintf ( (*out).data, "%10s  ", name );

          for ( base = index; (base <= (*hits).end) && (base - index < LINE_LENGTH); base++ )

            if ( ( (*hits).hit [ hit_index ].start + (*hits).hit [ hit_index ].first - 1
                   <= base ) &&
                 ( (*hits).hit [ hit_index ].end + (*hits).hit [ hit_index ].first - 1
                   >= base ) )

              fprintf ( (*out).data, "%c", (*hits).hit [ hit_index ].ident.base 
                  [ base - (*hits).hit [ hit_index ].first + 1 ] );

            else
              fprintf ( (*out).data, " " );

          fprintf ( (*out).data, "\n" );
        }  /* if */
      }  /* if */
  }  /* for */
}  /* print_DNA_msa */


/******************************************************************************/
/* This function promts for a file name. */
prompt_file ( text, prompt )
t_text  *text;		/* ASCII text file */
char    prompt [];	/* request prompt */
{
  (*text).name [ 0 ] = '\0';
  (*text).eof = TRUE;

  /* Prompt for a valid file name. */
  while ( ( (*text).eof == TRUE ) && ( d_strcmp ( (*text).name, "exit" ) != 0 ) )
  {
    printf ( "%s or 'exit' ", prompt );

    scanf ( "%s", (*text).name );

    if ( d_strcmp ( (*text).name, "exit" ) != 0 )

      open_text_file ( text );
  }  /* while */

  printf ( "\n" );
}  /* prompt_file */


/******************************************************************************/
/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text		*text;		/* ASCII text file */
t_DNA_seq	*seq;		/* DNA sequence */
{
  int  index;


  d_strcpy ( (*seq).name, (*text).name );

  /* Read in the DNA sequence. */
  (*seq).total = 0;

  while ( ( (*text).eof != TRUE ) && ( (*seq).total < MAX_DNA_SEQ ) )
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
    {
      (*seq).base [ (*seq).total++ ] = (*text).next;

      if ( (*seq).total == MAX_DNA_SEQ - 1 )
        printf ( "*WARNING* Maximum number of sequence bases (%d) reached.\n",
          MAX_DNA_SEQ );
    }  /* if */

    get_char ( text );		/* get the next character */

    if ( (*text).next == '>' )  return;
  }  /* while */
}  /* read_DNA_seq */


/******************************************************************************/
/* Reverse string s in place. */
reverse(s)
char s[];
{
  int  c, i, j;

  for (i = 0, j = d_strlen(s)-1; i < j; i++, j--)
  {
    c    = s[i];
    s[i] = s[j];
    s[j] = c;
  }  /* for */
}  /* reverse */


/******************************************************************************/
/* This function concatenates t to the end of s. */
d_strcat (s, t)
char  s[], t[];		/* s must be big enough */
{
  int  i = 0, j = 0;


  /* Find the end of s. */
  while ( s [ i ] != '\0' )  i++;

  /* Copy t. */
  while ( ( s [ i++ ] = t [ j++ ] ) != '\0' )  
    ;
}  /* d_strcat */


/******************************************************************************/
/* Return <0 if s<t, 0 if s==t, >0 if s>t */
d_strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* d_strcmp */


/******************************************************************************/
/* Copy t to s */
d_strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* d_strcpy */


/******************************************************************************/
/* This function returns the index of t in s or index of '\0'. */
d_stridx (s, t)
char s[], t[];
{
  int  i, j, k;

  for (i = 0; s [ i ] != '\0'; i++ )
  {
    for ( j = i, k = 0; t [ k ] != '\0' && s [ j ] == t [ k ]; j++, k++ )  ;

    if ( t [ k ] == '\0' )  return ( i );
  }  /* for */

  return ( i );
}  /* d_stridx */


/******************************************************************************/
/* Return length of string s */
d_strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* d_strlen */


/******************************************************************************/
/* This function reverses and complements a DNA sequence. */
reverse_seq ( seq, rev )
t_DNA_seq	*seq;		/* DNA sequence */
t_DNA_seq	*rev;		/* reversed & complemented DNA sequence */
{
  int  index;		/* base index */


  (*rev).total = (*seq).total;

  d_strcpy ( (*rev).name, (*seq).name );

  for ( index = 0; index < (*seq).total; index++ )

    (*rev).base [ (*rev).total - index - 1 ] = 
      complement [ (*seq).base [ index ] ];
}  /* reverse_seq */


/******************************************************************************/
/* This function gets the sequence name. */
get_name ( hit, name )
t_homology    *hit;		/* homology hit */
char          name [];		/* sequence name or accession number */
{
  while ( (*hit).desc_1 [ 0 ] == ' ' )
    d_strcpy ( (*hit).desc_1, &((*hit).desc_1 [ 1 ]) );

  /* Setup a default sequence name. */
  d_strcpy ( name, (*hit).desc_1 );
  name [ d_stridx ( name, "\n" ) ] = '\0';

  /* Get the name in a database dependent manner. */
  if ( d_strcmp ( (*hit).database, "SELF" ) == 0 )
  {
    name [ d_stridx ( name, " " ) ] = '\0';
    return;
  }  /* if */

  /* Check for database|accession|name formats. */
  if ( name [ d_stridx ( name, "gb|" ) ] == 'g' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "gb|" ) + 3 ]) ); 
    name [ d_stridx ( name, "|" ) ] = '\0';
    return;
  }  /* if */

  if ( name [ d_stridx ( name, "gp|" ) ] == 'g' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "gp|" ) + 3 ]) ); 
    name [ d_stridx ( name, "|" ) ] = '\0';
    return;
  }  /* if */

  if ( name [ d_stridx ( name, "sp|" ) ] == 's' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "sp|" ) + 3 ]) ); 
    name [ d_stridx ( name, "|" ) ] = '\0';
    return;
  }  /* if */

  if ( name [ d_stridx ( name, "emb|" ) ] == 'e' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "emb|" ) + 4 ]) ); 
    name [ d_stridx ( name, "|" ) ] = '\0';
    return;
  }  /* if */

  if ( name [ d_stridx ( name, "gi|" ) ] == 'g' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "gi|" ) + 3 ]) ); 
    name [ d_stridx ( name, ")" ) ] = '\0';

    if ( name [ d_stridx ( name, "|" ) ] == '|' )
    {
      d_strcpy ( name, &(name [ d_stridx ( name, "|" ) + 1 ]) );
      name [ d_stridx ( name, " " ) ] = '\0';
    }
    else
      if ( name [ d_stridx ( name, "(" ) ] != '(' )
        printf ( "get_name: unknown syntax '%s'\n", name );
      else
        d_strcpy ( name, &(name [ d_stridx ( name, "(" ) + 1 ]) ); 

    return;
  }  /* if */

  if ( name [ d_stridx ( name, "bbs|" ) ] == 'b' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "bbs|" ) + 4 ]) ); 
    name [ d_stridx ( name, ")" ) ] = '\0';

    if ( name [ d_stridx ( name, "|" ) ] == '|' )
    {
      d_strcpy ( name, &(name [ d_stridx ( name, "|" ) + 1 ]) );
      name [ d_stridx ( name, " " ) ] = '\0';
    }
    else
      if ( name [ d_stridx ( name, "(" ) ] != '(' )
        printf ( "get_name: unknown syntax '%s'\n", name );
      else
        d_strcpy ( name, &(name [ d_stridx ( name, "(" ) + 1 ]) ); 

    return;
  }  /* if */

  if ( name [ d_stridx ( name, "gnl|" ) ] == 'g' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "gnl|" ) + 8 ]) ); 
    name [ d_stridx ( name, " " ) ] = '\0';
    name [ d_stridx ( name, "|" ) ] = '\0';
    return;
  }  /* if */

  if ( name [ d_stridx ( name, "dbj|" ) ] == 'd' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "dbj|" ) + 4 ]) ); 
    name [ d_stridx ( name, "|" ) ] = '\0';
    return;
  }  /* if */

  if ( name [ d_stridx ( name, "pir|" ) ] == 'p' ) 
  {
    d_strcpy ( name, &(name [ d_stridx ( name, "pir|" ) + 6 ]) ); 
    name [ d_stridx ( name, " " ) ] = '\0';
    return;
  }  /* if */

  /* Get the name in a database dependent manner. */
  if ( d_strcmp ( (*hit).database, "Swiss" ) == 0 )
  {
    name [ d_stridx ( name, " " ) ] = '\0';
    return;
  }  /* if */

  if ( ( d_strcmp ( (*hit).database, "dbEST" ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "dbSTS" ) == 0 ) )
  {
    /* Trim off prefixes on dbEST & dbSTS sequences. */
    while ( ( name [ d_stridx ( name, "|" ) ] == '|' ) &&
            ( d_stridx ( name, "|" ) < MAX_PREFIX ) )

      d_strcpy ( name, &(name [ d_stridx ( name, "|" ) + 1 ]) );

    /* Check for new format with accession number separated. */
    if ( ( name [ 0 ] >= '0' ) && ( name [ 0 ] <= '9' ) )

      d_strcpy ( name, &(name [ d_stridx ( name, " " ) + 1 ]) );

    name [ d_stridx ( name, " " ) ] = '\0';
    return;
  }  /* if */

  if ( d_strcmp ( (*hit).database, "GSDB" ) == 0 )
  {
    if ( name [ 0 ] == ' ' )  d_strcpy ( name, &(name [ 1 ]) );
    if ( name [ d_stridx ( name, " " ) ] != '\0' )
      d_strcpy ( name, &(name [ d_stridx ( name, " " ) + 1 ]) ); 
    name [ d_stridx ( name, " " ) ] = '\0';
    return;
  }  /* if */

  if ( ( d_strcmp ( (*hit).database, "REPEAT"  ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "RepBase" ) == 0 ) ||
       ( d_strcmp ( (*hit).database, "ecoli"   ) == 0 ) )
  {
    name [ d_stridx ( name, " " ) ] = '\0';
    return;
  }  /* if */
}  /* get_name */


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
/* This function returns the index of a feature name. */
valid_name ( name )
char  name [];		/* feature name */
{
  int  index;		/* feature name list index */


  /* Check for a null feature name. */
  if ( name [ 0 ] == '\0' )  return ( 0 );

  /* Check all of the feature names. */
  for ( index = 0; d_strcmp ( feature_names [ index ], "" ) != 0; index++ )

    /* Check for the feature name. */
    if ( d_strcmp ( feature_names [ index ], name ) == 0 )  return ( index );

  return ( 0 );
}  /* valid_name */


/******************************************************************************/
/* This function searches the feature table for the current pair. */
find_feature ( name, start, end, features, index )
char            name [];	/* sequence name */
long		start;		/* start of feature */
long		end;		/* end of feature */
t_hits      	*features;	/* features table */
int		*index;		/* table index */
{
  *index = 0;
  if ( (*features).total == 0 )  return;

  /* Find the first occurance of the sequence name. */
  while ( ( (*index) < (*features).total ) &&
          ( d_strcmp ( name, (*features).hit [ *index ].name ) > 0 ) )

    (*index)++;

  /* Find the first possible overlapping record. */
  while ( ( (*index) < (*features).total ) &&
          ( d_strcmp ( name, (*features).hit [ *index ].name ) == 0 ) &&
          ( start > (*features).hit [ *index ].end ) )

    (*index)++;
}  /* find_feature */


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
  if ( d_strcmp ( (*new).name, (*features).hit [ index ].name ) != 0 )  return ( TRUE );

  /* Determine if the features overlap. */
  start = ( (*new).start > (*features).hit [ index ].start ) ?
          (*new).start : (*features).hit [ index ].start;

  end = ( (*new).end < (*features).hit [ index ].end ) ?
          (*new).end : (*features).hit [ index ].end;

  new_len = (*new).end - (*new).start + 1;
  old_len = (*features).hit [ index ].end - (*features).hit [ index ].start + 1;

  /* Check for identical feature types for: homology, repeat, L1, Alu, contaminants. */
  if ( d_strcmp ( (*new).seq_type, (*features).hit [ index ].seq_type ) == 0 )
  {
    if ( ( d_strcmp ( (*new).seq_type, "similarity" ) == 0 ) ||
         ( priority ( (*new).seq_type ) >= priority ( "repeats" ) ) )
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
/* This function merges two sequences. */
merge_seq ( new, old )
t_seq	*new;		/* new sequence */
t_seq	*old;		/* old sequence */
{
  int  end = 0;			/* end of sequence */
  int  index;			/* sequence index */
  int  start = MAX_BASES;	/* start of sequence */


  /* Copy the sequence. */
  for ( index = 0; index < MAX_BASES; index++ )

    if ( (*old).base [ index ] == ' ' )

      (*old).base [ index ] = (*new).base [ index ];

  /* Find the limits of the sequence. */
  for ( index = 0; index < MAX_BASES; index++ )
    if ( (*old).base [ index ] != ' ' )
    {
      if ( index < start )  start = index;
      if ( index > end )  end = index;
    }  /* if */

  (*old).total = end - start + 1;
}  /* merge_seq */


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
  (*old).size       = (*new).size;
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

  d_strcpy ( (*old).name,      (*new).name );
  d_strcpy ( (*old).code,      (*new).code );
  d_strcpy ( (*old).seq_type,  (*new).seq_type );
  d_strcpy ( (*old).program,   (*new).program );
  d_strcpy ( (*old).database,  (*new).database );
  d_strcpy ( (*old).accession, (*new).accession );
  d_strcpy ( (*old).desc_1,    (*new).desc_1 );
  d_strcpy ( (*old).desc_2,    (*new).desc_2 );

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
    printf ( "*WARNING* Maximum features (%d) reached in insert_feature.\n", MAX_FEATURES );
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

      d_strcpy ( (*features).hit [ move ].name, 
          (*features).hit [ move - 1 ].name );
      d_strcpy ( (*features).hit [ move ].code, 
          (*features).hit [ move - 1 ].code );
      d_strcpy ( (*features).hit [ move ].seq_type, 
          (*features).hit [ move - 1 ].seq_type );
      d_strcpy ( (*features).hit [ move ].program, 
          (*features).hit [ move - 1 ].program);
      d_strcpy ( (*features).hit [ move ].database, 
          (*features).hit [ move - 1 ].database );
      d_strcpy ( (*features).hit [ move ].accession, 
          (*features).hit [ move - 1 ].accession );
      d_strcpy ( (*features).hit [ move ].desc_1, 
          (*features).hit [ move - 1 ].desc_1 );
      d_strcpy ( (*features).hit [ move ].desc_2, 
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
/* This function copies pattern pat2 to pattern pat1. */
copy_pattern ( pat1, pat2 )
t_pattern	*pat1;		/* new copy of pat2 */
t_pattern	*pat2;		/* the pattern to copy from */
{
  (*pat1).length  = (*pat2).length;
  (*pat1).minimum = (*pat2).minimum;
  (*pat1).type    = (*pat2).type;
  (*pat1).match   = (*pat2).match;
  (*pat1).start   = (*pat2).start;
  (*pat1).count   = (*pat2).count;

  d_strcpy ( (*pat1).name, (*pat2).name );
  d_strcpy ( (*pat1).base, (*pat2).base );
}  /* copy_pattern */


/******************************************************************************/
/* This function deletes a pattern from the patterns table. */
delete_pattern ( patterns, index )
t_patterns	*patterns;	/* matched patterns */
int  		index;		/* index to delete */
{
  int  i;	/* index */


  /* Shift all of the patterns up one position. */
  if ( (*patterns).total > 1 )

    for ( i = index; i < (*patterns).total - 1; i++ )

      copy_pattern ( &((*patterns).pat [ i ]), &((*patterns).pat [ i + 1 ]) );

  (*patterns).total--;
}  /* delete_pattern */


/******************************************************************************/
/* This functions adds a pattern to the patterns table if it does not overlap */
/* with an existing pattern in the patterns table. */
add_pattern ( pattern, patterns )
t_pattern	*pattern;	/* current pattern */
t_patterns	*patterns;	/* matched patterns */
{
  int  copied = FALSE;	/* copied flag */
  int  end;		/* overlap end */
  int  index;		/* table index */
  int  size1;		/* size of pattern 1 */
  int  size2;		/* size of pattern 2 */
  long start;		/* overlap start */


  if ( (*patterns).total <= 0 )
  {
    copy_pattern ( &((*patterns).pat [ 0 ]), pattern );
    (*patterns).total++;
    return;
  }  /* if */

  /* Check for overlapping patterns. */
  size1 = (*pattern).length * (*pattern).count;
  for ( index = (*patterns).total - 1; index >= 0; index-- )
  {
    size2 = (*patterns).pat [ index ].length * (*patterns).pat [ index ].count;

    if ( ( (*pattern).start + size1 - 1 > (*patterns).pat [ index ].start ) &&
         ( (*pattern).start < (*patterns).pat [ index ].start + size2 - 1 ) )
    {
      start = (*patterns).pat [ index ].start;
      end   = start + size2 - 1;
      if ( (*pattern).start > start )  start = (*pattern).start;
      if ( (*pattern).start + size1 < end )  end = (*pattern).start + size1 - 1;

      if ( size1 > size2 )
      {
        if ( ( end - start + 1 ) * 2 > size2 )
        {
          if ( copied != TRUE )
          {
            /* Overwrite the existing pattern. */
            copy_pattern ( &((*patterns).pat [ index ]), pattern );
            copied = TRUE;
          }
          else
            delete_pattern ( patterns, index );
        }  /* if */
      }
      else
      {
        if ( ( end - start + 1 ) * 2 > size1 )
        {
          /* Discard the new pattern. */
          return;
        }  /* if */
      }  /* else */
    }  /* if */
  }  /* for */

  if ( copied == TRUE )  return;

  /* Check for patterns table overflow. */
  if ( (*patterns).total + 1 >= MAX_PATTERNS )
  {
    printf ( "add_pattern: MAX_PATTERNS (%d) reached.\n", MAX_PATTERNS );
    return;
  }  /* if */

  /* Add the pattern to the patterns table. */

  /* Find the insertion spot. */
  for ( index = (*patterns).total - 1; index >= 0; index-- )
  {
    if ( (*pattern).start < (*patterns).pat [ index ].start )
    {
      copy_pattern ( &((*patterns).pat [ index + 1 ]), &((*patterns).pat [ index ]) );
    }
    else
    {
      copy_pattern ( &((*patterns).pat [ index + 1 ]), pattern );
      (*patterns).total++;
      return;
    }  /* else */
  }  /* for */

  copy_pattern ( &((*patterns).pat [ 0 ]), pattern );
  (*patterns).total++;
}  /* add_pattern */


/******************************************************************************/
/* This function searches a sequence for the pattern. */
find_pattern ( sequence, pattern, patterns )
t_DNA_seq	*sequence;	/* entire sequence */
t_pattern	*pattern;	/* current pattern */
t_patterns	*patterns;	/* matched patterns */
{
  int	count;		/* number of subpattern matches */
  long	index;		/* start of comparison index */
  long	sub_index;	/* pattern index */


  /* Scan the entire sequence. */
  for ( index = 0; index < 
      (*sequence).total - (*pattern).length * (*pattern).minimum + 1; index++ )
  {
    count = 0;
    do
    {
      sub_index = 0;
      while ((( dna_mask [ (*sequence).base
          [ index + (*pattern).length * count + sub_index ] ] &
          dna_mask [ (*pattern).base [ sub_index ] ]) != 0 ) &&
          ( sub_index <= (*pattern).length ))  sub_index++;
      if ( sub_index >= (*pattern).length )  count++;
    }
    while (( sub_index >= (*pattern).length ) &&	/* match */
        ( (*pattern).type == P_SUBUNIT ) &&		/* repeat */
        ( index + (*pattern).length * (count + 1) - 1 <= (*sequence).total ));

    /* Report any matches. */
    if (( count < (*pattern).minimum ) && ( (*pattern).type == P_SUBUNIT ))
    {
      if ( count > 0 )  index += (*pattern).length * count - 1;
      count = 0;
    }  /* if */

    if ( count > 0 )
    {
      (*pattern).start = index;
      (*pattern).count = count;
      add_pattern ( pattern, patterns );
      index += (*pattern).length * count - 1;
    }  /* if */
  }  /* for */
}  /* find_pattern */


/******************************************************************************/
/*This function searches for unit patterns. */
unit_patterns ( sequence, hits, pats, flanks )
t_DNA_seq	*sequence;		/* entire sequence */
t_hits  	*hits;			/* homology hits table */
t_text          *pats;			/* Repeat patterns output file */
t_text          *flanks;		/* flanking regions of patterns */
{
  int	  	index;				/* pattern index */
  double  	length;				/* calculated sequence length */
  unsigned	mask;				/* bit representation of base */
  char		*pat_bases [ MAX_UNITS + 2 ];	/* pointers to nucleotides */
  int	  	pat_index;			/* pattern index */
  t_pattern	pattern;			/* search pattern */
  t_patterns	patterns;			/* all matching patterns */
  int		not_done;			/* search flag */
  char	  	nucleotides [ MAX_LINE ];	/* possible bases for patterns */
  char          seq_name [ MAX_LINE ];		/* sequence name */
  int	  	unit;				/* current unit length */


  if ( (*sequence).total <= 0 )  return;
  patterns.total = 0;

  /* Generate the search patterns. */
  pattern.type = P_SUBUNIT;
  pattern.match = 1.0;
  for ( unit = 1; unit <= MAX_UNITS; unit++ )
  {
    d_strcpy ( nucleotides, "ACGT" );

    pattern.length = unit;

    /* Initialize the pattern bases. */
    for ( index = 0; index < unit; index++ )
      pat_bases [ index ] = nucleotides;

    /* Cycle through all possible bases at each pattern position. */
    index = unit - 1;
    while ( index >= 0 )
    {
      pattern.start = pattern.count = 0;

      /* Copy the pattern. */
      for ( pat_index = 0; pat_index < unit; pat_index++ )
      
        pattern.base [ pat_index ] = pattern.name [ pat_index ] = *pat_bases [ pat_index ];

      pattern.base [ unit ] = pattern.name [ unit ] = '\0';
      pattern.base [ unit + 1 ] = pattern.name [ unit + 1 ] = '\0';
      pattern.minimum = 14;
      if ( unit == 2 )  pattern.minimum = 7;
      if ( unit == 3 )  pattern.minimum = 5;
      if ( unit == 4 )  pattern.minimum = 4;
      if ( unit == 5 )  pattern.minimum = 3;

      find_pattern ( sequence, &pattern, &patterns );

      /* Set up the next pattern. */
      index = unit - 1;
      pat_bases [ index ]++;
      not_done = ( *pat_bases [ index ] == '\0' );
      while ( not_done )
      {
        pat_bases [ index ] = nucleotides;
        index--;
        if ( index >= 0 )
        {
          pat_bases [ index ]++;
          not_done = ( *pat_bases [ index ] == '\0' );
        }  /* if */
        else not_done = ( '\0' != '\0' );
      }  /* while */
    }  /* while */
  }  /* for */

  /* Extract the sequence name. */
  d_strcpy ( seq_name, (*sequence).name );
  if ( seq_name [ d_stridx ( seq_name, "../seq/" ) ] == '.' )
  d_strcpy ( seq_name, &(seq_name [ 7 ]) );

  /* Print out any matching patterns. */
  if ( patterns.total > 0 )
  {
    for ( index = 0; index < patterns.total; index++ )

     if ( patterns.pat [ index ].length >= MIN_UNITS )
     {
      /* Convert the repeat pattern to a homology hit. */
      convert_match ( &(patterns.pat [ index ]), sequence, hits );

      /* Write out the pattern. */
      print_match ( sequence, &(patterns.pat [ index ]), pats ); 

      /* Write out the flanking regions. */
      write_flanks ( sequence, &(patterns.pat [ index ]), flanks );
     }  /* if */
  }  /* if */
}  /* unit_patterns */


/******************************************************************************/
/* This function reads in known patterns from a FASTA-like file. */
read_patterns ( patterns_file, known_patterns )
t_text		*patterns_file;		/* Known patterns input file */
t_patterns      *known_patterns;	/* Set of patterns to search for */
{
  char  current [ MAX_LINE ];		/* current line */


  /* Initialization. */
  (*known_patterns).total = 0;

  /* Read in each of the patterns. */
  while ( (*patterns_file).eof != TRUE )
  {
    /* Get the sequence name from the description line. */ 
    d_strcpy ( current, &((*patterns_file).line [ 1 ]) );
    current [ d_stridx ( current, "\n" ) ] = '\0';
    current [ d_stridx ( current, " " ) ] = '\0';
    current [ d_stridx ( current, "(" ) ] = '\0';
 
    /* Advance to the pattern line of the sequence. */
    get_line ( patterns_file );

    (*patterns_file).line [ d_stridx ( (*patterns_file).line, "\n" ) ] = '\0';
    (*patterns_file).line [ d_stridx ( (*patterns_file).line, " "  ) ] = '\0';

printf ( "\t%s \t%s\n", current, (*patterns_file).line );

    (*known_patterns).pat [ (*known_patterns).total ].type = P_SEQUENCE;
    (*known_patterns).pat [ (*known_patterns).total ].match = 1.0;
    (*known_patterns).pat [ (*known_patterns).total ].start = 0;
    (*known_patterns).pat [ (*known_patterns).total ].count = 1;
    (*known_patterns).pat [ (*known_patterns).total ].minimum = 1;
    (*known_patterns).pat [ (*known_patterns).total ].length =
        d_strlen ( (*patterns_file).line );
    d_strcpy ( (*known_patterns).pat [ (*known_patterns).total ].base, 
        (*patterns_file).line );
    d_strcpy ( (*known_patterns).pat [ (*known_patterns).total ].name, 
        current );

    (*known_patterns).total++;
 
    /* Advance to the next line of the sequence. */
    get_line ( patterns_file );
  }  /* while */
}  /* read_patterns */


/******************************************************************************/
/*This function searches for specific patterns. */
fixed_patterns ( sequence, known_patterns, hits, pats, flanks, strand )
t_DNA_seq	*sequence;		/* entire sequence */
t_patterns      *known_patterns;	/* Set of patterns to search for */
t_hits  	*hits;			/* homology hits table */
t_text          *pats;			/* patterns output file */
t_text          *flanks;		/* flanking regions of patterns */
char            strand;			/* sequence strand */
{
  int	  	index;				/* pattern index */
  t_patterns	patterns;			/* all matching patterns */
  int           pat_index;			/* pattern index 2 */


  if ( (*sequence).total <= 0 )  return;

  /* Generate the search patterns. */
  for ( index = 0; index < (*known_patterns).total; index++ )
  {
    patterns.total = 0;

    /* Cycle through all specified patterns. */
    find_pattern ( sequence, &((*known_patterns).pat [ index ]), &patterns );

    /* Print out any matching patterns. */
    if ( patterns.total > 0 )
    {
      printf ( "%s %s found %d\n", 
          (*known_patterns).pat [ index ].name,
          (*known_patterns).pat [ index ].base,
          patterns.total );

      for ( pat_index = 0; pat_index < patterns.total; pat_index++ )
      {
        /* Convert the repeat pattern to a homology hit. */
        convert_match ( &(patterns.pat [ pat_index ]), sequence, hits, strand );

        /* Write out the pattern. */
        print_match ( sequence, &(patterns.pat [ pat_index ]), pats ); 

        /* Write out the flanking regions. */
        /* write_flanks ( sequence, &(patterns.pat [ pat_index ]), flanks ); */
      }  /* for */
    }  /* if */
  }  /* for */

}  /* fixed_patterns */


/******************************************************************************/
convert_match ( pattern, sequence, hits, strand )
t_pattern	*pattern;		/* current pattern */
t_DNA_seq	*sequence;		/* entire sequence */
t_hits  	*hits;			/* homology hits table */
{
  char  count [ MAX_LINE ];		/* number of pattern subunits */
  int   index;				/* array index */
  char  name  [ MAX_LINE ];		/* pattern name */


  /* Check if the hits table is full. */
  if ( (*hits).total + 1 >= MAX_FEATURES )
  {
    printf ( "*WARNING* Maximum hits reached (MAX_FEATURES = %d) in convert_match.\n",
        MAX_FEATURES );
    return;
  }  /* if */

  /* Initialize the hit template. */
  init_homology ( &((*hits).hit [ (*hits).total ]) );
  (*hits).hit [ (*hits).total ].strand = strand;
  (*hits).hit [ (*hits).total ].db_type = DNA;

  /* Copy the pattern. */
  d_strcpy ( name, (*pattern).name );
  d_strcat ( name, "  " );
  d_strcat ( name, (*pattern).base );
  d_strcpy ( (*hits).hit [ (*hits).total ].desc_1, name );

  (*hits).hit [ (*hits).total ].desc_2 [ 0 ] = '\0';

  (*hits).hit [ (*hits).total ].first = 1;

  (*hits).hit [ (*hits).total ].len = 
      (*hits).hit [ (*hits).total ].size = (*sequence).total;

  (*hits).hit [ (*hits).total ].length =
      (*hits).hit [ (*hits).total ].identities = 
      (*hits).hit [ (*hits).total ].ident.total = 
      (*hits).hit [ (*hits).total ].region.total = 
      (*pattern).length * (*pattern).count;

  (*hits).hit [ (*hits).total ].hit_start = 1;
  (*hits).hit [ (*hits).total ].hit_end = (*pattern).length;

  (*hits).hit [ (*hits).total ].start = (*pattern).start + 1;
  (*hits).hit [ (*hits).total ].end =
      (*pattern).start + (*pattern).length * (*pattern).count;

  /* Check for complement strand. */
  if ( strand == '-' )
  {
    /* Change the strand of the coordinates. */
    index = (*hits).hit [ (*hits).total ].start;
    (*hits).hit [ (*hits).total ].start = (*sequence).total - 
        (*hits).hit [ (*hits).total ].end + 1;
    (*hits).hit [ (*hits).total ].end = (*sequence).total - index + 1;
  }  /* if */

  /* Shift the coordinates of the pattern to < 1000. */
  while ( (*hits).hit [ (*hits).total ].start > 1000 )
  {
    (*hits).hit [ (*hits).total ].first += 1000;
    (*hits).hit [ (*hits).total ].start -= 1000;
    (*hits).hit [ (*hits).total ].end   -= 1000;
  }  /* while */

  /* Copy the sequence pattern. */
  for ( index = 0; index < (*hits).hit [ (*hits).total ].length; index++ )
  {
    (*hits).hit [ (*hits).total ].region.base [ 
        (*hits).hit [ (*hits).total ].start + index ] =
        (*hits).hit [ (*hits).total ].ident.base [ 
        (*hits).hit [ (*hits).total ].start + index ] =
        (*sequence).base [ (*pattern).start + index ];
  }  /* for */

  (*hits).hit [ (*hits).total ].percent = 100;

  if ( (*hits).hit [ (*hits).total ].name [ 0 ] == '\0' )
    d_strcpy ( (*hits).hit [ (*hits).total ].name, (*sequence).name ); 

  d_strcpy ( (*hits).hit [ (*hits).total ].seq_type, "repeats" );
  d_strcpy ( (*hits).hit [ (*hits).total ].database, "SELF" );
  d_strcpy ( (*hits).hit [ (*hits).total ].program,  "program_name" );
  (*hits).total++;
}  /* convert_match */


/******************************************************************************/
/* This function writes out the flanking regions of a pattern match. */
write_flanks ( sequence, pattern, flanks )
t_DNA_seq	*sequence;	/* entire sequence */
t_pattern	*pattern;	/* current pattern */
t_text          *flanks;	/* flanking regions output file */
{
  int  end;
  int  start;

  /* Check for no flanks file. */
  if ( flanks == NULL )  return;

  /* Write out the preceeding sequence flank to the pattern. */
  end = (*pattern).start - 1;
  start = end - BEFORE_BASES + 1;
  if ( start < 0 )  start = 0;

  if ( ( end - start >= 1 ) && ( end >= 0 ) )
  {
    fprintf ( (*flanks).data, ">%s", (*sequence).name ); 
    fprintf ( (*flanks).data, "_%s_%d_l\n", (*pattern).name, (*pattern).start + 1 );
    write_DNA_seq ( sequence, start, end, flanks );
  }  /* if */

  /* Write out the trailing sequence flank to the pattern. */
  start = (*pattern).start + (*pattern).length * (*pattern).count; 
  end = start + AFTER_BASES - 1;
  if ( end >= (*sequence).total )  end = (*sequence).total - 1;

  if ( ( end - start >= 1 ) && ( end >= 0 ) )
  {
    fprintf ( (*flanks).data, ">%s", (*sequence).name ); 
    fprintf ( (*flanks).data, "_%s_%d_r\n", (*pattern).name, (*pattern).start + 1 );
    write_DNA_seq ( sequence, start, end, flanks );
  }  /* if */
}  /* write_flanks */


/******************************************************************************/
/* This function prints out a pattern match. */
print_match ( sequence, pattern, pats )
t_DNA_seq	*sequence;	/* entire sequence */
t_pattern	*pattern;	/* current pattern */
t_text          *pats;		/* patterns output file */
{
  int   i;		/* space counter */
  int	printed;	/* number of bases printed on current line */
  long	sub_index;	/* pattern index */


  sub_index = (*pattern).start - BEFORE_BASES;	/* include surrounding bases */
  printed = 0;
  do 
  {
    /* Separate matched sequence from surrounding bases. */
    if ( sub_index == (*pattern).start )  fprintf ( (*pats).data, "  " ); 
    if ( sub_index == (*pattern).start + (*pattern).length * (*pattern).count )
    {
      fprintf ( (*pats).data, "  " );

      /* If repeated subunit search, then space match to maximum width. */
      if (( (*pattern).type == P_SUBUNIT ) &&
          ( printed < MAX_PER_LINE - AFTER_BASES ))
        while ( printed < MAX_PER_LINE - AFTER_BASES )
        {
          fprintf ( (*pats).data, " " );
          printed++;
        }  /* if */
      }  /* if */

    /* Print the base or a space. */
    if (( sub_index < 0 ) || ( sub_index >= (*sequence).total ))
      fprintf ( (*pats).data, " " );
    else
      fprintf ( (*pats).data, "%c", (*sequence).base [ sub_index ] );
    printed++;

    /* Print the partial line trailer. */
    if (( printed == MAX_PER_LINE - AFTER_BASES ) &&
        ( sub_index < (*pattern).start + (*pattern).length * (*pattern).count - 1 ))
    {  
      for ( i = 1; i <= AFTER_BASES; i++ )  fprintf ( (*pats).data, " " );
      fprintf ( (*pats).data, "  " );
      printed = MAX_PER_LINE;
    }  /* if */


    /* Print the pattern information on end of lines. */
    if (( printed == MAX_PER_LINE ) ||
        ( sub_index == (*pattern).start + (*pattern).length * (*pattern).count - 1 + AFTER_BASES))
    {
      fprintf ( (*pats).data, "    %4s(%4d)  %6d", (*pattern).name, (*pattern).count, (*pattern).start + 1 );
      fprintf ( (*pats).data, "    %s", (*sequence).name ); 
      fprintf ( (*pats).data, "\n" );

      /* Print the leader for the next line. */
      if ( sub_index < (*pattern).start + (*pattern).length * (*pattern).count - 1 + AFTER_BASES )
      {
        for ( i = 1; i <= BEFORE_BASES; i++ )  fprintf ( (*pats).data, " " );
        fprintf ( (*pats).data, "  " );
        printed = BEFORE_BASES;
      }  /* if */
      else  printed = 0;
    }  /* if */
    sub_index++;
  }
  while ( sub_index <= (*pattern).start + (*pattern).length * (*pattern).count - 1 + AFTER_BASES );
}  /* print_match */


/******************************************************************************/
/* This function returns the priority of a feature name. */
priority ( name )
char  name [];		/* feature name */
{
  int  index;				/* feature name list index */


  /* Check for a null feature name. */
  if ( name [ 0 ] == '\0' )  return ( 0 );

  /* Look up the index of this name. */
  index = valid_name ( name );

  if ( ( index > valid_name ( "repeat" ) ) && ( index < valid_name ( "last_repeat" ) ) )
  {
    index = valid_name ( "repeat" ) + 1;
    return ( index );
  }  /* if */

  /* Check for the feature name. */
  if ( d_strcmp ( feature_names [ index ], name ) == 0 )  return ( index );

  return ( 0 );		/* lowest priority - not found */
}  /* priority */


