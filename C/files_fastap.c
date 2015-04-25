
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

#define  TRUE		1
#define  FALSE		0

#define  DNA		0
#define  PROTEIN	1

#define  BLOCK_SIZE	60	/* output block size */

#define  CODON_SIZE     3	/* number of bases in DNA codon */

				/* "EGHPRSU" */
#define  DNA_LIBRARIES  9	/* dbEST, GenBank, GenPepet, PIR, Repeats, Swiss, GBCU, */
				/* cs16p13, c16exons */

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      200000  /* maximum DNA sequence length */

#define  MAX_CODON	64	/* number of codons */

#define  MAX_LINE	1012	/* maximum line length */

#define  MIN_SEQ	12	/* minimum sequence length */

#define  MAX_TLC	29	/* maximum number of files to Time Logic Computer */



/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  name [ MAX_LINE ];	/* file name */
  char  base [ MAX_BASES ];	/* DNA bases */
} t_seq;


/* Text file. */
typedef struct {
  char  name [ MAX_LINE ];	/* file name */
  char  next;			/* current character */
  char  token [ MAX_LINE ];	/* current token */
  char  line  [ MAX_LINE ];	/* current line */
  int   line_index;		/* line index */
  FILE  *data;			/* data file */
  short eof;			/* end of file flag */
} t_text;


static  int  base_map [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/* 51  52  53  54  55  56  57  58  59  60  61  62  63  64 */
   64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,

/* A   B  C   D   E   F  G   H   I   J   K   L   M */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* N   O   P   Q   R   S  T  U   V   W   X   Y   Z */
  64, 64, 64, 64, 64, 64, 0, 0, 64, 64, 64, 64, 64,

  64, 64, 64, 64, 64, 64,

/* a   b  c   d   e   f  g   h   i   j   k   l   m */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* n   o   p   q   r   s  t  u   v   w   x   y   z */
  64, 64, 64, 64, 64, 64, 0, 0, 64, 64, 64, 64, 64
};


static  int  base_map_3 [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/* 51  52  53  54  55  56  57  58  59  60  61  62  63  64 */
   64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,

/* A   B  C   D   E   F  G   H   I   J   K   L   M */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* N   O   P   Q   R   S  T  U   V   W   X   Y   Z */
  64, 64, 64, 64,  2, 64, 0, 0, 64, 64, 64,  0, 64,

  64, 64, 64, 64, 64, 64,

/* a   b  c   d   e   f  g   h   i   j   k   l   m */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* n   o   p   q   r   s  t  u   v   w   x   y   z */
  64, 64, 64, 64,  2, 64, 0, 0, 64, 64, 64,  0, 64
};


/* Genetic code */
static  char  codon_map [ 64 ] = {
  'F', 'F', 'L', 'L',     'S', 'S', 'S', 'S',  
  'Y', 'Y', '*', '*',     'C', 'C', '*', 'W',

  'L', 'L', 'L', 'L',     'P', 'P', 'P', 'P',
  'H', 'H', 'Q', 'Q',     'R', 'R', 'R', 'R',

  'I', 'I', 'I', 'M',     'T', 'T', 'T', 'T',
  'N', 'N', 'K', 'K',     'S', 'S', 'R', 'R',

  'V', 'V', 'V', 'V',     'A', 'A', 'A', 'A',
  'D', 'D', 'E', 'E',     'G', 'G', 'G', 'G'  };


static  char  complement [ 123 ] = {

/*    1    2    3    4    5    6    7    8    9   10 */
' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 20 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 30 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 40 */
     ' ', ' ', ' ', ' ', '-', '.', ' ', ' ', ' ', ' ',  /* 50 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 60 */
     ' ', ' ', ' ', ' ',                                /* 64 */

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   'T', 'V', 'G', 'H', ' ', ' ', 'C', 'D', ' ', ' ', 'M', ' ', 'K',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'N', ' ', ' ', ' ', 'Y', 'S', 'A', 'A', 'B', 'W', 'N', 'R', ' ',

   ' ', ' ', ' ', ' ', ' ', ' ',

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   't', 'v', 'g', 'h', ' ', ' ', 'c', 'd', ' ', ' ', 'm', ' ', 'k',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'n', ' ', ' ', ' ', 'y', 's', 'a', 'a', 'b', 'w', 'n', 'r', ' '
};


static  int  dna_mask [ 123 ] = { 0,

/*    1    2    3    4    5    6    7    8    9   10 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 20 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 30 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 40 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 50 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 60 */
      0,   0,   0,   0,   

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1, 0xE,   4, 0xB,   0,   0,   2, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   8,   8,   7,   9, 0xF, 0xC,   0,

    0,   0,   0,   0,   0,   0,

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1, 0xE,   4, 0xB,   0,   0,   2, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   8,   8,   7,   9, 0xF, 0xC,   0 };


/* List of database names. */
static  char  *database [] = {
  "gbest", "genbank", "genpept", "pir", "repeats", "swiss", "gbcu", "cs16p13", "c16exons" };



main ()
{
  t_text  erase;		/* Unix erase shell commands */
  t_text  names;		/* file of sequence names file */


  printf ( "This is the Sequence files --> Fasta format program (Version 0.1).\n\n" );

  printf ( "This version writes to ../h_blast and ../h_fasta\n\n" );

  /* Open the commands file. */
  erase.data = fopen ( "erase", "w" );

  /* Prompt for the input file name. */
  prompt_file ( &names, "What is the list of sequences file name?" );

  /* Set up BLAST search files. */
/*  blast_main ( &erase, &names );
*/
  /* Set up FASTA search files. */
  fasta_main ( &erase, &names );

  /* Set up TLC search files. */
/*  tlc_main ( &erase, &names );
*/
  fprintf ( erase.data, "rm erase\n" );

  fclose ( erase.data );

  printf ( "\nEnd of DNA Sequences -> Fasta format program.\n" );
}  /* main */


/* This function sets up the BLAST search files. */
blast_main ( erase, names )
t_text  *erase;			/* Unix erase shell commands */
t_text  *names;			/* file of sequence names file */
{
  t_text  cmds;			/* Unix shell commands */
  long    end;			/* end of DNA sequence segment */
  t_text  in;			/* input DNA sequence file */
  int     index;		/* commands files index */
  t_seq   seq;         		/* DNA sequence */
  long    start;		/* start of DNA sequence segment */
  int     version;     		/* DNA segment number */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();		/* file open function */


  /* Open the commands file. */
  strcpy ( cmds.name, "blast.cmds" );
  cmds.data = fopen ( cmds.name, "w" );

  /* Process the sequences. */
  while ( (*names).eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, (*names).line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );
    fclose ( in.data );

    version = 1;
    start = 0;
    end = 999;
    (*names).line [ stridx ( (*names).line, "\n" ) ] = '\0';
    strcpy ( seq.name, (*names).line );

    /* Segment the DNA sequence in blocks of 1000 bp. */
    while ( start + MIN_SEQ < seq.total )
    {
      if ( (*names).eof != TRUE ) 
      {
        /* Check for end of sequence. */
        if ( end > seq.total )  end = seq.total - 1;

        setup_blast ( &seq, start, end, version,  
            &cmds, erase );

        version++;
        if ( end == seq.total - 1 )  start = seq.total;
        else  start += 900;
        end += 900;
      }  /* if */
    }  /* while */

    /* Get the next name. */
    get_line ( names );
  }  /* while */

  fprintf ( (*erase).data, "rm %s\n", cmds.name );

  fclose ( cmds.data );
}  /* blast_main */


/* This function sets up the FASTA search files. */
fasta_main ( erase, names )
t_text  *erase;			/* Unix erase shell commands */
t_text  *names;			/* file of sequence names file */
{
  long    end;			/* end of DNA sequence segment */
  t_text  fasta_cmds;		/* Unix shell Fasta commands */
  t_text  in;			/* input DNA sequence file */
  int     index;		/* commands files index */
  t_seq   seq;         		/* DNA sequence */
  long    start;		/* start of DNA sequence segment */
  int     version;     		/* DNA segment number */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();		/* file open function */


  /* Open the file of file names for reading. */
  open_text_file ( names );

  /* Open the commands file. */
  strcpy ( fasta_cmds.name, "fastap.cmds" );
  fasta_cmds.data = fopen ( fasta_cmds.name, "w" );

  /* Process the sequences. */
  while ( (*names).eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, (*names).line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );
    fclose ( in.data );

    version = 1;
    start = 0;
    end = 2999;
    (*names).line [ stridx ( (*names).line, "\n" ) ] = '\0';
    strcpy ( seq.name, (*names).line );

    /* Segment the DNA sequence in blocks of 3000 bp. */
    while ( start + MIN_SEQ < seq.total )
    {
      if ( (*names).eof != TRUE ) 
      {
        /* Check for end of sequence. */
        if ( end > seq.total )  end = seq.total - 1;

        setup_fasta_protein ( &seq, start, end, version,  
            &fasta_cmds, erase, seq.name );

        version++;
        if ( end == seq.total - 1 )  start = seq.total;
        else  start += 2900;
        end += 2900;
      }  /* if */
    }  /* while */

    /* Get the next name. */
    get_line ( names );
  }  /* while */

  fprintf ( (*erase).data, "rm %s\n", fasta_cmds.name );

  fclose ( fasta_cmds.data );
  fclose ( (*names).data );
}  /* fasta_main */


/* This function sets up the search files for TLC Decypher II search engine. */
tlc_main ( erase, names )
t_text  *erase;		/* Unix erase shell commands */
t_text  *names;		/* file of sequence names file */
{
  char    base_name [ MAX_LINE ];	/* TLC commands base name */
  int     count = 0;			/* count of sequences */
  char    dos_name [ MAX_LINE ];	/* DOS file name */
  long    end;				/* end of DNA sequence segment */
  t_text  fasta;			/* FASTA format sequence file */
  t_text  in;				/* input DNA sequence file */
  int     index;			/* commands files index */
  char    libraries [ MAX_LINE ];	/* Database libraries search codes */
  t_seq   seq;         			/* DNA sequence */
  long    start;			/* start of DNA sequence segment */
  t_text  tlc [ DNA_LIBRARIES ];	/* TLC search files */
  t_text  tlc_cmds;			/* TLC Unix commands file */
  t_text  tlc_ftp;			/* TLC Unix FTP input file */
  int     version;     			/* DNA segment number */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Open the file of file names for reading. */
  open_text_file ( names );

  /* Open the commands file. */
  strcpy ( tlc_cmds.name, "tlc.cmds" );
  tlc_cmds.data = fopen ( tlc_cmds.name, "w" );

  /* Open the commands file. */
  strcpy ( tlc_ftp.name, "tlc.ftp" );
  tlc_ftp.data = fopen ( tlc_ftp.name, "w" );

  /* Open the TLC QUERY output file. */
  strcpy ( base_name, (*names).name );
  base_name [ stridx ( base_name, "\n" ) ] = '\0';
  base_name [ stridx ( base_name, "." ) ] = '\0';
  base_name [ 6 ] = '\0';
  strcpy ( version_str, "E0" );
  strcpy ( libraries, "EGHPRSUCX" );
  for ( index = 0; index < DNA_LIBRARIES; index++ )
  {
    version_str [ 0 ] = libraries [ index ];
    strcpy ( tlc [ index ].name, base_name );
    strcat ( tlc [ index ].name, version_str );
    strcat ( tlc [ index ].name, ".TL" );
    tlc [ index ].data = fopen ( tlc [ index ].name, "w" );

    if ( ( index == 2 ) || ( index == 3 ) || ( index == 5 ) )
      write_tlc_header ( &(tlc [ index ]), PROTEIN, base_name );
    else
      write_tlc_header ( &(tlc [ index ]), DNA, base_name );
  }  /* for */

  if ( tlc_ftp.data != NULL )
  {
    fprintf ( tlc_ftp.data, "user ricke ricke\n" );
    fprintf ( tlc_ftp.data, "prompt\n" );
    fprintf ( tlc_ftp.data, "ascii\n" );
    fprintf ( tlc_ftp.data, "cd c:\\SEARCH\n" );
    fprintf ( tlc_ftp.data, "cd d:\\TLC\n" );
    fprintf ( tlc_ftp.data, "mkdir %s\n", base_name );
    fprintf ( tlc_ftp.data, "cd ../SASE\n" );
    fprintf ( tlc_ftp.data, "mkdir %s\n", base_name );
    fprintf ( tlc_ftp.data, "cd %s\n", base_name );
  }  /* if */

  /* Process the sequences. */
  while ( (*names).eof != TRUE )
  {
    /* Open the sequence output file. */
    strcpy ( in.name, (*names).line );
    in.name [ stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_DNA_seq ( &in, &seq );
    fclose ( in.data );

    /* Open the DNA output file. */
    strcpy ( fasta.name, in.name );
    strcat ( fasta.name, ".Fasta" );
    fasta.data = fopen ( fasta.name, "w" );

    write_fasta_name ( fasta, seq.name, 1, seq.total, seq.total );

    /* Write out the Fasta format DNA sequence. */
    write_DNA_seq ( seq, 0, seq.total - 1, fasta );

    fclose ( fasta.data );
    fprintf ( (*erase).data, "rm %s\n", fasta.name );

    map_to_dos ( in.name, dos_name );
    if ( tlc_ftp.data != NULL )
      fprintf ( tlc_ftp.data, "put %s %s\n", fasta.name, dos_name );
    for ( index = 0; index < DNA_LIBRARIES; index++ )
      if ( tlc [ index ].data != NULL )
        fprintf ( tlc [ index ].data, "%s\n", dos_name );
    count++;

    if ( ( count % MAX_TLC ) == 0 )
    {
      version_str [ 1 ]++;
      fprintf ( tlc_ftp.data, "cd c:\n" );
      for ( index = 0; index < DNA_LIBRARIES; index++ )
      {
        fprintf ( tlc_ftp.data, "put %s\n", tlc [ index ].name );
        fprintf ( (*erase).data, "rm %s\n", tlc [ index ].name );
        fprintf ( tlc_ftp.data, "rename %s %sC\n", tlc [ index ].name,
            tlc [ index ].name );
        if ( ( index == 2 ) || ( index == 3 ) || ( index == 5 ) )
          finish_tlc_file ( &(tlc [ index ]), PROTEIN, database [ index ], base_name );
        else
          finish_tlc_file ( &(tlc [ index ]), DNA, database [ index ], base_name );

        version_str [ 0 ] = libraries [ index ];
        strcpy ( tlc [ index ].name, base_name );
        strcat ( tlc [ index ].name, version_str );
        strcat ( tlc [ index ].name, ".TL" );
        tlc [ index ].data = fopen ( tlc [ index ].name, "w" );

        if ( ( index == 2 ) || ( index == 3 ) || ( index == 5 ) )
          write_tlc_header ( &(tlc [ index ]), PROTEIN, base_name );
        else
          write_tlc_header ( &(tlc [ index ]), DNA, base_name );
      }  /* for */

      fprintf ( tlc_ftp.data, "cd d:\n" );
    }  /* if */

    /* Get the next name. */
    get_line ( names );
  }  /* while */

  if ( ( count % MAX_TLC ) != 0 )
  {
    fprintf ( tlc_ftp.data, "cd c:\n" );
    for ( index = 0; index < DNA_LIBRARIES; index++ )
    {
      fprintf ( tlc_ftp.data, "put %s\n", tlc [ index ].name );
      fprintf ( (*erase).data, "rm %s\n", tlc [ index ].name );
      fprintf ( tlc_ftp.data, "rename %s %sC\n", tlc [ index ].name,
          tlc [ index ].name );
      if ( ( index == 2 ) || ( index == 3 ) || ( index == 5 ) )
        finish_tlc_file ( &(tlc [ index ]), PROTEIN, database [ index ], base_name );
      else
        finish_tlc_file ( &(tlc [ index ]), DNA, database [ index ], base_name );
    }  /* for */
  }  /* if */

  fprintf ( tlc_ftp.data, "quit\n" );

  fprintf ( (*erase).data, "rm %s\n", tlc_cmds.name );
  fprintf ( (*erase).data, "rm %s\n", tlc_ftp.name );

  fprintf ( tlc_cmds.data, "ftp -in hin < %s\n", tlc_ftp.name );

  fclose ( tlc_cmds.data );
  fclose ( tlc_ftp.data );
  fclose ( (*names).data );
}  /* tlc_main */


/* This function writes out a TLC file header. */
write_tlc_header ( tlc_file, search_type, subdirectory )
t_text	*tlc_file;		/* TLC output file */
int	search_type;		/* DNA or PROTEIN */
char	subdirectory [];	/* subdirectory name */
{
  if ( (*tlc_file).data == NULL )  return;

/*  fprintf ( (*tlc_file).data, "[SEARCH ID] %sC\n", (*tlc_file).name ); */
  fprintf ( (*tlc_file).data, "[COLUMNS] 135\n" );
  fprintf ( (*tlc_file).data, "[COMMENT] Test script.\n" );
  fprintf ( (*tlc_file).data, "[ALGORITHM] SW\n" );

  if ( search_type == PROTEIN )
  {
    fprintf ( (*tlc_file).data, "[MATRIX] c:\\MATRICES\\ricke2.maa\n" );
    fprintf ( (*tlc_file).data, "[SCALE FACTOR] 1\n" );
    fprintf ( (*tlc_file).data, "[OPEN PENALTY] 20\n" );
    fprintf ( (*tlc_file).data, "[EXTEND PENALTY] 4\n" );
  }
  else
  {
    fprintf ( (*tlc_file).data, "[MATRIX] c:\\MATRICES\\NUC4X4HB.MNT\n" );
    fprintf ( (*tlc_file).data, "[SCALE FACTOR] 1\n" );
    fprintf ( (*tlc_file).data, "[OPEN PENALTY] 20\n" );
    fprintf ( (*tlc_file).data, "[EXTEND PENALTY] 4\n" );
  }  /* else */

  fprintf ( (*tlc_file).data, "[QUERY FORMAT] FASTA/PEARSON\n" );
  fprintf ( (*tlc_file).data, "[QUERY TYPE] NT\n" );

  if ( search_type == PROTEIN )
    fprintf ( (*tlc_file).data, "[QUERY SEARCH] 1 2 3 -1 -2 -3\n" );
  else
    fprintf ( (*tlc_file).data, "[QUERY SEARCH] B\n" );

  fprintf ( (*tlc_file).data, "[QUERY PATH] d:\\SASE\\%s\n", subdirectory );
  fprintf ( (*tlc_file).data, "[QUERY SET] " );
}  /* write_tlc_header */


/* This function writes out the end of a TLC file. */
finish_tlc_file ( tlc_file, search_type, database_name, subdirectory )
t_text	*tlc_file;		/* TLC output file */
int	search_type;		/* DNA or PROTEIN */
char	database_name [];	/* subdirectory name */
char	subdirectory [];	/* subdirectory name */
{
  if ( search_type == PROTEIN )
  {
    fprintf ( (*tlc_file).data, "[TARGET TYPE] AA\n" );
    fprintf ( (*tlc_file).data, "[TARGET FRAMES] 1\n" );
  }
  else
  {
    fprintf ( (*tlc_file).data, "[TARGET TYPE] NT\n" );
    fprintf ( (*tlc_file).data, "[TARGET FRAMES] D\n" );
  }  /* else */

  fprintf ( (*tlc_file).data, "[TARGET PATH] C:\\BINARIES\n" );
  fprintf ( (*tlc_file).data, "[TARGET SET] %s\n", database_name );
  fprintf ( (*tlc_file).data, "[RESULT PATH] d:\\TLC\\%s\n", subdirectory );
  fprintf ( (*tlc_file).data, "[MAX SCORES] 20\n" );
  fprintf ( (*tlc_file).data, "[MAX ALIGNMENTS] 20\n" );
  fclose ( (*tlc_file).data );
}  /* finish_tlc_file */


/* This function maps a Sample Sequence file name to DOS file name. */
map_to_dos ( sase_name, dos_name )
char	sase_name [];		/* Sample Sequence file name */
char	dos_name  [];		/* DOS file name */
{
  char    cosmid    [ MAX_LINE ];	/* BAC or cosmid name */
  int     index = 0;			/* array index */
  char    subclone  [ MAX_LINE ];	/* subclone name */
  char    direction [ MAX_LINE ];	/* sequence direction */
  char    to_add    [ MAX_LINE ];	/* character to add string */
  char    version   [ MAX_LINE ];	/* sequence version */
  char    method    [ MAX_LINE ];	/* method */


  /* Parse the sequence name into subcomponent parts. */
  parse_name ( sase_name, cosmid, subclone, direction, version, method );

  strcpy ( dos_name, subclone );

  strcat ( dos_name, direction );

  strcpy ( to_add, "1" );
  while ( version [ index ] != '\0' )
  {
    to_add [ 0 ] = version [ index ];
    index++;
  }  /* while */
  strcat ( dos_name, to_add );

  strcpy ( to_add, method );
  to_add [ 1 ] = '\0';
  strcat ( dos_name, to_add );

  strcat ( dos_name, cosmid );

  dos_name [ 8 ] = '\0';
  strcat ( dos_name, ".Seq" );
}  /* map_to_dos */


/* This function sets up the Blast searches. */
setup_blast ( seq, start, end, version, cmds, erase )
t_seq   *seq;		/* DNA sequence */
long    start;		/* start of DNA sequence segment */
long    end;		/* end of DNA sequence segment */
int	version;	/* current version number */
t_text	*cmds;		/* Unix e-mail message send commands file */
t_text  *erase;		/* Unix remove e-mail message file commands file */
{
  t_text  segment;			/* Fasta formated DNA segment */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Set up the Fasta segment sequence file name. */
  strcpy ( segment.name, (*seq).name );
  segment.name [ stridx ( segment.name, "-" ) ] = '\0'; 
  strcat ( segment.name, "-" );
  itoa ( version, version_str );
  if ( version < 100 )  strcat ( segment.name, "0" );
  if ( version < 10  )  strcat ( segment.name, "0" );
  strcat ( segment.name, version_str );
  strlower ( segment.name );

  /* Open the Fasta segment output file. */
  segment.data = fopen ( segment.name, "w" );
  if ( segment.data == NULL )
  {
    printf ( "Could not open the file '%s' for writing.\n", segment.name );
    return;
  }  /* if */

  /* Set up Unix e-mail send command. */
  if ( (*cmds).data != NULL )
  {
    fprintf ( (*cmds).data, "blastn genbank %s V=20 B=20 > %sBG\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBG ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastn gbest %s V=20 B=20 > %sBE\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBE ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastn gbcu %s V=20 B=20 > %sBU\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBU ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastn repeats %s V=20 B=20 > %sBR\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBR ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastx swiss %s V=20 B=20 S=65 S2=65 > %sBS\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBS ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastx pir %s V=20 B=20 S=65 S2=65 > %sBP\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBP ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastx genpept %s V=20 B=20 S=65 S2=65 > %sBT\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBT ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastn cs16p13 %s V=20 B=20 > %sBC\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBC ../h_blast\n", segment.name );
    fprintf ( (*cmds).data, "blastn c16exons %s V=20 B=20 > %sBX\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sBX ../h_blast\n", segment.name );
  }  /* if */

  /* Write out the Fasta name line. */
  write_fasta_name ( segment, segment.name, 
      start + 1, (*seq).total, end - start + 1 );

  /* Write out the DNA sequence. */
  write_DNA_seq ( seq, start, end, segment );

  /* Close the e-mail message output file. */
  fclose ( segment.data );
}  /* setup_blast */


/* This function sets up the Fasta searches. */
setup_fasta ( seq, start, end, version, cmds, erase, library )
t_seq   *seq;		/* DNA sequence */
long    start;		/* start of DNA sequence segment */
long    end;		/* end of DNA sequence segment */
int	version;	/* current version number */
t_text	*cmds;		/* Unix e-mail message send commands file */
t_text  *erase;		/* Unix remove e-mail message file commands file */
char	library [];	/* name of DNA sequence library for self comparison */
{
  char    base_name [ MAX_LINE ];	/* Translated sequence filename */
  int     frame;			/* Translation frame */
  t_text  segment;			/* Fasta formated DNA segment */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Set up the Fasta segment sequence file name. */
  strcpy ( segment.name, (*seq).name );
  segment.name [ stridx ( segment.name, "-" ) ] = '\0'; 
  strcat ( segment.name, "-" );
  itoa ( version, version_str );
  if ( version < 100 )  strcat ( segment.name, "0" );
  if ( version < 10  )  strcat ( segment.name, "0" );
  strcat ( segment.name, version_str );
  strlower ( segment.name );
  strcpy ( base_name, segment.name );

  /* Set up Unix e-mail send command. */
  if ( (*cmds).data != NULL )
  {
    /* G = GenBank & U = GenBank cummulative update */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%GU ktup 2 > %sFG\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFG ../h_fasta\n", segment.name );
  }  /* if */

  /* Open the Fasta segment output file. */
  segment.data = fopen ( segment.name, "w" );
  if ( segment.data == NULL )
  {
    printf ( "Could not open the file '%s' for writing.\n", segment.name );
    return;
  }  /* if */

  /* Set up Unix e-mail send command. */
  if ( (*cmds).data != NULL )
  {
    /* G = GenBank & U = GenBank cummulative update */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%GU ktup 1 > %sFG\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFG ../h_fasta\n", segment.name );
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -i -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%GU ktup 1 > %sFGr\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFGr ../h_fasta\n", segment.name );

    /* E = EST database */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%E ktup 1 > %sFE\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFE ../h_fasta\n", segment.name );
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -i -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%E ktup 1 > %sFEr\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFEr ../h_fasta\n", segment.name );

    /* R = repeat database */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%R ktup 1 > %sFR\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFR ../h_fasta\n", segment.name );
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -i -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%R ktup 1 > %sFRr\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFRr ../h_fasta\n", segment.name );

    /* Self comparisons */
    fprintf ( (*cmds).data, "fasta -o -b 5 -d 5 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %s ktup 1 > %sFL\n", 
        segment.name, library, segment.name );
    fprintf ( (*cmds).data, "mv %sFL ../h_fasta\n", segment.name );
    fprintf ( (*cmds).data, "fasta -o -b 5 -d 5 -h -i -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %s ktup 1 > %sFLr\n", 
        segment.name, library, segment.name );
    fprintf ( (*cmds).data, "mv %sFLr ../h_fasta\n", segment.name );

    /* Chromosome 16p13 SASE sequences comparisons */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%C ktup 1 > %sFC\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFC ../h_fasta\n", segment.name );
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -i -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%C ktup 1 > %sFCr\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFCr ../h_fasta\n", segment.name );

    /* Chromosome 16 Trapped Exons comparisons */
    fprintf ( (*cmds).data, "fasta -o -b 5 -d 5 -h -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%X ktup 1 > %sFX\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFX ../h_fasta\n", segment.name );
    fprintf ( (*cmds).data, "fasta -o -b 5 -d 5 -h -i -m 0 -n -Q -z " );
    fprintf ( (*cmds).data, "%s %%X ktup 1 > %sFXr\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFXr ../h_fasta\n", segment.name );
  }  /* if */

  /* Set up Unix erase file command. */
  if ( (*erase).data != NULL )

    fprintf ( (*erase).data, "rm %s\n", segment.name );

  /* Write out the Fasta name line. */
  write_fasta_name ( segment, segment.name, 
      start + 1, (*seq).total, end - start + 1 );

  /* Write out the DNA sequence. */
  write_DNA_seq ( seq, start, end, segment );

  /* Close the sequence file. */
  fclose ( segment.data );
}  /* setup_fasta */


/* This function sets up the Fasta searches. */
setup_fasta_protein ( seq, start, end, version, cmds, erase, library )
t_seq   *seq;		/* DNA sequence */
long    start;		/* start of DNA sequence segment */
long    end;		/* end of DNA sequence segment */
int	version;	/* current version number */
t_text	*cmds;		/* Unix e-mail message send commands file */
t_text  *erase;		/* Unix remove e-mail message file commands file */
char	library [];	/* name of DNA sequence library for self comparison */
{
  char    base_name [ MAX_LINE ];	/* Translated sequence filename */
  int     frame;			/* Translation frame */
  t_text  segment;			/* Fasta formated DNA segment */
  t_seq   rev_seq;			/* reverse & complemented sequence */
  long    rev_end;			/* end of reverse DNA sequence segment */
  long    rev_start;			/* start of reverse DNA sequence segment */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Reverse & complement the DNA sequence. */
  reverse_seq ( seq, &rev_seq );
  rev_end   = (*seq).total - 1 - start;
  rev_start = (*seq).total - 1 - end;	

  /* Set up the Fasta segment sequence file name. */
  strcpy ( segment.name, (*seq).name );
  segment.name [ stridx ( segment.name, "-" ) ] = '\0'; 
  strcat ( segment.name, "-" );
  itoa ( version, version_str );
  if ( version < 100 )  strcat ( segment.name, "0" );
  if ( version < 10  )  strcat ( segment.name, "0" );
  strcat ( segment.name, version_str );
  strlower ( segment.name );
  strcpy ( base_name, segment.name );

  /* Write out the three forward translation files. */
  for ( frame = 0; frame <= 2; frame++ )
  {
    strcpy ( segment.name, base_name );
    strcat ( segment.name, "F" );
    itoa ( frame, version_str );
    strcat ( segment.name, version_str );

    /* Open the Fasta segment output file. */
    segment.data = fopen ( segment.name, "w" );
    if ( segment.data == NULL )
    {
      printf ( "Could not open the file '%s' for writing.\n", segment.name );
      return;
    }  /* if */

    /* Set up Unix erase file command. */
    if ( (*erase).data != NULL )

      fprintf ( (*erase).data, "rm %s\n", segment.name );

    /* Write out the Fasta name line. */
    write_fasta_name ( segment, segment.name, 
        start + 1, (*seq).total, end - start + 1 );

    /* Translate the DNA into amino acids. */
    translate_DNA ( seq, start, end, frame, &segment );

    /* Close the sequence file. */
    fclose ( segment.data );

    /* S = Swiss Protein database; P = PIR database; */
    /* H = GenPept database */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -Q -z " );
    fprintf ( (*cmds).data, "%s %%SPH ktup 2 > %sFP\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFP ../h_fasta\n", segment.name );
  }  /* for */

  /* Write out the three reverse translation files. */
  for ( frame = 0; frame <= 2; frame++ )
  {
    strcpy ( segment.name, base_name );
    strcat ( segment.name, "R" );
    itoa ( frame, version_str );
    strcat ( segment.name, version_str );

    /* Open the Fasta segment output file. */
    segment.data = fopen ( segment.name, "w" );
    if ( segment.data == NULL )
    {
      printf ( "Could not open the file '%s' for writing.\n", segment.name );
      return;
    }  /* if */

    /* Set up Unix erase file command. */
    if ( (*erase).data != NULL )

      fprintf ( (*erase).data, "rm %s\n", segment.name );

    /* Write out the Fasta name line. */
    write_fasta_name ( segment, segment.name, 
        start + 1, (*seq).total, end - start + 1 );

    /* Translate the DNA into amino acids. */
    translate_DNA ( rev_seq, rev_start, rev_end, frame, &segment );

    /* Close the sequence file. */
    fclose ( segment.data );

    /* S = Swiss Protein database; P = PIR database; */
    /* H = GenPept database */
    fprintf ( (*cmds).data, "fasta -o -b 20 -d 20 -h -m 0 -Q -z " );
    fprintf ( (*cmds).data, "%s %%SPH ktup 2 > %sFPr\n", 
        segment.name, segment.name );
    fprintf ( (*cmds).data, "mv %sFPr ../h_fasta\n", segment.name );
  }  /* for */
}  /* setup_fasta_protein */


/* This function writes out a Fasta format name line. */
write_fasta_name ( out, name, first, length, size )
t_text  *out;		/* output file */
char    name [];	/* sequence name */
int	first;		/* First base of sequence segment */
int	length;		/* Length of sequence */
int	size;		/* Size of sequence segment */
{
  /* Check for NULL file. */
  if ( (*out).data == NULL )  return;

  /* Pass useful information on the name line. */
  fprintf ( (*out).data, ">%s ",         name );
  fprintf ( (*out).data, " First= %d, ",  first );
  fprintf ( (*out).data, " Length= %d, ", length );
  fprintf ( (*out).data, " Size= %d ..\n",size );
}  /* write_fasta_name */


/* This function writes out a e-mail message in a file. */
write_email ( database, method, seq, start, end, version, email, cmds, erase )
char	database [];	/* database name to search */
char	method [];	/* search method command string */
t_seq   *seq;		/* DNA sequence */
long    start;		/* start of DNA sequence segment */
long    end;		/* end of DNA sequence segment */
int	version;	/* current version number */
t_text  *email;		/* e-mail message file */
t_text	*cmds;		/* Unix e-mail message send commands file */
t_text  *erase;		/* Unix remove e-mail message file commands file */
{
  char    email_name [ MAX_LINE ];	/* e-mail message output file name */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Set up the e-mail message sequence file name. */
  strcpy ( email_name, (*email).name );
  email_name [ stridx ( email_name, "-" ) ] = '\0'; 
  strcat ( email_name, "-" );

  /* Identify the search method and database. */
  version_str [ 0 ] = method [ 7 ];
  version_str [ 1 ] = database [ 0 ];
  version_str [ 2 ] = '\0';
  strcat ( email_name, version_str );

  itoa   ( version, version_str );
  if ( version < 100 )  strcat ( email_name, "0" );
  if ( version < 10 )  strcat ( email_name, "0" );
  strcat ( email_name, version_str );
  strlower ( email_name );

  /* Open the e-mail output file. */
  (*email).data = fopen ( email_name, "w" );
  if ( (*email).data == NULL )
  {
    printf ( "Could not open the file '%s' for writing.\n", email_name );
    return;
  }  /* if */

  /* Set up Unix e-mail send command. */
  if ( (*cmds).data != NULL )

    fprintf ( (*cmds).data, "mail Q@ORNL.GOV < %s\n", 
        email_name );

  /* Set up Unix erase file command. */
  if ( (*erase).data != NULL )

    fprintf ( (*erase).data, "rm %s\n", email_name );

  /* Identify the DNA type for target database. */
  if ( strcmp ( database, "SwissProt" ) == 0 )
    fprintf ( (*email).data, "TYPE DNA6\n" );
  else
    fprintf ( (*email).data, "TYPE DNA\n" ); 

  /* Identify the target database. */
  fprintf ( (*email).data, "TARGET %s\n", database );

  /* Identify the search method. */
  fprintf ( (*email).data, "%s\n", method );

  /* Pass useful information on the comment line. */
  fprintf ( (*email).data, "COMMENT %s ", email_name );
  fprintf ( (*email).data, " /First= %d, ", start + 1 );
  fprintf ( (*email).data, " /Length= %d, ", (*seq).total );
  fprintf ( (*email).data, " /Size= %d\n", end - start + 1 );

  /* Identify the following lines as DNA sequence. */
  fprintf ( (*email).data, "SEQ\n" );

  /* Write out the DNA sequence. */
  write_DNA_seq ( seq, start, end, email );

  /* Identify the end of the sequence/message. */
  fprintf ( (*email).data, "END\n" );

  /* Close the e-mail message output file. */
  fclose ( (*email).data );
}  /* write_email */


/* This function capitalizes a string. */
capitalize ( s )
char s [];		/* string */
{
  int  i = 0;

  while ( ( s [ i ] != '\0' ) && ( i < MAX_LINE ) )
  {
    if ( ( s [ i ] >= 'a' ) && ( s [ i ] <= 'z' ) )

      s [ i ] = s [ i ] - 'a' + 'A';

    i++;
  }  /* while */
}  /* capitalize */


/* This function gets the next text character. */
get_char ( text )
t_text  *text;		/* ASCII text file */
{
  if ( (*text).next == EOF )  return; 
  (*text).next = ' ';

  /* Get the next sequence character. */
  if ( ( (*text).line [ (*text).line_index ] != '\n' ) &&
       ( (*text).line [ (*text).line_index ] != '\0' ) )

    (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).next != EOF ) )

    if ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) )
    {
      if ( (*text).eof == TRUE )  (*text).next = EOF;
      else  get_line ( text );
    }
    else
      (*text).next = (*text).line [ (*text).line_index++ ];

}  /* get_char */


/* This function gets the next integer from the current line. */
int  get_int ( text )
t_text  *text;		/* ASCII text file */
{
  char  c;
  int   i, sign = 1;


  /* Get the first character. */
  get_char ( text );

  /* Check for a sign. */
  if ( ( (*text).next == '+' ) || ( (*text).next == '-' ) )
  {
    sign = ( (*text).next == '+' ) ? 1 : -1;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  /* Traverse the integer. */
  for ( i = 0; ( (*text).next >= '0' && (*text).next <= '9' );
      ( (*text).next = (*text).line [ (*text).line_index++ ] ) )

    i = i * 10 + (*text).next - '0';

  /* Set the integer sign. */
  i *= sign;

  return ( i );  /* return the integer */
}  /* get_int */


/* This function gets the next text line. */
get_line ( text )
t_text  *text;		/* ASCII text file */
{
  int  c = 0, i = 0;


  if ( (*text).next == EOF )  return;

  (*text).line_index = 0;

  /* Get the text line. */
  while ( ( i < MAX_LINE ) && ( ( c = getc ( (*text).data ) ) != EOF ) &&
          ( c != '\n' ) )

    (*text).line [ i++ ] = c;

  /* Properly terminate the text line. */
  (*text).line [ i++ ] = '\n';
  (*text).line [ i   ] = '\0';

  /* Check for end of file. */
  if ( c == EOF )  (*text).eof = TRUE;

  /* Get the first character. */
  (*text).next = (*text).line [ (*text).line_index++ ];
}  /* get_line */


/* This function gets the next text token. */
get_token ( text )
t_text  *text;		/* ASCII text file */
{
  int  i = 0;


  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )
  {
    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  strcpy ( (*text).token, "" );
  if ( (*text).eof == TRUE )  return;

  /* Copy the token. */
  while ( ( ( (*text).next >= '0' ) && ( (*text).next <= '9' ) ) ||
          ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
          ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ) 
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  /* Check for non-alphanumeric. */
  if ( i == 0 )
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  (*text).token [ i ] = '\0';
}  /* get_token */


/* Convert n to characters in s. */
itoa (n, s)
char s[];
int n;
{
  int i, sign;

  if ((sign = n) < 0)  /* record sign */
    n = -n;

  /* Generate the digits in reverse order. */
  i = 0;
  do 
  {
    s[i++] = n % 10 + '0';	/* get next digit */
  }
  while ((n /= 10) > 0);	/* delete it */

  if (sign < 0)  s[i++] = '-';

  s[i] ='\0';

  reverse (s);
}  /* itoa */


/* Convert c to lower case; ASCII only */
lower (c)
int c;
{
  if (c >= 'A' && c <= 'Z')

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


/* This function capitalizes a string. */
strlower ( s )
char s [];		/* string */
{
  int  i = 0;


  while ( ( s [ i ] != '\0' ) && ( i < MAX_LINE ) )
  {
    s [ i ] = lower ( s [ i ] );
    i++;
  }  /* while */
}  /* strlower */


/* This function opens a file. */
open_text_file ( text )
t_text  *text;		/* ASCII text file */
{
  FILE  *fopen ();


  /* Initialization. */
  (*text).next = ' ';
  (*text).token [ 0 ] = '\0';
  (*text).line  [ 0 ] = '\0';
  (*text).eof         = TRUE;
  (*text).data        = NULL;

  /* Check for a valid file name. */
  if ( (*text).name [ 0 ] == '\0' )  return;

  /* Open the text file in read mode. */
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) == NULL )

    printf ( "Could not open '%s'\n", (*text).name );
  else
    (*text).eof = FALSE;

  /* Get the first text line. */
  get_line ( text );

}  /* open_text_file */


/* This function promts for a file name. */
prompt_file ( text, prompt )
t_text  *text;		/* ASCII text file */
char    prompt [];	/* request prompt */
{
  (*text).name [ 0 ] = '\0';
  (*text).eof = TRUE;

  /* Prompt for a valid file name. */
  while ( ( (*text).eof == TRUE ) && ( strcmp ( (*text).name, "exit" ) != 0 ) )
  {
    printf ( "%s or 'exit' ", prompt );

    scanf ( "%s", (*text).name );

    if ( strcmp ( (*text).name, "exit" ) != 0 )

      open_text_file ( text );
  }  /* while */

  printf ( "\n" );
}  /* promp_file */


/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* DNA sequence */
{
  int  count;		/* composition count */
  int  composition [ MAX_ASCII ];	/* composition array */
  int  DNA_composition;			/* A,C,G,T on current line */
  int  end_of_file = FALSE;		/* end of file flag */
  int  index;


  /* Find the beginning of the DNA sequence. */
  do
  {
    /* Initialization. */
    for ( index = 0; index < MAX_ASCII; index++ )

      composition [ index ] = 0;

    count = 0;

    /* Total the line composition. */
    for ( index = 0; ( (*text).line [ index ] != '\n' ) &&
                     ( (*text).line [ index ] != '\0' ) &&
                     ( (*text).line [ index ] != EOF ); index ++ )
    {
      composition [ (*text).line [ index ] ]++;

      /* Count the non-blank characters. */
      if ( ( (*text).line [ index ] != ' ' ) &&
           ( (*text).line [ index ] != '\t' ) )  count++;
    }  /* for */

    DNA_composition = composition [ 'A' ] + composition [ 'a' ] +
                      composition [ 'C' ] + composition [ 'c' ] +
                      composition [ 'G' ] + composition [ 'g' ] +
                      composition [ 'T' ] + composition [ 't' ] +
                      composition [ 'N' ] + composition [ 'n' ] +
                      composition [ 'R' ] + composition [ 'r' ] +
                      composition [ 'Y' ] + composition [ 'y' ] +
                      composition [ 'B' ] + composition [ 'b' ];

    if ( DNA_composition * 1.0 <= count * 0.75 )

      get_line ( text );	/* get the next text line */
  }
  while ( ( (*text).eof != TRUE ) &&
          ( DNA_composition * 1.0 <= count * 0.75 ) );

  /* Read in the DNA sequence. */
  (*seq).total = 0;

  do
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
    {
      (*seq).base [ (*seq).total++ ] = (*text).next;

      if ( (*seq).total == MAX_BASES - 1 )
        printf ( "Maximum number of sequence bases reached.\n" );
    }  /* if */

    get_char ( text );		/* get the next character */

    if ( ( (*text).eof == TRUE ) && 
         ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) ||
           ( (*text).next == EOF ) ) )
      end_of_file = TRUE;
  }  
  while ( ( end_of_file != TRUE ) && ( (*seq).total < MAX_BASES ) );

  /* Pad the end of the sequence with two N's for translation. */
  if ( (*seq).total + 2 < MAX_BASES )
  {
    (*seq).base [ (*seq).total ] = 'N';
    (*seq).base [ (*seq).total + 1 ] = 'N';
  }  /* if */
}  /* read_DNA_seq */


/* Reverse string s in place. */
reverse(s)
char s[];
{
  int  c, i, j;

  for (i = 0, j = strlen(s)-1; i < j; i++, j--)
  {
    c    = s[i];
    s[i] = s[j];
    s[j] = c;
  }  /* for */
}  /* reverse */


/* This function concatenates t to the end of s. */
strcat (s, t)
char  s[], t[];		/* s must be big enough */
{
  int  i = 0, j = 0;


  /* Find the end of s. */
  while ( s [ i ] != '\0' )  i++;

  /* Copy t. */
  while ( ( s [ i++ ] = t [ j++ ] ) != '\0' )  
    ;
}  /* strcat */


/* Return <0 if s<t, 0 if s==t, >0 if s>t */
strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* strcmp */


/* Copy t to s */
strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* strcpy */


/* This function returns the index of t in s or index of '\0'. */
stridx (s, t)
char s[], t[];
{
  int  i, j, k;

  for (i = 0; s [ i ] != '\0'; i++ )
  {
    for ( j = i, k = 0; t [ k ] != '\0' && s [ j ] == t [ k ]; j++, k++ )  ;

    if ( t [ k ] == '\0' )  return ( i );
  }  /* for */

  return ( i );
}  /* stridx */


/* Return length of string s */
strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* strlen */


/* This function writes out a DNA sequence block. */
write_DNA_seq ( seq, first, last, text )
t_seq	*seq;		/* DNA sequence */
long	first;		/* first base of block */
long	last;		/* last base of block */
t_text  *text;		/* output text file */
{
  int   flag;
  long  index;		/* sequence index */


  /* Check for NULL output file. */
  if ( (*text).data == NULL )  return;
  
  /* Print out the DNA block. */
  for ( index = first; index <= last; index++ )
  {
    fprintf ( (*text).data, "%c", (*seq).base [ index ] );
    flag = 1;

    if ( ( ( index - first + 1 ) % 50 ) == 0 )
    {
      fprintf ( (*text).data, "\n" );

      flag = 0;
    }  /* if */
  }  /* for */

  if ( flag == 1 )

    fprintf ( (*text).data, "\n" );
}  /* write_DNA_seq */


/* This function translates a codon into an amino acid. */
char map_codon ( codon )
char codon [];		/* DNA codon */
{
  char amino_acid;	/* translated amino acid */
  int  index;		/* codon table index */


  capitalize ( codon );

  index = base_map   [ codon [ 0 ] ] * 16 +
          base_map   [ codon [ 1 ] ] *  4 +
          base_map_3 [ codon [ 2 ] ];

  if ( index >= MAX_CODON )
  {
    index = base_map [ codon [ 0 ] ] * 16 +
            base_map [ codon [ 1 ] ] *  4;

    /* TRA */
    if ( ( codon [ 0 ] == 'T' ) && ( codon [ 1 ] == 'R' ) &&
         ( codon [ 2 ] == 'A' ) )  return ( '*' );

    /* YTR */
    if ( ( codon [ 0 ] == 'Y' ) && ( codon [ 1 ] == 'T' ) )

      if ( ( codon [ 2 ] == 'R' ) || ( codon [ 2 ] == 'A' ) ||
           ( codon [ 2 ] == 'G' ) )  return ( 'L' );

    /* MGR */
    if ( ( codon [ 0 ] == 'M' ) && ( codon [ 1 ] == 'G' ) )

      if ( ( codon [ 2 ] == 'R' ) || ( codon [ 2 ] == 'A' ) ||
           ( codon [ 2 ] == 'G' ) )  return ( 'R' );

    if ( index >= MAX_CODON )  return ( 'X' );

    amino_acid = codon_map [ index ];

    /* GCN, GGN, CCN, ACN, & GTN */
    if ( ( amino_acid == 'A' ) || ( amino_acid == 'G' ) || 
         ( amino_acid == 'P' ) ||
         ( amino_acid == 'T' ) || ( amino_acid == 'V' ) )
      return ( amino_acid );

    /* CTN */
    if ( ( amino_acid == 'L' ) && ( codon [ 0 ] == 'C' ) )
      return ( amino_acid );

    /* CGN */
    if ( ( amino_acid == 'R' ) && ( codon [ 0 ] == 'C' ) )
      return ( amino_acid );

    /* TCN */
    if ( ( amino_acid == 'S' ) && ( codon [ 0 ] == 'T' ) )
      return ( amino_acid );

    /* ATH */
    if ( ( amino_acid == 'I' ) && ( ( codon [ 2 ] == 'H' ) ||
         ( codon [ 2 ] == 'K' ) || ( codon [ 2 ] == 'W' ) ) )
      return ( amino_acid );
 
    return ( 'X' );
  }
  else
    return ( codon_map [ index ] );
}  /* map_codon */


/* This function reverses and complements a DNA sequence. */
reverse_seq ( seq, rev )
t_seq	*seq;		/* DNA sequence */
t_seq	*rev;		/* reversed & complemented DNA sequence */
{
  int  index;		/* base index */


  (*rev).total = (*seq).total;

  strcpy ( (*rev).name, (*seq).name );

  for ( index = 0; index < (*seq).total; index++ )

    (*rev).base [ (*rev).total - index - 1 ] = 
      complement [ (*seq).base [ index ] ];
}  /* reverse_seq */


/* This function maps a DNA sequence range. */
translate_DNA ( seq, start, end, frame, text )
t_seq   *seq;		/* DNA sequence */
int	start;		/* start of DNA segment */
int	end;		/* end of DNA segment */		/* ### */
int     frame;		/* translation frame [0, 1, 2] */
t_text  *text;		/* output text file */
{
  long  block;		/* end of sequence block */
  long  index;		/* sequence index */


  /* Map the DNA sequence range. */
  index = start + frame;
  while ( index <= end )
  {
    /* Process one block of sequence at a time. */
    block = index + BLOCK_SIZE - 1;

    if ( block >= (*seq).total )  block = (*seq).total - 1;

    /* Write out the DNA translation. */
    map_seq ( seq, index, block, 'F', text, index );

    index += BLOCK_SIZE;
  }  /* while */
}  /* translate_DNA */


/* This function writes out the DNA translation. */
map_seq ( seq, first, last, strand, text, start )
t_seq	*seq;		/* DNA sequence */
long	first;		/* first base of block */
long	last;		/* last base of block */
char    strand;		/* F = forward; R = reverse */
t_text	*text;		/* output text file */
int	start;		/* start of DNA segment */
{
  char  amino_acid;			/* translated amino acid */
  char  codon [ CODON_SIZE + 1 ];	/* DNA codon */
  long  index;				/* sequence index */


  /* Initialization. */
  codon [ CODON_SIZE ] = '\0';

  /* Check for forward reading frame. */
  if ( strand == 'F' )
  {
    /* Translate the reading frame. */
    for ( index = first; index <= last; index += 3 )
    {
      if ( index + 2 < MAX_BASES )  
        amino_acid = map_codon ( &((*seq).base [ index ]) );
      else  amino_acid = 'X';

      /* Fasta doesn't tolerate protein sequences that start with '*'. */
      if ( ( index == start ) && ( amino_acid == '*' ) )  amino_acid = 'X';

      if ( ( index + 2 < (*seq).total ) || 
           ( ( index + 2 >= (*seq).total ) && ( amino_acid != 'X' ) ) )

        fprintf ( (*text).data, "%c", amino_acid );
    }  /* for */

    fprintf ( (*text).data, "\n" );
  }  /* if */
  else
  {
    /* Translate the complement reading frame. */
    for ( index = last; index >= first; index -= 3 )
    {
      codon [ 0 ] = complement [ (*seq).base [ index ] ];

      if ( index - 1 >= 0 )
        codon [ 1 ] = complement [ (*seq).base [ index - 1 ] ];
      else
        codon [ 1 ] = 'N';

      if ( index - 2 >= 0 )
        codon [ 2 ] = complement [ (*seq).base [ index - 2 ] ];
      else
        codon [ 2 ] = 'N';

      amino_acid = map_codon ( codon );

      if ( ( index > 1 ) || 
           ( ( index <= 1 ) && ( amino_acid != 'X' ) ) )

        fprintf ( (*text).data, "%c", amino_acid );
    }  /* for */

    fprintf ( (*text).data, "\n" );
  }  /* else */
}  /* map_seq */


/******************************************************************************/
/* This function parses a standard name. */
parse_name ( name, cosmid, subclone, direction, version, method )
char	name      [];		/* sequence name */
char	cosmid    [];		/* BAC or cosmid name */
char	subclone  [];		/* subclone number */
char    direction [];		/* sequence direction */
char	version   [];		/* sequence version */
char	method    [];		/* sequencing method */
{
  int  index;			/* array index */
  int  name_index;		/* name index */
  char sub_name [ MAX_LINE ];	/* name subcomponent */


  /* Initialize subcomponent names. */
  strcpy ( cosmid,   "" );
  strcpy ( subclone, "" );
  strcpy ( direction, "" );
  strcpy ( version,  "" );
  strcpy ( method,   "" );

  /* Extract the BAC or cosmid name. */
  strcpy ( sub_name, name );
  name_index = stridx ( sub_name, "." );
  sub_name [ name_index ] = '\0';
  strcpy ( cosmid, sub_name );

  /* Extract the subclone name. */
  name_index++;
  if ( name_index >= strlen ( name ) )  return;
  strcpy ( sub_name, &(name [ name_index ]) );
  index = stridx ( sub_name, "." );
  sub_name [ index ] = '\0';
  strcpy ( subclone, sub_name );

  /* Extract the direction. */
  name_index += index + 1;
  if ( name_index >= strlen ( name ) )  return;
  strcpy ( sub_name, &(name [ name_index ]) );
  index = stridx ( sub_name, "." );
  sub_name [ index ] = '\0';
  strcpy ( direction, sub_name );

  /* Extract the version. */
  name_index += index + 1;
  if ( name_index >= strlen ( name ) )  return;
  strcpy ( sub_name, &(name [ name_index ]) );
  index = stridx ( sub_name, "." );
  sub_name [ index ] = '\0';
  strcpy ( version, sub_name );

  /* Extract the method. */
  name_index += index + 1;
  if ( name_index >= strlen ( name ) )  return;
  strcpy ( sub_name, &(name [ name_index ]) );
  index = stridx ( sub_name, "." );
  sub_name [ index ] = '\0';
  strcpy ( method, sub_name );
}  /* parse_name */


