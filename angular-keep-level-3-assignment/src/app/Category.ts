export class Category {
    id: string;
    categoryName: string;
    categoryDescription: string;
    categoryCreatedBy: string;

    constructor() {
        this.categoryName = '';
        this.categoryDescription = ''
        this.categoryCreatedBy = '';
    }
}