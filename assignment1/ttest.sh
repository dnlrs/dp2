#!/bin/bash

for i in {0..100}
do
    echo -n "seed: "$i" -> "
    ant -Dseed="$i" -Dtestacase="2" runFuncTest >> /dev/null
    if [ $? -eq 0 ]; then
        echo "success"
    else
        echo "fail"
    fi

done
