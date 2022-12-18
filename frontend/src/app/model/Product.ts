export interface Product {
  productId: string,
  categoryId: string,
  name: string,
  description: string,
  deleted: boolean,
  confirmed: boolean,
  properties: Array<string> [];
}
