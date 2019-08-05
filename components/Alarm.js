import React, { Component } from "react";
import {
  Button,
  CheckBox,
  NativeModules,
  StyleSheet,
  Text,
  TextInput,
  View
} from 'react-native';
import moment from 'moment';

import DateTimePicker from "react-native-modal-datetime-picker";
import { Colors } from 'react-native/Libraries/NewAppScreen';

var AlarmApp = NativeModules.AlarmApp;

const initialCreateAlarmSettings = {
  isDateTimePickerVisible: false,
  selectedDateTime: '',
  repeat: false,
  vibrate: false,
  text: ''
};

let UNIQUE_ID = 1;

export default class Alarm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      alarmsList: {},
      ...initialCreateAlarmSettings,
    };
  }

  saveAlarm = () => {
    const _hours = moment(this.state.selectedDateTime).format('HH');
    const _minutes = moment(this.state.selectedDateTime).format('mm');
    const _id = UNIQUE_ID++;

    AlarmApp.setAlarm(
      _id.toString(),
      _hours.toString(),
      _minutes.toString(),
      this.state.repeat,
      this.state.vibrate,
      this.state.text
    );

    const alarmsList = {
      ...this.state.alarmsList,
      [_id]: {
        selectedDateTime: this.state.selectedDateTime,
        repeat: this.state.repeat,
        vibrate: this.state.vibrate,
        text: this.state.text
      },
    };

    this.setState({ alarmsList, ...initialCreateAlarmSettings  });
  };

  removeAlarm = id => {
    const _data = this.state.alarmsList[id];
    const _hours = moment(_data.selectedDateTime).format('HH');
    const _minutes = moment(_data.selectedDateTime).format('mm');

    AlarmApp.removeAlarm(
      id,
      _hours,
      _minutes,
      _data.repeat,
      _data.vibrate,
      _data.text
    );
    const _updatedAlarmsList = this.state.alarmsList;
    delete _updatedAlarmsList[id];
    this.setState({ alarmsList: _updatedAlarmsList });
  };

  showDateTimePicker = () => {
    this.setState({ isDateTimePickerVisible: true });
  };

  hideDateTimePicker = () => {
    this.setState({ isDateTimePickerVisible: false });
  };

  handleDatePicked = date => {
    this.setState({ selectedDateTime: moment(new Date(date.toString())) });
    this.hideDateTimePicker();
  };

  formatDate = date => date.format('HH:mm');

  renderAlarmsList = () => {
    return Object.keys(this.state.alarmsList).map((id) => {
      const _data = this.state.alarmsList[id];
      return (
        <View key={id} style={styles.alarmContainer}>
          <Text style={styles.alarmText}>{_data['text']}</Text>
          <View style={styles.alarmData}>
            <Text>{this.formatDate(_data['selectedDateTime'])}</Text>
            {_data['repeat'] && (<Text>Repeat every day</Text>)}
            {_data['vibrate'] && (<Text>Vibration On</Text>)}
            <Button title="Remove"
                    onPress={() => this.removeAlarm(id)} />
          </View>
        </View>
      );
    });
  };

  render() {
    return (
      <>
        <View style={styles.sectionContainer}>
          <Text>Time: { this.state.selectedDateTime && this.formatDate(this.state.selectedDateTime) }</Text>
          <Button
            title={this.state.selectedDateTime ? 'Update time' : 'Select time'}
            onPress={this.showDateTimePicker} />
          <DateTimePicker
            isVisible={this.state.isDateTimePickerVisible}
            mode='time'
            onConfirm={this.handleDatePicked}
            onCancel={this.hideDateTimePicker}
          />
        </View>
        <View style={styles.sectionContainer}>
          <Text>Repeat every day this time: </Text>
          <CheckBox
            value={this.state.repeat}
            onValueChange={(repeat) => this.setState({repeat})}
          />
        </View>
        <View style={styles.sectionContainer}>
          <Text>Vibration: </Text>
          <CheckBox
            value={this.state.vibrate}
            onValueChange={(vibrate) => this.setState({vibrate})}
          />
        </View>
        <View style={styles.sectionContainer}>
          <Text>Alarm text: </Text>
          <TextInput
            style={styles.alarmTextInput}
            onChangeText={(text) => this.setState({text})}
            value={this.state.text}
          />
        </View>
        <View style={styles.buttonContainer}>
          <Button disabled={!this.state.selectedDateTime}
                  title="Set alarm"
                  onPress={this.saveAlarm} />
        </View>
        {this.renderAlarmsList()}
      </>
    );
  }
}

const paddings = {
  horizontal: 24,
  vertical: 12,
};

const styles = StyleSheet.create({
  sectionContainer: {
    alignItems: 'center',
    borderTopColor: 'gray',
    borderTopWidth: 1,
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: paddings.horizontal,
    paddingVertical: paddings.vertical,
  },
  buttonContainer: {
    paddingBottom: paddings.vertical,
  },
  alarmContainer: {
    backgroundColor: Colors.lighter,
    borderTopColor: Colors.white,
    borderTopWidth: 1,
    paddingVertical: paddings.vertical,
  },
  alarmText: {
    paddingHorizontal: paddings.horizontal,
  },
  alarmData: {
    alignItems: 'center',
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: paddings.horizontal,
  },
  alarmTextInput: {
    borderColor: 'gray',
    borderWidth: 1,
    height: 40,
    paddingHorizontal: paddings.horizontal/2,
    width: 200,
  },
});
