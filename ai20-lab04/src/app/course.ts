export class Course {
  id: number;
  title: string;
  path: string;
  constructor(id: number, title: string, path: string) {
    this.id = id;
    this.title = title;
    this.path = path;
  }
}
