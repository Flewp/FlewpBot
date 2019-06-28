package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.helix.HelixFollow;
import com.flewp.flewpbot.model.helix.HelixPagination;

import java.util.List;

public class GetFollowsResponse {
    public Integer total;
    public List<HelixFollow> data;
    public HelixPagination pagination;
}
