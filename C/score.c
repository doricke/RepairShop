
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

#define  MAX_BASES      100000  /* maximum DNA sequence length */

#define  MAX_LINE	512	/* maximum line length */


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
  t_text  fof;			/* file of filenames */
  t_text  in;			/* input DNA sequence file */
  t_text  repeat;		/* repeat list for FATAL: errors */
  t_text  results;		/* Summary of Blast results */
  FILE    *fopen ();		/* file open function */


  printf ( "This is the Blast e-mail response rename program.\n\n" );

  /* Open the commands file. */
  results.data = fopen ( "results", "w" );

  /* Open the repeat list for FATAL: errors. */
  repeat.data = fopen ( "repeat", "w" );

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

      process_blast ( &in, &results, &repeat );

    /* Get the next file name. */
    get_line ( &fof );
 
    /* Close the BLAST results file. */ 
    fclose ( in.data );
  }  /* while */

  /* Close the commands file. */
  fclose ( fof.data );
  fclose ( results.data );
  fclose ( repeat.data );

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
find_query ( in )
t_text  *in;		/* BLAST output file */
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

  /* Get the name. */
  get_token ( in );	/* consume the = */

  /* Get the query sequence name. */
  get_token ( in );
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


/* This function processes the BLAST output file. */
process_blast ( in, out, repeat )
t_text  *in;		/* BLAST output file */
t_text  *out;		/* Summary output file */
t_text  *repeat;	/* repeat list for FATAL: errors */
{
  int  end = 0, best_end = 0;			/* end of homology */
  int  fatal = 0;				/* count of FATAL reports */
  int  hits = 0;				/* > scores */
  int  ident = 0, best_ident = 0;		/* identical bases */
  int  length = 0, best_length = 0;		/* length of homology */
  int  start = 0, best_start = 0;		/* start of homology */
  int  prev_start = 0, prev_end = 0;		/* previous start & end */

  char desc_1 [ MAX_LINE ];			/* description */
  char desc_2 [ MAX_LINE ];			/* description */
  char desc_3 [ MAX_LINE ];			/* subsequent description */
  char query [ MAX_LINE ];			/* query sequence name */


  desc_1 [ 0 ] = '\0';		/* initialize */
  desc_2 [ 0 ] = '\0';
  desc_3 [ 0 ] = '\0';

  /* Get the query sequence name. */
  find_query ( in );
  strcpy ( query, (*in).token );
  query [ stridx ( query, "\n" ) ] = '\0';

  printf ( "-----------------------------" );
  printf ( "--------------------------------------------------------------\n" );
  printf ( "%s\n", (*in).line );

  /* Process the output file. */
  while ( (*in).eof != TRUE )
  {
    get_line  ( in );
    get_token ( in );

    /* Check for FATAL error message. */
    if ( strcmp ( (*in).token, "FATAL" ) == 0 )
    {
      fprintf ( (*out).data, "%s\t0\t0\t0\t0\t%s\n", query, (*in).line );

      if ( ( (*repeat).data != NULL ) && ( fatal == 0 ) )

        fprintf ( (*repeat).data, "%s\n", query );

      fatal++;
    }  /* if */

    /* Check for multiple database searches. */
    if ( ( strcmp ( (*in).token, "Sequences" ) == 0 ) && ( best_start > 0 ) )
    {
      if ( best_ident >= 30 )

        fprintf ( (*out).data, "%s\t%d\t%d\t%d\t%d\t%s%s\n", query, best_start, 
            best_end, best_ident, best_length, desc_1, desc_2 );

      hits = best_start = best_end = best_ident = 0;
      desc_1 [ 0 ] = desc_2 [ 0 ] = '\0';

      printf ( "\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" );
      printf ( "+++++++++++++++++++++\n" );
    }  /* if */

    /* Check for a description line */
    if ( (*in).line [ 0 ] == '>' ) 
    {
      if ( hits == 0 )
      {
        strcpy ( desc_1, &((*in).line [ 1 ]) );
        desc_1 [ stridx ( desc_1, "\n" ) ] = '\0';

        /* Check for a continued description line. */
        get_line ( in );
        get_token ( in );

        if ( strcmp ( (*in).token, "Length" ) == 0 )
          desc_2 [ 0 ] = '\0';
        else
        {
          strcpy ( desc_2, &((*in).line [ 0 ]) );

          desc_2 [ stridx ( desc_2, "\n" ) ] = '\0';
        }  /* else */

        printf ( "\n%s\n%s\n", desc_1, desc_2 );
      }
      else
        strcpy ( desc_3, &((*in).line [ 1 ]) );

      hits++;
    }  /* if */

    /* Check for next alignments. */
    if ( ( strcmp ( (*in).token, "Identities" ) == 0 ) || 
         ( (*in).line [ 0 ] == '>' ) )     
    {
      if ( best_start == 0 )
      {
        best_start = start; 
        best_end   = end;   
      }  /* if */
      else
      {
        /* Test if adjacent alignments. */
        if ( ( start - 8 <= best_end ) && ( start + 8 >= best_end ) )
        {
          best_end     = end;    
          best_ident  += ident;   ident  = 0;
          best_length += length;  length = 0;
        }  /* if */
        else
          if ( ( end - 8 <= best_start ) && ( end + 8 >= best_start ) )
          {
            best_start   = start;  
            best_ident  += ident;   ident  = 0;
            best_length += length;  length = 0;
          }  /* if */
      }  /* else */
    }  /* if */

    /* Check for identities line. */
    if ( ( hits == 1 ) && ( strcmp ( (*in).token, "Identities" ) == 0 ) )
    {
      get_token ( in );		/* consume '=' */

      ident = get_int ( in );

      get_token ( in );		/* consume '/' */

      length = get_int ( in );

      start = end = 0;		/* new alignment */

      if ( best_ident == 0 )
      {
        best_ident  = ident;  ident  = 0;
        best_length = length; length = 0;
      }  /* if */
    }  /* if */

    /* Check for Query line. */
    if ( ( hits == 1 ) && ( strcmp ( (*in).token, "Query" ) == 0 ) &&
         ( (*in).line [ 5 ] == ':' ) )
    {
      prev_start = start;  prev_end = end;

      get_token ( in );		/* consume ':' */

      start = get_int ( in );

      get_token ( in );		/* consume DNA sequence */

      end = get_int ( in );

      /* Print out the DNA sequence line. */
      printf ( "%s", (*in).line );

      /* Print out the identity line. */
      get_line ( in );
      printf ( "%s\n", (*in).line );

      /* Check for continuation of previous alignment. */
      if ( ( start + 1 == prev_end ) || ( start - 1 == prev_end ) )

        start = prev_start;
    }  /* if */
  }  /* while */

  /* Print best results. */
  if ( ( best_start > 0 ) && ( best_ident >= 30 ) )
  
    fprintf ( (*out).data, "%s\t%d\t%d\t%d\t%d\t%s%s\n", query, best_start, 
        best_end, best_ident, best_length, desc_1, desc_2 );
}  /* process_blast */


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

