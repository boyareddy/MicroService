export const updateResponse = (labSettings) => {
    const updatedLabSettings: ExpectedResponse[] = [];
    for (const setting in labSettings) {
        if (labSettings.hasOwnProperty(setting)) {
            const labSettingsKey = labSettings[setting];
            for (const key in labSettingsKey) {
                if (labSettingsKey.hasOwnProperty(key)) {
                    if (key !== 'labLogo') {
                        const newLable: ExpectedResponse = {} as ExpectedResponse;
                        newLable.attributeType = setting;
                        newLable.attributeName = key;
                        newLable.attributeValue = labSettingsKey[key];
                        updatedLabSettings.push(newLable);
                    }
                }
            }
        }
    }
    return updatedLabSettings;
};

export const formatter = (responseData) => {
    const labSettings = {};
    const keys = responseData.map(ele => ele.attributeType);
    const updatedKey = keys.filter(ele => ele !== 'reportsettings');
    const filteredKeys = new Set(updatedKey);

    filteredKeys.forEach((newKey: any) => {
        labSettings[newKey] = {};
        responseData.forEach(ele => {
            if (newKey === ele.attributeType) {
                if (ele.attributeName !== 'labLogo') {
                    labSettings[newKey][ele.attributeName] = ele.attributeValue;
                }
            }
        });
    });
    return labSettings;
};

interface ExpectedResponse {
    attributeType: string;
    attributeName: string;
    attributeValue: string;
}

export const expImages = ['jpg', 'png', 'jpeg'];
export enum LabSettingConstants {
    FILE = 'file',
    IMAGE = 'image',
    DATA = 'data'
}
