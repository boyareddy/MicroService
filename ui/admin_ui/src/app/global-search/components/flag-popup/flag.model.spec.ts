import { getDeviceType } from './flag.model';
import { FormControl } from '@angular/forms';

describe('getDeviceType ', () => {
    it('should get Device Type lp24', () => {
        const sample = {'assayType': 'NIPTHTP', 'processStepName': 'NA Extraction', 'workflowType': ''};
        const deviceType = getDeviceType(sample);
        expect(deviceType).toBe('lp24');
    });

});
