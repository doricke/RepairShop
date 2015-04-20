#!/usr/bin/perl -w

use strict;

main: 
{
  my %names;

  # Zero out values for each input file.
  my @list;
  foreach my $i ( 0 .. 17 )
  {
    $list[ $i ] = 0;
  }  # foreach
  my $zeros = join ( "\t", @list );

  # Read in the Names and Identifiers for this chip.
  # open IDS, "A.ids";
  open IDS, "B.ids";
  $_ = <IDS>;
  chomp;
  my $header = $_;
  while ( <IDS> )
  {
    chomp;
    my $text = $_;
    my ($ct, $probe, $name, $id) = split (/\t/);
    $names{$probe} = $text . "\t" . $zeros;
  }  # while
  close IDS;

  # Read in one file name at a time.
  my $index = 0;
  while ( <> )
  {
    chomp;
    my $filename = $_;
    print "<- $filename\n";
    s/.txt//;			# strip ".txt" from file name
    my $line = $_;
    $header .= "\tCy5_" . $line . "\tCy3_" . $line;

    open FILE, "<$filename";
    my $head = <FILE>;		# file header line.

    while ( <FILE> )
    {
      chomp;
      my ($ct, $probe, $name, $id, $cy5, $cy3, $cy5n, $cy3n) = split (/\t/);

      # print "$names{$probe}\n";
      my @values = split ( /\t/, $names{$probe} );
      $values[$index*2+4] = $cy5;
      $values[$index*2+5] = $cy3;
      $names{$probe} = join ( "\t", @values );
    }  # while
    close FILE;
    $index++;
  }  # while

  open OUT, ">B_merge.txt";
  print OUT ( $header . "\n" );
  foreach my $probe ( keys %names )
  {
    print OUT ( $names{$probe} . "\n" );
  }  # foreach
  close OUT;

}  # main

1
