

/******************************************************************************/
main ( argc, argv )
int  argc;		/* number of command line arguments */
char *argv [];		/* vector of command line arguments */
{
  t_text  anno;


  /* Check if command line argument specified. */
  if ( argc <= 1 )

    /* Prompt for the input file name. */
    prompt_file ( &anno, "What is the name of the annotation file?" );

  else
  {
    strcpy ( anno.name, argv [ 1 ] );
    open_text_file ( &anno );
  }  /* else */

  /* Close the commands file. */
  fclose ( anno.data );

  printf ( "\nEnd of program.\n" );
}  /* main */


