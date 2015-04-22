

#include <stdio.h>
#include <math.h>

/* This program implements a simple exclusize OR (XOR) neural network. */
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

#define  ALPHA            0.5   /* learning rate constant */

#define  MAX_LAYER_NODES  3     /* maximum number of nodes per layer */

#define  MAX_LAYERS       3     /* maximum number of neural network layers */

#define  MAX_PATTERNS     4     /* number of input patterns */

#define  MAX_RANDOM       (32768 * 65536 - 1)    /* 2^31-1 - maximum random number from rand */


/* Ne ) fractal_sum += change;

    /* Total patterns separately. */
    if ( ( comp_type & comp_types [ PATTERN ] ) != 0 ) 
      pattern_sum += change;

    sum_all += change;

    if ( ( ( (seq_index + 1) % FRACTAL_REPORTING ) == 0 ) ||
         ( seq_index + 1 == (*sequence).length ) )
      fprintf ( fractal, "%d\t%d\t%d\t%d\n", seq_index + 1, fractal_sum,
          sum_all, pattern_sum );
  }  /* for */

  fclose ( fractal );    /* close the fractal output file */
}  /* write_fractal */


/******************************************************************************/
/* This function processes a GCG sequence data file. */
scan_file ( patterns, file_name, data_file, seq_stats )
t_patterns	*patterns;		/* current patterns to search for */
char		file_name [];		/* sequence file name */
FILE		*data_file;		/* GCG sequence file */
t_seq_stats	*seq_stats;		/* sequence statistics table */
{
  static  	t_components	components;	/* sequence components table */
  static	t_sequence	sequence;	/* entire sequence data */

  t_gcg_header	gcg_header;		/* GCG data file header */
  t_genes	genes;			/* table of genes */
  int		index;			/* current pattern */
  char		line [ MAX_LINE ];	/* current sequence line */
  int		line_index;		/* current line index */
  int		mask;			/* sequence bit representation */
  char		seq_char;		/* current sequence character */
  int		status;			/* function return status */

  file_name [ charidx ( ' ', file_name ) ] = '\0';  /* truncate name at 1st space */
  strcopy ( sequence.filename, file_name );
  strcopy ( components.file_name, file_name );

  components.total = 0;

  /* Process the GCG file header up to the features table. */
  status = read_gcg_header ( data_file, &gcg_header );

  /* Process the GCG features table. */
  if ( status == S_NORMAL )
    status = read_features ( data_file, &components, &genes, &gcg_header );

  /* Find the sequence data. */
  if ( status == S_NORMAL )
    find_data ( data_file );
  if ( status == S_DATA )  status = S_NORMAL;

  /* Read in the sequence. */
  sequence.length = 0;
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
      sequence.base [ sequence.length++ ].bits = mask;

/* if (( seq_char != 'A' ) && ( seq_char != 'C' ) && ( seq_char != 'G' ) &&
    ( seq_char != 'T' ) && ( seq_char != 'X' ) && ( seq_char != 'N' ) &&
    ( seq_char != 'R' ) && ( seq_char != 'Y' ))
  printf ( "*INFO* Non-DNA base '%c' in file '%s'.\n", seq_char, file_name ); */

      if ( sequence.length == MAX_BASES - 1 )
      {
        printf ( "*WARN* Sequence '%s' is too long.\n", file_name );
        status = S_TABLE_OVERFLOW;
      }  /* if */
    }  /* if */
  }  /* while */

  /* Compute the sequence statistics for this sequence. */
  count_bases ( &sequence, &components, seq_stats );

  print_components ( &components );

  /* Mark the splicing consensus regions around exons. */
/*  mark_splice ( &components ); */

/* printf ("\n(%s) length = %d\n", sequence.filename, sequence.length ); */

/* print_components ( &components ); */

/*  write_sequence ( &sequence, &components, comp_types [ gDNA_INTRON ] );
  write_sequence ( &sequence, &components, comp_types [ gDNA_INTRON ] +
      comp_types [ REPEAT ] ); */

/* repeats ( &sequence, &components ); */

/*  unit_patterns ( seq_stats, &sequence, &components ); */

/*  write_sequence ( &sequence, &components, comp_types [ gDNA_INTRON ] );
  write_sequence ( &sequence, &components, comp_types [ gDNA_INTRON ] +
      comp_types [ REPEAT ] ); */

  /* Search the sequence for pattern matches. */
  for ( index = 0; index < (*patterns).total; index++ )
    find_pattern ( &sequence, &((*patterns).pattern [ index ]), &components );

  /* write out the fractal sums over the sequence length */
  write_fractal ( &sequence, &components, comp_types [ gDNA_INTRON ] );

  print_components ( &components );
}  /* scan_file */


/******************************************************************************/
/* This function processes a list of sequence files. */
process_files ( file_of_files, patterns_file, suffix )
FILE	*file_of_files;		/* list of file names to process */
FILE	*patterns_file;		/* file of search patterns */
char	suffix [];		/* pattern output suffix */
{
  FILE		*data_file;			/* GCG sequence file */
  char		file_name [ MAX_LINE ];		/* file name of the GCG data file */
  FILE          *fopen ();                      /* file open routine */
  int		index;				/* composition index */
  char		line [ MAX_LINE ];		/* current line of file_of_files */
  int		line_index;			/* current line index */
  t_patterns	patterns;			/* search patterns table */
  char		patterns_name [ MAX_LINE ];	/* output patterns file name */
  t_seq_stats	seq_stats;			/* sequence statistics table */
  int		status = S_NORMAL;		/* function return status */

  /* Initialize the sequence statistics table. */
  init_stats ( &seq_stats );

  suffix [ charidx ( '.', suffix ) ] = '\0';

  /* Process the patterns file. */
  patterns.total = 0;
  if ( patterns_file != NULL )
  {
    find_data ( patterns_file );
    while (( status != S_EOF ) && ( status != S_TABLE_OVERFLOW ))
    {
      status = get_pattern ( patterns_file,
        &(patterns.pattern [ patterns.total ]) );
      if ( status == S_NORMAL )  
      {
        strcpy ( patterns_name, patterns.pattern [ patterns.total ].name );
        concatenate ( patterns_name, "." );
        concatenate ( patterns_name, suffix );
        patterns.pattern [ patterns.total ].pat_file =
            fopen ( patterns_name, "w" );
        for ( index = 0; index < MAX_HISTOGRAM; index++ )
          patterns.pattern [ patterns.total ].histo.lengths [ index ] = 0;
        patterns.total++; 
      }  /* if */

      if ( patterns.total == MAX_PATTERNS - 1 )
      {
        printf ( "*WARN* Too many search patterns, first %d used.\n",
            MAX_PATTERNS );
        status = S_TABLE_OVERFLOW;
      }  /* if */
    }  /* while */
  }  /* if */
  status = S_NORMAL;

  print_patterns ( &patterns ); 

  /* Process the file of file names. */
  while ( status != S_EOF )
  {
    /* Get the next line from the file of file names. */
    status = get_line ( file_of_files, line );
    line_index = 0;
    while ( line [ line_index ] == ' ' )  line_index++;

    if (( status == S_NORMAL ) && 
        ( blank_line ( &(line [ line_index ]) ) != 0 ))
    {
/* printf ( "opening '%s'\n", &(line [ line_index ]) ); */

      status = open_file ( &(line [ line_index ]), &data_file );

      if ( status == S_NORMAL )
      {
        scan_file ( &patterns, &(line [ line_index ]), data_file, &seq_stats );
        fclose ( data_file );  /* close the GCG data file */
      }  /* if */
    }  /* if */
  }  /* while */

  /* Print out the pattern match lengths histograms. */
/*  for ( index = 0; index < patterns.total; index++ )
    print_histogram ( patterns.pattern [ index ].name,
        &(patterns.pattern [ index ].histo) ); */

  /* Print out the sequence(s) statistics. */
/*  print_stats ( &seq_stats ); */

  /* Close the pattern output files. */
  for ( index = 0; index < patterns.total; index++ )
  {
    fclose ( patterns.pattern [ index ].pat_file );

/* print pattern length histograms */

  }  /* for */
}  /* process_files */


/******************************************************************************/
/* This function prints out the components table. */
print_components ( components )
t_components	*components;	/* sequence components table */
{
  int	index = 0;	/* components table index */

  printf ( "\nThe components table:\n" );
  printf ( "\nTotal components = %d.\n\n", (*components).total );
  printf ( "Begin	End	Type\n" );

  while ( index < (*components).total )
  {
    printf ( "%6d   %6d     ", (*components).atom [ index ].begin,
        (*components).atom [ index ].end );

/*    print_types ( comp_types [ (*components).atom [ index ].type ] ); */

printf ( "(%d)", (*components).atom [ index ].type );

    printf ( "\n" );
    index++;
  }  /* while */
  printf ( "\n" );
}  /* print_components */


/* This function prints out a histogram table. */
print_histogram ( name, histogram )
char         name [];     /* histogram name */
t_histogram  *histogram;  /* the histogram to print */
{
  int  index;  /* histogram index */

  printf ( "\"Histogram for %s:\"\n", name );

  for ( index = 0; index < MAX_HISTOGRAM - 1; index++ )
/*    if ( (*histogram).lengths [ index ] > 0 ) */
      printf ( "%3d  %6d\n", index, (*histogram).lengths [ index ] );

  if ( (*histogram).lengths [ MAX_HISTOGRAM - 1 ] > 0 )
    printf ( "%3d+ %6d\n", MAX_HISTOGRAM - 1, (*histogram).lengths [ index ] );

  printf ( "\n" );

}  /* print_histogram */


/******************************************************************************/
/* This function prints out the patterns table. */
print_patterns ( patterns )
t_patterns	*patterns;	/* table of search patterns */
{
  int	base;		/* pattern index */
  int	index = 0;	/* patterns table index */

  printf ( "\nThe patterns table:\n" );
  printf ( "\nTotal patterns = %d.\n\n", (*patterns).total );
  printf ( "Name    length     minimum     type     match    pattern\n" );

  while ( index < (*patterns).total )
  {
    printf ( "%10s  %6d  %6d   %6d  %f  ",
      (*patterns).pattern [ index ].name,
      (*patterns).pattern [ index ].length,
      (*patterns).pattern [ index ].minimum,
      (*patterns).pattern [ index ].type,
      (*patterns).pattern [ index ].match );

    for ( base = 0; base < (*patterns).pattern [ index ].length; base++ )
      printf ( "%c",
          dna_map [ (*patterns).pattern [ index ].base [ base ].bits ] );
    printf ( "\n" );
    index++;
  }  /* while */
  printf ( "\n" );
}  /* print_patterns */


/******************************************************************************/
/* This function prints out the total sequences composition. */
print_composition ( composition )
t_composition	*composition;	/* combined sequences composition */
{
  int	index;		/* composition index */
  int	printed = 0;	/* number of characters printed */

  printf ( "\n" );

  for ( index = 0; index <= 'Z' - 'A'; index++ )
    if ( (*composition).count [ index ] > 0 )
    {
      printf ( "%c  %5d  ", index + 'A', (*composition).count [ index ] );
      printed++;
      if ( printed >= 10 )
      {
        printf ( "\n" );
        printed = 0;
      }  /* if */
    }  /* if */
  printf ( "\n\n" );
}  /* print_composition */


/******************************************************************************/
/* This function prints out a range of bases. */
print_bases ( sequence, start, end, print_range, data_file, printed )
t_sequence	*sequence;	/* entire sequence */
long		start;		/* start of print range */
long		end;		/* end of print range */
int		print_range;	/* Boolean flag indicating if range is selected */
FILE		*data_file;	/* output data file */
int		*printed;	/* Bases printed */
{
  long	seq_index;	/* sequence index */

  for ( seq_index = start; seq_index <= end; seq_index++ )
  {
    if ( *printed == 0 )  fprintf ( data_file, "%10d  ", seq_index );

    if ( print_range == TRUE )
      fprintf ( data_file, "%c", 
          dna_map [ (*sequence).base [ seq_index - 1 ].bits ] );
    else
      fprintf ( data_file, "." );
    (*printed)++;

    /* Space every 10 bases. */
    if (( (*printed) % 10 ) == 0 )  fprintf ( data_file, " " );

    if ( (*printed) == 50 )
    {
      fprintf ( data_file, "\n" );
      *printed = 0;
    }  /* if */
  }  /* for */
}
