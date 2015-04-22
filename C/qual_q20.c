
/******************************************************************************/
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


/* Sequence quality. */
typedef  struct {
  long    total;		/* number of quality values */
  char    name [ MAX_NAME ];	/* quality file name */
  char    desc [ MAX_LINE ];	/* description line */
  int     edited;		/* number of edited bases */
  int     first_Q20;		/* coordinate for first Q20 base */
  int     last_Q20;		/* coordinate of last Q20 base */
  int     longest_M26_stretch;  /* longest stretch of M26 bases */
  int     longest_Q20_stretch;  /* longest stretch of Q20 bases */
  int     Q17_bases;		/* the number of bases >= Phred 17 */
  int     Q20_bases;		/* the number of bases >= Phred 20 */
  int     lower_bases;          /* the number of bases < Myriad 20 */
  int     M20_good_bases;       /* the number of bases >= Myriad 20 with 11 base window > 50 */
  int     M26_bases;            /* the number of bases >= Myriad 26 */
  int     M60_bases;            /* the number of bases >= Myriad 60 */
  int     qual [ MAX_BASES ];	/* quality value for each DNA base */
} t_qual;


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
/*  t_text  bases_file;			/* file of bases and quality information */
  char    directory [ MAX_NAME ];	/* current directory name */
  t_text  fof;				/* file of file names file */
  int     index;
  char    name [ MAX_LINE ];		/* list file name */
  t_qual  quality;			/* DNA sequence quality values */
  t_text  seq_file;            		/* DNA sequence file */
  t_seq   sequence;			/* DNA sequence */
  long    sequence_qual_id = 12000001;	/* sequence_qual_id field of SequenceQual table */
  t_text  summary_file;			/* summary file */

  FILE    *fopen ();			/* file open function */
  int     get_integer ();		/* specify function type */


  /* Check if command line argument specified. */
  if ( argc <= 1 )

    /* Prompt for the input file name. */
    prompt_file ( &fof, "What is the name of the .qual file?" );

  else
  {
    d_strcpy ( fof.name, argv [ 1 ] );
    open_text_file ( &fof );
  }  /* else */

  if ( argc > 2 )
  {
    sequence_qual_id = (long) get_integer ( argv [ 2 ] );
  }  /* if */

  /* Set up the main output files. */
  d_strcpy ( summary_file.name, fof.name );
  summary_file.name [ d_stridx ( summary_file.name, "." ) ] = '\0';
  d_strcat ( summary_file.name, ".sql" );
  summary_file.data = fopen ( summary_file.name, "w" );

  fprintf ( summary_file.data, "set AUTOCOMMIT 1\n\n" );

  d_strcpy ( seq_file.name, fof.name );
  seq_file.name [ d_stridx ( seq_file.name, ".qual" ) ] = '\0';
  d_strcat ( seq_file.name, ".fasta" );
  open_text_file ( &seq_file );

  /* Process the .qual file. */
  while ( fof.eof != TRUE )
  {
    d_strcpy ( directory, fof.name );
    index = d_stridx ( directory, "." );
    if ( directory [ index ] == '.' )
    {
      directory [ index ] = '\0';
    }
    else directory [ 0 ] = '\0';

    if ( ( fof.eof != TRUE ) && ( fof.data != NULL ) )
    {
        read_DNA_seq ( &seq_file, &sequence );

        sequence.name [ d_stridx ( sequence.name, ".scf" ) ] = '\0';
        if ( sequence.name [ d_stridx ( sequence.name, "/" ) ] == '/' )
          d_strcpy ( sequence.name, &(sequence.name [ d_stridx ( sequence.name, "/" ) + 1 ]) );

        qual_Q20 ( &fof, &quality );
/*
        for ( index = 0; (index < sequence.total) && (index < 600); index++ )
        {
          fprintf ( bases_file.data, "%s\t", directory );
          fprintf ( bases_file.data, "%s\t", sequence.name );
          fprintf ( bases_file.data, "%d\t", index + 1 );
          fprintf ( bases_file.data, "%c\t", sequence.base [ index ] );
          fprintf ( bases_file.data, "%d\n", quality.qual [ index ] );
        }  /* for */

        fprintf ( summary_file.data, "INSERT INTO SequenceQual values ( " );
        fprintf ( summary_file.data, "%d, ", sequence_qual_id++ );
        fprintf ( summary_file.data, "'%s', ", sequence.name );
        fprintf ( summary_file.data, "%d, ", sequence.total );
        fprintf ( summary_file.data, "%d, ", sequence.N_bases );

        fprintf ( summary_file.data, "%d, ", quality.Q17_bases );
        fprintf ( summary_file.data, "%d, ", quality.Q20_bases );
        fprintf ( summary_file.data, "%d, ", quality.first_Q20 );
        fprintf ( summary_file.data, "%d, ", quality.last_Q20 );
        fprintf ( summary_file.data, "%d );\n", quality.longest_Q20_stretch );

        printf ( "%s \tlen = %d, \tQ20s = %d, \tbest = %d\n",
            sequence.name, sequence.total, quality.Q20_bases, quality.longest_Q20_stretch );
    }  /* if */
  }  /* while */

  fprintf ( summary_file.data, "commit;\n\n" );

  /* Close the input file of PHD file names. */
  fclose ( fof.data );
  fclose ( seq_file.data );
  fclose ( summary_file.data );
}  /* main */


/******************************************************************************/
/* This function longest stretch of bases >= min_value in the base qualities. */
int find_longest ( min_value, quality )
int     min_value;		/* minimum quality value to consider */
t_qual  *quality;		/* Sequence base quality */
{
  int current = 0;		/* length of the current stretch */
  int index;			/* loop index */
  int longest = 0;		/* length of longest stretch found so far */

  for ( index = 0; index < (*quality).total; index++ )
  {
    if ( (*quality).qual [ index ] < min_value )  current = 0;
    else
    {
      current++;

      if ( current > longest )  longest = current;
    }  /* else */
  }  /* for */

  return longest;
}  /* function find_longest */


/******************************************************************************/
/* This function counts the number of good bases using Myriad's method. */
good_bases ( min_value, quality )
int     min_value;		/* minimum quality value to consider */
t_qual  *quality;		/* Sequence base quality */
{
  int  count;			/* number of surrounding bases */
  int  near;			/* adjacent bases */
  int  index;			/* current base position */
  int  sum;			/* sum of surrounding bases */

  /* Evaluate each base. */
  for ( index = 0; index < (*quality).total; index++ )
  {
    if ( (*quality).qual [ index ] < 20 )  
    {
      /* Don't count the bases with quality values of 0. */
      if ( (*quality).qual [ index ] > 0 )  (*quality).lower_bases++;
    }
    else
    {
      count = sum = 0;  /* initialize */

      /* Sum up the preceeding bases. */
      for ( near = index - 5; near < index; near++ )

        if ( near >= 0 )
        {
          count++;
          sum += (*quality).qual [ near ];
        }  /* if */

      /* Sum up the following bases. */
      for ( near = index + 5; near > index; near-- )

        if ( near < (*quality).total )
        {
          count++;
          sum += (*quality).qual [ near ];
        }  /* if */

      if ( count > 0 )
        if ( sum / count >= 50 )  (*quality).M20_good_bases++;
    }  /* else */
  }  /* for */
}  /* function good_bases */


/******************************************************************************/
qual_Q20 ( qual_file, quality )
t_text  *qual_file;		/* .qual file */
t_qual  *quality;		/* Sequence base quality */
{
  int     bases;		/* the number of bases in the sequence */
  int     index;		/* line index */
  int     score;		/* Phred quality score for current base */

  int     get_integer ();	/* specify function type */


  /* Initialize the variables. */
  bases = score = 0;

  (*quality).total = 0;
  (*quality).desc [ 0 ] = '\0';
  (*quality).edited = 0;
  (*quality).first_Q20 = 0;
  (*quality).last_Q20 = 0;
  (*quality).longest_Q20_stretch = 0;
  (*quality).longest_M26_stretch = 0;
  (*quality).lower_bases = 0;
  (*quality).Q17_bases = 0;
  (*quality).Q20_bases = 0;
  (*quality).M26_bases = 0;
  (*quality).M60_bases = 0;
  (*quality).M20_good_bases = 0;

  /* Find the ">" line. */
  while ( ( (*qual_file).eof != TRUE ) &&
          ( (*qual_file).line [ d_stridx ( (*qual_file).line, ">" ) ] != '>' ) )

    get_line ( qual_file );

  /* Save the description line. */
  d_strcpy ( (*quality).desc, &((*qual_file).line [ 1 ]) );

  /* Get the next data line. */
  get_line ( qual_file );

  /* Count the number of bases & the number greater than quality 20. */
  while ( ( (*qual_file).eof != TRUE ) &&
          ( (*qual_file).line [ d_stridx ( (*qual_file).line, ">" ) ] != '>' ) )
  {
    /* Check for end of the DNA sequence. */
    if ( ( (*qual_file).eof != TRUE ) &&
          ( (*qual_file).line [ d_stridx ( (*qual_file).line, ">" ) ] != '>' ) )
    {
      index = 0;
      while ( (*qual_file).line [ index ] != '\0' )
      {
        if ( ( (*qual_file).line [ index ] >= '0' ) &&
             ( (*qual_file).line [ index ] <= '9' ) )
        {
          bases++;

          /* Get the quality score for this base. */
          score = get_integer ( &((*qual_file).line [ index ]) );
          (*quality).qual [ (*quality).total ] = score;
          (*quality).total++;

          /* Skip the current integer. */
          while ( ( (*qual_file).line [ index ] >= '0' ) &&
                  ( (*qual_file).line [ index ] <= '9' ) )  index++;

          /* Look for scores >= 20 but less than 90 which indicates hand editing. */
          if ( ( score >= 20 ) && ( score < 90 ) )
          {
            (*quality).Q20_bases++;

            /* Check if first base with a Q20 score. */
            if ( (*quality).first_Q20 == 0 )  (*quality).first_Q20 = bases;

            /* Save the last Q20 base position. */
            (*quality).last_Q20 = bases;
          }  /* if */
          /* Count edited bases. */
          else
            if ( score >= 90 ) (*quality).edited++;

          if ( ( score >= 17 ) && ( score < 90 ) )  (*quality).Q17_bases++;
        }  /* if */

        index++;
      }  /* while */
    }  /* if */

    /* Get the next data line. */
    get_line ( qual_file );
  }  /* while */

  (*quality).longest_Q20_stretch = find_longest ( 20, quality );
}  /* qual_Q20 */


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

