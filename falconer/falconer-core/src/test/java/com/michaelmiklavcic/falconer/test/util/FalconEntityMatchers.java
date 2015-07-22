package com.michaelmiklavcic.falconer.test.util;

import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;
import org.hamcrest.TypeSafeMatcher;

public class FalconEntityMatchers {

    public static TypeSafeMatcher<Feed> equalTo(Feed operand) {
        return new FeedMatcher(operand);
    }

    public static TypeSafeMatcher<Process> equalTo(Process operand) {
        return new ProcessMatcher(operand);
    }
}
