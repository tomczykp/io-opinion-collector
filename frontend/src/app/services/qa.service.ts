import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { environment } from '../../environments/environment'
import { Observable } from 'rxjs'
import { Question } from '../model/Question'
import { Answer } from '../model/Answer'

@Injectable({
	providedIn: 'root'
})
export class QAService {
	constructor(private http: HttpClient) {}

	public getQuestionsOfProduct(id: string): Observable<Question[]> {
		return this.http.get<Question[]>(environment.apiUrl + '/products/' + id + '/questions')
	}

	public getAnswersOfQuestion(id: string): Observable<Answer[]> {
		return this.http.get<Answer[]>(environment.apiUrl + '/questions/' + id + '/answers')
	}

	public addQuestion(question: Question): void {
		this.http.post<any>(environment.apiUrl + '/questions', question).subscribe((data) => {
			console.log(data)
		})
	}

	public addAnswer(answer: Answer): void {
		this.http.post<any>(environment.apiUrl + '/answers', answer).subscribe((data) => {
			console.log(data)
		})
	}

	public deleteQuestion(id: string): void {
		this.http.delete(environment.apiUrl + '/questions/' + id).subscribe((data) => {
			console.log(data)
		})
	}

	public deleteAnswer(id: string): void {
		this.http.delete(environment.apiUrl + '/answers/' + id).subscribe((data) => {
			console.log(data)
		})
	}

	public getQuestion(id: string): Observable<Question> {
		return this.http.get<Question>(`${environment.apiUrl}/questions/${id}`)
	}

	public reportQuestion(id: string, reason: string): void {
		this.http
			.post<any>(environment.apiUrl + '/questions/' + id + '/report', reason)
			.subscribe((data) => {
				console.log(data)
			})
	}

	public reportAnswer(id: string, reason: string): void {
		this.http
			.post<any>(environment.apiUrl + '/answers/' + id + '/report', reason)
			.subscribe((data) => {
				console.log(data)
			})
	}
}
