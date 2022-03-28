import { CommonUtils } from '../utils/common.util';
import { Observable, throwError } from 'rxjs';
import { catchError, publishReplay, refCount } from 'rxjs/operators';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';

export const QUESTION_MARK = '?';
export const SEPARATOR = '&';
export const EQUALITY = '=';

export class BaseService {
    private static cache = new Map<string, Observable<any>>();
    private static loadIndicators: string[] = new Array<string>();

    public static DropCache() {
        this.cache = new Map<string, Observable<any>>();
    }

    constructor() {
    }

    protected useCache( type: string, obs: Observable<any> ) {
        if ( !BaseService.cache.get( type ) ) {
            BaseService.cache.set( type, obs.pipe(
                publishReplay( 1 ), // this tells Rx to cache the latest emitted
                refCount() // and this tells Rx to keep the Observable alive as long as there are any Subscribers
            ) );
            // Ez arra az esetre, ha hibára fut a lekérdezés, akkor ne kerüljön mentésre a cache-be!
            BaseService.cache.get( type ).toPromise().catch( err => {
                const ret = BaseService.cache.get( type );
                BaseService.cache.delete( type );
                return ret;
            } );
        } else {
            console.log( 'Load from cahce: ', type );
        }
        return BaseService.cache.get( type );
    }

    public createURI( baseURI: string, parameters: any, pagination?: any ) {

        if ( !baseURI.includes( QUESTION_MARK ) ) {
            baseURI += QUESTION_MARK;
        } else {
            baseURI += SEPARATOR;
        }

        if ( parameters != null ) {
            // REMOVE EMPTY ATTRIBBUTES
            Object.keys( parameters ).forEach( ( key ) => (CommonUtils.isEmpty( parameters[ key ] )) && delete parameters[ key ] );

            // CREATE QUERY PARAMETERS STRING
            const queryString = Object.keys( parameters ).map( ( key ) => {
                let value = parameters[ key ];
                if ( value instanceof Date ) {
                    value = value.toISOString();
                }
                if ( Array.isArray( value ) ) {
                    for ( let i = 0; i < value.length; i++ ) {
                        if ( value[ i ] instanceof Date ) {
                            value[ i ] = value[ i ].toISOString();
                            console.log( 'Date tömb' );
                        }
                    }
                }
                return [ key, value ].map( encodeURIComponent ).join( EQUALITY );
            } ).join( '&' );

            // ADD QUERY PARAMETERS
            baseURI += queryString;
        }

        // ADD PAGINATION QUERY
        if ( pagination != null ) {
            baseURI += parameters != null ? SEPARATOR : '';
            for ( const key in pagination ) {
                if ( pagination.hasOwnProperty( key ) && CommonUtils.isNotEmpty( pagination[ key ] ) ) {
                    if ( key === 'sort' && pagination.hasOwnProperty( 'sortDirection' ) && CommonUtils.isNotEmpty( pagination[ 'sortDirection' ] ) ) {
                        baseURI += key + EQUALITY + pagination[ key ] + ':' + pagination[ 'sortDirection' ] + SEPARATOR;
                    } else {
                        baseURI += key + EQUALITY + pagination[ key ] + SEPARATOR;
                    }
                }
            }
        }

        return baseURI;
    }

    addLoadingIndicator( tableBodyId: string ) {
        tableBodyId = tableBodyId + 'loader';
        BaseService.loadIndicators.push( tableBodyId );
        this.tryAddLoadingIndicators();
    }

    tryAddLoadingIndicators() {
        if (BaseService.loadIndicators && BaseService.loadIndicators.length > 0) {
            BaseService.loadIndicators.forEach( tableBodyId => {
                if ( document.getElementById( tableBodyId ) ) {
                    const mainElement = document.getElementById( tableBodyId );
                    const loadingDiv = document.createElement( 'div' );
                    loadingDiv.setAttribute( 'class', 'loader_shadow_div' );
                    loadingDiv.setAttribute( 'id', tableBodyId + '_shadow_div' );
                    const loaderDiv = document.createElement( 'div' );
                    loaderDiv.setAttribute( 'class', 'loader' );
                    loadingDiv.appendChild( loaderDiv );
                    mainElement.appendChild( loadingDiv );
                    BaseService.loadIndicators = BaseService.loadIndicators.filter( value => value !== tableBodyId );
                }
            } );
            if (BaseService.loadIndicators.length > 0) {
                setTimeout( this.tryAddLoadingIndicators, 100 );
            }
        }
    }

    removeLoadingIndicator( tableBodyId: string ) {
        tableBodyId = tableBodyId + 'loader';
        BaseService.loadIndicators = BaseService.loadIndicators.filter( value => value !== tableBodyId );
        const loadingDiv = document.getElementById( tableBodyId + '_shadow_div' );
        if ( loadingDiv ) {
            loadingDiv.parentNode.removeChild( loadingDiv );
        }
    }


}
