#!/bin/bash
 
display_usage() {
    echo -e "Usage: $(basename "$0") [-h|--help] <VK_USER_ID>";
}
 
display_help() {
    display_usage
    echo -e '\nThis script takes a single argument - vk.com user ID (for example, id1 or durov),
and displays the system notification (notify-send (online)) in the case of the
presence of the appropriate person online.
 
Options:
    -h, --help     displays this help
 
Exit codes:
    0: Success
    1: Network error / Invalid user ID'
}
 
NETWORK_ERROR="Network error: please check your internet connection"
 
if [ $# -lt 1 ]
then
    display_usage
    exit 1
fi
 
if [[ $1 == "--help" || $1 == "-h" ]]
then
    display_help
    exit 0
fi
 
response=$(curl -s "https://api.vk.com/method/users.get?user_ids=$1&fields=online") || { echo $NETWORK_ERROR; exit 1; };
 
error_msg=$(echo $response | grep -oP '"error_msg":"\K[^"]+');
if [ -n "$error_msg" ]; then
    echo -e $error_msg; exit 1;
fi;
 
is_online=$(python -c "import sys, json; print $response['response'][0]['online']")
if [ $is_online -eq 1 ]; then
    notify-send "online"
    exit 0
else
    notify-send "offline"
    exit 0
fi