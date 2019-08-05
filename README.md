# alarmapp
AlarmClock application for Android

run npm install

delete files inside directory android/app/src/main/assets

run react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res

run react-native run-android
