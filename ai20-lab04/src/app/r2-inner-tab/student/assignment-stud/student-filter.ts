import {Pipe, PipeTransform} from '@angular/core';
import {Implementation} from '../../../models/implementation.model';

@Pipe({name: 'filterStudent'})
export class FilterStudentPipe implements PipeTransform {
  transform(data: Implementation[], studentId: number): Implementation[] {
    let filtered = [...data];
    for (let impl of data) {
      if (impl?.student?.id != studentId)
        filtered = filtered.filter(i => i?.id != impl?.id);
    }
    return filtered;
  }
}
