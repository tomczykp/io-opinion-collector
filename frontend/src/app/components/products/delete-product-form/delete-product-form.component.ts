import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductsService} from "../../../services/products.service";

@Component({
  selector: 'app-delete-product-form',
  templateUrl: './delete-product-form.component.html'
})
export class DeleteProductFormComponent implements OnInit {
  match: RegExpMatchArray | null;
  uuid: string;
  regexGet: RegExp = new RegExp('[0-9a-f\-]+$');

  deleteProductForm = new FormGroup({                                //Idk if 20 is fine
    description: new FormControl('', [Validators.required, Validators.minLength(20)]),
  });

  get description() {
    return this.deleteProductForm.get('description');
  }


  constructor(
    private productService: ProductsService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // let href = this.router.url;
    // this.match = href.match(this.regexGet);
    // this.uuid = this.match![0].toString();
    this.route.paramMap.subscribe((params) =>
    {this.uuid = params.get('uuid')!.toString()});
  }

  public deleteProduct() {
    if (this.deleteProductForm.valid) {
      const productDF: object = {
        "description": this.deleteProductForm.getRawValue().description,
      }
      console.log(productDF);
      this.productService.makeDeleteForm(this.uuid, productDF)
        .subscribe((result) => {
            if (result.status === 204) {
              this.router.navigate(['/']);
            }
          }
        )
    }
  }

}
