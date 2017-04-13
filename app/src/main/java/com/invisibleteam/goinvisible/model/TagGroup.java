package com.invisibleteam.goinvisible.model;


import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class TagGroup implements Parent<Tag> {

    private List<Tag> tags;
    private String name;

    public TagGroup(String name, List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public List<Tag> getChildList() {
        return tags;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    public String getName() {
        return name;
    }
}
