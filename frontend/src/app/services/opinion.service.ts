import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Opinion } from '../model/Opinion';

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

    rate(productId: string, opinionId: string, positive: boolean): Observable<Opinion>
    {
        const url = `${environment.apiUrl}/products/${productId}/opinions/${opinionId}/${positive ? 'like' : 'dislike'}`;

        return this.httpClient.put<Opinion>(url, null);
    }

    report(productId: string, opinionId: string, reason: string)
    {
        const url = `${environment.apiUrl}/products/${productId}/opinions/${opinionId}/report`;

        console.log(url);

        this.httpClient.post<null>(url, reason).subscribe();
    }
}
