
#include <stdio.h>


main ( argc, argv )

int argc;	/* number of command line arguments */

char *argv [];	/* command line arguments */
{
  int i;

  printf ( "argc = %d (number of arguments) \n", argc );

  for ( i = 0; i < argc; i++ )

    printf ( "argv [ %d ] = '%s'\n", i, argv [ i ] );

}  /* main */

