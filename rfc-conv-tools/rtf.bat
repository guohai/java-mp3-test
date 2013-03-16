@echo off
C:
cd C:\Program Files\Harigaya\Rtfconv
RTFCONV -cRTF -cUTF8 %1 > %2
cd C:\rfc-conv-tools
exit