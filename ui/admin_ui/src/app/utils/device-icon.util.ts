import { Device } from "../device-list/device-list.component";
import { NewDevice } from "../models/device.model";

export enum EQUIPMENT_STATUS{
  "PU" = "Powered Up",
  "IN" = "Initializing",
  "ID" = "Idle",
  "OP" = "Normal Operation",
  "PD" = "Paused",
  "SD" = "Shutting down",
  "DI" = "Diagnose",
  "MA" = "Maintenance",
  "RS" = "Ready to start",
  "FL" = "Failure"

}

export const setIcon = (status): string => {
    switch(status?status.split('/')[0].toLowerCase():null){
      case('online'):
        return 'online';
      case('warning'):
        return 'warning';
      case('error'):
        return 'error';
      case('offline'):
        return 'offline';
      case('maintenance'):
        return 'offline';
      default:
        return null;
    }
  }

  export const setCss = (status): string => {
    return status ? status.toLowerCase().replace(' ', '_').replace('/', '_') : null;
  }

  export const formatDeviceList = (deviceList: any[], registrationStatus, deviceStatus): Device[] => {
    let formattedList: Device[] = [];
    
    deviceList.forEach((device: any) => {

      // Check whether the device is registered or not.
      if(device && device.attributes && device.status === deviceStatus && device.attributes.isRegistered === registrationStatus){

        let formattedDevice: Device = {} as Device;
        formattedDevice.deviceName = device ? device.name : null;
        formattedDevice.deviceType = device && device.deviceType ? device.deviceType.name : null;
        formattedDevice.status = device && device.attributes && device.attributes.deviceStatus ? device.attributes.deviceStatus.charAt(0).toUpperCase() + device.attributes.deviceStatus.slice(1) : null;
        formattedDevice.equipmentStatus = device && device.attributes && device.attributes.deviceStatus && device.attributes.deviceStatus.toLowerCase() !== 'offline' && device.attributes && device.attributes.deviceSubStatus ? EQUIPMENT_STATUS[device.attributes.deviceSubStatus] : null;
        formattedDevice.serialNo = device ? device.serialNo : null;
        formattedDevice.deviceId = device ? device.deviceId : null;
        formattedDevice.state = device ? device.state : null;
        
        formattedList.push(formattedDevice);
      }
    });

    return formattedList;
  }

  export const validDevice = (response: any) => {
    return response && response[0] && response[0].status ? response[0] : null
  }

  export const deviceCompleteStatus = (device: NewDevice) => {
    if(device.attributes.deviceSubStatus && device.attributes.deviceStatus.toLowerCase() !== 'offline'){
      return `${device.attributes.deviceStatus.charAt(0).toUpperCase() + device.attributes.deviceStatus.slice(1)}/${EQUIPMENT_STATUS[device.attributes.deviceSubStatus]}`;
    }else{
      return device.attributes.deviceStatus.charAt(0).toUpperCase() + device.attributes.deviceStatus.slice(1);
    }
  }