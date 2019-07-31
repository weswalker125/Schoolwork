#!/bin/bash

SERVER=dev8.jhuep.com
USER=change_it
PASSWORD="change_it"

echo "user: $USER"
echo "pass: $PASSWORD"

curl -T ./build/libs/wwalke24_assignment12.war "https://$USER:$PASSWORD@$SERVER/manager/text/deploy?path=/wwalke24_assignment12&update=true"