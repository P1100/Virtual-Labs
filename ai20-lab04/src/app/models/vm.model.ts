import {Student} from './student.model';

export class Vm {
  constructor(public vcpu: number,
              public disk: number,
              public ram: number,
              public active: boolean,
              public teamId?: number,
              public studentCreatorId?: number, // dont delete, used by server
              public creator?: Student,
              public sharedOwners?: Student[],
              public id?: number) {
  }
}
