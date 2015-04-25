

/******************************************************************************/
/* This function adds the computed length to the histogram. */
add_length ( begin, end, histogram, size )
long         begin;       /* beginning coordinates */
long         end;         /* ending coordinates */
t_histogram  *histogram;  /* histogram to add length to */
long         size;        /* histogram element size */
{
  long  index;   /* histogram index */
  long  length;  /* the length to add to the histogram */

  length = end - begin + 1;
  index = length / size;

  /* Check for length past the end of the histogram. */
  if ( index < MAX_HISTOGRAM - 1 )
    (*histogram).lengths [ index ]++;
  else
    (*histogram).lengths [ MAX_HISTOGRAM - 1 ]++;
}  /* add_length */
