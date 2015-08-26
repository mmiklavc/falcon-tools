#!/bin/bash
# curl -sS http://sandbox.hortonworks.com:15000/static/wiki-data.tar.gz | tar xz && hadoop fs -mkdir -p $1 && hadoop fs -put wiki-data/*.txt $1
curl -sS http://bailando.sims.berkeley.edu/enron/enron_with_categories.tar.gz | tar xz && hadoop fs -mkdir -p $1 && hadoop fs -put enron_with_categories/*/*.txt $1

