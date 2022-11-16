#!/usr/bin/env bash

path=$1
echo "Creating and Changing directory"
echo $path

mkdir -p $path
# shellcheck disable=SC2164
cd "$path"

branch=$2

module=$3

cvs -z3 co -P -r $branch $module