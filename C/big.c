#include <stdio.h>

#define  WORD_SIZE	12	/* base pairs in word */
#define  WORD_TOTAL	16777216	/* 4^12 */

main (){  
  long  count;  
  long  sum = 1;  

  for ( count = 0; count <= 15; count++ )  
  {    
    printf ( "4^%2d = %d\n", count, sum );    
    sum *= 4;  
  }  /* for */  

  printf ( "End of program.\n" );
}  /* main */
