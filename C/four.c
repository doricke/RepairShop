
#include <stdio.h>

main ()
{
  long  count;
  long  sum = 1;

  for ( count = 0; count <= 15; count++ )
  {
    printf ( "4^%2d = %d\n", count, sum );

    sum *= 4;
  }  /* for */

  printf ( "End of program.\n" );
}  /* main */
