
#include <stdio.h>

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


#define  TRUE		1
#define  FALSE		0

#define  BLOCK_SIZE	60	/* output block size */

#define  MAX_ASCII      256     /* maximum ASCII characters */

#define  MAX_BASES      440000  /* maximum DNA sequence length */

#define  MAX_LINE	80012	/* maximum line length */

#define  MIN_SEQ	12	/* minimum sequence length */



/* DNA sequence. */
typedef  struct {
  long  total;			/* sequence length */
  char  name [ MAX_LINE ];	/* file name */
  char  desc [ MAX_LINE ];	/* description */
  char  base [ MAX_BASES ];	/* DNA bases */
} t_seq;


/* Text file. */
typedef struct {
  char  name [ MAX_LINE ];	/* file name */
  char  next;			/* current character */
  char  token [ MAX_LINE ];	/* current token */
  char  line  [ MAX_LINE ];	/* current line */
  int   line_index;		/* line index */
  FILE  *data;			/* data file */
  short eof;			/* end of file flag */
} t_text;


static  int  base_map [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/* 51  52  53  54  55  56  57  58  59  60  61  62  63  64 */
   64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,

/* A   B  C   D   E   F  G   H   I   J   K   L   M */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* N   O   P   Q   R   S  T  U   V   W   X   Y   Z */
  64, 64, 64, 64, 64, 64, 0, 0, 64, 64, 64, 64, 64,

  64, 64, 64, 64, 64, 64,

/* a   b  c   d   e   f  g   h   i   j   k   l   m */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* n   o   p   q   r   s  t  u   v   w   x   y   z */
  64, 64, 64, 64, 64, 64, 0, 0, 64, 64, 64, 64, 64
};


static  int  base_map_3 [ 123 ] = { 0,

/* 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/*26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 */
  70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,64,70,70,70,70,70,70,70,70,

/* 51  52  53  54  55  56  57  58  59  60  61  62  63  64 */
   64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,

/* A   B  C   D   E   F  G   H   I   J   K   L   M */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* N   O   P   Q   R   S  T  U   V   W   X   Y   Z */
  64, 64, 64, 64,  2, 64, 0, 0, 64, 64, 64,  0, 64,

  64, 64, 64, 64, 64, 64,

/* a   b  c   d   e   f  g   h   i   j   k   l   m */  
   2, 64, 1, 64, 64, 64, 3, 64, 64, 64, 64, 64, 64, 

/* n   o   p   q   r   s  t  u   v   w   x   y   z */
  64, 64, 64, 64,  2, 64, 0, 0, 64, 64, 64,  0, 64
};


/* Genetic code */
static  char  codon_map [ 64 ] = {
  'F', 'F', 'L', 'L',     'S', 'S', 'S', 'S',  
  'Y', 'Y', '*', '*',     'C', 'C', '*', 'W',

  'L', 'L', 'L', 'L',     'P', 'P', 'P', 'P',
  'H', 'H', 'Q', 'Q',     'R', 'R', 'R', 'R',

  'I', 'I', 'I', 'M',     'T', 'T', 'T', 'T',
  'N', 'N', 'K', 'K',     'S', 'S', 'R', 'R',

  'V', 'V', 'V', 'V',     'A', 'A', 'A', 'A',
  'D', 'D', 'E', 'E',     'G', 'G', 'G', 'G'  };


static  char  complement [ 123 ] = {

/*    1    2    3    4    5    6    7    8    9   10 */
' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 20 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 30 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 40 */
     ' ', ' ', ' ', ' ', '-', '.', ' ', ' ', ' ', ' ',  /* 50 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 60 */
     ' ', ' ', ' ', ' ',                                /* 64 */

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   'T', 'V', 'G', 'H', ' ', ' ', 'C', 'D', ' ', ' ', 'M', ' ', 'K',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'N', ' ', ' ', ' ', 'Y', 'S', 'A', 'A', 'B', 'W', 'N', 'R', ' ',

   ' ', ' ', ' ', ' ', ' ', ' ',

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   't', 'v', 'g', 'h', ' ', ' ', 'c', 'd', ' ', ' ', 'm', ' ', 'k',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'n', ' ', ' ', ' ', 'y', 's', 'a', 'a', 'b', 'w', 'n', 'r', ' '
};


static  int  dna_mask [ 123 ] = { 0,

/*    1    2    3    4    5    6    7    8    9   10 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 20 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 30 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 40 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 50 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 60 */
      0,   0,   0,   0,   

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1, 0xE,   4, 0xB,   0,   0,   2, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   8,   8,   7,   9, 0xF, 0xC,   0,

    0,   0,   0,   0,   0,   0,

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    1, 0xE,   4, 0xB,   0,   0,   2, 0xD,   0,   0, 0xA,   0,   5,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
  0xF,   0,   0,   0,   3,   6,   8,   8,   7,   9, 0xF, 0xC,   0 };




main ()
{
  long    end;			/* end of DNA sequence segment */
  t_text  in;			/* input DNA sequence file */
  int     index;		/* commands files index */
  t_text  library;		/* library of DNA sequences */
  t_text  names;		/* file of sequence names file */
  t_seq   seq;         		/* DNA sequence */
  long    start;		/* start of DNA sequence segment */
  int     version;     		/* DNA segment number */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();		/* file open function */


  printf ( "This is the Sequence files --> Fasta format program.\n\n" );

  /* Prompt for the input file name. */
  prompt_file ( &names, "What is the list of sequences file name?" );

  /* Open the DNA library output file. */
  d_strcpy ( library.name, names.name );
  library.name [ d_stridx ( library.name, "\n" ) ] = '\0';
  library.name [ d_stridx ( library.name, "." ) ] = '\0';
  d_strcat ( library.name, ".Library" );
  library.data = fopen ( library.name, "w" );

  /* Process the sequences. */
  while ( names.eof != TRUE )
  {
    /* Open the sequence output file. */
    d_strcpy ( in.name, names.line );
    in.name [ d_stridx ( in.name, "\n" ) ] = '\0';

    /* Open the DNA sequence file for reading. */
    open_text_file ( &in );

    /* Read in the DNA sequence. */
    read_AA_seq ( &in, &seq );
    fclose ( in.data );

    names.line [ d_stridx ( names.line, "\n" ) ] = '\0';
    d_strcpy ( seq.name, names.line );

    /* Drop short sequences. */
    if ( seq.total >= 50000 )
    {
      /* Write out the library name line. */
      fprintf ( library.data, ">%s %s\n", seq.name, seq.desc );

      /* Write out the library DNA sequence. */
      write_DNA_seq ( &seq, 0, seq.total - 1, &library );
    }  /* if */

    /* Get the next name. */
    get_line ( &names );
  }  /* while */

  fclose ( library.data );

  printf ( "\nEnd of DNA Sequences -> Fasta format program.\n" );
}  /* main */


/* This function writes out a Fasta format name line. */
write_fasta_name ( out, name, first, length, size )
t_text  *out;		/* output file */
char    name [];	/* sequence name */
int	first;		/* First base of sequence segment */
int	length;		/* Length of sequence */
int	size;		/* Size of sequence segment */
{
  /* Check for NULL file. */
  if ( (*out).data == NULL )  return;

  /* Pass useful information on the name line. */
  fprintf ( (*out).data, ">%s\n", name );
}  /* write_fasta_name */


/* This function writes out a e-mail message in a file. */
write_email ( database, method, seq, start, end, version, email, cmds, erase )
char	database [];	/* database name to search */
char	method [];	/* search method command string */
t_seq   *seq;		/* DNA sequence */
long    start;		/* start of DNA sequence segment */
long    end;		/* end of DNA sequence segment */
int	version;	/* current version number */
t_text  *email;		/* e-mail message file */
t_text	*cmds;		/* Unix e-mail message send commands file */
t_text  *erase;		/* Unix remove e-mail message file commands file */
{
  char    email_name [ MAX_LINE ];	/* e-mail message output file name */
  char    version_str [ MAX_LINE ];	/* ascii version number */
  FILE    *fopen ();			/* file open function */


  /* Set up the e-mail message sequence file name. */
  d_strcpy ( email_name, (*email).name );
  email_name [ d_stridx ( email_name, "-" ) ] = '\0'; 
  d_strcat ( email_name, "-" );

  /* Identify the search method and database. */
  version_str [ 0 ] = method [ 7 ];
  version_str [ 1 ] = database [ 0 ];
  version_str [ 2 ] = '\0';
  d_strcat ( email_name, version_str );

  itoa   ( version, version_str );
  if ( version < 100 )  d_strcat ( email_name, "0" );
  if ( version < 10 )  d_strcat ( email_name, "0" );
  d_strcat ( email_name, version_str );
  strlower ( email_name );

  /* Open the e-mail output file. */
  (*email).data = fopen ( email_name, "w" );
  if ( (*email).data == NULL )
  {
    printf ( "Could not open the file '%s' for writing.\n", email_name );
    return;
  }  /* if */

  /* Set up Unix e-mail send command. */
  if ( (*cmds).data != NULL )

    fprintf ( (*cmds).data, "mail Q@ORNL.GOV < %s\n", 
        email_name );

  /* Set up Unix erase file command. */
  if ( (*erase).data != NULL )

    fprintf ( (*erase).data, "rm %s\n", email_name );

  /* Identify the DNA type for target database. */
  if ( d_strcmp ( database, "SwissProt" ) == 0 )
    fprintf ( (*email).data, "TYPE DNA6\n" );
  else
    fprintf ( (*email).data, "TYPE DNA\n" ); 

  /* Identify the target database. */
  fprintf ( (*email).data, "TARGET %s\n", database );

  /* Identify the search method. */
  fprintf ( (*email).data, "%s\n", method );

  /* Pass useful information on the comment line. */
  fprintf ( (*email).data, "COMMENT %s ", email_name );
  fprintf ( (*email).data, " /First= %d, ", start + 1 );
  fprintf ( (*email).data, " /Length= %d, ", (*seq).total );
  fprintf ( (*email).data, " /Size= %d\n", end - start + 1 );

  /* Identify the following lines as DNA sequence. */
  fprintf ( (*email).data, "SEQ\n" );

  /* Write out the DNA sequence. */
  write_DNA_seq ( seq, start, end, email );

  /* Identify the end of the sequence/message. */
  fprintf ( (*email).data, "END\n" );

  /* Close the e-mail message output file. */
  fclose ( (*email).data );
}  /* write_email */


/* This function capitalizes a string. */
capitalize ( s )
char s [];		/* string */
{
  int  i = 0;

  while ( ( s [ i ] != '\0' ) && ( i < MAX_LINE ) )
  {
    if ( ( s [ i ] >= 'a' ) && ( s [ i ] <= 'z' ) )

      s [ i ] = s [ i ] - 'a' + 'A';

    i++;
  }  /* while */
}  /* capitalize */


/* This function gets the next text character. */
get_char ( text )
t_text  *text;		/* ASCII text file */
{
  if ( (*text).next == (char) EOF )  return; 
  (*text).next = ' ';

  /* Get the next sequence character. */
  if ( ( (*text).line [ (*text).line_index ] != '\n' ) &&
       ( (*text).line [ (*text).line_index ] != '\0' ) )

    (*text).next = (*text).line [ (*text).line_index++ ];

  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).next != (char) EOF ) )

    if ( ( (*text).next == '\n' ) || ( (*text).next == '\0' ) )
    {
      if ( (*text).eof == TRUE )  (*text).next = (char) EOF;
      else  get_line ( text );
    }
    else
      (*text).next = (*text).line [ (*text).line_index++ ];

}  /* get_char */


/* This function gets the next integer from the current line. */
int  get_int ( text )
t_text  *text;		/* ASCII text file */
{
  char  c;
  int   i, sign = 1;


  /* Get the first character. */
  get_char ( text );

  /* Check for a sign. */
  if ( ( (*text).next == '+' ) || ( (*text).next == '-' ) )
  {
    sign = ( (*text).next == '+' ) ? 1 : -1;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  /* Traverse the integer. */
  for ( i = 0; ( (*text).next >= '0' && (*text).next <= '9' );
      ( (*text).next = (*text).line [ (*text).line_index++ ] ) )

    i = i * 10 + (*text).next - '0';

  /* Set the integer sign. */
  i *= sign;

  return ( i );  /* return the integer */
}  /* get_int */


/* This function gets the next text line. */
get_line ( text )
t_text  *text;		/* ASCII text file */
{
  int  c = 0, i = 0;


  if ( (*text).next == (char) EOF )  return;

  (*text).line_index = 0;

  /* Get the text line. */
  while ( ( i < MAX_LINE ) && ( ( c = getc ( (*text).data ) ) != EOF ) &&
          ( c != '\n' ) )

    (*text).line [ i++ ] = c;

  /* Properly terminate the text line. */
  (*text).line [ i++ ] = '\n';
  (*text).line [ i   ] = '\0';

  /* Check for end of file. */
  if ( c == EOF )  (*text).eof = TRUE;

  /* Get the first character. */
  (*text).next = (*text).line [ (*text).line_index++ ];
}  /* get_line */


/* This function gets the next text token. */
get_token ( text )
t_text  *text;		/* ASCII text file */
{
  int  i = 0;


  /* skip white space */
  while ( ( ( (*text).next == ' '  ) ||
            ( (*text).next == '\n' ) ||
            ( (*text).next == '\t' ) ) && ( (*text).eof != TRUE ) )
  {
    if ( (*text).next == '\n' )  get_line ( text );
    else
      (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  d_strcpy ( (*text).token, "" );
  if ( (*text).eof == TRUE )  return;

  /* Copy the token. */
  while ( ( ( (*text).next >= '0' ) && ( (*text).next <= '9' ) ) ||
          ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
          ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ) 
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* while */

  /* Check for non-alphanumeric. */
  if ( i == 0 )
  {
    (*text).token [ i++ ] = (*text).next;

    (*text).next = (*text).line [ (*text).line_index++ ];
  }  /* if */

  (*text).token [ i ] = '\0';
}  /* get_token */


/* Convert n to characters in s. */
itoa (n, s)
char s[];
int n;
{
  int i, sign;

  if ((sign = n) < 0)  /* record sign */
    n = -n;

  /* Generate the digits in reverse order. */
  i = 0;
  do 
  {
    s[i++] = n % 10 + '0';	/* get next digit */
  }
  while ((n /= 10) > 0);	/* delete it */

  if (sign < 0)  s[i++] = '-';

  s[i] ='\0';

  reverse (s);
}  /* itoa */


/* Convert c to lower case; ASCII only */
lower (c)
int c;
{
  if (c >= 'A' && c <= 'Z')

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


/* This function capitalizes a string. */
strlower ( s )
char s [];		/* string */
{
  int  i = 0;


  while ( ( s [ i ] != '\0' ) && ( i < MAX_LINE ) )
  {
    s [ i ] = lower ( s [ i ] );
    i++;
  }  /* while */
}  /* strlower */


/* This function opens a file. */
open_text_file ( text )
t_text  *text;		/* ASCII text file */
{
  FILE  *fopen ();


  /* Initialization. */
  (*text).next = ' ';
  (*text).token [ 0 ] = '\0';
  (*text).line  [ 0 ] = '\0';
  (*text).eof         = TRUE;
  (*text).data        = NULL;

  /* Check for a valid file name. */
  if ( (*text).name [ 0 ] == '\0' )  return;

  /* Open the text file in read mode. */
  if ( ( (*text).data = fopen ( (*text).name, "r" ) ) == NULL )

    printf ( "Could not open '%s'\n", (*text).name );
  else
    (*text).eof = FALSE;

  /* Get the first text line. */
  get_line ( text );

}  /* open_text_file */


/* This function prompts for a file name. */
prompt_file ( text, prompt )
t_text  *text;		/* ASCII text file */
char    prompt [];	/* request prompt */
{
  (*text).name [ 0 ] = '\0';
  (*text).eof = TRUE;

  /* Prompt for a valid file name. */
  while ( ( (*text).eof == TRUE ) && ( d_strcmp ( (*text).name, "exit" ) != 0 ) )
  {
    printf ( "%s or 'exit' ", prompt );

    scanf ( "%s", (*text).name );

    if ( d_strcmp ( (*text).name, "exit" ) != 0 )

      open_text_file ( text );
  }  /* while */

  printf ( "\n" );
}  /* prompt_file */


/******************************************************************************/
/* This function reads in a FASTA sequence header line. */
read_header_line ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* AA sequence */
{
  int  index;
  char string [ MAX_LINE ];		/* text string */


  /* Read in the FASTA header line. */
  if ( (*text).line [ 0 ] == '>' )
  {
    d_strcpy ( string, &((*text).line [ 1 ]) );
    index = d_stridx ( string, " " );

    string [ index ] = '\0';		/* truncate to name */
    string [ d_stridx ( string, "\n" ) ] = '\0';
    d_strcpy ( (*seq).name, string );

    if ( (*text).line [ index + 1 ] == ' ' )
    {
      d_strcpy ( (*seq).desc, &((*text).line [ index+2 ]) );
      (*seq).desc [ d_stridx ( (*seq).desc, "\n" ) ] = '\0';
    }  /* if */
    else
      d_strcpy ( (*seq).desc, "" );

    /* Advance to the next line of text. */
    get_line ( text );
  }  /* if */
  else
  {
    printf ( "*Warning: FASTA header line expected! (%s)\n", (*text).line );
    return;
  }  /* else */
}  /* read_header_line */


/******************************************************************************/
/* This function reads in a AA sequence. */
read_AA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* AA sequence */
{
  int  end_of_seq = FALSE;		/* end of file flag */
  int  index;


  /* Read in the FASTA header line. */
  read_header_line ( text, seq );

  /* Read in the AA sequence. */
  (*seq).total = 0;

  do
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
    {
      (*seq).base [ (*seq).total++ ] = (*text).next;

      if ( (*seq).total == MAX_BASES - 1 )
        printf ( "Maximum number of sequence bases reached.\n" );
    }  /* if */

    get_char ( text );		/* get the next character */

    if ( ( (*text).line [ 0 ] == '>' ) ||
         ( (*text).next == (char) EOF ) )

      end_of_seq = TRUE;
  }  
  while ( ( end_of_seq != TRUE ) && ( (*seq).total < MAX_BASES ) );
}  /* read_AA_seq */


/******************************************************************************/
/* This function reads in a DNA sequence. */
read_DNA_seq ( text, seq )
t_text	*text;		/* ASCII text file */
t_seq	*seq;		/* DNA sequence */
{
  int  end_of_seq = FALSE;		/* end of file flag */


  /* Read in the FASTA header line. */
  read_header_line ( text, seq );

  /* Read in the DNA sequence. */
  (*seq).total = 0;
  get_line ( text );		/* get the first line of the sequence */

  do
  {
    if ( ( ( (*text).next >= 'a' ) && ( (*text).next <= 'z' ) ) ||
         ( ( (*text).next >= 'A' ) && ( (*text).next <= 'Z' ) ) ||
           ( (*text).next == '.' ) || ( (*text).next == '-' ) )
    {
      (*seq).base [ (*seq).total++ ] = (*text).next;

      if ( (*seq).total == MAX_BASES - 1 )
        printf ( "Maximum number of sequence bases reached.\n" );
    }  /* if */

    get_char ( text );		/* get the next character */

    if ( ( (*text).line [ 0 ] == '>' ) ||
         ( (*text).next == (char) EOF ) )

      end_of_seq = TRUE;
  }  
  while ( ( end_of_seq != TRUE ) && ( (*seq).total < MAX_BASES ) );
}  /* read_DNA_seq */


/* Reverse string s in place. */
reverse(s)
char s[];
{
  int  c, i, j;

  for (i = 0, j = d_strlen(s)-1; i < j; i++, j--)
  {
    c    = s[i];
    s[i] = s[j];
    s[j] = c;
  }  /* for */
}  /* reverse */


/* This function concatenates t to the end of s. */
d_strcat (s, t)
char  s[], t[];		/* s must be big enough */
{
  int  i = 0, j = 0;


  /* Find the end of s. */
  while ( s [ i ] != '\0' )  i++;

  /* Copy t. */
  while ( ( s [ i++ ] = t [ j++ ] ) != '\0' )  
    ;
}  /* d_strcat */


/* Return <0 if s<t, 0 if s==t, >0 if s>t */
d_strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* d_strcmp */


/* Copy t to s */
d_strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* d_strcpy */


/* This function returns the index of t in s or index of '\0'. */
d_stridx (s, t)
char s[], t[];
{
  int  i, j, k;

  for (i = 0; s [ i ] != '\0'; i++ )
  {
    for ( j = i, k = 0; t [ k ] != '\0' && s [ j ] == t [ k ]; j++, k++ )  ;

    if ( t [ k ] == '\0' )  return ( i );
  }  /* for */

  return ( i );
}  /* d_stridx */


/* Return length of string s */
d_strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* d_strlen */


/* This function writes out a DNA sequence block. */
write_DNA_seq ( seq, first, last, text )
t_seq	*seq;		/* DNA sequence */
long	first;		/* first base of block */
long	last;		/* last base of block */
t_text  *text;		/* output text file */
{
  int   flag;
  long  index;		/* sequence index */


  /* Check for NULL output file. */
  if ( (*text).data == NULL )  return;
  
  /* Print out the DNA block. */
  for ( index = first; index <= last; index++ )
  {
    fprintf ( (*text).data, "%c", (*seq).base [ index ] );
    flag = 1;

    if ( ( ( index - first + 1 ) % BLOCK_SIZE ) == 0 )
    {
      fprintf ( (*text).data, "\n" );

      flag = 0;
    }  /* if */
  }  /* for */

  if ( flag == 1 )

    fprintf ( (*text).data, "\n" );
}  /* write_DNA_seq */


/* This function reverses and complements a DNA sequence. */
reverse_seq ( seq, rev )
t_seq	*seq;		/* DNA sequence */
t_seq	*rev;		/* reversed & complemented DNA sequence */
{
  int  index;		/* base index */


  (*rev).total = (*seq).total;

  d_strcpy ( (*rev).name, (*seq).name );

  for ( index = 0; index < (*seq).total; index++ )

    (*rev).base [ (*rev).total - index - 1 ] = 
      complement [ (*seq).base [ index ] ];
}  /* reverse_seq */


