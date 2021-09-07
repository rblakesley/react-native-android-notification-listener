import { NativeModules } from 'react-native'

const { RNAndroidNotificationListener } = NativeModules

export const RNAndroidNotificationListenerHeadlessJsName = 'RNAndroidNotificationListenerHeadlessJs'
export const NotificationFlags = {
  FLAG_GROUP_SUMMARY: 512,
  FLAG_LOCAL_ONLY: 256,
  FLAG_HIGH_PRIORITY: 128,
  FLAG_FOREGROUND_SERVICE: 64,
  FLAG_NO_CLEAR: 32,
  FLAG_AUTO_CANCEL: 16,
  FLAG_ONLY_ALERT_ONCE: 8,
  FLAG_INSISTENT: 4,
  FLAG_ONGOING_EVENT: 2,  
}

export default RNAndroidNotificationListener
