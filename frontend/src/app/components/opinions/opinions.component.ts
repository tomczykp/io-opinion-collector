import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';
import { Opinion } from 'src/app/model/Opinion';
import { OpinionService } from 'src/app/services/opinion.service';


@Component({
    selector: 'app-opinions',
    templateUrl: './opinions.component.html',
    styleUrls: ['./opinions.component.css']
})
export class OpinionsComponent implements OnChanges
{
    @Input() productId: string;

    opinions$: Observable<Opinion[]>;

    constructor(
        private opinionService: OpinionService
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
        this.opinions$ = this.opinionService.getOpinions(this.productId);
    }
}
