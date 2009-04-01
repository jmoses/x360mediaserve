
@echo off
C:
cd "C:\Program Files\VideoLAN\VLC"
taskkill /F /IM vlc.exe 1> C:\temp\kill.txt
vlc %1 -I dummy --dummy-quiet --sout=#transcode{vcodec=WMV2,vb=4096,scale=1,acodec=wma,ab=192}:standard{access=file,mux=asf,url=C:\temp\buffer.wmv} vlc:quit
REM vlc %1 --sout=#transcode{vcodec=WMV2,vb=4096,scale=1,acodec=wma,ab=192}:standard{access=file,mux=asf,url=C:\temp\buffer.wmv} 

REM ffmpeg -i %1 -f asf -b 3072 -vcodec wmv2 -an -y -


REM mencoder.exe -of lavf -o file.asf -of lavf -ovc lavc -lavc opts vcodec=msmpeg4 ..\test.avi -oac pcm  -lavfopts i_certify_that_my_video_stream_does_not_use_b_frames