package com.model.bitbucket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BitBucketRepositories {

    @SerializedName("pagelen")
    @Expose
    private Integer pagelen;
    @SerializedName("values")
    @Expose
    private ArrayList<BitBucketRepo> values = new ArrayList<>();
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("size")
    @Expose
    private Integer size;

    /**
     *
     * @return
     * The pagelen
     */
    public Integer getPagelen() {
        return pagelen;
    }

    /**
     *
     * @return
     * The values
     */
    public ArrayList<BitBucketRepo> getValues() {
        return values;
    }

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @return
     * The size
     */
    public Integer getSize() {
        return size;
    }


}
