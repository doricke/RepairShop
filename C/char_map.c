#include <stdio.h>

main ()
{  
  int i;  
  for ( i = 'A'; i <= 'z'; i++ )  
  {    
    if ( ( ( i >= 'A' ) && ( i <= 'Z' ) ) || ( ( i >= 'a' ) && ( i <= 'z' ) ) )      
      printf ( "%d - '%c',  ", i, i );    

    if ( ( i % 5 ) == 0 )  printf ( "\n" );  
  }  /* for */  

  printf ( "\n\n" );  
  printf ( "%d - '%c',  %d - '%c'\n", '.', '.', '-', '-' );
}  /* main */
