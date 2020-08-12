package my.jalal.consumerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowList {

    @SerializedName("results")
    private List<TvShow> tvShowList = null;

    public List<TvShow> getTvShowList() {
        return tvShowList;
    }

}
