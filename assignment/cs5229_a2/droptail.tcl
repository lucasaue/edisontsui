#
# Drop tail queue
#
#   The scripts used to make the figure in the paper were from a
# simulator that is now several generations old.  These ns-2 scripts
# are a slightly-modified version of scripts from Davide Bergamasco at
# Cisco.
#

source common.tcl

set BuffSize [lindex $argv 0]
set seed  [lindex $argv 1]
set queuetype DropTail
init $seed $BuffSize
topo
make_traffic
$ns at $SimTime "get_queue; finish"
puts "Running simulation $seed Buff= $BuffSize"
$ns run

