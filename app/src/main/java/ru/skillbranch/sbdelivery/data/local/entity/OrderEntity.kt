package ru.skillbranch.sbdelivery.data.local.entity

import androidx.room.*

@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "total") val total: Float?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "statusId") val statusId: String?,
    @ColumnInfo(name = "active") val active: Boolean?,
    @ColumnInfo(name = "completed") val completed: Boolean?,
    @ColumnInfo(name = "createdAt") val createdAt: String?,
    @ColumnInfo(name = "updatedAt") val updatedAt: String?,
)

@Entity(tableName = "order_item_table", foreignKeys = [ForeignKey(
    entity = OrderEntity::class,
    parentColumns = ["id"],
    childColumns = ["orderId"],
    onDelete = ForeignKey.CASCADE
)])
data class OrderItemEntity(
    @PrimaryKey @ColumnInfo(name = "id_order_item") val id: String = "",
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "price") val price: Float?,
    @ColumnInfo(name = "dishId") val dishId: String?,
    @ColumnInfo(name = "orderId") val orderId: String
)

data class OrderWithItems(

    @Embedded
    val order: OrderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_order_item"
    )
    val items: List<OrderItemEntity>,

    @Relation(
        parentColumn = "statusId",
        entityColumn = "id"
    )
    val status: OrderStatusEntity

)
