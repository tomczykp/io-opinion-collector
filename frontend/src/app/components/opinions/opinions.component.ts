import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject } from 'rxjs';
import { CreateUpdateOpinionDto, Opinion } from 'src/app/model/Opinion';
import { AuthService } from 'src/app/services/auth.service';
import { OpinionService } from 'src/app/services/opinion.service';
import { OpinionModalComponent } from './opinion-modal/opinion-modal.component';
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

    onReactionClick(opinion: Opinion, positive: boolean)
    {
        if (this.isUser())
        {
            if (positive && !opinion.liked)
            {
                this.opinionService
                    .rate(opinion.productId, opinion.opinionId, positive)
                    .subscribe(u => this.replaceUpdated(u));
            }
            else if (!positive && !opinion.disliked)
            {
                this.opinionService
                    .rate(opinion.productId, opinion.opinionId, positive)
                    .subscribe(u => this.replaceUpdated(u));
            }
            else if (positive && opinion.liked)
            {
                this.opinionService
                    .removeReaction(opinion.productId, opinion.opinionId, positive)
                    .subscribe(u => this.replaceUpdated(u));
            }
            else if (!positive && opinion.disliked)
            {
                this.opinionService
                    .removeReaction(opinion.productId, opinion.opinionId, positive)
                    .subscribe(u => this.replaceUpdated(u));
            }
        }
    }

    isAuthor(opinion: Opinion): boolean
    {
        // TODO retrieve username from authService
        return true;
    }

    isUser(): boolean
    {
        return this.authService.getRole() === 'USER';
    }

    openReportModal(opinion: Opinion)
    {
        const modalRef = this.modalService.open(OpinionReportModalComponent);

        (modalRef.componentInstance as OpinionReportModalComponent).opinion = opinion;
    }

    openCreateOpinionModal()
    {
        const modalRef = this.modalService.open(OpinionModalComponent);

        (modalRef.componentInstance as OpinionModalComponent).opinion = new CreateUpdateOpinionDto();

        modalRef.result
            .then((dto: CreateUpdateOpinionDto) =>
            {
                console.log(dto);
                // TODO
            })
            .catch(() => { });
    }

    openEditOpinionModal(opinion: Opinion)
    {
        const modalRef = this.modalService.open(OpinionModalComponent);

        (modalRef.componentInstance as OpinionModalComponent).opinion = new CreateUpdateOpinionDto(opinion);

        modalRef.result
            .then((dto: CreateUpdateOpinionDto) =>
            {
                this.opinionService
                    .updateOpinion(opinion.productId, opinion.opinionId, dto)
                    .subscribe(u => this.replaceUpdated(u));
            })
            .catch(() => { });
    }

    private replaceUpdated(updated: Opinion)
    {
        this.opinions$.next(
            this.opinions$.value.map(old =>
            {
                if (old.opinionId == updated.opinionId)
                {
                    Object.assign(old, updated);
                }
                return old;
            })
        );
    }
}
