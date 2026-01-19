// mobile-exists.validator.ts
import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { UserService } from '../services/user.service';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

export function mobileExistsValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl) => {
    if (!control.value) return of(null);
    console.log('Validating mobile:', control.value);
    return userService.checkMobileExists(control.value).pipe(
      map(exists => {
console.log('Mobile number exists:', exists);
return exists ? { mobileTaken: true } : null;
}),
      catchError(() => of(null))
    );
  };
}
