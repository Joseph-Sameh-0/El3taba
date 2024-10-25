package com.example.el3taba.core

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.seller.myProducts.Category
import com.example.el3taba.seller.myProducts.Product
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    // Function to authenticate user (e.g., login/signup)
    fun authenticateUser(email: String, password: String): LiveData<Boolean> {
        val authResult = MutableLiveData<Boolean>()

        // Simulate authentication logic
        authResult.value = true

        return authResult
    }

    fun addProduct(
        product: FinalProduct,
        imageUris: List<Uri>?,  // Accept a list of image URIs
        categoryId: String,
        subcategoryId: String
    ): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        if (imageUris != null && imageUris.isNotEmpty()) {
            uploadImagesAndSaveProduct(product, imageUris, categoryId, subcategoryId, result)
        } else {
            saveProductToFirestore(product.copy(), categoryId, subcategoryId, result)
        }

        return result
    }


    private fun uploadImagesAndSaveProduct(
        product: FinalProduct,
        imageUris: List<Uri>,  // List of image URIs to upload
        categoryId: String,
        subcategoryId: String,
        result: MutableLiveData<Boolean>
    ) {
        val uploadedImageUrls = mutableListOf<String>() // To store uploaded image URLs
        val totalImages = imageUris.size // Total number of images to upload

        if (totalImages == 0) {
            // If no images, save the product with an empty list of image URLs
            saveProductToFirestore(
                product.copy(imageUrls = emptyList()),
                categoryId,
                subcategoryId,
                result
            )
            return
        }

        // Iterate through each image URI
        for (imageUri in imageUris) {
            val imageRef =
                FirebaseStorage.getInstance().reference.child("products/${UUID.randomUUID()}.jpg")

            // Upload the image
            imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
                // Get the download URL after a successful upload
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    uploadedImageUrls.add(uri.toString()) // Add the URL to the list

                    // Check if all images have been uploaded
                    if (uploadedImageUrls.size == totalImages) {
                        // Save the product with the list of uploaded image URLs
                        saveProductToFirestore(
                            product.copy(imageUrls = uploadedImageUrls),
                            categoryId,
                            subcategoryId,
                            result
                        )
                    }
                }.addOnFailureListener {
                    // Handle failure to get download URL
                    result.value = false
                }
            }.addOnFailureListener {
                // Handle failure to upload image
                result.value = false
            }
        }
    }

    private fun saveProductToFirestore(
        product: FinalProduct,
        categoryId: String,
        subcategoryId: String,
        result: MutableLiveData<Boolean>
    ) {
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

    private fun uploadImageAndSaveCategory(
        category: Category,
        imageUri: Uri,
        result: MutableLiveData<Boolean>
    ) {
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

    fun addSubcategory(
        subcategory: Category,
        categoryId: String,
        imageUri: Uri?
    ): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        if (imageUri != null) {
            uploadImageAndSaveSubcategory(subcategory, categoryId, imageUri, result)
        } else {
            saveSubcategoryToFirestore(subcategory.copy(), categoryId, result)
        }

        return result
    }

    private fun uploadImageAndSaveSubcategory(
        subcategory: Category,
        categoryId: String,
        imageUri: Uri,
        result: MutableLiveData<Boolean>
    ) {
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

    private fun saveSubcategoryToFirestore(
        subcategory: Category,
        categoryId: String,
        result: MutableLiveData<Boolean>
    ) {
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

    fun getSubcategoriesByCategoryId(categoryId: String): LiveData<List<Category>> {
        val subcategoryList = MutableLiveData<List<Category>>()

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
                    Category(
                        id = document.id, // Firestore auto-generated ID
                        name = document.getString("name") ?: "",
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                } ?: listOf() // Return an empty list if no documents

                subcategoryList.value = subcategories
            }

        return subcategoryList
    }

    fun getProductsBySubcategoryId(
        subcategoryId: String,
        categoryId: String
    ): LiveData<List<FinalProduct>> {
        val productList = MutableLiveData<List<FinalProduct>>()

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
                    FinalProduct(
                        id = document.id, // Firestore auto-generated ID
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        stock = document.getLong("stock")?.toInt() ?: 0,
                        price = document.getDouble("price") ?: 0.0,
                        imageUrls = document.get("imageUrls") as List<String>
                    )
                } ?: listOf() // Return an empty list if no documents

                productList.value = products
            }

        return productList
    }

    fun getRandom10Products(): LiveData<List<FinalProduct>> {
        val productList = MutableLiveData<List<FinalProduct>>()
        val allProducts = mutableListOf<FinalProduct>()
        val tasks = mutableListOf<Task<QuerySnapshot>>()

        // Fetch all categories
        db.collection("categories")
            .get()
            .addOnSuccessListener { categoriesSnapshot ->
                // Loop through each category
                for (categoryDoc in categoriesSnapshot.documents) {
                    val categoryId = categoryDoc.id

                    // Fetch all subcategories for each category
                    db.collection("categories")
                        .document(categoryId)
                        .collection("subcategories")
                        .get()
                        .addOnSuccessListener { subcategoriesSnapshot ->
                            // Loop through each subcategory
                            for (subcategoryDoc in subcategoriesSnapshot.documents) {
                                val subcategoryId = subcategoryDoc.id

                                // Fetch all products for each subcategory and add the task to the list
                                val productTask = db.collection("categories")
                                    .document(categoryId)
                                    .collection("subcategories")
                                    .document(subcategoryId)
                                    .collection("products")
                                    .get()

                                tasks.add(productTask)
                            }

                            // When all tasks are complete
                            Tasks.whenAllSuccess<QuerySnapshot>(tasks)
                                .addOnSuccessListener { allSnapshots ->
                                    // Collect products from all snapshots
                                    for (snapshot in allSnapshots) {
                                        val products = snapshot.toObjects(FinalProduct::class.java)
                                        allProducts.addAll(products)
                                    }

                                    // Shuffle the list and take 10 random products
                                    if (allProducts.size >= 10) {
                                        allProducts.shuffle()
                                        productList.value = allProducts.take(10)
                                    } else {
                                        productList.value =
                                            allProducts // Return all if fewer than 10
                                    }
                                }
                        }
                }
            }
            .addOnFailureListener {
                productList.value = emptyList() // Handle any failure
            }

        return productList
    }

    fun getProductById(productId: String): MutableLiveData<FinalProduct?> {
        val productLiveData =
            MutableLiveData<FinalProduct?>()
        val db = FirebaseFirestore.getInstance()

        // Start searching for the product in Firestore
        db.collection("categories").get().addOnSuccessListener { categoriesSnapshot ->
            for (categoryDoc in categoriesSnapshot.documents) {
                val subcategoriesRef = categoryDoc.reference.collection("subcategories")

                subcategoriesRef.get().addOnSuccessListener { subcategoriesSnapshot ->
                    for (subcategoryDoc in subcategoriesSnapshot.documents) {
                        val productsRef = subcategoryDoc.reference.collection("products")

                        productsRef.get().addOnSuccessListener { productsSnapshot ->
                            for (productDoc in productsSnapshot.documents) {
                                Log.d("product", productDoc.toString())
                                Log.d(
                                    "product",
                                    productDoc.toObject(FinalProduct::class.java).toString()
                                )
                                if (productDoc.id == productId) {
                                    // Map Firestore document data to FinalProduct
                                    val product =
                                        productDoc.toObject(FinalProduct::class.java)?.copy(
                                            id = productDoc.id, // Ensure product ID is mapped
                                            category = categoryDoc.id, // Set category from parent document
                                            subcategory = subcategoryDoc.id // Set subcategory from parent document
                                        )
                                    productLiveData.postValue(product)
                                    return@addOnSuccessListener // Exit the loop when the product is found
                                }
                            }
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            // Handle any errors here
            productLiveData.postValue(null)
        }

        return productLiveData
    }

    fun getProductByIds(
        categoryId: String,
        subcategoryId: String,
        productId: String
    ): MutableLiveData<FinalProduct?> {
        val productLiveData =
            MutableLiveData<FinalProduct?>()
        val db = FirebaseFirestore.getInstance()

        val productRef =
            db.document("categories/$categoryId/subcategories/$subcategoryId/products/$productId")

        // Fetch the product by its ID
        productRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Map Firestore document data to FinalProduct
                    val product = documentSnapshot.toObject(FinalProduct::class.java)?.copy(
                        id = productId, // Ensure the product ID is set
                        category = categoryId, // Set category from known value
                        subcategory = subcategoryId // Set subcategory from known value
                    )
                    // Post the product to LiveData
                    productLiveData.postValue(product)
                } else {
                    // Product does not exist
                    productLiveData.postValue(null)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
                exception.printStackTrace()
                productLiveData.postValue(null)
            }
        return productLiveData
    }

    fun getFavProducts(): LiveData<MutableList<FinalProduct>> {
        val productList = MutableLiveData<MutableList<FinalProduct>>()
        val favoriteProducts = mutableListOf<FinalProduct>()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val favoriteProductsRef = db.collection("users/${currentUser.uid}/favorites")

            favoriteProductsRef.get().addOnSuccessListener { querySnapshot ->
                val productRefs = mutableListOf<DocumentReference>()
                for (document in querySnapshot.documents) {
                    val productRef = document.getDocumentReference("productRef")
                    if (productRef != null) {
                        productRefs.add(productRef)
                    }
                }
                val productFetchTasks = productRefs.map { productRef ->
                    productRef.get().addOnSuccessListener { productSnapshot ->
                        if (productSnapshot.exists()) {
                            favoriteProducts.add(productSnapshot.toObject(FinalProduct::class.java)!!)
                        }
                        if (favoriteProducts.size == productRefs.size) {
                            productList.value = favoriteProducts
                        }
                    }
                }
            }
        }
        return productList
    }

    fun addProductToFavorites(
        categoryId: String,
        subcategoryId: String,
        productId: String
    ): LiveData<Boolean> {
        val productRef =
            db.document("categories/$categoryId/subcategories/$subcategoryId/products/$productId")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val isAdded = MutableLiveData<Boolean>()

        if (currentUser != null) {
            val favoritesCollection = db.collection("users/${currentUser.uid}/favorites")

            val query = favoritesCollection.whereEqualTo("productRef", productRef)
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        // Product not found in favorites, add it
                        val favoriteProduct = hashMapOf(
                            "productRef" to productRef
                        )

                        favoritesCollection.add(favoriteProduct)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Product added to favorites")
                                isAdded.value = true
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding product to favorites", e)
                            }
                    } else {
                        // Product already in favorites
                        Log.d("Firestore", "Product already in favorites")
                        isAdded.value = false
                    }
                } else {
                    // Error checking for existing product
                    Log.w("Firestore", "Error checking for existing product", task.exception)
                }
            }
        } else {
            // User not logged in
            Log.w("Firestore", "User not logged in, cannot add to favorites")
        }

        return isAdded
    }

    fun removeProductFromFavorites(
        categoryId: String,
        subcategoryId: String,
        productId: String
    ) {
        val productRef =
            db.document("categories/$categoryId/subcategories/$subcategoryId/products/$productId")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val favoritesCollection = db.collection("users/${currentUser.uid}/favorites")

            // Query to find the document with the matching productRef
            val query = favoritesCollection.whereEqualTo("productRef", productRef)

            query.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Delete the document
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Product removed from favorites")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error removing product from favorites", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching favorite product document", e)
            }
        } else {
            // User not logged in, handle accordingly
            Log.w("Firestore", "User not logged in, cannot remove from favorites")
        }
    }

    fun getBagProducts(): LiveData<MutableList<Map<FinalProduct,Int>>> {
        val productList = MutableLiveData<MutableList<Map<FinalProduct,Int>>>()
        val favoriteProducts = mutableListOf<Map<FinalProduct,Int>>()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val favoriteProductsRef = db.collection("users/${currentUser.uid}/bag")

            favoriteProductsRef.get().addOnSuccessListener { querySnapshot ->
                val productRefs = mutableMapOf<DocumentReference,Int>()
                for (document in querySnapshot.documents) {
                    val productRef = document.getDocumentReference("productRef")
                    val count = document.get("count") as Long
                    if (productRef != null) {
                        productRefs[productRef] = count.toInt()
                    }
                }
                val productFetchTasks = productRefs.map { productRef ->
                    productRef.key.get().addOnSuccessListener { productSnapshot ->
                        if (productSnapshot.exists()) {
                            favoriteProducts.add(mapOf(productSnapshot.toObject(FinalProduct::class.java)!! to productRef.value))
                        }
                        if (favoriteProducts.size == productRefs.size) {
                            productList.value = favoriteProducts
                        }
                    }
                }
            }
        }
        return productList
    }

    fun addProductToBag(
        categoryId: String,
        subcategoryId: String,
        productId: String
    ): LiveData<Boolean> {
        val productRef =
            db.document("categories/$categoryId/subcategories/$subcategoryId/products/$productId")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val isAdded = MutableLiveData<Boolean>()

        if (currentUser != null) {
            val bagCollection = db.collection("users/${currentUser.uid}/bag")

            val query = bagCollection.whereEqualTo("productRef", productRef)
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        // Product not found in bag, add it
                        val favoriteProduct = hashMapOf(
                            "productRef" to productRef,
                            "count" to 1
                        )

                        bagCollection.add(favoriteProduct)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Product added to bag")
                                isAdded.value = true
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding product to bag", e)
                            }
                    } else {
                        // Product already in bag
                        Log.d("Firestore", "Product already in bag")
                        isAdded.value = false
                    }
                } else {
                    // Error checking for existing product
                    Log.w("Firestore", "Error checking for existing product", task.exception)
                }
            }
        } else {
            // User not logged in
            Log.w("Firestore", "User not logged in, cannot add to bag")
        }

        return isAdded
    }

    fun removeProductFromBag(
        categoryId: String,
        subcategoryId: String,
        productId: String
    ) {
        val productRef =
            db.document("categories/$categoryId/subcategories/$subcategoryId/products/$productId")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val bagCollection = db.collection("users/${currentUser.uid}/bag")

            // Query to find the document with the matching productRef
            val query = bagCollection.whereEqualTo("productRef", productRef)

            query.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Delete the document
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Product removed from bag")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error removing product from bag", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching favorite product document", e)
            }
        } else {
            // User not logged in, handle accordingly
            Log.w("Firestore", "User not logged in, cannot remove from bag")
        }
    }


    private val storageRef = FirebaseStorage.getInstance().reference

}
