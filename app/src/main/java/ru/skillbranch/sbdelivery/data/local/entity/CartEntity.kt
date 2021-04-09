package ru.skillbranch.sbdelivery.data.local.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "promocode") val promoCode: String?,
    @ColumnInfo(name = "promotext") val promoText: String?,
    @ColumnInfo(name = "total") val total: Float?,
){
    constructor(promoCode: String?,
                promoText: String?,
                total: Float?) : this(0, promoCode, promoText, total)
}

@Entity(tableName = "cart_item_table", foreignKeys = [ForeignKey(
    entity = CartEntity::class,
    parentColumns = ["id"],
    childColumns = ["cartId"],
    onDelete = CASCADE
)])
data class CartItemEntity(
    @PrimaryKey @ColumnInfo(name = "id_cart_item") val id: String,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "price") val price: Float?,
    @ColumnInfo(name = "cartId") val cartId: String?
)

data class CartWithItems(

    @Embedded
    val cart: CartEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_cart_item"
    )
    val items: List<CartItemEntity>

)