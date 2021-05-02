package ru.skillbranch.sbdelivery.data.local.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "promocode") val promoCode: String? = null,
    @ColumnInfo(name = "promotext") val promoText: String? = null,
    @ColumnInfo(name = "total") val total: Float?,
){
    constructor(promoCode: String? = null,
                promoText: String? = null,
                total: Float? = null) : this(0, promoCode, promoText, total)
}

@Entity(tableName = "cart_item_table", foreignKeys = [ForeignKey(
    entity = CartEntity::class,
    parentColumns = ["id"],
    childColumns = ["cartId"],
    onDelete = CASCADE
)])
data class CartItemEntity(
    @PrimaryKey @ColumnInfo(name = "dish_id") val dishId: String,
    @ColumnInfo(name = "remote_id_cart") val remoteId: String? = null,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "price") val price: Float?,
    @ColumnInfo(name = "cartId", index = true) val cartId: Long
)

data class CartWithItems(

    @Embedded
    val cart: CartEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "cartId"
    )
    val items: List<CartWithImage>

)

@DatabaseView("SELECT cart_item_table.amount, cart_item_table.price, cart_item_table.cartId, " +
        "cart_item_table.dish_id, dishes_table.image, dishes_table.name FROM cart_item_table " +
        "LEFT JOIN dishes_table ON cart_item_table.dish_id=dishes_table.id")
data class CartWithImage(
    @ColumnInfo(name = "dish_id") val dishId: String,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "price") val price: Float?,
    @ColumnInfo(name = "cartId") val cartId: Long,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "name") val name: String
)