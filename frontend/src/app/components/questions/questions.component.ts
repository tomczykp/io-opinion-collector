import { AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router'
import { NgbModal } from '@ng-bootstrap/ng-bootstrap'
import { Question } from 'src/app/model/Question'
import { AuthService } from 'src/app/services/auth.service'
import { QAService } from 'src/app/services/qa.service'
import { QuestionReportModalComponent } from './report/question-report-modal.component'

@Component({
	selector: 'app-questions',
	templateUrl: './questions.component.html',
	styleUrls: ['./questions.component.css']
})
export class QuestionsComponent implements OnInit, OnChanges, AfterViewInit {
	questionForm: FormGroup

	counter: number
	authenticated: boolean
	role: string
	username: string
	submitted: Boolean = false
	questions: Question[] = []
	@Input() productId: string
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
		this.route.queryParamMap.subscribe((params) => {
			this.highlightedId = params.get('highlightQuestion') || ''
		})
	}

	ngAfterViewInit(): void {
		setTimeout(() => {
			if (this.highlightedId != '') {
				console.log(this.highlightedId)
				const itemToScrollTo = document.getElementById(this.highlightedId)
				if (itemToScrollTo) itemToScrollTo.scrollIntoView({behavior: "smooth", block: "center", inline: "center"})
			} else {
				return
			}
		}, 1000)
	}

	ngOnChanges(changes: SimpleChanges): void {
		this.qaService.getQuestionsOfProduct(this.productId).subscribe((value) => {
			this.questions = value
		})
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

	deleteQuestion(id: string): void {
		this.qaService.deleteQuestion(id)
		location.reload()
	}

	openReportModal(id: string) {
		const modalRef = this.modalService.open(QuestionReportModalComponent, { centered: true })

		;(modalRef.componentInstance as QuestionReportModalComponent).questionId = id
	}
}
