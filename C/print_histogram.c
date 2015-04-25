

/******************************************************************************/
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
