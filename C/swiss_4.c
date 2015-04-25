
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

#define S_BAD_BASE	4	/* Invalid DNA base */
#define S_EOF		1	/* End Of File encountered */
#define S_EOL		8	/* End Of Line encountered */
#define S_LOST		2	/* Inconsistant file position */
#define S_NO_INTEGER	3	/* no integer on current line */
#define S_NORMAL	6	/* normal function termination */
#define S_OPEN_FAILED	5	/* file open failed */

#define S_SEQ_TOO_LONG	7	/* sequence exceeds array bounds */

#define MAX_LINE 	232  	/* maximum line length */
#define MAX_PER_LINE    15      /* maximum species names per line */
#define MAX_SPECIES     500     /* maximum number of species per gene */

#define MIN_COUNT       4       /* minimum number of species to report */


/* Table of species names */
typedef struct {
  char  name [ MAX_LINE ];     /* species name */
} t_species;


main ( )
{
  int   animals = 0;                    /* count of selected species found */
  int	count;				/* current token number */
  FILE  *file_names = NULL;		/* list of file names to process */
  char  gene_name [ MAX_LINE ];         /* current gene name */
  int   human = 0;                      /* human species found flag */
  char  human_line [ MAX_LINE ];        /* human species input line */
  char  line [ MAX_LINE ];              /* current input line */
  int	line_index;			/* line index */
  char  next_species [ MAX_LINE ];      /* the next species name */
  int   print_count;                    /* number of species names printed/line */
  t_species  species [ MAX_SPECIES ];   /* list of species names */
  long  species_count;                  /* number of species for current gene */
  int	status = S_NORMAL;		/* function return status */
  char	token [ MAX_LINE ];		/* current token */

  prompt_files ( &file_names );

  strcpy ( gene_name, " " );
  species_count = 0;

  while ( ( status == S_NORMAL ) || ( status == S_EOL ) )
  {
    /* Get the next line from the file. */
    status = get_line ( file_names, line );
    line_index = 3;

    /* Get the gene name. */
    if ( status == S_NORMAL )
      status = get_token ( line, &line_index, token );

    /* Get the species name. */
    if ( status == S_NORMAL )
      status = get_token ( line, &line_index, next_species );

    /* Check if new gene. */
    if ( status == S_NORMAL )
      if ( strcmp ( token, gene_name ) != 0 ) 
      {
        /* Print the previous gene? */
        if (( species_count >= MIN_COUNT ) && ( human != 0 ) &&
           ( animals >= 6 ) )
        {
          printf ( "\n%s\n", human_line );
          print_count = 0;
          for ( count = 0; count < species_count; count++ )
          {
            printf ( "  %s", species [ count ].name );
            print_count++;
            if (( print_count == MAX_PER_LINE ) &&
                ( print_count + 1 < species_count - 1 ))
            {
              printf ( "\n" );
              print_count = 0;
            }  /* if */
          }  /* for (count) */
          printf ( "\n" );
        }  /* if */

        species_count = 1;
        animals = 0;
        human = 0;
        strcpy ( species [ 0 ].name, next_species );
        strcpy ( gene_name, token );
      }  /* if */
      else
      {
        strcpy ( species [ species_count ].name, next_species );
        species_count++;
      }  /* else */

    if ( strcmp ( next_species, "canfa" ) == 0 )  animals++;
    if ( strcmp ( next_species, "cavpo" ) == 0 )  animals++;
    if ( strcmp ( next_species, "crigr" ) == 0 )  animals++;
    if ( strcmp ( next_species, "bovin" ) == 0 )  animals++;
    if ( strcmp ( next_species, "horse" ) == 0 )  animals++;
    if ( strcmp ( next_species, "mouse" ) == 0 )  animals++;
    if ( strcmp ( next_species, "pig"   ) == 0 )  animals++;
    if ( strcmp ( next_species, "rabit" ) == 0 )  animals++;
    if ( strcmp ( next_species, "rat"   ) == 0 )  animals++;
    if ( strcmp ( next_species, "sheep" ) == 0 )  animals++;

    /* Check if human species. */
    if ( strcmp ( next_species, "human" ) == 0 )
    {
      human++;
      strcpy ( human_line, line );    /* save the input line */
    }  /* if */
  }  /* while */

  if ( status == S_EOF )  status = S_NORMAL;
  if ( status != S_NORMAL ) printf ("Main program status '%d'.\n", status );

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

/*  printf ("get_line, '%s'\n", line);  */

  if (c == EOF)  return (S_EOF);
  return (S_NORMAL);
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
    printf ("What is the name of Swiss protein database file names?");
    scanf ( "%s", response );
    open_file ( response, file_names );
  }  /* while */

}  /* prompt_files */
