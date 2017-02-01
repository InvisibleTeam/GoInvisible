#!/bin/bash

set -ev

# Create and start emulator
echo no | android create avd --force --name test --target $ANDROID_TARGET --abi $ANDROID_ABI    #Create AVD for given api
emulator -avd test -no-skin -no-audio -no-window &    #Start emulator
android-wait-for-emulator
adb devices   #Display list of devices
adb shell input keyevent 82 &
