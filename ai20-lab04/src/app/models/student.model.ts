export class Student {
  id: number;
  firstName: string;
  lastName: string;
  profilePhoto: string;
  constructor(id: number, serial: string, name: string, lastName: string, profilePhoto: string) {
    this.id = id;
    this.firstName = name;
    this.lastName = lastName;
    this.profilePhoto = profilePhoto;
  }
  //     this.displayedColumnsTable = ['select', 'id', 'firstName', 'lastName', 'group'];
}
