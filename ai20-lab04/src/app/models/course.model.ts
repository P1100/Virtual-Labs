export class Course {
  id: number;
  fullName: string;
  minEnrolled: number;
  maxEnrolled: number;
  enabled: boolean;
  label: string;
  path: string;
  constructor(id: number, label: string, path: string) {
    this.id = id;
    this.label = label;
    this.path = path;
  }
}
