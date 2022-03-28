import { Directive, ElementRef, forwardRef, HostListener, Input, OnChanges, OnInit, Renderer2, SimpleChanges } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CommonUtils } from '../../core/utils/common.util';

export function createInputNumberValidator( regex, max, min, maxlenght, required ) {
  return ( c: FormControl ) => {
    let isValid = true;
    let err = {};
    let re: RegExp;
    if ( CommonUtils.isNotEmpty( regex ) ) {
      re = new RegExp( regex );
      if ( !re.test( c.value ) ) {
        err = {
          regexError: {
            given: c.value,
            regex: regex
          }
        };
        isValid = false;
      }
    }
    if ( CommonUtils.isNotEmpty( max ) && Number( c.value ) > max ) {
      err = {
        maxError: {
          given: c.value,
          max: max
        }
      };
      isValid = false;
    }
    if ( CommonUtils.isNotEmpty( min ) && Number( c.value ) < min ) {
      err = {
        minError: {
          given: c.value,
          min: min
        }
      };
      isValid = false;
    }
    if ( CommonUtils.isNotEmpty( maxlenght ) && c.value && c.value.length > maxlenght ) {
      err = {
        maxlenghtError: {
          given: c.value,
          maxlenght: maxlenght
        }
      };
      isValid = false;
    }
    if ( required && CommonUtils.isEmpty( c.value ) ) {
      err = {
        requiredError: {
          given: c.value,
          required: required
        }
      };
      isValid = false;
    }
    if ( !required && CommonUtils.isEmpty( c.value ) ) {
      isValid = true;
    }
    return isValid ? null : err;
  };
}

@Directive( {
  selector: '[peasNumberChecker]',
  providers: [
    { provide: NG_VALUE_ACCESSOR, useExisting: forwardRef( () => NumberCheckerDirective ), multi: true },
    { provide: NG_VALIDATORS, useExisting: forwardRef( () => NumberCheckerDirective ), multi: true }
  ]
} )
export class NumberCheckerDirective implements ControlValueAccessor, OnChanges, OnInit {
  @Input() min: number;
  @Input() max: number;
  @Input() maxlength: number;
  @Input() regex = '[-.0-9]+';
  @Input() regexErrorCode = 'number_checker.warning.universal_regex_error';
  @Input() required = false;
  @Input() integer = false;
  @Input() negative = false;

  prevNumber: number;

  prevKey: string;
  showMinError = false;
  // label: Label;

  propagateChange: any = () => {
  }
  validateFn: any = () => {
  }

  constructor( private renderer: Renderer2,
               private hostElement: ElementRef,
               // private toastr: ToastsManager,
               // private translate: TranslateService
  ) {
    // this.label = new Label(this.translate);
  }

  ngOnInit() {
    // console.log( 'Init number check - ', this.integer );
    if ( !this.negative && CommonUtils.isEmpty( this.min ) ) {
      this.min = 0;
    }
    if ( this.regex ) {
      if ( !this.regex.startsWith( '^' ) ) {
        this.regex = '^' + this.regex;
      }
      if ( !this.regex.endsWith( '$' ) ) {
        this.regex = this.regex + '$';
      }
    }
  }

  @HostListener( 'input', [ '$event' ] )
  changeDetect( event ) {
    let re: RegExp;
    this.showMinError = false;
    const newvalue: string = event.target.value;
    if ( CommonUtils.isEmpty( newvalue ) ) {
      if ( CommonUtils.isEmpty( this.prevNumber ) ) {
        this.prevNumber = null;
        this.propagateChange( null );
      } else {
        if ( event.data !== null ) {
          this.hostElement.nativeElement.value = this.prevNumber;
        } else {
          this.prevNumber = null;
          this.propagateChange( null );
        }
      }
    } else {
      if ( CommonUtils.isNotEmpty( this.regex ) ) {
        re = new RegExp( this.regex );
      }
      if ( (CommonUtils.isNotEmpty( this.regex ) && !re.test( newvalue )) ||
        (CommonUtils.isNotEmpty( this.max ) && Number( newvalue ) > this.max) ||
        (CommonUtils.isNotEmpty( this.min ) && Number( newvalue ) < this.min && this.min <= 0) ||
        (CommonUtils.isNotEmpty( this.maxlength ) && newvalue.toString().length > this.maxlength) ) {
        if ( CommonUtils.isNotEmpty( this.regex ) && !re.test( newvalue ) ) {
          // this.toastr.warning(this.label.getLabel(this.regexErrorCode));
        }
        if ( CommonUtils.isNotEmpty( this.max ) && Number( newvalue ) > this.max ) {
          // this.toastr.warning(this.label.getLabelWithParams('number_checker.warning.max_number', { value: this.max}));
          console.log( 'Túl nagy az érték, ', this.max );
        }
        if ( CommonUtils.isNotEmpty( this.min ) && Number( newvalue ) < this.min ) {
          // this.toastr.warning(this.label.getLabelWithParams('number_checker.warning.min_number', { value: this.min}));
          console.log( 'Túl kicsi az érték, ', this.min );
        }
        if ( CommonUtils.isNotEmpty( this.maxlength ) && newvalue.toString().length > this.maxlength ) {
          // this.toastr.warning(this.label.getLabelWithParams('number_checker.warning.max_length', { value: this.maxlength}));
          console.log( 'Túl hosszú az érték, ', this.maxlength );
        }
        this.hostElement.nativeElement.value = this.prevNumber;
      } else {
        if ( CommonUtils.isEmpty( newvalue ) ) {
          this.prevNumber = null;
          this.propagateChange( null );
        } else {
          this.prevNumber = Number( newvalue );
          if ( event.target.value.length > 1 && event.target.value.startsWith( '0' ) ) {
            this.hostElement.nativeElement.value = Number( newvalue );
          }
          this.validateFn = createInputNumberValidator( this.regex, this.max, this.min, this.maxlength, this.required );
          this.propagateChange( newvalue );
        }
      }
    }
  }

  @HostListener( 'change', [ '$event' ] )
  outDetect( event ) {
    if ( CommonUtils.isNotEmpty( this.min ) ) {
      const newvalue = event.target.value;
      if ( newvalue.length !== 0 && Number( newvalue ) < this.min ) {
        this.hostElement.nativeElement.value = null;
        this.prevNumber = null;
        this.propagateChange( null );
        this.showMinError = true;
      }
    }
  }

  @HostListener( 'keydown', [ '$event' ] )
  keyPress( event ) {
    if ( event.key === '-' && this.negative && event.target.selectionStart <= 0 ) {
      this.prevKey = event.key;
      return true;
    }
    if ( event.key === '-' || event.key === '+' || event.key === 'e' || event.key === ',' ) {
      this.prevKey = event.key;
      if ( event.target.value.length === 1 && event.key === '-' ) {
        // this.toastr.warning(this.label.getLabel('number_checker.warning.positive_only'));
        console.log( 'Csak pozítiv szám adható meg' );
      } else {
        // this.toastr.warning(this.label.getLabelWithParams('number_checker.warning.wrong_key', {keys: '- + e ,'}));
        console.log( 'Csak szám adható meg' );
      }
      return false;
    }
    if ( (event.target.value.length <= 0 || this.integer || event.target.value.indexOf( '.' ) >= 0)
      && event.key === '.' ) {
      this.prevKey = event.key;
      if ( event.target.value.indexOf( '.' ) >= 0 ) {
        // this.toastr.warning(this.label.getLabel('number_checker.warning.only_one_point'));
        console.log( 'Csak egy tizedes jel adahtó meg' );
      } else {
        // this.toastr.warning(this.label.getLabel('number_checker.warning.integer_only'));
        console.log( 'Csak egész szám adható meg' );
      }
      return false;
    }
    this.prevKey = event.key;
    return true;
  }

  writeValue( value: number ): void {
    this.hostElement.nativeElement.value = CommonUtils.isEmpty(value) ? '' : value;
  }

  registerOnChange( fn: any ): void {
    this.propagateChange = fn;
  }

  registerOnTouched( fn: any ): void {
  }

  setDisabledState( isDisabled: boolean ): void {
  }

  ngOnChanges( changes: SimpleChanges ): void {
    if ( changes.number ) {
      this.prevNumber = changes.number.currentValue;
    }
  }

  validate( c: FormControl ) {
    return this.validateFn( c );
  }
}
