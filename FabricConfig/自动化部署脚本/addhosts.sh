#!/bin/bash
cat >> /etc/hosts << EOF
172.16.3.190 orderer
172.16.3.192 peer1
172.16.3.193 peer2
172.16.3.194 peer3
172.16.3.195 peer4
EOF
