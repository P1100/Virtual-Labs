export class Image {
  id: number;
  status: number; // {NULL, READ, SUBMITTED, REVIEWED, DEFINITIVE}
  permanent: boolean;
  grade: number;
  readStatus: Date;
  definitiveStatus: Date;

  constructor() {
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
