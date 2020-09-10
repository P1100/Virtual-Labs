import {Image} from './image.model';
import {Implementation} from './implementation.model';

export class Assignment {
  constructor(
  public id: number,
  public releaseDate: Date,
  public expireDate: Date,
  public content: Image,
  public implementations: Implementation[],
  public name?: string) {
  }
}
