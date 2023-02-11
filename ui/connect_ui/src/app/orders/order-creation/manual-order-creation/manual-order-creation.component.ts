import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AfterContentChecked, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, Renderer, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { MatDatepickerInputEvent, MatDialog, MatSnackBar } from '@angular/material';
import { DateAdapter, ErrorStateMatcher, MAT_DATE_FORMATS } from '@angular/material/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { AppDateAdapter, APP_DATE_FORMATS } from '../../../shared/date-format-provider/date.adapter';
import { CreateOrderErrorMessages } from '../../../shared/error-messages/createOrderErrorMessages';
import { HeaderInfo } from '../../../shared/header.model';
import { WarningRequiredService } from '../../../shared/inline-htmls/warning-required.service';
import { SharedService } from '../../../shared/shared.service';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { CreateOrderValidation } from '../../../shared/validations/create-order-validations';
import { OrderSections, RequiredFieldValidationsForOrder, SnackbarClasses, StringConstants, FormFieldNames } from '../../../standard-names/constants';
import { OrderService } from '../../order.service';
import { OrderFormatter } from '../../util/Order-input-formatter';



/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return (control.invalid && control.touched);
  }
}

/** Error when invalid control is dirty, touched, or submitted. */
export class MyNumberErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-manual-order-creation',
  templateUrl: './manual-order-creation.component.html',
  styleUrls: ['./manual-order-creation.component.scss'],
  providers: [
    {
        provide: DateAdapter, useClass: AppDateAdapter
    },
    {
        provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS
    }
  ]
})

export class ManualOrderCreationComponent implements OnInit, AfterContentChecked, OnDestroy {

  @ViewChild('panel', { read: ElementRef }) public panel: ElementRef<any>;
  @ViewChild('eggDonnerLabel') eggDonnerLabel: ElementRef;
  @ViewChild('dobInput') dobInput: ElementRef;

  public patientWarningSymbolInfo = false;
  public assayWarningSymbolInfo = false;
  public orderWarningSymbolInfo = false;


  public matcher = new MyErrorStateMatcher();
  public numberMatcher = new MyNumberErrorStateMatcher();
  public step = 0;
  public assayTypeList: any = [];
  public sampleTypeList: any = [];
  public fetusesValues: any = [];
  public eggDonnerValues: any = [];
  public ivfStatusValues: any = [];
  public gestationalWeeks: any = [];
  public gestationalDays: any = [];
  public isMultiTestNotChecked = false;
  public assayTypeId: number;
  public minDate: Date;
  public maxDate: Date;
  public collectionDateMax: Date;
  public receivedDateMax: Date;
  public maternal: any = [];
  public required = 'required';
  public orderId;
  public checkDuplicateId;
  public isValidAccountNumber;
  public eggDonorAgeValues;
  public correctJson = {};
  public headerInfo: HeaderInfo = {
    headerName: 'Edit order',
    isBackRequired: true,
    isCardsPage: true
  };
  public testOptionsArr;
  public formattedTestOptions: any = {};
  // public isNIPT = false;
  public orderStatus = 'unassigned';
  public isDisabledEggDonorField;
  public isDisabledEggDonorAgeField;
  /* editOrderData; */
  public editOrderForm;
  public oldFormData;
  public stepNumber;
  public updatedTestOptions;
  public isCollectionDateError = false;
  public isAvailableInCSV = false;

  /* PHI Info */
  public patientConfigInfo: any = [];
  public processStepNameInfo = 'unassinged';

  /* PHI Field Details */
  public commentsField =  RequiredFieldValidationsForOrder.COMMENTS;
  public maternal_ageField = RequiredFieldValidationsForOrder.MATERNAL_AGE;
  public gestational_ageField = RequiredFieldValidationsForOrder.GESTATIONAL_AGE;
  public iVF_statusField = RequiredFieldValidationsForOrder.IVF_STATUS;
  public egg_donorField = RequiredFieldValidationsForOrder.EGG_DONOR;
  public egg_donor_ageField = RequiredFieldValidationsForOrder.EGG_DONOR_AGE;
  public number_of_fetusField = RequiredFieldValidationsForOrder.NUMBER_OF_FETUS;
  public first_nameField = RequiredFieldValidationsForOrder.FIRST_NAME;
  public last_nameField = RequiredFieldValidationsForOrder.LAST_NAME;
  public medical_record_numberField = RequiredFieldValidationsForOrder.MEDICAL_REC_NUMBER;
  public dOBField = RequiredFieldValidationsForOrder.DOB;
  public referring_clinicianField = RequiredFieldValidationsForOrder.REFERRING_CLINICIAN;
  public laboratory_IDField = RequiredFieldValidationsForOrder.LABORATORY_ID;
  public other_clinicianField = RequiredFieldValidationsForOrder.OTHER_CLINICIAN;
  public clinic_nameField = RequiredFieldValidationsForOrder.CLINIC_NAME;
  public reason_for_ReferralField = RequiredFieldValidationsForOrder.REASON_FOR_REFERRAL;
  public accountField = RequiredFieldValidationsForOrder.ACCOUNT_NUMBER;

  public isWarningPatientInfo;

  /* order sections */
  public orderSectionName = OrderSections.ORDER;
  public assaySectionName = OrderSections.ASSAY;
  public patientSectionName = OrderSections.PATIENT;

  constructor(
    private formBuider: FormBuilder,
    private _orderService: OrderService,
    private _snackBar: MatSnackBar,
    private _router: Router,
    private _sharedService: SharedService,
    private _cdr: ChangeDetectorRef,
    private _location: Location,
    private _acroute: ActivatedRoute,
    private _el: ElementRef,
    private renderer: Renderer,
    private _snackBarSvc: SnackBarService,
    private _dialogBox: MatDialog,
    private _translate: TranslateService,
    private _warningService: WarningRequiredService
  ) {

  }
  public orderInfo: FormGroup;
  public marginTop;

  public ngOnInit() {
    this.onInitializingOrderForm();
    this._warningService.getLastProcessStepName();
    this.step = 0;
    console.log(this.orderInfo.value);
    /* Checking Purpose */
    this._acroute.params.subscribe(params => {
      this.orderId = params['id'];
      this.stepNumber = params['step'];
      if (this.orderId !== null && this.orderId !== undefined) {
        // tslint:disable-next-line:radix
        this.step = parseInt(this.stepNumber);
        this._orderService.getOrderInfo(this.orderId).subscribe(response => {
          this.editOrderForm = response;
          console.log(this.editOrderForm.order.assayType, 'assay Type');
          if (this.editOrderForm.order.assayType) {
            if (this.editOrderForm.order.assayType === 'NIPTDPCR') {
              this._orderService.validateCSVAccessioningId(this.editOrderForm.order.accessioningId).subscribe(res => {
                const responseForAvailable = res;
                if (responseForAvailable) {
                  this.isAvailableInCSV = true;
                } else {
                  this.isAvailableInCSV = false;
                }
              });
            }
          }
          this.getRequiredFieldsByAssay(this.editOrderForm.order.assayType);
          window.scrollTo(0, 0);
          this.orderStatus = this.editOrderForm.order.orderStatus;
          if (this.orderStatus.indexOf('workflow') > -1) {
            this.getProcessStepByAccessioningId(this.editOrderForm.order.accessioningId);
          }
          console.log(this.editOrderForm.order, 'oooooooooo');
          const ConvertedFormat = OrderFormatter(this.editOrderForm);
          this.checkDuplicateId = ConvertedFormat.order.accessioningId;
          this.isValidAccountNumber = ConvertedFormat.order.patient.accountNumber;
          const ivfStatus = ConvertedFormat.order.assay.ivfStatus;
          const eggDonarAge: string = ConvertedFormat.order.assay.eggDonor;
          if (ivfStatus.toLowerCase() === 'yes') {
            this.isDisabledEggDonorField = false;
            if (eggDonarAge.toLowerCase().indexOf('self') !== -1) {
              this.isDisabledEggDonorAgeField = false;
            } else {
              this.isDisabledEggDonorAgeField = true;
            }
          } else {
            this.isDisabledEggDonorField = true;
          }

          // delete ConvertedFormat.order.assay.testOptions;
          // ConvertedFormat.order.assay.testOptions = [];
          this.formattedTestOptions = ConvertedFormat.order.assay.testOptions;
          this.updatedTestOptions = Object.assign({}, this.formattedTestOptions);
          this.oldFormData = Object.assign({}, ConvertedFormat);
          if (!this._sharedService.getData('oldData')) {
            this._sharedService.setData('oldData', ConvertedFormat);
          }
          ConvertedFormat.order.assay['testOption'] = [];
          this.orderInfo.setValue(ConvertedFormat);
          this.headerInfo.headerName = 'Edit order';
          console.log('orders/order-details/' + this.orderId);
          this.headerInfo.navigateUrl = 'orders/order-details/' + this.orderId;
          /* this.step = this._sharedService.getData('orderStep'); */
          /* end of checking */
          this.loadAssayInformations();
        }, error => {
          console.log('getting order details', error);
        });
      } else {
        this.loadAssayInformations();
        // this.orderInfo.patchValue({
        //   order: {
        //     patient: {
        //       patientGender: 'Male'
        //     }
        //   }
        // });
      }
    });
    /* End of checking purpose */

    this.isDisabledEggDonorField = true;
    this.isDisabledEggDonorAgeField = true;
    this.maxDate = new Date();
    this.minDate = new Date();
    this.collectionDateMax = new Date;
    this.receivedDateMax = new Date();
    this.minDate.setFullYear(this.maxDate.getFullYear() - 99);
  }

  public loadAssayInformations() {
    this.onGettingAssayTypes();
  }

  /**
  * angular destroy the component when user navigates to any other route
  */
  public ngOnDestroy(): void {
    this._sharedService.deleteData('orderDetails');
    this._sharedService.deleteData('orderStep');
  }

  /**
  *  Angular checks the content projected into the component
  */
  public ngAfterContentChecked() {
    this._cdr.detectChanges();
    if (this.editOrderForm !== null && this.editOrderForm !== undefined) {
      if (this.orderInfo.dirty || (JSON.stringify(this.updatedTestOptions) !== JSON.stringify(this.formattedTestOptions))) {
        this.headerInfo.isBackRequired = false;
        this.headerInfo.isBackRequiredWarning = true;
        this.headerInfo.message = StringConstants.STRING('CANCEL_UPDATE_ORDER');
      }
    }
  }

  /**
  * Accordion which step should be opened based on id
  * @param index which accordion should disaply
  */

  public setStep(index: number) {
    this.step = index;
  }

  /**
  * user click on next increase the step number.
  */

  public nextStep() {
    this.step++;
  }

  /**
  * user click on previous descrease the step number
  */

  public prevStep() {
    this.step--;
  }

  /**
  * onInitializingOrderForm initializes the Create Order Form
  */
  public onInitializingOrderForm(): void {
    this.orderInfo = this.formBuider.group({

      /* Json Structure */
      'order': this.formBuider.group({
        'orderId': null,
        'accessioningId': ['', [CreateOrderValidation.accessioningIdValidations]],
        'assayType': ['', CreateOrderValidation.assayTypeValidations],
        'sampleType': ['', CreateOrderValidation.sampleTypeValidations],
        'retestSample': [true],
        'orderComments': [''],
        'reqFieldMissingFlag': false,
        'assay': this.formBuider.group({
          'maternalAge': [null],
          'gestationalAgeWeeks': [null],
          'gestationalAgeDays': [null],
          'eggDonor': [''],
          'eggDonorAge': [null],
          'ivfStatus': '',
          'fetusNumber': '',
          'collectionDate': ['', CreateOrderValidation.collectionDateValidations],
          'receivedDate': ['', CreateOrderValidation.receivedDateValidations],
          'testOptions': ['', CreateOrderValidation.testOptionValidation],
          'testOption': this.formBuider.array([])
        }),
        'patient': this.formBuider.group({
          'patientLastName': ['', [CreateOrderValidation.nameValidations]],
          'patientFirstName': ['', [CreateOrderValidation.nameValidations]],
          'patientDOB': [''],
          'patientMedicalRecNo': ['', [CreateOrderValidation.medicalRecordNumberValidations]],
          'labId': ['', [CreateOrderValidation.minFieldValidation(3), CreateOrderValidation.AlphabetsNumberSpecial]],
          'accountNumber': ['', [CreateOrderValidation.minFieldValidation(3), CreateOrderValidation.AlphabetsNumber]],
          'otherClinicianName': ['', [CreateOrderValidation.nameValidations]],
          'clinicName': ['', [CreateOrderValidation.nameValidations]],
          'refClinicianName': ['', [CreateOrderValidation.nameValidations]],
          'reasonForReferral': ['']
        })
      })
    });
  }


  /* API Calls */

    /**
    * Getting Assay Details from API
    */
    public onGettingAssayTypes() {
      this._orderService.getAssayTypes().subscribe(resp => {
        this.assayTypeList = resp;
        if (this.orderInfo.get('order.orderId').value) {
          const assayId = this.orderInfo.get('order.assayType').value;
          // Getting the sample types for an seledted assay type
          // this.getAssayTypeId(this.orderInfo.get('order.assayType').value);
          // if (this.orderInfo.get('order.assayType').value.includes('NIPT')) {
          //   this.isNIPT = true;
          // } else {
          //   this.isNIPT = false;
          // }
          this.onGettingSampleType(assayId);
          this.onGettingMaternalAge(assayId);
          this.onGettingIVFStatusValues(assayId);
          this.onGettingGestationalAgeDaysValues(assayId);
          this.onGettingGestationalAgeWeeksValues(assayId);
          this.onGettingFetusesValues(assayId);
          this.onGettingEggDonnerValues(assayId);
          this.onGettingEggDonorAgeValues(assayId);
          this.getTestOptions(assayId);
          this.onDonorChange();
        }
      }, error => {
        console.log('Error on AssayTypes', error);
      });
    }

    public onDonorChange() {
      const eggDonorName = this.orderInfo.get('order').get('assay').get('eggDonor').value;
      const eggDonarAge = this.orderInfo.get('order').get('assay').get('eggDonorAge');
      const assayId = this.orderInfo.get('order.assayType').value;
      // this.onGettingEggDonorAgeValues(assayId);
      if (this.eggDonorAgeValues) {
        if (eggDonorName.toLowerCase().indexOf('self') !== -1) {
          this.isDisabledEggDonorAgeField = false;
          const minValue = this.eggDonorAgeValues.minVal;
          const maxValue = this.eggDonorAgeValues.maxVal;
          eggDonarAge.setValidators(
            [CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.EGG_DONOR_AGE),
              CreateOrderValidation.eggDonorAgeValidations]);
          eggDonarAge.updateValueAndValidity();
          if (eggDonorName.toLowerCase() === 'self') {
            this.orderInfo.get('order').get('assay').get('eggDonorAge').markAsTouched();
            this.updatedmaternalAgeValidation();
          }
        } else {
          eggDonarAge.setValue(null);
          this.isDisabledEggDonorAgeField = true;
          eggDonarAge.clearValidators();
          eggDonarAge.updateValueAndValidity();
          // eggDonarAge.reset();
        }
      }
    }

    /**
     * Getting sample type details by passing assayId
     */
    public onGettingSampleType(assayId) {
      this._orderService.getSampleTypes(assayId).subscribe(response => {
        this.sampleTypeList = response;
      }, error => {
        console.log('Error on samples types', error);
      });
    }

    /**
    * onGettingFetusesValues make a XHR call to get Fetus values
    */
    public onGettingMaternalAge(assayTypeDetails): void {
      this._orderService.getMaternalAge(assayTypeDetails).subscribe(resp => {
        this.maternal = resp;
        console.log(resp);
        const minValue = resp[0].minVal;
        const maxValue = resp[0].maxVal;
        this.orderInfo.get('order.assay.maternalAge').setValidators(
          [CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.MATERNAL_AGE)]);
        this.orderInfo.get('order.assay.maternalAge').updateValueAndValidity();
      }, error => {
        console.log('Error on MaternalAge', error);
      });
    }

    /**
    * onGettingIVFStatusValues make a XHR call to get IVF status values
    */
    public onGettingIVFStatusValues(assayTypeDetails): void {
      this._orderService.getIVFStatus(assayTypeDetails).subscribe(resp => {
        this.ivfStatusValues = resp;
      }, error => {
        console.log('Error on ivf status values', error);
      });
    }

    /**
    * onGettingGestationalAgeDaysValues make a XHR call to get Gestational Age Days values
    */
    public onGettingGestationalAgeDaysValues(assayTypeDetails): void {
        this._orderService.onGettingGestationalAgeDaysValues(assayTypeDetails).subscribe(resp => {
          this.gestationalDays = resp;
          const minValue = resp[0].minVal;
          const maxValue = resp[0].maxVal;
          this.orderInfo.get('order.assay.gestationalAgeDays').setValidators(
            [CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.GESTATIONAL_AGE_DAYS)]);
          this.orderInfo.get('order.assay.gestationalAgeDays').updateValueAndValidity();
        }, error => {
          console.log('Error on Getting Gestational Age Days Values', error);
        });
      }

    /**
    * onGettingGestationalAgeWeeksValues make a XHR call to get Gestational Age Weeks values
    */
    public onGettingGestationalAgeWeeksValues(assayTypeDetails): void {
      this._orderService.onGettingGestationalAgeWeeksValues(assayTypeDetails).subscribe(resp => {
        this.gestationalWeeks = resp;
        const minValue = resp[0].minVal;
        const maxValue = resp[0].maxVal;
        this.orderInfo.get('order.assay.gestationalAgeWeeks').setValidators(
          [CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.GESTATIONAL_AGE_WEEKS)]);
        this.orderInfo.get('order.assay.gestationalAgeWeeks').updateValueAndValidity();
      }, error => {
        console.log('Error on Getting Gestational Age Weeks Values', error);
      });
    }

    /**
    * onGettingFetusesValues make a XHR call to get Fetus values
    */
    public onGettingFetusesValues(assayTypeDetails): void {
      this._orderService.getNumberOfFetuses(assayTypeDetails).subscribe(resp => {
        this.fetusesValues = resp;
      }, error => {
        console.log('Error on fetus values', error);
      });
    }

    /**
    * onGettingEggDonarValues make a XHR call to get eggDonar values
    */
    public onGettingEggDonnerValues(assayTypeDetails): void {
      this._orderService.getEggDonner(assayTypeDetails).subscribe(resp => {
        this.eggDonnerValues = resp;
      }, error => {
        console.log('Error on Eggdonr values', error);
      });
    }

    /**
    * onGettingEggDonorAgeValues make a XHR call to get EggDonorAge Values
    */
    public onGettingEggDonorAgeValues(assayTypeDetails) {
      this._orderService.getEggDonorAge(assayTypeDetails).subscribe(resp => {
      this.eggDonorAgeValues = resp[0];

        this.onDonorChange();

      // const minValue = resp[0].minVal;
      // const maxValue = resp[0].maxVal;
        console.log(this.eggDonorAgeValues);
      }, error => {
        console.log('Error on EggDonorAge values', error);
      });
    }

    /**
  * Getting the assay type test options values.
  */
  public getTestOptions(id: number) {
    this._orderService.getTestOptions(id).subscribe(resp => {
      this.testOptionsArr = resp;
    }, error => {
      console.log('Error on Testoptionss', error);
    });
  }


  /* Change detection functions */

    /**
    * onAssayTypeChange get call on change of Assay Types dropdown
    * onAssayTypeChange makes a XHR call to get Sample Types based on selected Assay Type
    * onAssayTypeChange updates the Sample Types dropdown values
    */
    public onAssayTypeChange(event: any): void {
      this.orderInfo.get('order.assay.testOptions').setValue('');
      console.log(event);
      const id = event.value;
      this.onGettingEggDonnerValues(id);
      this.onGettingFetusesValues(id);
      this.onGettingIVFStatusValues(id);
      this.onGettingMaternalAge(id);
      this.onGettingEggDonorAgeValues(id);
      this.onGettingGestationalAgeDaysValues(id);
      this.onGettingGestationalAgeWeeksValues(id);
      /**
      * Setting the default gender when user click on NIPT
      */
      // if (event.value.includes('NIPT')) {
      //   this.isNIPT = true;
      //   this.orderInfo.patchValue({
      //     order: {
      //       patient: {
      //         patientGender: 'Female'
      //       }
      //     }
      //   });
      // } else {
      //   this.isNIPT = false;
      // }
      this._orderService.getSampleTypes(id).subscribe(resp => {
        this.sampleTypeList = resp;
        if (this.sampleTypeList.length === 1) {
          this.orderInfo.get('order').get('sampleType').setValue(this.sampleTypeList[0].sampleType);
        }
      }, error => {
        console.log('Error on Sample Type', error);
      });

      /**
      * When user select Assay Type test options should call XHR call.
      */
      this._orderService.getTestOptions(id).subscribe(resp => {
        this.testOptionsArr = resp;
        this.formattedTestOptions = {};
        this.testOptionsArr.forEach(option => {
          this.formattedTestOptions[option.testName] = false;
        });
      }, error => {
        console.log('Error on Testoptionss', error);
      });
    }

      /**
      * Egg donor change event
      */
    public eggDonorChange(event: any) {
      const eggDonorName: string = event.value;
      const eggDonarAge = this.orderInfo.get('order').get('assay').get('eggDonorAge');
      const maternalAge = this.orderInfo.get('order').get('assay').get('maternalAge');

      if (eggDonorName.toLowerCase().indexOf('self') !== -1) {
        this.isDisabledEggDonorAgeField = false;
        const minValue = this.eggDonorAgeValues.minVal;
        const maxValue = this.eggDonorAgeValues.maxVal;
        eggDonarAge.setValidators(
          [CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.EGG_DONOR_AGE),
            CreateOrderValidation.eggDonorAgeValidations]);
          eggDonarAge.updateValueAndValidity();
          if (eggDonorName.toLowerCase() === 'self') {
            eggDonarAge.setValidators([
              CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.EGG_DONOR_AGE),
              CreateOrderValidation.eggDonorAgeValidations,
              CreateOrderValidation.eggDonorAgeSelfValidation(minValue, maxValue,maternalAge)]);
              if(eggDonarAge.value)
              {
                eggDonarAge.markAsTouched();
              }
            eggDonarAge.updateValueAndValidity();
            maternalAge.updateValueAndValidity();
          }
        } else {
        eggDonarAge.setValue(null);
        this.isDisabledEggDonorAgeField = true;
        eggDonarAge.clearValidators();
        eggDonarAge.updateValueAndValidity();
       // eggDonarAge.reset();
      }
    }

  public updatedmaternalAgeValidation() {
    const eggDonorName = this.orderInfo.get('order').get('assay').get('eggDonor').value;
    const eggDonarAge = this.orderInfo.get('order').get('assay').get('eggDonorAge');
    const maternalAge = this.orderInfo.get('order').get('assay').get('maternalAge');

    if (eggDonorName && this.eggDonorAgeValues) {
      const minValue = this.eggDonorAgeValues.minVal;
      const maxValue = this.eggDonorAgeValues.maxVal;
      if (eggDonorName.toLowerCase() === 'self') {
        eggDonarAge.setValidators([
          CreateOrderValidation.RangeValidations(minValue, maxValue, FormFieldNames.EGG_DONOR_AGE),
          CreateOrderValidation.eggDonorAgeValidations,
          CreateOrderValidation.eggDonorAgeSelfValidation(minValue, maxValue, maternalAge)]);
        eggDonarAge.updateValueAndValidity();
      }
    }
  }

    /**
    * when change occred in HTML checkbox onSelectOfTestOption  enabled
    * This method checks whether it is checked or not based on event
    * @param event event data
    */
    public onSelectOfTestOption(event) {
      // console.log(this.formattedTestOptions);
      this.formattedTestOptions[event.source.value.testName] = event.checked;
      this.orderInfo.get('order.assay.testOptions').setValue(this.formattedTestOptions);
      this.onFocusOfTestOption();
    }

    /**
    * when change occred in HTML checkbox onFocusOfTestOption  enabled
    * This method checks whether it is checked or not based on event
    * @param event event data
    */
    public onFocusOfTestOption() {
      this.isMultiTestNotChecked = true;
      Object.keys(this.formattedTestOptions).forEach((key) => {
        if (this.formattedTestOptions[key] === true) {
          this.isMultiTestNotChecked = false;
        }
      });
      if (this.isMultiTestNotChecked) {
        this.orderInfo.get('order.assay.testOptions').setErrors({ 'incorrect': true });
      }
    }

    /**
    * When selecting the IVF status.
    * Depends on the value selected.
    * Restrict the other field value
    */
    public onIVFStatusChange(event: any): void {
      // const eggDonnerLab = this.eggDonnerLabel.nativeElement.innerHTML;
      const eggDonarC = this.orderInfo.get('order').get('assay').get('eggDonor');
      const eggDonarAge = this.orderInfo.get('order').get('assay').get('eggDonorAge');

      if (event.value.toLowerCase() === 'yes') {
        this.isDisabledEggDonorField = false;
        eggDonarC.setValidators([Validators.required]);
        eggDonarC.updateValueAndValidity();
      } else {
        eggDonarC.setValue('');
        eggDonarAge.setValue(null);
        this.isDisabledEggDonorField = true;
        this.isDisabledEggDonorAgeField = true;
        eggDonarC.clearValidators();
        eggDonarAge.clearValidators();
        eggDonarAge.updateValueAndValidity();
        eggDonarC.updateValueAndValidity();
       // eggDonarC.reset();
        // eggDonarAge.reset();
      }
    }

    /**
    * Checkboxes needs to be auto checked while editing an order
    * @param option checked values
    */
    public checkValueChecked(option) {
      return this.orderInfo.get('order.assay.testOptions').value[option.testName];
    }

    /* Validations */

    /**
    * Setting focus for calender
    */
    public setFocusCalender(dateInput, controlName) {
      this.orderInfo.get('order').get(controlName).clearValidators();
      this.orderInfo.get('order').get(controlName).updateValueAndValidity();
      dateInput.open();
    }

    /**
    * Setting focus for calender
    */
    public focusOutCalender(dateInput, controlName) {
      if (controlName) {
        if (controlName.indexOf('collectionDate') !== -1) {
          this.orderInfo.get('order').get(controlName).setValidators(CreateOrderValidation.collectionDateValidations);
        }
        if (controlName.indexOf('receivedDate') !== -1) {
          this.orderInfo.get('order').get(controlName).setValidators(CreateOrderValidation.receivedDateValidations);
        }
        this.orderInfo.get('order').get(controlName).updateValueAndValidity();
      }
      if (dateInput.value === undefined) {
        this.isCollectionDateError = true;
      }
      dateInput.close();
    }

    /* Checking the Duplicate Accessioning Id */
    public duplicateAccessioningIdValidation() {
      // tslint:disable-next-line:max-line-length
      if ((this.checkDuplicateId === null || this.checkDuplicateId === undefined || this.checkDuplicateId === '') ||
          (this.checkDuplicateId !== this.orderInfo.get('order.accessioningId').value)) {
        this.orderInfo.get('order.accessioningId').setValidators(CreateOrderValidation.accessioningIdValidations);
        // console.log(this.orderInfo.get('order.accessioningId').valid);
        if (this.orderInfo.get('order.accessioningId').valid) {
          const id = this.orderInfo.get('order.accessioningId').value;
          this._orderService.validateAccessioningId(id).subscribe(respose => {
            // console.log(respose);
            if (respose) {
              this.orderInfo.get('order.accessioningId').setErrors({
                isNotValidAccessioningId: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['duplicate']
              });
            }
          });
        }
      }
    }

    public duplicateAccountNumber() {
      const accountNumber = this.orderInfo.get('order.patient.accountNumber');
      if ((this.isValidAccountNumber === null || this.isValidAccountNumber === undefined || this.isValidAccountNumber === '') ||
      (this.isValidAccountNumber !== accountNumber.value)) {
        accountNumber.setValidators([CreateOrderValidation.minFieldValidation(3), CreateOrderValidation.AlphabetsNumber]);
        if (accountNumber.valid) {
          const value = accountNumber.value;
          this._orderService.validateAccountNumber(value).subscribe(response => {
            if (response) {
              accountNumber.setErrors({
                isNotValidAccountNumber: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['accountNumber']['duplicate']
              })
            }
          })
        }
      }
    }

    /**
    * Validating the Order Information
    */
    public isValidOrder() {
      return (this.orderInfo.get('order.accessioningId').valid &&
        this.orderInfo.get('order.assayType').valid &&
        this.orderInfo.get('order.sampleType').valid) &&
        this.orderInfo.get('order.orderComments').valid;
    }

    /* For checking disabled condition */
    public isDisabled() {
      return ((this.orderInfo.get('order.orderId').value != null) && (this.orderStatus.toLowerCase() !== 'unassigned'));
    }

    /**
    * Restrict the user to enter the alphabets
    * @param event getting data
    */
    public _keyPress(event: any) {
    const pattern = /^[0-9]*$/;
    const inputChar = String.fromCharCode(event.charCode);
      if (!pattern.test(inputChar)) {
        // invalid character, prevent input
        event.preventDefault();
      }
    }

    /* Save and Update Forms*/
    /**
    * on submitting the create order form submit
    */
    public onSave() {
      if (this.orderInfo.valid) {
        this.createOrder(false);
      }
    }

    public onSaveAndCreate() {
      if (this.orderInfo.valid) {
        this.createOrder(true);
      }
    }

    /**
    * Sending all the data entered by user to server.
    * checking the response from server, whether success or error.
    * If it is success navigating to orders page.
    */
    public createOrder(isSaveNext) {
      const translations = this._translate.translations[this._translate.currentLang];
        const formData = Object.assign({}, this.orderInfo.value);
        console.log('formData', formData);
        delete formData.order.assay.testOption;
        this._orderService.postOrders(formData).subscribe(resp => {
          console.log(resp);
          if (isSaveNext) {
            const message = `${translations.orders.notificationmsg.orderSucWithAccessioningID}`;
            const expMessage = this._snackBarSvc.getSingleMessage(message).replace('$$$', formData.order.accessioningId);
            this._snackBarSvc.showSuccessSnackBar(`${expMessage}`, SnackbarClasses.successBottom2);
            window.scrollTo(0, 0);

            setTimeout(() => {
              window.location.reload();
            }, 1500);

          } else {
            const message = `${translations.orders.notificationmsg.orderSucWithAccessioningID}`;
            const expMessage = this._snackBarSvc.getSingleMessage(message).replace('$$$', formData.order.accessioningId);
            this._sharedService.setData('orderCreated', `${expMessage}`);
            this._router.navigate(['orders']);
          }
        }, (error1: HttpErrorResponse) => {
          const message1 = `${translations.orders.notificationmsg.orderSysNotAvailable}`;
          if (error1.status === 400) {
          console.log('Error on order save', error1.error.errorMessage);
          const errorMsg = error1.error.errorMessage;
          this._snackBarSvc.showErrorSnackBar(`${errorMsg}`, SnackbarClasses.errorBottom2);
          } else {
            this._snackBarSvc.showErrorSnackBar(`${message1}`, SnackbarClasses.errorBottom2);
          }
          this.step = 0;
          console.log('Error on order save', error1);
        });
      }

    /**
    * Update the Order Details calling API from service.
    */
    public onUpdateOrderDetils() {
      const translations = this._translate.translations[this._translate.currentLang];
      if (this.headerInfo.isBackRequiredWarning) {
        if (this.orderInfo.valid) {
          /* console.log('order update ', this.orderInfo.value); */
          const updatedOrderDetails = this.orderInfo.value;
          delete updatedOrderDetails.order.assay.testOption;
          this._orderService.updateOrderDetails(updatedOrderDetails).subscribe(response => {
            /* console.log(response); */
            this._location.back();
          }, (error1: HttpErrorResponse) => {
            const message1 = `${translations.orders.notificationmsg.orderSysNotAvailable}`;
            if (error1.status === 400) {
            const parsedData = JSON.parse(error1.error);
            const errorMsg = parsedData.errorMessage;
            this._snackBarSvc.showErrorSnackBar(`${errorMsg}`, SnackbarClasses.errorBottom1);
            } else {
              this._snackBarSvc.showErrorSnackBar(`${message1}`, SnackbarClasses.errorBottom1);
            }
            this.step = 0;
            console.log('Error on order save', error1);
          });
        }
      } else {
        const message1 = `${translations.orders.noChanges}`;
        this._dialogBox.open(ConfirmDialogComponent, {
          width: '486px',
          height: '160px',
          data: {onlyWarn: `${message1}`}
        });
      }
    }

      public findInvalidFormDetails() {
        let msg = '';
        if (!this.isValidOrder()) {
          msg += 'Please enter the mandatory values in Order details ';
        }
        if (this.orderInfo.get('order.assay').invalid) {
          msg += 'Please enter the mandatory values in Assay details';
        }

        return msg;
      }

  public dobChange(event: MatDatepickerInputEvent<Date>) {
    console.log(event.value);
    console.log('this._el.nativeElement', this.dobInput.nativeElement.value);
    const dobValue = this.dobInput.nativeElement.value;
    console.log('dobValue', dobValue);
    if (dobValue === '') {
      this.orderInfo.get('order.patient.patientDOB').setErrors(null);
    } else if (dobValue === null) {
      this.orderInfo.get('order.patient.patientDOB').setErrors({
        isNotValidAccessioningId: true,
        errorMsg: CreateOrderErrorMessages.errorMsg['dobDate']['format']
      });
    } else {
      if (dobValue.indexOf('/') > -1) {
        const str = dobValue.split('/');
        const year = Number(str[2]);
        const day = Number(str[1]);
        const month = Number(str[0]);
        const minValue = Number(this.maxDate.getFullYear() - 99);
        const maxValue = Number(this.maxDate.getFullYear());
        const maximumDate = new Date(year, month, 0).getDate();
        if (str.length !== 3) {
          this.orderInfo.get('order.patient.patientDOB').setErrors({
            isNotValidAccessioningId: true,
            errorMsg: CreateOrderErrorMessages.errorMsg['dobDate']['format']
          });
        } else if (isNaN(day) || isNaN(month) || isNaN(year) || day < 1 || month < 1 || day > maximumDate || month > 12) {
          this.orderInfo.get('order.patient.patientDOB').setErrors({
            isNotValidAccessioningId: true,
            errorMsg: CreateOrderErrorMessages.errorMsg['dobDate']['errorDate']
          });
        } else if (year < minValue) {
          this.orderInfo.get('order.patient.patientDOB').setErrors({
            isNotValidAccessioningId: true,
            errorMsg: CreateOrderErrorMessages.errorMsg['dobDate']['minValue']
          });
        } else if (year > maxValue || new Date(year, month - 1, day) >  this.maxDate) {
          this.orderInfo.get('order.patient.patientDOB').setErrors({
            isNotValidAccessioningId: true,
            errorMsg: CreateOrderErrorMessages.errorMsg['dobDate']['maxValue']
          });
        }
      } else {
        this.orderInfo.get('order.patient.patientDOB').setErrors({
          isNotValidAccessioningId: true,
          errorMsg: CreateOrderErrorMessages.errorMsg['dobDate']['format']
        });
      }
    }
  }

  getRequiredFieldsByAssay(assayType: string) {
    this._orderService.getRequiredFieldsByAssay(assayType).subscribe(response => {
      this.patientConfigInfo = response;
    }, error => {
      console.log(error);
    });
  }

  getProcessStepByAccessioningId(accessioningID: string) {
    this._orderService.getProcessStepByAccessioningId(accessioningID).subscribe(response => {
      this.processStepNameInfo = response[0].workflowType;
    }, error => {
      console.log(error);
    });
  }

  getValidField(fieldVal) {
    if (fieldVal === null || fieldVal === undefined || fieldVal === '') {
      return true;
    }
    return false;
  }

  handleWarningIcon(event) {
    // console.log(event, 'emitter details');
    const {stepName, isValid} = event;
    if (stepName === this.patientSectionName) {
      if (isValid) {
        this.patientWarningSymbolInfo = true;
      } else {
        this.patientWarningSymbolInfo = true;
      }
    } else if (stepName === this.assaySectionName) {
      if (isValid) {
        this.assayWarningSymbolInfo = true;
      } else {
        this.assayWarningSymbolInfo = false;
      }
    } else if (stepName === this.orderSectionName) {
      if (isValid) {
        this.orderWarningSymbolInfo = true;
      } else {
        this.orderWarningSymbolInfo = false;
      }
    }
  }

}
