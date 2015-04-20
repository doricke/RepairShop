
package SeqLengths;

use strict;

################################################################################
#
# This is the SeqLengths module.  It reads in the sequence names and lengths
# from the specified file.
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
    warn "SeqLengths error: new expects a GLOB file reference not $fh\n";
    return 0;
  }  # if

  my $this = bless {};		# Create an object
  $this->{FH} = $fh;		# save the file handle reference

  my %lengths;			# hash of sequence name->lengths

  # Read in the sequence name lengths.
  while ( <$fh> )
  {
    # Structure <sequence name><tab><sequence length>\n
    my ($name, $len) = split;

    $lengths{$name} = $len;	# save the length for this sequence
  } # while

  $this->{LENS} = \%lengths;	# save a reference to the lengths hash

  return $this;
}  # new


################################################################################
# This function returns the sequence length for a sequence name.
# The first parameter is the name of the sequence.
sub length
{
  my ($this, $name) = @_;

  my $len_ref = $this->{LENS};

  return $len_ref->{$name};
}  # length


################################################################################

1
