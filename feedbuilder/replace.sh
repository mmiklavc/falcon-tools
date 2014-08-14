#!/bin/bash

#####
# load default values, override with feed1,2,3 methods and write out feed definitions using sed on the feedtemplate.xml
#####

default_load() {
    export DESCRIPTION="Raw customer email feed"
    export FEEDNAME="rawEmailFeed"
    export TAGS="externalSystem=USWestEmailServers,classification=secure"
    export PIPELINE_GROUPS="churnAnalysisDataPipeline"
    export FREQUENCY="hours(1)"
    export LATE_CUTOFF="hours(4)"
    export CLUSTER1="primaryCluster"
    export CLUSTER1_VALIDITIY_START="2014-02-28T00:00Z"
    export CLUSTER1_VALIDITIY_END="2016-03-31T00:00Z"
    export CLUSTER1_RETENTION_LIMIT="days(30)"
    export DATA_PATH='\/user\/ambari-qa\/falcon\/demo\/primary\/input\/enron\/${YEAR}-${MONTH}-${DAY}-${HOUR}'
    export ACL_OWNER="ambari-qa"
    export ACL_GROUP="users"
}

feed1() {
    export DESCRIPTION="Raw customer email feed-1"
    export FEEDNAME="rawEmailFeed-1"
    export DATA_PATH='\/user\/ambari-qa\/falcon\/demo\/primary\/input\/enron-1\/${YEAR}-${MONTH}-${DAY}-${HOUR}'
}

feed2() {
    export DESCRIPTION="Raw customer email feed-2"
    export FEEDNAME="rawEmailFeed-2"
    export DATA_PATH='\/user\/ambari-qa\/falcon\/demo\/primary\/input\/enron-2\/${YEAR}-${MONTH}-${DAY}-${HOUR}'
}

feed3() {
    export DESCRIPTION="Raw customer email feed-3"
    export FEEDNAME="rawEmailFeed-3"
    export DATA_PATH='\/user\/ambari-qa\/falcon\/demo\/primary\/input\/enron-3\/${YEAR}-${MONTH}-${DAY}-${HOUR}'
}

var_list=(
    {DESCRIPTION}
    {FEEDNAME}
    {TAGS}
    {PIPELINE_GROUPS}
    {FREQUENCY}
    {LATE_CUTOFF}
    {CLUSTER1}
    {CLUSTER1_VALIDITIY_START}
    {CLUSTER1_VALIDITIY_END}
    {CLUSTER1_RETENTION_LIMIT}
    {DATA_PATH}
    {ACL_OWNER}
    {ACL_GROUP})

feed_list=(
    feed1
    feed2
    feed3)

create_feeds() {
    for feed in ${feed_list[@]}; do
        eval ${feed}
        replacements=""

        for item in ${var_list[@]}; do
            export envvar=$(echo $item | sed 's/{*}*//g')
            val=$(eval echo \$"$envvar")
            echo "Replacing $item with $val"
            replacements+="-e 's/$item/$val/g' "
        done

        echo "Replacements: $replacements"
        echo "Writing file ${feed}.xml"
        cat feedtemplate.xml | eval sed "$replacements" > ${feed}.xml

    done
}

# load defaults values
default_load

create_feeds

