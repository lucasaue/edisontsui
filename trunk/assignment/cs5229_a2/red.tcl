#
# RED queue
#
#   The scripts used to make the figure in the paper were from a
# simulator that is now several generations old.  These ns-2 scripts
# are a slightly-modified version of scripts from Davide Bergamasco at
# Cisco.
#
source common.tcl

set minthresh [lindex $argv 0]
set seed  [lindex $argv 1]
set queuetype RED
set maxthresh [expr $minthresh * 3]
puts "minthresh $minthresh maxthresh $maxthresh"
set BuffSize 140
Queue/RED set linterm_ 50 
Queue/RED set thresh_ $minthresh
Queue/RED set maxthresh_ $maxthresh
Queue/RED set limit_ $BuffSize 
init $seed $BuffSize
topo
make_traffic
$ns at $SimTime "get_queue; finish"
puts "Running simulation $seed Buff= $BuffSize"
$ns run

