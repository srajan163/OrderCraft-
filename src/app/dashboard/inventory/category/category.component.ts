import { Component, OnInit } from '@angular/core';
import { Category, CategoryService } from '../../../services/product-categorization/category.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  categories: Category[] = [];

  // No categoryId (it will be auto-generated)
  newCategory = { categoryName: '' };

  editing = false;
  editId: number | null = null;   // <-- FIXED

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.getAllCategories().subscribe(data => {
      this.categories = data;
    });
  }

  createCategory() {
    this.categoryService.createCategory(this.newCategory).subscribe(() => {
      this.loadCategories();
      this.resetForm();
      alert("Category added successfully!");
    });
  }

  editCategory(c: Category) {
    this.editing = true;
    this.editId = c.categoryId!;                     // store ID
    this.newCategory = { categoryName: c.categoryName }; // load name
  }

  updateCategory() {
    if (!this.editId) return;

    this.categoryService.updateCategory(this.editId, this.newCategory)
      .subscribe(() => {
        this.loadCategories();
        this.cancelEdit();
        alert("Category updated successfully!");
      });
  }

  confirmDelete(id: number) {
    if (confirm("Are you sure you want to delete this category?")) {
      this.deleteCategory(id);
    }
  }

  deleteCategory(id: number) {
    this.categoryService.deleteCategory(id).subscribe(() => {
      this.loadCategories();
      alert("Category deleted!");
    });
  }

  cancelEdit() {
    this.editing = false;
    this.editId = null;
    this.resetForm();
  }

  resetForm() {
    this.newCategory = { categoryName: '' };
  }
}
