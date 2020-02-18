
package model.models.AutoMotiveCategorySubcategory;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class AutoMotiveData {

    @SerializedName("Category")
    private String mCategory;
    @SerializedName("CategoryImage")
    private String mCategoryImage;
    @SerializedName("Subcategory")
    private List<Subcategory> mSubcategory;

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getCategoryImage() {
        return mCategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        mCategoryImage = categoryImage;
    }

    public List<Subcategory> getSubcategory() {
        return mSubcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        mSubcategory = subcategory;
    }

}
