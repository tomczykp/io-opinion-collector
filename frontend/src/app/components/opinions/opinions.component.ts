import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject } from 'rxjs';
import { CreateUpdateOpinionDto, Opinion } from 'src/app/model/Opinion';
import { AuthService } from 'src/app/services/auth.service';
import { OpinionService } from 'src/app/services/opinion.service';
import { DeleteOpinionModalComponent } from './delete-opinion-modal/delete-opinion-modal.component';
import { OpinionModalComponent } from './opinion-modal/opinion-modal.component';
import { OpinionReportModalComponent } from './opinion-report-modal/opinion-report-modal.component';

export enum SortingOrder
{
    NONE = -1,
    BEST_TO_WORST = 0,
    WORST_TO_BEST = 1,
    NEWEST_TO_OLDEST = 2,
    OLDEST_TO_NEWEST = 3
}

@Component({
    selector: 'app-opinions',
    templateUrl: './opinions.component.html',
    styleUrls: ['./opinions.component.css']
})
export class OpinionsComponent implements OnChanges
{
    hasCreatedOpinion = false;

    ORDER = SortingOrder;

    @Input() productId: string;

    selectedOrder: SortingOrder = SortingOrder.NONE;
    currentlyUsedOrder: SortingOrder = SortingOrder.NONE;

    selectedMinRating = 0;
    selectedMaxRating = 10;

    usedMinRating = 0;
    usedMaxRating = 10;

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
            this.opinionService
                .getOpinions(this.productId)
                .subscribe(data =>
                {
                    this.opinions$.next(data);
                    this.hasCreatedOpinion = data.some(o => o.authorName === this.authService.getUsername());
                });
        }
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
        return this.authService.getUsername() === opinion.authorName;
    }

    isUser(): boolean
    {
        return this.authService.getRole() === 'USER';
    }

    openReportModal(opinion: Opinion)
    {
        const modalRef = this.modalService.open(OpinionReportModalComponent, { centered: true });

        (modalRef.componentInstance as OpinionReportModalComponent).opinion = opinion;
    }

    openCreateOpinionModal()
    {
        const modalRef = this.modalService.open(OpinionModalComponent);

        (modalRef.componentInstance as OpinionModalComponent).opinion = new CreateUpdateOpinionDto();

        modalRef.result
            .then((dto: CreateUpdateOpinionDto) =>
            {
                this.opinionService
                    .createOpinion(this.productId, dto)
                    .subscribe(u =>
                    {
                        this.opinions$.value.push(u);
                        this.hasCreatedOpinion = true;
                        this.resetFiltersAndSortingOrder();
                        this.applyFiltersAndSortingOrder();
                    });
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
                    .subscribe(u =>
                    {
                        this.replaceUpdated(u);
                        this.resetFiltersAndSortingOrder();
                        this.applyFiltersAndSortingOrder();
                    })
            })
            .catch(() => { });
    }

    openDeleteConfirmationModal(opinion: Opinion)
    {
        const modalRef = this.modalService.open(DeleteOpinionModalComponent, {
            centered: true,
            size: 'sm'
        });

        (modalRef.componentInstance as DeleteOpinionModalComponent).opinion = opinion;

        modalRef.result
            .then((wasDeleted: boolean) =>
            {
                if (wasDeleted)
                {
                    this.opinions$.next(
                        this.opinions$.value.filter(o => o.opinionId !== opinion.opinionId)
                    );
                    this.hasCreatedOpinion = false;
                    this.resetFiltersAndSortingOrder();
                    this.applyFiltersAndSortingOrder();
                }
            })
            .catch(() => { });
    }

    applyFiltersAndSortingOrder()
    {
        this.currentlyUsedOrder = this.selectedOrder;
        this.opinions$.next(this.opinions$.value.sort(this.comparatorFn))

        this.usedMinRating = this.selectedMinRating;
        this.usedMaxRating = this.selectedMaxRating;
    }

    onMinRatingChange()
    {
        if (!this.selectedMinRating && this.selectedMinRating !== 0)
            this.selectedMinRating = 0;
        else if (this.selectedMinRating < 0)
            this.selectedMinRating = 0;
        else if (this.selectedMinRating > 10)
            this.selectedMinRating = 10;
        else if (this.selectedMinRating > this.selectedMaxRating)
            this.selectedMinRating = this.selectedMaxRating;
    }

    onMaxRatingChange()
    {
        if (!this.selectedMaxRating && this.selectedMaxRating !== 0)
            this.selectedMaxRating = 10;
        else if (this.selectedMaxRating > 10)
            this.selectedMaxRating = 10;
        else if (this.selectedMaxRating < 0)
            this.selectedMaxRating = 0
        else if (this.selectedMaxRating < this.selectedMinRating)
            this.selectedMaxRating = this.selectedMinRating;
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

    private resetFiltersAndSortingOrder()
    {
        this.selectedMinRating = 0;
        this.selectedMaxRating = 10;
        this.selectedOrder = this.ORDER.NONE;
    }

    protected readonly comparatorFn = (o1: Opinion, o2: Opinion) =>
    {
        switch (this.currentlyUsedOrder)
        {
            case SortingOrder.BEST_TO_WORST:
                return o2.rate - o1.rate;
            case SortingOrder.WORST_TO_BEST:
                return o1.rate - o2.rate;
            case SortingOrder.NEWEST_TO_OLDEST:
                return (new Date(o2.createdAt)).getTime() - (new Date(o1.createdAt)).getTime();
            case SortingOrder.OLDEST_TO_NEWEST:
                return (new Date(o1.createdAt)).getTime() - (new Date(o2.createdAt)).getTime();
            default:
                return -1;
        }
    }

    protected filterFn = (o: Opinion) =>
    {
        return o.rate >= this.usedMinRating && o.rate <= this.usedMaxRating;
    }
}
