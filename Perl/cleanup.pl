#!/usr/bin/perl -w

  use strict;

  print "This is the Java & BLAST clean up program:\n";
  my @names = qw \ java blastall \;
  &clean ( @names );

  print "\nEnd of program\n";


sub clean ()
{
  my @names = @_;

  foreach my $name ( @names )
  {
    print "\nProcessing $name:\n";

    my $pids = system ( "ps -aef | grep ricke | grep $name | cut -c10-14 > cleanup.pids" );

    if ( $pids == 0 )
    {
      open PIDS, "<cleanup.pids";
      while ( <PIDS> )
      {
        chomp;
        print "killing $_\n";
        system ( "kill -9 $_" );
      }  # while

      close PIDS;
    }  # else

    system ( "rm cleanup.pids" );

    print "\n";
  }  # foreach

}  # subroutine status

