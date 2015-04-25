
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


/******************************************************************************/

#define  TRUE		1
#define  FALSE		0

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      50000000  /* maximum DNA sequence length */

#define  MAX_GRAIL_LEN  5000000  /* maximum length of DNA sequence for GRAIL */

#define  MAX_LINE	1012	/* maximum line length */

#define  MAX_NAME	120	/* maximum name length */

#define  MIN_DNA_SEQ_LENGTH  50	/* minimum DNA sequence length to process */

#define  MIN_GRAIL_OVERLAP  500 /* minimum overlap between DNA segments */

#define  MIN_SEQ	12	/* minimum sequence length */

#define  SEPARATOR      "_-"	/* name separator */


/******************************************************************************/

/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  name [ MAX_NAME  ];	/* sequence name */
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

main ( argc, argv )
int  argc;				/* number of command line arguments */
char *argv [];				/* command line arguments */
{
  char    base_name [ MAX_NAME ];	/* Request base name */
  t_text  cmds;				/* Unix shell commands */
  t_text  core_cmds;			/* CorePromoter commands */
  t_text  fof;				/* file of file names */
  t_text  in;				/* input DNA sequence file */
  long    num_seqs = 0;			/* total number of sequences */
  t_seq   seq;         			/* DNA sequence */
  t_text  genscan;			/* GENSCAN commands file */
  t_text  genemark;			/* GenMark commands file */
  t_text  mzef;				/* MZEF sequence file */
  t_text  mzef_cmds;			/* MZEF commands file */
  t_text  netgene_cmds;			/* NetGene2 commands file */
  long    start;			/* start of DNA sequence segment */
  long    end;				/* end of DNA sequence segment */
  char    segment_name [ MAX_NAME ];	/* string of version number */
  int     version;			/* current version number */
  FILE    *fopen ();			/* file open function */


  printf ( "This is the DNA Sequence files to Gene Predictions program.\n\n" );

  /* Check for command line parameters. */
  if ( argc > 1 )
  {
    d_strcpy ( fof.name, argv [ 1 ] );
    open_text_file ( &fof );
  }
  else
    /* Prompt for the input file name. */
    prompt_file ( &fof, "What is the list of sequences file name?" );

  /* Open the commands file. */
  d_strcpy ( cmds.name, fof.name );
  cmds.name [ d_stridx ( cmds.name, "." ) ] = '\0';
  d_strcpy ( genscan.name, cmds.name );
  d_strcpy ( genemark.name, cmds.name );
  d_strcpy ( mzef_cmds.name, cmds.name );
  d_strcpy ( core_cmds.name, cmds.name );
  d_strcpy ( netgene_cmds.name, cmds.name );

  d_strcat ( cmds.name, ".cmds" );
  cmds.data = fopen ( cmds.name, "w" );

  d_strcat ( mzef_cmds.name, ".mzef_cmds" );
  mzef_cmds.data = fopen ( mzef_cmds.name, "w" );

  d_strcat ( core_cmds.name, ".core_cmds" );
  core_cmds.data = fopen ( core_cmds.name, "w" );

  d_strcat ( genscan.name, ".genscan_cmds" );
  genscan.data = fopen ( genscan.name, "w" );

  d_strcat ( genemark.name, ".genemark_cmds" );
  genemark.data = fopen ( genemark.name, "w" );

  d_strcat ( netgene_cmds.name, ".netgene_cmds" );
  netgene_cmds.data = fopen ( netgene_cmds.name, "w" );

  d_strcpy ( base_name, fof.name );
  base_name [ d_stridx ( base_name, "\n" ) ] = '\0';
  base_name [ d_stridx ( base_name, "." ) ] = '\0';

  /* Count the number of DNA sequences. */
  while ( fof.eof != TRUE )
  {
    /* Open the sequence output file. */
    d_strcpy ( in.name, fof.line );
    in.name [ d_stridx ( in.name, "\n" ) ] = '\0';

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

  /* Write the sequences to the file. */
  while ( fof.eof != TRUE )
  {
    /* Open the DNA sequence file for reading. */
    d_strcpy ( in.name, fof.line );
    in.name [ d_stridx ( in.name, "\n" ) ] = '\0';
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );
    d_strcpy ( seq.name, in.name );
    fclose ( in.data );

    fof.line [ d_stridx ( fof.line, "\n" ) ] = '\0';

    if ( ( fof.eof != TRUE ) && ( genscan.data != NULL ) )
    {
      /* Segment the DNA sequence into maximum sized fragments. */
      version = 1;
      for ( start = 0; start < seq.total; start += MAX_GRAIL_LEN - MIN_GRAIL_OVERLAP )
      {
        /* Set up the MZEF input file. */
        make_segment_name ( &seq, version, mzef.name );

        fprintf ( genscan.data, 
            "genscan /data6/progs/genscan/Arabidopsis.smat %s -v -cds -ps %s.ps > %s.genscan\n",
            mzef.name, mzef.name, mzef.name );
        fprintf ( genemark.data, 
            "gm -v -m ../share/matrices/A.thaliana_1_4.mat -gnforsx -orx -onp -rnp %s %s\n", 
            mzef.name, mzef.name );
        fprintf ( cmds.data, 
            "gmhmme -o %s.gmhmm -m /data6/progs/share/matrices/athalian.mtx %s\n", 
            mzef.name, mzef.name );
        fprintf ( netgene_cmds.data, "netgene2 %s\n", mzef.name );

        /* Write the MZEF analysis commands. */
        fprintf ( mzef_cmds.data, "mzef %s 1 0.03 0 > %s.mzef_for\n",
            mzef.name, mzef.name );
        /* Analyze the reverse strand. */
        fprintf ( mzef_cmds.data, "mzef %s 2 0.03 0 > %s.mzef_rev\n",
            mzef.name, mzef.name );

        fprintf ( core_cmds.data, 
            "cpromoter %s 1 0.003 1 20 0 > %s.cpromoter", mzef.name, mzef.name );

        /* Set up the MZEF file for writing. */
        mzef.data  = fopen ( mzef.name, "w" );

        end = start + MAX_GRAIL_LEN - 1;
        if ( end >= seq.total )  end = seq.total - 1;
        fprintf ( mzef.data,  ">%s First=%d Length=%d\n", fof.line, start, seq.total );

        /* Write out the DNA sequence. */
        write_DNA_seq ( &seq, start, end, &mzef);
        fclose ( mzef.data );
        version++;
      }  /* for */
    }  /* if */

    /* Get the name and sequence. */
    get_line ( &fof );
  }  /* while */

  fclose ( cmds.data );
  fclose ( core_cmds.data );
  fclose ( mzef_cmds.data );
  fclose ( genscan.data );
  fclose ( genemark.data );
  fclose ( netgene_cmds.data );

  printf ( "\nEnd of DNA Sequences GENSCAN & GeneMark program.\n" );
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
/* This function gets the next text character. */
get_char ( text )
t_text  *text;		/* ASCII text file */
{
  if ( (*text).next == (char) EOF )  return; 
  (*text).next = ' ';

  /* Get the next sequence character. */
  if ( ( (*text).line [ (*text).line_index ] != '\n' ) &&
       ( (*text).line [ (*text).line_index ] != '\0' ) )

    (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).next != (char) EOF ) )

    if ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) )
    {
      if ( (*text).eof == TRUE )  (*text).next = (char) EOF;
      else  get_line ( text );
    }
    else
      (*text).next = (*text).line [ (*text).line_index++ ];

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


  if ( (*text).next == (char) EOF )  return;

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


/******************************************************************************/
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
           ( (*text).next == (char) EOF ) ) )
      end_of_file = TRUE;
  }  
  while ( ( end_of_file != TRUE ) && ( (*seq).total < MAX_BASES ) );
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


/******************************************************************************/
/* This function writes out a DNA segment. */
make_segment_name ( seq, version, segment_name )
t_seq   *seq;			/* DNA sequence */
int	version;		/* current version number */
char    segment_name [];	/* current segment name */
{
  char    version_str [ MAX_LINE ];	/* ascii version number */


  /* Set up the Fasta segment sequence file name. */
  d_strcpy ( segment_name, (*seq).name );
  segment_name [ d_stridx ( segment_name, SEPARATOR ) ] = '\0'; 
  d_strcat ( segment_name, SEPARATOR );

  itoa ( version, version_str );
  if ( version < 100 )  d_strcat ( segment_name, "0" );
  if ( version <  10 )  d_strcat ( segment_name, "0" );
  d_strcat ( segment_name, version_str );
}  /* make_segment_name */


