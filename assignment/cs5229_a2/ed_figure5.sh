#!/bin/bash

########
# Cleanup
########
rm *.data
rm out

########
# Param
########
NUM_TRY=10

DT_EXP=11
DT_BUFFSIZE=( 15 20 25 30 40 50 60 80 100 120 140 )
DT_SEED=( 0 0 0 0 0 0 0 0 0 0 0 )
RED_EXP=12
RED_MINTHRESH=( 3 5 7 9 11 13 15 20 25 30 40 50 )
RED_MAXTHRESH=( 9 15 21 27 33 39 45 60 75 90 120 150 )
RED_Q_WEIGHT=( 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 0.00 )

RED_SEED=( 0 0 0 0 0 0 0 0 0 0 0 0 )

#########
# Prepare Experiment
#########
for ((i=0; i < DT_EXP ; i++ ))
do 
	for ((j=0; j < NUM_TRY; j++))
	do
		ns droptail.tcl ${DT_BUFFSIZE[$i]} ${DT_SEED[$i]} >> dt$i.data
	done
	echo "Finished DropTail Exp:" $i ", Param:" ${DT_BUFFSIZE[$i]} ${DT_SEED[$i]}
done
#
for ((i=0; i < RED_EXP ; i++ ))
do 
	for ((j=0; j < NUM_TRY; j++))
	do
		ns red.tcl ${RED_MINTHRESH[$i]} ${RED_SEED[$i]} ${RED_MAXTHRESH[$i]} ${RED_Q_WEIGHT[$i]} >> red$i.data 
	done
	echo "Finished RED Exp:" $i ", Param:" ${RED_MINTHRESH[$i]} ${RED_SEED[$i]} 
done


########
# Print data to graph file
########
sh ed_print.sh $DT_EXP $RED_EXP
