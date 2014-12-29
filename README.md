FacebookNotifications
=====================

Periodically checks for new Facebook notifications via an OS X system notification tray.

Build instructions:
* Requires Maven:
```
brew install maven
```

* First off, download and install appbundler:
```
git clone https://github.com/federkasten/appbundler-plugin
cd appbundler-plugin
sh build.sh
mvn install
```

* Then, download and compile Facebook Notifications:
```
git clone https://github.com/jackpf/FacebookNotifications
cd FacebookNotifications
mvn package appbundle:bundle
```

* Generated app will be in the target directory, should be good to go.
