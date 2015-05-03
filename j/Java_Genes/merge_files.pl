#!/usr/bin/perl -w

use strict;

main: 
{
  my %names;

  # Zero out values for each input file.
  my @list;
  foreach my $i ( 0 .. 31 )
  {
    $list[ $i ] = 1;
  }  # foreach
  my $ones = join ( "\t", @list );

  # Read in the Names and Identifiers for this chip.
  open IDS, "Gene.names";
  $_ = <IDS>;
  chomp;
  my $header = $_;
  while ( <IDS> )
  {
    chomp;
    my $line = $_;
    my ($name, $chr) = split (/\t/);
    $names{$name} = $line . "\t" . $ones;
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
    $header .= "\t" . $line;

    open FILE, "<$filename";
    my $head = <FILE>;		# file header line.

    while ( <FILE> )
    {
      chomp;
      my ($name, $chr, $start, $end, $pv ) = split (/\t/);

      # print "$name\t$names{$name}\n";
      my @values = split ( /\t/, $names{$name} );
      $values[$index+2] = $pv;
      $names{$name} = join ( "\t", @values );
    }  # while
    close FILE;
    $index++;
  }  # while

  open OUT, ">MEF_Genes.txt";
  print OUT ( $header . "\n" );
  foreach my $name ( keys %names )
  {
    print OUT ( $names{$name} . "\n" );
  }  # foreach
  close OUT;

}  # main

1
