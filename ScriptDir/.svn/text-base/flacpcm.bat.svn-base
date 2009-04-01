#!/bin/bash
PATH=/sw/bin:/:$PATH
# this is horrific but I can't seem to get it to work another way, any bash experts willing to help
# remove " from filename
var=${1%\"}
var=${var#\"}
flac -d $var  -c --endian=big --force-raw-format --sign=signed