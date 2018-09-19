#!/bin/bash
ERVERS="orderer peer1 peer2 peer3 peer4"
PASSWORD=3012

auto_ssh_copy_id() {
    expect -c "set timeout -1;
        spawn ssh-copy-id -i /root/.ssh/id_rsa.pub $1;
        expect {
            *(yes/no)* {send -- yes\r;exp_continue;}
            *assword:* {send -- $2\r;exp_continue;}
            eof        {exit 0;}
        }";
}

ssh_copy_id_to_all() {
    for SERVER in $SERVERS
    do
        auto_ssh_copy_id $SERVER $PASSWORD
    done
}

ssh_copy_id_to_all
