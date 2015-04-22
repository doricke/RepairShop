
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

#define  MAX_LINE	1012	/* maximum line length */

#define  MIN_DNA_SEQ_LENGTH  50	/* minimum DNA sequence length to process */

#define  MIN_SEQ	12	/* minimum sequence length */


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
  t_text  cmds;				/* Unix shell commands */
  t_text  in;				/* input DNA sequence file */
  char    name [ MAX_LINE ];		/* file name */
  long    num_seqs = 0;			/* total number of sequences */
  t_seq   seq;         			/* DNA sequence */
  t_text  sequence;			/* trapped exon sequence */
  t_text  trapped;			/* trapped exons sequence file */
  FILE    *fopen ();			/* file open function */


  printf ( "This is the Sequence files --> GenQuest GRAIL program.\n\n" );

  /* Open the commands file. */
  cmds.data = fopen ( "cmds", "w" );

  /* Prompt for the input file name. */
  prompt_file ( &trapped, "What is the list of sequences file name?" );

  strcpy ( name, trapped.name );
  name [ stridx ( name, "\n" ) ] = '\0';
  name [ stridx ( name, "." ) ] = '\0';
  strcat ( sequence.name, ".e-mail" );

  if ( cmds.data != NULL )
  {
    fprintf ( cmds.data, "mail GRAIL@ORNL.GOV < %s\n", name );
    printf ( "File cmds written with e-mail command.\n" );
  }  /* if */
  fclose ( cmds.data );


  /* Count the number of DNA sequences. */
  while ( trapped.eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, trapped.line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the sequence. */
    read_seq ( &in, &seq );

    /* Count the number of reasonable length DNA sequences. */
    if ( seq.total > MIN_DNA_SEQ_LENGTH )  num_seqs++;

    /* Close the DNA sequence file. */
    fclose ( in.data );

    /* Get the next sequence name. */
    get_line ( &trapped );
  }  /* while */

  printf ( "\nThe number of DNA sequences is %d.\n", num_seqs );

  /* Re-open the list of sequences file name. */
  fclose ( trapped.data );
  open_text_file ( &trapped );

  /* Open the GRAIL e-mail message output file. */
  strcpy ( sequence.name, trapped.line );
  sequence.name [ stridx ( sequence.name, "\n" ) ] = '\0';
  sequence.name [ stridx ( sequence.name, "." ) ] = '\0';
  strcat ( sequence.name, ".grail" );

  sequence.data = fopen ( sequence.name, "w" );

  if ( sequence.data != NULL )
  {
    fprintf ( sequence.data, "Sequences %d -2\n", num_seqs );
    printf ( "\nWriting GRAIL e-mail message to file '%s'.\n", sequence.name );
  }
  else
  {
    printf ( "Could not open GRAIL e-mail message output file '%s'.\n",
        sequence.name );
    /* exit; */
  }  /* else */

  /* Write the sequences to the grail e-mail message file. */
  while ( trapped.eof != TRUE )
  {
    /* Open the DNA sequence file for reading. */
    strcpy ( in.name, trapped.line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';
    open_text_file ( &in );

    /* Read in the sequence. */
    read_seq ( &in, &seq );
    fclose ( in.data );

    trapped.line [ stridx ( trapped.line, "\n" ) ] = '\0';

    if ( ( trapped.eof != TRUE ) && ( sequence.data != NULL ) )
    {
      fprintf ( sequence.data, ">%s\n", trapped.line );

      /* Write out the DNA sequence. */
      write_DNA_seq ( &seq, 1, seq.total, &sequence );
    }  /* if */

    /* Get the trapped exon name and sequence. */
    get_line ( &trapped );
  }  /* while */

  printf ( "\nEnd of DNA Sequences->GENQUEST GRAIL program.\n" );
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
  if ( (*text).next == EOF )  return; 
  (*text).next = ' ';

  /* Get the next sequence character. */
  if ( ( (*text).line [ (*text).line_index ] != '\n' ) &&
       ( (*text).line [ (*text).line_index ] != '\0' ) )

    (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\0' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).next != EOF ) )

    if ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) )
    {
      if ( (*text).eof == TRUE )  (*text).next = EOF;
      else  get_line ( text );
    }
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


  if ( (*text).next == EOF )  return;

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


/* This function reads in a sequence. */
read_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* sequence */
{
  int  end_of_file = FALSE;		/* end of file flag */
  int  index;

  /* Find the begining of the sequence. */
  while ( (*text).line [ stridx ( (*text).line, ".." ) ] == '\0' )

    get_line ( text );

  /* Ignore the line with the ".." in it. */
  get_line ( text );

  /* Read in the sequence. */
  (*seq).total = 0;

  do
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) )
    {
      (*seq).base [ (*seq).total++ ] = (*text).next;

      if ( (*seq).total == MAX_BASES - 1 )

        printf ( "Maximum number of sequence bases reached.\n" );
    }  /* if */

    get_char ( text );		/* get the next character */

    if ( ( (*text).eof == TRUE ) && 
         ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) ||
           ( (*text).next == EOF ) ) )
      end_of_file = TRUE;
  }  
  while ( ( end_of_file != TRUE ) && ( (*seq).total < MAX_BASES ) );
}  /* read_seq */


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

