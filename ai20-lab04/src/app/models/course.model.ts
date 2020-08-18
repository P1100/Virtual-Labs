export class Course {
  id: string;
  fullName: string;
  minEnrolled: number;
  maxEnrolled: number;
  enabled: boolean;
  vmModelPath: string;
  // _embedded?: object;

  constructor(id: string, fullName: string, minEnrolled: number, maxEnrolled: number, enabled: boolean, vmmodelPath: string) {
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
      this.enabled + ' ' +
      this.vmModelPath;
  }
}
