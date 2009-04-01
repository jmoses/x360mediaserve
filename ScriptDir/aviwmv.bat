
@echo off
REM C:
REM cd "C:\Program Files\Windows Media Components\Encoder\"
REM taskkill /F /IM vlc.exe 1> C:\temp\kill.txt
REM socketwrapper -c "C:\Program Files\VideoLAN\VLC\vlc %1 -I dummy --sout=#transcode{vcodec=WMV2,vb=4096,scale=1,acodec=wma,ab=192}:standard{access=file,mux=asf,url=#PIPE#}"
REM socketwrapper -c "cscript.exe wmcmd.vbs -input C:\tools\mplayer\test.avi -output #PIPE# -v_codec wmv9 -v_bitrate 900000 -a_codec wmastd -v_performance 20"

ffmpeg -i %1 -f asf -b 907200 -vcodec wmv2 -acodec wmav2 -y -bt 0 -ab 128000 -minrate 907200 -maxrate 907200 -bufsize 90720000 -


REM mencoder.exe -of lavf -o file.asf -of lavf -ovc lavc -lavc opts vcodec=msmpeg4 ..\test.avi -oac pcm  -lavfopts i_certify_that_my_video_stream_does_not_use_b_frames


