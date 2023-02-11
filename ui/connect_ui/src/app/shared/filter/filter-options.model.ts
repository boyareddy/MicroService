export const filterOptions = [{
    tab: "completed",
    filterOptions: [{
        filterGroup: 'workflow',
        filterGroupName: 'Workflow',
        options: [{
            fieldKey: "processStepName",
            fieldName: "Workflow step",
            fieldType: "dropdown",
            fieldValue: null,
        },{
            fieldKey: "operatorName",
            fieldName: "User name",
            fieldType: "dropdown",
            fieldValue: null,
            isTranslationRequired: true
        },{
            fieldKey: "assayType",
            fieldName: "Assay",
            fieldType: "dropdown",
            fieldValue: null
        },{
            fieldKey: "sampleType",
            fieldName: "Sample type",
            fieldType: "dropdown",
            fieldValue: null
        },{
            fieldKey: "deviceType",
            fieldName: "Device type",
            fieldType: "dropdown",
            fieldValue: null
        }]
    },{
        filterGroup: 'completionDate',
        filterGroupName: 'Completion date',
        options: [{
            fieldKey: "dateFrom",
            fieldName: "Date from",
            fieldType: "date",
            fieldValue: null
        },{
            fieldKey: "dateTo",
            fieldName: "Date to",
            fieldType: "date",
            fieldValue: null
        }]
    },{
        filterGroup: 'specialAttributes',
        filterGroupName: 'Special attributes',
        options: [{
            fieldKey: "flags",
            fieldName: "Has flags",
            fieldType: "checkbox",
            fieldValue: null
        },{
            fieldKey: "comments",
            fieldName: "Has comments",
            fieldType: "checkbox",
            fieldValue: null
        }]
    }]
},{
    tab: "unassigned",
    filterOptions: [{
        filterGroup: 'orders',
        filterGroupName: 'Orders',
        options: [{
            fieldKey: "assayType",
            fieldName: "Assay",
            fieldType: "dropdown",
            fieldValue: null
        },{
            fieldKey: "sampleType",
            fieldName: "Sample type",
            fieldType: "dropdown",
            fieldValue: null
        }]
    },{
        filterGroup: 'datemodified',
        filterGroupName: 'Date modified',
        options: [{
            fieldKey: "dateFrom",
            fieldName: "Date from",
            fieldType: "date",
            fieldValue: null
        },{
            fieldKey: "dateTo",
            fieldName: "Date to",
            fieldType: "date",
            fieldValue: null
        }]
    },{
        filterGroup: 'specialAttributes',
        filterGroupName: 'Special attributes',
        options: [{
            fieldKey: "comments",
            fieldName: "Has comments",
            fieldType: "checkbox",
            fieldValue: null
        }]
    }]
},{
    tab: "inworkflow",
    filterOptions: [{
        filterGroup: 'workflow',
        filterGroupName: 'Workflow',
        options: [{
            fieldKey: "workflowType",
            fieldName: "Workflow step",
            fieldType: "dropdown",
            fieldValue: null
        },{
            fieldKey: "workflowStatus",
            fieldName: "Workflow status",
            fieldType: "dropdown",
            fieldValue: null
        },{
            fieldKey: "assaytype",
            fieldName: "Assay type",
            fieldType: "dropdown",
            fieldValue: null
        },{
            fieldKey: "sampletype",
            fieldName: "Sample type",
            fieldType: "dropdown",
            fieldValue: null
        }]
    },{
        filterGroup: 'createData',
        filterGroupName: 'Create date',
        options: [{
            fieldKey: "dateFrom",
            fieldName: "Date from",
            fieldType: "date",
            fieldValue: null
        },{
            fieldKey: "dateTo",
            fieldName: "Date to",
            fieldType: "date",
            fieldValue: null
        }]
    },{
        filterGroup: 'specialAttributes',
        filterGroupName: 'Special attributes',
        options: [{
            fieldKey: "flags",
            fieldName: "Has flags",
            fieldType: "checkbox",
            fieldValue: null
        },{
            fieldKey: "comments",
            fieldName: "Has comments",
            fieldType: "checkbox",
            fieldValue: null
        },{
            fieldKey : "mandatoryFieldMissing",
            fieldName: "Missing information",
            fieldType: "checkbox",
            fieldValue: null
        }]
    }]
}]