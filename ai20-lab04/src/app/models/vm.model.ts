export class Vm {
  constructor(public vcpu: number,
              public disk: number,
              public ram: number,
              public active: boolean,
              public studentCreatorId?: number,
              public teamId?: number,
              public id?: number) {
  }
}
