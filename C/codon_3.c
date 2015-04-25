
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
#define S_LOST		2	/* Inconsistant file position */
#define S_NO_INTEGER	3	/* no integer on current line */
#define S_NORMAL	0	/* normal function termination */
#define S_OPEN_FAILED	5	/* file open failed */

#define ADENINE  'A'
#define CYTOSINE 'C'
#define GUANINE  'G'
#define THYMINE  'T'

#define BASE_1 0  /* first base position in a codon */
#define BASE_2 1  /* second base position in a codon */
#define BASE_3 2  /* third base position in a codon */

#define MAX_CODON 3  /* number of bases in a codon */
#define MAX_LINE 132  /* maximum line length */

#define O_ALA	0
#define O_ARG1	O_ALA + 1	/* AGR */
#define O_ARG2	O_ARG1 + 1	/* CGN */
#define O_ASN	O_ARG2 + 1
#define O_ASP	O_ASN + 1
#define O_CYS	O_ASP + 1
#define O_GLN	O_CYS + 1
#define O_GLU	O_GLN + 1
#define O_GLY	O_GLU + 1
#define O_HIS	O_GLY + 1
#define O_ILE	O_HIS + 1
#define O_LEU1	O_ILE + 1	/* UUR */
#define O_LEU2	O_LEU1 + 1	/* CUN */
#define O_LYS	O_LEU2 + 1
#define O_MET	O_LYS + 1
#define O_PHE	O_MET + 1
#define O_PRO	O_PHE + 1
#define O_SER1	O_PRO + 1	/* UCN */
#define O_SER2	O_SER1 + 1	/* AGY */
#define O_TERM1	O_SER2 + 1	/* UAR */
#define O_TERM2	O_TERM1 + 1	/* UGA */
#define O_THR	O_TERM2 + 1
#define O_TRP	O_THR + 1
#define O_TYR	O_TRP + 1
#define O_VAL	O_TYR + 1

#define MAX_AA_PAIRS  O_VAL + 1 /* maximum number of amino acid pairs */
#define MAX_CODE      64	/* number of codons */

/* amino acid comparison statistics */
typedef struct {
  long total;  /* total amino acids compared */
  long identical;  /* identical amino acids */
  long different_codons;  /* total a.a. encoded by different codons */
  long transitions [ MAX_AA_PAIRS ];  /* transitions for each amino acid */
  long transversions [ MAX_AA_PAIRS ];  /* transversions for each aa. */
  long same_group [ MAX_AA_PAIRS ];  /* same first 2 bases of codon */
}  t_aa_stats;

int  genetic_code [ MAX_CODE ];  /* triplet genetic code */

char  amino_acids [ MAX_AA_PAIRS ];  /* table of amino acids */

static  char  *codon_names [] = {  /* codon names */
  "GCN", "AGR", "CGN", "AAY", "GAY", "UGY", "CAR", "GAR",
  "GGN", "CAY", "AUx", "UUR", "CUN", "AAR", "AUG", "UUY",
  "CCN", "UCN", "AGY", "UAR", "UGA", "ACN", "UGG", "UAY", "GUN" };

main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  FILE  *protein_1;	/* first amino acid sequence */
  FILE  *protein_2;	/* second amino acid sequence */
  FILE  *dna_1;		/* first DNA sequence */
  FILE  *dna_2;		/* second DNA sequence */
  long  begin_dna_1 = - 1;    /* offset to coding in first DNA sequence */
  long  begin_dna_2 = - 1;    /* offset to coding in second DNA sequence */

  t_aa_stats  aa_stats;		/* amino acid comparison statistics table */

  initialize_tables ( &genetic_code, &amino_acids );

  if ( argc > 1 )
  {
    open_file ( argv [ 1 ], &protein_1 );
    open_file ( argv [ 2 ], &dna_1 );
/*    begin_dna_1 = argv [ 3 ]; */  begin_dna_1 = 3;
    open_file ( argv [ 4 ], &protein_2 );
    open_file ( argv [ 5 ], &dna_2 );
/*    begin_dna_2 = argv [ 6 ]; */  begin_dna_2 = 76;
  }
  else
    parameters ( &protein_1, &dna_1, &begin_dna_1, &protein_2, &dna_2,
      &begin_dna_2 );

  initialize_statistics ( &aa_stats );

  compare_sequences ( protein_1, dna_1, begin_dna_1, protein_2, dna_2,
    begin_dna_2, &aa_stats );

  print_statistics ( &aa_stats );

  printf ("\n\nEnd main program.\n");
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

/*  printf ("get_line, '%s'\n", line); */

  if (c == EOF)  return (S_EOF);
  return (S_NORMAL);
}  /* get_line */

/* This function opens the specified file for reading. */
open_file (name, input_file)
char  name [];		/* filename */
FILE  **input_file;	/* file to open for reading */
{
  FILE  *fopen ();	/* file open function */

/*  printf ("open_file, name = '%s'\n", name); */

  *input_file = fopen (name, "r");
  if (*input_file == NULL)
  {
    printf ("WARNING: could not open the file '%s'.\n");
    return (S_OPEN_FAILED);
  }  /* if */

  return (S_NORMAL);
}  /* open_file */


/* This function initializes the genetic code tables. */
initialize_tables ( genetic_code, amino_acids )
int  genetic_code [];	/* the triplet genetic code */
char amino_acids [];	/* the amino acids */
{
  genetic_code [ 0 ] = O_PHE;
  genetic_code [ 1 ] = O_PHE;
  genetic_code [ 2 ] = O_LEU1;
  genetic_code [ 3 ] = O_LEU1;

  genetic_code [ 4 ] = O_SER1;
  genetic_code [ 5 ] = O_SER1;
  genetic_code [ 6 ] = O_SER1;
  genetic_code [ 7 ] = O_SER1;

  genetic_code [ 8 ] = O_TYR;
  genetic_code [ 9 ] = O_TYR;
  genetic_code [ 10 ] = O_TERM1;
  genetic_code [ 11 ] = O_TERM1;

  genetic_code [ 12 ] = O_CYS;
  genetic_code [ 13 ] = O_CYS;
  genetic_code [ 14 ] = O_TERM2;
  genetic_code [ 15 ] = O_TRP;

  genetic_code [ 16 ] = O_LEU2;
  genetic_code [ 17 ] = O_LEU2;
  genetic_code [ 18 ] = O_LEU2;
  genetic_code [ 19 ] = O_LEU2;

  genetic_code [ 20 ] = O_PRO;
  genetic_code [ 21 ] = O_PRO;
  genetic_code [ 22 ] = O_PRO;
  genetic_code [ 23 ] = O_PRO;

  genetic_code [ 24 ] = O_HIS;
  genetic_code [ 25 ] = O_HIS;
  genetic_code [ 26 ] = O_GLN;
  genetic_code [ 27 ] = O_GLN;

  genetic_code [ 28 ] = O_ARG1;
  genetic_code [ 29 ] = O_ARG1;
  genetic_code [ 30 ] = O_ARG1;
  genetic_code [ 31 ] = O_ARG1;

  genetic_code [ 32 ] = O_ILE;
  genetic_code [ 33 ] = O_ILE;
  genetic_code [ 34 ] = O_ILE;
  genetic_code [ 35 ] = O_MET;

  genetic_code [ 36 ] = O_THR;
  genetic_code [ 37 ] = O_THR;
  genetic_code [ 38 ] = O_THR;
  genetic_code [ 39 ] = O_THR;

  genetic_code [ 40 ] = O_ASN;
  genetic_code [ 41 ] = O_ASN;
  genetic_code [ 42 ] = O_LYS;
  genetic_code [ 43 ] = O_LYS;

  genetic_code [ 44 ] = O_SER2;
  genetic_code [ 45 ] = O_SER2;
  genetic_code [ 46 ] = O_ARG2;
  genetic_code [ 47 ] = O_ARG2;

  genetic_code [ 48 ] = O_VAL;
  genetic_code [ 49 ] = O_VAL;
  genetic_code [ 50 ] = O_VAL;
  genetic_code [ 51 ] = O_VAL;

  genetic_code [ 52 ] = O_ALA;
  genetic_code [ 53 ] = O_ALA;
  genetic_code [ 54 ] = O_ALA;
  genetic_code [ 55 ] = O_ALA;

  genetic_code [ 56 ] = O_ASP;
  genetic_code [ 57 ] = O_ASP;
  genetic_code [ 58 ] = O_GLU;
  genetic_code [ 59 ] = O_GLU;

  genetic_code [ 60 ] = O_GLY;
  genetic_code [ 61 ] = O_GLY;
  genetic_code [ 62 ] = O_GLY;
  genetic_code [ 63 ] = O_GLY;

  amino_acids [ O_ALA ] = 'A';
  amino_acids [ O_ARG1 ] = 'R';
  amino_acids [ O_ARG2 ] = 'R';
  amino_acids [ O_ASN ] = 'N';
  amino_acids [ O_ASP ] = 'D';
  amino_acids [ O_CYS ] = 'C';
  amino_acids [ O_GLN ] = 'Q';
  amino_acids [ O_GLU ] = 'E';
  amino_acids [ O_GLY ] = 'G';
  amino_acids [ O_HIS ] = 'H';
  amino_acids [ O_ILE ] = 'I';
  amino_acids [ O_LEU1 ] = 'L';
  amino_acids [ O_LEU2 ] = 'L';
  amino_acids [ O_LYS ] = 'K';
  amino_acids [ O_MET ] = 'M';
  amino_acids [ O_PHE ] = 'F';
  amino_acids [ O_PRO ] = 'P';
  amino_acids [ O_SER1 ] = 'S';
  amino_acids [ O_SER2 ] = 'S';
  amino_acids [ O_TERM1 ] = '*';
  amino_acids [ O_TERM2 ] = '*';
  amino_acids [ O_THR ] = 'T';
  amino_acids [ O_TRP ] = 'W';
  amino_acids [ O_TYR ] = 'Y';
  amino_acids [ O_VAL ] = 'V';
}  /* initialize_tables */


/* This function prompts for the unspecified parameters. */
parameters ( protein_1, dna_1, begin_dna_1,
	     protein_2, dna_2, begin_dna_2)
FILE  **protein_1;	/* amino acid sequence 1 */
FILE  **dna_1;		/* DNA sequence of protein_1 */
long  *begin_dna_1;	/* offset to the first coding base */
FILE  **protein_2;
FILE  **dna_2;
long  *begin_dna_2;
{
  int   line_index;		/* line index for get_integer */
  char  response [ MAX_LINE ];	/* user's response */

  /* check  if the first protein file name has been specified */
  while ( *protein_1 == NULL )
  {
    printf ("What is the name of the first protein sequence?");
    scanf ( "%s", response );
    open_file ( response, protein_1 );
  }  /* while */

  /* check  if the first DNA file name has been specified */
  while ( *dna_1 == NULL )
  {
    printf ("What is the name of the corresponding DNA sequence?");
    scanf ( "%s", response );
    open_file ( response, dna_1 );
  }  /* while */

  /* check  if the first DNA code offset has been specified */
  while ( *begin_dna_1 == -1 )
  {
    printf ("What is the offset to the first codon?");
    scanf ( "%s", response );
    line_index = 1;
    get_integer ( response, &line_index, begin_dna_1 );
  }  /* while */

  /* check  if the second protein file name has been specified */
  while ( *protein_2 == NULL )
  {
    printf ("What is the name of the second protein sequence?");
    scanf ( "%s", response );
    open_file ( response, protein_2 );
  }  /* while */

  /* check  if the second DNA file name has been specified */
  while ( *dna_2 == NULL )
  {
    printf ("What is the name of the corresponding DNA sequence?");
    scanf ( "%s", response );
    open_file ( response, dna_2 );
  }  /* while */

  /* check  if the second DNA code offset has been specified */
  while ( *begin_dna_2 == -1 )
  {
    printf ("What is the offset to the first codon?");
    scanf ( "%s", response );
    line_index = 1;
    get_integer ( response, &line_index, begin_dna_2 );
  }  /* while */

}  /* parameters */


/* This function initializes the amino acid comparison statistics table. */
initialize_statistics ( aa_stats )
t_aa_stats *aa_stats;	/* amino acid comparison statistics table */
{
  int  index;	/* array index */

  (*aa_stats).total = 0;
  (*aa_stats).different_codons = 0;
  (*aa_stats).identical = 0;

  for ( index = 0; index < MAX_AA_PAIRS; index++ )
  {
    (*aa_stats).transitions [ index ] = 0;
    (*aa_stats).transversions [ index ] = 0;
    (*aa_stats).same_group [ index ] = 0;
  }  /* for */
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

  if (status != S_NORMAL)  return (status);
  *character = line [ (*line_index)++ ];
  return (S_NORMAL);
}  /* next_data_character */


/* This function returns the next codon. */
next_codon (data, line, line_index, codon)
FILE *data;  /* DNA sequence data file */
char line [];  /* current data line */
int *line_index;  /* current line index */
char codon [];  /* return codon */
{
  int status = S_NORMAL;  /* function return status */
  int codon_index;  /* position in codon */

  /* reteive the codon */
  for (codon_index = 0;
    (codon_index < MAX_CODON) && (status == S_NORMAL);
    codon_index++)
    status = next_data_character (data, line, line_index,
      &(codon [ codon_index ]));

  return (status);
}  /* next_codon */


/* This function compares two aligned amino acid sequences and the third */
/* base of codons for identical amino acids. */
compare_sequences (protein_1, dna_1, begin_dna_1, protein_2, dna_2,
  begin_dna_2, aa_stats)
FILE *protein_1;  /* first aligned amino acid sequence */
FILE *dna_1;      /* DNA sequence for protein_1 */
long begin_dna_1; /* offset to coding in dna_1 */
FILE *protein_2;  /* second aligned amino acid sequence */
FILE *dna_2;      /* DNA sequence for protein_2 */
long begin_dna_2; /* offset to coding in dna_2 */
t_aa_stats  *aa_stats;	/* amino acid comparison statistics table */
{
  char codon_1 [ MAX_CODON ];  /* codon for dna_1 */
  char codon_2 [ MAX_CODON ];  /* codon for dna_2 */
  int index_1 = 0;  /* line 1 index */
  int index_2 = 0;  /* line 2 index */
  int index_3 = 0;  /* line 3 index */
  int index_4 = 0;  /* line 4 index */
  char line_1 [ MAX_LINE ];
  char line_2 [ MAX_LINE ];
  char line_3 [ MAX_LINE ];
  char line_4 [ MAX_LINE ];
  int status = S_NORMAL;  /* function return status */
  char  lower = ' ';
  char  upper = ' ';

  line_1 [ 0 ] = '\0';
  line_2 [ 0 ] = '\0';
  line_3 [ 0 ] = '\0';
  line_4 [ 0 ] = '\0';

  /* find the sequence data - advance to '..' */
  find_data ( protein_1 );
  find_data ( dna_1 );
  find_data ( protein_2 );
  find_data ( dna_2 );

/*  printf ("compare_sequences, begin_dna_1 = '%i'", begin_dna_1);
  printf (", begin_dna_2 = %i\n", begin_dna_2); */

  /* ignore the 5' non-coding regions */
  while ( begin_dna_1 > 1 )
  {
    next_data_character ( dna_1, line_2, &index_2, &upper );
    begin_dna_1--;
  }  /* while */

  while ( begin_dna_2 > 1 )
  {
    next_data_character ( dna_2, line_4, &index_4, &lower );
    begin_dna_2--;
  }  /* while */

/*  printf ("dna_1, index_2 = '%i'\n", index_2);
  printf ("line_2 '%s'\n", line_2);
  printf ("dna_2, index_4 = '%i'\n", index_4);
  printf ("line_4 '%s'\n", line_4); */

  /* compare the sequences */
  while (status == S_NORMAL)
  {
    /* get the next amino acid */
    status = next_data_character (protein_1, line_1, &index_1, &upper);
    if (status == S_NORMAL)
      status = next_data_character (protein_2, line_3, &index_3, &lower);

    /* get the DNA associated codons */
    if ((status == S_NORMAL) && (upper != '.'))
      status = next_codon (dna_1, line_2, &index_2, codon_1);
    if ((status == S_NORMAL) && (lower != '.'))
      status = next_codon (dna_2, line_4, &index_4, codon_2);

    if ((status == S_NORMAL) && (lower != '.') && (upper != '.'))
      status = compare_codons ( aa_stats, upper, lower, codon_1, codon_2 );
  }  /* while */
}  /* compare_sequences */


/* This function compares amino acids and third codon bases. */
compare_codons (aa_stats, amino_acid_1, amino_acid_2, codon_1, codon_2)
t_aa_stats *aa_stats;  /* amino acid comparison statistics table */
char amino_acid_1;
char amino_acid_2;  /* amino acids */
char codon_1 [];
char codon_2 [];  /* associated codons to amino acids */
{
  int status;  /* function return status */
  char codon_1_aa, codon_2_aa;  /* translated codons */
  int stat_index;  /* codon statistics index */

/*  printf ("compare_codons, aa1 = '%c' aa2 = '%c'", amino_acid_1,
    amino_acid_2);
  printf (" codon1 = '%c%c%c' codon2 = '%c%c%c'\n",
    codon_1 [ 0 ], codon_1 [ 1 ], codon_1 [ 2 ],
    codon_2 [ 0 ], codon_2 [ 1 ], codon_2 [ 2 ]); */

  /* translate the codons */
  status = translate ( codon_1, &codon_1_aa, &stat_index );
  if (status == S_NORMAL)
    status = translate ( codon_2, &codon_2_aa, &stat_index );
  if (status != S_NORMAL)  return (status);

  /* check file positions */
  if (amino_acid_1 != codon_1_aa)
  {
    printf ("FATAL: lost position in first DNA sequence file.\n");
    printf ("Codon = '%c%c%c' encodes '%c' mismatches '%c'\n",
      codon_1 [ BASE_1 ], codon_1 [ BASE_2 ], codon_1 [ BASE_3 ],
      codon_1_aa, amino_acid_1);
    return (S_LOST);
  }  /* off on upper sequence */

  if (amino_acid_2 != codon_2_aa)
  {
    printf ("FATAL: lost position in second DNA sequence file.\n");
    printf ("Codon = '%c%c%c' encodes '%c' mismatches '%c'\n",
      codon_2 [ BASE_1 ], codon_2 [ BASE_2 ], codon_2 [ BASE_3 ],
      codon_2_aa, amino_acid_2);
    return (S_LOST);
  }  /* off on lower sequence */

  /* compare the amino acids */
  (*aa_stats).total++;
  if (amino_acid_1 == amino_acid_2)
  {
    (*aa_stats).identical++;

    /* compare the codons */
    if ((codon_1 [ BASE_1 ] != codon_2 [ BASE_1 ]) ||
       (codon_1 [ BASE_2 ] != codon_2 [ BASE_2 ]))
    {
      (*aa_stats).different_codons++;

/*      printf ("codons different\n"); */
    }
    else
    {
      /* count the amino acids in the same coding group of codons */
      (*aa_stats).same_group [ stat_index ]++;

      /* check for identity */
      if (codon_1 [ BASE_3 ] == codon_2 [ BASE_3 ])  return (S_NORMAL);

      /* check for a transition */
      if (((codon_1 [ BASE_3 ] == ADENINE) &&
	  (codon_2 [ BASE_3 ] == GUANINE)) ||
	 ((codon_1 [ BASE_3 ] == GUANINE) &&
	  (codon_2 [ BASE_3 ] == ADENINE)) ||
	 ((codon_1 [ BASE_3 ] == CYTOSINE) &&
	  (codon_2 [ BASE_3 ] == THYMINE)) ||
	 ((codon_1 [ BASE_3 ] == THYMINE) &&
	  (codon_2 [ BASE_3 ] == CYTOSINE)))
      {
	(*aa_stats).transitions [ stat_index ]++;

/*	printf ("transition\n"); */
      }
      else
      {
	(*aa_stats).transversions [ stat_index ]++;

/*	printf ("transversion, index = '%i', count = '%i'\n",
	  stat_index, (*aa_stats).transversions [ stat_index ]); */
      }
    }  /* else */
  }  /* if */

  return (S_NORMAL);

      /* check for isoleucine */
}  /* compare_codons */


/* This function translates a codon to an amino acid. */
translate (codon, amino_acid, stat_index)
char  codon [];  /* codon to translate */
char  *amino_acid;  /* encoded amino acid of the codon */
int   *stat_index;  /* amino acid statistics index */
{
  int  position = 0;  /* triplet position */

  /* calculate codon position in the genetic code */
  switch (codon [ BASE_1 ])
  {
    case ADENINE:  position = 2 * 16; break;
    case CYTOSINE: position = 1 * 16; break;
    case GUANINE:  position = 3 * 16; break;
    case THYMINE:  position = 0 * 16; break;
    default:
      printf ("FATAL: invalid DNA base '%c'.\n", codon [ BASE_1 ]);
      return (S_BAD_BASE);
  }  /* switch */
  switch (codon [ BASE_2 ])
  {
    case 'A': position += 2 * 4; break;
    case 'C': position += 1 * 4; break;
    case 'G': position += 3 * 4; break;
    case 'T': position += 0 * 4; break;
    default:
      printf ("FATAL: invalid DNA base '%c'.\n", codon [ BASE_2 ]);
      return (S_BAD_BASE);
  }  /* switch */
  switch (codon [ BASE_3 ])
  {
    case 'A': position += 2 * 1; break;
    case 'C': position += 1 * 1; break;
    case 'G': position += 3 * 1; break;
    case 'T': position += 0 * 1; break;
    default:
      printf ("FATAL: invalid DNA base '%c'.\n", codon [ BASE_3 ]);
      return (S_BAD_BASE);
  }  /* switch */

  /* translate position in genetic code table into amino acid. */
  *stat_index = genetic_code [ position ];
  *amino_acid = amino_acids [ *stat_index ];
  return (S_NORMAL);
}  /* translate */


/* This function prints out the statistics. */
print_statistics ( aa_stats )
t_aa_stats  *aa_stats;	/* amino acid comparison statistics table */
{
  int  index;  /* array index */

  printf ("\n\n\nTotal amino acids compared %i\n\n", (*aa_stats).total);

  printf ("Identical amino acids %i\n\n", (*aa_stats).identical);

  printf ("Total identical amino acids encoded by different codons %i\n\n",
    (*aa_stats).different_codons);

  printf ("Codon    ");
  printf ("Amino acid	  Transitions	  Transversions");
  printf ("	Same Group\n");

  for ( index = 0; index < MAX_AA_PAIRS; index++ )
  {
    printf (" %s     ", codon_names [ index ]);
    printf ("     %c	       %i	       ",
      amino_acids [ index ], (*aa_stats).transitions [ index ]);
    printf (" %i	", (*aa_stats).transversions [ index ]);
    printf ("	   %i\n", (*aa_stats).same_group [ index ]);
  }  /* for */
}  /* print_statistics */
