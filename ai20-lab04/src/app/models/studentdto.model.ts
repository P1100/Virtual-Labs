import {Student} from './student.model';

export interface StudentDto {
  _embedded: {
    studentDTOList: Student[];
    _links: [];
  };
}
