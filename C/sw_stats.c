

#include <stdio.h>

/* This program searches thru a list of files for the patterns specified. */
/* Additionally, subsets of sequences can be examined by coding, noncoding, */
/* and/or repeat sequences. */
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
#define S_SKIP          23      /* skip */

#define	TRUE		1	/* Boolean flags */
#define FALSE		2

#define	MAX_BASES	100000	/* maximum number of sequence bases */

#define MAX_COMPONENTS	5000	/* maximum components per sequence file */

#define MAX_HISTO_CATEGORY 75   /* maximum histogram length for classification category */

#define MAX_HISTO_LINE  16      /* maximum histograms on one line */

#define MAX_HISTO_PATTERN 75    /* maximum histogram length for search patterns */

#define MAX_HISTOGRAM	202	/* maximum elements in histogram array */

#define MAX_LINE 	132	/* maximum line length */

#define	MAX_NAME	9	/* maximum pattern name length */

#define	MAX_PATTERN	1000	/* maximum search pattern size */

#define	MAX_PATTERNS	1000	/* maximum number of patterns */

#define	MAX_PER_LINE	70	/* maximum bases printed per line */

#define MAX_START	10	/* maximum spaces before start of feature */

#define MAX_UNITS	1	/* maximum unit length to search up to */

#define	MIN_REPEAT	10	/* minimum direct repeat length */

#define	REPEAT_WINDOW	50	/* maximum window size for direct repeats */

#define AFTER_BASES	10	/* bases to print after a pattern match */
#define BEFORE_BASES	10	/* bases to print before a pattern match */

#define H_HISTOGRAM     1       /* starndard histogram element size */
#define H_SEQUENCE      1       /* sequence subcomponent element size */
#define H_FILE_LEN      1000    /* file length histogram element size */

#define	P_SEQUENCE	1	/* search for unique sequence only */
#define	P_SUBUNIT	2	/* repeat pattern subunit minimum times */
#define P_REPEAT	3	/* direct repeat pattern */
#define P_PALINDROME	4	/* palindrome pattern */

#define CR		13	/* carriage return */

#define	P		0.1	/* Pattern search probablity */

/* Sequence component classification categories. */
#define	UNSPECIFIED	0	/* unspecified region */
#define SIGNAL          1       /* signal peptide */
#define PROPEP          2       /* propeptide region */
#define DOMAIN          3       /* homology domain */
#define TOTAL		4	/* entire sequence(s) */
#define	PATTERN		5	/* pattern matched region */

#define	MAX_AA_TYPES	PATTERN + 1	/* AA classification categories */

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


/* Swiss sequence data file header information. */
typedef struct {
  char	definition [ MAX_LINE ];	/* sequence definition line */
  char	species [ MAX_LINE ];		/* */
  long	seq_length;			/* sequence length */
} t_sw_header;


/* Histogram of lengths. */
typedef struct {
  long	lengths [ MAX_HISTOGRAM ];	/* histogram of lengths */
} t_histogram;


/* sequence component */
typedef struct {
  long	begin;				/* start of component */
  long	end;				/* end of component */
  int	type;				/* type of component */
  char	description [ MAX_LINE ];	/* component description */
} t_component;

/* sequence components table */
typedef struct {
  long		total;				/* number of components */
  long		active;				/* current component */
  char		file_name [ MAX_LINE ];		/* sequence filename */
  t_component	atom [ MAX_COMPONENTS ];	/* each component */
} t_components;



/* DNA or RNA sequence */
typedef struct {
  long		length;			/* length of sequence */
  char		filename [ MAX_LINE ];	/* sequence filename */
  char	        aa [ MAX_BASES ];	/* Each base of sequence */
} t_sequence;


/* Search pattern information */
typedef struct {
  int		length;			/* pattern length */
  int		minimum;		/* minimum number of repeats */
  int		type;			/* type of pattern */
  double	expected;		/* number of hits expected by random */
  float		match;			/* percentage of match required */
  char		name [ MAX_NAME ];	/* pattern name */
  char	        aa [ MAX_PATTERN ];	/* search pattern */
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


/* table of sequence statistics */
typedef struct {
  long              total_sequences;            /* # of sequences processed */
  t_histogram       file_lengths;		/* sequence lengths */
  t_composition     comp [ MAX_AA_TYPES ];	/* DNA category composition */
  t_histogram       lengths [ MAX_AA_TYPES ];   /* component lengths */
} t_seq_stats;


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
static	char	*aa_types [] = {
  "Unspecified", "SIGNAL PEPTIDE", "PROPEPTIDE",  "DOMAIN",
  "All sequences", "Patterns" };


/* DNA component category classification type masks. */
static	int	comp_types [] = {
  0, 0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80, 0x100, 0x200, 0x400, 0x800 };

/* DNA component category classification type names. */
static	char	*comp_names [] = {
  "GEN", "c5UT", "CODE", "c3UT", "g5UT", "EXON",
  "IVS", "Code", "g3UT", "REP",  "ALL", "PAT",  "SPL" };


/******************************************************************************/

main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  FILE	*file_names = NULL;		/* list of file names to process */
  char	file_of_files [ MAX_LINE ];	/* name of file of file names */
  FILE	*patterns_file;			/* search pattern list */
  char	patterns_name [ MAX_LINE ];	/* name of patterns file */

  prompt_file_name ( &file_names,
      "What is the name of the file of file names? ", file_of_files );

  prompt_file_name ( &patterns_file, "What is the search patterns file name? ",
      patterns_name );

  process_files ( file_names, patterns_file, file_of_files );

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
    printf ( "*WARN* '%s%s' shortended to %d characters.\n", (MAX_LINE - 1));

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
FILE	*data_file;	/* Swiss data file */
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
/* This function sets the mask bit(s) for the current sequence character. */
set_mask ( masks, character, mask )
int		masks [];	/* bit representation look up table */
char		character;	/* current sequence characeter */
unsigned	*mask;		/* conservative substitution mask bits */
{
  if ( (character < 'A') || (character > 'Z') )
    *mask = 0;
  else
    *mask = masks [ character - 'A' ];
  if ( character == '.' )  *mask = NO_BITS;
}  /* set_mask */


/******************************************************************************/
/* This fuction reads from file data until two adjacent periods are found. */
find_data (data)
FILE *data;  /* data file */
{
  char current;  /* current character */
  char previous = ' ';  /* previous character */

  while (((current = getc (data)) != EOF)
    && ((previous != '.') || (current != '.')))
    previous = current;
}  /* find_data */


/******************************************************************************/
/* This function returns the next data character from the data file. */
next_data_character (data, line, line_index, character)
FILE *data;  /* sequence data file */
char line [];  /* current data file line */
int *line_index;  /* current line index */
char *character;  /* next data character */
{
  int status = S_NORMAL;  /* function return status */
  int integer;  /* beginning of line position */

  while (((line [ *line_index ] == ' ') ||
    (line [ *line_index ] == '\0')) && (status != S_EOF))
  {
    /* ignore leading spaces */
    while (line [ *line_index ] == ' ')
      (*line_index)++;

    /* check for end of line */
    if (line [ *line_index ] == '\0')
    {
      status = get_line ( data, line );
      capitalize ( line );
      *line_index = 0;  /* reset */
      get_integer ( line, line_index, &integer );  /* ignore position */
    }  /* end of line */
  }  /* no data */

  if (status != S_NORMAL)
  {
    *character = '.';
    return (status);
  }  /* if */
  *character = line [ (*line_index)++ ];
  return (S_NORMAL);
}  /* next_data_character */


/******************************************************************************/
/* This function reads in the Swiss header up to the features section. */
read_sw_header ( sw_file, sw_header )
FILE		*sw_file;	/* Swiss sequence file */
t_sw_header	*sw_header;	/* Swiss data file header */
{
  int	length;			/* locus line sequence length */
  char	line [ MAX_LINE ];	/* current header line */
  int	line_index;		/* current position in line */
  int	line_start;		/* first non-blank character in a line */
  int	status = S_NORMAL;	/* function return status */
  char	token [ MAX_LINE ];	/* current token */

  /* Initialize the Swiss header table. */
  (*sw_header).definition [ 0 ] = '\0';
  (*sw_header).seq_length = 0;
  (*sw_header).species [ 0 ] = '\0';

  /* Process the Swiss file header. */
  status = get_line ( sw_file, line );
  line_index = 0;
  while ( status == S_NORMAL )
  {
    /* check the first token of the line. */
    if ( status == S_NORMAL )
    {
      status = next_token ( sw_file, line, &line_index, token );
      if (( token [ 0 ] == '.') && ( line [ line_index ] == '.' ))
        return ( S_DATA );
    }  /* if */

    line_start = 0;
    while ( line [ line_start ] == ' ' )  line_start++;

    if (( status == S_NORMAL ) && ( line_start <= MAX_START ))
      switch ( token [ 0 ] )
      {
        case 'D':
          if ( strcmp ( "DE", token ) == 0 )
            strcopy ( (*sw_header).definition, line );
          break;

        case 'O':
          if ( strcmp ( "OS", token ) == 0 )
          {
            strcopy ( (*sw_header).species, line );
            if ( line [ stridx ( "..", line ) ] != '\0' )  return ( S_DATA );
            return ( S_NORMAL );
          }  /* if */
          break;

        default:
          break;
      }  /* switch */
  }  /* while */

  return ( status );
}  /* read_sw_header */


/******************************************************************************/
/* This function reads in the Swiss file header features. */
read_sw_features ( sw_file, components )
FILE		*sw_file;	/* Swiss sequence data file */
t_components	*components;	/* sequence components table */
{
  int	genomic = TRUE;		/* genomic sequence flag */
  char	line [ MAX_LINE ];	/* current features line */
  int	line_index;		/* current position in line */
  int	line_start;		/* first non-blank character in a line */
  int	status = S_NORMAL;	/* function return status */
  char	token [ MAX_LINE ];	/* current token */


  (*components).total = 0;
  /* Process the features table. */
  status = get_line ( sw_file, line );
  line_index = 0;

  while ( status == S_NORMAL )
  {
    if ( line [ stridx ( "..", line ) ] != '\0' )  return ( S_DATA );

    /* Check the first token of the line. */
    if ( status  == S_NORMAL )
      status = next_token ( sw_file, line, &line_index, token );

    if ( line [ stridx ( "..", line ) ] != '\0' )  return ( S_DATA );

    if ( ( status == S_NORMAL ) && ( strcmp ( "FT", token ) == 0 ) )
      status = next_token ( sw_file, line, &line_index, token );
    else status = S_SKIP;

    if ( status == S_NORMAL )
      switch ( token [ 0 ] )
      {
        case 'D':
          if ( strcmp ( "DOMAIN", token ) == 0 )
            status = location ( DOMAIN, sw_file, line, &line_index,
                components );
          break;

        case 'P':
          if ( strcmp ( "PROPEP", token ) == 0 )
            status = location ( PROPEP, sw_file, line, &line_index,
                components );
          break;

        case 'S':
          if ( strcmp ( "SIGNAL", token ) == 0 )
            status = location ( SIGNAL, sw_file, line, &line_index,
                components );
          break;

        default:
          status = get_line ( sw_file, line );
          line_index = 0; 
          break; 
      }  /* switch */

    if ( status != S_EOF )  status = S_NORMAL;
  }  /* while */

  return ( status );
}  /* read_sw_features */


/******************************************************************************/
/* This function searches the sequence table for the current pair. */
find_pair ( begin, end, components, comp_index )
long		begin;		/* start of component */
long		end;		/* end of component */
t_components	*components;	/* sequence component table */
int		*comp_index;	/* sequence component table index */
{
  *comp_index = 0;
  if ( (*components).total == 0 )  return ( S_NOT_FOUND );

  while (( (*comp_index) < (*components).total ) &&
      ( begin > (*components).atom [ *comp_index ].end ))
    (*comp_index)++;

  if ( (*comp_index) == (*components).total )  return ( S_NOT_FOUND );

  if ( end < (*components).atom [ *comp_index ].begin )  return ( S_NOT_FOUND );

  /* Begin's match */
  if ( begin == (*components).atom [ *comp_index ].begin )
  {
    if ( end == (*components).atom [ *comp_index ].end )  return ( S_FOUND );
    if ( end < (*components).atom [ *comp_index ].end )
      return ( S_BEGIN_COMP );
    else  return ( S_AFTER_COMP );
  }  /* if */

  /* End's match */
  if ( end == (*components).atom [ *comp_index ].end )
  {
    if ( begin > (*components).atom [ *comp_index ].begin )
      return ( S_END_COMP );
    else  return ( S_BEFORE_COMP );
  }  /* if */

  if (( begin < (*components).atom [ *comp_index ].begin ) &&
      ( end > (*components).atom [ *comp_index ].end ))
    return ( S_SUPER_COMP );

  if (( begin > (*components).atom [ *comp_index ].begin ) &&
      ( end > (*components).atom [ *comp_index ].end ))
    return ( S_END_OVERLAP );

  if (( begin < (*components).atom [ *comp_index ].begin ) &&
      ( end < (*components).atom [ *comp_index ].end ))
    return ( S_BEGIN_OVERLAP );

  if (( begin > (*components).atom [ *comp_index ].begin ) &&
      ( end < (*components).atom [ *comp_index ].end ))
    return ( S_SUB_COMP );

  return ( S_ERROR );
}  /* find_pair */


/******************************************************************************/
/* This function processes a location list. */
location ( type, sw_file, line, line_index, components )
int		type;		/* component type */
FILE		*sw_file;	/* Swiss data file */
char		line [];	/* current data file line */
int		*line_index;	/* current position in line */
t_components	*components;	/* sequence component table */
{
  long	begin;			/* first of coordinate pair */
  long	end;			/* second of coordinate pair */
  int	index;			/* token index */
  int	status = S_NORMAL;	/* function return status */
  char	token [ MAX_LINE ];	/* current line token */


  status = next_token ( sw_file, line, line_index, token );

  if (( token [ 0 ] == '<' ) && ( status == S_NORMAL ))
    status = next_token ( sw_file, line, line_index, token );
  if ( status != S_NORMAL )  return ( status );

  /* Get the first number of the coordinate pair. */
  index = 0;
  if ( status != S_NORMAL )  return ( status );
  status = get_integer ( token, &index, &begin );
  if ( status != S_NORMAL )  return ( status );

  /* Check for '>' */
  if ( line [ *line_index ] == '>' )  (*line_index)++;

  /* Get the second number of the coordinate pair. */
  status = next_token ( sw_file, line, line_index, token );
  if ( token [ 0 ] == '?' )  return ( S_NORMAL );
  index = 0;
  if ( status == S_NORMAL )
    status = get_integer ( token, &index, &end );
  if ( status != S_NORMAL )  return ( status );

  status = add_pair ( begin, end, type, components );

  return ( status );
}  /* location */


/******************************************************************************/
/* This function adds a coordinate pair to the sequence. */
add_pair ( begin, end, type, components )
long		begin;		/* start of component */
long		end;		/* end of component */
int		type;		/* component type */
t_components	*components;	/* sequence components table */
{
  int	comp_index;	/* sequence component table index */
  long	index;		/* components table index */
  long	new_begin = 0;	/* split component old begining */
  long	new_end = 0;	/* split component new end */
  int	new_type = 0;	/* split component old type */
  int	status;		/* function return status */

  if ( end < begin )
  {
    printf ( "*WARN* file '%s' (add_pair) begin = %d > end = %d.\n", 
        (*components).file_name, begin, end );
    return ( S_BAD_PAIR );
  }  /* if */

  if ( (*components).total == MAX_COMPONENTS )
  {
    printf ( "*WARN* Components table overflow, file '%s'.\n", 
        (*components).file_name );
    return ( S_TABLE_OVERFLOW );
  }  /* if */

  /* search the sequence component table for the coordinate pair. */
  status = find_pair ( begin, end, components, &comp_index );

  if ( status == S_ERROR )
  {
    printf ( "*WARN* error returned from find_pair, file '%s'.\n",
        (*components).file_name );
    return ( S_NORMAL );
  }  /* if */

  (*components).active = comp_index;
  if ( status == S_FOUND )
  {
    (*components).atom [ comp_index ].type = type;
    return ( S_NORMAL );
  }  /* if */

  /* Insert a blank record into the components table. */
  if ( status != S_AFTER_COMP )
  {
    (*components).total++;
    if ( (*components).total > 0 )
      for ( index = (*components).total; index > (*components).active; 
          index-- )
      {
        (*components).atom [ index ].begin =
            (*components).atom [ index - 1 ].begin;
        (*components).atom [ index ].end =
            (*components).atom [ index - 1 ].end;
        (*components).atom [ index ].type =
            (*components).atom [ index - 1 ].type;
        strcopy ( (*components).atom [ index ].description, 
            (*components).atom [ index - 1 ].description );
    }  /* for */
  }  /* if */

  switch ( status )
  {
    case S_AFTER_COMP:		/* begin == .begin && end > .end */
      (*components).atom [ comp_index ].type = type;
      new_begin = (*components).atom [ comp_index ].end + 1;
      new_end = end;
      new_type = type;
      break;

    case S_BEFORE_COMP:		/* begin < .begin  && end == .end */
      end = (*components).atom [ (*components).active ].begin - 1;
      break;

    case S_BEGIN_COMP:		/* begin == .begin && end < .end */
      (*components).atom [ (*components).active + 1 ].begin = end + 1;
      break;

    case S_EMPTY_TABLE:  break;

    case S_END_COMP:		/* begin > .begin & end == .end */
      comp_index++;		/* insert after current */
      (*components).atom [ (*components).active ].end = begin - 1;
      break;

    case S_BEGIN_OVERLAP:	/* begin < .begin && end < .end */
      new_begin = (*components).atom [ comp_index ].begin;
      new_end = end;
      new_type = type;
      end = (*components).atom [ comp_index ].begin - 1;
      break;

    case S_END_OVERLAP:		/* begin > .begin && end > .end */
      new_end = end;
      end = (*components).atom [ comp_index ].end;
      (*components).atom [ comp_index ].end = begin - 1;
      new_begin = end + 1;
      new_type = type;
      comp_index++;		/* insert after current */
      break;

    case S_NORMAL:  break;

    case S_NOT_FOUND:  break;

    case S_SUB_COMP:		/* begin > .begin && end < .end */
      new_begin = (*components).atom [ (*components).active + 1 ].begin;
      new_end = begin - 1;
      new_type = (*components).atom [ (*components).active + 1 ].type;
      (*components).atom [ (*components).active + 1 ].begin = end + 1;
      break;

    case S_SUPER_COMP:		/* begin < .begin && end > .end */
      new_type = type;
      new_begin = (*components).atom [ (*components).active + 1 ].end + 1;
      new_end = end;
      end = (*components).atom [ (*components).active + 1 ].begin - 1;
      break;

    default:
      printf ( "*WARN* file '%s': unknown status from find_pair '%d'.\n", 
          (*components).file_name, status );
      break;
  }  /* switch */

  /* Initialize the new record. */
  if ( status != S_AFTER_COMP )
  {
    (*components).atom [ comp_index ].begin             = begin;
    (*components).atom [ comp_index ].end               = end;
    (*components).atom [ comp_index ].type              = type;
    (*components).atom [ comp_index ].description [ 0 ] = '\0';
  }  /* if */

  /* Split the old component record into two parts. */
  if (( status == S_SUB_COMP ) || ( status == S_SUPER_COMP ) ||
      ( status == S_BEGIN_OVERLAP ) || ( status == S_END_OVERLAP ) ||
      ( status == S_AFTER_COMP ))
    add_pair ( new_begin, new_end, new_type, components );

  return ( S_NORMAL );
}  /* add_pair */


/******************************************************************************/
/* This function reads a pttern from the patterns file. */
get_pattern ( patterns_file, pattern )
FILE		*patterns_file;		/* file of search patterns */
t_pattern	*pattern;		/* pattern information */
{
  int		index;			/* token index */
  char		line [ MAX_LINE ];	/* current line of patterns file */
  int		line_index = 0;		/* current line index */
  unsigned	mask;			/* bit representation */
  int		status = S_NORMAL;	/* function return status */
  char		token [ MAX_LINE ];	/* current line token */

  (*pattern).match = 0.0;

  status = get_line ( patterns_file, line );
  if ( status != S_NORMAL )  return ( status );

  /* Find a search pattern. */
  token [ 0 ] = '\0';
  while (( status == S_NORMAL ) && ( token [ 0 ] == '\0' ) || 
      ( token [ 0 ] == '!' ))
    status = next_token ( patterns_file, line, &line_index, token );
  if ( status != S_NORMAL )  return ( status );

  /* Crack the pattern line. */
  strcopy ( (*pattern).name, token );
  (*pattern).minimum = 1;

  /* Ignore the offset */
  get_integer ( line, &line_index, &index );

  /* Get the beginning of the pattern. */
  status = get_token ( line, &line_index, token );

  if ( token [ 0 ] != '(' )
    (*pattern).type = P_SEQUENCE;
  else
  {
    (*pattern).type = P_SUBUNIT;
    status = get_token ( line, &line_index, token );
  }  /* else */
  (*pattern).length = 0;
  if ( status != S_NORMAL )  return ( status );

  /* Bit map the pattern. */
  index = 0;
  capitalize ( token );
  while ( token [ index ] != '\0' )
    (*pattern).aa [ (*pattern).length++ ] = token [ index++ ];
  
  if ( (*pattern).type == P_SEQUENCE )  return ( status );

  /* Get the minimum repeat count. */
  status = get_token ( line, &line_index, token );
  if ( token [ 0 ] != ')' )
  {
    printf ( "*WARN* pattern error, ')' expected: '%s'.\n", line );
    return ( status );
  }  /* if */
  status = get_token ( line, &line_index, token );
  if ( token [ 0 ] != '{' )
  {
    printf ( "*WARN* pattern error, '{' expected: '%s'.\n", line );
    return ( status );
  }  /* if */
  status = get_integer ( line, &line_index, &((*pattern).minimum) );
  return ( status );
}  /* get_pattern */


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
  index = length / size;

  /* Check for length past the end of the histogram. */
  if ( index < MAX_HISTOGRAM - 1 )
    (*histogram).lengths [ index ]++;
  else
    (*histogram).lengths [ MAX_HISTOGRAM - 1 ]++;
}  /* add_length */


/******************************************************************************/
/* This function gets the component type(s) of a range. */
get_types ( components, start, end, types )
t_components	*components;	/* sequence components table */
long		start;		/* start of selected range */
long		end;		/* end of selected range */
int		*types;		/* types of components spanned */
{
  int	comp_index;	/* components table index */
  long	seq_index;	/* sequence index */
  int	status;		/* function return status */

  *types = 0;
  /* Traverse the slected sequence range. */
  seq_index = start;
  while ( seq_index <= end )
  {
    /* Search the components table for the current position. */
    status = find_pair ( seq_index, seq_index, components, &comp_index );

    /* Check if within a sequence component. */
    if ( status != S_EMPTY_TABLE )
    {
      if (( seq_index >= (*components).atom [ comp_index ].begin ) &&
          ( seq_index <= (*components).atom [ comp_index ].end ))
      {
        (*types) |= comp_types [ (*components).atom [ comp_index ].type ];
        seq_index = (*components).atom [ comp_index ].end + 1;
      }  /* if */
      else  seq_index++;
    }  /* if */
    else  return;
  }  /* while */
}  /* get_types */


/******************************************************************************/
/* This function prints out the component type(s). */
print_types ( types, data_file )
int	types;		/* types of components */
FILE	*data_file;	/* file to write types to */
{
  int	first = TRUE;	/* Boolean flag for first printed */
  int	index;		/* DNA classification types index */

  if ( types == UNSPECIFIED )
  {
    fprintf ( data_file, "%s", comp_names [ UNSPECIFIED ] );
    return;
  }  /* if */

  for ( index = 0; index < MAX_AA_TYPES; index++ )
    if (( types & comp_types [ index ] ) != 0 )
    {
      if ( first == FALSE )  fprintf ( data_file, ", " );
      fprintf ( data_file, "%s", comp_names [ index ] );
      first = FALSE;
    }  /* if */
}  /* print_types */


/******************************************************************************/
/* This function prints out a pattern match. */
print_match ( sequence, pattern, components, index, count )
t_sequence	*sequence;	/* entire sequence */
t_pattern	*pattern;	/* current pattern */
t_components	*components;	/* sequence components table */
long		index;		/* start of comparison index */
int		count;		/* number of subpattern matches */
{
  int	printed;	/* number of bases printed on current line */
  long	sub_index;	/* pattern index */
  int	types;		/* types of components spanned by match */

  sub_index = index - BEFORE_BASES;	/* include surrounding bases */
  printed = 0;
  do 
  {
    /* Separate matched sequence from surrounding bases. */
    if ( sub_index == index )  fprintf ( (*pattern).pat_file, "  " ); 
    if ( sub_index == index + (*pattern).length * count )
    {
      fprintf ( (*pattern).pat_file, "  " );

      /* If repeated subunit search, then space match to maximum width. */
      if (( (*pattern).type == P_SUBUNIT ) &&
          ( printed < MAX_PER_LINE - AFTER_BASES ))
        while ( printed < MAX_PER_LINE - AFTER_BASES )
        {
          fprintf ( (*pattern).pat_file, " " );
          printed++;
        }  /* if */
      }  /* if */

    /* Print the base or a space. */
    if (( sub_index < 0 ) || ( sub_index >= (*sequence).length ))
      fprintf ( (*pattern).pat_file, " " );
    else
      fprintf ( (*pattern).pat_file, 
          "%c", (*sequence).aa [ sub_index ] );
    printed++;

    /* Print the partial line trailer. */
    if (( printed == MAX_PER_LINE - AFTER_BASES ) &&
        ( sub_index < index + (*pattern).length * count - 1 ))
    {  
      fprintf ( (*pattern).pat_file, "            " );
      printed = MAX_PER_LINE;
    }  /* if */


    /* Print the pattern information on end of lines. */
    if (( printed == MAX_PER_LINE ) ||
        ( sub_index == index + (*pattern).length * count - 1 + AFTER_BASES))
    {
      fprintf ( (*pattern).pat_file, "    %s(%4d)  %6d  %s  ", 
          (*pattern).name, count, index + 1, (*sequence).filename );
      get_types ( components, index, index + (*pattern).length * count - 1,
          &types );
      print_types ( types, (*pattern).pat_file );
      fprintf ( (*pattern).pat_file, "\n" );

      /* Print the leader for the next line. */
      if ( sub_index < index + (*pattern).length * count - 1 + AFTER_BASES )
      {
        fprintf ( (*pattern).pat_file, "            " );
        printed = BEFORE_BASES;
      }  /* if */
      else  printed = 0;
    }  /* if */
    sub_index++;
  }
  while ( sub_index <= index + (*pattern).length * count - 1 + AFTER_BASES );
}  /* print_match */


/******************************************************************************/
/* This function searches a sequence for the pattern. */
find_pattern ( sequence, pattern, components )
t_sequence	*sequence;	/* entire sequence */
t_pattern	*pattern;	/* current pattern */
t_components	*components;	/* sequence components table */
{
  int	count;		/* number of subpattern matches */
  long	index;		/* start of comparison index */
  long	sub_index;	/* pattern index */

/* printf ("\nPattern '%s', minimum = %3d, expected = %6.4f\n", 
(*pattern).name, (*pattern).minimum, (*pattern).expected); */

  /* Scan the entire sequence. */
  for ( index = 0; index < 
      (*sequence).length - (*pattern).length * (*pattern).minimum + 1; index++ )
  {
    count = 0;
    do
    {
      sub_index = 0;
      while ((( (*sequence).aa
          [ index + (*pattern).length * count + sub_index ] ==
          (*pattern).aa [ sub_index ] ) ) &&
          ( sub_index <= (*pattern).length ))  sub_index++;
      if ( sub_index >= (*pattern).length )  count++;
    }
    while (( sub_index >= (*pattern).length ) &&	/* match */
        ( (*pattern).type == P_SUBUNIT ) &&		/* repeat */
        ( index + (*pattern).length * (count + 1) - 1 <= (*sequence).length ));

    /* Report any matches. */
    if (( count < (*pattern).minimum ) && ( (*pattern).type == P_SUBUNIT ))
    {
      if ( count > 0 )  index += (*pattern).length * count - 1;
      count = 0;
    }  /* if */

    if ( count > 0 )
    {
      print_match ( sequence, pattern, components, index, count );

      /* Mark the pattern match area. */
/*      add_pair ( index, index + (*pattern).length * count - 1, PATTERN, 
          components ); */

      add_length ( index, index + (*pattern).length * count - 1, 
          &((*pattern).histo), H_HISTOGRAM );

      index += (*pattern).length * count - 1;
    }  /* if */
  }  /* for */
}  /* find_pattern */


/******************************************************************************/
/* This function processes a Swiss sequence data file. */
sw_scan_file ( patterns, file_name, data_file, seq_stats )
t_patterns	*patterns;		/* current patterns to search for */
char		file_name [];		/* sequence file name */
FILE		*data_file;		/* Swiss sequence file */
t_seq_stats	*seq_stats;		/* sequence statistics table */
{
  static  	t_components	components;	/* sequence components table */
  static	t_sequence	sequence;	/* entire sequence data */

  t_sw_header	sw_header;		/* Swiss data file header */
  int		index;			/* current pattern */
  char		line [ MAX_LINE ];	/* current sequence line */
  int		line_index;		/* current line index */
  char		seq_char;		/* current sequence character */
  int		status;			/* function return status */

  file_name [ charidx ( ' ', file_name ) ] = '\0';  /* truncate name at 1st space */
  strcopy ( sequence.filename, file_name );
  strcopy ( components.file_name, file_name );

  components.total = 0;

  /* Process the Swiss file header. */
  status = read_sw_header ( data_file, &sw_header );

  if ( status == S_NORMAL )
    status = read_sw_features ( data_file, &components );

  /* Find the sequence data. */
  if ( status == S_NORMAL )
    find_data ( data_file );
  if ( status == S_DATA )  status = S_NORMAL;

  /* Read in the sequence. */
  sequence.length = 0;
  line [ 0 ] = '\0';
  line_index = 0;
  while ( status == S_NORMAL )
  {
    /* Get the next sequence character. */
    status = next_data_character ( data_file, line, &line_index, &seq_char );

    if ( status == S_NORMAL )
    {
      sequence.aa [ sequence.length++ ] = seq_char;

      if ( sequence.length == MAX_BASES - 1 )
      {
        printf ( "*WARN* Sequence '%s' is too long.\n", file_name );
        status = S_TABLE_OVERFLOW;
      }  /* if */
    }  /* if */
  }  /* while */

  /* Compute the sequence statistics for this sequence. */
  count_bases ( &sequence, &components, seq_stats );

/*  print_components ( &components ); */

/* printf ("\n(%s) length = %d\n", sequence.filename, sequence.length ); */
}  /* sw_scan_file */


/******************************************************************************/
/* This function processes a list of sequence files. */
process_files ( file_of_files, patterns_file, suffix )
FILE	*file_of_files;		/* list of file names to process */
FILE	*patterns_file;		/* file of search patterns */
char	suffix [];		/* pattern output suffix */
{
  FILE		*data_file;			/* sequence file */
  char		file_name [ MAX_LINE ];		/* file name of the data file */
  FILE          *fopen ();                      /* file open routine */
  int		index;				/* composition index */
  char		line [ MAX_LINE ];		/* current line of file_of_files */
  int		line_index;			/* current line index */
  t_patterns	patterns;			/* search patterns table */
  char		patterns_name [ MAX_LINE ];	/* output patterns file name */
  t_seq_stats	seq_stats;			/* sequence statistics table */
  int		status = S_NORMAL;		/* function return status */

  /* Initialize the sequence statistics table. */
  init_stats ( &seq_stats );

  suffix [ charidx ( '.', suffix ) ] = '\0';

  /* Process the patterns file. */
  patterns.total = 0;
  if ( patterns_file != NULL )
  {
    find_data ( patterns_file );
    while (( status != S_EOF ) && ( status != S_TABLE_OVERFLOW ))
    {
      status = get_pattern ( patterns_file,
        &(patterns.pattern [ patterns.total ]) );
      if ( status == S_NORMAL )  
      {
        strcpy ( patterns_name, patterns.pattern [ patterns.total ].name );
        concatenate ( patterns_name, "." );
        concatenate ( patterns_name, suffix );
        patterns.pattern [ patterns.total ].pat_file =
            fopen ( patterns_name, "w" );
        for ( index = 0; index < MAX_HISTOGRAM; index++ )
          patterns.pattern [ patterns.total ].histo.lengths [ index ] = 0;
        patterns.total++; 
      }  /* if */

      if ( patterns.total == MAX_PATTERNS - 1 )
      {
        printf ( "*WARN* Too many search patterns, first %d used.\n",
            MAX_PATTERNS );
        status = S_TABLE_OVERFLOW;
      }  /* if */
    }  /* while */
  }  /* if */
  status = S_NORMAL;

  print_patterns ( &patterns ); 

  /* Process the file of file names. */
  while ( status != S_EOF )
  {
    /* Get the next line from the file of file names. */
    status = get_line ( file_of_files, line );
    line_index = 0;
    while ( line [ line_index ] == ' ' )  line_index++;

    if (( status == S_NORMAL ) && 
        ( blank_line ( &(line [ line_index ]) ) != 0 ))
    {
/* printf ( "opening '%s'\n", &(line [ line_index ]) ); */

      status = open_file ( &(line [ line_index ]), &data_file );

      if ( status == S_NORMAL )
      {
        sw_scan_file ( &patterns, &(line [ line_index ]), data_file, 
            &seq_stats );
        fclose ( data_file );  /* close the data file */
      }  /* if */
    }  /* if */
  }  /* while */

  /* Print out the pattern match lengths histograms. */
/*  for ( index = 0; index < patterns.total; index++ )
    print_histogram ( patterns.pattern [ index ].name,
        &(patterns.pattern [ index ].histo) ); */

  print_pattern_histograms ( &patterns );

  /* Print out the sequence(s) statistics. */
  print_stats ( &seq_stats );

  /* Close the pattern output files. */
  for ( index = 0; index < patterns.total; index++ )
    fclose ( patterns.pattern [ index ].pat_file );

}  /* process_files */


/******************************************************************************/
/* This function prints out the components table. */
print_components ( components )
t_components	*components;	/* sequence components table */
{
  int	index = 0;	/* components table index */

  printf ( "\nThe components table:\n" );
  printf ( "\nTotal components = %d.\n\n", (*components).total );
  printf ( "Begin	End	Type\n" );

  while ( index < (*components).total )
  {
    printf ( "%6d   %6d     ", (*components).atom [ index ].begin,
        (*components).atom [ index ].end );

/*    print_types ( comp_types [ (*components).atom [ index ].type ] ); */

    printf ( "(%d)", (*components).atom [ index ].type );

    printf ( "\n" );
    index++;
  }  /* while */
  printf ( "\n" );
}  /* print_components */


/******************************************************************************/
/* This function prints out the histograms for the search patterns. */
print_pattern_histograms ( patterns )
t_patterns  *patterns;    /* search patterns table */
{
  int  end;      /* index of last pattern to print on line */
  int  index;    /* */
  int  pat;      /* pattern index */
  int  slice;    /* current slice of histograms that fit on one line */


  slice = 0;
  printf ( "\0" );
  do
  {
    /* Print out the histogram titles. */
    for ( pat = slice; pat < MAX_HISTO_LINE; pat++ )
      printf ( "%s\t", (*patterns).pattern [ pat ].name );
    printf ( "\n" );

    /* Print out each item for all of the histograms in this slice. */
    for ( index = 0; index <= MAX_HISTO_PATTERN + 1; index++ )
    {
      if ( slice + MAX_HISTO_LINE >= (*patterns).total )
        end = (*patterns).total;
      else
        end = slice + MAX_HISTO_LINE;

      /* Print out one line of the histograms slice. */
/*      for ( pat = slice; pat <= end; pat++ )
        print_histo ( &((*patterns).pattern [ pat ].histo), index, pat, end,
            MAX_HISTO_PATTERN ); */
    }  /* for */

    slice += MAX_HISTO_LINE;    /* process the next slice of histograms */
  }
  while ( slice < (*patterns).total );
}  /* print_pattern_histograms */


/******************************************************************************/
/* This function prints out a histogram table item. */
print_histo ( histogram, index, line_index, line_last, max_index )
t_histogram  *histogram;    /* the histogram to print */
int          index;         /* histogram index */
int          line_index;    /* different histograms on one line */
int          line_last;     /* last histogram on the line index */
int          max_index;     /* largest histogram index to print */
{
  int   end_index;        /* index into histogram past end of printed range */
  long  end_total = 0;    /* total of histogram past end of printed range */

  /* Print out histogram index on the beginning of the line. */
  if ( line_index == 0 )  printf ( "%4d", index );

  if ( index <= max_index )
    printf ( "\t%7d", (*histogram).lengths [ index ] );
  else
  {
    /* Total up the unprinted region of the histogram. */
    for ( end_index = max_index + 1; end_index < MAX_HISTOGRAM; end_index++ )
      end_total += (*histogram).lengths [ index ];
    printf ( "\t%7d", end_total );
  }  /* else */

  if ( line_index == line_last )  printf ( "\n" );
}  /* print_histo */


/******************************************************************************/
/* This function prints out a histogram table. */
print_histogram ( name, histogram )
char         name [];     /* histogram name */
t_histogram  *histogram;  /* the histogram to print */
{
  int  index;  /* histogram index */

  printf ( "\"Histogram for %s:\"\n", name );

  for ( index = 0; index < MAX_HISTOGRAM - 1; index++ )
/*    if ( (*histogram).lengths [ index ] > 0 ) */
      printf ( "%3d  %6d\n", index, (*histogram).lengths [ index ] );

  if ( (*histogram).lengths [ MAX_HISTOGRAM - 1 ] > 0 )
    printf ( "%3d+ %6d\n", MAX_HISTOGRAM - 1, (*histogram).lengths [ index ] );

  printf ( "\n" );

}  /* print_histogram */


/******************************************************************************/
/* This function prints out the patterns table. */
print_patterns ( patterns )
t_patterns	*patterns;	/* table of search patterns */
{
  int	base;		/* pattern index */
  int	index = 0;	/* patterns table index */

  printf ( "\nThe patterns table:\n" );
  printf ( "\nTotal patterns = %d.\n\n", (*patterns).total );
  printf ( "Name    length     minimum     type     match    pattern\n" );

  while ( index < (*patterns).total )
  {
    printf ( "%10s  %6d  %6d   %6d  %f  ",
      (*patterns).pattern [ index ].name,
      (*patterns).pattern [ index ].length,
      (*patterns).pattern [ index ].minimum,
      (*patterns).pattern [ index ].type,
      (*patterns).pattern [ index ].match );

    for ( base = 0; base < (*patterns).pattern [ index ].length; base++ )
      printf ( "%c",
          dna_map [ (*patterns).pattern [ index ].aa [ base ] ] );
    printf ( "\n" );
    index++;
  }  /* while */
  printf ( "\n" );
}  /* print_patterns */


/******************************************************************************/
/* This function prints out the total sequences composition. */
print_composition ( composition )
t_composition	*composition;	/* combined sequences composition */
{
  int	index;		/* composition index */
  int	printed = 0;	/* number of characters printed */

  printf ( "\n" );

  for ( index = 0; index <= 'Z' - 'A'; index++ )
    if ( (*composition).count [ index ] > 0 )
    {
      printf ( "%c  %5d  ", index + 'A', (*composition).count [ index ] );
      printed++;
      if ( printed >= 8 )
      {
        printf ( "\n" );
        printed = 0;
      }  /* if */
    }  /* if */
  printf ( "\n\n" );
}  /* print_composition */


/******************************************************************************/
/* This function prints out a range of bases. */
print_bases ( sequence, start, end, print_range, data_file, printed )
t_sequence	*sequence;	/* entire sequence */
long		start;		/* start of print range */
long		end;		/* end of print range */
int		print_range;	/* Boolean flag indicating if range is selected */
FILE		*data_file;	/* output data file */
int		*printed;	/* Bases printed */
{
  long	seq_index;	/* sequence index */

  for ( seq_index = start; seq_index <= end; seq_index++ )
  {
    if ( *printed == 0 )  fprintf ( data_file, "%10d  ", seq_index );

    if ( print_range == TRUE )
      fprintf ( data_file, "%c", 
          (*sequence).aa [ seq_index - 1 ] );
    else
      fprintf ( data_file, "." );
    (*printed)++;

    /* Space every 10 bases. */
    if (( (*printed) % 10 ) == 0 )  fprintf ( data_file, " " );

    if ( (*printed) == 50 )
    {
      fprintf ( data_file, "\n" );
      *printed = 0;
    }  /* if */
  }  /* for */
}  /* print_bases */


/******************************************************************************/
/* This function initializes the sequence statistics table. */
print_stats ( seq_stats )
t_seq_stats	*seq_stats;	/* sequence statistics table */
{
  int	index;		/* array index */
  int	types;		/* DNA category types */

  printf ( "\nTotal sequence(s) statistics:\n" );

  printf ( "\nSequences searched: %d\n\n", (*seq_stats).total_sequences );

  /* Print out each classification category statistics. */
  for ( types = 0; types <= TOTAL; types++ )
  {
    printf ( "Classification category '%s':\n\n", aa_types [ types ] );

    /* Print out the base composition. */
    print_composition ( &((*seq_stats).comp [ types ]) );
  }  /* for */

  /* Print out the histograms for each classification category lengths. */
  printf ( "\n" );
  for ( types = 0; types <= TOTAL; types++ )
    printf ( "%s\t", aa_types [ types ] );
  printf ( "\n" );

  for ( index = 0; index <= MAX_HISTO_CATEGORY + 1; index++ )
    for ( types = 0; types <= TOTAL; types++ )
      print_histo ( &((*seq_stats).lengths [ types ]), index, types, TOTAL,
          MAX_HISTO_CATEGORY );

  /* Print out the histogram of file lengths. */
/*  print_histogram ( "Files", &((*seq_stats).file_lengths) ); */

}  /* print_stats */


/******************************************************************************/
/* This function searches for direct repeats. */
repeats ( sequence, components )
t_sequence	*sequence;	/* entire sequence */
t_components	*components;	/* sequence components table */
{
  int		match;		/* number of direct repeat base matches */
  t_pattern	repeat;		/* direct repeat pattern */
  int		rep_index;	/* direct repeat index */
  long		seq_index;	/* sequence index */

  strcpy ( repeat.name, "Repeat" );
  repeat.type = P_REPEAT;

  /* Scan the entire sequence. */
  for ( seq_index = 0; seq_index < (*sequence).length; seq_index++ )
    for ( rep_index = seq_index + 1; rep_index <= seq_index + REPEAT_WINDOW + 1;
        rep_index++ )
    {
      /* Test for direct repeat. */
      match = 0;
      while (( (*sequence).aa [ seq_index + match ] ==
          (*sequence).aa [ rep_index + match ] ) &&
          ( rep_index + match < (*sequence).length ))
        match++;

      /* Direct repeat is contigous or overlaps and is of minimum size. */
      if (( seq_index + match >= rep_index - 1 ) && ( match > MIN_REPEAT ))
      {
        repeat.length = rep_index + match - seq_index;
        print_match ( sequence, &repeat, components, seq_index, 1 );
        seq_index += match;
        rep_index = REPEAT_WINDOW + 1;
      }  /* if */
    }  /* for */
}  /* repeats */


/******************************************************************************/
/* This function searches for unit patterns. */
unit_patterns ( seq_stats, sequence, components )
t_seq_stats	*seq_stats;	/* sequence statistics table */
t_sequence	*sequence;	/* entire sequence */
t_components	*components;	/* sequence components table */
{
  double  	fract;				/* pattern fraction */
  double  	fraction [ 'Z' - 'A' + 1 ];	/* computed nucleotide fractions */
  int	  	index;				/* pattern index */
  double  	length;				/* calculated sequence length */
  unsigned	mask;				/* bit representation of base */
  double	n;				/* power */
  char		*pat_bases [ MAX_UNITS ];	/* pointers to nucleotides */
  int	  	pat_index;			/* pattern index */
  t_pattern	pattern;			/* search pattern */
  int		not_done;			/* search flag */
  char	  	nucleotides [ MAX_LINE ];	/* possible bases for patterns */
  int	  	unit;				/* current unit length */

  double	log10 ();			/* log base 10 */
  double	pow ();				/* power */

  /* Initialize fraction and compute sequence length. */
  length = 0.0;
  for ( index = 0; index <= 'Z' - 'A'; index++ )
  {
    fraction [ index ] = 0.0;
    length += ( (*seq_stats).comp [ TOTAL ].count [ index ] * 1.0 );
  }  /* for */

  if ( length == 0.0 )  return;

  /* Compute the nucleotide ambiquity fractions. */
  fraction [ 'A' - 'A' ] = 
      (( (*seq_stats).comp [ TOTAL ].count [ 'A' - 'A' ] * 1.0 ) / length );
  fraction [ 'C' - 'A' ] = 
      (( (*seq_stats).comp [ TOTAL ].count [ 'C' - 'A' ] * 1.0 ) / length );
  fraction [ 'G' - 'A' ] = 
      (( (*seq_stats).comp [ TOTAL ].count [ 'G' - 'A' ] * 1.0 ) / length );
  fraction [ 'T' - 'A' ] = 
      (( (*seq_stats).comp [ TOTAL ].count [ 'T' - 'A' ] * 1.0 ) / length );

  fraction [ 'R' - 'A' ] = fraction [ 'A' - 'A' ] + fraction [ 'G' - 'A' ];
  fraction [ 'W' - 'A' ] = fraction [ 'A' - 'A' ] + fraction [ 'T' - 'A' ];
  fraction [ 'S' - 'A' ] = fraction [ 'C' - 'A' ] + fraction [ 'G' - 'A' ];
  fraction [ 'Y' - 'A' ] = fraction [ 'C' - 'A' ] + fraction [ 'T' - 'A' ];
  fraction [ 'K' - 'A' ] = fraction [ 'T' - 'A' ] + fraction [ 'G' - 'A' ];
  fraction [ 'M' - 'A' ] = fraction [ 'A' - 'A' ] + fraction [ 'C' - 'A' ];

  fraction [ 'B' - 'A' ] = fraction [ 'M' - 'A' ] + fraction [ 'G' - 'A' ];
  fraction [ 'D' - 'A' ] = fraction [ 'M' - 'A' ] + fraction [ 'T' - 'A' ];
  fraction [ 'H' - 'A' ] = fraction [ 'K' - 'A' ] + fraction [ 'A' - 'A' ];
  fraction [ 'V' - 'A' ] = fraction [ 'K' - 'A' ] + fraction [ 'C' - 'A' ];

  /* Generate the search patterns. */
  pattern.type = P_SUBUNIT;
  pattern.expected = 0.0;
  for ( unit = 1; unit <= MAX_UNITS; unit++ )
  {
    if ( unit == 1 )
      strcopy ( nucleotides, "ACGTMRWSYKBDHV" );
    else
      strcopy ( nucleotides, "MRWSYK" );

    pattern.length = unit;

    /* Initialize the pattern bases. */
    for ( index = 0; index < unit; index++ )
      pat_bases [ index ] = nucleotides;

    /* Cycle through all possible bases at each pattern position. */
    index = unit - 1;
    while ( index >= 0 )
    {
      /* Copy the pattern. */
      for ( pat_index = 0; pat_index < unit; pat_index++ )
      {
        pattern.name [ pat_index ] = *pat_bases [ pat_index ];
        pattern.aa [ pat_index ] = pattern.name [ pat_index ];
      }  /* for */

      /* Compute the pattern probability. */
      fract = 1.0;
      for ( pat_index = 0; pat_index < unit; pat_index++ )
        fract *= fraction [ pattern.name [ pat_index ] - 'A' ];

      n = 10.0;
      if ( log10 ( fract ) != 0.0 )
        n =  -1.0 * log10 ( length / P ) / log10 ( fract );
      pattern.minimum = n + 0.999;
      pattern.expected = length * pow ( fract, (pattern.minimum * 1.0) );  

      find_pattern ( sequence, &pattern, components );

      /* Set up the next pattern. */
      index = unit - 1;
      pat_bases [ index ]++;
      not_done = ( *pat_bases [ index ] == '\0' );
      while ( not_done )
      {
        pat_bases [ index ] = nucleotides;
        index--;
        if ( index >= 0 )
        {
          pat_bases [ index ]++;
          not_done = ( *pat_bases [ index ] == '\0' );
        }  /* if */
        else not_done = ( '\0' != '\0' );
      }  /* while */
    }  /* while */
  }  /* for */
}  /* unit_patterns */


/******************************************************************************/
/* This function writes out a view of a sequence file. */
write_sequence ( sequence, components, selection )
t_sequence	*sequence;	/* entire sequence */
t_components	*components;	/* sequence components table */
int		selection;	/* sequence component selection */
{
  int	comp_index;		/* components table index */
  FILE	*fopen ();		/* file open function */
  char	file_name [ MAX_LINE ];	/* modified output filename */
  long	index;			/* sequence table index */
  int	print_range;		/* Boolean print bases flag */
  int	printed = 0;		/* bases printed */
  FILE	*view;			/* select component of sequence */

  /* Create the new output file name. */

  /* Remove the disk drive prefix. */
  index = charidx ( ':', (*sequence).filename );
  if ( (*sequence).filename [ index ] != '\0' )
    strcpy ( file_name, (*sequence).filename [ ++index ] );
  else
    strcpy ( file_name, (*sequence).filename );

  /* Remove the suffix. */
  file_name [ charidx ( '.', file_name ) ] = '\0';

  concatenate ( file_name, "." );
  for ( index = 0; index < MAX_AA_TYPES; index++ )
    if (( selection & comp_types [ index ] ) != 0 )
      concatenate ( file_name, comp_names [ index ] );
  if ( selection == 0 )
    concatenate ( file_name, comp_names [ 0 ] );

  /* Open the file for writing. */
  view = fopen ( file_name, "w" );
  if ( view == NULL )
  {
    printf ( "*WARN* unable to open output file '%s'\n.", file_name );
    return;
  }  /* if */

  /* Write out the header. */
  fprintf ( view, "..\n" );

  /* Write out the sequence slice. */
  index = 1;
  /* Traverse the components table. */
  if ( (*components).total > 0 )
    for ( comp_index = 0; comp_index < (*components).total; comp_index++ )
    {
      /* Check if gap before the next component table entry. */
      if ( index < (*components).atom [ comp_index ].begin )
      {
        print_range = TRUE;

        print_bases ( sequence, index, (*components).atom [ comp_index ].begin
            -1, print_range, view, &printed );
        index = (*components).atom [ comp_index ].begin;
      }  /* if */

      /* Print the current component. */
      if (( selection & (*components).atom [ comp_index ].type ) != 0 )
        print_range = TRUE;
      else  print_range = FALSE;

      print_range = TRUE;

      print_bases ( sequence, (*components).atom [ comp_index ].begin,
          (*components).atom [ comp_index ].end, print_range, view, &printed );
      index = (*components).atom [ comp_index ].end + 1;
    }  /* for */

  /* Check if entire sequence traversed. */
  if ( index <= (*sequence).length )
  {
    print_range = TRUE;

    print_bases ( sequence, index, (*sequence).length, print_range, 
        view, &printed );
  }  /* if */

  fclose ( view );  /* close the output file */
}  /* write_sequence */


/******************************************************************************/
/* This function initializes the sequence statistics table. */
init_stats ( seq_stats )
t_seq_stats	*seq_stats;	/* sequence statistics table */
{
  int	index;		/* array index */
  int	types;		/* DNA category types */

  /* Histogram of file lengths. */
  for ( index = 0; index < MAX_HISTOGRAM; index++ )
    (*seq_stats).file_lengths.lengths [ index ] = 0;

  /* Initialize the sequence statistics table. */
  (*seq_stats).total_sequences = 0;
  for ( types = 0; types < MAX_AA_TYPES; types++ )
  {
    /* Initialize the category base composition table. */
    for ( index = 'A' - 'A'; index <= 'Z' - 'A'; index++ )
      (*seq_stats).comp [ types ].count [ index ] = 0;

    /* Histograms of component lengths. */
    for ( index = 0; index < MAX_HISTOGRAM; index++ )
      (*seq_stats).lengths [ types ].lengths [ index ] = 0;
  }  /* for */
}  /* init_stats */


/******************************************************************************/
/* This function counts bases for a sequence range. */
count_range ( sequence, start, end, seq_stats, type )
t_sequence	*sequence;	/* entire sequence */
long		start;		/* start of print range */
long		end;		/* end of print range */
t_seq_stats	*seq_stats;	/* sequence statistics table */
int		type;		/* component type */
{
  char	base;		/* current sequence base */
  long	seq_index;	/* sequence index */

  /* Traverse the sequence range. */
  for ( seq_index = start; seq_index <= end; seq_index++ )
  {
    /* Recover the sequence base. */
    base = (*sequence).aa [ seq_index - 1 ];

    /* Add the base to the statistics. */
    if ( base != ' ' )
      (*seq_stats).comp [ type ].count [ base - 'A' ]++;
  }  /* for */
}  /* count_range */


/******************************************************************************/
/* This function counts bases, dinucleotides, and trinucleotides. */
count_bases ( sequence, components, seq_stats )
t_sequence	*sequence;	/* entire sequence */
t_components	*components;	/* sequence components table */
t_seq_stats	*seq_stats;	/* sequence statistics table */
{
  int	comp_index;		/* components table index */
  long	seq_index;		/* sequence table index */

  (*seq_stats).total_sequences++;

  /* Scan the sequence. */
  seq_index = 1;
  /* Traverse the components table. */
  if ( (*components).total > 0 )
    for ( comp_index = 0; comp_index < (*components).total; comp_index++ )
    {
      /* Check if gap before the next component table entry. */
      if ( seq_index < (*components).atom [ comp_index ].begin )
      {
        count_range ( sequence, seq_index, (*components).atom [ comp_index ].begin
            - 1, seq_stats, UNSPECIFIED );
        seq_index = (*components).atom [ comp_index ].begin;

        add_length ( seq_index, (*components).atom [ comp_index ].begin - 1,
            &((*seq_stats).lengths [ UNSPECIFIED ]), H_SEQUENCE );
      }  /* if */

      /* Compute statistics on the current component element. */
      count_range ( sequence, (*components).atom [ comp_index ].begin,
          (*components).atom [ comp_index ].end, seq_stats,
          (*components).atom [ comp_index ].type );
      seq_index = (*components).atom [ comp_index ].end + 1;

      add_length ( (*components).atom [ comp_index ].begin,
          (*components).atom [ comp_index ].end,
          &((*seq_stats).lengths [ (*components).atom [ comp_index ].type ]),
          H_SEQUENCE );
    }  /* for */

  /* Check if entire sequence traversed. */
  if ( seq_index <= (*sequence).length )
  {
    count_range ( sequence, seq_index, (*sequence).length, seq_stats, 
        UNSPECIFIED );

    add_length ( seq_index, (*sequence).length, 
        &((*seq_stats).lengths [ UNSPECIFIED ]), H_SEQUENCE );
  }  /* if */

  /* Compute statistics on the entire sequence. */
  count_range ( sequence, 1, (*sequence).length, seq_stats, TOTAL );

  add_length ( 1, (*sequence).length, &((*seq_stats).file_lengths),
      H_FILE_LEN );
}  /* count_bases */
