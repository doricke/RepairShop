
#include <stdio.h>
#include <math.h>

/* Complementary Peptides Program. */
#define  Copyright  "Copyright 1993 c Darrell O. Ricke"
/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 1993 Darrell O. Ricke, Ph.D., Paragon Software
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

#define MAX_COMPLEMENTS   8     /* Maximum number of complementary amino acids */

#define MAX_HYDROPATHY  'Z' - 'A' + 1     /* Maximum hydropathy values */

#define MAX_SEQUENCE     10000    /* Maximum amino acid sequence length */

/******************************************************************************/

/* Complementary Amino acids */
typedef struct {
  char  complement [ MAX_COMPLEMENTS ];    /* Complementary amino acids */
} t_complements;


/* Table of complementary amino acids for each amino acid. */
typedef struct {
  t_complements  amino_acid [ MAX_HYDROPATHY ];    /* amino acids */
}  t_amino_acids;


/******************************************************************************/
/* Complementary Peptides Program main program. */
main ( )
{
  double  add_hydropathy ( );               /* function */
  t_amino_acids  amino_acids;               /* table of amino acid complements */
  char    best [ MAX_SEQUENCE ];            /* running best complementary sequence */
  double  best_hydropathy;                  /* best sequence hydropathy */
  double  fabs ( );                         /* absolute value function */
  double  hydropathy [ MAX_HYDROPATHY ];    /* Amino acid hydropathy values */
  int     index;                            /* complementary amino acid index */
  int     length;                           /* target sequence length */
  char    match [ MAX_SEQUENCE ];           /* complementary sequence */
  double  match_hydropathy;                 /* match sequence hydropathy */
  char    next_complement;                  /* next complementary amino acid */
  int     position;                         /* current sequence position */
  char    target [ MAX_SEQUENCE ];          /* target sequence */
  double  target_hydropathy;                /* target sequence hydropathy */


  printf ("\nComplementary Peptides Program.\n");
  printf ("\nCopyright 1993 c Darrell O. Ricke.\n");
  printf ("\nReference: Archives of Biochemistry and Biophysics.\n");
  printf ("Vol 296, No. 1, July, pp. 137-143, 1992.\n");

  /* Set the hydropathy values for each amino acid. */
  set_hydropathy ( hydropathy );

  /* Set the complementary amino acids for each amino acid. */
  set_complements ( &amino_acids );

  /* Initialize the target sequence. */
  strcopy ( target, "FAESGQVYFGIIAL" );
  length = str_len ( target );
  target_hydropathy = add_hydropathy ( hydropathy, target );    /* add up the hydropathy */

  printf ( "\nThe target sequences is '%s'.\n", target );
  printf ( "Target hydropathy = %6.3f\n", target_hydropathy );

  /* Initialize the complementary sequence. */
  set_complement ( &amino_acids, target, match );
  match_hydropathy = add_hydropathy ( hydropathy, match );    /* add up the hydropathy */

  /* Initialize the best complementary match. */
  strcopy ( best, match );
  best_hydropathy = match_hydropathy;

  /* Create all posible complementary sequences. */
  for ( position = 0; ( position < MAX_SEQUENCE ) && ( position < length ); 
      position++ )
  {
    index = 1;    /* the first complement has already been used */

    next_complement = amino_acids.amino_acid [ target [ position ] - 'A' ]
        .complement [ index ];
    while ( ( index < MAX_COMPLEMENTS ) && ( next_complement != '\0' ) )
    {
      match_hydropathy = match_hydropathy - hydropathy 
          [ match [ position ] - 'A' ] + hydropathy [ next_complement - 'A' ];

      match [ position ] = next_complement;

printf ( "Target %6.3f, Best %6.3f, Current %6.3f", 
    target_hydropathy, best_hydropathy, match_hydropathy );

printf ( "  %s\n", match );

      /* Check for a better complementary match. */
      if ( fabs ( target_hydropathy + match_hydropathy ) <
           fabs ( target_hydropathy + best_hydropathy ) )
      {
        strcopy ( best, match );
        best_hydropathy = match_hydropathy;
      }  /* if */
      index++;

      next_complement = amino_acids.amino_acid [ target [ position ] - 'A' ]
          .complement [ index ];
    }  /* while */
printf ( "\n" );
    strcopy ( match, best );
  }  /* for */

  printf ( "\nBest complement = '%s'.\n", best );
  printf ( "Best complement hydropathy = %6.3f\n", best_hydropathy );

  printf ( "\nEnd main program.\n" );
}  /* main */


/******************************************************************************/
/* This function sets the complementary amino acids for each amino acid. */
set_complements ( amino_acids )
t_amino_acids  *amino_acids;    /* table of amino acid complements */
{
  strcopy ( (*amino_acids).amino_acid [ 'A' - 'A' ].complement, "PSWY" );
  strcopy ( (*amino_acids).amino_acid [ 'C' - 'A' ].complement, "NHP" );
  strcopy ( (*amino_acids).amino_acid [ 'D' - 'A' ].complement, "CILFV" );
  strcopy ( (*amino_acids).amino_acid [ 'E' - 'A' ].complement, "CILFV" );
  strcopy ( (*amino_acids).amino_acid [ 'F' - 'A' ].complement, "NH" );
  strcopy ( (*amino_acids).amino_acid [ 'G' - 'A' ].complement, "G" );
  strcopy ( (*amino_acids).amino_acid [ 'H' - 'A' ].complement, "CLFV" );
  strcopy ( (*amino_acids).amino_acid [ 'I' - 'A' ].complement, "RNK" );
  strcopy ( (*amino_acids).amino_acid [ 'K' - 'A' ].complement, "ILV" );
  strcopy ( (*amino_acids).amino_acid [ 'L' - 'A' ].complement, "RNHK" );
  strcopy ( (*amino_acids).amino_acid [ 'M' - 'A' ].complement, "PWY" );
  strcopy ( (*amino_acids).amino_acid [ 'N' - 'A' ].complement, "CILFV" );
  strcopy ( (*amino_acids).amino_acid [ 'P' - 'A' ].complement, "ACM" );
  strcopy ( (*amino_acids).amino_acid [ 'Q' - 'A' ].complement, "CILFV" );
  strcopy ( (*amino_acids).amino_acid [ 'R' - 'A' ].complement, "ILV" );
  strcopy ( (*amino_acids).amino_acid [ 'S' - 'A' ].complement, "A" );
  strcopy ( (*amino_acids).amino_acid [ 'T' - 'A' ].complement, "AG" );
  strcopy ( (*amino_acids).amino_acid [ 'V' - 'A' ].complement, "RNHK" );
  strcopy ( (*amino_acids).amino_acid [ 'W' - 'A' ].complement, "AM" );
  strcopy ( (*amino_acids).amino_acid [ 'Y' - 'A' ].complement, "AM" );
}  /* set_complements */


/******************************************************************************/
/* This function initializes the hydropathy values for each amino acid. */
set_hydropathy ( hydropathy )
double  hydropathy [];    /* amino acid hydropathy values */
{
  int  index;    /* array index */

  /* Initialize for invalid amino acids. */
  for ( index = 0; index < MAX_HYDROPATHY; index++ )
    hydropathy [ index ] = 0;

  /* Initialize the valid amino acids. */
  hydropathy [ 'A' - 'A' ] =  1.8;
  hydropathy [ 'C' - 'A' ] =  2.5;
  hydropathy [ 'D' - 'A' ] = -3.5;
  hydropathy [ 'E' - 'A' ] = -3.5;
  hydropathy [ 'F' - 'A' ] =  2.7;
  hydropathy [ 'G' - 'A' ] = -0.4;
  hydropathy [ 'H' - 'A' ] = -3.2;
  hydropathy [ 'I' - 'A' ] =  4.5;
  hydropathy [ 'K' - 'A' ] = -3.9;
  hydropathy [ 'L' - 'A' ] =  3.8;
  hydropathy [ 'M' - 'A' ] =  1.9;
  hydropathy [ 'N' - 'A' ] = -3.5;
  hydropathy [ 'P' - 'A' ] = -1.6;
  hydropathy [ 'Q' - 'A' ] = -3.5;
  hydropathy [ 'R' - 'A' ] = -4.5;
  hydropathy [ 'S' - 'A' ] = -0.8;
  hydropathy [ 'T' - 'A' ] = -0.7;
  hydropathy [ 'V' - 'A' ] =  4.2;
  hydropathy [ 'W' - 'A' ] = -0.9;
  hydropathy [ 'Y' - 'A' ] = -1.3;
}  /* set_hydropathy */


/******************************************************************************/
/* This function adds up the hydropathy for a sequence. */
double  add_hydropathy ( hydropathy, sequence )
double  hydropathy [];    /* Amino acid hydropathy values */
char    sequence [];      /* sequence to add hydropathy up for */
{
  int     index = 0;    /* sequence index */
  double  sum = 0.0;    /* running sum of hydropathy values */

  while ( ( sequence [ index ] != '\0' ) && ( index < MAX_SEQUENCE ) )
  {
    sum += hydropathy [ sequence [ index ] - 'A' ];
    index++;
  }  /* while */

  return ( sum );
}  /* add_hydropathy */


/******************************************************************************/
/* This function creates a complementary hydropathy sequence for a target */
set_complement ( amino_acids, target, match )
t_amino_acids  *amino_acids;    /* table of amino acid complements */
char           target [];       /* target sequence to complement */
char           match [];        /* complementary sequence to the target sequence */
{
  int  index = 0;    /* sequence index */

  while ( ( target [ index ] != '\0' ) && ( index < MAX_SEQUENCE ) )
  {
    match [ index ] = (*amino_acids).amino_acid [ target [ index ] - 'A' ]
        .complement [ 0 ];
    index++;
  }  /* while */

  match [ index ] = '\0';    /* terminate the sequence */
}  /* set_complement */


/******************************************************************************/
/* This function copies the string t to s. */
strcopy ( s, t )
char	*s, *t;
{
  while ( *s++ = *t++ ) ;
}  /* strcopy */


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
