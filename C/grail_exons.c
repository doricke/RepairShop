
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


#define  BLOCK_SIZE	60	/* output block size */

#define  CODON_SIZE     3	/* number of bases in DNA codon */

#define  TRUE		1
#define  FALSE		0

#define  MAX_ASCII	256	/* number of ASCII characters */

#define  MAX_BASES      100000  /* maximum DNA sequence length */

#define  MAX_CODON	64	/* number of codons */

#define  MAX_EXONS	1000	/* maximum number of exons */

#define  MAX_LINE	132	/* maximum line length */

#define  MIN_THREE_PRIME  8	/* minimum 3' splice site score */

#define  MIN_FIVE_PRIME   6     /* minimum 5' splice site score */

#define  SPLICE_SIZE	10	/* size of splicing signal templates */



static  int  base_map [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/*51 52 53 54 55 56 57 58 59 60 61 62 63 64 */
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

/* a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z */
   2, 0, 1, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 1, 0, 0, 1, 0, 0, 0, 0,
   0, 0, 0, 0, 0, 0,
   2, 0, 1, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 1, 0, 0, 1, 0, 0, 0, 0 
};


/* Genetic code */
static  char  codon_map [ 64 ] = {
  'F', 'F', 'L', 'L',     'S', 'S', 'S', 'S',  
  'Y', 'Y', '*', '*',     'C', 'C', '*', 'W',

  'L', 'L', 'L', 'L',     'P', 'P', 'P', 'P',
  'H', 'H', 'Q', 'Q',     'R', 'R', 'R', 'R',

  'I', 'I', 'I', 'M',     'T', 'T', 'T', 'T',
  'N', 'N', 'K', 'K',     'S', 'S', 'R', 'R',

  'V', 'V', 'V', 'V',     'A', 'A', 'A', 'A',
  'D', 'D', 'E', 'E',     'G', 'G', 'G', 'G'  };


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
    1, 0xE,   4, 0xB,   0,   0,   2, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   8,   8,   7,   9, 0xF, 0xC,   0,

    0,   0,   0,   0,   0,   0,

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1, 0xE,   4, 0xB,   0,   0,   2, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   8,   8,   7,   9, 0xF, 0xC,   0 };


/* GRAIL exon data. */
typedef struct {
  char  strand;		/* Forward (F) or Reverse (R) strand */
  char  frame;		/* Reading frame (0, 1, or 2) */
  long  position_1;	/* start position */
  long  position_2;	/* end   position */
  long  orf_1;		/* open reading frame start */
  long  orf_2;		/* open reading frame end */
  char  quality;	/* exon quality (m, g, e) */
} t_exon;

/* Table of GRAIL exons. */
typedef struct {
  int   total;		/* number of exons */
  t_exon  exon [ MAX_EXONS ];	/* exon array */
} t_exons;


/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  base [ MAX_BASES ];	/* DNA bases */
} t_seq;


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


main ()
{
  t_text  dna;		/* DNA input sequence */
  t_exons exons;	/* GRAIL exons table */
  t_text  grail;	/* GRAIL exon predictions */
  int     index;	/* exon index */
  t_text  out;		/* output file */
  t_seq   seq;		/* DNA sequence */


  /* Open the files. */
  strcpy ( grail.name, "grail.in" );
  open_text_file ( &grail );

  strcpy ( dna.name, "grail.cosmid" );
  open_text_file ( &dna );

  strcpy ( out.name, "grail.exons" );
  out.data = fopen ( out.name, "w" );

  /* Read in the GRAIL exon predictions. */
  read_grail ( &grail, &exons );

  printf ( "Grail exons read %d\n", exons.total );

  /* Read in the DNA sequence. */
  read_DNA_seq ( &dna, &seq );

  printf ( "DNA bases read %d\n", seq.total - 1 );

  /* Write out the GRAIL exons. */
  write_exons ( &exons, &seq, &out );

  /* Write the exons to individual files. */
  write_exon_files ( &exons, &seq, "grail" );

  printf ( "Exons written out.\n" );

  /* Map the GRAIL exons. */
  for ( index = 0; index < exons.total; index++ )
  {
    /* Write out the exon header. */
    exon_id ( &out, exons, index );

    map_DNA ( &seq, exons.exon [ index ].orf_1, 
        exons.exon [ index ].orf_2, &(exons.exon [ index ]), &out );
  }  /* for */

  /* Close the files. */
  fclose ( dna.data );
  fclose ( grail.data );
  fclose ( out.data );

  printf ( "\nEnd of main program.\n" );
}  /* main */


/* This function adds an exon to the exon table. */
add_exon ( exon, exons )
t_exon	*exon;		/* exon to add */
t_exons	*exons;		/* GRAIL exons */
{
  int  index;		/* exons table index */


  /* Scan for duplicate exon. */
  for ( index = 0; index < (*exons).total; index++ )
  
    /* Check for identical open reading frames. */
    if ( ( (*exons).exon [ index ].orf_1  == (*exon).orf_1 ) &&
         ( (*exons).exon [ index ].orf_2  == (*exon).orf_2 ) &&
         ( (*exons).exon [ index ].strand == (*exon).strand ) &&
         ( (*exons).exon [ index ].frame  == (*exon).frame ) )  return;

  if ( (*exons).total == 0 )  index = 0;
  else
    index = (*exons).total - 1;

  /* Check for full table. */
  if ( (*exons).total == MAX_EXONS )  return;

  if ( (*exons).total == MAX_EXONS - 1 )
    printf ( "Maximum number of GRAIL exons reached.\n" );

  /* Find the insertion location. */
  while ( ( index >= 0 ) && ( (*exon).orf_1 < (*exons).exon [ index ].orf_1 ) )
  {
    /* Shift records down one. */
    (*exons).exon [ index + 1 ].strand     = (*exons).exon [ index ].strand;
    (*exons).exon [ index + 1 ].frame      = (*exons).exon [ index ].frame;
    (*exons).exon [ index + 1 ].position_1 = (*exons).exon [ index ].position_1;
    (*exons).exon [ index + 1 ].position_2 = (*exons).exon [ index ].position_2;
    (*exons).exon [ index + 1 ].orf_1      = (*exons).exon [ index ].orf_1;
    (*exons).exon [ index + 1 ].orf_2      = (*exons).exon [ index ].orf_2;
    (*exons).exon [ index + 1 ].quality    = (*exons).exon [ index ].quality;
    index--;
  }  /* while */

  if ( index < 0 )  index = 0;

  if ( (*exons).total > 0 )

    if ( (*exon).orf_1 >= (*exons).exon [ index ].orf_1 )  index++;

  /* Store the new exon. */
  (*exons).exon [ index ].strand     = (*exon).strand;
  (*exons).exon [ index ].frame      = (*exon).frame;
  (*exons).exon [ index ].position_1 = (*exon).position_1;
  (*exons).exon [ index ].position_2 = (*exon).position_2;
  (*exons).exon [ index ].orf_1      = (*exon).orf_1;
  (*exons).exon [ index ].orf_2      = (*exon).orf_2;
  (*exons).exon [ index ].quality    = (*exon).quality;
  (*exons).total++;
}  /* add_exon */


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


/* This function writes out an exon header. */
exon_id ( text, exons, index )
t_text   *text;         /* output file */
t_exons  *exons;	/* GRAIL exons table */
int      index;         /* current exon */
{
    fprintf ( (*text).data, "GRAIL exon %d ", index + 1 );
    fprintf ( (*text).data, "%c   ",  (*exons).exon [ index ].strand     );
    fprintf ( (*text).data, "%c   ",  (*exons).exon [ index ].frame      );
    fprintf ( (*text).data, "%6d - ", (*exons).exon [ index ].position_1 );
    fprintf ( (*text).data, "%6d   ", (*exons).exon [ index ].position_2 );
    fprintf ( (*text).data, "%6d - ", (*exons).exon [ index ].orf_1      );
    fprintf ( (*text).data, "%6d   ", (*exons).exon [ index ].orf_2      );
    fprintf ( (*text).data, "%c\n\n", (*exons).exon [ index ].quality    );
}  /* exon_id */


/* This function gets the next text character. */
get_char ( text )
t_text  *text;		/* ASCII text file */
{
  /* Get the next sequence character. */
  (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )

    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];

}  /* get_char */


/* This function gets the next integer from the current line. */
int  get_int ( text )
t_text  *text;		/* ASCII text file */
{
  char  c;
  int   i, sign = 1;


  /* Get the first character. */
  get_char ( text );

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
  while ( ( (*text).next == ' '  ) ||
          ( (*text).next == '\n' ) ||
          ( (*text).next == '\t' ) )
  {
    (*text).next = (*text).line [ (*text).line_index++ ];

    if ( (*text).next == '\n' )  get_line ( text );
  }  /* while */

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


/* This function translates a codon into an amino acid. */
char map_codon ( codon )
char codon [];		/* DNA codon */
{
  int  index;	/* codon table index */

  capitalize ( codon );

  index = base_map [ codon [ 0 ] ] * 16 +
          base_map [ codon [ 1 ] ] *  4 +
          base_map [ codon [ 2 ] ];

  if ( index >= MAX_CODON )  return ( '?' );
  else
    return ( codon_map [ index ] );
}  /* map_codon */


/* This function maps a DNA sequence range. */
map_DNA ( seq, first, last, exon, text )
t_seq   *seq;		/* DNA sequence */
long    first;		/* first DNA base index */
long    last;		/* last DNA base index */
t_exon  *exon;		/* GRAIL exon */
t_text  *text;		/* output text file */
{
  long  block;		/* end of sequence block */
  long  index;		/* sequence index */


  printf ( "Mapping exon [%d - %d]\n", first, last );

  /* Map the DNA sequence range. */
  index = first;
  while ( index <= last )
  {
    /* Process one block of sequence at a time. */
    block = index + BLOCK_SIZE - 1;

    if ( block > last )  block = last;

    if ( block >= (*seq).total )  block = (*seq).total - 1;

    /* Write out the DNA sequence map header. */
    map_header ( seq, index, block, text );

    /* Write out the DNA sequence. */
    write_seq ( seq, index, block, text );

    /* Write out the DNA translation. */
    map_seq ( seq, index, block, exon, text );

    fprintf ( (*text).data, "\n" );

    index += BLOCK_SIZE;
  }  /* while */
}  /* map_DNA */


/* This function writes out the DNA sequence map header. */
map_header ( seq, first, last, text )
t_seq  *seq;		/* DNA sequence */
long   first;		/* first DNA base index */
long   last;		/* last  DNA base index */
t_text *text;		/* output text file */
{
  long  i, index;			/* sequence index */
  int   score_1, score_2;		/* splice site scores */
  char  header      [ MAX_LINE ];       /* line header */
  char  five_prime  [ MAX_LINE ];	/* 5' splice consensus sequence */
  char  three_prime [ MAX_LINE ];	/* 3' splice consensus sequence */


  fprintf ( (*text).data, "          " );
  index = first;
  strcpy ( five_prime,  "AGGTAAGTJJ" );
  strcpy ( three_prime, "YYYYYYNCAG" );

  for ( i = 0; (i < MAX_LINE) && (i <= last - first); i++ )
    header [ i ] = ' ';
  header [ i ] = '\0';

  /* Search for the splicing signals. */
  while ( index <= last )
  {
    /* Check for splice site. */
    score_1 = score_2 = 0;

    for ( i = 0; i < SPLICE_SIZE; i++ )
    {
      score_1 += ( ( dna_mask [ five_prime [ i ] ] &
          dna_mask [ (*seq).base [ index + i ] ] ) != 0 );

      score_2 += ( ( dna_mask [ three_prime [ i ] ] &
          dna_mask [ (*seq).base [ index + i ] ] ) != 0 );
    }  /* for */

    if ( ( score_1 >= MIN_FIVE_PRIME ) &&
         ( ( dna_mask [ (*seq).base [ index + 2 ] ] & 
             dna_mask [ 'G' ] ) != 0 ) && 
         ( ( dna_mask [ (*seq).base [ index + 3 ] ] & 
             dna_mask [ 'T' ] ) != 0 ) )
    {
      header [ index - first + 1 ] = '5';
      header [ index - first + 2 ] = '\'';
    }
    else

      if ( ( score_2 >= MIN_THREE_PRIME ) &&
           ( ( dna_mask [ (*seq).base [ index + 8 ] ] &
               dna_mask [ 'A' ] ) != 0 ) &&
           ( ( dna_mask [ (*seq).base [ index + 9 ] ] &
               dna_mask [ 'G' ] ) != 0 ) )
      {
        header [ index - first + 9 ] = '3';
        header [ index - first + 10 ] = '\'';
      }  /* if */

    index++;
  }  /* while */

  fprintf ( (*text).data, "%s\n", header );
}  /* map_header */


/* This function writes out the DNA translation. */
map_seq ( seq, first, last, exon, text )
t_seq	*seq;		/* DNA sequence */
long	first;		/* first base of block */
long	last;		/* last base of block */
t_exon  *exon;		/* GRAIL exon */
t_text	*text;		/* output text file */
{
  char  codon [ CODON_SIZE + 1 ];	/* DNA codon */
  long  index;				/* sequence index */


  codon [ CODON_SIZE ] = '\0';
  index = first;

  /* Write out the DNA translation frame. */
  fprintf ( (*text).data, "%c ",          (*exon).strand );
  fprintf ( (*text).data, "%c       ", (*exon).frame );

  /* Check for forward reading frame. */
  if ( (*exon).strand == 'F' )
  {
/*    if ( (*exon).frame >= '1' )
    {
      index++;
      fprintf ( (*text).data, " " );
    }  /* if */

 /*   if ( (*exon).frame == '2' )
    {
      index++;
      fprintf ( (*text).data, " " );
    }  /* if */

    /* Translate the reading frame. */
    for ( ; index <= last; index += 3 )

      fprintf ( (*text).data, "%c  ", 
          map_codon ( &((*seq).base [ index ]) ) );

    fprintf ( (*text).data, "\n           " );
    /* Translate the reading frame. */
    for ( index = first + 1; index <= last; index += 3 )

      fprintf ( (*text).data, "%c  ", 
          map_codon ( &((*seq).base [ index ]) ) );

    fprintf ( (*text).data, "\n            " );
    /* Translate the reading frame. */
    for ( index = first + 2; index <= last; index += 3 )

      fprintf ( (*text).data, "%c  ", 
          map_codon ( &((*seq).base [ index ]) ) );
  }  /* if */
  else
  {
    /* Translate the complement reading frame. */

    for ( index = first; index <= last; index += 3 )
    {
      codon [ 0 ] = complement [ (*seq).base [ index + 2 ] ];
      codon [ 1 ] = complement [ (*seq).base [ index + 1 ] ];
      codon [ 2 ] = complement [ (*seq).base [ index     ] ];

      fprintf ( (*text).data, "%c  ", map_codon ( codon ) );
    }  /* for */

    fprintf ( (*text).data, "\n           " );

    for ( index = first + 1; index <= last; index += 3 )
    {
      codon [ 0 ] = complement [ (*seq).base [ index + 2 ] ];
      codon [ 1 ] = complement [ (*seq).base [ index + 1 ] ];
      codon [ 2 ] = complement [ (*seq).base [ index     ] ];

      fprintf ( (*text).data, "%c  ", map_codon ( codon ) );
    }  /* for */

    fprintf ( (*text).data, "\n            " );

    for ( index = first + 2; index <= last; index += 3 )
    {
      codon [ 0 ] = complement [ (*seq).base [ index + 2 ] ];
      codon [ 1 ] = complement [ (*seq).base [ index + 1 ] ];
      codon [ 2 ] = complement [ (*seq).base [ index     ] ];

      fprintf ( (*text).data, "%c  ", map_codon ( codon ) );
    }  /* for */
  }  /* else */

  fprintf ( (*text).data, "\n" );
}  /* map_seq */


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

  /* Get the first text line. */
  get_line ( text );

}  /* open_text_file */


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


/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* DNA sequence */
{
  int  count;		/* composition count */
  int  composition [ MAX_ASCII ];	/* composition array */
  int  index;


  /* Find the beginning of the DNA sequence. */
  do
  {
    /* Initialization. */
    for ( index = 0; index < MAX_ASCII; index++ )
      composition [ index ] = 0;

    count = 0;
    get_line ( text );		/* get the next line */

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
  }
  while ( ( (*text).eof != TRUE ) &&
          ( ( composition [ 'A' ] + composition [ 'a' ] +
              composition [ 'C' ] + composition [ 'c' ] +
              composition [ 'G' ] + composition [ 'g' ] +
              composition [ 'T' ] + composition [ 't' ] )*1.0 < count*0.75 ) );

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


/* This function reads a GRAIL annotation report. */
read_grail ( text, exons )
t_text  *text;		/* ASCII text file */
t_exons  *exons;	/* GRAIL exons table */
{
  t_exon  exon;		/* new exon */

  /* Initialization. */
  (*exons).total = 0;

  get_token ( text );

  /* Process the list of candidate exons. */
  while ( ( (*text).eof != TRUE ) && ( (*exons).total < MAX_EXONS ) )
  {
    /* Check for exon line. */
    if ( strcmp ( (*text).token, "exon" ) == 0 )
    {
      /* Get the DNA strand. */
      get_token ( text );	/* skip past 'exon' */
      exon.strand = (*text).token [ 0 ];

      /* Get the reading frame. */
      get_token ( text );
      exon.frame = (*text).token [ 0 ];

      /* Get the exon position. */
      exon.position_1 = get_int ( text );

      get_token ( text );	/* skip '-' */

      exon.position_2 = get_int ( text );

      /* Get the open reading frame coordinates. */
      exon.orf_1 = get_int ( text );

      get_token ( text );	/* skip '-' */

      exon.orf_2 = get_int ( text );

      /* Get the exon quality. */
      get_token ( text );		/* does get_int call get_token??? */
      exon.quality = (*text).token [ 0 ];

      /* Add the exon to the table. */
      add_exon ( &exon, exons );
    }  /* if */

    get_line  ( text );
    get_token ( text );
  }  /* while */
}  /* read_grail */


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


/* This function writes out the GRAIL exon open reading frames. */
write_exon_files ( exons, seq, name )
t_exons	*exons;		/* GRAIL exons */
t_seq   *seq;           /* DNA sequence */
char    name [];	/* source name */
{
  int  base, index, offset;
  FILE *out, *fopen (); 
  char exon_number [ MAX_LINE ];	/* exon number */
  char new_name    [ MAX_LINE ];	/* exon file name */


  /* Write out the GRAIL exons. */
  for ( index = 0; index < (*exons).total; index++ )
  {
    /* Truncate source name at first period. */
    strcpy ( new_name, name );
    new_name [ stridx ( new_name, "." ) ] = '\0';
    strcat ( new_name, "-" );
    itoa   ( (*exons).exon [ index ].orf_1, exon_number );
    strcat ( new_name, exon_number );
    strcat ( new_name, ".seq" );

    /* Open the exon output file. */
    out = fopen ( new_name, "w" );

    /* Start sequence files with sequence name. */
    fprintf ( out, ">%s\n", new_name );

    /* Write out the DNA sequence. */
    offset = (*exons).exon [ index ].orf_2 - (*exons).exon [ index ].orf_1 + 1;

    /* Write out the DNA sequence. */
    for ( base = 0; ( base <= offset ) && 
        ( base + (*exons).exon [ index ].orf_1 < (*seq).total ) ; base++ )
    {
      fprintf ( out, "%c", 
          (*seq).base [ base + (*exons).exon [ index ].orf_1 ] );

      if ( ( ( base + 1 ) % BLOCK_SIZE ) == 0 )

        fprintf ( out, "\n" );
    }  /* for */

    fprintf ( out, "\n\n" );

    fclose ( out );	/* close the exon file */
  }  /* for */
}  /* write_exon_files */


/* This function writes out the GRAIL exon open reading frames. */
write_exons ( exons, seq, text )
t_exons	*exons;		/* GRAIL exons */
t_seq   *seq;           /* DNA sequence */
t_text	*text;		/* output text file */
{
  int  base, index, offset;


  /* Write out the GRAIL exons. */
  for ( index = 0; index < (*exons).total; index++ )
  {
    /* Print out the exon header. */
    exon_id ( text, exons, index );

    /* Write out the DNA sequence. */
    offset = (*exons).exon [ index ].orf_2 - (*exons).exon [ index ].orf_1 + 1;

    for ( base = 0; ( base <= offset ) && 
        ( base + (*exons).exon [ index ].orf_1 < (*seq).total ) ; base++ )
    {
      fprintf ( (*text).data, "%c", 
          (*seq).base [ base + (*exons).exon [ index ].orf_1 ] );

      if ( ( ( base + 1 ) % BLOCK_SIZE ) == 0 )

        fprintf ( (*text).data, "\n" );
    }  /* for */

    fprintf ( (*text).data, "\n\n" );
  }  /* for */
}  /* write_exons */


/* This function writes out a DNA sequence block. */
write_seq ( seq, first, last, text )
t_seq	*seq;		/* DNA sequence */
long    first;		/* first base of block */
long    last;		/* last base of block */
t_text  *text;		/* output text file */
{
  long  index;		/* sequence index */


  /* Print out the coordinate of first base. */
  fprintf ( (*text).data, "%8d  ", first );

  /* Print out the DNA block. */
  for ( index = first; index <= last; index++ )

    fprintf ( (*text).data, "%c", (*seq).base [ index ] );

  /* Print out the last base coordinate. */
  fprintf ( (*text).data, "  %8d\n", last );

  /* Print out the sequence index markers. */
  fprintf ( (*text).data, "          " );

  for ( index = first; index <= last; index++ )

    if ( ( index % 10 ) == 0 )

      fprintf ( (*text).data, "0" );
    else
      fprintf ( (*text).data, "-" );

  /* Print out the complementary strand. */
  fprintf ( (*text).data, "\n          " );

  for ( index = first; index <= last; index++ )

    fprintf ( (*text).data, "%c", complement [ (*seq).base [ index ] ] );

  fprintf ( (*text).data, "\n" );
}  /* write_seq */


