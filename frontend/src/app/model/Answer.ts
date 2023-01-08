import { User } from './User'

export interface Answer {
	answerId: string
	content: string
	date: Date
	questionId: string
	author: User
}
