import { Component } from '@angular/core'
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap'
import { QAService } from 'src/app/services/qa.service'

@Component({
	selector: 'app-question-report-modal',
	templateUrl: './question-report-modal.component.html'
})
export class QuestionReportModalComponent {
	protected reason = ''

	questionId: string

	constructor(private qaService: QAService, protected activeModal: NgbActiveModal) {}

	onSubmit() {
		this.qaService.reportQuestion(this.questionId, this.reason)
		this.activeModal.close()
	}
}
