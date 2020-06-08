import {Component, Input, OnInit} from '@angular/core';
import {Student} from '../../student';

// TODO: aggiungere gruppi! ... e verificarne funzionalità (tabella, visualizzazione, inserimento)
// TODO: StudentsContComponent che conterrà le informazioni sul DB studenti e sugli studenti iscritti, informazioni che passerà al componente
// StudentsComponent attraverso il property binding
// TODO: aggiornare struttura DB a es5 formato
// TODO: usare observables nel progetto
// TODO: confrontare dati client (struttura) e dati REST dell'esercitazione server
const DB_STUDENT: Student[] = [
  {id: 1, serial: '123451', name: 'Pietro', surname: 'Giasone', courseId: 1, groupId: 1},
  {id: 2, serial: '123452', name: 'Paolo', surname: 'Ferri', courseId: 1, groupId: 1},
  {id: 3, serial: '123453', name: 'Gino', surname: 'Rossi', courseId: 1, groupId: 1},
  {id: 4, serial: '123454', name: 'Giovanni', surname: 'Bianchi', courseId: 1, groupId: 2},
  {id: 5, serial: '123455', name: 'Tizio', surname: 'Tizio', courseId: 1, groupId: 2},
  {id: 6, serial: '123456', name: 'Caio', surname: 'Caio', courseId: 1, groupId: 2},
  {id: 7, serial: '123457', name: 'Alberto', surname: 'Terri', courseId: 1, groupId: 0},
  {id: 8, serial: '123458', name: 'Luca', surname: 'Manni', courseId: 1, groupId: 0},
  {
    id: 9, serial: '123459', name: 'Valentina', surname: 'Guano', courseId: 2, groupId: 0
  }
].map((el) => new Student(el.id, el.serial, el.name, el.surname, el.groupId, el.courseId));
//   {id: '1', name: 'Pietro', firstName: 'Giasone', group: 'none'},
//   {id: '2', name: 'Paolo', firstName: 'Ferri', group: 'Cavalry'},
//   {id: '3', name: 'Gino', firstName: 'Rossi', group: 'Cavalry'},
//   {id: '4', name: 'Giovanni', firstName: 'Bianchi', group: 'Cavalry2'},
//   {id: '5', name: 'Tizio', firstName: 'Tizio'},
//   {id: '6', name: 'Caio', firstName: 'Caio'},
//   {id: '7', name: 'Alberto', firstName: 'Terri'},
//   {id: '8', name: 'Luca', firstName: 'Manni'},
//   {id: '9', name: 'Valentina', firstName: 'Guano'},
// ].map((el) => new Student(el.id, el.name, el.firstName, el.group));

@Component({
  selector: 'app-students-cont',
  templateUrl: './students-cont.component.html',
  styleUrls: ['../../../_unused/students-cont.component.css']
})
export class StudentsContComponent implements OnInit {
  fatherAllStudents = DB_STUDENT;
  fatherEnrolledStudents: Student[] = [...DB_STUDENT.slice(2, 8)];
  @Input()
  title;

  constructor() {
  }

  ngOnInit(): void {
  }

}
