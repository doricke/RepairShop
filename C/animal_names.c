
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

#define S_EOF		1	/* End Of File encountered */
#define S_EOL		8	/* End Of Line encountered */
#define S_NO_INTEGER	3	/* no integer on current line */
#define S_NORMAL	6	/* normal function termination */
#define S_OPEN_FAILED	5	/* file open failed */

#define S_SEQ_TOO_LONG	7	/* sequence exceeds array bounds */
#define S_NOT_FOUND     8       /* search item not found */

#define MAX_BLANK        60     /* search for a blank in the first n chars */
#define MAX_LINE 	232  	/* maximum line length */
#define MAX_SPECIES   50000     /* maximum number of species per gene */

#define MAX_SPECIES_NAME  6       /* maximum Swiss database species name */


/* Gene name */
typedef struct {
  char  species [ MAX_SPECIES_NAME ];    /* Swiss protein database species name */
} t_species;

/* Text string */
typedef struct {
  char  name [ MAX_LINE ];     /* one name */
} t_name;

/* Table of names */
typedef struct {
  int        total;                     /* number of names in the table */
  long       count [ MAX_SPECIES ];     /* count of name occurances */
  t_species  unique [ MAX_SPECIES ];    /* array of species names */
  t_name     text [ MAX_SPECIES ];      /* array of first occurance lines */
} t_names;


main ( )
{
  int	count;				/* current token number */
  FILE  *file_names = NULL;		/* list of file names to process */
  char  gene_name [ MAX_LINE ];         /* current gene name */
  int   index;                          /* index */
  int   insert;                         /* insertion position */
  char  line [ MAX_LINE ];              /* current input line */
  long  line_count = 0;                 /* count of lines read in */
  int	line_index;			/* line index */
  char  next_species [ MAX_LINE ];      /* the next species name */
  t_names  species;                     /* list of species names */
  int	status = S_NORMAL;		/* function return status */
  char	token [ MAX_LINE ];		/* current token */

  prompt_files ( &file_names );

  /* Initializations. */
  strcpy ( gene_name, " " );
  species.total = 0;
  for ( index = 0; index < MAX_SPECIES; index++ )
    species.count [ index ] = 0;

  /* Search the list of Swiss Protein Sequence Names. */
  while ( status != S_EOF )
  {
    /* Get the next line from the file. */
    status = get_line ( file_names, line );
    line_index = 3;

    line_count++;

    /* Get the gene name. */
    get_token ( line, &line_index, token );

    /* Get the species name. */
    get_token ( line, &line_index, next_species );

    /* Check if new species. */
    if ( ( status == S_NORMAL ) && ( line [ 0 ] == 's' ))
    {
      if ( str_find ( next_species, &species, &insert ) == S_NOT_FOUND ) 
      {
        /* Shift the table down to allow for insertion. */
        for ( index = species.total; index >= insert; index-- )
        {
          strcpy ( species.text [ index + 1 ].name, 
              species.text [ index ].name );
          strcpy ( species.unique [ index + 1 ].species, 
              species.unique [ index ].species );
          species.count [ index + 1 ] = species.count [ index ];
        }  /* for */

        /* Add the new species. */
        species.total++;
        strcpy ( species.text [ insert ].name, line );
        strcpy ( species.unique [ insert ].species, next_species );
        species.count [ insert ] = 1;
      }  /* if */
      else
        species.count [ insert ]++;
    }  /* if */
  }  /* while */

  printf ( "\n\nTotal species = %d\n\n", species.total );

  printf ( "Number of lines processed %d\n\n", line_count );

  /* Print out the list of species. */
  for ( index = 0; index < species.total; index++ )
    if ( species.count [ index ] >= 10 )
    {
      count = 0;
      while ( ( species.text [ index ].name [ count ] != '\0' ) &&
            ( ( species.text [ index ].name [ count ] != 'O' ) ||
              ( species.text [ index ].name [ count + 1 ] != 'S' ) ||
              ( species.text [ index ].name [ count + 2 ] != ' ' ) ) )
        count++;

      printf ( "%s\t%5d\t\t%s\n", species.unique [ index ].species, 
          species.count [ index ], &(species.text [ index ].name [ count ]) );
    }  /* if */

  printf ( "\n\n" );
  for ( index = 0; index < species.total; index++ )
    if ( species.count [ index ] >= 10 )
    {
      for ( count = 0; count <= MAX_BLANK; count++ )
        if ( species.text [ index ].name [ count ] == ' ' )
          species.text [ index ].name [ count ] = '\0';

      printf ( "%s\n", species.text [ index ].name );
    }  /* if */

  printf ("\nEnd main program.\n");
}  /* main */


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

  if ( c == EOF )  return ( S_EOF );
  return ( S_NORMAL );
}  /* get_line */


/* This function returns the next token from line. */
get_token ( line, line_index, token )
char	line [];	/* source line */
int	*line_index;	/* character to start at */
char	token [];	/* next token */
{
  int	index = 0;	/* array index of current token character */

  /* skip leading blanks or line feeds. */
  while ( (line [ *line_index ] == ' ') || (line [ *line_index ] == '\n') ||
          (line [ *line_index ] == '_') )
    (*line_index)++;

  token [ index ] = '\0';
  /* check for end of line. */
  if ( line [ *line_index ] == '\0' )
    return ( S_EOL );

  /* copy the token. */
  while ( (line [ *line_index + index ] != '\0') &&
	  (line [ *line_index + index ] != '\n') &&
	  (line [ *line_index + index ] != '_') &&
	  (line [ *line_index + index ] != ' ') )
  {
    token [ index ] = line [ *line_index + index ];
    ++index;
  }  /* while */

  *line_index = *line_index + index;
  token [ index ] = '\0';
  return ( S_NORMAL );
}  /* get_token */


/* This function opens the specified file for reading. */
open_file (name, input_file)
char  name [];		/* filename */
FILE  **input_file;	/* file to open for reading */
{
  FILE  *fopen ();	/* file open function */

  /* printf ("open_file, name = '%s'\n", name); */

  *input_file = fopen (name, "r");
  if (*input_file == NULL)
  {
    printf ("WARNING: could not open the file '%s'.\n");
    return (S_OPEN_FAILED);
  }  /* if */

  return (S_NORMAL);
}  /* open_file */


/* This function prompts for the name of the file of file names. */
prompt_files ( file_names )
FILE  **file_names;	/* file of file names */
{
  int   line_index;		/* line index for get_integer */
  char  response [ MAX_LINE ];	/* user's response */

  /* check  if the first protein file name has been specified */
  while ( *file_names == NULL )
  {
    printf ("What is the filename of Swiss protein database file names? ");
    scanf ( "%s", response );
    open_file ( response, file_names );
  }  /* while */

}  /* prompt_files */


/* This function binary searches an array of strings. */
str_find ( name, names, lower )
char     name [];    /* name to search for */
t_names  *names;     /* table of names to search for */
int      *lower;     /* position of name or insertion index */
{
  int  middle;                        /* middle index of search range */
  int  upper = (*names).total - 1;    /* upper index of search range */

  *lower = 0;    /* lower index of search range */
  while ( *lower <= upper )
  {
    middle = (*lower + upper) / 2;
    if ( strcmp ( name, (*names).unique [ middle ].species ) > 0 )
      *lower = middle + 1;    /* look in upper half */
    else
      upper = middle - 1;    /* look in lower half */
  }  /* while */

  if ( ( *lower == (*names).total ) || 
      ( strcmp ( name, (*names).unique [ *lower ].species ) != 0 ) )
    return ( S_NOT_FOUND );

  return ( S_NORMAL );
}  /* str_find */
