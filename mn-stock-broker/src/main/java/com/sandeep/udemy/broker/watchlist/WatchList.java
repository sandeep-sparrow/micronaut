package com.sandeep.udemy.broker.watchlist;

import com.sandeep.udemy.broker.Symbol;

import java.util.ArrayList;
import java.util.List;

public record WatchList(List<Symbol> symbols) {

    public WatchList(){
        this(new ArrayList<>());
    }
}
