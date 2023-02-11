
import { FormControl } from '@angular/forms';
import { getDeviceType } from './flags.util';

describe('getDeviceType ', () => {
    it('should get Device Type lp24', () => {
        const sample = {'assayType': 'NIPTHTP', 'processStepName': 'NA Extraction', 'workflowType': ''};
        const deviceType = getDeviceType(sample);
        expect(deviceType).toBe('lp24');
    });

});
