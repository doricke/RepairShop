
#include <stdio.h>

/* This program compares multiple amino acid sequences and computes */
/* the generics, partial generics, specifics, and nonconserved amino */
/* acid residues. */

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
#define S_NORMAL	6	/* normal function termination */

#define MAX_SEQUENCES	20	/* Maximum number of protein sequences */

#define	TRUE		1	/* Boolean flags */
#define FALSE		0

/* sequence information for one protein */
typedef struct {
  FILE	*protein;		/* internal C file */
  char	file_name [ MAX_LINE ];	/* name of the protein file */
  int	group;			/* protein family group identifier */
  char	line [ MAX_LINE ];	/* current line of the file */
  char	pep_char;		/* current amino acid */
  char	line_index;		/* line index of pep_char */
} t_sequence;

/* table of protein sequences */
typedef struct {
  int		total;			/* number of files */
  t_sequence	seq [ MAX_SEQUENCES ];	/* protein sequence information */
} t_files;

/* table of protein family group information */
typedef struct {
  int		total;			/* number of groups */
  long	masks [ MAX_SEQUENCES ];	/* specific family consensus */
  long	other_groups [ MAX_SEQUENCES ];	/* all other families combined */
} t_groups;

/* This function opens all of the files in the file of filenames. */
open_files ( file_of_files, files )
FILE	*file_of_files;	/* file containing list of filenames */
t_files	*files;		/* table of protein sequences */
{
  int	line_index = 0;		/* current character index in line */
  int	status = S_NORMAL;	/* status of called functions */
  char	line [ MAX_LINE ];	/* current file of filenames line */

  (*files).total = 0;	/* initially no files */
  while ( status != S_EOF )
  {
    status = get_line ( file_of_files, line );

    if (( line [ 0 ] != '\0' ) && ( status == S_NORMAL ))
    {
      status = get_token ( line, &line_index,
	&((*files).seq [ (*files).total ].file_name) );

      if ( status == S_NORMAL )
	status = get_integer ( line, &line_index,
	  &((*files).seq [ (*files).total ].group) );

      if ( status == S_NORMAL )
	status = open_file ( (*files).seq [ (*files).total ].file_name,
	  &((*files).seq [ file_index ].protein) );

      if ( status == S_NORMAL )  /* initialize */
      {
	(*files).seq [ (*files).total ].line = '\0';
	(*files).seq [ (*files).total ].pep_char = ' ';
	(*files).seq [ (*files).total ].line_index = 0;
	printf ( "'%s'\n", (*files).seq [ (*files).total ].file_name );
	(*files).total++;
      }  /* if */
    }  /* if */
  }  /* while */

}  /* open_files */

/* This function computes nonconserved, specifics, partial generics */
/* and generics. */
generics ( files, groups )
t_files		files;		/* table of */
t_groups	*groups;	/* table of */
{
  long	generic = ALL_BITS;
  int	group_index;		/* groups table index */
  int	index;
  char	previous = ' ';

  /* Initialize the group masks. */
  for ( group_index = 0; group_index <= groups.total; group_index++ )
  {
    (*groups).masks [ group_index ] = ALL_BITS;
    (*groups).other_groups [ group_index ] = 0;
  }  /* for */

  /* Traverse each sequence */
  for ( index = 0; index <= files.total; index++ )
  {
    if ( previous != files.seq [ index ].pep_char )
    {
      (*groups).masks [ index ] &=
	masks [ files.seq [ index ].pep_char - 'A' ];
      generic &= masks [ files.seq [ index ].pep_char - 'A' ];
      previous = files.seq [ index ].pep_char;
    }  /* if */
  }  /* for */

  /* Set up partial specifics mask in other_groups. */
  for ( group_index = 0; group_index <= (*groups).total; group_index++ )
    for ( index = 0; index <= (*groups).total; index++ )
      if ( index != group_index )
	(*groups).other_groups [ group_index ] |= (*groups).masks [ index ];

  if ( generic != 0 )
    printf ( "generic\n" );
  else
  {
    for ( group_index = 0; group_index <= (*groups).total; group_index++ )
      if ( (*groups).masks == 0 )
	printf ( "N " );
      else
	if (( (*groups).masks [ group_index ] &
	  (*groups).other_groups [ group_index ]) != 0 )
	  printf ( "P " );
	else
	  printf ( "S " );
      printf ( "\n" );
  }  /* else */
}  /* generics */

/* This function computes the conservation based upon identity */
/* within a family controling if conservative substitutions are allowed. */
generics_2 ( files )
t_files	files;
{
  char	group_char;	/* current group amino acid */
  int	group_index;	/* current group */
  short	iden[ical;	/* flag indicating amino acid identity */

  /* Define the conservation for each family separately. */
  for ( group_index = 0; group_index <= groups.total; group_index++ )
  {
    identical = TRUE;
    group_char = ' ';
    /* Find the members of this family. */
    for ( index = 0; index <= files.total; index++ )
      if ( files.seq [ index ].group == group_index )
      {
	(*groups).mask &= mask [ files.seq [ index ].pep_char - 'A' ];
	if ( ( group_char != ' ' ) &&
	  ( group_char != files.seq [ index ].pep_char ))
	  identical = FALSE;
      }  /* if */
  }  /* for */

  generic = TRUE;	/* assume generic */
  for ( group_index = 0; group_index <= groups.total; group_index++ )
  {
  }  /* for */
}  /* generics_2 */
