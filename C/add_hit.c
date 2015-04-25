

/******************************************************************************/
/* This function adds a homology hit to the homology hits table. */
add_hit ( hit, hits )
t_homology  *hit;		/* homology hit to add */
t_hits      *hits;		/* homology hits table */
{
  int         done;		/* loop termination flag */
  int         hit_index;	/* hits table index */
  int	      ignore_hit = FALSE;
  t_homology  new_hit;		/* merged homology hit */


  /* Check table limit. */
  if ( (*hits).total + 1 >= MAX_FEATURES )  return;

  /* Check if current hit is continuation of previous hit. */
  if ( merge_hit ( hit, hits, &new_hit ) != TRUE )
  {
    /* Check for overlapping hits. */
    overlap ( hit, hits, &ignore_hit, FALSE );

    if ( ignore_hit != TRUE )
    {
      /* Shift all of the homology hits up one position. */
      hit_index = (*hits).total - 1;
      done = FALSE;
      if ( (*hit).start >= (*hits).hit [ hit_index ].start ) done = TRUE;
      while ( ( hit_index >= 0 ) && ( done != TRUE ) )
      {
        copy_homology ( &((*hits).hit [ hit_index     ]), 
                        &((*hits).hit [ hit_index + 1 ]) );

        hit_index--;
        if ( hit_index >= 0 )
          if ( (*hit).start >= (*hits).hit [ hit_index ].start ) done = TRUE;
      }  /* while */
      if ( ( (*hit).start == (*hits).hit [ hit_index ].start ) &&
           ( (*hit).end   <  (*hits).hit [ hit_index ].end   ) )  done = FALSE;
      while ( ( hit_index >= 0 ) && ( done != TRUE ) )
      {
        copy_homology ( &((*hits).hit [ hit_index     ]), 
                        &((*hits).hit [ hit_index + 1 ]) );

        hit_index--;
        if ( hit_index >= 0 )
          if ( ( (*hit).start >  (*hits).hit [ hit_index ].start ) ||
               ( (*hit).end   >= (*hits).hit [ hit_index ].end   ) )
             done = TRUE;
      }  /* while */
      if ( hit_index < 0 )  hit_index = 0;

      /* Add the homology hit to the homology hits table. */
      copy_homology ( hit, &((*hits).hit [ hit_index ]) );

      (*hits).total++;
    }  /* if */
    else  if ( ignore_hit == TRUE )  return;
  }  /* if */
}  /* add_hit */


