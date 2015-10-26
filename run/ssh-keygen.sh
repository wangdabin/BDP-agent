#!/bin/bash
if [ ! -f ~/.ssh/id_rsa.pub ];then
echo | ssh-keygen -N '' -t rsa -q -b 2048
fi
