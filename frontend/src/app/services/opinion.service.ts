import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Opinion, OpinionId } from '../model/Opinion';

@Injectable({
    providedIn: 'root'
})
export class OpinionService
{
    constructor(private readonly httpClient: HttpClient) { }

    getOpinions(productId: string): Observable<Opinion[]>
    {
        const url = `${environment.apiUrl}/products/${productId}/opinions`;
        return this.httpClient.get<Opinion[]>(url);
    }

    rate(id: OpinionId, positive: boolean): Observable<Opinion>
    {
        const url = `${environment.apiUrl}/products/${id.productId}/opinions/${id.opinionId}/${positive ? 'like' : 'dislike'}`;

        return this.httpClient.put<Opinion>(url, null);
    }

    report(id: OpinionId, reason: string)
    {
        const url = `${environment.apiUrl}/products/${id.productId}/opinions/${id.opinionId}/report`;

        console.log(url);

        this.httpClient.post<null>(url, reason).subscribe();
    }
}
