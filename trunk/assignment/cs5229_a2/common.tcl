#
#   The scripts used to make the figure in the paper were from a
# simulator that is now several generations old.  These ns-2 scripts
# are a slightly-modified version of scripts from Davide Bergamasco at
# Cisco.
#
# window = 70, bandwidth = 5ms
# window = 240, bandwidth = 20ms 
set bandwidth 20ms
set window 240 

proc make_traffic {} {
    global ns n3 n4 n5 n6 tcp3 tcp4 fmon_

    set tcp3 [new Agent/TCP]
    $tcp3 set overhead_ 0.00017

    $ns attach-agent $n3 $tcp3
    set snk3 [new Agent/TCPSink]
    $ns attach-agent $n6 $snk3
    $ns connect $tcp3 $snk3
    $tcp3 set fid_ 0
    
    set tcp4 [new Agent/TCP]
    $tcp4 set overhead_ 0.00017
    $ns attach-agent $n4 $tcp4
    set snk4 [new Agent/TCPSink]
    $ns attach-agent $n6 $snk4
    $ns connect $tcp4 $snk4
    $tcp4 set fid_ 1
    
    set ftp3 [new Source/FTP]
    $ftp3 set agent_ $tcp3
    $ns at 0.0 "$ftp3 start"
    
    set ftp4 [new Source/FTP]
    $ftp4 set agent_ $tcp4
    set StartTime [expr [ns-random]  / 2147483647.0 / 100]
    puts "starttime $StartTime"
    $ns at $StartTime "$ftp4 start"

    set fmon_ [$ns makeflowmon Fid]
    $ns attach-fmon [$ns link $n5 $n6] $fmon_
}

#
# This isn't actually used, but if called, it would make a plot
# of the instantaneous queue size.
#
proc trace_red {} {
    global ns n5 n6
    set redq [[$ns link $n5 $n6] queue]
    set tchan_ [open all.q w]
    $redq trace curq_ 
    $redq attach $tchan_
}

proc init {seed BuffSize} {
    global ns SimTime packetsize window
    
    puts "$seed"
    ns-random $seed
    set packetsize 1000
    set SimTime 5.0
    
    set ns [new Simulator]

    #set nf [open out.nam w]
    #$ns namtrace-all $nf
    
    $ns color 0 blue
    $ns color 1 red
    Agent/TCP set window_ $window
    Agent/TCP set packetSize_ $packetsize
    Queue set limit_ $BuffSize
}

proc topo {} {
    global ns n3 n4 n5 n6 queuetype bandwidth
    set n3 [$ns node]
    $n3 color "blue"
    set n4 [$ns node]
    $n4 color "red"
    set n5 [$ns node]
    $n5 shape box
    set n6 [$ns node]
    
    $ns duplex-link $n3 $n5 100Mb 1ms DropTail
    $ns duplex-link $n4 $n5 100Mb 1ms DropTail
    $ns duplex-link $n5 $n6 45Mb $bandwidth $queuetype
    $ns duplex-link-op $n5 $n6 queuePos 0.5
}

proc get_queue {} {
    global ns fmon_

    set fcl [$fmon_ classifier]
    set fids { 0 1 }
    set total 0.0
    foreach i $fids {
            set flow [$fcl lookup auto 0 0 $i]
            if { $flow != "" } {
                    set dsamp [$flow get-delay-samples]
		    set mean [$dsamp mean]
		    set total [expr $total + $mean]
                    # puts "flow $i mean [format "%8.6f" $mean]"
            }
    }
    puts "mean delay (in seconds) [format "%8.6f" [expr $total / 2]]"
    puts "(A queue of 100 packets corresponds to delay of 0.017 seconds.)"

}

proc finish {} {
    global SimTime packetsize
    global tcp3 
    global tcp4

    set throughput [expr [$tcp3 set t_seqno_] * $packetsize * 8.0 / $SimTime/1000000]
    # puts "flow [$tcp3 set fid_] throughput [format "%6.2f" $throughput]"

    set throughput1 [expr [$tcp4 set t_seqno_] * $packetsize * 8.0 / $SimTime/1000000] 
    # puts "flow [$tcp4 set fid_] throughput [format "%6.2f" $throughput1]"
    puts "total throughput (in Mbps) [format "%6.2f" [expr $throughput + $throughput1]]"

    #  puts "running nam..."
    #  exec nam out.nam &
    exit 0
}
