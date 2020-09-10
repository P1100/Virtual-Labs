import {Image} from './image.model';
import {Implementation} from './implementation.model';

export class Assignment {
  id: number;
  name?: string;
  releaseDate: string;
  expireDate: string;
  content: Image;
  implementations: Implementation[];

  constructor(id: number, releaseDate: string, expireDate: string) {
    this.id = id;
    this.releaseDate = releaseDate;
    this.expireDate = expireDate;
  }
}
