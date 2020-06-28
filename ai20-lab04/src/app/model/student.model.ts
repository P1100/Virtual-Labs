export class Student {
  id: number;
  serial: string;
  firstName: string;
  lastName: string;
  groupId: number;
  courseId: number;
  constructor(id: number, serial: string, name: string, lastName: string, groupId: number, courseId: number) {
    this.id = id;
    this.serial = serial;
    this.firstName = name;
    this.lastName = lastName;
    this.groupId = groupId;
    this.courseId = courseId;
  }
  //     this.displayedColumnsTable = ['select', 'id', 'firstName', 'lastName', 'group'];
}
