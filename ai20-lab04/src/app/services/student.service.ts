import {Injectable} from '@angular/core';
import {Student} from './student';

// TODO: aggiungere gruppi! ... e verificarne funzionalità (tabella, visualizzazione, inserimento)
// TODO: StudentsContComponent che conterrà le informazioni sul DB studenti e sugli studenti iscritti, informazioni che passerà al componente
// StudentsComponent attraverso il property binding
// TODO: aggiornare struttura DB a es5 formato
// TODO: usare observables nel progetto
// TODO: confrontare dati client (struttura) e dati REST dell'esercitazione server
// TODO: per progetto, questo é file DTO. Non c'é id, come id viene usata matricola/serial
const DB_STUDENT: Student[] = [
  {id: 1, serial: '123451', firstName: 'Pietro', lastName: 'Giasone', courseId: 1},
  {id: 2, serial: '123452', firstName: 'Paolo', lastName: 'Ferri', courseId: 1, groupId: 1},
  {id: 3, serial: '123453', firstName: 'Gino', lastName: 'Rossi', courseId: 1, groupId: 1},
  {id: 4, serial: '123454', firstName: 'Giovanni', lastName: 'Bianchi', courseId: 1, groupId: 2},
  {id: 5, serial: '123455', firstName: 'Tizio', lastName: 'Tizio', courseId: 1, groupId: 2},
  {id: 6, serial: '123456', firstName: 'Caio', lastName: 'Caio', courseId: 1, groupId: 2},
  {id: 7, serial: '123457', firstName: 'Alberto', lastName: 'Terri', courseId: 1, groupId: 0},
  {id: 8, serial: '123458', firstName: 'Luca', lastName: 'Manni', courseId: 1, groupId: 0},
  {
    id: 9, serial: '123459', firstName: 'Valentina', lastName: 'Guano', courseId: 2, groupId: 0
  }
].map((el) => new Student(el.id, el.serial, el.firstName, el.lastName, el.groupId, el.courseId));
//   {id: '1', firstName: 'Pietro', firstName: 'Giasone', group: 'none'},
//   {id: '2', firstName: 'Paolo', firstName: 'Ferri', group: 'Cavalry'},
//   {id: '3', firstName: 'Gino', firstName: 'Rossi', group: 'Cavalry'},
//   {id: '4', firstName: 'Giovanni', firstName: 'Bianchi', group: 'Cavalry2'},
//   {id: '5', firstName: 'Tizio', firstName: 'Tizio'},
//   {id: '6', firstName: 'Caio', firstName: 'Caio'},
//   {id: '7', firstName: 'Alberto', firstName: 'Terri'},
//   {id: '8', firstName: 'Luca', firstName: 'Manni'},
//   {id: '9', firstName: 'Valentina', firstName: 'Guano'},
// ].map((el) => new Student(el.id, el.firstName, el.firstName, el.group));

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  constructor() {
  }
  getStudents(): Student[] {
    return DB_STUDENT;
  }
  // create() {}
  // find() {}
  updateStudent(student: Student) {
  }
  // delete(){}
  queryStudents(): Student[] {
    return [];
  }
  enrollStudents(students: Student[], courseId: number) {
  }
  disenrollStudents(students: Student[], courseId: number) {
  }
  getEnrolledCourse(courseId: number) {
  }
}

function test_add(x, y): number {
  return x + y;
}
