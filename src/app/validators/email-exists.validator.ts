// email-exists.validator.ts
import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { UserService } from '../services/user.service';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

export function emailExistsValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl) => {
    if (!control.value) return of(null);

    console.log('Validating email:', control.value);

    return userService.checkEmailExists(control.value).pipe(
      map(exists => {
        console.log('Email exists:', exists);
        return exists ? { emailTaken: true } : null;
      }),
      catchError(() => of(null))
    );
  };
}

