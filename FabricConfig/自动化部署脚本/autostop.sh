#!/bin/bash
#自动停止区块链网络
servers="orderer peer1 peer2 peer3 peer4"
for i in $servers
do
    echo "=====================Close node" $i "...====================="
    ssh $i "cd /root/go/src/github.com/hyperledger/sada/;./network.sh down"
    echo "=====================Node " $i " close completion=========="
done
