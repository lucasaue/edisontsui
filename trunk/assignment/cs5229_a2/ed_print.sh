#!/bin/bash
#######
# Take arg
#######
DT_EXP=$1
RED_EXP=$2

#echo "DT: " $DT_EXP ", RED: " $RED_EXP

########
# Print data to graph file
########
echo '"droptail' > out
for ((i=0; i < DT_EXP; i++))
do
	 awk '{ if ($2=="delay"){delay+=$5};if ($2=="throughput"){throughput+=$5/45.0; count+=1} } END { print throughput/count, delay/count, count  }' dt$i.data >> out

done

echo ' ' >> out
echo '"red' >> out
for ((i=0; i < RED_EXP; i++))
do
	 awk '{ if ($2=="delay"){delay+=$5};if ($2=="throughput"){throughput+=$5/45.0; count+=1} } END { print throughput/count, delay/count, count  }' red$i.data >> out

done

xgraph -bb -tk -m -nl -x throughput_% -y delay_sec out &

