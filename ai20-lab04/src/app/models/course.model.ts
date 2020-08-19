export class Course {
  id: string;
  fullName: string;
  minSizeGroup: number;
  maxSizeGroup: number;
  enabled: boolean;
  vmModelPath: string;

  constructor(id: string, fullName: string, minEnrolled: number, maxEnrolled: number, enabled: boolean, vmModelPath: string) {
    this.id = id;
    this.fullName = fullName;
    this.minSizeGroup = minEnrolled;
    this.maxSizeGroup = maxEnrolled;
    this.enabled = enabled;
    this.vmModelPath = vmModelPath;
  }
  toString(): string {
    return this.id + ' ' +
      this.fullName + ' ' +
      this.minSizeGroup + ' ' +
      this.maxSizeGroup + ' ' +
      this.enabled + ' ' +
      this.vmModelPath;
  }
}
