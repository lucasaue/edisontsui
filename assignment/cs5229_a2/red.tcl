#
# RED queue
#
#   The scripts used to make the figure in the paper were from a
# simulator that is now several generations old.  These ns-2 scripts
# are a slightly-modified version of scripts from Davide Bergamasco at
# Cisco.
#
# Modified by Edison
# Added maxthresh & q_weight, refer to red.cc
source common.tcl

set minthresh [lindex $argv 0]
set seed  [lindex $argv 1]
set maxthresh [lindex $argv 2]
set q_weight [lindex $argv 3]

set queuetype RED
#set maxthresh [expr $minthresh * 3]
puts "minthresh $minthresh maxthresh $maxthresh q_weight $q_weight"
set BuffSize 140
Queue/RED set linterm_ 50 
Queue/RED set thresh_ $minthresh
Queue/RED set maxthresh_ $maxthresh
Queue/RED set limit_ $BuffSize 
Queue/RED set q_weight_ $q_weight
init $seed $BuffSize
topo
make_traffic
$ns at $SimTime "get_queue; finish" 
puts "Running simulation $seed Buff= $BuffSize"
$ns run

