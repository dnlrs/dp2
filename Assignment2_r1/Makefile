

all: reset-all
	echo "Completed."

reset-all:
	echo -n "stopping tomcat..."
	tomcat-stop >> /dev/null 2>&1
	sleep 2
	echo "done."
	echo -n "stopping neo4j..."
	neo4j stop >> /dev/null 2>&1
	sleep 5
	echo "done."
	echo -n "clearing neo4j databases..."
	rm -rf /opt/neo4j-community-3.2.12/data/databases/graph.db >> /dev/null
	sleep 3
	echo "done."
	echo -n "starting neo4j..."
	neo4j start >> /dev/null
	sleep 5
	echo "done."
	echo -n "starting tomcat..."
	tomcat-start >> /dev/null
	sleep 2
	echo "done."
	
	
extended-tests: reset-all
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
	
	
