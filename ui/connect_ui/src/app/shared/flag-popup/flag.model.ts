export interface Sample {
  processStepName: string;
  assayType: string;
  flag: string;
}

export enum deviceTypes {
  LP24 = 'lp24',
  MP24 = 'mp24',
  MP96 = 'mp96',
  DCPR = 'dpcr'
}
