import {Pipe, PipeTransform} from '@angular/core';
import {Implementation} from '../../../models/implementation.model';

@Pipe({name: 'filterNull'})
export class FilterNullPipe implements PipeTransform {
  transform(impl: Implementation[]): Implementation[] {
    const filtered = impl.filter(i => i.status != 'NULL');
    return filtered;
  }
}
