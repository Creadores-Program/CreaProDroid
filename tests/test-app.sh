#!/bin/bash
set -e
PACKAGE="org.CreadoresProgram.CreaProDroid"
ACTIVITY="org.CreadoresProgram.CreaProDroid.MainActivity"
echo "===Open App==="
adb shell am start -n "$PACKAGE/$ACTIVITY"
sleep 5
echo "===On Pause==="
adb shell input keyevent 3
sleep 3
echo "===On Resume==="
adb shell am start -n "$PACKAGE/$ACTIVITY"
sleep 3
echo "===Logs==="
adb logcat | grep "CreaProDroid"
echo "===End==="
