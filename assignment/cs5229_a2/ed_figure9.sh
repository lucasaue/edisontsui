#!/bin/bash

########
# Usage: sh ed_figure8.sh
########


########
# Cleanup
########
rm *.data
rm out

########
# Param
########
NUM_TRY=5

DT_EXP=11
DT_BUFFSIZE=( 15 20 25 30 40 50 60 80 100 120 140 )
DT_SEED=0
RED_EXP=12
RED_MINTHRESH=( 3 5 7 9 11 13 15 20 25 30 40 50 )
RED_MAXTHRESH=( 9 15 21 27 33 39 45 60 75 90 120 150 )
RED_Q_WEIGHT=0.002
RED_SEED=0

#########
# Prepare Experiment
#########
for ((i=0; i < RED_EXP ; i++ ))
do 
	for ((j=0; j < NUM_TRY; j++))
	do
		ns red.tcl ${RED_MINTHRESH[$i]} $RED_SEED ${RED_MAXTHRESH[$i]} $RED_Q_WEIGHT TRUE FALSE TRUE >> red_geometric1_$i.data 
	done
	echo "Finished RED Exp:" $i ", Param:" ${RED_MINTHRESH[$i]} $RED_SEED 
done
#
for ((i=0; i < RED_EXP ; i++ ))
do 
	for ((j=0; j < NUM_TRY; j++))
	do
		ns red.tcl ${RED_MINTHRESH[$i]} $RED_SEED ${RED_MAXTHRESH[$i]} $RED_Q_WEIGHT TRUE FALSE FALSE >> red_geometric0_$i.data 
	done
	echo "Finished RED Exp:" $i ", Param:" ${RED_MINTHRESH[$i]} $RED_SEED 
done

########
# Print data to graph file
########
echo ' ' >> out
echo '"red_geometric_false' >> out
for ((i=0; i < RED_EXP; i++))
do
	 awk '{ if ($2=="delay"){delay+=$5};if ($2=="throughput"){throughput+=$5/45.0; count+=1} } END { print throughput/count, delay/count, count  }' red_geometric0_$i.data  >> out

done

echo ' ' >> out
echo '"red_geometric_true' >> out
for ((i=0; i < RED_EXP; i++))
do
	 awk '{ if ($2=="delay"){delay+=$5};if ($2=="throughput"){throughput+=$5/45.0; count+=1} } END { print throughput/count, delay/count, count  }' red_geometric1_$i.data  >> out

done

xgraph -bb -tk -m -nl -x throughput_% -y delay_sec out &


