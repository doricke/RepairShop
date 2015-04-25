
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

#define  MAX_BASES      500000  /* maximum DNA sequence length */

#define  MAX_GRAIL_LEN  100000  /* maximum length of DNA sequence for GRAIL */

#define  MAX_LINE	1012	/* maximum line length */

#define  MIN_DNA_SEQ_LENGTH  50	/* minimum DNA sequence length to process */

#define  MIN_GRAIL_OVERLAP  500 /* minimum overlap between DNA segments */

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
  char    base_name [ MAX_LINE ];	/* Request base name */
  t_text  cmds;				/* Unix shell commands */
  t_text  fof;				/* file of file names */
  t_text  in;				/* input DNA sequence file */
  long    num_seqs = 0;			/* total number of sequences */
  t_seq   seq;         			/* DNA sequence */
  t_text  grail1;			/* GRAIL 1 email file */
  t_text  grail1a;			/* GRAIL 1a email file */
  t_text  grail2;			/* GRAIL 2 email file */
  long    start;			/* start of DNA sequence segment */
  long    end;				/* end of DNA sequence segment */
  FILE    *fopen ();			/* file open function */


  printf ( "This is the Sequence files --> GenQuest GRAIL program.\n\n" );

  /* Open the commands file. */
  cmds.data = fopen ( "cmds", "w" );

  /* Prompt for the input file name. */
  prompt_file ( &fof, "What is the list of sequences file name?" );

  strcpy ( grail1.name, fof.name );
  grail1.name [ stridx ( grail1.name, "\n" ) ] = '\0';
  grail1.name [ stridx ( grail1.name, "." ) ] = '\0';
  strcpy ( base_name, grail1.name );
  strcpy ( grail1a.name, grail1.name );
  strcpy ( grail2.name, grail1.name );
  strcat ( grail1.name,  ".grail1.email" );
  strcat ( grail1a.name, ".grail1a.email" );
  strcat ( grail2.name,  ".grail2.email" );

  if ( cmds.data != NULL )
  {
    fprintf ( cmds.data, "mail -s %s.GRAIL1 GRAIL@ORNL.GOV < %s\n",
        base_name, grail1.name );
    fprintf ( cmds.data, "mail -s %s.GRAIL1a GRAIL@ORNL.GOV < %s\n", 
        base_name, grail1a.name );
    fprintf ( cmds.data, "mail -s %s.GRAIL2 GRAIL@ORNL.GOV < %s\n", 
        base_name, grail2.name );
    printf ( "\nFile cmds written with e-mail commands.\n" );
  }  /* if */
  fclose ( cmds.data );


  /* Count the number of DNA sequences. */
  while ( fof.eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, fof.line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );

    /* Count the number of reasonable length DNA sequences. */
    if ( seq.total > MIN_DNA_SEQ_LENGTH )  

      num_seqs += ( ( seq.total + MAX_GRAIL_LEN - 1 ) + 
          ( ( ( seq.total + MAX_GRAIL_LEN - 1 ) / MAX_GRAIL_LEN ) * MIN_GRAIL_OVERLAP ) ) 
          / MAX_GRAIL_LEN;

    /* Close the DNA sequence file. */
    fclose ( in.data );

    /* Get the next sequence name. */
    get_line ( &fof );
  }  /* while */

  printf ( "\nThe number of DNA sequences is %d.\n", num_seqs );

  /* Re-open the list of sequences file name. */
  fclose ( fof.data );
  open_text_file ( &fof );

  /* Open the GRAIL e-mail message output file. */
  grail1.data  = fopen ( grail1.name, "w" );
  grail1a.data = fopen ( grail1a.name, "w" );
  grail2.data  = fopen ( grail2.name, "w" );

  if ( grail1.data != NULL )
  {
    fprintf ( grail1.data, "Sequences %d -1\n", num_seqs );
    printf ( "\nWriting GRAIL e-mail message to file '%s'.\n", grail1.name );
  }
  else
    printf ( "Could not open GRAIL e-mail message output file '%s'.\n",
        grail1.name );

  if ( grail1a.data != NULL )
  {
    fprintf ( grail1a.data, "Sequences %d -1a\n", num_seqs );
    printf ( "\nWriting GRAIL e-mail message to file '%s'.\n", grail1a.name );
  }
  else
    printf ( "Could not open GRAIL e-mail message output file '%s'.\n",
        grail1a.name );

  if ( grail2.data != NULL )
  {
    fprintf ( grail2.data, "Sequences %d -2\n", num_seqs );
    printf ( "\nWriting GRAIL e-mail message to file '%s'.\n", grail2.name );
  }
  else
    printf ( "Could not open GRAIL e-mail message output file '%s'.\n",
        grail2.name );

  /* Write the sequences to the grail e-mail message file. */
  while ( fof.eof != TRUE )
  {
    /* Open the DNA sequence file for reading. */
    strcpy ( in.name, fof.line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );
    fclose ( in.data );

    fof.line [ stridx ( fof.line, "\n" ) ] = '\0';

    if ( ( fof.eof != TRUE ) && ( grail1.data != NULL ) )
    {
      /* Segment the DNA sequence into maximum sized fragments. */
      for ( start = 0; start < seq.total; start += MAX_GRAIL_LEN - MIN_GRAIL_OVERLAP )
      {
        end = start + MAX_GRAIL_LEN - 1;
        if ( end >= seq.total )  end = seq.total - 1;

        fprintf ( grail1.data,  ">%s First=%d Length=%d\n", fof.line, start, seq.total );
        fprintf ( grail1a.data, ">%s First=%d Length=%d\n", fof.line, start, seq.total );
        fprintf ( grail2.data,  ">%s First=%d Length=%d\n", fof.line, start, seq.total );

        /* Write out the DNA sequence. */
        write_DNA_seq ( &seq, start, end, &grail1 );
        write_DNA_seq ( &seq, start, end, &grail1a );
        write_DNA_seq ( &seq, start, end, &grail2 );
      }  /* for */
    }  /* if */

    /* Get the name and sequence. */
    get_line ( &fof );
  }  /* while */

  fclose ( grail1.data );
  fclose ( grail1a.data );
  fclose ( grail2.data );

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


/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* DNA sequence */
{
  int  count;		/* composition count */
  int  composition [ MAX_ASCII ];	/* composition array */
  int  DNA_composition;			/* A,C,G,T on current line */
  int  end_of_file = FALSE;		/* end of file flag */
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
                      composition [ 'T' ] + composition [ 't' ] +
                      composition [ 'N' ] + composition [ 'n' ] +
                      composition [ 'R' ] + composition [ 'r' ] +
                      composition [ 'Y' ] + composition [ 'y' ] +
                      composition [ 'B' ] + composition [ 'b' ];

    if ( DNA_composition * 1.0 <= count * 0.75 )

      get_line ( text );	/* get the next text line */
  }
  while ( ( (*text).eof != TRUE ) &&
          ( DNA_composition * 1.0 <= count * 0.75 ) );

  /* Read in the DNA sequence. */
  (*seq).total = 0;

  do
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) )
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

