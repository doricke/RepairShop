
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

#define  MAX_LINE	132	/* maximum line length */


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


main ( argc, argv )
int  argc;		/* number of command line arguments */
char *argv [];		/* command line arguments */
{
  t_text  retrieve;	/* NCBI retrieve mail file */
  t_text  sequence;	/* retrieved sequence */
  FILE    *fopen ();	/* file open function */


  printf ( "This is the SPLIT program.\n\n" );

  /* Check for command line parameter. */
  if ( argc > 1 )
  {
    d_strcpy ( retrieve.name, argv [ 1 ] );

    open_text_file ( &retrieve );
  }
  else
    /* Prompt for the input file name. */
    prompt_file ( &retrieve, "What is the retrieve file name?" );

  /* Process the retrieved sequences. */
  while ( retrieve.eof != TRUE )
  {
    /* Find the LOCUS line. */
    while ( ( d_strcmp ( retrieve.token, "LOCUS" ) != 0 ) &&
            ( retrieve.eof != TRUE ) )
    {
      get_line ( &retrieve );
    }  /* while */

    /* Get the LOCUS name. */
    get_token ( &retrieve );

    /* Open the sequence output file. */
    d_strcpy ( sequence.name, retrieve.token );

    sequence.data = fopen ( sequence.name, "w" );

    if ( retrieve.eof != TRUE )
      printf ( "Writing '%s'\n", sequence.name );

    /* Copy the sequence file. */
    while ( ( sequence.data       != NULL ) &&
            ( retrieve.eof        != TRUE ) &&
            ( retrieve.line [ 0 ] != '/'  ) )
    {
      fprintf ( sequence.data, "%s", retrieve.line );

      get_line ( &retrieve );
    }  /* while */

    fclose ( sequence.data );
  }  /* while */

  fclose ( retrieve.data );

  printf ( "\nEnd of Split program.\n" );
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


/* This function gets the next text character. */
get_char ( text )
t_text  *text;		/* ASCII text file */
{
  /* Get the next sequence character. */
  (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( (*text).next == ' '  ) ||
          ( (*text).next == '\n' ) ||
          ( (*text).next == '\t' ) )

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

  /* Get the first text token. */
  get_token ( text );

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
  while ( ( (*text).eof == TRUE ) && ( d_strcmp ( (*text).name, "exit" ) != 0 ) )
  {
    printf ( "%s or 'exit' ", prompt );

    scanf ( "%s", (*text).name );

    if ( d_strcmp ( (*text).name, "exit" ) != 0 )

      open_text_file ( text );
  }  /* while */

  printf ( "\n" );
}  /* prompt_file */


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


/* Return <0 if s<t, 0 if s==t, >0 if s>t */
d_strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* d_strcmp */


/* Copy t to s */
d_strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* d_strcpy */


/* Return length of string s */
d_strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* d_strlen */


