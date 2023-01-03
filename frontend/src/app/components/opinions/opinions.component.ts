import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Opinion, OpinionId } from 'src/app/model/Opinion';
import { AuthService } from 'src/app/services/auth.service';
import { OpinionService } from 'src/app/services/opinion.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OpinionReportModalComponent } from './opinion-report-modal/opinion-report-modal.component';


@Component({
    selector: 'app-opinions',
    templateUrl: './opinions.component.html',
    styleUrls: ['./opinions.component.css']
})
export class OpinionsComponent implements OnChanges
{
    @Input() productId: string;

    opinions$ = new BehaviorSubject<Opinion[]>([]);

    constructor(
        private opinionService: OpinionService,
        protected authService: AuthService,
        private modalService: NgbModal
    ) { }

    ngOnChanges(changes: SimpleChanges): void
    {
        if (!changes['productId'].isFirstChange())
        {
            this.getOpinions();
        }
    }

    getOpinions()
    {
        this.opinionService.getOpinions(this.productId).subscribe(data =>
        {
            this.opinions$.next(data);
        })
    }

    rateOpinion(id: OpinionId, positive: boolean)
    {
        if (this.authService.authenticated.value)
        {
            this.opinionService.rate(id, positive).subscribe(updated =>
            {
                this.opinions$.next(
                    this.opinions$.value.map(o =>
                    {
                        if (o.id.opinionId == id.opinionId)
                        {
                            o.likesCounter = updated.likesCounter;
                        }
                        return o;
                    })
                )
            });
        }
    }

    isAuthor(opinion: Opinion): boolean
    {
        // TODO
        return false;
    }

    isUser(): boolean
    {
        return this.authService.getRole() === 'USER';
    }

    openReportModal(id: OpinionId)
    {
        const modalRef = this.modalService.open(OpinionReportModalComponent);

        (modalRef.componentInstance as OpinionReportModalComponent).opinionId = id;
    }
}
