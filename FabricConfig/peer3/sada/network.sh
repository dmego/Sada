#!/bin/bash
#
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

UP_DOWN="$1"
IF_COUCHDB="$2"

function printHelp () {
	echo "Usage: ./network_setup <up|down> <cdb>.\nThe arguments must be in order."
}

function validateArgs () {
	if [ -z "${UP_DOWN}" ]; then
		echo "Option up / down / restart not mentioned"
		printHelp
		exit 1
	fi
}

function clearContainers () {
        CONTAINER_IDS=$(docker ps -aq)
        if [ -z "$CONTAINER_IDS" -o "$CONTAINER_IDS" = " " ]; then
                echo "---- No containers available for deletion ----"
        else
                docker rm -f $CONTAINER_IDS
        fi
}

function removeUnwantedImages() {
        DOCKER_IMAGE_IDS=$(docker images | grep "dev\|none\|test-vp\|peer[0-9]-" | awk '{print $3}')
        if [ -z "$DOCKER_IMAGE_IDS" -o "$DOCKER_IMAGE_IDS" = " " ]; then
                echo "---- No images available for deletion ----"
        else
                docker rmi -f $DOCKER_IMAGE_IDS
        fi
}

function networkUp () {
    if [ "${IF_COUCHDB}" == "cdb" ]; then
      docker-compose -f docker-compose-peer.yaml -f docker-compose-couch.yaml up -d 
    else
      docker-compose -f docker-compose-peer.yaml up -d 
    fi
}

function networkDown () {
    docker-compose -f docker-compose-peer.yaml down

    #Cleanup the chaincode containers
    clearContainers

    #Cleanup images
    removeUnwantedImages

    # remove orderer block and other channel configuration transactions and certs
    #rm -rf channel/*.block channel/*.tx crypto-config
}

validateArgs

#Create the network using docker compose
if [ "${UP_DOWN}" == "up" ]; then
	networkUp
elif [ "${UP_DOWN}" == "down" ]; then ## Clear the network
	networkDown
elif [ "${UP_DOWN}" == "restart" ]; then ## Restart the network
	networkDown
	networkUp
else
	printHelp
	exit 1
fi
