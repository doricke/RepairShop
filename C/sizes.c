
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

#define  MAX_CMDS	8	/* maximum Unix commands files */

#define  MAX_LINE	1012	/* maximum line length */

#define  MIN_SEQ	12	/* minimum sequence length */


#define MAX_HISTOGRAM	202	/* maximum elements in histogram array */


#define H_HISTOGRAM     1       /* starndard histogram element size */
#define H_SEQUENCE      100      /* sequence subcomponent element size */


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


/* Histogram of lengths. */
typedef struct {
  long	lengths [ MAX_HISTOGRAM ];	/* histogram of lengths */
} t_histogram;


main ()
{
  long    count = 1;		/* number of command file lines written */
  long    end;			/* end of DNA sequence segment */
  t_text  erase;		/* Unix erase shell commands */
  t_text  in;			/* input DNA sequence file */
  int     index;		/* commands files index */
  t_text  names;		/* file of sequence names file */
  t_seq   seq;         		/* DNA sequence */
  t_text  email;		/* e-mail output file */
  long    start;		/* start of DNA sequence segment */
  long    total_size = 0;	/* Sum of sequence lengths */
  int     version;     		/* DNA segment number */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();		/* file open function */

  t_histogram  histogram;	/* DNA sequence sizes histogram */
  t_text  size;			/* Size of each DNA sequence file */
  t_text  histo;		/* Histogram of DNA sequence sizes file */



  printf ( "This is the Sequence files Sizes program.\n\n" );

  /* Prompt for the input file name. */
  prompt_file ( &names, "What is the list of sequences file name?" );

  strcpy ( size.name, "size" );
  size.data = fopen ( size.name, "w" );

  strcpy ( histo.name, "size.histo" );
  histo.data = fopen ( histo.name, "w" );

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

    fprintf ( size.data, "%d\t%d\t%s\n", count, seq.total, in.name );
    total_size += seq.total;

    /* Add the sequence length to the histogram. */
    add_length ( 1, seq.total, &histogram, H_SEQUENCE );

    /* Get the next name. */
    get_line ( &names );

    count++;
  }  /* while */

  fprintf ( size.data, "%d\t%d\tTotal sequence lengths\n", count, total_size );
  fclose ( size.data );

  /* Print out the sequence sizes histogram. */
  print_histogram ( "DNA Sequence Sizes Histogram", &histogram, H_SEQUENCE, &histo );

  fclose ( histo.data );

  printf ( "\nEnd of DNA Sequence files Sizes program.\n" );
}  /* main */


/* This function writes out a e-mail message in a file. */
write_email ( database, method, seq, start, end, version, email, cmds, erase )
char	database [];	/* database name to search */
char	method [];	/* search method command string */
t_seq   *seq;		/* DNA sequence */
long    start;		/* start of DNA sequence segment */
long    end;		/* end of DNA sequence segment */
int	version;	/* current version number */
t_text  *email;		/* e-mail message file */
t_text	*cmds;		/* Unix e-mail message send commands file */
t_text  *erase;		/* Unix remove e-mail message file commands file */
{
  char    email_name [ MAX_LINE ];	/* e-mail message output file name */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Set up the e-mail message sequence file name. */
  strcpy ( email_name, (*email).name );
  email_name [ stridx ( email_name, "-" ) ] = '\0'; 
  strcat ( email_name, "-" );

  /* Identify the search method and database. */
  version_str [ 0 ] = method [ 7 ];
  version_str [ 1 ] = database [ 0 ];
  version_str [ 2 ] = '\0';
  strcat ( email_name, version_str );

  itoa   ( version, version_str );
  if ( version < 100 )  strcat ( email_name, "0" );
  if ( version < 10 )  strcat ( email_name, "0" );
  strcat ( email_name, version_str );
  strlower ( email_name );

  /* Open the e-mail output file. */
  (*email).data = fopen ( email_name, "w" );
  if ( (*email).data == NULL )
  {
    printf ( "Could not open the file '%s' for writing.\n", email_name );
    return;
  }  /* if */

  /* Set up Unix e-mail send command. */
  if ( (*cmds).data != NULL )

    fprintf ( (*cmds).data, "mail Q@ORNL.GOV < %s\n", 
        email_name );

  /* Set up Unix erase file command. */
  if ( (*erase).data != NULL )

    fprintf ( (*erase).data, "rm %s\n", email_name );

  /* Identify the DNA type for target database. */
  if ( strcmp ( database, "SwissProt" ) == 0 )
    fprintf ( (*email).data, "TYPE DNA6\n" );
  else
    fprintf ( (*email).data, "TYPE DNA\n" ); 

  /* Identify the target database. */
  fprintf ( (*email).data, "TARGET %s\n", database );

  /* Identify the search method. */
  fprintf ( (*email).data, "%s\n", method );

  /* Pass useful information on the comment line. */
  fprintf ( (*email).data, "COMMENT %s ", email_name );
  fprintf ( (*email).data, " /First= %d, ", start + 1 );
  fprintf ( (*email).data, " /Length= %d, ", (*seq).total );
  fprintf ( (*email).data, " /Size= %d\n", end - start + 1 );

  /* Identify the following lines as DNA sequence. */
  fprintf ( (*email).data, "SEQ\n" );

  /* Write out the DNA sequence. */
  write_DNA_seq ( seq, start, end, email );

  /* Identify the end of the sequence/message. */
  fprintf ( (*email).data, "END\n" );

  /* Close the e-mail message output file. */
  fclose ( (*email).data );
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
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
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


/******************************************************************************/
/* This function adds the computed length to the histogram. */
add_length ( begin, end, histogram, size )
long         begin;       /* beginning coordinates */
long         end;         /* ending coordinates */
t_histogram  *histogram;  /* histogram to add length to */
long         size;        /* histogram element size */
{
  long  index;   /* histogram index */
  long  length;  /* the length to add to the histogram */

  length = end - begin + 1;
  index = length / size + 1;

  if ( length == 0 )  index = 0;

  /* Check for length past the end of the histogram. */
  if ( index < MAX_HISTOGRAM - 1 )
    (*histogram).lengths [ index ]++;
  else
    (*histogram).lengths [ MAX_HISTOGRAM - 1 ]++;
}  /* add_length */


/******************************************************************************/
/* This function prints out a histogram table. */
print_histogram ( name, histogram, size, histo )
char         	name [];     /* histogram name */
t_histogram  	*histogram;  /* the histogram to print */
long         	size;        /* histogram element size */
t_text		*histo;		/* histogram output file */
{
  int  index;  /* histogram index */


  fprintf ( (*histo).data, "Histogram for %s:\n", name );

  for ( index = 0; index < MAX_HISTOGRAM - 1; index++ )

    fprintf ( (*histo).data, "%3d\t%6d\n", index * size, 
        (*histogram).lengths [ index ] );

  if ( (*histogram).lengths [ MAX_HISTOGRAM - 1 ] > 0 )

    fprintf ( (*histo).data, "%3d\t%6d\n", (MAX_HISTOGRAM - 1) * size, 
        (*histogram).lengths [ index ] );
}  /* print_histogram */


