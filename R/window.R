
# *****************************************************************************
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


# *****************************************************************************
start_window <- function(begin, current, pos)
{
  while ( pos[current] - pos[begin] > 125 )  begin <- begin + 1
  begin
}  # function start_window

# *****************************************************************************
end_window <- function(current, last, pos)
{
  while ( ( last < length(pos) ) && ( pos[last+1] - pos[current] < 125 ) )  last <- last + 1
  last
}  # function end_window

# *****************************************************************************
# Start of main script.

inp <- scan("control", list(pos=0, chr="", X=0, Y=0, intens=0, probe=""))
trt <- scan("treatment", list(pos="", chr="", X=0, Y=0, intens=0, probe=""))
out <- file("p.value", "w")

win_start <- 1;
win_end <- 1;

cat ( "Position\tP-value\tsample\tcontrol\n", file=out );

for (win_center in 1:length(inp$pos) )
{
  win_start <- start_window(win_start, win_center, inp$pos)

  win_end <- end_window(win_center, win_end, inp$pos)

  pv = wilcox.test (trt$intens[win_start:win_end], inp$intens[win_start:win_end], alternative="g")

  cat(trt$pos[win_center], "\t", pv$p.value, "\t", trt$intens[win_center], "\t", inp$intens[win_center], "\n", file=out)
}  # for

close(out)

rm (win_start, win_end, inp, trt, pv, out)
q(save="no")
