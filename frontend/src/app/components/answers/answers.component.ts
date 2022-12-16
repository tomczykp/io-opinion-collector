import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Answer } from 'src/app/model/Answer';
import { AuthService } from 'src/app/services/auth.service';
import { QAService } from 'src/app/services/qa.service';

@Component({
  selector: 'app-answers',
  templateUrl: './answers.component.html',
  styleUrls: ['./answers.component.css']
})
export class AnswersComponent implements OnInit, OnChanges  {

  answers: Answer[] = [];
  authenticated: boolean;
  role: string;
  @Input() questionId: string;
  answerForm: FormGroup;
  submitted: Boolean = false;
  reply: boolean = false;
  
  
  constructor(private qaService: QAService,
            private formBuilder: FormBuilder,
            private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;

      if (this.authenticated) {
        this.role = localStorage.getItem("role")!;
      }
    })
    this.answerForm = this.formBuilder.group({
      answer: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.qaService.getAnswersOfQuestion(this.questionId).subscribe(value => {
      this.answers = value;
      console.log(this.answers)
    });
  }

  deleteAnswer(id: string): void {
    this.qaService.deleteAnswer(id);
    location.reload();
  }

  onSubmit() {
    this.submitted = true;
    if (this.answerForm.invalid) {
      return false;
    } else {
      let answer = <Answer>{};
      answer.content = this.answerForm.controls['answer'].value;
      answer.questionId = this.questionId;
      this.qaService.addAnswer(answer);
    }
    location.reload();
    return true;
  }

  onReply() {
    this.reply = !this.reply;
  }
 

}
