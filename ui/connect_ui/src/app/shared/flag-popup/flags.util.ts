import { deviceTypes } from './flag.model';


export const DEVICE_FLAGS = [{
    assayWorkflowType: 'nipthtp-lp-post-pcr-pooling',
    deviceType: deviceTypes.LP24
}, {
    assayWorkflowType: 'nipthtp-lp-pre-pcr-pooling',
    deviceType: deviceTypes.LP24
}, {
    assayWorkflowType: 'nipthtp-lp-sequencing-prep',
    deviceType: deviceTypes.LP24
}, {
    assayWorkflowType: 'nipthtp-lp-pre-pcr',
    deviceType: deviceTypes.LP24
}, {
    assayWorkflowType: 'niptdpcr-library-preparation',
    deviceType: deviceTypes.LP24
}, {
    assayWorkflowType: 'nipthtp-na-extraction',
    deviceType: deviceTypes.MP24
}, {
    assayWorkflowType: 'niptdpcr-na-extraction',
    deviceType: deviceTypes.MP96
}, {
    assayWorkflowType: 'nipthtp-dpcr',
    deviceType: deviceTypes.DCPR
}, {
    assayWorkflowType: 'niptdpcr-dpcr',
    deviceType: deviceTypes.DCPR
}];


export const getDeviceType = (sample) => {
    let deviceType;
    const flowType = sample.processStepName ? sample.processStepName : sample.workflowType;
    const assayDetails = sample.assayType ? sample.assayType : sample.assaytype;
    const assayWorkflowType = `${assayDetails.toLowerCase()}-${flowType.trim().split(' ').join('-').split('/').join('-').toLowerCase()}`;
    deviceType = DEVICE_FLAGS.filter(deviceFlag => deviceFlag.assayWorkflowType === assayWorkflowType)[0].deviceType;
    return deviceType;
};
