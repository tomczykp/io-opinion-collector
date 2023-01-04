export interface AdvantageOrDisadvantage
{
    id: string;
    value: string;
}

export class Opinion
{
    productId: string;
    opinionId: string;
    rate: number = 0;
    description: string = '';
    likesCounter: number = 0;
    pros: AdvantageOrDisadvantage[] = [];
    cons: AdvantageOrDisadvantage[] = [];
    createdAt: Date;

    liked: boolean;
    disliked: boolean;

    authorName: string;
}
