/* Also used for professor */
export class Student {

  constructor(public id: number,
              public firstName: string,
              public lastName: string,
              public email: string,
              public teamName?: string, // current active team, for course of getEnrolledStudents
              public proposalAccepted?:string,
              public proposalRejected?:string,
              public urlTokenConfirm?:string,
              public urlTokenReject?:string
  ) {}
}
