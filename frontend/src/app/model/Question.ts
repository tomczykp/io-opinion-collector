import { User } from './User'

export interface Question {
	questionId: string
	content: string
	date: Date
	productId: string
	author: User
}
