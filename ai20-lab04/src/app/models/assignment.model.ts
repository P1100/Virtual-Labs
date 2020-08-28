export class Assignment {
  id: number;
  name?: string;
  releaseDate: string;
  expireDate: string;

  constructor(id: number, releaseDate: string, expireDate: string) {
    this.id = id;
    this.releaseDate = releaseDate;
    this.expireDate = expireDate;
  }

  toString(): string {
    return this.id + ' ' +
      this?.name + ' ' +
      this.releaseDate + ' ' +
      this.expireDate;
  }
}
