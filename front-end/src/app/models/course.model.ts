export class Course {
  id: string;
  fullName: string;
  minSizeTeam: number;
  maxSizeTeam: number;
  enabled: boolean;
  vmModelPath?: string;

  constructor(id: string, fullName: string, minEnrolled: number, maxEnrolled: number, enabled: boolean, vmModelPath: string) {
    this.id = id;
    this.fullName = fullName;
    this.minSizeTeam = minEnrolled;
    this.maxSizeTeam = maxEnrolled;
    this.enabled = enabled;
    this.vmModelPath = vmModelPath;
  }
  toString(): string {
    return this.id + ' ' +
      this.fullName + ' ' +
      this.minSizeTeam + ' ' +
      this.maxSizeTeam + ' ' +
      this.enabled + ' ' +
      this?.vmModelPath;
  }
}
