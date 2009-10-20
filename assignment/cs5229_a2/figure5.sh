#
ns droptail.tcl 15 0 >> dt.data
ns droptail.tcl 20 0 >> dt.data
ns droptail.tcl 25 0 >> dt.data
ns droptail.tcl 30 0 >> dt.data
ns droptail.tcl 40 0 >> dt.data
ns droptail.tcl 50 0 >> dt.data
ns droptail.tcl 60 0 >> dt.data
ns droptail.tcl 80 0 >> dt.data
ns droptail.tcl 100 0 >> dt.data
ns droptail.tcl 120 0 >> dt.data
ns droptail.tcl 140 0 >> dt.data
#
ns red.tcl 3 0 >> red.data 
ns red.tcl 5 0 >> red.data 
ns red.tcl 7 0 >> red.data 
ns red.tcl 9 0 >> red.data 
ns red.tcl 11 0 >> red.data 
ns red.tcl 13 0 >> red.data
ns red.tcl 15 0 >> red.data
ns red.tcl 20 0 >> red.data
ns red.tcl 25 0 >> red.data
ns red.tcl 30 0 >> red.data
ns red.tcl 40 0 >> red.data
ns red.tcl 50 0 >> red.data
#
echo '"droptail' > out
awk '{if ($2=="delay"){delay=$5};if ($2=="throughput"){print $5/45.0, delay}}' \
 dt.data >> out
echo ' ' >> out
echo '"red' >> out
awk '{if ($2=="delay"){delay=$5};if ($2=="throughput"){print $5/45.0, delay}}' \
 red.data >> out
xgraph -bb -tk -m -nl -x throughput_% -y delay_sec out &

