export interface Product {
  productId: string,
  name: string,
  description: string,
  deleted: boolean,
  confirmed: boolean,
  properties: Array<string> [];
}

export interface ProductDF {
  description: string;
}
