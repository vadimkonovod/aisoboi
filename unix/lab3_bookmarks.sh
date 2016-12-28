#!/bin/bash

mkdir -p ~/bin

#  --------------- create s script ---------------
if [ -f ~/bin/s ]; then
	rm ~/bin/s
fi

cat > ~/bin/s << EOFS
#!/bin/bash
if [ \$# -lt 1 ]; then 
	echo "bookmark name is required"; exit 1;
fi
key=\$1
value=\$PWD
sed -i "/\$1/d" ~/.profile
echo "export DIR_\$key=\$value" » ~/.profile
source ~/.profile
exit 0;
EOFS

chmod +x ~/bin/s

# --------------- create g script ---------------

if [ -f ~/bin/g ]; then
	rm ~/bin/g
fi

cat > ~/bin/g << EOFG
#!/bin/bash
if [ \$# -lt 1 ]; then 
	echo "bookmark name is required"; exit 1;
fi
dir=grep -oP "DIR_\$1=\K[^\n]+" ~/.profile
if [ -n "\$dir" ]; then
	cd \$(grep -oP "DIR_\$1=\K[^\n]+" ~/.profile)
else
	echo -e "bookmark \$1 not found"; exit 1; 
fi;
exit 0;
EOFG

chmod +x ~/bin/g

# --------------- create d script ---------------

if [ -f ~/bin/d ]; then
	rm ~/bin/d
fi

cat > ~/bin/d << EOFD
#!/bin/bash
if [ \$# -lt 1 ]; then 
	echo "bookmark name is required"; exit 1;
fi
dir=grep -oP "DIR_\$1=\K[^\n]+" ~/.profile
if [ -n "\$dir" ]; then
	sed -i "/\$1/d" ~/.profile
	source ~/.profile
else
	echo -e "bookmark \$1 not found"; exit 1; 
fi;
exit 0;
EOFD

chmod +x ~/bin/d

# --------------- create l script ---------------

if [ -f ~/bin/l ]; then
	rm ~/bin/l
fi

cat > ~/bin/l << EOFL
#!/bin/bash
env | sort | awk '/^DIR_.+/{split(substr($0,5),parts,"="); printf("\033[0;33m%-20s\033[0m %s\n", parts[1], parts[2]);}'
exit 0;
EOFD

chmod +x ~/bin/l

# -----------------------------------------------

export PATH=$PATH":$HOME/bin"
source ~/.profile
