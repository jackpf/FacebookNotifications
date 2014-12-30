FacebookNotifications
=====================

Periodically checks for new Facebook notifications via a system notification tray.
Working on OS X and Linux (albeit buggy), untested on Windows.

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

You'll need to set up your own Facebook app to obtain an API key and secret.
Then navigate to src/main/resources and rename api_keys.dist to api_keys, and replace the values with your own.

* Generated app will be in the target directory, should be good to go.
