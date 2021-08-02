/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';

const MyHeadlessTask = async taskData => {
  console.log('BackgroundJob is started!');
  console.log(taskData);
};

AppRegistry.registerHeadlessTask('BackgroundJob', () => MyHeadlessTask);

AppRegistry.registerComponent(appName, () => App);
