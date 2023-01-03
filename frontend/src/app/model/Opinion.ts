import { User } from "./User";

export interface OpinionId {
    productId: string;
    opinionId: string;
}

export interface AdvantageOrDisadvantage {
    id: string;
    value: string;
}

export class Opinion {
    id: OpinionId;
    rate: number = 0;
    description: string = '';
    likesCounter: number = 0;
    pros: AdvantageOrDisadvantage[] = [];
    cons: AdvantageOrDisadvantage[] = [];
    createdAt: Date;

    author: User;
}
