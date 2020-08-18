export class Student {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  constructor(id: number, firstName: string, lastName: string, email: string) {
    this.id = id;
    this.firstName = name;
    this.lastName = lastName;
    this.email = email;
  }
  toString(): string {
    return this.lastName + ' ' +
      this.firstName + ' (' +
      this.id + ')';
  }
}
