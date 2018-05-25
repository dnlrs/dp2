#!/bin/bash

for i in {0..3}
do
    echo -n "seed: "$i" -> "
    ant -Dseed="$i" -Dtestacase=4 runFuncTest >> /dev/null
    if [ $? -eq 0 ]; then
        echo "success"
    else
        echo "fail"
    fi

done
