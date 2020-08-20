export class Image {
  constructor(
    public id: number,
    public status: number,   // status -->  {NULL, READ, SUBMITTED, REVIEWED, DEFINITIVE}
    public permanent: boolean,
    public grade: number,
    public readStatus: Date,
    public definitiveStatus: Date) {
  }
//   toString(): string {
//   return this. + ' ' +
//     this. + ' ' +
//     this. + ' ' +
//     this. + ' ' +
//     this. + ' ' +
//     this. + ' ' +
//     this. + ' ' +
//     this. + ' ' +;
// }

}
