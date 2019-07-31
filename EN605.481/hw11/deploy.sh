#!/bin/bash

SERVER=dev8.jhuep.com
USER=change_it
PASSWORD="change_it"
ARGS="org.apache.catalina.filters.CSRF_NONCE=2C6112657C17F3A1FE7621FDFFD0FD8D"

echo "user: $USER"
echo "pass: $PASSWORD"

curl -T ./build/libs/wwalke24_assignment11.war "https://$USER:$PASSWORD@$SERVER/manager/text/deploy?$ARGS&path=/wwalke24_assignment11&update=true"