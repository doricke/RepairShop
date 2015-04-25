
#include <stdio.h>
#include <math.h>

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
#define AMINO_ACIDS     147     /* Number of amino acids in p53 */
#define MAX_HITS       2000     /* Maximum number of mutations */
#define F_SPACERS      0.35     /* Fraction of spacers in p53 */

main ( )
{
  double dna_bases = AMINO_ACIDS * 3.0;  /* number of DNA base pairs */
  double d_hits;                         /* double of hits */
  int    hits;                           /* original number of mutations */
  double not_hit;                        /* all but one base is not hit */
  double pow ();                         /* power function */
  double q;                              /* base probability of not being hit */
  double unhit;                          /* number of unhit amino acids */
  double fraction_hit;                   /* fraction of amino acids hit */
  double hit_aa;                         /* number of mutated amino acids */
  double hit_spacers;                    /* number of mutated spacer residues */
  double fraction_spacers_hit;           /* faction of spacers hit */
  double percent_hit;                    /* percentage of residues hit (spacers) */


  /* Only one DNA base is being mutated at a time. */
  not_hit = ( dna_bases - 1.0 ) / dna_bases;

  printf ( "\"Hits, Unhit, Hit, f, Predicted,  %%\"\n" );

  /* Generate mutations. */
  for ( hits = 10; hits <= MAX_HITS; hits += 10 )
  {
    d_hits = hits * 1.0;

    /* q is the probability that a DNA base is unhit after N hits. */
    q = pow ( not_hit, d_hits );

    /* Number of unhit amino acids remaining. */
    unhit = AMINO_ACIDS * ( ( q * q * q + q * q ) / 2.0 );

    /* Number of mutated amino acids. */
    hit_aa = AMINO_ACIDS * 1.0 - unhit;

    /* Total fraction of amino acids hit. */
    fraction_hit = hit_aa / ( AMINO_ACIDS * 1.0 );

    /* Correct for mutations at critical residues. */
    hit_spacers = (AMINO_ACIDS * F_SPACERS) * (1.0 -
        ( ( q * q * q + q * q ) / 2.0 ) );

    fraction_spacers_hit = hit_spacers / 
        ( AMINO_ACIDS * F_SPACERS );

    /* Percentage of residues hit. */
    percent_hit = 100.0 * fraction_spacers_hit; 

    printf ( "%3d, %4.0f, %4.0f, %5.2f, ",
        hits, unhit, hit_aa, fraction_hit );

    printf ( "%4.0f, %5.2f, %5.1f%%\n",
        hit_spacers, fraction_spacers_hit, percent_hit );
  }  /* for */

  printf ("\nEnd main program.\n");
}  /* main */
