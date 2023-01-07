import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { Question } from 'src/app/model/Question'
import { AuthService } from 'src/app/services/auth.service'
import { QAService } from 'src/app/services/qa.service'

@Component({
	selector: 'app-questions',
	templateUrl: './questions.component.html',
	styleUrls: ['./questions.component.css']
})
export class QuestionsComponent implements OnInit, OnChanges {
	questionForm: FormGroup

	counter: number
	authenticated: boolean
	role: string
	username: string
	submitted: Boolean = false
	questions: Question[] = []
	@Input() productId: string

	constructor(
		private qaService: QAService,
		private formBuilder: FormBuilder,
		private authService: AuthService
	) {}

	ngOnInit(): void {
		this.questionForm = this.formBuilder.group({
			question: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]]
		})
		this.authService.authenticated.subscribe((change) => {
			this.authenticated = change

			if (this.authenticated) {
				this.username = this.authService.getUsername()!
				this.role = this.authService.getRole()!
			}
		})
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (!changes['productId'].isFirstChange()) {
			this.qaService.getQuestionsOfProduct(this.productId).subscribe((value) => {
				this.questions = value
				console.log(value)
			})
		}
	}

	onSubmit() {
		this.submitted = true
		if (this.questionForm.invalid) {
			return false
		} else {
			let question = <Question>{}
			question.content = this.questionForm.controls['question'].value
			question.productId = this.productId
			this.qaService.addQuestion(question)
		}
		location.reload()
		return true
	}

	deleteQuestion(id: string): void {
		this.qaService.deleteQuestion(id)
		location.reload()
	}
}
