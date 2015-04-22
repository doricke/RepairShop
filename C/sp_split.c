
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

#define  MAX_GROUPS	7	/* maximum number of evolutionary groups */
			        /* 0 = Planta, 1 = Fungi, 2 = Protozoa, 3 = Metazoa, */
                                /* 4 = Chordata, 5 = bacteria, 6 = Viridae */

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


main ( argc, argv )
int argc;		/* number of command line arguments */
char *argv [];		/* command line arguments */
{

  t_text  acs;			/* Accession numbers and taxonomy */
  t_text  sp;			/* SwissProt .dat file */
  FILE    *fopen ();		/* file open function */
  int     index;

  char    line [ MAX_LINE ];		/* current .seq file line */

  t_text  groups [ MAX_GROUPS ];	/* Evolutionary groups */


  printf ( "This is the SwissProt species split program.\n\n" );

  /* Check for command line parameters. */
  if ( argc > 1 )
  {
    /* Assume the first parameter is the file name of the list of sequences to process. */
    d_strcpy ( acs.name, argv [ 1 ] );
    open_text_file ( &acs );
  }  /* if */
  else

    prompt_file ( &acs, "What is the name of the SwissProt accession & taxonomy file?" );

  if ( argc > 2 )
  {
    /* Assume the first parameter is the file name of the list of sequences to process. */
    d_strcpy ( sp.name, argv [ 1 ] );
    open_text_file ( &sp );
  }  /* if */
  else

    /* Prompt for the input file name. */
    prompt_file ( &sp, "What is the name of the SwissProt .dat file?" );

  /* Set up the evolutionary group files. */
  swiss_files ( groups, &sp );

  /* Process the names. */
  while ( acs.eof != TRUE )
  {
    d_strcpy ( line, acs.line );
    line [ d_stridx ( line, "\n" ) ] = '\0';

    /* Classify the sequences by species. */
    index = classify_species ( line );

    /* Copy the associated sequence. */
    if ( ( index > -1 ) && ( index < MAX_GROUPS ) )
      copy_swiss ( line, &(groups [ index ]), &sp );

    /* Get the next line */
    get_line ( &acs );
  }  /* while */

  /* Close the files. */
  fclose ( acs.data );
  fclose ( sp.data );
  for ( index = 0; index < MAX_GROUPS; index++ )
    fclose ( groups [ index ].data );
}  /* main */


/* This function sets up the SwissProt split files. */
swiss_files ( groups, swiss )
t_text  groups [];		/* SwissProt evolutionary groups */
t_text  *swiss;			/* .dat input file */
{
  int  index;
  char prefix [ MAX_LINE ];

  d_strcpy ( prefix, (*swiss).name );
  prefix [ d_stridx ( prefix, "." ) ] = '\0';

  for ( index = 0; index <= 6; index++ )

   d_strcpy ( groups [ index ].name, prefix ); 

  d_strcat ( groups [ 0 ].name, "_plant" );
  d_strcat ( groups [ 1 ].name, "_fungi" );
  d_strcat ( groups [ 2 ].name, "_protozoa" );
  d_strcat ( groups [ 3 ].name, "_metazoa" );
  d_strcat ( groups [ 4 ].name, "_chordata" );
  d_strcat ( groups [ 5 ].name, "_prokaryota" );
  d_strcat ( groups [ 6 ].name, "_viridae" );

  /* Open the SwissProt split files. */
  for ( index = 0; index < MAX_GROUPS; index++ )
    groups [ index ].data = fopen ( groups [ index ].name, "w" );
}  /* swiss_files */


/* This function classifies the group associated with a taxonomy. */
int classify_species ( acs )
char  acs [];			/* accession number & taxonomy */
{
  int  index = -1;		/* file selector */

  /* Identify group. */
  if ( acs [ d_stridx ( acs, "METAZOA" ) ] == 'M' )  index = 3;
  if ( acs [ d_stridx ( acs, "Metazoa" ) ] == 'M' )  index = 3;

  /* Must follow check for METAZOA. */
  if ( acs [ d_stridx ( acs, "CHORDATA" ) ] == 'C' )  index = 4;
  if ( acs [ d_stridx ( acs, "Chordata" ) ] == 'C' )  index = 4;

  if ( acs [ d_stridx ( acs, "PROTOZOA" ) ] == 'P' )  index = 2;
  if ( acs [ d_stridx ( acs, "Protozoz" ) ] == 'P' )  index = 2;

  if ( acs [ d_stridx ( acs, "PROKARYOTA" ) ] == 'P' )  index = 5;
  if ( acs [ d_stridx ( acs, "Prokaryota" ) ] == 'P' )  index = 5;

  if ( acs [ d_stridx ( acs, "Bacteria" ) ] == 'B' )  index = 5;

  /* Group the Archaebacteria with the Prokaryota. */
  if ( acs [ d_stridx ( acs, "ARCHAEBACTERIA" ) ] == 'A' )  index = 5;
  if ( acs [ d_stridx ( acs, "Archaebacteria" ) ] == 'A' )  index = 5;

  if ( acs [ d_stridx ( acs, "Archaea" ) ] == 'A' )  index = 5;

  if ( acs [ d_stridx ( acs, "EMBRYOPHYTA" ) ] == 'E' )  index = 0;
  if ( acs [ d_stridx ( acs, "Embryophyta" ) ] == 'E' )  index = 0;

  if ( acs [ d_stridx ( acs, "ASCOMYCOTINA" ) ] == 'A' )  index = 1;
  if ( acs [ d_stridx ( acs, "Ascomycotina" ) ] == 'A' )  index = 1;

  if ( acs [ d_stridx ( acs, "VIRIDAE" ) ] == 'V' )  index = 6;
  if ( acs [ d_stridx ( acs, "Viridae" ) ] == 'V' )  index = 6;

  if ( acs [ d_stridx ( acs, "Viruses" ) ] == 'V' )  index = 6;

  if ( acs [ d_stridx ( acs, "FUNGI" ) ] == 'F' )  index = 1;
  if ( acs [ d_stridx ( acs, "Fungi" ) ] == 'F' )  index = 1;

  if ( acs [ d_stridx ( acs, "PLANTA" ) ] == 'P' )  index = 0;
  if ( acs [ d_stridx ( acs, "Planta" ) ] == 'P' )  index = 0;

  if ( acs [ d_stridx ( acs, "Viridiplantae" ) ] == 'V' )  index = 0;

  if ( index == -1 )
  {
    printf ( "Unknown species: '%s'\n", acs );
    index = 3;
  }  /* if */

  return ( index );
}  /* classify_species */


/* This function copies the SwissProt entry for the current accession number. */
copy_swiss ( acs, group, sp )
char	acs [];			/* accession number */
t_text	*group;			/* evolutionary group file */
t_text	*sp;			/* SwissProt .dat file */
{
  char  id [ MAX_LINE ];	/* ID line */


  /* Select the accession number. */
  acs [ d_stridx ( acs, "\t" ) ] = '\0';

  /* Get and copy the ID line. */
  d_strcpy ( id, (*sp).line );
  fprintf ( (*group).data, "%s", id );

  /* Get the AC line. */
  get_line ( sp );

  /* Check that the accession numbers match. */
  if ( (*sp).line [ d_stridx ( (*sp).line, acs ) ] == '\0' )
    printf ( "Accession number mismatch: '%s' # '%s'\n", acs, (*sp).line );

  /* Copy the annotated sequence entry. */
  while ( ( (*sp).eof != TRUE ) && ( (*sp).line [ 0 ] != '/' ) && ( (*sp).line [ 1 ] != '/' ) )
  {
    fprintf ( (*group).data, "%s", (*sp).line );
    get_line ( sp );
  }  /* while */

  if ( (*sp).eof != TRUE )
  {
    /* Copy the final // line. */
    fprintf ( (*group).data, (*sp).line );

    /* Set the next line of the SwissProt .dat file to the ID line. */
    get_line ( sp );
  }  /* if */
}  /* copy_swiss */


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

  if ( (*text).eof == TRUE )  (*text).next = (char) EOF;
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
  while ( ( (*text).eof == TRUE ) && ( d_strcmp ( (*text).name, "exit" ) != 0 ) )
  {
    printf ( "%s or 'exit' ", prompt );

    scanf ( "%s", (*text).name );

    if ( d_strcmp ( (*text).name, "exit" ) != 0 )

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

