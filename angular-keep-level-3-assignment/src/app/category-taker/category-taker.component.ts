import { Component, OnInit } from '@angular/core';
import { Category } from '../Category';
import { CategoryService } from '../services/category.service';

@Component({
  selector: 'app-category-taker',
  templateUrl: './category-taker.component.html',
  styleUrls: ['./category-taker.component.css']
})
export class CategoryTakerComponent implements OnInit {

  category: Category;
  categoryObj: Category;
  categories: Array<Category> = [];
  errMessage: string;

  constructor(private categoryService: CategoryService) {
    this.category = new Category();
    this.categoryObj = new Category();
  }

  ngOnInit() {
    this.categoryService.getCategories().subscribe((data) => {
      this.categories = data;
    }, (err) => {
      this.errMessage = err.message;
    });
  }


  deleteCategory() {
    this.categoryService.deletCategory(this.categoryObj.id).subscribe(res => {
      this.categoryObj.categoryDescription = '';
    })
  }

  public addCategory() {
    this.category.id = (Math.floor(Math.random()*20) + 1).toString();
    if (this.category.categoryName === '' || this.category.categoryDescription === '') {
      this.errMessage = 'Name and Description both are required fields';
    } else {
      this.categoryService.createCategory(this.category).subscribe(
        () => {
          this.category = new Category();
        },
        (error) => {
          const index = this.categories.findIndex(category => category.categoryName === this.category.categoryName);
          this.categories.splice(index, 1);
          this.errMessage = error.message;
        })
    }
  }
}