export class Course {
  id: number;
  fullName: string;
  minEnrolled: number;
  maxEnrolled: number;
  enabled: boolean;
  _embedded: object;

  constructor(id: number, fullName: string, minEnrolled: number, maxEnrolled: number, enabled: boolean) {
    this.id = id;
    this.fullName = fullName;
    this.minEnrolled = minEnrolled;
    this.maxEnrolled = maxEnrolled;
    this.enabled = enabled;
  }
  toString(): string {
    return this.id + ' ' +
      this.fullName + ' ' +
      this.minEnrolled + ' ' +
      this.maxEnrolled + ' ' +
      this.enabled;
  }
}
