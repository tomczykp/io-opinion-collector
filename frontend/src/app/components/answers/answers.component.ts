import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router'
import { NgbModal } from '@ng-bootstrap/ng-bootstrap'
import { Answer } from 'src/app/model/Answer'
import { AuthService } from 'src/app/services/auth.service'
import { QAService } from 'src/app/services/qa.service'
import { AnswerReportModalComponent } from './report/answer-report-modal.component'

@Component({
	selector: 'app-answers',
	templateUrl: './answers.component.html',
	styleUrls: ['./answers.component.css']
})
export class AnswersComponent implements OnInit, OnChanges {
	answers: Answer[] = []
	authenticated: boolean
	role: string
	username: string
	@Input() questionId: string
	answerForm: FormGroup
	submitted: Boolean = false
	reply: boolean = false
	highlightedId: string

	constructor(
		private qaService: QAService,
		private formBuilder: FormBuilder,
		private authService: AuthService,
		private modalService: NgbModal,
		protected route: ActivatedRoute,
		protected router: Router
	) {}

	ngOnInit(): void {
		this.authService.authenticated.subscribe((change) => {
			this.authenticated = change

			if (this.authenticated) {
				this.username = this.authService.getUsername()!
				this.role = this.authService.getRole()!
			}
		})
		this.answerForm = this.formBuilder.group({
			answer: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]]
		})

		this.route.queryParamMap.subscribe((params) => {
			this.highlightedId = params.get('highlightAnswer') || ''
		})
	}

	ngOnChanges(changes: SimpleChanges): void {
		this.qaService.getAnswersOfQuestion(this.questionId).subscribe((value) => {
			this.answers = value
		})
	}

	ngAfterViewInit(): void {
		setTimeout(() => {
			if (this.highlightedId != '') {
				console.log(this.highlightedId)
				const itemToScrollTo = document.getElementById(this.highlightedId)
				if (itemToScrollTo) itemToScrollTo.scrollIntoView(true)
			} else {
				return
			}
		}, 1000)
	}

	deleteAnswer(id: string): void {
		this.qaService.deleteAnswer(id)
		location.reload()
	}

	onSubmit() {
		this.submitted = true
		if (this.answerForm.invalid) {
			return false
		} else {
			let answer = <Answer>{}
			answer.content = this.answerForm.controls['answer'].value
			answer.questionId = this.questionId
			this.qaService.addAnswer(answer)
		}
		this.router
			.navigate([], {
				relativeTo: this.route,
				queryParams: { highlightQuestion: null, highlightOpinion: null, highlightAnswer: null },
				queryParamsHandling: 'merge'
			})
			.then(() => {
				location.reload()
			})
		return true
	}

	onReply() {
		this.reply = !this.reply
	}

	openReportModal(id: string) {
		const modalRef = this.modalService.open(AnswerReportModalComponent, { centered: true })

		;(modalRef.componentInstance as AnswerReportModalComponent).answerId = id
	}
}
