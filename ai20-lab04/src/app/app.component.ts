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
  enrolledStudents: Student[] = [...DB_STUDENT.slice(1, 5)];

  ngOnInit(): void {
    console.log('On init: ' + this.title);
  }
}

/*
  // private _submitMessage = '';
  toggleTitle(event: Event) {
    // get submitMessage() {
    //   return this._submitMessage;
    // }
    // onSubmit(form: NgForm) {
    //   this._submitMessage = 'Submitted. Form value is ' + JSON.stringify(form.value);
    this.title = (this.title === 'ai20-lab04') ? 'Applicazioni Internet (2020)' : 'ai20-lab04';
    console.log('toggleTitle: ' + this.title);
    console.log(event);
  }

today: number = Date.now();
@ViewChild('yourname') bindingInput: ElementRef;

// @ViewChild('itemForm') form: NgForm;
values = '';
// }
onKey(event: KeyboardEvent) {
  this.values += (event.target as HTMLInputElement).value + '|';
  console.log('element', event);
  console.log('element.value', event.target.value);
}
getHTMLAttributeValue(): any {
  console.warn('HTML attribute value: ' + this.bindingInput.nativeElement.getAttribute('value'));
}
getDOMPropertyValue(): any {
  console.warn('DOM property value: ' + this.bindingInput.nativeElement.value);
}

working(): any {
  console.warn('Test Button works!');
}

toggleDisabled(): any {
  const testButton = <HTMLInputElement> document.getElementById('testButton');
  testButton.disabled = !testButton.disabled;
  console.warn(testButton.disabled);
}

todayf() {
  return Date.now();
}

stateFlag = false;

toggleState() {
  this.stateFlag = !this.stateFlag;
  console.log(this.stateFlag);
}

calculateClasses() {
  console.log(this.stateFlag ? 'btn-success' : '');
  return {
    btn: true,
    'btn-primary': true,
    'btn-success': this.stateFlag
  };
}
*/
