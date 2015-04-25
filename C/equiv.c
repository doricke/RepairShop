
#include <stdio.h>

/* This program compares two protein sequences and counts the */
/* number of identical amino acids and allowable conservative */
/* substitutions. */
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

#define S_EOF		1	/* End Of File encountered */
#define S_LOST		2	/* Inconsistant file position */
#define S_NO_INTEGER	3	/* no integer on current line */
#define S_NORMAL	6	/* normal function termination */
#define S_OPEN_FAILED	5	/* file open failed */

#define MAX_COMPARE 2	/* maximum number of sequence comparisons */
#define MAX_LINE 132	/* maximum line length */

/* sequence information for one gene */
typedef struct {
  FILE	*protein;		/* amino acid sequence for the gene */
  char  pep_line [ MAX_LINE ];	/* current line of protein file */
  char  pep_char;		/* current character of protein file */
  int   pep_index;		/* current pep_line index */
} t_sequence;

/* table of gene sequences */
typedef struct {
  int  total;	/* current number of files */
  t_sequence  seq [ MAX_COMPARE ];  /* file sequence information */
} t_files;

/* comparison statistics */
typedef struct {
  long 	identical;  	/* identical amino acids */
  long  equivalent;	/* conservatively substitutable amino acids */
  long 	different;	/* differnt amino acids */
  long  compared;	/* amino acids compared */
  long  x_one;		/* unknown amino acids in sequence one */
  long  x_two;		/* unknown amino acids in sequence two */
  long  dot_one;	/* sequence one gaps */
  long  dot_two;	/* sequence two gaps */
  long  length_one;	/* length of the first sequence */
  long  length_two;	/* length of the second sequence */
}  t_statistics;


static  char  amino_acids [] = {  /* table of amino acids */
  'A', 'R', 'R', 'R', 'N', 'D', 'C', 'Q', 'E', 'G',
  'H', 'I', 'L', 'L', 'L', 'K', 'M', 'F', 'P', 'S',
  'S', '*', '*', '*', 'T', 'W', 'Y', 'V' };


main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  t_files  	files;			/* table of sequence files */
  FILE		*file_names = NULL;	/* list of file names to process */
  int		index;			/* current file index */
  char		name [ MAX_LINE ];	/* current protein file name */
  t_statistics  statistics;		/* comparison statistics table */
  int  		stat = S_NORMAL;	/* function return status */

  if ( argc > 1 )
  {
    open_file ( argv [ 1 ], &(files.seq [ 0 ].protein) );
    open_file ( argv [ 2 ], &(files.seq [ 1 ].protein) );
  }
  else
  {
    prompt_files ( &file_names );

    index = 0;
    while ( stat != S_EOF )
    {
      stat = get_line ( file_names, name );

      if ( name [ 0 ] != '\0' )
      {
        stat = open_file ( name, &(files.seq [ index ].protein) );
        printf ( "'%s'\n", name );
        if ( stat == S_NORMAL )
        {
          files.seq [ index ].pep_line [ 0 ] = '\0';
          files.seq [ index ].pep_char = ' ';
          files.seq [ index ].pep_index = 0;
          index++;
          files.total++;
        }
      }  /* if */
    }  /* while */
  }  /* else */

  if ( stat == S_EOF )  stat = S_NORMAL;

  initialize_statistics ( &statistics );

  if ( stat == S_NORMAL )
    stat = compare_sequences ( files, &statistics );

  if ( stat == S_NORMAL )
    print_statistics ( &statistics );

  if ( stat != S_NORMAL ) printf ("Main program status '%d'.\n", stat);

  printf ("\nEnd main program.\n");
}  /* main */


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


/* This function returns the next integer from line. */
get_integer (line, line_index, integer)
char line [];  /* the current line */
int *line_index;  /* the current line index */
int *integer;  /* the next integer from line */
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


/* This function opens the specified file for reading. */
open_file (name, input_file)
char  name [];		/* filename */
FILE  **input_file;	/* file to open for reading */
{
  FILE  *fopen ();	/* file open function */

  *input_file = fopen (name, "r");
  if (*input_file == NULL)
  {
    printf ("WARNING: could not open the file '%s'.\n");
    return (S_OPEN_FAILED);
  }  /* if */

  return (S_NORMAL);
}  /* open_file */


/* This function initializes the comparison statistics table. */
initialize_statistics ( statistics )
t_statistics *statistics;	/* comparison statistics table */
{
  (*statistics).identical = 0;
  (*statistics).equivalent = 0;
  (*statistics).different = 0;
  (*statistics).compared = 0;
  (*statistics).x_one = 0;
  (*statistics).x_two = 0;
  (*statistics).dot_one = 0;
  (*statistics).dot_two = 0;
  (*statistics).length_one = 0;
  (*statistics).length_two = 0;
}  /* initialize_statistics */


/* This fuction reads from file data until two adjacent periods are found. */
find_data (data)
FILE *data;  /* GCG data file */
{
  char current;  /* current character */
  char previous = ' ';  /* previous character */

  while (((current = getc (data)) != EOF)
    && ((previous != '.') || (current != '.')))
    previous = current;
}  /* find_data */


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


/* This function compares two aligned amino acid sequences and the third */
/* base of codons for identical amino acids. */
compare_sequences ( files, statistics )
t_files		files;		/* table of sequence files */
t_statistics	*statistics;	/* comparison statistics table */
{
  int index;			/* current sequence */
  int status = S_NORMAL;	/* function return status */

  /* set up each sequence */
  for ( index = 0; index < files.total; index++ )
  {
    files.seq [ index ].pep_line [ 0 ] = '\0';
    files.seq [ index ].pep_index = 0;

    /* find the sequence data - advance to '..' */
    find_data ( files.seq [ index ].protein );
  }  /* for */

  /* compare the sequences */
  while (status == S_NORMAL)
  {
    /* for each sequence */
    for ( index = 0; index < files.total; index++ )
      /* get the next amino acid */
      status = next_data_character ( files.seq [ index ].protein,
	files.seq [ index ].pep_line, &(files.seq [ index ].pep_index),
	&(files.seq [ index ].pep_char) );

    if (status == S_NORMAL)
      status = compare_amino_acids ( files, statistics );
  }  /* while */
  if ( status == S_EOF ) return (  S_NORMAL );
  return ( status );
}  /* compare_sequences */


/* This function compares amino acids and third codon bases. */
compare_amino_acids ( files, statistics )
t_files		files;		/* table of sequence files */
t_statistics	*statistics;	/* comparison statistics table */
{
  char	amino_acid_0;	/* amino acid of the first sequence file */
  char	amino_acid_1;	/* amino acid of the second sequence file */
  int  	index;		/* current sequence */
  int  	status;		/* function return status */
  int	equal;		/* flag indicating equivalence */

  amino_acid_0 = files.seq [ 0 ].pep_char;
  amino_acid_1 = files.seq [ 1 ].pep_char;

  if (( amino_acid_0 == '.' ) && ( amino_acid_1 == '.' ))  return ( S_NORMAL );

  if ( amino_acid_0 == '.' )  (*statistics).dot_one++;
  else (*statistics).length_one++;

  if ( amino_acid_1 == '.' )  (*statistics).dot_two++;
  else (*statistics).length_two++;

  if ( amino_acid_0 == 'X' )  (*statistics).x_one++;

  if ( amino_acid_1 == 'X' )  (*statistics).x_two++;

  if (( amino_acid_0 == '.' ) || ( amino_acid_0 == 'X' ) ||
      ( amino_acid_1 == '.' ) || ( amino_acid_1 == 'X' ))
  {
    printf ( "%c # %c\n", amino_acid_0, amino_acid_1 );
    return ( S_NORMAL ); 
  }  /* if */

  /* compare the amino acids */
  (*statistics).compared++;
  if ( amino_acid_0 == amino_acid_1 )
  {
    (*statistics).identical++;
    printf ( "%c = %c\n", amino_acid_0, amino_acid_1 );
  }  /* if */
  else
  {
    equal = 0;
    switch ( amino_acid_0 )
    {
      case 'A': if ( amino_acid_1 == 'S' )  equal = 1;
                break;
	
      case 'D': if (( amino_acid_1 == 'E' ) || ( amino_acid_1 == 'N' ))
                  equal = 1;
                break;

      case 'E': if (( amino_acid_1 == 'D' ) || ( amino_acid_1 == 'Q' ))
                  equal = 1;
                break;

      case 'F': if ( amino_acid_1 == 'Y' )  equal = 1;
                break;

      case 'I': if (( amino_acid_1 == 'L' ) || ( amino_acid_1 == 'V' ))
                  equal = 1;
                break;

      case 'K': if ( amino_acid_1 == 'R' )  equal = 1;
                break;

      case 'L': if (( amino_acid_1 == 'I' ) || ( amino_acid_1 == 'V' ))
                  equal = 1;
                break;

      case 'N': if (( amino_acid_1 == 'D' ) || ( amino_acid_1 == 'Q' ))
                  equal = 1;
                break;

      case 'Q': if (( amino_acid_1 == 'E' ) || ( amino_acid_1 == 'N' ))
                  equal = 1;
                break;

      case 'R': if ( amino_acid_1 == 'K' )  equal = 1;
                break;

      case 'S': if (( amino_acid_1 == 'T' ) || ( amino_acid_1 == 'A' ))
                  equal = 1;
                break;

      case 'T': if ( amino_acid_1 == 'S' )  equal = 1;
                break;

      case 'V': if (( amino_acid_1 == 'I' ) || ( amino_acid_1 == 'L' ))
                  equal = 1;
                break;

      case 'Y': if ( amino_acid_1 == 'F' )  equal = 1;
                break;

      default:  ;
    }  /* switch */

    if ( equal == 1 )
    {
      (*statistics).equivalent++;
      printf ( "%c ~ %c    ****\n", amino_acid_0, amino_acid_1 );
    }  /* if */
    else
    {
      (*statistics).different++;
      printf ( "%c ! %c\n", amino_acid_0, amino_acid_1 );
    }  /* else */
  }

  return ( S_NORMAL );
}  /* compare_amino_acids */


/* This function prints out the statistics. */
print_statistics ( statistics )
t_statistics  *statistics;	/* comparison statistics table */
{
  float	similarity;		

  printf ("\n\n\n" );
  printf ("Identical amino acids %d\n\n", (*statistics).identical);

  printf ("Different amino acids %d\n\n", (*statistics).different);

  printf ("Equivalent amino acids %d\n\n", (*statistics).equivalent);

  printf ("Amino acids compared %d\n\n", (*statistics).compared);

  similarity = ( ( (*statistics).identical +
    (*statistics).equivalent ) * 1.0 ) /
    ((*statistics).compared * 1.0); 

  printf ("Percent similarity = %%%8.2f\n\n", similarity * 100.0 );

  printf ("First sequence length %d\n\n", (*statistics).length_one);

  printf ("Second sequence length %d\n\n", (*statistics).length_two);

  printf ("Gaps in first sequence %d\n\n", (*statistics).dot_one);

  printf ("Gaps in second sequence %d\n\n", (*statistics).dot_two);

  if ( (*statistics).x_one > 0 )
    printf ("Unknowns in first sequence %d\n\n", (*statistics).x_one);

  if ( (*statistics).x_two > 0 )
    printf ("unknowns in second sequence %d\n\n", (*statistics).x_two);

}  /* print_statistics */


/* This function prompts for the name of the file of file names. */
prompt_files ( file_names )
FILE  **file_names;	/* file of file names */
{
  int   line_index;		/* line index for get_integer */
  char  response [ MAX_LINE ];	/* user's response */

  /* check  if the first protein file name has been specified */
  while ( *file_names == NULL )
  {
    printf ("What is the name of the file of file names?");
    scanf ( "%s", response );
    open_file ( response, file_names );
  }  /* while */

  printf ( "\n" );
}  /* prompt_files */
