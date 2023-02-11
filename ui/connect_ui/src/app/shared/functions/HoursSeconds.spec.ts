import { getHoursMin } from './HoursSeconds';

describe('getHoursMin', () => {
    it('should give the time in hours and minuites', () => {
        const milliSeconds = 60000;
        expect(getHoursMin(milliSeconds)).toBe('0h 1mins');
    });

    it('shouldnot give the time in hours and minuites', () => {
        const milliSeconds = '';
        expect(getHoursMin(milliSeconds)).toBeNull();
    });
});
