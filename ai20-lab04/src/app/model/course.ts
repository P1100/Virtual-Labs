export class Course {
  id: number;
  label: string;
  path: string;
  constructor(id: number, label: string, path: string) {
    this.id = id;
    this.label = label;
    this.path = path;
  }
}
