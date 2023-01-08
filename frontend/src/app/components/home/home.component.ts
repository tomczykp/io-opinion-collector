import { Component, Inject, OnInit, ViewChild } from '@angular/core'
import { MatPaginator } from '@angular/material/paginator'
import { MatSort } from '@angular/material/sort'
import { MatTableDataSource } from '@angular/material/table'
import { ProductsService } from 'src/app/services/products.service'
import { AuthService } from '../../services/auth.service'
import { ProductFull } from '../../model/Product'
import { DeleteProductFormComponent } from '../products/delete-product-form/delete-product-form.component'
import { CategoriesService } from 'src/app/services/categories.service'
import { Category } from 'src/app/model/category'
import { FormArray, FormBuilder, FormGroup } from '@angular/forms'

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
	adminColumns: string[] = ['name', 'description', 'deleted', 'categoryId', 'action']
	userColumns: string[] = ['name', 'description', 'categoryId', 'makeUpdateForm', 'makeDeleteForm']
	columns: string[] = ['name', 'description', 'categoryId']
	data: MatTableDataSource<ProductFull>
	authenticated = false
	text: string
	role: string
	search: string = ''
	allCategories: Category[]
	displayedCategories: Category[]
	allProducts: ProductFull[]
	displayedProducts: ProductFull[]
	selectedCategory: string = 'All categories'
	parentCategory: string = 'All categories'
	isCategorySelected: boolean = false
	propertiesForm = new FormGroup({
		properties: this.formBuilder.array([])
	})
	propertiesNames: any[]

	@Inject(DeleteProductFormComponent) productFormComponent: DeleteProductFormComponent
	@ViewChild(MatPaginator) paginator: MatPaginator
	@ViewChild(MatSort) sort: MatSort

	constructor(
		public productsService: ProductsService,
		public categoriesService: CategoriesService,
		public authService: AuthService,
		public formBuilder: FormBuilder
	) {}

	get properties() {
		return this.propertiesForm.get('properties') as FormArray
	}

	handleCategoryChange() {
		this.isCategorySelected = true
		let found = this.allCategories.find((c) => c.name === this.selectedCategory)
		if (found?.parentCategory == null) {
			this.parentCategory = 'All categories'
		} else {
			this.parentCategory = found.parentCategory.name
		}
		this.displayedCategories = this.allCategories.filter(
			(c) => found?.categoryID === c.parentCategory?.categoryID
		)

		this.updateDisplayedProducts(new Map())

		let properties: { [id: string]: any } = []
		while (found !== null) {
			found?.fields.forEach((field) => {
				properties[field.name] = ''
			})
			found = found?.parentCategory as any
		}

		this.propertiesForm.controls.properties.clear()
		this.propertiesNames = []
		Object.keys(properties).forEach((key) => {
			let value = properties[key]
			this.propertiesNames.push(key)
			this.propertiesForm.controls.properties.push(this.formBuilder.control(value))
		})
	}

	filterProduct(product: ProductFull, filters: Map<string, string>): boolean {
		const productProperties = product.properties as { [id: string]: any }
		for (let [key, value] of filters) {
			const productKeys = Object.keys(productProperties)
			if (value === '') continue
			if (!productKeys.includes(key)) return false
			if (productKeys.length == 0) return false
			for (let productPropsKey of productKeys) {
				if (productPropsKey === key) {
					if (!productProperties[productPropsKey].toLowerCase().includes(value.toLowerCase()))
						return false
				}
			}
		}
		return true
	}

	handlePropertiesChange() {
		const updatedValues = this.propertiesForm.getRawValue().properties
		const filters: Map<string, string> = new Map()
		for (let i = 0; i < updatedValues.length; i++) {
			filters.set(this.propertiesNames[i], updatedValues[i] as string)
		}
		this.updateDisplayedProducts(filters)
	}

	handleChangeToLastCategory() {
		let found = this.allCategories.find((c) => c.name === this.selectedCategory)
		if (found?.parentCategory == null) {
			this.selectedCategory = 'All categories'
			this.isCategorySelected = false
			this.displayedCategories = this.allCategories.filter((c) => c.parentCategory === null)
			this.propertiesForm.controls.properties.clear()
		} else {
			this.selectedCategory = found.parentCategory.name
			this.parentCategory =
				found.parentCategory.parentCategory == undefined
					? 'All categories'
					: found.parentCategory.parentCategory.name

			this.displayedCategories = this.allCategories.filter(
				(c) => found?.parentCategory?.categoryID === c.parentCategory?.categoryID
			)
			let properties: { [id: string]: any } = []
			found = found.parentCategory
			while (found !== null) {
				found?.fields.forEach((field) => {
					properties[field.name] = ''
				})
				found = found?.parentCategory as any
			}

			this.propertiesForm.controls.properties.clear()
			this.propertiesNames = []
			Object.keys(properties).forEach((key) => {
				let value = properties[key]
				this.propertiesNames.push(key)
				this.propertiesForm.controls.properties.push(this.formBuilder.control(value))
			})
		}
		this.updateDisplayedProducts(new Map())
	}

	ngOnInit(): void {
		this.authService.authenticated.subscribe((change) => {
			this.authenticated = change

			if (this.authenticated) {
				this.text = localStorage.getItem('email')!
				this.role = localStorage.getItem('role')!
			} else {
				this.text = 'not logged in.'
			}
		})
	}

	ngAfterViewInit(): void {
		this.getProducts()
		this.categoriesService.getCategories().subscribe((value) => {
			this.allCategories = value
			this.displayedCategories = value.filter((c) => c.parentCategory == null)
		})
	}

	getProducts(): void {
		this.productsService.getProductsFull().subscribe((value) => {
			this.role == 'ADMIN'
				? (this.allProducts = value)
				: (this.allProducts = value.filter((p) => !p.deleted && p.confirmed))
			this.displayedProducts = this.allProducts
			this.data = new MatTableDataSource(this.displayedProducts)
			this.data.paginator = this.paginator
			this.data.sort = this.sort
		})
	}

	updateDisplayedProducts(filters: Map<string, string>): void {
		this.displayedProducts = this.allProducts.filter(
			(p) =>
				p.name.toLowerCase().includes(this.search.toLowerCase()) &&
				this.isInSelectedCategory(p.category)
		)
		if (filters.size > 0)
			this.displayedProducts = this.displayedProducts.filter((p) => this.filterProduct(p, filters))
		this.data = new MatTableDataSource(this.displayedProducts)
		this.data.paginator = this.paginator
		this.data.sort = this.sort
	}

	getAllCategoriesString(category: string) {
		if (this.allCategories == undefined) return ''
		let current = this.allCategories.find((c) => c.name === category)

		const allCategories = [current?.name]
		while (current?.parentCategory != null) {
			current = current.parentCategory
			allCategories.push(current.name)
		}
		const reversed = allCategories.reverse().join(' > ')
		if (reversed === '') return reversed
		return ' > ' + reversed
	}

	isInSelectedCategory(productCategory: Category): boolean {
		if (this.selectedCategory === 'All categories') return true
		const selected = this.allCategories.find((c) => c.name === this.selectedCategory)
		let current = productCategory
		while (current != null) {
			if (current.categoryID === selected?.categoryID) return true
			current = current.parentCategory as any
		}
		return false
	}

	deleteProduct(id: string): void {
		this.productsService.deleteProduct(id)
		location.reload()
	}

	makeDeleteForm(id: string) {
		this.productFormComponent.deleteProduct()
	}

	handleChange() {
		this.updateDisplayedProducts(new Map())
	}
}
