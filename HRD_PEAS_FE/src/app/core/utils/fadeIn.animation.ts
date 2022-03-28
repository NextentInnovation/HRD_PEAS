// import the required animation functions from the angular animations module
import { animate, animateChild, group, query, stagger, state, style, transition, trigger } from '@angular/animations';

export const fadeAnimation =

  trigger( 'routeAnimations', [
    transition( '* => *', [
      group( [

          query( ':enter h2', [
              style( { opacity: 0, transform: 'translate3d(0, -50px, 0)' } ),
              animate( '500ms ease-in', style( { opacity: 1, transform: 'translate3d(0, 0, 0)' } ) )
            ], { optional: true }
          ),

          query( ':leave', [
            style( { opacity: 1, position: 'absolute', top: '15px', left: '15px', right: '15px' }, ),
            animate( '200ms', style( { opacity: 0, } ) )
          ], { optional: true } ),

          query( ':enter .card', [
              style( { opacity: 0, transform: 'translate3d(0, 50px, 0)' } ),
              stagger( '200ms ease-in', animate( '400ms', style( { opacity: 1, transform: 'translate3d(0, 0, 0)' } ) ) )
            ], { optional: true }
          ),


        ],
      ),

    ] ),


  ] );

/*

  trigger( 'routeAnimations', [

    transition( '* <=> *', [
      style({ position: 'relative' }),
      query( ':enter, .card',
        [
          style( { opacity: 0 } ),
          animate( '1.5s', style( { opacity: 1 } ) )
        ],
        { optional: true }
      ),
      query( ':leave, .card',
        [
          style( { opacity: 1 } ), animateChild(),
          animate( '1.5s', style( { opacity: 0 } ) )
        ],
        { optional: true }
      ),

      query( ':enter, .card',
        [
          style( { opacity: 0 } ),
          animate( '1.5s', style( { opacity: 1 } ) )
        ],
        { optional: true }
      )

    ] )

  ] );*/

export const slideInAnimation =
  trigger( 'slideInAnimation', [
    transition( '* <=> *', [
      style( { position: 'relative' } ),
      query( ':enter, :leave', [
        style( {
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%'
        } )
      ] ),
      query( ':enter', [
        style( { left: '-100%' } )
      ] ),
      query( ':leave', animateChild() ),
      group( [
        query( ':leave', [
          animate( '300ms ease-out', style( { left: '100%' } ) )
        ] ),
        query( ':enter', [
          animate( '300ms ease-out', style( { left: '0%' } ) )
        ] )
      ] ),
      query( ':enter', animateChild() ),
    ] ),
    transition( '* <=> *', [
      style( { position: 'relative' } ),
      query( ':enter, :leave', [
        style( {
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%'
        } )
      ] ),
      query( ':enter', [
        style( { left: '-100%' } )
      ] ),
      query( ':leave', animateChild() ),
      group( [
        query( ':leave', [
          animate( '200ms ease-out', style( { left: '100%' } ) )
        ] ),
        query( ':enter', [
          animate( '300ms ease-out', style( { left: '0%' } ) )
        ] )
      ] ),
      query( ':enter', animateChild() ),
    ] )
  ] );

export const SlideInOutAnimation = [
  trigger( 'slideInOut', [

    state( 'in', style( {
      'max-height': '9000px', 'opacity': '1', 'visibility': 'visible', 'padding': '1.25rem'
    } ) ),

    state( 'out', style( {
      'max-height': '0px', 'opacity': '0', 'visibility': 'hidden', 'padding': '0'
    } ) ),

    state( 'minimized', style( {
      'max-height': '0px', 'opacity': '0', 'visibility': 'hidden', 'padding': '0'
    } ) ),

    transition( 'in => out', [ group( [
        animate( '300ms ease-in-out', style( {
          'opacity': '0'
        } ) ),
        animate( '300ms ease-in-out', style( {
          'max-height': '0px'
        } ) ),
        animate( '700ms ease-in-out', style( {
          'visibility': 'hidden'
        } ) ),
        animate( '800ms ease-in-out', style( {
          'padding': '0'
        } ) ),
      ]
    ) ] ),

    transition( 'minimized => in,out => in', [ group( [
        animate( '1ms ease-in-out', style( {
          'visibility': 'visible'
        } ) ),
        animate( '600ms ease-in-out', style( {
          'max-height': '9000px'
        } ) ),
        animate( '100ms ease-in-out', style( {
          'padding': '1.25rem'
        } ) ),
        animate( '700ms ease-in-out', style( {
          'opacity': '1'
        } ) )
      ]
    ) ] )
  ] ),
];
