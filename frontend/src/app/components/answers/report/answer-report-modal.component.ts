import { Component } from '@angular/core'
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap'
import { QAService } from 'src/app/services/qa.service'

@Component({
	selector: 'app-answer-report-modal',
	templateUrl: './answer-report-modal.component.html'
})
export class AnswerReportModalComponent {
	protected reason = ''

	answerId: string

	constructor(private qaService: QAService, protected activeModal: NgbActiveModal) {}

	onSubmit() {
		this.qaService.reportAnswer(this.answerId, this.reason)
		this.activeModal.close()
	}
}
