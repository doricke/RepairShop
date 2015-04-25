
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

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      10001	/* maximum DNA sequence length */

#define  MAX_HIT_DISPLAY 250	/* maximum bases of homology to display */

#define  MAX_LINE	512	/* maximum line length */

#define  MIN_HOMOLOGY   45	/* minimum homology identity bases */

#define  MIN_OVERLAP    50	/* minimum repeat overlap with other hit */

#define  MIN_PERCENT    60	/* minimum percent homology to report */

#define  FORWARD	1	/* forward (plus) strand */
#define  REVERSE	2	/* reverse (minus) strand */


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
  long  identities;		/* number of identity bases */
  long  length;			/* length of homology */
  char  name [ MAX_LINE ];	/* name of query sequence */
  char  database [ MAX_LINE ];	/* database name */
  char  seq_type [ MAX_LINE ];	/* sequence classification type */
  char  desc_1 [ MAX_LINE ];	/* Description line 1 */
  char  desc_2 [ MAX_LINE ];	/* Description line 2 */
  t_seq DNA_region;		/* DNA sequence in area of homology */
  t_seq DNA_ident;		/* DNA sequence identity bases */
} t_homology;


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
  t_text  fof;			/* file of filenames */
  t_text  in;			/* input DNA sequence file */
  t_text  repeat;		/* repeat list for FATAL: errors */
  t_text  results;		/* Summary of Blast results */
  t_text  trace;		/* trace report */
  FILE    *fopen ();		/* file open function */


  printf ( "This is the Blast e-mail response output parsing program.\n\n" );

  /* Open the commands file. */
  results.data = fopen ( "results", "w" );

  /* Open the repeat list for FATAL: errors. */
  repeat.data = fopen ( "repeat", "w" );

  /* Open debug trace file. */
  trace.data = fopen ( "trace", "w" );

  /* Prompt for the input file name. */
  prompt_file ( &fof, "What is the list of sequences file name?" );

  /* Process the sequences. */
  while ( fof.eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, fof.line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Process the BLAST results file. */
    if ( fof.eof != TRUE )

      process_blast ( &in, &results, &repeat, &trace );

    /* Get the next file name. */
    get_line ( &fof );
 
    /* Close the BLAST results file. */ 
    fclose ( in.data );
  }  /* while */

  /* Close the commands file. */
  fclose ( fof.data );
  fclose ( results.data );
  fclose ( repeat.data );
  fclose ( trace.data );

  printf ( "\nEnd of program.\n" );
}  /* main */


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
/* printf ( "/Length= %d, '%s'\n", (*hit).len, (*in).line ); */
}  /* find_query */


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
            ( (*text).next == '.' ) || ( (*text).next == '-' ) ) 
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
  if (c >= 'A' && c <= 'Z')

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


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
  seq_composition ( &((*hit).DNA_ident), composition, &count );

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
        (*repeat).DNA_region.base [ index ] = (*other).DNA_region.base [ index ];
        (*repeat).DNA_ident.base  [ index ] = (*other).DNA_ident.base [ index ];
        if ( (*other).DNA_ident.base [ index ] != ' ' )  (*repeat).identities++;
      }  /* for */
      (*repeat).start = (*other).start;
    }  /* if */

    if ( (*other).end > (*repeat).end )
    {
      for ( index = (*repeat).end + 1; index <= (*other).end; index++ )
      {
        (*repeat).DNA_region.base [ index ] = (*other).DNA_region.base [ index ];
        (*repeat).DNA_ident.base  [ index ] = (*other).DNA_ident.base [ index ];
        if ( (*other).DNA_ident.base [ index ] != ' ' )  (*repeat).identities++;
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

  (*best).identities = (*hit).identities;
  (*best).length     = (*hit).length;

  strcpy ( (*best).name, (*hit).name );
  strcpy ( (*best).database, (*hit).database );
  strcpy ( (*best).seq_type, (*hit).seq_type );
  strcpy ( (*best).desc_1, (*hit).desc_1 );
  strcpy ( (*best).desc_2, (*hit).desc_2 );

  (*best).DNA_region.total = (*hit).DNA_region.total;
  (*best).DNA_ident.total  = (*hit).DNA_ident.total;

  for ( index = 0; ( index < MAX_BASES ) && ( index < (*hit).DNA_region.total );
      index++ )

    (*best).DNA_region.base [ index ] = (*hit).DNA_region.base [ index ];

  for ( index = 0; ( index < MAX_BASES ) && ( index < (*hit).DNA_ident.total ); 
      index++ )

    (*best).DNA_ident.base [ index ] = (*hit).DNA_ident.base [ index ];
}  /* copy_homology */


/* This function intializes an homology hit record. */
init_homology ( hit )
t_homology  *hit;		/* homology hit */
{
  int  index;


  /* Initialize. */
  (*hit).start = (*hit).end = (*hit).identities = (*hit).length = 0;
  (*hit).len = (*hit).hit_start = (*hit).hit_end = 0;
  (*hit).first = 1;

  (*hit).seq_type [ 0 ] = (*hit).desc_1 [ 0 ] = (*hit).desc_2 [ 1 ] = '\0';

  (*hit).DNA_region.total = (*hit).DNA_ident.total = 0;

  for ( index = 0; index < MAX_BASES; index++ )
  {
    (*hit).DNA_region.base [ index ] = ' ';
    (*hit).DNA_ident.base [ index ] = ' ';
  }  /* for */
}  /* init_homology */


/* Record no homology hit. */
print_no_homology ( hit, out )
t_homology  *hit;		/* homology hit to print */
t_text      *out;		/* Summary output file */
{
  /* Write the homology hit to the summary output file. */
  fprintf ( (*out).data, "%s\t", (*hit).name );
  fprintf ( (*out).data, "0\t" );
  fprintf ( (*out).data, "0\t" );
  fprintf ( (*out).data, "0\t" );
  fprintf ( (*out).data, "0\t" );
  fprintf ( (*out).data, " \t" );
  fprintf ( (*out).data, "-\t" );
  fprintf ( (*out).data, "No significant homology\n" );
}  /* print_no_homology */


/* Print out the homology hit. */
print_homology ( hit, out )
t_homology  *hit;		/* homology hit to print */
t_text      *out;		/* Summary output file */
{
  int  base;			/* base index */
  int  index;			/* loop index */


  /* Write the homology hit to the summary output file. */
  fprintf ( (*out).data, "%s\t", (*hit).name );
  fprintf ( (*out).data, "%d\t", (*hit).start + (*hit).first - 1 );
  fprintf ( (*out).data, "%d\t", (*hit).end + (*hit).first - 1 );
  fprintf ( (*out).data, "%d\t", (*hit).identities );
  fprintf ( (*out).data, "%d\t", (*hit).length );
  fprintf ( (*out).data, "%s\t", (*hit).database );
  fprintf ( (*out).data, "%s\t", (*hit).seq_type );
  fprintf ( (*out).data, "%d\t", (*hit).hit_start );
  fprintf ( (*out).data, "%s",   (*hit).desc_1 );
  fprintf ( (*out).data, "%s\n", (*hit).desc_2 );

  /* Don't report E. coli, vector, and repeat hits in summary. */
  if ( ( strcmp ( (*hit).seq_type, "vector" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "E.coli" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "alu" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "cs16" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "MER" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "rodent" ) == 0 ) ||
       ( strcmp ( (*hit).database, "REPEAT" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "THE" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "MLT1d" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "MSTb" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "microsatellite" ) == 0 ) ||
       ( strcmp ( (*hit).seq_type, "L1" ) == 0 ) )  return;

  /* Check the DNA composition of the homology hit. */
  /* check_composition ( hit ); */

  printf ( "----------------------------------------" );
  printf ( "----------------------------------------\n" );
  printf ( "%s  ", (*hit).name );
  if ( (*hit).length > 0 )
    printf ( "%d%% identity,  ", ( (*hit).identities * 100 / (*hit).length ) );
  printf ( "Size %d,  ", (*hit).length );
  if ( (*hit).len > 1 )
    printf ( "Length %d,  ", (*hit).len );
  printf ( "Database '%s',  ", (*hit).database );
  printf ( "Sequence type '%s'\n\n", (*hit).seq_type );

  printf ( "%s", (*hit).desc_1 );

  if ( (*hit).desc_2 [ 0 ] != '\0' )
    printf ( "%s", (*hit).desc_2 );

  /* Print out the DNA sequence homology hit. */
  for ( index = (*hit).start; (index <= (*hit).end) && 
      (index - (*hit).start < MAX_HIT_DISPLAY); index += 50 )
  {
    /* Print the query sequence. */
    printf ( "\n %7d  ", index  + (*hit).first  - 1 );
    for ( base = index; (base < index + 50) && (base <= (*hit).end); base++ )
      printf ( "%c", (*hit).DNA_region.base [ base ] );

    /* Print only the database sequence hit bases identical to query seq. */
    printf ( "\n(%7d) ", index  - (*hit).start + (*hit).hit_start );
    for ( base = index; (base < index + 50) && (base <= (*hit).end); base++ )
      printf ( "%c", (*hit).DNA_ident.base [ base ] );
    printf ( "\n" );
  }  /* for */
}  /* print_homology */


/* This function processes the BLAST output file. */
process_blast ( in, out, repeat, trace )
t_text  *in;		/* BLAST output file */
t_text  *out;		/* Summary output file */
t_text  *repeat;	/* repeat list for FATAL: errors */
t_text  *trace;		/* debug trace file */
{
  int  fatal = 0;				/* count of FATAL reports */
  char fatal_line [ MAX_LINE ];			/* fatal line */
  int  first_hit = TRUE;			/* first hit flag */
  t_homology  hit, best;			/* homology hits */

  int    get_int ();				/* get integer function */


  /* Initialization. */
  init_homology ( &hit );
  init_homology ( &best );
  fatal_line [ 0 ] = '\0';

  /* Get the query sequence name. */
  find_query ( in, &hit );

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
      check_repeat ( &hit, &best );
      if ( hit.identities > best.identities )
      {
        copy_homology ( &hit, &best );
        init_homology ( &hit );
      }  /* if */

      hit.database [ 0 ] = '\0';

      if ( (*in).line [ stridx ( (*in).line, "GSDB" ) ] != '\0' )
        strcpy ( hit.database, "GSDB" );

      if ( (*in).line [ stridx ( (*in).line, "REPEAT" ) ] != '\0' )
        strcpy ( hit.database, "REPEAT" );

      /* Add EST database ### */

    }  /* if */

    /* Check for multiple database searches. */
    if ( ( strcmp ( (*in).token, "Sequences" ) == 0 ) ||
         ( ( first_hit == TRUE ) && ( strcmp ( (*in).token, "MINUS" ) == 0 ) ) )
    {
      /* Check if recent hit is better than best hit. */
      check_repeat ( &hit, &best );
      if ( hit.identities > best.identities )
      {
        copy_homology ( &hit, &best );
        init_homology ( &hit );
      }  /* if */

      first_hit = TRUE;
    }  /* if */

    /* Check for a description line */
    if ( ( (*in).line [ 0 ] == '>' ) && ( hit.length == 0 ) )
    {
      strcpy ( hit.desc_1, &((*in).line [ 1 ]) );
      hit.desc_1 [ stridx ( hit.desc_1, "\n" ) ] = '\0';

      /* Check for a continued description line. */
      get_line ( in );
      get_token ( in );

      if ( strcmp ( (*in).token, "Length" ) != 0 )
      {
        strcpy ( hit.desc_2, &((*in).line [ 0 ]) );

        hit.desc_2 [ stridx ( hit.desc_2, "\n" ) ] = '\0';
      }  /* if */

      /* Classify the homology sequence type. */
      classify_hit ( &hit );
    }  /* if */
    else
      if ( (*in).line [ 0 ] == '>' )  first_hit = FALSE;

    /* Check for Query line. */
    if ( ( first_hit == TRUE ) && ( strcmp ( (*in).token, "Query" ) == 0 ) &&
         ( (*in).line [ 5 ] == ':' ) )

      /* Process the query line. */
      query_line ( in, &hit );

  }  /* while */

  /* Check if recent hit is better than best hit. */
  check_repeat ( &hit, &best );
  if ( hit.identities > best.identities )  copy_homology ( &hit, &best );

  /* Print best results. */
  if ( ( best.start > 0 ) && ( best.identities >= MIN_HOMOLOGY ) &&
       ( ( ( best.identities * 100 ) / best.length ) >= MIN_PERCENT ) )

    print_homology ( &best, out );

  else
  {
    /* Check for FATAL error message. */
    if ( fatal > 0 )
    {
      printf ( "----------------------------------------" );
      printf ( "----------------------------------------\n" );
      printf ( "%s\t%s\n", hit.name, fatal_line );

      if ( (*repeat).data != NULL )
        fprintf ( (*repeat).data, "%s\n", hit.name );

    }  /* if */

    print_no_homology ( &hit, out );
  }  /* else */
}  /* process_blast */


/* This function processes the Blast Query: line. */
query_line ( in, hit )
t_text	    *in;		/* input file */
t_homology  *hit;		/* homology hit */
{
  int  direction;		/* alignment strand */
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
    direction = FORWARD;

    if ( ( start < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = start;

    if ( end > (*hit).end )  (*hit).end = end;
  }
  else
  {
    direction = REVERSE;

    if ( ( end < (*hit).start ) || ( (*hit).start == 0 ) )
      (*hit).start = end;

    if ( start > (*hit).end )  (*hit).end = start;
  }  /* else */

  /* Save the DNA line. */
  strcpy ( DNA_line, (*in).line );

  /* Get the identity line. */
  get_line ( in );

  /* Copy the identity bases. */
  for ( index = 0, offset = 0; index < strlen ( DNA_line ); index++ )

    /* Check for a DNA base. */
    if ( DNA_base [ DNA_line [ index ] ] != 0 )
    {
      if ( direction == FORWARD )
      {
        (*hit).DNA_region.base [ start + offset ] = DNA_line [ index ];

        if ( (*in).line [ index ] == '|' )
        {
          if ( (*hit).DNA_ident.base [ start + offset ] == ' ' )

            (*hit).identities++;

          (*hit).DNA_ident.base [ start + offset ] = DNA_line [ index ];
        }  /* if */
      }
      else
      {
        (*hit).DNA_region.base [ start - offset ] = 
            reverse_DNA [ DNA_line [ index ] ];

        if ( (*in).line [ index ] == '|' )
        {
          if ( (*hit).DNA_ident.base [ start - offset ] == ' ' )

            (*hit).identities++;

          (*hit).DNA_ident.base [ start - offset ] = 
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

  (*hit).DNA_region.total = (*hit).end;
  (*hit).DNA_ident.total  = (*hit).end;
  (*hit).length = (*hit).end - (*hit).start + 1;
}  /* query_line */


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


/* This function tallies a DNA sequence composition. */
seq_composition ( seq, composition, count )
t_seq  *seq;		/* DNA sequence */
int    composition [];	/* DNA composition */
int    *count;		/* DNA bases */
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
    if ( ( (*seq).base [ index ] != ' ' ) &&
         ( (*seq).base [ index ] != '\t' ) )  (*count)++;
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

