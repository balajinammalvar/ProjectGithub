
package model.models.AutoMotiveCategorySubcategory;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Subcategory {

    @SerializedName("Subcategory")
    private String mSubcategory;
    @SerializedName("Subcategoryimage")
    private String mSubcategoryimage;

    public String getSubcategory() {
        return mSubcategory;
    }

    public void setSubcategory(String subcategory) {
        mSubcategory = subcategory;
    }

    public String getSubcategoryimage() {
        return mSubcategoryimage;
    }

    public void setSubcategoryimage(String subcategoryimage) {
        mSubcategoryimage = subcategoryimage;
    }

}
