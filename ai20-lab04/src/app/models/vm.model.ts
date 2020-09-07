export class Vm {
  constructor(public vcpu: number,
              public disk: number,
              public ram: number,
              public studentCreatorId?: number,
              public teamId?: number,
              public id?: number,
              public active?: boolean) {
  }
}
