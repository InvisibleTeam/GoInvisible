package com.invisibleteam.goinvisible.model;


import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class TagGroup implements Parent<Tag> {

    private final TagGroupType type;
    private final List<Tag> tags;
    private final boolean initiallyExpanded;

    public TagGroup(TagGroupType type, List<Tag> tags, boolean initiallyExpanded) {
        this.type = type;
        this.tags = tags;
        this.initiallyExpanded = initiallyExpanded;
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
        return initiallyExpanded;
    }
}
