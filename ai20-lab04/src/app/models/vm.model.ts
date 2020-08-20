export class Vm {
  constructor(public id: number,
              public vcpu: number,
              public disk: number,
              public ram: number,
              public active: boolean) {
  }
}
