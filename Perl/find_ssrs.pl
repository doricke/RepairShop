#!/usr/bin/perl -w


use strict;
# use warnings;

main:
{
  # Read in the prosite patterns.
  my %patterns;
  my %accessions;
  my %descriptions;
  # open PROSITE, "prosite.dat.perl";
  while ( <DATA> )
  {
    chomp;

    my @fields = split ( /\t/, $_ );	# split line on tabs

    $patterns{$fields[0]} = $fields[1];
    $accessions{$fields[0]} = $fields[2];
    $descriptions{$fields[0]} = $fields[3];
  }  # while
  # close PROSITE;


  # Change the record separator.
  local $/ = "\n>";

  # Read in protein sequences one sequence at a time.
  while ( <> )
  {
    chomp;
    s/>//;		# strip ">" from header line

    # Split record into header field and sequence fields.
    my ($header, @seq) = split (/\n/);

    # Combine the sequence lines.
    my $seq = join ( "", @seq );

    my ($name, @desc) = split ( " ", $header );

    # Search the protein sequence for the Prosite patterns.
    foreach my $motif_name ( keys %patterns )
    {
      if ( $seq =~ /($patterns{$motif_name})/ )
      {
        my $motif_start = length ( $` ) + 1;
        my $motif_end = length ( $` ) + length ( $1 );

        print "$name\t";
        print "$motif_start\t";
        print "$motif_end\t";
        print "$motif_name\t";
        print "$accessions{$motif_name}\t";
        print "$descriptions{$motif_name}\t";
        print "$1\t";
        print "$1\n";
      }  # if
    }  # foreach
  }  # while
}  # main
__DATA__
AC	(AC){7,}	AC	(AC)n
AG	(AG){7,}	AG	(AG)n
AT	(AT){7,}	AT	(AT)n
CA	(CA){7,}	CA	(CA)n
CG	(CG){7,}	CG	(CG)n
CT	(CT){7,}	CT	(CT)n
GA	(GA){7,}	GA	(GA)n
GC	(GC){7,}	GC	(GC)n
GT	(GT){7,}	GT	(GT)n
TA	(TA){7,}	TA	(TA)n
TC	(TC){7,}	TC	(TC)n
TG	(TG){7,}	TG	(TG)n
