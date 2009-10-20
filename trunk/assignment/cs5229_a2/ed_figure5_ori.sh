#!/bin/bash

ITERATION=10
DT_EXP=11
DT_BUFFSIZE=( 15 20 25 30 40 50 60 80 100 120 140 )
DT_SEED=( 0 0 0 0 0 0 0 0 0 0 0 )
RED_EXP=12
RED_MINTHRESH=( 3 5 7 9 11 13 15 20 25 30 40 50 )
RED_SEED=( 0 0 0 0 0 0 0 0 0 0 0 0 )

#
for ((i=0; i < DT_EXP ; i++ ))
do 
	ns droptail.tcl ${DT_BUFFSIZE[$i]} ${DT_SEED[$i]} >> dt.data
	echo "ns droptail.tcl" ${DT_BUFFSIZE[$i]} ${DT_SEED[$i]} ">> dt.data"

done
#
for ((i=0; i < RED_EXP ; i++ ))
do 
	ns red.tcl ${RED_MINTHRESH[$i]} ${RED_SEED[$i]} >> red.data 
	echo "ns red.tcl" ${RED_MINTHRESH[$i]} ${RED_SEED[$i]} ">> red.data" 

done
#
echo '"droptail' > out
awk '{if ($2=="delay"){delay=$5};if ($2=="throughput"){print $5/45.0, delay}}' \
 dt.data >> out
echo ' ' >> out
echo '"red' >> out
awk '{if ($2=="delay"){delay=$5};if ($2=="throughput"){print $5/45.0, delay}}' \
 red.data >> out
xgraph -bb -tk -m -nl -x throughput_% -y delay_sec out &

