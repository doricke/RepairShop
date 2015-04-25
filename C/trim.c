
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

/******************************************************************************/
/* This function trims off the low homology ends on DNA hits. */
trim_DNA_ends ( hit )
t_homology  *hit;		/* homology hit to trim */
{
  int  base;			/* small region index */
  int  hit_count = 0;		/* number of identity bases in small region */
  int  index;			/* end index */
  int  in_gap;			/* flag indicating if within a gap */


  /* Check for small alignment. */
  if ( (*hit).identities < MIN_DNA_IDENTITIES )  return;

  /* Trim the beginning of the homology hit. */
  index = (*hit).start;
  while ( ( hit_count < 14 ) && ( index < (*hit).end ) )
  {
    /* Add up the identity amino acids in an amino acids window. */
    for ( base = index, hit_count = 0; (base < (*hit).end) && (base < index + 21); base++ )
    {
      if ( (*hit).dashes.base [ base ] == '-' )  hit_count--;
      else  if ( (*hit).ident.base [ base ] != ' ' )  hit_count++;
    }  /* for */

    /* Check for low homology. */
    if ( ( hit_count < 14 ) && ( index < (*hit).end ) )
    {
      /* Check if erasing an identity character. */
      if ( (*hit).ident.base [ index ] != ' ' )
      {
        (*hit).identities--;
        (*hit).ident.base [ index ] = ' ';
      }  /* if */

      index = (*hit).start++;
      (*hit).hit_start++;
    }  /* if */
  }  /* while */

  /* Trim beginning blanks. */
  while ( ( index < (*hit).end ) && ( (*hit).ident.base [ index ] == ' ' ) )

    index = (*hit).start++;

  /* Trim the end of the homology hit. */
  index = (*hit).end;

  /* Trim trailing blanks. */
  while ( ( index > (*hit).start ) && ( (*hit).ident.base [ index ] == ' ' ) )
  {
    (*hit).end--;
    index = (*hit).end;
  }  /* while */

  hit_count = 0;
  while ( ( hit_count < 14 ) && ( index > (*hit).start ) )
  {
    /* Add up the identity amino acids in an amino acids window. */
    for ( base = index, hit_count = 0; (base > (*hit).start) && (base > index - 21); base-- )
    {
      if ( (*hit).dashes.base [ base ] == '-' )  hit_count--;
      else  if ( (*hit).ident.base [ base ] != ' ' )  hit_count++;
    }  /* for */

    /* Check for low homology. */
    if ( ( hit_count < 14 ) && ( index > (*hit).start ) )
    {
      /* Check if erasing an identity character. */
      if ( (*hit).ident.base [ index ] != ' ' )
      {
        (*hit).identities--;
        (*hit).ident.base [ index ] = ' ';
      }  /* if */

      index = (*hit).end--;
      (*hit).hit_end--;
    }  /* if */
  }  /* while */

  /* Trim trailing blanks. */
  while ( ( index > (*hit).start ) && ( (*hit).ident.base [ index ] == ' ' ) )

    index = (*hit).end--;

  /* Count the gaps in the highest homology region. */
  for ( base = (*hit).start, in_gap = FALSE; base <= (*hit).end; base++ )

    if ( (*hit).dashes.base [ base ] == ' ' )  in_gap = FALSE;
    else
      if ( in_gap == FALSE )
      {
        in_gap = TRUE;
        (*hit).gaps++;
      }  /* if */
}  /* trim_DNA_ends */


/******************************************************************************/


