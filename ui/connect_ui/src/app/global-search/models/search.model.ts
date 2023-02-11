// export type Order = {
//     runResultsId: string;
//     sampleResultId: string;
//     accesssioningId: string;
//     orderId: string;
//     status: string;
//     processStepName: string;
//     flag: string;
//     assayType: string;
//     sampleType: string;
// }

// export type RunResult = {
//     runResultId: number;
//     deviceName?: string;
//     processStepName: string;
//     deviceRunId: string;
//     runStatus: string;
//     operatorName: string;
//     sampleCount: number;
//     runCompletionTime: string;
//     container?: Container
// }
export type SearchResult = {
    orders: Order[];
    runResults: RunResult[];
}

export type Container = {
    containerId?: string;
    containerType?: string;
}

export type ValidSearchInput = {
    isValid: boolean;
    error?: string;
}

// New model based on new search API

export interface Order {
    accessioningId: number;
    assayType: string;
    orderId: number;
    processStepName: string;
    runResultsId: number;
    sampleResultId: number;
    sampleType: string;
    status: string;
}

export interface SearchOrderElements {
    totalElements: number;
    orders: Order[];
}

export interface RunResult {
    device: string;
    deviceRunId: string;
    operatorName: string;
    runCompletionTime: Date;
    runResultId: number;
    runStatus: string;
    sampleCount: number;
    processStepName: string;
}

export interface SearchRunResultElements {
    totalElements: number;
    runResults: RunResult[];
}

export interface SearchElement {
    searchOrderElements: SearchOrderElements;
    searchRunResultElements: SearchRunResultElements;
}

export interface ScrollEvent {
    offset: number;
    content: string;
    limit: number;
}

export const ORDER_TABLE_FIELDS = [
    "accessioningId",
    "assayType",
    "sampleType",
    "status",
    "flag",
    "Symbol"
];

export const RUN_TABLE_FIELDS = [
    "runResultId",
    "processStepName",
    "deviceName",
    "sampleCount",
    "runCompletionTime",
    "operatorName",
    "Symbol"
];