// tslint:disable-next-line:interface-over-type-literal
export type RunCard = {
  totalSamplecount?: number;
  deviceRunId?: string;
  runStatus?: string;
  processStepName?: string;
  stripInfo?: StripInfo[];
  totalSampleFlagCount?: number;
  totalSampleFailedCount?: number;
  deviceId?: string;
};

// tslint:disable-next-line:interface-over-type-literal
export type StripInfo = {
  stripLabelName?: string;
  stripLabelInfo?: string;
  stripSamples?: number;
};

// tslint:disable-next-line:interface-over-type-literal
export type RunCardQueryParam = {
  assayTypeDetail: string;
  processStepVal: string;
  statusVal?: string;
};

export const list = 'list';
export const incompletedwfs = 'incompletedwfs';
export enum STATUS_CODES {
  'COMPLETED' = 'Completed',
  'COMPLETED_WITH_FLAGS' = 'Completed with flags',
  'IN_PROGRESS' = 'InProgress',
  'ABORTED' = 'Aborted',
  'FAILED' = 'Failed',
  'ONGOING' = 'Ongoing',
  'PENDING' = 'Pending'
}

export const runCardAPICalls = {
  'NA Extraction': [
    `${list}?assayType=NIPTDPCR&processstep=NA%20Extraction&status=${
      STATUS_CODES.FAILED
    }`,
    `${incompletedwfs}?assayType=NIPTDPCR&processstep=NA%20Extraction
    }`
  ]
};
