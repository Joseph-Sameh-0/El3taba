package com.example.el3taba.core

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.el3taba.seller.myProducts.Category
import com.example.el3taba.seller.myProducts.Product
import com.example.el3taba.seller.myProducts.Subcategory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    // Function to authenticate user (e.g., login/signup)
    fun authenticateUser(email: String, password: String): LiveData<Boolean> {
        val authResult = MutableLiveData<Boolean>()

        // Simulate authentication logic
        authResult.value = true

        return authResult
    }
    fun addProduct(product: Product, imageUri: Uri?, categoryId: String, subcategoryId: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        if (imageUri != null) {
            uploadImageAndSaveProduct(product, imageUri, categoryId, subcategoryId, result)
        } else {
            saveProductToFirestore(product.copy(), categoryId, subcategoryId, result)
        }

        return result
    }

    private fun uploadImageAndSaveProduct(product: Product, imageUri: Uri, categoryId: String, subcategoryId: String, result: MutableLiveData<Boolean>) {
        val fileReference = storageRef.child("products/${System.currentTimeMillis()}.jpg")

        fileReference.putFile(imageUri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val productWithImage = product.copy(imageUrl = uri.toString())
                    saveProductToFirestore(productWithImage, categoryId, subcategoryId, result)
                }
            }
            .addOnFailureListener {
                result.value = false
            }
    }

    private fun saveProductToFirestore(product: Product, categoryId: String, subcategoryId: String, result: MutableLiveData<Boolean>) {
        // Adding the product to the subcategory's products collection
        db.collection("categories")
            .document(categoryId)
            .collection("subcategories")
            .document(subcategoryId)
            .collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                val updatedProduct = product.copy(id = documentReference.id)
                // Update the product document with the new ID
                db.collection("categories")
                    .document(categoryId)
                    .collection("subcategories")
                    .document(subcategoryId)
                    .collection("products")
                    .document(updatedProduct.id)
                    .set(updatedProduct)
                    .addOnSuccessListener {
                        result.value = true
                    }
                    .addOnFailureListener {
                        result.value = false
                    }
            }
            .addOnFailureListener {
                result.value = false
            }
    }

    // Function to get all products
    fun getAllProducts(): LiveData<List<Product>> {
        val productList = MutableLiveData<List<Product>>()

        db.collection("products")
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    // Handle the error
                    return@addSnapshotListener
                }

                val products = snapshot?.toObjects(Product::class.java)
                productList.value = products ?: listOf()
            }

        return productList
    }

    // Function to get products by category
    fun getProductByCategory(category: String): LiveData<List<Product>> {
        val productList = MutableLiveData<List<Product>>()

        db.collection("products")
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    // Handle the error
                    return@addSnapshotListener
                }

                val products = snapshot?.toObjects(Product::class.java)
                productList.value = products ?: listOf()
            }

        return productList
    }

    fun addCategory(category: Category, imageUri: Uri?): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        if (imageUri != null) {
            uploadImageAndSaveCategory(category, imageUri, result)
        } else {
            saveCategoryToFirestore(category.copy(), result)
        }

        return result
    }

    private fun uploadImageAndSaveCategory(category: Category, imageUri: Uri, result: MutableLiveData<Boolean>) {
        val fileReference = storageRef.child("categories/${System.currentTimeMillis()}.jpg")

        fileReference.putFile(imageUri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val categoryWithImage = category.copy(imageUrl = uri.toString())
                    saveCategoryToFirestore(categoryWithImage, result)
                }
            }
            .addOnFailureListener {
                result.value = false
            }
    }

    private fun saveCategoryToFirestore(category: Category, result: MutableLiveData<Boolean>) {
        db.collection("categories")
            .add(category)
            .addOnSuccessListener { documentReference ->
                val updatedCategory = category.copy(id = documentReference.id)
                db.collection("categories")
                    .document(updatedCategory.id)
                    .set(updatedCategory)
                    .addOnSuccessListener {
                        result.value = true
                    }
                    .addOnFailureListener {
                        result.value = false
                    }
            }
            .addOnFailureListener {
                result.value = false
            }
    }

    fun addSubcategory(subcategory: Subcategory, categoryId: String, imageUri: Uri?): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        if (imageUri != null) {
            uploadImageAndSaveSubcategory(subcategory, categoryId, imageUri, result)
        } else {
            saveSubcategoryToFirestore(subcategory.copy(), categoryId, result)
        }

        return result
    }

    private fun uploadImageAndSaveSubcategory(subcategory: Subcategory, categoryId: String, imageUri: Uri, result: MutableLiveData<Boolean>) {
        val fileReference = storageRef.child("subcategories/${System.currentTimeMillis()}.jpg")

        fileReference.putFile(imageUri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val subcategoryWithImage = subcategory.copy(imageUrl = uri.toString())
                    saveSubcategoryToFirestore(subcategoryWithImage, categoryId, result)
                }
            }
            .addOnFailureListener {
                result.value = false
            }
    }

    private fun saveSubcategoryToFirestore(subcategory: Subcategory, categoryId: String, result: MutableLiveData<Boolean>) {
        db.collection("categories")
            .document(categoryId)
            .collection("subcategories")
            .add(subcategory)
            .addOnSuccessListener { documentReference ->
                val updatedSubcategory = subcategory.copy(id = documentReference.id)
                db.collection("categories")
                    .document(categoryId)
                    .collection("subcategories")
                    .document(updatedSubcategory.id)
                    .set(updatedSubcategory)
                    .addOnSuccessListener {
                        result.value = true
                    }
                    .addOnFailureListener {
                        result.value = false
                    }
            }
            .addOnFailureListener {
                result.value = false
            }
    }

    fun getAllCategories(): LiveData<List<Category>> {
        val categoryList = MutableLiveData<List<Category>>()

        db.collection("categories")
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    // Handle the error
                    categoryList.value = emptyList() // Return an empty list on error
                    return@addSnapshotListener
                }

                val categories = snapshot?.documents?.map { document ->
                    // Create a Category object from the document
                    Category(
                        id = document.id, // Firestore auto-generated ID
                        name = document.getString("name") ?: "",
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                } ?: listOf() // Return an empty list if no documents

                categoryList.value = categories
            }

        return categoryList
    }

    fun getSubcategoriesByCategoryId(categoryId: String): LiveData<List<Subcategory>> {
        val subcategoryList = MutableLiveData<List<Subcategory>>()

        db.collection("categories")
            .document(categoryId)
            .collection("subcategories")
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    // Handle the error
                    subcategoryList.value = emptyList() // Return an empty list on error
                    return@addSnapshotListener
                }

                val subcategories = snapshot?.documents?.map { document ->
                    // Create a Subcategory object from the document
                    Subcategory(
                        id = document.id, // Firestore auto-generated ID
                        name = document.getString("name") ?: "",
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                } ?: listOf() // Return an empty list if no documents

                subcategoryList.value = subcategories
            }

        return subcategoryList
    }
    fun getProductsBySubcategoryId(subcategoryId: String, categoryId: String): LiveData<List<Product>> {
        val productList = MutableLiveData<List<Product>>()

        db.collection("categories")
            .document(categoryId)
            .collection("subcategories")
            .document(subcategoryId)
            .collection("products")
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    // Handle the error
                    productList.value = emptyList() // Return an empty list on error
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.map { document ->
                    // Create a Product object from the document
                    Product(
                        id = document.id, // Firestore auto-generated ID
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        stock = document.getLong("stock")?.toInt() ?: 0,
                        price = document.getDouble("price") ?: 0.0,
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                } ?: listOf() // Return an empty list if no documents

                productList.value = products
            }

        return productList
    }

    //    // Function to add product
//    fun addProduct(product: Product): LiveData<Boolean> {
//        val result = MutableLiveData<Boolean>()
//
//        // Add product to Firestore without id to let Firestore auto-generate the ID
//        db.collection("products")
//            .add(product)
//            .addOnSuccessListener { documentReference ->
//                // Get the auto-generated ID
//                val generatedId = documentReference.id
//
//                // Update the product's id field with the generated ID
//                val updatedProduct = product.copy(id = generatedId)
//
//                // Now update the document with the new ID field
//                db.collection("products").document(generatedId)
//                    .set(updatedProduct)
//                    .addOnSuccessListener {
//                        result.value = true
//                    }
//                    .addOnFailureListener {
//                        result.value = false
//                    }
//            }
//            .addOnFailureListener {
//                result.value = false
//            }
//
//        return result
//    }
//
//    // Function to delete a product
//    fun deleteProduct(productId: String): LiveData<Boolean> {
//        val result = MutableLiveData<Boolean>()
//
//        db.collection("products")
//            .document(productId)
//            .delete()
//            .addOnSuccessListener {
//                result.value = true
//            }
//            .addOnFailureListener {
//                result.value = false
//            }
//
//        return result
//    }
   private val storageRef = FirebaseStorage.getInstance().reference

//    // Function to upload the image and save the product
//    fun addProductWithImage(product: Product, imageUri: Uri?): LiveData<Boolean> {
//        val result = MutableLiveData<Boolean>()
//
//        if (imageUri != null) {
//            // Upload image to Firebase Storage
//            val fileReference = storageRef.child("products/" + System.currentTimeMillis() + ".jpg")
//
//            fileReference.putFile(imageUri)
//                .addOnSuccessListener {
//                    // Get the download URL of the uploaded image
//                    fileReference.downloadUrl.addOnSuccessListener { uri ->
//                        val imageUrl = uri.toString()
//
//                        // After uploading the image, save the product with the image URL
//                        val productWithImage = product.copy(imageUrl = imageUrl)
//                        saveProductToFirestore(productWithImage, result)
//                    }
//                }
//                .addOnFailureListener {
//                    // Handle failure in uploading image
//                    result.value = false
//                }
//        } else {
//            // If no image is provided, just save the product
//            saveProductToFirestore(product, result)
//        }
//
//        return result
//    }
//
//    // Function to save product to Firestore
//    private fun saveProductToFirestore(product: Product, result: MutableLiveData<Boolean>) {
//        // Add product to Firestore without ID, Firestore auto-generates ID
//        db.collection("products")
//            .add(product)
//            .addOnSuccessListener { documentReference ->
//                // Get the auto-generated ID and update the product's ID field
//                val generatedId = documentReference.id
//                val updatedProduct = product.copy(id = generatedId)
//
//                // Update the product document with the new ID
//                db.collection("products").document(generatedId)
//                    .set(updatedProduct)
//                    .addOnSuccessListener {
//                        result.value = true
//                    }
//                    .addOnFailureListener {
//                        result.value = false
//                    }
//            }
//            .addOnFailureListener {
//                result.value = false
//            }
//    }
}