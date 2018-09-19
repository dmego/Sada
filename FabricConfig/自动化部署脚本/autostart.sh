#!/bin/bash
#自动启动区块链网络
servers="orderer peer1 peer2 peer3 peer4"
for i in $servers
do
    echo "=====================Start node" $i "...====================="
    ssh $i "cd /root/go/src/github.com/hyperledger/sada/;./network.sh up cdb"
    echo "=====================Node " $i " startup completion=========="
done

ssh peer4 "docker logs -f cli"
