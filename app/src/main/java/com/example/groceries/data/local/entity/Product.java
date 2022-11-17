package com.example.groceries.data.local.entity;

import static androidx.room.ForeignKey.SET_DEFAULT;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "product",foreignKeys = {
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = SET_DEFAULT
        )},
        indices = {
                @Index("categoryId"),
        })

public class Product implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(defaultValue = "1")
    private long categoryId;
    private String name;
    private float unitPrice;
    private int quantity;

    public void setId(int id) {
        this.id = id;
    }



    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    protected Product(Parcel in) {
        id = in.readLong();
        categoryId = in.readLong();
        name = in.readString();
        unitPrice = in.readFloat();
        quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product(long id, long categoryId, String name, float unitPrice, int quantity) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    @Ignore
    public Product(){
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(categoryId);
        parcel.writeString(name);
        parcel.writeFloat(unitPrice);
        parcel.writeInt(quantity);
    }



    public boolean isValid(){
        boolean valid = true;

        if(categoryId == 0)
            valid = false;
        if(name == null || name.isEmpty())
            valid = false;
        if(unitPrice == 0)
            valid = false;
        if(quantity == 0)
            valid = false;
        return valid;
    }
}
