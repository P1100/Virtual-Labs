import {Component, Input, OnInit} from '@angular/core';
import {Student} from '../../student';
import {Course} from '../../course';

// TODO: aggiungere gruppi! ... e verificarne funzionalità (tabella, visualizzazione, inserimento)
// TODO: StudentsContComponent che conterrà le informazioni sul DB studenti e sugli studenti iscritti, informazioni che passerà al componente
// StudentsComponent attraverso il property binding
//TODO: aggiornare struttura DB a es5 formato
const DB_STUDENT: Student[] = [
  {id: '1', name: 'Pietro', firstName: 'Giasone', group: 'none'},
  {id: '2', name: 'Paolo', firstName: 'Ferri', group: 'Cavalry'},
  {id: '3', name: 'Gino', firstName: 'Rossi', group: 'Cavalry'},
  {id: '4', name: 'Giovanni', firstName: 'Bianchi', group: 'Cavalry2'},
  {id: '5', name: 'Tizio', firstName: 'Tizio'},
  {id: '6', name: 'Caio', firstName: 'Caio'},
  {id: '7', name: 'Alberto', firstName: 'Terri'},
  {id: '8', name: 'Luca', firstName: 'Manni'},
  {id: '9', name: 'Valentina', firstName: 'Guano'},
].map((el) => new Student(el.id, el.name, el.firstName, el.group));
const DB_COURSES: Course[] = [
  {id: 1, title: 'Applicazioni Internet', path: 'applicazioni-internet'},
  {id: 1, title: 'Programmazione di sistema', path: 'programmazione-di-sistema'},
  {id: 1, title: 'Mobile development', path: 'mobile-development'}
];

@Component({
  selector: 'app-students-cont',
  templateUrl: './students-cont.component.html',
  styleUrls: ['./students-cont.component.css']
})
export class StudentsContComponent implements OnInit {
  allStudents = DB_STUDENT;
  enrolledStudents: Student[] = [...DB_STUDENT.slice(2, 8)];
  courses = DB_COURSES;
  @Input()
  title;

  constructor() {
  }

  ngOnInit(): void {
  }

}
