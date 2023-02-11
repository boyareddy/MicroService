export type Sample = {
    processStepName: string;
    assayType: string;
    flag: string;
}

export const DEVICE_FLAGS = [{
    assayWorkflowType: "nipthtp-lp-post-pcr-pooling",
    deviceType: "lp24"
  },{
    assayWorkflowType: "nipthtp-lp-pre-pcr-pooling",
    deviceType: "lp24"
  },{
    assayWorkflowType: "nipthtp-lp-sequencing-prep",
    deviceType: "lp24"
  },{
    assayWorkflowType: "nipthtp-lp-pre-pcr",
    deviceType: "lp24"
  },{
    assayWorkflowType: "niptdpcr-library-preparation",
    deviceType: "lp24"
  },{
    assayWorkflowType: "nipthtp-na-extraction",
    deviceType: "lp24"
  },{
    assayWorkflowType: "niptdpcr-na-extraction",
    deviceType: "mp96"
  },{
    assayWorkflowType: "nipthtp-dpcr",
    deviceType: "dpcr"
  },{
    assayWorkflowType: "niptdpcr-dpcr",
    deviceType: "dpcr"
  }];

  export const getDeviceType = (sample) => {
    let deviceType;
    let flowType = sample.processStepName ? sample.processStepName : sample.workflowType;
    let assayWorkflowType = `${sample.assayType.toLowerCase()}-${flowType.trim().split(" ").join("-").split("/").join("-").toLowerCase()}`;
    deviceType = DEVICE_FLAGS.filter(deviceFlag => deviceFlag.assayWorkflowType === assayWorkflowType)[0].deviceType;

    return deviceType;
  }