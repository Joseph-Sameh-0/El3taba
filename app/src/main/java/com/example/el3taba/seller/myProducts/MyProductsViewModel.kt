package com.example.el3taba.seller.myProducts

import com.example.el3taba.core.FirestoreRepository
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.el3taba.core.dataClasses.FinalProduct

class MyProductsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }



    val text: LiveData<String> = _text
    private val repository = FirestoreRepository()

    // Expose LiveData to UI
    fun getAllProducts(): LiveData<List<Product>> {
        return repository.getAllProducts()
    }

    fun getProductsByCategory(category: String): LiveData<List<Product>> {
        return repository.getProductByCategory(category)
    }

//    fun addProduct(product: Product): LiveData<Boolean> {
//        return repository.addProduct(product)
//    }

//    fun deleteProduct(productId: String): LiveData<Boolean> {
//        return repository.deleteProduct(productId)
//    }

    fun authenticateUser(email: String, password: String): LiveData<Boolean> {
        return repository.authenticateUser(email, password)
    }


    // Function to add a product with image
    fun addProduct(product: Product, imageUri: Uri?, categoryId: String, subcategoryId: String): LiveData<Boolean> {
        return repository.addProduct(product, imageUri, categoryId, subcategoryId)
    }

    fun addCategory(category: Category, imageUri: Uri?): LiveData<Boolean> {
        return repository.addCategory(category, imageUri)
    }

    fun addSubcategory(subcategory: Category, categoryId: String, imageUri: Uri?): LiveData<Boolean> {
        return repository.addSubcategory(subcategory, categoryId, imageUri)
    }
    fun getAllCategories(): LiveData<List<Category>> {
        return repository.getAllCategories()
    }
    fun getSubcategories(categoryId: String): LiveData<List<Category>> {
        return repository.getSubcategoriesByCategoryId(categoryId)
    }
    // Function to retrieve products by subcategory ID
    fun getProducts(subcategoryId: String, categoryId: String): LiveData<List<FinalProduct>> {
        return repository.getProductsBySubcategoryId(subcategoryId, categoryId)
    }
    fun getRandom10Products(): LiveData<List<FinalProduct>> {
        return repository.getRandom10Products()
    }
    fun getProductById(productId: String): LiveData<FinalProduct?>{
        return repository.getProductById(productId)
    }
}