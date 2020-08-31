export class User {
  constructor(public username: string,
              public password: string,
              public id: number,
              public firstName: string,
              public lastName: string,
              public email: string,
              public roles?: string[],
              public imageId?: number) {
  }
}
