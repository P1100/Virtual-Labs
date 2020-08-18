import {Student} from './student.model';
import {Course} from './course.model';

export interface HateoasModel {
  _links?: any;
  links?: any;
  _embedded?: {
    studentDTOList?: Student[];
    courseDTOList?: Course[];
    _links?: any;
    links?: any;
  };
}
