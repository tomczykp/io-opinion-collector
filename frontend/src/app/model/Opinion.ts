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

export class CreateUpdateOpinionDto
{
    rate: number = 0;
    description: string = '';
    pros: string[] = [];
    cons: string[] = [];

    constructor(opinon?: Opinion)
    {
        if (opinon)
        {
            this.rate = opinon.rate;
            this.description = opinon.description;
            this.pros = opinon.pros.map(p => p.value);
            this.cons = opinon.cons.map(c => c.value);
        }
    }
}