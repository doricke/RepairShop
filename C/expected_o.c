
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

#define	 MAX_N  40	/* maximum value for n - palindrome half length */

main ( )
{
  int	n;
  double  A, C, G, T;

  A = 0.324;
  C = 0.194;
  G = 0.196;
  T = 0.286;

  for ( n = 1; n <= 20; n++ )
    expected ( n, A, C, G, T );

  for ( n = 1; n <= 20; n++ )
    direct ( n, A, C, G, T );

  printf ( "\nEnd of main program.\n" );

}  /* main */


expected ( n, A, C, G, T )
int	n;	/* length of half of the palindrome */
double  A;	/* fraction of bases which are A */
double  C;
double  G;
double  T;
{
  double  fac [ MAX_N ];	/* factorial array */
  double  a [ MAX_N ];	/* a [ i ] = A ^ i */
  double  c [ MAX_N ];	/* c [ i ] = C ^ i */
  double  g [ MAX_N ];  /* g [ i ] = G ^ i */
  double  t [ MAX_N ];  /* t [ i ] = T ^ i */

  int	i;	/* array index */
  int	p, q, r, s;
  double  sum;	/* running sum of the expected */

  /* initialize the arrays */
  fac [ 0 ] = 1.0;
  a [ 0 ] = 1.0;  c [ 0 ] = 1.0;  g [ 0 ] = 1.0;  t [ 0 ] = 1.0;
  for ( i = 1; i <= n; i++ )
  {
    fac [ i ] = i * fac [ i - 1 ];
    a [ i ] = a [ i - 1 ] * A;
    c [ i ] = c [ i - 1 ] * C;
    g [ i ] = g [ i - 1 ] * G;
    t [ i ] = t [ i - 1 ] * T;
  }  /* for */

  /* compute the expected */
  sum = 0.0;
  for ( p = 0; p <= n; p++ )
    for ( q = 0; q <= n; q++ )
      for ( r = 0; r <= n; r++ )
	for ( s = 0; s <= n; s++ )
	  if ( p + q + r + s == n )
      {
	sum = sum + ( fac [ n ] * a [ p ] * c [ q ] * g [ r ] * t [ s ]
	  * a [ s ] * c [ r ] * g [ q ] * t [ p ] )
	  / ( fac [ p ] * fac [ q ] * fac [ r ] * fac [ s ] );

/*  printf ( "fac [ n ] = %7.3e", fac [ n ] );
  printf ( ", a [ p ] = %7.3f", a [ p ] );
  printf ( ", c [ q ] = %7.3f", c [ q ] );
  printf ( ", g [ r ] = %7.3f", g [ q ] );
  printf ( ", t [ s ] = %7.3f\n", t [ s ] );

  printf ( "  a [ s ] = %7.3f", a [ s ] );
  printf ( ", c [ r ] = %7.3f", c [ r ] );
  printf ( ", g [ q ] = %7.3f", g [ q ] );
  printf ( ", t [ p ] = %7.3f\n", t [ p ] );

  printf ( "fac [ p ] = %7.1f", fac [ p ] );
  printf ( ", fac [ q ] = %7.1f", fac [ q ] );
  printf ( ", fac [ r ] = %7.1f", fac [ r ] );
  printf ( ", fac [ s ] = %7.1f\n", fac [ s ] );

  printf ( "p = %d", p );
  printf ( ", q = %d", q );
  printf ( ", r = %d", r );
  printf ( ", s = %d", s );
  printf ( ", sum = %e\n", sum ); */

      }  /* for r = */

  printf ( "For n = %2d, sum = %8.4e, expected = %8.4e\n", n, sum
    , sum * (38059.0 - 2.0 * n + 1) );

}  /* expected */


direct ( n, A, C, G, T )
int	n;	/* length of half of the palindrome */
double  A;	/* fraction of bases which are A */
double  C;
double  G;
double  T;
{
  double  fac [ MAX_N ];	/* factorial array */
  double  a [ MAX_N ];	/* a [ i ] = A ^ i */
  double  c [ MAX_N ];	/* c [ i ] = C ^ i */
  double  g [ MAX_N ];  /* g [ i ] = G ^ i */
  double  t [ MAX_N ];  /* t [ i ] = T ^ i */

  int	i;	/* array index */
  int	p, q, r, s;
  double  sum;	/* running sum of the expected */

  /* initialize the arrays */
  fac [ 0 ] = 1.0;
  a [ 0 ] = 1.0;  c [ 0 ] = 1.0;  g [ 0 ] = 1.0;  t [ 0 ] = 1.0;
  for ( i = 1; i <= n; i++ )
  {
    fac [ i ] = i * fac [ i - 1 ];
    a [ i ] = a [ i - 1 ] * A;
    c [ i ] = c [ i - 1 ] * C;
    g [ i ] = g [ i - 1 ] * G;
    t [ i ] = t [ i - 1 ] * T;
  }  /* for */

  /* compute the expected */
  sum = 0.0;
  for ( p = 0; p <= n; p++ )
    for ( q = 0; q <= n; q++ )
      for ( r = 0; r <= n; r++ )
	for ( s = 0; s <= n; s++ )
	  if ( p + q + r + s == n )
      {
	sum = sum + ( fac [ n ] * a [ p ] * c [ q ] * g [ r ] * t [ s ]
	  * a [ p ] * c [ q ] * g [ r ] * t [ s ] )
	  / ( fac [ p ] * fac [ q ] * fac [ r ] * fac [ s ] );

      }  /* for r = */

  printf ( "For n = %2d, sum = %8.4e, expected = %8.4e\n", n, sum
    , sum * (38059.0 - 2.0 * n + 1.0) );

}  /* direct */
