export class Team {
  id: number;
  name: string;
  active: boolean; // status
  maxVcpu: number;
  maxDisk: number;
  maxRam: number;
  maxRunningVM: number;
  maxTotVM: number; // sum of enabled and disabled

  constructor() {
  }
}
