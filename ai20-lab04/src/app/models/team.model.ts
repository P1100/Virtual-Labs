import {Student} from './student.model';

export class Team {
  constructor(public id: number,
              public name: string,
              public active: boolean, // status
              public maxVcpu: number,
              public maxDisk: number,
              public maxRam: number,
              public maxRunningVM: number,
              public maxTotVM: number, // sum of enabled and disabled)
              public disabled: boolean,
              private createdDate: any,
              /* Proposals DTO Transient data */
              public students?: Student[]) {
  }
}
