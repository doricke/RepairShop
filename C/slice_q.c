
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

#define  MIN_SEQ	25	/* minimum sequence length */


/* DNA sequence ignore sequence range. */
typedef  struct {
  char  name [ MAX_LINE ];	/* DNA sequence name */
  long  start;			/* start of DNA sequence range to ignore */
  long  end;			/* end   of DNA sequence range to ignore */
} t_ignore;


/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  name [ MAX_LINE ];	/* sequence name */
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
  t_text  cmds;			/* Unix shell commands output file */
  long    end = 0;		/* end of DNA sequence segment */
  t_text  hits;			/* Previous search hits summary file */
  t_ignore  ignore;		/* DNA segment range to ignore */
  t_text  in;			/* input DNA sequence file */
  char    msg_name [ MAX_LINE ];/* e-mail message output file name */
  t_text  seq_names;		/* input DNA sequence file names */
  t_seq   seq;			/* DNA sequence */
  long    start = 0;		/* start of DNA sequence segment */
  int     version = 1;		/* DNA segment number */
  FILE    *fopen ();		/* file open function */


  printf ( "This is the DNA Sequences Homology Searches program.\n\n" );

  seq.total = 0;	/* initialize */
  in.name [ 0 ] = '\0';

  /* Open the commands file. */
  cmds.data = fopen ( "cmds", "w" );

  /* Prompt for the input file name. */
  prompt_file ( &seq_names, "What is the list of sequences file name?" );

  /* Prompt for the name of the previous hits summary file. */
  prompt_file ( &hits, "What is the previous homology hits file name?" );

  /* Get the first homology result record. */  
  get_hit ( &hits, &ignore );

  /* Process the sequences. */
  while ( ( seq_names.eof != TRUE ) || ( start < end ) )
  {
    /* Get the next DNA segment to process. */
    next_segment ( &seq_names, &seq, &start, &end, &hits, &ignore,
        &version, msg_name, &in );

    /* Check for end of input sequences. */
    if ( ( seq_names.eof != TRUE ) || ( start + MIN_SEQ <= end ) )

      /* Write out a Blast e-mail message. */
      write_Blast ( &seq, start, end, msg_name, &cmds );
  }  /* while */

  /* Close the input and output files. */
  fclose ( seq_names.data );
  fclose ( hits.data );
  fclose ( cmds.data );

  printf ( "\nEnd of DNA Sequences Homology Searches program.\n" );
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
            ( (*text).next == '.' ) ) 
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
}  /* prompt_file */


/* This function gets the next DNA sequence homology hit to ignore. */
get_hit ( hits, ignore )
t_text	*hits;		/* previous search hits summary file */
t_ignore	*ignore;	/* DNA sequence range to ignore */
{
  /* Get the ignore sequence name. */
  get_token ( hits, (*ignore).name );

  strcpy ( (*ignore).name, (*hits).token );

  /* Get the coordinates of the DNA sequence range to ignore. */
  (*ignore).start = get_int ( hits );
  (*ignore).end   = get_int ( hits );

  /* Get the next DNA segment range to ignore. */
  get_line ( hits );
}  /* get_hit */


/* This function gets the next DNA sequence. */
get_next_sequence ( seq_names, in, seq )
t_text	*seq_names;	/* file of input sequence names */
t_text	*in;		/* input DNA sequence file */
t_seq	*seq;		/* DNA sequence */
{
  (*seq).total = 0;

  /* Check for no more sequences to process. */
  if ( (*seq_names).eof == TRUE )  return;

  (*seq_names).line [ stridx ( (*seq_names).line, "\n" ) ] = '\0';

  /* Open the DNA sequence input file. */
  strcpy ( (*in).name, (*seq_names).line );
  strcpy ( (*seq).name, (*seq_names).line );

  /* Open the DNA sequence file for reading. */
  open_text_file ( in );

  /* Read in the DNA sequence. */
  read_DNA_seq ( in, seq );
  fclose ( (*in).data );

  /* Get the next DNA sequence file name. */
  get_line ( seq_names );
}  /* get_next_sequence */


/* This function gets the next DNA segment. */
next_segment ( seq_names, seq, start, end, hits, ignore, version, msg_name, in )
t_text	*seq_names;	/* file of input sequence names */
t_seq	*seq;		/* DNA sequence */
long	*start;		/* Start of current DNA segment */
long	*end;		/* End of current DNA segment */
t_text	*hits;		/* previous search hits summary file */
t_ignore *ignore;	/* DNA sequence range to ignore */
long	*version;	/* e-mail message version number */
char	msg_name [];	/* e-mail output file message file name */
t_text  *in;		/* input DNA sequence file */
{
  char    version_str [ MAX_LINE ];	/* ascii version number */


  /* Determine if another DNA segment within current sequence. */

  /* While the next DNA segment is too small. */
  *start = *end;
  while ( ( *end - *start + 1 ) < MIN_SEQ )
  {
    if ( ( *end == 0 ) && ( (*seq_names).eof == TRUE ) )  return;

    /* Check if new DNA segment is needed. */
    if ( ( *end == (*seq).total - 1 ) || ( (*seq).total == 0 ) )
    {
      /* Get the next DNA sequence to process. */
      get_next_sequence ( seq_names, in, seq );

      *version = *start = 0;
      *end = 999;
    }  /* if */

    /* Get the next ignore record. */
    (*version)++;

    while ( strcmp ( (*in).name, (*ignore).name ) != 0 )

      /* Get the next homology hit to ignore. */
      get_hit ( hits, ignore );

    /* Set up the new start and end coordinates. */
    if ( (*ignore).end > 0 )
      if ( *start + MIN_SEQ <= (*ignore).start )
      {
        if ( ( *end + 1 ) >= (*ignore).start )  *end = (*ignore).start - 2;
      }
      else
      {
        *start = (*ignore).end;
        *end   = *start + 999;
      }  /* else */

    if ( ( *end - *start + 1 ) < MIN_SEQ )  *start = *end;

    /* Check if past end of sequence length. */
    if ( *end >= (*seq).total )  *end = (*seq).total - 1;
    if ( *end < 0 )  *end = 0;
  }  /* while */

  /* Segment the DNA sequence in blocks of 1000 bp. */

  /* Truncate source name at first period. */
  strcpy ( msg_name, (*in).name );
  msg_name [ stridx ( msg_name, "." ) ] = '\0';
  strcat ( msg_name, "." );
  itoa   ( *version, version_str );
  strcat ( msg_name, version_str );
}  /* next_segment */


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
  (*seq).total = 0;

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


/* This function writes out a Blast e-mail search message. */
write_Blast ( seq, start, end, msg_name, cmds )
t_seq	*seq;		/* DNA sequence */
long	start;		/* start of DNA segment */
long	end;		/* end of DNA segment */
char	msg_name [];	/* e-mail message output file name */
t_text	*cmds;		/* Unix shell commands e-mail send commands file */
{
  t_text	e_mail;		/* electronic mail output file */

  if ( (*cmds).data != NULL )

    fprintf ( (*cmds).data, "mail search@galahad.EPM.ORNL.GOV < %s\n", 
              msg_name );

  /* Open the Blast e-mail output file. */
  e_mail.data = fopen ( msg_name, "w" );

  /* Check for file open error. */
  if ( e_mail.data == NULL )
  {
    printf ( "*Error*: could not open '%s' for writing.\n", msg_name );
    return;
  }  /* if */

/*  fprintf ( e_mail.data, "TYPE DNA\n" ); */
  fprintf ( e_mail.data, "TYPE DNA6\n" );
/*  fprintf ( e_mail.data, "TARGET REPETITIVE\n" ); */
  fprintf ( e_mail.data, "TARGET SwissProt\n" );
/*  fprintf ( e_mail.data, "TARGET GSDB\n" ); */
/*        fprintf ( e_mail.data, "TARGET DBEST\n" );  */
  fprintf ( e_mail.data, "METHOD BLAST V=25 B=25\n" );
/*  fprintf ( e_mail.data, "METHOD FASTA -b 25 -d 25\n" ); */
  fprintf ( e_mail.data, "COMMENT %s <- ", msg_name );
  fprintf ( e_mail.data, "%s", (*seq).name );
  fprintf ( e_mail.data, "  /First %d, ", start + 1 );
  fprintf ( e_mail.data, " /Length= %d, ", (*seq).total );
  fprintf ( e_mail.data, " /Size= %d\n", end - start + 1 );
  fprintf ( e_mail.data, "SEQ\n" );

  /* Write out the DNA sequence. */
  write_DNA_seq ( seq, start, end, &e_mail );

  fprintf ( e_mail.data, "END\n" );
  fclose ( e_mail.data );
}  /* write_Blast */


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

