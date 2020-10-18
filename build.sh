#!/bin/bash

cd /work && mvn package -Dmaven.test.skip=true -B
