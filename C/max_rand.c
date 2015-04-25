
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


#define  COSMID_SIZE	40000	/* default cosmid size */

#define  MAX_COSMID	40000	/* maximum cosmid size */

#define  MAX_PAIRS	100	/* maximum sequence pairs */

#define  MAX_RANDOM	200000	/* maximum random number */

#define  MAX_SEGMENTS	500	/* maximum sequence segments */

#define  MIN_OVERLAP	50	/* minimum number of overlapping base pairs */

#define  PRIMER_BASE	50	/* bases needed for primer site */

#define  SEGMENT_LEN	3000	/* default segment length */

#define  SEQUENCE_LEN	500	/* default size of sequence segment */


/* Random sequence segment. */
typedef struct {
  long  first;		/* start of first sequence segment */
  long  second;		/* start of second sequence segment */
  int   len_1;		/* length of first sequence segment */
  int   len_2;		/* length of second sequence segment */
  int   contig;		/* contig number */
} t_segment;

/* Table of sequence segments. */
typedef struct {
  int        total;			/* number of sequence segments */
  long       size;			/* cosmid size */
  t_segment  sase [ MAX_SEGMENTS + 1 ];	/* sequence segments */
} t_sassy;


main ()
{
  t_sassy  sassy; 			/* sample sequence segments table */
  int      segments;			/* sequence reaction pairs */
  short    sequence  [ MAX_COSMID + 1 ];/* cosmid DNA bases sequenced */
  int      steps     [ MAX_PAIRS + 1 ];	/* number of walking steps */
  float    ave_seq1  [ MAX_PAIRS + 1 ];	/* average times a base sequenced 1 */
  float    ave_seq2  [ MAX_PAIRS + 1 ];	/* average times a base sequenced 2 */
  float    coverage1 [ MAX_PAIRS + 1 ];	/* cosmid coverage 1 */
  float    coverage2 [ MAX_PAIRS + 1 ];	/* cosmid coverage 2 */
  int      groups1   [ MAX_PAIRS + 1 ];	/* ordered groups 1 */
  int      groups2   [ MAX_PAIRS + 1 ];	/* ordered groups 2 */
  int      islands1  [ MAX_PAIRS + 1 ];	/* sequence islands 1 */
  int      islands2  [ MAX_PAIRS + 1 ];	/* sequence islands 2 */

  long  count = 0;
  long  max_rand = 0;  /* temporary code */
  long  last_rand; 

  /* Compute the maximum random number. */
  for ( segments = 1; count < 50000; segments++ )
  {
    last_rand = rand ();
    count++;
    if ( last_rand > max_rand )
    {
      max_rand = last_rand;
      count = 0;
    }  /* if */
  }  /* for */
  printf ( "Maximum random number = %d\n", max_rand );
  return;

  /* Test series on increasing number of segments. */
  for ( segments = 1; segments <= MAX_PAIRS; segments++ )
  {
    /* Randomly select segments. */
    new_segments ( segments, COSMID_SIZE, &sassy );

    /* Count the number of ordered groups. */
    count_groups ( &sassy, &(groups1 [ segments ]) );

    /* Compute the cosmid coverage. */
    coverage ( &sassy, &(islands1 [ segments ]), &(coverage1 [ segments ]),
        &(ave_seq1 [ segments ]), sequence );

    /* Perform walk into all unknown regions. */
    walk_once ( &sassy, sequence, &(steps [ segments ]) );

    /* Count the number of ordered groups after walk. */
    count_groups ( &sassy, &(groups2 [ segments ]) );

    /* Compute the cosmid coverage after walk. */
    coverage ( &sassy, &(islands2 [ segments ]), &(coverage2 [ segments ]),
        &(ave_seq2 [ segments ]), sequence );
  }  /* for */

  /* Print out the results. */

}  /* main */


/* This function adds a sequence segment to the table. */
add_segment ( seg, sassy )
t_segment  *seg;	/* sequence segment */
t_sassy    *sassy;	/* sample sequence */
{
  int  sassy_index;	/* sassy table index */


  sassy_index = (*sassy).total;

  /* Check for full table. */
  if ( (*sassy).total == MAX_SEGMENTS )  return;

  if ( (*sassy).total == MAX_SEGMENTS - 1 )
    printf ( "Maximum number of segments reached.\n" );

  /* Find the insertion location. */
  while ( ( sassy_index > 0 ) &&
      ( ( (*seg).first < (*sassy).sase [ sassy_index ].first ) ||
        ( (*seg).second < (*sassy).sase [ sassy_index ].second ) ) )
  {
    /* Shift records down one. */
    (*sassy).sase [ sassy_index + 1 ].first = 
        (*sassy).sase [ sassy_index ].first;
    (*sassy).sase [ sassy_index + 1 ].second =
        (*sassy).sase [ sassy_index ].second;
    (*sassy).sase [ sassy_index + 1 ].len_1 =
        (*sassy).sase [ sassy_index ].len_1;
    (*sassy).sase [ sassy_index + 1 ].len_2 =
        (*sassy).sase [ sassy_index ].len_2;
    sassy_index--;
  }  /* while */

  /* Store the current segment. */
  (*sassy).sase [ sassy_index ].first  = (*seg).first;
  (*sassy).sase [ sassy_index ].second = (*seg).second;
  (*sassy).sase [ sassy_index ].len_1  = (*seg).len_1;
  (*sassy).sase [ sassy_index ].len_2  = (*seg).len_2;
  (*sassy).total++;
}  /* add_segment */


/* This function counts the number of ordered groups. */
count_groups ( sassy, groups )
t_sassy  *sassy;	/* sequence segments table */
int      *groups;	/* ordered groups */
{
  int  next_index;	/* next segment index */
  int  sassy_index;	/* sassy table index */


  *groups = 0;

  /* Traverse the sample sequence table. */
  for ( sassy_index = 1; sassy_index <= (*sassy).total; sassy_index++ )
  {
    /* Check for a new group. */
    if ( (*sassy).sase [ sassy_index ].contig == 0 )
    {
      (*groups)++;
      (*sassy).sase [ sassy_index ].contig = *groups;
    }  /* if */

    /* Combine all subsequent overlaps. */
    next_index = sassy_index + 1;

    while ( next_index <= (*sassy).total )
    {
      /* Test for nonoverlapped sequence. */
      if ( (*sassy).sase [ next_index ].contig == 0 )
      {
        /* Test for overlaps. */
        if ( ( (*sassy).sase [ next_index  ].first <
             ( (*sassy).sase [ sassy_index ].first + 
               (*sassy).sase [ sassy_index ].len_1 - MIN_OVERLAP ) ) &&
             ( (*sassy).sase [ next_index  ].first != 0 ) )

          (*sassy).sase [ next_index ].contig =
              (*sassy).sase [ sassy_index ].contig;

        if ( ( (*sassy).sase [ next_index  ].first >
             ( (*sassy).sase [ sassy_index ].second -
               (*sassy).sase [ sassy_index ].len_2 + MIN_OVERLAP ) ) &&
             ( (*sassy).sase [ next_index  ].first <
             ( (*sassy).sase [ sassy_index ].second +
               (*sassy).sase [ sassy_index ].len_2 - MIN_OVERLAP ) ) )

          (*sassy).sase [ next_index ].contig =
              (*sassy).sase [ sassy_index ].contig;
      }  /* if */

      /* Check for no possible overlap. */
      if ( (*sassy).sase [ next_index  ].first >
         ( (*sassy).sase [ sassy_index ].second +
           (*sassy).sase [ sassy_index ].len_2 - MIN_OVERLAP ) )

        next_index = (*sassy).total;    /* exit while loop */
      else
        next_index++;
    }  /* while */
  }  /* for */
}  /* count_groups */


/* This function computes the cosmid coverage. */
coverage ( sassy, islands, coverage, ave_sequenced, sequence )
t_sassy  *sassy;		/* sequence segments table */
int      *islands;		/* sequence islands */
float    *coverage;		/* cosmid coverage */
float    *ave_sequenced;	/* average times a base was sequenced */
short    sequence [];		/* cosmid DNA sequence */
{
  int   base;			/* base pair index */
  long  bases_sequenced = 0;	/* number of bases sequenced */
  long  index;			/* table index */
  long  unique_sequenced = 1;	/* unique bases sequenced */


  /* Initialization. */
  for ( index = 0; index <= (*sassy).size; index++ )
    sequence [ index ] = 0;

  /* Traverse the sample sequences. */
  for ( index = 1; index < (*sassy).total; index++ )
  {
    /* Traverse the first sequence. */
    if ( (*sassy).sase [ index ].first > 0 )
    {
      for ( base = 1; base <= (*sassy).sase [ index].len_1; base++ )

        /* Range check. */
        if ( (*sassy).sase [ index ].first + base - 1 <= MAX_COSMID )

          sequence [ (*sassy).sase [ index ].first + base - 1 ]++;

      bases_sequenced += (*sassy).sase [ index ].len_1;
    }  /* if */

    /* Traverse the second sequence. */
    if ( (*sassy).sase [ index ].second > 0 )
    {
      for ( base = 1; base <= (*sassy).sase [ index ].len_2; base++ )

        /* Range check. */
        if ( (*sassy).sase [ index ].second + base - 1 <= MAX_COSMID )

          sequence [ (*sassy).sase [ index ].second + base - 1 ]++;

      bases_sequenced += (*sassy).sase [ index ].len_2;
    }  /* if */
  }  /* for */

  /* Count the sequence islands. */
  for ( index = 2; index <= (*sassy).size; index++ )
  {
    /* Count the unique bases sequenced. */
    if ( sequence [ index ] > 0 )  unique_sequenced++;

    /* Count the island transitions. */
    if ( ( sequence [ index - 1 ] > 0 ) && ( sequence [ index ] == 0 ) )
      (*islands)++;
  }  /* for */

  *coverage = ( unique_sequenced * 1.0 ) / ( (*sassy).size * 1.0 );

  *ave_sequenced = ( bases_sequenced * 1.0 ) / ( unique_sequenced * 1.0 );
}  /* coverage */


/* This function randomly generates segments. */
new_segments ( no_segments, cosmid_size, sassy )
int      no_segments;		/* number of segments to generate */
long     cosmid_size;		/* number of base pairs in cosmid */
t_sassy  *sassy;		/* sequence segments table */
{
  int        new_segs;		/* number of segments generated */
  t_segment  seg;		/* randomly generated segment */


  /* Initialization. */
  (*sassy).total = 0;
  (*sassy).size  = cosmid_size;

  seg.first  = 1;
  seg.second = 0;
  seg.len_1  = SEQUENCE_LEN;
  seg.len_2  = 0;
  seg.contig = 0;

  /* Sequence from the cosmid ends. */
  add_segment ( &seg, sassy );

  seg.first  = 0;
  seg.second = cosmid_size - SEQUENCE_LEN + 1;
  seg.len_1  = 0;
  seg.len_2  = SEQUENCE_LEN;

  add_segment ( &seg, sassy );

  seg.len_1 = SEQUENCE_LEN;

  /* Generate the new segments. */
  for ( new_segs = 1; new_segs <= no_segments; new_segs++ )
  {
    /* Select the segment starting base. */
    seg.first = ( ( rand () * 1.0 ) / ( MAX_RANDOM * 1.0 ) ) *
        ( ( cosmid_size - SEGMENT_LEN ) + 1.0 );

    seg.second = seg.first + SEGMENT_LEN - SEQUENCE_LEN;

    add_segment ( &seg, sassy );
  }  /* for */
}  /* new_segments */


/* This function extends sequences into unsequenced regions. */
walk_once ( sassy, sequence, steps )
t_sassy  *sassy;	/* sequence segments table */
short    sequence [];	/* cosmid DNA coverage */
int      *steps;	/* number of walk steps */
{
  long       index;	/* index */
  t_segment  seg;	/* sequence walk segment */


  /* Initialization. */
  seg.first  = 0;
  seg.second = 0;
  seg.len_1  = SEQUENCE_LEN;
  seg.len_2  = 0;
  seg.contig = 0;

  /* Traverse the DNA sequence. */
  for ( index = 1; index <= (*sassy).size - 1; index++ )

    /* Check for the beginning of an island. */
    if ( ( sequence [ index ] == 0 ) && ( sequence [ index + 1 ] > 0 ) )
    {
      seg.first = index - SEQUENCE_LEN + PRIMER_BASE + 1;

      if ( seg.first < 1 )  seg.first = 1;

      add_segment ( &seg, sassy );

      (*steps)++;
    }
    else
      /* Check for the end of an island. */
      if ( ( sequence [ index ] > 0 ) && ( sequence [ index + 1 ] == 0 ) )
      {
        seg.first = index - PRIMER_BASE + 1;

        if ( seg.first < 1 )  seg.first = 1;

        add_segment ( &seg, sassy );

        (*steps)++;
      }  /* if */

  /* Reset the contig assignments. */
  for ( index = 1; index <= (*sassy).total; index++ )
    (*sassy).sase [ index ].contig = 0;

}  /* walk_once */
