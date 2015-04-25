
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


#define  TRUE		1
#define  FALSE		0

#define  MAX_AA_PERCENT  45	/* maximum amino acid percentage in a homology hit */

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      10001	/* maximum DNA sequence length */

#define  MAX_EXONS	102	/* maximum number of GRAIL exons per sequence */

#define  MAX_HIT_DISPLAY 250	/* maximum bases of homology to display */

#define  MAX_HITS	30	/* maximum number of homology hits to save */

#define  MAX_LINE	512	/* maximum line length */

#define  MAX_NAME	20	/* maximum name length */

#define  MIN_OVERLAP    50	/* minimum repeat overlap with other hit */

#define  MIN_AA_IDENTITIES 17   /* minimum amino acid identities to report */

#define  MIN_DNA_IDENTITIES 50   /* minimum DNA identities to report */

#define  MIN_AA_PERCENT    40   /* minimum percent amino acid homology to report */

#define  MIN_DNA_PERCENT  60	/* minimum percent DNA homology to report */

#define  QUERY_START    12      /* offset past query and start on Query: line */

#define  FORWARD	1	/* forward (plus) strand */
#define  REVERSE	2	/* reverse (minus) strand */


/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  base [ MAX_BASES ];	/* DNA bases */
} t_seq;


/* GRAIL exon */
typedef  struct {
  char  name [ MAX_LINE ];	/* sequence name */
  int	length;			/* sequence length */
  int   start;			/* exon donor start */
  int   end;			/* exon acceptor stop */
  int   orf_start;		/* start of open reading frame */
  int   orf_end;		/* end of open reading frame */
  char  strand;			/* DNA sequence strand: f - Plus strand, f - Minus strand */
  int   frame;			/* reading frame */
  int	score;			/* quality score */
  char  quality [ MAX_LINE ];	/* exon quality */
} t_exon;


/* Table of GRAIL exons */
typedef  struct {
  int     total;		/* number of GRAIL exons */
  t_exon  exon [ MAX_EXONS ];	/* array of GRAIL exons */
} t_exons;


/* Homology hit. */
typedef  struct {
  long  first;			/* coordinate of first base of query seq. */
  long  len;			/* length of query sequence */
  long  start;			/* start of homology */
  long  end;			/* end of homology */
  long  hit_start;		/* start of homology in database hit */
  long  hit_end;		/* end of homology in database hit */
  int   orientation;		/* Forward or Reverse orientation */
  long  identities;		/* number of identity bases */
  long  percent;		/* percent identity */
  long  length;			/* length of homology */
  char  name [ MAX_LINE ];	/* name of query sequence */
  char  database [ MAX_LINE ];	/* database name */
  char  seq_type [ MAX_LINE ];	/* sequence classification type */
  char  desc_1 [ MAX_LINE ];	/* Description line 1 */
  char  desc_2 [ MAX_LINE ];	/* Description line 2 */
  t_seq region;			/* sequence in area of homology */
  t_seq ident;			/* sequence identity bases */
} t_homology;


/* Multiple homology hits table. */
typedef  struct {
  int   total;			/* number of significant homologies */
  int   repeats;		/* number of repetitive homologies */
  long  start;			/* lowest start of homologies */
  long  end;			/* highest end of homologies */
  t_homology  hit [ MAX_HITS ]; /* single homologies */
} t_hits;


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
   2, 0, 1, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0,
   0, 0, 0, 0, 0, 0,
   2, 0, 1, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0 
};


static  char  reverse_DNA [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*51 52 53 54 55 56 57 58 59 60 61 62 63 64 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*  a   b   c   d  e  f   g   h  i  j  k  l  m   n   o  p  q   r   s   t   u  v  w   x    y   z */
   'T', 0, 'G', 0, 0, 0, 'C', 0, 0, 0, 0, 0, 0, 'N', 0, 0, 0, 'Y', 0, 'A', 0, 0, 0, 'X', 'R', 0,
   0, 0, 0, 0, 0, 0,
   't', 0, 'g', 0, 0, 0, 'c', 0, 0, 0, 0, 0, 0, 'n', 0, 0, 0, 'y', 0, 'a', 0, 0, 0, 'x', 'r', 0
};


main ()
{
  char    clone_name [ MAX_LINE ];	/* source identifier clone name */
  t_text  cmds;				/* Unix shell commands file */
  t_exon  exon;				/* GRAIL exon */
  t_text  fof;				/* file of filenames */
  char    grail_version [ MAX_NAME ];	/* GRAIL version (1, 1a, 2) */
  t_text  in;				/* input GRAIL output file */
  FILE    *fopen ();			/* file open function */


  printf ( "This is the GRAIL e-mail response output naming program.\n\n" );

  /* Prompt for the input file name. */
  prompt_file ( &fof, "What is the name of the list of GRAIL output file names?" );
 
  printf ( "The output file being generated is:\n\n" ); 
  strcpy ( cmds.name, "cmds" );
  printf ( "\t%s\t\tUnix shell commands to rename files\n\n", cmds.name );

  /* Open the commands file. */
  cmds.data = fopen ( cmds.name, "w" );

  /* Process the sequences. */
  if ( fof.eof != TRUE )
  {
    /* Process the sequences. */
    while ( fof.eof != TRUE )
    {
      /* Open the sequence output file. */
      strcpy ( in.name, fof.line );
      in.name [ stridx ( in.name, "\n" ) ] = '\0';
      in.name [ stridx ( in.name, "*" ) ] = '\0';

      /* Open the DNA sequence file for reading. */
      open_text_file ( &in );

      /* Find a sequence name. */
      while ( ( in.eof != TRUE ) && ( ( in.line [ 0 ] != '>' ) ||
              ( in.line [ 1 ] == '%' ) ) )
        get_line ( &in );

      if ( in.eof != TRUE )
      {
        /* Get the sequence name. */
        read_name_length ( &in, &exon );
        fclose ( in.data );
        open_text_file ( &in );

        /* Identify the GRAIL output file. */
        identify_grail ( &in, grail_version, clone_name );

        exon.name [ stridx ( exon.name, "." ) ] = '\0';
        printf ( "Processing %s (%s): GRAIL Version '%s'\n", 
            in.name, exon.name, grail_version );
        fprintf ( cmds.data, "mv %s %s\n", in.name, clone_name );
      }  /* if */

      fclose ( in.data );
      get_line ( &fof );
    }  /* while */
  }  /* if */

  /* Close the commands file. */
  fclose ( fof.data );
  fclose ( cmds.data );

  printf ( "\nEnd of program.\n" );
}  /* main */


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
/* This function finds the Query= line. */
find_query ( in, hit )
t_text      *in;		/* BLAST output file */
t_homology  *hit;		/* database homology hit */
{
  int     flag;			/* Boolean flag */
  int     index;		/* line index */


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
}  /* find_query */


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


/******************************************************************************/
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


/******************************************************************************/
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


/******************************************************************************/
/* This function gets the next text token. */
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
  if (c >= 'A' && c <= 'Z')

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


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
  if ( (*text).name [ 0 ] == '\0' )  return;

  /* Open the text file in read mode. */
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) != NULL )
  {
    (*text).eof = FALSE;

    /* Get the first line of text. */
    get_line ( text );
  }  /* if */
}  /* open_text_file */


/******************************************************************************/
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
  if ( desc [ stridx ( desc, " alu" ) ] != '\0' )  strcpy ( (*hit).seq_type, "alu" );
  if ( desc [ stridx ( desc, " Alu" ) ] != '\0' )  strcpy ( (*hit).seq_type, "alu" );
  if ( desc [ stridx ( desc, "ALU" ) ] != '\0' )  strcpy ( (*hit).seq_type, "alu" );
  if ( ( desc [ 7 ] == 'A' ) && ( desc [ 8 ] == 'L' ) && ( desc [ 9 ] == 'U' ) )
    strcpy ( (*hit).seq_type, "alu" );

  if ( desc [ stridx ( desc, " L1 " ) ] != '\0' )  strcpy ( (*hit).seq_type, "L1" );
  if ( ( desc [ 0 ] == 'L' ) && ( desc [ 1 ] == '1' ) &&
       ( ( desc [ 2 ] == '\n' ) || ( desc [ 2 ] == '\0' ) ) )  strcpy ( (*hit).seq_type, "L1" );
  if ( strcmp ( desc, "L1" ) == 0 )  strcpy ( (*hit).seq_type, "L1" );
  if ( desc [ stridx ( desc, " Line" ) ] != '\0' )  strcpy ( (*hit).seq_type, "LINE" );
  if ( desc [ stridx ( desc, " LINE" ) ] != '\0' )  strcpy ( (*hit).seq_type, "LINE" );
  if ( desc [ stridx ( desc, "LINE-1" ) ] != '\0' )  strcpy ( (*hit).seq_type, "L1" );
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



/******************************************************************************/
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


/******************************************************************************/
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
  if ( (*hits).total == MAX_HITS )  return;

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

  (*best).hit_start   = (*hit).hit_start;
  (*best).hit_end     = (*hit).hit_end;
  (*best).orientation = (*hit).orientation;

  (*best).identities = (*hit).identities;
  (*best).percent    = (*hit).percent;
  (*best).length     = (*hit).length;

  strcpy ( (*best).name, (*hit).name );
  strcpy ( (*best).database, (*hit).database );
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


/* This function intializes an exon record. */
init_exon ( exon )
t_exon  *exon;			/* exon record */
{
  /* Note that the exon name is not initialized. */
  (*exon).start     = 0;
  (*exon).end       = 0;
  (*exon).orf_start = 0;
  (*exon).orf_end   = 0;
  (*exon).strand    = ' ';
  (*exon).frame     = 0;
  (*exon).score     = 0;

  (*exon).quality [ 0 ] = '\0';
}  /* init_exon */


/* This function intializes an homology hit record. */
init_homology ( hit )
t_homology  *hit;		/* homology hit */
{
  int  index;


  /* Initialize. */
  (*hit).start = (*hit).end = (*hit).identities = (*hit).length = 0;
  (*hit).percent = (*hit).hit_start = (*hit).hit_end = 0;
  (*hit).first = 1;
  (*hit).orientation = FORWARD;

  (*hit).region.total = (*hit).ident.total = 0;

  for ( index = 0; index < MAX_BASES; index++ )
  {
    (*hit).region.base [ index ] = ' ';
    (*hit).ident.base [ index ] = ' ';
  }  /* for */
}  /* init_homology */


/* This function tests if the homology hit is to a repetitive sequence. */
is_repeat ( hit )
t_homology  *hit;		/* homology hit to check */
{
  if ( ( strcmp ( (*hit).seq_type, "alu" ) == 0 ) ||
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
  if ( (*hit).orientation == REVERSE )
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
  if ( (*hit).orientation == REVERSE )  printf ( ", Minus Strand" );
  printf ( "\n\n" );
  printf ( "Highest Homology: %s", (*hit).desc_1 );

  if ( (*hit).desc_2 [ 0 ] != '\0' )
    printf ( "%s", (*hit).desc_2 );

  /* Print out the DNA sequence homology hit. */
  if ( strcmp ( (*hit).database, "Swiss" ) != 0 )
    for ( index = (*hit).start; (index <= (*hit).end) && 
        (index - (*hit).start < MAX_HIT_DISPLAY); index += 50 )
    {
      /* Print the query sequence. */
      printf ( "\n %7d  ", index  + (*hit).first  - 1 );
      for ( base = index; (base < index + 50) && (base <= (*hit).end); base++ )
        printf ( "%c", (*hit).region.base [ base ] );

      /* Print only the database sequence hit bases identical to query seq. */
      printf ( "\n(%7d) ", index  - (*hit).start + (*hit).hit_start );
      for ( base = index; (base < index + 50) && (base <= (*hit).end); base++ )
        printf ( "%c", (*hit).ident.base [ base ] );
      printf ( "\n" );
    }  /* for */
  else
    print_aa_msa ( hits );
}  /* print_homology */


/* This function prints out a multiple hit multiple sequence alignment. */
print_aa_msa ( hits )
t_hits  *hits;			/* homology hits table */
{
  int  base;
  int  index;			/* amino acid index */
  int  hit_index;		/* hits table index */
  char name [ MAX_LINE ];	/* Swiss protein sequence name */

/*  printf ( "\nhits.repeats = %d\n", (*hits).repeats ); */

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
/*      printf ( "(%7d) ", index  - (*hits).hit [ hit_index ].start + 
          (*hits).hit [ hit_index ].hit_start ); */
      printf ( "%10s  ", name );

      for ( base = index; (base <= (*hits).end); base += 3 )

        printf ( "%c", (*hits).hit [ hit_index ].ident.base [ base/3 ] );
      printf ( "\n" );
    }  /* for */
}  /* print_aa_msa */


/* This function finds the GRAIL exon score. */
find_exon_score ( exon, exons )
t_exon  *exon;		/* current exon */
t_exons *exons;		/* table of exons */
{
  int  index;		/* exons table index */


  /* Find the matching exon. */
  for ( index = 0; index < (*exons).total; index++ )
  {
    if ( ( (*exon).start     == (*exons).exon [ index ].start ) &&
         ( (*exon).end       == (*exons).exon [ index ].end   ) &&
         ( (*exon).frame     == (*exons).exon [ index ].frame ) &&
         ( (*exon).orf_start == (*exons).exon [ index ].orf_start ) &&
         ( (*exon).orf_end   == (*exons).exon [ index ].orf_end   ) )
    {
      (*exon).score = (*exons).exon [ index ].score;
      return;
    }  /* if */
  }  /* for */
}  /* find_exon_score */


/* Identify the GRAIL version. */
identify_grail ( in, version, clone_name )
t_text  *in;		/* GRAIL output file */
char    version [];	/* GRAIL version (1, 1a, 2) */
char    clone_name [];	/* source clone name */
{
  int  index = 0;	/* array index */


  clone_name [ 0 ] = '\0';
  version [ 0 ] = '\0';

  /* Process the output file. */
  while ( ( (*in).eof != TRUE ) && 
          ( (*in).line [ stridx ( (*in).line, "Subject" ) ] != 'S' ) )

    /* Get the next line from the GRAIL file. */
    get_line  ( in );

  if ( (*in).line [ stridx ( (*in).line, "Subject" ) ] != 'S' ) 
  {
    printf ( "identify_grail: Could not find Subject line in input file '%s'.\n", (*in).name );
    printf ( "Stopped on line '%s'\n", (*in).line );
    return;
  }  /* if */

  index = stridx ( (*in).line, "GRAIL" );
  while ( ( index > 1 ) &&
          ( (*in).line [ index ] != ' ' ) )

    if ( (*in).line [ index ] != ' ' )  index--;

  if ( (*in).line [ index ] == ' ' )
    strcpy ( clone_name, &((*in).line [ index + 1 ]) );

  clone_name [ stridx ( clone_name, "\n" ) ] = '\0';

  /* Check for GRAIL1a. */
  if ( (*in).line [ stridx ( (*in).line, "GRAIL1a" ) ] == 'G' ) 
  {
    strcpy ( version, "1a" );
    return;
  }  /* if */

  /* Check for GRAIL2. */
  if ( (*in).line [ stridx ( (*in).line, "GRAIL2" ) ] == 'G' ) 
  {
    strcpy ( version, "2" );
    return;
  }  /* if */

  /* Check for GRAIL1. */
  if ( (*in).line [ stridx ( (*in).line, "GRAIL1" ) ] == 'G' ) 
  {
    strcpy ( version, "1" );
    return;
  }  /* if */
}  /* identify_grail */


/* Identify the GRAIL version. */
identify_grail_old ( in, version )
t_text  *in;		/* GRAIL output file */
char    version [];	/* GRAIL version (1, 1a, 2) */
{
  version [ 0 ] = '\0';

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    get_token ( in );

    /* Check for Potential exons line. */
    if ( strcmp ( (*in).token, "Potential" ) == 0 )
    {
      strcpy ( version, "1" );
      return;
    }  /* if */

    /* Check for Final Exon Prediction: line. */
    if ( strcmp ( (*in).token, "Final" ) == 0 ) 
    {
      get_line  ( in );
      get_line  ( in );
      get_token ( in );
      if ( strcmp ( (*in).token, "left" ) == 0 )
      {
        strcpy ( version, "1a" );
        return;
      }  /* if */

      if ( strcmp ( (*in).token, "start" ) == 0 )
      {
        strcpy ( version, "2" );
        return;
      }  /* if */
    }  /* if */

    get_line  ( in );
  }  /* while */
}  /* identify_grail_old */


/* This function processes the GRAIL output file. */
process_grail1 ( in, out )
t_text  *in;		/* GRAIL output file */
t_text  *out;		/* Summary output file */
{
  t_exon  exon;		/* current exon */

  int    get_int ();	/* get integer function */


  /* Initialization. */
  init_exon ( &exon );
  exon.name [ 0 ] = '\0';

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    if ( (*in).line [ 0 ] != '>' )
    {
      get_line ( in );

      /* Ignore blank lines. */
      while ( ( blank ( (*in).line ) == TRUE ) && ( (*in).eof != TRUE ) )
        get_line ( in );

      /* Get the first non-blank token. */
      if ( (*in).eof != TRUE )
        get_token ( in );
    }  /* if */

    /* Check for a description line */
    if ( (*in).line [ 0 ] == '>' ) 
    {
      read_name_length ( in, &exon );

      /* Advance to the next line. */
      get_line ( in );
    }  /* if */

    /* Check for Potential exons line. */
    if ( strcmp ( (*in).token, "Potential" ) == 0 ) 
    {
      get_line  ( in );
      get_line  ( in );
      get_line  ( in );
      get_token ( in );
      if ( strcmp ( (*in).token, "pos" ) == 0 )
      {
        get_line  ( in );
        get_line  ( in );
      }  /* if */

      /* Process the final exons. */
      while ( ( (*in).eof != TRUE ) && ( (*in).line [ 0 ] != '>' ) &&
              ( blank ( (*in).line ) != TRUE ) )
      {
        /* Read in the exon. */
        read_grail1_exon ( in, &exon );

        /* Write out the exon. */
        write_grail_exon ( out, &exon, "1" );

        init_exon ( &exon );		/* clear the fields */
        get_line  ( in );
      }  /* while */
    }  /* if */
  }  /* while */
}  /* process_grail1 */


/* This function processes the GRAIL output file. */
process_grail1a ( in, out )
t_text  *in;		/* GRAIL output file */
t_text  *out;		/* Summary output file */
{
  t_exon  exon;		/* current exon */
  t_exons exons;	/* table of exons */

  int    get_int ();	/* get integer function */


  /* Initialization. */
  exons.total = 0;
  init_exon ( &exon );
  exon.name [ 0 ] = '\0';

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    if ( (*in).line [ 0 ] != '>' )
    {
      get_line ( in );

      /* Ignore blank lines. */
      while ( ( blank ( (*in).line ) == TRUE ) && ( (*in).eof != TRUE ) )
        get_line ( in );

      /* Get the first non-blank token. */
      if ( (*in).eof != TRUE )
        get_token ( in );
    }  /* if */

    /* Check for a description line */
    if ( (*in).line [ 0 ] == '>' ) 
    {
      read_name_length ( in, &exon );

      exons.total = 0;

      /* Advance to the next line. */
      get_line ( in );
    }  /* if */

    /* Check for numeric score line. */
    if ( strcmp ( (*in).token, "left" ) == 0 )
    {
      get_line  ( in );
      if ( ( (*in).line [ 0 ] != '-' ) && ( (*in).eof != TRUE ) )
        printf ( "*WARNING* dashed line expected after left edge line '%s'.\n", (*in).line );

      get_line  ( in );

      /* Process the exon predictions. */
      while ( ( (*in).eof != TRUE ) && ( (*in).line [ 0 ] != '>' ) &&
              ( blank ( (*in).line ) != TRUE ) )
      {
        /* Read in the exon. */
        init_exon ( &(exons.exon [ exons.total ]) );		/* clear the fields */
        read_exon_score ( in, &(exons.exon [ exons.total ]) );

        if ( exons.total < MAX_EXONS )
          exons.total++;
        else
          printf ( "*WARNING* MAX_EXONS limit (%d) reached.\n", MAX_EXONS );

        get_line  ( in );
      }  /* while */
    }  /* if */

    /* Check for Final Exon Prediction: line. */
    if ( strcmp ( (*in).token, "Final" ) == 0 ) 
    {
      get_line  ( in );
      get_line  ( in );
      get_token ( in );
      if ( strcmp ( (*in).token, "left" ) == 0 )
      {
        get_line  ( in );
        if ( (*in).line [ 0 ] != '-' )
          printf ( "*WARNING* dashed line expected after left edge line '%s'.\n", (*in).line );
        get_line  ( in );
      }  /* if */

      /* Process the final exons. */
      while ( ( (*in).eof != TRUE ) && ( (*in).line [ 0 ] != '>' ) &&
              ( blank ( (*in).line ) != TRUE ) )
      {
        /* Read in the exon. */
        read_final_exon ( in, &exon );

        /* Find the exon score. */
        find_exon_score ( &exon, &exons );

        /* Write out the exon. */
        write_grail_exon ( out, &exon, "1a" );

        init_exon ( &exon );		/* clear the fields */
        get_line  ( in );
      }  /* while */
    }  /* if */
  }  /* while */
}  /* process_grail1a */


/* This function processes the GRAIL output file. */
process_grail2 ( in, out )
t_text  *in;		/* GRAIL output file */
t_text  *out;		/* Summary output file */
{
  t_exon  exon;					/* current exon */
  t_exons exons;	/* table of exons */

  int    get_int ();				/* get integer function */


  /* Initialization. */
  exons.total = 0;
  init_exon ( &exon );
  exon.name [ 0 ] = '\0';

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    if ( (*in).line [ 0 ] != '>' )
    {
      get_line ( in );

      /* Ignore blank lines. */
      while ( ( blank ( (*in).line ) == TRUE ) && ( (*in).eof != TRUE ) )
        get_line ( in );

      /* Get the first non-blank token. */
      if ( (*in).eof != TRUE )
        get_token ( in );
    }  /* if */

    /* Check for a description line */
    if ( (*in).line [ 0 ] == '>' ) 
    {
      read_name_length ( in, &exon );

      exons.total = 0;

      /* Advance to the next line. */
      get_line ( in );
    }  /* if */

    /* Check for numeric score line. */
    if ( strcmp ( (*in).token, "start" ) == 0 )
    {
      get_line  ( in );
      if ( ( (*in).line [ 0 ] != '-' ) && ( (*in).eof != TRUE ) )
        printf ( "*WARNING* dashed line expected after start/ line '%s'.\n", (*in).line );

      get_line  ( in );

      /* Process the exon predictions. */
      while ( ( (*in).eof != TRUE ) && ( (*in).line [ 0 ] != '>' ) &&
              ( blank ( (*in).line ) != TRUE ) )
      {
        /* Read in the exon. */
        init_exon ( &(exons.exon [ exons.total ]) );		/* clear the fields */
        read_exon_score ( in, &(exons.exon [ exons.total ]) );

        if ( exons.total < MAX_EXONS )
          exons.total++;
        else
          printf ( "*WARNING* MAX_EXONS limit (%d) reached.\n", MAX_EXONS );

        get_line  ( in );
      }  /* while */
    }  /* if */

    /* Check for Final Exon Prediction: line. */
    if ( strcmp ( (*in).token, "Final" ) == 0 ) 
    {
      get_line  ( in );
      get_line  ( in );
      get_token ( in );
      if ( strcmp ( (*in).token, "start" ) != 0 )
        printf ( "*WARNING* start/donor acceptor/stop expected on line '%s'.\n", (*in).line );
      get_line ( in );
      if ( (*in).line [ 0 ] != '-' )
        printf ( "*WARNING* dashed line expected on line '%s'.\n", (*in).line );
      get_line ( in );

      /* Process the final exons. */
      while ( ( (*in).eof != TRUE ) && ( (*in).line [ 0 ] != '>' ) &&
              ( blank ( (*in).line ) != TRUE ) )
      {
        /* Read in the exon. */
        read_final_exon ( in, &exon );

        /* Find the exon score. */
        find_exon_score ( &exon, &exons );

        /* Write out the exon. */
        write_grail_exon ( out, &exon, "2" );

        init_exon ( &exon );		/* clear the fields */
        get_line  ( in );
      }  /* while */
    }  /* if */
  }  /* while */
}  /* process_grail2 */


/* This function reads in the sequence name and length. */
read_name_length ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  int  index;			/* index */
  char token [ MAX_LINE ];	/* string token */


  /* Extract the sequence name. */
  if ( (*in).line [ 0 ] == '>' )
  {
    strcpy ( token, &((*in).line [ 1 ]) );
    token [ stridx ( token, " " ) ] = '\0';
    strcpy ( (*exon).name, token );
  }  /* if */
  else
  {
    printf ( "*WARNING* name line expected in read_name_length '%s'.\n", (*in).line );
    return;
  }  /* else */

  /* Extract the sequence length. */
  if ( (*in).line [ stridx ( (*in).line, ", len =" ) ] == ',' )
  {
    index = stridx ( (*in).line, ", len =" ) + 7;
    (*exon).length = get_integer ( &((*in).line [ index ]) );
  }  /* if */
  else
  {
    (*exon).length = 0;
    printf ( "*WARNING* no length found for sequence '%s'.\n", (*in).line );
  }  /* else */
}  /* read_name_length */


/* This function reads in the Exon prediction line. */
read_exon_score ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  int    get_int ();				/* get integer function */


  /* Read in the GRAIL exon coordinates. */
  read_grail_exon ( in, exon );

  /* Get the exon reading frame. */
  (*exon).frame = get_int ( in );		/* reading frame */

  /* Get the exon score. */
  (*exon).score = get_int ( in );		/* score */

  /* Read in the GRAIL Open Reading Frame (orf) pair. */
  read_grail_orf ( in, exon );
}  /* read_exon_score */


/* This function reads in the Final Exon Prediction: line. */
read_grail1_exon ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  int    get_int ();				/* get integer function */


  /* Read in the GRAIL exon coordinates. */
  read_grail_exon ( in, exon );

  /* Get the exon orientation. */
  get_token ( in );
  if ( ( strcmp ( (*in).token, "f" ) != 0 ) && ( strcmp ( (*in).token, "r" ) != 0 ) )
    printf ( "Unknown stand designator '%s' on line '%s'.\n", 
        (*in).token, (*in).line );
  else
    (*exon).strand = (*in).token [ 0 ];

  /* Get the strand probability. */
  get_token ( in );

  /* Get the exon reading frame. */
  (*exon).frame = get_int ( in );		/* reading frame */

  /* Get the exon quality. */
  read_exon_quality ( in, exon );

  /* Read in the GRAIL Open Reading Frame pair. */
  read_grail_orf ( in, exon );
}  /* read_grail1_exon */


/* This function reads in the Final Exon Prediction: line. */
read_final_exon ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  int    get_int ();				/* get integer function */


  /* Read in the GRAIL exon coordinates. */
  read_grail_exon ( in, exon );

  /* Get the exon orientation. */
  get_token ( in );
  if ( ( strcmp ( (*in).token, "f" ) != 0 ) && ( strcmp ( (*in).token, "r" ) != 0 ) )
    printf ( "Unknown stand designator '%s' on line '%s'.\n", 
        (*in).token, (*in).line );
  else
    (*exon).strand = (*in).token [ 0 ];

  /* Get the exon reading frame. */
  (*exon).frame = get_int ( in );		/* reading frame */

  /* Get the exon quality. */
  read_exon_quality ( in, exon );

  /* Read in the GRAIL Open Reading Frame pair. */
  read_grail_orf ( in, exon );
}  /* read_final_exon */


/* This function reads in the GRAIL exon coordinates. */
read_grail_exon ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  int    get_int ();				/* get integer function */


  /* Get the exon start/donor coordinate. */
  (*exon).start = get_int ( in );

  /* Ignore the '--' or '-' separator. */
  get_token ( in );
  if ( (*in).token [ 0 ] != '-' )
    printf ( "- separator expected not '%s' on line '%s'.\n", 
        (*in).token, (*in).line );

  /* Get the exon acceptor/stop coordinate. */
  (*exon).end = get_int ( in );
}  /* read_grail_exon */


/* This function reads in a GRAIL exon quality. */
read_exon_quality ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  /* Get the exon quality. */
  get_token ( in );
  if ( ( strcmp ( (*in).token, "marginal" )  != 0 ) &&
       ( strcmp ( (*in).token, "good" )      != 0 ) &&
       ( strcmp ( (*in).token, "excellent" ) != 0 ) )
     printf ( "Unknown exon quality '%s' on line '%s'.\n", (*in).token, (*in).line );
  else
    strcpy ( (*exon).quality, (*in).token );
}  /* read_exon_quality */


/* This function reads in the open reading frame pair. */
read_grail_orf ( in, exon )
t_text  *in;		/* GRAIL output file */
t_exon  *exon;		/* current exon */
{
  int    get_int ();				/* get integer function */


  /* Get the start of the open reading frame. */
  (*exon).orf_start = get_int ( in );

  /* Ignore the '-' separator. */
  get_token ( in );
  if ( strcmp ( (*in).token, "-" ) != 0 )
    printf ( "- orf separator expected not '%s' on line '%s'.\n", 
        (*in).token, (*in).line );

  /* Get the end of the open reading frame. */
  (*exon).orf_end = get_int ( in );	
}  /* read_grail_orf */


/* This function writes out a GRAIL exon. */
write_grail_exon ( out, exon, version )
t_text  *out;		/* Summary output file */
t_exon  *exon;		/* current exon */
char    version [];	/* GRAIL version */
{
  fprintf ( (*out).data, "%s\t", (*exon).name );
  fprintf ( (*out).data, "%d\t", (*exon).length );
  fprintf ( (*out).data, "GRAIL\t" );
  fprintf ( (*out).data, "%s\t", version );
  fprintf ( (*out).data, "%d\t", (*exon).start );
  fprintf ( (*out).data, "%d\t", (*exon).end );
  fprintf ( (*out).data, "%d\t", (*exon).orf_start );
  fprintf ( (*out).data, "%d\t", (*exon).orf_end );
  fprintf ( (*out).data, "%c\t", (*exon).strand );
  fprintf ( (*out).data, "%d\t", (*exon).frame );
  fprintf ( (*out).data, "%s\t", (*exon).quality );
  fprintf ( (*out).data, "%d\n", (*exon).score );
}  /* write_grail_exon */


/* This function checks if the homology is significant enough to record. */
high_homology ( best )
t_homology *best;		/* best homology result */
{
  /* Check for no alignments. */
  if ( (*best).length <= 0 )  return ( FALSE );

  /* Check for protein database search results. */
  if ( strcmp ( (*best).database, "Swiss" ) == 0 )
  {
    /* Check for low amino acid homology complexity. */
    if ( low_aa_complexity ( best ) == TRUE )  return ( FALSE );

    /* Check for significant amino acid homology. */
    if ( ( ( (*best).identities * 100 ) / ((*best).length/3) >= MIN_AA_PERCENT ) &&
           ( (*best).identities >= MIN_AA_IDENTITIES ) )  return ( TRUE );
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
    (*hit).orientation = FORWARD;

    if ( ( start < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = start;

    if ( end > (*hit).end )  (*hit).end = end;
  }
  else
  {
    (*hit).orientation = REVERSE;

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
      if ( (*hit).orientation == FORWARD )
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
    (*hit).orientation = FORWARD;

    if ( ( start < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = start;

    if ( end > (*hit).end )  (*hit).end = end;
  }
  else
  {
    (*hit).orientation = REVERSE;

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
  if ( (*hit).length <= 0 )  (*hit).percent = 0;
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
}  /* prompt_file */


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

