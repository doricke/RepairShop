
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

#define MAX_AMINO_ACIDS	26 + 4	/* maximum number of amino acids */
#define MAX_FILE_NAME	144	/* maximum file name length */
#define MAX_LINE 	232  	/* maximum line length */
#define MAX_SEQ_LENGTH	100	/* maximum amino acid sequence length */


main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  int	count;				/* current token number */
  FILE  *file_names = NULL;		/* list of file names to process */
  char  name [ MAX_FILE_NAME ];		/* current protein file name */
  int	length;				/* current sequence length */
  char  line [ MAX_LINE ];		/* current line */
  int	line_indx;			/* line index */
  int	stat = S_NORMAL;		/* function return status */
  char	token [ MAX_LINE ];		/* current token */

  if ( argc <= 1 )
    prompt_files ( &file_names );
  else
    stat = open_file ( argv [ 1 ], &file_names );

  while ( ( stat == S_NORMAL ) || ( stat == S_EOL )
    || ( stat == S_NO_INTEGER ) )
  {
    /* Get the next line from the file. */
    stat = get_line ( file_names, line );
    line_indx = 0;

    /* Skip the first 3 tokens */
    for ( count = 1; count <= 3; count++ )
      if ( stat == S_NORMAL )
        stat = get_token ( line, &line_indx, token );

    /* Get the length of the current sequence */
    length = 0;
    if ( stat == S_NORMAL )
      stat = get_integer ( line, &line_indx, &length );

    if ( length >= 10000 )
      printf ( "%s\n", line );
  }  /* while */

  if ( stat == S_EOF )  stat = S_NORMAL;

  if ( stat != S_NORMAL ) printf ("Main program status '%d'.\n", stat);

  printf ("\nEnd main program.\n");
}  /* main */


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
  while ( (line [ *line_index ] == ' ') || (line [ *line_index ] == '\n') )
    (*line_index)++;

  token [ index ] = '\0';
  /* check for end of line. */
  if ( line [ *line_index ] == '\0' )
    return ( S_EOL );

  /* copy the token. */
  while ( (line [ *line_index + index ] != '\0') &&
	  (line [ *line_index + index ] != '\n') &&
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
    printf ("What is the name of the file of file names?");
    scanf ( "%s", response );
    open_file ( response, file_names );
  }  /* while */

}  /* prompt_files */
