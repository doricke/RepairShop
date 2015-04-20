
package ScanFeatures;

use strict;

################################################################################
#
# This is the ScanFeatures module.  It reads in fields from a SCAN .Results file.
#
################################################################################
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
################################################################################
# The first parameter is a type GLOB file handle.
# The input file is expected to contain sequence_name\tlength pairs.
sub new
{
  my ($class, $fh) = @_;

  if ( ref $fh !~ /GLOB/ )
  {
    warn "ScanFeatures error: new expects a GLOB file reference not $fh\n";
    return 0;
  }  # if

  my $this = bless {};		# Create an object
  $this->{FH} = $fh;		# save the file handle reference

  return $this;
}  # new


################################################################################
# This function returns the sequence length for a sequence name.
# The first parameter is the name of the sequence.
sub next
{
  my ($this, $name) = @_;

  my $fh = $this->{FH};

  my $line = <$fh>;

  if ( ! defined $line ) { return $line }

  ( $this->{SEQ_NAME}
  , $this->{QUERY_START}
  , $this->{QUERY_END}
  , $this->{QUERY_STRAND} 
  , $this->{HIT_STRAND} 
  , $this->{IDENTITIES} 
  , $this->{SCORE} 
  , $this->{P_VALUE} 
  , $this->{HIT_LENGTH} 
  , $this->{PERCENT} 
  , $this->{SEQ_TYPE} 
  , $this->{PROGRAM} 
  , $this->{DATABASE} 
  , $this->{ACCESSION} 
  , $this->{HIT_START} 
  , $this->{HIT_END} 
  , $this->{DESCRIPTION} 
  ) = split ( /\t/, $line );

  return $line;
}  # next


################################################################################
# These functions return the features from the current SCAN .Results line.

sub sequence_name 
{ 
  my ($this) = shift ( @_ );

  my $seq_name = $this->{SEQ_NAME};

  if ( $seq_name =~ /protein/ )
  {
    $seq_name = $`;
  }  # if


  return $seq_name;
} # subroutine sequence_name

sub query_start   { shift->{QUERY_START} }
sub query_end     { shift->{QUERY_END} }
sub query_strand  { shift->{QUERY_STRAND} }
sub hit_strand    { shift->{HIT_STRAND} }
sub identities    { shift->{IDENTITIES} }
sub score         { shift->{SCORE} }
sub p_value       { shift->{P_VALUE} }
sub hit_length    { shift->{HIT_LENGTH} }
sub percent       { shift->{PERCENT} }
sub seq_type      { shift->{SEQ_TYPE} }
sub program       { shift->{PROGRAM} }
sub database      { shift->{DATABASE} }
sub accession     { shift->{ACCESSION} }
sub hit_start     { shift->{HIT_START} }
sub hit_end       { shift->{HIT_END} }
sub description   { shift->{DESCRIPTION} }


################################################################################

1
