package com.michaelmiklavcic.falconer.entity;

import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;

public class FeedEntityBuilder extends EntityBuilder {

    public FeedEntityBuilder(String entity, String entityTemplate) {
        super(Feed.class, entity, entityTemplate);
    }

    @Override
    public Entity build() {
        Feed entity = unmarshall(getEntity());
        Feed defaults = unmarshall(getDefaultTemplate());
        if (entity.getACL() != null) {
            defaults.setACL(entity.getACL());
        }
        if (entity.getAvailabilityFlag() != null) {
            defaults.setAvailabilityFlag(entity.getAvailabilityFlag());
        }
        if (entity.getClusters() != null) {
            defaults.setClusters(entity.getClusters());
        }
        if (entity.getDescription() != null) {
            defaults.setDescription(entity.getDescription());
        }
        if (entity.getEntityType() != null) {
            defaults.setDescription(entity.getDescription());
        }
        return defaults;
    }

}
