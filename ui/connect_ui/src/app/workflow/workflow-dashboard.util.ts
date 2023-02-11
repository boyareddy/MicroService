import { STATUS_CODES } from './model';

export const getCss = (runstatus: string) => {
  const status = runstatus.toLowerCase();
  const cssClasses: CSSClasses = {} as CSSClasses;
  switch (status) {
    case 'completed':
      cssClasses.color = colorCodes.Completed;
      cssClasses.transistionMode = TransistionModes.DETERMINATE;
      cssClasses.transistionValue = TransistionValue.MAX;
      break;
    case 'completed with flags':
      cssClasses.color = colorCodes.Completed;
      cssClasses.transistionMode = TransistionModes.DETERMINATE;
      cssClasses.transistionValue = TransistionValue.MAX;
      break;
    case 'aborted':
      cssClasses.color = colorCodes.Aborted;
      cssClasses.transistionMode = TransistionModes.DETERMINATE;
      cssClasses.transistionValue = TransistionValue.MAX;
      break;
    case 'failed':
      cssClasses.color = colorCodes.Aborted;
      cssClasses.transistionMode = TransistionModes.DETERMINATE;
      cssClasses.transistionValue = TransistionValue.MAX;
      break;
    case 'inprogress':
      cssClasses.color = colorCodes.InProgress;
      cssClasses.transistionMode = TransistionModes.INDETERMINATE;
      cssClasses.transistionValue = TransistionValue.MIN;
      break;
    case 'ongoing':
      cssClasses.color = colorCodes.InProgress;
      cssClasses.transistionMode = TransistionModes.INDETERMINATE;
      cssClasses.transistionValue = TransistionValue.MIN;
      break;
    case 'open':
      cssClasses.color = colorCodes.Pending;
      cssClasses.transistionMode = TransistionModes.DETERMINATE;
      cssClasses.transistionValue = TransistionValue.MAX;
      break;
    case 'pending':
      cssClasses.color = colorCodes.Pending;
      cssClasses.transistionMode = TransistionModes.DETERMINATE;
      cssClasses.transistionValue = TransistionValue.MAX;
      break;
    default:
      cssClasses.color = colorCodes.Pending;
      cssClasses.transistionMode = TransistionModes.INDETERMINATE;
      cssClasses.transistionValue = TransistionValue.MIN;
      break;
  }
  return cssClasses;
};

export const labelKeys = (processStepName: string, runStatus: string, assay: string, wfmsFlag: string) => {
  let labelName = '';
  const status = runStatus.indexOf('Completed') > -1 ? STATUS_CODES.COMPLETED : runStatus;
  const key = `${processStepName}-${status}-${assay}`;
  console.log(processStepName, status, assay);
  switch (key) {
    /* NA Extraction HTP */
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.SINLGE_TUBE_ID;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
    console.log(wfmsFlag);
      labelName = labels.STRIP_ID;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.STRIP_ID;
      break;

    /*NA Extraction DPCR  */
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.PENDING}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.WEEL_PLATE_ID;
      break;

    /* Library Preparation NIPTHTP */
    /* Lp pre PCR */
    case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.STRIP_ID;
      break;
    case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.WEEL_PLATE_ID;
      break;

    /* Lp post PCR */
    case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.WEEL_PLATE_ID;
      break;

    /* Lp sequencing prep */
    case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.SEQUENCING_COMPLEX_ID;
      break;
    case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.SEQUENCING_COMPLEX_ID;
      break;

    /* Library Preparion NIPTDPCR */
    case `${processStepNames.Library_Preparation}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.WEEL_PLATE_ID;
      break;
    case `${processStepNames.Library_Preparation}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.DPCR_PLATE;
      break;
    case `${processStepNames.Library_Preparation}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.DPCR_PLATE;
      break;

    /* Sequencing for NIPTHTP */
    case `${processStepNames.Sequencing}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.POOL_TUBE_ID;
      break;
    case `${processStepNames.Sequencing}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.CONSUMABLE_DEVICE_ID;
      break;
    case `${processStepNames.Sequencing}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTHTP}`:
      labelName = labels.CONSUMABLE_DEVICE_ID;
      break;

    /* dpcr for NIPTDPCR */
    case `${processStepNames.DPCR}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.DPCR_PLATE_ID;
      break;
    case `${processStepNames.DPCR}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.OUTPUT_FILE_LOCATION;
      break;
    case `${processStepNames.DPCR}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTDPCR}`:
      labelName = labels.OUTPUT_FILE_LOCATION;
      break;
    default:
      labelName = labels.STRIP_ID;
  }
  return labelName;
};

export const getUpdatedPRocessStepName = (processStepName) => {
  let newStep = '';
  switch (processStepName) {
    case processStepNames.NA_Extraction:
      newStep = processStepNames.Library_Preparation;
      break;
    case processStepNames.LP_Sequencing_Prep:
        newStep = processStepNames.Sequencing;
        break;
    case processStepNames.Library_Preparation:
        newStep = processStepNames.DPCR;
        break;
  }
  return newStep;
};


export const UpdatedlabelKeys = (processStepName: string, runStatus: string, assay: string, wfmsFlag: string, count: number) => {
  let labelName = '';
  let containerIcon = '';
  const status = runStatus.indexOf('Completed') > -1 ? STATUS_CODES.COMPLETED : runStatus;
  const key = `${processStepName}-${status}-${assay}`;
  switch (key) {
    /* NA Extraction HTP */
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      if (count === 1) {
        labelName = LocalizationKeys.SINGLE_TUBE;
      } else {
        labelName = LocalizationKeys.SINGLE_TUBES;
      }
      containerIcon = LablesImagePath.SINGLE_TUBE;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      if (count === 1) {
        labelName = LocalizationKeys.TUBE_STRIP_8;
      } else {
        labelName = LocalizationKeys.TUBE_STRIPS_8;
      }
      containerIcon = LablesImagePath._8TUBE_STRIP;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = null;
      break;

    /*NA Extraction DPCR  */
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.PENDING}-${BasicAssayType.NIPTDPCR}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.NA_Extraction}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTDPCR}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
    containerIcon = LablesImagePath._96WELL_PLATE;
      break;

    /* Library Preparation NIPTHTP */
    /* Lp pre PCR */
    case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      if (count === 1) {
        labelName = LocalizationKeys.TUBE_STRIP_8;
      } else {
        labelName = LocalizationKeys.TUBE_STRIPS_8;
      }
      containerIcon = LablesImagePath._8TUBE_STRIP;
      break;
    case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      if (count === 1) {
        labelName = LocalizationKeys.WELL_PLATE;
      } else {
        labelName = LocalizationKeys.WELL_PLATES;
      }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = null;
      break;

    /* Lp post PCR */
    case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = null;
      break;

    /* Lp sequencing prep */
    case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      if (count === 1) {
        labelName = LocalizationKeys.SINLGE_POOL_TUBE;
      } else {
        labelName = LocalizationKeys.SINLGE_POOL_TUBES;
      }
      containerIcon = LablesImagePath.SINGLE_TUBE;
      break;
    case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
      labelName = null;
      break;

    /* Library Preparion NIPTDPCR */
    case `${processStepNames.Library_Preparation}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTDPCR}`:
    if (count === 1) {
      labelName = LocalizationKeys.WELL_PLATE;
    } else {
      labelName = LocalizationKeys.WELL_PLATES;
    }
      containerIcon = LablesImagePath._96WELL_PLATE;
      break;
    case `${processStepNames.Library_Preparation}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
      if (count === 1) {
        labelName = LocalizationKeys.DPCR_PLATE;
      } else {
        labelName = LocalizationKeys.DPCR_PLATES;
      }
      containerIcon = LablesImagePath.DPCR_PLATE;
      break;
    case `${processStepNames.Library_Preparation}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTDPCR}`:
      labelName = null;
      break;

    /* Sequencing for NIPTHTP */
    case `${processStepNames.Sequencing}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTHTP}`:
      labelName = LocalizationKeys.SINLGE_POOL_TUBE;
      containerIcon = LablesImagePath.SINGLE_TUBE;
      break;
    case `${processStepNames.Sequencing}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
      labelName = LocalizationKeys.CONSUMABLE_DEVICE_ID;
      containerIcon = LablesImagePath.CONSUMABLE_DEVICE_ID;
      break;
    case `${processStepNames.Sequencing}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTHTP}`:
      labelName = LocalizationKeys.CONSUMABLE_DEVICE_ID;
      containerIcon = LablesImagePath.CONSUMABLE_DEVICE_ID;
      break;

    /* dpcr for NIPTDPCR */
    case `${processStepNames.DPCR}-${STATUS_CODES.IN_PROGRESS}-${BasicAssayType.NIPTDPCR}`:
      if (count === 1) {
        labelName = LocalizationKeys.DPCR_PLATE;
      } else {
        labelName = LocalizationKeys.DPCR_PLATES;
      }
      containerIcon = LablesImagePath.DPCR_PLATE;
      break;
    case `${processStepNames.DPCR}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
    if (count === 1) {
      labelName = LocalizationKeys.DPCR_PLATE;
    } else {
      labelName = LocalizationKeys.DPCR_PLATES;
    }
    containerIcon = LablesImagePath.DPCR_PLATE;
      break;
    case `${processStepNames.DPCR}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTDPCR}`:
    if (count === 1) {
      labelName = LocalizationKeys.DPCR_PLATE;
    } else {
      labelName = LocalizationKeys.DPCR_PLATES;
    }
    containerIcon = LablesImagePath.DPCR_PLATE;
      break;
    default:
      labelName = labels.STRIP_ID;
      containerIcon = LablesImagePath.SINGLE_TUBE;
  }
  return {labelName: labelName, containerIcon: containerIcon };
};

export const getProcessStatus = (status: string) => {
  const oldstatus = status.toLowerCase();
  const ongoingStatus = ['inprogress', 'inprocess', 'started'];
  return ongoingStatus.indexOf(oldstatus) > -1 ? STATUS_CODES.ONGOING : status;
};

export const getRunDetailsLableInfo = (stepName: string, runStatus: string, assayType: any) => {
    const status = runStatus.indexOf('Completed') > -1 ? STATUS_CODES.COMPLETED : runStatus;
    const key = `${stepName}-${status}-${assayType}`;

    const displayedColumns6 = ['accessioningId', 'assaytype', 'position', 'status', 'flags', 'comments'];
    const columnsToDisplay6 = ['Accessioning ID', 'Assay', 'Position', 'Status', 'Flags', 'Comments'];


    const displayedColumns5 = ['accessioningId', 'assaytype', 'status', 'flags', 'comments'];
    const columnsToDisplay5 = ['Accessioning ID', 'Assay', 'Status', 'Flags', 'Comments'];

    let labelName: any;
    let displayedColumns: any;
    let columnsToDisplay: any;
    let labelImage;
    switch (key) {
      /* NA Extraction HTP */
      case `${processStepNames.NA_Extraction}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTHTP}`:
        labelName = null;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        break;
      case `${processStepNames.NA_Extraction}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.STRIP_IDS;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_8TUBE_STRIP;
        break;
      case `${processStepNames.NA_Extraction}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.STRIP_IDS;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_ABORTED;
        break;

      /*NA Extraction DPCR  */
      case `${processStepNames.NA_Extraction}-${STATUS_CODES.PENDING}-${BasicAssayType.NIPTDPCR}`:
        labelName = null;
        break;
      case `${processStepNames.NA_Extraction}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;
      case `${processStepNames.NA_Extraction}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTDPCR}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;

      /* Library Preparation NIPTHTP */
      /* Lp pre PCR */
      case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.STRIP_IDS;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_8TUBE_STRIP;
        break;
      case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;
      case `${processStepNames.LP_Pre_PCR}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_ABORTED;
        break;

      /* Lp post PCR */
      case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;
      case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;
      case `${processStepNames.LP_Post_PCRPooling}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_ABORTED;
        break;

      /* Lp sequencing prep */
      case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;
      case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.SINLGE_TUBE;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_SINGLE_TUBE;
        break;
      case `${processStepNames.LP_Sequencing_Prep}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.SINLGE_TUBE;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_ABORTED;
        break;

      /* Library Preparion NIPTDPCR */
      case `${processStepNames.Library_Preparation}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTDPCR}`:
        labelName = labels.PLATE_ID;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
        break;
      case `${processStepNames.Library_Preparation}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
        labelName = labels.CHIP_ID;
        columnsToDisplay = columnsToDisplay5;
        displayedColumns = displayedColumns5;
        labelImage = LablesImagePath.RUN_DPCR_PLATE;
        break;
      case `${processStepNames.Library_Preparation}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTDPCR}`:
        labelName = labels.CHIP_ID;
        columnsToDisplay = columnsToDisplay5;
        displayedColumns = displayedColumns5;
        labelImage = LablesImagePath.RUN_ABORTED;
        break;

      /* Sequencing for NIPTHTP */
      case `${processStepNames.Sequencing}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.POOL_TUBE_ID;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_SINGLE_TUBE;
        break;
      case `${processStepNames.Sequencing}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTHTP}`:
        labelName = labels.CONSUMABLE_DEVICE_ID;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_CONSUMABLE_DEVICE_ID;
        break;
      case `${processStepNames.Sequencing}-${STATUS_CODES.FAILED}-${BasicAssayType.NIPTHTP}`:
        labelName =  labels.CONSUMABLE_DEVICE_ID;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_CONSUMABLE_DEVICE_ID;
        break;

      /* dpcr for NIPTDPCR */
      case `${processStepNames.DPCR}-${STATUS_CODES.ONGOING}-${BasicAssayType.NIPTDPCR}`:
        labelName = labels.DPCR_PLATE_Id;
        displayedColumns = displayedColumns6;
        columnsToDisplay = columnsToDisplay6;
        labelImage = LablesImagePath.RUN_DPCR_PLATE;
        break;
      case `${processStepNames.DPCR}-${STATUS_CODES.COMPLETED}-${BasicAssayType.NIPTDPCR}`:
        labelName = null;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_DPCR_PLATE;
        break;
      case `${processStepNames.DPCR}-${STATUS_CODES.ABORTED}-${BasicAssayType.NIPTDPCR}`:
        labelName = null;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_DPCR_PLATE;
        break;
      default:
        labelName = labels.STRIP_IDS;
        displayedColumns = displayedColumns5;
        columnsToDisplay = columnsToDisplay5;
        labelImage = LablesImagePath.RUN_96WELL_PLATE;
    }
    return {labelName, columnsToDisplay, displayedColumns, labelImage};
};



/**
 * To create the image path absed on processstepname and run status
 * @param processStepName processStepName
 * @param runStatus status of run
 */
export const imgPath = (processStepName: string, runStatus: string, assayType: string) => {
  const niptHtpLpNames = ['LP Pre PCR', 'LP Post PCR/Pooling', 'LP Sequencing Prep'];
  const lpProcessNames = 'Library Preparation';
  const stepName =
    niptHtpLpNames.indexOf(processStepName) > -1
      ? lpProcessNames
      : processStepName;
  const newStepName = stepName.split(' ').join('');
  const newStatusName =
    runStatus.indexOf('Completed') > -1 ? 'Completed' : runStatus;
    let status;
    if (processStepName === processStepNames.NA_Extraction) {
      if (assayType === BasicAssayType.NIPTDPCR) {
        status = `${newStatusName.toLowerCase()}mp96`;
      } else if (assayType === BasicAssayType.NIPTHTP) {
        status = `${newStatusName.toLowerCase()}mp24`;
      }
    } else {
      status = newStatusName.toLowerCase();
    }
  return `${newStepName}/${status}.svg`;
};

/* Basic color codes to display cards */
export enum colorCodes {
  Completed = 'completed',
  Aborted = 'aborted',
  InProgress = 'ongoing',
  Pending = 'pending'
}

/* Label name keys in I18N */
export enum labels {
  STRIP_ID = 'stripId',
  MULTIPLE_IDS = 'multipleIds',
  SINLGE_TUBE_ID = 'singleTubeIds',
  WEEL_PLATE_ID = 'wellPlate',
  SEQUENCING_COMPLEX_ID = 'sequencingComplexId',
  OUTPUT_FILE_LOCATION = 'outputFileLocation',
  DPCR_PLATE = 'dpcrPlate',
  DPCR_PLATE_ID = 'dpcrPlateId',
  PLATE_ID = 'plateID',
  CHIP_ID = 'chipId',
  SINLGE_TUBE = 'Singletube',
  DPCR_PLATE_Id = 'dpcrPlateID',
  STRIP_IDS = 'stripID',
  POOL_TUBE_ID = 'poolTubeID',
  CONSUMABLE_DEVICE_ID = 'consumableDeviceId'
}

/* Assay type process step names */
export enum processStepNames {
  NA_Extraction = 'NA Extraction',
  LP_Pre_PCR = 'LP Pre PCR',
  LP_Post_PCRPooling = 'LP Post PCR/Pooling',
  LP_Sequencing_Prep = 'LP Sequencing Prep',
  Sequencing = 'Sequencing',
  Analysis = 'Analysis',
  Library_Preparation = 'Library Preparation',
  DPCR = 'dPCR',
  Read_Out = 'Read out'
}

/* Progressbar animation states */
export enum TransistionModes {
  INDETERMINATE = 'indeterminate',
  DETERMINATE = 'determinate'
}

/* Progressbar animation values */
enum TransistionValue {
  MIN = 70,
  MAX = 100
}

export enum ConstantNames {
  Library_Preparation = 'Library Preparation',
  Na_Extraction_Open_Status_key = 'open,senttodevice',
  Consumable_Device_Part_Number= 'consumableDevicePartNumber',
  Internal_Control = 'Internal Control',
  Reagents = 'Reagents',
  ComplexId = 'complexId',
  Lane_No = 'laneNo',
  Dpcr_Analyzer_FilePath = 'dpcr analyzer filepath'
}

export enum LowerCaseStepNames {
  na_extraction = 'na extraction',
  library_preparation = 'library preparation',
  lp_pre_pcr = 'lp pre pcr',
  lp_post_pcrpooling = 'lp post pcr/pooling',
  lp_sequencing_prep = 'lp sequencing prep',
  sequencing = 'sequencing',
  dpcr = 'dpcr',
  analysis = 'analysis'
}

export enum LowerCaseStatus {
  completed = 'completed',
  failed = 'failed',
  aborted = 'aborted',
  ongoing = 'ongoing',
  pending = 'pending',
  inprogress = 'inprogress',
  passed = 'passed',
}

export enum LocalizationKeys {
  SINGLE_TUBE = 'singleTube',
  SINGLE_TUBES = 'singleTubes',
  TUBE_STRIP_8 = '8tubeStrip',
  TUBE_STRIPS_8 = '8tubeStrips',
  WELL_PLATE = 'wellPlate',
  WELL_PLATES = 'wellPlates',
  SINLGE_POOL_TUBE = 'singlePoolTube',
  SINLGE_POOL_TUBES = 'singlePoolTubes',
  CONSUMABLE_DEVICE_ID = 'consumableDeviceId',
  DPCR_PLATE = '8dpcrPlate',
  DPCR_PLATES = '8dpcrPlates'
}

export enum LablesImagePath {
  SAMPLE_TUBE = 'assets/Images/sampleTube.png',
  PLATE_ID = 'assets/Images/plate.png',
  CONNECTIONS = 'assets/Images/connections.png',
  ABORTED = 'assets/Images/run_aborted.png',

  RUN_96WELL_PLATE = 'assets/Images/Containers/RunDetails/96wellPlate.svg',
  RUN_8TUBE_STRIP = 'assets/Images/Containers/RunDetails/8tubeStrip.svg',
  RUN_CONSUMABLE_DEVICE_ID = 'assets/Images/Containers/RunDetails/consumableDeviceId.svg',
  RUN_DPCR_PLATE = 'assets/Images/Containers/RunDetails/dpcrPlate.svg',
  RUN_SINGLE_TUBE = 'assets/Images/Containers/RunDetails/singleTube.svg',
  RUN_ABORTED = 'assets/Images/Containers/RunDetails/aborted.svg',

  _96WELL_PLATE = 'assets/Images/Containers/96wellPlate.svg',
  _8TUBE_STRIP = 'assets/Images/Containers/8tubeStrip.svg',
  CONSUMABLE_DEVICE_ID = 'assets/Images/Containers/consumableDeviceId.svg',
  DPCR_PLATE = 'assets/Images/Containers/dpcrPlate.svg',
  SINGLE_TUBE = 'assets/Images/Containers/singleTube.svg'
}

export const processStepDetails = ['NA Extraction', 'Library Preparation', 'Read out', 'Analysis'];
export const htpLPSteps = ['LP Pre PCR', 'LP Post PCR/Pooling', 'LP Sequencing Prep'];
export const readOutSteps = ['dPCR', 'Sequencing'];
export const completedTabInfo = ['archived', 'partiallymoved'];

/* BAsic Assay Types */
export enum BasicAssayType {
  NIPTHTP = 'NIPTHTP',
  NIPTDPCR = 'NIPTDPCR'
}

/* Interface for creating classes */
interface CSSClasses {
  color: string;
  transistionMode: string;
  transistionValue: number;
}

interface LabelsInfo {
  labelName: string;
  displayedColumns: any;
}
