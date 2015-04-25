
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
#define S_NORMAL	6	/* normal function termination */
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
#define O_ARG3  O_ARG2 + 1	/* xGR */
#define O_ASN	O_ARG3 + 1
#define O_ASP	O_ASN + 1
#define O_CYS	O_ASP + 1
#define O_GLN	O_CYS + 1
#define O_GLU	O_GLN + 1
#define O_GLY	O_GLU + 1
#define O_HIS	O_GLY + 1
#define O_ILE	O_HIS + 1
#define O_LEU1	O_ILE + 1	/* UUR */
#define O_LEU2	O_LEU1 + 1	/* CUN */
#define O_LEU3	O_LEU2 + 1	/* YUR */
#define O_LYS	O_LEU3 + 1
#define O_MET	O_LYS + 1
#define O_PHE	O_MET + 1
#define O_PRO	O_PHE + 1
#define O_SER1	O_PRO + 1	/* UCN */
#define O_SER2	O_SER1 + 1	/* AGY */
#define O_TERM1	O_SER2 + 1	/* UAR */
#define O_TERM2	O_TERM1 + 1	/* UGA */
#define O_TERM3 O_TERM2 + 1	/* URA */
#define O_THR	O_TERM3 + 1
#define O_TRP	O_THR + 1
#define O_TYR	O_TRP + 1
#define O_VAL	O_TYR + 1

#define MAX_GROUPS  O_VAL + 1	/* synonymous amino acids pairs */
#define MAX_CODE      64	/* number of codons */


/* DNA composition */
typedef struct {
  long adenine;
  long cytosine;
  long guanine;
  long thymine;
} t_dna;

/* comparison statistics */
typedef struct {
  long total;  /* total number of amino acids compared */
  long identical;  /* identical amino acids */
  long different_codons;  /* total a.a. encoded by different codons */
  long transitions [ MAX_GROUPS ];  /* transitions for each group */
  long transversions [ MAX_GROUPS ];  /* transversions for each group */
  long same_group [ MAX_GROUPS ];  /* number in comparison group */
  t_dna dna_1;  /* composition of the first DNA sequence */
  t_dna dna_2;  /* composition of the second DNA sequence */
  t_dna syn_1 [ MAX_GROUPS ];  /* composition for each group for dna_1 */
  t_dna syn_2 [ MAX_GROUPS ];  /* composition for each group for dna_2 */
}  t_statistics;


static  int  genetic_code [] = {  /* triplet genetic code */
  O_PHE,  O_PHE,  O_LEU1,  O_LEU1,
  O_SER1, O_SER1, O_SER1,  O_SER1,
  O_TYR,  O_TYR,  O_TERM1, O_TERM1,
  O_CYS,  O_CYS,  O_TERM2, O_TRP,

  O_LEU2, O_LEU2, O_LEU2, O_LEU2,
  O_PRO,  O_PRO,  O_PRO,  O_PRO,
  O_HIS,  O_HIS,  O_GLN,  O_GLN,
  O_ARG1, O_ARG1, O_ARG1, O_ARG1,

  O_ILE,  O_ILE,  O_ILE,  O_MET,
  O_THR,  O_THR,  O_THR,  O_THR,
  O_ASN,  O_ASN,  O_LYS,  O_LYS,
  O_SER2, O_SER2, O_ARG2, O_ARG2,

  O_VAL, O_VAL, O_VAL, O_VAL,
  O_ALA, O_ALA, O_ALA, O_ALA,
  O_ASP, O_ASP, O_GLU, O_GLU,
  O_GLY, O_GLY, O_GLY, O_GLY };

static  char  amino_acids [] = {  /* table of amino acids */
  'A', 'R', 'R', 'R', 'N', 'D', 'C', 'Q', 'E', 'G',
  'H', 'I', 'L', 'L', 'L', 'K', 'M', 'F', 'P', 'S',
  'S', '*', '*', '*', 'T', 'W', 'Y', 'V' };


static  char  *codon_names [] = {  /* codon names */
  "GCN", "CGN", "AGR", "xGr", "AAY", "GAY", "UGY", "CAR", "GAR",
  "GGN", "CAY", "AUH", "UUR", "CUN", "YUr", "AAR", "AUG", "UUY",
  "CCN", "UCN", "AGY", "UAR", "UGA", "URA", "ACN", "UGG", "UAY", "GUN" };


main ( argc, argv )
int argc;	/* number of command line arguments */
char *argv [];	/* command line arguments */
{
  FILE  *protein_1;	/* first amino acid sequence */
  FILE  *protein_2;	/* second amino acid sequence */
  FILE  *dna_1;		/* first DNA sequence */
  FILE  *dna_2;		/* second DNA sequence */
  int   begin_dna_1;    /* offset to coding in first DNA sequence */
  int   begin_dna_2;    /* offset to coding in second DNA sequence */

  int line_indx;		/* parameter line index */
  t_statistics  statistics;	/* comparison statistics table */
  int  stat;			/* function return status */

  if ( argc > 1 )
  {
    open_file ( argv [ 1 ], &protein_1 );
    open_file ( argv [ 2 ], &dna_1 );
    line_indx = 0;
    stat = get_integer ( argv [ 3 ], &line_indx, &begin_dna_1 );
    open_file ( argv [ 4 ], &protein_2 );
    open_file ( argv [ 5 ], &dna_2 );
    line_indx = 0;
    if ( stat == S_NORMAL )
      stat = get_integer ( argv [ 6 ], &line_indx, &begin_dna_2 );
  }
  else
    parameters ( &protein_1, &dna_1, &begin_dna_1, &protein_2, &dna_2,
      &begin_dna_2 );

  initialize_statistics ( &statistics );

  if ( stat == S_NORMAL )
    stat = compare_sequences ( protein_1, dna_1, begin_dna_1, protein_2,
      dna_2, begin_dna_2, &statistics );

  if ( stat == S_NORMAL )
    print_statistics ( &statistics, argv [ 2 ], argv [ 5 ] );

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


/* This function prompts for the unspecified parameters. */
parameters ( protein_1, dna_1, begin_dna_1,
	     protein_2, dna_2, begin_dna_2)
FILE  **protein_1;	/* amino acid sequence 1 */
FILE  **dna_1;		/* DNA sequence of protein_1 */
int   *begin_dna_1;	/* offset to the first coding base */
FILE  **protein_2;
FILE  **dna_2;
int   *begin_dna_2;
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


/* This function initializes the comparison statistics table. */
initialize_statistics ( statistics )
t_statistics *statistics;	/* comparison statistics table */
{
  int  index;	/* array index */

  (*statistics).total = 0;
  (*statistics).different_codons = 0;
  (*statistics).identical = 0;
  (*statistics).dna_1.adenine = 0;
  (*statistics).dna_1.cytosine = 0;
  (*statistics).dna_1.guanine = 0;
  (*statistics).dna_1.thymine = 0;
  (*statistics).dna_2.adenine = 0;
  (*statistics).dna_2.cytosine = 0;
  (*statistics).dna_2.guanine = 0;
  (*statistics).dna_2.thymine = 0;

  for ( index = 0; index < MAX_GROUPS; index++ )
  {
    (*statistics).transitions [ index ] = 0;
    (*statistics).transversions [ index ] = 0;
    (*statistics).same_group [ index ] = 0;
    (*statistics).syn_1 [ index ].adenine = 0;
    (*statistics).syn_1 [ index ].cytosine = 0;
    (*statistics).syn_1 [ index ].guanine = 0;
    (*statistics).syn_1 [ index ].thymine = 0;
    (*statistics).syn_2 [ index ].adenine = 0;
    (*statistics).syn_2 [ index ].cytosine = 0;
    (*statistics).syn_2 [ index ].guanine = 0;
    (*statistics).syn_2 [ index ].thymine = 0;
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
next_codon (data, line, line_index, codon, dna_n)
FILE *data;  /* DNA sequence data file */
char line [];  /* current data line */
int *line_index;  /* current line index */
char codon [];  /* return codon */
t_dna *dna_n;  /* composition of current dna file */
{
  int status = S_NORMAL;  /* function return status */
  int codon_index;  /* position in codon */

  /* reteive the codon */
  for (codon_index = 0;
    (codon_index < MAX_CODON) && (status == S_NORMAL);
    codon_index++)
  {
    status = next_data_character (data, line, line_index,
      &(codon [ codon_index ]));
    if (status == S_NORMAL)
      record_base ( codon [ codon_index ], dna_n );
  }  /* for */

  return (status);
}  /* next_codon */


/* This function records the DNA base composition for a base. */
record_base ( base, dna_n )
char base;	/* current DNA base to record */
t_dna *dna_n;	/* composition of current dna file */
{
  switch (base)
  {
    case ADENINE:  (*dna_n).adenine++;  break;
    case CYTOSINE: (*dna_n).cytosine++; break;
    case GUANINE:  (*dna_n).guanine++;  break;
    case THYMINE:  (*dna_n).thymine++;  break;
    default: printf ("INFO: unusual DNA base in file 1 '%c'.\n", base);
  }  /* switch */
}  /* record_base */


/* This function compares two aligned amino acid sequences and the third */
/* base of codons for identical amino acids. */
compare_sequences (protein_1, dna_1, begin_dna_1, protein_2, dna_2,
  begin_dna_2, statistics)
FILE *protein_1;  /* first aligned amino acid sequence */
FILE *dna_1;      /* DNA sequence for protein_1 */
int  begin_dna_1; /* offset to coding in dna_1 */
FILE *protein_2;  /* second aligned amino acid sequence */
FILE *dna_2;      /* DNA sequence for protein_2 */
int  begin_dna_2; /* offset to coding in dna_2 */
t_statistics  *statistics;	/* comparison statistics table */
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

  /* ignore the 5' non-coding regions */
  while ( begin_dna_1 > 1 )
  {
    next_data_character ( dna_1, line_2, &index_2, &upper );
    record_base ( upper, &((*statistics).dna_1) );
    begin_dna_1--;
  }  /* while */

  while ( begin_dna_2 > 1 )
  {
    next_data_character ( dna_2, line_4, &index_4, &lower );
    record_base ( lower, &((*statistics).dna_2) );
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
      status = next_codon (dna_1, line_2, &index_2, codon_1,
	&((*statistics).dna_1) );
    if ((status == S_NORMAL) && (lower != '.'))
      status = next_codon ( dna_2, line_4, &index_4, codon_2,
	&((*statistics).dna_2) );

    if ((status == S_NORMAL) && (lower != '.') && (upper != '.'))
      status = compare_codons ( statistics, upper, lower, codon_1, codon_2 );
  }  /* while */
  if ( status == S_EOF ) return (  S_NORMAL );
  return ( status );
}  /* compare_sequences */


/* This function records the syn. base change. */
record_difference ( base_1, base_2, statistics, stat_index )
char base_1;	/* DNA base of codon from DNA file 1 */
char base_2;	/* DNA base of codon from DNA file 2 */
t_statistics *statistics;	/* comparison statistics table */
int stat_index;			/* statistics table index */
{
  /* count the amino acids in the same coding group of codons */
  (*statistics).same_group [ stat_index ]++;

  /* check for identity */
  if ( base_1 == base_2 )  return (S_NORMAL);

  /* record the first DNA file base composition */
  switch ( base_1 )
  {
    case ADENINE:  (*statistics).syn_1 [ stat_index ].adenine++; break;
    case CYTOSINE: (*statistics).syn_1 [ stat_index ].cytosine++; break;
    case GUANINE:  (*statistics).syn_1 [ stat_index ].guanine++; break;
    case THYMINE:  (*statistics).syn_1 [ stat_index ].thymine++; break;
    default: printf ( "INFO: unusual DNA base '%c' in file 1.\n", base_1 );
  }  /* switch */

  /* record the second DNA file base composition */
  switch ( base_2 )
  {
    case ADENINE:  (*statistics).syn_2 [ stat_index ].adenine++; break;
    case CYTOSINE: (*statistics).syn_2 [ stat_index ].cytosine++; break;
    case GUANINE:  (*statistics).syn_2 [ stat_index ].guanine++; break;
    case THYMINE:  (*statistics).syn_2 [ stat_index ].thymine++; break;
    default: printf ( "INFO: unusual DNA base '%c' in file 2.\n", base_2 );
  }  /* switch */

  /* check for a transition */
  if (((base_1 == ADENINE)  && (base_2 == GUANINE)) ||
      ((base_1 == GUANINE)  && (base_2 == ADENINE)) ||
      ((base_1 == CYTOSINE) && (base_2 == THYMINE)) ||
      ((base_1 == THYMINE)  && (base_2 == CYTOSINE)))
    (*statistics).transitions [ stat_index ]++;
  else
    (*statistics).transversions [ stat_index ]++;
}  /* record_difference */


/* This function compares amino acids and third codon bases. */
compare_codons (statistics, amino_acid_1, amino_acid_2, codon_1, codon_2)
t_statistics *statistics;  /* comparison statistics table */
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
  (*statistics).total++;
  if (amino_acid_1 == amino_acid_2)
  {
    (*statistics).identical++;

    /* compare the codons */
    if ((codon_1 [ BASE_1 ] != codon_2 [ BASE_1 ]) ||
	(codon_1 [ BASE_2 ] != codon_2 [ BASE_2 ]))
    {
      /* check for leucine or arginine */
      if ((codon_1 [ BASE_2 ] == codon_2 [ BASE_2 ]) &&
	  (codon_1 [ BASE_3 ] == codon_2 [ BASE_3 ]))
      {
	if (amino_acid_1 == 'R') stat_index = O_ARG3;
	if (amino_acid_1 == 'L') stat_index = O_LEU3;
	record_difference ( codon_1 [ BASE_1 ], codon_2 [ BASE_1 ],
	  statistics, stat_index );
      }
      else
      {
	if ((codon_1 [ BASE_1 ] == codon_2 [ BASE_1 ]) &&
	    (codon_1 [ BASE_3 ] == codon_2 [ BASE_3 ]))
	{
	  if (amino_acid_1 == '*') stat_index = O_TERM3;
	  record_difference ( codon_1 [ BASE_2 ], codon_2 [ BASE_2 ],
	    statistics, stat_index);
	}
	else
	  (*statistics).different_codons++;  /* 2 base hit */
      }
    }
    else
      record_difference ( codon_1 [ BASE_3 ], codon_2 [ BASE_3 ],
	statistics, stat_index );
  }  /* if identical amino acids */

  return (S_NORMAL);
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
    case ADENINE: position += 2 * 4; break;
    case CYTOSINE: position += 1 * 4; break;
    case GUANINE: position += 3 * 4; break;
    case THYMINE: position += 0 * 4; break;
    default:
      printf ("FATAL: invalid DNA base '%c'.\n", codon [ BASE_2 ]);
      return (S_BAD_BASE);
  }  /* switch */
  switch (codon [ BASE_3 ])
  {
    case ADENINE: position += 2 * 1; break;
    case CYTOSINE: position += 1 * 1; break;
    case GUANINE: position += 3 * 1; break;
    case THYMINE: position += 0 * 1; break;
    default:
      printf ("FATAL: invalid DNA base '%c'.\n", codon [ BASE_3 ]);
      return (S_BAD_BASE);
  }  /* switch */

  /* translate position in genetic code table into amino acid. */
  *stat_index = genetic_code [ position ];
  *amino_acid = amino_acids [ *stat_index ];
  return (S_NORMAL);
}  /* translate */


/* This function adds the base composition of a group to a sum. */
add_group ( index, statistics, group_x_1, group_x_2 )
int  index;			/* statistics table index */
t_statistics  *statistics;	/* comparison statistics table */
t_dna  *group_x_1;		/* summary composition for group x */
t_dna  *group_x_2;		/* summary group for dna file 2 */
{
  (*group_x_1).adenine  += (*statistics).syn_1 [ index ].adenine;
  (*group_x_1).cytosine += (*statistics).syn_1 [ index ].cytosine;
  (*group_x_1).guanine  += (*statistics).syn_1 [ index ].guanine;
  (*group_x_1).thymine  += (*statistics).syn_1 [ index ].thymine;
  (*group_x_2).adenine  += (*statistics).syn_2 [ index ].adenine;
  (*group_x_2).cytosine += (*statistics).syn_2 [ index ].cytosine;
  (*group_x_2).guanine  += (*statistics).syn_2 [ index ].guanine;
  (*group_x_2).thymine  += (*statistics).syn_2 [ index ].thymine;
}  /* add_group */

print_composition ( group, dna_group )
char  group;		/* group letter - Y, R, or N */
t_dna  *dna_group;	/* dna composition of a group */
{
  if ( group != 'Y' )
    printf (" %5d ", (*dna_group).adenine);
  else  printf ("       ");
  if ( group != 'R' )
    printf (" %5d ", (*dna_group).thymine);
  else  printf ("       ");
  if ( group != 'Y' )
    printf (" %5d ", (*dna_group).guanine);
  else  printf ("       ");
  if ( group != 'R' )
    printf (" %5d ", (*dna_group).cytosine);
  else  printf ("       ");
  printf ("   ");
}  /* print_composition */


/* This function prints out the statistics. */
print_statistics ( statistics, dna_file_1, dna_file_2 )
t_statistics  *statistics;	/* comparison statistics table */
char dna_file_1 [];		/* name of the first DNA file */
char dna_file_2 [];		/* name of the second DNA file */
{
  /* group composition summary for the 3rd base of codons */
  t_dna  group_n_1 = { 0, 0, 0, 0 };
  t_dna  group_n_2 = { 0, 0, 0, 0 };
  t_dna  group_r_1 = { 0, 0, 0, 0 };
  t_dna  group_r_2 = { 0, 0, 0, 0 };
  t_dna  group_y_1 = { 0, 0, 0, 0 };
  t_dna  group_y_2 = { 0, 0, 0, 0 };
  int  index;  /* array index */

  printf ("\n\n\nTotal amino acids compared %i\n\n",
    (*statistics).total);

  printf ("Identical amino acids %i\n\n", (*statistics).identical);

  printf ("Total identical amino acids encoded by different codons %i\n\n",
    (*statistics).different_codons);

  printf ("       Amino  Trans-   Trans-    Same\n");
  printf ("Codon  Acid   sitions  versions  Group\n");

  /* print out the syn. base changes statistics */
  for ( index = 0; index < MAX_GROUPS; index++ )
  {
    printf (" %3s  ", codon_names [ index ]);
    printf ("   %c    %5d   ",
      amino_acids [ index ], (*statistics).transitions [ index ]);
    printf (" %5d  ", (*statistics).transversions [ index ]);
    printf (" %5d\n", (*statistics).same_group [ index ]);
  }  /* for */

  printf ("\n\n");
  printf ("       Amino        %14s                %14s\n",
    dna_file_1, dna_file_2);
  printf ("Codon  Acid       A      T      G      C    ");
  printf ("     A      T      G      C\n");
  /* print out the DNA base compositions */
  for ( index = 0; index < MAX_GROUPS; index++ )
  {
    printf (" %3s  ", codon_names [ index ]);
    printf ("   %c   ", amino_acids [ index ]);
    print_composition ( codon_names [ index ] [ BASE_3 ],
      &((*statistics).syn_1 [ index ]) );
    print_composition ( codon_names [ index ] [ BASE_3 ],
      &((*statistics).syn_2 [ index ]) );
    printf ("\n");

    switch ( codon_names [ index ] [ BASE_3 ] )
    {
      case 'N': add_group ( index, statistics, &group_n_1, &group_n_2 );
	break;
      case 'R': add_group ( index, statistics, &group_r_1, &group_r_2 );
	break;
      case 'Y': add_group ( index, statistics, &group_y_1, &group_y_2 );
      default: ;
    }  /* switch */
  }  /* for */

  printf ("\nComposition  ");
  print_composition ( 'N', &((*statistics).dna_1) );
  print_composition ( 'N', &((*statistics).dna_2) );
  printf ("\n");

  printf ("\nSummary xxY  ");
  print_composition ( 'Y', &group_y_1 );
  print_composition ( 'Y', &group_y_2 );
  printf ("\n");
  printf ("Summary xxR  ");
  print_composition ( 'R', &group_r_1 );
  print_composition ( 'R', &group_r_2 );
  printf ("\n");
  printf ("Summary xxN  ");
  print_composition ( 'N', &group_n_1 );
  print_composition ( 'N', &group_n_2 );
  printf ("\n");

}  /* print_statistics */
