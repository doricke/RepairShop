
#define	GROUP_INDEX		2	/* index to file group code */


/******************************************************************************/
/* This function processes a list of locus names. */
get_process_files ( file_of_names, suffix )
FILE	*file_of_names;		/* list of locus names to process */
char	suffix [];		/* pattern output suffix */
{
  char	current_locus [ MAX_LINE ];	/* current locus name */
  FILE	*infile;			/* sequence input file */
	/* genbank input file name template */
  char	in_file_name [ MAX_LINE ] = "GBXXX.SEQ";
  FILE  *fopen ();                      /* file open routine */
  int	index;				/* composition index */
  char	line [ MAX_LINE ];		/* current input line */
  int	line_index;			/* current line index */
  FILE	*outfile;			/* sequence output file */
  char	out_file_name [ MAX_LINE ];	/* output file name */
  char	previous_group [ MAX_LINE ];	/* previous file group identifier */
  char	previous_locus [ MAX_LINE ];	/* previous locus name */
  int	stat;				/* status of current line */ int	status = S_NORMAL;		/* function return status */ char	token [ MAX_LINE ];		/* current token */ 

  current_locus [ 0 ] = '\0';
  previous_group [ 0 ] = '\0';
  previous_locus [ 0 ] = '\0';
  infile = NULL;

  /* Process the file of locus names. */
  while ( status != S_EOF )
  {
    /* Get the next line from the file of locus names. */
    status = get_line ( file_of_names, line );
    line_index = 0;
    while ( line [ line_index ] == ' ' )  line_index++;

    if ( blank_line ( &(line [ line_index ]) ) != 0 )
    {
      /* Get the locus name. */
      strcpy ( previous_locus, current_locus );
      stat = get_token ( line, &line_index, current_locus );

      /* Check that the next token is a period. */
      if ( line [ line_index ] != '.' )
	printf ( "Period expected after locus name on '%s'.\n", line );
      else
      {
	line_index++;	/* advance past the period */

	if ( stat == S_NORMAL )
	{
	  /* Set the locus name as the first part of the output file name. */
	  strcpy ( out_file_name, current_locus );

	  /* Get the sequence entry file group identifier. */
	  stat = get_token ( line, &line_index, token );
	}  /* if */

	if ( stat != S_NORMAL )
	  printf ( "Error %d on locus line '%s'.\n", stat, line );
	else
	{
	  /* Check if the group identifier is different from the previous. */
	  if ( ( previous_group [ 0 ] != '\0' ) &&
	       ( strcmp ( previous_group, token ) != 0 ) )
	    /* Close the old input file. */
	    fclose ( infile );

	  strcpy ( previous_group, token );	/* save the current group */

	  /* Set the sequence entry file group identifier into the input file name. */
	  previous_group [ 0 ] = in_file_name [ GROUP_INDEX ] = token [ 0 ];
	  previous_group [ 1 ] = in_file_name [ GROUP_INDEX + 1 ] = token [ 1 ];
	  previous_group [ 2 ] = in_file_name [ GROUP_INDEX + 2 ] = token [ 2 ];
	  previous_group [ 3 ] = '\0';

	  /* Open the sequence entry file. */
	  printf ( "Processing '%s' for ", in_file_name );

	  if ( ( previous_locus [ 0 ] == '\0' ) ||
	       ( strcmp ( previous_locus, out_file_name ) >= 0 ) )
	  {
	    printf ( "strcmp '%s' to '%s' yields '%d'.\n",
		previous_locus, out_file_name,
		strcmp ( previous_locus, out_file_name ) );

	    if ( infile != NULL )
	      fclose ( infile );  /* close the genbank data file */

	    stat = rom_open ( in_file_name, &infile );


	    if ( stat == S_NORMAL )
	      process_header ( infile );	/* skip the file header */
	  }
	  if ( stat != S_NORMAL )
	    printf ( "Error %d opening input file '%s'.\n", stat,
		in_file_name );
	  else
	  {
	    printf ( "Getting '%s' from '%s'.\n", current_locus,
	      in_file_name );

	    /* Add the suffix to the output file name. */
	    concatenate ( out_file_name, "." );
	    concatenate ( out_file_name, suffix );

	    printf ( "'%s'.\n", out_file_name );

	    /* Open the output file for writing. */
	    outfile = fopen ( out_file_name, "w" );

	    if ( outfile == NULL )
	      printf ( "Could not open '%s' as the output file.\n",
		  out_file_name );
	  }  /* else */

	  if ( ( stat == S_NORMAL ) && ( outfile != NULL ) )
	  {
	    get_scan_file ( current_locus, infile, outfile );
	    fclose ( outfile );
	    printf ( "Finished '%s'.\n",
	      current_locus );
	  }  /* if */
	}  /* else */
      }  /* else */
    }  /* if */
  }  /* while */

  fclose ( infile );	/* close the genbank data file */
}  /* get_process_files */

