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
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_cart_item") var id: Long = 0,
    @ColumnInfo(name = "remote_id_cart") val remoteId: String? = null,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "price") val price: Float?,
    @ColumnInfo(name = "cartId") val cartId: Long
)

data class CartWithItems(

    @Embedded
    val cart: CartEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "cartId"
    )
    val items: List<CartItemEntity>

)