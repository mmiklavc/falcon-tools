A = load '$input' using PigStorage(',');
B = foreach A generate $0 as id;
store B into '$output' USING PigStorage();
