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


#define  PENT	5	/* number of hash positions */

#define  MAX_BASES	10000	/* total sequence length */

#define  MAX_HASH	1024	/* number of pentanucleotides */

#define  MAX_ALIGN_RECORDS	1000	/* maximum alignment records */

#define  MAX_POSITIONS	10	/* maximum identical hash positions */


/* Alignment identity records. */
typedef struct {
  int	align;		/* alignment group */
  long	first;		/* first sequence index */
  long	second;		/* second sequence index */
  long	length;		/* match length */
} t_align;

/* Alignment records. */
typedef struct {
  int	total;		/* number of records */
  int	best_align;	/* best alignment number */
  int	best_score;	/* best alignment score */
  t_align  pair [ MAX_ALIGN_RECORDS ];	/* identity pairs */
} t_alignments;


/* Matching position indices. */
typedef struct {
  long	index [ MAX_POSITIONS ];
} t_positions;

/* Hash table. */
typedef struct {
  int           hash_size;		/* DNA bases hashed */
  t_positions	hash [ MAX_HASH ];	/* sequence hash positions */
  short		matches [ MAX_HASH ];	/* duplicate hash positions */
} t_hash;


/* DNA or protein sequence. */
typedef struct {
  long  total;			/* sequence length */
  char  base [ MAX_BASES ];	/* DNA sequence */
} t_seq;

/* DNA base hash table values. */
static  int  DNA_hash [ 26 ] = {

  /* A,  B,   C,  D,    E,    F,   G,  H,    I,    J, */
     0, 4096, 1, 4096, 4096, 4096, 2, 4096, 4096, 4096,

  /* K,    L,    M,    N,    O,    P,    Q,    S,   T,  U, */
    4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 3, 4096,

  /* V,    W,    X,    Y,    Z */
    4096, 4096, 4096, 4096, 4096
};


main ()
{
  printf ( "hello world.\n" );
}  /* main */


/* This function sets the alignment number and scores the best alignment. */
align_separate ( ident )
t_alignments  *ident;	/* identity records */
{
  int  align_no;	/* alignment number */
  int  end_1 = 0;	/* sequence 1 index */
  int  end_2 = 0;	/* sequence 2 index */
  int  ident_index = 0;	/* current identity record */
  int  length;		/* current record length */
  int  score = 0;	/* current score */
  int  skipped = 0;	/* number of skipped records */


  /* Set the alignment numbers. */
  while ( ident_index < (*ident).total )
  {
    if ( (*ident).pair [ ident_index ].align >= 0 )

      /* Check if part of current alignment. */
      if ( ( end_1 < (*ident).pair [ ident_index ].first ) &&
           ( end_2 < (*ident).pair [ ident_index ].second ) )
      {
        length = (*ident).pair [ ident_index ].length;
        score += length;
        end_1 = (*ident).pair [ ident_index ].first + length - 1;
        end_2 = (*ident).pair [ ident_index ].second + length - 1;
        (*ident).pair [ ident_index ].align = align_no;
      }
      else  skipped++;

    /* Check if end of table. */
    ident_index++;
    if ( ident_index == (*ident).total )
    {
      /* Check for best alignment. */
      if ( score > (*ident).best_score )
      {
        (*ident).best_score = score;
        (*ident).best_align = align_no;
      } /* if */

      /* Check for skipped records. */
      if ( skipped > 0 )
      {
        ident_index = score = end_1 = end_2 = skipped = 0;
        align_no++;
      }  /* if */
    }  /* if */
  }  /* while */
}  /* align_separate */

/* This function combines adjacent alignment records. */
align_combine ( ident, hash_size )
t_alignments  *ident;	/* identity records */
int           hash_size;	/* number of hash bases */
{
  int  ident_index;	/* current identity record */
  int  next_1;		/* next sequence 1 index */
  int  next_2;		/* next sequence 2 index */
  int  scan_index;	/* ident scanning index */


  /* Combine the alignment records. */
  for ( ident_index = 0; ident_index < (*ident).total; ident_index++ )

    /* Check for records to combine. */
    if ( (*ident).pair [ ident_index ].align >= 0 )
    {
      next_1 = (*ident).pair [ ident_index ].first + hash_size;
      next_2 = (*ident).pair [ ident_index ].second + hash_size;
      scan_index = ident_index + 1;

      while ( scan_index < (*ident).total )
      {
        /* Check for adjacent identities. */
        if ( ( next_1 == (*ident).pair [ scan_index ].first ) &&
             ( next_2 == (*ident).pair [ scan_index ].second ) )
        {
          /* Combine the records. */
          (*ident).pair [ ident_index ].length += hash_size;
          (*ident).pair [ scan_index ].align = -1;
          next_1 += hash_size;
          next_2 += hash_size;
        }  /* if */

        /* Check for an alignment gap. */
        if ( ( next_1 + hash_size < (*ident).pair [ scan_index ].first ) ||
             ( next_2 + hash_size < (*ident).pair [ scan_index ].second ) )
          scan_index = (*ident).total;
        else  scan_index++;
      }  /* while */
    }  /* if */
}  /* align_combine */


/* This function hashes DNA bases. */
int  base_hash ( seq, seq_index, hash_size )
t_seq  *seq;		/* DNA sequence */
long   seq_index;	/* first base of hash sequence */
int    hash_size;	/* number of DNA hashing bases */
{
  short  base_index;	/* bases index */
  int    hash = 0;	/* hash value */


  /* Hash the DNA bases. */
  for ( base_index = 0; base_index < hash_size; base_index++ )
    hash = hash * 4 + DNA_hash [ (*seq).base [ seq_index + base_index ] - 'A' ];

  return ( hash );
}  /* base_hash */


/* This function compares two sequences using hashing. */
hash_compare ( hash, seq_1, seq_2, ident )
t_hash  *hash;		/* sequence 1 hash table */
t_seq   *seq_1;		/* sequence 1 */
t_seq   *seq_2;		/* sequence 2 */
t_alignments  *ident;	/* identity positions */
{
  int   hash_i;		/* first hash */
  int   hash_j;		/* second hash */
  int   pos_i;          /* first hash position */
  int   pos_j;          /* second hash position */
  long  seq_index = 1;	/* sequence index */


  /* Initialization. */
  (*ident).total = 0;

  /* Traverse the second sequence. */
  hash_j = base_hash ( seq_2, seq_index, (*hash).hash_size );
  while ( seq_index < (*seq_2).total - ( (*hash).hash_size * 2 ) + 1 )
  {
    /* Hash the next set of bases. */
    hash_i = hash_j;
    hash_j = base_hash ( seq_2, seq_index + (*hash).hash_size, 
        (*hash).hash_size );
    pos_i = pos_j = 0;

    /* Check for pairwise mathches. */
    while ( pos_i < (*hash).matches [ hash_i ] )
    {
      while ( pos_j < (*hash).matches [ hash_j ] )
      {
        if ( (*hash).hash [ hash_i ].index [ pos_i ] + (*hash).hash_size ==
             (*hash).hash [ hash_j ].index [ pos_j ] )
        {
          /* Check for a full table. */
          if ( (*ident).total == MAX_ALIGN_RECORDS - 1 )
            printf ( "Alignments table filled.\n" );

          if ( (*ident).total < MAX_ALIGN_RECORDS )
          {
            (*ident).pair [ (*ident).total ].align = 0;
            (*ident).pair [ (*ident).total ].first = 
                (*hash).hash [ hash_i ].index [ pos_i ];
            (*ident).pair [ (*ident).total ].second = seq_index;
            (*ident).pair [ (*ident).total ].length = (*hash).hash_size * 2;
            (*ident).total++;
          }  /* if */

          hash_j++;
        }  /* if */
      }  /* while */

      hash_i++;
    }  /* while */

    seq_index += (*hash).hash_size;	/* ? by 1 or hash_size */
  }  /* while */
}  /* hash_compare */


/* This function hashes a sequence. */
hash_seq ( seq, hash, hash_size )
t_seq  *seq;		/* DNA sequence */
t_hash  *hash;		/* sequence hash table */
int	hash_size;	/* number of hash bases */
{
  int   hash_i;		/* hash index */
  long  seq_index;	/* sequence index */


  /* Initialize the hash table. */
  for ( hash_i = 0; hash_i < MAX_HASH; hash_i++ )
    (*hash).matches [ hash_i ] = 0;
  (*hash).hash_size = hash_size;

  /* Hash the sequence. */
  for ( seq_index = 1; seq_index <= (*seq).total - hash_size + 1; seq_index++ )
  {
    hash_i = base_hash ( seq, seq_index, (*hash).hash_size );

    if ( hash_i < MAX_HASH )

      /* Add the position to the hash table. */
      if ( (*hash).matches [ hash_i ] < MAX_POSITIONS )
      {
        (*hash).hash [ hash_i ].index [ (*hash).matches [ hash_i ] ] =
            seq_index;

        (*hash).matches [ hash_i ]++;
      }  /* if */
  }  /* for */
}  /* hash_seq */



