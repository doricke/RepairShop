

#include <stdio.h>
#include <math.h>

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

#define S_END_OF_LINE	7	/* end of line encountered */
#define S_EOF		1	/* End Of File encountered */
#define S_LOST		2	/* Inconsistant file position */
#define S_NO_INTEGER	3	/* no integer on current line */
#define S_NOT_FOUND     8       /* target not found in search */
#define S_NORMAL	6	/* normal function termination */
#define S_OPEN_FAILED	5	/* file open failed */
#define S_UNCHECKED     9       /* Paradox mutation record not checked */
#define S_ERROR         10      /* Error in Paradox mutation record */
#define S_TABLE_OVERFLOW  4     /* Table overflow */
#define S_NO_DNA_FILE     11    /* No DNA sequence file */
#define S_NO_REAL         12    /* No real number on current line */

#define ADENINE  'A'
#define CYTOSINE 'C'
#define GUANINE  'G'
#define THYMINE  'T'

#define SEQ_GAP  '.'            /* alignment gap in sequence */
#define SEQ_SKIP 'J'            /* sequence skip character */

#define BASE_1          0       /* first base position in a codon */
#define BASE_2          1       /* second base position in a codon */
#define BASE_3          2       /* third base position in a codon */

#define ON              1
#define OFF             0
#define BRIEF_MODE      ON      /* Brief mode: 1 (ON) & 0 (OFF) */

#define	MAX_BASES	400000	/* maximum number of sequence bases */
#define MAX_PER_LINE    70      /* maximum bases printed on a line */

#define AFTER_DELETION	10	/* bases to print after a deletion */
#define BEFORE_DELETION	10	/* bases to print before a deletion */

#define MAX_CODON       3       /* number of bases in a codon */
#define MAX_CODON_NUMBER 99999  /* maximum codon number */
#define MAX_LINE        132	/* maximum line length */
#define SUPER_LINE      2132	/* maximum super line length */
#define MAX_SEQUENCES	200	/* Maximum number of protein sequences */

#define MAX_AA_NAMES    21      /* Number of names of amino acids */
#define MAX_ATOM_NAME   6       /* Maximum size of atom name */
#define MAX_ATOMS       50000   /* Maximum number of atoms in PDB file */
#define MAX_CHANGE      4       /* Maximum characters in short mutation G:A */
#define MAX_COORDINATE  8       /* Maximum atom coordinate name */
#define MAX_FIELD       256     /* Maximum Paradox field size */
#define MAX_GENE_NAME   6       /* Maximum Paradox gene name size */
#define MAX_GENES       200     /* Maximum number of different genes */
#define MAX_LOCATION    26      /* Maximum size of P53 database location field */
#define MAX_NAMES_LINE  10      /* Maximum .csf names on a line */
#define MAX_RANDOM      65535   /* 2^16-1 - maximum random number from rand */
#define MAX_RESIDUES    5000    /* Maximum number of PDB file residues */
#define MAX_RESIDUE_NAME 10     /* Largest coordinate name */
#define MAX_SIZES       3       /* Maximum number of cluster sizes to keep */
#define MAX_TRIALS      10000   /* Maximum number of random coloring trials */

#define MIN_SURFACE_AREA  0    /* Minimum DSSP surface area */

#define	TRUE		1	/* Boolean flags */
#define FALSE		2

#define ALL_BITS	0x7FFFF	/* All mask bits */

#define	NO_BITS		0	/* Zero mask bits */

#define MUTATION	-1	/* Reserved group number of mutation data */
#define MUTATION_DNA    -2      /* Reserved mutation target DNA sequence */
#define UNKNOWN         -3      /* Reserved unknown protein sequence */
#define DNA_SEQUENCE    -4      /* Reserved DNA sequence for previous protein */
#define PDB_FILE        -5      /* Reserved PDB coordinate file */
#define MUTATIONS_FILE  -6      /* Reserved Paradox mutations file */
#define CODONS_TABLE    -7      /* Reserved Paradox codons table */
#define COMPARE_FILE    -8      /* Reserved N x N comparison output file */
#define DOT_FILE        -9      /* Reserved QUANTA surface dot file */
#define DSSP_FILE       -10     /* Reserved DSSP surface area file */

#define MUTATION_GROUP	0	/* Reserved mutation target group number */
#define MUTATION_SEQ	0	/* Reserved mutation target sequence number */

#define SOURCE          0       /* Source base or amino acid A:G */
#define CHANGE          2       /* Mutated base or amino acid A:G */

#define GENERIC		0	/* Generic amino acid */
#define CONS_GENERIC	1	/* Conservately generic amino acid */
#define PARTIAL         2       /* Partially generic amino acid */
#define CONS_PARTIAL	3	/* Conservately partial generic */
#define SPECIFIC	4	/* Gene specific amino acid */
#define CONS_SPECIFIC	5	/* Conservately specific amino acid */
#define NON_CONSERVED	6	/* Non-conserved amino acid */
#define OVERALL         7       /* Overall comparison category */

#define CATEGORIES	4	/* Number of conservation categories */
#define MAX_CONSERVATION	8	/* Number of conservation groups */

#define DIVERGE_MAMMALS   160.0   /* Appr. divergence time of unrelated mammals */
#define X_MUTATION_RATE   0.75    /* 3 X chromosomes for 4 autosomes */
#define RHO_MAMMALS       0.0021  /* Mammalian rho constant */
#define RHO_VERTEBRATES   0.0010  /* Vertebrate rho constant */
#define X_CHROMOSOME      1       /* X chromosome descriptor flag */
#define MUTATION_RATE     RHO_MAMMALS * 1.3    /* Default mutation rate per million years */

#define S_LEU  0  /* second codon for Leucine */
#define S_ARG  1  /* second codon for Arginine */
#define S_SER  2  /* second codon for Serine */

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

#define POS_AMINO_ACID  17      /* Position of amino acid name in PDB file */
#define POS_RESIDUE_NO  22      /* Position of amino acid residue number in PDB file. */

#define NOT_SPECIFIC    -1      /* Flag indicating no specific cluster */
#define MIN_CLUSTER      3      /* Minimum number of contiguous specifics */
#define MIN_CLUSTER2   2.5      /* Minimum number of contiguous specifics */
#define MIN_SCORE      0.9      /* Minimum cluster score before resetting */

/******************************************************************************/

/* base composition table */
typedef struct {
  long	count [ 'Z' - 'A' + 1 ];		/* base compositions */
} t_composition;


/* Amino acid conservation groups. */
typedef struct {
  long	category [ MAX_CONSERVATION ];	/* generics, paritals, ... */
}  t_conservation;


/* Single mutation from the Paradox mutations file. */
typedef struct {
  long   key;                                /* record key number */
  char   gene_name [ MAX_GENE_NAME ];        /* gene name */
  long   nucleotide_begin;                   /* first nucleotide of mutation */
  long   nucleotide_end;                     /* last nucleotide of mutation */
  char   base_change [ MAX_CHANGE ];         /* G:A base change */
  char   nucleotide_change [ MAX_FIELD ];    /* nucleotide base change */
  long   codon_number;                       /* first codon mutated */
  long   codon_number_end;                   /* last codon mutated */
  char   codon_change [ MAX_CHANGE ];        /* codon mutation */
  int    codon_base;                         /* base of codon mutated */
  char   mutation_type;                      /* type of mutation */
  long   reference;                          /* paper reference number */
  char   splicing;                           /* RNA splicing flag */
  char   mutation_code;                      /* mutation_polymorphism_error */
  char   location [ MAX_LOCATION ];          /* location of mutation */
  char   length [ MAX_LOCATION ];            /* length of mutation */
}  t_mutation;


/* Summary table of mutations against the first sequence. */
typedef struct {
  float	 cg_amino_acids [ MAX_CONSERVATION ];	/* CpG amino acid totals */
  float	 nn_amino_acids [ MAX_CONSERVATION ];	/* non-CpG amino acid totals */
  t_conservation  non_sites_cpg;	/* # of nonsense CpG mutation sites */
  t_conservation  non_total_cpg;	/* total # of CpG nonsense mutations */
  t_conservation  non_sites;	/* number of non-CpG nonsense mutation sites */
  t_conservation  non_total;	/* total number of non-CpG nonsense mutations */
  t_conservation  mis_sites_cpg;	/* # of missense CpG mutation sites */
  t_conservation  mis_total_cpg;	/* total # of CpG missense mutations */
  t_conservation  mis_sites;	/* number of non-CpG missense mutation sites */
  t_conservation  mis_total;	/* total number of non-CpG missense mutations */
  t_conservation  total;	/* total number of mutations */
  t_composition	  category [ MAX_CONSERVATION ];	/* aa conservation */
  int             pn;                             /* partialness index */
  long            genes;                          /* number of genes */
  long            sites_i       [ MAX_GENES ];    /* missense sites by Pn */
  long            sites_c       [ MAX_GENES ];    /* missense sites by Pn */
  long            missense_i    [ MAX_GENES ];    /* missense mutations by Pn */
  long            missense_c    [ MAX_GENES ];    /* missense mutations by Pn */
  long            amino_acids_i [ MAX_GENES ];    /* amino acids by Pn */
  long            amino_acids_c [ MAX_GENES ];    /* amino acids by Pn */
}  t_mutations;


/* sequence information for one protein sequence file */
typedef struct {
  FILE	*protein;		/* internal C file */
  long  amino_acids;            /* total number of amino acids */
  char	file_name [ MAX_LINE ];	/* name of the protein file */
  char	gene_name [ MAX_LINE ];	/* gene name */
  int	group;			/* protein family group identifier */
  int   descriptor;             /* file descriptor number */
  char  class;                  /* Phylogenetic class */
  int   diverge;                /* divergence time */
  int   inc_diverge;            /* incremental divergence time */
  char	line [ MAX_LINE ];	/* current line of the file */
  char	pep_char;		/* current amino acid */
  int	line_index;		/* line index of pep_char */
  long	cons_mask;		/* conservative substitution mask */
  long	ident_mask;		/* identity mask */
  char  previous_codon [ MAX_CODON + 1 ];  /* previous codon */
  char  current_codon [ MAX_CODON + 1 ];   /* current codon */
  char  next_codon [ MAX_CODON + 1 ];      /* next codon */
  int   novel;                  /* new changes due to this sequence */
} t_sequence;

/* table of protein sequences */
typedef struct {
  int		total;			/* number of files */
  long          codon_number;           /* current codon number */
  int           codons_table_index;     /* codon table index */
  int           mutations_index;        /* Paradox mutations table index */
  double        f_change;               /* fraction of changable residues */
  t_sequence	seq [ MAX_SEQUENCES ];	/* protein sequence information */
} t_files;


/* table of protein family group information */
typedef struct {
  int	total;				/* number of groups */
  long	spec_cons [ MAX_SEQUENCES ];	/* conservative family specifics mask */
  long  spec_ident [ MAX_SEQUENCES ];	/* identity family specifics mask */
  long	other_cons [ MAX_SEQUENCES ];	/* conservative non-family mask */
  long	other_ident [ MAX_SEQUENCES ];	/* identify non-family mask */
  char	identical [ MAX_SEQUENCES ];	/* group specific amino acid */
  int	conservation [ MAX_SEQUENCES ]; /* amino acid conservation category */
  int   g_level [ MAX_SEQUENCES ];      /* class conservation level */
  int   g_genes [ MAX_SEQUENCES ];      /* conserved in N genes */
  int   partial_index;                  /* number of partials with main */
} t_groups;


/* Single sequence comparison */
typedef struct {
  int  identity;    /* number of identity matches between two sequences */
  int  compared;    /* total number of amino acids compared */
} t_ident;

/* Totals of amino acid comparisons of unknown to all sequences */
typedef struct {
  int  identity [ MAX_CONSERVATION ];        /* identity amino acid matches */
  int  conservation [ MAX_CONSERVATION ];    /* conservative substitutions only */
  int  compared [ MAX_CONSERVATION ];        /* total amino acids compared */
  int  lost     [ MAX_CONSERVATION ];        /* residues lost on each level */
  t_ident  N [ MAX_SEQUENCES ];              /* N x N comparison stats */
} t_identify;

/* Unknown protein sequence statistics */
typedef struct {
  int    unknown_seq;    /* index of unknown sequence */
  int    total;          /* number of known protein sequences */
  t_identify  sequences [ MAX_SEQUENCES ];    /* each seq comparison stats */
} t_comparison;


/* One PDB file residue - single and three letter names. */
typedef struct {
  char  amino_acid;                   /* single letter code for amino acid */
  int   surface_area;                 /* DSSP file surface area */
  char  name [ MAX_RESIDUE_NAME ];    /* Crystal coordinate 3 letter name */
  char  coordinate [ MAX_RESIDUE_NAME ];    /* Crystal coordinate name */
} t_residue;

/* Table of PDB file amino acid residues. */
typedef struct {
  long       total;                  /* number of residues in the PDB file. */
  int        min_area;               /* minimum surface area */
  float      resolution;             /* crystal structure resolution */
  t_residue  aa [ MAX_RESIDUES ];    /* table of amino acid names */
} t_residues;


/* One PDB atom coordinate information. */
typedef struct {
  long    atom_number;                      /* PDB atom number */
  char    atom_name [ MAX_ATOM_NAME ];      /* atom name */
  char    residue;    /* amino acid residue (single letter code ) */
  char    coordinate [ MAX_COORDINATE ];    /* atom coordinate name */
  double  x;                                /* x coordinate */
  double  y;                                /* y coordinate */
  double  z;                                /* z coordinate */
  long    residue_index;                    /* residues table index */
  int     dots;                             /* number of dots per atom */
} t_atom;


/* Table of PDB atom coordinate information. */
typedef struct {
  long    total;                 /* number of atoms in the atoms table */
  t_atom  atom [ MAX_ATOMS ];    /* array of atoms */
} t_atoms;


/* Array of observed missense mutations. */
typedef struct {
  int  substitution [ 'Z' - 'A' + 1 ];    /* each type of substitution */
} t_missense;


/* Table of main sequence of interest and observed conservation levels */
typedef struct {
  long  total;                              /* sequence length */
  char  amino_acid [ MAX_RESIDUES ];        /* amino acid sequence */
  char  conservation [ MAX_RESIDUES ];      /* observed conservation levels */
  long  previous [ MAX_RESIDUES ];          /* previous cluster index */
  long  next [ MAX_RESIDUES ];              /* next cluster index */
  short mark [ MAX_RESIDUES ];              /* marking flag */
  long  surface [ MAX_RESIDUES ];           /* number of surface atoms */
  long  dots [ MAX_RESIDUES ];              /* number of surface dots */
  
  /* I - identity, C - conservative, N - nonconserved */
  char  comparison [ MAX_RESIDUES ];

  short  genes [ MAX_RESIDUES ];    /* number of genes conserved in */
  char   level [ MAX_RESIDUES ];    /* level of conservation in I1 & C1 */

  t_missense  missense [ MAX_RESIDUES ];    /* table of missense mutations */
} t_main_sequence;


/* DNA or RNA base */
typedef struct {
  unsigned	bits : 4;	/* bit map of nucleotide base */
} t_base;

/* DNA or RNA sequence */
typedef struct {
  long		length;			/* length of sequence */
  char		filename [ MAX_LINE ];	/* sequence filename */
  t_base	base [ MAX_BASES ];	/* Each base of sequence */
} t_DNA_sequence;


/* Histogram of largest surface clusters. */
typedef struct {
  int  trial;    /* trial */
  int  size;     /* Deceasing cluster sizes */
  int  atoms;    /* Number of surface atoms */
  int  area;     /* surface area of the cluster */
} t_cluster;


/* Histogram of largest surface clusters. */
typedef struct {
  t_cluster  cluster [ MAX_SIZES ];    /* trial, size, atoms, surface area */
} t_size;

/* Histograms of largest surface clusters for all random coloring trials. */
typedef struct {
  t_size  sizez [ MAX_TRIALS + 1 ];    /* Array of cluster sizes for each trial */
  t_size  atomz [ MAX_TRIALS + 1 ];    /* Array of cluster sizes for each trial */
  t_size  areas [ MAX_TRIALS + 1 ];    /* Array of cluster sizes for each trial */
} t_sizes;

/******************************************************************************/


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

static  char  amino_acids_1 [] = {  /* Single letter codes for amino acids */
  'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'E', 'G',
  'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 
  'T', 'W', 'Y', 'V', 'Z' }; 

static  char  *amino_acids_3 [] = {  /* Three letter codes for amino acids */
  "ALA", "ARG", "ASN", "ASP", "CYS", "GLA", "GLN", "GLU", "GLY",
  "HIS", "ILE", "LEU", "LYS", "MET", "PHE", "PRO", "SER",
  "THR", "TRP", "TYR", "VAL", "ZZZ" };


static  char  amino_acids [] = {  /* table of amino acids */
  'A', 'R', 'R', 'R', 'N', 'D', 'C', 'Q', 'E', 'G',
  'H', 'I', 'L', 'L', 'L', 'K', 'M', 'F', 'P', 'S',
  'S', '*', '*', '*', 'T', 'W', 'Y', 'V' };


/* Second codons for six codon amino acids. */
static  char  *codons_2 [] = { "CTN", "CGN", "TCN" };


static  char  *codon_names [] = {  /* codon names */
  "GCN", "CGN", "AGR", "xGr", "AAY", "GAY", "UGY", "CAR", "GAR",
  "GGN", "CAY", "AUH", "UUR", "CUN", "YUr", "AAR", "AUG", "UUY",
  "CCN", "UCN", "AGY", "UAR", "UGA", "URA", "ACN", "UGG", "UAY", "GUN" };

static	char	*codons [] = {
/*  A      B      C      D      E      F      G      H      I      J */
  "GCN", "   ", "TGY", "GAY", "GAR", "TTY", "GGN", "CAY", "ATH", "   ",
/*  K      L      M      N      O      P      Q      R      S      T */
  "AAR", "TTR", "ATG", "AAY", "   ", "CCN", "CAR", "AGR", "AGY", "ACN",
/*  U      V      W      X      Y      Z */
  "   ", "GTN", "TGG", "   ", "TAY", "   " };


/* Conservation category letter code. */
static  char	cons_letters [] = { 'G', 'g', 'P', 'p', 'S', 's', 'N' };

/* Conservation .csf colors. */
static  int	csf_color [] = { 4, 4, 2, 2, 6, 6, 1 };

/* Largest clusters .csf colors. */
static  int     size_color [] = { 4, 2, 7, 5, 3, 8, 9, 10, 11, 12, 13, 14 };

/* Conservation category names. */
static  char	*cons_names [] = {
  "Identity     Generics ", "Conservation Generics ", "Identity     Partials ",
  "Conservation Partials ", "Identity     Specifics", "Conservation Specifics",
  "Non-conserved         ", "Overall               " };


static	char	*categories [] = { "Generics    ", "P. Generics ", 
    "Specifics   ", "Nonconserved" };


/* overlapping bit specification indicates allowable conservative */
/* substitutions. */
static	long	cons_masks [] = { /* table of amino acid masks */
/* A   B  C       D     E     F    G       H       I    J  K      L */
  0x2, 0, 0x1000, 0x30, 0x90, 0x8, 0x2000, 0x4000, 0x1, 0, 0x100, 0x1, 
/* M     N     O  P        Q     R      S    T    U  V    W        X */
  0x800, 0x60, 0, 0x10000, 0xc0, 0x100, 0x6, 0x4, 0, 0x1, 0x20000, ALL_BITS,
/* Y   Z */
  0x8, 0 };

static	long	ident_masks [] = { /* table of amino acid masks */
/* A   B  C      D    E     F     G     H     I  J      K      L */
  0x1, 0, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80, 0, 0x100, 0x200, 
/* M      N     O   P       Q       R       S       T       U   V */
  0x400, 0x800, 0, 0x1000, 0x2000, 0x4000, 0x8000, 0x10000, 0, 0x20000, 
/* W        X         Y       Z */
  0x20000, ALL_BITS, 0x40000, 0 };


static  float  nnc [] = {  /* table of CpG fractions for codons NNC GXX */
/* UU[C] CU   AU  GU UC CC AC GC  UA[C]  CA    AA    GA    UG  CG   AG  GG */
   0.25, 0, 0.125, 0, 0, 0, 0, 0, 0.25, 0.25, 0.25, 0.25, 0.25, 0, 0.25, 0 };

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

/* Inter-atomic van der Waals radii. */
static	double  van_der_Waals [] = {
/*  A      B      C      D      E      F      G      H      I      J */
    0,     0,   1.77,    0,     0,     0,     0,    0.9,    0,     0,
/*  K      L      M      N      O      P      Q      R      S      T */
    0,     0,     0,    1.6,  1.52,   1.8,    0,     0,    1.8,    0,
/*  U      V      W      X      Y      Z */
    0,     0,     0,     0,     0,     0
  };


/******************************************************************************/
main ( )
{
  t_files  	   files;		  /* table of sequence files */
  FILE		   *file_names = NULL;	  /* list of file names to process */
  t_groups	   groups;		  /* */
  char		   in_name [ MAX_LINE ];  /* file name of file of file names */
  t_main_sequence  main_sequence;	  /* main amino acid sequence & EC */
  char		   name [ MAX_LINE ];	  /* current protein file name */
  int  		   stat = S_NORMAL;	  /* function return status */
  t_mutations	   method_I;		  /* Old method of conservation definition */
  t_mutations	   method_II;		  /* New method of conservation definition */
  t_residues       residues;              /* PDB residue and name array */
  t_atoms          atoms;                 /* atoms table */


  prompt_files ( in_name, &file_names );

  open_files ( file_names, &files, TRUE );

  printf ( "\n\n" );

  scan_sequences ( &files, &groups, &main_sequence, &method_I, &method_II );

  user_randomize ();    /* initialize the random number generator */

  /* Process the PDB file if there is one. */
  process_pdb ( &files, &main_sequence, &residues, &atoms );

  /* Read in the DSSP file. */
  read_DSSP ( &files, &residues );

  /* Generate random specific .csf and .dsf files. */
  random_csfs ( &files, &main_sequence, &residues, &atoms );

  printf ("\nEnd of program.\n");
}  /* main */


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


/******************************************************************************/
/* This function opens all of the files in the file of filenames. */
open_files ( file_of_files, files, to_print )
FILE	*file_of_files;	/* file containing list of filenames */
t_files	*files;		/* table of sequences */
int     to_print;       /* flag indicating to print list of filenames */
{
  FILE  *fopen ();	        /* file open function */
  int	line_index = 0;		/* current character index in line */
  int   stat;                   /* current line status */
  int	status = S_NORMAL;	/* status of called functions */
  char	line [ MAX_LINE ];	/* current file of filenames line */
  char  token [ MAX_LINE ];     /* current character token */


  (*files).total = 0;	              /* initially no files */
  (*files).codon_number = 0;          /* initialize */
  (*files).codons_table_index = 0;    /* initialize */
  (*files).mutations_index = -1;      /* initialize */
  (*files).f_change = 0;

  /* Read until end of file is reached. */
  while ( status != S_EOF )
  {
    status = get_line ( file_of_files, line );
    line_index = 0;

    if (( line [ 0 ] != '\0' ) && ( status == S_NORMAL ))
    {
      (*files).seq [ (*files).total ].line [ 0 ] = '\0';    /* initialize */
      (*files).seq [ (*files).total ].gene_name [ 0 ] = '\0';

      status = get_token ( line, &line_index,
	&((*files).seq [ (*files).total ].file_name) );

      if ( status == S_NORMAL )
	status = get_integer ( line, &line_index,
	  &((*files).seq [ (*files).total ].group) );

      (*files).seq [ (*files).total ].descriptor = 0;

      stat = status;
      if ( status == S_NORMAL )
        stat = get_integer ( line, &line_index, 
            &((*files).seq [ (*files).total ].descriptor) );
  
      /* Get the phylogenetic class. */
      if ( ( (*files).seq [ (*files).total ].group >= 0 ) ||
           ( (*files).seq [ (*files).total ].group == PDB_FILE ) )
      {
        (*files).seq [ (*files).total ].class = ' ';
        if ( stat == S_NORMAL )
          stat = get_token ( line, &line_index, token );
        if ( stat == S_NORMAL )
          (*files).seq [ (*files).total ].class = token [ 0 ];

        /* Get the phylogenetic divergence time. */
        (*files).seq [ (*files).total ].diverge = 0;
        if ( stat == S_NORMAL )
          stat = get_integer ( line, &line_index, 
              &((*files).seq [ (*files).total ].diverge) );

        /* Get the phylogenetic incremental divergence time. */
        (*files).seq [ (*files).total ].inc_diverge = 0;
        if ( stat == S_NORMAL )
          stat = get_integer ( line, &line_index, 
              &((*files).seq [ (*files).total ].inc_diverge) );
      }  /* if */

      /* Get the gene name. */
      if ( ( (*files).seq [ (*files).total ].group == CODONS_TABLE ) ||
           ( (*files).seq [ (*files).total ].group == COMPARE_FILE ) ||
           ( (*files).seq [ (*files).total ].group == MUTATIONS_FILE ) )
      {
        get_field ( line, &line_index,
            &((*files).seq [ (*files).total ].gene_name) );
        strcopy ( (*files).seq [ (*files).total ].line,
                  (*files).seq [ (*files).total ].gene_name );
/*printf ( "Gene name = '%s'\n", (*files).seq [ (*files).total ].gene_name ); */
      }  /* if */

      if ( (*files).seq [ (*files).total ].group == MUTATIONS_FILE )
        (*files).mutations_index = (*files).total;

      if ( ( (*files).seq [ (*files).total ].group == CODONS_TABLE ) ||
           ( (*files).seq [ (*files).total ].group == COMPARE_FILE ) )
      {
        if ( status == S_NORMAL )
        {
          (*files).seq [ (*files).total ].protein = 
              fopen ( (*files).seq [ (*files).total ].file_name, "w" );

          if ( ( (*files).seq [ (*files).total ].group == CODONS_TABLE ) ||
               ( (*files).seq [ (*files).total ].group == MUTATIONS_FILE ) )
            if ( (*files).seq [ (*files).total ].protein != NULL )
              (*files).codons_table_index = (*files).total;
        }  /* if */
      }
      else
        /* Open the file for reading. */
        if ( status == S_NORMAL )
  	  status = open_file ( (*files).seq [ (*files).total ].file_name,
  	    &((*files).seq [ (*files).total ].protein) );

      if ( status == S_NORMAL )  /* initialize */
      {
        (*files).seq [ (*files).total ].amino_acids = 0;
	(*files).seq [ (*files).total ].pep_char = ' ';
	(*files).seq [ (*files).total ].line_index = 0;
        (*files).seq [ (*files).total ].cons_mask = 0;
        (*files).seq [ (*files).total ].ident_mask = 0;
        strcopy ( (*files).seq [ (*files).total ].previous_codon, "   " );
        strcopy ( (*files).seq [ (*files).total ].current_codon, "   " );
        strcopy ( (*files).seq [ (*files).total ].next_codon, "   " );
        (*files).seq [ (*files).total ].novel = 0;

        if ( to_print == TRUE )
          printf ( "%d\t%s\n", (*files).total, line );
	(*files).total++;
      }  /* if */
    }  /* if */
  }  /* while */
}  /* open_files */


/******************************************************************************/
/* This function closes all of the files in the files table. */
close_files ( files )
t_files	*files;		/* table of sequences */
{
  int  index;	/* files table index */

  for ( index = 0; index < (*files).total; index++ )
    if ( (*files).seq [ index ].protein != NULL )
    {
      fclose ( (*files).seq [ index ].protein );
      (*files).seq [ index ].protein = NULL;
    }  /* if */

  (*files).total = 0;
}  /* close_files */


/******************************************************************************/
/* This function maps a DNA base onto 0..3. */
map_base ( dna_base )
char  dna_base;  /* current DNA base */
{
  switch ( dna_base )
  {
    case ADENINE:  return ( 2 );
    case CYTOSINE: return ( 1 );
    case GUANINE:  return ( 3 );
    case THYMINE:  return ( 0 );
    default:
/*      printf ( "*INFO*: invalid DNA base '%c'\n", dna_base ); */
      return ( 0 );
  }  /* switch */
}  /* map_base */


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
  if ( character == SEQ_GAP )  *mask = ALL_BITS;
}  /* set_mask */


/******************************************************************************/
/* This function checks if 4 base codon appropriate for the mutation. */
check_2_codons ( mutation, current, mut_codon )
char  mutation;	    /* mutation at current codon */
char  current [];   /* current codon */
char  **mut_codon;   /* codons of mutation amino acid */
{
  switch ( mutation )
  {
    case 'L':
      if (( current [ BASE_1 ] == 'C' ) || ( current [ BASE_3 ] == 'T' ) ||
          ( current [ BASE_3 ] == 'C' ))
        *mut_codon = codons_2 [ S_LEU ];
      break;

    case 'R':
      if (( current [ BASE_1 ] == 'C' ) || ( current [ BASE_3 ] == 'T' ) ||
          ( current [ BASE_3 ] == 'C' ))
        *mut_codon = codons_2 [ S_ARG ];
      break;

    case 'S':
      if (( current [ BASE_2 ] == 'C' ) || ( current [ BASE_3 ] == 'T' ) ||
          ( current [ BASE_3 ] == 'C' ))
        *mut_codon = codons_2 [ S_SER ];
      break;

    default:
      break;
  }  /* switch */
}  /* check_2_codons */


/******************************************************************************/
/* This function computes the fraction of mutations of a codon that are CpG. */
fraction_cpg ( previous, current, next, fraction, mutation, at_cpg )
char    previous [];  	/* previous codon */
char    current [];   	/* current codon */
char    next [];      	/* next codon */
float   *fraction;    	/* fraction of mutations which are Cpg related */
char	mutation;	/* mutation at current codon */
int	*at_cpg;	/* mutation caused by CpG mutation */
{
  char  *mut_codon;     /* codons of mutation amino acid */

  *fraction = 0.0;
  *at_cpg = 0;

  /* Translate the mutation into its codons. */
  if (( mutation >= 'A' ) && ( mutation <= 'Z' ))
    mut_codon = codons [ mutation - 'A' ];
  else
    mut_codon = codons [ 'B' - 'A' ];  /* undefined */

  if (( mutation == 'L' ) || ( mutation == 'R' ) || ( mutation == 'S' ))
    check_2_codons ( mutation, current, &mut_codon );

  /* GNN */
  if (( current [ BASE_1 ] == 'G' ) && ( previous [ BASE_3 ] == 'C' ))
  {
    if ( current [ BASE_2 ] == 'A' )  *fraction = 0.375;
    else  *fraction = 0.5;

    if ( mut_codon [ BASE_1 ] != current [ BASE_1 ] )  *at_cpg = 1;
  }  /* if */

  /* NCG */
  if (( current [ BASE_2 ] == 'C' ) && ( current [ BASE_3 ] == 'G' ))
  {
    *fraction += 0.5;
    if ( mut_codon [ BASE_2 ] != current [ BASE_2 ] )  *at_cpg = 1;
  }  /* if */

  /* NNC */
  if (( current [ BASE_3 ] == 'C' ) && ( next [ BASE_1 ] == 'G' ))
  {
    *fraction += nnc [ map_base ( current [ BASE_1 ] ) * 4 +
        map_base ( current [ BASE_2 ] ) ];
    if (( mut_codon [ BASE_1 ] == current [ BASE_1 ] ) &&
        ( mut_codon [ BASE_2 ] == current [ BASE_2 ] ))
      *at_cpg = 1;
  }  /* if */

  /* CGN */
  if (( current [ BASE_1 ] == 'C' ) && ( current [ BASE_2 ] == 'G' ))
  {
    *fraction = 1.0;
    *at_cpg = 1;
  }  /* if */

/* printf ("\nCpG: %s %s %s Mutation '%c' (%s) fraction %5.2f at_cpg %d\n",
  previous, current, next, mutation, mut_codon, *fraction, *at_cpg ); */
}  /* fraction_cpg */


/******************************************************************************/
/* This function counts the mutations at an amino acid position. */
count_mutations ( files, mutations, conserved )
t_files  	*files;		/* table of sequences */
t_mutations	*mutations;	/* table of mutation statistics */
int		conserved;	/* Amino acid conservation level */
{
  int   at_cpg = 0;             /* mutation at CpG */
  float fraction_cg = 0.0;      /* fraction of mutations due to CpG */
  int	index;			/* current file index */
  int   cg_missense = 0;        /* # missense mutations at CpG */
  int   cg_nonsense = 0;        /* # nonsense mutations at CpG */
  int	nn_missense = 0;	/* number of missense mutations at this site */
  int	nn_nonsense = 0;	/* number of nonsense mutations at this site */

  /* Initialize fraction_cg in case no mutations at this site. */
  fraction_cpg ( (*files).seq [ MUTATION_SEQ ].previous_codon,
      (*files).seq [ MUTATION_SEQ ].current_codon,
      (*files).seq [ MUTATION_SEQ ].next_codon, &fraction_cg,
      (*files).seq [ MUTATION_SEQ ].pep_char, &at_cpg );

  /* Sum the mutations at this site. */
  for ( index = 0; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group == MUTATION )
      if ((( (*files).seq [ index ].pep_char >= 'A') &&
          ( (*files).seq [ index ].pep_char <= 'Z' )) ||
          ( (*files).seq [ index ].pep_char == '*' ))
      {
        fraction_cpg ( (*files).seq [ MUTATION_SEQ ].previous_codon,
            (*files).seq [ MUTATION_SEQ ].current_codon,
            (*files).seq [ MUTATION_SEQ ].next_codon, &fraction_cg,
            (*files).seq [ index ].pep_char, &at_cpg );

        if ( (*files).seq [ index ].pep_char != '*' )
        {
          cg_missense += at_cpg;
          nn_missense += 1 - at_cpg;
        }  /* if */
        else
        {
          cg_nonsense += at_cpg;
          nn_nonsense += 1 - at_cpg;
        }  /* if */
      }  /* if */

  if (( (*files).seq [ MUTATION_SEQ ].pep_char >= 'A') &&
      ( (*files).seq [ MUTATION_SEQ ].pep_char <= 'Z' ))
  {
    (*mutations).category [ conserved ].count
        [ (*files).seq [ MUTATION_SEQ ].pep_char - 'A' ]++;
    (*mutations).category [ OVERALL ].count
        [ (*files).seq [ MUTATION_SEQ ].pep_char - 'A' ]++;
  }  /* if */

  /* Total the conservation by level. */
  if ( nn_missense > 0 )
  {
    (*mutations).mis_sites.category [ conserved ]++;
    (*mutations).mis_total.category [ conserved ] += nn_missense;
  }  /* if */
  if ( cg_missense > 0 )
  {
    (*mutations).mis_sites_cpg.category [ conserved ]++;
    (*mutations).mis_total_cpg.category [ conserved ] += cg_missense;
  }  /* if */

  (*mutations).cg_amino_acids [ conserved ] += fraction_cg;
  (*mutations).nn_amino_acids [ conserved ] += 1.0 - fraction_cg;

  if ( nn_nonsense > 0 )
  {
    (*mutations).non_sites.category [ conserved ]++;
    (*mutations).non_total.category [ conserved ] += nn_nonsense;
  }  /* if */
  if ( cg_nonsense > 0 )
  {
    (*mutations).non_sites_cpg.category [ conserved ]++;
    (*mutations).non_total_cpg.category [ conserved ] += cg_nonsense;
  }  /* if */

  (*mutations).total.category [ conserved ] += 
      cg_missense + nn_missense + cg_nonsense + nn_nonsense;
}  /* count_mutations */


/******************************************************************************/
/* This function counts the mutations at an amino acid position. */
count_mutations_paradox ( files, mutations, mutation, conserved )
t_files      *files;        /* table of sequences */
t_mutations  *mutations;    /* table of mutation statistics */
t_mutation   *mutation;     /* current mutations table record */
int          conserved;	    /* Amino acid conservation level */
{
  int   at_cpg = 0;             /* mutation at CpG */
  float fraction_cg = 0.0;      /* fraction of mutations due to CpG */
  int   cg_missense = 0;        /* # missense mutations at CpG */
  int   cg_nonsense = 0;        /* # nonsense mutations at CpG */
  int	nn_missense = 0;	/* number of missense mutations at this site */
  int	nn_nonsense = 0;	/* number of nonsense mutations at this site */

  /* Initialize fraction_cg in case no mutations at this site. */
  fraction_cpg ( (*files).seq [ MUTATION_SEQ ].previous_codon,
      (*files).seq [ MUTATION_SEQ ].current_codon,
      (*files).seq [ MUTATION_SEQ ].next_codon, &fraction_cg,
      (*files).seq [ MUTATION_SEQ ].pep_char, &at_cpg );

  /* Sum the mutations at this site. */
  if ( (*mutation).codon_change [ CHANGE ] != '*' )
  {
    cg_missense += at_cpg;
    nn_missense += 1 - at_cpg;
  }  /* if */
  else
  {
    cg_nonsense += at_cpg;
    nn_nonsense += 1 - at_cpg;
  }  /* if */

  /* Total the conservation by level. */
  if ( nn_missense > 0 )
    (*mutations).mis_total.category [ conserved ] += nn_missense;

  if ( cg_missense > 0 )
    (*mutations).mis_total_cpg.category [ conserved ] += cg_missense;

  if ( nn_nonsense > 0 )
    (*mutations).non_total.category [ conserved ] += nn_nonsense;

  if ( cg_nonsense > 0 )
    (*mutations).non_total_cpg.category [ conserved ] += cg_nonsense;
}  /* count_mutations_paradox */


/******************************************************************************/
/* This function counts the mutations at an amino acid position. */
count_mutations_pn ( files, mutations, partial, specific_identity )
t_files  	*files;		      /* table of sequences */
t_mutations	*mutations;	      /* table of mutation statistics */
int		partial;	      /* partialness level of amino acid */
long            specific_identity;    /* if conserverative or not */
{
  int	index;		/* current file index */
  int   missense = 0;   /* # missense mutations */
  int   nonsense = 0;   /* # nonsense mutations */


  /* Sum the mutations at this site. */
  for ( index = 0; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group == MUTATION )
      if ((( (*files).seq [ index ].pep_char >= 'A') &&
          ( (*files).seq [ index ].pep_char <= 'Z' )) ||
          ( (*files).seq [ index ].pep_char == '*' ))
      {
        if ( (*files).seq [ index ].pep_char != '*' )
          missense++;
        else
          nonsense++;
      }  /* if */

  if (( (*files).seq [ MUTATION_SEQ ].pep_char >= 'A') &&
      ( (*files).seq [ MUTATION_SEQ ].pep_char <= 'Z' ))
  {
    if ( specific_identity != 0 )
    {
      (*mutations).missense_i    [ partial ] += missense;
      (*mutations).amino_acids_i [ partial ]++;
      if ( missense > 0 ) (*mutations).sites_i [ partial ]++;
    }
    else
    {
      (*mutations).missense_c    [ partial ] += missense;
      (*mutations).amino_acids_c [ partial ]++;
      if ( missense > 0 ) (*mutations).sites_c [ partial ]++;
    }  /* else */
  }  /* if */
}  /* count_mutations_pn */


/******************************************************************************/
/* This function reads in one mutation table record. */
read_mutation ( files, mutation )
t_files     *files;       /* table of sequences */
t_mutation  *mutation;    /* current mutations table record */
{
  char  line [ SUPER_LINE ];    /* very long files from Paradox */
  int   line_index = 0;         /* current line index */
  int   status;                 /* function return status */
  char  token [ MAX_LINE ];     /* next token from line */
  int   token_index = 0;        /* current token index */


  /* Initialize. */
  (*mutation).key              = 0;
  (*mutation).nucleotide_begin = 0;
  (*mutation).nucleotide_end   = 0;
  (*mutation).codon_number     = 0;
  (*mutation).codon_number_end = 0;
  (*mutation).codon_base       = 0;
  (*mutation).reference        = 0;
  (*mutation).mutation_type    = ' ';
  (*mutation).mutation_code    = ' ';
  (*mutation).splicing         = ' ';
  strcpy ( (*mutation).gene_name, " " );
  strcpy ( (*mutation).base_change, "   " );
  strcpy ( (*mutation).nucleotide_change, " " );
  strcpy ( (*mutation).codon_change, "   " );
  strcpy ( (*mutation).location, " " );
  strcpy ( (*mutation).length, " " );

  line [ 0 ] = '\0';
  status = S_END_OF_LINE;
  while ( ( status != S_NORMAL ) && ( status != S_EOF ) )
    status = get_big_line ( 
        (*files).seq [ (*files).mutations_index ].protein, line );

  if (( status == S_EOF ) && ( line [ 0 ] == '\0' ) )
  {
    (*mutation).codon_number = MAX_CODON_NUMBER;
    return ( S_EOF );
  }  /* if */

  get_field ( line, &line_index, token );    /* key (N) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).key) );

  get_field ( line, &line_index, &((*mutation).gene_name) );/* gene_name (A5) */

  get_field ( line, &line_index, token );    /* PIN (N) */

  get_field ( line, &line_index, token );    /* nucleotide_number (N) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).nucleotide_begin) );

  get_field ( line, &line_index, token );    /* nucleotide_number_end (N) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).nucleotide_end) );

  get_field ( line, &line_index, token );    /* CpG_Site (A1) */

  get_field ( line, &line_index, token );    /* nucleotide_base_change (A3) */
  strcpy ( (*mutation).base_change, token );

  get_field ( line, &line_index, token );    /* nucleotide_change (A255) */
  strcpy ( (*mutation).nucleotide_change, token );

  get_field ( line, &line_index, token );    /* nucleotide_comment (A100) */

  get_field ( line, &line_index, token );    /* location (A25) */
  strcpy ( (*mutation).location, token );

  get_field ( line, &line_index, token );    /* length (A20) */
  strcpy ( (*mutation).length, token );

  get_field ( line, &line_index, token );    /* codon_number (N) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).codon_number) );

  get_field ( line, &line_index, token );    /* exon (N) */

  get_field ( line, &line_index, token );    /* codon_number_end (N) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).codon_number_end) );

  get_field ( line, &line_index, token );    /* codon_change (A3) */
  strcpy ( (*mutation).codon_change, token );

  get_field ( line, &line_index, token );    /* codon_base (S) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).codon_base) );

  get_field ( line, &line_index, token );    /* mutation_type (A1) */
  if ( token [ 0 ] != '\0' )
    (*mutation).mutation_type = token [ 0 ];

  get_field ( line, &line_index, token );    /* Allelic_status */
  get_field ( line, &line_index, token );    /* Organ group*/
  get_field ( line, &line_index, token );    /* Organ */
  get_field ( line, &line_index, token );    /* tumor_type */
  get_field ( line, &line_index, token );    /* tumor_histology */
  get_field ( line, &line_index, token );    /* tumor_stage */
  get_field ( line, &line_index, token );    /* clinical_course */
  get_field ( line, &line_index, token );    /* Mutagen */
  get_field ( line, &line_index, token );    /* tumor_name */
  get_field ( line, &line_index, token );    /* immunostaining */

  get_field ( line, &line_index, token );    /* reference (N) */
  token_index = 0;
  get_integer ( token, &token_index, &((*mutation).reference) );

  get_field ( line, &line_index, token );    /* splicing */
  if ( token [ 0 ] != '\0' )
    (*mutation).splicing = token [ 0 ];

  get_field ( line, &line_index, token );    /* germline_or_somatic */
  get_field ( line, &line_index, token );    /* method_key */

  get_field ( line, &line_index, token );    /* mutation_polymorph_error (A1) */
  if ( token [ 0 ] != '\0' )
    (*mutation).mutation_code = token [ 0 ];

  get_field ( line, &line_index, token );    /* dna_source */
  get_field ( line, &line_index, token );    /* patient_name */
  get_field ( line, &line_index, token );    /* sex */
  get_field ( line, &line_index, token );    /* age */
  get_field ( line, &line_index, token );    /* race */
  get_field ( line, &line_index, token );    /* ethnicity */
  get_field ( line, &line_index, token );    /* tumor_source */
  get_field ( line, &line_index, token );    /* multiple_mutations_key */
  get_field ( line, &line_index, token );    /* update_key */
  get_field ( line, &line_index, token );    /* comment */

/*  print_mutation ( mutation ); */
  return ( S_NORMAL );
}  /* read_mutation */


/******************************************************************************/
/* This function prints out a mutation record. */
print_mutation ( mutation )
t_mutation  *mutation;    /* current mutations table record */
{
  printf ( "key: %5d, ",       (*mutation).key );
  printf ( "%s, ", (*mutation).gene_name );

  if ( (*mutation).nucleotide_begin > 0 )
    printf ( "(%d) ", (*mutation).nucleotide_begin );

  printf ( "codon %d ", (*mutation).codon_number ); 
  printf ( "(%s), ",    (*mutation).codon_change );
  printf ( "base: %d ", (*mutation).codon_base );
  printf ( "'%s', ",    (*mutation).base_change );
  printf ( "type %c, ", (*mutation).mutation_type );
  printf ( "ref %d, ",  (*mutation).reference );
  printf ( "MPE '%c', ", (*mutation).mutation_code );
  printf ( "loc '%s', ", (*mutation).location );
  printf ( "len '%s', ", (*mutation).length );

  if ( ( (*mutation).mutation_type != 'M' ) && 
       ( (*mutation).mutation_type != 'N' ) &&
       ( (*mutation).nucleotide_change [ 0 ] != '\0' ) )
    printf ( ", Bases: %s", (*mutation).nucleotide_change );

  printf ( "\n" );
}  /* print_mutation */


/******************************************************************************/
/* This function checks a Paradox mutations table record. */
check_mutation ( files, mutation, mut_index, sequence )
t_files         *files;       /* table of sequences */
t_mutation      *mutation;    /* current mutations table record */
int             mut_index;    /* mutation index */
t_DNA_sequence  *sequence;    /* DNA sequence */
{
  char  amino_acid;                  /* expected amino acid mutant */
  char  codon [ MAX_CODON + 1 ];     /* mutated codon */
  long  expected_codon;              /* expected codon number */
  long  start = -1;                  /* nucleotide number of first amino acid */
  int   status;                      /* function return code */


  /* Check for incomplete mutations or uncheckable mutations. */
  status = unchecked_mutation ( mutation, sequence );
  if ( status != S_NORMAL )  return ( status );

  /* Check for the current codon number. */
  if ( (*files).codon_number < (*mutation).codon_number )
    return ( S_NOT_FOUND );

  /* Check the nucleotide number - not implemented yet. */
  if ( ( mut_index > 0 ) && ( (*mutation).codon_number > 0 ) )
  {
    start = (*files).seq [ mut_index ].descriptor;
    expected_codon = ( (*mutation).nucleotide_begin - start ) / 3;
    if ( (*mutation).nucleotide_begin > 0 )
      if ( ( expected_codon != (*mutation).codon_number ) && 
          ( BRIEF_MODE == OFF ) )
      {
        printf ( "\nNucleotide number disagrees with calculated codon number" );
        printf ( " %d\n", expected_codon );
        print_mutation ( mutation );
      }  /* if */
  }  /* if */

  /* Check that the original amino acid matches. */
  if ( ( (*files).seq [ MUTATION_SEQ ].pep_char  != 
      (*mutation).codon_change [ SOURCE ] ) )
  {
    printf ( "\nWrong source amino acid in mutation at codon %d, ", 
        (*files).codon_number );
    printf ( "%c != %c.\n", (*files).seq [ MUTATION_SEQ ].pep_char, 
        (*mutation).codon_change [ SOURCE ] );
    print_mutation ( mutation );
    return ( S_ERROR );
  }  /* if */

  /* Check the source DNA mutation base. */
  if ( (*files).seq [ MUTATION_SEQ ].current_codon 
      [ (*mutation).codon_base - 1 ] != (*mutation).base_change [ SOURCE ] ) 
  {
    printf ( "\nWrong source DNA base for codon %d, ", 
        (*files).codon_number );
    printf ( "%s.%1d %c != %c.\n", 
        (*files).seq [ MUTATION_SEQ ].current_codon,
        (*mutation).codon_base,
        (*files).seq [ MUTATION_SEQ ].current_codon 
            [ (*mutation).codon_base - 1 ],
        (*mutation).base_change [ SOURCE ] );
    print_mutation ( mutation );
  }  /* if */

  /* Check that the mutant codon matches the mutant amino acid. */
  strcpy ( codon, (*files).seq [ MUTATION_SEQ ].current_codon );

  codon [ (*mutation).codon_base - 1 ] = 
      (*mutation).base_change [ CHANGE ];

  /* Translate the mutated codon to the mutated amino acid. */
  translate ( codon, &amino_acid );

  if ( amino_acid != (*mutation).codon_change [ CHANGE ] )
  {
    printf ( "\nMismatch between DNA change and amino acid change at" );
    printf ( " codon %d,", (*files).codon_number );
    printf ( "%s.%1d -> %c (%s), %c != %c.\n", 
        (*files).seq [ MUTATION_SEQ ].current_codon,
        (*mutation).codon_base, 
        (*mutation).base_change [ CHANGE ], codon, amino_acid, 
        (*mutation).codon_change [ CHANGE ] );
    print_mutation ( mutation );
  }  /* if */

  /* Total the mutations. */
/*
  amino_acid = (*mutation).codon_change [ CHANGE ];
  if ( ( amino_acid >= 'A' ) && ( amino_acid <= 'Z' ) )
    (*sequence).missense [ (*sequence).total ]
        .substitution [ amino_acid - 'A' ]++;
*/

  /* Report the mutation. */
  report_mutation ( (*mutation).codon_change [ CHANGE ], files );

  return ( S_NORMAL );
}  /* check_mutation */


/******************************************************************************/
/* This function checks a Paradox mutations table record. */
unchecked_mutation ( mutation, sequence )
t_mutation      *mutation;    /* current mutations table record */
t_DNA_sequence  *sequence;    /* DNA sequence */
{
  int  size;                 /* number of bases in nucleotide_change field */

  /* Check for a polymorphism or an incomplete record. */
  if ( ( (*mutation).mutation_code == 'P' ) ||
       ( (*mutation).mutation_code == 'I' ) )  return ( S_UNCHECKED );

  if ( ( (*mutation).codon_change [ 0 ] == '\0' ) || 
       ( (*mutation).codon_number <= 0 ) || 
       ( (*mutation).codon_number_end > (*mutation).codon_number ) ||
       ( (*mutation).codon_base == 0 ) ||
       ( str_len ( (*mutation).codon_change ) < MAX_CHANGE - 1 ) ||
       ( str_len ( (*mutation).base_change ) < MAX_CHANGE - 1 ) ||
       ( (*mutation).codon_change [ 1 ] != ':' ) ||
       ( (*mutation).base_change [ 1 ] != ':' ) )
  {
    if ( BRIEF_MODE == OFF )  printf ( "\n" );

    /* Check for a splicing mutation. */
    if ( (*mutation).splicing == 'Y' )
    {
      if ( BRIEF_MODE == OFF )  printf ( "Splicing: " );
    }
    else
      /* Check the type of the mutation. */
      switch ( (*mutation).mutation_type )
      {
        case 'I':    /* insertion mutation */
          if ( BRIEF_MODE == OFF )
            printf ( "Insertion: " );
          break;

        case 'D':    /* deletion mutation */
          if ( BRIEF_MODE == OFF )
            /* Print out the deletion mutation */
            process_deletion ( mutation, sequence );

          size = str_len ( (*mutation).nucleotide_change ); 
          if ( size != ( (*mutation).nucleotide_end - 
              (*mutation).nucleotide_begin + 1 ) )
            if ( BRIEF_MODE == OFF )
              printf ( "Deleted bases (%d) disagree with deletion size\n", size );

          if ( BRIEF_MODE == OFF )
            printf ( "Deletion: " );
          break;

        case 'C':    /* complex mutation */
          if ( BRIEF_MODE == OFF )
            printf ( "Complex: " );
          break;

        case 'M': ;    /* missense mutation */
        case 'N': ;    /* nonsense mutation */
          break;

        default :
          if ( BRIEF_MODE == OFF )
            printf ( "Incomplete: " );
          break;
      }  /* switch */

    if ( BRIEF_MODE == OFF )  print_mutation ( mutation );
    return ( S_UNCHECKED );
  }  /* if */

  if ( (*mutation).splicing == 'Y' )  return ( S_UNCHECKED );
  return ( S_NORMAL );
}  /* unchecked_mutations */


/******************************************************************************/
/* This function processes the Paradox mutations table. */
process_mutations ( files, mutations, mutation, conserved, sequence )
t_files         *files;        /* table of sequences */
t_mutations     *mutations;    /* table of mutation statistics */
t_mutation      *mutation;     /* current mutations table record */
int             conserved;     /* Amino acid conservation level */
t_DNA_sequence  *sequence;     /* DNA sequence */
{
  int   mut_index;            /* mutations table index */
  int   status = S_NORMAL;    /* function return code */

  mut_index = (*files).mutations_index;

  if ( mut_index == -1 )  return;

  /* Check if end of file has previously been reached. */
  if ( (*mutation).codon_number < MAX_CODON_NUMBER )
  {
    /* Process all of the mutations for this codon. */
    while 
    ( ( ( ( (*mutation).codon_number <= (*files).codon_number ) ||
          ( strcmp ( (*files).seq [ (*files).codons_table_index ].gene_name,
              (*mutation).gene_name ) != 0 ) 
        ) 
      ) &&
      ( (*mutation).codon_number < MAX_CODON_NUMBER ) )
    {
      /* Check for the same gene. */
      if ( strcmp ( (*files).seq [ (*files).codons_table_index ].gene_name, 
                    (*mutation).gene_name            ) == 0 )
      {
        /* Process a mutation */
        status = check_mutation ( files, mutation, mut_index, &sequence );

        /* Add the mutation to the statistics table. */
        if ( status == S_NORMAL )
          count_mutations_paradox ( files, mutations, mutation, conserved );
      }  /* if */

      /* read in a record */
      status = read_mutation ( files, mutation );
    }  /* while */
  }  /* if */
}  /* process_mutations */


/******************************************************************************/
/* This function processes the mutations at an amino acid position. */
process_seq_mutations ( files, sequence )
t_files  	 *files;       /* table of sequences */
t_main_sequence  *sequence;    /* main sequence of interest */
{
  char  change;    /* amino acid mutation */
  int	index;     /* current file index */

  printf ( "  " );

  /* Print the mutations at this site. */
  for ( index = 0; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group == MUTATION )
    {
      change = (*files).seq [ index ].pep_char;
      report_mutation ( change, files );

      /* Total the mutations. */
      if ( ( change >= 'A' ) && ( change <= 'Z' ) )
        (*sequence).missense [ (*sequence).total ]
            .substitution [ change - 'A' ]++;
    }  /* if */
}  /* process_seq_mutations */


/******************************************************************************/
/* This function report a mutation at an amino acid position. */
report_mutation ( change, files )
char            change;         /* amino acid mutation */
t_files  	*files;		/* table of sequences */
{
  int   at_cpg = 0;             /* mutation at CpG */
  float fraction_cg = 0.0;      /* fraction of mutations due to CpG */

  /* Print the mutation at this site. */
  if ( (( change >= 'A' ) && ( change <= 'Z' )) || ( change == '*' ) )
  {
    fraction_cpg ( (*files).seq [ MUTATION_SEQ ].previous_codon,
        (*files).seq [ MUTATION_SEQ ].current_codon,
        (*files).seq [ MUTATION_SEQ ].next_codon, &fraction_cg,
        change, &at_cpg );

    if (( at_cpg == 0 ) || ( change == SEQ_GAP ))
      printf ( "%c", change );
    else
      if ( change == '*' )
        printf ( "@" );
      else  /* print CpG mutations in lower case */
        printf ( "%c", change - 'A' + 'a' );
  }  /* if */
}  /* report_mutation */


/******************************************************************************/
/* This function computes nonconserved, specifics, partial generics */
/* and generics. */
generics ( files, groups, mutations )
t_files		*files;		/* table of sequences */
t_groups	*groups;	/* table of protein family group information */
t_mutations	*mutations;	/* table of mutation statistics */
{
  long	cons_generic = ALL_BITS;	/* generic amino acid status flag */
  long  conservative;                   /* conservative site or not */
  int	group_index;			/* groups table index */
  long  ident_generic = ALL_BITS;	/* identical generic amino acid */
  int	index;
  int   pn_cons;                        /* pn conservation bit mask */
  char	previous = ' ';			/* previous sequence amino acid */

  (*mutations).genes = (*groups).total;

  /* Find the highest group number. */
  (*groups).total = 0;
  for ( index = 0; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group >= (*groups).total )
      (*groups).total = (*files).seq [ index ].group + 1;

  /* Initialize the group table. */
  (*groups).partial_index = 0;
  (*mutations).pn = 0;
  for ( group_index = 0; group_index < (*groups).total; group_index++ )
  {
    (*groups).spec_cons [ group_index ] = ALL_BITS;
    (*groups).spec_ident [ group_index ] = ALL_BITS;
    (*groups).other_cons [ group_index ] = 0;
    (*groups).other_ident [ group_index ] = 0;
    (*groups).identical [ group_index ] = SEQ_GAP;	/* no non-identical amino acids yet */
    (*groups).conservation [ group_index ] = NON_CONSERVED;
  }  /* for */

  /* Set up the identity character for each group. */
  for ( group_index = 0; group_index < (*groups).total; group_index++ )
    for ( index = 0; index < (*files).total; index++ )
      if ( (*files).seq [ index ].group == group_index )
        if ( (*files).seq [ index ].pep_char !=
          (*groups).identical [ group_index ] ) 
        {
          if ( (*groups).identical [ group_index ] == SEQ_GAP )
            (*groups).identical [ group_index ] = 
              (*files).seq [ index ].pep_char;
          else
            if ( (*files).seq [ index ].pep_char != SEQ_GAP )
              (*groups).identical [ group_index ] = ' ';
        }  /* if */

  /* Traverse each sequence setting up the conservative substitution masks. */
  for ( index = 0; index < (*files).total; index++ )
  {
    if ( (*files).seq [ index ].group >= 0 )
    {
      (*groups).spec_cons [ (*files).seq [ index ].group ] &= 
        (*files).seq [ index ].cons_mask;
      (*groups).spec_ident [ (*files).seq [ index ].group ] &= 
        (*files).seq [ index ].ident_mask;
      if ( previous != (*files).seq [ index ].pep_char )
      {
        cons_generic &= (*files).seq [ index ].cons_mask;
        ident_generic &= (*files).seq [ index ].ident_mask;
        previous = (*files).seq [ index ].pep_char;
      }  /* if */
    }  /* if */
  }  /* for */

  /* Set to non-conservative any group without a specific amino acid. */
  for ( group_index = 0; group_index < (*groups).total; group_index++ )
    if ( (*groups).spec_cons [ group_index ] == ALL_BITS )
    {
      (*groups).spec_cons [ group_index ] = 0;
      (*groups).spec_ident [ group_index ] = 0;
      cons_generic = 0;
      ident_generic = 0;
    }  /* if */

  /* Set up partial specifics mask in other_groups. */
  for ( group_index = 0; group_index < (*groups).total; group_index++ )
    for ( index = 0; index < (*groups).total; index++ )
      if ( index != group_index )
      {
	(*groups).other_cons [ group_index ] |= (*groups).spec_cons [ index ];
	(*groups).other_ident [ group_index ] |= (*groups).spec_ident [ index ];
      }  /* if */

  /* Count the partialness of the main mutation sequence. */
  pn_cons = (*groups).spec_cons [ 0 ];
  if ( pn_cons != 0 )
    for ( group_index = 0; group_index < (*groups).total; group_index++ )
      if ( ( pn_cons & (*groups).spec_cons [ group_index ] ) != 0 )
      {
        (*groups).partial_index++;
        pn_cons = pn_cons & (*groups).spec_cons [ group_index ];
      }  /* if */

  (*mutations).pn = (*groups).partial_index;

  for ( group_index = 0; group_index < (*groups).total; group_index++ )
  {
    if ( cons_generic != 0 )
    {
      if ( ident_generic != 0 )
        (*groups).conservation [ group_index ] = GENERIC;
      else
        (*groups).conservation [ group_index ] = CONS_GENERIC;
    }  /* if */
    else
      if ( (*groups).spec_cons [ group_index ] == 0 )
        (*groups).conservation [ group_index ] = NON_CONSERVED;
      else
	if (( (*groups).spec_cons [ group_index ] &
	  (*groups).other_cons [ group_index ]) != 0 )
        {
          if (( (*groups).spec_ident [ group_index ] &
  	    (*groups).other_ident [ group_index ]) != 0 )
            (*groups).conservation [ group_index ] = PARTIAL;
          else
            (*groups).conservation [ group_index ] = CONS_PARTIAL;
        }  /* if */
	else
        {
          if ( (*groups).spec_ident [ group_index ] != 0 )
            (*groups).conservation [ group_index ] = SPECIFIC;
          else
            (*groups).conservation [ group_index ] = CONS_SPECIFIC;
        }  /* else */
  }  /* for */

  if ( ( (*groups).conservation [ MUTATION_GROUP ] == CONS_GENERIC ) ||
       ( (*groups).conservation [ MUTATION_GROUP ] == CONS_PARTIAL ) ||
       ( (*groups).conservation [ MUTATION_GROUP ] == CONS_SPECIFIC ) ||
       ( (*groups).conservation [ MUTATION_GROUP ] == NON_CONSERVED ) )
    conservative = 0;
  else  conservative = 1;

  /* Sum up the mutations at this amino acid position. */
  if ( ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP ) &&
       ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_SKIP ) )
  {
    count_mutations ( files, mutations, 
      (*groups).conservation [ MUTATION_GROUP ] );
    count_mutations_pn ( files, mutations, (*mutations).pn, conservative );
  }  /* if */
}  /* generics */


/******************************************************************************/
/* This function computes the conservation based upon identity */
/* within a family controling if conservative substitutions are allowed. */
/* This function requires the generics routine to have been previously called. */
generics_2 ( files, groups, mutations )
t_files		*files;		/* table of sequences */
t_groups 	*groups;	/* table of protein family group information */
t_mutations	*mutations;	/* table of mutation statistics */
{
  short	generic;		/* flag indicating generic status */
  int	group_index;		/* current group */
  int	index;
  short	identical;		/* flag indicating amino acid identity */
  short	partial;		/* Partial generic status */
  int   no_non_conserved = 0;   /* number of groups with nonconserved conservation */
  int   no_partials = 0;        /* number of groups with partial or generic conservation */


  printf ( " EC: " );
  (*mutations).genes = (*groups).total;
  (*mutations).pn    = (*groups).total;

  /* Set up the identity character for each group. */
  for ( group_index = 0; group_index < (*groups).total; group_index++ )
    for ( index = 0; index < (*files).total; index++ )
      if ( (*files).seq [ index ].group == group_index )
        if ( (*files).seq [ index ].pep_char !=
          (*groups).identical [ group_index ] ) 
        {
          if ( (*groups).identical [ group_index ] == SEQ_GAP )
            (*groups).identical [ group_index ] = 
              (*files).seq [ index ].pep_char;
          else
            if ( (*files).seq [ index ].pep_char != SEQ_GAP )
              (*groups).identical [ group_index ] = ' ';
        }  /* if */

  /* Define the conservation for each family separately. */
  for ( group_index = 0; group_index < (*groups).total; group_index++ )
  {
    /* Check if not conserved. */
    if ( (*groups).spec_cons [ group_index ] == 0 )
    {
      (*groups).conservation [ group_index ] = NON_CONSERVED;
      no_non_conserved++;
      identical = FALSE;

      if ( group_index == 0 )  (*mutations).pn = 0;
    }  /* if */
    else
    {
      if ( (*groups).identical [ group_index ] == ' ' )
        identical = FALSE;
      else
        identical = TRUE;

      partial = FALSE;
      generic = TRUE;
      /* Check if generic or partially generic. */
      for ( index = 0; index < (*groups).total; index++ )
        if ( index != group_index )
        {
          if ( identical == TRUE )
          {
            if ( (*groups).identical [ index ] !=
              (*groups).identical [ group_index ] )
            {
              generic = FALSE;

              if ( group_index == 0 )  (*mutations).pn--;
            }
            else  partial = TRUE;
          }  /* if */
          else
          {
            if ( ((*groups).spec_cons [ group_index ] &
                (*groups).spec_cons [ index ]) == 0 )
            {
              generic = FALSE;

              if ( group_index == 0 )  (*mutations).pn--;
            }
            else  partial = TRUE;
          }
        }  /* if */

      if ( generic == TRUE )
      {
        (*groups).conservation [ group_index ] = GENERIC;
        no_partials++;
      }
      else
        if ( partial == TRUE )
        {
          (*groups).conservation [ group_index ] = PARTIAL;
          no_partials++;
        }
        else
          (*groups).conservation [ group_index ] = SPECIFIC;
    }  /* else */

    /* check for conservative substitutions used. */
    if (( identical != TRUE ) && 
        ( (*groups).conservation [ group_index ] < NON_CONSERVED ))
      (*groups).conservation [ group_index ]++;

    if ( (*groups).identical [ group_index ] == SEQ_GAP )  
      printf ( "%c", SEQ_GAP );
    else
      printf ( "%c", cons_letters [ (*groups).conservation [ group_index ] ] );
  }  /* for */

/* ### */
/*  printf ( "  P%d N%d  ", no_partials, no_non_conserved ); */
/*  printf ( "  P%d  ", (*mutations).pn ); */

  /* Sum up the mutations at this amino acid site. */
  if ( ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP ) &&
       ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_SKIP ) )
  {
    count_mutations ( files, mutations,
     (*groups).conservation [ MUTATION_GROUP ] );
    count_mutations_pn ( files, mutations, (*mutations).pn,
        (*groups).spec_ident [ MUTATION_GROUP ] );

    /* Check if Paradox codon table generation is selected. */
    if ( (*files).codons_table_index != 0 )
      write_codon_record ( files, groups );
  }  /* if */
}  /* generics_2 */


/******************************************************************************/
/* This function writes out a Paradox codon table record. */
write_codon_record ( files, groups )
t_files		*files;		/* table of sequences */
t_groups 	*groups;	/* table of protein family group information */
{
  char  cpg [ MAX_CODON + 1 ] = "NNN";    /* CpG indicator */


  /* Check for CpG dinucleotide. */
  if ( ( (*files).seq [ MUTATION_SEQ ].previous_codon [ BASE_3 ] == 'C' ) &&
       ( (*files).seq [ MUTATION_SEQ ].current_codon [ BASE_1 ] == 'G' ) )
    cpg [ BASE_1 ] = 'Y';

  if ( ( (*files).seq [ MUTATION_SEQ ].current_codon [ BASE_1 ] == 'C' ) &&
       ( (*files).seq [ MUTATION_SEQ ].current_codon [ BASE_2 ] == 'G' ) )
  {
    cpg [ BASE_1 ] = 'Y';
    cpg [ BASE_2 ] = 'Y';
  }  /* if */

  if ( ( (*files).seq [ MUTATION_SEQ ].current_codon [ BASE_2 ] == 'C' ) &&
       ( (*files).seq [ MUTATION_SEQ ].current_codon [ BASE_3 ] == 'G' ) )
  {
    cpg [ BASE_2 ] = 'Y';
    cpg [ BASE_3 ] = 'Y';
  }  /* if */

  if ( ( (*files).seq [ MUTATION_SEQ ].current_codon [ BASE_3 ] == 'C' ) &&
       ( (*files).seq [ MUTATION_SEQ ].next_codon [ BASE_1 ] == 'G' ) )
    cpg [ BASE_3 ] = 'Y';

  /* Write out each base of the codon */
  write_base ( files, groups, 1, cpg );
  write_base ( files, groups, 2, cpg );
  write_base ( files, groups, 3, cpg );
}  /* write_codon_record */


/******************************************************************************/
/* This function writes out a Paradox codon table record. */
write_base ( files, groups, base, cpg )
t_files		*files;		/* table of sequences */
t_groups 	*groups;	/* table of protein family group information */
int             base;           /* codon base indicator */
char            cpg [];         /* CpG indicator */
{
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein,
            "\"%s\",", (*files).seq [ (*files).codons_table_index ].line );
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein,
            "%d,", (*files).codon_number );
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein,
            "%d,", base );
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein,
            "\"%c\",", (*files).seq [ MUTATION_SEQ ].pep_char );
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein,
            "\"%s\",", (*files).seq [ MUTATION_SEQ ].current_codon );
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein,
            "\"%c\",", cpg [ base - 1 ] );
  fprintf ( (*files).seq [ (*files).codons_table_index ].protein, 
      "\"%c\",0,0\n", 
      cons_letters [ (*groups).conservation [ MUTATION_GROUP ] ] );
}  /* write_base */


/******************************************************************************/
/* This function initializes the mutations table. */
initialize_mutations ( mutations )
t_mutations	*mutations;	/* table of mutation statistics */
{
  int	base;	/* current amino acid */
  int	index;	/* mutation category index */

  for ( index = 0; index < MAX_CONSERVATION; index ++ )
  {
    (*mutations).nn_amino_acids [ index ] = 0.0;
    (*mutations).cg_amino_acids [ index ] = 0.0;
    (*mutations).mis_sites_cpg.category [ index ] = 0;
    (*mutations).mis_total_cpg.category [ index ] = 0;
    (*mutations).mis_sites    .category [ index ] = 0;
    (*mutations).mis_total    .category [ index ] = 0;
    (*mutations).non_sites_cpg.category [ index ] = 0;
    (*mutations).non_total_cpg.category [ index ] = 0;
    (*mutations).non_sites    .category [ index ] = 0;
    (*mutations).non_total    .category [ index ] = 0;
    (*mutations).total        .category [ index ] = 0;
  
    for ( base = 'A' - 'A'; base <= 'Z' - 'A'; base++ )
      (*mutations).category [ index ].count [ base ] = 0;
  }  /* for */

  /* Initialize the Pn statistics. */
  (*mutations).genes = 0;
  for ( index = 0; index < MAX_GENES; index++ )
  {
    (*mutations).sites_i       [ index ] = 0;
    (*mutations).sites_c       [ index ] = 0;
    (*mutations).missense_i    [ index ] = 0;
    (*mutations).missense_c    [ index ] = 0;
    (*mutations).amino_acids_i [ index ] = 0;
    (*mutations).amino_acids_c [ index ] = 0;
  }  /* for */
}  /* initialize_mutations */


/******************************************************************************/
/* Initialize the comparison table. */
initialize_comparison ( comparison )
t_comparison    *comparison;    /* comparisons against unknown sequence */
{
  int  cons_index;    /* conservation index */
  int  index;         /* sequence index */
  int  seq_index;     /* sequence index */


  (*comparison).unknown_seq = -1;
  (*comparison).total = 0;

  for ( seq_index = 0; seq_index < MAX_SEQUENCES; seq_index++ )
    for ( cons_index = 0; cons_index < MAX_CONSERVATION; cons_index++ )
    {
      (*comparison).sequences [ seq_index ].identity     [ cons_index ] = 0;
      (*comparison).sequences [ seq_index ].conservation [ cons_index ] = 0;
      (*comparison).sequences [ seq_index ].compared     [ cons_index ] = 0;
      (*comparison).sequences [ seq_index ].lost         [ cons_index ] = 0;

      /* Initialize the N x N comparison statistics. */
      for ( index = 0; index < MAX_SEQUENCES; index++ )
      {
        (*comparison).sequences [ seq_index ].N [ index ].identity = 0;
        (*comparison).sequences [ seq_index ].N [ index ].compared = 0;
      }  /* for */
    }  /* for */
}  /* initialize_comparison */


/******************************************************************************/
mutation_density ( sequence, method )
t_main_sequence  *sequence;        /* main sequence of interest */
t_mutations	 *method;	   /* method of conservation definition */
{
  char          amino_acid;        /* translated amino acid of codon */
  int  		index;			   /* current sequence */
  int  		status = S_NORMAL;	   /* function return status */

}  /* mutation_density */


/******************************************************************************/
/* This function scans through the protein sequences one amino acid at a */
/* time. */
scan_sequences ( files, groups, sequence, method_I, method_II )
t_files		 *files;	
t_groups	 *groups;
t_main_sequence  *sequence;        /* main sequence of interest */
t_mutations	 *method_I;	   /* Old method of conservation definition */
t_mutations	 *method_II;	   /* New method of conservation definition */
{
  char          amino_acid;        /* translated amino acid of codon */
  t_mutation    mutation;          /* next mutation from the mutation table */
  int  		index;			   /* current sequence */
  int           position = 0;              /* mutation target amino acid # */
  int  		status = S_NORMAL;	   /* function return status */
  int  		read_status = S_NORMAL;	   /* more sequence data to read */
  static  t_DNA_sequence  DNA_sequence;    /* DNA sequence */
  FILE    *DNA_file;                       /* DNA sequence file */


  /* Open the DNA sequence file. */
  open_sequence ( files, DNA_file, &DNA_sequence );

  mutation.codon_number = 0;

  /* Read in the first Paradox mutations table record. */
  read_mutation ( files, &mutation );

  (*sequence).total = 0;
  for ( index = 0; index < MAX_RESIDUES; index++ )
    for ( amino_acid = 'A' - 'A'; amino_acid <= 'Z' - 'A'; amino_acid++ )
      (*sequence).missense [ index ].substitution [ amino_acid ] = 0;

  initialize_mutations ( method_I ); 
  initialize_mutations ( method_II ); 

  /* Find the beginning of each sequence. */
  for ( index = 0; index < (*files).total; index++ )
  {
    if ( (*files).seq [ index ].group >= DNA_SEQUENCE ) 
      find_data ( (*files).seq [ index ].protein );

    /* Initialize the current codon. */
    if ( (*files).seq [ index ].group == MUTATION_DNA )
      status = next_codon ( (*files).seq [ index ].protein,
        (*files).seq [ index ].line, &((*files).seq [ index ].line_index),
        (*files).seq [ MUTATION_SEQ ].next_codon );
  }  /* for */

  /* Print out a header index. */
  for ( index = 0; index < (*files).total; index++ )
  {
    if ( index > 0 )
      if ( ( (*files).seq [ index - 1 ].group != (*files).seq [ index ].group )
          && ( (*files).seq [ index ].group >= 1 ) )
        printf ( " " );

    if ( (*files).seq [ index ].group >= 0 )
      printf ( "%1d", ( (index / 10) % 10 ) );
  }  /* for */
  printf ( "\n" );

  for ( index = 0; index < (*files).total; index++ )
  {
    if ( index > 0 )
      if ( ( (*files).seq [ index - 1 ].group != (*files).seq [ index ].group )
          && ( (*files).seq [ index ].group >= 1 ) )
        printf ( " " );

    if ( (*files).seq [ index ].group >= 0 )
      printf ( "%1d", (index % 10) );
  }  /* for */
  printf ( "\n\n" );

  /* Scan the sequences */
  while ( read_status == S_NORMAL )
  {
    read_status = S_EOF;
    amino_acid++;

    /* Process each sequence */
    for ( index = 0; index < (*files).total; index++ )
    {
      strcopy ( (*files).seq [ index ].previous_codon,  /* move lower */
          (*files).seq [ index ].current_codon );
      strcopy ( (*files).seq [ index ].current_codon,
          (*files).seq [ index ].next_codon );

      /* Get the next amino acid. */
      if (( (*files).seq [ index ].group >= 0 ) || 
          ( (*files).seq [ index ].group == UNKNOWN ))
        status = next_data_character ( (*files).seq [ index ].protein,
            (*files).seq [ index ].line,
            &((*files).seq [ index ].line_index),
            &((*files).seq [ index ].pep_char) );
      else
      {
        (*files).seq [ index ].pep_char = ' ';
        if ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP ) 
        {
          if ( (*files).seq [ index ].group == MUTATION_DNA )
            status = next_codon ( (*files).seq [ index ].protein,
                (*files).seq [ index ].line,
                &((*files).seq [ index ].line_index),
                (*files).seq [ MUTATION_SEQ ].next_codon );
          else
            if ( (*files).seq [ index ].group >= DNA_SEQUENCE )
              status = next_data_character ( (*files).seq [ index ].protein,
                  (*files).seq [ index ].line,
                  &((*files).seq [ index ].line_index),
                  &((*files).seq [ index ].pep_char) );
        }  /* if */
      }  /* else */

      if ( index > 0 )
        if (( (*files).seq [ index - 1 ].group != (*files).seq [ index ].group )
            && ( (*files).seq [ index ].group >= 1 ) )
          printf ( " " );

      if ( (*files).seq [ index ].group >= 0 )
      {
        printf ( "%c", (*files).seq [ index ].pep_char );

        if ( ( (*files).seq [ index ].pep_char != SEQ_GAP ) && 
             ( (*files).seq [ index ].pep_char != SEQ_SKIP ) )
          (*files).seq [ index ].amino_acids++;
      }  /* if */

      if ( (*files).seq [ index ].group == UNKNOWN )
        printf ( " %c", (*files).seq [ index ].pep_char );

      if ( status == S_NORMAL )
      {
        read_status = S_NORMAL;

        /* Set the conservative substitution mask bit(s). */
        set_mask ( cons_masks, (*files).seq [ index ].pep_char, 
          &((*files).seq [ index ].cons_mask) );

        set_mask ( ident_masks, (*files).seq [ index ].pep_char, 
          &((*files).seq [ index ].ident_mask) );
      }  /* if */
      else
      {
        (*files).seq [ index ].cons_mask = ALL_BITS;  /* End of file */
        (*files).seq [ index ].ident_mask = ALL_BITS;  /* End of file */
      }  /* else */
    }  /* for */

    /* Count the novel sequence changes. */
    count_novel ( files );

    /* Check the codon against the mutation sequence amino acid. */
/*    if ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP )
    {
      translate ( (*files).seq [ MUTATION_SEQ ].current_codon, &amino_acid );
      if (( (*files).seq [ MUTATION_SEQ ].pep_char != amino_acid ) &&
          ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_SKIP ))
        printf ( "*WARN* codon '%s' mismatches amino acid '%c'\n",
            (*files).seq [ MUTATION_SEQ ].current_codon, 
            (*files).seq [ MUTATION_SEQ ].pep_char );
    }  /* if */

    /* Print the mutation sequence amino acid position. */
    if ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP )
    {
      position++;
      printf ( "%5d (%s) ", position, 
          (*files).seq [ MUTATION_SEQ ].current_codon );
      (*files).codon_number = position;
    }  /* if */
    else
      printf ( "            " );

    /* Compute the conservation of the amino acids at this position. */
    generics ( files, groups, method_II );
    generics_2 ( files, groups, method_I );

    /* Record the main sequence and its conservation level. */
    if ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP )
    {
      /* Store the main sequence amino acid letter. */
      (*sequence).amino_acid [ (*sequence).total ] =
          (*files).seq [ MUTATION_SEQ ].pep_char;

      /* Store the main sequence conservation level. */
      (*sequence).conservation [ (*sequence).total ] =
          cons_letters [ (*groups).conservation [ 0 ] ];

      if ( (*sequence).total < MAX_RESIDUES )
        (*sequence).total++;    /* new residue added */
      else
        printf ( "*INFO* Main sequence is longer than the sequence table.\n" );
    }  /* if */

    /* Record the main sequence and its conservation level. */
    if ( (*files).seq [ MUTATION_SEQ ].pep_char != SEQ_GAP )
    {
      /* Store the main sequence amino acid letter. */
      (*sequence).amino_acid [ (*sequence).total - 1 ] =
          (*files).seq [ MUTATION_SEQ ].pep_char;

      /* Store the main sequence conservation level. */
      (*sequence).conservation [ (*sequence).total - 1 ] =
          cons_letters [ (*groups).conservation [ 0 ] ];
    }  /* if */

    /* Print out the mutations at this amino acid site. */
    process_seq_mutations ( files, sequence );

    /* Process the mutations table. */
    process_mutations ( files, method_I, &mutation, 
        (*groups).conservation [ MUTATION_GROUP ], &DNA_sequence );
    printf ( "\n" );
  }  /* while */

  /* Print out the mutation summary. */
  printf ( "\nMethod I: Conservative substitutions allowed if seen in mammals.\n" );
  print_stats ( method_I );
}  /* scan_sequences */


/******************************************************************************/
/* This function counts the novel sequence changes. */
count_novel ( files )
t_files  *files;    /* files table */
{
  int   index;       /* current sequence */
  char  pep_char;    /* first sequence amino acid */


  /* Check for gap in original sequence or ignore character. */
  pep_char = (*files).seq [ MUTATION_SEQ ].pep_char;

  if ( ( pep_char == SEQ_GAP ) || ( pep_char == SEQ_SKIP ) )  return;

  /* Scan for the first sequences that differs from the first sequence. */
  for ( index = 1; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group >= 0 )
      if ( (*files).seq [ index ].pep_char != SEQ_GAP )
        if ( (*files).seq [ index ].pep_char != pep_char )
        {
          (*files).seq [ index ].novel++;
          return; 
        }  /* if */
}  /* count_novel */


/******************************************************************************/
/* This function does a binary search of an array of character strings. */
binary_strings ( target, strings, size, lower )
char  target [];      /* target character string */
char  *strings [];    /* array of sorted character strings */
int   size;          /* upper array bound of strings */
int   *lower;         /* position of target in strings or insertion index */
{
  int  middle;          /* middle index of search range */
  int  upper = size;    /* upper index of search range */

  *lower = 0;    /* lower index of search range */
  while ( *lower <= upper )
  {
    middle = (*lower + upper) / 2;

    if ( strcmp ( target, strings [ middle ] ) > 0 )
      *lower = middle + 1;    /* look in the upper half */
    else
      upper = middle - 1;    /* look in the lower half */
  }  /* while */

  if ( *lower == size + 1 ) return ( S_NOT_FOUND );
  if ( strcmp ( target, strings [ *lower ] ) != 0 ) 
    return ( S_NOT_FOUND );
  return ( S_NORMAL );
}  /* binary_strings */


/******************************************************************************/
/* This function returns the next token from line. */
get_field ( line, line_index, token )
char	line [];	/* source line */
int	*line_index;	/* character to start at */
char	token [];	/* next token */
{
  char  delimiter = ' ';    /* delimiter character */
  int	index = 0;  	    /* array index of current token character */

  /* Skip leading spaces or blank lines. */
  while ( (line [ *line_index ] == ' ') || (line [ *line_index ] == '\n') )
    (*line_index)++;

  token [ index ] = '\0';
  /* Check for the end of line. */
  if ( line [ *line_index ] == '\0' )  return ( S_END_OF_LINE );

  /* Check for text string. */
  if ( line [ *line_index ] == '"' )
  {  
    delimiter = '"';
    (*line_index)++;
  }  /* if */

  /* Copy the token. */
  while ( (line [ *line_index + index ] != '\0') &&
      ( line [ *line_index + index ] != '\n' ) &&
    ( ( line [ *line_index + index ] != ',' ) || ( delimiter == '"') ) &&
      ( line [ *line_index + index ] != '"' ) )
  {
    token [ index ] = line [ *line_index + index ];
    ++index;
  }  /* while */

  /* Ignore trailing quote mark. */
  if ( line [ *line_index + index ] == '"' )  (*line_index)++;

  /* Ignore trailing comma. */
  if ( line [ *line_index + index ] == ',' )  (*line_index)++;

  (*line_index) += index;
  token [ index ] = '\0';
  return ( S_NORMAL );
} /* get_field */


/******************************************************************************/
/* This function returns the next token from line. */
get_token ( line, line_index, token )
char	line [];	/* source line */
int	*line_index;	/* character to start at */
char	token [];	/* next token */
{
  int	index = 0;	/* array index of current token character */

  /* Skip leading spaces or blank lines. */
  while ( (line [ *line_index ] == ' ') || (line [ *line_index ] == '\n') )
    (*line_index)++;

  token [ index ] = '\0';
  /* Check for the end of line. */
  if ( line [ *line_index ] == '\0' )  return ( S_END_OF_LINE );

  /* Copy the token. */
  while ( (line [ *line_index + index ] != '\0') &&
    ( line [ *line_index + index ] != '\n' ) &&
    ( line [ *line_index + index ] != ' ' ) &&
    ( line [ *line_index + index ] != ',' ) )
  {
    token [ index ] = line [ *line_index + index ];
    ++index;
  }  /* while */

  /* Ignore trailing comma. */
  if ( line [ *line_index + index ] == ',' )  (*line_index)++;

  (*line_index) += index;
  token [ index ] = '\0';
  return ( S_NORMAL );
} /* get_token */


/******************************************************************************/
/* This function capitalizes a string. */
capitalize (s)
char  *s;	/* string to capitalize */
{
  /* Traverse the string. */
  for ( ; *s != '\0'; s++ )

    /* Check for a lower case letter. */
    if ((*s >= 'a') && (*s <= 'z'))
      *s += 'A' - 'a';
}  /* capitalize */


/******************************************************************************/
/* This function converts a string to all lower case. */
lower_case (s)
char  *s;	/* string to capitalize */
{
  /* Traverse the string. */
  for ( ; *s != '\0'; s++ )

    /* Check for a lower case letter. */
    if ((*s >= 'A') && (*s <= 'Z'))
      *s += 'a' - 'A';
}  /* lower_case */


/******************************************************************************/
/* This function returns the next integer from line. */
get_integer ( line, line_index, integer )
char  line [];      /* the current line */
int   *line_index;  /* the current line index */
int   *integer;     /* the next integer from line */
{
  int sign = 1;  /* the sign of the integer */
  int status = S_NO_INTEGER;  /* fuction return status */

  *integer = 0;

  if ( line [ *line_index ] == '\0' )  return ( S_END_OF_LINE );

  /* ignore leading spaces */
  while ( line [ *line_index ] == ' ' )
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
  return ( status );
}  /* get_integer */


/******************************************************************************/
/* This function returns the next real number from line. */
get_real ( line, line_index, real )
char    line [];        /* the current line */
int     *line_index;    /* the current line index */
double  *real;          /* the next real number from line */
{
  double  decimal = 1.0;      /* no decimal point yet */
  double  divisor = 1.0;      /* divisor to set decimal point */
  double  sign = 1.0;         /* the sign of the integer */
  int  status = S_NO_REAL;    /* fuction return status */

  *real = 0.0;

  if ( line [ *line_index ] == '\0' )  return ( S_END_OF_LINE );

  /* ignore leading spaces */
  while ( line [ *line_index ] == ' ' )
    (*line_index)++;

  /* check for a negative sign */
  if (line [ *line_index ] == '-')
  {
    sign = -1.0;
    (*line_index)++;
  }  /* minus sign */

  /* traverse the number */
  while ( ( ( line [ *line_index ] >= '0' ) &&
            ( line [ *line_index ] <= '9' ) ) ||
            ( line [ *line_index ] == '.' ) )
  {
    if ( line [ *line_index ] == '.' )  
      decimal = 10.0;
    else
    {
      (*real) = (*real) * 10.0 + ( line [ *line_index ] - '0' ) * 1.0;
      divisor = divisor * decimal;
    }  /* else */
    (*line_index)++;
    status = S_NORMAL;
  }  /* traverse */

  *real = sign * (*real) / divisor;

  return ( status );
}  /* get_real */


/******************************************************************************/
/* This function returns the current line. */
get_line ( data, line )
FILE  *data;    /* the data file to input from */
char  line [];  /* the next line from the data file */
{
  int  c;      /* current character */
  int  index;  /* line index */

  for ( index = 0;
       (index < MAX_LINE - 1) && ((c = getc (data)) != EOF) && (c != '\n');
       ++index )
    line [ index ] = c;

  line [ index ] = '\0';  /* terminate line */

  if ( c == EOF )  return ( S_EOF );
  return ( S_NORMAL );
}  /* get_line */


/******************************************************************************/
/* This function returns the current big line. */
get_big_line ( data, line )
FILE  *data;    /* the data file to input from */
char  line [];  /* the next line from the data file */
{
  int  c;      /* current character */
  int  index;  /* line index */

  for ( index = 0;
       (index < SUPER_LINE - 1) && ((c = getc (data)) != EOF) && (c != '\n');
       ++index )
    line [ index ] = c;

  line [ index ] = '\0';  /* terminate line */

  if ( c == EOF )  return ( S_EOF );
  return ( S_NORMAL );
}  /* get_big_line */


/******************************************************************************/
/* This function reverses string s in place. */
/* Kernighan & Ritchie, 1978 p.59 */
reverse ( s )
char  s [];
{
  int  c, i, j;

  for ( i = 0, j = strlen ( s ) - 1; i < j; i++, j-- )
  {
    c = s [ i ];
    s [ i ] = s [ j ];
    s [ j ] = c;
  }  /* for */
}  /* reverse */


/******************************************************************************/
/* This function converts an integer to an ascii string. */
/* Kernighan & Ritchie, 1978 p.60 */
itoa ( n, s )
int   n;       /* integer to convert to an ascii string */
char  s [];    /* function return string */
{
  int  i = 0;    /* string index */
  int  sign;     /* sign of number */

  if ( ( sign = n ) < 0 )    /* record sign */
    n = -n;                  /* make n positive */

  /* Generate the digits in reverse order. */
  do
  {
    s [ i++ ] = n % 10 + '0';    /* get the next digit */
  }  /* do */
  while ( ( n /= 10 ) > 0 );    /* delete it */

  if ( sign < 0 )  s [ i++ ] = '-';

  s [ i ] = '\0';    /* terminate the string */

  reverse ( s );
}  /* itoa */


/******************************************************************************/
/* This function opens the specified file for reading. */
open_file ( name, input_file )
char  name [];		/* filename */
FILE  **input_file;	/* file to open for reading */
{
  FILE  *fopen ();	/* file open function */

  *input_file = fopen ( name, "r" );
  if ( *input_file == NULL )
  {
    printf ( "*WARNING*: could not open the file '%s'.\n", name );
    return ( S_OPEN_FAILED );
  }  /* if */

  return ( S_NORMAL );
}  /* open_file */


/******************************************************************************/
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

  while ((( line [ *line_index ] == ' ' ) ||
    ( line [ *line_index ] == '\0' )) && ( status != S_EOF ))
  {
    /* ignore leading spaces */
    while ( line [ *line_index ] == ' ' )
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

  if ( status != S_NORMAL )
  {
    *character = SEQ_GAP;
    return ( status );
  }  /* if */
  *character = line [ (*line_index)++ ];
  return ( S_NORMAL );
}  /* next_data_character */


/******************************************************************************/
/* This function returns the next codon. */
next_codon ( data, line, line_index, codon )
FILE *data;  /* DNA sequence data file */
char line [];  /* current data line */
int *line_index;  /* current line index */
char codon [];  /* return codon */
{
  int status = S_NORMAL;  /* function return status */
  int codon_index;  /* position in codon */

  /* retreive the codon */
  for (codon_index = 0;
      (codon_index < MAX_CODON) && (status == S_NORMAL);
      codon_index++)
    status = next_data_character (data, line, line_index,
      &(codon [ codon_index ]));
  return (status);
}  /* next_codon */


/******************************************************************************/
/* This function translates a codon to an amino acid. */
translate ( codon, amino_acid )
char  codon [];  /* codon to translate */
char  *amino_acid;  /* encoded amino acid of the codon */
{
  int  position = 0;  /* triplet position */
  int  stat_index;    /* amino acid statistics index */

  position = map_base ( codon [ BASE_1 ] ) * 16 +
      map_base ( codon [ BASE_2 ] ) * 4 + 
      map_base ( codon [ BASE_3 ] );

  /* translate position in genetic code table into amino acid. */
  stat_index = genetic_code [ position ];
  *amino_acid = amino_acids [ stat_index ];
  return ( S_NORMAL );
}  /* translate */


/******************************************************************************/
/* This function prompts for the name of the file of file names. */
prompt_files ( file_name, file_of_file_names )
char  file_name [];		/* name of input file */
FILE  **file_of_file_names;	/* file of file names */
{
  int   line_index;		/* line index for get_integer */

  /* check  if the first protein file name has been specified */
  while ( *file_of_file_names == NULL )
  {
    printf ("What is the name of the file of file names? ");
    scanf ( "%s", file_name );
    open_file ( file_name, file_of_file_names );
  }  /* while */

  printf ( "\n" );
}  /* prompt_files */


/******************************************************************************/
/* This function prints out the N x N comparison statistics. */
print_NxN ( comparison, files )
t_comparison  *comparison;    /* unknown sequences comparison statistics */
t_files       *files;         /* table of sequences */
{
  FILE   *compare;           /* standard output comparison file */
  int    compare_index;      /* comparison sequence index */
  FILE   *fopen ();	     /* file open function */
  float  identity;           /* conservation level identity comparison */
  int    file_index = -1;    /* comparision output file index */
  int    percent;            /* integer percent identity */
  int    seq_index;          /* current sequence */
  double spacers;            /* fraction of spacer residues */


  /* Search for the comparison output file name. */
  for ( seq_index = 0; seq_index < (*files).total; seq_index++ )

    if ( (*files).seq [ seq_index ].group == COMPARE_FILE )
      file_index = seq_index;

  if ( file_index == -1 )  return;

  /* Print the sequence names and index to the comparison output file. */
  for ( seq_index = 0; seq_index < (*files).total; seq_index++ )

    fprintf ( (*files).seq [ file_index ].protein, "%d,\"%14s\"\n", seq_index, 
        (*files).seq [ seq_index ].file_name );

  printf ( "\n\nN x N Sequence comparisons:\n\n" );

  for ( seq_index = 0; seq_index < (*files).total; seq_index++ )
  
    /* Check for a protein sequence. */
    if ( (*files).seq [ seq_index ].group >= 0 )
    {
      for ( compare_index = 0; compare_index <= seq_index; compare_index++ )
        fprintf ( (*files).seq [ file_index ].protein, "0," );

      for ( compare_index = seq_index + 1; compare_index < (*files).total; 
          compare_index++ )

        /* Check for a protein sequence. */
        if ( (*files).seq [ compare_index ].group >= 0 )
        {
          if ( (*comparison).sequences [ seq_index ]
               .N [ compare_index ].compared <= 0 )  identity = 0;
          else
            identity = ( (*comparison).sequences [ seq_index ]
                .N [ compare_index ].identity * 1.0 ) /
                ( (*comparison).sequences [ seq_index ]
                .N [ compare_index ].compared * 1.0 );

          printf ( "%14s x ", (*files).seq [ seq_index ].file_name );
          printf ( "%14s: ", (*files).seq [ compare_index ].file_name );

          printf ( "%5d/", (*comparison).sequences [ seq_index ]
              .N [ compare_index ].identity );
          printf ( "%5d * 100%% = ", (*comparison).sequences [ seq_index ]
              .N [ compare_index ].compared );
          printf ( "%3.0f%%\n", identity * 100.0 );
          percent = identity * 100;
          fprintf ( (*files).seq [ file_index ].protein, "%d,", percent );
        }  /* if */

      printf ( "\n" );
      fprintf ( (*files).seq [ file_index ].protein, "\n" );
    }  /* if */

  /* Compute the fraction of spacer residues. */
  fraction_spacers ( files, comparison, &spacers );

  /* Compare observed and estimated sequence differences. */
  est_hits ( files, comparison );
}  /* print_NxN */


/******************************************************************************/
/* This function prints out the comparison data. */
print_comparison ( comparison, files, start, end )
t_comparison  *comparison;    /* unknown sequences comparison statistics */
t_files       *files;         /* table of sequences */
int           start;          /* lowest conservation level to start at */
int           end;            /* ending conservation level */
{
  int    cons_index;      /* current conservation level index */
  float  conservation;    /* conservation level conservation comparison */
  float  identity;        /* conservation level identity comparison */
  int    seq_index;       /* current sequence */


  if ( (*comparison).unknown_seq == -1 )  return;

  printf ( "\n\nSequence '%s' comparison statistics.\n\n",
    (*files).seq [ (*comparison).unknown_seq ].file_name );

  printf ( "Sequence        " );
  for ( cons_index = start; cons_index <= end; cons_index++ )
    printf ( " %s    ", cons_names [ cons_index ] );
  printf ( "\n" );

  for ( seq_index = 0; seq_index < (*files).total; seq_index++ )
    if ( (*files).seq [ seq_index ].group >= 0 )
    {
      printf ( "%14s  ", (*files).seq [ seq_index ].file_name );
  
      for ( cons_index = start; cons_index <= end; cons_index++ )
      {
        if ( (*comparison).sequences [ seq_index ].compared [ cons_index ] > 0 )
        {
          identity = ( (*comparison).sequences [ seq_index ].identity 
              [ cons_index ] * 1.0 ) / ( (*comparison).sequences [ seq_index ]
              .compared [ cons_index ] * 1.0 );
          conservation = ( (*comparison).sequences [ seq_index ].conservation 
              [ cons_index ] * 1.0 ) / ( (*comparison).sequences [ seq_index ]
              .compared [ cons_index ] * 1.0 );
        }  /* if */
        else 
        {
          identity = 0.0;
          conservation = 0.0;
        }  /* else */
        printf ( "%3.0f%%%/", identity * 100.0 );
        printf ( "%3.0f%%% ", (identity + conservation) * 100.0 );
        printf ( "  (%3d+%3d)/%3d   ", 
            (*comparison).sequences [ seq_index ].identity [ cons_index ], 
            (*comparison).sequences [ seq_index ].conservation [ cons_index ], 
            (*comparison).sequences [ seq_index ].compared [ cons_index ] );
      }  /* for */

    printf ( "\n" );
  }  /* for */
}  /* print_comparison */


/******************************************************************************/
/* This function prints out the lost comparison data. */
print_lost ( comparison, files )
t_comparison  *comparison;    /* unknown sequences comparison statistics */
t_files       *files;         /* table of sequences */
{
  int    cons_index;      /* current conservation level index */
  int    seq_index;       /* current sequence */


  if ( (*comparison).unknown_seq == -1 )  return;

  printf ( "\n\nSequence '%s' lost comparison statistics.\n\n",
    (*files).seq [ (*comparison).unknown_seq ].file_name );

  printf ( "Sequence        IG   CG   IP   CP   IS   CS  NC  ALL\n" );

  for ( seq_index = 0; seq_index < (*files).total; seq_index++ )
    if ( (*files).seq [ seq_index ].group >= 0 )
    {
      printf ( "%14s  ", (*files).seq [ seq_index ].file_name );
  
      for ( cons_index = GENERIC; cons_index <= OVERALL; cons_index++ )
        printf ( "%5d ", 
            (*comparison).sequences [ seq_index ].lost [ cons_index ] ); 
    printf ( "\n" );
  }  /* for */
}  /* print_lost */


/******************************************************************************/
/* This function prints out the mutations table. */
print_stats ( mutations )
t_mutations	*mutations;	/* table of mutation statistics */
{
  float  amino_acids;	/* number of amino acids */
  int    index;         /* category index */


  printf ( "\nCategory          AA\n" );

  amino_acids = (*mutations).cg_amino_acids [ 0 ] +
      (*mutations).nn_amino_acids [ 0 ] +
      (*mutations).cg_amino_acids [ 1 ] +
      (*mutations).nn_amino_acids [ 1 ];

  for ( index = 0; index < CATEGORIES; index++ )
  {
    printf ( "%s  ", categories [ index ] ); 

    amino_acids = (*mutations).cg_amino_acids [ index * 2 ] +
        (*mutations).nn_amino_acids [ index * 2 ];

    if ( index < CATEGORIES - 1 )
      amino_acids += (*mutations).cg_amino_acids [ index * 2 + 1 ] +
          (*mutations).nn_amino_acids [ index * 2 + 1 ];

    printf ( "  %5.1f\n", amino_acids );
  }  /* for */
}  /* print_stats */


/******************************************************************************/
/* This function converts a three letter amino acid name to a single letter name. */
char  map_aa_name ( amino_acid )
char  amino_acid [];    /* amino acid three letter name */
{
  int  index;    /* table index of amino acid name */
  int  status;    /* function return status */


  /* Binary search the sorted list of known three letter names. */
  status = binary_strings ( amino_acid, amino_acids_3, MAX_AA_NAMES, &index );

  if ( status == S_NORMAL )
    return ( amino_acids_1 [ index ] );

  printf ( "*INFORMATION* Unknown three letter code in PDB file '%s'.\n",
      amino_acid );
  return ( '-' );    /* invalid amino acid name */
}  /* map_aa_name */


/******************************************************************************/
/* This function reads in a PDB file. */
read_pdb ( data, residues, atoms )
FILE        *data;        /* PDB file */
t_residues  *residues;    /* residue and name array */
t_atoms     *atoms;       /* atoms table */
{
  char coordinate [ MAX_LINE ];       /* amino acid residue number */
  char line [ MAX_LINE ];             /* current PDB file line */
  int  line_index;                    /* character index of line */
  char previous_coordinate [ MAX_LINE ];    /* previous amino acid residue number */
  char previous_name [ MAX_LINE ];    /* previous amino acid residue name */
  int  status = S_NORMAL;             /* status of called functions */
  char token [ MAX_LINE ];            /* token from current line */


/* printf ( "\n\nPDB coordinates:\n" ); */

  /* Read in the PDB file. */
  (*residues).total = 0;         /* no residues */
  (*residues).min_area = MIN_SURFACE_AREA;    /* minimum surface area */
  previous_name [ 0 ] = '\0';    /* no previous amino acid residue */
  while ( status != S_EOF )
  {
    /* Get the next line from the PDB file. */
    status = get_line ( data, line );
    line_index = 0;

    /* Get the line identifier. */
    token [ 0 ] = '\0';
    if ( ( line [ 0 ] != '\0' ) && ( status == S_NORMAL ) )
      get_token ( line, &line_index, token );

    /* Check for the end of the file or fragment. */
    if ( ( strcmp ( token, "END" ) == 0 ) || ( strcmp ( token, "TER" ) == 0 ) )
      return;

    /* Check for the atom line. */
    if ( strcmp ( token, "ATOM" ) == 0 )
    {
      if ( (*atoms).total < MAX_ATOMS - 1 )  (*atoms).total++;
      else printf ( "*Warning* atoms table filled.\n" );

      /* Get the atom number. */
      get_integer ( line, &line_index, 
          &((*atoms).atom [ (*atoms).total ].atom_number) );

      /* Get the atom name. */
      get_token ( line, &line_index,
          (*atoms).atom [ (*atoms).total ].atom_name );

      /* Get the amino acid three letter name. */
      line_index = POS_AMINO_ACID;
      get_token ( line, &line_index, token );

      /* Convert the name to the single letter code and save. */
      (*atoms).atom [ (*atoms).total ].residue = map_aa_name ( token );

      /* Get the amino acid coordinate number. */
      line_index = POS_RESIDUE_NO;
      get_token ( line, &line_index, coordinate );

      /* Save the atom coordinate. */
      strcopy ( (*atoms).atom [ (*atoms).total ].coordinate, coordinate );

      /* Get the atom coordinates. */
      get_real ( line, &line_index, &((*atoms).atom [ (*atoms).total ].x) );
      get_real ( line, &line_index, &((*atoms).atom [ (*atoms).total ].y) );
      get_real ( line, &line_index, &((*atoms).atom [ (*atoms).total ].z) );
      (*atoms).atom [ (*atoms).total ].dots = 0;

      /* Check if first residue. */
      if ( (*residues).total == 0 )
        (*atoms).atom [ (*atoms).total ].residue_index = 0;
      else
        (*atoms).atom [ (*atoms).total ].residue_index = (*residues).total - 1;

/* printf ( "%d Atom %d, ", (*atoms).total, 
    (*atoms).atom [ (*atoms).total ].atom_number );
printf ( "%Residue %s ", coordinate );
printf ( "x %7.3f", (*atoms).atom [ (*atoms).total ].x );
printf ( ", y %7.3f", (*atoms).atom [ (*atoms).total ].y );
printf ( ", z %7.3f\n", (*atoms).atom [ (*atoms).total ].z ); */

      /* Check if new residue */
      if ( ( ( strcmp ( token, previous_name ) != 0 ) ||
             ( strcmp ( coordinate, previous_coordinate ) != 0 ) )
           && ( status == S_NORMAL ) )
      {
        /* Save the name */
        token [ MAX_RESIDUE_NAME - 1 ] = '\0';    /* truncate to fit */
        strcopy ( (*residues).aa [ (*residues).total ].name, token );

        /* Save the amino acid single letter code. */
        (*residues).aa [ (*residues).total ].amino_acid =
            (*atoms).atom [ (*atoms).total ].residue;

        /* Save the current to compare to the next. */
        strcopy ( previous_coordinate, coordinate );
        strcopy ( previous_name, token );

        /* Save the coordinate name. */
        coordinate [ MAX_RESIDUE_NAME - 1 ] = '\0';    /* truncate to fit */
        strcopy ( (*residues).aa [ (*residues).total ].coordinate, coordinate );

        /* Initialize the surface area. */
        /* All surface areas > 0 for when there is no DSSP file. */
        (*residues).aa [ (*residues).total ].surface_area = MIN_SURFACE_AREA;

        (*residues).total++;    /* table entry added */
        /* Check if PDB file is too big. */
        if ( (*residues).total == MAX_RESIDUES - 1 )
        {
          printf ( "*WARNING* PDB file larger than residues table.\n" );
          return;
        }  /* if */
      }  /* if */
    }  /* if */
  }  /* while */

/*  printf ( "\n\n" ); */
}  /* read_pdb */


/******************************************************************************/
/* This function inserts blanks into the PDB sequence for gaps in sequence. */
fill_gaps ( residues )
t_residues  *residues;    /* residue and name array */
{
  int  current;      /* current residue number */
  int  index;        /* residues index */
  int  last = 0;     /* previous residue number */
  int  move;         /* index */

  /* Compare the sequences. */
  for ( index = 0; index < (*residues).total; index++ )
  {
    move = 0;    /* line index of coordinate */
    get_integer ( (*residues).aa [ index ].coordinate, &move, &current );
    if (( current > last + 1 ) && ( last != 0 ))
    {
      for ( move = (*residues).total; move >= index; move-- )
      {
        (*residues).aa [ move + current - last - 1 ].amino_acid = 
            (*residues).aa [ move ].amino_acid; 
         strcopy ( (*residues).aa [ move + current - last - 1 ].name, 
            (*residues).aa [ move ].name );
        strcopy ( (*residues).aa [ move + current - last - 1 ].coordinate, 
            (*residues).aa [ move ].coordinate );
      }  /* for */

      for ( move = index + current - last - 1; move >= index; move-- )
      {
        (*residues).aa [ move ].amino_acid = ' ';
        strcopy ( (*residues).aa [ move ].name, "   " );
        strcopy ( (*residues).aa [ move ].coordinate, "   " );
      }  /* for */
      (*residues).total += current - last - 1;
    }  /* if */
    last = current;
  }  /* for */
}  /* fill_gaps */


/******************************************************************************/
/* This function processes the PDB file if there is one. */
#define  DEFAULT_RESOLUTION  0.2    /* default X-ray resolution in nm */

process_pdb ( files, sequence, residues, atoms )
t_files		 *files;	 /* table of files */
t_main_sequence  *sequence;      /* main sequence of interest */
t_residues       *residues;      /* PDB residue and name array */
t_atoms          *atoms;         /* atoms table */
{
  int         cons;                     /* conservation class index */
  FILE	      *fopen ();		/* file open function */
  int         index = 0;
  int         length;                   /* coordinate name string length */
  int         offset = 0;               /* offset into sequence for PDB */
  char        pdb_name [ MAX_LINE ];    /* PDB file name */
  int         pdb_index = 0;            /* index of PDB file */
  int         printed;                  /* number of residue names printed */


  (*atoms).total = 0;    /* no atoms */

  /* Check for a PDB file. */
  while ( ( pdb_index < (*files).total ) && 
          ( (*files).seq [ pdb_index ].group != PDB_FILE ) )  pdb_index++;
  if ( (*files).seq [ pdb_index ].group != PDB_FILE )  return;

  /* Set the resolution of the crystal structure. */
  if ( (*files).seq [ pdb_index ].diverge > 0 )
  {
    (*residues).resolution = 
        ( (*files).seq [ pdb_index ].diverge * 1.0 ) / 1000.0;
    printf ( "\nSpecified crystal structure resolution for PDB = %5.3f nm\n", 
        (*residues).resolution );
  }  /* if */
  else  (*residues).resolution = DEFAULT_RESOLUTION;

  /* Read in the PDB file. */
  read_pdb ( (*files).seq [ pdb_index ].protein, residues, atoms );

  /* Remove trailing non-amino acids. */
  while ( ( (*residues).total > 0 ) &&
      ( (*residues).aa [ (*residues).total - 1 ].amino_acid == '-' ) )
    (*residues).total--;

  if ( (*residues).total == 0 )
  {
    printf ( "No residues in the PDB file '%s'.\n",
        (*files).seq [ pdb_index ].file_name );
    return;
  }  /* if */

/*  fill_gaps ( residues );    /* fill in any empty gaps */

  /* Compare the sequences. */
  offset = (*files).seq [ pdb_index ].descriptor - 1;
}  /* process_pdb */


/******************************************************************************/
#define  DEF_TRIALS  100       /* default number of random trials */
/* This function generates random specific .csf and .dsf files. */
random_csfs ( files, sequence, residues, atoms )
t_files          *files;       /* table of files */
t_main_sequence  *sequence;    /* main sequence of interest */
t_residues       *residues;    /* PDB residue and name array */
t_atoms          *atoms;       /* atoms table */
{
  char             csf_name [ MAX_LINE ];    /* random .csf file name */
  int              index, i, j;                 /* loop index */
  int              offset = 0;               /* offset into sequence for PDB */
  char             pdb_name [ MAX_LINE ];    /* pdb name */
  t_main_sequence  random;                   /* randomly colored sequence */
  int              specifics = 0;            /* number of specific residues */
  t_sizes          sizes;                    /* cluster sizes for random trials */
  int              trials;                   /* number of random trials */
  char             version [ MAX_LINE ];     /* pdb name */


  /* Check for a PDB file. */
  index = 0;
  while ( ( index < (*files).total ) && 
          ( (*files).seq [ index ].group != PDB_FILE ) )  index++;
  if ( (*files).seq [ index ].group != PDB_FILE )  return;

  if ( (*residues).total == 0 )  return;

  strcpy ( pdb_name, (*files).seq [ index ].file_name );
  pdb_name [ charidx ( '.', pdb_name ) ] = '\0';    /* remove the suffix */
  lower_case ( pdb_name );    /* convert the pdb_name to lower case */

  offset = (*files).seq [ index ].descriptor - 1;
  if ( offset < 0 )  offset = 0;

  trials = (*files).seq [ index ].inc_diverge;
  if ( trials <= 0 )  trials = DEF_TRIALS;
  if ( trials > MAX_TRIALS )  trials = MAX_TRIALS;

  /* Count the number of specifics in the crystal structure. */
  for ( index = 0; index < (*residues).total; index++ )
    if ( ( (*residues).aa [ index ].amino_acid != ' ' ) &&
         ( (*residues).aa [ index ].surface_area > 
           (*residues).min_area ) )
      if ( ( (*sequence).conservation [ index + offset ] == 'S' ) ||
           ( (*sequence).conservation [ index + offset ] == 's' ) )
        specifics++;

  printf ( "\nSurface specifics = %d\n\n", specifics );

  for ( index = 0; index < (*sequence).total; index++ )
  {
    (*sequence).previous [ index ] = -1;
    (*sequence).next [ index ] = -1;
    (*sequence).mark [ index ] = OFF;
    (*sequence).surface [ index ] = 0;
    (*sequence).dots [ index ] = 0;
  }  /* for */

  /* Process the QUANTA surface dots file. */
  process_dots ( files, atoms );

  /* Copy the surface dots to the specifics. */
  copy_dots ( atoms, sequence, offset );

  /* Initialize cluster sizes. */
  initialize_sizes ( &sizes );

  /* Calculate the surface specific clusters. */
  surface_clusters ( atoms, residues, sequence, offset );

  /* List the specific clusters. */
  printf ( "\nSurface Specific Clusters.\n" );
  list_clusters ( sequence, &sizes, 0, residues, offset );

  /* List the distances between specifics. */
/*  list_distances ( atoms, residues, sequence, offset ); */

  printf ( "\nSequence Summary:\n" );

  index = 0;
  while ( index < offset )
  {
    printf ( "%5d (-----) %c-%c,  surface atoms %5d, surface dots %5d\n",
        index + 1,
        (*sequence).amino_acid   [ index ],
        (*sequence).conservation [ index ],
        (*sequence).surface      [ index ],
        (*sequence).dots         [ index ] );
    index++;
  }  /* while */

  for ( index = offset; index < (*sequence).total, 
        index - offset < (*residues).total; index++ )
  {
    printf ( "%5d (%5s) %c-%c, surface atoms %5d, surface dots %5d",
        index + 1,
        (*residues).aa [ index - offset ].coordinate, 
        (*sequence).amino_acid   [ index ],
        (*sequence).conservation [ index ],
        (*sequence).surface      [ index ],
        (*sequence).dots         [ index ] );
    printf ( ", area %3d\n", (*residues).aa [ index - offset ].surface_area );
  }  /* for */

  index = (*residues).total + offset;
  while ( index < (*sequence).total )
  {
    printf ( "%5d (-----) %c-%c,  surface atoms %5d, surface dots %5d\n",
        index + 1,
        (*sequence).amino_acid   [ index ],
        (*sequence).conservation [ index ],
        (*sequence).surface      [ index ],
        (*sequence).dots         [ index ] );
    index++;
  }  /* while */

  printf ( "\n" );

/*  for ( index = 0; index < (*sequence).total; index++ )
  {
    if ( ( (*sequence).conservation [ index ] == 'S' ) ||
         ( (*sequence).conservation [ index ] == 's' ) )
      printf ( "Specific %d, previous = %d, next = %d\n", index,
          (*sequence).previous [ index ], (*sequence).next [ index ] );
  }  /* for */

  /* Initialize the random sequence. */
  random.total = (*sequence).total;
  for ( index = 0; index < (*sequence).total; index++ )
  {
    random.amino_acid   [ index ] = (*sequence).amino_acid [ index ];
    random.conservation [ index ] = 'N';
    random.surface      [ index ] = (*sequence).surface [ index ];
    random.dots         [ index ] = (*sequence).dots [ index ];
  }  /* for */

  for ( index = 1; index <= trials; index ++ )
  {
    printf ( "\nRandomly colored surface model %d\n", index );

    /* Generate a randomly colored model. */
    strcpy ( csf_name, pdb_name );
    concatenate ( csf_name, "_r" );
    itoa ( index, version );
    concatenate ( csf_name, version );
    concatenate ( csf_name, ".csf" );
    random_csf ( residues, specifics, &random, offset );

    /* Calculate the specific clusters. */
    surface_clusters ( atoms, residues, &random, offset );

    /* List the specific clusters. */
    list_clusters ( &random, &sizes, index, residues, offset );

    /* Write out the random csf. */
    write_csf ( residues, pdb_name, csf_name, specifics, &random, offset,
        &(sizes.sizez [ index ]) );
  }  /* for */

  /* Print cluster sizes. */
  print_sizes ( &sizes, trials );
}  /* random_csfs */


/******************************************************************************/
/* Print out all of the largest random specific cluster sizes. */
print_sizes ( sizes, trials )
t_sizes  *sizes;    /* largest cluster sizes */
int      trials;    /* number of trials */
{
  int  index;       /* trial index */
  int  j;
  int  to_print;    /* printing flag */
  int  ge_size;     /* number of clusters >= to size */
  int  ge_atoms;    /* number of clusters >= to # atoms */
  int  ge_area;     /* number of clusters >= to surface area */


  /* Print cluster sizes. */
  printf ( "\nList of largest surface cluster sizes:\n" );
  printf ( "\nTrial  Residues/Surface atoms/Surface area\n" );
  for ( index = 0; index <= trials; index++ )
  {
    printf ( "%3d", index );
    for ( j = 0; j < MAX_SIZES; j++ )
    {
      if ( (*sizes).sizez [ index ].cluster [ j ].size >= 
           (*sizes).sizez [ 0     ].cluster [ j ].size ) 
        printf ( ",    %3d*", (*sizes).sizez [ index ].cluster [ j ].size );
      else
        printf ( ",    %3d ", (*sizes).sizez [ index ].cluster [ j ].size );
      if ( (*sizes).atomz [ index ].cluster [ j ].atoms >= 
           (*sizes).atomz [ 0     ].cluster [ j ].atoms ) 
        printf ( "  %3d*", (*sizes).atomz [ index ].cluster [ j ].atoms );
      else
        printf ( "  %3d ", (*sizes).atomz [ index ].cluster [ j ].atoms );
      if ( (*sizes).areas [ index ].cluster [ j ].area >= 
           (*sizes).areas [ 0     ].cluster [ j ].area ) 
        printf ( "  %4d*", (*sizes).areas [ index ].cluster [ j ].area );
      else
        printf ( "  %4d ", (*sizes).areas [ index ].cluster [ j ].area );
    }  /* for */
    printf ( "\n" );
  }  /* for */

  printf ( "\nRandom trial clusters larger than actual.\n" );
  for ( j = 0; j < MAX_SIZES; j++ )
  {
    ge_size = -1;    /* don't count trial 0 */
    ge_atoms = -1;
    ge_area = -1;

    printf ( "\nCluster %d\n", j + 1 );
    printf ( "Trial  Residues/Surface atoms/Surface area\n" );
    for ( index = 0; index <= trials; index++ )
    {
      to_print = OFF;
      if ( (*sizes).sizez [ index ].cluster [ j ].size >= 
           (*sizes).sizez [ 0     ].cluster [ j ].size )
      {
        ge_size++;
        to_print = ON;
      }  /* if */

      if ( (*sizes).atomz [ index ].cluster [ j ].atoms >= 
           (*sizes).atomz [ 0     ].cluster [ j ].atoms )
      {
        ge_atoms++;
        to_print = ON;
      }  /* if */

      if ( (*sizes).areas [ index ].cluster [ j ].area >= 
           (*sizes).areas [ 0     ].cluster [ j ].area )
      {
        ge_area++;
        to_print = ON;
      }  /* if */

      if ( to_print == ON )
      {
        printf ( "%3d", index );
        if ( (*sizes).sizez [ index ].cluster [ j ].size >= 
             (*sizes).sizez [ 0     ].cluster [ j ].size ) 
          printf ( ",   %3d*", (*sizes).sizez [ index ].cluster [ j ].size );
        else
          printf ( ",       " );
        if ( (*sizes).atomz [ index ].cluster [ j ].atoms >= 
             (*sizes).atomz [ 0     ].cluster [ j ].atoms ) 
          printf ( "  %3d*", (*sizes).atomz [ index ].cluster [ j ].atoms );
        else
          printf ( "      " );
        if ( (*sizes).areas [ index ].cluster [ j ].area >= 
             (*sizes).areas [ 0     ].cluster [ j ].area ) 
          printf ( "  %4d*\n", (*sizes).areas [ index ].cluster [ j ].area );
        else
          printf ( "\n" );
      }  /* if */
    }  /* for */

    printf ( "\n      >= %d   >= %d   >= %d\n", ge_size, ge_atoms, ge_area );
  }  /* for */
  printf ( "\n" );
}  /* print_sizes */


/******************************************************************************/
/* This function generates random specific .csf file data. */
random_csf ( residues, specifics, random, offset )
t_residues       *residues;       /* PDB residue and name array */
int              specifics;       /* number of specifics in crystal structure */
t_main_sequence  *random;         /* main sequence of interest */
int              offset;          /* offset into sequence */
{
  int         index;                    /* loop index */
  int         specific;                 /* random specific residue index */


  /* Initialize all of the residues to nonconserved. */
  for ( index = 0; index < MAX_RESIDUES; index++ )
    (*random).conservation [ index ] = 'N';

  /* Randomly assign residues to the specifics class. */
  index = 0;
  while ( index < specifics )
  {
    specific = ( ( rand () * 1.0 ) / MAX_RANDOM ) * ( (*residues).total * 1.0 );
    if ( ( specific >= 0 ) && ( specific < (*residues).total ) )
    {
      if ( ( (*residues).aa [ specific ].amino_acid != ' ' ) &&
           ( (*residues).aa [ specific ].surface_area > 
             (*residues).min_area ) &&
           ( (*random).conservation [ specific + offset ] != 'S' ) )
      {
        (*random).conservation [ specific + offset ] = 'S';
        index++;  /* one more specific assigned. */
      }  /* if */
    }
    else
      printf ( "random specific location %d\n", specific + offset );
  }  /* while */
}  /* random_csf */


/******************************************************************************/
/* This function writes out the .csf file. */
write_csf ( residues, pdb_name, csf_name, specifics, random, offset, size )
t_residues       *residues;       /* PDB residue and name array */
char             pdb_name [ ];    /* PDB name */
char             csf_name [ ];    /* .csf file name */
int              specifics;       /* number of specifics in crystal structure */
t_main_sequence  *random;         /* main sequence of interest */
int              offset;          /* offset into sequence */
t_size           *size;           /* largest cluster sizes */
{
  int         cons;                     /* conservation class index */
  FILE        *csf;                     /* color selection file */
  char        data_name [ MAX_LINE ];   /* data line */
  FILE	      *fopen ();		/* file open function */
  int         index, j;                 /* loop index */
  int         printed;                  /* number of residue names printed */


  /* Write out the .csf file. */
  csf = fopen ( csf_name, "w" );

  /* Check the open files for writing. */
  if ( csf == NULL )
  {
    printf ( "*WARN* unable to open .csf file '%s'\n.", csf_name );
    return;
  }  /* if */

  fprintf ( csf, "DATA %s.msf\n\n", pdb_name );

  /* Print out each conservation class. */
  for ( cons = 0; cons < MAX_CONSERVATION; cons++ )
  {
    printed = 0;
    /* Traverse the sequence printing out the conservation class. */
    for ( index = 0; index < (*residues).total; index++ )
      if (( (*random).conservation [ index + offset ] == 
          cons_letters [ cons ] )
          && ( (*residues).aa [ index ].amino_acid != ' ' ) )
      {
        if ( printed == 0 )    /* Check if new line */
          fprintf ( csf, "zone " );

        fprintf ( csf, "%s ", (*residues).aa [ index ].coordinate );
        printed++;
        
        if ( printed == MAX_NAMES_LINE )
        {
          fprintf ( csf, " = col %d\n", csf_color [ cons ] );
          printed = 0;
        }  /* if */
      }  /* if */

    if ( printed > 0 )
      fprintf ( csf, " = col %d\n", csf_color [ cons ] );

    fprintf ( csf, "\n" );
  }  /* for */

  /* Recolor the specifics in the largest clusters. */
  for ( j = 0; j < MAX_SIZES; j++ )
    print_cluster ( csf, random, size, residues, offset, j );

  fclose ( csf );    /* close the .csf file */
}  /* write_csf */


/******************************************************************************/
/* This function searches the atoms table for the atom number. */
find_atom ( atoms, atom_number )
t_atoms     *atoms;       /* atoms table */
int      atom_number;    /* atom_number to find */
{
  int  i;    /* index */


  /* Search for the atom number. */
  for ( i = 1; i <= (*atoms).total; i++ )
    if ( (*atoms).atom [ i ].atom_number == atom_number )  return ( i );

  return ( 0 );
}  /* find_atom */


/******************************************************************************/
/* This function reads in a QUANTA surface dot file. */
process_dots ( files, atoms )
t_files          *files;       /* table of files */
t_atoms          *atoms;       /* atoms table */
{
  int   atom_number = -1;       /* previous atom number */
  int   dots = 0;               /* dots for previous atom */
  int   index = 0;              /* loop index */
  int	line_index; 		/* current character index in line */
  int	status = S_NORMAL;	/* status of called functions */
  char	line [ MAX_LINE ];	/* current file of filenames line */
  char  token [ MAX_LINE ];     /* current character token */


  /* Check for a QUANTA dot file. */
  while ( ( index < (*files).total ) && 
          ( (*files).seq [ index ].group != DOT_FILE ) )  index++;
  if ( (*files).seq [ index ].group != DOT_FILE )  return;

  do
  {
    /* Get the next line from the QUANTA surface dot file. */
    status = get_line ( (*files).seq [ index ].protein, line );

    /* Get the first token from the line. */
    line_index = 0;
    if ( status == S_NORMAL )
      status = get_token ( line, &line_index, token );
    if ( status == S_NORMAL )
      if ( strcmp ( "Dots", token ) == 0 )
      {
        /* Set the number of dots for the atom. */
        if ( ( atom_number >= 1 ) && ( atom_number <= (*atoms).total ) )
          (*atoms).atom [ atom_number ].dots = dots;
        else
          if ( atom_number != -1 )
            printf ( "*INFO* unknown atom number %d.\n", atom_number );

        get_token ( line, &line_index, token );    /* consume 'for' */
        get_token ( line, &line_index, token );    /* consume 'atom' */
        status = get_integer ( line, &line_index, &atom_number );  
      }  /* if */
      else

        /* Get the number of dots on the previous atom. */
        if ( atom_number >= 0 )
        {
          line_index = 0;
          status = get_integer ( line, &line_index, &dots );
        }  /* if */
  }
  while ( status != S_EOF );

  if ( ( atom_number >= 1 ) && ( atom_number <= (*atoms).total ) )
    (*atoms).atom [ atom_number ].dots = dots;
  else
    printf ( "*INFO* unknown atom number %d.\n", atom_number );
}  /* process_dots */


/******************************************************************************/
/* This function calculates the fraction of spacers in a protein based on the */
/* percent identities between human and other mammals (not primates).         */
fraction_spacers ( files, comparison, spacers )
t_files       *files;         /* table of sequence files */
t_comparison  *comparison;    /* unknown sequences comparison statistics */
double        *spacers;       /* fraction of spacers computed */
{
  double  average = 0.0;    /* average number of amino acid differences */
  int     index;            /* current file index */
  int     mammals = 0;      /* number of mammals other than human */
  int     observed;         /* observed differences */


  /* Compute the average identity for all of the mammals. */
  for ( index = 1; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group == 0 )
      if ( (*files).seq [ index ].class == 'M' )
      {
        observed = 
            (*comparison).sequences [ MUTATION_SEQ ].N [ index ].compared -
            (*comparison).sequences [ MUTATION_SEQ ].N [ index ].identity;

        average += observed;
        mammals++;
      }  /* if */

  if ( mammals > 0 )  average = average / ( mammals * 1.0 );
  else  average = 0.0;    /* no other mammals */

  if ( (*files).seq [ MUTATION_SEQ ].descriptor == X_CHROMOSOME )
    *spacers = average / ( DIVERGE_MAMMALS * RHO_MAMMALS * X_MUTATION_RATE );
  else
    *spacers = average / ( DIVERGE_MAMMALS * RHO_MAMMALS );

  if ( (*spacers) >= (*files).seq [ MUTATION_SEQ ].amino_acids )
    *spacers = (*files).seq [ MUTATION_SEQ ].amino_acids * 1.0;

  printf ( "Computed spacers = %4.0f, ", *spacers );

  *spacers = (*spacers) / ( (*files).seq [ MUTATION_SEQ ].amino_acids * 1.0 );

  (*files).f_change = *spacers;    /* save the fraction of spacer residues */

  printf ( "fraction spacers = %6.3f\n", *spacers );
}  /* fraction_spacers */


/******************************************************************************/
/* This function estimates the number of hits for a specified time. */
hits ( files, index, comparison, years, hit_spacers )
t_files       *files;	       /* table of sequence files */
int           index;           /* current file index */
t_comparison  *comparison;     /* unknown sequences comparison statistics */
int           years;           /* divergence time in million of years */
double        *hit_spacers;    /* number of mutated spacer residues */
{
  long   amino_acids;                    /* protein length */
  double dna_bases;                      /* number of DNA base pairs */
  double d_hits;                         /* double of hits */
  double not_hit;                        /* all but one base is not hit */
  /* extern double pow ( double, double );                         /* power function */
  double q;                              /* base probability of not being hit */
  double unhit;                          /* number of unhit amino acids */
  double fraction_hit;                   /* fraction of amino acids hit */
  double hit_aa;                         /* number of mutated amino acids */


  if ( (*files).seq [ index ].group >= 0 )
  {
    amino_acids = (*files).seq [ index ].amino_acids;

    dna_bases = amino_acids * 3.0;

    /* Only one DNA base is being mutated at a time. */
    not_hit = ( dna_bases - 1.0 ) / dna_bases;

    d_hits = MUTATION_RATE * (*files).seq [ index ].amino_acids * years;

    if ( (*files).seq [ index ].descriptor == X_CHROMOSOME )
      d_hits = d_hits * X_MUTATION_RATE;

    /* q is the probability that a DNA base is unhit after N hits. */
    q = 0.5; /* pow ( not_hit, d_hits ); */

    /* Number of unhit amino acids remaining. */
    unhit = amino_acids * ( ( q * q * q + q * q ) / 2.0 );

    /* Number of mutated amino acids. */
    hit_aa = amino_acids * 1.0 - unhit;

    /* Total fraction of amino acids hit. */
    fraction_hit = hit_aa / ( amino_acids * 1.0 );

    /* Correct for mutations at critical residues. */
    (*hit_spacers) = (amino_acids * (*files).f_change) * (1.0 -
        ( ( q * q * q + q * q ) / 2.0 ) );

    printf ( "years: %3d, ", years );

/*    printf ( "hits: %3.0f, hit_AA: %4.0f, ", d_hits, hit_aa ); */

    printf ( "T-E: %4.0f, ", *hit_spacers );
  }  /* if */
}  /* hits */


/******************************************************************************/
/* This function estimates the number of hits for a specified time. */
est_hits ( files, comparison )
t_files       *files;	      /* table of sequence files */
t_comparison  *comparison;    /* unknown sequences comparison statistics */
{
  int     index;          /* current file index */
  double  hit_spacers;    /* number of mutated spacer residues */
  double  novel;          /* novel hits predicted */
  double  observed;       /* observed differences */
  float   rho;            /* rho constant */
  double  total = 0.0;    /* total number of hits observed */


  printf ( "\nTotal Divergence calculations:\n" );
  /* Compare all of the sequences against the first sequence. */
  for ( index = 1; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group >= 0 )
    {
      hits ( files, index, comparison, (*files).seq [ index ].diverge, 
          &hit_spacers );

      observed = (*comparison).sequences [ MUTATION_SEQ ].N [ index ].compared -
          (*comparison).sequences [ MUTATION_SEQ ].N [ index ].identity;

      printf ( "O: %4.0f, ", observed );

      printf ( "O-E: %4.0f\n", observed - hit_spacers );
    }  /* if */

  printf ( "\nFormula Total Divergence calculations:\n" );
  /* Compare all of the sequences against the first sequence. */
  for ( index = 1; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group >= 0 )
    {
      if ( ( ( (*files).seq [ index ].class == 'M' ) ||
             ( (*files).seq [ index ].class == 'P' ) ) &&
           ( (*files).seq [ index ].group == MUTATION_GROUP ) )
      {
        if ( (*files).seq [ index ].descriptor == X_CHROMOSOME )
          rho = RHO_MAMMALS * X_MUTATION_RATE;
        else
          rho = RHO_MAMMALS;
      }
      else rho = RHO_VERTEBRATES;

      hit_spacers = (*files).seq [ index ].diverge * rho *
          (*files).seq [ index ].amino_acids * (*files).f_change;

      if ( hit_spacers > (*files).seq [ MUTATION_SEQ ].amino_acids * 
          (*files).f_change )
        hit_spacers = (*files).seq [ MUTATION_SEQ ].amino_acids *
            (*files).f_change;

      printf ( "Years: %3d, ", (*files).seq [ index ].diverge );
      printf ( "T-E: %4.0f, ", hit_spacers );

      observed = (*comparison).sequences [ MUTATION_SEQ ].N [ index ].compared -
          (*comparison).sequences [ MUTATION_SEQ ].N [ index ].identity;

      printf ( "O: %4.0f, ", observed );

      printf ( "O-E: %4.0f\n", observed - hit_spacers );
    }  /* if */

  printf ( "\nIncremental Divergence calculations:\n" );
  /* Compare all of the sequences against the first sequence. */
  for ( index = 1; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group >= 0 )
    {
      hits ( files, index, comparison, (*files).seq [ index ].inc_diverge, 
          &hit_spacers );

      if ( (*files).f_change > 0.0 )
        novel = hit_spacers - ( total / ( (*files).f_change * 
            (*files).seq [ MUTATION_SEQ ].amino_acids ) ) * hit_spacers;
      else  novel = 0.0;

      if ( novel < 0.0 )  novel = 0.0;

      printf ( "E: %4.0f, (%4.0f), ", novel, total );

      printf ( "O: %3d, ", (*files).seq [ index ].novel );

      printf ( "O-E: %4.0f\n", (*files).seq [ index ].novel - novel * 1.0 );

      total += (*files).seq [ index ].novel;
    }  /* if */

  printf ( "\nFormula Incremental Divergence calculations:\n" );
  total = 0.0;
  /* Compare all of the sequences against the first sequence. */
  for ( index = 1; index < (*files).total; index++ )
    if ( (*files).seq [ index ].group >= 0 )
    {
      if ( ( ( (*files).seq [ index ].class == 'M' ) ||
             ( (*files).seq [ index ].class == 'P' ) ) &&
           ( (*files).seq [ index ].group == MUTATION_GROUP ) )
      {
        if ( (*files).seq [ index ].descriptor == X_CHROMOSOME )
          rho = RHO_MAMMALS * X_MUTATION_RATE;
        else
          rho = RHO_MAMMALS;
      }
      else  rho = RHO_VERTEBRATES;

      hit_spacers = (*files).seq [ index ].inc_diverge * rho *
          (*files).seq [ index ].amino_acids * (*files).f_change;

      if ( hit_spacers > (*files).seq [ MUTATION_SEQ ].amino_acids * 
          (*files).f_change )
        hit_spacers = (*files).seq [ MUTATION_SEQ ].amino_acids *
            (*files).f_change;

      printf ( "Years: %3d, ", (*files).seq [ index ].inc_diverge );
      printf ( "T-E: %4.0f, ", hit_spacers );

      if ( (*files).f_change > 0.0 )
        novel = hit_spacers - ( total / ( (*files).f_change * 
            (*files).seq [ MUTATION_SEQ ].amino_acids ) ) * hit_spacers;
      else  novel = 0.0;

      if ( novel < 0.0 )  novel = 0.0;

      printf ( "E: %4.0f, (%4.0f), ", novel, total );

      printf ( "O: %3d, ", (*files).seq [ index ].novel );

      printf ( "O-E: %4.0f\n", (*files).seq [ index ].novel - novel * 1.0 );

      total += (*files).seq [ index ].novel;
    }  /* if */
}  /* est_hits */


/******************************************************************************/
/* This function processes a known deletion. */
process_deletion ( mutation, sequence )
t_mutation      *mutation;    /* current mutations table record */
t_DNA_sequence	*sequence;    /* entire sequence */
{
  int	start;	       /* first nucleotide */
  int	length = 1;    /* length of deletion */

  if ( (*mutation).codon_number > 0 )
  {
    start = (*mutation).codon_number * 3 - 1;    
    if ( (*mutation).codon_base > 0 )
      start += (*mutation).codon_base - 1;
    if ( (*mutation).codon_number_end > 0 )
      length = (*mutation).codon_number_end * 3 - start;
  }
  else
  {
    start = (*mutation).nucleotide_begin - 1;
    length = (*mutation).nucleotide_end - (*mutation).nucleotide_begin + 1;
  }  /* else */

  if ( start < 0 )  start = 0;
  if ( start > (*sequence).length )  start = 0;

  if ( length < 0 )  length = 1; 

  printf ( "key %5d, codon %5d", (*mutation).key, (*mutation).codon_number );
  printf ( ".%1d-", (*mutation).codon_base );
  printf ( "%5d, ", (*mutation).codon_number_end );
  printf ( "(%5d-%5d) ", (*mutation).nucleotide_begin, 
      (*mutation).nucleotide_end );
  printf ( "Ref %d, ", (*mutation).reference );
  printf ( "'%s', ", (*mutation).codon_change );
  printf ( "%s\n", (*mutation).nucleotide_change );

  print_deletion ( sequence, start, length );

}  /* process_deletion */


/******************************************************************************/
/* This function opens and reads in the DNA sequence file. */
open_sequence ( files, data_file, sequence )
t_files         *files;        /* table of sequences */
FILE            *data_file;    /* Genbank sequence file. */
t_DNA_sequence	*sequence;     /* entire sequence data */
{
  int  DNA_index = 0;    /* index of DNA sequence file */
  int  index;            /* search index */
  int  status;           /* function return code */


  /* Find the DNA sequence file. */
  for ( index = 0; index < (*files).total; index++ )
  
    if ( (*files).seq [ index ].group == MUTATION_DNA )
      DNA_index = index;

  if ( DNA_index == 0 )  
  {
    printf ( "*INFO* No DNA sequence file.\n" );
    return ( S_NO_DNA_FILE );
  }  /* if */

  /* Open the DNA sequence file. */
  status = open_file ( (*files).seq [ DNA_index ].file_name, &data_file );

  if ( status != S_NORMAL )  return ( status );

  /* Find the beginning of the sequence data. */
  find_data ( data_file );

  /* Read in the DNA sequence. */
  read_sequence ( data_file, sequence );

  /* Close the sequence data file. */
  fclose ( data_file );

  return ( S_NORMAL );
}  /* open_sequence */


/******************************************************************************/
/* This function reads in a Genbank sequence data file. */
read_sequence ( data_file, sequence )
FILE		*data_file;	/* Genbank sequence file */
t_DNA_sequence	*sequence;	/* entire sequence data */
{
  int		index;			/* current pattern */
  char		line [ MAX_LINE ];	/* current sequence line */
  int		line_index;		/* current line index */
  int		mask;			/* sequence bit representation */
  char		seq_char;		/* current sequence character */
  int		status = S_NORMAL;	/* function return status */


  /* Read in the sequence. */
  (*sequence).length = 0;
  line [ 0 ] = '\0';
  line_index = 0;
  while ( status == S_NORMAL )
  {
    /* Get the next sequence character. */
    status = next_data_character ( data_file, line, &line_index, &seq_char );

    /* Bit map the sequence character. */
    if ( status == S_NORMAL )
    {
      set_mask ( base_masks, seq_char, &mask );
      (*sequence).base [ (*sequence).length++ ].bits = mask;

/* if (( seq_char != 'A' ) && ( seq_char != 'C' ) && ( seq_char != 'G' ) &&
    ( seq_char != 'T' ) && ( seq_char != 'X' ) && ( seq_char != 'N' ) &&
    ( seq_char != 'R' ) && ( seq_char != 'Y' ))
  printf ( "*INFO* Non-DNA base '%c' in file '%s'.\n", seq_char, file_name ); */

      if ( (*sequence).length == MAX_BASES - 1 )
      {
        printf ( "*WARNING* Sequence '%s' is too long.\n", 
            (*sequence).filename );
        status = S_TABLE_OVERFLOW;
      }  /* if */
    }  /* if */
  }  /* while */
}  /* read_sequence */


/******************************************************************************/
/* This function prints out a deletion selection. */
print_deletion ( sequence, index, length )
t_DNA_sequence	*sequence;	/* entire sequence */
long		index;		/* deletion start */
int             length;         /* length of the deletion */
{
  int	printed;	/* number of bases printed on current line */
  long	sub_index;	/* pattern index */

  sub_index = index - BEFORE_DELETION;	/* include surrounding bases */
  printed = 0;
  do 
  {
    /* Separate matched sequence from surrounding bases. */
    if ( sub_index == index )  printf ( "  (" ); 
    if ( sub_index == index + length )  printf ( ")  " );

    /* Print the base or a space. */
    if (( sub_index < 0 ) || ( sub_index >= (*sequence).length ))
      printf ( " " );
    else
      printf ( "%c", dna_map [ (*sequence).base [ sub_index ].bits ] );
    printed++;

    /* Print the pattern information on end of lines. */
    if (( printed == MAX_PER_LINE ) ||
        ( sub_index == index + length - 1 + AFTER_DELETION ))

      printf ( "  %6d\n", index + 1 );

    sub_index++;
  }
  while ( sub_index <= index + length - 1 + AFTER_DELETION );
}  /* print_deletion */


/******************************************************************************/
/* This function copies the surface dots to the sequence residues. */
copy_dots ( atoms, sequence, offset )
t_atoms          *atoms;       /* atoms table */
t_main_sequence  *sequence;    /* main sequence of interest */
int              offset;       /* offset into sequence */
{
  int     i, j;           /* index */


  /* Traverse the atoms table. */
  for ( i = 1; i <= (*atoms).total; i++ )

    /* Check if surface atom. */
    if ( (*atoms).atom [ i ].dots > 0 )
    {
      j = (*atoms).atom [ i ].residue_index + offset;
      (*sequence).dots [ j ] += (*atoms).atom [ i ].dots;
      (*sequence).surface [ j ]++;    /* another surface atom */
    }  /* if */
}  /* copy_dots */


/******************************************************************************/
/* Link all surface specific clusters together. */
surface_clusters ( atoms, residues, sequence, offset )
t_atoms          *atoms;       /* atoms table */
t_residues       *residues;    /* PDB residue and name array */
t_main_sequence  *sequence;    /* main sequence of interest */
int              offset;       /* offset into sequence */
{
  int     i, j;           /* index */
  int     r_i, r_j;       /* residue index */
  double  d;              /* distance */
  double  distance ();    /* distance function */
  double  vdr;            /* van der Waals radii */


  /* Initialize the surface cluster lists. */
  for ( i = 0; i < (*sequence).total; i++ )
  {
    (*sequence).next     [ i ] = -1;
    (*sequence).previous [ i ] = -1;
  }  /* for */

  for ( i = 1; i <= (*atoms).total; i++ )
  {
    r_i = (*atoms).atom [ i ].residue_index;

    if ( ( (*residues).aa [ r_i ].amino_acid != ' ' ) &&
         ( (*residues).aa [ r_i ].surface_area > 
           (*residues).min_area ) &&
         ( (*atoms).atom [ i ].dots > 0 ) &&
         ( ( (*sequence).conservation [ r_i + offset ] == 'S' ) ||
           ( (*sequence).conservation [ r_i + offset ] == 's' ) ) )

      for ( j = 1; j <= (*atoms).total; j++ )
      {
        r_j = (*atoms).atom [ j ].residue_index;

        if ( ( (*residues).aa [ r_j ].amino_acid != ' ' ) &&
             ( (*residues).aa [ r_j ].surface_area > 
               (*residues).min_area ) &&
             ( (*atoms).atom [ j ].dots > 0 ) &&
             ( ( (*sequence).conservation [ r_j + offset ] == 'S' ) ||
               ( (*sequence).conservation [ r_j + offset ] == 's' ) ) )

          if ( ( i != j ) && ( r_i != r_j ) )
          {
            /* Calculate the distance between the atomic centers. */
            d = distance ( atoms, i, j );

            /* Calculate the sum of the van der Waals radii of the two atoms. */
            vdr = van_der_Waals [ (*atoms).atom [ i ].atom_name [ 0 ] - 'A' ] +
                  van_der_Waals [ (*atoms).atom [ j ].atom_name [ 0 ] - 'A' ];

            /* Is the distance within an angstrom of the van der Waals radii? */
            if ( d <= vdr + (*residues).resolution )  /* adjacent */

              /* Link the specifics together. */
              link_specifics ( sequence, r_i + offset, r_j + offset );
          }  /* if */
      }  /* for */
  }  /* for */
}  /* surface_clusters */


/******************************************************************************/
/* List the distances between specifics. */
list_distances ( atoms, residues, sequence, offset )
t_atoms          *atoms;       /* atoms table */
t_residues       *residues;    /* PDB residue and name array */
t_main_sequence  *sequence;    /* main sequence of interest */
int              offset;       /* offset into sequence */
{
  int     i, j;           /* index */
  int     r_i, r_j;       /* residue index */
  double  d;              /* distance */
  double  distance ();    /* distance function */
  double  vdr;            /* van der Waals radii */


  printf ( "\n\nList of Distances between nearby Specifics\n\n" );

  for ( i = 1; i <= (*atoms).total; i++ )
  {
    r_i = (*atoms).atom [ i ].residue_index;

    if ( ( (*residues).aa [ r_i ].amino_acid != ' ' ) &&
         ( ( (*sequence).conservation [ r_i + offset ] == 'S' ) ||
           ( (*sequence).conservation [ r_i + offset ] == 's' ) ) )

      for ( j = 1; j <= (*atoms).total; j++ )
      {
        r_j = (*atoms).atom [ j ].residue_index;

        if ( ( (*residues).aa [ r_j ].amino_acid != ' ' ) &&
             ( ( (*sequence).conservation [ r_j + offset ] == 'S' ) ||
               ( (*sequence).conservation [ r_j + offset ] == 's' ) ) )

          if ( ( i != j ) && ( r_i != r_j ) )
          {
            /* Calculate the distance between the atomic centers. */
            d = distance ( atoms, i, j );

            /* Calculate the sum of the van der Waals radii of the two atoms. */
            vdr = van_der_Waals [ (*atoms).atom [ i ].atom_name [ 0 ] - 'A' ] +
                  van_der_Waals [ (*atoms).atom [ j ].atom_name [ 0 ] - 'A' ];

            /* Is the distance within an angstrom of the van der Waals radii? */
            if ( d <= vdr + (*residues).resolution )  /* adjacent */
            {
              printf ( "%s %c a %3d atom %4d: %3d @ ",
                  (*residues).aa [ r_i ].coordinate, 
                  (*sequence).amino_acid [ r_i + offset ],
                  (*residues).aa [ r_i ].surface_area, 
                  (*atoms).atom [ i ].atom_number,
                  (*atoms).atom [ i ].dots );
              printf ( "%s %c a %3d atom %4d: %3d",
                  (*residues).aa [ r_j ].coordinate, 
                  (*sequence).amino_acid [ r_j + offset ],
                  (*residues).aa [ r_j ].surface_area, 
                  (*atoms).atom [ j ].atom_number, 
                  (*atoms).atom [ j ].dots );
              printf ( "; d= %4.1f, v %4.1f\n", d, vdr );
            }  /* if */
          }  /* if */
      }  /* for */
  }  /* for */
  printf ( "\n\n" );
}  /* list_distances */


/******************************************************************************/
/* This function adds the length to the size histogram. */
add_size ( size, length, no_atoms, surface_area )
t_size  *size;           /* largest cluster sizes */
int     length;          /* cluster length to add */
int     no_atoms;        /* number of surface atoms */
int     surface_area;    /* surface area of the cluster */
{
  int  i = MAX_SIZES - 1;


  /* Check if the length is long enough to add to the list. */
  if ( length < (*size).cluster [ i ].size ) return;
  if ( ( length == (*size).cluster [ i ].size ) &&
       ( surface_area <= (*size).cluster [ i ].area ) ) return;

  /* Traverse the list shifting sizes down by one position. */
  while ( ( i > 0 ) && ( length > (*size).cluster [ i ].size ) )
  {
    /* shift down by 1 */
    (*size).cluster [ i ].size  = (*size).cluster [ i - 1 ].size;    
    (*size).cluster [ i ].atoms = (*size).cluster [ i - 1 ].atoms;    
    (*size).cluster [ i ].area  = (*size).cluster [ i - 1 ].area;
    i--;
  }  /* while */

  /* Traverse the list shifting sizes down by one position. */
  while ( ( i > 0 ) && ( length == (*size).cluster [ i ].size ) &&
      ( surface_area > (*size).cluster [ i ].area ) )
  {
    /* shift down by 1 */
    (*size).cluster [ i ].size  = (*size).cluster [ i - 1 ].size;    
    (*size).cluster [ i ].atoms = (*size).cluster [ i - 1 ].atoms;    
    (*size).cluster [ i ].area  = (*size).cluster [ i - 1 ].area;
    i--;
  }  /* while */

  if ( ( length > (*size).cluster [ i ].size ) || ( i == MAX_SIZES - 1 ) )
  {
    (*size).cluster [ i ].size  = length;
    (*size).cluster [ i ].atoms = no_atoms;
    (*size).cluster [ i ].area  = surface_area;
  }  /* if */
  else
  {
    (*size).cluster [ i + 1 ].size  = length;
    (*size).cluster [ i + 1 ].atoms = no_atoms;
    (*size).cluster [ i + 1 ].area  = surface_area;
  }  /* else */
}  /* add_size */


/******************************************************************************/
/* This function adds the length to the size histogram. */
add_atoms ( size, length, no_atoms, surface_area )
t_size  *size;           /* largest cluster sizes */
int     length;          /* cluster length to add */
int     no_atoms;        /* number of surface atoms */
int     surface_area;    /* surface area of the cluster */
{
  int  i = MAX_SIZES - 1;


  /* Check if enough atoms to add to the list. */
  if ( no_atoms < (*size).cluster [ i ].atoms ) return;
  if ( ( no_atoms == (*size).cluster [ i ].atoms ) &&
       ( surface_area <= (*size).cluster [ i ].area ) ) return;

  /* Traverse the list shifting sizes down by one position. */
  while ( ( i > 0 ) && ( no_atoms > (*size).cluster [ i ].atoms ) )
  {
    /* shift down by 1 */
    (*size).cluster [ i ].size  = (*size).cluster [ i - 1 ].size;    
    (*size).cluster [ i ].atoms = (*size).cluster [ i - 1 ].atoms;    
    (*size).cluster [ i ].area  = (*size).cluster [ i - 1 ].area;
    i--;
  }  /* while */

  if ( ( no_atoms > (*size).cluster [ i ].atoms ) || ( i == MAX_SIZES - 1 ) )
  {
    (*size).cluster [ i ].size  = length;
    (*size).cluster [ i ].atoms = no_atoms;
    (*size).cluster [ i ].area  = surface_area;
  }  /* if */
  else
  {
    (*size).cluster [ i + 1 ].size  = length;
    (*size).cluster [ i + 1 ].atoms = no_atoms;
    (*size).cluster [ i + 1 ].area  = surface_area;
  }  /* else */
}  /* add_atoms */


/******************************************************************************/
/* This function adds the length to the size histogram. */
add_area ( size, length, no_atoms, surface_area )
t_size  *size;           /* largest cluster sizes */
int     length;          /* cluster length to add */
int     no_atoms;        /* number of surface atoms */
int     surface_area;    /* surface area of the cluster */
{
  int  i = MAX_SIZES - 1;


  /* Check if the length is long enough to add to the list. */
  if ( surface_area < (*size).cluster [ i ].area ) return;
  if ( ( surface_area == (*size).cluster [ i ].area ) &&
       ( length <= (*size).cluster [ i ].size ) ) return;

  /* Traverse the list shifting sizes down by one position. */
  while ( ( i > 0 ) && ( surface_area > (*size).cluster [ i ].area ) )
  {
    /* shift down by 1 */
    (*size).cluster [ i ].size  = (*size).cluster [ i - 1 ].size;    
    (*size).cluster [ i ].atoms = (*size).cluster [ i - 1 ].atoms;    
    (*size).cluster [ i ].area  = (*size).cluster [ i - 1 ].area;
    i--;
  }  /* while */

  if ( ( surface_area > (*size).cluster [ i ].area ) || ( i == MAX_SIZES - 1 ) )
  {
    (*size).cluster [ i ].size  = length;
    (*size).cluster [ i ].atoms = no_atoms;
    (*size).cluster [ i ].area  = surface_area;
  }  /* if */
  else
  {
    (*size).cluster [ i + 1 ].size  = length;
    (*size).cluster [ i + 1 ].atoms = no_atoms;
    (*size).cluster [ i + 1 ].area  = surface_area;
  }  /* else */
}  /* add_area */


/******************************************************************************/
/* This function initializes the sizes table. */
initialize_sizes ( sizes )
t_sizes  *sizes;    /* table of cluster sizes */
{
  int  i, j;    /* index */

  for ( j = 0; j <= MAX_TRIALS; j++ )
    for ( i = 0; i < MAX_SIZES; i++ )
    {
      (*sizes).sizez [ j ].cluster [ i ].trial  = j;
      (*sizes).sizez [ j ].cluster [ i ].size   = 0;
      (*sizes).sizez [ j ].cluster [ i ].atoms  = 0;
      (*sizes).sizez [ j ].cluster [ i ].area   = 0;

      (*sizes).atomz [ j ].cluster [ i ].trial  = j;
      (*sizes).atomz [ j ].cluster [ i ].size   = 0;
      (*sizes).atomz [ j ].cluster [ i ].atoms  = 0;
      (*sizes).atomz [ j ].cluster [ i ].area   = 0;

      (*sizes).areas [ j ].cluster [ i ].trial  = j;
      (*sizes).areas [ j ].cluster [ i ].size   = 0;
      (*sizes).areas [ j ].cluster [ i ].atoms  = 0;
      (*sizes).areas [ j ].cluster [ i ].area   = 0;
    }  /* for */
}  /* initialize_sizes */


/******************************************************************************/
/* Print out all of the specific clusters. */
list_clusters ( sequence, sizes, trial, residues, offset )
t_main_sequence  *sequence;    /* main sequence of interest */
t_sizes          *sizes;       /* largest cluster sizes */
int              trial;        /* trial index */
t_residues       *residues;    /* PDB residue and name array */
int              offset;       /* offset into sequence */
{
  int  i;               /* index */
  int  next;            /* next specific on list */
  int  no_atoms;        /* number of surface atoms in the cluster */
  int  printed;         /* specifics printed */
  int  surface_area;    /* surface area of a cluster */


  printf ( "\nSpecific Clusters:\n" );

  /* Initialize marks. */
  for ( i = 0; i < (*sequence).total; i++ )
    (*sequence).mark [ i ] = OFF;

  /* Traverse the sequence. */
  i = offset;
  while ( i < (*sequence).total )
  {
    /* Check for a specific. */
    if ( ( ( (*sequence).conservation [ i ] == 'S' ) ||
           ( (*sequence).conservation [ i ] == 's' ) ) &&
           ( (*sequence).mark [ i ] != ON ) )
    {
      printed = 1;
      printf ( "[ %3d", i + 1 );
      if ( i - offset < (*residues).total )
        printf ( " (%s)", (*residues).aa [ i - offset ].coordinate );
      else
        printf ( " (---)" );
      (*sequence).mark [ i ] = ON;
      no_atoms = (*sequence).surface [ i ];
      surface_area = (*residues).aa [ i - offset ].surface_area;

      next = (*sequence).next [ i ];
      while ( ( next != i ) && ( next != -1 ) )
      {
        printf ( ", %3d", next + 1 );
        if ( next - offset < (*residues).total )
          printf ( " (%s)", (*residues).aa [ next - offset ].coordinate );
        else
          printf ( " (---)" );
        (*sequence).mark [ next ] = ON;
        no_atoms += (*sequence).surface [ next ];
        surface_area += (*residues).aa [ next - offset ].surface_area;
        next = (*sequence).next [ next ];
        printed++;
        if ( ( printed % 5 ) == 0 )  printf ( "\n" );
      }  /* while */
      printf ( " ] = %3d res., %d atoms, %d area.\n", printed, no_atoms,
          surface_area );

      /* Add the cluster size to the sizes histograms. */
      add_size  ( &((*sizes).sizez [ trial ]), printed, no_atoms, 
          surface_area );   
      add_atoms ( &((*sizes).atomz [ trial ]), printed, no_atoms, 
          surface_area );   
      add_area  ( &((*sizes).areas [ trial ]), printed, no_atoms, 
          surface_area );   
    }  /* if */
    i++;
  }  /* while */
}  /* list_clusters */


/******************************************************************************/
/* Write out the largest specific clusters to the .csf file. */
print_cluster ( csf, sequence, size, residues, offset, cluster )
FILE             *csf;         /* color selection file */
t_main_sequence  *sequence;    /* main sequence of interest */
t_size           *size;        /* largest cluster sizes */
t_residues       *residues;    /* PDB residue and name array */
int              offset;       /* offset into sequence */
int              cluster;      /* cluster size to print */
{
  int  i;               /* index */
  int  next;            /* next specific on list */
  int  no_atoms;        /* number of surface atoms in the cluster */
  int  no_residues;     /* number of specific residues in a cluster */
  int  surface_area;    /* surface area of a cluster */


  /* Initialize marks. */
  for ( i = 0; i < (*sequence).total; i++ )
    (*sequence).mark [ i ] = OFF;

  /* Traverse the sequence. */
  i = offset;
  while ( i < (*sequence).total )
  {
    /* Check for a specific. */
    if ( ( ( (*sequence).conservation [ i ] == 'S' ) ||
           ( (*sequence).conservation [ i ] == 's' ) ) &&
           ( (*sequence).mark [ i ] != ON ) )
    {
      no_residues = 1;
      (*sequence).mark [ i ] = ON;
      no_atoms = (*sequence).surface [ i ];
      surface_area = (*residues).aa [ i - offset ].surface_area;

      next = (*sequence).next [ i ];
      while ( ( next != i ) && ( next != -1 ) )
      {
        (*sequence).mark [ next ] = ON;
        no_residues++;
        no_atoms += (*sequence).surface [ next ];
        if ( next - offset < (*residues).total )
          surface_area += (*residues).aa [ next - offset ].surface_area;
        next = (*sequence).next [ next ];
      }  /* while */

      /* Check if target cluster. */
      if ( ( (*size).cluster [ cluster ].size  == no_residues ) &&
           ( (*size).cluster [ cluster ].atoms == no_atoms ) &&
           ( (*size).cluster [ cluster ].area  == surface_area ) )
      {
        /* Retraverse the cluster printing out the coordinates. */
        fprintf ( csf, "\nzone %s", (*residues).aa [ i - offset ].coordinate );
        next = (*sequence).next [ i ];
        while ( ( next != i ) && ( next != -1 ) )
        {
          if ( next - offset < (*residues).total )
            fprintf ( csf, " %s", 
                (*residues).aa [ next - offset ].coordinate );
          next = (*sequence).next [ next ];
        }  /* while */
        fprintf ( csf, " = col %d\n", size_color [ cluster ] );
        return;
      }  /* if */
    }  /* if */
    i++;
  }  /* while */
}  /* print_cluster */


/******************************************************************************/
/* Is j on i's linked list? */
int  on_list ( sequence, i, j )
t_main_sequence  *sequence;    /* main sequence of interest */
int              i;            /* specific i */
int              j;            /* specific j */
{
  int  next;    /* next specific on list */


  next = (*sequence).next [ i ];
  while ( ( next != -1 ) && ( next != i ) && ( next != j ) )
    next = (*sequence).next [ next ];

  if ( next == j )  return ( ON );
  return ( OFF );
}  /* on_list */


/******************************************************************************/
/* Link all specific clusters together. */
link_specifics ( sequence, i, j )
t_main_sequence  *sequence;    /* main sequence of interest */
int              i;            /* specific i */
int              j;            /* specific j */
{
  int  i_next;        /* i.next */
  int  j_previous;    /* j.previous */


  i_next     = (*sequence).next [ i ];
  j_previous = (*sequence).previous [ j ];

  /* Check if j is an unlinked specific. */
  if ( (*sequence).next [ j ] == -1 )
  {
    (*sequence).next     [ j ] = i_next;
    (*sequence).previous [ j ] = i;
    (*sequence).next     [ i ] = j;

    /* Check if i is an unlinked specific. */
    if ( i_next == -1 )
    {
      (*sequence).next     [ j ] = i;    /* doublely linked list */
      (*sequence).previous [ i ] = j;
    }  /* if */
    else
      (*sequence).previous [ i_next ] = j;
    return;
  }  /* if */


  /* Check if i is an unlinked specific. */
  if ( i_next == -1 )
  {
    (*sequence).next     [ i ] = (*sequence).next [ j ];
    (*sequence).previous [ i ] = j;
    (*sequence).next     [ j ] = i;

    /* Check if j is an unlinked specific. */
    if ( (*sequence).next [ i ] == -1 )
    {
      (*sequence).next     [ i ] = j;    /* doublely linked list */
      (*sequence).previous [ j ] = i;
    }  /* if */
    else
      (*sequence).previous [ (*sequence).next [ i ] ] = i;
    return;
  }  /* if */

  if ( ( i_next == -1 ) || ( j_previous == -1 ) )
  {
    printf ( "*WARN* Doublely linked specific list error.\n" );
    return;
  }  /* if */

  if ( on_list ( sequence, i, j ) == ON )  return;

  (*sequence).next     [ i ] = j;
  (*sequence).previous [ j ] = i;

  (*sequence).previous [ i_next ] = j_previous;
  (*sequence).next [ j_previous ] = i_next;
}  /* link_specifics */


/******************************************************************************/
/* This function calculates the distance between two atoms. */
double  distance ( atoms, i, j )
t_atoms  *atoms;    /* atoms table */
int      i;         /* index of first atom */
int      j;         /* index of second atom */
{
  double  d = 0.0;    /* distance */
  /* double  sqrt ();    /* square root function */
  double  x, y, z;    


  x = (*atoms).atom [ i ].x - (*atoms).atom [ j ].x;
  y = (*atoms).atom [ i ].y - (*atoms).atom [ j ].y;
  z = (*atoms).atom [ i ].z - (*atoms).atom [ j ].z;
/*
  d = sqrt ( x * x + y * y + z * z );
*/

  return ( d );
}  /* distance */



/******************************************************************************/
/* This function randomizes the random number generator based on user seed integer. */
user_randomize ()
{
  int  count;        /* loop counter */
  int  seed = 3;     /* random number generator seed number */

/*  printf ( "\nWhat is the random number generator seed integer? " );
  scanf ( "%d", &seed ); */
  srand ( seed );
/*  printf ( "\n" ); */

  for ( count = 0; count < seed; count++ )
    rand ();    /* randomize */
}  /* user_randomize */


/******************************************************************************/
/* This function reads in a DSSP output file. */
#define  DSSP_AMINO_ACID     13        /* position of amino acid - 1 */
#define  DSSP_PEP_CHAIN      11        /* position of chain letter - 1 */
#define  DSSP_POUND_SIGN      2        /* position of # in header - 1 */ 
#define  DSSP_SURFACE_AREA   35        /* position of surface area - 1 */

read_DSSP ( files, residues )
t_files     *files;       /* table of sequences */
t_residues  *residues;    /* residue and name array */
{
  char amino_acid;                    /* the amino acid read in */
  char coordinate [ MAX_LINE ];       /* amino acid residue number */
  int  dssp_index = 0;                /* DSSP file index */
  char line [ MAX_LINE ];             /* current PDB file line */
  int  line_index;                    /* character index of line */
  int  offset = 0;                    /* offset for ! lines */
  char residue_name [ MAX_LINE ];     /* amino acid residue name */
  int  residue_number;                /* sequential residue number */
  int  status = S_NORMAL;             /* status of called functions */
  int  surface_area;                  /* surface area of a residue */
  char token [ MAX_LINE ];            /* token from current line */


  /* Check for a DSSP surface area file. */
  while ( ( dssp_index < (*files).total ) && 
          ( (*files).seq [ dssp_index ].group != DSSP_FILE ) )  dssp_index++;
  if ( (*files).seq [ dssp_index ].group != DSSP_FILE )  return;

  /* Check if PDB file has been read in. */
  if ( (*residues).total == 0 )  return;

  (*residues).min_area = MIN_SURFACE_AREA;    /* minimum surface area */
  if ( (*files).seq [ dssp_index ].diverge > 0 )
    (*residues).min_area = (*files).seq [ dssp_index ].diverge;

  /* Find the DSSP header line. */
  do
    status = get_line ( (*files).seq [ dssp_index ].protein, line );
  while ( ( status != S_EOF ) && ( line [ DSSP_POUND_SIGN ] != '#' ) );

  /* Check if end of file found. */
  if ( status == S_EOF )
  {
    printf ( "*Info* End of DSSP file found before header record.\n" );
    return;
  }  /* if */

  /* Read in the DSSP surface area for each residue. */
  while ( status != S_EOF )
  {
    /* Get the next line from the PDB file. */
    status = get_line ( (*files).seq [ dssp_index ].protein, line );
    line_index = 0;

    if ( status == S_EOF )  return;

    /* Get the residue number. */
    get_integer ( line, &line_index, &(residue_number) );
    residue_number += offset;

    /* Get the residue name. */
    residue_name [ 0 ] = '\0';
    strcopy ( token, line );    /* copy the line */
    token [ DSSP_PEP_CHAIN ] = '\0';    /* terminate */
    get_token ( token, &line_index, residue_name );

    /* Get the amino acid single letter code. */
    amino_acid = line [ DSSP_AMINO_ACID ];

    /* Get the surface area. */
    line_index = DSSP_SURFACE_AREA;
    get_integer ( line, &line_index, &(surface_area) );

    if ( amino_acid == '!' )  offset--;
    else
    {
      /* Compare the residue identifiers. */
      if ( (*residues).aa [ residue_number - 1 ].amino_acid != amino_acid )
        printf ( "*INFO* Amino acid mismatch: residue %d %s:%s %c#%c\n",
            residue_number, 
            (*residues).aa [ residue_number - 1 ].coordinate, residue_name,
            (*residues).aa [ residue_number - 1 ].amino_acid, amino_acid );

      /* Compare the residue coordinate names. */
      if ( strcmp ( residue_name, 
          (*residues).aa [ residue_number - 1 ].coordinate ) != 0 )
        if ( ( residue_name [ strlen ( residue_name ) ] >= '0' ) &&
             ( residue_name [ strlen ( residue_name ) ] <= '9' ) )
          printf ( "*INFO* Amino acid mismatch: residue %d %s#%s %c:%c\n",
              residue_number, 
              (*residues).aa [ residue_number - 1 ].coordinate, residue_name,
              (*residues).aa [ residue_number - 1 ].amino_acid, amino_acid );

      /* Set the surface area. */
      if ( ( residue_number >= 1 ) && ( residue_number <= (*residues).total ) )
        (*residues).aa [ residue_number - 1 ].surface_area = surface_area;
    }  /* else */
  }  /* while */
}  /* read_DSSP */
