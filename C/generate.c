

#include <stdio.h>

/* This program randomly generates a DNA sequence. */
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

#define A_BITS		1	/* bit map of DNA base A */
#define C_BITS		4	/* bit map of DNA base C */
#define G_BITS		2	/* bit map of DNA base G */
#define T_BITS		8	/* bit map of DNA base T */

#define A_COMP_RANGE   30	/* % A composition */
#define C_COMP_RANGE   48	/* % C & A compositions */
#define G_COMP_RANGE   71	/* % G & C & A compositions */
#define T_COMP_RANGE   99	/* 0 - 99 for 100% composition */

#define RANDOM_LENGTH	1523	/* length of randomly generated sequence */
#define RANDOM_NAME	"HUMFIX.RANDOM"	/* random sequence output filename */

#define S_BAD_PAIR	8	/* bad coordinate pair - no . . */
#define S_END_OF_LINE	7	/* end of line encountered */
#define S_EOF		1	/* End Of File encountered */
#define S_LOST		2	/* Inconsistant file position */
#define S_MISMATCH	9	/* mismatch */
#define S_NO_INTEGER	3	/* no integer on current line */
#define S_NORMAL	6	/* normal function termination */
#define S_OPEN_FAILED	5	/* file open failed */
#define S_TABLE_OVERFLOW  4	/* table overflow */

#define S_AFTER_COMP	16	/* pair extents after & overlaps */
#define S_BEFORE_COMP	15	/* pair extends before & overlaps */
#define S_BEGIN_COMP	11	/* pair matches begining of component */
#define S_BEGIN_OVERLAP	22	/* begin < .begin & end < .end */
#define S_DATA		14	/* start of sequence data ".." */
#define S_EMPTY_TABLE	18	/* table empty */
#define	S_END_COMP	10	/* pair matches end of component */
#define S_END_OVERLAP	20	/* begin > .begin & end > .end */
#define S_ERROR		21	/* error found */
#define S_FOUND		13	/* pair exists in component table */
#define S_NOT_FOUND	19	/* pair past end of table */
#define S_SUB_COMP	17	/* pair is contained within component */
#define S_SUPER_COMP	12	/* pair contains current component */

#define C_3_SPLICE	10	/* 3' of exon splicing region */
#define C_5_SPLICE	20	/* 5' of exon splicing region */

#define DEL_SUFFIX      "TEMPLATE"    /* deletion sequnce template suffix */

#define	TRUE		1	/* Boolean flags */
#define FALSE		2

#define	MAX_BASES	400000	/* maximum number of sequence bases */

#define MAX_COMPONENTS	5000	/* maximum components per sequence file */

#define	MAX_DINUCLEOTIDES  16	/* maximum number of dinucleotides */

#define MAX_GENES	1000	/* maximum genes per sequence file */

#define MAX_HISTOGRAM	50	/* maximum elements in histogram array */

#define MAX_LINE 	132	/* maximum line length */

#define	MAX_NAME	9	/* maximum pattern name length */

#define	MAX_PATTERN	1000	/* maximum search pattern size */

#define	MAX_PATTERNS	1000	/* maximum number of patterns */

#define	MAX_PER_LINE	70	/* maximum bases printed per line */

#define  MAX_RANDOM     (32768 * 65536 - 1)    /* 2^31-1 - maximum random number from rand */

#define MAX_START	10	/* maximum spaces before start of feature */

#define	MAX_TRINUCLEOTIDES  64	/* maximum number of trinucleotides */

#define MAX_UNITS	1	/* maximum unit length to search up to */

#define	MIN_REPEAT	10	/* minimum direct repeat length */

#define	REPEAT_WINDOW	50	/* maximum window size for direct repeats */

#define AFTER_BASES	10	/* bases to print after a pattern match */
#define BEFORE_BASES	10	/* bases to print before a pattern match */

#define AFTER_DELETION	20	/* bases to print after a deletion */
#define BEFORE_DELETION	20	/* bases to print before a deletion */

#define H_HISTOGRAM     1       /* starndard histogram element size */
#define H_SEQUENCE      10      /* sequence subcomponent element size */
#define H_FILE_LEN      1000    /* file length histogram element size */

#define	P_SEQUENCE	1	/* search for unique sequence only */
#define	P_SUBUNIT	2	/* repeat pattern subunit minimum times */
#define P_REPEAT	3	/* direct repeat pattern */
#define P_PALINDROME	4	/* palindrome pattern */
#define P_DELETION      5       /* random deletion selection */

#define CR		13	/* carriage return */

#define	P		0.1	/* Pattern search probablity */

/* Sequence component classification categories. */
#define	UNSPECIFIED	0	/* unspecified region */
#define	cDNA_5_PRIME	1	/* cDNA 5' untranslated region */
#define	cDNA_CODE	2	/* cDNA coding region */
#define	cDNA_3_PRIME	3	/* cDNA 3' untranslated region */
#define	gDNA_5_PRIME	4	/* genomic DNA 5' untranslated region */
#define	gDNA_EXON	5	/* genomic DNA exon region */
#define	gDNA_INTRON	6	/* genomic DNA intron region */
#define	gDNA_CODE	7	/* genomic DNA coding region */
#define	gDNA_3_PRIME	8	/* genomic DNA 3' untranslated region */
#define	REPEAT		9	/* repeat region */
#define TOTAL		10	/* entire sequence(s) */
#define	PATTERN		11	/* pattern matched region */
#define	SPLICE		12	/* mRNA splicing consensus region */

#define	MAX_DNA_TYPES	SPLICE + 1	/* DNA classification categories */

#define	NO_BITS		0	/* zero bit mask */


/******************************************************************************/

/* sequence information for one sequence file */
typedef struct {
  FILE	*seq_file;		/* internal C file */
  char	file_name [ MAX_LINE ];	/* name of the sequence file */
  char	line [ MAX_LINE ];	/* current line of the file */
  char	seq_char;		/* current sequence character */
  int	line_index;		/* line index of seq_char */
} t_seq;


/* Gene specific information. */
typedef struct {
  char	name [ MAX_LINE ];	/* gene description */
  long	begin;			/* start of transcript */
  long	end;			/* end of transcript */
  long	start;			/* first codon */
} t_gene;

/* table of genes */
typedef struct {
  long		total;			/* number of genes */
  t_gene	gene [ MAX_GENES ];	/* each gene info */
} t_genes;


/* GCG sequence data file header information. */
typedef struct {
  char	definition [ MAX_LINE ];	/* sequence definition line */
  char	seq_type [ MAX_LINE ];		/* DNA or mRNA */
  long	seq_length;			/* sequence length */
} t_gcg_header;


/* Histogram of lengths. */
typedef struct {
  long	lengths [ MAX_HISTOGRAM ];	/* histogram of lengths */
} t_histogram;


/* sequence component */
typedef struct {
  long	begin;				/* start of component */
  long	end;				/* end of component */
  int	type;				/* type of component */
  int	gene;				/* genes table index */
  char	description [ MAX_LINE ];	/* component description */
} t_component;

/* sequence components table */
typedef struct {
  long		total;				/* number of components */
  long		active;				/* current component */
  char		file_name [ MAX_LINE ];		/* sequence filename */
  t_component	atom [ MAX_COMPONENTS ];	/* each component */
} t_components;



/* DNA or RNA base */
typedef struct {
  unsigned	bits : 4;	/* bit map of nucleotide base */
} t_base;

/* DNA or RNA sequence */
typedef struct {
  long		length;			/* length of sequence */
  char		filename [ MAX_LINE ];	/* sequence filename */
  t_base	base [ MAX_BASES ];	/* Each base of sequence */
} t_sequence;


/* Search pattern information */
typedef struct {
  int		length;			/* pattern length */
  int		minimum;		/* minimum number of repeats */
  int           maximum;                /* maximum number of repeats */
  int		type;			/* type of pattern */
  double	expected;		/* number of hits expected by random */
  float		match;			/* percentage of match required */
  char		name [ MAX_NAME ];	/* pattern name */
  t_base	base [ MAX_PATTERN ];	/* search pattern */
  FILE		*pat_file;		/* pattern output file */
  t_histogram	histo;			/* pattern match lengths */
} t_pattern;

/* table of patterns to search for */
typedef struct {
  int		total;				/* total number of patterns */
  t_pattern	pattern [ MAX_PATTERNS ];	/* each pattern information */
} t_patterns;


/* base composition table */
typedef struct {
  long	count [ 'Z' - 'A' + 1 ];		/* base compositions */
} t_composition;


/* dinucleotide base composition table */
typedef struct {
  long	count [ MAX_DINUCLEOTIDES ];		/* dinucleotide totals */
} t_dinucleotide;


/* trinucleotide base composition table */
typedef struct {
  long	count [ MAX_TRINUCLEOTIDES ];		/* trinucleotide totals */
} t_trinucleotide;


/* table of sequence statistics */
typedef struct {
  long		 	total_sequences;	/* # of sequences processed */
  t_histogram		file_lengths;		/* sequence lengths */
  t_composition  	comp [ MAX_DNA_TYPES ];	/* DNA category composition */
  t_dinucleotide	di [ MAX_DNA_TYPES ];	/* dinucleotide compositions */
  t_trinucleotide	tri [ MAX_DNA_TYPES ];	/* trinucleotide compositions */
  t_histogram		lengths [ MAX_DNA_TYPES ];	/* component lengths */
} t_seq_stats;


/* table of deletion statistics */
typedef struct {
  t_histogram	prior_repeat;   	/* direct repeats prior to deletion */
  t_histogram	prior_palindrome;    	/* direct repeats prior to deletion */
  t_histogram	after_repeat;    	/* direct repeats prior to deletion */
  t_histogram	after_palindrome;    	/* direct repeats prior to deletion */
} t_del_stats;


/* Best find of direct repeat or palindrome. */
typedef struct {
  int  start;    /* start of the find */
  int  length;   /* subunit length */
  int  count;    /* number of subunits (repeats) */
} t_best_find;

/******************************************************************************/

/* DNA/RNA bit representation - allows IUB nucleotide ambiguity */
static	int	base_masks [] = {

/* A    B    C    D   E  F   G    H   I  J   K   L   M */
  0x1, 0xE, 0x4, 0xB, 0, 0, 0x2, 0xD, 0, 0, 0xA, 0, 0x5,

/* N    O  P  Q   R    S    T    U    V    W    X   Y   Z */
   0, 0, 0, 0, 0x3, 0x6, 0x8, 0x8, 0x7, 0x9,  0, 0xC, 0 };


/* DNA/RNA bit representation to nucleotide map. */
static	char	dna_map [] = {

/* 0    1    2    3    4    5    6    7 */
  'N', 'A', 'G', 'R', 'C', 'M', 'S', 'V',

/* 8    9    A    B    C    D    E    F */
  'T', 'W', 'K', 'D', 'Y', 'H', 'B', 'X' };


/* DNA classification category type names. */
static	char	*dna_types [] = {
  "Unspecified", "cDNA 5' UT", "cDNA coding",   "cDNA 3' UT",
  "gDNA 5' UT",  "gDNA exons", "gDNA introns",  "gDNA coding",
  "gDNA 3' UT",  "Repeats",    "All sequences", "Patterns", "Splice" };


/* DNA component category classification type masks. */
static	int	comp_types [] = {
  0, 0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80, 0x100, 0x200, 0x400, 0x800 };

/* DNA component category classification type names. */
static	char	*comp_names [] = {
  "GEN", "c5UT", "CODE", "c3UT", "g5UT", "EXON",
  "IVS", "Code", "g3UT", "REP",  "ALL", "PAT",  "SPL" };


/******************************************************************************/

main ( )
{
  t_sequence  sequence;    /* the sequence to randomly generate */


  user_randomize ();    /* initialize the random number generator */

  strcopy ( sequence.filename, RANDOM_NAME );
  sequence.length = RANDOM_LENGTH;

  generate_seq ( &sequence );

  write_seq ( &sequence );

  printf ("\nEnd main program.\n");
}  /* main */


/******************************************************************************/
/* This function checks the string s for a blank line. */
/* This function returns 0 if blank line else != 0 */
blank_line ( s )
char	*s;
{
  while ( *s == ' ' )  s++;
  if ( *s == CR )  return ( 0 );
  return ( *s );
}  /* blank_line */


/******************************************************************************/
/* This function capitalizes a string. */
capitalize (s)
char  *s;	/* string to capitalize */
{
  /* traverse the string */
  for ( ; *s != '\0'; s++ )

    /* check for a lower case letter */
    if ((*s >= 'a') && (*s <= 'z'))
      *s += 'A' - 'a';
}  /* capitalize */


/******************************************************************************/
/* This function searches s for c. */
/* This function returns index of c or '\0'. */
charidx ( c, s )
char	c;	/* character to find in string */
char	*s;	/* string to search */
{
  char	*p = s;	/* string pointer */

  /* Search the string. */
  while (( *p != '\0') && ( *p != c ))  p++;

  return ( p - s );
}  /* charidx */


/******************************************************************************/
/* This function concatenates t to the end of s. */
concatenate ( s, t)
char	s [], t [];
{
  int	i = 0;
  int	j = 0;

  /* Check for strings being too long. */
  if (( str_len ( s ) + str_len ( t ) + 1 ) > MAX_LINE )
    printf ( "*WARNING* '%s%s' shortended to %d characters.\n", (MAX_LINE - 1));

  /* Find the end of s; */
  while ( s [ i ] != '\0' )  i++;

  /* Copy t. */
  while ((( s [ i++ ] = t [ j++ ] ) != '\0' ) && ( i <= MAX_LINE ))  ;
}  /* concatenate */


/******************************************************************************/
/* This function returns the length of the string s. */
str_len ( s )
char	*s;	/* the string to determine the length of */
{
  char	*p = s;		/* string pointer */

  /* Advance to the end of the string */
  while ( *p != '\0' )  p++;

  /* Return the length of the string. */
  return ( p - s );
}  /* str_len */


/******************************************************************************/
/* This function compares the string s to t. */
/* This function returns < 0 if s < t, 0 if s == t, > 0 if s > t */
strcmp (s, t)
char	*s, *t;
{
  /* compare the strings for equivalence */
  for ( ; *s == *t; s++, t++)
    if ( *s == '\0')  return (0);

  return ( *s - *t );
}  /* strcmp */



/******************************************************************************/
/* This function copies the string t to s. */
strcopy ( s, t )
char	*s, *t;
{
  while ( *s++ = *t++ ) ;
}  /* strcopy */


/******************************************************************************/
/* This function searches s for t. */
/* This function returns index of t or '\0'. */
stridx ( t, s )
char	*t;	/* substring to find in string */
char	*s;	/* string to search */
{
  char	*p = s;	/* string pointer */

  /* Find the start of the string. */
  while (( *p != '\0') && ( substring ( t, p ) != 0 ))  p++;

  return ( p - s );
}  /* stridx */



/******************************************************************************/
/* This function checks if s is a substring of t. */
/* This function returns < 0 if s < t, 0 if s == t, > 0 if s > t */
substring ( s, t )
char	*s, *t;
{
  /* Compare the strings for equivalence */
  for ( ; *s == *t; s++, t++ )
    if ( *s == '\0' )  return ( 0 );

  if ( *s == '\0' )  return ( 0 );
  return ( *s - *t );
}  /* substring */


/******************************************************************************/
/* This function consumes the specified token. */
consume ( data_file, item, line, line_index )
FILE	*data_file;	/* GCG data file */
char	item [];	/* item to consume */
char	line [];	/* current line of the data file */
char	*line_index;	/* current line index */
{
  int	status = S_NORMAL;	/* function return status */
  char	token [ MAX_LINE ];	/* current token */

  while ( status != S_EOF )
  {
    /* check for end of line */
    if ( status == S_END_OF_LINE )
    {
      status = get_line ( data_file, line );
      *line_index = 0;
    }  /* if */

    if ( status == S_NORMAL )
    {
      status = get_token ( line, line_index, token );
      if ( strcmp ( item, token ) == 0 )  return ( S_NORMAL );
      if ( blank_line ( token ) != 0 )  return ( S_MISMATCH );
    }  /* if */
  }  /* while */
  return ( status );
}  /* consume */


/******************************************************************************/
/* This function returns the next token from line. */
get_token ( line, line_index, token )
char	line [];	/* source line */
int	*line_index;	/* character to start at */
char	token [];	/* next token */
{
  char	current;	/* current token character */
  int	index = 0;	/* array index of current token character */
  char	next;		/* next character from line */

  /* Skip leading spaces or blank lines. */
  while ( (line [ *line_index ] == ' ') || (line [ *line_index ] == '\n') )
    (*line_index)++;

  token [ index ] = '\0';
  /* Check for the end of line. */
  if ( line [ *line_index ] == '\0' )  return ( S_END_OF_LINE );

  /* Copy the token. */
  do
  {
    token [ index ] = line [ *line_index + index ];
    current = token [ index ];
    index++;
    next = line [ *line_index + index ];
  }
  while (( next != '\0') && ( next != '\n' ) && ( next != ' ' ) &&
      ((( current >= 'a' ) && ( current <= 'z' )) ||
      (( current >= 'A' ) && ( current <= 'Z' )) || ( current == '_' ) ||
      (( current >= '0' ) && ( current <= '9' ))) &&
      ((( next >= 'a' ) && ( next <= 'z' )) ||
      (( next >= 'A' ) && ( next <= 'Z' )) || ( next == '_' ) ||
      (( next >= '0' ) && ( next <= '9' ))));

  token [ index ] = '\0';
  *line_index += index;
  return ( S_NORMAL );
} /* get_token */


/******************************************************************************/
/* This function returns the next token form the data file. */
next_token ( data_file, line, line_index, token )
FILE	*data_file;	/* current data file */
char	line [];	/* current data file line */
int	*line_index;	/* current line index */
char	token [];	/* the next token */
{
  int	status;		/* function return status */

  do
  {
    status = get_token ( line, line_index, token );

    if ( status == S_END_OF_LINE )
    {
      status = get_line ( data_file, line );
      *line_index = 0;
      if ( status != S_NORMAL )  return ( status );
    }  /* if */
  }
  while ( token [ 0 ] == '\0' );
  return ( S_NORMAL );
}  /* next_token */


/******************************************************************************/
/* This function returns the next integer from line. */
get_integer ( line, line_index, integer )
char 	line [];  	/* the current line */
int 	*line_index;  	/* the current line index */
int	*integer;  	/* the next integer from line */
{
  int sign = 1;  /* the sign of the integer */
  int status = S_NO_INTEGER;  /* fuction return status */

  *integer = 0;

  /* ignore leading spaces */
  while (line [ *line_index ] == ' ')
    (*line_index)++;

  /* check for a negative sign */
  if (line [ *line_index ] == '-')
  {
    sign = -1;
    (*line_index)++;
  }  /* minus sign */

  /* traverse the number */
  while ((line [ *line_index ] >= '0') &&
    (line [ *line_index ] <= '9'))
  {
    (*integer) = (*integer) * 10 + (line [ (*line_index)++ ] - '0');
    status = S_NORMAL;
  }  /* traverse */

  *integer = sign * (*integer);
  return (status);
}  /* get_integer */


/******************************************************************************/
/* This function reads in the next integer if it is an integer. */
next_integer ( data_file, line, line_index, integer )
FILE	*data_file;	/* data file */
char	line [];	/* current data file line */
int	*line_index;	/* current line index */
int	*integer;	/* the next integer */
{
  int	status;		/* function return status */

  status = get_integer ( line, line_index, integer );
  if ( status == S_NORMAL )  return ( status );

  while ( line [ *line_index ] == '\0' )
  {
    status = get_line ( data_file, line );
    *line_index = 0;
    status = get_integer ( line, line_index, integer );
  }  /* while */
  return ( status );
}  /* next_integer */


/******************************************************************************/
/* This function returns the current line. */
get_line (data, line)
FILE *data;  /* the data file to input from */
char line [];  /* the next line from the data file */
{
  int c;  /* current character */
  int index;  /* line index */

  for (index = 0;
       (index < MAX_LINE - 1) && ((c = getc (data)) != EOF) && (c != '\n');
       ++index)
    line [ index ] = c;

  line [ index ] = '\0';  /* terminate line */

  if (c == EOF)  return (S_EOF);
  return (S_NORMAL);
}  /* get_line */


/******************************************************************************/
/* This function randomizes the random number generator based on user seed integer. */
user_randomize ()
{
  int  count;    /* loop counter */
  int  seed;     /* random number generator seed number */

  printf ( "\nWhat is the random number generator seed integer? " );
  scanf ( "%d", &seed );
  srand ( seed );
  printf ( "\n" );

  for ( count = 0; count < seed; count++ )
    rand ();    /* randomize */
}  /* user_randomize */


/******************************************************************************/
/* This function opens the specified file for reading. */
open_file (name, input_file)
char  name [];		/* filename */
FILE  **input_file;	/* file to open for reading */
{
  FILE  *fopen ();	/* file open function */

  name [ charidx ( ' ', name ) ] = '\0';  /* truncate after file name */

  *input_file = fopen (name, "r");
  if (*input_file == NULL)
  {
    printf ("WARNING: could not open the file '%s'.\n");
    return (S_OPEN_FAILED);
  }  /* if */

  return (S_NORMAL);
}  /* open_file */


/******************************************************************************/
/* This function prompts for the name of the data file. */
prompt_file_name ( data_file, prompt, file_name )
FILE	**data_file;	/* data file to open */
char	prompt [];	/* user prompt */
char	file_name [];	/* user specified filename */
{
  int   line_index;		/* line index for get_integer */

  do
  {
    printf ( "%s", prompt );
    scanf ( "%s", file_name );
    if ( file_name [ 0 ] == '\0' )
    {
      printf ( "\n*Info* Attempting to continue without requested file.\n" );
      return;
    }  /* if */
    else
      open_file ( file_name, data_file );
  } 
  while ( *data_file == NULL );
  printf ( "\n\n" );
}  /* prompt_file_name */


/******************************************************************************/
/* This function prints out all of the DNA bases. */
print_all_bases ( sequence, data_file )
t_sequence	*sequence;	/* entire sequence */
FILE		*data_file;	/* output data file */
{
  int	printed = 0;	/* Bases printed */
  long	seq_index;	/* sequence index */

  for ( seq_index = 1; seq_index <= (*sequence).length; seq_index++ )
  {
    if ( printed == 0 )  fprintf ( data_file, "%10d  ", seq_index );

    fprintf ( data_file, "%c", 
        dna_map [ (*sequence).base [ seq_index - 1 ].bits ] );
    printed++;

    /* Space every 10 bases. */
    if (( printed % 10 ) == 0 )  fprintf ( data_file, " " );

    if ( printed == 50 )
    {
      fprintf ( data_file, "\n" );
      printed = 0;
    }  /* if */
  }  /* for */
}  /* print_all_bases */


/******************************************************************************/
/* This function creates a sequence file. */
write_seq ( sequence )
t_sequence	*sequence;	/* entire sequence */
{
  FILE	*fopen ();		/* file open function */
  FILE  *view;                  /* Output file for sequence */


  /* Open the file for writing. */
  view = fopen ( (*sequence).filename, "w" );
  if ( view == NULL )
  {
    printf ( "*WARNING* unable to open output file '%s'\n.", 
        (*sequence).filename );
    return;
  }  /* if */

  /* Write out the header. */
  fprintf ( view, "..\n" );

  /* Write out the sequence. */
  print_all_bases ( sequence, view );

  fclose ( view );  /* close the output file */
}  /* write_seq */


/******************************************************************************/
/* This function randomly generates a DNA sequence. */
generate_seq ( sequence )
t_sequence	*sequence;	/* entire sequence */
{
  int	base;		/* randomly generated base */
  int   dna_bits;	/* bit map of DNA bases */
  int	index;		/* sequence base index */

  for ( index = 0; index < (*sequence).length; index++ )
  {
    base = ( ( rand () * 1.0 ) / MAX_RANDOM ) * 100.0;
    if ( base <= A_COMP_RANGE )
      dna_bits = A_BITS;
    else
      if ( base <= C_COMP_RANGE )
        dna_bits = C_BITS;
      else
        if ( base <= G_COMP_RANGE )
          dna_bits = G_BITS;
        else
          dna_bits = T_BITS;
      (*sequence).base [ index ].bits = dna_bits;
  }  /* for */
}  /* generate_seq */
