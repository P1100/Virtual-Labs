export class Student {
  id: number;
  serial: string;
  firstName: string;
  lastName: string;
  groupId: number;
  courseId: number;
  print1234() {
    console.log('$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$');
  }
  constructor(id: number, serial: string, name: string, lastName: string, groupId: number, courseId: number) {
    this.id = id;
    this.serial = serial;
    this.firstName = name;
    this.lastName = lastName;
    this.groupId = groupId;
    this.courseId = courseId;
    Object.setPrototypeOf(this, Student.prototype);
  }
  // toString(): string {
  // }
  // public tooString = () : string => {
  //   // return `Foo (id: ${this.id})`;
  //   // const s = this.firstName + this.lastName + '(' + this.id.toString() + ')';
  //   // return s;
  // }

  //     this.displayedColumnsTable = ['select', 'id', 'firstName', 'lastName', 'group'];
}
