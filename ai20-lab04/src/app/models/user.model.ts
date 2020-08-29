import {Student} from './student.model';

export class User extends Student {
  constructor(id: number, firstName: string, lastName: string, email: string, public roles?: string[]) {
    super(id, firstName, lastName, email);
  }
}
