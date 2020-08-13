import {Component, OnInit} from '@angular/core';
import {BackendService} from '../../services/backend.service';
import {CourseService} from '../../services/course-service';

@Component({
  selector: 'app-testing',
  templateUrl: './testing.component.html',
  styleUrls: ['./testing.component.css']
})
export class TestingComponent implements OnInit {

  constructor(backendService: BackendService, courseService: CourseService) {
  }

  ngOnInit(): void {
  }

}
