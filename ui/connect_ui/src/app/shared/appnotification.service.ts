import { Injectable } from '@angular/core';
import { APPNOTFROUTERURLS, NOTFTOPICSCONST, NOTFSEVERITY } from './utils/notification.util';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AppNotificationService {
    notfRes;

    constructor(private _router: Router) {}

    getNotf(_topicName?: string) {
        console.log(_topicName, '_topicName');
        if (_topicName && this.notfRes) {
            if (_topicName === NOTFTOPICSCONST.ALLTOPICS) {
                const _NFILT = this.notfRes.filter(
                    elm => (elm.viewed === 'N' && elm.severity !== NOTFSEVERITY.INFO));
                return _NFILT;
            } else {
                const _NFILT = this.notfRes.filter(
                    elm => (
                        elm.topic && elm.topic.toLowerCase() === _topicName.toLowerCase()
                         && elm.viewed === 'N' && elm.severity !== NOTFSEVERITY.INFO));
                return _NFILT;
            }
        } else {
            return  this.notfRes;
        }
    }

    setNOTF(res) {
        if (res) {
            this.notfRes = res;
        }
    }

    getNotfByRouter() {
        if (this.notfRes) {
            const topic = this.getTopicByRouter();
            console.log(topic, 'topic===========================');
            console.log(this.notfRes, 'this.notfRes===========================');
            if (topic && this.notfRes) {
                if (topic === NOTFTOPICSCONST.ALLTOPICS) {
                    return this.notfRes.filter(
                        elm => (elm.viewed === 'N' && elm.severity !== NOTFSEVERITY.INFO));
                } else {
                    const _NFILT = this.notfRes.filter(
                        elm => (
                            elm.topic && elm.topic.toLowerCase() === topic.toLowerCase() &&
                            elm.viewed === 'N' && elm.severity !== NOTFSEVERITY.INFO));
                    console.log(topic, 'topic00000000');
                    console.log(_NFILT, '_NFILT');
                    return _NFILT;
                }
            } else {
                return  null;
            }
        } else {
            return  null;
        }
    }

    getTopicByRouter(routerURL?: string) {
        console.log(routerURL, 'routerURL getTopicByRouter$$$$$$$$$$$$$');
        console.log(this._router.url, 'this._router.url getTopicByRouter$$$$$$$$$$$$$');
        routerURL = routerURL ? routerURL : this._router.url;
        console.log(routerURL, 'routerURL getTopicByRouter#############');
        let topic = null;
        APPNOTFROUTERURLS.forEach((elm) => {
            console.log(elm.URL.toLowerCase().match(new RegExp(routerURL, 'g')));
            if (routerURL.match(new RegExp(elm.URL.toLowerCase(), 'g')) !== null) {
                console.log(elm.TOPIC, 'elm');
               topic = elm.TOPIC;
            }
        });
       return topic;
    }

}
