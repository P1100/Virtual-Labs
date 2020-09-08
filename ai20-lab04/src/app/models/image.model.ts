export class Image {
  constructor(public id: number,
              public name: string,
              public type: string,
              public revisionCycle: number,
              public createDate: Date,
              public modifyDate: Date,
              public imageStringBase64: string,
              public directLink?: string) {
  }

}
