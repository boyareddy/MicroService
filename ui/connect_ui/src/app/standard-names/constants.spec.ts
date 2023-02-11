import { StringConstants } from './constants';

describe('StringConstants', () => {
    it('should return the string constant "CANCEL_UPDATE_ORDER"', () => {
        const input = StringConstants.STRING('CANCEL_UPDATE_ORDER');
        const output = 'Do you want to proceed without saving the changes';
        expect(input).toEqual(output);
    });
});
