export class Student {
  id: number;
  serial: string;
  name: string;
  surname: string;
  groupId: number;
  courseId: number;
  constructor(id: number, serial: string, name: string, surname: string, groupId: number, courseId: number) {
    this.id = id;
    this.serial = serial;
    this.name = name;
    this.surname = surname;
    this.groupId = groupId;
    this.courseId = courseId;
  }
}
