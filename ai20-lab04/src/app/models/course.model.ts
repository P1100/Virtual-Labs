export class Course {
  id: string;
  fullName: string;
  minEnrolled: number;
  maxEnrolled: number;
  enabled: boolean;
  vmModelPath: string;

  constructor(id: string, fullName: string, minEnrolled: number, maxEnrolled: number, enabled: boolean, vmModelPath: string) {
    this.id = id;
    this.fullName = fullName;
    this.minEnrolled = minEnrolled;
    this.maxEnrolled = maxEnrolled;
    this.enabled = enabled;
    this.vmModelPath = vmModelPath;
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
