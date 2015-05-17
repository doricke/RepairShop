#!/usr/bin/perl -w


use strict;
# use warnings;

main:
{
  # Read in the prosite patterns.
  my %patterns;
  open PROSITE, "prosite.patterns.perl";
  while ( <PROSITE> )
  {
    chomp;

    my @fields = split ( /\t/, $_ );	# split line on tabs

    $patterns{$fields[0]} = $fields[1];
  }  # while
  close PROSITE;


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
      $seq =~ /($patterns{$motif_name})/ and print "$name\t$motif_name\t$1\n";
    }  # foreach
  }  # while
}  # main
