/* Also used for professor */
export class Student {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  /* current active team, for course of getEnrolledStudents */
  teamName?: string;

  constructor(id: number, firstName: string, lastName: string, email: string, teamName: string) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.teamName = teamName;
  }
}
