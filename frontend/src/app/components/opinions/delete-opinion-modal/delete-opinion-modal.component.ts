import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Opinion } from 'src/app/model/Opinion';
import { OpinionService } from 'src/app/services/opinion.service';

@Component({
    selector: 'app-delete-opinion-modal',
    templateUrl: './delete-opinion-modal.component.html'
})
export class DeleteOpinionModalComponent
{
    @Input()
    opinion: Opinion;

    constructor(
        private opinionService: OpinionService,
        protected activeModal: NgbActiveModal
    ) { }

    protected delete()
    {
        this.opinionService
            .deleteOpinion(this.opinion.productId, this.opinion.opinionId)
            .subscribe(deleted => this.activeModal.close(deleted));
    }
}
