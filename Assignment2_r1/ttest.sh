#!/bin/bash

for i in {0..100}
do
    echo -n "seed: "$i" -> "
    echo " ==========================================================================================" >> log.txt
    echo " seed: "$i >> log.txt 
    echo " ==========================================================================================" >> log.txt
    ant -Dseed="$i" runFuncTest >> log.txt
    if [ $? -eq 0 ]; then
        echo "success"
    else
        echo "fail"
    fi

done
