package com.invisibleteam.goinvisible.model;


import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class TagGroup implements Parent<Tag> {

    private final TagGroupType type;
    private final List<Tag> tags;

    public TagGroup(TagGroupType type, List<Tag> tags) {
        this.type = type;
        this.tags = tags;
    }

    public TagGroupType getType() {
        return type;
    }

    @Override
    public List<Tag> getChildList() {
        return tags;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
