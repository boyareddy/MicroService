export const HTP_TYPE = ["name", "serialNo", "location", "clientId", "password"];

export const NON_HTP_TYPE = ["name", "serialNo", "location"];

export const SEQUENCER_TYPE = ["name", "serialNo", "outputDirectory", "location"];

export const QIASYMPHONY = ["name", "serialNo", "outputDirectory", "location"];

export const TTV2ANALYSIS_TYPE = ["name", "ttv2OutputDirectory", "jwtCertificate", "clientCertificate", "sshCertificate", "url"];

export enum DEVICE_TYPES{
    "MP24" = "MagNaPure24",
    "MP96" = "MagNa Pure 96",
    "HTP" = "High Throughput sequencing",
    "LP24" = "LP24",
    "DPCR" = "cobas dPCR",
    "SEQUENCER" = "Illumina Sequencer",
    "QIASYMPHONY" = "QIAsymphony",
   // "TTV2ANALYSIS" = "TTv2 Analysis software"
    "TTV2ANALYSIS" = "Analysis SW TTv2"
}