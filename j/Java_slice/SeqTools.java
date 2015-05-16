

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2002 Darrell O. Ricke, Ph.D., Paragon Software
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
public class SeqTools extends Object
{


/******************************************************************************/

private static int MAX_CODON = 64;		// Maximum codon index

private static int [] base_map = { 0,

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


private static  int [] base_map_3 = { 0,

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
private static  char [] codon_map = {
  'F', 'F', 'L', 'L',     'S', 'S', 'S', 'S',  
  'Y', 'Y', '*', '*',     'C', 'C', '*', 'W',

  'L', 'L', 'L', 'L',     'P', 'P', 'P', 'P',
  'H', 'H', 'Q', 'Q',     'R', 'R', 'R', 'R',

  'I', 'I', 'I', 'M',     'T', 'T', 'T', 'T',
  'N', 'N', 'K', 'K',     'S', 'S', 'R', 'R',

  'V', 'V', 'V', 'V',     'A', 'A', 'A', 'A',
  'D', 'D', 'E', 'E',     'G', 'G', 'G', 'G'  };


/* Genetic code */
public static  String [] codons = {
  "Phe", "Phe", "Leu", "Leu",     "Ser", "Ser", "Ser", "Ser",  
  "Tyr", "Tyr", "Ter", "Ter",     "Cys", "Cys", "Ter", "Trp",

  "Leu", "Leu", "Leu", "Leu",     "Pro", "Pro", "Pro", "Pro",
  "His", "His", "Gln", "Gln",     "Arg", "Arg", "Arg", "Arg",

  "Ile", "Ile", "Ile", "Met",     "Thr", "Thr", "Thr", "Thr",
  "Asn", "Asn", "Lys", "Lys",     "Ser", "Ser", "Arg", "Arg",

  "Val", "Val", "Val", "Val",     "Ala", "Ala", "Ala", "Ala",
  "Asp", "Asp", "Glu", "Glu",     "Gly", "Gly", "Gly", "Gly"  };


/* Genetic code & encoding triplet codon */
public static  String [] triplets = {
  "Phe:TTT", "Phe:TTC", "Leu:TTA", "Leu:TTG",     
  "Ser:TCT", "Ser:TCC", "Ser:TCA", "Ser:TCG",  
  "Tyr:TAT", "Tyr:TAC", "Ter:TAA", "Ter:TAG",     
  "Cys:TGT", "Cys:TGC", "Ter:TGA", "Trp:TGG",

  "Leu:CTT", "Leu:CTC", "Leu:CTA", "Leu:CTG",     
  "Pro:CCT", "Pro:CCC", "Pro:CCA", "Pro:CCG",
  "His:CAT", "His:CAC", "Gln:CAA", "Gln:CAG",     
  "Arg:CGT", "Arg:CGC", "Arg:CGA", "Arg:CGG",

  "Ile:ATT", "Ile:ATC", "Ile:ATA", "Met:ATG",     
  "Thr:ACT", "Thr:ACC", "Thr:ACA", "Thr:ACG",
  "Asn:AAT", "Asn:AAC", "Lys:AAA", "Lys:AAG",     
  "Ser:AGT", "Ser:AGC", "Arg:AGA", "Arg:AGG",

  "Val:GTT", "Val:GTC", "Val:GTA", "Val:GTG",     
  "Ala:GCT", "Ala:GCC", "Ala:GCA", "Ala:GCG",
  "Asp:GAT", "Asp:GAC", "Glu:GAA", "Glu:GAG",     
  "Gly:GGT", "Gly:GGC", "Gly:GGA", "Gly:GGG"  };


// Map from single letter amino acid code to 3 letter code.
private static String [] map1to3 = {
  //  A      B      C      D      E      F      G      H      I
    "Ala", "Err", "Cys", "Asp", "Glu", "Phe", "Gly", "His", "Ile",
  //  J      K      L      M      N      O      P      Q      R
    "Err", "Lys", "Leu", "Met", "Asn", "Err", "Pro", "Gln", "Arg",
  //  S      T      U      V      W      X      Y      Z
    "Ser", "Thr", "Err", "Val", "Trp", "Xxx", "Tyr", "Err" };


private static  char [] complement = {

/*    1    2    3    4    5    6    7    8    9   10 */
' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 20 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 30 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',  /* 40 */
     ' ', ' ', ' ', ' ', '-', '.', ' ', ' ', ' ', ' ',  /* 50 */
     ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>',  /* 60 */
     ' ', '<', ' ', ' ',                                /* 64 */

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   'T', 'V', 'G', 'H', ' ', ' ', 'C', 'D', ' ', ' ', 'M', ' ', 'K',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'N', ' ', ' ', ' ', 'Y', 'S', 'A', 'A', 'B', 'W', 'N', 'R', ' ',

   ' ', ' ', ' ', '^', ' ', ' ',

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
   't', 'v', 'g', 'h', ' ', ' ', 'c', 'd', ' ', ' ', 'm', ' ', 'k',

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
   'n', ' ', ' ', ' ', 'y', 's', 'a', 'a', 'b', 'w', 'n', 'r', ' ',

   ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
};


private static  int [] dna_mask = { 0,

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


private static  int [] mask_score = { 0,

/*    1    2    3    4    5    6    7    8    9   10 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 20 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 30 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 40 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 50 */
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  /* 60 */
      0,   0,   0,   0,   

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    4,   1,   4,   1,   0,   0,   4,   1,   0,   0,   2,   0,   2,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
    0,   0,   0,   0,   2,   2,   4,   4,   1,   2,   0,   2,   0,

    0,   0,   0,   0,   0,   0,

/*  a    b    c    d    e    f    g    h    i    j    k    l    m */
    4,   1,   4,   1,   0,   0,   4,   1,   0,   0,   2,   0,   2,

/*  n    o    p    q    r    s    t    u    v    w    x    y    z */
    0,   0,   0,   0,   2,   2,   4,   4,   1,   2,   0,   2,   0 };



/******************************************************************************/
public SeqTools ()
{
}  // constructor SeqTools


/******************************************************************************/
public static String cleanSequence ( String sequence )
{
  StringBuffer clean = new StringBuffer ( sequence );

  // Check for no sequence.
  if ( sequence.length () <= 0 )  return "";

  for ( int i = sequence.length () - 1; i >= 0; i-- )
  {
    if ( ( clean.charAt ( i ) == '|' ) ||
         ( clean.charAt ( i ) == '<' ) ||
         ( clean.charAt ( i ) == '>' ) ||
         ( clean.charAt ( i ) == '.' ) ||
         ( clean.charAt ( i ) == '-' ) )

      clean.deleteCharAt ( i );
  }  // for

  return clean.toString ();
}  // method cleanSequence


/******************************************************************************/
public static int computeStartCodon ( String mRNA )
{
  // Check if the mRNA sequence is too short.
  if ( mRNA.length () < 3 )  return 0;

  // Check for the start codon.
  if ( mapCodon ( mRNA.substring ( 0, 3 ) ) == 'M' )  return 1;

  return 0;		// No ATG found.
}  // method computeStartCodon


/******************************************************************************/
// This method assumes the end of the mRNA is the stop codon.
public static int computeStopCodon ( String mRNA )
{
  // Check if the mRNA sequence is too short.
  if ( mRNA.length () < 3 )  return 0;

  // Find the last base.
  int last_base = mRNA.length () - 1;

  // Check for a 3' incomplete marker.
  if ( mRNA.charAt ( last_base ) == '>' )  last_base--;

  // Compute the first base of the stop codon.
  int first_base = last_base - 2;

  // Check for a valid first base of the stop codon.
  if ( first_base < 0 )  return 0;

  // Check for the stop codon.
  if ( mapCodon ( mRNA.substring ( first_base ) ) == '*' )

    return first_base + 1;

  return 0;		// No TAA, TAG, or TGA found.
}  // method computeStopCodon


/******************************************************************************/
public static int findAcceptor ( String genomic, int center )
{
  int acceptor = findAcceptor ( genomic, center, 15 );

  if ( acceptor == -1 )  acceptor = findAcceptor ( genomic, center, 25 );

  if ( acceptor == -1 )  acceptor = findAcceptor ( genomic, center, 50 );

  if ( acceptor >= 0 )  return acceptor;

  return center;
}  // method findAcceptor


/******************************************************************************/
// Compute the Acceptor splice site.
public static int findAcceptor
    ( String genomic				// Genomic sequence
    , int  center				// approximate location of acceptor site
    , int  window				// search window size
    )
{
  int acceptor = center;			// next length of current exon
  int score = 9;				// Acceptor splice site score

  if ( genomic == null )  return acceptor;
  if ( genomic.length () <= 0 )  return acceptor;

  // Compure the search window limits.
  int start = center - window;
  int end   = center + window;
  if ( end >= genomic.length () )  end = genomic.length () - 1;

  // Evaluate alternatives in the 5' direction.
  for ( int i = end; i <= start; i-- )
  {
    // Score the candidate exon acceptor site.
    int new_score = scoreAcceptor ( genomic, i );

    if ( new_score >= score )
    {
      acceptor = i;
      score = new_score;
    }  // if
    else
      if ( new_score == score )
      {
        int delta1 = center - i;
        int delta2 = center - acceptor;
        if ( delta1 < 0 )  delta1 = - delta1;
        if ( delta2 < 0 )  delta2 = - delta2;
        if ( delta1 < delta2 )
        {
          acceptor = i;
          score = new_score;
        }  // if
      }  // if
  }  // for

  return acceptor;
}  // method findAcceptor


/******************************************************************************/
// Compute the Acceptor splice site on the complement strand.
public static long findAcceptorC
    ( String genomic				// Genomic sequence
    , long   center				// approximate location of acceptor site
    )
{
  long acceptor = -1L;				// next length of current exon
  int score = 0;				// Acceptor splice site score
  int window = 14;				// search window size

  if ( genomic == null )  return acceptor;
  if ( genomic.length () <= 0 )  return acceptor;

  // Compure the search window limits.
  long start = center - window;
  long end   = center + window;
  if ( start < 0L )  start = 0L;
  if ( end >= genomic.length () )  end = genomic.length () - 1L;

  // Evaluate alternatives in the 5' direction.
  for ( long i = start; i <= end; i++ )
  {
    // Score the candidate exon acceptor site.
    int new_score = scoreAcceptorC ( genomic, (int) i );

    if ( new_score > score )
    {
      acceptor = i;
      score = new_score;
    }  // if
    else
      if ( new_score == score )
      {
        long delta1 = center - i;
        long delta2 = center - acceptor;
        if ( delta1 < 0 )  delta1 = - delta1;
        if ( delta2 < 0 )  delta2 = - delta2;
        if ( delta1 < delta2 )
        {
          acceptor = i;
          score = new_score;
        }  // if
      }  // if
  }  // for

  return acceptor;
}  // method findAcceptorC


/******************************************************************************/
public static int findDonor ( String genomic, int center )
{
  int donor = findDonor ( genomic, center, 15 );

  if ( donor == -1 )  donor = findDonor ( genomic, center, 25 );

  if ( donor == -1 )  donor = findDonor ( genomic, center, 50 );

  if ( donor >= 0 )  return donor;

  return center;
}  // method findDonor


/******************************************************************************/
// Compute the Donor splice site.
public static int findDonor
    ( String  genomic				// Genomic sequence
    , int     center				// approximate location of donor site
    , int     window				// window to examine donor site
    )
{
  int donor = -1;				// next length of current exon
  int score = 7;				// Donor splice site score

  if ( genomic == null )  return donor;
  if ( genomic.length () <= 0 )  return donor;

  // Compure the search window limits.
  int start = center - window;
  int end   = center + window;
  if ( start < 0 )  start = 0;
  if ( end > genomic.length () )  end = genomic.length () - 1;

  // Evaluate alternatives in the 5' direction.
  for ( int i = start; i <= end; i++ )
  {
    // Score the candidate exon donor site.
    int new_score = scoreDonor ( genomic, i );

    if ( new_score >= score )
    {
      donor = i;
      score = new_score;
    }  // if
  }  // for

  return donor;
}  // method findDonor


/******************************************************************************/
// Compute the Donor splice site on the complement strand.
public static long findDonorC
    ( String genomic				// Genomic sequence
    , long   center				// approximate location of donor site
    )
{
  long donor = -1L;				// next length of current exon
  int score = 0;				// Donor splice site score
  int window = 14;				// search window size

  if ( genomic == null )  return donor;
  if ( genomic.length () <= 0 )  return donor;

  // Compure the search window limits.
  long start = center - window;
  long end   = center + window;
  if ( start < 0L )  start = 0L;
  if ( end >= genomic.length () )  end = genomic.length () - 1L;

  // Evaluate alternatives in the 5' direction.
  for ( long i = start; i <= end; i++ )
  {
    // Score the candidate exon donor site.
    int new_score = scoreDonorC ( genomic, (int) i );

    if ( new_score > score )
    {
      donor = i;
      score = new_score;
    }  // if
  }  // for

  return donor;
}  // method findDonorC


/******************************************************************************/
// Score a candidate Splice Acceptor site.
public static int scoreAcceptor ( String genomic, int exon_start )
{
  int score = 0;				// splice site score

  // Validate exon_start.
  if ( exon_start < 0 )  return score;

  // Score splice acceptor site
  if ( ( exon_start - 10 >= 0 ) && ( exon_start - 10 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 10 ) == 'C' )  ||
         ( genomic.charAt ( exon_start - 10 ) == 'T' ) )  score++;

  if ( ( exon_start - 9 >= 0 ) && ( exon_start - 9 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 9 ) == 'C' )  ||
         ( genomic.charAt ( exon_start - 9 ) == 'T' ) )  score++;

  if ( ( exon_start - 8 >= 0 ) && ( exon_start - 8 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 8 ) == 'C' )  ||
         ( genomic.charAt ( exon_start - 8 ) == 'T' ) )  score++;

  if ( ( exon_start - 7 >= 0 ) && ( exon_start - 7 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 7 ) == 'C' )  ||
         ( genomic.charAt ( exon_start - 7 ) == 'T' ) )  score++;

  if ( ( exon_start - 6 >= 0 ) && ( exon_start - 6 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 6 ) == 'C' )  ||
         ( genomic.charAt ( exon_start - 6 ) == 'T' ) )  score++;

  if ( ( exon_start - 5 >= 0 ) && ( exon_start - 5 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 5 ) == 'C' )  ||
         ( genomic.charAt ( exon_start - 5 ) == 'T' ) )  score++;

  if ( ( exon_start - 3 >= 0 ) && ( exon_start - 3 < genomic.length () ) )
    if ( genomic.charAt ( exon_start - 3 ) == 'C' )  score += 3;

  if ( ( exon_start - 2 >= 0 ) && ( exon_start - 2 < genomic.length () ) )
    if ( genomic.charAt ( exon_start - 2 ) == 'A' )  score += 3;

  if ( ( exon_start - 1 >= 0 ) && ( exon_start - 1 < genomic.length () ) )
    if ( genomic.charAt ( exon_start - 1 ) == 'G' )  score += 3;

  return score;
}  // method scoreAcceptor


/******************************************************************************/
// Score a candidate Splice Acceptor site on the complement strand.
public static int scoreAcceptorC ( String genomic, int exon_start )
{
  int score = 0;				// splice site score

  // Validate exon_start.
  if ( exon_start < 0 )  return score;

  // Score splice acceptor site
  if ( ( exon_start - 10 >= 0 ) && ( exon_start - 10 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 10 ) == 'A' )  ||
         ( genomic.charAt ( exon_start - 10 ) == 'G' ) )  score++;

  if ( ( exon_start - 9 >= 0 ) && ( exon_start - 9 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 9 ) == 'A' )  ||
         ( genomic.charAt ( exon_start - 9 ) == 'G' ) )  score++;

  if ( ( exon_start - 8 >= 0 ) && ( exon_start - 8 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 9 ) == 'A' )  ||
         ( genomic.charAt ( exon_start - 9 ) == 'G' ) )  score++;

  if ( ( exon_start - 7 >= 0 ) && ( exon_start - 7 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 9 ) == 'A' )  ||
         ( genomic.charAt ( exon_start - 9 ) == 'G' ) )  score++;

  if ( ( exon_start - 6 >= 0 ) && ( exon_start - 6 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 9 ) == 'A' )  ||
         ( genomic.charAt ( exon_start - 9 ) == 'G' ) )  score++;

  if ( ( exon_start - 5 >= 0 ) && ( exon_start - 5 < genomic.length () ) )
    if ( ( genomic.charAt ( exon_start - 9 ) == 'A' )  ||
         ( genomic.charAt ( exon_start - 9 ) == 'G' ) )  score++;

  if ( ( exon_start - 3 >= 0 ) && ( exon_start - 3 < genomic.length () ) )
    if ( genomic.charAt ( exon_start - 3 ) == 'G' )  score += 3;

  if ( ( exon_start - 2 >= 0 ) && ( exon_start - 2 < genomic.length () ) )
    if ( genomic.charAt ( exon_start - 2 ) == 'T' )  score += 3;

  if ( ( exon_start - 1 >= 0 ) && ( exon_start - 1 < genomic.length () ) )
    if ( genomic.charAt ( exon_start - 1 ) == 'C' )  score += 3;

  return score;
}  // method scoreAcceptorC


/******************************************************************************/
// Score the candidate Donor splice site.
public static int scoreDonor ( String genomic, int exon_end )
{
  int score = 0;				// splice site score

  // Validate exon_end.
  if ( exon_end < 0 )  return score;

  // score splice donor site
  if ( ( exon_end - 1 < genomic.length () ) && ( exon_end - 1 >= 0 ) )
    if ( genomic.charAt ( exon_end - 1 ) == 'A' )  score++;

  if ( exon_end < genomic.length () )
    if ( genomic.charAt ( exon_end ) == 'G' )  score++;

  if ( exon_end + 1 < genomic.length () )
    if ( genomic.charAt ( exon_end + 1 ) == 'G' )  score += 2;

  if ( exon_end + 2 < genomic.length () )
    if ( genomic.charAt ( exon_end + 2 ) == 'T' )  score += 2;

  if ( exon_end + 3 < genomic.length () )
    if ( genomic.charAt ( exon_end + 3 ) == 'A' )  score++;

  if ( exon_end + 4 < genomic.length () )
    if ( genomic.charAt ( exon_end + 4 ) == 'A' )  score++;

  if ( exon_end + 5 < genomic.length () )
    if ( genomic.charAt ( exon_end + 5 ) == 'G' )  score++;

  if ( exon_end + 6 < genomic.length () )
    if ( genomic.charAt ( exon_end + 6 ) == 'T' )  score++;

  return score;
}  // method scoreDonor


/******************************************************************************/
// Score the candidate Donor splice site on the complement strand.
public static int scoreDonorC ( String genomic, int exon_end )
{
  int score = 0;				// splice site score

  // Validate exon_end.
  if ( exon_end < 0 )  return score;

  // score splice donor site
  if ( exon_end + 6 < genomic.length () )
    if ( genomic.charAt ( exon_end + 6 ) == 'A' )  score++;

  if ( exon_end + 5 < genomic.length () )
    if ( genomic.charAt ( exon_end + 5 ) == 'C' )  score++;

  if ( exon_end + 4 < genomic.length () )
    if ( genomic.charAt ( exon_end + 4 ) == 'T' )  score++;

  if ( exon_end + 3 < genomic.length () )
    if ( genomic.charAt ( exon_end + 3 ) == 'T' )  score++;

  if ( exon_end + 2 < genomic.length () )
    if ( genomic.charAt ( exon_end + 2 ) == 'A' )  score += 2;

  if ( exon_end + 1 < genomic.length () )
    if ( genomic.charAt ( exon_end + 1 ) == 'C' )  score += 2;

  if ( exon_end < genomic.length () )
    if ( genomic.charAt ( exon_end ) == 'C' )  score++;

  if ( ( exon_end - 1 < genomic.length () ) && ( exon_end - 1 >= 0 ) )
    if ( genomic.charAt ( exon_end - 1 ) == 'T' )  score++;

  return score;
}  // method scoreDonorC


/******************************************************************************/
public static int findBestPattern 
    ( String sequence
    , int first
    , int last
    , String pattern 
    )
{
  int best_start = -1;		// the best position in the sequence
  int score = 0;		// the best position in the sequence

  // Evaluate all possible pattern positions in the sequence.
  for ( int s = first; s <= last - pattern.length () + 1; s++ )
  {
    // Evaluate the pattern at this position.
    int matches = 0;
    int new_score = 0;
    for ( int p = 0; p < pattern.length (); p++ )
    {
      // Check if the bases match allowing for IUB codes.
      if ( s + p < sequence.length () )
      {
        if ( (dna_mask [ sequence.charAt ( s + p ) ] & dna_mask [ pattern.charAt ( p ) ]) != 0 )
        {
          matches++;
          int s_score = mask_score [ sequence.charAt ( s + p ) ];
          int p_score = mask_score [ pattern.charAt ( p ) ];
  
          // Add the lowest score (based on IUB letters).
          if ( s_score < p_score )
            new_score += s_score;
          else
            new_score += p_score;
        }  // if
      }  // if
      else  p = pattern.length ();  // exit the loop
    }  // for p

    // Check for a better pattern match & select 3' most pattern.
    if ( ( new_score >= score ) && ( matches == pattern.length () ) )
    {
      score = new_score;
      best_start = s;
    }  // if
  }  // for s

  return best_start;
}  // method findBestPattern


/******************************************************************************/
public static int findPolyA ( String sequence, int first )
{
  // Validate input.
  if ( ( sequence == null ) || ( first < 1 ) || ( first > sequence.length () ) )
    return -1;

  // Return the position of the next PolyAdenylation site (if found).
  return sequence.indexOf ( "AATAAA", first );
}  // method findPolyA


/******************************************************************************/
// Find the promoter sequence patterns.
public static void findPromoterElements ( String genomic, int gene_start )
{
  int promoter_start = gene_start - 1500;
  if ( promoter_start < 0 )  promoter_start = 0;

  int promoter_end = gene_start - 25;
  if ( promoter_end < 0 )  promoter_end = 0;

  // Find the TATA box.
  int start;
  start = findTATAbox ( genomic, promoter_start, promoter_end );

  if ( start >= 0 )  
    System.out.println ( "TATA box found at " + start );

  start = findBestPattern ( genomic, promoter_start, promoter_end, "TATAAAA" );
  if ( start >= 0 )  
    System.out.println ( "TATA box found at " + start + " using findBestPattern" );


  // Find the CAAT box.
  start = findBestPattern ( genomic, promoter_start, promoter_end, "GGNCAATCT" );

  if ( start >= 0 )  
    System.out.println ( "CAAT box found at " + start );

  // Find a GC box.
  start = findBestPattern ( genomic, promoter_start, promoter_end, "GGGCGG" );

  if ( start >= 0 )  
    System.out.println ( "GC box found at " + start );

}  // method findPromoterElements



/******************************************************************************/
public static int findTATAbox
    ( String sequence
    , int first
    , int last
    )
{
  int best_start = -1;		// the best position in the sequence
  int score = 0;		// the best position in the sequence

  // Evaluate all possible pattern positions in the sequence.
  for ( int s = first; s <= last - 6; s++ )
  {
    // Evaluate the TATA pattern at this position.
    int new_score = 0;

    if ( s < sequence.length () )
      if ( (dna_mask [ sequence.charAt ( s ) ] & dna_mask [ 'T' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s ) ] * 2;
  
    if ( s + 1 < sequence.length () )
      if ( (dna_mask [ sequence.charAt ( s + 1 ) ] & dna_mask [ 'A' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s + 1 ) ] * 2;
  
    if ( s + 2 < sequence.length () )
      if ( (dna_mask [ sequence.charAt ( s + 2 ) ] & dna_mask [ 'T' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s + 2 ) ] * 2;
  
    if ( s + 3 < sequence.length () )
      if ( (dna_mask [ sequence.charAt ( s + 3 ) ] & dna_mask [ 'A' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s + 3 ) ] * 2;
  
    if ( s + 4 < sequence.length () )
    {
      if ( (dna_mask [ sequence.charAt ( s + 4 ) ] & dna_mask [ 'A' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s + 4 ) ] * 2;
      else
        if ( (dna_mask [ sequence.charAt ( s + 4 ) ] & dna_mask [ 'T' ]) != 0 )
          new_score += mask_score [ sequence.charAt ( s + 4 ) ];
    }  // if
  
    if ( s + 5 < sequence.length () )
    {
      if ( (dna_mask [ sequence.charAt ( s + 5 ) ] & dna_mask [ 'A' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s + 5 ) ] * 2;
    }  // if
 
    if ( s + 6 < sequence.length () )
    {
      if ( (dna_mask [ sequence.charAt ( s + 6 ) ] & dna_mask [ 'A' ]) != 0 )
        new_score += mask_score [ sequence.charAt ( s + 6 ) ] * 2;
      else
        if ( (dna_mask [ sequence.charAt ( s + 6 ) ] & dna_mask [ 'T' ]) != 0 )
          new_score += mask_score [ sequence.charAt ( s + 6 ) ];
    }  // if

    // Check for a better pattern match & select 3' most pattern.
    if ( ( new_score >= score ) && ( new_score >= 48 ) )
    {
      score = new_score;
      best_start = s;
    }  // if
  }  // for s

  return best_start;
}  // method findTATAbox


/******************************************************************************/
private static int scoreStart ( String frame )
{
  int score = 0;

  // Traverse the frame backwards.
  for ( int i = frame.length () - 1; i >= 0; i-- )
  {
    // Can not go past a stop codon.
    if ( frame.charAt ( i ) == '*' )  return score;

    // Check for a possible translation start site.
    if ( ( frame.charAt ( i ) == 'M' ) ||
         ( frame.charAt ( i ) == 'm' ) )  

      score = frame.length () - i;
  }  // for

  if ( score > 0 )  return score;

  return frame.length ();
}  // method scoreStart


/******************************************************************************/
// Return the best frame that could be a start frame.
private static int findBestStart ( String [] frames )
{
  int score = 0;
  int best_frame = -1;

  // Compare each of the three frames.
  for ( int i = 0; i < 3; i++ )
  {
    int new_score = scoreStart ( frames [ i ] );
    if ( new_score > score )
    {
      score = new_score;
      best_frame = i;
    }  // if
  }  // for

  return best_frame; 
}  // findBestStart


/******************************************************************************/
public static int findStart ( String sequence, int first, int last )
{
  if ( first >= sequence.length () )
  {
    System.out.println ( "SeqTools.findStart: first (" + first + 
        ") >= sequence.length = " + sequence.length () );
    return -1;
  }  // if

  if ( last >= sequence.length () )
  {
    System.out.println ( "SeqTools.findStart: last (" + last + 
        ") >= sequence.length = " + sequence.length () );
    return -1;
  }  // if

  String frames [] = new String [ 3 ];			// mRNA translation frames

  // Create a 3 frame translation of the DNA sequence.
  if ( last + 1 < sequence.length () )
  {
    frames [ 0 ] = SeqTools.translate 
        ( sequence.substring ( first, last + 1 ) );
  
    frames [ 1 ] = "X" + SeqTools.translate 
        ( sequence.substring ( first + 1, last + 1 ) );
  
    frames [ 2 ] = "XX" + SeqTools.translate 
        ( sequence.substring ( first + 2, last + 1 ) );
  }  // if
  else
  {
    frames [ 0 ] = SeqTools.translate 
        ( sequence.substring ( first ) );
  
    frames [ 1 ] = "X" + SeqTools.translate 
        ( sequence.substring ( first + 1 ) );
  
    frames [ 2 ] = "XX" + SeqTools.translate 
        ( sequence.substring ( first + 2 ) );
  }  // else
 
  int best_frame = findBestStart ( frames );

  if ( best_frame != -1 )
  {
    System.out.println ( "best_frame = " + best_frame + " " + frames [ best_frame ] );
    System.out.println ( "first = " + first + " score = " + 
        scoreStart ( frames [ best_frame ] ) );

    return first - best_frame + 1 +
        ( ( frames [ best_frame ].length () - scoreStart ( frames [ best_frame ] ) ) * 3 );
  }  // if

  return -1;
}  // method findStart


/******************************************************************************/
public static int getCodonIndex ( String codon_seq )
{
  // Validate codon_seq length.
  if ( codon_seq.length () < 3 )  return 64;

  String codon = codon_seq.toUpperCase ();

  int index = base_map [ codon.charAt ( 0 ) ] * 16 +
              base_map [ codon.charAt ( 1 ) ] *  4 +
              base_map [ codon.charAt ( 2 ) ];

  // Check for a perfect codon
  if ( ( index >= 0 ) && ( index < 64 ) )  return index;

  return 64;		// not a perfect codon
}  // method getCodonIndex


/******************************************************************************/
public static boolean isORF 
    ( String  sequence
    , char    strand
    , long    start
    , long    end
    , int     frame 
    )
{
  // Validate the coordinates.
  if ( ( start < 1L ) || ( start > sequence.length () ) ||
       ( end < 1L ) || ( end > sequence.length () ) )  return false;

  // Scan the sequence looking for a stop codon.
  char amino = ' ';

  // Check for the forward strand.
  if ( strand == '+' )
  {
    // Check the forward frame.
    for ( int base = (int) start + frame; base < end - 1; base += 3 )
    {
      amino = mapCodon ( sequence.substring ( base - 1, base + 2 ) );

      if ( amino == '*' )  return false;
    }  // for
  }  // if
  else  // reverse strand
  {
    // Reverse the sequence.
    String reverse = reverseSequence ( sequence );

    int  rev_start = sequence.length () - (int) start + 1;
    int  rev_end   = sequence.length () - (int) end + 1;

    // Check the reverse frame.
    for ( int base = rev_start + frame; base < rev_end - 1; base += 3 )
    {
      amino = mapCodon ( reverse.substring ( base - 1, base + 2 ) );

      if ( amino == '*' )  return false;
    }  // for
  }  // else

  return true;
}  // method isORF


/******************************************************************************/
/* This function translates a codon into an amino acid. */
public static char mapCodon ( String codon_seq )
{
  char amino_acid;	/* translated amino acid */
  int  index;		/* codon table index */


  String codon = codon_seq.toUpperCase ();

// System.out.println ( "mapCodon: codon_seq = '" + codon_seq + "'" );

  index = base_map   [ codon.charAt ( 0 ) ] * 16 +
          base_map   [ codon.charAt ( 1 ) ] *  4 +
          base_map_3 [ codon.charAt ( 2 ) ];

  if ( index >= MAX_CODON )
  {
    index = base_map [ codon.charAt ( 0 ) ] * 16 +
            base_map [ codon.charAt ( 1 ) ] *  4;

    /* TRA */
    if ( ( codon.charAt ( 0 ) == 'T' ) && ( codon.charAt ( 1 ) == 'R' ) &&
         ( codon.charAt ( 2 ) == 'A' ) )  return ( '*' );

    /* YTR */
    if ( ( codon.charAt ( 0 ) == 'Y' ) && ( codon.charAt ( 1 ) == 'T' ) )

      if ( ( codon.charAt ( 2 ) == 'R' ) || ( codon.charAt ( 2 ) == 'A' ) ||
           ( codon.charAt ( 2 ) == 'G' ) )  return ( 'L' );

    /* MGR */
    if ( ( codon.charAt ( 0 ) == 'M' ) && ( codon.charAt ( 1 ) == 'G' ) )

      if ( ( codon.charAt ( 2 ) == 'R' ) || ( codon.charAt ( 2 ) == 'A' ) ||
           ( codon.charAt ( 2 ) == 'G' ) )  return ( 'R' );

    if ( index >= MAX_CODON )  return ( 'X' );

    amino_acid = codon_map [ index ];

    /* GCN, GGN, CCN, ACN, & GTN */
    if ( ( amino_acid == 'A' ) || ( amino_acid == 'G' ) || 
         ( amino_acid == 'P' ) ||
         ( amino_acid == 'T' ) || ( amino_acid == 'V' ) )
      return ( amino_acid );

    /* CTN */
    if ( ( amino_acid == 'L' ) && ( codon.charAt ( 0 ) == 'C' ) )
      return ( amino_acid );

    /* CGN */
    if ( ( amino_acid == 'R' ) && ( codon.charAt ( 0 ) == 'C' ) )
      return ( amino_acid );

    /* TCN */
    if ( ( amino_acid == 'S' ) && ( codon.charAt ( 0 ) == 'T' ) )
      return ( amino_acid );

    /* ATH */
    if ( ( amino_acid == 'I' ) && ( ( codon.charAt ( 2 ) == 'H' ) ||
         ( codon.charAt ( 2 ) == 'K' ) || ( codon.charAt ( 2 ) == 'W' ) ) )
      return ( amino_acid );
 
    return ( 'X' );
  }
  else
    return ( codon_map [ index ] );
}  // method mapCodon 


/******************************************************************************/
/* This function translates a codon into an amino acid (three letter code). */
public static String mapCodon3 ( String codon_seq )
{
  String amino_acid;	/* translated amino acid */
  int  index;		/* codon table index */


  String codon = codon_seq.toUpperCase ();

  // System.out.println ( "mapCodon3: codon_seq = '" + codon_seq + "'" );

  index = base_map   [ codon.charAt ( 0 ) ] * 16 +
          base_map   [ codon.charAt ( 1 ) ] *  4 +
          base_map_3 [ codon.charAt ( 2 ) ];

  if ( index >= MAX_CODON )
  {
    index = base_map [ codon.charAt ( 0 ) ] * 16 +
            base_map [ codon.charAt ( 1 ) ] *  4;

    /* TRA */
    if ( ( codon.charAt ( 0 ) == 'T' ) && ( codon.charAt ( 1 ) == 'R' ) &&
         ( codon.charAt ( 2 ) == 'A' ) )  return ( "Ter" );

    /* YTR */
    if ( ( codon.charAt ( 0 ) == 'Y' ) && ( codon.charAt ( 1 ) == 'T' ) )

      if ( ( codon.charAt ( 2 ) == 'R' ) || ( codon.charAt ( 2 ) == 'A' ) ||
           ( codon.charAt ( 2 ) == 'G' ) )  return ( "Leu" );

    /* MGR */
    if ( ( codon.charAt ( 0 ) == 'M' ) && ( codon.charAt ( 1 ) == 'G' ) )

      if ( ( codon.charAt ( 2 ) == 'R' ) || ( codon.charAt ( 2 ) == 'A' ) ||
           ( codon.charAt ( 2 ) == 'G' ) )  return ( "Arg" );

    if ( index >= MAX_CODON )  return ( "Xxx" );

    char aminoacid = codon_map [ index ];
    amino_acid = codons [ index ];

    /* GCN, GGN, CCN, ACN, & GTN */
    if ( ( aminoacid == 'A' ) || ( aminoacid == 'G' ) || 
         ( aminoacid == 'P' ) ||
         ( aminoacid == 'T' ) || ( aminoacid == 'V' ) )
      return ( amino_acid );

    /* CTN */
    if ( ( aminoacid == 'L' ) && ( codon.charAt ( 0 ) == 'C' ) )
      return ( amino_acid );

    /* CGN */
    if ( ( aminoacid == 'R' ) && ( codon.charAt ( 0 ) == 'C' ) )
      return ( amino_acid );

    /* TCN */
    if ( ( aminoacid == 'S' ) && ( codon.charAt ( 0 ) == 'T' ) )
      return ( amino_acid );

    /* ATH */
    if ( ( aminoacid == 'I' ) && ( ( codon.charAt ( 2 ) == 'H' ) ||
         ( codon.charAt ( 2 ) == 'K' ) || ( codon.charAt ( 2 ) == 'W' ) ) )
      return ( amino_acid );
 
    return ( "Xxx" );
  }
  else
    return ( codons [ index ] );
}  // method mapCodon3 


/******************************************************************************/
public static String convert1to3
    ( String seq
    )
{
  StringBuffer aa_seq = new StringBuffer ( seq.length () * 3 );

  for ( int i = 0; i < seq.length (); i++ )
  {
    if ( ( seq.charAt ( i ) >= 'a' ) && ( seq.charAt ( i ) <= 'z' ) )
      aa_seq.append ( map1to3 [ (int) (seq.charAt ( i ) - 'a') ] );
    else
      if ( ( seq.charAt ( i ) >= 'A' ) && ( seq.charAt ( i ) <= 'Z' ) )
        aa_seq.append ( map1to3 [ (int) (seq.charAt ( i ) - 'A') ] );
      else
        if ( seq.charAt ( i ) == '*' )
          aa_seq.append ( "Ter" );
        else
          if ( seq.charAt ( i ) == '.' ) 
            aa_seq.append ( "..." );
          else
            if ( seq.charAt ( i ) == '-' )
              aa_seq.append ( "---" );
            else
              aa_seq.append ( "Err" );
  }  // for

  return aa_seq.toString ();
}  // method convert1to3


/******************************************************************************/
public static String translate
  ( String seq
  , int    first
  , int    last
  )
{
  StringBuffer aa_seq = new StringBuffer ( seq.length () / 3 );

  for ( int i = first; i < last - 1; i += 3 )
  {
    if ( i == last - 2 )
      aa_seq.append ( mapCodon ( seq.substring ( i ) ) );
    else
      aa_seq.append ( mapCodon ( seq.substring ( i, i+3 ) ) );
  }  // for

  return aa_seq.toString ();
}  // method translate


/******************************************************************************/
public static String translate ( String seq )
{
  return translate ( seq, 0, seq.length () - 1 );
}  // method translate


/******************************************************************************/
public static String translate3 ( String seq )
{
  return translate3 ( seq, 0, seq.length () - 1 );
}  // method translate3


/******************************************************************************/
public static String translate3
  ( String seq
  , int    first
  , int    last
  )
{
  StringBuffer aa_seq = new StringBuffer ( seq.length () );
  StringBuffer codon = new StringBuffer ( 4 );
  aa_seq.setLength ( 0 );
  codon.setLength ( 0 );
  codon.append ( "AAA" );

  for ( int i = first; i <= last; )
  {
    int c = 0;
    while ( c < 3 )
    {
      // Check if at the end of the sequence.
      if ( i > last )  return aa_seq.toString ();

      if ( ( seq.charAt ( i ) != '|' ) &&
           ( seq.charAt ( i ) != '>' ) &&
           ( seq.charAt ( i ) != '<' ) )
      {
        codon.setCharAt ( c, seq.charAt ( i ) );
        c++;
      }  // if

      i++;
    }  // while
/*
System.out.println ( "i = " + i + ", codon = " + codon.toString () + 
    " -> " + mapCodon3 ( codon.toString () ) + " = " +
    mapCodon ( codon.toString () ) );
*/
    aa_seq.append ( mapCodon3 ( codon.toString () ) );
  }  // for

  return aa_seq.toString ();
}  // method translate3


/******************************************************************************/
// This function checks if a sequence base is an IUB code.
public static boolean isIUB ( char c )
{
  // Check for an IUB code.
  if ( ( mask_score [ c ] != 4 ) &&
       ( mask_score [ c ] > 0 ) )

    return true;

  return false;
}  // method isIUB


/******************************************************************************/
  // This method search for IUB codes in a sequence.
  public static boolean isIUBs ( String sequence )
  {
    for ( int index = 0; index < sequence.length (); index++ )

      if ( mask_score [ sequence.charAt ( index ) ] != 4 )  return true;

    return false;
  }  // method is IUBs


/******************************************************************************/
// This method removes the markers <, >, and | from a DNA sequence.
public static String removeMarkers ( String sequence )
{
  if ( sequence.length () <= 0 )  return "";

  StringBuffer seq = new StringBuffer ( sequence );

  for ( int i = seq.length () - 1; i >= 0; i-- )

    // Delete all non-sequence letters.
    if ( ( ( seq.charAt ( i ) >= 'A' ) && ( seq.charAt ( i ) <= 'Z' ) ) ||
         ( ( seq.charAt ( i ) >= 'a' ) && ( seq.charAt ( i ) <= 'z' ) ) )
    {
    }  // if
    else
      seq.deleteCharAt ( i );

  return seq.toString (); 
}  // method removeMarkers


/******************************************************************************/
/* This function reverses and complements a DNA sequence. */
public static String reverseSequence ( String seq )
{
  StringBuffer rev = new StringBuffer ( seq );

  for ( int index = 0; index < seq.length (); index++ )

    rev.setCharAt ( index, 
        complement [ seq.charAt ( seq.length () - index - 1 ) ] );

  return rev.toString ();
}  // method reverseSequence 


/******************************************************************************/
public static void writeFasta ( OutputTools file, String sequence )
{
  for ( int index = 0; index < sequence.length (); index += 50 )
  {
    if ( index + 50 >= sequence.length() )

      file.println ( sequence.substring( index ) );

    else

      file.println ( sequence.substring( index, index + 50 ) );
  }  // end: for
}  // method writeFasta


/******************************************************************************/
public static void writeFasta ( String sequence )
{
  for ( int index = 0; index < sequence.length (); index += 50 )
  {
    if ( index + 50 >= sequence.length() )

      System.out.println ( sequence.substring( index ) );

    else

      System.out.println ( sequence.substring( index, index + 50 ) );
  }  // end: for
}  // method writeFasta


/******************************************************************************/
private static int extent ( String seq1, String seq2 )
{
  // Extend the alignment
  int length = 0;
  while ( ( length < seq1.length () ) && 
          ( length < seq2.length () ) &&
          ( seq1.charAt ( length ) == seq2.charAt ( length ) ) )

    length++;

  return length;
}  // method extent


/******************************************************************************/
public static void align ( String genomic, String cdna )
{
  int  length;
  int  g_start = -1;
  int  c_start = 0;
  int  window = 12;

  // Find the first block that aligns.
  boolean found = false;
  while ( ( c_start < cdna.length () - window ) && ( found == false ) )
  {
    // Search for the start of the cDNA sequence.
    g_start = genomic.indexOf ( cdna.substring ( c_start, window ) );

    // Check if found.
    if ( g_start != -1 )
      found = true;
    else
      c_start++;
  }  // for 

  System.out.println ( "cDNA starting index = " + c_start );
  System.out.println ( "gDNA starting index = " + g_start );

  // Extend the alignment
  length = extent ( genomic.substring ( g_start ), cdna.substring ( c_start ) );

  System.out.println ( "Aligned segment length = " + length );
  System.out.println ();
  System.out.println ( "Genomic: [" + g_start + "," + (g_start + length - 1) + "]" );
  System.out.println ( "cDNA   : [" + g_start + "," + (g_start + length - 1) + "]" );

}  // method align


/******************************************************************************/
  // This method tests an alignment for low amino acid complexity.
  // This method considers the alignment region.
  public static boolean isLowAAComplexity ( String query, String target )
  {
    final int letters = 26;			// alphabet letters count

    int composition [] = 
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    String q = query.toUpperCase ();
    String t = target.toUpperCase ();

    int count = 0;				// count of amino acids in query

    // Traverse the alignment.
    for ( int i = 0; (i < q.length ()) && (i < t.length ()); i++ )
    {
      // Check for an amino acid.
      if ( ( q.charAt ( i ) >= 'A' ) && ( q.charAt ( i ) <= 'Z' ) )
      {
        count++;

        // Check for identity amino acids.
        if ( q.charAt ( i ) == t.charAt ( i ) ) 

          composition [ q.charAt ( i ) - 'A' ]++;
      }  // if
    }  // for

    // Check the amino acid composition counts.
    int fifty = (count + 1) / 2;
    int sixty = ( count * 6 ) / 10;
    for ( int i = 'A' - 'A'; i <= 'Z' - 'A'; i++ )
    {
      // Check if a single amino acids represents 50% of the alignment.
      if ( composition [ i ] >= fifty )  return true;

      // Check for two common amino acids.
      int j = i + 1;
      while ( j <= 'Z' - 'A' )
      {
        if ( composition [ i ] + composition [ j ] >= sixty )  return true;
        j++;
      }  // while
    }  // for

    return false;
  }  // method isLowAAComplexity


/******************************************************************************/
  // This method tests an alignment for low amino acid complexity.
  // This method only considers the identical residues.
  public static boolean isLowAAComplexity2 ( String query, String target )
  {
    final int letters = 26;			// alphabet letters count

    int composition [] = 
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    String q = query.toUpperCase ();
    String t = target.toUpperCase ();

    int count = 0;				// count of amino acids in query

    // Traverse the alignment.
    for ( int i = 0; (i < q.length ()) && (i < t.length ()); i++ )
    {
      // Check for an amino acid.
      if ( ( q.charAt ( i ) >= 'A' ) && ( q.charAt ( i ) <= 'Z' ) )
      {
        // Check for identity amino acids.
        if ( q.charAt ( i ) == t.charAt ( i ) ) 
        {
          composition [ q.charAt ( i ) - 'A' ]++;
          count++;
        }  // if
      }  // if
    }  // for

    // Check the amino acid composition counts.
    int fifty = (count + 1) / 2;
    int sixty = ( count * 6 ) / 10;
    for ( int i = 'A' - 'A'; i <= 'Z' - 'A'; i++ )
    {
      // Check if a single amino acids represents 50% of the alignment.
      if ( composition [ i ] >= fifty )  return true;

      // Check for two common amino acids.
      int j = i + 1;
      while ( j <= 'Z' - 'A' )
      {
        if ( composition [ i ] + composition [ j ] >= sixty )  return true;
        j++;
      }  // while
    }  // for

    return false;
  }  // method isLowAAComplexity2


/******************************************************************************/
// This method looks for a dinucleotide SSR starting at the offset.
public static int findDiSSR ( String sequence, int start )
{
  for ( int i = start; i + 15 < sequence.length (); i++ )
  {
    // Quick test.
    if ( ( sequence.charAt ( i ) == sequence.charAt ( i + 14 ) ) &&
         ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 15 ) ) )
    {
      // The rest of the test.
      if ( ( sequence.charAt ( i ) == sequence.charAt ( i + 2 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 4 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 6 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 8 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 10 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 12 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 3 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 5 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 7 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 9 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 11 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 13 ) ) &&
           ( sequence.charAt ( i ) != sequence.charAt ( i + 1 ) ) )

      return i;
    }  // if
  }  // for

  return -1;		// not found
}  // method findDiSSR


/******************************************************************************/
// This method returns the length of a Simple Sequence Repeat (SSR).
public static int findSSRLength ( String sequence, int start, String pattern )
{
  // Determine the pattern size.
  int size = pattern.length ();

  // Validate the pattern size.
  if ( size <= 0 )  return 0;

  // Validate the start of the pattern.
  if ( start + size >= sequence.length () )  return 0;

  // Count the number of occurences of this pattern starting at start.
  int count = 0;

  while ( ( start + (count + 1) * size < sequence.length () ) &&
          ( sequence.substring ( start, start + size ).equals
              ( sequence.substring ( start + count * size
                                   , start + (count + 1) * size 
                                   ) 
              ) 
           )
         )

    count++;

  return count * size;
}  // method findSSRLength


/******************************************************************************/
// This method looks for a trinucleotide SSR starting at the offset.
public static int findTriSSR ( String sequence, int start )
{
  for ( int i = start; i + 14 < sequence.length (); i++ )
  {
    // Quick test.
    if ( ( sequence.charAt ( i     ) == sequence.charAt ( i + 12 ) ) &&
         ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 13 ) ) &&
         ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 14 ) ) )
    {
      // The rest of the test.
      if ( ( sequence.charAt ( i ) == sequence.charAt ( i + 3 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 6 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 9 ) ) &&

           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 4 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 7 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 10 ) ) &&

           ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 5 ) ) &&
           ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 8 ) ) &&
           ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 11 ) ) )
      {
        // Ignore poly-nucleotide repeats.
        if ( ( sequence.charAt ( i ) != sequence.charAt ( i + 1 ) ) ||
             ( sequence.charAt ( i ) != sequence.charAt ( i + 2 ) ) )

        return i;
      }  // if
    }  // if
  }  // for

  return -1;		// not found
}  // method findTriSSR


/******************************************************************************/
// This method looks for a quadnucleotide SSR starting at the offset.
public static int findQuadSSR ( String sequence, int start )
{
  for ( int i = start; i + 15 < sequence.length (); i++ )
  {
    // Quick test.
    if ( ( sequence.charAt ( i     ) == sequence.charAt ( i + 12 ) ) &&
         ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 13 ) ) &&
         ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 14 ) ) &&
         ( sequence.charAt ( i + 3 ) == sequence.charAt ( i + 15 ) ) )
    {
      // The rest of the test.
      if ( ( sequence.charAt ( i ) == sequence.charAt ( i + 4 ) ) &&
           ( sequence.charAt ( i ) == sequence.charAt ( i + 8 ) ) &&

           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 5 ) ) &&
           ( sequence.charAt ( i + 1 ) == sequence.charAt ( i + 9 ) ) &&

           ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 6 ) ) &&
           ( sequence.charAt ( i + 2 ) == sequence.charAt ( i + 10 ) ) &&

           ( sequence.charAt ( i + 3 ) == sequence.charAt ( i + 7 ) ) &&
           ( sequence.charAt ( i + 3 ) == sequence.charAt ( i + 11 ) ) )
      {
        // Ignore poly-nucleotide repeats.
        if ( ( sequence.charAt ( i ) != sequence.charAt ( i + 1 ) ) ||
             ( sequence.charAt ( i ) != sequence.charAt ( i + 2 ) ) ||
             ( sequence.charAt ( i ) != sequence.charAt ( i + 3 ) ) )

        return i;
      }  // if
    }  // if
  }  // for

  return -1;		// not found
}  // method findQuadSSR


/******************************************************************************/
  public static void main ( String [] args )
  {
    char c;
    int i;

    c = 'A';
    i = (int) c;

    System.out.println ( "Char " + c + " = " + i );

    String seq = "AGTC";

    System.out.println ( "Sequence " + seq + " complement = " + 
        reverseSequence ( seq ) );

    seq = "ATG";

    char aa =  mapCodon ( seq );

    System.out.println ( "Codon " + seq + " tranlation = " + aa );

    seq = "ATGTTTCTTATTGTTTCTCCTACTGCTTATTAATAGCATCAAAATAAAGATGAATGTTGATGGCGTAGTAGAGGT";
//  DNA = "ATGTTTCTTATT........TACTGCTTATTAA........AAATAAAGATGA........GGCGTAGTAGAGG";
//                   1         2         3         4         5         6         7
//         012345678901234567890123456789012345678901234567890123456789012345678901234567890

    // Translate a DNA sequence into a protein sequence.
    for ( i = 0; i <= 2; i++ )

      System.out.println ( "Frame " + i + " " +
        translate ( seq, i, seq.length () - 1 ) );

    // Translate a DNA sequence into a protein sequence (3 letter code).
    for ( i = 0; i <= 2; i++ )

      System.out.println ( "Frame " + i + " " +
        translate3 ( seq, i, seq.length () - 1 ) );

    String cDNA = "ATGTTTCTTATTTACTGCTTATTAAAAATAAAGATGAGGCGTAGTAGAGG";

    align ( seq, cDNA );
  }  // method main


/******************************************************************************/

}  // class SeqTools

