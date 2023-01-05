import {Category} from "./category";

export interface Product {
  productId: string,
  categoryId: string,
  name: string,
  description: string,
  deleted: boolean,
  confirmed: boolean,
  properties: Array<string>;
}

export interface ProductFull {
  productId: string,
  parentProductId: string,
  constantProductId: string,
  category: Category,
  name: string,
  description: string,
  deleted: boolean,
  confirmed: boolean,
  properties: Array<string>;
}

export interface ProductDF {
  description: string;
}
