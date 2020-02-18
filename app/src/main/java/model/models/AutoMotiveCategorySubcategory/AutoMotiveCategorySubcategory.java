
package model.models.AutoMotiveCategorySubcategory;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class AutoMotiveCategorySubcategory {

    @SerializedName("data")
    private List<AutoMotiveData> mData;
    @SerializedName("result")
    private String mResult;

    public List<AutoMotiveData> getData() {
        return mData;
    }

    public void setData(List<AutoMotiveData> data) {
        mData = data;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

}
