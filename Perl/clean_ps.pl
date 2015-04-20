#!/usr/bin/perl -w

use strict;

main:
{
  opendir DH, "." or die "Cannot open local directory!: $!";

  foreach ( readdir DH )
  {
    if ( /.ps$/ )
    {
      `rm $_\n`;
    }  # if
  }  # foreach

}  # main program

