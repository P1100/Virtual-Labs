import {Student} from './student.model';
import {Image} from './image.model';

export class Implementation {
  constructor(
    public id: number,
    public status: number,   // status -->  {NULL, READ, SUBMITTED, REVIEWED, DEFINITIVE}
    public permanent: boolean,
    public grade: number,
    public readStatus: Date,
    public definitiveStatus: Date,
    public currentCorrection?: string,
    public creator?: Student,
    public imageSubmissions?: Image) {
  }
}
