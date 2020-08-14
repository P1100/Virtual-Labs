import {Student} from './student.model';
import {Course} from './course.model';

export interface HateoasModel {
  _embedded?: {
    studentDTOList?: Student[];
    courseDTOList?: Course[];
    _links?: any;
    links?: any;
  };
}
