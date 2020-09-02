/* Also used for professor */
export class Student {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  teamName?: string;

  constructor(id: number, firstName: string, lastName: string, email: string) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  toString(): string {
    return this.lastName + ' ' +
      this.firstName + ' (' +
      this.id + ')' + ' (' +
      this.teamName + ')' + ' (' +
      this.email + ')'
      ;
  }
}
