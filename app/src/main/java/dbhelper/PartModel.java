package dbhelper;

import java.io.Serializable;

public class PartModel implements Serializable {

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getOemPartNo() {
        return oemPartNo;
    }

    public void setOemPartNo(String oemPartNo) {
        this.oemPartNo = oemPartNo;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPartimage() {
        return Partimage;
    }

    public void setPartimage(String partimage) {
        Partimage = partimage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    String partNo;
    String oemPartNo;
    String Quantity;
    String Partimage;
    String productName;
    String model;
    String description;
    String S_Id;
    String Proca_Id;
    String V_Id;
    String listprice;
    String category;
    String subcategory;
    String segement;

    public String getSegement() {
        return this.segement;
    }

    public void setSegement(final String segement) {
        this.segement = segement;
    }

    public String getTotalamt() {
        return this.totalamt;
    }

    public void setTotalamt(final String totalamt) {
        this.totalamt = totalamt;
    }

    public String getMrp() {
        return this.mrp;
    }

    public void setMrp(final String mrp) {
        this.mrp = mrp;
    }

    String mrp;
    String totalamt;

    public String getModel() {
        return this.model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getS_Id() {
        return this.S_Id;
    }

    public void setS_Id(final String s_Id) {
        this.S_Id = s_Id;
    }

    public String getProca_Id() {
        return this.Proca_Id;
    }

    public void setProca_Id(final String proca_Id) {
        this.Proca_Id = proca_Id;
    }

    public String getV_Id() {
        return this.V_Id;
    }

    public void setV_Id(final String v_Id) {
        this.V_Id = v_Id;
    }

    public String getListprice() {
        return this.listprice;
    }

    public void setListprice(final String listprice) {
        this.listprice = listprice;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return this.subcategory;
    }

    public void setSubcategory(final String subcategory) {
        this.subcategory = subcategory;
    }
}
