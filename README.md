# Web Doctor Educator (WDE) Android App
This Android app is designed to be compatible with the webdoctor-server which can be found
[here](https://github.com/btomaszewski/webdoctor-server/).

## Purpose
The purpose of the WDE is to help doctors in Rwanda by providing a single
application which includes many of the tasks they use daily. Currently the goals are as follows:

 - Search system which collects results from disparate sources (e.g. Google, Wikipedia, on-device
   PDF files, etc.) and displays those results in one list
 - Discussion board where doctors can communicate about the issues they are treating and
   ask for help and advice from other doctors using the system.

## Libraries
WDE uses the excellent [MuPDF library](http://mupdf.com/) to load and render PDF files.
The downside to this is that we have to include native libraries. Precompiled binaries
can be found under `app/src/main/jniLibs/` where the Android build system will automatically
detect them and add them to the application. The downside is that this makes the app very large.
For testing purposes it's recommended to rename jniLibs to something else such as jniLibs-bak.
Then make a new folder called jniLibs and copy only the specific native libraries you need for
testing. This means if you are testing on a phone with an ARMv7 processor you'll want your jniLibs
folder to contain the armeabi-v7a folder with libmupdf.so in it. If you're testing on an x86
emulator or tablet then you'll want the x86 folder. Remember that each library in the jniLibs
folder increases the apk size by about 7 MB and increases the time it takes to upload to the test
device.


