export class Field {
  fieldID: string;
  name: string;
  type: string;
}

export class Category {
  categoryID: string;
  parentCategory: Category | null;
  name: string;
  fields: Field[];
}
