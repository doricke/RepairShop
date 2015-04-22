
#include <stdio.h>
#include <math.h>

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

#define  BLOCK_SIZE	60	/* output block size */

#define  CODON_SIZE     3	/* number of bases in DNA codon */

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      100000  /* maximum DNA sequence length */

#define  MAX_CODON	64	/* number of codons */

#define  MAX_LINE	1012	/* maximum line length */

#define  MAX_NAME	20	/* maximum name length */

#define  MIN_SEQ	12	/* minimum sequence length */



#define	MAX_PATTERN	10	/* maximum search pattern size */

#define	MAX_PATTERNS	5000	/* maximum number of patterns */

#define	MAX_PER_LINE	100	/* maximum bases printed per line */

#define MAX_UNITS	4	/* maximum unit length to search up to */

#define AFTER_BASES	30	/* bases to print after a pattern match */
#define BEFORE_BASES	30	/* bases to print before a pattern match */

#define	P_SEQUENCE	1	/* search for unique sequence only */
#define	P_SUBUNIT	2	/* repeat pattern subunit minimum times */

#define CR		13	/* carriage return */

#define	P		0.1	/* Pattern search probablity */



/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  name [ MAX_LINE ];	/* file name */
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


/******************************************************************************/

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


static  int  base_map [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/* 51  52  53  54  55  56  57  58  59  60  61  62  63  64 */
   64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,

/* A   B  C   D   E   F  G   H   I   J   K   L   M */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* N   O   P   Q   R   S  T  U   V   W   X   Y   Z */
  64, 64, 64, 64, 64, 64, 0, 0, 64, 64, 64, 64, 64,

  64, 64, 64, 64, 64, 64,

/* a   b  c   d   e   f  g   h   i   j   k   l   m */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* n   o   p   q   r   s  t  u   v   w   x   y   z */
  64, 64, 64, 64, 64, 64, 0, 0, 64, 64, 64, 64, 64
};


static  int  base_map_3 [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/* 51  52  53  54  55  56  57  58  59  60  61  62  63  64 */
   64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,

/* A   B  C   D   E   F  G   H   I   J   K   L   M */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* N   O   P   Q   R   S  T  U   V   W   X   Y   Z */
  64, 64, 64, 64,  2, 64, 0, 0, 64, 64, 64,  0, 64,

  64, 64, 64, 64, 64, 64,

/* a   b  c   d   e   f  g   h   i   j   k   l   m */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* n   o   p   q   r   s  t  u   v   w   x   y   z */
  64, 64, 64, 64,  2, 64, 0, 0, 64, 64, 64,  0, 64
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


/******************************************************************************/

/* DNA/RNA bit representation - allows IUB nucleotide ambiguity */
static	int	base_masks [] = {

/* A    B    C    D   E  F   G    H   I  J   K   L   M */
  0x1, 0xE, 0x4, 0xB, 0, 0, 0x2, 0xD, 0, 0, 0xA, 0, 0x5,

/* N  O  P  Q   R    S    T    U    V    W    X   Y   Z */
   0, 0, 0, 0, 0x3, 0x6, 0x8, 0x8, 0x7, 0x9,  0, 0xC, 0 };

/* DNA/RNA bit representation to nucleotide map. */
static	char	dna_map [] = {

/* 0    1    2    3    4    5    6    7 */
  'x', 'A', 'G', 'R', 'C', 'M', 'S', 'V',

/* 8    9    A    B    C    D    E    F */
  'T', 'W', 'K', 'D', 'Y', 'H', 'B', 'N' };


/******************************************************************************/



main ()
{
  t_text  in;			/* input DNA sequence file */
  t_text  names;		/* file of sequence names file */
  t_seq   seq;         		/* DNA sequence */
  FILE    *fopen ();		/* file open function */


  printf ( "This is the Unit Patterns program.\n\n" );

  /* Prompt for the input file name. */
  prompt_file ( &names, "What is the list of sequences file name?" );

  printf ( "\n" );

  /* Process the sequences. */
  while ( names.eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, names.line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );
    fclose ( in.data );

    unit_patterns ( &seq );

    /* Get the next name. */
    get_line ( &names );
  }  /* while */

  printf ( "End of program.\n" );
}  /* main */


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


  strcpy ( (*seq).name, (*text).name );

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
  (*seq).total = 0;

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

  strcpy ( (*pat1).name, (*pat2).name );
  strcpy ( (*pat1).base, (*pat2).base );
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
t_seq		*sequence;	/* entire sequence */
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
/*      print_match ( sequence, pattern ); */
      add_pattern ( pattern, patterns );
      index += (*pattern).length * count - 1;
    }  /* if */
  }  /* for */
}  /* find_pattern */


/******************************************************************************/
/*This function searches for unit patterns. */
unit_patterns ( sequence )
t_seq		*sequence;		/* entire sequence */
{
  int	  	index;				/* pattern index */
  double  	length;				/* calculated sequence length */
  unsigned	mask;				/* bit representation of base */
  char		*pat_bases [ MAX_UNITS ];	/* pointers to nucleotides */
  int	  	pat_index;			/* pattern index */
  t_pattern	pattern;			/* search pattern */
  t_patterns	patterns;			/* all matching patterns */
  int		not_done;			/* search flag */
  char	  	nucleotides [ MAX_LINE ];	/* possible bases for patterns */
  int	  	unit;				/* current unit length */


  if ( (*sequence).total <= 0 )  return;
  patterns.total = 0;

  /* Generate the search patterns. */
  pattern.type = P_SUBUNIT;
  pattern.match = 1.0;
  for ( unit = 1; unit <= MAX_UNITS; unit++ )
  {
    if ( unit == 1 )
      strcpy ( nucleotides, "ACGTMRWSYK" );
    else
      strcpy ( nucleotides, "MRWSYK" );

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
      pattern.minimum = 15;
      if ( unit == 2 )  pattern.minimum = 10;
      if ( unit == 3 )  pattern.minimum = 8;
      if ( unit == 4 )  pattern.minimum = 6;

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

  /* Print out any matching patterns. */
  if ( patterns.total > 0 )
  {
    printf ( "%s\n\n", (*sequence).name );

    for ( index = 0; index < patterns.total; index++ )

      print_match ( sequence, patterns.pat [ index ] );

    printf ( "\n" );
  }  /* if */
}  /* unit_patterns */


/******************************************************************************/
/* This function prints out a pattern match. */
print_match ( sequence, pattern )
t_seq		*sequence;	/* entire sequence */
t_pattern	*pattern;	/* current pattern */
{
  int   i;		/* space counter */
  int	printed;	/* number of bases printed on current line */
  long	sub_index;	/* pattern index */


  printf ( "     " );
  sub_index = (*pattern).start - BEFORE_BASES;	/* include surrounding bases */
  printed = 0;
  do 
  {
    /* Separate matched sequence from surrounding bases. */
    if ( sub_index == (*pattern).start )  printf ( "  " ); 
    if ( sub_index == (*pattern).start + (*pattern).length * (*pattern).count )
    {
      printf ( "  " );

      /* If repeated subunit search, then space match to maximum width. */
      if (( (*pattern).type == P_SUBUNIT ) &&
          ( printed < MAX_PER_LINE - AFTER_BASES ))
        while ( printed < MAX_PER_LINE - AFTER_BASES )
        {
          printf ( " " );
          printed++;
        }  /* if */
      }  /* if */

    /* Print the base or a space. */
    if (( sub_index < 0 ) || ( sub_index >= (*sequence).total ))
      printf ( " " );
    else
      printf ( "%c", (*sequence).base [ sub_index ] );
    printed++;

    /* Print the partial line trailer. */
    if (( printed == MAX_PER_LINE - AFTER_BASES ) &&
        ( sub_index < (*pattern).start + (*pattern).length * (*pattern).count - 1 ))
    {  
      for ( i = 1; i <= AFTER_BASES; i++ )  printf ( " " );
      printf ( "  " );
      printed = MAX_PER_LINE;
    }  /* if */


    /* Print the pattern information on end of lines. */
    if (( printed == MAX_PER_LINE ) ||
        ( sub_index == (*pattern).start + (*pattern).length * (*pattern).count - 1 + AFTER_BASES))
    {
      printf ( "    %4s(%4d)  %6d", (*pattern).name, (*pattern).count, (*pattern).start + 1 );
/*      printf ( "%s", (*sequence).name ); */
      printf ( "\n" );

      /* Print the leader for the next line. */
      if ( sub_index < (*pattern).start + (*pattern).length * (*pattern).count - 1 + AFTER_BASES )
      {
        printf ( "     " );
        for ( i = 1; i <= BEFORE_BASES; i++ )  printf ( " " );
        printf ( "  " );
        printed = BEFORE_BASES;
      }  /* if */
      else  printed = 0;
    }  /* if */
    sub_index++;
  }
  while ( sub_index <= (*pattern).start + (*pattern).length * (*pattern).count - 1 + AFTER_BASES );
}  /* print_match */


