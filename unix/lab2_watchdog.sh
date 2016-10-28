display_usage() {
	echo -e "Usage: watchdog [user_id]"
}

if [ $# -lt 1 ]
then
	display_usage
	exit 1
fi

if [[ $1 == "--help" || $1 == "-h" ]]
then
	display_usage
	exit 0
fi

is_online=$(curl —request POST "https://api.vk.com/method/users.get?user_ids=$1&fields=online" | python -c "import sys, json; print json.load(sys.stdin)['response'][0]['online']")

if [ $is_online -eq 1 ]; then
	notify-send "online"
	exit 0
else
	notify-send "offline"
	exit 0
fi