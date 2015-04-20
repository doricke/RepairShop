#!/usr/bin/perl -w

  use strict;


# Main program:
main:
{

  # my @fields;				# name fields

  # my $accession;			# current accession number

  # my $full_name;			# complete file name

  # my $next_name;			# next file name

  # initialization:
  $file_name = "";

  # Check the first line of the 

  while ( <STDIN> )
  {
    chomp;

    $full_name = $_;
    @fields = split ( /./, $_ );

    $accession = $fields [ 0 ];

    if ( $previous_name =~ /$accession/ )
    {
      print "old $previous_name\n";
    }  # if

    $accession = $next_name;
    $previous_name = $full_name;
  }  # while

}  # main program


