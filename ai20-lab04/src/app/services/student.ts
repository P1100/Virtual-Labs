export class Student {
  id: number;
  serial: string;
  firstName: string;
  lastName: string;
  groupId: number;
  courseId: number;
  constructor(id: number, serial: string, name: string, surname: string, groupId: number, courseId: number) {
    this.id = id;
    this.serial = serial;
    this.firstName = name;
    this.lastName = surname;
    this.groupId = groupId;
    this.courseId = courseId;
  }
  //     this.displayedColumnsTable = ['select', 'id', 'firstName', 'lastName', 'group'];
}
