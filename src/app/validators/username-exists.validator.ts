// username-exists.validator.ts
import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { UserService } from '../services/user.service';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

export function usernameExistsValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl) => {
    if (!control.value) return of(null);
    console.log('Validating username:', control.value);
    return userService.checkUsernameExists(control.value).pipe(
      map(exists => {
console.log('Username exists:', exists);
return exists ? { usernameTaken: true } : null;
}),
      catchError(() => of(null))
    );
  };
}
