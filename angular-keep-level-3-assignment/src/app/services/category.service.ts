import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable } from 'rxjs/Observable'
import { AuthenticationService } from './authentication.service';
import { Category } from '../Category';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import 'rxjs/add/operator/do';

@Injectable()
export class CategoryService {

  token: string;
  categories: Array<Category>;
  categorySubject: BehaviorSubject<Array<Category>>
  userId: string;

  constructor(private httpClient: HttpClient, private authService: AuthenticationService) {
    this.token = this.authService.getBearerToken();
    this.userId = this.authService.getUserNameFromSession();
    this.categories = [];
    this.categorySubject = new BehaviorSubject(this.categories);
    this.fetchCategoriesFromServer();
  }

  fetchCategoriesFromServer() {
    this.httpClient.get<Array<Category>>(`http://localhost:8083/api/v1/category/${this.userId}`, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.token}`)
    })
      .subscribe((res) => {
        this.categories = res;
        this.categorySubject.next(this.categories);
      }, (error) => {
        this.categorySubject.error(error);
      });
  }

  deletCategory(categoryId) {
    return this.httpClient.delete(`http://localhost:8083/api/v1/category/${categoryId}`, {
      headers: new HttpHeaders()
      .set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).do(res => {
        const deleteCategory = this.categories.findIndex(cat => cat.id === categoryId);
        this.categories.splice(deleteCategory, 1);
        this.categorySubject.next(this.categories);
    });
  }

  getCategories(): BehaviorSubject<Array<Category>> {
    return this.categorySubject;
  }

  createCategory(category): Observable<Category>{
    category.categoryCreatedBy = this.userId;
    return this.httpClient.post<Category>('http://localhost:8083/api/v1/category', category, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.token}`)
    }).do((data) => {
        this.categories.push(data);
        this.categorySubject.next(this.categories);
    })
  }

}
