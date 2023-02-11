export function getHoursMin(date: any) {
    if (!Number.isNaN(date) && date !== null && date !== undefined && date !== '' && date !== 0) {
        /* If time is milliseconds */
        date = Number(date);
        const hours = Math.floor((date % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const min = Math.floor((date % (1000 * 60 * 60)) / (1000 * 60));
        /* If time is seconds */
        // date = Number(date);
        // const hours = Math.floor(date / 3600);
        // const min = Math.floor(date % 3600 / 60);
        // const s = Math.floor(date % 3600 % 60);
        return hours + 'h ' + min + 'mins';
    } else {
        return null;
    }
}
