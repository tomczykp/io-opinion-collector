import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CreateUpdateOpinionDto } from 'src/app/model/Opinion';

@Component({
    selector: 'app-opinion-modal',
    templateUrl: './opinion-modal.component.html',
    styles: [
        `
        .pros-and-cons-list {
            max-height: 12vh;
            overflow-y: scroll;
        }
        `,
        `
        .pros-and-cons-list span {
            cursor: pointer;
        }`
    ]
})
export class OpinionModalComponent
{
    @Input()
    opinion: CreateUpdateOpinionDto;

    protected currentProsValue = '';
    protected currentConsValue = '';

    constructor(protected activeModal: NgbActiveModal) { }

    onSubmit()
    {
        this.activeModal.close(this.opinion);
    }

    addPros()
    {
        this.opinion.pros.push(this.currentProsValue);
        this.currentProsValue = '';
    }


    addCons()
    {
        this.opinion.cons.push(this.currentConsValue);
        this.currentConsValue = '';
    }

}
