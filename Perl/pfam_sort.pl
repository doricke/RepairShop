#!/usr/bin/perl -w

use strict;

main:
{
  use PfamFeatures;

  my $pfam = new PfamFeatures ( \*STDIN );

  my %drop_list = 
      ( PF00126 => "bacterial"
      , PF00140 => "bacterial"
      , PF00154 => "bacterial"
      , PF00344 => "bacterial"
      , PF00392 => "bacterial"
      , PF00440 => "bacterial"
      , PF00470 => "bacterial"
      , PF00497 => "bacterial"
      , PF00589 => "bacterial"
      , PF00776 => "bacterial"
      , PF01022 => "bacterial"
      , PF01334 => "bacterial"
      , PF02631 => "bacterial"

      , PF00078 => "retro"
      , PF00665 => "retro"
      , PF01141 => "retro" 
      , PF01614 => "retro"
      , PF02992 => "retro"
      , PF03017 => "retro"
      , PF03108 => "retro"
      , PF03229 => "retro"
      );

  my $line;
  do
  {
    $line = $pfam->next ();

    if ( defined $line )
    {
      my $acc = $pfam->pfam_accession ();

      if ( defined $drop_list{$acc} )
      {
        print "mv " . $pfam->sequence_name . " " . $drop_list{$acc} . "\n";
      }  # if
    }  # if
  }
  while ( defined $line );
}  # main
