import { trigger, state, animate, transition, style, query, animateChild, group } from '@angular/animations';

export const routeAnimations =
trigger('routeAnimations', [
  transition('* <=> slideBtmTop', [
      query('.slideBtmTopCls', style({ position: 'fixed', width: '100%' })
        , { optional: true }),
      group([
        query(':enter', [
          query('.slideBtmTopCls', [
            style({ transform: 'translateY(100%)', width: '100%', 'z-index': '1000000000' }),
            animate('.5s linear', style({ transform: 'translateY(0%)' }))
          ], { optional: true })
        ], { optional: true }),
        query(':leave', [
          query('.slideBtmTopCls', [
            style({ transform: 'translateY(0%)', width: '100%', 'z-index': '1000000000', position: 'fixed'}),
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
            style({ transform: 'translateX(100%)', width: '100%', 'z-index': '1000000000' }),
            animate('.5s linear', style({ transform: 'translateX(0%)' }))
          ], { optional: true })
        ], { optional: true }),
        query(':leave', [
          query('.slideLftRgtCls', [
            style({ transform: 'translateX(0%)', width: '100%', 'z-index': '1000000000', position: 'fixed', top: 0   }),
            animate('.5s linear', style({ transform: 'translateX(100%)' }))
          ], { optional: true })
        ], { optional: true })
      ])
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
