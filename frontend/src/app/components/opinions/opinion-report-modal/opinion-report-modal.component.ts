import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { OpinionId } from 'src/app/model/Opinion';
import { OpinionService } from 'src/app/services/opinion.service';

@Component({
    selector: 'app-opinion-report-modal',
    templateUrl: './opinion-report-modal.component.html'
})
export class OpinionReportModalComponent
{
    protected reason = '';

    opinionId: OpinionId;

    constructor(
        private opinionService: OpinionService,
        protected activeModal: NgbActiveModal
    ) { }

    onSubmit()
    {
        this.opinionService.report(this.opinionId, this.reason);
        this.activeModal.close();
    }
}
