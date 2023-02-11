import { getCss, processStepNames,
    BasicAssayType, labels, labelKeys,
    TransistionModes, getProcessStatus, getRunDetailsLableInfo,
    LablesImagePath, imgPath } from './workflow-dashboard.util';
import { STATUS_CODES } from './model';

describe('WorkflowDashboardUtil', () =>  {
    const displayedColumns6 = ['accessioningId', 'assaytype', 'position', 'status', 'flags', 'comments'];
    const columnsToDisplay6 = ['Accessioning ID', 'Assay', 'Position', 'Status', 'Flags', 'Comments'];
    const displayedColumns5 = ['accessioningId', 'assaytype', 'status', 'flags', 'comments'];
    const columnsToDisplay5 = ['Accessioning ID', 'Assay', 'Status', 'Flags', 'Comments'];

    it('should get Css based on run status completed', () =>  {
        const runStatus = 'completed';
        const expop = {
            color: 'completed',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });


    it('should get Css based on run status completed with flags', () =>  {
        const runStatus = 'completed with flags';
        const expop = {
            color: 'completed',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status failed', () =>  {
        const runStatus = 'failed';
        const expop = {
            color: 'aborted',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status aborted', () =>  {
        const runStatus = 'aborted';
        const expop = {
            color: 'aborted',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status inprogress', () =>  {
        const runStatus = 'inprogress';
        const expop = {
            color: 'ongoing',
            transistionMode: TransistionModes.INDETERMINATE,
            transistionValue: 70
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status ongoing', () =>  {
        const runStatus = 'ongoing';
        const expop = {
            color: 'ongoing',
            transistionMode: TransistionModes.INDETERMINATE,
            transistionValue: 70
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status open', () =>  {
        const runStatus = 'open';
        const expop = {
            color: 'pending',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status open', () =>  {
        const runStatus = 'open';
        const expop = {
            color: 'pending',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status pending', () =>  {
        const runStatus = 'pending';
        const expop = {
            color: 'pending',
            transistionMode: TransistionModes.DETERMINATE,
            transistionValue: 100
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });

    it('should get Css based on run status default', () =>  {
        const runStatus = 'anything';
        const expop = {
            color: 'pending',
            transistionMode: TransistionModes.INDETERMINATE,
            transistionValue: 70
        };
        const op = getCss(runStatus);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
    });



    it('should get the label keys stepname na extraction inprogress and nipthtp', () => {
       const stepName = processStepNames.NA_Extraction;
       const inprogress = STATUS_CODES.IN_PROGRESS;
       const assay = BasicAssayType.NIPTHTP;
       const wfmsFlag = null;
       const expop = labels.SINLGE_TUBE_ID;
       expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
    });
    it('should get the label keys stepname na extraction completed and nipthtp', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.STRIP_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });
     it('should get the label keys stepname na extraction aborted and nipthtp', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.STRIP_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname na extraction pending and niptdpcr', () => {
         const stepName = processStepNames.NA_Extraction;
         const inprogress = STATUS_CODES.PENDING;
         const assay = BasicAssayType.NIPTDPCR;
         const wfmsFlag = null;
         const expop = labels.WEEL_PLATE_ID;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname na extraction completed and niptdpcr', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTDPCR;
        const wfmsFlag = null;
        const expop = labels.WEEL_PLATE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname na extraction aborted and niptdpcr', () => {
         const stepName = processStepNames.NA_Extraction;
         const inprogress = STATUS_CODES.ABORTED;
         const assay = BasicAssayType.NIPTDPCR;
         const wfmsFlag = null;
         const expop = labels.WEEL_PLATE_ID;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname Lp pre PCR inprogress and nipthtp', () => {
        const stepName = processStepNames.LP_Pre_PCR;
        const inprogress = STATUS_CODES.IN_PROGRESS;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.STRIP_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname Lp pre PCR completed and nipthtp', () => {
         const stepName = processStepNames.LP_Pre_PCR;
         const inprogress = STATUS_CODES.COMPLETED;
         const assay = BasicAssayType.NIPTHTP;
         const wfmsFlag = null;
         const expop = labels.WEEL_PLATE_ID;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname Lp pre PCR aborted and nipthtp', () => {
        const stepName = processStepNames.LP_Pre_PCR;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.WEEL_PLATE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname Lp post PCR inprogress and nipthtp', () => {
         const stepName = processStepNames.LP_Post_PCRPooling;
         const inprogress = STATUS_CODES.IN_PROGRESS;
         const assay = BasicAssayType.NIPTHTP;
         const wfmsFlag = null;
         const expop = labels.WEEL_PLATE_ID;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname Lp post PCR completed and nipthtp', () => {
        const stepName = processStepNames.LP_Post_PCRPooling;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.WEEL_PLATE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname Lp post PCR aborted and nipthtp', () => {
         const stepName = processStepNames.LP_Post_PCRPooling;
         const inprogress = STATUS_CODES.ABORTED;
         const assay = BasicAssayType.NIPTHTP;
         const wfmsFlag = null;
         const expop = labels.WEEL_PLATE_ID;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
/* *****8 */
      it('should get the label keys stepname Lp sequencing prep inprogress and nipthtp', () => {
        const stepName = processStepNames.LP_Sequencing_Prep;
        const inprogress = STATUS_CODES.IN_PROGRESS;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.WEEL_PLATE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname Lp sequencing prep completed and nipthtp', () => {
         const stepName = processStepNames.LP_Sequencing_Prep;
         const inprogress = STATUS_CODES.COMPLETED;
         const assay = BasicAssayType.NIPTHTP;
         const wfmsFlag = null;
         const expop = labels.SEQUENCING_COMPLEX_ID;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname Lp sequencing prep aborted and nipthtp', () => {
        const stepName = processStepNames.LP_Sequencing_Prep;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.SEQUENCING_COMPLEX_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname Library Preparion inprogress and niptdpcr', () => {
        const stepName = processStepNames.Library_Preparation;
        const inprogress = STATUS_CODES.IN_PROGRESS;
        const assay = BasicAssayType.NIPTDPCR;
        const wfmsFlag = null;
        const expop = labels.WEEL_PLATE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname Library Preparion completed and niptdpcr', () => {
         const stepName = processStepNames.Library_Preparation;
         const inprogress = STATUS_CODES.COMPLETED;
         const assay = BasicAssayType.NIPTDPCR;
         const wfmsFlag = null;
         const expop = labels.DPCR_PLATE;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname Library Preparion failed and niptdpcr', () => {
        const stepName = processStepNames.Library_Preparation;
        const inprogress = STATUS_CODES.FAILED;
        const assay = BasicAssayType.NIPTDPCR;
        const wfmsFlag = null;
        const expop = labels.DPCR_PLATE;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });

      it('should get the label keys stepname dpcr inprogress and niptdpcr', () => {
        const stepName = processStepNames.DPCR;
        const inprogress = STATUS_CODES.IN_PROGRESS;
        const assay = BasicAssayType.NIPTDPCR;
        const wfmsFlag = null;
        const expop = labels.DPCR_PLATE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname sequencing inprogress and nipthtp', () => {
        const stepName = processStepNames.Sequencing;
        const inprogress = STATUS_CODES.IN_PROGRESS;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.POOL_TUBE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname sequencing completed and nipthtp', () => {
        const stepName = processStepNames.Sequencing;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.CONSUMABLE_DEVICE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname sequencing failed and nipthtp', () => {
        const stepName = processStepNames.Sequencing;
        const inprogress = STATUS_CODES.FAILED;
        const assay = BasicAssayType.NIPTHTP;
        const wfmsFlag = null;
        const expop = labels.CONSUMABLE_DEVICE_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
     });

     it('should get the label keys stepname dpcr completed and niptdpcr', () => {
         const stepName = processStepNames.DPCR;
         const inprogress = STATUS_CODES.COMPLETED;
         const assay = BasicAssayType.NIPTDPCR;
         const wfmsFlag = null;
         const expop = labels.OUTPUT_FILE_LOCATION;
         expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });
      it('should get the label keys stepname dpcr aborted and niptdpcr', () => {
        const stepName = processStepNames.DPCR;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTDPCR;
        const wfmsFlag = null;
        const expop = labels.OUTPUT_FILE_LOCATION;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });

      it('should get the label keys dpcr', () => {
        const stepName = 'process';
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTDPCR;
        const wfmsFlag = null;
        const expop = labels.STRIP_ID;
        expect(labelKeys(stepName, inprogress, assay, wfmsFlag)).toBe(expop);
      });

      it('should get run status by passing old status as Completed', () => {
        const runstatus = 'Completed';
        expect(getProcessStatus(runstatus)).toBe(runstatus);
      });

      it('should get run status by passing old status as inprogress', () => {
        const runstatus = 'inprogress';
        expect(getProcessStatus(runstatus)).toBe(STATUS_CODES.ONGOING);
      });

      it('should get run status by passing old status as inprocess', () => {
        const runstatus = 'inprocess';
        expect(getProcessStatus(runstatus)).toBe(STATUS_CODES.ONGOING);
      });

      it('should get run status by passing old status as started', () => {
        const runstatus = 'started';
        expect(getProcessStatus(runstatus)).toBe(STATUS_CODES.ONGOING);
      });

      it('should get getRunDetailsLableInfo by passing stepname naextraction ongoing and NIPTHTP', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
                labelName: null,
                columnsToDisplay: columnsToDisplay5,
                displayedColumns: displayedColumns5
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname naextraction completed and NIPTHTP', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
                labelName: labels.STRIP_IDS,
                columnsToDisplay: columnsToDisplay6,
                displayedColumns: displayedColumns6,
                labelImage: LablesImagePath.SAMPLE_TUBE
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname naextraction aborted and NIPTHTP', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.STRIP_IDS,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.ABORTED
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });


      it('should get getRunDetailsLableInfo by passing stepname naextraction pending and NIPTDPCR', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.PENDING;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: null,
            };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname naextraction completed and NIPTDPCR', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname naextraction failed and NIPTDPCR', () => {
        const stepName = processStepNames.NA_Extraction;
        const inprogress = STATUS_CODES.FAILED;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp pre PCR ongoing and NIPTHTP', () => {
        const stepName = processStepNames.LP_Pre_PCR;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.STRIP_IDS,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.SAMPLE_TUBE
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp pre PCR completed and NIPTHTP', () => {
        const stepName = processStepNames.LP_Pre_PCR;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp pre PCR aborted and NIPTHTP', () => {
        const stepName = processStepNames.LP_Pre_PCR;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.ABORTED
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp post PCR Ongoing and NIPTHTP', () => {
        const stepName = processStepNames.LP_Post_PCRPooling;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp post PCR Completed and NIPTHTP', () => {
        const stepName = processStepNames.LP_Post_PCRPooling;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp post PCR aborted and NIPTHTP', () => {
        const stepName = processStepNames.LP_Post_PCRPooling;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.ABORTED
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp sequencing prep ongoing and NIPTHTP', () => {
        const stepName = processStepNames.LP_Sequencing_Prep;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.CONNECTIONS
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp sequencing prep completed and NIPTHTP', () => {
        const stepName = processStepNames.LP_Sequencing_Prep;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.SINLGE_TUBE,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.CONNECTIONS
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Lp sequencing prep aborted and NIPTHTP', () => {
        const stepName = processStepNames.LP_Sequencing_Prep;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.SINLGE_TUBE,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.ABORTED
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Library Preparion ongoing and NIPTDPCR', () => {
        const stepName = processStepNames.Library_Preparation;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.PLATE_ID,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Library Preparion completed and NIPTDPCR', () => {
        const stepName = processStepNames.Library_Preparation;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.CHIP_ID,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.PLATE_ID
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Library Preparion failed and NIPTDPCR', () => {
        const stepName = processStepNames.Library_Preparation;
        const inprogress = STATUS_CODES.FAILED;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.CHIP_ID,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.ABORTED
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Sequencing ongoing and NIPTHTP', () => {
        const stepName = processStepNames.Sequencing;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.POOL_TUBE_ID,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.CONNECTIONS
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Sequencing completed and NIPTHTP', () => {
        const stepName = processStepNames.Sequencing;
        const inprogress = STATUS_CODES.COMPLETED_WITH_FLAGS;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.CONSUMABLE_DEVICE_ID,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.CONNECTIONS
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname Sequencing failed and NIPTHTP', () => {
        const stepName = processStepNames.Sequencing;
        const inprogress = STATUS_CODES.FAILED;
        const assay = BasicAssayType.NIPTHTP;
          const expop = {
            labelName: labels.CONSUMABLE_DEVICE_ID,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.CONNECTIONS
              };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname dpcr ongoing and NIPTDPCR', () => {
        const stepName = processStepNames.DPCR;
        const inprogress = STATUS_CODES.ONGOING;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.DPCR_PLATE_Id,
            columnsToDisplay: columnsToDisplay6,
            displayedColumns: displayedColumns6,
            labelImage: LablesImagePath.SAMPLE_TUBE
        };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get getRunDetailsLableInfo by passing stepname dpcr completed and NIPTDPCR', () => {
        const stepName = processStepNames.DPCR;
        const inprogress = STATUS_CODES.COMPLETED;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: null,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.CONNECTIONS
        };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

     it('should get getRunDetailsLableInfo by passing stepname dpcr aborted and NIPTDPCR', () => {
        const stepName = processStepNames.DPCR;
        const inprogress = STATUS_CODES.ABORTED;
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: null,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.ABORTED
        };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

     it('should get getRunDetailsLableInfo by default', () => {
        const stepName = 'processStepNames';
        const inprogress = 'STATUS_CODES.ONGOING';
        const assay = BasicAssayType.NIPTDPCR;
          const expop = {
            labelName: labels.STRIP_IDS,
            columnsToDisplay: columnsToDisplay5,
            displayedColumns: displayedColumns5,
            labelImage: LablesImagePath.PLATE_ID
        };
        const op = getRunDetailsLableInfo(stepName, inprogress, assay);
        expect(JSON.stringify(op)).toBe(JSON.stringify(expop));
      });

      it('should get image path by passing stepname naextraction and completed', () => {
          const stepName = processStepNames.NA_Extraction;
          const status = 'Completed';
          expect(imgPath(stepName, status)).toBe('NAExtraction/completed.png');
      });

      it('should get image path by passing stepname LP Pre PCR and completed', () => {
          const stepName = processStepNames.LP_Pre_PCR;
          const status = 'Completed';
          expect(imgPath(stepName, status)).toBe('LibraryPreparation/completed.png');
      });

      it('should get image path by passing stepname LP Post PCR/Pooling and completed', () => {
          const stepName = processStepNames.LP_Post_PCRPooling;
          const status = 'Completed';
          expect(imgPath(stepName, status)).toBe('LibraryPreparation/completed.png');
      });

      it('should get image path by passing stepname LP Sequencing Prep and completed', () => {
          const stepName = processStepNames.LP_Sequencing_Prep;
          const status = 'Failed';
          expect(imgPath(stepName, status)).toBe('LibraryPreparation/failed.png');
      });
});
