
/******************************************************************************/
#include <stdio.h>

/******************************************************************************/

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

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      20000  /* maximum DNA sequence length */

#define  MAX_LINE	512	/* maximum line length */

#define  MAX_NAME       120     /* maximum name length */


/******************************************************************************/
/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  int   IUB_bases;		/* number of IUB bases */
  int   N_bases;		/* number of N bases */
  char  name [ MAX_NAME ];	/* sequence name */
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
static  int  iub_mask [ 123 ] = { 0,

/*    1    2    3    4    5    6    7    8    9   10 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 20 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 30 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 40 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 50 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 60 */
      0,   0,   0,   0,   

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    0, 0xE,   0, 0xB,   0,   0,   0, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   0,   0,   7,   9, 0xF, 0xC,   0,

    0,   0,   0,   0,   0,   0,

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    0, 0xE,   0, 0xB,   0,   0,   0, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   0,   0,   7,   9, 0xF, 0xC,   0 };


/******************************************************************************/
main ( argc, argv )
int  argc;		/* number of command line arguments */
char *argv [];		/* vector of command line arguments */
{
  t_text  fof;				/* file of file names file */

  FILE    *fopen ();			/* file open function */


  /* Check if command line argument specified. */
  if ( argc <= 1 )

    /* Prompt for the input file name. */
    prompt_file ( &fof, "What is the name of the Protein FASTA sequence library file?" );

  else
  {
    d_strcpy ( fof.name, argv [ 1 ] );
    open_text_file ( &fof );
  }  /* else */

  /* Process the FASTA file. */
  while ( fof.eof != TRUE )
  {
    composition ( &fof );
  }  /* while */

  fclose ( fof.data );
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
/* This function reads in a DNA sequence and computes the composition. */
composition ( text )
t_text	*text;		/* ASCII text file */
{
  char  c;				/* current line character */
  int   composition [ MAX_ASCII ];	/* composition array */
  int   end_of_file = FALSE;		/* end of file flag */
  long  index;
  char  name [ MAX_LINE ];		/* sequence name */
  long  total = 0;			/* total number of sequence bases */


  d_strcpy ( name, &((*text).line [ 1 ]) );
  name [ d_stridx ( name, " " ) ] = '\0';
  name [ d_stridx ( name, "|" ) ] = '\0';
  name [ d_stridx ( name, "\n" ) ] = '\0';
  printf ( "%s\t", name );

  /* Initialization. */
  for ( index = 0; index < MAX_ASCII; index++ )

    composition [ index ] = 0;

  /* Get the first line of the sequence. */
  get_line ( text );	/* get the next text line */

  do
  {
    /* Total the line composition. */
    for ( index = 0; ( (*text).line [ index ] != '\n' ) &&
                     ( (*text).line [ index ] != '\0' ) &&
                     ( (*text).line [ index ] != (char) EOF ); index ++ )
    {
      c = (*text).line [ index ];
      composition [ c ]++;

      if ( ( ( c >= 'a' ) && ( c <= 'z' ) ) || 
           ( ( c >= 'A' ) && ( c <= 'Z' ) ) )

        total++;
    }  /* for */

    get_line ( text );	/* get the next text line */
  }
  while ( ( (*text).eof != TRUE ) &&
          ( (*text).line [ 0 ] != '>' ) );

  printf ( "A\t%d\t", composition [ 'a' ] + composition [ 'A' ] );
  printf ( "B\t%d\t", composition [ 'b' ] + composition [ 'B' ] );
  printf ( "C\t%d\t", composition [ 'c' ] + composition [ 'C' ] );
  printf ( "D\t%d\t", composition [ 'd' ] + composition [ 'D' ] );
  printf ( "E\t%d\t", composition [ 'e' ] + composition [ 'E' ] );
  printf ( "F\t%d\t", composition [ 'f' ] + composition [ 'F' ] );
  printf ( "G\t%d\t", composition [ 'g' ] + composition [ 'G' ] );
  printf ( "H\t%d\t", composition [ 'h' ] + composition [ 'H' ] );
  printf ( "I\t%d\t", composition [ 'i' ] + composition [ 'I' ] );
  printf ( "K\t%d\t", composition [ 'k' ] + composition [ 'K' ] );
  printf ( "L\t%d\t", composition [ 'l' ] + composition [ 'L' ] );
  printf ( "M\t%d\t", composition [ 'm' ] + composition [ 'M' ] );
  printf ( "N\t%d\t", composition [ 'n' ] + composition [ 'N' ] );
  printf ( "P\t%d\t", composition [ 'p' ] + composition [ 'P' ] );
  printf ( "Q\t%d\t", composition [ 'q' ] + composition [ 'Q' ] );
  printf ( "R\t%d\t", composition [ 'r' ] + composition [ 'R' ] );
  printf ( "S\t%d\t", composition [ 's' ] + composition [ 'S' ] );
  printf ( "T\t%d\t", composition [ 't' ] + composition [ 'T' ] );
  printf ( "U\t%d\t", composition [ 'u' ] + composition [ 'U' ] );
  printf ( "V\t%d\t", composition [ 'v' ] + composition [ 'V' ] );
  printf ( "W\t%d\t", composition [ 'w' ] + composition [ 'W' ] );
  printf ( "X\t%d\t", composition [ 'x' ] + composition [ 'X' ] );
  printf ( "Y\t%d\t", composition [ 'y' ] + composition [ 'Y' ] );
  printf ( "Z\t%d\t", composition [ 'z' ] + composition [ 'Z' ] );
  printf ( "-\t%d\t", composition [ '-' ] );
  printf ( ".\t%d\t", composition [ '.' ] );
  printf ( "\n" );
}  /* composition */


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

  if ( (*text).eof == TRUE )  (*text).next = (char) EOF;
}  /* get_char */


/******************************************************************************/
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
  if ( (*text).name [ 0 ] == '\0' )  return;

  /* Open the text file in read mode. */
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) == NULL )

    printf ( "Could not open '%s'\n", (*text).name );
  else
    (*text).eof = FALSE;

  /* Get the first text line. */
  get_line ( text );

}  /* open_text_file */


/******************************************************************************/
/* This function prompts for a file name. */
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
/* This function adds the base to the DNA sequence. */
add_base ( base, seq )
char    base;		/* next base to be added */
t_seq	*seq;		/* DNA sequence */
{

  if ( (*seq).total >= MAX_BASES - 1 )
  {
    printf ( "*WARNING* Maximum number of sequence bases reached in sequence %s.\n",
        (*seq).name );

    return;
  }  /* if */

  if ( ( ( base >= 'a' ) && ( base <= 'z' ) ) ||
       ( ( base >= 'A' ) && ( base <= 'Z' ) ) ||
       ( base == '.' ) || ( base == '-' ) )
  {
    if ( ( base == 'N' ) || ( base == 'n' )  || ( base == 'X' ) || ( base == 'x' ) )
      (*seq).N_bases++;
    else
      if ( iub_mask [ base ] > 0 )  (*seq).IUB_bases++;
      else
        if ( ( base != '.' ) && ( base != '-' ) && ( dna_mask [ base ] == 0 ) )
          printf ( "*WARNING* unknown base '%c' in %s\n", base, (*seq).name );

    if ( dna_mask [ base ] > 0 )

      (*seq).base [ (*seq).total++ ] = base;
  }  /* if */
}  /* add_base */


/******************************************************************************/
/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* DNA sequence */
{
  /* Check for the FASTA header line. */
  if ( (*text).line [ 0 ] == '>' )
  {
    /* Strip off the name of the sequence. */
    d_strcpy ( (*seq).name, &((*text).line [ 1 ]) );
    (*seq).name [ d_stridx ( (*seq).name, " "  ) ] = '\0';
    (*seq).name [ d_stridx ( (*seq).name, "\t" ) ] = '\0';
    (*seq).name [ d_stridx ( (*seq).name, "\n" ) ] = '\0';

    get_line ( text );	/* get the next text line */
  }  /* if */

  /* Read in the DNA sequence. */
  (*seq).total = 0;
  (*seq).N_bases = 0;
  (*seq).IUB_bases = 0;

  while ( ( (*text).eof != TRUE ) && 
          ( (*text).line [ 0 ] != '>' ) &&
          ( (*seq).total < MAX_BASES ) )
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
    {
      /* Add the base to the DNA sequence. */
      add_base ( (*text).next, seq );
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

