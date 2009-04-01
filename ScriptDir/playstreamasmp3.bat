@echo off
REM mplayer %1 -really-quiet -af resample=44100:0:0,format=u16le,channels=2 -cache-min 5 -ao pcm:nowaveheader:file=/dev/fd/9  9>&1  1>&2 | lame -x --preset insane -
socketwrapper -c "mplayer %1 -really-quiet -af resample=44100:0:0,format=u16le,channels=2 -cache-min 5 -ao pcm:nowaveheader:file=#PIPE# 1>&2 | lame -x --preset insane -"