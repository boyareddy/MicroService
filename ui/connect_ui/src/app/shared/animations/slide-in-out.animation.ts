import { trigger, state, animate, transition, style, query, animateChild, group } from '@angular/animations';

export const routeAnimations =
trigger('routeAnimations', [
  transition('* <=> slideBtmTop', [
      query('.slideBtmTopCls', style({ position: 'fixed', width: '100%' })
        , { optional: true }),
      group([
        query(':enter', [
          query('.slideBtmTopCls', [
            style({ transform: 'translateY(100%)', width: '100%', 'z-index': '97' }),
            animate('.5s linear', style({ transform: 'translateY(0%)' }))
          ], { optional: true })
        ], { optional: true }),
        query(':leave', [
          query('.slideBtmTopCls', [
            style({ transform: 'translateY(0%)', width: '100%', 'z-index': '1000000000', position: 'fixed' }),
            animate('.5s linear', style({ transform: 'translateY(100%)' }))
          ], { optional: true })
        ], { optional: true })
      ])
    ]),
    transition('* <=> slideLftRgt', [
      query('.slideLftRgtCls', style({ position: 'fixed', width: '100%' })
        , { optional: true }),
      group([
        query(':enter', [
          query('.slideLftRgtCls', [
            style({ transform: 'translateX(100%)', width: '100%', 'z-index': '97' }),
            animate('.5s linear', style({ transform: 'translateX(0%)' }))
          ], { optional: true })
        ], { optional: true }),
        query(':leave', [
          query('.slideLftRgtCls', [
            style({ transform: 'translateX(0%)', width: '100%', 'z-index': '97', position: 'fixed', top: 0   }),
            animate('.5s linear', style({ transform: 'translateX(100%)' }))
          ], { optional: true })
        ], { optional: true })
      ])
    ]),
    transition('* <=> slideLftRgt1', [
      query('.slideLftRgtCls1', style({ position: 'fixed', width: '100%' })
        , { optional: true }),
      group([
        query(':enter', [
          query('.slideLftRgtCls1', [
            style({ transform: 'translateX(100%)', width: '100%', 'z-index': '97' }),
            animate('.5s linear', style({ transform: 'translateX(0%)' }))
          ], { optional: true })
        ], { optional: true }),
        query(':leave', [
          query('.slideLftRgtCls1', [
            style({ transform: 'translateX(0%)', width: '100%', 'z-index': '97', position: 'fixed', top: 0   }),
            animate('.5s linear', style({ transform: 'translateX(100%)' }))
          ], { optional: true })
        ], { optional: true })
      ])
    ])
]);
  export const slideLftRgtAnim =
  trigger('slideLftRgtAnim', [
    transition(':enter', [
      query('.slideLftRgtAnimCls', [
        style({ transform: 'translateX(100%)', width: '100%', 'z-index': '99' }),
        animate('.7s ease-in-out', style({ transform: 'translateX(0%)' }))
      ], { optional: true })
    ]),
    transition(':leave', [
      query('.slideLftRgtAnimCls', [
        style({ transform: 'translateX(100%)', width: '100%', 'z-index': '99' }),
        animate('.7s ease-in-out', style({ transform: 'translateX(0%)' }))
      ], { optional: true })
    ])
  ]);
  export const slideBtmTopAnim =
  trigger('slideBtmTopAnim', [
    transition(':enter', [
      query('.slideBtmTopAnimCls', [
        style({ transform: 'translateY(100%)', width: '100%', 'z-index': '1000000000' }),
        animate('20s ease-in-out', style({ transform: 'translateY(0%)' }))
      ], { optional: true })
    ]),
    transition(':leave', [
      query('.slideBtmTopAnimCls', [
        style({ transform: 'translateY(100%)', width: '100%', 'z-index': '10000000' }),
        animate('20s ease-in-out', style({ transform: 'translateY(0%)' }))
      ], { optional: true })
    ])
  ]);
// [
//     transition(':enter', [
//         style({transform: 'translateY(100%)'}),
//         animate('1000ms ease-in', style({transform: 'translateY(0%)'}))
//     ]),
//     transition(':leave', [
//         animate('1000ms ease-out', style({transform: 'translateY(-100%)'}))
//     ])
// ]);
