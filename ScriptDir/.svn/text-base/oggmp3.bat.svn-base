@echo off
sox -t ogg %1 -t raw -r 44100 -c 2 -w -s - | lame -x --preset insane -
REM oggdec %1 -o - | lame --preset insane -