
#include <stdio.h>
#include <signal.h>

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

#define  MAX_BASES      100000  /* maximum DNA sequence length */

#define  MAX_GROUPS	1	/* maximum number of evolutionary groups */

#define  MAX_LINE	512	/* maximum line length */


/******************************************************************************/

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
  long  line_count;		/* number of lines written */
  FILE  *data;			/* data file */
  short eof;			/* end of file flag */
} t_text;


/******************************************************************************/
main ( argc, argv )
int  argc;                      /* number of command line arguments */
char *argv [];                  /* command line arguments */
{
  t_text  acs;			/* Accession numbers and taxonomy */
  t_text  gb;			/* GenBank .seq file */
  FILE    *fopen ();		/* file open function */
  int     index;

  char    line [ MAX_LINE ];		/* current .seq file line */

  t_text  groups [ MAX_GROUPS ];	/* Evolutionary groups */

  long  copy_entry ();			/* long function - number of lines written */
  long  lines;				/* current number of lines written */


  printf ( "This is the GenBank groups split program.\n\n" );

  if ( argc > 1 )
  {
    d_strcpy ( acs.name, argv [ 1 ] );
    open_text_file ( &acs );
  }  /* if */
  else
    prompt_file ( &acs, "What is the name of the GenBank accession & taxonomy file?" );

  if ( argc > 2 )
  {
    d_strcpy ( gb.name, argv [ 2 ] );
    open_text_file ( &gb );
  }
  else
    /* Prompt for the input file name. */
    prompt_file ( &gb, "What is the name of the GenBank .seq file?" );

  /* Select the GenBank file name prefix. */
  strcpy ( line, gb.name );
  line [ d_stridx ( line, "." ) ] = '\0';

  /* Set up the evolutionary group files. */
  group_files ( line, groups );

  /* Copy the .dat file header. */
  copy_header ( &gb, groups );

  /* Process the names. */
  while ( acs.eof != TRUE )
  {
    d_strcpy ( line, acs.line );
    line [ d_stridx ( line, "\n" ) ] = '\0';

    /* Classify the sequences by species. */
    capitalize ( line );

    index = classify_species ( line );

    /* Find the next LOCUS line. */
    find_locus ( &gb );

    /* Copy the associated sequence. */
    if ( ( index > -1 ) && ( index < MAX_GROUPS ) )
    {
      lines = copy_entry ( line, &(groups [ index ]), &gb );

      groups [ index ].line_count += lines;
    }  /* if */
    else
    {
      skip_entry ( &gb );
    }  /* else */

    /* Get the next line */
    get_line ( &acs );
  }  /* while */

  /* Close the files. */
  fclose ( acs.data );
  fclose ( gb.data );

  /* Close each of the evolutionary group files. */
  for ( index = 0; index < MAX_GROUPS; index++ )
  {
    fclose ( groups [ index ].data );

    /* Check for only the header copied. */
    if ( groups [ index ].line_count == 9 )
    {
      printf ( "Empty group file '%s'\n", groups [ index ].name );

      /* Remove the evolutionary group file with no entries. */
      d_strcpy ( line, "rm " );
      d_strcat ( line, groups [ index ].name );
      d_system ( line );
    }  /* if */
  }  /* for */
}  /* main */


/******************************************************************************/
/* This function sets up the group files. */
group_files ( prefix, groups )
char	prefix [];		/* GenBank file name prefix identifer. */
t_text  groups [];		/* evolutionary groups */
{
  int  index;		

  /* Set up the GenBank prefix identifer. */
  for ( index = 0; index < MAX_GROUPS; index++ )

    d_strcpy ( groups [ index ].name, prefix );

  d_strcat ( groups [  0 ].name, ".human" );

  /* Open the group files. */
  for ( index = 0; index < MAX_GROUPS; index++ )
  {
    groups [ index ].data = fopen ( groups [ index ].name, "w" );

    /* Initialize the number of lines written to each file. */
    groups [ index ].line_count = 0;
  }  /* for */
}  /* group_files */


/******************************************************************************/
/* This function copies the GenBank .dat file header. */
copy_header ( gb, groups )
t_text  *gb;			/* GenBank .dat file */
t_text  groups [];		/* groups files */
{
  int  group_index;		/* group index */
  int  index;


  /* Check for no header. */
  if ( (*gb).line [ d_stridx ( (*gb).line, "Genetic Sequence Data Bank" ) ] != 'G' )  return;

  /* Copy the nine GenBank .dat file header lines. */
  for ( index = 1; index <= 9; index++ )
  {
    /* Don't copy the LOCUS line.
    if ( (*gb).line [ d_stridx ( (*gb).line, "LOCUS" ) ] != 'L' )

      /* Copy the current line. */
      for ( group_index = 0; group_index < MAX_GROUPS; group_index++ )
      {
        fprintf ( groups [ group_index ].data, "%s", (*gb).line );
        groups [ group_index ].line_count++;
      }  /* for */

    /* Get the next line. */
    get_line ( gb );
  }  /* for */
}  /* copy_header */


/******************************************************************************/
/* This function classifies the group associated with a taxonomy. */
int classify_species ( acs )
char  acs [];			/* accession number & taxonomy */
{
  int  index = -1;		/* file selector */

  if ( ( acs [ d_stridx ( acs, "HOMO" ) ] == 'H' ) &&
       ( acs [ d_stridx ( acs, "PRIMATES" ) ] == 'P' ) )  index = 0;

  return ( index );
}  /* classify_species */


/******************************************************************************/
/* This function copies the GenBank entry for the current accession number. */
long copy_entry ( acs, group, gb )
char	acs [];			/* accession number */
t_text	*group;			/* evolutionary group file */
t_text	*gb;			/* GenBank .seq file */
{
  long  line_count = 0;		/* number of lines written */


  /* Check for end of file. */
  if ( (*gb).eof == TRUE )  return;

  /* Select the accession number. */
  acs [ d_stridx ( acs, "\t" ) ] = '\0';

  /* Copy the annotated sequence entry. */
  while ( ( (*gb).eof != TRUE ) && ( (*gb).line [ 0 ] != '/' ) && ( (*gb).line [ 1 ] != '/' ) )
  {
    /* Check for the ACCESSION number line. */
    if ( (*gb).line [ d_stridx ( (*gb).line, "ACCESSION" ) ] == 'A' )
    {
      /* Check that the accession numbers match. */
      if ( (*gb).line [ d_stridx ( (*gb).line, acs ) ] != acs [ 0 ] )
      {
        printf ( "Accession number mismatch: '%s' # '%s'\n", acs, (*gb).line );
        fprintf ( (*group).data, "//\n" );
        skip_entry ( gb );
        return 0;
      }  /* if */
    }  /* if */

    fprintf ( (*group).data, "%s", (*gb).line );

    /* Count the number of lines written to each file. */
    line_count++;
    get_line ( gb );
  }  /* while */

  if ( (*gb).eof != TRUE )
  {
    /* Copy the final // line. */
    fprintf ( (*group).data, (*gb).line );
    line_count++;

    /* Set the next line of the GenBank .seq file to the LOCUS line. */
    get_line ( gb );
  }  /* if */

  return ( line_count );
}  /* copy_entry */


/******************************************************************************/
/* This function finds the next LOCUS line. */
find_locus ( gb )
t_text  *gb;			/* GenBank .seq file */
{
  while ( ( (*gb).eof != TRUE ) &&
          ( (*gb).line [ d_stridx ( (*gb).line, "LOCUS" ) ] != 'L' ) )

    get_line ( gb );
}  /* method find_locus */


/******************************************************************************/
/* This function skips the GenBank entry. */
skip_entry ( gb )
t_text	*gb;			/* GenBank .seq file */
{
  get_line ( gb );

  /* Check for end of file. */
  if ( (*gb).eof == TRUE )  return;

  /* Copy the annotated sequence entry. */
  while ( ( (*gb).eof != TRUE ) && ( (*gb).line [ 0 ] != '/' ) && ( (*gb).line [ 1 ] != '/' ) )

    get_line ( gb );
}  /* skip_entry */


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
                     ( (*text).line [ index ] != (char) EOF ); index ++ )
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


/* Return length of string s */
d_strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* d_strlen */


/******************************************************************************/
/* This is function "system" from page 229 of the book "The Unix Programming Environment". */
d_system ( command )
char  *command;		/* command to execute */
{
  int  status, pid, w, tty;
  int (*istat)(), (*qstat)();

/*  printf ( "d_system: %s\n", command );  */

  status = 0;
  fflush ( stdout );
  tty = open ( "/dev/tty", 2 );
  if ( tty == -1 )
  {
    printf ( "*WARNING* Could not open '/dev/tty'\n" );
    return -1;
  }  /* if */

  if ((pid = fork ()) == 0 )
  {
    close ( 0 );  dup ( tty );
    close ( 1 );  dup ( tty );
    close ( 2 );  dup ( tty );
    close ( tty );
    execlp ( "sh", "sh", "-c", command, (char *) 0 );
    exit ( 127 );
  }

  close ( tty );
  istat = signal ( SIGINT, SIG_IGN );
  qstat = signal ( SIGQUIT, SIG_IGN );
  while ((w = wait (&status)) != pid && w != -1)  ;

  if ( w == -1 )  status = -1;
  signal ( SIGINT, istat );
  signal ( SIGQUIT, qstat );
  return status;
}  /* d_system */


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

