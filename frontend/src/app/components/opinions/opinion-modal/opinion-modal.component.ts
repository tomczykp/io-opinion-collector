import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CreateUpdateOpinionDto } from 'src/app/model/Opinion';

@Component({
    selector: 'app-opinion-modal',
    templateUrl: './opinion-modal.component.html',
    styleUrls: ['./opinion-modal.component.css']
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

    /**
     * Function used for checking whether both pros and cons inputs are empty.
     * @returns true if both empty, false otherwise
     */
    protected allProsAndConsAdded(): boolean {
        return !this.currentProsValue && !this.currentConsValue;
    }
}
