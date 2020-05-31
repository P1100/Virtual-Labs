import {Component, OnInit} from '@angular/core';
import {Student} from './student.module';

const DB_STUDENT: Student[] = [
  {id: '1', name: 'Pietro', firstName: 'Giasone'},
  {id: '2', name: 'Paolo', firstName: 'Ferri'},
  {id: '3', name: 'Gino', firstName: 'Rossi'},
  {id: '4', name: 'Giovanni', firstName: 'Bianchi'},
  {id: '5', name: 'Tizio', firstName: 'Tizio'},
  {id: '6', name: 'Caio', firstName: 'Caio'},
  {id: '7', name: 'Alberto', firstName: 'Terri'},
  {id: '8', name: 'Luca', firstName: 'Manni'},
  {id: '9', name: 'Valentina', firstName: 'Guano'},
].map((el) => new Student(el.id, el.name, el.firstName));

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'VirtualLabs';
  courses = ['Applicazioni Internet', 'Programmazione di sistema'];
  allStudents = DB_STUDENT;
  enrolledStudents: Student[] = [...DB_STUDENT.slice(2, 8)];

  ngOnInit(): void {
    console.log('On init: ' + this.title);
  }
}
