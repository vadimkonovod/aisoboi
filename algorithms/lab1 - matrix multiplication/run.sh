for n in `seq $1 $2 $3`
do
	echo "n=" $n
	./cache $n
done