#!/bin/bash
set -e
PACKAGE="org.CreadoresProgram.CreaProDroid"
ACTIVITY="org.CreadoresProgram.CreaProDroid.MainActivity"
echo "Loading Logs..."
adb logcat | grep "CreaProDroid" &
LOGCAT_PID=$!
echo "===Open App==="
adb shell am start -n "$PACKAGE/$ACTIVITY"
sleep 5
echo "===On Pause==="
adb shell input keyevent 3
sleep 3
echo "===On Resume==="
adb shell am start -n "$PACKAGE/$ACTIVITY"
sleep 3
echo "===On Back=="
adb shell input keyevent 4
adb shell input keyevent 4
sleep 3
adb shell am force-stop "$PACKAGE"
sleep 3
echo "===End==="
kill $LOGCAT_PID
