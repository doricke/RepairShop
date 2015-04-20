#!/usr/bin/perl -w

use strict;
# use warnings;

#
# Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
# Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
# License::   	GNU GPL license  (http://www.gnu.org/licenses/gpl.html)
# Contact::   	Paragon Software, 1314 Viking Blvd., Cedar, MN 55011
#
#             	This program is free software; you can redistribute it and/or modify
#             	it under the terms of the GNU General Public License as published by
#             	the Free Software Foundation; either version 2 of the License, or
#             	(at your option) any later version.
#         
#             	This program is distributed in the hope that it will be useful,
#             	but WITHOUT ANY WARRANTY; without even the implied warranty of
#             	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#             	GNU General Public License for more details.
#
#               You should have received a copy of the GNU General Public License
#               along with this program. If not, see <http://www.gnu.org/licenses/>.

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
AAC	(AAC){5,}	AAC	(AAC)n
AAG	(AAG){5,}	AAG	(AAG)n
AAT	(AAT){5,}	AAT	(AAT)n
ACA	(ACA){5,}	ACA	(ACA)n
ACC	(ACC){5,}	ACC	(ACC)n
ACG	(ACG){5,}	ACG	(ACG)n
ACT	(ACT){5,}	ACT	(ACT)n
AGA	(AGA){5,}	AGA	(AGA)n
AGC	(AGC){5,}	AGC	(AGC)n
AGG	(AGG){5,}	AGG	(AGG)n
AGT	(AGT){5,}	AGT	(AGT)n
ATA	(ATA){5,}	ATA	(ATA)n
ATC	(ATC){5,}	ATC	(ATC)n
ATG	(ATG){5,}	ATG	(ATG)n
ATT	(ATT){5,}	ATT	(ATT)n
CAA	(CAA){5,}	CAA	(CAA)n
CAC	(CAC){5,}	CAC	(CAC)n
CAG	(CAG){5,}	CAG	(CAG)n
CAT	(CAT){5,}	CAT	(CAT)n
CCA	(CCA){5,}	CCA	(CCA)n
CCG	(CCG){5,}	CCG	(CCG)n
CCT	(CCT){5,}	CCT	(CCT)n
CGA	(CGA){5,}	CGA	(CGA)n
CGC	(CGC){5,}	CGC	(CGC)n
CGG	(CGG){5,}	CGG	(CGG)n
CGT	(CGT){5,}	CGT	(CGT)n
CTA	(CTA){5,}	CTA	(CTA)n
CTC	(CTC){5,}	CTC	(CTC)n
CTG	(CTG){5,}	CTG	(CTG)n
CTT	(CTT){5,}	CTT	(CTT)n
GAA	(GAA){5,}	GAA	(GAA)n
GAC	(GAC){5,}	GAC	(GAC)n
GAG	(GAG){5,}	GAG	(GAG)n
GAT	(GAT){5,}	GAT	(GAT)n
GCA	(GCA){5,}	GCA	(GCA)n
GCC	(GCC){5,}	GCC	(GCC)n
GCG	(GCG){5,}	GCG	(GCG)n
GCT	(GCT){5,}	GCT	(GCT)n
GGA	(GGA){5,}	GGA	(GGA)n
GGC	(GGC){5,}	GGC	(GGC)n
GGT	(GGT){5,}	GGT	(GGT)n
GTA	(GTA){5,}	GTA	(GTA)n
GTC	(GTC){5,}	GTC	(GTC)n
GTG	(GTG){5,}	GTG	(GTG)n
GTT	(GTT){5,}	GTT	(GTT)n
TAA	(TAA){5,}	TAA	(TAA)n
TAC	(TAC){5,}	TAC	(TAC)n
TAG	(TAG){5,}	TAG	(TAG)n
TAT	(TAT){5,}	TAT	(TAT)n
TCA	(TCA){5,}	TCA	(TCA)n
TCC	(TCC){5,}	TCC	(TCC)n
TCG	(TCG){5,}	TCG	(TCG)n
TCT	(TCT){5,}	TCT	(TCT)n
TGA	(TGA){5,}	TGA	(TGA)n
TGC	(TGC){5,}	TGC	(TGC)n
TGG	(TGG){5,}	TGG	(TGG)n
TGT	(TGT){5,}	TGT	(TGT)n
TTA	(TTA){5,}	TTA	(TTA)n
TTC	(TTC){5,}	TTC	(TTC)n
TTG	(TTG){5,}	TTG	(TTG)n
