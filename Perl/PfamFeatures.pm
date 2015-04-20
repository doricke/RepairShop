
package PfamFeatures;

use strict;

################################################################################
#
# This is the PfamFeatures module.  It reads in fields from a Pfam .out file.
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
# The input file is expected to contain 
#
# 1. <sequence_name><tab>
# 2. <pfam_name><tab>
# 3. <pfam_accession><tab>
# 4. <pfam_description>\n
#
################################################################################
sub new
{
  my ($class, $fh) = @_;

  if ( ref $fh !~ /GLOB/ )
  {
    warn "PfamFeatures error: new expects a GLOB file reference not $fh\n";
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
  , $this->{PFAM_NAME}
  , $this->{PFAM_ACCESSION}
  , $this->{PFAM_DESCRIPTION} 
  ) = split ( /\t/, $line );

  return $line;
}  # next


################################################################################
# These accessor functions return the features from the current Pfam .out line.

sub sequence_name    { shift->{SEQ_NAME} }
sub pfam_name        { shift->{PFAM_NAME} }
sub pfam_accession   { shift->{PFAM_ACCESSION} }
sub pfam_description { shift->{PFAM_DESCRIPTION} }


################################################################################

1
